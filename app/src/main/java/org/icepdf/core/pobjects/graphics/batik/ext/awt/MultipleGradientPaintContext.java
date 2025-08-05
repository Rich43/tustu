package org.icepdf.core.pobjects.graphics.batik.ext.awt;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.lang.ref.WeakReference;
import org.icepdf.core.pobjects.graphics.batik.ext.awt.MultipleGradientPaint;
import org.icepdf.core.pobjects.graphics.batik.ext.awt.image.GraphicsUtil;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/batik/ext/awt/MultipleGradientPaintContext.class */
abstract class MultipleGradientPaintContext implements PaintContext {
    protected static final boolean DEBUG = false;
    protected ColorModel dataModel;
    protected ColorModel model;
    protected static ColorModel cachedModel;
    protected static WeakReference<WritableRaster> cached;
    protected WritableRaster saved;
    protected MultipleGradientPaint.CycleMethodEnum cycleMethod;
    protected MultipleGradientPaint.ColorSpaceEnum colorSpace;
    protected float a00;
    protected float a01;
    protected float a10;
    protected float a11;
    protected float a02;
    protected float a12;
    protected boolean isSimpleLookup = true;
    protected boolean hasDiscontinuity;
    protected int fastGradientArraySize;
    protected int[] gradient;
    protected int[][] gradients;
    protected int gradientAverage;
    protected int gradientUnderflow;
    protected int gradientOverflow;
    protected int gradientsLength;
    protected float[] normalizedIntervals;
    protected float[] fractions;
    private int transparencyTest;
    protected static final int GRADIENT_SIZE = 256;
    protected static final int GRADIENT_SIZE_INDEX = 255;
    private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;
    private static ColorModel lrgbmodel_NA = new DirectColorModel(ColorSpace.getInstance(1004), 24, 16711680, NormalizerImpl.CC_MASK, 255, 0, false, 3);
    private static ColorModel srgbmodel_NA = new DirectColorModel(ColorSpace.getInstance(1000), 24, 16711680, NormalizerImpl.CC_MASK, 255, 0, false, 3);
    private static ColorModel graybmodel_NA = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{1}, false, false, 1, 3);
    private static ColorModel lrgbmodel_A = new DirectColorModel(ColorSpace.getInstance(1004), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, false, 3);
    private static ColorModel srgbmodel_A = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, false, 3);
    private static ColorModel graybmodel_A = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{1, 1}, true, false, 3, 3);
    private static final int[] SRGBtoLinearRGB = new int[256];
    private static final int[] LinearRGBtoSRGB = new int[256];

    protected abstract void fillRaster(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7);

    static {
        for (int k2 = 0; k2 < 256; k2++) {
            SRGBtoLinearRGB[k2] = convertSRGBtoLinearRGB(k2);
            LinearRGBtoSRGB[k2] = convertLinearRGBtoSRGB(k2);
        }
    }

    protected MultipleGradientPaintContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform t2, RenderingHints hints, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod, MultipleGradientPaint.ColorSpaceEnum colorSpace) throws NoninvertibleTransformException {
        this.hasDiscontinuity = false;
        boolean fixFirst = false;
        boolean fixLast = false;
        int len = fractions.length;
        if (fractions[0] != 0.0f) {
            fixFirst = true;
            len++;
        }
        if (fractions[fractions.length - 1] != 1.0f) {
            fixLast = true;
            len++;
        }
        for (int i2 = 0; i2 < fractions.length - 1; i2++) {
            if (fractions[i2] == fractions[i2 + 1]) {
                len--;
            }
        }
        this.fractions = new float[len];
        Color[] loColors = new Color[len - 1];
        Color[] hiColors = new Color[len - 1];
        this.normalizedIntervals = new float[len - 1];
        this.gradientUnderflow = colors[0].getRGB();
        this.gradientOverflow = colors[colors.length - 1].getRGB();
        int idx = 0;
        if (fixFirst) {
            this.fractions[0] = 0.0f;
            loColors[0] = colors[0];
            hiColors[0] = colors[0];
            this.normalizedIntervals[0] = fractions[0];
            idx = 0 + 1;
        }
        for (int i3 = 0; i3 < fractions.length - 1; i3++) {
            if (fractions[i3] == fractions[i3 + 1]) {
                if (!colors[i3].equals(colors[i3 + 1])) {
                    this.hasDiscontinuity = true;
                }
            } else {
                this.fractions[idx] = fractions[i3];
                loColors[idx] = colors[i3];
                hiColors[idx] = colors[i3 + 1];
                this.normalizedIntervals[idx] = fractions[i3 + 1] - fractions[i3];
                idx++;
            }
        }
        this.fractions[idx] = fractions[fractions.length - 1];
        if (fixLast) {
            Color color = colors[colors.length - 1];
            hiColors[idx] = color;
            loColors[idx] = color;
            this.normalizedIntervals[idx] = 1.0f - fractions[fractions.length - 1];
            this.fractions[idx + 1] = 1.0f;
        }
        AffineTransform tInv = t2.createInverse();
        double[] m2 = new double[6];
        tInv.getMatrix(m2);
        this.a00 = (float) m2[0];
        this.a10 = (float) m2[1];
        this.a01 = (float) m2[2];
        this.a11 = (float) m2[3];
        this.a02 = (float) m2[4];
        this.a12 = (float) m2[5];
        this.cycleMethod = cycleMethod;
        this.colorSpace = colorSpace;
        if (cm.getColorSpace() == lrgbmodel_A.getColorSpace()) {
            this.dataModel = lrgbmodel_A;
        } else if (cm.getColorSpace() == srgbmodel_A.getColorSpace() || cm.getColorSpace() == graybmodel_A.getColorSpace()) {
            this.dataModel = srgbmodel_A;
        } else {
            throw new IllegalArgumentException("Unsupported ColorSpace for interpolation");
        }
        calculateGradientFractions(loColors, hiColors);
        this.model = GraphicsUtil.coerceColorModel(this.dataModel, cm.isAlphaPremultiplied());
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [int[], int[][]] */
    protected final void calculateGradientFractions(Color[] loColors, Color[] hiColors) {
        if (this.colorSpace == LinearGradientPaint.LINEAR_RGB) {
            int[] workTbl = SRGBtoLinearRGB;
            for (int i2 = 0; i2 < loColors.length; i2++) {
                loColors[i2] = interpolateColor(workTbl, loColors[i2]);
                hiColors[i2] = interpolateColor(workTbl, hiColors[i2]);
            }
        }
        this.transparencyTest = -16777216;
        if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
            this.transparencyTest &= this.gradientUnderflow;
            this.transparencyTest &= this.gradientOverflow;
        }
        this.gradients = new int[this.fractions.length - 1];
        this.gradientsLength = this.gradients.length;
        int n2 = this.normalizedIntervals.length;
        float Imin = 1.0f;
        float[] workTbl2 = this.normalizedIntervals;
        for (int i3 = 0; i3 < n2; i3++) {
            Imin = Imin > workTbl2[i3] ? workTbl2[i3] : Imin;
        }
        int estimatedSize = 0;
        if (Imin == 0.0f) {
            estimatedSize = Integer.MAX_VALUE;
            this.hasDiscontinuity = true;
        } else {
            for (float f2 : workTbl2) {
                estimatedSize = (int) (estimatedSize + ((f2 / Imin) * 256.0f));
            }
        }
        if (estimatedSize > 5000) {
            calculateMultipleArrayGradient(loColors, hiColors);
            if (this.cycleMethod == MultipleGradientPaint.REPEAT && this.gradients[0][0] != this.gradients[this.gradients.length - 1][255]) {
                this.hasDiscontinuity = true;
            }
        } else {
            calculateSingleArrayGradient(loColors, hiColors, Imin);
            if (this.cycleMethod == MultipleGradientPaint.REPEAT && this.gradient[0] != this.gradient[this.fastGradientArraySize]) {
                this.hasDiscontinuity = true;
            }
        }
        if ((this.transparencyTest >>> 24) == 255) {
            if (this.dataModel.getColorSpace() == lrgbmodel_NA.getColorSpace()) {
                this.dataModel = lrgbmodel_NA;
            } else if (this.dataModel.getColorSpace() == srgbmodel_NA.getColorSpace()) {
                this.dataModel = srgbmodel_NA;
            } else if (this.dataModel.getColorSpace() == graybmodel_NA.getColorSpace()) {
                this.dataModel = graybmodel_NA;
            }
            this.model = this.dataModel;
        }
    }

    private static Color interpolateColor(int[] workTbl, Color inColor) {
        int oldColor = inColor.getRGB();
        int newColorValue = ((workTbl[(oldColor >> 24) & 255] & 255) << 24) | ((workTbl[(oldColor >> 16) & 255] & 255) << 16) | ((workTbl[(oldColor >> 8) & 255] & 255) << 8) | (workTbl[oldColor & 255] & 255);
        return new Color(newColorValue, true);
    }

    private void calculateSingleArrayGradient(Color[] loColors, Color[] hiColors, float Imin) {
        this.isSimpleLookup = true;
        int gradientsTot = 1;
        int aveA = 32768;
        int aveR = 32768;
        int aveG = 32768;
        int aveB = 32768;
        for (int i2 = 0; i2 < this.gradients.length; i2++) {
            int nGradients = (int) ((this.normalizedIntervals[i2] / Imin) * 255.0f);
            gradientsTot += nGradients;
            this.gradients[i2] = new int[nGradients];
            int rgb1 = loColors[i2].getRGB();
            int rgb2 = hiColors[i2].getRGB();
            interpolate(rgb1, rgb2, this.gradients[i2]);
            int argb = this.gradients[i2][128];
            float norm = this.normalizedIntervals[i2];
            aveA += (int) (((argb >> 8) & 16711680) * norm);
            aveR += (int) ((argb & 16711680) * norm);
            aveG += (int) (((argb << 8) & 16711680) * norm);
            aveB += (int) (((argb << 16) & 16711680) * norm);
            this.transparencyTest &= rgb1 & rgb2;
        }
        this.gradientAverage = ((aveA & 16711680) << 8) | (aveR & 16711680) | ((aveG & 16711680) >> 8) | ((aveB & 16711680) >> 16);
        this.gradient = new int[gradientsTot];
        int curOffset = 0;
        for (int i3 = 0; i3 < this.gradients.length; i3++) {
            System.arraycopy(this.gradients[i3], 0, this.gradient, curOffset, this.gradients[i3].length);
            curOffset += this.gradients[i3].length;
        }
        this.gradient[this.gradient.length - 1] = hiColors[hiColors.length - 1].getRGB();
        if (this.colorSpace == LinearGradientPaint.LINEAR_RGB) {
            if (this.dataModel.getColorSpace() == ColorSpace.getInstance(1000)) {
                for (int i4 = 0; i4 < this.gradient.length; i4++) {
                    this.gradient[i4] = convertEntireColorLinearRGBtoSRGB(this.gradient[i4]);
                }
                this.gradientAverage = convertEntireColorLinearRGBtoSRGB(this.gradientAverage);
            }
        } else if (this.dataModel.getColorSpace() == ColorSpace.getInstance(1004)) {
            for (int i5 = 0; i5 < this.gradient.length; i5++) {
                this.gradient[i5] = convertEntireColorSRGBtoLinearRGB(this.gradient[i5]);
            }
            this.gradientAverage = convertEntireColorSRGBtoLinearRGB(this.gradientAverage);
        }
        this.fastGradientArraySize = this.gradient.length - 1;
    }

    private void calculateMultipleArrayGradient(Color[] loColors, Color[] hiColors) {
        this.isSimpleLookup = false;
        int aveA = 32768;
        int aveR = 32768;
        int aveG = 32768;
        int aveB = 32768;
        for (int i2 = 0; i2 < this.gradients.length; i2++) {
            if (this.normalizedIntervals[i2] != 0.0f) {
                this.gradients[i2] = new int[256];
                int rgb1 = loColors[i2].getRGB();
                int rgb2 = hiColors[i2].getRGB();
                interpolate(rgb1, rgb2, this.gradients[i2]);
                int argb = this.gradients[i2][128];
                float norm = this.normalizedIntervals[i2];
                aveA += (int) (((argb >> 8) & 16711680) * norm);
                aveR += (int) ((argb & 16711680) * norm);
                aveG += (int) (((argb << 8) & 16711680) * norm);
                aveB += (int) (((argb << 16) & 16711680) * norm);
                this.transparencyTest &= rgb1;
                this.transparencyTest &= rgb2;
            }
        }
        this.gradientAverage = ((aveA & 16711680) << 8) | (aveR & 16711680) | ((aveG & 16711680) >> 8) | ((aveB & 16711680) >> 16);
        if (this.colorSpace == LinearGradientPaint.LINEAR_RGB) {
            if (this.dataModel.getColorSpace() == ColorSpace.getInstance(1000)) {
                for (int j2 = 0; j2 < this.gradients.length; j2++) {
                    for (int i3 = 0; i3 < this.gradients[j2].length; i3++) {
                        this.gradients[j2][i3] = convertEntireColorLinearRGBtoSRGB(this.gradients[j2][i3]);
                    }
                }
                this.gradientAverage = convertEntireColorLinearRGBtoSRGB(this.gradientAverage);
                return;
            }
            return;
        }
        if (this.dataModel.getColorSpace() == ColorSpace.getInstance(1004)) {
            for (int j3 = 0; j3 < this.gradients.length; j3++) {
                for (int i4 = 0; i4 < this.gradients[j3].length; i4++) {
                    this.gradients[j3][i4] = convertEntireColorSRGBtoLinearRGB(this.gradients[j3][i4]);
                }
            }
            this.gradientAverage = convertEntireColorSRGBtoLinearRGB(this.gradientAverage);
        }
    }

    private void interpolate(int rgb1, int rgb2, int[] output) {
        int nSteps = output.length;
        float stepSize = 1.0f / nSteps;
        int a1 = (rgb1 >> 24) & 255;
        int r1 = (rgb1 >> 16) & 255;
        int g1 = (rgb1 >> 8) & 255;
        int b1 = rgb1 & 255;
        int da = ((rgb2 >> 24) & 255) - a1;
        int dr = ((rgb2 >> 16) & 255) - r1;
        int dg = ((rgb2 >> 8) & 255) - g1;
        int db = (rgb2 & 255) - b1;
        float tempA = 2.0f * da * stepSize;
        float tempR = 2.0f * dr * stepSize;
        float tempG = 2.0f * dg * stepSize;
        float tempB = 2.0f * db * stepSize;
        output[0] = rgb1;
        int nSteps2 = nSteps - 1;
        output[nSteps2] = rgb2;
        for (int i2 = 1; i2 < nSteps2; i2++) {
            output[i2] = (((a1 + ((((int) (i2 * tempA)) + 1) >> 1)) & 255) << 24) | (((r1 + ((((int) (i2 * tempR)) + 1) >> 1)) & 255) << 16) | (((g1 + ((((int) (i2 * tempG)) + 1) >> 1)) & 255) << 8) | ((b1 + ((((int) (i2 * tempB)) + 1) >> 1)) & 255);
        }
    }

    private static int convertEntireColorLinearRGBtoSRGB(int rgb) {
        int a1 = (rgb >> 24) & 255;
        int r1 = (rgb >> 16) & 255;
        int g1 = (rgb >> 8) & 255;
        int b1 = rgb & 255;
        int[] workTbl = LinearRGBtoSRGB;
        int r12 = workTbl[r1];
        int g12 = workTbl[g1];
        return (a1 << 24) | (r12 << 16) | (g12 << 8) | workTbl[b1];
    }

    private static int convertEntireColorSRGBtoLinearRGB(int rgb) {
        int a1 = (rgb >> 24) & 255;
        int r1 = (rgb >> 16) & 255;
        int g1 = (rgb >> 8) & 255;
        int b1 = rgb & 255;
        int[] workTbl = SRGBtoLinearRGB;
        int r12 = workTbl[r1];
        int g12 = workTbl[g1];
        return (a1 << 24) | (r12 << 16) | (g12 << 8) | workTbl[b1];
    }

    protected final int indexIntoGradientsArrays(float position) {
        if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
            if (position >= 1.0f) {
                return this.gradientOverflow;
            }
            if (position <= 0.0f) {
                return this.gradientUnderflow;
            }
        } else {
            if (this.cycleMethod == MultipleGradientPaint.REPEAT) {
                float position2 = position - ((int) position);
                if (position2 < 0.0f) {
                    position2 += 1.0f;
                }
                int w2 = 0;
                int c1 = 0;
                int c2 = 0;
                if (this.isSimpleLookup) {
                    float position3 = position2 * this.gradient.length;
                    int idx1 = (int) position3;
                    if (idx1 + 1 < this.gradient.length) {
                        return this.gradient[idx1];
                    }
                    w2 = (int) ((position3 - idx1) * 65536.0f);
                    c1 = this.gradient[idx1];
                    c2 = this.gradient[0];
                } else {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= this.gradientsLength) {
                            break;
                        }
                        if (position2 >= this.fractions[i2 + 1]) {
                            i2++;
                        } else {
                            float delta = ((position2 - this.fractions[i2]) / this.normalizedIntervals[i2]) * 256.0f;
                            int index = (int) delta;
                            if (index + 1 < this.gradients[i2].length || i2 + 1 < this.gradientsLength) {
                                return this.gradients[i2][index];
                            }
                            w2 = (int) ((delta - index) * 65536.0f);
                            c1 = this.gradients[i2][index];
                            c2 = this.gradients[0][0];
                        }
                    }
                }
                return (((((c1 >> 8) & 16711680) + (((c2 >>> 24) - (c1 >>> 24)) * w2)) & 16711680) << 8) | (((c1 & 16711680) + ((((c2 >> 16) & 255) - ((c1 >> 16) & 255)) * w2)) & 16711680) | (((((c1 << 8) & 16711680) + ((((c2 >> 8) & 255) - ((c1 >> 8) & 255)) * w2)) & 16711680) >> 8) | (((((c1 << 16) & 16711680) + (((c2 & 255) - (c1 & 255)) * w2)) & 16711680) >> 16);
            }
            if (position < 0.0f) {
                position = -position;
            }
            int part = (int) position;
            position -= part;
            if ((part & 1) == 1) {
                position = 1.0f - position;
            }
        }
        if (this.isSimpleLookup) {
            return this.gradient[(int) (position * this.fastGradientArraySize)];
        }
        for (int i3 = 0; i3 < this.gradientsLength; i3++) {
            if (position < this.fractions[i3 + 1]) {
                return this.gradients[i3][(int) (((position - this.fractions[i3]) / this.normalizedIntervals[i3]) * 255.0f)];
            }
        }
        return this.gradientOverflow;
    }

    protected final int indexGradientAntiAlias(float position, float sz) {
        float p1;
        float p2;
        float frac;
        int interior;
        if (this.cycleMethod == MultipleGradientPaint.NO_CYCLE) {
            float p12 = position - (sz / 2.0f);
            float p22 = position + (sz / 2.0f);
            if (p12 >= 1.0f) {
                return this.gradientOverflow;
            }
            if (p22 <= 0.0f) {
                return this.gradientUnderflow;
            }
            float top_weight = 0.0f;
            float bottom_weight = 0.0f;
            if (p22 >= 1.0f) {
                top_weight = (p22 - 1.0f) / sz;
                if (p12 <= 0.0f) {
                    bottom_weight = (-p12) / sz;
                    frac = 1.0f;
                    interior = this.gradientAverage;
                } else {
                    frac = 1.0f - p12;
                    interior = getAntiAlias(p12, true, 1.0f, false, 1.0f - p12, 1.0f);
                }
            } else if (p12 <= 0.0f) {
                bottom_weight = (-p12) / sz;
                frac = p22;
                interior = getAntiAlias(0.0f, true, p22, false, p22, 1.0f);
            } else {
                return getAntiAlias(p12, true, p22, false, sz, 1.0f);
            }
            int norm = (int) ((65536.0f * frac) / sz);
            int pA = (((interior >>> 20) & 4080) * norm) >> 16;
            int pR = (((interior >> 12) & 4080) * norm) >> 16;
            int pG = (((interior >> 4) & 4080) * norm) >> 16;
            int pB = (((interior << 4) & 4080) * norm) >> 16;
            if (bottom_weight != 0.0f) {
                int bPix = this.gradientUnderflow;
                int norm2 = (int) (65536.0f * bottom_weight);
                pA += (((bPix >>> 20) & 4080) * norm2) >> 16;
                pR += (((bPix >> 12) & 4080) * norm2) >> 16;
                pG += (((bPix >> 4) & 4080) * norm2) >> 16;
                pB += (((bPix << 4) & 4080) * norm2) >> 16;
            }
            if (top_weight != 0.0f) {
                int tPix = this.gradientOverflow;
                int norm3 = (int) (65536.0f * top_weight);
                pA += (((tPix >>> 20) & 4080) * norm3) >> 16;
                pR += (((tPix >> 12) & 4080) * norm3) >> 16;
                pG += (((tPix >> 4) & 4080) * norm3) >> 16;
                pB += (((tPix << 4) & 4080) * norm3) >> 16;
            }
            return ((pA & 4080) << 20) | ((pR & 4080) << 12) | ((pG & 4080) << 4) | ((pB & 4080) >> 4);
        }
        int intSz = (int) sz;
        float weight = 1.0f;
        if (intSz != 0) {
            sz -= intSz;
            weight = sz / (intSz + sz);
            if (weight < 0.1d) {
                return this.gradientAverage;
            }
        }
        if (sz > 0.99d) {
            return this.gradientAverage;
        }
        float p13 = position - (sz / 2.0f);
        float p23 = position + (sz / 2.0f);
        boolean p1_up = true;
        boolean p2_up = false;
        if (this.cycleMethod == MultipleGradientPaint.REPEAT) {
            p1 = p13 - ((int) p13);
            p2 = p23 - ((int) p23);
            if (p1 < 0.0f) {
                p1 += 1.0f;
            }
            if (p2 < 0.0f) {
                p2 += 1.0f;
            }
        } else {
            if (p23 < 0.0f) {
                p13 = -p13;
                p1_up = 1 == 0;
                p23 = -p23;
                p2_up = 0 == 0;
            } else if (p13 < 0.0f) {
                p13 = -p13;
                p1_up = 1 == 0;
            }
            int part1 = (int) p13;
            p1 = p13 - part1;
            int part2 = (int) p23;
            p2 = p23 - part2;
            if ((part1 & 1) == 1) {
                p1 = 1.0f - p1;
                p1_up = !p1_up;
            }
            if ((part2 & 1) == 1) {
                p2 = 1.0f - p2;
                p2_up = !p2_up;
            }
            if (p1 > p2 && !p1_up && p2_up) {
                float t2 = p1;
                p1 = p2;
                p2 = t2;
                p1_up = true;
                p2_up = false;
            }
        }
        return getAntiAlias(p1, p1_up, p2, p2_up, sz, weight);
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x031d A[PHI: r16 r18 r20
  0x031d: PHI (r16v5 'idx1' int) = (r16v1 'idx1' int), (r16v1 'idx1' int), (r16v6 'idx1' int) binds: [B:47:0x02e8, B:49:0x02ee, B:51:0x0317] A[DONT_GENERATE, DONT_INLINE]
  0x031d: PHI (r18v5 'i1' int) = (r18v1 'i1' int), (r18v1 'i1' int), (r18v6 'i1' int) binds: [B:47:0x02e8, B:49:0x02ee, B:51:0x0317] A[DONT_GENERATE, DONT_INLINE]
  0x031d: PHI (r20v5 'f1' float) = (r20v1 'f1' float), (r20v1 'f1' float), (r20v7 'f1' float) binds: [B:47:0x02e8, B:49:0x02ee, B:51:0x0317] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int getAntiAlias(float r6, boolean r7, float r8, boolean r9, float r10, float r11) {
        /*
            Method dump skipped, instructions count: 2157
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.pobjects.graphics.batik.ext.awt.MultipleGradientPaintContext.getAntiAlias(float, boolean, float, boolean, float, float):int");
    }

    private static int convertSRGBtoLinearRGB(int color) {
        float output;
        float input = color / 255.0f;
        if (input <= 0.04045f) {
            output = input / 12.92f;
        } else {
            output = (float) Math.pow((input + 0.055d) / 1.055d, 2.4d);
        }
        return Math.round(output * 255.0f);
    }

    private static int convertLinearRGBtoSRGB(int color) {
        float output;
        float input = color / 255.0f;
        if (input <= 0.0031308f) {
            output = input * 12.92f;
        } else {
            output = (1.055f * ((float) Math.pow(input, 0.4166666666666667d))) - 0.055f;
        }
        return Math.round(output * 255.0f);
    }

    @Override // java.awt.PaintContext
    public final Raster getRaster(int x2, int y2, int w2, int h2) {
        if (w2 == 0 || h2 == 0) {
            return null;
        }
        WritableRaster raster = this.saved;
        if (raster == null || raster.getWidth() < w2 || raster.getHeight() < h2) {
            WritableRaster raster2 = getCachedRaster(this.dataModel, w2, h2);
            this.saved = raster2;
            raster = raster2.createWritableChild(raster2.getMinX(), raster2.getMinY(), w2, h2, 0, 0, null);
        }
        DataBufferInt rasterDB = (DataBufferInt) raster.getDataBuffer();
        int[] pixels = rasterDB.getBankData()[0];
        int off = rasterDB.getOffset();
        int scanlineStride = ((SinglePixelPackedSampleModel) raster.getSampleModel()).getScanlineStride();
        int adjust = scanlineStride - w2;
        fillRaster(pixels, off, adjust, x2, y2, w2, h2);
        GraphicsUtil.coerceData(raster, this.dataModel, this.model.isAlphaPremultiplied());
        return raster;
    }

    protected static synchronized WritableRaster getCachedRaster(ColorModel cm, int w2, int h2) {
        WritableRaster ras;
        if (cm == cachedModel && cached != null && (ras = cached.get()) != null && ras.getWidth() >= w2 && ras.getHeight() >= h2) {
            cached = null;
            return ras;
        }
        if (w2 < 32) {
            w2 = 32;
        }
        if (h2 < 32) {
            h2 = 32;
        }
        return cm.createCompatibleWritableRaster(w2, h2);
    }

    protected static synchronized void putCachedRaster(ColorModel cm, WritableRaster ras) {
        WritableRaster cras;
        if (cached != null && (cras = cached.get()) != null) {
            int cw = cras.getWidth();
            int ch = cras.getHeight();
            int iw = ras.getWidth();
            int ih = ras.getHeight();
            if ((cw >= iw && ch >= ih) || cw * ch >= iw * ih) {
                return;
            }
        }
        cachedModel = cm;
        cached = new WeakReference<>(ras);
    }

    @Override // java.awt.PaintContext
    public final void dispose() {
        if (this.saved != null) {
            putCachedRaster(this.model, this.saved);
            this.saved = null;
        }
    }

    @Override // java.awt.PaintContext
    public final ColorModel getColorModel() {
        return this.model;
    }
}
