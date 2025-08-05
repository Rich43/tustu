package com.sun.imageio.plugins.jpeg;

import java.io.IOException;
import javax.imageio.IIOException;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGBuffer.class */
class JPEGBuffer {
    private boolean debug = false;
    final int BUFFER_SIZE = 4096;
    byte[] buf = new byte[4096];
    int bufAvail = 0;
    int bufPtr = 0;
    ImageInputStream iis;

    JPEGBuffer(ImageInputStream imageInputStream) {
        this.iis = imageInputStream;
    }

    void loadBuf(int i2) throws IOException {
        if (this.debug) {
            System.out.print("loadbuf called with ");
            System.out.print("count " + i2 + ", ");
            System.out.println("bufAvail " + this.bufAvail + ", ");
        }
        if (i2 != 0) {
            if (this.bufAvail >= i2) {
                return;
            }
        } else if (this.bufAvail == 4096) {
            return;
        }
        if (this.bufAvail > 0 && this.bufAvail < 4096) {
            System.arraycopy(this.buf, this.bufPtr, this.buf, 0, this.bufAvail);
        }
        int i3 = this.iis.read(this.buf, this.bufAvail, this.buf.length - this.bufAvail);
        if (this.debug) {
            System.out.println("iis.read returned " + i3);
        }
        if (i3 != -1) {
            this.bufAvail += i3;
        }
        this.bufPtr = 0;
        if (this.bufAvail < Math.min(4096, i2)) {
            throw new IIOException("Image Format Error");
        }
    }

    void readData(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (this.bufAvail >= length) {
            System.arraycopy(this.buf, this.bufPtr, bArr, 0, length);
            this.bufAvail -= length;
            this.bufPtr += length;
            return;
        }
        int i2 = 0;
        if (this.bufAvail > 0) {
            System.arraycopy(this.buf, this.bufPtr, bArr, 0, this.bufAvail);
            i2 = this.bufAvail;
            length -= this.bufAvail;
            this.bufAvail = 0;
            this.bufPtr = 0;
        }
        if (this.iis.read(bArr, i2, length) != length) {
            throw new IIOException("Image format Error");
        }
    }

    void skipData(int i2) throws IOException {
        if (this.bufAvail >= i2) {
            this.bufAvail -= i2;
            this.bufPtr += i2;
            return;
        }
        if (this.bufAvail > 0) {
            i2 -= this.bufAvail;
            this.bufAvail = 0;
            this.bufPtr = 0;
        }
        if (this.iis.skipBytes(i2) != i2) {
            throw new IIOException("Image format Error");
        }
    }

    void pushBack() throws IOException {
        this.iis.seek(this.iis.getStreamPosition() - this.bufAvail);
        this.bufAvail = 0;
        this.bufPtr = 0;
    }

    long getStreamPosition() throws IOException {
        return this.iis.getStreamPosition() - this.bufAvail;
    }

    boolean scanForFF(JPEGImageReader jPEGImageReader) throws IOException {
        boolean z2 = false;
        boolean z3 = false;
        while (!z3) {
            while (true) {
                if (this.bufAvail <= 0) {
                    break;
                }
                byte[] bArr = this.buf;
                int i2 = this.bufPtr;
                this.bufPtr = i2 + 1;
                if ((bArr[i2] & 255) == 255) {
                    this.bufAvail--;
                    z3 = true;
                    break;
                }
                this.bufAvail--;
            }
            loadBuf(0);
            if (z3) {
                while (this.bufAvail > 0 && (this.buf[this.bufPtr] & 255) == 255) {
                    this.bufPtr++;
                    this.bufAvail--;
                }
            }
            if (this.bufAvail == 0) {
                z2 = true;
                this.buf[0] = -39;
                this.bufAvail = 1;
                this.bufPtr = 0;
                z3 = true;
            }
        }
        return z2;
    }

    void print(int i2) {
        System.out.print("buffer has ");
        System.out.print(this.bufAvail);
        System.out.println(" bytes available");
        if (this.bufAvail < i2) {
            i2 = this.bufAvail;
        }
        int i3 = this.bufPtr;
        while (i2 > 0) {
            int i4 = i3;
            i3++;
            System.out.print(" " + Integer.toHexString(this.buf[i4] & 255));
            i2--;
        }
        System.out.println();
    }
}
