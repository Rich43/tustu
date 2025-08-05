package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/CalRGB.class */
public class CalRGB extends PColorSpace {
    public static final Name WHITE_POINT_KEY = new Name("WhitePoint");
    public static final Name GAMMA_KEY = new Name("Gamma");
    public static final Name MATRIX_KEY = new Name("Matrix");
    public static final Name CALRGB_KEY = new Name("CalRGB");
    protected float[] whitepoint;
    protected float[] gamma;
    protected float[] matrix;

    CalRGB(Library l2, HashMap h2) {
        super(l2, h2);
        this.whitepoint = new float[]{1.0f, 1.0f, 1.0f};
        this.gamma = new float[]{1.0f, 1.0f, 1.0f};
        this.matrix = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        List m2 = (List) h2.get(WHITE_POINT_KEY);
        if (m2 != null) {
            for (int i2 = 0; i2 < 3; i2++) {
                this.whitepoint[i2] = ((Number) m2.get(i2)).floatValue();
            }
        }
        List m3 = (List) h2.get(GAMMA_KEY);
        if (m3 != null) {
            for (int i3 = 0; i3 < 3; i3++) {
                this.gamma[i3] = ((Number) m3.get(i3)).floatValue();
            }
        }
        List m4 = (List) h2.get(MATRIX_KEY);
        if (m4 != null) {
            for (int i4 = 0; i4 < 9; i4++) {
                this.matrix[i4] = ((Number) m4.get(i4)).floatValue();
            }
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return 3;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] f2, boolean fillAndStroke) {
        return new Color(f2[2], f2[1], f2[0]);
    }
}
