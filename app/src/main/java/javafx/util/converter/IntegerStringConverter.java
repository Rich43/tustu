package javafx.util.converter;

import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/IntegerStringConverter.class */
public class IntegerStringConverter extends StringConverter<Integer> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Integer fromString(String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.length() < 1) {
            return null;
        }
        return Integer.valueOf(value2);
    }

    @Override // javafx.util.StringConverter
    public String toString(Integer value) {
        if (value == null) {
            return "";
        }
        return Integer.toString(value.intValue());
    }
}
