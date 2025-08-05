package javafx.css;

import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:javafx/css/ParsedValue.class */
public class ParsedValue<V, T> {
    protected final V value;
    protected final StyleConverter<V, T> converter;

    public final V getValue() {
        return this.value;
    }

    public final StyleConverter<V, T> getConverter() {
        return this.converter;
    }

    public T convert(Font font) {
        return this.converter != null ? this.converter.convert(this, font) : this.value;
    }

    protected ParsedValue(V value, StyleConverter<V, T> converter) {
        this.value = value;
        this.converter = converter;
    }
}
