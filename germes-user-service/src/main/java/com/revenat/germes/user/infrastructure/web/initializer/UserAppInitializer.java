package com.revenat.germes.user.infrastructure.web.initializer;

import com.revenat.germes.user.infrastructure.config.UserSpringConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Loads Spring configuration for User service application on web container startup
 *
 * @author Vitaliy Dragun
 */
public class UserAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(UserSpringConfig.class);

        servletContext.addListener(new ContextLoaderListener(context));

        final DispatcherServlet servlet = new DispatcherServlet(context);
        final ServletRegistration.Dynamic registration = servletContext.addServlet("api", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/*");
    }
}
