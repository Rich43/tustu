package javafx.util.converter;

import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/DefaultStringConverter.class */
public class DefaultStringConverter extends StringConverter<String> {
    @Override // javafx.util.StringConverter
    public String toString(String value) {
        return value != null ? value : "";
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public String fromString(String value) {
        return value;
    }
}
