package com.sun.xml.internal.ws.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/ByteArrayBuffer.class */
public class ByteArrayBuffer extends OutputStream {
    protected byte[] buf;
    private int count;
    private static final int CHUNK_SIZE = 4096;

    public ByteArrayBuffer() {
        this(32);
    }

    public ByteArrayBuffer(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        this.buf = new byte[size];
    }

    public ByteArrayBuffer(byte[] data) {
        this(data, data.length);
    }

    public ByteArrayBuffer(byte[] data, int length) {
        this.buf = data;
        this.count = length;
    }

    public final void write(InputStream in) throws IOException {
        while (true) {
            int cap = this.buf.length - this.count;
            int sz = in.read(this.buf, this.count, cap);
            if (sz < 0) {
                return;
            }
            this.count += sz;
            if (cap == sz) {
                ensureCapacity(this.buf.length * 2);
            }
        }
    }

    @Override // java.io.OutputStream
    public final void write(int b2) {
        int newcount = this.count + 1;
        ensureCapacity(newcount);
        this.buf[this.count] = (byte) b2;
        this.count = newcount;
    }

    @Override // java.io.OutputStream
    public final void write(byte[] b2, int off, int len) {
        int newcount = this.count + len;
        ensureCapacity(newcount);
        System.arraycopy(b2, off, this.buf, this.count, len);
        this.count = newcount;
    }

    private void ensureCapacity(int newcount) {
        if (newcount > this.buf.length) {
            byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
            System.arraycopy(this.buf, 0, newbuf, 0, this.count);
            this.buf = newbuf;
        }
    }

    public final void writeTo(OutputStream out) throws IOException {
        int remaining = this.count;
        int i2 = 0;
        while (true) {
            int off = i2;
            if (remaining > 0) {
                int chunk = remaining > 4096 ? 4096 : remaining;
                out.write(this.buf, off, chunk);
                remaining -= chunk;
                i2 = off + chunk;
            } else {
                return;
            }
        }
    }

    public final void reset() {
        this.count = 0;
    }

    public final byte[] toByteArray() {
        byte[] newbuf = new byte[this.count];
        System.arraycopy(this.buf, 0, newbuf, 0, this.count);
        return newbuf;
    }

    public final int size() {
        return this.count;
    }

    public final byte[] getRawData() {
        return this.buf;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    public final InputStream newInputStream() {
        return new ByteArrayInputStream(this.buf, 0, this.count);
    }

    public final InputStream newInputStream(int start, int length) {
        return new ByteArrayInputStream(this.buf, start, length);
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }
}
