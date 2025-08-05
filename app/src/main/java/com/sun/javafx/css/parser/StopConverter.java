package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/parser/StopConverter.class */
public final class StopConverter extends StyleConverterImpl<ParsedValue[], Stop> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], Stop>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/parser/StopConverter$Holder.class */
    private static class Holder {
        static final StopConverter INSTANCE = new StopConverter();

        private Holder() {
        }
    }

    public static StopConverter getInstance() {
        return Holder.INSTANCE;
    }

    private StopConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Stop convert(ParsedValue<ParsedValue[], Stop> value, Font font) {
        ParsedValue[] values = value.getValue();
        Double offset = Double.valueOf(((Size) values[0].convert(font)).pixels(font));
        Color color = (Color) values[1].convert(font);
        return new Stop(offset.doubleValue(), color);
    }

    public String toString() {
        return "StopConverter";
    }
}
