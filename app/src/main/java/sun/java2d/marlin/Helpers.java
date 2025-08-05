package sun.java2d.marlin;

/* loaded from: rt.jar:sun/java2d/marlin/Helpers.class */
final class Helpers implements MarlinConst {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Helpers.class.desiredAssertionStatus();
    }

    private Helpers() {
        throw new Error("This is a non instantiable class");
    }

    static boolean within(float f2, float f3, float f4) {
        float f5 = f3 - f2;
        return f5 <= f4 && f5 >= (-f4);
    }

    static boolean within(double d2, double d3, double d4) {
        double d5 = d3 - d2;
        return d5 <= d4 && d5 >= (-d4);
    }

    static int quadraticRoots(float f2, float f3, float f4, float[] fArr, int i2) {
        int i3 = i2;
        if (f2 != 0.0f) {
            float f5 = (f3 * f3) - ((4.0f * f2) * f4);
            if (f5 > 0.0f) {
                float fSqrt = (float) Math.sqrt(f5);
                if (f3 >= 0.0f) {
                    int i4 = i3 + 1;
                    fArr[i3] = (2.0f * f4) / ((-f3) - fSqrt);
                    i3 = i4 + 1;
                    fArr[i4] = ((-f3) - fSqrt) / (2.0f * f2);
                } else {
                    int i5 = i3 + 1;
                    fArr[i3] = ((-f3) + fSqrt) / (2.0f * f2);
                    i3 = i5 + 1;
                    fArr[i5] = (2.0f * f4) / ((-f3) + fSqrt);
                }
            } else if (f5 == 0.0f) {
                i3++;
                fArr[i3] = (-f3) / (2.0f * f2);
            }
        } else if (f3 != 0.0f) {
            i3++;
            fArr[i3] = (-f4) / f3;
        }
        return i3 - i2;
    }

    static int cubicRootsInAB(float f2, float f3, float f4, float f5, float[] fArr, int i2, float f6, float f7) {
        int i3;
        if (f2 == 0.0f) {
            return filterOutNotInAB(fArr, i2, quadraticRoots(f3, f4, f5, fArr, i2), f6, f7) - i2;
        }
        float f8 = f3 / f2;
        float f9 = f4 / f2;
        double d2 = f8 * f8;
        double d3 = 0.3333333333333333d * (((-0.3333333333333333d) * d2) + f9);
        double d4 = 0.5d * ((((0.07407407407407407d * f8) * d2) - ((0.3333333333333333d * f8) * f9)) + (f5 / f2));
        double d5 = d3 * d3 * d3;
        double d6 = (d4 * d4) + d5;
        if (d6 < 0.0d) {
            double dAcos = 0.3333333333333333d * Math.acos((-d4) / Math.sqrt(-d5));
            double dSqrt = 2.0d * Math.sqrt(-d3);
            fArr[i2 + 0] = (float) (dSqrt * Math.cos(dAcos));
            fArr[i2 + 1] = (float) ((-dSqrt) * Math.cos(dAcos + 1.0471975511965976d));
            fArr[i2 + 2] = (float) ((-dSqrt) * Math.cos(dAcos - 1.0471975511965976d));
            i3 = 3;
        } else {
            double dSqrt2 = Math.sqrt(d6);
            fArr[i2] = (float) (Math.cbrt(dSqrt2 - d4) + (-Math.cbrt(dSqrt2 + d4)));
            i3 = 1;
            if (within(d6, 0.0d, 1.0E-8d)) {
                fArr[i2 + 1] = -(fArr[i2] / 2.0f);
                i3 = 2;
            }
        }
        float f10 = 0.33333334f * f8;
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = i2 + i4;
            fArr[i5] = fArr[i5] - f10;
        }
        return filterOutNotInAB(fArr, i2, i3, f6, f7) - i2;
    }

    static float evalCubic(float f2, float f3, float f4, float f5, float f6) {
        return (f6 * ((f6 * ((f6 * f2) + f3)) + f4)) + f5;
    }

    static float evalQuad(float f2, float f3, float f4, float f5) {
        return (f5 * ((f5 * f2) + f3)) + f4;
    }

    static int filterOutNotInAB(float[] fArr, int i2, int i3, float f2, float f3) {
        int i4 = i2;
        int i5 = i2 + i3;
        for (int i6 = i2; i6 < i5; i6++) {
            if (fArr[i6] >= f2 && fArr[i6] < f3) {
                int i7 = i4;
                i4++;
                fArr[i7] = fArr[i6];
            }
        }
        return i4;
    }

    static float polyLineLength(float[] fArr, int i2, int i3) {
        if (!$assertionsDisabled && (i3 % 2 != 0 || fArr.length < i2 + i3)) {
            throw new AssertionError((Object) "");
        }
        float fLinelen = 0.0f;
        for (int i4 = i2 + 2; i4 < i2 + i3; i4 += 2) {
            fLinelen += linelen(fArr[i4], fArr[i4 + 1], fArr[i4 - 2], fArr[i4 - 1]);
        }
        return fLinelen;
    }

    static float linelen(float f2, float f3, float f4, float f5) {
        float f6 = f4 - f2;
        float f7 = f5 - f3;
        return (float) Math.sqrt((f6 * f6) + (f7 * f7));
    }

    static void subdivide(float[] fArr, int i2, float[] fArr2, int i3, float[] fArr3, int i4, int i5) {
        switch (i5) {
            case 6:
                subdivideQuad(fArr, i2, fArr2, i3, fArr3, i4);
                return;
            case 8:
                subdivideCubic(fArr, i2, fArr2, i3, fArr3, i4);
                return;
            default:
                throw new InternalError("Unsupported curve type");
        }
    }

    static void isort(float[] fArr, int i2, int i3) {
        int i4 = i2 + i3;
        for (int i5 = i2 + 1; i5 < i4; i5++) {
            float f2 = fArr[i5];
            int i6 = i5 - 1;
            while (i6 >= i2 && fArr[i6] > f2) {
                fArr[i6 + 1] = fArr[i6];
                i6--;
            }
            fArr[i6 + 1] = f2;
        }
    }

    static void subdivideCubic(float[] fArr, int i2, float[] fArr2, int i3, float[] fArr3, int i4) {
        float f2 = fArr[i2 + 0];
        float f3 = fArr[i2 + 1];
        float f4 = fArr[i2 + 2];
        float f5 = fArr[i2 + 3];
        float f6 = fArr[i2 + 4];
        float f7 = fArr[i2 + 5];
        float f8 = fArr[i2 + 6];
        float f9 = fArr[i2 + 7];
        if (fArr2 != null) {
            fArr2[i3 + 0] = f2;
            fArr2[i3 + 1] = f3;
        }
        if (fArr3 != null) {
            fArr3[i4 + 6] = f8;
            fArr3[i4 + 7] = f9;
        }
        float f10 = (f2 + f4) / 2.0f;
        float f11 = (f3 + f5) / 2.0f;
        float f12 = (f8 + f6) / 2.0f;
        float f13 = (f9 + f7) / 2.0f;
        float f14 = (f4 + f6) / 2.0f;
        float f15 = (f5 + f7) / 2.0f;
        float f16 = (f10 + f14) / 2.0f;
        float f17 = (f11 + f15) / 2.0f;
        float f18 = (f12 + f14) / 2.0f;
        float f19 = (f13 + f15) / 2.0f;
        float f20 = (f16 + f18) / 2.0f;
        float f21 = (f17 + f19) / 2.0f;
        if (fArr2 != null) {
            fArr2[i3 + 2] = f10;
            fArr2[i3 + 3] = f11;
            fArr2[i3 + 4] = f16;
            fArr2[i3 + 5] = f17;
            fArr2[i3 + 6] = f20;
            fArr2[i3 + 7] = f21;
        }
        if (fArr3 != null) {
            fArr3[i4 + 0] = f20;
            fArr3[i4 + 1] = f21;
            fArr3[i4 + 2] = f18;
            fArr3[i4 + 3] = f19;
            fArr3[i4 + 4] = f12;
            fArr3[i4 + 5] = f13;
        }
    }

    static void subdivideCubicAt(float f2, float[] fArr, int i2, float[] fArr2, int i3, float[] fArr3, int i4) {
        float f3 = fArr[i2 + 0];
        float f4 = fArr[i2 + 1];
        float f5 = fArr[i2 + 2];
        float f6 = fArr[i2 + 3];
        float f7 = fArr[i2 + 4];
        float f8 = fArr[i2 + 5];
        float f9 = fArr[i2 + 6];
        float f10 = fArr[i2 + 7];
        if (fArr2 != null) {
            fArr2[i3 + 0] = f3;
            fArr2[i3 + 1] = f4;
        }
        if (fArr3 != null) {
            fArr3[i4 + 6] = f9;
            fArr3[i4 + 7] = f10;
        }
        float f11 = f3 + (f2 * (f5 - f3));
        float f12 = f4 + (f2 * (f6 - f4));
        float f13 = f7 + (f2 * (f9 - f7));
        float f14 = f8 + (f2 * (f10 - f8));
        float f15 = f5 + (f2 * (f7 - f5));
        float f16 = f6 + (f2 * (f8 - f6));
        float f17 = f11 + (f2 * (f15 - f11));
        float f18 = f12 + (f2 * (f16 - f12));
        float f19 = f15 + (f2 * (f13 - f15));
        float f20 = f16 + (f2 * (f14 - f16));
        float f21 = f17 + (f2 * (f19 - f17));
        float f22 = f18 + (f2 * (f20 - f18));
        if (fArr2 != null) {
            fArr2[i3 + 2] = f11;
            fArr2[i3 + 3] = f12;
            fArr2[i3 + 4] = f17;
            fArr2[i3 + 5] = f18;
            fArr2[i3 + 6] = f21;
            fArr2[i3 + 7] = f22;
        }
        if (fArr3 != null) {
            fArr3[i4 + 0] = f21;
            fArr3[i4 + 1] = f22;
            fArr3[i4 + 2] = f19;
            fArr3[i4 + 3] = f20;
            fArr3[i4 + 4] = f13;
            fArr3[i4 + 5] = f14;
        }
    }

    static void subdivideQuad(float[] fArr, int i2, float[] fArr2, int i3, float[] fArr3, int i4) {
        float f2 = fArr[i2 + 0];
        float f3 = fArr[i2 + 1];
        float f4 = fArr[i2 + 2];
        float f5 = fArr[i2 + 3];
        float f6 = fArr[i2 + 4];
        float f7 = fArr[i2 + 5];
        if (fArr2 != null) {
            fArr2[i3 + 0] = f2;
            fArr2[i3 + 1] = f3;
        }
        if (fArr3 != null) {
            fArr3[i4 + 4] = f6;
            fArr3[i4 + 5] = f7;
        }
        float f8 = (f2 + f4) / 2.0f;
        float f9 = (f3 + f5) / 2.0f;
        float f10 = (f6 + f4) / 2.0f;
        float f11 = (f7 + f5) / 2.0f;
        float f12 = (f8 + f10) / 2.0f;
        float f13 = (f9 + f11) / 2.0f;
        if (fArr2 != null) {
            fArr2[i3 + 2] = f8;
            fArr2[i3 + 3] = f9;
            fArr2[i3 + 4] = f12;
            fArr2[i3 + 5] = f13;
        }
        if (fArr3 != null) {
            fArr3[i4 + 0] = f12;
            fArr3[i4 + 1] = f13;
            fArr3[i4 + 2] = f10;
            fArr3[i4 + 3] = f11;
        }
    }

    static void subdivideQuadAt(float f2, float[] fArr, int i2, float[] fArr2, int i3, float[] fArr3, int i4) {
        float f3 = fArr[i2 + 0];
        float f4 = fArr[i2 + 1];
        float f5 = fArr[i2 + 2];
        float f6 = fArr[i2 + 3];
        float f7 = fArr[i2 + 4];
        float f8 = fArr[i2 + 5];
        if (fArr2 != null) {
            fArr2[i3 + 0] = f3;
            fArr2[i3 + 1] = f4;
        }
        if (fArr3 != null) {
            fArr3[i4 + 4] = f7;
            fArr3[i4 + 5] = f8;
        }
        float f9 = f3 + (f2 * (f5 - f3));
        float f10 = f4 + (f2 * (f6 - f4));
        float f11 = f5 + (f2 * (f7 - f5));
        float f12 = f6 + (f2 * (f8 - f6));
        float f13 = f9 + (f2 * (f11 - f9));
        float f14 = f10 + (f2 * (f12 - f10));
        if (fArr2 != null) {
            fArr2[i3 + 2] = f9;
            fArr2[i3 + 3] = f10;
            fArr2[i3 + 4] = f13;
            fArr2[i3 + 5] = f14;
        }
        if (fArr3 != null) {
            fArr3[i4 + 0] = f13;
            fArr3[i4 + 1] = f14;
            fArr3[i4 + 2] = f11;
            fArr3[i4 + 3] = f12;
        }
    }

    static void subdivideAt(float f2, float[] fArr, int i2, float[] fArr2, int i3, float[] fArr3, int i4, int i5) {
        switch (i5) {
            case 6:
                subdivideQuadAt(f2, fArr, i2, fArr2, i3, fArr3, i4);
                break;
            case 8:
                subdivideCubicAt(f2, fArr, i2, fArr2, i3, fArr3, i4);
                break;
        }
    }
}
