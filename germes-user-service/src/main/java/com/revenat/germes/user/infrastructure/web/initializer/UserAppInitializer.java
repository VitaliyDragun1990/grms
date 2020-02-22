package com.revenat.germes.user.infrastructure.web.initializer;

import com.revenat.germes.user.infrastructure.config.UserSpringConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Loads Spring configuration for User service application on web container startup
 *
 * @author Vitaliy Dragun
 */
public class UserAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/*"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { UserSpringConfig.class };
    }
}
