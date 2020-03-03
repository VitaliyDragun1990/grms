package com.revenat.germes.common.infrastructure.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.revenat.germes.common.core.shared.exception.flow.ValidationException;
import com.revenat.germes.common.core.shared.helper.Asserts;
import com.revenat.germes.common.infrastructure.json.JsonTranslator;

/**
 * Gson-based implementation of {@link JsonTranslator}
 *
 * @author Vitaliy Dragun
 */
public class GsonJsonTranslator implements JsonTranslator {

    private static final Gson GSON = new Gson();

    @Override
    public <T> String toJson(final T t) {
        return GSON.toJson(t);
    }

    @Override
    public <T> T fromJson(final String json, final Class<T> clz) {
        Asserts.assertNotNull(clz, "class of the object to get from JSON can not be null");

        try {
            return GSON.fromJson(json, clz);
        } catch (final JsonSyntaxException e) {
            throw new ValidationException("Error parsing JSON string", e);
        }
    }
}
