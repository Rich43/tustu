package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: gson-2.9.0.jar:com/google/gson/JsonSerializer.class */
public interface JsonSerializer<T> {
    JsonElement serialize(T t2, Type type, JsonSerializationContext jsonSerializationContext);
}
