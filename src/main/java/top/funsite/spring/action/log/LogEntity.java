package top.funsite.spring.action.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogEntity {

    private Long id;

    private String name;

    private String methodName;

    private String requestIp;

    private String requestUri;

    private String httpMethod;

    private String token;

    private String headers;

    private String requestParameter;

    private String responseResult;

    private String errorMessage;

    private String operator;

    private Long requestTimestamp;

    private Long responseTimestamp;

}
