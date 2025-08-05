package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.geometry.Side;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/BackgroundPositionConverter.class */
public final class BackgroundPositionConverter extends StyleConverterImpl<ParsedValue[], BackgroundPosition> {
    private static final BackgroundPositionConverter BACKGROUND_POSITION_CONVERTER = new BackgroundPositionConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], BackgroundPosition>) parsedValue, font);
    }

    public static BackgroundPositionConverter getInstance() {
        return BACKGROUND_POSITION_CONVERTER;
    }

    private BackgroundPositionConverter() {
    }

    @Override // javafx.css.StyleConverter
    public BackgroundPosition convert(ParsedValue<ParsedValue[], BackgroundPosition> value, Font font) {
        ParsedValue[] positions = value.getValue();
        Size top = (Size) positions[0].convert(font);
        Size right = (Size) positions[1].convert(font);
        Size bottom = (Size) positions[2].convert(font);
        Size left = (Size) positions[3].convert(font);
        boolean verticalEdgeProportional = (bottom.getValue() > 0.0d && bottom.getUnits() == SizeUnits.PERCENT) || (top.getValue() > 0.0d && top.getUnits() == SizeUnits.PERCENT) || (top.getValue() == 0.0d && bottom.getValue() == 0.0d);
        boolean horizontalEdgeProportional = (right.getValue() > 0.0d && right.getUnits() == SizeUnits.PERCENT) || (left.getValue() > 0.0d && left.getUnits() == SizeUnits.PERCENT) || (left.getValue() == 0.0d && right.getValue() == 0.0d);
        double t2 = top.pixels(font);
        double r2 = right.pixels(font);
        double b2 = bottom.pixels(font);
        double l2 = left.pixels(font);
        return new BackgroundPosition((l2 != 0.0d || r2 == 0.0d) ? Side.LEFT : Side.RIGHT, (l2 != 0.0d || r2 == 0.0d) ? l2 : r2, horizontalEdgeProportional, (t2 != 0.0d || b2 == 0.0d) ? Side.TOP : Side.BOTTOM, (t2 != 0.0d || b2 == 0.0d) ? t2 : b2, verticalEdgeProportional);
    }

    public String toString() {
        return "BackgroundPositionConverter";
    }
}
