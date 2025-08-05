package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/LayeredBorderStyleConverter.class */
public final class LayeredBorderStyleConverter extends StyleConverterImpl<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]> {
    private static final LayeredBorderStyleConverter LAYERED_BORDER_STYLE_CONVERTER = new LayeredBorderStyleConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]>) parsedValue, font);
    }

    public static LayeredBorderStyleConverter getInstance() {
        return LAYERED_BORDER_STYLE_CONVERTER;
    }

    private LayeredBorderStyleConverter() {
    }

    @Override // javafx.css.StyleConverter
    public BorderStrokeStyle[][] convert(ParsedValue<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]> value, Font font) {
        ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[] layers = value.getValue();
        BorderStrokeStyle[][] styles = new BorderStrokeStyle[layers.length][0];
        for (int layer = 0; layer < layers.length; layer++) {
            styles[layer] = layers[layer].convert(font);
        }
        return styles;
    }

    public String toString() {
        return "LayeredBorderStyleConverter";
    }
}
