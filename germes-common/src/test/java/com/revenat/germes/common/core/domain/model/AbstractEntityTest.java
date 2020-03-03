package com.revenat.germes.common.core.domain.model;

import com.revenat.germes.common.core.domain.model.AbstractEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("abstract entity")
class AbstractEntityTest {

    @Test
    void shouldBeEqualToAnotherAbstractEntityWithSameId() {
        SampleEntity entity1 = new SampleEntity();
        entity1.setId(1);
        SampleEntity entity2 = new SampleEntity();
        entity2.setId(1);

        assertThat(entity1, equalTo(entity2));
    }

    @Test
    void shouldConsiderOnlyIdPropertyWhenPerformEqualityCheck() {
        SampleEntity entity1 = new SampleEntity();
        entity1.setId(1);
        entity1.setCreatedAt(LocalDateTime.now().minusDays(10));
        entity1.setModifiedAt(LocalDateTime.now().minusDays(2));

        SampleEntity entity2 = new SampleEntity();
        entity2.setId(1);
        entity2.setCreatedAt(LocalDateTime.now().minusDays(2));
        entity2.setModifiedAt(LocalDateTime.now());

        assertThat(entity1, equalTo(entity2));
    }

    private static class SampleEntity extends AbstractEntity {
    }
}