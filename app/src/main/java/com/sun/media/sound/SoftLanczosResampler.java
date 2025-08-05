package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftLanczosResampler.class */
public final class SoftLanczosResampler extends SoftAbstractResampler {
    int sinc_table_fsize = 2000;
    int sinc_table_size = 5;
    int sinc_table_center = this.sinc_table_size / 2;
    float[][] sinc_table = new float[this.sinc_table_fsize];

    /* JADX WARN: Type inference failed for: r1v7, types: [float[], float[][]] */
    public SoftLanczosResampler() {
        for (int i2 = 0; i2 < this.sinc_table_fsize; i2++) {
            this.sinc_table[i2] = sincTable(this.sinc_table_size, (-i2) / this.sinc_table_fsize);
        }
    }

    public static double sinc(double d2) {
        if (d2 == 0.0d) {
            return 1.0d;
        }
        return Math.sin(3.141592653589793d * d2) / (3.141592653589793d * d2);
    }

    public static float[] sincTable(int i2, float f2) {
        int i3 = i2 / 2;
        float[] fArr = new float[i2];
        for (int i4 = 0; i4 < i2; i4++) {
            float f3 = (-i3) + i4 + f2;
            if (f3 < -2.0f || f3 > 2.0f) {
                fArr[i4] = 0.0f;
            } else if (f3 == 0.0f) {
                fArr[i4] = 1.0f;
            } else {
                fArr[i4] = (float) (((2.0d * Math.sin(3.141592653589793d * f3)) * Math.sin((3.141592653589793d * f3) / 2.0d)) / ((3.141592653589793d * f3) * (3.141592653589793d * f3)));
            }
        }
        return fArr;
    }

    @Override // com.sun.media.sound.SoftAbstractResampler
    public int getPadding() {
        return (this.sinc_table_size / 2) + 2;
    }

    @Override // com.sun.media.sound.SoftAbstractResampler
    public void interpolate(float[] fArr, float[] fArr2, float f2, float[] fArr3, float f3, float[] fArr4, int[] iArr, int i2) {
        float f4 = fArr3[0];
        float f5 = fArr2[0];
        int i3 = iArr[0];
        if (f3 == 0.0f) {
            while (f5 < f2 && i3 < i2) {
                int i4 = (int) f5;
                float[] fArr5 = this.sinc_table[(int) ((f5 - i4) * this.sinc_table_fsize)];
                int i5 = i4 - this.sinc_table_center;
                float f6 = 0.0f;
                int i6 = 0;
                while (i6 < this.sinc_table_size) {
                    f6 += fArr[i5] * fArr5[i6];
                    i6++;
                    i5++;
                }
                int i7 = i3;
                i3++;
                fArr4[i7] = f6;
                f5 += f4;
            }
        } else {
            while (f5 < f2 && i3 < i2) {
                int i8 = (int) f5;
                float[] fArr6 = this.sinc_table[(int) ((f5 - i8) * this.sinc_table_fsize)];
                int i9 = i8 - this.sinc_table_center;
                float f7 = 0.0f;
                int i10 = 0;
                while (i10 < this.sinc_table_size) {
                    f7 += fArr[i9] * fArr6[i10];
                    i10++;
                    i9++;
                }
                int i11 = i3;
                i3++;
                fArr4[i11] = f7;
                f5 += f4;
                f4 += f3;
            }
        }
        fArr2[0] = f5;
        iArr[0] = i3;
        fArr3[0] = f4;
    }
}
