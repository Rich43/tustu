package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/StrokeBorderPaintConverter.class */
public class StrokeBorderPaintConverter extends StyleConverterImpl<ParsedValue<?, Paint>[], Paint[]> {
    private static final StrokeBorderPaintConverter STROKE_BORDER_PAINT_CONVERTER = new StrokeBorderPaintConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<?, Paint>[], Paint[]>) parsedValue, font);
    }

    public static StrokeBorderPaintConverter getInstance() {
        return STROKE_BORDER_PAINT_CONVERTER;
    }

    private StrokeBorderPaintConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Paint[] convert(ParsedValue<ParsedValue<?, Paint>[], Paint[]> value, Font font) {
        ParsedValue<?, Paint>[] borders = value.getValue();
        Paint[] paints = new Paint[4];
        paints[0] = borders.length > 0 ? borders[0].convert(font) : Color.BLACK;
        paints[1] = borders.length > 1 ? borders[1].convert(font) : paints[0];
        paints[2] = borders.length > 2 ? borders[2].convert(font) : paints[0];
        paints[3] = borders.length > 3 ? borders[3].convert(font) : paints[1];
        return paints;
    }

    public String toString() {
        return "StrokeBorderPaintConverter";
    }
}
