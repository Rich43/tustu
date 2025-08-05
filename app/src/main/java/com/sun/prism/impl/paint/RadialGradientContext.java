package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.RadialGradient;

/* loaded from: jfxrt.jar:com/sun/prism/impl/paint/RadialGradientContext.class */
final class RadialGradientContext extends MultipleGradientContext {
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

    RadialGradientContext(RadialGradient paint, BaseTransform t2, float cx, float cy, float r2, float fx, float fy, float[] fractions, Color[] colors, int cycleMethod) {
        super(paint, t2, fractions, colors, cycleMethod);
        this.isSimpleFocus = false;
        this.isNonCyclic = false;
        this.centerX = cx;
        this.centerY = cy;
        this.focusX = fx;
        this.focusY = fy;
        this.radius = r2;
        this.isSimpleFocus = this.focusX == this.centerX && this.focusY == this.centerY;
        this.isNonCyclic = cycleMethod == 0;
        this.radiusSq = this.radius * this.radius;
        float dX = this.focusX - this.centerX;
        float dY = this.focusY - this.centerY;
        double distSq = (dX * dX) + (dY * dY);
        if (distSq > this.radiusSq * SCALEBACK) {
            float scalefactor = (float) Math.sqrt((this.radiusSq * SCALEBACK) / distSq);
            dX *= scalefactor;
            this.focusX = this.centerX + dX;
            this.focusY = this.centerY + (dY * scalefactor);
        }
        this.trivial = (float) Math.sqrt(this.radiusSq - (dX * dX));
        this.constA = this.a02 - this.centerX;
        this.constB = this.a12 - this.centerY;
        this.gDeltaDelta = (2.0f * ((this.a00 * this.a00) + (this.a10 * this.a10))) / this.radiusSq;
    }

    @Override // com.sun.prism.impl.paint.MultipleGradientContext
    protected void fillRaster(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        if (this.isSimpleFocus && this.isNonCyclic && this.isSimpleLookup) {
            simpleNonCyclicFillRaster(pixels, off, adjust, x2, y2, w2, h2);
        } else {
            cyclicCircularGradientFillRaster(pixels, off, adjust, x2, y2, w2, h2);
        }
    }

    private void simpleNonCyclicFillRaster(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        int i2;
        float rowX = (this.a00 * x2) + (this.a01 * y2) + this.constA;
        float rowY = (this.a10 * x2) + (this.a11 * y2) + this.constB;
        float gDeltaDelta = this.gDeltaDelta;
        int adjust2 = adjust + w2;
        int rgbclip = this.gradient[this.fastGradientArraySize];
        for (int j2 = 0; j2 < h2; j2++) {
            float gRel = ((rowX * rowX) + (rowY * rowY)) / this.radiusSq;
            float gDelta = ((2.0f * ((this.a00 * rowX) + (this.a10 * rowY))) / this.radiusSq) + (gDeltaDelta / 2.0f);
            int i3 = 0;
            while (i3 < w2 && gRel >= 1.0f) {
                pixels[off + i3] = rgbclip;
                gRel += gDelta;
                gDelta += gDeltaDelta;
                i3++;
            }
            while (i3 < w2 && gRel < 1.0f) {
                if (gRel <= 0.0f) {
                    i2 = 0;
                } else {
                    float fIndex = gRel * 2048.0f;
                    int iIndex = (int) fIndex;
                    float s0 = sqrtLut[iIndex];
                    float s1 = sqrtLut[iIndex + 1] - s0;
                    i2 = (int) ((s0 + ((fIndex - iIndex) * s1)) * this.fastGradientArraySize);
                }
                int gIndex = i2;
                pixels[off + i3] = this.gradient[gIndex];
                gRel += gDelta;
                gDelta += gDeltaDelta;
                i3++;
            }
            while (i3 < w2) {
                pixels[off + i3] = rgbclip;
                i3++;
            }
            off += adjust2;
            rowX += this.a01;
            rowY += this.a11;
        }
    }

    static {
        for (int i2 = 0; i2 < sqrtLut.length; i2++) {
            sqrtLut[i2] = (float) Math.sqrt(i2 / 2048.0f);
        }
    }

    private void cyclicCircularGradientFillRaster(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        double solutionX;
        double d2;
        double d3;
        double constC = (-this.radiusSq) + (this.centerX * this.centerX) + (this.centerY * this.centerY);
        float constX = (this.a00 * x2) + (this.a01 * y2) + this.a02;
        float constY = (this.a10 * x2) + (this.a11 * y2) + this.a12;
        float precalc2 = 2.0f * this.centerY;
        float precalc3 = (-2.0f) * this.centerX;
        int indexer = off;
        int pixInc = w2 + adjust;
        for (int j2 = 0; j2 < h2; j2++) {
            float X2 = (this.a01 * j2) + constX;
            float Y2 = (this.a11 * j2) + constY;
            for (int i2 = 0; i2 < w2; i2++) {
                if (X2 == this.focusX) {
                    solutionX = this.focusX;
                    double solutionY = this.centerY;
                    d2 = solutionY;
                    d3 = Y2 > this.focusY ? this.trivial : -this.trivial;
                } else {
                    double slope = (Y2 - this.focusY) / (X2 - this.focusX);
                    double yintcpt = Y2 - (slope * X2);
                    double A2 = (slope * slope) + 1.0d;
                    double B2 = precalc3 + ((-2.0d) * slope * (this.centerY - yintcpt));
                    double C2 = constC + (yintcpt * (yintcpt - precalc2));
                    float det = (float) Math.sqrt((B2 * B2) - ((4.0d * A2) * C2));
                    solutionX = ((-B2) + (X2 < this.focusX ? -det : det)) / (2.0d * A2);
                    d2 = slope * solutionX;
                    d3 = yintcpt;
                }
                double solutionY2 = d2 + d3;
                float deltaXSq = X2 - this.focusX;
                float deltaXSq2 = deltaXSq * deltaXSq;
                float deltaYSq = Y2 - this.focusY;
                float currentToFocusSq = deltaXSq2 + (deltaYSq * deltaYSq);
                float deltaXSq3 = ((float) solutionX) - this.focusX;
                float deltaXSq4 = deltaXSq3 * deltaXSq3;
                float deltaYSq2 = ((float) solutionY2) - this.focusY;
                float intersectToFocusSq = deltaXSq4 + (deltaYSq2 * deltaYSq2);
                float g2 = (float) Math.sqrt(currentToFocusSq / intersectToFocusSq);
                pixels[indexer + i2] = indexIntoGradientsArrays(g2);
                X2 += this.a00;
                Y2 += this.a10;
            }
            indexer += pixInc;
        }
    }
}
