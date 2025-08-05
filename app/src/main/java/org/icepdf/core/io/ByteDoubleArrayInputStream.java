package org.icepdf.core.io;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/ByteDoubleArrayInputStream.class */
public class ByteDoubleArrayInputStream extends InputStream {
    protected byte[][] buf;
    protected int[] bufOffset;
    protected int count;
    protected int mark = 0;
    protected int markIndex = 0;
    protected int pos = 0;
    protected int posIndex = 0;

    public ByteDoubleArrayInputStream(byte[][] buf) {
        this.buf = buf;
        this.bufOffset = new int[buf.length];
        for (int i2 = 0; i2 < buf.length; i2++) {
            this.bufOffset[i2] = this.count;
            this.count += buf[i2].length;
        }
    }

    @Override // java.io.InputStream
    public synchronized int read() {
        float posOffset = this.bufOffset[this.posIndex] + this.pos;
        if (posOffset >= this.count) {
            return -1;
        }
        if (posOffset < this.bufOffset[this.posIndex] + this.buf[this.posIndex].length) {
            byte[] bArr = this.buf[this.posIndex];
            int i2 = this.pos;
            this.pos = i2 + 1;
            return bArr[i2] & 255;
        }
        this.posIndex++;
        this.pos = 0;
        byte[] bArr2 = this.buf[this.posIndex];
        int i3 = this.pos;
        this.pos = i3 + 1;
        return bArr2[i3] & 255;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] b2, int off, int len) {
        int posOffset;
        if (b2 == null) {
            throw new NullPointerException();
        }
        if (off < 0 || off > b2.length || len < 0 || off + len > b2.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (this.posIndex >= this.buf.length || (posOffset = this.bufOffset[this.posIndex] + this.pos) >= this.count) {
            return -1;
        }
        if (posOffset + len > this.count) {
            len = this.count - posOffset;
        }
        if (len <= 0) {
            return 0;
        }
        if (this.pos + len < this.buf[this.posIndex].length) {
            System.arraycopy(this.buf[this.posIndex], this.pos, b2, off, len);
            this.pos += len;
        } else {
            int newLength = len;
            int length = this.buf[this.posIndex].length - this.pos;
            while (true) {
                int partialOffset = length;
                if (newLength <= 0) {
                    break;
                }
                System.arraycopy(this.buf[this.posIndex], this.pos, b2, off, partialOffset);
                off += partialOffset;
                newLength -= partialOffset;
                this.pos += partialOffset;
                if (newLength == 0) {
                    break;
                }
                this.posIndex++;
                this.pos = 0;
                if (this.pos + newLength < this.buf[this.posIndex].length) {
                    length = newLength;
                } else {
                    length = this.buf[this.posIndex].length - this.pos;
                }
            }
        }
        return len;
    }

    @Override // java.io.InputStream
    public synchronized long skip(long n2) {
        if (this.pos + n2 > this.count) {
            n2 = this.count - this.pos;
        }
        if (n2 < 0) {
            return 0L;
        }
        if (this.pos + n2 < this.bufOffset[this.posIndex]) {
            this.pos = (int) (this.pos + n2);
        } else {
            long j2 = this.bufOffset[this.posIndex] - this.pos;
            while (true) {
                long partialOffset = j2;
                if (n2 <= 0) {
                    break;
                }
                n2 -= partialOffset;
                this.posIndex++;
                if (this.pos + n2 < this.bufOffset[this.posIndex]) {
                    j2 = n2;
                } else {
                    j2 = this.bufOffset[this.posIndex] - this.pos;
                }
            }
        }
        return n2;
    }

    @Override // java.io.InputStream
    public synchronized int available() {
        return this.count - (this.bufOffset[this.posIndex] + this.pos);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.InputStream
    public void mark(int readAheadLimit) {
        this.mark = this.pos;
        this.markIndex = this.posIndex;
    }

    @Override // java.io.InputStream
    public synchronized void reset() {
        this.pos = this.mark;
        this.posIndex = this.markIndex;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }
}
