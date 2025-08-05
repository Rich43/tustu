package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/QPEncoderStream.class */
public class QPEncoderStream extends FilterOutputStream {
    private int count;
    private int bytesPerLine;
    private boolean gotSpace;
    private boolean gotCR;
    private static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public QPEncoderStream(OutputStream out, int bytesPerLine) {
        super(out);
        this.count = 0;
        this.gotSpace = false;
        this.gotCR = false;
        this.bytesPerLine = bytesPerLine - 1;
    }

    public QPEncoderStream(OutputStream out) {
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
        int c3 = c2 & 255;
        if (this.gotSpace) {
            if (c3 == 13 || c3 == 10) {
                output(32, true);
            } else {
                output(32, false);
            }
            this.gotSpace = false;
        }
        if (c3 == 13) {
            this.gotCR = true;
            outputCRLF();
            return;
        }
        if (c3 == 10) {
            if (!this.gotCR) {
                outputCRLF();
            }
        } else if (c3 == 32) {
            this.gotSpace = true;
        } else if (c3 < 32 || c3 >= 127 || c3 == 61) {
            output(c3, true);
        } else {
            output(c3, false);
        }
        this.gotCR = false;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.out.close();
    }

    private void outputCRLF() throws IOException {
        this.out.write(13);
        this.out.write(10);
        this.count = 0;
    }

    protected void output(int c2, boolean encode) throws IOException {
        if (encode) {
            int i2 = this.count + 3;
            this.count = i2;
            if (i2 > this.bytesPerLine) {
                this.out.write(61);
                this.out.write(13);
                this.out.write(10);
                this.count = 3;
            }
            this.out.write(61);
            this.out.write(hex[c2 >> 4]);
            this.out.write(hex[c2 & 15]);
            return;
        }
        int i3 = this.count + 1;
        this.count = i3;
        if (i3 > this.bytesPerLine) {
            this.out.write(61);
            this.out.write(13);
            this.out.write(10);
            this.count = 1;
        }
        this.out.write(c2);
    }
}
