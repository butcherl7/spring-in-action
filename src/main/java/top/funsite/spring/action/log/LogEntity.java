package top.funsite.spring.action.log;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class LogEntity {

    private Long id;

    private String name;

    private String methodName;

    private String requestURI;

    private String httpMethod;

    private String token;

    private String headers;

    private String queryString;

    private String requestBody;

    private String responseBody;

    // private Integer httpStatusCode;

    private String errorMessage;

    private String operator;

    private LocalDateTime requestTime;

    private LocalDateTime responseTime;

}
