package top.funsite.spring.action.crypto;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashProvider;
import org.apache.shiro.crypto.hash.HashSpi;
import org.apache.shiro.lang.util.SimpleByteSource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.StreamSupport;

public class ShiroTest {

    @Test
    public void testBCryptHash() {
        List<String> hashAlgorithms = StreamSupport.stream(ServiceLoader.load(HashSpi.class).spliterator(), false)
                .map(HashSpi::getImplementedAlgorithms)
                .flatMap(Set::stream)
                .toList();
        System.out.println("HashAlgorithms: " + hashAlgorithms);

        String plainText = "123456";

        DefaultHashService hashService = new DefaultHashService();
        hashService.setDefaultAlgorithmName("2b");

        DefaultPasswordService passwordService = new DefaultPasswordService();
        passwordService.setHashService(hashService);

        String encryptPassword = passwordService.encryptPassword(plainText);
        System.out.println(encryptPassword);

        boolean b1 = passwordService.passwordsMatch(plainText, encryptPassword);
        System.out.println(b1);

        HashProvider.getByAlgorithmName("2b").ifPresent(hashSpi -> {
            Hash hash = hashSpi.fromString("$2b$10$kJ/Gvbcc1bXD929k5DXA..TAs5sfvHNddDU1jaqy2GvH9aAEhybp.");
            boolean b = hash.matchesPassword(new SimpleByteSource(plainText));
            System.out.println(b);
            System.out.println(hash);
        });
    }
}
