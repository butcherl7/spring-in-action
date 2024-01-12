package top.funsite.spring.action.domin;

/**
 * <li>0 OK</li>
 * <li>1 错误（无法判断具体的错误类型）</li>
 * <li>100xx 登录错误</li>
 * <li>101xx 通用错误</li>
 */
public enum ServiceStatus {

    OK(0, "OK"),

    ERROR(1, "Error"),

    /// 100xx

    AUTHENTICATION_ERROR(10000, "Authentication Error"),

    UNKNOWN_ACCOUNT(10001, "Unknown Account"),

    INCORRECT_CREDENTIALS(10002, "Incorrect Credentials"),

    ACCOUNT_DISABLED(10003, "Account Disabled"),

    ACCOUNT_LOCKED(10004, "Account Locked"),

    LOGIN_TIMEOUT(10005, "Login Timeout"),

    /// 101xx

    BAD_PARAMETER(10101, "Bad Parameter");

    private final int value;

    private final String reasonPhrase;

    ServiceStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String reasonPhrase() {
        return this.reasonPhrase;
    }

    public boolean isError() {
        return value != 0;
    }
}
