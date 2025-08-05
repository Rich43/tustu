package org.apache.commons.net.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/ToNetASCIIInputStream.class */
public final class ToNetASCIIInputStream extends FilterInputStream {
    private static final int __NOTHING_SPECIAL = 0;
    private static final int __LAST_WAS_CR = 1;
    private static final int __LAST_WAS_NL = 2;
    private int __status;

    public ToNetASCIIInputStream(InputStream input) {
        super(input);
        this.__status = 0;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.__status == 2) {
            this.__status = 0;
            return 10;
        }
        int ch = this.in.read();
        switch (ch) {
            case 10:
                if (this.__status != 1) {
                    this.__status = 2;
                    return 13;
                }
                break;
            case 13:
                this.__status = 1;
                return 13;
        }
        this.__status = 0;
        return ch;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int i2;
        if (length < 1) {
            return 0;
        }
        int ch = available();
        if (length > ch) {
            length = ch;
        }
        if (length < 1) {
            length = 1;
        }
        int i3 = read();
        int ch2 = i3;
        if (i3 == -1) {
            return -1;
        }
        do {
            int i4 = offset;
            offset++;
            buffer[i4] = (byte) ch2;
            length--;
            if (length <= 0) {
                break;
            }
            i2 = read();
            ch2 = i2;
        } while (i2 != -1);
        return offset - offset;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        int result = this.in.available();
        if (this.__status == 2) {
            return result + 1;
        }
        return result;
    }
}
