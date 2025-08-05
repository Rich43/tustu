package org.icepdf.core.pobjects.graphics.batik.ext.awt;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import org.icepdf.core.pobjects.graphics.batik.ext.awt.MultipleGradientPaint;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/batik/ext/awt/RadialGradientPaintContext.class */
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
    private float trivial;
    private static final int FIXED_POINT_IMPL = 1;
    private static final int DEFAULT_IMPL = 2;
    private static final int ANTI_ALIAS_IMPL = 3;
    private int fillMethod;
    private static final float SCALEBACK = 0.999f;
    private float invSqStepFloat;
    private static final int MAX_PRECISION = 256;
    private int[] sqrtLutFixed;

    public RadialGradientPaintContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform t2, RenderingHints hints, float cx, float cy, float r2, float fx, float fy, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod, MultipleGradientPaint.ColorSpaceEnum colorSpace) throws NoninvertibleTransformException {
        super(cm, deviceBounds, userBounds, t2, hints, fractions, colors, cycleMethod, colorSpace);
        this.isSimpleFocus = false;
        this.isNonCyclic = false;
        this.sqrtLutFixed = new int[256];
        this.centerX = cx;
        this.centerY = cy;
        this.focusX = fx;
        this.focusY = fy;
        this.radius = r2;
        this.isSimpleFocus = this.focusX == this.centerX && this.focusY == this.centerY;
        this.isNonCyclic = cycleMethod == RadialGradientPaint.NO_CYCLE;
        this.radiusSq = this.radius * this.radius;
        float dX = this.focusX - this.centerX;
        float dY = this.focusY - this.centerY;
        double dist = Math.sqrt((dX * dX) + (dY * dY));
        if (dist > this.radius * SCALEBACK) {
            double angle = Math.atan2(dY, dX);
            this.focusX = ((float) (SCALEBACK * this.radius * Math.cos(angle))) + this.centerX;
            this.focusY = ((float) (SCALEBACK * this.radius * Math.sin(angle))) + this.centerY;
        }
        float dX2 = this.focusX - this.centerX;
        this.trivial = (float) Math.sqrt(this.radiusSq - (dX2 * dX2));
        this.constA = this.a02 - this.centerX;
        this.constB = this.a12 - this.centerY;
        Object colorRend = hints.get(RenderingHints.KEY_COLOR_RENDERING);
        Object rend = hints.get(RenderingHints.KEY_RENDERING);
        this.fillMethod = 0;
        if (rend == RenderingHints.VALUE_RENDER_QUALITY || colorRend == RenderingHints.VALUE_COLOR_RENDER_QUALITY) {
            this.fillMethod = 3;
        }
        if (rend == RenderingHints.VALUE_RENDER_SPEED || colorRend == RenderingHints.VALUE_COLOR_RENDER_SPEED) {
            this.fillMethod = 2;
        }
        if (this.fillMethod == 0) {
            this.fillMethod = 2;
        }
        if (this.fillMethod == 2 && this.isSimpleFocus && this.isNonCyclic && this.isSimpleLookup) {
            calculateFixedPointSqrtLookupTable();
            this.fillMethod = 1;
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.batik.ext.awt.MultipleGradientPaintContext
    protected void fillRaster(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        switch (this.fillMethod) {
            case 1:
                fixedPointSimplestCaseNonCyclicFillRaster(pixels, off, adjust, x2, y2, w2, h2);
                break;
            case 2:
            default:
                cyclicCircularGradientFillRaster(pixels, off, adjust, x2, y2, w2, h2);
                break;
            case 3:
                antiAliasFillRaster(pixels, off, adjust, x2, y2, w2, h2);
                break;
        }
    }

    private void fixedPointSimplestCaseNonCyclicFillRaster(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        float indexFactor = this.fastGradientArraySize / this.radius;
        float constX = (this.a00 * x2) + (this.a01 * y2) + this.constA;
        float constY = (this.a10 * x2) + (this.a11 * y2) + this.constB;
        float deltaX = indexFactor * this.a00;
        float deltaY = indexFactor * this.a10;
        int fixedArraySizeSq = this.fastGradientArraySize * this.fastGradientArraySize;
        int indexer = off;
        float temp = (deltaX * deltaX) + (deltaY * deltaY);
        float gDeltaDelta = temp * 2.0f;
        if (temp > fixedArraySizeSq) {
            int val = this.gradientOverflow;
            for (int j2 = 0; j2 < h2; j2++) {
                int end = indexer + w2;
                while (indexer < end) {
                    pixels[indexer] = val;
                    indexer++;
                }
                indexer += adjust;
            }
            return;
        }
        for (int j3 = 0; j3 < h2; j3++) {
            float dX = indexFactor * ((this.a01 * j3) + constX);
            float dY = indexFactor * ((this.a11 * j3) + constY);
            float g2 = (dY * dY) + (dX * dX);
            float gDelta = (((deltaY * dY) + (deltaX * dX)) * 2.0f) + temp;
            int end2 = indexer + w2;
            while (indexer < end2) {
                if (g2 >= fixedArraySizeSq) {
                    pixels[indexer] = this.gradientOverflow;
                } else {
                    float iSq = g2 * this.invSqStepFloat;
                    int iSqInt = (int) iSq;
                    float iSq2 = iSq - iSqInt;
                    int gIndex = this.sqrtLutFixed[iSqInt];
                    pixels[indexer] = this.gradient[gIndex + ((int) (iSq2 * (this.sqrtLutFixed[iSqInt + 1] - gIndex)))];
                }
                g2 += gDelta;
                gDelta += gDeltaDelta;
                indexer++;
            }
            indexer += adjust;
        }
    }

    private void calculateFixedPointSqrtLookupTable() {
        float sqStepFloat = (this.fastGradientArraySize * this.fastGradientArraySize) / 254.0f;
        int[] workTbl = this.sqrtLutFixed;
        int i2 = 0;
        while (i2 < 255) {
            workTbl[i2] = (int) Math.sqrt(i2 * sqStepFloat);
            i2++;
        }
        workTbl[i2] = workTbl[i2 - 1];
        this.invSqStepFloat = 1.0f / sqStepFloat;
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
                if (X2 - this.focusX > -1.0E-6f && X2 - this.focusX < 1.0E-6f) {
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
                float deltaXSq = ((float) solutionX) - this.focusX;
                float deltaXSq2 = deltaXSq * deltaXSq;
                float deltaYSq = ((float) solutionY2) - this.focusY;
                float intersectToFocusSq = deltaXSq2 + (deltaYSq * deltaYSq);
                float deltaXSq3 = X2 - this.focusX;
                float deltaXSq4 = deltaXSq3 * deltaXSq3;
                float deltaYSq2 = Y2 - this.focusY;
                float currentToFocusSq = deltaXSq4 + (deltaYSq2 * deltaYSq2);
                float g2 = (float) Math.sqrt(currentToFocusSq / intersectToFocusSq);
                pixels[indexer + i2] = indexIntoGradientsArrays(g2);
                X2 += this.a00;
                Y2 += this.a10;
            }
            indexer += pixInc;
        }
    }

    private void antiAliasFillRaster(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        double solutionX;
        double solutionY;
        double solutionX2;
        double d2;
        double d3;
        double solutionX3;
        double d4;
        double d5;
        double constC = (-this.radiusSq) + (this.centerX * this.centerX) + (this.centerY * this.centerY);
        float precalc2 = 2.0f * this.centerY;
        float precalc3 = (-2.0f) * this.centerX;
        float constX = (this.a00 * (x2 - 0.5f)) + (this.a01 * (y2 + 0.5f)) + this.a02;
        float constY = (this.a10 * (x2 - 0.5f)) + (this.a11 * (y2 + 0.5f)) + this.a12;
        int indexer = off - 1;
        double[] prevGs = new double[w2 + 1];
        float X2 = constX - this.a01;
        float Y2 = constY - this.a11;
        for (int i2 = 0; i2 <= w2; i2++) {
            float dx = X2 - this.focusX;
            if (dx > -1.0E-6f && dx < 1.0E-6f) {
                solutionX3 = this.focusX;
                double solutionY2 = this.centerY;
                d4 = solutionY2;
                d5 = Y2 > this.focusY ? this.trivial : -this.trivial;
            } else {
                double slope = (Y2 - this.focusY) / (X2 - this.focusX);
                double yintcpt = Y2 - (slope * X2);
                double A2 = (slope * slope) + 1.0d;
                double B2 = precalc3 + ((-2.0d) * slope * (this.centerY - yintcpt));
                double C2 = constC + (yintcpt * (yintcpt - precalc2));
                double det = Math.sqrt((B2 * B2) - ((4.0d * A2) * C2));
                solutionX3 = ((-B2) + (X2 < this.focusX ? -det : det)) / (2.0d * A2);
                d4 = slope * solutionX3;
                d5 = yintcpt;
            }
            double solutionY3 = d4 + d5;
            double deltaXSq = solutionX3 - this.focusX;
            double deltaXSq2 = deltaXSq * deltaXSq;
            double deltaYSq = solutionY3 - this.focusY;
            double intersectToFocusSq = deltaXSq2 + (deltaYSq * deltaYSq);
            double deltaXSq3 = X2 - this.focusX;
            double deltaXSq4 = deltaXSq3 * deltaXSq3;
            double deltaYSq2 = Y2 - this.focusY;
            double currentToFocusSq = deltaXSq4 + (deltaYSq2 * deltaYSq2);
            prevGs[i2] = Math.sqrt(currentToFocusSq / intersectToFocusSq);
            X2 += this.a00;
            Y2 += this.a10;
        }
        for (int j2 = 0; j2 < h2; j2++) {
            float X3 = (this.a01 * j2) + constX;
            float Y3 = (this.a11 * j2) + constY;
            double g10 = prevGs[0];
            float dx2 = X3 - this.focusX;
            if (dx2 > -1.0E-6f && dx2 < 1.0E-6f) {
                solutionX = this.focusX;
                double solutionY4 = this.centerY;
                solutionY = solutionY4 + (Y3 > this.focusY ? this.trivial : -this.trivial);
            } else {
                double slope2 = (Y3 - this.focusY) / (X3 - this.focusX);
                double yintcpt2 = Y3 - (slope2 * X3);
                double A3 = (slope2 * slope2) + 1.0d;
                double B3 = precalc3 + ((-2.0d) * slope2 * (this.centerY - yintcpt2));
                double C3 = constC + (yintcpt2 * (yintcpt2 - precalc2));
                double det2 = Math.sqrt((B3 * B3) - ((4.0d * A3) * C3));
                solutionX = ((-B3) + (X3 < this.focusX ? -det2 : det2)) / (2.0d * A3);
                solutionY = (slope2 * solutionX) + yintcpt2;
            }
            double deltaXSq5 = solutionX - this.focusX;
            double deltaXSq6 = deltaXSq5 * deltaXSq5;
            double deltaYSq3 = solutionY - this.focusY;
            double intersectToFocusSq2 = deltaXSq6 + (deltaYSq3 * deltaYSq3);
            double deltaXSq7 = X3 - this.focusX;
            double deltaXSq8 = deltaXSq7 * deltaXSq7;
            double deltaYSq4 = Y3 - this.focusY;
            double currentToFocusSq2 = deltaXSq8 + (deltaYSq4 * deltaYSq4);
            double g11 = Math.sqrt(currentToFocusSq2 / intersectToFocusSq2);
            prevGs[0] = g11;
            float X4 = X3 + this.a00;
            float Y4 = Y3 + this.a10;
            for (int i3 = 1; i3 <= w2; i3++) {
                double g00 = g10;
                double g01 = g11;
                g10 = prevGs[i3];
                float dx3 = X4 - this.focusX;
                if (dx3 > -1.0E-6f && dx3 < 1.0E-6f) {
                    solutionX2 = this.focusX;
                    double solutionY5 = this.centerY;
                    d2 = solutionY5;
                    d3 = Y4 > this.focusY ? this.trivial : -this.trivial;
                } else {
                    double slope3 = (Y4 - this.focusY) / (X4 - this.focusX);
                    double yintcpt3 = Y4 - (slope3 * X4);
                    double A4 = (slope3 * slope3) + 1.0d;
                    double B4 = precalc3 + ((-2.0d) * slope3 * (this.centerY - yintcpt3));
                    double C4 = constC + (yintcpt3 * (yintcpt3 - precalc2));
                    double det3 = Math.sqrt((B4 * B4) - ((4.0d * A4) * C4));
                    solutionX2 = ((-B4) + (X4 < this.focusX ? -det3 : det3)) / (2.0d * A4);
                    d2 = slope3 * solutionX2;
                    d3 = yintcpt3;
                }
                double solutionY6 = d2 + d3;
                double deltaXSq9 = solutionX2 - this.focusX;
                double deltaXSq10 = deltaXSq9 * deltaXSq9;
                double deltaYSq5 = solutionY6 - this.focusY;
                double intersectToFocusSq3 = deltaXSq10 + (deltaYSq5 * deltaYSq5);
                double deltaXSq11 = X4 - this.focusX;
                double deltaXSq12 = deltaXSq11 * deltaXSq11;
                double deltaYSq6 = Y4 - this.focusY;
                double currentToFocusSq3 = deltaXSq12 + (deltaYSq6 * deltaYSq6);
                g11 = Math.sqrt(currentToFocusSq3 / intersectToFocusSq3);
                prevGs[i3] = g11;
                pixels[indexer + i3] = indexGradientAntiAlias((float) ((((g00 + g01) + g10) + g11) / 4.0d), (float) Math.max(Math.abs(g11 - g00), Math.abs(g10 - g01)));
                X4 += this.a00;
                Y4 += this.a10;
            }
            indexer += w2 + adjust;
        }
    }
}
