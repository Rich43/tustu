package com.google.gson;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/* loaded from: gson-2.9.0.jar:com/google/gson/JsonParser.class */
public final class JsonParser {
    @Deprecated
    public JsonParser() {
    }

    public static JsonElement parseString(String json) throws JsonSyntaxException {
        return parseReader(new StringReader(json));
    }

    public static JsonElement parseReader(Reader reader) throws JsonSyntaxException, JsonIOException {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            JsonElement element = parseReader(jsonReader);
            if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonSyntaxException("Did not consume the entire document.");
            }
            return element;
        } catch (MalformedJsonException e2) {
            throw new JsonSyntaxException(e2);
        } catch (IOException e3) {
            throw new JsonIOException(e3);
        } catch (NumberFormatException e4) {
            throw new JsonSyntaxException(e4);
        }
    }

    public static JsonElement parseReader(JsonReader reader) throws JsonSyntaxException, JsonIOException {
        boolean lenient = reader.isLenient();
        reader.setLenient(true);
        try {
            try {
                JsonElement jsonElement = Streams.parse(reader);
                reader.setLenient(lenient);
                return jsonElement;
            } catch (OutOfMemoryError e2) {
                throw new JsonParseException("Failed parsing JSON source: " + ((Object) reader) + " to Json", e2);
            } catch (StackOverflowError e3) {
                throw new JsonParseException("Failed parsing JSON source: " + ((Object) reader) + " to Json", e3);
            }
        } catch (Throwable th) {
            reader.setLenient(lenient);
            throw th;
        }
    }

    @Deprecated
    public JsonElement parse(String json) throws JsonSyntaxException {
        return parseString(json);
    }

    @Deprecated
    public JsonElement parse(Reader json) throws JsonSyntaxException, JsonIOException {
        return parseReader(json);
    }

    @Deprecated
    public JsonElement parse(JsonReader json) throws JsonSyntaxException, JsonIOException {
        return parseReader(json);
    }
}
