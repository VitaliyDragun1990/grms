package com.revenat.germes.common.core.shared.transform.impl.helper.cached;

import com.revenat.germes.common.core.shared.transform.provider.BaseFieldProvider;
import com.revenat.germes.common.core.shared.transform.provider.CachedFieldProvider;
import com.revenat.germes.common.core.shared.transform.provider.FieldProvider;
import com.revenat.germes.common.core.shared.transform.annotation.DomainProperty;
import com.revenat.germes.common.core.shared.transform.impl.helper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

/**
 * @author Vitaliy Dragun
 */
@DisplayName("cached field provider")
@ExtendWith(MockitoExtension.class)
class CachedFieldProviderTest {

    private FieldProvider actualFieldProvider;

    @BeforeEach
    void setUp() {
        actualFieldProvider = new BaseFieldProvider(new SimilarFieldsLocator(), new FieldManager());
    }

    @Test
    void shouldProvideSimilarFieldNames() {
        final CachedFieldProvider fieldProvider = new CachedFieldProvider(actualFieldProvider);

        final List<String> result = fieldProvider.getSimilarFieldNames(Source.class, Destination.class);

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(equalTo("value")));
    }

    @Test
    void shouldCacheSimilarFieldNamesForSameClassPairs(@Mock final FieldProvider fieldProviderMock) {
        final CachedFieldProvider fieldProvider = new CachedFieldProvider(fieldProviderMock);
        when(fieldProviderMock.getSimilarFieldNames(Source.class, Destination.class)).thenReturn(List.of("value"));

        fieldProvider.getSimilarFieldNames(Source.class, Destination.class);
        fieldProvider.getSimilarFieldNames(Source.class, Destination.class);
        fieldProvider.getSimilarFieldNames(Source.class, Destination.class);

        verify(fieldProviderMock, times(1)).getSimilarFieldNames(Source.class, Destination.class);
    }

    @Test
    void shouldProvideDomainPropertyFields() {
        final CachedFieldProvider fieldProvider = new CachedFieldProvider(actualFieldProvider);

        final List<String> result = fieldProvider.getDomainPropertyFields(Destination.class);

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(equalTo("text")));
    }

    @Test
    void shouldCacheDomainPropertyFieldsForSameClass(@Mock final FieldProvider fieldProviderMock) {
        final CachedFieldProvider fieldProvider = new CachedFieldProvider(fieldProviderMock);
        when(fieldProviderMock.getDomainPropertyFields(Destination.class)).thenReturn(List.of("text"));

        fieldProvider.getDomainPropertyFields(Destination.class);
        fieldProvider.getDomainPropertyFields(Destination.class);
        fieldProvider.getDomainPropertyFields(Destination.class);

        verify(fieldProviderMock, times(1))
                .getDomainPropertyFields(ArgumentMatchers.any(Class.class));
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