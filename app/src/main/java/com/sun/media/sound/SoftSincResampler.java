package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/SoftSincResampler.class */
public final class SoftSincResampler extends SoftAbstractResampler {
    int sinc_scale_size = 100;
    int sinc_table_fsize = 800;
    int sinc_table_size = 30;
    int sinc_table_center = this.sinc_table_size / 2;
    float[][][] sinc_table = new float[this.sinc_scale_size][this.sinc_table_fsize][];

    public SoftSincResampler() {
        for (int i2 = 0; i2 < this.sinc_scale_size; i2++) {
            float fPow = (float) (1.0d / (1.0d + (Math.pow(i2, 1.1d) / 10.0d)));
            for (int i3 = 0; i3 < this.sinc_table_fsize; i3++) {
                this.sinc_table[i2][i3] = sincTable(this.sinc_table_size, (-i3) / this.sinc_table_fsize, fPow);
            }
        }
    }

    public static double sinc(double d2) {
        if (d2 == 0.0d) {
            return 1.0d;
        }
        return Math.sin(3.141592653589793d * d2) / (3.141592653589793d * d2);
    }

    public static float[] wHanning(int i2, float f2) {
        float[] fArr = new float[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            fArr[i3] = (float) (((-0.5d) * Math.cos((6.283185307179586d * (i3 + f2)) / i2)) + 0.5d);
        }
        return fArr;
    }

    public static float[] sincTable(int i2, float f2, float f3) {
        int i3 = i2 / 2;
        float[] fArrWHanning = wHanning(i2, f2);
        for (int i4 = 0; i4 < i2; i4++) {
            fArrWHanning[i4] = (float) (fArrWHanning[r1] * sinc(((-i3) + i4 + f2) * f3) * f3);
        }
        return fArrWHanning;
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
        int i4 = this.sinc_scale_size - 1;
        if (f3 != 0.0f) {
            while (f5 < f2 && i3 < i2) {
                int i5 = (int) f5;
                int i6 = (int) ((f4 - 1.0f) * 10.0f);
                if (i6 < 0) {
                    i6 = 0;
                } else if (i6 > i4) {
                    i6 = i4;
                }
                float[] fArr5 = this.sinc_table[i6][(int) ((f5 - i5) * this.sinc_table_fsize)];
                int i7 = i5 - this.sinc_table_center;
                float f6 = 0.0f;
                int i8 = 0;
                while (i8 < this.sinc_table_size) {
                    f6 += fArr[i7] * fArr5[i8];
                    i8++;
                    i7++;
                }
                int i9 = i3;
                i3++;
                fArr4[i9] = f6;
                f5 += f4;
                f4 += f3;
            }
        } else {
            int i10 = (int) ((f4 - 1.0f) * 10.0f);
            if (i10 < 0) {
                i10 = 0;
            } else if (i10 > i4) {
                i10 = i4;
            }
            float[][] fArr6 = this.sinc_table[i10];
            while (f5 < f2 && i3 < i2) {
                int i11 = (int) f5;
                float[] fArr7 = fArr6[(int) ((f5 - i11) * this.sinc_table_fsize)];
                int i12 = i11 - this.sinc_table_center;
                float f7 = 0.0f;
                int i13 = 0;
                while (i13 < this.sinc_table_size) {
                    f7 += fArr[i12] * fArr7[i13];
                    i13++;
                    i12++;
                }
                int i14 = i3;
                i3++;
                fArr4[i14] = f7;
                f5 += f4;
            }
        }
        fArr2[0] = f5;
        iArr[0] = i3;
        fArr3[0] = f4;
    }
}
