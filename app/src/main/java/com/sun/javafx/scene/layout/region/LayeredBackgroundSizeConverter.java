package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/LayeredBackgroundSizeConverter.class */
public final class LayeredBackgroundSizeConverter extends StyleConverterImpl<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]> {
    private static final LayeredBackgroundSizeConverter LAYERED_BACKGROUND_SIZE_CONVERTER = new LayeredBackgroundSizeConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]>) parsedValue, font);
    }

    public static LayeredBackgroundSizeConverter getInstance() {
        return LAYERED_BACKGROUND_SIZE_CONVERTER;
    }

    private LayeredBackgroundSizeConverter() {
    }

    @Override // javafx.css.StyleConverter
    public BackgroundSize[] convert(ParsedValue<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]> value, Font font) {
        ParsedValue<ParsedValue[], BackgroundSize>[] layers = value.getValue();
        BackgroundSize[] sizes = new BackgroundSize[layers.length];
        for (int l2 = 0; l2 < layers.length; l2++) {
            sizes[l2] = layers[l2].convert(font);
        }
        return sizes;
    }
}
