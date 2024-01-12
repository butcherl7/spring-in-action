package top.funsite.spring.action.domin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 响应状态码<strong>不是成功响应</strong> (200–299) 下的响应信息。例如：
 * <li>401 未通过身份认证</li>
 * <li>403 未经授权</li>
 * <li>500 服务器遇到未知错误</li>
 * <li>……</li>
 *
 * @see <a href="https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status">HTTP 响应状态码 - HTTP | MDN</a>
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
