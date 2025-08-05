package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/ColorConverter.class */
public final class ColorConverter extends StyleConverterImpl<String, Color> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<String, Color>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/ColorConverter$Holder.class */
    private static class Holder {
        static final ColorConverter COLOR_INSTANCE = new ColorConverter();

        private Holder() {
        }
    }

    public static StyleConverter<String, Color> getInstance() {
        return Holder.COLOR_INSTANCE;
    }

    private ColorConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Color convert(ParsedValue<String, Color> value, Font font) {
        Object val = value.getValue();
        if (val == null) {
            return null;
        }
        if (val instanceof Color) {
            return (Color) val;
        }
        if (val instanceof String) {
            String str = (String) val;
            if (str.isEmpty() || FXMLLoader.NULL_KEYWORD.equals(str)) {
                return null;
            }
            try {
                return Color.web((String) val);
            } catch (IllegalArgumentException e2) {
            }
        }
        System.err.println("not a color: " + ((Object) value));
        return Color.BLACK;
    }

    public String toString() {
        return "ColorConverter";
    }
}
