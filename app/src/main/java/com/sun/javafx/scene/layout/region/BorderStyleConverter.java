package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.ParsedValueImpl;
import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/BorderStyleConverter.class */
public class BorderStyleConverter extends StyleConverterImpl<ParsedValue[], BorderStrokeStyle> {
    public static final ParsedValueImpl<ParsedValue[], Number[]> NONE = new ParsedValueImpl<>(null, null);
    public static final ParsedValueImpl<ParsedValue[], Number[]> HIDDEN = new ParsedValueImpl<>(null, null);
    public static final ParsedValueImpl<ParsedValue[], Number[]> DOTTED = new ParsedValueImpl<>(null, null);
    public static final ParsedValueImpl<ParsedValue[], Number[]> DASHED = new ParsedValueImpl<>(null, null);
    public static final ParsedValueImpl<ParsedValue[], Number[]> SOLID = new ParsedValueImpl<>(null, null);
    private static final BorderStyleConverter BORDER_STYLE_CONVERTER = new BorderStyleConverter();

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], BorderStrokeStyle>) parsedValue, font);
    }

    public static BorderStyleConverter getInstance() {
        return BORDER_STYLE_CONVERTER;
    }

    private BorderStyleConverter() {
    }

    @Override // javafx.css.StyleConverter
    public BorderStrokeStyle convert(ParsedValue<ParsedValue[], BorderStrokeStyle> value, Font font) {
        List<Double> dashes;
        StrokeLineCap strokeLineCap;
        ParsedValue[] values = value.getValue();
        ParsedValue parsedValue = values[0];
        boolean onlyNamed = values[1] == null && values[2] == null && values[3] == null && values[4] == null && values[5] == null;
        if (NONE == parsedValue) {
            return BorderStrokeStyle.NONE;
        }
        if (DOTTED == parsedValue && onlyNamed) {
            return BorderStrokeStyle.DOTTED;
        }
        if (DASHED == parsedValue && onlyNamed) {
            return BorderStrokeStyle.DASHED;
        }
        if (SOLID == parsedValue && onlyNamed) {
            return BorderStrokeStyle.SOLID;
        }
        ParsedValue<?, Size>[] dash_vals = (ParsedValue[]) values[0].getValue();
        if (dash_vals == null) {
            if (DOTTED == parsedValue) {
                dashes = BorderStrokeStyle.DOTTED.getDashArray();
            } else if (DASHED == parsedValue) {
                dashes = BorderStrokeStyle.DASHED.getDashArray();
            } else if (SOLID == parsedValue) {
                dashes = BorderStrokeStyle.SOLID.getDashArray();
            } else {
                dashes = Collections.emptyList();
            }
        } else {
            dashes = new ArrayList<>(dash_vals.length);
            for (ParsedValue<?, Size> parsedValue2 : dash_vals) {
                Size size = parsedValue2.convert(font);
                dashes.add(Double.valueOf(size.pixels(font)));
            }
        }
        double dash_phase = values[1] != null ? ((Double) values[1].convert(font)).doubleValue() : 0.0d;
        StrokeType stroke_type = values[2] != null ? (StrokeType) values[2].convert(font) : StrokeType.INSIDE;
        StrokeLineJoin line_join = values[3] != null ? (StrokeLineJoin) values[3].convert(font) : StrokeLineJoin.MITER;
        double miter_limit = values[4] != null ? ((Double) values[4].convert(font)).doubleValue() : 10.0d;
        if (values[5] != null) {
            strokeLineCap = (StrokeLineCap) values[5].convert(font);
        } else {
            strokeLineCap = DOTTED == parsedValue ? StrokeLineCap.ROUND : StrokeLineCap.BUTT;
        }
        StrokeLineCap line_cap = strokeLineCap;
        BorderStrokeStyle borderStyle = new BorderStrokeStyle(stroke_type, line_join, line_cap, miter_limit, dash_phase, dashes);
        if (BorderStrokeStyle.SOLID.equals(borderStyle)) {
            return BorderStrokeStyle.SOLID;
        }
        return borderStyle;
    }

    public String toString() {
        return "BorderStyleConverter";
    }
}
