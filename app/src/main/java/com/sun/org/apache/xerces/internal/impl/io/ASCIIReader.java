package com.sun.org.apache.xerces.internal.impl.io;

import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import com.sun.xml.internal.stream.util.BufferAllocator;
import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/io/ASCIIReader.class */
public class ASCIIReader extends Reader {
    public static final int DEFAULT_BUFFER_SIZE = 2048;
    protected InputStream fInputStream;
    protected byte[] fBuffer;
    private MessageFormatter fFormatter;
    private Locale fLocale;

    public ASCIIReader(InputStream inputStream, MessageFormatter messageFormatter, Locale locale) {
        this(inputStream, 2048, messageFormatter, locale);
    }

    public ASCIIReader(InputStream inputStream, int size, MessageFormatter messageFormatter, Locale locale) {
        this.fFormatter = null;
        this.fLocale = null;
        this.fInputStream = inputStream;
        BufferAllocator ba2 = ThreadLocalBufferAllocator.getBufferAllocator();
        this.fBuffer = ba2.getByteBuffer(size);
        if (this.fBuffer == null) {
            this.fBuffer = new byte[size];
        }
        this.fFormatter = messageFormatter;
        this.fLocale = locale;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        int b0 = this.fInputStream.read();
        if (b0 >= 128) {
            throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[]{Integer.toString(b0)});
        }
        return b0;
    }

    @Override // java.io.Reader
    public int read(char[] ch, int offset, int length) throws IOException {
        if (length > this.fBuffer.length) {
            length = this.fBuffer.length;
        }
        int count = this.fInputStream.read(this.fBuffer, 0, length);
        for (int i2 = 0; i2 < count; i2++) {
            byte b2 = this.fBuffer[i2];
            if (b2 < 0) {
                throw new MalformedByteSequenceException(this.fFormatter, this.fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[]{Integer.toString(b2 & 255)});
            }
            ch[offset + i2] = (char) b2;
        }
        return count;
    }

    @Override // java.io.Reader
    public long skip(long n2) throws IOException {
        return this.fInputStream.skip(n2);
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
