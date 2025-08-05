package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javafx.fxml.FXMLLoader;

/* loaded from: gson-2.9.0.jar:com/google/gson/internal/bind/JsonTreeReader.class */
public final class JsonTreeReader extends JsonReader {
    private static final Reader UNREADABLE_READER = new Reader() { // from class: com.google.gson.internal.bind.JsonTreeReader.1
        @Override // java.io.Reader
        public int read(char[] buffer, int offset, int count) throws IOException {
            throw new AssertionError();
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            throw new AssertionError();
        }
    };
    private static final Object SENTINEL_CLOSED = new Object();
    private Object[] stack;
    private int stackSize;
    private String[] pathNames;
    private int[] pathIndices;

    public JsonTreeReader(JsonElement element) {
        super(UNREADABLE_READER);
        this.stack = new Object[32];
        this.stackSize = 0;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        push(element);
    }

    @Override // com.google.gson.stream.JsonReader
    public void beginArray() throws IOException {
        expect(JsonToken.BEGIN_ARRAY);
        JsonArray array = (JsonArray) peekStack();
        push(array.iterator());
        this.pathIndices[this.stackSize - 1] = 0;
    }

    @Override // com.google.gson.stream.JsonReader
    public void endArray() throws IOException {
        expect(JsonToken.END_ARRAY);
        popStack();
        popStack();
        if (this.stackSize > 0) {
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
        }
    }

    @Override // com.google.gson.stream.JsonReader
    public void beginObject() throws IOException {
        expect(JsonToken.BEGIN_OBJECT);
        JsonObject object = (JsonObject) peekStack();
        push(object.entrySet().iterator());
    }

    @Override // com.google.gson.stream.JsonReader
    public void endObject() throws IOException {
        expect(JsonToken.END_OBJECT);
        popStack();
        popStack();
        if (this.stackSize > 0) {
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
        }
    }

    @Override // com.google.gson.stream.JsonReader
    public boolean hasNext() throws IOException {
        JsonToken token = peek();
        return (token == JsonToken.END_OBJECT || token == JsonToken.END_ARRAY || token == JsonToken.END_DOCUMENT) ? false : true;
    }

    @Override // com.google.gson.stream.JsonReader
    public JsonToken peek() throws IOException {
        if (this.stackSize == 0) {
            return JsonToken.END_DOCUMENT;
        }
        Object o2 = peekStack();
        if (o2 instanceof Iterator) {
            boolean isObject = this.stack[this.stackSize - 2] instanceof JsonObject;
            Iterator<?> iterator = (Iterator) o2;
            if (!iterator.hasNext()) {
                return isObject ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
            }
            if (isObject) {
                return JsonToken.NAME;
            }
            push(iterator.next());
            return peek();
        }
        if (o2 instanceof JsonObject) {
            return JsonToken.BEGIN_OBJECT;
        }
        if (o2 instanceof JsonArray) {
            return JsonToken.BEGIN_ARRAY;
        }
        if (o2 instanceof JsonPrimitive) {
            JsonPrimitive primitive = (JsonPrimitive) o2;
            if (primitive.isString()) {
                return JsonToken.STRING;
            }
            if (primitive.isBoolean()) {
                return JsonToken.BOOLEAN;
            }
            if (primitive.isNumber()) {
                return JsonToken.NUMBER;
            }
            throw new AssertionError();
        }
        if (o2 instanceof JsonNull) {
            return JsonToken.NULL;
        }
        if (o2 == SENTINEL_CLOSED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        throw new AssertionError();
    }

    private Object peekStack() {
        return this.stack[this.stackSize - 1];
    }

    private Object popStack() {
        Object[] objArr = this.stack;
        int i2 = this.stackSize - 1;
        this.stackSize = i2;
        Object result = objArr[i2];
        this.stack[this.stackSize] = null;
        return result;
    }

    private void expect(JsonToken expected) throws IOException {
        if (peek() != expected) {
            throw new IllegalStateException("Expected " + ((Object) expected) + " but was " + ((Object) peek()) + locationString());
        }
    }

    @Override // com.google.gson.stream.JsonReader
    public String nextName() throws IOException {
        expect(JsonToken.NAME);
        Iterator<?> i2 = (Iterator) peekStack();
        Map.Entry<?, ?> entry = (Map.Entry) i2.next();
        String result = (String) entry.getKey();
        this.pathNames[this.stackSize - 1] = result;
        push(entry.getValue());
        return result;
    }

    @Override // com.google.gson.stream.JsonReader
    public String nextString() throws IOException {
        JsonToken token = peek();
        if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected " + ((Object) JsonToken.STRING) + " but was " + ((Object) token) + locationString());
        }
        String result = ((JsonPrimitive) popStack()).getAsString();
        if (this.stackSize > 0) {
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
        }
        return result;
    }

    @Override // com.google.gson.stream.JsonReader
    public boolean nextBoolean() throws IOException {
        expect(JsonToken.BOOLEAN);
        boolean result = ((JsonPrimitive) popStack()).getAsBoolean();
        if (this.stackSize > 0) {
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
        }
        return result;
    }

    @Override // com.google.gson.stream.JsonReader
    public void nextNull() throws IOException {
        expect(JsonToken.NULL);
        popStack();
        if (this.stackSize > 0) {
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
        }
    }

    @Override // com.google.gson.stream.JsonReader
    public double nextDouble() throws IOException {
        JsonToken token = peek();
        if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + ((Object) JsonToken.NUMBER) + " but was " + ((Object) token) + locationString());
        }
        double result = ((JsonPrimitive) peekStack()).getAsDouble();
        if (!isLenient() && (Double.isNaN(result) || Double.isInfinite(result))) {
            throw new NumberFormatException("JSON forbids NaN and infinities: " + result);
        }
        popStack();
        if (this.stackSize > 0) {
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
        }
        return result;
    }

    @Override // com.google.gson.stream.JsonReader
    public long nextLong() throws IOException {
        JsonToken token = peek();
        if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + ((Object) JsonToken.NUMBER) + " but was " + ((Object) token) + locationString());
        }
        long result = ((JsonPrimitive) peekStack()).getAsLong();
        popStack();
        if (this.stackSize > 0) {
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
        }
        return result;
    }

    @Override // com.google.gson.stream.JsonReader
    public int nextInt() throws IOException {
        JsonToken token = peek();
        if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + ((Object) JsonToken.NUMBER) + " but was " + ((Object) token) + locationString());
        }
        int result = ((JsonPrimitive) peekStack()).getAsInt();
        popStack();
        if (this.stackSize > 0) {
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
        }
        return result;
    }

    JsonElement nextJsonElement() throws IOException {
        JsonToken peeked = peek();
        if (peeked == JsonToken.NAME || peeked == JsonToken.END_ARRAY || peeked == JsonToken.END_OBJECT || peeked == JsonToken.END_DOCUMENT) {
            throw new IllegalStateException("Unexpected " + ((Object) peeked) + " when reading a JsonElement.");
        }
        JsonElement element = (JsonElement) peekStack();
        skipValue();
        return element;
    }

    @Override // com.google.gson.stream.JsonReader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.stack = new Object[]{SENTINEL_CLOSED};
        this.stackSize = 1;
    }

    @Override // com.google.gson.stream.JsonReader
    public void skipValue() throws IOException {
        if (peek() == JsonToken.NAME) {
            nextName();
            this.pathNames[this.stackSize - 2] = FXMLLoader.NULL_KEYWORD;
        } else {
            popStack();
            if (this.stackSize > 0) {
                this.pathNames[this.stackSize - 1] = FXMLLoader.NULL_KEYWORD;
            }
        }
        if (this.stackSize > 0) {
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
        }
    }

    @Override // com.google.gson.stream.JsonReader
    public String toString() {
        return getClass().getSimpleName() + locationString();
    }

    public void promoteNameToValue() throws IOException {
        expect(JsonToken.NAME);
        Iterator<?> i2 = (Iterator) peekStack();
        Map.Entry<?, ?> entry = (Map.Entry) i2.next();
        push(entry.getValue());
        push(new JsonPrimitive((String) entry.getKey()));
    }

    private void push(Object newTop) {
        if (this.stackSize == this.stack.length) {
            int newLength = this.stackSize * 2;
            this.stack = Arrays.copyOf(this.stack, newLength);
            this.pathIndices = Arrays.copyOf(this.pathIndices, newLength);
            this.pathNames = (String[]) Arrays.copyOf(this.pathNames, newLength);
        }
        Object[] objArr = this.stack;
        int i2 = this.stackSize;
        this.stackSize = i2 + 1;
        objArr[i2] = newTop;
    }

    private String getPath(boolean usePreviousPath) {
        StringBuilder result = new StringBuilder().append('$');
        int i2 = 0;
        while (i2 < this.stackSize) {
            if (this.stack[i2] instanceof JsonArray) {
                i2++;
                if (i2 < this.stackSize && (this.stack[i2] instanceof Iterator)) {
                    int pathIndex = this.pathIndices[i2];
                    if (usePreviousPath && pathIndex > 0 && (i2 == this.stackSize - 1 || i2 == this.stackSize - 2)) {
                        pathIndex--;
                    }
                    result.append('[').append(pathIndex).append(']');
                }
            } else if (this.stack[i2] instanceof JsonObject) {
                i2++;
                if (i2 < this.stackSize && (this.stack[i2] instanceof Iterator)) {
                    result.append('.');
                    if (this.pathNames[i2] != null) {
                        result.append(this.pathNames[i2]);
                    }
                }
            }
            i2++;
        }
        return result.toString();
    }

    @Override // com.google.gson.stream.JsonReader
    public String getPreviousPath() {
        return getPath(true);
    }

    @Override // com.google.gson.stream.JsonReader
    public String getPath() {
        return getPath(false);
    }

    private String locationString() {
        return " at path " + getPath();
    }
}
