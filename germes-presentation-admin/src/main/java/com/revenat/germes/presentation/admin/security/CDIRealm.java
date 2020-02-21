package com.revenat.germes.presentation.admin.security;

import com.revenat.germes.user.presentation.rest.client.UserFacade;
import com.revenat.germes.user.presentation.rest.dto.LoginDTO;
import com.revenat.germes.user.presentation.rest.dto.UserDTO;
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

    public CDIRealm(final UserFacade userFacade) {
        this.userFacade = userFacade;

        setCredentialsMatcher(new SimpleCredentialsMatcher());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
        final UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        final String username = upToken.getUsername();
        final String password = String.valueOf(upToken.getPassword());

        try {
            final UserDTO loggedUser = Optional.ofNullable(username)
                    .map(name -> userFacade.login(new LoginDTO(name, password)))
                    .orElseThrow(() -> new UnknownAccountException("No account found for user " + username));

            return new SimpleAuthenticationInfo(loggedUser.getUserName(), password, getName());
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
