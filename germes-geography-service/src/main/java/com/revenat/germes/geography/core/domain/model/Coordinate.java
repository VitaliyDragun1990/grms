package com.revenat.germes.geography.core.domain.model;

import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Geographical coordinate of an object
 *
 * @author Vitaliy Dragun
 */
@Embeddable
@Setter
public class Coordinate {

    private double x;

    private double y;

    public Coordinate(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate() {
    }

    @Column(name = "X")
    public double getX() {
        return x;
    }

    @Column(name = "Y")
    public double getY() {
        return y;
    }

}
