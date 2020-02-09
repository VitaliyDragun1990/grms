package com.revenat.germes.presentation.admin.bean.startup;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CDI extension that automatically loads all the CDI application scoped beans
 * (marked with {@linkplain ApplicationScoped @ApplicationScoped} annotation)
 * marked with {@linkplain Eager @Eager} annotation on application startup
 *
 * @author Vitaliy Dragun
 */
public class EagerExtension implements Extension {

    private final List<Bean<?>> startupBeans = new ArrayList<>();

    /**
     * Collects all CDI application scoped beans annotated with {@linkplain Eager @Eager} annotation
     *
     * @param event event fired by CDI container for each enabled bean
     * @param <T>   type of the bean
     */
    public <T> void collect(@Observes final ProcessBean<T> event) {
        if (event.getAnnotated().isAnnotationPresent(Eager.class) &&
                event.getAnnotated().isAnnotationPresent(ApplicationScoped.class)) {
            startupBeans.add(event.getBean());
        }
    }

    /**
     * Effectively loads startup beans
     *
     * @param event       event fired by CDI container before creating context
     * @param beanManager bean manager instance responsible to load beans
     */
    public void load(@Observes final AfterDeploymentValidation event, final BeanManager beanManager) {
        for (final Bean<?> bean : startupBeans) {
            beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean))
                    .toString(); // toString call loads bean for sure
        }
    }
}
