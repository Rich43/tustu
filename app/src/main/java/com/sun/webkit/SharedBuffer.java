package com.sun.webkit;

/* loaded from: jfxrt.jar:com/sun/webkit/SharedBuffer.class */
public final class SharedBuffer {
    private long nativePointer;

    private static native long twkCreate();

    private static native long twkSize(long j2);

    private static native int twkGetSomeData(long j2, long j3, byte[] bArr, int i2, int i3);

    private static native void twkAppend(long j2, byte[] bArr, int i2, int i3);

    private static native void twkDispose(long j2);

    SharedBuffer() {
        this.nativePointer = twkCreate();
    }

    private SharedBuffer(long nativePointer) {
        if (nativePointer == 0) {
            throw new IllegalArgumentException("nativePointer is 0");
        }
        this.nativePointer = nativePointer;
    }

    private static SharedBuffer fwkCreate(long nativePointer) {
        return new SharedBuffer(nativePointer);
    }

    long size() {
        if (this.nativePointer == 0) {
            throw new IllegalStateException("nativePointer is 0");
        }
        return twkSize(this.nativePointer);
    }

    int getSomeData(long position, byte[] buffer, int offset, int length) {
        if (this.nativePointer == 0) {
            throw new IllegalStateException("nativePointer is 0");
        }
        if (position < 0) {
            throw new IndexOutOfBoundsException("position is negative");
        }
        if (position > size()) {
            throw new IndexOutOfBoundsException("position is greater than size");
        }
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        if (offset < 0) {
            throw new IndexOutOfBoundsException("offset is negative");
        }
        if (length < 0) {
            throw new IndexOutOfBoundsException("length is negative");
        }
        if (length > buffer.length - offset) {
            throw new IndexOutOfBoundsException("length is greater than buffer.length - offset");
        }
        return twkGetSomeData(this.nativePointer, position, buffer, offset, length);
    }

    void append(byte[] buffer, int offset, int length) {
        if (this.nativePointer == 0) {
            throw new IllegalStateException("nativePointer is 0");
        }
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        if (offset < 0) {
            throw new IndexOutOfBoundsException("offset is negative");
        }
        if (length < 0) {
            throw new IndexOutOfBoundsException("length is negative");
        }
        if (length > buffer.length - offset) {
            throw new IndexOutOfBoundsException("length is greater than buffer.length - offset");
        }
        twkAppend(this.nativePointer, buffer, offset, length);
    }

    void dispose() {
        if (this.nativePointer == 0) {
            throw new IllegalStateException("nativePointer is 0");
        }
        twkDispose(this.nativePointer);
        this.nativePointer = 0L;
    }
}
