package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.converters.BooleanConverter;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/BackgroundSizeConverter.class */
public final class BackgroundSizeConverter extends StyleConverterImpl<ParsedValue[], BackgroundSize> {
    private static final BackgroundSizeConverter BACKGROUND_SIZE_CONVERTER = new BackgroundSizeConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], BackgroundSize>) parsedValue, font);
    }

    public static BackgroundSizeConverter getInstance() {
        return BACKGROUND_SIZE_CONVERTER;
    }

    private BackgroundSizeConverter() {
    }

    @Override // javafx.css.StyleConverter
    public BackgroundSize convert(ParsedValue<ParsedValue[], BackgroundSize> value, Font font) {
        ParsedValue[] values = value.getValue();
        Size wSize = values[0] != null ? (Size) values[0].convert(font) : null;
        Size hSize = values[1] != null ? (Size) values[1].convert(font) : null;
        boolean proportionalWidth = true;
        boolean proportionalHeight = true;
        if (wSize != null) {
            proportionalWidth = wSize.getUnits() == SizeUnits.PERCENT;
        }
        if (hSize != null) {
            proportionalHeight = hSize.getUnits() == SizeUnits.PERCENT;
        }
        double w2 = wSize != null ? wSize.pixels(font) : -1.0d;
        double h2 = hSize != null ? hSize.pixels(font) : -1.0d;
        boolean cover = values[2] != null ? BooleanConverter.getInstance().convert(values[2], font).booleanValue() : false;
        boolean contain = values[3] != null ? BooleanConverter.getInstance().convert(values[3], font).booleanValue() : false;
        return new BackgroundSize(w2, h2, proportionalWidth, proportionalHeight, contain, cover);
    }

    public String toString() {
        return "BackgroundSizeConverter";
    }
}
