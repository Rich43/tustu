package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/BorderImageWidthConverter.class */
public class BorderImageWidthConverter extends StyleConverterImpl<ParsedValue[], BorderWidths> {
    private static final BorderImageWidthConverter CONVERTER_INSTANCE;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], BorderWidths>) parsedValue, font);
    }

    static {
        $assertionsDisabled = !BorderImageWidthConverter.class.desiredAssertionStatus();
        CONVERTER_INSTANCE = new BorderImageWidthConverter();
    }

    public static BorderImageWidthConverter getInstance() {
        return CONVERTER_INSTANCE;
    }

    private BorderImageWidthConverter() {
    }

    @Override // javafx.css.StyleConverter
    public BorderWidths convert(ParsedValue<ParsedValue[], BorderWidths> value, Font font) {
        double top;
        double right;
        double bottom;
        double left;
        ParsedValue[] sides = value.getValue();
        if (!$assertionsDisabled && sides.length != 4) {
            throw new AssertionError();
        }
        boolean topPercent = false;
        boolean rightPercent = false;
        boolean bottomPercent = false;
        boolean leftPercent = false;
        ParsedValue val = sides[0];
        if ("auto".equals(val.getValue())) {
            top = -1.0d;
        } else {
            Size size = (Size) val.convert(font);
            top = size.pixels(font);
            topPercent = size.getUnits() == SizeUnits.PERCENT;
        }
        ParsedValue val2 = sides[1];
        if ("auto".equals(val2.getValue())) {
            right = -1.0d;
        } else {
            Size size2 = (Size) val2.convert(font);
            right = size2.pixels(font);
            rightPercent = size2.getUnits() == SizeUnits.PERCENT;
        }
        ParsedValue val3 = sides[2];
        if ("auto".equals(val3.getValue())) {
            bottom = -1.0d;
        } else {
            Size size3 = (Size) val3.convert(font);
            bottom = size3.pixels(font);
            bottomPercent = size3.getUnits() == SizeUnits.PERCENT;
        }
        ParsedValue val4 = sides[3];
        if ("auto".equals(val4.getValue())) {
            left = -1.0d;
        } else {
            Size size4 = (Size) val4.convert(font);
            left = size4.pixels(font);
            leftPercent = size4.getUnits() == SizeUnits.PERCENT;
        }
        return new BorderWidths(top, right, bottom, left, topPercent, rightPercent, bottomPercent, leftPercent);
    }

    public String toString() {
        return "BorderImageWidthConverter";
    }
}
