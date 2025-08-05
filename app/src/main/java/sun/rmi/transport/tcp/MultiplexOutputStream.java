package sun.rmi.transport.tcp;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/rmi/transport/tcp/MultiplexOutputStream.class */
final class MultiplexOutputStream extends OutputStream {
    private ConnectionMultiplexer manager;
    private MultiplexConnectionInfo info;
    private byte[] buffer;
    private int pos;
    private int requested = 0;
    private boolean disconnected = false;
    private Object lock = new Object();

    MultiplexOutputStream(ConnectionMultiplexer connectionMultiplexer, MultiplexConnectionInfo multiplexConnectionInfo, int i2) {
        this.pos = 0;
        this.manager = connectionMultiplexer;
        this.info = multiplexConnectionInfo;
        this.buffer = new byte[i2];
        this.pos = 0;
    }

    @Override // java.io.OutputStream
    public synchronized void write(int i2) throws IOException {
        while (this.pos >= this.buffer.length) {
            push();
        }
        byte[] bArr = this.buffer;
        int i3 = this.pos;
        this.pos = i3 + 1;
        bArr[i3] = (byte) i2;
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
        int i4;
        if (i3 <= 0) {
            return;
        }
        if (i3 <= this.buffer.length - this.pos) {
            System.arraycopy(bArr, i2, this.buffer, this.pos, i3);
            this.pos += i3;
            return;
        }
        flush();
        while (true) {
            synchronized (this.lock) {
                while (true) {
                    i4 = this.requested;
                    if (i4 >= 1 || this.disconnected) {
                        break;
                    } else {
                        try {
                            this.lock.wait();
                        } catch (InterruptedException e2) {
                        }
                    }
                }
                if (this.disconnected) {
                    throw new IOException("Connection closed");
                }
            }
            if (i4 < i3) {
                this.manager.sendTransmit(this.info, bArr, i2, i4);
                i2 += i4;
                i3 -= i4;
                synchronized (this.lock) {
                    this.requested -= i4;
                }
            } else {
                this.manager.sendTransmit(this.info, bArr, i2, i3);
                synchronized (this.lock) {
                    this.requested -= i3;
                }
                return;
            }
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public synchronized void flush() throws IOException {
        while (this.pos > 0) {
            push();
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.manager.sendClose(this.info);
    }

    void request(int i2) {
        synchronized (this.lock) {
            this.requested += i2;
            this.lock.notifyAll();
        }
    }

    void disconnect() {
        synchronized (this.lock) {
            this.disconnected = true;
            this.lock.notifyAll();
        }
    }

    private void push() throws IOException {
        int i2;
        synchronized (this.lock) {
            while (true) {
                i2 = this.requested;
                if (i2 >= 1 || this.disconnected) {
                    break;
                } else {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e2) {
                    }
                }
            }
            if (this.disconnected) {
                throw new IOException("Connection closed");
            }
        }
        if (i2 < this.pos) {
            this.manager.sendTransmit(this.info, this.buffer, 0, i2);
            System.arraycopy(this.buffer, i2, this.buffer, 0, this.pos - i2);
            this.pos -= i2;
            synchronized (this.lock) {
                this.requested -= i2;
            }
            return;
        }
        this.manager.sendTransmit(this.info, this.buffer, 0, this.pos);
        synchronized (this.lock) {
            this.requested -= this.pos;
        }
        this.pos = 0;
    }
}
