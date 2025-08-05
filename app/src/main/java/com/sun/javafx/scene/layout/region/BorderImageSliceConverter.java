package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/BorderImageSliceConverter.class */
public final class BorderImageSliceConverter extends StyleConverterImpl<ParsedValue[], BorderImageSlices> {
    private static final BorderImageSliceConverter BORDER_IMAGE_SLICE_CONVERTER = new BorderImageSliceConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], BorderImageSlices>) parsedValue, font);
    }

    public static BorderImageSliceConverter getInstance() {
        return BORDER_IMAGE_SLICE_CONVERTER;
    }

    private BorderImageSliceConverter() {
    }

    @Override // javafx.css.StyleConverter
    public BorderImageSlices convert(ParsedValue<ParsedValue[], BorderImageSlices> layer, Font font) {
        ParsedValue[] values = layer.getValue();
        ParsedValue<?, Size>[] sizes = (ParsedValue[]) values[0].getValue();
        Size topSz = sizes[0].convert(font);
        Size rightSz = sizes[1].convert(font);
        Size bottomSz = sizes[2].convert(font);
        Size leftSz = sizes[3].convert(font);
        return new BorderImageSlices(new BorderWidths(topSz.pixels(font), rightSz.pixels(font), bottomSz.pixels(font), leftSz.pixels(font), topSz.getUnits() == SizeUnits.PERCENT, rightSz.getUnits() == SizeUnits.PERCENT, bottomSz.getUnits() == SizeUnits.PERCENT, leftSz.getUnits() == SizeUnits.PERCENT), ((Boolean) values[1].getValue()).booleanValue());
    }

    public String toString() {
        return "BorderImageSliceConverter";
    }
}
