package top.funsite.spring.action.domin;

/**
 * 接口状态码定义。
 * <table>
 *     <thead>
 *         <tr><th>Code</th><th>Comment</th></tr>
 *     </thead>
 *     <tbody>
 *         <tr><td>0</td><td>OK</td></tr>
 *         <tr><td>1</td><td>错误（无法判断具体的错误类型）</td></tr>
 *         <tr><td>100xx</td><td>登录错误</td></tr>
 *         <tr><td>101xx</td><td>通用错误</td></tr>
 *     </tbody>
 * </table>
 */
public enum ServiceStatus {

    /// 基础状态码与 HTTP 响应状态码相同，方便设置响应状态码。

    OK(200, "OK"),

    UNAUTHORIZED(401, "Unauthorized"),

    FORBIDDEN(403, "Forbidden"),

    ERROR(500, "Internal Server Error"),

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
}
