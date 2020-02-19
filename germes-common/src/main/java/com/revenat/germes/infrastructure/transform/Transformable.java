package com.revenat.germes.infrastructure.transform;

/**
 * Declares transform/untransform operations that should be used to copy
 * data between pair of objects in two directions (for example, business entity and DTO)
 *
 * @author Vitaliy Dragun
 */
public interface Transformable<T, P> {

    /**
     * Transforms object {@code t} into object {@code p}
     *
     * @param t object to transform from
     * @param p object to transform to
     * @return transformed object {@code p}
     */
    default P transform(T t, P p) {
        return p;
    }

    /**
     * Untransforms object {@code p} into object {@code t}
     *
     * @param p object to untransform from
     * @param t object to transform to
     * @return untransformed object {@code t}
     */
    default T untransform(P p, T t) {
        return t;
    }
}
