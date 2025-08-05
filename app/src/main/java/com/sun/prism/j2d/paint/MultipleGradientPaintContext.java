package com.sun.prism.j2d.paint;

import com.sun.prism.j2d.paint.MultipleGradientPaint;
import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/paint/MultipleGradientPaintContext.class */
abstract class MultipleGradientPaintContext implements PaintContext {
    protected ColorModel model;
    protected static ColorModel cachedModel;
    protected static WeakReference<Raster> cached;
    protected Raster saved;
    protected MultipleGradientPaint.CycleMethod cycleMethod;
    protected MultipleGradientPaint.ColorSpaceType colorSpace;
    protected float a00;
    protected float a01;
    protected float a10;
    protected float a11;
    protected float a02;
    protected float a12;
    protected boolean isSimpleLookup;
    protected int fastGradientArraySize;
    protected int[] gradient;
    private int[][] gradients;
    private float[] normalizedIntervals;
    private float[] fractions;
    private int transparencyTest;
    protected static final int GRADIENT_SIZE = 256;
    protected static final int GRADIENT_SIZE_INDEX = 255;
    private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;
    private static ColorModel xrgbmodel = new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
    private static final int[] SRGBtoLinearRGB = new int[256];
    private static final int[] LinearRGBtoSRGB = new int[256];

    protected abstract void fillRaster(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7);

    static {
        for (int k2 = 0; k2 < 256; k2++) {
            SRGBtoLinearRGB[k2] = convertSRGBtoLinearRGB(k2);
            LinearRGBtoSRGB[k2] = convertLinearRGBtoSRGB(k2);
        }
    }

    protected MultipleGradientPaintContext(MultipleGradientPaint mgp, ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform t2, RenderingHints hints, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpace) {
        AffineTransform tInv;
        if (deviceBounds == null) {
            throw new NullPointerException("Device bounds cannot be null");
        }
        if (userBounds == null) {
            throw new NullPointerException("User bounds cannot be null");
        }
        if (t2 == null) {
            throw new NullPointerException("Transform cannot be null");
        }
        try {
            tInv = t2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            tInv = new AffineTransform();
        }
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
        this.fractions = fractions;
        this.gradient = mgp.gradient != null ? mgp.gradient.get() : null;
        this.gradients = mgp.gradients != null ? mgp.gradients.get() : (int[][]) null;
        if (this.gradient == null && this.gradients == null) {
            calculateLookupData(colors);
            mgp.model = this.model;
            mgp.normalizedIntervals = this.normalizedIntervals;
            mgp.isSimpleLookup = this.isSimpleLookup;
            if (this.isSimpleLookup) {
                mgp.fastGradientArraySize = this.fastGradientArraySize;
                mgp.gradient = new SoftReference<>(this.gradient);
                return;
            } else {
                mgp.gradients = new SoftReference<>(this.gradients);
                return;
            }
        }
        this.model = mgp.model;
        this.normalizedIntervals = mgp.normalizedIntervals;
        this.isSimpleLookup = mgp.isSimpleLookup;
        this.fastGradientArraySize = mgp.fastGradientArraySize;
    }

    /* JADX WARN: Type inference failed for: r1v13, types: [int[], int[][]] */
    private void calculateLookupData(Color[] colors) {
        Color[] normalizedColors;
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            normalizedColors = new Color[colors.length];
            for (int i2 = 0; i2 < colors.length; i2++) {
                int argb = colors[i2].getRGB();
                int a2 = argb >>> 24;
                int r2 = SRGBtoLinearRGB[(argb >> 16) & 255];
                int g2 = SRGBtoLinearRGB[(argb >> 8) & 255];
                int b2 = SRGBtoLinearRGB[argb & 255];
                normalizedColors[i2] = new Color(r2, g2, b2, a2);
            }
        } else {
            normalizedColors = colors;
        }
        this.normalizedIntervals = new float[this.fractions.length - 1];
        for (int i3 = 0; i3 < this.normalizedIntervals.length; i3++) {
            this.normalizedIntervals[i3] = this.fractions[i3 + 1] - this.fractions[i3];
        }
        this.transparencyTest = -16777216;
        this.gradients = new int[this.normalizedIntervals.length];
        float Imin = 1.0f;
        for (int i4 = 0; i4 < this.normalizedIntervals.length; i4++) {
            Imin = Imin > this.normalizedIntervals[i4] ? this.normalizedIntervals[i4] : Imin;
        }
        int estimatedSize = 0;
        for (int i5 = 0; i5 < this.normalizedIntervals.length; i5++) {
            estimatedSize = (int) (estimatedSize + ((this.normalizedIntervals[i5] / Imin) * 256.0f));
        }
        if (estimatedSize > 5000) {
            calculateMultipleArrayGradient(normalizedColors);
        } else {
            calculateSingleArrayGradient(normalizedColors, Imin);
        }
        if ((this.transparencyTest >>> 24) == 255) {
            this.model = xrgbmodel;
        } else {
            this.model = ColorModel.getRGBdefault();
        }
    }

    private void calculateSingleArrayGradient(Color[] colors, float Imin) {
        this.isSimpleLookup = true;
        int gradientsTot = 1;
        for (int i2 = 0; i2 < this.gradients.length; i2++) {
            int nGradients = (int) ((this.normalizedIntervals[i2] / Imin) * 255.0f);
            gradientsTot += nGradients;
            this.gradients[i2] = new int[nGradients];
            int rgb1 = colors[i2].getRGB();
            int rgb2 = colors[i2 + 1].getRGB();
            interpolate(rgb1, rgb2, this.gradients[i2]);
            this.transparencyTest &= rgb1;
            this.transparencyTest &= rgb2;
        }
        this.gradient = new int[gradientsTot];
        int curOffset = 0;
        for (int i3 = 0; i3 < this.gradients.length; i3++) {
            System.arraycopy(this.gradients[i3], 0, this.gradient, curOffset, this.gradients[i3].length);
            curOffset += this.gradients[i3].length;
        }
        this.gradient[this.gradient.length - 1] = colors[colors.length - 1].getRGB();
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            for (int i4 = 0; i4 < this.gradient.length; i4++) {
                this.gradient[i4] = convertEntireColorLinearRGBtoSRGB(this.gradient[i4]);
            }
        }
        this.fastGradientArraySize = this.gradient.length - 1;
    }

    private void calculateMultipleArrayGradient(Color[] colors) {
        this.isSimpleLookup = false;
        for (int i2 = 0; i2 < this.gradients.length; i2++) {
            this.gradients[i2] = new int[256];
            int rgb1 = colors[i2].getRGB();
            int rgb2 = colors[i2 + 1].getRGB();
            interpolate(rgb1, rgb2, this.gradients[i2]);
            this.transparencyTest &= rgb1;
            this.transparencyTest &= rgb2;
        }
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            for (int j2 = 0; j2 < this.gradients.length; j2++) {
                for (int i3 = 0; i3 < this.gradients[j2].length; i3++) {
                    this.gradients[j2][i3] = convertEntireColorLinearRGBtoSRGB(this.gradients[j2][i3]);
                }
            }
        }
    }

    private void interpolate(int rgb1, int rgb2, int[] output) {
        float stepSize = 1.0f / output.length;
        int a1 = (rgb1 >> 24) & 255;
        int r1 = (rgb1 >> 16) & 255;
        int g1 = (rgb1 >> 8) & 255;
        int b1 = rgb1 & 255;
        int da = ((rgb2 >> 24) & 255) - a1;
        int dr = ((rgb2 >> 16) & 255) - r1;
        int dg = ((rgb2 >> 8) & 255) - g1;
        int db = (rgb2 & 255) - b1;
        for (int i2 = 0; i2 < output.length; i2++) {
            output[i2] = (((int) ((a1 + ((i2 * da) * stepSize)) + 0.5d)) << 24) | (((int) ((r1 + ((i2 * dr) * stepSize)) + 0.5d)) << 16) | (((int) ((g1 + ((i2 * dg) * stepSize)) + 0.5d)) << 8) | ((int) (b1 + (i2 * db * stepSize) + 0.5d));
        }
    }

    private int convertEntireColorLinearRGBtoSRGB(int rgb) {
        int a1 = (rgb >> 24) & 255;
        int r1 = (rgb >> 16) & 255;
        int g1 = (rgb >> 8) & 255;
        int b1 = rgb & 255;
        int r12 = LinearRGBtoSRGB[r1];
        int g12 = LinearRGBtoSRGB[g1];
        return (a1 << 24) | (r12 << 16) | (g12 << 8) | LinearRGBtoSRGB[b1];
    }

    protected final int indexIntoGradientsArrays(float position) {
        if (this.cycleMethod == MultipleGradientPaint.CycleMethod.NO_CYCLE) {
            if (position > 1.0f) {
                position = 1.0f;
            } else if (position < 0.0f) {
                position = 0.0f;
            }
        } else if (this.cycleMethod == MultipleGradientPaint.CycleMethod.REPEAT) {
            position -= (int) position;
            if (position < 0.0f) {
                position += 1.0f;
            }
        } else {
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
        for (int i2 = 0; i2 < this.gradients.length; i2++) {
            if (position < this.fractions[i2 + 1]) {
                float delta = position - this.fractions[i2];
                int index = (int) ((delta / this.normalizedIntervals[i2]) * 255.0f);
                return this.gradients[i2][index];
            }
        }
        return this.gradients[this.gradients.length - 1][255];
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
        if (input <= 0.0031308d) {
            output = input * 12.92f;
        } else {
            output = (1.055f * ((float) Math.pow(input, 0.4166666666666667d))) - 0.055f;
        }
        return Math.round(output * 255.0f);
    }

    @Override // java.awt.PaintContext
    public final Raster getRaster(int x2, int y2, int w2, int h2) {
        Raster raster = this.saved;
        if (raster == null || raster.getWidth() < w2 || raster.getHeight() < h2) {
            raster = getCachedRaster(this.model, w2, h2);
            this.saved = raster;
        }
        DataBufferInt rasterDB = (DataBufferInt) raster.getDataBuffer();
        int[] pixels = rasterDB.getData(0);
        int off = rasterDB.getOffset();
        int scanlineStride = ((SinglePixelPackedSampleModel) raster.getSampleModel()).getScanlineStride();
        int adjust = scanlineStride - w2;
        fillRaster(pixels, off, adjust, x2, y2, w2, h2);
        return raster;
    }

    private static synchronized Raster getCachedRaster(ColorModel cm, int w2, int h2) {
        Raster ras;
        if (cm == cachedModel && cached != null && (ras = cached.get()) != null && ras.getWidth() >= w2 && ras.getHeight() >= h2) {
            cached = null;
            return ras;
        }
        return cm.createCompatibleWritableRaster(w2, h2);
    }

    private static synchronized void putCachedRaster(ColorModel cm, Raster ras) {
        Raster cras;
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
