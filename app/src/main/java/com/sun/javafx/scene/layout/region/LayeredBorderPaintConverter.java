package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/LayeredBorderPaintConverter.class */
public final class LayeredBorderPaintConverter extends StyleConverterImpl<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]> {
    private static final LayeredBorderPaintConverter LAYERED_BORDER_PAINT_CONVERTER = new LayeredBorderPaintConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]>) parsedValue, font);
    }

    public static LayeredBorderPaintConverter getInstance() {
        return LAYERED_BORDER_PAINT_CONVERTER;
    }

    private LayeredBorderPaintConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Paint[][] convert(ParsedValue<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]> value, Font font) {
        ParsedValue<ParsedValue<?, Paint>[], Paint[]>[] layers = value.getValue();
        Paint[][] paints = new Paint[layers.length][0];
        for (int layer = 0; layer < layers.length; layer++) {
            paints[layer] = StrokeBorderPaintConverter.getInstance().convert(layers[layer], font);
        }
        return paints;
    }

    public String toString() {
        return "LayeredBorderPaintConverter";
    }
}
