package top.funsite.spring.action.crypto;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashSpi;
import org.apache.shiro.lang.util.SimpleByteSource;
import org.junit.jupiter.api.Test;
import top.funsite.spring.action.shiro.credential.Algorithm;
import top.funsite.spring.action.shiro.credential.NoIdentifierFormat;
import top.funsite.spring.action.shiro.credential.StrongerHashPasswordService;

import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
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

        /*String encryptPassword = passwordService.encryptPassword(plainText);
        System.out.println(encryptPassword);*/

        boolean b1 = passwordService.passwordsMatch(plainText, "$shiro2$2b$10$Q00HLmH6EBxMHjyQ0icFAuu5oQYzrV2Nn7eeg8L0dDhInPqTemYBK");
        System.out.println(b1);

       /* HashProvider.getByAlgorithmName("2b").ifPresent(hashSpi -> {
            Hash hash = hashSpi.fromString("$2b$10$kJ/Gvbcc1bXD929k5DXA..TAs5sfvHNddDU1jaqy2GvH9aAEhybp.");
            boolean b = hash.matchesPassword(new SimpleByteSource(plainText));
            System.out.println(b);
            System.out.println(hash);
        });*/
    }

    @Test
    public void testByteSource() {
        SimpleByteSource byteSource = new SimpleByteSource("123456@ +.1");
        String base64 = byteSource.toBase64();
        assertEquals("MTIzNDU2QCArLjE=", base64);
    }

    @Test
    public void testNoIdentifierFormat() {
        String formatted = "$2b$10$0Jk1V2gHjqyPKthfuiC9puPHlpsy40wmIramkz1oYnZllWVrt69FO";

        NoIdentifierFormat format = new NoIdentifierFormat();
        Hash hash = format.parse(formatted);
        String string = format.format(hash);

        log.info(hash.toString());

        assertEquals(formatted, string);
    }

    @Test
    public void testBcryptPasswordService() {
        String plaintext = "123456";
        String ciphertext = "$2b$10$MEkYQf7wy3KhbLh9SyI0Oe.zy.u1J6MfIUjcBPu38FTwbIlr2Tv/2";

        StrongerHashPasswordService passwordService = new StrongerHashPasswordService();
        String encryptPassword = passwordService.encryptPassword(plaintext);
        log.info(encryptPassword);

        boolean bString = passwordService.passwordsMatch(plaintext, ciphertext);
        assertTrue(bString);
    }

    @Test
    public void testArgon2() {
        var plaintext = "123456";
        var ciphertext = "$argon2id$v=19$t=1,m=65536,p=4$uYg08RWy4+lHoPHwz/Ou1w$y7K7Awgw51rdEmHiQBDacqLV//3gb7q3OU2wSvD0DWg";

        StrongerHashPasswordService passwordService = new StrongerHashPasswordService(Algorithm.ARGON2_ID);
        // String password = passwordService.encryptPassword(plaintext);
        // log.info("password: {}", password);
        assertTrue(passwordService.passwordsMatch(plaintext, ciphertext));
    }
}
