package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: gson-2.9.0.jar:com/google/gson/JsonDeserializationContext.class */
public interface JsonDeserializationContext {
    <T> T deserialize(JsonElement jsonElement, Type type) throws JsonParseException;
}
