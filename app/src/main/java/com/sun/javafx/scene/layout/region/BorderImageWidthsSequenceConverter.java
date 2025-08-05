package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/BorderImageWidthsSequenceConverter.class */
public class BorderImageWidthsSequenceConverter extends StyleConverterImpl<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]> {
    private static final BorderImageWidthsSequenceConverter CONVERTER = new BorderImageWidthsSequenceConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]>) parsedValue, font);
    }

    public static BorderImageWidthsSequenceConverter getInstance() {
        return CONVERTER;
    }

    @Override // javafx.css.StyleConverter
    public BorderWidths[] convert(ParsedValue<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]> value, Font font) {
        ParsedValue<ParsedValue[], BorderWidths>[] layers = value.getValue();
        BorderWidths[] widths = new BorderWidths[layers.length];
        for (int l2 = 0; l2 < layers.length; l2++) {
            widths[l2] = BorderImageWidthConverter.getInstance().convert(layers[l2], font);
        }
        return widths;
    }

    public String toString() {
        return "BorderImageWidthsSequenceConverter";
    }
}
