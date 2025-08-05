package javafx.util.converter;

import java.math.BigInteger;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/BigIntegerStringConverter.class */
public class BigIntegerStringConverter extends StringConverter<BigInteger> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public BigInteger fromString(String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.length() < 1) {
            return null;
        }
        return new BigInteger(value2);
    }

    @Override // javafx.util.StringConverter
    public String toString(BigInteger value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}
