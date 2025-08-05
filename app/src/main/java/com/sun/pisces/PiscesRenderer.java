package com.sun.pisces;

/* loaded from: jfxrt.jar:com/sun/pisces/PiscesRenderer.class */
public final class PiscesRenderer {
    public static final int ARC_OPEN = 0;
    public static final int ARC_CHORD = 1;
    public static final int ARC_PIE = 2;
    private long nativePtr = 0;
    private AbstractSurface surface;

    private native void initialize();

    private native void setColorImpl(int i2, int i3, int i4, int i5);

    private native void setCompositeRuleImpl(int i2);

    private native void setLinearGradientImpl(int i2, int i3, int i4, int i5, int[] iArr, int i6, Transform6 transform6);

    private native void setRadialGradientImpl(int i2, int i3, int i4, int i5, int i6, int[] iArr, int i7, Transform6 transform6);

    private native void setTextureImpl(int i2, int[] iArr, int i3, int i4, int i5, Transform6 transform6, boolean z2, boolean z3, boolean z4);

    private native void setClipImpl(int i2, int i3, int i4, int i5);

    private native void clearRectImpl(int i2, int i3, int i4, int i5);

    private native void fillRectImpl(int i2, int i3, int i4, int i5);

    private native void emitAndClearAlphaRowImpl(byte[] bArr, int[] iArr, int i2, int i3, int i4, int i5);

    private native void fillAlphaMaskImpl(byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7);

    private native void setLCDGammaCorrectionImpl(float f2);

    private native void fillLCDAlphaMaskImpl(byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7);

    private native void drawImageImpl(int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7, Transform6 transform6, boolean z2, boolean z3, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, boolean z4);

    private native void nativeFinalize();

    public PiscesRenderer(AbstractSurface surface) {
        this.surface = surface;
        initialize();
    }

    public void setColor(int red, int green, int blue, int alpha) {
        checkColorRange(red, "RED");
        checkColorRange(green, "GREEN");
        checkColorRange(blue, "BLUE");
        checkColorRange(alpha, "ALPHA");
        setColorImpl(red, green, blue, alpha);
    }

    private void checkColorRange(int v2, String componentName) {
        if (v2 < 0 || v2 > 255) {
            throw new IllegalArgumentException(componentName + " color component is out of range");
        }
    }

    public void setColor(int red, int green, int blue) {
        setColor(red, green, blue, 255);
    }

    public void setCompositeRule(int compositeRule) {
        if (compositeRule != 0 && compositeRule != 1 && compositeRule != 2) {
            throw new IllegalArgumentException("Invalid value for Composite-Rule");
        }
        setCompositeRuleImpl(compositeRule);
    }

    public void setLinearGradient(int x0, int y0, int x1, int y1, int[] fractions, int[] rgba, int cycleMethod, Transform6 gradientTransform) {
        GradientColorMap gradientColorMap = new GradientColorMap(fractions, rgba, cycleMethod);
        setLinearGradientImpl(x0, y0, x1, y1, gradientColorMap.colors, cycleMethod, gradientTransform == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : gradientTransform);
    }

    public void setLinearGradient(int x0, int y0, int x1, int y1, GradientColorMap gradientColorMap, Transform6 gradientTransform) {
        setLinearGradientImpl(x0, y0, x1, y1, gradientColorMap.colors, gradientColorMap.cycleMethod, gradientTransform == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : gradientTransform);
    }

    public void setLinearGradient(int x0, int y0, int color0, int x1, int y1, int color1, int cycleMethod) {
        int[] fractions = {0, 65536};
        int[] rgba = {color0, color1};
        Transform6 ident = new Transform6(65536, 0, 0, 65536, 0, 0);
        setLinearGradient(x0, y0, x1, y1, fractions, rgba, cycleMethod, ident);
    }

    public void setRadialGradient(int cx, int cy, int fx, int fy, int radius, int[] fractions, int[] rgba, int cycleMethod, Transform6 gradientTransform) {
        GradientColorMap gradientColorMap = new GradientColorMap(fractions, rgba, cycleMethod);
        setRadialGradientImpl(cx, cy, fx, fy, radius, gradientColorMap.colors, cycleMethod, gradientTransform == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : gradientTransform);
    }

    public void setRadialGradient(int cx, int cy, int fx, int fy, int radius, GradientColorMap gradientColorMap, Transform6 gradientTransform) {
        setRadialGradientImpl(cx, cy, fx, fy, radius, gradientColorMap.colors, gradientColorMap.cycleMethod, gradientTransform == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : gradientTransform);
    }

    public void setTexture(int imageType, int[] data, int width, int height, int stride, Transform6 textureTransform, boolean repeat, boolean linearFiltering, boolean hasAlpha) {
        inputImageCheck(width, height, 0, stride, data.length);
        setTextureImpl(imageType, data, width, height, stride, textureTransform, repeat, linearFiltering, hasAlpha);
    }

    public void setClip(int minX, int minY, int width, int height) {
        int x1 = Math.max(minX, 0);
        int y1 = Math.max(minY, 0);
        int x2 = Math.min(minX + width, this.surface.getWidth());
        int y2 = Math.min(minY + height, this.surface.getHeight());
        setClipImpl(x1, y1, x2 - x1, y2 - y1);
    }

    public void resetClip() {
        setClipImpl(0, 0, this.surface.getWidth(), this.surface.getHeight());
    }

    public void clearRect(int x2, int y2, int w2, int h2) {
        int x1 = Math.max(x2, 0);
        int y1 = Math.max(y2, 0);
        int x22 = Math.min(x2 + w2, this.surface.getWidth());
        int y22 = Math.min(y2 + h2, this.surface.getHeight());
        clearRectImpl(x1, y1, x22 - x1, y22 - y1);
    }

    public void fillRect(int x2, int y2, int w2, int h2) {
        int x1 = Math.max(x2, 0);
        int y1 = Math.max(y2, 0);
        int x22 = Math.min(x2 + w2, this.surface.getWidth() << 16);
        int y22 = Math.min(y2 + h2, this.surface.getHeight() << 16);
        int w22 = x22 - x1;
        int h22 = y22 - y1;
        if (w22 > 0 && h22 > 0) {
            fillRectImpl(x1, y1, w22, h22);
        }
    }

    public void emitAndClearAlphaRow(byte[] alphaMap, int[] alphaDeltas, int pix_y, int pix_x_from, int pix_x_to, int rowNum) {
        if (pix_x_to - pix_x_from > alphaDeltas.length) {
            throw new IllegalArgumentException("rendering range exceeds length of data");
        }
        emitAndClearAlphaRowImpl(alphaMap, alphaDeltas, pix_y, pix_x_from, pix_x_to, rowNum);
    }

    public void fillAlphaMask(byte[] mask, int x2, int y2, int width, int height, int offset, int stride) {
        if (mask == null) {
            throw new NullPointerException("Mask is NULL");
        }
        inputImageCheck(width, height, offset, stride, mask.length);
        fillAlphaMaskImpl(mask, x2, y2, width, height, offset, stride);
    }

    public void setLCDGammaCorrection(float gamma) {
        if (gamma <= 0.0f) {
            throw new IllegalArgumentException("Gamma must be greater than zero");
        }
        setLCDGammaCorrectionImpl(gamma);
    }

    public void fillLCDAlphaMask(byte[] mask, int x2, int y2, int width, int height, int offset, int stride) {
        if (mask == null) {
            throw new NullPointerException("Mask is NULL");
        }
        inputImageCheck(width, height, offset, stride, mask.length);
        fillLCDAlphaMaskImpl(mask, x2, y2, width, height, offset, stride);
    }

    public void drawImage(int imageType, int imageMode, int[] data, int width, int height, int offset, int stride, Transform6 textureTransform, boolean repeat, boolean linearFiltering, int bboxX, int bboxY, int bboxW, int bboxH, int lEdge, int rEdge, int tEdge, int bEdge, int txMin, int tyMin, int txMax, int tyMax, boolean hasAlpha) {
        inputImageCheck(width, height, offset, stride, data.length);
        drawImageImpl(imageType, imageMode, data, width, height, offset, stride, textureTransform, repeat, linearFiltering, bboxX, bboxY, bboxW, bboxH, lEdge, rEdge, tEdge, bEdge, txMin, tyMin, txMax, tyMax, hasAlpha);
    }

    private void inputImageCheck(int width, int height, int offset, int stride, int data_length) {
        if (width < 0) {
            throw new IllegalArgumentException("WIDTH must be positive");
        }
        if (height < 0) {
            throw new IllegalArgumentException("HEIGHT must be positive");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("OFFSET must be positive");
        }
        if (stride < 0) {
            throw new IllegalArgumentException("STRIDE must be positive");
        }
        if (stride < width) {
            throw new IllegalArgumentException("STRIDE must be >= WIDTH");
        }
        int nbits = ((32 - Integer.numberOfLeadingZeros(stride)) + 32) - Integer.numberOfLeadingZeros(height);
        if (nbits > 31) {
            throw new IllegalArgumentException("STRIDE * HEIGHT is too large");
        }
        if (offset + (stride * (height - 1)) + width > data_length) {
            throw new IllegalArgumentException("STRIDE * HEIGHT exceeds length of data");
        }
    }

    protected void finalize() {
        nativeFinalize();
    }
}
