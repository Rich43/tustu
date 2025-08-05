package org.icepdf.core.io;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/SizeInputStream.class */
public class SizeInputStream extends InputStream {
    private InputStream in;
    private int size;
    private int bytesRead = 0;

    public SizeInputStream(InputStream in, int size) {
        this.in = null;
        this.size = 0;
        this.in = in;
        this.size = size;
    }

    @Override // java.io.InputStream
    public int available() {
        return this.size - this.bytesRead;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int b2 = this.in.read();
        if (b2 != -1) {
            this.bytesRead++;
        }
        return b2;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2) throws IOException {
        int read = this.in.read(b2);
        this.bytesRead += read;
        return read;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int len) throws IOException {
        int read = this.in.read(b2, off, len);
        this.bytesRead += read;
        return read;
    }
}
