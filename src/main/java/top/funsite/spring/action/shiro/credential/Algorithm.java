package top.funsite.spring.action.shiro.credential;

import lombok.Getter;

@Getter
public enum Algorithm {

    ARGON2_D("argon2d"),

    ARGON2_I("argon2i"),

    ARGON2_ID("argon2id"),

    BCRYPT_2("2"),

    BCRYPT_2A("2a"),

    BCRYPT_2B("2b"),

    BCRYPT_2Y("2y");

    private final String algorithmName;

    Algorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }
}
