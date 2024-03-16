package top.funsite.spring.action.shiro.credential;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.AbstractCryptHash;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.format.DefaultHashFormatFactory;
import org.apache.shiro.crypto.hash.format.ProvidedHashFormat;

import java.util.Map;
import java.util.Set;

/**
 * Hash Password Service，可使用的算法有：
 * <ul>
 *     <li>Argon2: argon2id, argon2i, argon2d</li>
 *     <li>BCrypt: 2, 2a, 2b, 2y</li>
 * </ul>
 * 默认使用 Bcrypt 2b。
 *
 * @see AbstractCryptHash
 * @see NoIdentifierFormat
 * @see ProvidedHashFormat
 */
public class StrongerHashPasswordService extends DefaultPasswordService {

    private static final Set<String> ALGORITHMS_ARGON2 = Set.of("argon2id", "argon2i", "argon2d");

    private static final Set<String> ALGORITHMS_BCRYPT = Set.of("2", "2a", "2b", "2y");

    public static final String DEFAULT_HASH_ALGORITHM = "2b";

    private static final NoIdentifierFormat NO_IDENTIFIER_FORMAT = new NoIdentifierFormat();

    private static final DefaultHashFormatFactory HASH_FORMAT_FACTORY = new DefaultHashFormatFactory();

    static {
        Map<String, String> formatClassNames = HASH_FORMAT_FACTORY.getFormatClassNames();
        String formatName = NoIdentifierFormat.class.getName();
        ALGORITHMS_ARGON2.forEach(v -> formatClassNames.put(v, formatName));
        ALGORITHMS_BCRYPT.forEach(v -> formatClassNames.put(v, formatName));
    }

    public StrongerHashPasswordService() {
        super();
        DefaultHashService hashService = new DefaultHashService();
        hashService.setDefaultAlgorithmName(DEFAULT_HASH_ALGORITHM);

        super.setHashService(hashService);
        super.setHashFormatFactory(HASH_FORMAT_FACTORY);
        super.setHashFormat(NO_IDENTIFIER_FORMAT);
    }
}
