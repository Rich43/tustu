package javafx.util.converter;

import java.math.BigDecimal;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/BigDecimalStringConverter.class */
public class BigDecimalStringConverter extends StringConverter<BigDecimal> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public BigDecimal fromString(String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.length() < 1) {
            return null;
        }
        return new BigDecimal(value2);
    }

    @Override // javafx.util.StringConverter
    public String toString(BigDecimal value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}
