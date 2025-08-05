package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;

/* loaded from: jfxrt.jar:com/sun/prism/impl/paint/MultipleGradientContext.class */
abstract class MultipleGradientContext {
    protected int cycleMethod;
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

    protected abstract void fillRaster(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7);

    protected MultipleGradientContext(Gradient mgp, BaseTransform t2, float[] fractions, Color[] colors, int cycleMethod) {
        BaseTransform tInv;
        if (t2 == null) {
            throw new NullPointerException("Transform cannot be null");
        }
        try {
            tInv = t2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            tInv = BaseTransform.IDENTITY_TRANSFORM;
        }
        this.a00 = (float) tInv.getMxx();
        this.a10 = (float) tInv.getMyx();
        this.a01 = (float) tInv.getMxy();
        this.a11 = (float) tInv.getMyy();
        this.a02 = (float) tInv.getMxt();
        this.a12 = (float) tInv.getMyt();
        this.cycleMethod = cycleMethod;
        this.fractions = fractions;
        calculateLookupData(colors);
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [int[], int[][]] */
    private void calculateLookupData(Color[] colors) {
        this.normalizedIntervals = new float[this.fractions.length - 1];
        for (int i2 = 0; i2 < this.normalizedIntervals.length; i2++) {
            this.normalizedIntervals[i2] = this.fractions[i2 + 1] - this.fractions[i2];
        }
        this.transparencyTest = -16777216;
        this.gradients = new int[this.normalizedIntervals.length];
        float Imin = 1.0f;
        for (int i3 = 0; i3 < this.normalizedIntervals.length; i3++) {
            Imin = Imin > this.normalizedIntervals[i3] ? this.normalizedIntervals[i3] : Imin;
        }
        float estimatedSize = 0.0f;
        for (int i4 = 0; i4 < this.normalizedIntervals.length && Float.isFinite(estimatedSize); i4++) {
            estimatedSize += (this.normalizedIntervals[i4] / Imin) * 256.0f;
        }
        if (estimatedSize <= 5000.0f) {
            calculateSingleArrayGradient(colors, Imin);
        } else {
            calculateMultipleArrayGradient(colors);
        }
    }

    private void calculateSingleArrayGradient(Color[] colors, float Imin) {
        this.isSimpleLookup = true;
        int gradientsTot = 1;
        for (int i2 = 0; i2 < this.gradients.length; i2++) {
            int nGradients = (int) ((this.normalizedIntervals[i2] / Imin) * 255.0f);
            gradientsTot += nGradients;
            this.gradients[i2] = new int[nGradients];
            int rgb1 = colors[i2].getIntArgbPre();
            int rgb2 = colors[i2 + 1].getIntArgbPre();
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
        this.gradient[this.gradient.length - 1] = colors[colors.length - 1].getIntArgbPre();
        this.fastGradientArraySize = this.gradient.length - 1;
    }

    private void calculateMultipleArrayGradient(Color[] colors) {
        this.isSimpleLookup = false;
        for (int i2 = 0; i2 < this.gradients.length; i2++) {
            this.gradients[i2] = new int[256];
            int rgb1 = colors[i2].getIntArgbPre();
            int rgb2 = colors[i2 + 1].getIntArgbPre();
            interpolate(rgb1, rgb2, this.gradients[i2]);
            this.transparencyTest &= rgb1;
            this.transparencyTest &= rgb2;
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

    protected final int indexIntoGradientsArrays(float position) {
        if (this.cycleMethod == 0) {
            if (position > 1.0f) {
                position = 1.0f;
            } else if (position < 0.0f) {
                position = 0.0f;
            }
        } else if (this.cycleMethod == 2) {
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
        if (position < this.fractions[0]) {
            return this.gradients[0][0];
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
}
