package com.revenat.germes.application.service.transfrom.impl.cache;

import com.revenat.germes.application.service.transfrom.annotation.DomainProperty;
import com.revenat.germes.application.service.transfrom.helper.FieldManager;
import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsLocator;
import com.revenat.germes.application.service.transfrom.impl.FieldProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("cached field provider")
@ExtendWith(MockitoExtension.class)
class CachedFieldProviderTest {

    @Test
    void shouldProvideSimilarFieldNames() {
        final FieldProvider fieldProvider = new CachedFieldProvider(new SimilarFieldsLocator(), new FieldManager());

        final List<String> result = fieldProvider.getSimilarFieldNames(Source.class, Destination.class);

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(equalTo("value")));
    }

    @Test
    void shouldCacheSimilarFieldNamesForSameClassPairs(@Mock final SimilarFieldsLocator fieldLocatorMock) {
        final FieldProvider fieldProvider = new CachedFieldProvider(fieldLocatorMock, new FieldManager());
        when(fieldLocatorMock.findByName(Source.class, Destination.class)).thenReturn(List.of("value"));

        fieldProvider.getSimilarFieldNames(Source.class, Destination.class);
        fieldProvider.getSimilarFieldNames(Source.class, Destination.class);
        fieldProvider.getSimilarFieldNames(Source.class, Destination.class);

        verify(fieldLocatorMock, times(1)).findByName(Source.class, Destination.class);
    }

    @Test
    void shouldProvideDomainPropertyFields() {
        final FieldProvider fieldProvider = new CachedFieldProvider(new SimilarFieldsLocator(), new FieldManager());

        final List<String> result = fieldProvider.getDomainPropertyFields(Destination.class);

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(equalTo("text")));
    }

    @Test
    void shouldCacheDomainPropertyFieldsForSameClass(@Mock FieldManager fieldManagerMock) {
        final FieldProvider fieldProvider = new CachedFieldProvider(new SimilarFieldsLocator(), fieldManagerMock);
        when(fieldManagerMock.getFieldNames(ArgumentMatchers.any(Class.class), ArgumentMatchers.any(List.class))).thenReturn(List.of("text"));

        fieldProvider.getDomainPropertyFields(Destination.class);
        fieldProvider.getDomainPropertyFields(Destination.class);
        fieldProvider.getDomainPropertyFields(Destination.class);

        verify(fieldManagerMock, times(1))
                .getFieldNames(ArgumentMatchers.any(Class.class), ArgumentMatchers.any(List.class));
    }

    static class Source {
        int value;
    }

    static class Destination {
        int value;

        @DomainProperty("test")
        String text;
    }
}