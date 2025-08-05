package java.awt;

import java.awt.MultipleGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/* loaded from: rt.jar:java/awt/RadialGradientPaintContext.class */
final class RadialGradientPaintContext extends MultipleGradientPaintContext {
    private boolean isSimpleFocus;
    private boolean isNonCyclic;
    private float radius;
    private float centerX;
    private float centerY;
    private float focusX;
    private float focusY;
    private float radiusSq;
    private float constA;
    private float constB;
    private float gDeltaDelta;
    private float trivial;
    private static final float SCALEBACK = 0.99f;
    private static final int SQRT_LUT_SIZE = 2048;
    private static float[] sqrtLut = new float[2049];

    RadialGradientPaintContext(RadialGradientPaint radialGradientPaint, ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints, float f2, float f3, float f4, float f5, float f6, float[] fArr, Color[] colorArr, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpaceType) {
        super(radialGradientPaint, colorModel, rectangle, rectangle2D, affineTransform, renderingHints, fArr, colorArr, cycleMethod, colorSpaceType);
        this.isSimpleFocus = false;
        this.isNonCyclic = false;
        this.centerX = f2;
        this.centerY = f3;
        this.focusX = f5;
        this.focusY = f6;
        this.radius = f4;
        this.isSimpleFocus = this.focusX == this.centerX && this.focusY == this.centerY;
        this.isNonCyclic = cycleMethod == MultipleGradientPaint.CycleMethod.NO_CYCLE;
        this.radiusSq = this.radius * this.radius;
        float f7 = this.focusX - this.centerX;
        float f8 = this.focusY - this.centerY;
        double d2 = (f7 * f7) + (f8 * f8);
        if (d2 > this.radiusSq * SCALEBACK) {
            float fSqrt = (float) Math.sqrt((this.radiusSq * SCALEBACK) / d2);
            f7 *= fSqrt;
            this.focusX = this.centerX + f7;
            this.focusY = this.centerY + (f8 * fSqrt);
        }
        this.trivial = (float) Math.sqrt(this.radiusSq - (f7 * f7));
        this.constA = this.a02 - this.centerX;
        this.constB = this.a12 - this.centerY;
        this.gDeltaDelta = (2.0f * ((this.a00 * this.a00) + (this.a10 * this.a10))) / this.radiusSq;
    }

    @Override // java.awt.MultipleGradientPaintContext
    protected void fillRaster(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (this.isSimpleFocus && this.isNonCyclic && this.isSimpleLookup) {
            simpleNonCyclicFillRaster(iArr, i2, i3, i4, i5, i6, i7);
        } else {
            cyclicCircularGradientFillRaster(iArr, i2, i3, i4, i5, i6, i7);
        }
    }

    private void simpleNonCyclicFillRaster(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8;
        float f2 = (this.a00 * i4) + (this.a01 * i5) + this.constA;
        float f3 = (this.a10 * i4) + (this.a11 * i5) + this.constB;
        float f4 = this.gDeltaDelta;
        int i9 = i3 + i6;
        int i10 = this.gradient[this.fastGradientArraySize];
        for (int i11 = 0; i11 < i7; i11++) {
            float f5 = ((f2 * f2) + (f3 * f3)) / this.radiusSq;
            float f6 = ((2.0f * ((this.a00 * f2) + (this.a10 * f3))) / this.radiusSq) + (f4 / 2.0f);
            int i12 = 0;
            while (i12 < i6 && f5 >= 1.0f) {
                iArr[i2 + i12] = i10;
                f5 += f6;
                f6 += f4;
                i12++;
            }
            while (i12 < i6 && f5 < 1.0f) {
                if (f5 <= 0.0f) {
                    i8 = 0;
                } else {
                    float f7 = f5 * 2048.0f;
                    int i13 = (int) f7;
                    float f8 = sqrtLut[i13];
                    i8 = (int) ((f8 + ((f7 - i13) * (sqrtLut[i13 + 1] - f8))) * this.fastGradientArraySize);
                }
                iArr[i2 + i12] = this.gradient[i8];
                f5 += f6;
                f6 += f4;
                i12++;
            }
            while (i12 < i6) {
                iArr[i2 + i12] = i10;
                i12++;
            }
            i2 += i9;
            f2 += this.a01;
            f3 += this.a11;
        }
    }

    static {
        for (int i2 = 0; i2 < sqrtLut.length; i2++) {
            sqrtLut[i2] = (float) Math.sqrt(i2 / 2048.0f);
        }
    }

    private void cyclicCircularGradientFillRaster(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        double dSqrt;
        double d2;
        double d3;
        double d4 = (-this.radiusSq) + (this.centerX * this.centerX) + (this.centerY * this.centerY);
        float f2 = (this.a00 * i4) + (this.a01 * i5) + this.a02;
        float f3 = (this.a10 * i4) + (this.a11 * i5) + this.a12;
        float f4 = 2.0f * this.centerY;
        float f5 = (-2.0f) * this.centerX;
        int i8 = i2;
        int i9 = i6 + i3;
        for (int i10 = 0; i10 < i7; i10++) {
            float f6 = (this.a01 * i10) + f2;
            float f7 = (this.a11 * i10) + f3;
            for (int i11 = 0; i11 < i6; i11++) {
                if (f6 == this.focusX) {
                    dSqrt = this.focusX;
                    d2 = this.centerY;
                    d3 = f7 > this.focusY ? this.trivial : -this.trivial;
                } else {
                    double d5 = (f7 - this.focusY) / (f6 - this.focusX);
                    double d6 = f7 - (d5 * f6);
                    double d7 = (d5 * d5) + 1.0d;
                    double d8 = f5 + ((-2.0d) * d5 * (this.centerY - d6));
                    dSqrt = ((-d8) + (f6 < this.focusX ? -r0 : (float) Math.sqrt((d8 * d8) - ((4.0d * d7) * (d4 + (d6 * (d6 - f4))))))) / (2.0d * d7);
                    d2 = d5 * dSqrt;
                    d3 = d6;
                }
                double d9 = d2 + d3;
                float f8 = f6 - this.focusX;
                float f9 = f8 * f8;
                float f10 = f7 - this.focusY;
                float f11 = f9 + (f10 * f10);
                float f12 = ((float) dSqrt) - this.focusX;
                float f13 = f12 * f12;
                float f14 = ((float) d9) - this.focusY;
                iArr[i8 + i11] = indexIntoGradientsArrays((float) Math.sqrt(f11 / (f13 + (f14 * f14))));
                f6 += this.a00;
                f7 += this.a10;
            }
            i8 += i9;
        }
    }
}
