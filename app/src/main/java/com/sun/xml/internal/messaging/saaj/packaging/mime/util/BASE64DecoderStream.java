package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/BASE64DecoderStream.class */
public class BASE64DecoderStream extends FilterInputStream {
    private byte[] buffer;
    private int bufsize;
    private int index;
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] pem_convert_array = new byte[256];
    private byte[] decode_buffer;

    public BASE64DecoderStream(InputStream in) {
        super(in);
        this.bufsize = 0;
        this.index = 0;
        this.decode_buffer = new byte[4];
        this.buffer = new byte[3];
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.index >= this.bufsize) {
            decode();
            if (this.bufsize == 0) {
                return -1;
            }
            this.index = 0;
        }
        byte[] bArr = this.buffer;
        int i2 = this.index;
        this.index = i2 + 1;
        return bArr[i2] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buf, int off, int len) throws IOException {
        int i2 = 0;
        while (true) {
            if (i2 >= len) {
                break;
            }
            int c2 = read();
            if (c2 == -1) {
                if (i2 == 0) {
                    i2 = -1;
                }
            } else {
                buf[off + i2] = (byte) c2;
                i2++;
            }
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        return ((this.in.available() * 3) / 4) + (this.bufsize - this.index);
    }

    static {
        for (int i2 = 0; i2 < 255; i2++) {
            pem_convert_array[i2] = -1;
        }
        for (int i3 = 0; i3 < pem_array.length; i3++) {
            pem_convert_array[pem_array[i3]] = (byte) i3;
        }
    }

    private void decode() throws IOException {
        this.bufsize = 0;
        int got = 0;
        while (got < 4) {
            int i2 = this.in.read();
            if (i2 == -1) {
                if (got == 0) {
                    return;
                } else {
                    throw new IOException("Error in encoded stream, got " + got);
                }
            } else if ((i2 >= 0 && i2 < 256 && i2 == 61) || pem_convert_array[i2] != -1) {
                int i3 = got;
                got++;
                this.decode_buffer[i3] = (byte) i2;
            }
        }
        byte a2 = pem_convert_array[this.decode_buffer[0] & 255];
        byte b2 = pem_convert_array[this.decode_buffer[1] & 255];
        byte[] bArr = this.buffer;
        int i4 = this.bufsize;
        this.bufsize = i4 + 1;
        bArr[i4] = (byte) (((a2 << 2) & 252) | ((b2 >>> 4) & 3));
        if (this.decode_buffer[2] == 61) {
            return;
        }
        byte b3 = pem_convert_array[this.decode_buffer[2] & 255];
        byte[] bArr2 = this.buffer;
        int i5 = this.bufsize;
        this.bufsize = i5 + 1;
        bArr2[i5] = (byte) (((b2 << 4) & 240) | ((b3 >>> 2) & 15));
        if (this.decode_buffer[3] == 61) {
            return;
        }
        byte b4 = pem_convert_array[this.decode_buffer[3] & 255];
        byte[] bArr3 = this.buffer;
        int i6 = this.bufsize;
        this.bufsize = i6 + 1;
        bArr3[i6] = (byte) (((b3 << 6) & 192) | (b4 & 63));
    }

    public static byte[] decode(byte[] inbuf) {
        int size = (inbuf.length / 4) * 3;
        if (size == 0) {
            return inbuf;
        }
        if (inbuf[inbuf.length - 1] == 61) {
            size--;
            if (inbuf[inbuf.length - 2] == 61) {
                size--;
            }
        }
        byte[] outbuf = new byte[size];
        int inpos = 0;
        int outpos = 0;
        for (int size2 = inbuf.length; size2 > 0; size2 -= 4) {
            int i2 = inpos;
            int inpos2 = inpos + 1;
            byte a2 = pem_convert_array[inbuf[i2] & 255];
            int inpos3 = inpos2 + 1;
            byte b2 = pem_convert_array[inbuf[inpos2] & 255];
            int i3 = outpos;
            int outpos2 = outpos + 1;
            outbuf[i3] = (byte) (((a2 << 2) & 252) | ((b2 >>> 4) & 3));
            if (inbuf[inpos3] == 61) {
                return outbuf;
            }
            int inpos4 = inpos3 + 1;
            byte b3 = pem_convert_array[inbuf[inpos3] & 255];
            int outpos3 = outpos2 + 1;
            outbuf[outpos2] = (byte) (((b2 << 4) & 240) | ((b3 >>> 2) & 15));
            if (inbuf[inpos4] == 61) {
                return outbuf;
            }
            inpos = inpos4 + 1;
            outpos = outpos3 + 1;
            outbuf[outpos3] = (byte) (((b3 << 6) & 192) | (pem_convert_array[inbuf[inpos4] & 255] & 63));
        }
        return outbuf;
    }
}
