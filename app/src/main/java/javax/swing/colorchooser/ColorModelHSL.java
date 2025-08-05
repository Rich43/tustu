package javax.swing.colorchooser;

/* loaded from: rt.jar:javax/swing/colorchooser/ColorModelHSL.class */
final class ColorModelHSL extends ColorModel {
    ColorModelHSL() {
        super("hsl", "Hue", "Saturation", "Lightness", "Transparency");
    }

    @Override // javax.swing.colorchooser.ColorModel
    void setColor(int i2, float[] fArr) {
        super.setColor(i2, fArr);
        RGBtoHSL(fArr, fArr);
        fArr[3] = 1.0f - fArr[3];
    }

    @Override // javax.swing.colorchooser.ColorModel
    int getColor(float[] fArr) {
        fArr[3] = 1.0f - fArr[3];
        HSLtoRGB(fArr, fArr);
        return super.getColor(fArr);
    }

    @Override // javax.swing.colorchooser.ColorModel
    int getMaximum(int i2) {
        return i2 == 0 ? 360 : 100;
    }

    @Override // javax.swing.colorchooser.ColorModel
    float getDefault(int i2) {
        if (i2 == 0) {
            return -1.0f;
        }
        return i2 == 2 ? 0.5f : 1.0f;
    }

    private static float[] HSLtoRGB(float[] fArr, float[] fArr2) {
        if (fArr2 == null) {
            fArr2 = new float[3];
        }
        float f2 = fArr[0];
        float f3 = fArr[1];
        float f4 = fArr[2];
        if (f3 > 0.0f) {
            float f5 = f2 < 1.0f ? f2 * 6.0f : 0.0f;
            float f6 = f4 + (f3 * (f4 > 0.5f ? 1.0f - f4 : f4));
            float f7 = (2.0f * f4) - f6;
            fArr2[0] = normalize(f6, f7, f5 < 4.0f ? f5 + 2.0f : f5 - 4.0f);
            fArr2[1] = normalize(f6, f7, f5);
            fArr2[2] = normalize(f6, f7, f5 < 2.0f ? f5 + 4.0f : f5 - 2.0f);
        } else {
            fArr2[0] = f4;
            fArr2[1] = f4;
            fArr2[2] = f4;
        }
        return fArr2;
    }

    private static float[] RGBtoHSL(float[] fArr, float[] fArr2) {
        if (fArr2 == null) {
            fArr2 = new float[3];
        }
        float fMax = max(fArr[0], fArr[1], fArr[2]);
        float fMin = min(fArr[0], fArr[1], fArr[2]);
        float f2 = fMax + fMin;
        float f3 = fMax - fMin;
        if (f3 > 0.0f) {
            f3 /= f2 > 1.0f ? 2.0f - f2 : f2;
        }
        fArr2[0] = getHue(fArr[0], fArr[1], fArr[2], fMax, fMin);
        fArr2[1] = f3;
        fArr2[2] = f2 / 2.0f;
        return fArr2;
    }

    static float min(float f2, float f3, float f4) {
        float f5 = f2 < f3 ? f2 : f3;
        return f5 < f4 ? f5 : f4;
    }

    static float max(float f2, float f3, float f4) {
        float f5 = f2 > f3 ? f2 : f3;
        return f5 > f4 ? f5 : f4;
    }

    static float getHue(float f2, float f3, float f4, float f5, float f6) {
        float f7;
        float f8 = f5 - f6;
        if (f8 > 0.0f) {
            if (f5 == f2) {
                f7 = (f3 - f4) / f8;
                if (f7 < 0.0f) {
                    f7 += 6.0f;
                }
            } else if (f5 == f3) {
                f7 = 2.0f + ((f4 - f2) / f8);
            } else {
                f7 = 4.0f + ((f2 - f3) / f8);
            }
            f8 = f7 / 6.0f;
        }
        return f8;
    }

    private static float normalize(float f2, float f3, float f4) {
        if (f4 < 1.0f) {
            return f3 + ((f2 - f3) * f4);
        }
        if (f4 < 3.0f) {
            return f2;
        }
        if (f4 < 4.0f) {
            return f3 + ((f2 - f3) * (4.0f - f4));
        }
        return f3;
    }
}
