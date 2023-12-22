package top.funsite.spring.action.shiro;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Http status 非 200 下的响应信息。
 */
@Getter
@Setter
public class HttpErrorEntity {

    private Long timestamp;

    private Long status;

    private String error;

    private String message;

    private String path;

    public static HttpErrorEntity create(HttpStatus status, String message, String path) {
        HttpErrorEntity entity = new HttpErrorEntity();
        entity.setTimestamp(System.currentTimeMillis());
        entity.setStatus((long) status.value());
        entity.setError(status.getReasonPhrase());
        entity.setMessage(message);
        entity.setPath(path);
        return entity;
    }
}
