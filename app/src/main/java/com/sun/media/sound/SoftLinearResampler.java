package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftLinearResampler.class */
public final class SoftLinearResampler extends SoftAbstractResampler {
    @Override // com.sun.media.sound.SoftAbstractResampler
    public int getPadding() {
        return 2;
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
                float f7 = fArr[i4];
                int i5 = i3;
                i3++;
                fArr4[i5] = f7 + ((fArr[i4 + 1] - f7) * f6);
                f5 += f4;
            }
        } else {
            while (f5 < f2 && i3 < i2) {
                int i6 = (int) f5;
                float f8 = f5 - i6;
                float f9 = fArr[i6];
                int i7 = i3;
                i3++;
                fArr4[i7] = f9 + ((fArr[i6 + 1] - f9) * f8);
                f5 += f4;
                f4 += f3;
            }
        }
        fArr2[0] = f5;
        iArr[0] = i3;
        fArr3[0] = f4;
    }
}
