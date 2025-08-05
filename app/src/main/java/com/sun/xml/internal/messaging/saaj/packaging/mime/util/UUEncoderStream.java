package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/UUEncoderStream.class */
public class UUEncoderStream extends FilterOutputStream {
    private byte[] buffer;
    private int bufsize;
    private boolean wrotePrefix;
    protected String name;
    protected int mode;

    public UUEncoderStream(OutputStream out) {
        this(out, "encoder.buf", 644);
    }

    public UUEncoderStream(OutputStream out, String name) {
        this(out, name, 644);
    }

    public UUEncoderStream(OutputStream out, String name, int mode) {
        super(out);
        this.bufsize = 0;
        this.wrotePrefix = false;
        this.name = name;
        this.mode = mode;
        this.buffer = new byte[45];
    }

    public void setNameMode(String name, int mode) {
        this.name = name;
        this.mode = mode;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b2, int off, int len) throws IOException {
        for (int i2 = 0; i2 < len; i2++) {
            write(b2[off + i2]);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] data) throws IOException {
        write(data, 0, data.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int c2) throws IOException {
        byte[] bArr = this.buffer;
        int i2 = this.bufsize;
        this.bufsize = i2 + 1;
        bArr[i2] = (byte) c2;
        if (this.bufsize == 45) {
            writePrefix();
            encode();
            this.bufsize = 0;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.bufsize > 0) {
            writePrefix();
            encode();
        }
        writeSuffix();
        this.out.flush();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        this.out.close();
    }

    private void writePrefix() throws IOException {
        if (!this.wrotePrefix) {
            PrintStream ps = new PrintStream(this.out);
            ps.println("begin " + this.mode + " " + this.name);
            ps.flush();
            this.wrotePrefix = true;
        }
    }

    private void writeSuffix() throws IOException {
        PrintStream ps = new PrintStream(this.out);
        ps.println(" \nend");
        ps.flush();
    }

    private void encode() throws IOException {
        byte b2;
        byte b3;
        int i2 = 0;
        this.out.write((this.bufsize & 63) + 32);
        while (i2 < this.bufsize) {
            int i3 = i2;
            i2++;
            byte a2 = this.buffer[i3];
            if (i2 < this.bufsize) {
                i2++;
                b2 = this.buffer[i2];
                if (i2 < this.bufsize) {
                    i2++;
                    b3 = this.buffer[i2];
                } else {
                    b3 = 1;
                }
            } else {
                b2 = 1;
                b3 = 1;
            }
            byte c2 = b3;
            int c1 = (a2 >>> 2) & 63;
            int c22 = ((a2 << 4) & 48) | ((b2 >>> 4) & 15);
            int c3 = ((b2 << 2) & 60) | ((c2 >>> 6) & 3);
            int c4 = c2 & 63;
            this.out.write(c1 + 32);
            this.out.write(c22 + 32);
            this.out.write(c3 + 32);
            this.out.write(c4 + 32);
        }
        this.out.write(10);
    }
}
