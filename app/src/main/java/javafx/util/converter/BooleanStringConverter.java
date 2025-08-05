package javafx.util.converter;

import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/BooleanStringConverter.class */
public class BooleanStringConverter extends StringConverter<Boolean> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Boolean fromString(String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.length() < 1) {
            return null;
        }
        return Boolean.valueOf(value2);
    }

    @Override // javafx.util.StringConverter
    public String toString(Boolean value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}
