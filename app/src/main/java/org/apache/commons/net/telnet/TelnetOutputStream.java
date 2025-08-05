package org.apache.commons.net.telnet;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/telnet/TelnetOutputStream.class */
final class TelnetOutputStream extends OutputStream {
    private final TelnetClient __client;
    private final boolean __convertCRtoCRLF = true;
    private boolean __lastWasCR = false;

    TelnetOutputStream(TelnetClient client) {
        this.__client = client;
    }

    @Override // java.io.OutputStream
    public void write(int ch) throws IOException {
        synchronized (this.__client) {
            int ch2 = ch & 255;
            if (this.__client._requestedWont(0)) {
                if (this.__lastWasCR) {
                    this.__client._sendByte(10);
                    if (ch2 == 10) {
                        this.__lastWasCR = false;
                        return;
                    }
                }
                switch (ch2) {
                    case 10:
                        if (!this.__lastWasCR) {
                            this.__client._sendByte(13);
                        }
                        this.__client._sendByte(ch2);
                        this.__lastWasCR = false;
                        break;
                    case 13:
                        this.__client._sendByte(13);
                        this.__lastWasCR = true;
                        break;
                    case 255:
                        this.__client._sendByte(255);
                        this.__client._sendByte(255);
                        this.__lastWasCR = false;
                        break;
                    default:
                        this.__client._sendByte(ch2);
                        this.__lastWasCR = false;
                        break;
                }
            } else if (ch2 == 255) {
                this.__client._sendByte(ch2);
                this.__client._sendByte(255);
            } else {
                this.__client._sendByte(ch2);
            }
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] buffer) throws IOException {
        write(buffer, 0, buffer.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] buffer, int offset, int length) throws IOException {
        synchronized (this.__client) {
            while (true) {
                int i2 = length;
                length--;
                if (i2 > 0) {
                    int i3 = offset;
                    offset++;
                    write(buffer[i3]);
                }
            }
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.__client._flushOutputStream();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.__client._closeOutputStream();
    }
}
