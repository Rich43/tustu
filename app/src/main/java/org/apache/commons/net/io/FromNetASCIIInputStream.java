package org.apache.commons.net.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/FromNetASCIIInputStream.class */
public final class FromNetASCIIInputStream extends PushbackInputStream {
    static final byte[] _lineSeparatorBytes;
    private int __length;
    static final String _lineSeparator = System.getProperty("line.separator");
    static final boolean _noConversionRequired = _lineSeparator.equals("\r\n");

    static {
        try {
            _lineSeparatorBytes = _lineSeparator.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("Broken JVM - cannot find US-ASCII charset!", e2);
        }
    }

    public static final boolean isConversionRequired() {
        return !_noConversionRequired;
    }

    public FromNetASCIIInputStream(InputStream input) {
        super(input, _lineSeparatorBytes.length + 1);
        this.__length = 0;
    }

    private int __read() throws IOException {
        int ch = super.read();
        if (ch == 13) {
            int ch2 = super.read();
            if (ch2 == 10) {
                unread(_lineSeparatorBytes);
                ch = super.read();
                this.__length--;
            } else {
                if (ch2 != -1) {
                    unread(ch2);
                    return 13;
                }
                return 13;
            }
        }
        return ch;
    }

    @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (_noConversionRequired) {
            return super.read();
        }
        return __read();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int i__read;
        if (_noConversionRequired) {
            return super.read(buffer, offset, length);
        }
        if (length < 1) {
            return 0;
        }
        int ch = available();
        this.__length = length > ch ? ch : length;
        if (this.__length < 1) {
            this.__length = 1;
        }
        int i__read2 = __read();
        int ch2 = i__read2;
        if (i__read2 == -1) {
            return -1;
        }
        do {
            int i2 = offset;
            offset++;
            buffer[i2] = (byte) ch2;
            int i3 = this.__length - 1;
            this.__length = i3;
            if (i3 <= 0) {
                break;
            }
            i__read = __read();
            ch2 = i__read;
        } while (i__read != -1);
        return offset - offset;
    }

    @Override // java.io.PushbackInputStream, java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        if (this.in == null) {
            throw new IOException("Stream closed");
        }
        return (this.buf.length - this.pos) + this.in.available();
    }
}
