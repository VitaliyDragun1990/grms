package com.revenat.germes.application.model.entity.geography;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Geographical coordinate of an object
 *
 * @author Vitaliy Dragun
 */
@Embeddable
public class Coordinate {

    private double x;

    private double y;

    public Coordinate(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    Coordinate() {
    }

    @Column(name = "X")
    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    @Column(name = "Y")
    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }
}
