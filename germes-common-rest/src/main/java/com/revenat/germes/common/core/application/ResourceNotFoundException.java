package com.revenat.germes.common.core.application;


import com.revenat.germes.common.core.shared.exception.base.ApplicationException;

/**
 * Signals that resource with given identifier can not be found.
 *
 * @author Vitaliy Dragun
 */
public class ResourceNotFoundException extends ApplicationException {

    private static final long serialVersionUID = 742925454039222420L;

    public ResourceNotFoundException(final Class<?> resourceClass, final int resourceId) {
        super("Invalid " + resourceClass.getSimpleName().toLowerCase() + " identifier:" + resourceId);
    }
}
