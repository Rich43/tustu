package com.google.gson;

import com.google.gson.stream.JsonReader;
import java.io.IOException;

/* loaded from: gson-2.9.0.jar:com/google/gson/ToNumberStrategy.class */
public interface ToNumberStrategy {
    Number readNumber(JsonReader jsonReader) throws IOException;
}
