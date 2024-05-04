package top.funsite.spring.action.shiro.filter.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 解码后的 JSON Web Token 验证程序。
 *
 * @see JwtFilter
 */
@FunctionalInterface
public interface DecodedJWTValidator {

    /**
     * 对解码过的 JSON Web Token 进行验证。
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param decodedJWT 解码后的的 Token.
     * @return 如果请求应继续处理，则为 true，如果子类直接处理（直接响应错误），则为 false。
     */
    boolean validate(HttpServletRequest request, HttpServletResponse response, DecodedJWT decodedJWT);
}
