package top.funsite.spring.action.shiro.filter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import top.funsite.spring.action.domain.Result;
import top.funsite.spring.action.shiro.filter.PassThruFilter;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class JwtFilter extends PassThruFilter {

    protected Map<String, DecodedJWTAuthenticator> authenticatorPaths = new LinkedHashMap<>();

    public JwtFilter() {
    }

    public JwtFilter(Map<String, DecodedJWTAuthenticator> authenticatorPaths) {
        this.authenticatorPaths = authenticatorPaths;
    }

    @Override
    protected boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            // demo.
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("123456"))
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);

            DecodedJWTAuthenticator validator = getValidator(request);

            if (validator != null) {
                return validator.validate(request, response, decodedJWT);
            }
        } catch (JWTVerificationException e) {
            log.error(e.getMessage(), e);
            return super.responseDenied(response, HttpServletResponse.SC_UNAUTHORIZED, Result.fail(e.getMessage()));
        }
        return true;
    }

    @Nullable
    protected DecodedJWTAuthenticator getValidator(ServletRequest request) {
        if (CollectionUtils.isEmpty(this.authenticatorPaths)) {
            return null;
        }

        for (String path : this.authenticatorPaths.keySet()) {
            if (pathsMatch(path, request)) {
                return this.authenticatorPaths.get(path);
            }
        }
        return null;
    }
}
