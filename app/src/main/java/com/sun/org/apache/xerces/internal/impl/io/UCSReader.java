package com.sun.org.apache.xerces.internal.impl.io;

import com.sun.xml.internal.stream.util.BufferAllocator;
import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/io/UCSReader.class */
public class UCSReader extends Reader {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final short UCS2LE = 1;
    public static final short UCS2BE = 2;
    public static final short UCS4LE = 4;
    public static final short UCS4BE = 8;
    protected InputStream fInputStream;
    protected byte[] fBuffer;
    protected short fEncoding;

    public UCSReader(InputStream inputStream, short encoding) {
        this(inputStream, 8192, encoding);
    }

    public UCSReader(InputStream inputStream, int size, short encoding) {
        this.fInputStream = inputStream;
        BufferAllocator ba2 = ThreadLocalBufferAllocator.getBufferAllocator();
        this.fBuffer = ba2.getByteBuffer(size);
        if (this.fBuffer == null) {
            this.fBuffer = new byte[size];
        }
        this.fEncoding = encoding;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        int b1;
        int b3;
        int b0 = this.fInputStream.read() & 255;
        if (b0 == 255 || (b1 = this.fInputStream.read() & 255) == 255) {
            return -1;
        }
        if (this.fEncoding >= 4) {
            int b2 = this.fInputStream.read() & 255;
            if (b2 == 255 || (b3 = this.fInputStream.read() & 255) == 255) {
                return -1;
            }
            System.err.println("b0 is " + (b0 & 255) + " b1 " + (b1 & 255) + " b2 " + (b2 & 255) + " b3 " + (b3 & 255));
            if (this.fEncoding == 8) {
                return (b0 << 24) + (b1 << 16) + (b2 << 8) + b3;
            }
            return (b3 << 24) + (b2 << 16) + (b1 << 8) + b0;
        }
        if (this.fEncoding == 2) {
            return (b0 << 8) + b1;
        }
        return (b1 << 8) + b0;
    }

    @Override // java.io.Reader
    public int read(char[] ch, int offset, int length) throws IOException {
        int byteLength = length << (this.fEncoding >= 4 ? 2 : 1);
        if (byteLength > this.fBuffer.length) {
            byteLength = this.fBuffer.length;
        }
        int count = this.fInputStream.read(this.fBuffer, 0, byteLength);
        if (count == -1) {
            return -1;
        }
        if (this.fEncoding >= 4) {
            int numToRead = (4 - (count & 3)) & 3;
            int i2 = 0;
            while (true) {
                if (i2 >= numToRead) {
                    break;
                }
                int charRead = this.fInputStream.read();
                if (charRead == -1) {
                    for (int j2 = i2; j2 < numToRead; j2++) {
                        this.fBuffer[count + j2] = 0;
                    }
                } else {
                    this.fBuffer[count + i2] = (byte) charRead;
                    i2++;
                }
            }
            count += numToRead;
        } else if ((count & 1) != 0) {
            count++;
            int charRead2 = this.fInputStream.read();
            if (charRead2 == -1) {
                this.fBuffer[count] = 0;
            } else {
                this.fBuffer[count] = (byte) charRead2;
            }
        }
        int numChars = count >> (this.fEncoding >= 4 ? 2 : 1);
        int curPos = 0;
        for (int i3 = 0; i3 < numChars; i3++) {
            int i4 = curPos;
            int curPos2 = curPos + 1;
            int b0 = this.fBuffer[i4] & 255;
            curPos = curPos2 + 1;
            int b1 = this.fBuffer[curPos2] & 255;
            if (this.fEncoding >= 4) {
                int curPos3 = curPos + 1;
                int b2 = this.fBuffer[curPos] & 255;
                curPos = curPos3 + 1;
                int b3 = this.fBuffer[curPos3] & 255;
                if (this.fEncoding == 8) {
                    ch[offset + i3] = (char) ((b0 << 24) + (b1 << 16) + (b2 << 8) + b3);
                } else {
                    ch[offset + i3] = (char) ((b3 << 24) + (b2 << 16) + (b1 << 8) + b0);
                }
            } else if (this.fEncoding == 2) {
                ch[offset + i3] = (char) ((b0 << 8) + b1);
            } else {
                ch[offset + i3] = (char) ((b1 << 8) + b0);
            }
        }
        return numChars;
    }

    @Override // java.io.Reader
    public long skip(long n2) throws IOException {
        int charWidth = this.fEncoding >= 4 ? 2 : 1;
        long bytesSkipped = this.fInputStream.skip(n2 << charWidth);
        return (bytesSkipped & ((long) (charWidth | 1))) == 0 ? bytesSkipped >> charWidth : (bytesSkipped >> charWidth) + 1;
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        return false;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return this.fInputStream.markSupported();
    }

    @Override // java.io.Reader
    public void mark(int readAheadLimit) throws IOException {
        this.fInputStream.mark(readAheadLimit);
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        this.fInputStream.reset();
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        BufferAllocator ba2 = ThreadLocalBufferAllocator.getBufferAllocator();
        ba2.returnByteBuffer(this.fBuffer);
        this.fBuffer = null;
        this.fInputStream.close();
    }
}
