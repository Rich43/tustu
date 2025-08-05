package org.icepdf.core.io;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/CountingOutputStream.class */
public class CountingOutputStream extends OutputStream {
    private OutputStream wrapped;
    private long count = 0;

    public CountingOutputStream(OutputStream wrap) {
        this.wrapped = wrap;
    }

    public long getCount() {
        return this.count;
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        this.wrapped.write(i2);
        this.count++;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bytes) throws IOException {
        this.wrapped.write(bytes);
        this.count += bytes.length;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bytes, int offset, int len) throws IOException {
        this.wrapped.write(bytes, offset, len);
        int num = Math.min(len, bytes.length - offset);
        this.count += num;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.wrapped.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.wrapped.close();
    }
}
