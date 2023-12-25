package top.funsite.spring.action.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Aspect
@Component
public class LogAspect {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("@annotation(RequestLog)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        LogEntity logEntity = null;

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            RequestLog requestLog = method.getAnnotation(RequestLog.class);

            if (requestLog == null) {
                return joinPoint.proceed();
            }

            String methodName = method.getDeclaringClass().getName() + "." + method.getName();

            logEntity = new LogEntity();
            logEntity.setName(requestLog.name());
            logEntity.setMethodName(methodName);
            logEntity.setRequestTime(LocalDateTime.now());

            Object[] args = joinPoint.getArgs();


            log.info("method: {}", methodName);

            HttpServletRequest request = getHttpServletRequest();

            if (request != null) {
                String requestURI = request.getRequestURI();
                String httpMethod = request.getMethod();

                logEntity.setRequestURI(requestURI);
                logEntity.setHttpMethod(httpMethod);

                if (requestLog.logQueryString()) {
                    logEntity.setQueryString(request.getQueryString());
                }

                String[] logHeaders = requestLog.headers();

                if (logHeaders != null) {
                    Map<String, Object> map = new HashMap<>(16);
                    for (String name : logHeaders) {
                        List<String> headers = new ArrayList<>();
                        Enumeration<String> enumeration = request.getHeaders(name);
                        while (enumeration.hasMoreElements()) {
                            headers.add(enumeration.nextElement());
                        }
                        if (headers.isEmpty()) {
                            map.put(name, null);
                        } else {
                            if (headers.size() == 1) {
                                map.put(name, headers.getFirst());
                            } else {
                                map.put(name, headers);
                            }
                        }
                    }
                    if (!map.isEmpty()) {
                        logEntity.setHeaders(objectMapper.writeValueAsString(map));
                    }
                }
            }

            Object proceed = joinPoint.proceed();

            if (requestLog.logResponse()) {
                logEntity.setResponseBody(objectMapper.writeValueAsString(proceed));
            }

            return proceed;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (logEntity != null) {
                logEntity.setResponseTime(LocalDateTime.now());
                try {
                    System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(logEntity));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getRequestIp(HttpServletRequest request) {
        final String unknown = "UNKNOWN";
        final String ipv4Localhost = "127.0.0.1";
        final String ipv6Localhost = "0:0:0:0:0:0:0:1";

        String ip = request.getHeader("X-Real-IP");
        String forwarded = request.getHeader("X-Forwarded-For");

        if (isNotBlank(forwarded) && !unknown.equalsIgnoreCase(forwarded)) {
            // 多次反向代理后会有多个 IP 值，第一个 IP 才是真实 IP
            int index = forwarded.indexOf(",");
            if (index != -1) {
                return forwarded.substring(0, index);
            } else {
                return forwarded;
            }
        }

        forwarded = ip;

        if (isNotBlank(forwarded) && !unknown.equalsIgnoreCase(forwarded)) {
            return forwarded;
        }
        if (!isNotBlank(forwarded) || unknown.equalsIgnoreCase(forwarded)) {
            forwarded = request.getHeader("Proxy-Client-IP");
        }
        if (!isNotBlank(forwarded) || unknown.equalsIgnoreCase(forwarded)) {
            forwarded = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!isNotBlank(forwarded) || unknown.equalsIgnoreCase(forwarded)) {
            forwarded = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!isNotBlank(forwarded) || unknown.equalsIgnoreCase(forwarded)) {
            forwarded = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!isNotBlank(forwarded) || unknown.equalsIgnoreCase(forwarded)) {
            forwarded = request.getRemoteAddr();
        }
        if (ipv6Localhost.equals(forwarded)) {
            forwarded = ipv4Localhost;
        }
        return forwarded;
    }

    private static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes instanceof ServletRequestAttributes
                ? ((ServletRequestAttributes) requestAttributes).getRequest()
                : null;
    }


}
