package com.sun.javafx.iio.png;

import com.sun.javafx.iio.common.ImageTools;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/png/PNGIDATChunkInputStream.class */
public class PNGIDATChunkInputStream extends InputStream {
    static final int IDAT_TYPE = 1229209940;
    private DataInputStream source;
    private int numBytesAvailable;
    private boolean foundAllIDATChunks = false;
    private int nextChunkLength = 0;
    private int nextChunkType = 0;

    PNGIDATChunkInputStream(DataInputStream input, int firstIDATChunkLength) throws IOException {
        this.numBytesAvailable = 0;
        if (firstIDATChunkLength < 0) {
            throw new IOException("Invalid chunk length");
        }
        this.source = input;
        this.numBytesAvailable = firstIDATChunkLength;
    }

    private void nextChunk() throws IOException {
        if (!this.foundAllIDATChunks) {
            ImageTools.skipFully(this.source, 4L);
            int chunkLength = this.source.readInt();
            if (chunkLength < 0) {
                throw new IOException("Invalid chunk length");
            }
            int chunkType = this.source.readInt();
            if (chunkType == IDAT_TYPE) {
                this.numBytesAvailable += chunkLength;
                return;
            }
            this.foundAllIDATChunks = true;
            this.nextChunkLength = chunkLength;
            this.nextChunkType = chunkType;
        }
    }

    boolean isFoundAllIDATChunks() {
        return this.foundAllIDATChunks;
    }

    int getNextChunkLength() {
        return this.nextChunkLength;
    }

    int getNextChunkType() {
        return this.nextChunkType;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.numBytesAvailable == 0) {
            nextChunk();
        }
        if (this.numBytesAvailable == 0) {
            return -1;
        }
        this.numBytesAvailable--;
        return this.source.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int len) throws IOException {
        if (this.numBytesAvailable == 0) {
            nextChunk();
            if (this.numBytesAvailable == 0) {
                return -1;
            }
        }
        int totalRead = 0;
        while (this.numBytesAvailable > 0 && len > 0) {
            int numToRead = len < this.numBytesAvailable ? len : this.numBytesAvailable;
            int numRead = this.source.read(b2, off, numToRead);
            if (numRead == -1) {
                throw new EOFException();
            }
            this.numBytesAvailable -= numRead;
            off += numRead;
            len -= numRead;
            totalRead += numRead;
            if (this.numBytesAvailable == 0 && len > 0) {
                nextChunk();
            }
        }
        return totalRead;
    }
}
