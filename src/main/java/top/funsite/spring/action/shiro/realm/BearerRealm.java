package top.funsite.spring.action.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;

/**
 * 多 Realm 测试（不同的用户类型使用不同的 Realm），可以通过 {@link #supports(AuthenticationToken)}. 或者使用 {@link AuthenticationStrategy}.
 *
 * @see ModularRealmAuthenticator#doMultiRealmAuthentication(Collection, AuthenticationToken)
 */
public class BearerRealm extends AbstractRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        BearerToken uToken = (BearerToken) authToken;
        String token = uToken.getToken();
        return new SimpleAuthenticationInfo(token, token, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof BearerToken;
    }
}
