package com.intel.bluetooth;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothRFCommOutputStream.class */
class BluetoothRFCommOutputStream extends OutputStream {
    private volatile BluetoothRFCommConnection conn;

    public BluetoothRFCommOutputStream(BluetoothRFCommConnection conn) {
        this.conn = conn;
    }

    @Override // java.io.OutputStream
    public void write(int b2) throws IOException {
        if (this.conn == null) {
            throw new IOException("Stream closed");
        }
        this.conn.bluetoothStack.connectionRfWrite(this.conn.handle, b2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2, int off, int len) throws IOException {
        if (off < 0 || len < 0 || off + len > b2.length) {
            throw new IndexOutOfBoundsException();
        }
        if (this.conn == null) {
            throw new IOException("Stream closed");
        }
        this.conn.bluetoothStack.connectionRfWrite(this.conn.handle, b2, off, len);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.conn == null) {
            throw new IOException("Stream closed");
        }
        super.flush();
        this.conn.bluetoothStack.connectionRfFlush(this.conn.handle);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        BluetoothRFCommConnection c2 = this.conn;
        if (c2 != null) {
            this.conn = null;
            c2.streamClosed();
        }
    }

    boolean isClosed() {
        return this.conn == null;
    }
}
