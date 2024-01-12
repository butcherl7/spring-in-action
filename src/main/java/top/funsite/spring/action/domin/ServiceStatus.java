package top.funsite.spring.action.domin;

/**
 * <li>100xx 登录错误</li>
 */
public enum ServiceStatus {

    OK(0, "OK"),

    ERROR(1, "Error"),

    AUTHENTICATION_ERROR(10000, "Authentication Error"),

    UNKNOWN_ACCOUNT(10001, "Unknown Account"),

    INCORRECT_CREDENTIALS(10002, "Incorrect Credentials"),

    ACCOUNT_DISABLED(10003, "Account Disabled"),

    ACCOUNT_LOCKED(10004, "Account Locked"),

    LOGIN_TIMEOUT(10005, "Login Timeout");

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
