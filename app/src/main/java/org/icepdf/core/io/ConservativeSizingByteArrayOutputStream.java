package org.icepdf.core.io;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/ConservativeSizingByteArrayOutputStream.class */
public class ConservativeSizingByteArrayOutputStream extends OutputStream {
    protected byte[] buf;
    protected int count;

    public ConservativeSizingByteArrayOutputStream(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Negative initial capacity: " + capacity);
        }
        this.buf = allocateByteArray(capacity);
        this.count = 0;
    }

    public ConservativeSizingByteArrayOutputStream(byte[] buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("Initial buffer is null");
        }
        if (buffer.length == 0) {
            throw new IllegalArgumentException("Initial buffer has zero length");
        }
        this.buf = buffer;
        this.count = 0;
    }

    @Override // java.io.OutputStream
    public synchronized void write(int b2) throws IOException {
        int newCount = this.count + 1;
        if (newCount > this.buf.length) {
            resizeArrayToFit(newCount);
        }
        this.buf[this.count] = (byte) b2;
        this.count = newCount;
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] b2, int off, int len) throws IOException {
        if (off < 0 || off >= b2.length || len < 0 || off + len > b2.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return;
        }
        int newCount = this.count + len;
        if (newCount > this.buf.length) {
            resizeArrayToFit(newCount);
        }
        System.arraycopy(b2, off, this.buf, this.count, len);
        this.count = newCount;
    }

    public synchronized void reset() {
        this.count = 0;
    }

    public synchronized byte[] toByteArray() {
        byte[] newBuf = allocateByteArray(this.count);
        System.arraycopy(this.buf, 0, newBuf, 0, this.count);
        return newBuf;
    }

    public int size() {
        return this.count;
    }

    public synchronized byte[] relinquishByteArray() {
        byte[] returnBuf = this.buf;
        this.buf = new byte[64];
        this.count = 0;
        return returnBuf;
    }

    public boolean trim() {
        if ((this.count == 0 && (this.buf == null || this.buf.length == 0)) || this.count == this.buf.length) {
            return true;
        }
        byte[] newBuf = allocateByteArray(this.count);
        if (newBuf == null) {
            return false;
        }
        System.arraycopy(this.buf, 0, newBuf, 0, this.count);
        this.buf = null;
        this.buf = newBuf;
        return true;
    }

    protected void resizeArrayToFit(int newCount) {
        int steppedSize;
        int steppedSize2 = this.buf.length;
        if (steppedSize2 == 0) {
            steppedSize = 64;
        } else if (steppedSize2 <= 1024) {
            steppedSize = steppedSize2 * 4;
        } else if (steppedSize2 <= 4024) {
            steppedSize = steppedSize2 * 2;
        } else if (steppedSize2 <= 2097152) {
            steppedSize = (steppedSize2 * 2) & (-4096);
        } else if (steppedSize2 <= 4194304) {
            steppedSize = ((steppedSize2 * 3) / 2) & (-4096);
        } else if (steppedSize2 <= 15728640) {
            steppedSize = ((steppedSize2 * 5) / 4) & (-4096);
        } else {
            steppedSize = (steppedSize2 + 3145728) & (-4096);
        }
        int newBufSize = Math.max(steppedSize, newCount);
        byte[] newBuf = allocateByteArray(newBufSize);
        System.arraycopy(this.buf, 0, newBuf, 0, this.count);
        this.buf = newBuf;
    }

    protected byte[] allocateByteArray(int size) {
        return new byte[size];
    }
}
