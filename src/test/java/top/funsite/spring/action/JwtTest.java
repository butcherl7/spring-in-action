package top.funsite.spring.action;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class JwtTest {

    @Test
    public void testCreate() {
        String sign = JWT.create()
                // .withHeader("{\"what\": \"if\"}")
                .withClaim("name", "Tom")
                .withClaim("age", 18)
                .withExpiresAt(LocalDateTime.now().plusSeconds(1).atZone(ZoneId.systemDefault()).toInstant())
                .sign(Algorithm.HMAC256("123456"));
        System.out.println("\n" + sign);
    }

    @Test
    public void testVerify() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiVG9tIiwiYWdlIjoxOSwiZXhwIjoxNzAzMjE4ODQzfQ.IhaxbooB_ZAbH_ZGgqiGpi-PZkyDvL7FJUfWT_Ro3HQ1";

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("123456"))
                    .build();
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            if (e instanceof TokenExpiredException) {
                ZonedDateTime zonedDateTime = ((TokenExpiredException) e).getExpiredOn().atZone(ZoneId.systemDefault());
                System.out.println("The Token has expired on " + zonedDateTime.format(formatter));
            } else {
                e.printStackTrace();
            }
        }
    }

}
