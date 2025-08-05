package com.ftdi;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: JavaFTD2XX.jar:com/ftdi/FTDeviceInputStream.class */
class FTDeviceInputStream extends InputStream {
    private final FTDevice device;

    FTDeviceInputStream(FTDevice device) {
        this.device = device;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.device.read();
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.device.getQueueStatus();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.device.close();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2) throws IOException {
        return this.device.read(b2);
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int len) throws IOException {
        return this.device.read(b2, off, len);
    }
}
