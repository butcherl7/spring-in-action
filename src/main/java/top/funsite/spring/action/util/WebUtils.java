package top.funsite.spring.action.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author kinglyq
 */
public class WebUtils {

    private WebUtils() {
    }

    public static String getRequestIp(HttpServletRequest request) {
        Objects.requireNonNull(request, "HttpServletRequest 不能为空");

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
}
