package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/LayeredBackgroundPositionConverter.class */
public final class LayeredBackgroundPositionConverter extends StyleConverterImpl<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]> {
    private static final LayeredBackgroundPositionConverter LAYERED_BACKGROUND_POSITION_CONVERTER = new LayeredBackgroundPositionConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]>) parsedValue, font);
    }

    public static LayeredBackgroundPositionConverter getInstance() {
        return LAYERED_BACKGROUND_POSITION_CONVERTER;
    }

    private LayeredBackgroundPositionConverter() {
    }

    @Override // javafx.css.StyleConverter
    public BackgroundPosition[] convert(ParsedValue<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]> value, Font font) {
        ParsedValue<ParsedValue[], BackgroundPosition>[] layers = value.getValue();
        BackgroundPosition[] positions = new BackgroundPosition[layers.length];
        for (int l2 = 0; l2 < layers.length; l2++) {
            positions[l2] = layers[l2].convert(font);
        }
        return positions;
    }

    public String toString() {
        return "LayeredBackgroundPositionConverter";
    }
}
