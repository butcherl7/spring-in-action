package top.funsite.spring.action.domin;

/**
 * 接口状态码定义。
 * <table>
 *     <thead>
 *         <tr><th>Code</th><th>Comment</th></tr>
 *     </thead>
 *     <tbody>
 *         <tr><td>200</td><td>OK</td></tr>
 *         <tr><td>500</td><td>错误（无法判断具体的错误类型）</td></tr>
 *         <tr><td>100xx</td><td>登录错误</td></tr>
 *         <tr><td>101xx</td><td>通用错误</td></tr>
 *     </tbody>
 * </table>
 */
public enum ServiceStatus {

    OK(200),

    /**
     * 未通过身份验证。
     */
    UNAUTHENTICATED(401),

    /**
     * 没有执行请求或访问资源的权限。
     */
    UNAUTHORIZED(403),

    ERROR(500),

    /// 100xx

    AUTHENTICATION_ERROR(10000),

    UNKNOWN_ACCOUNT(10001),

    INCORRECT_CREDENTIALS(10002),

    ACCOUNT_DISABLED(10003),

    ACCOUNT_LOCKED(10004),

    LOGIN_TIMEOUT(10005),

    /// 101xx

    BAD_PARAMETER(10101);

    private final int status;

    ServiceStatus(int status) {
        this.status = status;
    }

    public int status() {
        return this.status;
    }
}
