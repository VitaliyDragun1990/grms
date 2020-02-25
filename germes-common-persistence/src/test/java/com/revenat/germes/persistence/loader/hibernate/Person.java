package com.revenat.germes.persistence.loader.hibernate;

import com.revenat.germes.model.entity.base.AbstractEntity;

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