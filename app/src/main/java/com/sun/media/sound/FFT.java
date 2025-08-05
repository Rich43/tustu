package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/FFT.class */
public final class FFT {

    /* renamed from: w, reason: collision with root package name */
    private final double[] f11978w;
    private final int fftFrameSize;
    private final int sign;
    private final int[] bitm_array;
    private final int fftFrameSize2;

    public FFT(int i2, int i3) {
        this.f11978w = computeTwiddleFactors(i2, i3);
        this.fftFrameSize = i2;
        this.sign = i3;
        this.fftFrameSize2 = i2 << 1;
        this.bitm_array = new int[this.fftFrameSize2];
        for (int i4 = 2; i4 < this.fftFrameSize2; i4 += 2) {
            int i5 = 0;
            for (int i6 = 2; i6 < this.fftFrameSize2; i6 <<= 1) {
                if ((i4 & i6) != 0) {
                    i5++;
                }
                i5 <<= 1;
            }
            this.bitm_array[i4] = i5;
        }
    }

    public void transform(double[] dArr) {
        bitreversal(dArr);
        calc(this.fftFrameSize, dArr, this.sign, this.f11978w);
    }

    private static final double[] computeTwiddleFactors(int i2, int i3) {
        int iLog = (int) (Math.log(i2) / Math.log(2.0d));
        double[] dArr = new double[(i2 - 1) * 4];
        int i4 = 0;
        int i5 = 2;
        for (int i6 = 0; i6 < iLog; i6++) {
            int i7 = i5;
            i5 <<= 1;
            double d2 = 1.0d;
            double d3 = 0.0d;
            double d4 = 3.141592653589793d / (i7 >> 1);
            double dCos = Math.cos(d4);
            double dSin = i3 * Math.sin(d4);
            for (int i8 = 0; i8 < i7; i8 += 2) {
                int i9 = i4;
                int i10 = i4 + 1;
                dArr[i9] = d2;
                i4 = i10 + 1;
                dArr[i10] = d3;
                double d5 = d2;
                d2 = (d5 * dCos) - (d3 * dSin);
                d3 = (d5 * dSin) + (d3 * dCos);
            }
        }
        int i11 = 0;
        int length = dArr.length >> 1;
        int i12 = 2;
        for (int i13 = 0; i13 < iLog - 1; i13++) {
            int i14 = i12;
            i12 *= 2;
            int i15 = i11 + i14;
            for (int i16 = 0; i16 < i14; i16 += 2) {
                int i17 = i11;
                int i18 = i11 + 1;
                double d6 = dArr[i17];
                i11 = i18 + 1;
                double d7 = dArr[i18];
                int i19 = i15;
                int i20 = i15 + 1;
                double d8 = dArr[i19];
                i15 = i20 + 1;
                double d9 = dArr[i20];
                int i21 = length;
                int i22 = length + 1;
                dArr[i21] = (d6 * d8) - (d7 * d9);
                length = i22 + 1;
                dArr[i22] = (d6 * d9) + (d7 * d8);
            }
        }
        return dArr;
    }

    private static final void calc(int i2, double[] dArr, int i3, double[] dArr2) {
        if (2 >= (i2 << 1)) {
            return;
        }
        int i4 = 2 - 2;
        if (i3 == -1) {
            calcF4F(i2, dArr, i4, 2, dArr2);
        } else {
            calcF4I(i2, dArr, i4, 2, dArr2);
        }
    }

    private static final void calcF2E(int i2, double[] dArr, int i3, int i4, double[] dArr2) {
        for (int i5 = 0; i5 < i4; i5 += 2) {
            int i6 = i3;
            int i7 = i3 + 1;
            double d2 = dArr2[i6];
            i3 = i7 + 1;
            double d3 = dArr2[i7];
            int i8 = i5 + i4;
            double d4 = dArr[i8];
            double d5 = dArr[i8 + 1];
            double d6 = dArr[i5];
            double d7 = dArr[i5 + 1];
            double d8 = (d4 * d2) - (d5 * d3);
            double d9 = (d4 * d3) + (d5 * d2);
            dArr[i8] = d6 - d8;
            dArr[i8 + 1] = d7 - d9;
            dArr[i5] = d6 + d8;
            dArr[i5 + 1] = d7 + d9;
        }
    }

    private static final void calcF4F(int i2, double[] dArr, int i3, int i4, double[] dArr2) {
        int i5 = i2 << 1;
        int length = dArr2.length >> 1;
        while (i4 < i5) {
            if ((i4 << 2) == i5) {
                calcF4FE(i2, dArr, i3, i4, dArr2);
                return;
            }
            int i6 = i4;
            int i7 = i4 << 1;
            if (i7 == i5) {
                calcF2E(i2, dArr, i3, i4, dArr2);
                return;
            }
            i4 <<= 2;
            int i8 = i3 + i6;
            int i9 = i3 + length;
            int i10 = i3 + 2;
            int i11 = i8 + 2;
            int i12 = i9 + 2;
            int i13 = 0;
            while (true) {
                int i14 = i13;
                if (i14 >= i5) {
                    break;
                }
                int i15 = i14 + i6;
                double d2 = dArr[i15];
                double d3 = dArr[i15 + 1];
                double d4 = dArr[i14];
                double d5 = dArr[i14 + 1];
                int i16 = i14 + i7;
                int i17 = i15 + i7;
                double d6 = dArr[i17];
                double d7 = dArr[i17 + 1];
                double d8 = dArr[i16];
                double d9 = dArr[i16 + 1];
                double d10 = d4 - d2;
                double d11 = d5 - d3;
                double d12 = d4 + d2;
                double d13 = d5 + d3;
                double d14 = d6 - d8;
                double d15 = d7 - d9;
                double d16 = d10 + d15;
                double d17 = d11 - d14;
                double d18 = d10 - d15;
                double d19 = d11 + d14;
                double d20 = d8 + d6;
                double d21 = d9 + d7;
                double d22 = d12 - d20;
                double d23 = d13 - d21;
                double d24 = d12 + d20;
                double d25 = d13 + d21;
                dArr[i17] = d16;
                dArr[i17 + 1] = d17;
                dArr[i16] = d22;
                dArr[i16 + 1] = d23;
                int i18 = i16 - i7;
                int i19 = i17 - i7;
                dArr[i19] = d18;
                dArr[i19 + 1] = d19;
                dArr[i18] = d24;
                dArr[i18 + 1] = d25;
                i13 = i18 + i4;
            }
            for (int i20 = 2; i20 < i6; i20 += 2) {
                int i21 = i10;
                int i22 = i10 + 1;
                double d26 = dArr2[i21];
                i10 = i22 + 1;
                double d27 = dArr2[i22];
                int i23 = i11;
                int i24 = i11 + 1;
                double d28 = dArr2[i23];
                i11 = i24 + 1;
                double d29 = dArr2[i24];
                int i25 = i12;
                int i26 = i12 + 1;
                double d30 = dArr2[i25];
                i12 = i26 + 1;
                double d31 = dArr2[i26];
                int i27 = i20;
                while (true) {
                    int i28 = i27;
                    if (i28 < i5) {
                        int i29 = i28 + i6;
                        double d32 = dArr[i29];
                        double d33 = dArr[i29 + 1];
                        double d34 = dArr[i28];
                        double d35 = dArr[i28 + 1];
                        int i30 = i28 + i7;
                        int i31 = i29 + i7;
                        double d36 = dArr[i31];
                        double d37 = dArr[i31 + 1];
                        double d38 = dArr[i30];
                        double d39 = dArr[i30 + 1];
                        double d40 = (d32 * d26) - (d33 * d27);
                        double d41 = (d32 * d27) + (d33 * d26);
                        double d42 = d34 - d40;
                        double d43 = d35 - d41;
                        double d44 = d34 + d40;
                        double d45 = d35 + d41;
                        double d46 = (d38 * d28) - (d39 * d29);
                        double d47 = (d38 * d29) + (d39 * d28);
                        double d48 = (d36 * d30) - (d37 * d31);
                        double d49 = (d36 * d31) + (d37 * d30);
                        double d50 = d48 - d46;
                        double d51 = d49 - d47;
                        double d52 = d42 + d51;
                        double d53 = d43 - d50;
                        double d54 = d42 - d51;
                        double d55 = d43 + d50;
                        double d56 = d46 + d48;
                        double d57 = d47 + d49;
                        double d58 = d44 - d56;
                        double d59 = d45 - d57;
                        double d60 = d44 + d56;
                        double d61 = d45 + d57;
                        dArr[i31] = d52;
                        dArr[i31 + 1] = d53;
                        dArr[i30] = d58;
                        dArr[i30 + 1] = d59;
                        int i32 = i30 - i7;
                        int i33 = i31 - i7;
                        dArr[i33] = d54;
                        dArr[i33 + 1] = d55;
                        dArr[i32] = d60;
                        dArr[i32 + 1] = d61;
                        i27 = i32 + i4;
                    }
                }
            }
            i3 = i10 + (i6 << 1);
        }
        calcF2E(i2, dArr, i3, i4, dArr2);
    }

    private static final void calcF4I(int i2, double[] dArr, int i3, int i4, double[] dArr2) {
        int i5 = i2 << 1;
        int length = dArr2.length >> 1;
        while (i4 < i5) {
            if ((i4 << 2) == i5) {
                calcF4IE(i2, dArr, i3, i4, dArr2);
                return;
            }
            int i6 = i4;
            int i7 = i4 << 1;
            if (i7 == i5) {
                calcF2E(i2, dArr, i3, i4, dArr2);
                return;
            }
            i4 <<= 2;
            int i8 = i3 + i6;
            int i9 = i3 + length;
            int i10 = i3 + 2;
            int i11 = i8 + 2;
            int i12 = i9 + 2;
            int i13 = 0;
            while (true) {
                int i14 = i13;
                if (i14 >= i5) {
                    break;
                }
                int i15 = i14 + i6;
                double d2 = dArr[i15];
                double d3 = dArr[i15 + 1];
                double d4 = dArr[i14];
                double d5 = dArr[i14 + 1];
                int i16 = i14 + i7;
                int i17 = i15 + i7;
                double d6 = dArr[i17];
                double d7 = dArr[i17 + 1];
                double d8 = dArr[i16];
                double d9 = dArr[i16 + 1];
                double d10 = d4 - d2;
                double d11 = d5 - d3;
                double d12 = d4 + d2;
                double d13 = d5 + d3;
                double d14 = d8 - d6;
                double d15 = d9 - d7;
                double d16 = d10 + d15;
                double d17 = d11 - d14;
                double d18 = d10 - d15;
                double d19 = d11 + d14;
                double d20 = d8 + d6;
                double d21 = d9 + d7;
                double d22 = d12 - d20;
                double d23 = d13 - d21;
                double d24 = d12 + d20;
                double d25 = d13 + d21;
                dArr[i17] = d16;
                dArr[i17 + 1] = d17;
                dArr[i16] = d22;
                dArr[i16 + 1] = d23;
                int i18 = i16 - i7;
                int i19 = i17 - i7;
                dArr[i19] = d18;
                dArr[i19 + 1] = d19;
                dArr[i18] = d24;
                dArr[i18 + 1] = d25;
                i13 = i18 + i4;
            }
            for (int i20 = 2; i20 < i6; i20 += 2) {
                int i21 = i10;
                int i22 = i10 + 1;
                double d26 = dArr2[i21];
                i10 = i22 + 1;
                double d27 = dArr2[i22];
                int i23 = i11;
                int i24 = i11 + 1;
                double d28 = dArr2[i23];
                i11 = i24 + 1;
                double d29 = dArr2[i24];
                int i25 = i12;
                int i26 = i12 + 1;
                double d30 = dArr2[i25];
                i12 = i26 + 1;
                double d31 = dArr2[i26];
                int i27 = i20;
                while (true) {
                    int i28 = i27;
                    if (i28 < i5) {
                        int i29 = i28 + i6;
                        double d32 = dArr[i29];
                        double d33 = dArr[i29 + 1];
                        double d34 = dArr[i28];
                        double d35 = dArr[i28 + 1];
                        int i30 = i28 + i7;
                        int i31 = i29 + i7;
                        double d36 = dArr[i31];
                        double d37 = dArr[i31 + 1];
                        double d38 = dArr[i30];
                        double d39 = dArr[i30 + 1];
                        double d40 = (d32 * d26) - (d33 * d27);
                        double d41 = (d32 * d27) + (d33 * d26);
                        double d42 = d34 - d40;
                        double d43 = d35 - d41;
                        double d44 = d34 + d40;
                        double d45 = d35 + d41;
                        double d46 = (d38 * d28) - (d39 * d29);
                        double d47 = (d38 * d29) + (d39 * d28);
                        double d48 = (d36 * d30) - (d37 * d31);
                        double d49 = (d36 * d31) + (d37 * d30);
                        double d50 = d46 - d48;
                        double d51 = d47 - d49;
                        double d52 = d42 + d51;
                        double d53 = d43 - d50;
                        double d54 = d42 - d51;
                        double d55 = d43 + d50;
                        double d56 = d46 + d48;
                        double d57 = d47 + d49;
                        double d58 = d44 - d56;
                        double d59 = d45 - d57;
                        double d60 = d44 + d56;
                        double d61 = d45 + d57;
                        dArr[i31] = d52;
                        dArr[i31 + 1] = d53;
                        dArr[i30] = d58;
                        dArr[i30 + 1] = d59;
                        int i32 = i30 - i7;
                        int i33 = i31 - i7;
                        dArr[i33] = d54;
                        dArr[i33 + 1] = d55;
                        dArr[i32] = d60;
                        dArr[i32 + 1] = d61;
                        i27 = i32 + i4;
                    }
                }
            }
            i3 = i10 + (i6 << 1);
        }
        calcF2E(i2, dArr, i3, i4, dArr2);
    }

    private static final void calcF4FE(int i2, double[] dArr, int i3, int i4, double[] dArr2) {
        int i5 = i2 << 1;
        int length = dArr2.length >> 1;
        while (i4 < i5) {
            int i6 = i4;
            int i7 = i4 << 1;
            if (i7 == i5) {
                calcF2E(i2, dArr, i3, i4, dArr2);
                return;
            }
            i4 <<= 2;
            int i8 = i3 + i6;
            int i9 = i3 + length;
            int i10 = 0;
            while (i10 < i6) {
                int i11 = i3;
                int i12 = i3 + 1;
                double d2 = dArr2[i11];
                i3 = i12 + 1;
                double d3 = dArr2[i12];
                int i13 = i8;
                int i14 = i8 + 1;
                double d4 = dArr2[i13];
                i8 = i14 + 1;
                double d5 = dArr2[i14];
                int i15 = i9;
                int i16 = i9 + 1;
                double d6 = dArr2[i15];
                i9 = i16 + 1;
                double d7 = dArr2[i16];
                int i17 = i10 + i6;
                double d8 = dArr[i17];
                double d9 = dArr[i17 + 1];
                double d10 = dArr[i10];
                double d11 = dArr[i10 + 1];
                int i18 = i10 + i7;
                int i19 = i17 + i7;
                double d12 = dArr[i19];
                double d13 = dArr[i19 + 1];
                double d14 = dArr[i18];
                double d15 = dArr[i18 + 1];
                double d16 = (d8 * d2) - (d9 * d3);
                double d17 = (d8 * d3) + (d9 * d2);
                double d18 = d10 - d16;
                double d19 = d11 - d17;
                double d20 = d10 + d16;
                double d21 = d11 + d17;
                double d22 = (d14 * d4) - (d15 * d5);
                double d23 = (d14 * d5) + (d15 * d4);
                double d24 = (d12 * d6) - (d13 * d7);
                double d25 = (d12 * d7) + (d13 * d6);
                double d26 = d24 - d22;
                double d27 = d25 - d23;
                double d28 = d18 + d27;
                double d29 = d19 - d26;
                double d30 = d18 - d27;
                double d31 = d19 + d26;
                double d32 = d22 + d24;
                double d33 = d23 + d25;
                double d34 = d20 - d32;
                double d35 = d21 - d33;
                double d36 = d20 + d32;
                double d37 = d21 + d33;
                dArr[i19] = d28;
                dArr[i19 + 1] = d29;
                dArr[i18] = d34;
                dArr[i18 + 1] = d35;
                int i20 = i18 - i7;
                int i21 = i19 - i7;
                dArr[i21] = d30;
                dArr[i21 + 1] = d31;
                dArr[i20] = d36;
                dArr[i20 + 1] = d37;
                i10 = i20 + 2;
            }
            i3 += i6 << 1;
        }
    }

    private static final void calcF4IE(int i2, double[] dArr, int i3, int i4, double[] dArr2) {
        int i5 = i2 << 1;
        int length = dArr2.length >> 1;
        while (i4 < i5) {
            int i6 = i4;
            int i7 = i4 << 1;
            if (i7 == i5) {
                calcF2E(i2, dArr, i3, i4, dArr2);
                return;
            }
            i4 <<= 2;
            int i8 = i3 + i6;
            int i9 = i3 + length;
            int i10 = 0;
            while (i10 < i6) {
                int i11 = i3;
                int i12 = i3 + 1;
                double d2 = dArr2[i11];
                i3 = i12 + 1;
                double d3 = dArr2[i12];
                int i13 = i8;
                int i14 = i8 + 1;
                double d4 = dArr2[i13];
                i8 = i14 + 1;
                double d5 = dArr2[i14];
                int i15 = i9;
                int i16 = i9 + 1;
                double d6 = dArr2[i15];
                i9 = i16 + 1;
                double d7 = dArr2[i16];
                int i17 = i10 + i6;
                double d8 = dArr[i17];
                double d9 = dArr[i17 + 1];
                double d10 = dArr[i10];
                double d11 = dArr[i10 + 1];
                int i18 = i10 + i7;
                int i19 = i17 + i7;
                double d12 = dArr[i19];
                double d13 = dArr[i19 + 1];
                double d14 = dArr[i18];
                double d15 = dArr[i18 + 1];
                double d16 = (d8 * d2) - (d9 * d3);
                double d17 = (d8 * d3) + (d9 * d2);
                double d18 = d10 - d16;
                double d19 = d11 - d17;
                double d20 = d10 + d16;
                double d21 = d11 + d17;
                double d22 = (d14 * d4) - (d15 * d5);
                double d23 = (d14 * d5) + (d15 * d4);
                double d24 = (d12 * d6) - (d13 * d7);
                double d25 = (d12 * d7) + (d13 * d6);
                double d26 = d22 - d24;
                double d27 = d23 - d25;
                double d28 = d18 + d27;
                double d29 = d19 - d26;
                double d30 = d18 - d27;
                double d31 = d19 + d26;
                double d32 = d22 + d24;
                double d33 = d23 + d25;
                double d34 = d20 - d32;
                double d35 = d21 - d33;
                double d36 = d20 + d32;
                double d37 = d21 + d33;
                dArr[i19] = d28;
                dArr[i19 + 1] = d29;
                dArr[i18] = d34;
                dArr[i18 + 1] = d35;
                int i20 = i18 - i7;
                int i21 = i19 - i7;
                dArr[i21] = d30;
                dArr[i21 + 1] = d31;
                dArr[i20] = d36;
                dArr[i20 + 1] = d37;
                i10 = i20 + 2;
            }
            i3 += i6 << 1;
        }
    }

    private final void bitreversal(double[] dArr) {
        if (this.fftFrameSize < 4) {
            return;
        }
        int i2 = this.fftFrameSize2 - 2;
        for (int i3 = 0; i3 < this.fftFrameSize; i3 += 4) {
            int i4 = this.bitm_array[i3];
            if (i3 < i4) {
                int i5 = i3;
                double d2 = dArr[i5];
                dArr[i5] = dArr[i4];
                dArr[i4] = d2;
                int i6 = i5 + 1;
                int i7 = i4 + 1;
                double d3 = dArr[i6];
                dArr[i6] = dArr[i7];
                dArr[i7] = d3;
                int i8 = i2 - i3;
                int i9 = i2 - i4;
                double d4 = dArr[i8];
                dArr[i8] = dArr[i9];
                dArr[i9] = d4;
                int i10 = i8 + 1;
                int i11 = i9 + 1;
                double d5 = dArr[i10];
                dArr[i10] = dArr[i11];
                dArr[i11] = d5;
            }
            int i12 = i4 + this.fftFrameSize;
            int i13 = i3 + 2;
            double d6 = dArr[i13];
            dArr[i13] = dArr[i12];
            dArr[i12] = d6;
            int i14 = i13 + 1;
            int i15 = i12 + 1;
            double d7 = dArr[i14];
            dArr[i14] = dArr[i15];
            dArr[i15] = d7;
        }
    }
}
