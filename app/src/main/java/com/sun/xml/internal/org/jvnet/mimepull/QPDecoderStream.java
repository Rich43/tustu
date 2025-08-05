package com.sun.xml.internal.org.jvnet.mimepull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/QPDecoderStream.class */
final class QPDecoderStream extends FilterInputStream {

    /* renamed from: ba, reason: collision with root package name */
    private byte[] f12080ba;
    private int spaces;

    public QPDecoderStream(InputStream in) {
        super(new PushbackInputStream(in, 2));
        this.f12080ba = new byte[2];
        this.spaces = 0;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int c2;
        if (this.spaces > 0) {
            this.spaces--;
            return 32;
        }
        int c3 = this.in.read();
        if (c3 == 32) {
            while (true) {
                int i2 = this.in.read();
                c2 = i2;
                if (i2 != 32) {
                    break;
                }
                this.spaces++;
            }
            if (c2 == 13 || c2 == 10 || c2 == -1) {
                this.spaces = 0;
            } else {
                ((PushbackInputStream) this.in).unread(c2);
                c2 = 32;
            }
            return c2;
        }
        if (c3 == 61) {
            int a2 = this.in.read();
            if (a2 == 10) {
                return read();
            }
            if (a2 == 13) {
                int b2 = this.in.read();
                if (b2 != 10) {
                    ((PushbackInputStream) this.in).unread(b2);
                }
                return read();
            }
            if (a2 == -1) {
                return -1;
            }
            this.f12080ba[0] = (byte) a2;
            this.f12080ba[1] = (byte) this.in.read();
            try {
                return ASCIIUtility.parseInt(this.f12080ba, 0, 2, 16);
            } catch (NumberFormatException e2) {
                ((PushbackInputStream) this.in).unread(this.f12080ba);
                return c3;
            }
        }
        return c3;
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
    public long skip(long n2) throws IOException {
        long skipped;
        long j2 = 0;
        while (true) {
            skipped = j2;
            long j3 = n2;
            n2 = j3 - 1;
            if (j3 <= 0 || read() < 0) {
                break;
            }
            j2 = skipped + 1;
        }
        return skipped;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        return this.in.available();
    }
}
