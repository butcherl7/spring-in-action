package top.funsite.spring.action.shiro.credential;

import org.apache.shiro.crypto.hash.AbstractCryptHash;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashProvider;
import org.apache.shiro.crypto.hash.HashSpi;
import org.apache.shiro.crypto.hash.format.ModularCryptFormat;
import org.apache.shiro.crypto.hash.format.ParsableHashFormat;
import org.apache.shiro.crypto.hash.format.Shiro2CryptFormat;

import static java.util.Objects.requireNonNull;

/**
 * 没有额外标识符的格式化程序。格式化 Argon2 和 BCrypt.
 *
 * @see Shiro2CryptFormat
 */
public class NoIdentifierFormat implements ModularCryptFormat, ParsableHashFormat {

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String format(Hash hash) {
        requireNonNull(hash, "hash in NoIdentifierFormat.format(Hash hash)");

        if (hash instanceof AbstractCryptHash cryptHash) {
            return cryptHash.formatToCryptString();
        }
        throw new UnsupportedOperationException("NoIdentifierFormat can only format classes extending AbstractCryptHash.");
    }

    @Override
    public Hash parse(String formatted) {
        requireNonNull(formatted, "formatted in NoIdentifierFormat.parse(String formatted)");

        final String suffix = formatted.substring(TOKEN_DELIMITER.length());
        final String[] parts = suffix.split("\\$");
        final String algorithmName = parts[0];

        HashSpi kdfHash = HashProvider.getByAlgorithmName(algorithmName)
                .orElseThrow(() -> new UnsupportedOperationException("Algorithm " + algorithmName + " is not implemented."));
        return kdfHash.fromString(TOKEN_DELIMITER + suffix);
    }
}
