package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/BASE64EncoderStream.class */
public class BASE64EncoderStream extends FilterOutputStream {
    private byte[] buffer;
    private int bufsize;
    private int count;
    private int bytesPerLine;
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    public BASE64EncoderStream(OutputStream out, int bytesPerLine) {
        super(out);
        this.bufsize = 0;
        this.count = 0;
        this.buffer = new byte[3];
        this.bytesPerLine = bytesPerLine;
    }

    public BASE64EncoderStream(OutputStream out) {
        this(out, 76);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b2, int off, int len) throws IOException {
        for (int i2 = 0; i2 < len; i2++) {
            write(b2[off + i2]);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b2) throws IOException {
        write(b2, 0, b2.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int c2) throws IOException {
        byte[] bArr = this.buffer;
        int i2 = this.bufsize;
        this.bufsize = i2 + 1;
        bArr[i2] = (byte) c2;
        if (this.bufsize == 3) {
            encode();
            this.bufsize = 0;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.bufsize > 0) {
            encode();
            this.bufsize = 0;
        }
        this.out.flush();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        this.out.close();
    }

    private void encode() throws IOException {
        if (this.count + 4 > this.bytesPerLine) {
            this.out.write(13);
            this.out.write(10);
            this.count = 0;
        }
        if (this.bufsize == 1) {
            byte a2 = this.buffer[0];
            this.out.write(pem_array[(a2 >>> 2) & 63]);
            this.out.write(pem_array[((a2 << 4) & 48) + ((0 >>> 4) & 15)]);
            this.out.write(61);
            this.out.write(61);
        } else if (this.bufsize == 2) {
            byte a3 = this.buffer[0];
            byte b2 = this.buffer[1];
            this.out.write(pem_array[(a3 >>> 2) & 63]);
            this.out.write(pem_array[((a3 << 4) & 48) + ((b2 >>> 4) & 15)]);
            this.out.write(pem_array[((b2 << 2) & 60) + ((0 >>> 6) & 3)]);
            this.out.write(61);
        } else {
            byte a4 = this.buffer[0];
            byte b3 = this.buffer[1];
            byte c2 = this.buffer[2];
            this.out.write(pem_array[(a4 >>> 2) & 63]);
            this.out.write(pem_array[((a4 << 4) & 48) + ((b3 >>> 4) & 15)]);
            this.out.write(pem_array[((b3 << 2) & 60) + ((c2 >>> 6) & 3)]);
            this.out.write(pem_array[c2 & 63]);
        }
        this.count += 4;
    }

    public static byte[] encode(byte[] inbuf) {
        if (inbuf.length == 0) {
            return inbuf;
        }
        byte[] outbuf = new byte[((inbuf.length + 2) / 3) * 4];
        int inpos = 0;
        int outpos = 0;
        for (int size = inbuf.length; size > 0; size -= 3) {
            if (size == 1) {
                int i2 = inpos;
                inpos++;
                byte a2 = inbuf[i2];
                int i3 = outpos;
                int outpos2 = outpos + 1;
                outbuf[i3] = (byte) pem_array[(a2 >>> 2) & 63];
                int outpos3 = outpos2 + 1;
                outbuf[outpos2] = (byte) pem_array[((a2 << 4) & 48) + ((0 >>> 4) & 15)];
                int outpos4 = outpos3 + 1;
                outbuf[outpos3] = 61;
                outpos = outpos4 + 1;
                outbuf[outpos4] = 61;
            } else if (size == 2) {
                int i4 = inpos;
                int inpos2 = inpos + 1;
                byte a3 = inbuf[i4];
                inpos = inpos2 + 1;
                byte b2 = inbuf[inpos2];
                int i5 = outpos;
                int outpos5 = outpos + 1;
                outbuf[i5] = (byte) pem_array[(a3 >>> 2) & 63];
                int outpos6 = outpos5 + 1;
                outbuf[outpos5] = (byte) pem_array[((a3 << 4) & 48) + ((b2 >>> 4) & 15)];
                int outpos7 = outpos6 + 1;
                outbuf[outpos6] = (byte) pem_array[((b2 << 2) & 60) + ((0 >>> 6) & 3)];
                outpos = outpos7 + 1;
                outbuf[outpos7] = 61;
            } else {
                int i6 = inpos;
                int inpos3 = inpos + 1;
                byte a4 = inbuf[i6];
                int inpos4 = inpos3 + 1;
                byte b3 = inbuf[inpos3];
                inpos = inpos4 + 1;
                byte c2 = inbuf[inpos4];
                int i7 = outpos;
                int outpos8 = outpos + 1;
                outbuf[i7] = (byte) pem_array[(a4 >>> 2) & 63];
                int outpos9 = outpos8 + 1;
                outbuf[outpos8] = (byte) pem_array[((a4 << 4) & 48) + ((b3 >>> 4) & 15)];
                int outpos10 = outpos9 + 1;
                outbuf[outpos9] = (byte) pem_array[((b3 << 2) & 60) + ((c2 >>> 6) & 3)];
                outpos = outpos10 + 1;
                outbuf[outpos10] = (byte) pem_array[c2 & 63];
            }
        }
        return outbuf;
    }
}
