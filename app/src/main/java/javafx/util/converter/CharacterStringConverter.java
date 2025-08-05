package javafx.util.converter;

import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/CharacterStringConverter.class */
public class CharacterStringConverter extends StringConverter<Character> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Character fromString(String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.length() < 1) {
            return null;
        }
        return Character.valueOf(value2.charAt(0));
    }

    @Override // javafx.util.StringConverter
    public String toString(Character value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}
