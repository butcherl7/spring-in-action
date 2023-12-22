package top.funsite.spring.action.shiro.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class JwtFilter extends PassThruFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String token = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);
        try {
            // demo.
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("123456"))
                    .build();
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            log.error(e.getMessage(), e);
            return super.onAccessDenied(request, response);
        }
        return true;
    }
}
