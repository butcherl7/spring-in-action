package top.funsite.spring.action.shiro.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class JwtFilter extends PassThruFilter {

    protected Map<String, DecodedJWTValidator> decodedJWTValidationPaths = new LinkedHashMap<>();

    public JwtFilter() {
    }

    public JwtFilter(Map<String, DecodedJWTValidator> decodedJWTValidationPaths) {
        this.decodedJWTValidationPaths = decodedJWTValidationPaths;
    }

    @Override
    protected boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            // demo.
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("123456"))
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);

            DecodedJWTValidator validator = getValidator(request);

            if (validator != null) {
                return validator.validate(request, response, decodedJWT);
            }
        } catch (JWTVerificationException e) {
            log.error(e.getMessage(), e);
            return super.responseDenied(request, response, HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        return true;
    }

    @Nullable
    protected DecodedJWTValidator getValidator(ServletRequest request) {
        if (MapUtils.isEmpty(this.decodedJWTValidationPaths)) {
            return null;
        }

        for (String path : this.decodedJWTValidationPaths.keySet()) {
            if (pathsMatch(path, request)) {
                return this.decodedJWTValidationPaths.get(path);
            }
        }
        return null;
    }
}
