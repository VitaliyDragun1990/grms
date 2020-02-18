package com.revenat.germes.infrastructure.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.revenat.germes.infrastructure.exception.flow.ValidationException;
import com.revenat.germes.infrastructure.helper.Asserts;
import com.revenat.germes.infrastructure.json.JsonClient;

/**
 * Gson-based implementation of {@link JsonClient}
 *
 * @author Vitaliy Dragun
 */
public class GsonJsonClient implements JsonClient {

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
