package org.icepdf.core.io;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/ZeroPaddedInputStream.class */
public class ZeroPaddedInputStream extends InputStream {
    private InputStream in;

    public ZeroPaddedInputStream(InputStream in) {
        this.in = in;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int r2 = this.in.read();
        if (r2 < 0) {
            return 0;
        }
        return r2;
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int readIn = this.in.read(buffer, offset, length);
        if (readIn < length) {
            if (readIn > 0) {
                while (true) {
                    if ((buffer[(offset + readIn) - 1] != 10 && buffer[(offset + readIn) - 1] != 13 && buffer[(offset + readIn) - 1] != 32) || readIn <= 0) {
                        break;
                    }
                    readIn--;
                }
                return readIn;
            }
            int end = offset + length;
            for (int current = offset + Math.max(0, readIn); current < end; current++) {
                buffer[current] = 0;
            }
        }
        return length;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        int a2 = this.in.available();
        if (a2 <= 0) {
            a2 = 1;
        }
        return a2;
    }

    @Override // java.io.InputStream
    public void mark(int readLimit) {
        this.in.mark(readLimit);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.in.markSupported();
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
        this.in.reset();
    }

    @Override // java.io.InputStream
    public long skip(long n2) throws IOException {
        long s2 = this.in.skip(n2);
        return s2;
    }
}
