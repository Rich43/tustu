package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: gson-2.9.0.jar:com/google/gson/JsonDeserializer.class */
public interface JsonDeserializer<T> {
    T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException;
}
