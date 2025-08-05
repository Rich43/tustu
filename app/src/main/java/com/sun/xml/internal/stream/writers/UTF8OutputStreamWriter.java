package com.sun.xml.internal.stream.writers;

import com.sun.org.apache.xerces.internal.util.XMLChar;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/stream/writers/UTF8OutputStreamWriter.class */
public final class UTF8OutputStreamWriter extends Writer {
    OutputStream out;
    int lastUTF16CodePoint = 0;

    public UTF8OutputStreamWriter(OutputStream out) {
        this.out = out;
    }

    public String getEncoding() {
        return "UTF-8";
    }

    @Override // java.io.Writer
    public void write(int c2) throws IOException {
        if (this.lastUTF16CodePoint != 0) {
            int uc = (((this.lastUTF16CodePoint & 1023) << 10) | (c2 & 1023)) + 65536;
            if (uc < 0 || uc >= 2097152) {
                throw new IOException("Atttempting to write invalid Unicode code point '" + uc + PdfOps.SINGLE_QUOTE_TOKEN);
            }
            this.out.write(240 | (uc >> 18));
            this.out.write(128 | ((uc >> 12) & 63));
            this.out.write(128 | ((uc >> 6) & 63));
            this.out.write(128 | (uc & 63));
            this.lastUTF16CodePoint = 0;
            return;
        }
        if (c2 < 128) {
            this.out.write(c2);
            return;
        }
        if (c2 < 2048) {
            this.out.write(192 | (c2 >> 6));
            this.out.write(128 | (c2 & 63));
        } else if (c2 <= 65535) {
            if (!XMLChar.isHighSurrogate(c2) && !XMLChar.isLowSurrogate(c2)) {
                this.out.write(224 | (c2 >> 12));
                this.out.write(128 | ((c2 >> 6) & 63));
                this.out.write(128 | (c2 & 63));
                return;
            }
            this.lastUTF16CodePoint = c2;
        }
    }

    @Override // java.io.Writer
    public void write(char[] cbuf) throws IOException {
        for (char c2 : cbuf) {
            write(c2);
        }
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i2 = 0; i2 < len; i2++) {
            write(cbuf[off + i2]);
        }
    }

    @Override // java.io.Writer, com.sun.org.apache.xml.internal.serializer.WriterChain
    public void write(String str) throws IOException {
        int len = str.length();
        for (int i2 = 0; i2 < len; i2++) {
            write(str.charAt(i2));
        }
    }

    @Override // java.io.Writer
    public void write(String str, int off, int len) throws IOException {
        for (int i2 = 0; i2 < len; i2++) {
            write(str.charAt(off + i2));
        }
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.lastUTF16CodePoint != 0) {
            throw new IllegalStateException("Attempting to close a UTF8OutputStreamWriter while awaiting for a UTF-16 code unit");
        }
        this.out.close();
    }
}
