package com.google.gson;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: gson-2.9.0.jar:com/google/gson/JsonStreamParser.class */
public final class JsonStreamParser implements Iterator<JsonElement> {
    private final JsonReader parser;
    private final Object lock;

    public JsonStreamParser(String json) {
        this(new StringReader(json));
    }

    public JsonStreamParser(Reader reader) {
        this.parser = new JsonReader(reader);
        this.parser.setLenient(true);
        this.lock = new Object();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public JsonElement next() throws JsonParseException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return Streams.parse(this.parser);
        } catch (JsonParseException e2) {
            if (e2.getCause() instanceof EOFException) {
                throw new NoSuchElementException();
            }
            throw e2;
        } catch (OutOfMemoryError e3) {
            throw new JsonParseException("Failed parsing JSON source to Json", e3);
        } catch (StackOverflowError e4) {
            throw new JsonParseException("Failed parsing JSON source to Json", e4);
        }
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        boolean z2;
        synchronized (this.lock) {
            try {
                try {
                    z2 = this.parser.peek() != JsonToken.END_DOCUMENT;
                } catch (MalformedJsonException e2) {
                    throw new JsonSyntaxException(e2);
                }
            } catch (IOException e3) {
                throw new JsonIOException(e3);
            }
        }
        return z2;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
