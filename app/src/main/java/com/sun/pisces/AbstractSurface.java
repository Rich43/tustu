package com.sun.pisces;

/* loaded from: jfxrt.jar:com/sun/pisces/AbstractSurface.class */
public abstract class AbstractSurface implements Surface {
    private long nativePtr = 0;
    private int width;
    private int height;

    private native void getRGBImpl(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7);

    private native void setRGBImpl(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7);

    private native void nativeFinalize();

    AbstractSurface(int width, int height) {
        if (width < 0) {
            throw new IllegalArgumentException("WIDTH must be positive");
        }
        if (height < 0) {
            throw new IllegalArgumentException("HEIGHT must be positive");
        }
        int nbits = ((32 - Integer.numberOfLeadingZeros(width)) + 32) - Integer.numberOfLeadingZeros(height);
        if (nbits > 31) {
            throw new IllegalArgumentException("WIDTH * HEIGHT is too large");
        }
        this.width = width;
        this.height = height;
    }

    @Override // com.sun.pisces.Surface
    public final void getRGB(int[] argb, int offset, int scanLength, int x2, int y2, int width, int height) {
        rgbCheck(argb.length, offset, scanLength, x2, y2, width, height);
        getRGBImpl(argb, offset, scanLength, x2, y2, width, height);
    }

    @Override // com.sun.pisces.Surface
    public final void setRGB(int[] argb, int offset, int scanLength, int x2, int y2, int width, int height) {
        rgbCheck(argb.length, offset, scanLength, x2, y2, width, height);
        setRGBImpl(argb, offset, scanLength, x2, y2, width, height);
    }

    private void rgbCheck(int arr_length, int offset, int scanLength, int x2, int y2, int width, int height) {
        if (x2 < 0 || x2 >= this.width) {
            throw new IllegalArgumentException("X is out of surface");
        }
        if (y2 < 0 || y2 >= this.height) {
            throw new IllegalArgumentException("Y is out of surface");
        }
        if (width < 0) {
            throw new IllegalArgumentException("WIDTH must be positive");
        }
        if (height < 0) {
            throw new IllegalArgumentException("HEIGHT must be positive");
        }
        if (x2 + width > this.width) {
            throw new IllegalArgumentException("X+WIDTH is out of surface");
        }
        if (y2 + height > this.height) {
            throw new IllegalArgumentException("Y+HEIGHT is out of surface");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("OFFSET must be positive");
        }
        if (scanLength < 0) {
            throw new IllegalArgumentException("SCAN-LENGTH must be positive");
        }
        if (scanLength < width) {
            throw new IllegalArgumentException("SCAN-LENGTH must be >= WIDTH");
        }
        int nbits = ((32 - Integer.numberOfLeadingZeros(scanLength)) + 32) - Integer.numberOfLeadingZeros(height);
        if (nbits > 31) {
            throw new IllegalArgumentException("SCAN-LENGTH * HEIGHT is too large");
        }
        if (offset + (scanLength * (height - 1)) + width > arr_length) {
            throw new IllegalArgumentException("STRIDE * HEIGHT exceeds length of data");
        }
    }

    protected void finalize() {
        nativeFinalize();
    }

    @Override // com.sun.pisces.Surface
    public final int getWidth() {
        return this.width;
    }

    @Override // com.sun.pisces.Surface
    public final int getHeight() {
        return this.height;
    }
}
