package sun.rmi.transport.tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/rmi/transport/tcp/MultiplexInputStream.class */
final class MultiplexInputStream extends InputStream {
    private ConnectionMultiplexer manager;
    private MultiplexConnectionInfo info;
    private byte[] buffer;
    private int waterMark;
    private int present = 0;
    private int pos = 0;
    private int requested = 0;
    private boolean disconnected = false;
    private Object lock = new Object();
    private byte[] temp = new byte[1];

    MultiplexInputStream(ConnectionMultiplexer connectionMultiplexer, MultiplexConnectionInfo multiplexConnectionInfo, int i2) {
        this.manager = connectionMultiplexer;
        this.info = multiplexConnectionInfo;
        this.buffer = new byte[i2];
        this.waterMark = i2 / 2;
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        if (read(this.temp, 0, 1) != 1) {
            return -1;
        }
        return this.temp[0] & 255;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
        int iMax;
        if (i3 <= 0) {
            return 0;
        }
        synchronized (this.lock) {
            if (this.pos >= this.present) {
                this.present = 0;
                this.pos = 0;
            } else if (this.pos >= this.waterMark) {
                System.arraycopy(this.buffer, this.pos, this.buffer, 0, this.present - this.pos);
                this.present -= this.pos;
                this.pos = 0;
            }
            iMax = Math.max((this.buffer.length - this.present) - this.requested, 0);
        }
        if (iMax > 0) {
            this.manager.sendRequest(this.info, iMax);
        }
        synchronized (this.lock) {
            this.requested += iMax;
            while (this.pos >= this.present && !this.disconnected) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e2) {
                }
            }
            if (this.disconnected && this.pos >= this.present) {
                return -1;
            }
            int i4 = this.present - this.pos;
            if (i3 < i4) {
                System.arraycopy(this.buffer, this.pos, bArr, i2, i3);
                this.pos += i3;
                return i3;
            }
            System.arraycopy(this.buffer, this.pos, bArr, i2, i4);
            this.present = 0;
            this.pos = 0;
            return i4;
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        int i2;
        synchronized (this.lock) {
            i2 = this.present - this.pos;
        }
        return i2;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.manager.sendClose(this.info);
    }

    void receive(int i2, DataInputStream dataInputStream) throws IOException {
        synchronized (this.lock) {
            if (this.pos > 0 && this.buffer.length - this.present < i2) {
                System.arraycopy(this.buffer, this.pos, this.buffer, 0, this.present - this.pos);
                this.present -= this.pos;
                this.pos = 0;
            }
            if (this.buffer.length - this.present < i2) {
                throw new IOException("Receive buffer overflow");
            }
            dataInputStream.readFully(this.buffer, this.present, i2);
            this.present += i2;
            this.requested -= i2;
            this.lock.notifyAll();
        }
    }

    void disconnect() {
        synchronized (this.lock) {
            this.disconnected = true;
            this.lock.notifyAll();
        }
    }
}
