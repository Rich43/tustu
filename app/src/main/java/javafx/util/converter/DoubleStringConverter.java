package javafx.util.converter;

import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/DoubleStringConverter.class */
public class DoubleStringConverter extends StringConverter<Double> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Double fromString(String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.length() < 1) {
            return null;
        }
        return Double.valueOf(value2);
    }

    @Override // javafx.util.StringConverter
    public String toString(Double value) {
        if (value == null) {
            return "";
        }
        return Double.toString(value.doubleValue());
    }
}
