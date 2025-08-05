package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.CornerRadii;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/CornerRadiiConverter.class */
public final class CornerRadiiConverter extends StyleConverterImpl<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]> {
    private static final CornerRadiiConverter INSTANCE = new CornerRadiiConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]>) parsedValue, font);
    }

    public static CornerRadiiConverter getInstance() {
        return INSTANCE;
    }

    private CornerRadiiConverter() {
    }

    @Override // javafx.css.StyleConverter
    public CornerRadii[] convert(ParsedValue<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]> value, Font font) {
        ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[] values = value.getValue();
        CornerRadii[] cornerRadiiValues = new CornerRadii[values.length];
        for (int n2 = 0; n2 < values.length; n2++) {
            ParsedValue<?, Size>[][] sizes = values[n2].getValue();
            Size topLeftHorizontalRadius = sizes[0][0].convert(font);
            Size topRightHorizontalRadius = sizes[0][1].convert(font);
            Size bottomRightHorizontalRadius = sizes[0][2].convert(font);
            Size bottomLeftHorizontalRadius = sizes[0][3].convert(font);
            Size topLeftVerticalRadius = sizes[1][0].convert(font);
            Size topRightVerticalRadius = sizes[1][1].convert(font);
            Size bottomRightVerticalRadius = sizes[1][2].convert(font);
            Size bottomLeftVerticalRadius = sizes[1][3].convert(font);
            cornerRadiiValues[n2] = new CornerRadii(topLeftHorizontalRadius.pixels(font), topLeftVerticalRadius.pixels(font), topRightVerticalRadius.pixels(font), topRightHorizontalRadius.pixels(font), bottomRightHorizontalRadius.pixels(font), bottomRightVerticalRadius.pixels(font), bottomLeftVerticalRadius.pixels(font), bottomLeftHorizontalRadius.pixels(font), topLeftHorizontalRadius.getUnits() == SizeUnits.PERCENT, topLeftVerticalRadius.getUnits() == SizeUnits.PERCENT, topRightVerticalRadius.getUnits() == SizeUnits.PERCENT, topRightHorizontalRadius.getUnits() == SizeUnits.PERCENT, bottomRightHorizontalRadius.getUnits() == SizeUnits.PERCENT, bottomRightVerticalRadius.getUnits() == SizeUnits.PERCENT, bottomRightVerticalRadius.getUnits() == SizeUnits.PERCENT, bottomLeftHorizontalRadius.getUnits() == SizeUnits.PERCENT);
        }
        return cornerRadiiValues;
    }
}
