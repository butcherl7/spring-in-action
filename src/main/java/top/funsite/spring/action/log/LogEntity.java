package top.funsite.spring.action.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogEntity {

    /**
     * ID.
     */
    private Long id;

    /**
     * 接口在日志记录的名称。
     */
    private String name;

    /**
     * 接口对应的方法全名。
     */
    private String methodName;

    /**
     * 发起请求的 IP 地址。
     */
    private String requestIp;

    /**
     * 请求地址。
     *
     * @see HttpServletRequest#getRequestURI()
     */
    private String requestUri;

    /**
     * 发出此请求的 HTTP 方法的名称。
     */
    private String httpMethod;

    /**
     * Token（如果有的话）
     */
    private String token;

    /**
     * 记录的请求头信息。
     */
    private String headers;

    /**
     * 接口请求参数。
     */
    private String requestParameter;

    /**
     * 接口响应结果。
     */
    private String responseResult;

    /**
     * 接口调用发送异常的异常信息。
     */
    private String errorMessage;

    /**
     * 请求的发起人。
     */
    private String operator;

    /**
     * 接口开始执行的时间戳。
     */
    private Long requestTimestamp;

    /**
     * 接口结束执行的时间戳。
     */
    private Long responseTimestamp;

}
