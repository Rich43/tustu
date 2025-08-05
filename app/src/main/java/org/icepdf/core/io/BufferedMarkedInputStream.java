package org.icepdf.core.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/BufferedMarkedInputStream.class */
public class BufferedMarkedInputStream extends BufferedInputStream {
    private int fillCount;

    public BufferedMarkedInputStream(InputStream in) {
        super(in);
    }

    public BufferedMarkedInputStream(InputStream in, int size) {
        super(in, size);
    }

    public int getMarkedPosition() {
        return this.fillCount;
    }

    @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        this.fillCount++;
        return super.read();
    }

    @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        if (this.markpos > 0) {
            this.fillCount -= this.pos - this.markpos;
        }
        super.reset();
    }
}
