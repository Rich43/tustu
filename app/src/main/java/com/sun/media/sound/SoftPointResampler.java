package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftPointResampler.class */
public final class SoftPointResampler extends SoftAbstractResampler {
    @Override // com.sun.media.sound.SoftAbstractResampler
    public int getPadding() {
        return 100;
    }

    @Override // com.sun.media.sound.SoftAbstractResampler
    public void interpolate(float[] fArr, float[] fArr2, float f2, float[] fArr3, float f3, float[] fArr4, int[] iArr, int i2) {
        float f4 = fArr3[0];
        float f5 = fArr2[0];
        int i3 = iArr[0];
        float f6 = i2;
        if (f3 == 0.0f) {
            while (f5 < f2 && i3 < f6) {
                int i4 = i3;
                i3++;
                fArr4[i4] = fArr[(int) f5];
                f5 += f4;
            }
        } else {
            while (f5 < f2 && i3 < f6) {
                int i5 = i3;
                i3++;
                fArr4[i5] = fArr[(int) f5];
                f5 += f4;
                f4 += f3;
            }
        }
        fArr2[0] = f5;
        iArr[0] = i3;
        fArr3[0] = f4;
    }
}
