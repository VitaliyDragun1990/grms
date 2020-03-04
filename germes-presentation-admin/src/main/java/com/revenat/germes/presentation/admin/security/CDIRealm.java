package com.revenat.germes.presentation.admin.security;

import com.revenat.germes.common.core.shared.encrypter.Encrypter;
import com.revenat.germes.user.core.application.UserFacade;
import com.revenat.germes.user.core.application.LoginInfo;
import com.revenat.germes.user.core.application.UserInfo;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Authorization component that integrates with {@link UserFacade} to fetch
 * user by login
 *
 * @author Vitaliy Dragun
 */
public class CDIRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(CDIRealm.class);

    private final UserFacade userFacade;

    private final Encrypter encrypter;

    public CDIRealm(final UserFacade userFacade, final Encrypter encrypter) {
        this.userFacade = userFacade;
        this.encrypter = encrypter;

        setCredentialsMatcher(new SimpleCredentialsMatcher());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
        final UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        final String username = upToken.getUsername();
        final String password = String.valueOf(upToken.getPassword());

        try {
            final UserInfo loggedUser = Optional.ofNullable(username)
                    .map(name -> userFacade.login(new LoginInfo(name, encrypter.encryptSHA(password))))
                    .orElseThrow(() -> new UnknownAccountException("No account found for user " + username));

            return new SimpleAuthenticationInfo(loggedUser.getUserName(), password, getName());
        } catch (UnknownAccountException e) {
            throw new AuthenticationException(e.getMessage(), e);
        }
         catch (final Exception e) {
             throw new AuthenticationException("There was an error while authenticating user " + username, e);
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        return new SimpleAccount();
    }
}
