package com.revenat.germes.infrastructure.transform;

/**
 * Any object that supports direct/backward transformation
 * into some kind of object
 *
 * @author Vitaliy Dragun
 */
public interface Transformable<P> {

    /**
     * Transform given object into current one
     *
     * @param p object to transform from
     */
    void transform(P p);

    /**
     * Transform current object into given one
     * @param p object to transform to
     *
     * @return transformed object
     */
    P untransform(P p);
}
