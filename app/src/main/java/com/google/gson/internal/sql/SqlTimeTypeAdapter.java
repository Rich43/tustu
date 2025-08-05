package com.google.gson.internal.sql;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: gson-2.9.0.jar:com/google/gson/internal/sql/SqlTimeTypeAdapter.class */
final class SqlTimeTypeAdapter extends TypeAdapter<Time> {
    static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() { // from class: com.google.gson.internal.sql.SqlTimeTypeAdapter.1
        @Override // com.google.gson.TypeAdapterFactory
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == Time.class) {
                return new SqlTimeTypeAdapter();
            }
            return null;
        }
    };
    private final DateFormat format;

    private SqlTimeTypeAdapter() {
        this.format = new SimpleDateFormat("hh:mm:ss a");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.TypeAdapter
    /* renamed from: read */
    public Time read2(JsonReader in) throws IOException {
        Time time;
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String s2 = in.nextString();
        try {
            synchronized (this) {
                Date date = this.format.parse(s2);
                time = new Time(date.getTime());
            }
            return time;
        } catch (ParseException e2) {
            throw new JsonSyntaxException("Failed parsing '" + s2 + "' as SQL Time; at path " + in.getPreviousPath(), e2);
        }
    }

    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter out, Time value) throws IOException {
        String timeString;
        if (value == null) {
            out.nullValue();
            return;
        }
        synchronized (this) {
            timeString = this.format.format((Date) value);
        }
        out.value(timeString);
    }
}
