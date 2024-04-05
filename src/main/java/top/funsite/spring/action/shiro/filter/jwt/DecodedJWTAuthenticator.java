package top.funsite.spring.action.shiro.filter.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 对已经正确解码过的 JWT 进行进一步的验证。
 *
 * @see JwtFilter
 */
@FunctionalInterface
public interface DecodedJWTAuthenticator {

    /**
     * 验证解码过的 JWT 令牌。
     *
     * @param request    the incoming HttpServletRequest
     * @param response   the outgoing HttpServletResponse
     * @param decodedJWT 解码的 JSON Web Token.
     * @return 如果请求应继续处理，则为 true，如果子类将直接处理/呈现响应，则为 false。
     */
    boolean validate(HttpServletRequest request, HttpServletResponse response, DecodedJWT decodedJWT);
}
