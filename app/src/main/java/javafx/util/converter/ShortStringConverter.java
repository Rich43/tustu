package javafx.util.converter;

import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/ShortStringConverter.class */
public class ShortStringConverter extends StringConverter<Short> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Short fromString(String text) {
        if (text == null) {
            return null;
        }
        String text2 = text.trim();
        if (text2.length() < 1) {
            return null;
        }
        return Short.valueOf(text2);
    }

    @Override // javafx.util.StringConverter
    public String toString(Short value) {
        if (value == null) {
            return "";
        }
        return Short.toString(value.shortValue());
    }
}
