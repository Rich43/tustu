package javafx.util.converter;

import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/LongStringConverter.class */
public class LongStringConverter extends StringConverter<Long> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Long fromString(String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.length() < 1) {
            return null;
        }
        return Long.valueOf(value2);
    }

    @Override // javafx.util.StringConverter
    public String toString(Long value) {
        if (value == null) {
            return "";
        }
        return Long.toString(value.longValue());
    }
}
