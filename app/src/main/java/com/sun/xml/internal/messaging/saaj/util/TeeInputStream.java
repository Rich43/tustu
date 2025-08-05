package com.sun.xml.internal.messaging.saaj.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/TeeInputStream.class */
public class TeeInputStream extends InputStream {
    protected InputStream source;
    protected OutputStream copySink;

    public TeeInputStream(InputStream source, OutputStream sink) {
        this.copySink = sink;
        this.source = source;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int result = this.source.read();
        this.copySink.write(result);
        return result;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.source.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.source.close();
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readlimit) {
        this.source.mark(readlimit);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.source.markSupported();
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int len) throws IOException {
        int result = this.source.read(b2, off, len);
        this.copySink.write(b2, off, len);
        return result;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2) throws IOException {
        int result = this.source.read(b2);
        this.copySink.write(b2);
        return result;
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        this.source.reset();
    }

    @Override // java.io.InputStream
    public long skip(long n2) throws IOException {
        return this.source.skip(n2);
    }
}
