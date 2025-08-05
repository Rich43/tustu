package com.google.gson;

import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.math.BigDecimal;

/* loaded from: gson-2.9.0.jar:com/google/gson/ToNumberPolicy.class */
public enum ToNumberPolicy implements ToNumberStrategy {
    DOUBLE { // from class: com.google.gson.ToNumberPolicy.1
        @Override // com.google.gson.ToNumberStrategy
        public Double readNumber(JsonReader in) throws IOException {
            return Double.valueOf(in.nextDouble());
        }
    },
    LAZILY_PARSED_NUMBER { // from class: com.google.gson.ToNumberPolicy.2
        @Override // com.google.gson.ToNumberStrategy
        public Number readNumber(JsonReader in) throws IOException {
            return new LazilyParsedNumber(in.nextString());
        }
    },
    LONG_OR_DOUBLE { // from class: com.google.gson.ToNumberPolicy.3
        @Override // com.google.gson.ToNumberStrategy
        public Number readNumber(JsonReader in) throws JsonParseException, IOException {
            String value = in.nextString();
            try {
                return Long.valueOf(Long.parseLong(value));
            } catch (NumberFormatException e2) {
                try {
                    Double d2 = Double.valueOf(value);
                    if ((d2.isInfinite() || d2.isNaN()) && !in.isLenient()) {
                        throw new MalformedJsonException("JSON forbids NaN and infinities: " + ((Object) d2) + "; at path " + in.getPreviousPath());
                    }
                    return d2;
                } catch (NumberFormatException doubleE) {
                    throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), doubleE);
                }
            }
        }
    },
    BIG_DECIMAL { // from class: com.google.gson.ToNumberPolicy.4
        @Override // com.google.gson.ToNumberStrategy
        public BigDecimal readNumber(JsonReader in) throws IOException {
            String value = in.nextString();
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e2) {
                throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), e2);
            }
        }
    }
}
