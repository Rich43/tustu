package java.awt;

import java.awt.MultipleGradientPaint;
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

/* loaded from: rt.jar:java/awt/MultipleGradientPaintContext.class */
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
        for (int i2 = 0; i2 < 256; i2++) {
            SRGBtoLinearRGB[i2] = convertSRGBtoLinearRGB(i2);
            LinearRGBtoSRGB[i2] = convertLinearRGBtoSRGB(i2);
        }
    }

    protected MultipleGradientPaintContext(MultipleGradientPaint multipleGradientPaint, ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints, float[] fArr, Color[] colorArr, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpaceType) {
        AffineTransform affineTransform2;
        if (rectangle == null) {
            throw new NullPointerException("Device bounds cannot be null");
        }
        if (rectangle2D == null) {
            throw new NullPointerException("User bounds cannot be null");
        }
        if (affineTransform == null) {
            throw new NullPointerException("Transform cannot be null");
        }
        if (renderingHints == null) {
            throw new NullPointerException("RenderingHints cannot be null");
        }
        try {
            affineTransform.invert();
            affineTransform2 = affineTransform;
        } catch (NoninvertibleTransformException e2) {
            affineTransform2 = new AffineTransform();
        }
        double[] dArr = new double[6];
        affineTransform2.getMatrix(dArr);
        this.a00 = (float) dArr[0];
        this.a10 = (float) dArr[1];
        this.a01 = (float) dArr[2];
        this.a11 = (float) dArr[3];
        this.a02 = (float) dArr[4];
        this.a12 = (float) dArr[5];
        this.cycleMethod = cycleMethod;
        this.colorSpace = colorSpaceType;
        this.fractions = fArr;
        int[] iArr = multipleGradientPaint.gradient != null ? multipleGradientPaint.gradient.get() : null;
        int[][] iArr2 = multipleGradientPaint.gradients != null ? multipleGradientPaint.gradients.get() : (int[][]) null;
        if (iArr == null && iArr2 == null) {
            calculateLookupData(colorArr);
            multipleGradientPaint.model = this.model;
            multipleGradientPaint.normalizedIntervals = this.normalizedIntervals;
            multipleGradientPaint.isSimpleLookup = this.isSimpleLookup;
            if (this.isSimpleLookup) {
                multipleGradientPaint.fastGradientArraySize = this.fastGradientArraySize;
                multipleGradientPaint.gradient = new SoftReference<>(this.gradient);
                return;
            } else {
                multipleGradientPaint.gradients = new SoftReference<>(this.gradients);
                return;
            }
        }
        this.model = multipleGradientPaint.model;
        this.normalizedIntervals = multipleGradientPaint.normalizedIntervals;
        this.isSimpleLookup = multipleGradientPaint.isSimpleLookup;
        this.gradient = iArr;
        this.fastGradientArraySize = multipleGradientPaint.fastGradientArraySize;
        this.gradients = iArr2;
    }

    /* JADX WARN: Type inference failed for: r1v13, types: [int[], int[][]] */
    private void calculateLookupData(Color[] colorArr) {
        Color[] colorArr2;
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            colorArr2 = new Color[colorArr.length];
            for (int i2 = 0; i2 < colorArr.length; i2++) {
                int rgb = colorArr[i2].getRGB();
                colorArr2[i2] = new Color(SRGBtoLinearRGB[(rgb >> 16) & 255], SRGBtoLinearRGB[(rgb >> 8) & 255], SRGBtoLinearRGB[rgb & 255], rgb >>> 24);
            }
        } else {
            colorArr2 = colorArr;
        }
        this.normalizedIntervals = new float[this.fractions.length - 1];
        for (int i3 = 0; i3 < this.normalizedIntervals.length; i3++) {
            this.normalizedIntervals[i3] = this.fractions[i3 + 1] - this.fractions[i3];
        }
        this.transparencyTest = -16777216;
        this.gradients = new int[this.normalizedIntervals.length];
        float f2 = 1.0f;
        for (int i4 = 0; i4 < this.normalizedIntervals.length; i4++) {
            f2 = f2 > this.normalizedIntervals[i4] ? this.normalizedIntervals[i4] : f2;
        }
        int i5 = 0;
        for (int i6 = 0; i6 < this.normalizedIntervals.length; i6++) {
            i5 = (int) (i5 + ((this.normalizedIntervals[i6] / f2) * 256.0f));
        }
        if (i5 > 5000) {
            calculateMultipleArrayGradient(colorArr2);
        } else {
            calculateSingleArrayGradient(colorArr2, f2);
        }
        if ((this.transparencyTest >>> 24) == 255) {
            this.model = xrgbmodel;
        } else {
            this.model = ColorModel.getRGBdefault();
        }
    }

    private void calculateSingleArrayGradient(Color[] colorArr, float f2) {
        this.isSimpleLookup = true;
        int i2 = 1;
        for (int i3 = 0; i3 < this.gradients.length; i3++) {
            int i4 = (int) ((this.normalizedIntervals[i3] / f2) * 255.0f);
            i2 += i4;
            this.gradients[i3] = new int[i4];
            int rgb = colorArr[i3].getRGB();
            int rgb2 = colorArr[i3 + 1].getRGB();
            interpolate(rgb, rgb2, this.gradients[i3]);
            this.transparencyTest &= rgb;
            this.transparencyTest &= rgb2;
        }
        this.gradient = new int[i2];
        int length = 0;
        for (int i5 = 0; i5 < this.gradients.length; i5++) {
            System.arraycopy(this.gradients[i5], 0, this.gradient, length, this.gradients[i5].length);
            length += this.gradients[i5].length;
        }
        this.gradient[this.gradient.length - 1] = colorArr[colorArr.length - 1].getRGB();
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            for (int i6 = 0; i6 < this.gradient.length; i6++) {
                this.gradient[i6] = convertEntireColorLinearRGBtoSRGB(this.gradient[i6]);
            }
        }
        this.fastGradientArraySize = this.gradient.length - 1;
    }

    private void calculateMultipleArrayGradient(Color[] colorArr) {
        this.isSimpleLookup = false;
        for (int i2 = 0; i2 < this.gradients.length; i2++) {
            this.gradients[i2] = new int[256];
            int rgb = colorArr[i2].getRGB();
            int rgb2 = colorArr[i2 + 1].getRGB();
            interpolate(rgb, rgb2, this.gradients[i2]);
            this.transparencyTest &= rgb;
            this.transparencyTest &= rgb2;
        }
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            for (int i3 = 0; i3 < this.gradients.length; i3++) {
                for (int i4 = 0; i4 < this.gradients[i3].length; i4++) {
                    this.gradients[i3][i4] = convertEntireColorLinearRGBtoSRGB(this.gradients[i3][i4]);
                }
            }
        }
    }

    private void interpolate(int i2, int i3, int[] iArr) {
        float length = 1.0f / iArr.length;
        int i4 = ((i3 >> 24) & 255) - ((i2 >> 24) & 255);
        int i5 = ((i3 >> 16) & 255) - ((i2 >> 16) & 255);
        int i6 = ((i3 >> 8) & 255) - ((i2 >> 8) & 255);
        int i7 = (i3 & 255) - (i2 & 255);
        for (int i8 = 0; i8 < iArr.length; i8++) {
            iArr[i8] = (((int) ((r0 + ((i8 * i4) * length)) + 0.5d)) << 24) | (((int) ((r0 + ((i8 * i5) * length)) + 0.5d)) << 16) | (((int) ((r0 + ((i8 * i6) * length)) + 0.5d)) << 8) | ((int) (r0 + (i8 * i7 * length) + 0.5d));
        }
    }

    private int convertEntireColorLinearRGBtoSRGB(int i2) {
        int i3 = (i2 >> 24) & 255;
        int i4 = LinearRGBtoSRGB[(i2 >> 16) & 255];
        int i5 = LinearRGBtoSRGB[(i2 >> 8) & 255];
        return (i3 << 24) | (i4 << 16) | (i5 << 8) | LinearRGBtoSRGB[i2 & 255];
    }

    protected final int indexIntoGradientsArrays(float f2) {
        if (this.cycleMethod == MultipleGradientPaint.CycleMethod.NO_CYCLE) {
            if (f2 > 1.0f) {
                f2 = 1.0f;
            } else if (f2 < 0.0f) {
                f2 = 0.0f;
            }
        } else if (this.cycleMethod == MultipleGradientPaint.CycleMethod.REPEAT) {
            f2 -= (int) f2;
            if (f2 < 0.0f) {
                f2 += 1.0f;
            }
        } else {
            if (f2 < 0.0f) {
                f2 = -f2;
            }
            int i2 = (int) f2;
            f2 -= i2;
            if ((i2 & 1) == 1) {
                f2 = 1.0f - f2;
            }
        }
        if (this.isSimpleLookup) {
            return this.gradient[(int) (f2 * this.fastGradientArraySize)];
        }
        for (int i3 = 0; i3 < this.gradients.length; i3++) {
            if (f2 < this.fractions[i3 + 1]) {
                return this.gradients[i3][(int) (((f2 - this.fractions[i3]) / this.normalizedIntervals[i3]) * 255.0f)];
            }
        }
        return this.gradients[this.gradients.length - 1][255];
    }

    private static int convertSRGBtoLinearRGB(int i2) {
        float fPow;
        float f2 = i2 / 255.0f;
        if (f2 <= 0.04045f) {
            fPow = f2 / 12.92f;
        } else {
            fPow = (float) Math.pow((f2 + 0.055d) / 1.055d, 2.4d);
        }
        return Math.round(fPow * 255.0f);
    }

    private static int convertLinearRGBtoSRGB(int i2) {
        float fPow;
        float f2 = i2 / 255.0f;
        if (f2 <= 0.0031308d) {
            fPow = f2 * 12.92f;
        } else {
            fPow = (1.055f * ((float) Math.pow(f2, 0.4166666666666667d))) - 0.055f;
        }
        return Math.round(fPow * 255.0f);
    }

    @Override // java.awt.PaintContext
    public final Raster getRaster(int i2, int i3, int i4, int i5) {
        Raster cachedRaster = this.saved;
        if (cachedRaster == null || cachedRaster.getWidth() < i4 || cachedRaster.getHeight() < i5) {
            cachedRaster = getCachedRaster(this.model, i4, i5);
            this.saved = cachedRaster;
        }
        DataBufferInt dataBufferInt = (DataBufferInt) cachedRaster.getDataBuffer();
        fillRaster(dataBufferInt.getData(0), dataBufferInt.getOffset(), ((SinglePixelPackedSampleModel) cachedRaster.getSampleModel()).getScanlineStride() - i4, i2, i3, i4, i5);
        return cachedRaster;
    }

    private static synchronized Raster getCachedRaster(ColorModel colorModel, int i2, int i3) {
        Raster raster;
        if (colorModel == cachedModel && cached != null && (raster = cached.get()) != null && raster.getWidth() >= i2 && raster.getHeight() >= i3) {
            cached = null;
            return raster;
        }
        return colorModel.createCompatibleWritableRaster(i2, i3);
    }

    private static synchronized void putCachedRaster(ColorModel colorModel, Raster raster) {
        Raster raster2;
        if (cached != null && (raster2 = cached.get()) != null) {
            int width = raster2.getWidth();
            int height = raster2.getHeight();
            int width2 = raster.getWidth();
            int height2 = raster.getHeight();
            if ((width >= width2 && height >= height2) || width * height >= width2 * height2) {
                return;
            }
        }
        cachedModel = colorModel;
        cached = new WeakReference<>(raster);
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
