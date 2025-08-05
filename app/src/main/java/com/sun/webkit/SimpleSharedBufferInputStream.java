package com.sun.webkit;

import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/webkit/SimpleSharedBufferInputStream.class */
public final class SimpleSharedBufferInputStream extends InputStream {
    private final SharedBuffer sharedBuffer;
    private long position;

    public SimpleSharedBufferInputStream(SharedBuffer sharedBuffer) {
        if (sharedBuffer == null) {
            throw new NullPointerException("sharedBuffer is null");
        }
        this.sharedBuffer = sharedBuffer;
    }

    @Override // java.io.InputStream
    public int read() {
        byte[] buffer = new byte[1];
        int length = this.sharedBuffer.getSomeData(this.position, buffer, 0, 1);
        if (length != 0) {
            this.position++;
            return buffer[0] & 255;
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int len) {
        if (b2 == null) {
            throw new NullPointerException("b is null");
        }
        if (off < 0) {
            throw new IndexOutOfBoundsException("off is negative");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        if (len > b2.length - off) {
            throw new IndexOutOfBoundsException("len is greater than b.length - off");
        }
        if (len == 0) {
            return 0;
        }
        int length = this.sharedBuffer.getSomeData(this.position, b2, off, len);
        if (length != 0) {
            this.position += length;
            return length;
        }
        return -1;
    }

    @Override // java.io.InputStream
    public long skip(long n2) {
        long k2 = this.sharedBuffer.size() - this.position;
        if (n2 < k2) {
            k2 = n2 < 0 ? 0L : n2;
        }
        this.position += k2;
        return k2;
    }

    @Override // java.io.InputStream
    public int available() {
        return (int) Math.min(this.sharedBuffer.size() - this.position, 2147483647L);
    }
}
