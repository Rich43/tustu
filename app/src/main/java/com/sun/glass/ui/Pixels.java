package com.sun.glass.ui;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Pixels.class */
public abstract class Pixels {
    protected final int width;
    protected final int height;
    protected final int bytesPerComponent = 1;
    protected final ByteBuffer bytes;
    protected final IntBuffer ints;
    private final float scale;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Pixels$Format.class */
    public static class Format {
        public static final int BYTE_BGRA_PRE = 1;
        public static final int BYTE_ARGB = 2;
    }

    protected abstract void _fillDirectByteBuffer(ByteBuffer byteBuffer);

    protected abstract void _attachInt(long j2, int i2, int i3, IntBuffer intBuffer, int[] iArr, int i4);

    protected abstract void _attachByte(long j2, int i2, int i3, ByteBuffer byteBuffer, byte[] bArr, int i4);

    public static int getNativeFormat() {
        Application.checkEventThread();
        return Application.GetApplication().staticPixels_getNativeFormat();
    }

    protected Pixels(int width, int height, ByteBuffer pixels) {
        this.width = width;
        this.height = height;
        this.bytes = pixels.slice();
        if (this.width <= 0 || this.height <= 0 || this.width * this.height * 4 > this.bytes.capacity()) {
            throw new IllegalArgumentException("Too small byte buffer size " + this.width + LanguageTag.PRIVATEUSE + this.height + " [" + (this.width * this.height * 4) + "] > " + this.bytes.capacity());
        }
        this.ints = null;
        this.scale = 1.0f;
    }

    protected Pixels(int width, int height, IntBuffer pixels) {
        this.width = width;
        this.height = height;
        this.ints = pixels.slice();
        if (this.width <= 0 || this.height <= 0 || this.width * this.height > this.ints.capacity()) {
            throw new IllegalArgumentException("Too small int buffer size " + this.width + LanguageTag.PRIVATEUSE + this.height + " [" + (this.width * this.height) + "] > " + this.ints.capacity());
        }
        this.bytes = null;
        this.scale = 1.0f;
    }

    protected Pixels(int width, int height, IntBuffer pixels, float scale) {
        this.width = width;
        this.height = height;
        this.ints = pixels.slice();
        if (this.width <= 0 || this.height <= 0 || this.width * this.height > this.ints.capacity()) {
            throw new IllegalArgumentException("Too small int buffer size " + this.width + LanguageTag.PRIVATEUSE + this.height + " [" + (this.width * this.height) + "] > " + this.ints.capacity());
        }
        this.bytes = null;
        this.scale = scale;
    }

    public final float getScale() {
        Application.checkEventThread();
        return this.scale;
    }

    public final float getScaleUnsafe() {
        return this.scale;
    }

    public final int getWidth() {
        Application.checkEventThread();
        return this.width;
    }

    public final int getWidthUnsafe() {
        return this.width;
    }

    public final int getHeight() {
        Application.checkEventThread();
        return this.height;
    }

    public final int getHeightUnsafe() {
        return this.height;
    }

    public final int getBytesPerComponent() {
        Application.checkEventThread();
        return this.bytesPerComponent;
    }

    public final Buffer getPixels() {
        if (this.bytes != null) {
            this.bytes.rewind();
            return this.bytes;
        }
        if (this.ints != null) {
            this.ints.rewind();
            return this.ints;
        }
        throw new RuntimeException("Unexpected Pixels state.");
    }

    public final ByteBuffer asByteBuffer() {
        Application.checkEventThread();
        ByteBuffer bb2 = ByteBuffer.allocateDirect(getWidth() * getHeight() * 4);
        bb2.order(ByteOrder.nativeOrder());
        bb2.rewind();
        asByteBuffer(bb2);
        return bb2;
    }

    public final void asByteBuffer(ByteBuffer bb2) {
        Application.checkEventThread();
        if (!bb2.isDirect()) {
            throw new RuntimeException("Expected direct buffer.");
        }
        if (bb2.remaining() < getWidth() * getHeight() * 4) {
            throw new RuntimeException("Too small buffer.");
        }
        _fillDirectByteBuffer(bb2);
    }

    private void attachData(long ptr) {
        if (this.ints != null) {
            int[] array = !this.ints.isDirect() ? this.ints.array() : null;
            _attachInt(ptr, this.width, this.height, this.ints, array, array != null ? this.ints.arrayOffset() : 0);
        }
        if (this.bytes != null) {
            byte[] array2 = !this.bytes.isDirect() ? this.bytes.array() : null;
            _attachByte(ptr, this.width, this.height, this.bytes, array2, array2 != null ? this.bytes.arrayOffset() : 0);
        }
    }

    public final boolean equals(Object object) {
        Application.checkEventThread();
        boolean equals = object != null && getClass().equals(object.getClass());
        if (equals) {
            Pixels pixels = (Pixels) object;
            equals = getWidth() == pixels.getWidth() && getHeight() == pixels.getHeight();
            if (equals) {
                ByteBuffer b1 = asByteBuffer();
                ByteBuffer b2 = pixels.asByteBuffer();
                equals = b1.compareTo(b2) == 0;
            }
        }
        return equals;
    }

    public final int hashCode() {
        Application.checkEventThread();
        int val = getWidth();
        return (17 * ((31 * val) + getHeight())) + asByteBuffer().hashCode();
    }
}
