package top.funsite.spring.action;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class SecretTest {

    /**
     * <ul>
     *     <li>iss (issuer)：签发人</li>
     *     <li>sub (subject)：主题</li>
     *     <li>aud (audience)：受众</li>
     *     <li>iat (Issued At)：签发时间</li>
     *     <li>jti (JWT ID)：提供唯一的标识符</li>
     *     <li>kid (Key Id)：所有权证明密钥的密钥 ID 表示</li>
     *     <li>exp (expiration time)：过期时间</li>
     *     <li>nbf (Not Before)：生效时间，在此之前是无效的</li>
     * </ul>
     */
    @Test
    @DisplayName("测试生成 JWT")
    public void testCreate() {
        LocalDateTime now = LocalDateTime.now();
        String sign = JWT.create()
                .withIssuer("Umbrella")
                .withSubject("Test")
                .withAudience("a", "b", "c")
                .withIssuedAt(new Date())
                .withIssuedAt(new Date())
                .withJWTId(UUID.randomUUID().toString().replaceAll("-", ""))
                .withKeyId("Hello")
                // .withNotBefore(now.plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant())
                .withExpiresAt(now.plusHours(1).atZone(ZoneId.systemDefault()).toInstant())
                .sign(Algorithm.HMAC256("123456"));
        System.out.println("\n" + sign + "\n");
    }

    @Test
    @DisplayName("测试验证 JWT")
    public void testVerify() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJVbWJyZWxsYSIsInN1YiI6IlRlc3QiLCJhdWQiOlsiYSIsImIiLCJjIl0sImlhdCI6MTcwNDI3NzUwMiwiZXhwIjoxNzA0MjgxMTAyfQ.i_3NjRVnvPm5N_hd5PF1RygQuEYaTkT8kuvMiiyypl4";

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("123456"))
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String id = decodedJWT.getId();
            String keyId = decodedJWT.getKeyId();
            System.out.println(id);
            System.out.println(keyId);
        } catch (JWTVerificationException e) {
            if (e instanceof TokenExpiredException) {
                TokenExpiredException ex = (TokenExpiredException) e;
                ZonedDateTime zonedDateTime = ex.getExpiredOn().atZone(ZoneId.systemDefault());
                System.err.println("The Token has expired on " + zonedDateTime.format(formatter));
            } else if (e instanceof IncorrectClaimException) {
                IncorrectClaimException ex = (IncorrectClaimException) e;
                String claimName = ex.getClaimName();
                Claim claimValue = ex.getClaimValue();
                // RegisteredClaims.NOT_BEFORE
                System.err.printf("%s %s %s\n", claimName, claimValue, ex.getMessage());
            } else {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void bcryptHash() {
        String password = "123456";
        String hashed = BCrypt.withDefaults().hashToString(6, password.toCharArray());
        System.out.println(hashed);
    }

    @Test
    public void bcryptVerify() {
        String password = "123456";
        String hash = "$2a$18$BBLRD2SI2l9HCe4aH22qhuRhQy4bIac7hoZ8qf15gGfjcn6rxBxl2";

        BCrypt.Result verify = BCrypt.verifyer().verify(password.toCharArray(), hash.toCharArray());
        System.out.println(verify.verified);
    }

}
