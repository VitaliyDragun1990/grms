package com.revenat.germes.presentation.admin.config.security;

import com.revenat.germes.common.core.shared.encrypter.Encrypter;
import com.revenat.germes.user.core.application.UserFacade;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.WebEnvironment;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

/**
 * Initializes authentication realm that delegates processing to {@link CDIRealm}
 *
 * @author Vitaliy Dragun
 */
@WebListener
public class CdiEnvironmentLoaderListener extends EnvironmentLoaderListener {

    @Inject
    @Default
    private UserFacade userFacade;

    @Inject
    private Encrypter encrypter;

    @Override
    protected WebEnvironment createEnvironment(final ServletContext context) {
        final WebEnvironment environment = super.createEnvironment(context);

        final RealmSecurityManager rsm = (RealmSecurityManager) environment.getSecurityManager();

        rsm.setRealm(new CDIRealm(userFacade, encrypter));

        return environment;
    }
}
