package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/SliceSequenceConverter.class */
public final class SliceSequenceConverter extends StyleConverterImpl<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]> {
    private static final SliceSequenceConverter BORDER_IMAGE_SLICE_SEQUENCE_CONVERTER = new SliceSequenceConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]>) parsedValue, font);
    }

    public static SliceSequenceConverter getInstance() {
        return BORDER_IMAGE_SLICE_SEQUENCE_CONVERTER;
    }

    @Override // javafx.css.StyleConverter
    public BorderImageSlices[] convert(ParsedValue<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]> value, Font font) {
        ParsedValue<ParsedValue[], BorderImageSlices>[] layers = value.getValue();
        BorderImageSlices[] borderImageSlices = new BorderImageSlices[layers.length];
        for (int l2 = 0; l2 < layers.length; l2++) {
            borderImageSlices[l2] = BorderImageSliceConverter.getInstance().convert(layers[l2], font);
        }
        return borderImageSlices;
    }

    public String toString() {
        return "BorderImageSliceSequenceConverter";
    }
}
