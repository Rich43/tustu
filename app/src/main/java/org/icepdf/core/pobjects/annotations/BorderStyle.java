package org.icepdf.core.pobjects.annotations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/BorderStyle.class */
public class BorderStyle extends Dictionary {
    private static final float[] DEFAULT_DASH_ARRAY = {3.0f};
    public static final Name BORDER_STYLE_KEY = new Name(PdfOps.S_TOKEN);
    public static final Name BORDER_WIDTH_KEY = new Name(PdfOps.W_TOKEN);
    public static final Name BORDER_DASH_KEY = new Name(PdfOps.D_TOKEN);
    public static final Color DARKEST = Color.black;
    public static final Color DARK = new Color(-10461088);
    public static final Color LIGHT = new Color(-7303024);
    public static final Color LIGHTEST = new Color(-1710619);
    public static final Name BORDER_STYLE_SOLID = new Name(PdfOps.S_TOKEN);
    public static final Name BORDER_STYLE_DASHED = new Name(PdfOps.D_TOKEN);
    public static final Name BORDER_STYLE_BEVELED = new Name(PdfOps.B_TOKEN);
    public static final Name BORDER_STYLE_INSET = new Name("I");
    public static final Name BORDER_STYLE_UNDERLINE = new Name("U");
    private float strokeWidth;
    private Name borderStyle;
    private float[] dashArray;

    public BorderStyle(Library l2, HashMap h2) {
        super(l2, h2);
        this.strokeWidth = 1.0f;
        this.dashArray = DEFAULT_DASH_ARRAY;
        Number value = (Number) getObject(BORDER_WIDTH_KEY);
        if (value != null) {
            this.strokeWidth = value.floatValue();
        }
        Object style = getObject(BORDER_STYLE_KEY);
        if (style != null) {
            this.borderStyle = (Name) style;
        }
        List dashVector = (List) getObject(BORDER_STYLE_DASHED);
        if (dashVector != null) {
            int sz = dashVector.size();
            float[] dashArray = new float[sz];
            for (int i2 = 0; i2 < sz; i2++) {
                Number num = (Number) dashVector.get(i2);
                dashArray[i2] = num.floatValue();
            }
            this.dashArray = dashArray;
        }
    }

    public BorderStyle() {
        super(null, null);
        this.strokeWidth = 1.0f;
        this.dashArray = DEFAULT_DASH_ARRAY;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public Name getBorderStyle() {
        return this.borderStyle;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.entries.put(BORDER_WIDTH_KEY, Float.valueOf(this.strokeWidth));
    }

    public void setBorderStyle(Name lineStyle) {
        this.borderStyle = lineStyle;
        this.entries.put(BORDER_STYLE_KEY, this.borderStyle);
        if (this.borderStyle.equals(BORDER_STYLE_DASHED)) {
            this.entries.put(BORDER_DASH_KEY, Arrays.asList(Float.valueOf(3.0f)));
        } else {
            this.entries.remove(BORDER_DASH_KEY);
        }
    }

    public boolean isStyleSolid() {
        return BORDER_STYLE_SOLID.equals(this.borderStyle);
    }

    public boolean isStyleDashed() {
        return BORDER_STYLE_DASHED.equals(this.borderStyle);
    }

    public boolean isStyleBeveled() {
        return BORDER_STYLE_BEVELED.equals(this.borderStyle);
    }

    public boolean isStyleInset() {
        return BORDER_STYLE_INSET.equals(this.borderStyle);
    }

    public boolean isStyleUnderline() {
        return BORDER_STYLE_UNDERLINE.equals(this.borderStyle);
    }

    public void setDashArray(float[] dashArray) {
        if (dashArray != null) {
            this.dashArray = dashArray;
            int sz = dashArray.length;
            List<Number> dashVector = new ArrayList<>(sz);
            for (float f2 : dashArray) {
                dashVector.add(Float.valueOf(f2));
            }
            this.dashArray = dashArray;
            this.entries.put(BORDER_STYLE_DASHED, dashVector);
        }
    }

    public float[] getDashArray() {
        return this.dashArray;
    }
}
