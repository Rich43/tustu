package com.sun.javafx.css.parser;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/parser/LadderConverter.class */
public final class LadderConverter extends StyleConverterImpl<ParsedValue[], Color> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], Color>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/parser/LadderConverter$Holder.class */
    private static class Holder {
        static final LadderConverter INSTANCE = new LadderConverter();

        private Holder() {
        }
    }

    public static LadderConverter getInstance() {
        return Holder.INSTANCE;
    }

    private LadderConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Color convert(ParsedValue<ParsedValue[], Color> value, Font font) {
        ParsedValue[] values = value.getValue();
        Color color = (Color) values[0].convert(font);
        Stop[] stops = new Stop[values.length - 1];
        for (int v2 = 1; v2 < values.length; v2++) {
            stops[v2 - 1] = (Stop) values[v2].convert(font);
        }
        return Utils.ladder(color, stops);
    }

    public String toString() {
        return "LadderConverter";
    }
}
