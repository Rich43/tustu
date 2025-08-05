package com.ftdi;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: JavaFTD2XX.jar:com/ftdi/FTDeviceOutputStream.class */
class FTDeviceOutputStream extends OutputStream {
    private final FTDevice device;

    FTDeviceOutputStream(FTDevice device) {
        this.device = device;
    }

    @Override // java.io.OutputStream
    public void write(int b2) throws IOException {
        this.device.write(b2);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.device.close();
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2) throws IOException {
        this.device.write(b2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2, int off, int len) throws IOException {
        this.device.write(b2, off, len);
    }
}
