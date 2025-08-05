package org.icepdf.core.pobjects.filters;

import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/ChunkingInputStream.class */
public abstract class ChunkingInputStream extends InputStream {
    protected InputStream in = null;
    protected byte[] buffer = null;
    private int bufferPosition = 0;
    private int bufferAvailable = 0;

    protected abstract int fillInternalBuffer() throws IOException;

    protected void setInputStream(InputStream input) {
        this.in = input;
    }

    protected void setBufferSize(int size) {
        this.buffer = new byte[size];
    }

    protected int fillBufferFromInputStream() throws IOException {
        return fillBufferFromInputStream(0, this.buffer.length);
    }

    protected int fillBufferFromInputStream(int offset, int length) throws IOException {
        int read = 0;
        int mayRead = this.in.available();
        while (mayRead >= 0 && read < length) {
            try {
                int currRead = this.in.read(this.buffer, offset + read, length - read);
                if (currRead < 0 && read == 0) {
                    return currRead;
                }
                if (currRead <= 0) {
                    break;
                }
                read += currRead;
            } catch (IOException e2) {
            }
        }
        return read;
    }

    private int ensureDataAvailable() throws IOException {
        if (this.bufferAvailable > 0) {
            return this.bufferAvailable;
        }
        this.bufferPosition = 0;
        this.bufferAvailable = 0;
        int avail = fillInternalBuffer();
        if (avail > 0) {
            this.bufferAvailable = avail;
        }
        return this.bufferAvailable;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public void mark(int readlimit) {
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int avail = ensureDataAvailable();
        if (avail <= 0) {
            return -1;
        }
        byte b2 = this.buffer[this.bufferPosition];
        this.bufferPosition++;
        this.bufferAvailable--;
        return b2 & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2) throws IOException {
        return read(b2, 0, b2.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int length) throws IOException {
        int i2 = 0;
        while (true) {
            int read = i2;
            if (read < length) {
                int avail = ensureDataAvailable();
                if (avail <= 0) {
                    if (read > 0) {
                        return read;
                    }
                    return -1;
                }
                int toRead = Math.min(length - read, avail);
                int srcIdx = this.bufferPosition;
                int dstIdx = off + read;
                System.arraycopy(this.buffer, srcIdx, b2, dstIdx, toRead);
                this.bufferPosition += toRead;
                this.bufferAvailable -= toRead;
                i2 = read + toRead;
            } else {
                return read;
            }
        }
    }

    @Override // java.io.InputStream
    public long skip(long n2) throws IOException {
        long j2 = 0;
        while (true) {
            long skipped = j2;
            if (skipped < n2) {
                int avail = ensureDataAvailable();
                if (avail <= 0) {
                    if (skipped > 0) {
                        return skipped;
                    }
                    return -1L;
                }
                long toSkip = Math.min(n2 - skipped, avail);
                this.bufferPosition = (int) (this.bufferPosition + toSkip);
                this.bufferAvailable = (int) (this.bufferAvailable - toSkip);
                j2 = skipped + toSkip;
            } else {
                return skipped;
            }
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.bufferAvailable;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.in != null) {
            this.in.close();
            this.in = null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(": ");
        if (this.in == null) {
            sb.append(FXMLLoader.NULL_KEYWORD);
        } else {
            sb.append(this.in.toString());
        }
        return sb.toString();
    }
}
