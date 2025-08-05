package javax.swing.colorchooser;

import org.icepdf.core.pobjects.graphics.SoftMask;

/* loaded from: rt.jar:javax/swing/colorchooser/ColorModelCMYK.class */
final class ColorModelCMYK extends ColorModel {
    ColorModelCMYK() {
        super("cmyk", "Cyan", "Magenta", "Yellow", "Black", SoftMask.SOFT_MASK_TYPE_ALPHA);
    }

    @Override // javax.swing.colorchooser.ColorModel
    void setColor(int i2, float[] fArr) {
        super.setColor(i2, fArr);
        fArr[4] = fArr[3];
        RGBtoCMYK(fArr, fArr);
    }

    @Override // javax.swing.colorchooser.ColorModel
    int getColor(float[] fArr) {
        CMYKtoRGB(fArr, fArr);
        fArr[3] = fArr[4];
        return super.getColor(fArr);
    }

    private static float[] CMYKtoRGB(float[] fArr, float[] fArr2) {
        if (fArr2 == null) {
            fArr2 = new float[3];
        }
        fArr2[0] = ((1.0f + (fArr[0] * fArr[3])) - fArr[3]) - fArr[0];
        fArr2[1] = ((1.0f + (fArr[1] * fArr[3])) - fArr[3]) - fArr[1];
        fArr2[2] = ((1.0f + (fArr[2] * fArr[3])) - fArr[3]) - fArr[2];
        return fArr2;
    }

    private static float[] RGBtoCMYK(float[] fArr, float[] fArr2) {
        if (fArr2 == null) {
            fArr2 = new float[4];
        }
        float fMax = ColorModelHSL.max(fArr[0], fArr[1], fArr[2]);
        if (fMax > 0.0f) {
            fArr2[0] = 1.0f - (fArr[0] / fMax);
            fArr2[1] = 1.0f - (fArr[1] / fMax);
            fArr2[2] = 1.0f - (fArr[2] / fMax);
        } else {
            fArr2[0] = 0.0f;
            fArr2[1] = 0.0f;
            fArr2[2] = 0.0f;
        }
        fArr2[3] = 1.0f - fMax;
        return fArr2;
    }
}
