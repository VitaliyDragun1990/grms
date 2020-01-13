package com.revenat.germes.presentation.config;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Registers DI bindings
 *
 * @author Vitaliy Dragun
 */
public class ComponentFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new ComponentBinder());
        return true;
    }
}
