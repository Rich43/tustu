package javafx.util.converter;

import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/FloatStringConverter.class */
public class FloatStringConverter extends StringConverter<Float> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Float fromString(String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.length() < 1) {
            return null;
        }
        return Float.valueOf(value2);
    }

    @Override // javafx.util.StringConverter
    public String toString(Float value) {
        if (value == null) {
            return "";
        }
        return Float.toString(value.floatValue());
    }
}
