package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.Cursor;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/CursorConverter.class */
public final class CursorConverter extends StyleConverterImpl<String, Cursor> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<String, Cursor>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/CursorConverter$Holder.class */
    private static class Holder {
        static final CursorConverter INSTANCE = new CursorConverter();

        private Holder() {
        }
    }

    public static StyleConverter<String, Cursor> getInstance() {
        return Holder.INSTANCE;
    }

    private CursorConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Cursor convert(ParsedValue<String, Cursor> value, Font not_used) {
        String string = value.getValue();
        if (string != null) {
            int index = string.indexOf("Cursor.");
            if (index > -1) {
                string = string.substring(index + "Cursor.".length());
            }
            string = string.replace('-', '_').toUpperCase();
        }
        try {
            return Cursor.cursor(string);
        } catch (IllegalArgumentException | NullPointerException e2) {
            return Cursor.DEFAULT;
        }
    }

    public String toString() {
        return "CursorConverter";
    }
}
