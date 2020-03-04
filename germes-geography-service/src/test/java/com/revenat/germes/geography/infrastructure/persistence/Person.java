package com.revenat.germes.geography.infrastructure.persistence;

import com.revenat.germes.common.core.domain.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vitaliy Dragun
 */
@Entity
@Table(name = "PERSON")
class Person extends AbstractEntity {

    private String name;

    public Person(String name) {
        this.name = name;
    }

    private Person() {
    }

    @Column(name = "name", nullable = false)
    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}
