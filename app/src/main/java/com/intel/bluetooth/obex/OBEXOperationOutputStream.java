package com.intel.bluetooth.obex;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXOperationOutputStream.class */
class OBEXOperationOutputStream extends OutputStream {
    private final OBEXOperationDelivery operation;
    private byte[] buffer;
    private Object lock = new Object();
    private boolean isClosed = false;
    private int bufferLength = 0;

    OBEXOperationOutputStream(int mtu, OBEXOperationDelivery op) {
        this.operation = op;
        this.buffer = new byte[mtu - 11];
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        write(new byte[]{(byte) i2}, 0, 1);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2, int off, int len) throws IOException {
        if (this.operation.isClosed() || this.isClosed) {
            throw new IOException("stream closed");
        }
        if (b2 == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || off + len > b2.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return;
        }
        synchronized (this.lock) {
            int written = 0;
            while (written < len) {
                int available = this.buffer.length - this.bufferLength;
                if (len - written < available) {
                    available = len - written;
                }
                System.arraycopy(b2, off + written, this.buffer, this.bufferLength, available);
                this.bufferLength += available;
                written += available;
                if (this.bufferLength == this.buffer.length) {
                    this.operation.deliverPacket(false, this.buffer);
                    this.bufferLength = 0;
                }
            }
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.bufferLength > 0) {
            deliverBuffer(false);
        }
    }

    void deliverBuffer(boolean finalPacket) throws IOException {
        synchronized (this.lock) {
            byte[] b2 = new byte[this.bufferLength];
            System.arraycopy(this.buffer, 0, b2, 0, this.bufferLength);
            this.operation.deliverPacket(finalPacket, b2);
            this.bufferLength = 0;
        }
    }

    void abort() {
        synchronized (this.lock) {
            this.isClosed = true;
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.isClosed) {
            synchronized (this.lock) {
                this.isClosed = true;
                if (!this.operation.isClosed()) {
                    deliverBuffer(true);
                }
            }
        }
    }
}
