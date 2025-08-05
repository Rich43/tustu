package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/parser/DeriveColorConverter.class */
public final class DeriveColorConverter extends StyleConverterImpl<ParsedValue[], Color> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], Color>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/parser/DeriveColorConverter$Holder.class */
    private static class Holder {
        static final DeriveColorConverter INSTANCE = new DeriveColorConverter();

        private Holder() {
        }
    }

    public static DeriveColorConverter getInstance() {
        return Holder.INSTANCE;
    }

    private DeriveColorConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Color convert(ParsedValue<ParsedValue[], Color> value, Font font) {
        ParsedValue[] values = value.getValue();
        Color color = (Color) values[0].convert(font);
        Size brightness = (Size) values[1].convert(font);
        return Utils.deriveColor(color, brightness.pixels(font));
    }

    public String toString() {
        return "DeriveColorConverter";
    }
}
