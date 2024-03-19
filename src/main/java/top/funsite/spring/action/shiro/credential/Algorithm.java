package top.funsite.spring.action.shiro.credential;

import lombok.Getter;

/**
 * 定义了可用的哈希算法名称。
 * <p>
 * <strong>Argon2</strong> 是一种现代的密码哈希函数，它在 2015 年的密码哈希竞赛中获胜，被设计来抵抗各种密码破解攻击，Argon2 的设计目标是最大化内存填充率和多核心处理器的利用，同时提供对时间-空间权衡攻击的防御。它通过参数来调整时间成本、内存成本和并行度，以适应不同的应用需求和硬件限制。
 * </p>
 * <p>
 * <strong>Bcrypt</strong> 是一种密码哈希函数，它基于 Blowfish 加密算法，在 1999 年设计。Bcrypt 通过使用盐（salt）来抵御彩虹表攻击，并且具有自适应性，可以通过增加迭代次数来抵御暴力搜索攻击。这种自适应性意味着即使计算机的运算能力提高，Bcrypt 也能通过增加工作量（例如迭代次数）来保持加密的强度。
 * </p>
 */
@Getter
public enum Algorithm {

    /**
     * Argon2d 速度更快，并且使用数据依赖的内存访问方式，这使得它对 GPU 破解攻击有很强的抵抗力，适合没有 side-channel timing attacks 威胁的应用（例如加密货币）。
     */
    ARGON2_D("argon2d"),

    /**
     * Argon2i 使用数据无关的内存访问，这对于密码哈希和基于密码的密钥推导算法来说是首选，其特点是速度较慢，因为它在内存上运行了更多的处理逻辑，以防止 tradeoff attacks。
     */
    ARGON2_I("argon2i"),

    /**
     * Argon2id 是 Argon2i 和 Argon2d 的混合体，采用数据依赖型和数据独立型内存访问相结合的方式，从而可以同时抵御 side-channel timing attacks 和 GPU 破解攻击的能力。
     */
    ARGON2_ID("argon2id"),

    /**
     * 2 是 Bcrypt 的原始前缀，用于标识 OpenBSD 密码文件中使用的算法。
     */
    BCRYPT_2("2"),

    /**
     * 2a 的版本修订了对非 ASCII 字符和空终止符的处理方式。
     */
    BCRYPT_2A("2a"),

    /**
     * 2y (2011年6月) 是由于 PHP 实现的 crypt_blowfish 中发现的一个错误，该错误错误地处理了第 8 位设置的字符。
     */
    BCRYPT_2Y("2y"),

    /**
     * 2b (2014年2月) 在 OpenBSD 的实现中发现了一个错误，他们在一个无符号字符中存储了字符串的长度。如果密码超过 255 个字符，它会溢出并在 255 处换行。
     */
    BCRYPT_2B("2b");

    private final String algorithmName;

    Algorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }
}
