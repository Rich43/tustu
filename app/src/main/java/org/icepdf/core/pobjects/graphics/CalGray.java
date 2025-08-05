package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.HashMap;
import java.util.List;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/CalGray.class */
public class CalGray extends PColorSpace {
    public static final Name WHITE_POINT_KEY = new Name("WhitePoint");
    public static final Name GAMMA_KEY = new Name("Gamma");
    public static final Name MATRIX_KEY = new Name("Matrix");
    public static final Name CAL_GRAY_KEY = new Name("CalGray");
    private static ColorSpace grayCS = ColorSpace.getInstance(1003);
    protected float[] whitepoint;
    protected float gamma;

    public CalGray(Library l2, HashMap h2) {
        super(l2, h2);
        this.whitepoint = new float[]{1.0f, 1.0f, 1.0f};
        this.gamma = 1.0f;
        List m2 = (List) h2.get(WHITE_POINT_KEY);
        if (m2 != null) {
            for (int i2 = 0; i2 < 3; i2++) {
                this.whitepoint[i2] = ((Number) m2.get(i2)).floatValue();
            }
        }
        Object o2 = h2.get(GAMMA_KEY);
        if (o2 instanceof Float) {
            this.gamma = ((Float) o2).floatValue();
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] f2, boolean fillAndStroke) {
        float A2 = (float) Math.pow(f2[0], this.gamma);
        float X2 = this.whitepoint[0] * A2;
        float Y2 = this.whitepoint[1] * A2;
        float Z2 = this.whitepoint[2] * A2;
        if (X2 < 0.0f) {
            X2 = 0.0f;
        }
        if (Y2 < 0.0f) {
            Y2 = 0.0f;
        }
        if (Z2 < 0.0f) {
            Z2 = 0.0f;
        }
        if (X2 > 1.0f) {
        }
        if (Y2 > 1.0f) {
            Y2 = 1.0f;
        }
        if (Z2 > 1.0f) {
            Z2 = 1.0f;
        }
        Color tmp = new Color(grayCS, new float[]{Z2, Y2, Z2}, 1.0f);
        return tmp;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return 1;
    }
}
