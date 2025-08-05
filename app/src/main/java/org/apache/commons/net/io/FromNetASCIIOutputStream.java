package org.apache.commons.net.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/FromNetASCIIOutputStream.class */
public final class FromNetASCIIOutputStream extends FilterOutputStream {
    private boolean __lastWasCR;

    public FromNetASCIIOutputStream(OutputStream output) {
        super(output);
        this.__lastWasCR = false;
    }

    private void __write(int ch) throws IOException {
        switch (ch) {
            case 10:
                if (this.__lastWasCR) {
                    this.out.write(FromNetASCIIInputStream._lineSeparatorBytes);
                    this.__lastWasCR = false;
                    break;
                } else {
                    this.__lastWasCR = false;
                    this.out.write(10);
                    break;
                }
            case 13:
                this.__lastWasCR = true;
                break;
            default:
                if (this.__lastWasCR) {
                    this.out.write(13);
                    this.__lastWasCR = false;
                }
                this.out.write(ch);
                break;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(int ch) throws IOException {
        if (FromNetASCIIInputStream._noConversionRequired) {
            this.out.write(ch);
        } else {
            __write(ch);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] buffer) throws IOException {
        write(buffer, 0, buffer.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] buffer, int offset, int length) throws IOException {
        if (FromNetASCIIInputStream._noConversionRequired) {
            this.out.write(buffer, offset, length);
            return;
        }
        while (true) {
            int i2 = length;
            length--;
            if (i2 > 0) {
                int i3 = offset;
                offset++;
                __write(buffer[i3]);
            } else {
                return;
            }
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (FromNetASCIIInputStream._noConversionRequired) {
            super.close();
            return;
        }
        if (this.__lastWasCR) {
            this.out.write(13);
        }
        super.close();
    }
}
