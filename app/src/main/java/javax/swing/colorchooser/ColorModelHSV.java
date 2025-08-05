package javax.swing.colorchooser;

/* loaded from: rt.jar:javax/swing/colorchooser/ColorModelHSV.class */
final class ColorModelHSV extends ColorModel {
    ColorModelHSV() {
        super("hsv", "Hue", "Saturation", "Value", "Transparency");
    }

    @Override // javax.swing.colorchooser.ColorModel
    void setColor(int i2, float[] fArr) {
        super.setColor(i2, fArr);
        RGBtoHSV(fArr, fArr);
        fArr[3] = 1.0f - fArr[3];
    }

    @Override // javax.swing.colorchooser.ColorModel
    int getColor(float[] fArr) {
        fArr[3] = 1.0f - fArr[3];
        HSVtoRGB(fArr, fArr);
        return super.getColor(fArr);
    }

    @Override // javax.swing.colorchooser.ColorModel
    int getMaximum(int i2) {
        return i2 == 0 ? 360 : 100;
    }

    @Override // javax.swing.colorchooser.ColorModel
    float getDefault(int i2) {
        return i2 == 0 ? -1.0f : 1.0f;
    }

    private static float[] HSVtoRGB(float[] fArr, float[] fArr2) {
        if (fArr2 == null) {
            fArr2 = new float[3];
        }
        float f2 = fArr[0];
        float f3 = fArr[1];
        float f4 = fArr[2];
        fArr2[0] = f4;
        fArr2[1] = f4;
        fArr2[2] = f4;
        if (f3 > 0.0f) {
            float f5 = f2 < 1.0f ? f2 * 6.0f : 0.0f;
            int i2 = (int) f5;
            float f6 = f5 - i2;
            switch (i2) {
                case 0:
                    float[] fArr3 = fArr2;
                    fArr3[1] = fArr3[1] * (1.0f - (f3 * (1.0f - f6)));
                    float[] fArr4 = fArr2;
                    fArr4[2] = fArr4[2] * (1.0f - f3);
                    break;
                case 1:
                    float[] fArr5 = fArr2;
                    fArr5[0] = fArr5[0] * (1.0f - (f3 * f6));
                    float[] fArr6 = fArr2;
                    fArr6[2] = fArr6[2] * (1.0f - f3);
                    break;
                case 2:
                    float[] fArr7 = fArr2;
                    fArr7[0] = fArr7[0] * (1.0f - f3);
                    float[] fArr8 = fArr2;
                    fArr8[2] = fArr8[2] * (1.0f - (f3 * (1.0f - f6)));
                    break;
                case 3:
                    float[] fArr9 = fArr2;
                    fArr9[0] = fArr9[0] * (1.0f - f3);
                    float[] fArr10 = fArr2;
                    fArr10[1] = fArr10[1] * (1.0f - (f3 * f6));
                    break;
                case 4:
                    float[] fArr11 = fArr2;
                    fArr11[0] = fArr11[0] * (1.0f - (f3 * (1.0f - f6)));
                    float[] fArr12 = fArr2;
                    fArr12[1] = fArr12[1] * (1.0f - f3);
                    break;
                case 5:
                    float[] fArr13 = fArr2;
                    fArr13[1] = fArr13[1] * (1.0f - f3);
                    float[] fArr14 = fArr2;
                    fArr14[2] = fArr14[2] * (1.0f - (f3 * f6));
                    break;
            }
        }
        return fArr2;
    }

    private static float[] RGBtoHSV(float[] fArr, float[] fArr2) {
        if (fArr2 == null) {
            fArr2 = new float[3];
        }
        float fMax = ColorModelHSL.max(fArr[0], fArr[1], fArr[2]);
        float fMin = ColorModelHSL.min(fArr[0], fArr[1], fArr[2]);
        float f2 = fMax - fMin;
        if (f2 > 0.0f) {
            f2 /= fMax;
        }
        fArr2[0] = ColorModelHSL.getHue(fArr[0], fArr[1], fArr[2], fMax, fMin);
        fArr2[1] = f2;
        fArr2[2] = fMax;
        return fArr2;
    }
}
