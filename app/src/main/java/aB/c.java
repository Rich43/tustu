package aB;

import bH.C;
import com.ftdi.FTD2XXException;
import com.ftdi.FTDevice;
import java.io.InputStream;

/* loaded from: TunerStudioMS.jar:aB/c.class */
public class c extends InputStream {

    /* renamed from: a, reason: collision with root package name */
    FTDevice f2282a;

    /* renamed from: b, reason: collision with root package name */
    int f2283b = -1;

    /* renamed from: c, reason: collision with root package name */
    boolean f2284c = false;

    c(FTDevice fTDevice) {
        this.f2282a = fTDevice;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) throws FTD2XXException {
        if (!this.f2284c) {
            try {
                return read(bArr, 0, bArr.length);
            } catch (FTD2XXException e2) {
                this.f2282a.open();
                throw e2;
            }
        }
        int i2 = 0;
        do {
            int i3 = read();
            if (i3 == -1) {
                return i2;
            }
            int i4 = i2;
            i2++;
            bArr[i4] = (byte) i3;
        } while (i2 < bArr.length);
        return i2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x004b, code lost:
    
        r11 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x004d, code lost:
    
        r7.f2282a.open();
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0056, code lost:
    
        throw r11;
     */
    @Override // java.io.InputStream
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int read(byte[] r8, int r9, int r10) throws com.ftdi.FTD2XXException {
        /*
            r7 = this;
            r0 = r7
            boolean r0 = r0.f2284c
            if (r0 == 0) goto L40
            r0 = r7
            int r0 = r0.f2283b
            if (r0 < 0) goto L35
            r0 = r8
            r1 = r9
            r2 = r7
            int r2 = r2.f2283b
            byte r2 = (byte) r2
            r0[r1] = r2
            r0 = r7
            r1 = -1
            r0.f2283b = r1
            r0 = r10
            r1 = r9
            int r0 = r0 - r1
            r1 = 1
            if (r0 != r1) goto L24
            r0 = 1
            return r0
        L24:
            r0 = 1
            r1 = r7
            com.ftdi.FTDevice r1 = r1.f2282a
            r2 = r8
            r3 = r9
            r4 = 1
            int r3 = r3 + r4
            r4 = r10
            r5 = 1
            int r4 = r4 - r5
            int r1 = r1.read(r2, r3, r4)
            int r0 = r0 + r1
            return r0
        L35:
            r0 = r7
            com.ftdi.FTDevice r0 = r0.f2282a
            r1 = r8
            r2 = r9
            r3 = r10
            int r0 = r0.read(r1, r2, r3)
            return r0
        L40:
            r0 = r7
            com.ftdi.FTDevice r0 = r0.f2282a     // Catch: com.ftdi.FTD2XXException -> L4b
            r1 = r8
            r2 = r9
            r3 = r10
            int r0 = r0.read(r1, r2, r3)     // Catch: com.ftdi.FTD2XXException -> L4b
            return r0
        L4b:
            r11 = move-exception
            r0 = r7
            com.ftdi.FTDevice r0 = r0.f2282a
            r0.open()
            r0 = r11
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: aB.c.read(byte[], int, int):int");
    }

    @Override // java.io.InputStream
    public int read() throws FTD2XXException {
        if (!this.f2284c) {
            try {
                return this.f2282a.read();
            } catch (FTD2XXException e2) {
                this.f2282a.open();
                throw e2;
            }
        }
        if (this.f2283b >= 0) {
            int i2 = this.f2283b;
            this.f2283b = -1;
            return i2;
        }
        byte[] bArr = this.f2282a.read(1);
        if (bArr == null || bArr.length <= 0) {
            return -1;
        }
        return bArr[0];
    }

    @Override // java.io.InputStream
    public int available() throws FTD2XXException {
        if (this.f2284c) {
            if (this.f2283b >= 0) {
                return 1 + this.f2282a.getQueueStatus();
            }
            this.f2283b = read();
            return this.f2283b < 0 ? this.f2282a.getQueueStatus() : 1 + this.f2282a.getQueueStatus();
        }
        try {
            return this.f2282a.getInputStream().available();
        } catch (FTD2XXException e2) {
            C.a("Error reading from D2XX, will try reopening");
            this.f2282a.open();
            return this.f2282a.getInputStream().available();
        }
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            this.f2282a.close();
            this.f2283b = -1;
        } catch (Exception e2) {
        }
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }
}
