package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/Lab.class */
public class Lab extends PColorSpace {
    public static final Name LAB_KEY = new Name("Lab");
    public static final Name WHITE_POINT_KEY = new Name("WhitePoint");
    public static final Name RANGE_KEY = new Name("Range");
    private float[] whitePoint;
    private float[] blackPoint;
    private float[] range;
    private float lBase;
    private float lSpread;
    private float aBase;
    private float aSpread;
    private float bBase;
    private float bSpread;
    private float xBase;
    private float xSpread;
    private float yBase;
    private float ySpread;
    private float zBase;
    private float zSpread;

    Lab(Library l2, HashMap h2) {
        super(l2, h2);
        this.whitePoint = new float[]{0.95047f, 1.0f, 1.08883f};
        this.blackPoint = new float[]{0.0f, 0.0f, 0.0f};
        this.range = new float[]{-100.0f, 100.0f, -100.0f, 100.0f};
        List v2 = (List) l2.getObject(h2, WHITE_POINT_KEY);
        if (v2 != null) {
            this.whitePoint[0] = ((Number) v2.get(0)).floatValue();
            this.whitePoint[1] = ((Number) v2.get(1)).floatValue();
            this.whitePoint[2] = ((Number) v2.get(2)).floatValue();
        }
        List v3 = (List) l2.getObject(h2, RANGE_KEY);
        if (v3 != null) {
            this.range[0] = ((Number) v3.get(0)).floatValue();
            this.range[1] = ((Number) v3.get(1)).floatValue();
            this.range[2] = ((Number) v3.get(2)).floatValue();
            this.range[3] = ((Number) v3.get(3)).floatValue();
        }
        this.lBase = 0.0f;
        this.lSpread = 100.0f;
        this.aBase = this.range[0];
        this.aSpread = this.range[1] - this.aBase;
        this.bBase = this.range[2];
        this.bSpread = this.range[3] - this.bBase;
        this.xBase = this.blackPoint[0];
        this.xSpread = this.whitePoint[0] - this.xBase;
        this.yBase = this.blackPoint[1];
        this.ySpread = this.whitePoint[1] - this.yBase;
        this.zBase = this.blackPoint[2];
        this.zSpread = this.whitePoint[2] - this.zBase;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return 3;
    }

    private double g(double x2) {
        double x3;
        if (x2 < 0.2069000005722046d) {
            x3 = 0.12842d * (x2 - 0.13793d);
        } else {
            x3 = x2 * x2 * x2;
        }
        return x3;
    }

    private double gg(double r2) {
        double r3;
        if (r2 > 0.0031308d) {
            r3 = (1.055d * Math.pow(r2, 0.4166666666666667d)) - 0.055d;
        } else {
            r3 = r2 * 12.92d;
        }
        return r3;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public void normaliseComponentsToFloats(int[] in, float[] out, float maxval) {
        super.normaliseComponentsToFloats(in, out, maxval);
        out[2] = this.lBase + (this.lSpread * out[2]);
        out[1] = this.aBase + (this.aSpread * out[1]);
        out[0] = this.bBase + (this.bSpread * out[0]);
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] f2, boolean fillAndStroke) {
        double cie_b = f2[0];
        double cie_a = f2[1];
        double cie_L = f2[2];
        double var_Y = (cie_L + 16.0d) / 116.0d;
        double var_X = var_Y + (cie_a * 0.002d);
        double var_Z = var_Y - (cie_b * 0.005d);
        double X2 = g(var_X);
        double Y2 = g(var_Y);
        double Z2 = g(var_Z);
        double X3 = this.xBase + (X2 * this.xSpread);
        double Y3 = this.yBase + (Y2 * this.ySpread);
        double Z3 = this.zBase + (Z2 * this.zSpread);
        double X4 = Math.max(0.0d, Math.min(1.0d, X3));
        double Y4 = Math.max(0.0d, Math.min(1.0d, Y3));
        double Z4 = Math.max(0.0d, Math.min(1.0d, Z3));
        double r2 = (X4 * 3.241d) + (Y4 * (-1.5374d)) + (Z4 * (-0.4986d));
        double g2 = (X4 * (-0.9692d)) + (Y4 * 1.876d) + (Z4 * 0.0416d);
        double b2 = (X4 * 0.0556d) + (Y4 * (-0.204d)) + (Z4 * 1.057d);
        double r3 = gg(r2);
        double g3 = gg(g2);
        double b3 = gg(b2);
        int ir = (int) (r3 * 255.0d);
        int ig = (int) (g3 * 255.0d);
        int ib = (int) (b3 * 255.0d);
        return new Color(Math.max(0, Math.min(255, ir)), Math.max(0, Math.min(255, ig)), Math.max(0, Math.min(255, ib)));
    }
}
