package com.revenat.germes.common.ui.api.rest.handler.jersey.extension;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.util.List;
import java.util.function.Supplier;

/**
 * JUnit 5 extension for Jersey test library
 *
 * @author Vitaliy Dragun
 */
public class JerseyTestExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    /**
     * List of supported classes that can be injected as parameters
     */
    private static final List<Class<?>> PARAMETER_TYPES = List.of(WebTarget.class, Client.class);

    private final Supplier<Application> applicationSupplier;

    public JerseyTestExtension(final Supplier<Application> applicationSupplier) {
        this.applicationSupplier = applicationSupplier;
    }

    @Override
    public void afterEach(final ExtensionContext context) throws Exception {
        final Store store = getStore(context);
        store.remove(JerseyTest.class, JerseyTest.class).tearDown(); // stop jersey test container
        PARAMETER_TYPES.forEach(store::remove);
    }

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        final JerseyTest jerseyTest = new JerseyTest() {
            @Override
            protected Application configure() {
                return applicationSupplier.get();
            }
        };
        jerseyTest.setUp(); // start jersey test container
        getStore(context).put(JerseyTest.class, jerseyTest);
        getStore(context).put(WebTarget.class, jerseyTest.target());
        getStore(context).put(Client.class, jerseyTest.client());
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final Class<?> parameterType = parameterContext.getParameter().getType();
        return PARAMETER_TYPES.contains(parameterType);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final Class<?> parameterType = parameterContext.getParameter().getType();
        return getStore(extensionContext).get(parameterType, parameterType);
    }

    private Store getStore(final ExtensionContext extensionContext) {
        return extensionContext.getStore(ExtensionContext.Namespace.GLOBAL);
    }
}
