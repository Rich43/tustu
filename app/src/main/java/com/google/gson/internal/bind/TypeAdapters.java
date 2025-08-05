package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import javafx.fxml.FXMLLoader;
import org.slf4j.Marker;

/* loaded from: gson-2.9.0.jar:com/google/gson/internal/bind/TypeAdapters.class */
public final class TypeAdapters {
    public static final TypeAdapter<Class> CLASS = new TypeAdapter<Class>() { // from class: com.google.gson.internal.bind.TypeAdapters.1
        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Class value) throws IOException {
            throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + value.getName() + ". Forgot to register a type adapter?");
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Class read2(JsonReader in) throws IOException {
            throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
        }
    }.nullSafe();
    public static final TypeAdapterFactory CLASS_FACTORY = newFactory(Class.class, CLASS);
    public static final TypeAdapter<BitSet> BIT_SET = new TypeAdapter<BitSet>() { // from class: com.google.gson.internal.bind.TypeAdapters.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public BitSet read2(JsonReader in) throws IOException, NumberFormatException {
            boolean set;
            BitSet bitset = new BitSet();
            in.beginArray();
            int i2 = 0;
            JsonToken jsonTokenPeek = in.peek();
            while (true) {
                JsonToken tokenType = jsonTokenPeek;
                if (tokenType != JsonToken.END_ARRAY) {
                    switch (AnonymousClass35.$SwitchMap$com$google$gson$stream$JsonToken[tokenType.ordinal()]) {
                        case 1:
                        case 2:
                            int intValue = in.nextInt();
                            if (intValue == 0) {
                                set = false;
                                break;
                            } else if (intValue == 1) {
                                set = true;
                                break;
                            } else {
                                throw new JsonSyntaxException("Invalid bitset value " + intValue + ", expected 0 or 1; at path " + in.getPreviousPath());
                            }
                        case 3:
                            set = in.nextBoolean();
                            break;
                        default:
                            throw new JsonSyntaxException("Invalid bitset value type: " + ((Object) tokenType) + "; at path " + in.getPath());
                    }
                    if (set) {
                        bitset.set(i2);
                    }
                    i2++;
                    jsonTokenPeek = in.peek();
                } else {
                    in.endArray();
                    return bitset;
                }
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, BitSet src) throws IOException {
            out.beginArray();
            int length = src.length();
            for (int i2 = 0; i2 < length; i2++) {
                int value = src.get(i2) ? 1 : 0;
                out.value(value);
            }
            out.endArray();
        }
    }.nullSafe();
    public static final TypeAdapterFactory BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
    public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() { // from class: com.google.gson.internal.bind.TypeAdapters.3
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Boolean read2(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            if (peek == JsonToken.STRING) {
                return Boolean.valueOf(Boolean.parseBoolean(in.nextString()));
            }
            return Boolean.valueOf(in.nextBoolean());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Boolean value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() { // from class: com.google.gson.internal.bind.TypeAdapters.4
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Boolean read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return Boolean.valueOf(in.nextString());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Boolean value) throws IOException {
            out.value(value == null ? FXMLLoader.NULL_KEYWORD : value.toString());
        }
    };
    public static final TypeAdapterFactory BOOLEAN_FACTORY = newFactory(Boolean.TYPE, Boolean.class, BOOLEAN);
    public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.5
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Number read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                int intValue = in.nextInt();
                if (intValue > 255 || intValue < -128) {
                    throw new JsonSyntaxException("Lossy conversion from " + intValue + " to byte; at path " + in.getPreviousPath());
                }
                return Byte.valueOf((byte) intValue);
            } catch (NumberFormatException e2) {
                throw new JsonSyntaxException(e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Number value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapterFactory BYTE_FACTORY = newFactory(Byte.TYPE, Byte.class, BYTE);
    public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.6
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Number read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                int intValue = in.nextInt();
                if (intValue > 65535 || intValue < -32768) {
                    throw new JsonSyntaxException("Lossy conversion from " + intValue + " to short; at path " + in.getPreviousPath());
                }
                return Short.valueOf((short) intValue);
            } catch (NumberFormatException e2) {
                throw new JsonSyntaxException(e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Number value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapterFactory SHORT_FACTORY = newFactory(Short.TYPE, Short.class, SHORT);
    public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.7
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Number read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                return Integer.valueOf(in.nextInt());
            } catch (NumberFormatException e2) {
                throw new JsonSyntaxException(e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Number value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapterFactory INTEGER_FACTORY = newFactory(Integer.TYPE, Integer.class, INTEGER);
    public static final TypeAdapter<AtomicInteger> ATOMIC_INTEGER = new TypeAdapter<AtomicInteger>() { // from class: com.google.gson.internal.bind.TypeAdapters.8
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public AtomicInteger read2(JsonReader in) throws IOException {
            try {
                return new AtomicInteger(in.nextInt());
            } catch (NumberFormatException e2) {
                throw new JsonSyntaxException(e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, AtomicInteger value) throws IOException {
            out.value(value.get());
        }
    }.nullSafe();
    public static final TypeAdapterFactory ATOMIC_INTEGER_FACTORY = newFactory(AtomicInteger.class, ATOMIC_INTEGER);
    public static final TypeAdapter<AtomicBoolean> ATOMIC_BOOLEAN = new TypeAdapter<AtomicBoolean>() { // from class: com.google.gson.internal.bind.TypeAdapters.9
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public AtomicBoolean read2(JsonReader in) throws IOException {
            return new AtomicBoolean(in.nextBoolean());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, AtomicBoolean value) throws IOException {
            out.value(value.get());
        }
    }.nullSafe();
    public static final TypeAdapterFactory ATOMIC_BOOLEAN_FACTORY = newFactory(AtomicBoolean.class, ATOMIC_BOOLEAN);
    public static final TypeAdapter<AtomicIntegerArray> ATOMIC_INTEGER_ARRAY = new TypeAdapter<AtomicIntegerArray>() { // from class: com.google.gson.internal.bind.TypeAdapters.10
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public AtomicIntegerArray read2(JsonReader in) throws IOException {
            List<Integer> list = new ArrayList<>();
            in.beginArray();
            while (in.hasNext()) {
                try {
                    int integer = in.nextInt();
                    list.add(Integer.valueOf(integer));
                } catch (NumberFormatException e2) {
                    throw new JsonSyntaxException(e2);
                }
            }
            in.endArray();
            int length = list.size();
            AtomicIntegerArray array = new AtomicIntegerArray(length);
            for (int i2 = 0; i2 < length; i2++) {
                array.set(i2, list.get(i2).intValue());
            }
            return array;
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, AtomicIntegerArray value) throws IOException {
            out.beginArray();
            int length = value.length();
            for (int i2 = 0; i2 < length; i2++) {
                out.value(value.get(i2));
            }
            out.endArray();
        }
    }.nullSafe();
    public static final TypeAdapterFactory ATOMIC_INTEGER_ARRAY_FACTORY = newFactory(AtomicIntegerArray.class, ATOMIC_INTEGER_ARRAY);
    public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.11
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Number read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                return Long.valueOf(in.nextLong());
            } catch (NumberFormatException e2) {
                throw new JsonSyntaxException(e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Number value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.12
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Number read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return Float.valueOf((float) in.nextDouble());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Number value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.13
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Number read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return Double.valueOf(in.nextDouble());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Number value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>() { // from class: com.google.gson.internal.bind.TypeAdapters.14
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Character read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String str = in.nextString();
            if (str.length() != 1) {
                throw new JsonSyntaxException("Expecting character, got: " + str + "; at " + in.getPreviousPath());
            }
            return Character.valueOf(str.charAt(0));
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Character value) throws IOException {
            out.value(value == null ? null : String.valueOf(value));
        }
    };
    public static final TypeAdapterFactory CHARACTER_FACTORY = newFactory(Character.TYPE, Character.class, CHARACTER);
    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() { // from class: com.google.gson.internal.bind.TypeAdapters.15
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public String read2(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            if (peek == JsonToken.BOOLEAN) {
                return Boolean.toString(in.nextBoolean());
            }
            return in.nextString();
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() { // from class: com.google.gson.internal.bind.TypeAdapters.16
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public BigDecimal read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String s2 = in.nextString();
            try {
                return new BigDecimal(s2);
            } catch (NumberFormatException e2) {
                throw new JsonSyntaxException("Failed parsing '" + s2 + "' as BigDecimal; at path " + in.getPreviousPath(), e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, BigDecimal value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>() { // from class: com.google.gson.internal.bind.TypeAdapters.17
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public BigInteger read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String s2 = in.nextString();
            try {
                return new BigInteger(s2);
            } catch (NumberFormatException e2) {
                throw new JsonSyntaxException("Failed parsing '" + s2 + "' as BigInteger; at path " + in.getPreviousPath(), e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, BigInteger value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapter<LazilyParsedNumber> LAZILY_PARSED_NUMBER = new TypeAdapter<LazilyParsedNumber>() { // from class: com.google.gson.internal.bind.TypeAdapters.18
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public LazilyParsedNumber read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return new LazilyParsedNumber(in.nextString());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, LazilyParsedNumber value) throws IOException {
            out.value(value);
        }
    };
    public static final TypeAdapterFactory STRING_FACTORY = newFactory(String.class, STRING);
    public static final TypeAdapter<StringBuilder> STRING_BUILDER = new TypeAdapter<StringBuilder>() { // from class: com.google.gson.internal.bind.TypeAdapters.19
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public StringBuilder read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return new StringBuilder(in.nextString());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, StringBuilder value) throws IOException {
            out.value(value == null ? null : value.toString());
        }
    };
    public static final TypeAdapterFactory STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
    public static final TypeAdapter<StringBuffer> STRING_BUFFER = new TypeAdapter<StringBuffer>() { // from class: com.google.gson.internal.bind.TypeAdapters.20
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public StringBuffer read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return new StringBuffer(in.nextString());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, StringBuffer value) throws IOException {
            out.value(value == null ? null : value.toString());
        }
    };
    public static final TypeAdapterFactory STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
    public static final TypeAdapter<URL> URL = new TypeAdapter<URL>() { // from class: com.google.gson.internal.bind.TypeAdapters.21
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public URL read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String nextString = in.nextString();
            if (FXMLLoader.NULL_KEYWORD.equals(nextString)) {
                return null;
            }
            return new URL(nextString);
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, URL value) throws IOException {
            out.value(value == null ? null : value.toExternalForm());
        }
    };
    public static final TypeAdapterFactory URL_FACTORY = newFactory(URL.class, URL);
    public static final TypeAdapter<URI> URI = new TypeAdapter<URI>() { // from class: com.google.gson.internal.bind.TypeAdapters.22
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public URI read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String nextString = in.nextString();
                if (FXMLLoader.NULL_KEYWORD.equals(nextString)) {
                    return null;
                }
                return new URI(nextString);
            } catch (URISyntaxException e2) {
                throw new JsonIOException(e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, URI value) throws IOException {
            out.value(value == null ? null : value.toASCIIString());
        }
    };
    public static final TypeAdapterFactory URI_FACTORY = newFactory(URI.class, URI);
    public static final TypeAdapter<InetAddress> INET_ADDRESS = new TypeAdapter<InetAddress>() { // from class: com.google.gson.internal.bind.TypeAdapters.23
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public InetAddress read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return InetAddress.getByName(in.nextString());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, InetAddress value) throws IOException {
            out.value(value == null ? null : value.getHostAddress());
        }
    };
    public static final TypeAdapterFactory INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
    public static final TypeAdapter<UUID> UUID = new TypeAdapter<UUID>() { // from class: com.google.gson.internal.bind.TypeAdapters.24
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public UUID read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String s2 = in.nextString();
            try {
                return UUID.fromString(s2);
            } catch (IllegalArgumentException e2) {
                throw new JsonSyntaxException("Failed parsing '" + s2 + "' as UUID; at path " + in.getPreviousPath(), e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, UUID value) throws IOException {
            out.value(value == null ? null : value.toString());
        }
    };
    public static final TypeAdapterFactory UUID_FACTORY = newFactory(UUID.class, UUID);
    public static final TypeAdapter<Currency> CURRENCY = new TypeAdapter<Currency>() { // from class: com.google.gson.internal.bind.TypeAdapters.25
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Currency read2(JsonReader in) throws IOException {
            String s2 = in.nextString();
            try {
                return Currency.getInstance(s2);
            } catch (IllegalArgumentException e2) {
                throw new JsonSyntaxException("Failed parsing '" + s2 + "' as Currency; at path " + in.getPreviousPath(), e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Currency value) throws IOException {
            out.value(value.getCurrencyCode());
        }
    }.nullSafe();
    public static final TypeAdapterFactory CURRENCY_FACTORY = newFactory(Currency.class, CURRENCY);
    public static final TypeAdapter<Calendar> CALENDAR = new TypeAdapter<Calendar>() { // from class: com.google.gson.internal.bind.TypeAdapters.26
        private static final String YEAR = "year";
        private static final String MONTH = "month";
        private static final String DAY_OF_MONTH = "dayOfMonth";
        private static final String HOUR_OF_DAY = "hourOfDay";
        private static final String MINUTE = "minute";
        private static final String SECOND = "second";

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Calendar read2(JsonReader in) throws IOException, NumberFormatException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            in.beginObject();
            int year = 0;
            int month = 0;
            int dayOfMonth = 0;
            int hourOfDay = 0;
            int minute = 0;
            int second = 0;
            while (in.peek() != JsonToken.END_OBJECT) {
                String name = in.nextName();
                int value = in.nextInt();
                if ("year".equals(name)) {
                    year = value;
                } else if (MONTH.equals(name)) {
                    month = value;
                } else if (DAY_OF_MONTH.equals(name)) {
                    dayOfMonth = value;
                } else if (HOUR_OF_DAY.equals(name)) {
                    hourOfDay = value;
                } else if (MINUTE.equals(name)) {
                    minute = value;
                } else if ("second".equals(name)) {
                    second = value;
                }
            }
            in.endObject();
            return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Calendar value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name("year");
            out.value(value.get(1));
            out.name(MONTH);
            out.value(value.get(2));
            out.name(DAY_OF_MONTH);
            out.value(value.get(5));
            out.name(HOUR_OF_DAY);
            out.value(value.get(11));
            out.name(MINUTE);
            out.value(value.get(12));
            out.name("second");
            out.value(value.get(13));
            out.endObject();
        }
    };
    public static final TypeAdapterFactory CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, CALENDAR);
    public static final TypeAdapter<Locale> LOCALE = new TypeAdapter<Locale>() { // from class: com.google.gson.internal.bind.TypeAdapters.27
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public Locale read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String locale = in.nextString();
            StringTokenizer tokenizer = new StringTokenizer(locale, "_");
            String language = null;
            String country = null;
            String variant = null;
            if (tokenizer.hasMoreElements()) {
                language = tokenizer.nextToken();
            }
            if (tokenizer.hasMoreElements()) {
                country = tokenizer.nextToken();
            }
            if (tokenizer.hasMoreElements()) {
                variant = tokenizer.nextToken();
            }
            if (country == null && variant == null) {
                return new Locale(language);
            }
            if (variant == null) {
                return new Locale(language, country);
            }
            return new Locale(language, country, variant);
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Locale value) throws IOException {
            out.value(value == null ? null : value.toString());
        }
    };
    public static final TypeAdapterFactory LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
    public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter<JsonElement>() { // from class: com.google.gson.internal.bind.TypeAdapters.28
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public JsonElement read2(JsonReader in) throws IOException {
            if (in instanceof JsonTreeReader) {
                return ((JsonTreeReader) in).nextJsonElement();
            }
            switch (AnonymousClass35.$SwitchMap$com$google$gson$stream$JsonToken[in.peek().ordinal()]) {
                case 1:
                    String number = in.nextString();
                    return new JsonPrimitive(new LazilyParsedNumber(number));
                case 2:
                    return new JsonPrimitive(in.nextString());
                case 3:
                    return new JsonPrimitive(Boolean.valueOf(in.nextBoolean()));
                case 4:
                    in.nextNull();
                    return JsonNull.INSTANCE;
                case 5:
                    JsonArray array = new JsonArray();
                    in.beginArray();
                    while (in.hasNext()) {
                        array.add(read2(in));
                    }
                    in.endArray();
                    return array;
                case 6:
                    JsonObject object = new JsonObject();
                    in.beginObject();
                    while (in.hasNext()) {
                        object.add(in.nextName(), read2(in));
                    }
                    in.endObject();
                    return object;
                case 7:
                case 8:
                case 9:
                case 10:
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, JsonElement value) throws IOException {
            if (value == null || value.isJsonNull()) {
                out.nullValue();
                return;
            }
            if (value.isJsonPrimitive()) {
                JsonPrimitive primitive = value.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    out.value(primitive.getAsNumber());
                    return;
                } else if (primitive.isBoolean()) {
                    out.value(primitive.getAsBoolean());
                    return;
                } else {
                    out.value(primitive.getAsString());
                    return;
                }
            }
            if (value.isJsonArray()) {
                out.beginArray();
                Iterator<JsonElement> it = value.getAsJsonArray().iterator();
                while (it.hasNext()) {
                    write(out, it.next());
                }
                out.endArray();
                return;
            }
            if (value.isJsonObject()) {
                out.beginObject();
                for (Map.Entry<String, JsonElement> e2 : value.getAsJsonObject().entrySet()) {
                    out.name(e2.getKey());
                    write(out, e2.getValue());
                }
                out.endObject();
                return;
            }
            throw new IllegalArgumentException("Couldn't write " + ((Object) value.getClass()));
        }
    };
    public static final TypeAdapterFactory JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
    public static final TypeAdapterFactory ENUM_FACTORY = new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.TypeAdapters.29
        @Override // com.google.gson.TypeAdapterFactory
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
                return null;
            }
            if (!rawType.isEnum()) {
                rawType = rawType.getSuperclass();
            }
            return new EnumTypeAdapter(rawType);
        }
    };

    private TypeAdapters() {
        throw new UnsupportedOperationException();
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters$35, reason: invalid class name */
    /* loaded from: gson-2.9.0.jar:com/google/gson/internal/bind/TypeAdapters$35.class */
    static /* synthetic */ class AnonymousClass35 {
        static final /* synthetic */ int[] $SwitchMap$com$google$gson$stream$JsonToken = new int[JsonToken.values().length];

        static {
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NUMBER.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.STRING.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BOOLEAN.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NULL.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_ARRAY.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_OBJECT.ordinal()] = 6;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_DOCUMENT.ordinal()] = 7;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NAME.ordinal()] = 8;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_ARRAY.ordinal()] = 10;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    /* loaded from: gson-2.9.0.jar:com/google/gson/internal/bind/TypeAdapters$EnumTypeAdapter.class */
    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private final Map<String, T> nameToConstant = new HashMap();
        private final Map<T, String> constantToName = new HashMap();

        public EnumTypeAdapter(final Class<T> cls) {
            try {
                for (Field field : (Field[]) AccessController.doPrivileged(new PrivilegedAction<Field[]>() { // from class: com.google.gson.internal.bind.TypeAdapters.EnumTypeAdapter.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Field[] run2() throws SecurityException {
                        Field[] fields = cls.getDeclaredFields();
                        ArrayList<Field> constantFieldsList = new ArrayList<>(fields.length);
                        for (Field f2 : fields) {
                            if (f2.isEnumConstant()) {
                                constantFieldsList.add(f2);
                            }
                        }
                        Field[] constantFields = (Field[]) constantFieldsList.toArray(new Field[0]);
                        AccessibleObject.setAccessible(constantFields, true);
                        return constantFields;
                    }
                })) {
                    Enum r0 = (Enum) field.get(null);
                    String strName = r0.name();
                    SerializedName serializedName = (SerializedName) field.getAnnotation(SerializedName.class);
                    if (serializedName != null) {
                        strName = serializedName.value();
                        for (String str : serializedName.alternate()) {
                            this.nameToConstant.put(str, r0);
                        }
                    }
                    this.nameToConstant.put(strName, r0);
                    this.constantToName.put(r0, strName);
                }
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public T read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return this.nameToConstant.get(in.nextString());
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, T value) throws IOException {
            out.value(value == null ? null : this.constantToName.get(value));
        }
    }

    public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.TypeAdapters.30
            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.equals(type)) {
                    return typeAdapter;
                }
                return null;
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.TypeAdapters.31
            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.getRawType() == type) {
                    return typeAdapter;
                }
                return null;
            }

            public String toString() {
                return "Factory[type=" + type.getName() + ",adapter=" + ((Object) typeAdapter) + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.TypeAdapters.32
            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                if (rawType == unboxed || rawType == boxed) {
                    return typeAdapter;
                }
                return null;
            }

            public String toString() {
                return "Factory[type=" + boxed.getName() + Marker.ANY_NON_NULL_MARKER + unboxed.getName() + ",adapter=" + ((Object) typeAdapter) + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> base, final Class<? extends TT> sub, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.TypeAdapters.33
            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                if (rawType == base || rawType == sub) {
                    return typeAdapter;
                }
                return null;
            }

            public String toString() {
                return "Factory[type=" + base.getName() + Marker.ANY_NON_NULL_MARKER + sub.getName() + ",adapter=" + ((Object) typeAdapter) + "]";
            }
        };
    }

    public static <T1> TypeAdapterFactory newTypeHierarchyFactory(final Class<T1> clazz, final TypeAdapter<T1> typeAdapter) {
        return new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.TypeAdapters.34
            @Override // com.google.gson.TypeAdapterFactory
            public <T2> TypeAdapter<T2> create(Gson gson, TypeToken<T2> typeToken) {
                final Class<? super T2> rawType = typeToken.getRawType();
                if (!clazz.isAssignableFrom(rawType)) {
                    return null;
                }
                return (TypeAdapter<T2>) new TypeAdapter<T1>() { // from class: com.google.gson.internal.bind.TypeAdapters.34.1
                    @Override // com.google.gson.TypeAdapter
                    public void write(JsonWriter out, T1 value) throws IOException {
                        typeAdapter.write(out, value);
                    }

                    @Override // com.google.gson.TypeAdapter
                    /* renamed from: read */
                    public T1 read2(JsonReader jsonReader) throws IOException {
                        T1 t1 = (T1) typeAdapter.read2(jsonReader);
                        if (t1 != null && !rawType.isInstance(t1)) {
                            throw new JsonSyntaxException("Expected a " + rawType.getName() + " but was " + t1.getClass().getName() + "; at path " + jsonReader.getPreviousPath());
                        }
                        return t1;
                    }
                };
            }

            public String toString() {
                return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + ((Object) typeAdapter) + "]";
            }
        };
    }
}
