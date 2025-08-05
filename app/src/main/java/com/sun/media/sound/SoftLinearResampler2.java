package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftLinearResampler2.class */
public final class SoftLinearResampler2 extends SoftAbstractResampler {
    @Override // com.sun.media.sound.SoftAbstractResampler
    public int getPadding() {
        return 2;
    }

    @Override // com.sun.media.sound.SoftAbstractResampler
    public void interpolate(float[] fArr, float[] fArr2, float f2, float[] fArr3, float f3, float[] fArr4, int[] iArr, int i2) {
        float f4 = fArr3[0];
        float f5 = fArr2[0];
        int i3 = iArr[0];
        int i4 = i2;
        if (f5 >= f2 || i3 >= i4) {
            return;
        }
        int i5 = (int) (f5 * 32768.0f);
        int i6 = (int) (f2 * 32768.0f);
        int i7 = (int) (f4 * 32768.0f);
        float f6 = i7 * 3.0517578E-5f;
        if (f3 == 0.0f) {
            int i8 = i6 - i5;
            int i9 = i8 % i7;
            if (i9 != 0) {
                i8 += i7 - i9;
            }
            int i10 = i3 + (i8 / i7);
            if (i10 < i4) {
                i4 = i10;
            }
            while (i3 < i4) {
                int i11 = i5 >> 15;
                float f7 = f5 - i11;
                float f8 = fArr[i11];
                int i12 = i3;
                i3++;
                fArr4[i12] = f8 + ((fArr[i11 + 1] - f8) * f7);
                i5 += i7;
                f5 += f6;
            }
        } else {
            int i13 = (int) (f3 * 32768.0f);
            float f9 = i13 * 3.0517578E-5f;
            while (i5 < i6 && i3 < i4) {
                int i14 = i5 >> 15;
                float f10 = f5 - i14;
                float f11 = fArr[i14];
                int i15 = i3;
                i3++;
                fArr4[i15] = f11 + ((fArr[i14 + 1] - f11) * f10);
                f5 += f6;
                i5 += i7;
                f6 += f9;
                i7 += i13;
            }
        }
        fArr2[0] = f5;
        iArr[0] = i3;
        fArr3[0] = f6;
    }
}
