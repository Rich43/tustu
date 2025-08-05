package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftCubicResampler.class */
public final class SoftCubicResampler extends SoftAbstractResampler {
    @Override // com.sun.media.sound.SoftAbstractResampler
    public int getPadding() {
        return 3;
    }

    @Override // com.sun.media.sound.SoftAbstractResampler
    public void interpolate(float[] fArr, float[] fArr2, float f2, float[] fArr3, float f3, float[] fArr4, int[] iArr, int i2) {
        float f4 = fArr3[0];
        float f5 = fArr2[0];
        int i3 = iArr[0];
        if (f3 == 0.0f) {
            while (f5 < f2 && i3 < i2) {
                int i4 = (int) f5;
                float f6 = f5 - i4;
                float f7 = fArr[i4 - 1];
                float f8 = fArr[i4];
                float f9 = fArr[i4 + 1];
                float f10 = ((fArr[i4 + 2] - f9) + f8) - f7;
                int i5 = i3;
                i3++;
                fArr4[i5] = (((((f10 * f6) + ((f7 - f8) - f10)) * f6) + (f9 - f7)) * f6) + f8;
                f5 += f4;
            }
        } else {
            while (f5 < f2 && i3 < i2) {
                int i6 = (int) f5;
                float f11 = f5 - i6;
                float f12 = fArr[i6 - 1];
                float f13 = fArr[i6];
                float f14 = fArr[i6 + 1];
                float f15 = ((fArr[i6 + 2] - f14) + f13) - f12;
                int i7 = i3;
                i3++;
                fArr4[i7] = (((((f15 * f11) + ((f12 - f13) - f15)) * f11) + (f14 - f12)) * f11) + f13;
                f5 += f4;
                f4 += f3;
            }
        }
        fArr2[0] = f5;
        iArr[0] = i3;
        fArr3[0] = f4;
    }
}
