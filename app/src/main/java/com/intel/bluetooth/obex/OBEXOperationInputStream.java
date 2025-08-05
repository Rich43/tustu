package com.intel.bluetooth.obex;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXOperationInputStream.class */
class OBEXOperationInputStream extends InputStream {
    private final OBEXOperation operation;
    private byte[] buffer = new byte[256];
    private int readPos = 0;
    private int appendPos = 0;
    private Object lock = new Object();
    private boolean isClosed = false;
    private boolean eofReceived = false;

    OBEXOperationInputStream(OBEXOperation op) {
        this.operation = op;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.isClosed) {
            throw new IOException("Stream closed");
        }
        if (this.operation.isClosed() && this.appendPos == this.readPos) {
            return -1;
        }
        synchronized (this.lock) {
            while (!this.eofReceived && (this.operation instanceof OBEXOperationReceive) && !this.isClosed && !this.operation.isClosed() && this.appendPos == this.readPos) {
                ((OBEXOperationReceive) this.operation).receiveData(this);
            }
            if (this.appendPos == this.readPos) {
                return -1;
            }
            byte[] bArr = this.buffer;
            int i2 = this.readPos;
            this.readPos = i2 + 1;
            return bArr[i2] & 255;
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        int i2;
        synchronized (this.lock) {
            i2 = this.appendPos - this.readPos;
        }
        return i2;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.isClosed = true;
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0037 A[Catch: all -> 0x00b3, TryCatch #0 {, blocks: (B:11:0x001a, B:14:0x0023, B:16:0x0028, B:18:0x0037, B:20:0x0051, B:21:0x0058, B:22:0x008d, B:23:0x00a7, B:24:0x00af), top: B:32:0x001a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void appendData(byte[] r8, boolean r9) {
        /*
            Method dump skipped, instructions count: 187
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.intel.bluetooth.obex.OBEXOperationInputStream.appendData(byte[], boolean):void");
    }
}
