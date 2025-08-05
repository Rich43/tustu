package com.sun.xml.internal.messaging.saaj.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/ByteOutputStream.class */
public final class ByteOutputStream extends OutputStream {
    protected byte[] buf;
    protected int count;

    public ByteOutputStream() {
        this(1024);
    }

    public ByteOutputStream(int size) {
        this.count = 0;
        this.buf = new byte[size];
    }

    public void write(InputStream in) throws IOException {
        if (in instanceof ByteArrayInputStream) {
            int size = in.available();
            ensureCapacity(size);
            this.count += in.read(this.buf, this.count, size);
        } else {
            while (true) {
                int cap = this.buf.length - this.count;
                int sz = in.read(this.buf, this.count, cap);
                if (sz < 0) {
                    return;
                }
                this.count += sz;
                if (cap == sz) {
                    ensureCapacity(this.count);
                }
            }
        }
    }

    @Override // java.io.OutputStream
    public void write(int b2) {
        ensureCapacity(1);
        this.buf[this.count] = (byte) b2;
        this.count++;
    }

    private void ensureCapacity(int space) {
        int newcount = space + this.count;
        if (newcount > this.buf.length) {
            byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
            System.arraycopy(this.buf, 0, newbuf, 0, this.count);
            this.buf = newbuf;
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2, int off, int len) {
        ensureCapacity(len);
        System.arraycopy(b2, off, this.buf, this.count, len);
        this.count += len;
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2) {
        write(b2, 0, b2.length);
    }

    public void writeAsAscii(String s2) {
        int len = s2.length();
        ensureCapacity(len);
        int ptr = this.count;
        for (int i2 = 0; i2 < len; i2++) {
            int i3 = ptr;
            ptr++;
            this.buf[i3] = (byte) s2.charAt(i2);
        }
        this.count = ptr;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.buf, 0, this.count);
    }

    public void reset() {
        this.count = 0;
    }

    public byte[] toByteArray() {
        byte[] newbuf = new byte[this.count];
        System.arraycopy(this.buf, 0, newbuf, 0, this.count);
        return newbuf;
    }

    public int size() {
        return this.count;
    }

    public ByteInputStream newInputStream() {
        return new ByteInputStream(this.buf, this.count);
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    public byte[] getBytes() {
        return this.buf;
    }

    public int getCount() {
        return this.count;
    }
}
