package top.funsite.spring.action.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Aspect
@Component
public class LogAspect {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /*private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // LocalDate LocalTime 和 LocalDateTime 的格式。
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));

        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DATE_TIME_PATTERN));
    }*/


    @Pointcut("@annotation(top.funsite.spring.action.log.RequestLog)")
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
            logEntity.setRequestTimestamp(System.currentTimeMillis());

            // log request parameter
            if (requestLog.logRequest()) {
                Map<String, Object> parameterMap = new HashMap<>(16);

                Parameter[] parameters = method.getParameters();

                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    Object arg = joinPoint.getArgs()[i];
                    if (arg instanceof ServletRequest || arg instanceof ServletResponse || arg instanceof MultipartFile) {
                        continue;
                    }
                    parameterMap.put(parameters[i].getName(), arg);
                }
                if (!parameterMap.isEmpty()) {
                    if (parameterMap.size() == 1) {
                        Collection<Object> values = parameterMap.values();
                        for (Object value : values) {
                            if (value != null) {
                                logEntity.setRequestParameter(toJson(value));
                            }
                        }
                    } else {
                        logEntity.setRequestParameter(toJson(parameterMap));
                    }
                }
            }

            HttpServletRequest request = getHttpServletRequest();
            if (request != null) {
                logEntity.setHttpMethod(request.getMethod());
                logEntity.setRequestIp(getRequestIp(request));
                logEntity.setRequestUri(request.getRequestURI());

                // log headers
                if (requestLog.headers() != null) {
                    Map<String, Object> map = new HashMap<>(16);
                    for (String name : requestLog.headers()) {
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
                        logEntity.setHeaders(toJson(map));
                    }
                }
            }

            Object proceed = joinPoint.proceed();

            // log response result
            if (requestLog.logResponse()) {
                logEntity.setResponseResult(toJson(proceed));
            }

            return proceed;
        } catch (Throwable e) {
            if (logEntity != null) {
                logEntity.setErrorMessage(e.getMessage());
            }
            throw new RuntimeException(e);
        } finally {
            if (logEntity != null) {
                logEntity.setResponseTimestamp(System.currentTimeMillis());
                try {
                    System.out.println(OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(logEntity));
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private static String toJson(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    private static String getRequestIp(HttpServletRequest request) {
        final String unknown = "UNKNOWN", ipv4Localhost = "127.0.0.1", ipv6Localhost = "0:0:0:0:0:0:0:1";

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
