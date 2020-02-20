package com.revenat.germes.presentation.admin.security;

import com.revenat.germes.user.model.entity.User;
import com.revenat.germes.user.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Authorization component that integrates with {@link UserService} to fetch
 * user by login
 *
 * @author Vitaliy Dragun
 */
public class CDIRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(CDIRealm.class);

    private final UserService userService;

    public CDIRealm(final UserService userService) {
        this.userService = userService;

        final HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("SHA-256");
        credentialsMatcher.setStoredCredentialsHexEncoded(true);

        setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
        final UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        final String username = upToken.getUsername();

        try {
            final String password = Optional.ofNullable(username)
                    .flatMap(userService::findByUserName)
                    .map(User::getPassword)
                    .orElseThrow(() -> new UnknownAccountException("No account found for user " + username));

            return new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
        } catch (final Exception e) {
            final String message = "There was an error while authenticating user " + username;
            LOGGER.warn(message, e);

            throw new AuthenticationException(message, e);
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        return new SimpleAccount();
    }
}
