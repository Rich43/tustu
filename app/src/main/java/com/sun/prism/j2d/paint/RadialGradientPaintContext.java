package com.sun.prism.j2d.paint;

import com.sun.prism.j2d.paint.MultipleGradientPaint;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/paint/RadialGradientPaintContext.class */
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

    RadialGradientPaintContext(RadialGradientPaint paint, ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform t2, RenderingHints hints, float cx, float cy, float r2, float fx, float fy, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpace) {
        super(paint, cm, deviceBounds, userBounds, t2, hints, fractions, colors, cycleMethod, colorSpace);
        this.isSimpleFocus = false;
        this.isNonCyclic = false;
        this.centerX = cx;
        this.centerY = cy;
        this.focusX = fx;
        this.focusY = fy;
        this.radius = r2;
        this.isSimpleFocus = this.focusX == this.centerX && this.focusY == this.centerY;
        this.isNonCyclic = cycleMethod == MultipleGradientPaint.CycleMethod.NO_CYCLE;
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

    @Override // com.sun.prism.j2d.paint.MultipleGradientPaintContext
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
        double solutionY;
        double constC = (-this.radiusSq) + (this.centerX * this.centerX) + (this.centerY * this.centerY);
        float constX = (this.a00 * x2) + (this.a01 * y2) + this.a02;
        float constY = (this.a10 * x2) + (this.a11 * y2) + this.a12;
        float precalc2 = 2.0f * this.centerY;
        float precalc3 = (-2.0f) * this.centerX;
        int indexer = off;
        int pixInc = w2 + adjust;
        if (this.trivial == 0.0f) {
            int rgb0 = indexIntoGradientsArrays(0.0f);
            for (int j2 = 0; j2 < h2; j2++) {
                for (int i2 = 0; i2 < w2; i2++) {
                    pixels[indexer + i2] = rgb0;
                }
                indexer += pixInc;
            }
            return;
        }
        for (int j3 = 0; j3 < h2; j3++) {
            float X2 = (this.a01 * j3) + constX;
            float Y2 = (this.a11 * j3) + constY;
            for (int i3 = 0; i3 < w2; i3++) {
                if (X2 == this.focusX) {
                    solutionX = this.focusX;
                    double solutionY2 = this.centerY;
                    solutionY = solutionY2 + (Y2 > this.focusY ? this.trivial : -this.trivial);
                } else {
                    double slope = (Y2 - this.focusY) / (X2 - this.focusX);
                    double yintcpt = Y2 - (slope * X2);
                    double A2 = (slope * slope) + 1.0d;
                    double B2 = precalc3 + ((-2.0d) * slope * (this.centerY - yintcpt));
                    double C2 = constC + (yintcpt * (yintcpt - precalc2));
                    float det = (float) Math.sqrt((B2 * B2) - ((4.0d * A2) * C2));
                    solutionX = ((-B2) + (X2 < this.focusX ? -det : det)) / (2.0d * A2);
                    solutionY = (slope * solutionX) + yintcpt;
                }
                float deltaXSq = X2 - this.focusX;
                float deltaXSq2 = deltaXSq * deltaXSq;
                float deltaYSq = Y2 - this.focusY;
                float currentToFocusSq = deltaXSq2 + (deltaYSq * deltaYSq);
                float deltaXSq3 = ((float) solutionX) - this.focusX;
                float deltaXSq4 = deltaXSq3 * deltaXSq3;
                float deltaYSq2 = ((float) solutionY) - this.focusY;
                float intersectToFocusSq = deltaXSq4 + (deltaYSq2 * deltaYSq2);
                if (intersectToFocusSq == 0.0f) {
                    intersectToFocusSq = solutionY >= ((double) this.focusY) ? this.trivial : -this.trivial;
                }
                float g2 = (float) Math.sqrt(currentToFocusSq / intersectToFocusSq);
                pixels[indexer + i3] = indexIntoGradientsArrays(g2);
                X2 += this.a00;
                Y2 += this.a10;
            }
            indexer += pixInc;
        }
    }
}
