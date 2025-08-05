package com.sun.org.apache.xml.internal.security.utils;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/UnsyncBufferedOutputStream.class */
public class UnsyncBufferedOutputStream extends FilterOutputStream {
    protected byte[] buffer;
    protected int count;

    public UnsyncBufferedOutputStream(OutputStream outputStream) {
        super(outputStream);
        this.buffer = new byte[8192];
    }

    public UnsyncBufferedOutputStream(OutputStream outputStream, int i2) {
        super(outputStream);
        if (i2 <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
        this.buffer = new byte[i2];
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        flushInternal();
        this.out.flush();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (i3 >= this.buffer.length) {
            flushInternal();
            this.out.write(bArr, i2, i3);
        } else {
            if (i3 >= this.buffer.length - this.count) {
                flushInternal();
            }
            System.arraycopy(bArr, i2, this.buffer, this.count, i3);
            this.count += i3;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        if (this.count == this.buffer.length) {
            this.out.write(this.buffer, 0, this.count);
            this.count = 0;
        }
        byte[] bArr = this.buffer;
        int i3 = this.count;
        this.count = i3 + 1;
        bArr[i3] = (byte) i2;
    }

    private void flushInternal() throws IOException {
        if (this.count > 0) {
            this.out.write(this.buffer, 0, this.count);
            this.count = 0;
        }
    }
}
