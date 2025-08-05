package org.apache.commons.net.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/ToNetASCIIOutputStream.class */
public final class ToNetASCIIOutputStream extends FilterOutputStream {
    private boolean __lastWasCR;

    public ToNetASCIIOutputStream(OutputStream output) {
        super(output);
        this.__lastWasCR = false;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(int ch) throws IOException {
        switch (ch) {
            case 10:
                if (!this.__lastWasCR) {
                    this.out.write(13);
                    break;
                }
                break;
            case 13:
                this.__lastWasCR = true;
                this.out.write(13);
                return;
        }
        this.__lastWasCR = false;
        this.out.write(ch);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] buffer) throws IOException {
        write(buffer, 0, buffer.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] buffer, int offset, int length) throws IOException {
        while (true) {
            int i2 = length;
            length--;
            if (i2 > 0) {
                int i3 = offset;
                offset++;
                write(buffer[i3]);
            } else {
                return;
            }
        }
    }
}
