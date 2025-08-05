package javafx.util.converter;

import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/ByteStringConverter.class */
public class ByteStringConverter extends StringConverter<Byte> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Byte fromString(String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.length() < 1) {
            return null;
        }
        return Byte.valueOf(value2);
    }

    @Override // javafx.util.StringConverter
    public String toString(Byte value) {
        if (value == null) {
            return "";
        }
        return Byte.toString(value.byteValue());
    }
}
