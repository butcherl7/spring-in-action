package top.funsite.spring.action.shiro.credential;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.AbstractCryptHash;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.crypto.hash.format.DefaultHashFormatFactory;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.HashFormatFactory;
import org.apache.shiro.crypto.hash.format.ProvidedHashFormat;

import java.util.Map;

/**
 * Hash Password Service.
 *
 * @see AbstractCryptHash
 * @see NoIdentifierFormat
 * @see ProvidedHashFormat
 */
public final class StrongerHashPasswordService extends DefaultPasswordService {

    private static final NoIdentifierFormat NO_IDENTIFIER_FORMAT = new NoIdentifierFormat();

    private static final DefaultHashFormatFactory HASH_FORMAT_FACTORY = new DefaultHashFormatFactory();

    static {
        Map<String, String> formatClassNames = HASH_FORMAT_FACTORY.getFormatClassNames();
        String formatName = NoIdentifierFormat.class.getName();

        for (Algorithm algorithm : Algorithm.values()) {
            formatClassNames.put(algorithm.getAlgorithmName(), formatName);
        }
    }

    public StrongerHashPasswordService(Algorithm algorithm) {
        super();
        DefaultHashService hashService = new DefaultHashService();
        hashService.setDefaultAlgorithmName(algorithm.getAlgorithmName());

        super.setHashService(hashService);
        super.setHashFormat(NO_IDENTIFIER_FORMAT);
        super.setHashFormatFactory(HASH_FORMAT_FACTORY);
    }

    @Override
    public void setHashService(HashService hashService) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHashFormat(HashFormat hashFormat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHashFormatFactory(HashFormatFactory hashFormatFactory) {
        throw new UnsupportedOperationException();
    }
}
