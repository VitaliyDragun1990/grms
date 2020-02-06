package com.revenat.germes.application.service.transfrom.impl.cache;

import com.revenat.germes.application.service.transfrom.helper.SimilarFieldsLocator;
import com.revenat.germes.application.service.transfrom.impl.FieldProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    void shouldProvideSimilarFieldNames() {
        final FieldProvider fieldProvider = new CachedFieldProvider(new SimilarFieldsLocator());

        final List<String> result = fieldProvider.getSimilarFieldNames(Source.class, Destination.class);

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(equalTo("value")));
    }

    @Test
    void shouldCacheSimilarFieldNamesForSameClassPairs(@Mock final SimilarFieldsLocator fieldFinderMock) {
        final FieldProvider fieldProvider = new CachedFieldProvider(fieldFinderMock);
        when(fieldFinderMock.findByName(Source.class, Destination.class)).thenReturn(List.of("value"));

        fieldProvider.getSimilarFieldNames(Source.class, Destination.class);
        fieldProvider.getSimilarFieldNames(Source.class, Destination.class);
        fieldProvider.getSimilarFieldNames(Source.class, Destination.class);

        verify(fieldFinderMock, times(1)).findByName(Source.class, Destination.class);
    }

    static class Source {
        int value;
    }

    static class Destination {
        int value;

        String text;
    }
}