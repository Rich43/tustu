package com.intel.bluetooth;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothRFCommInputStream.class */
class BluetoothRFCommInputStream extends InputStream {
    private volatile BluetoothRFCommConnection conn;

    public BluetoothRFCommInputStream(BluetoothRFCommConnection conn) {
        this.conn = conn;
    }

    @Override // java.io.InputStream
    public synchronized int available() throws IOException {
        if (this.conn == null) {
            throw new IOException("Stream closed");
        }
        return this.conn.bluetoothStack.connectionRfReadAvailable(this.conn.handle);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.conn == null) {
            throw new IOException("Stream closed");
        }
        try {
            return this.conn.bluetoothStack.connectionRfRead(this.conn.handle);
        } catch (IOException e2) {
            if (isClosed()) {
                return -1;
            }
            throw e2;
        }
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int len) throws IOException {
        if (off < 0 || len < 0 || off + len > b2.length) {
            throw new IndexOutOfBoundsException();
        }
        if (this.conn == null) {
            throw new IOException("Stream closed");
        }
        if (len == 0) {
            return 0;
        }
        try {
            return this.conn.bluetoothStack.connectionRfRead(this.conn.handle, b2, off, len);
        } catch (IOException e2) {
            if (isClosed()) {
                return -1;
            }
            throw e2;
        }
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
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
