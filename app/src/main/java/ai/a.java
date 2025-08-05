package aI;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aI/a.class */
public class a extends InputStream {

    /* renamed from: f, reason: collision with root package name */
    private byte[] f2420f;

    /* renamed from: c, reason: collision with root package name */
    protected int f2423c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f2418d = true;

    /* renamed from: e, reason: collision with root package name */
    private List f2419e = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    protected int f2422b = 0;

    /* renamed from: a, reason: collision with root package name */
    protected int f2421a = 0;

    public a(byte[] bArr) {
        this.f2420f = null;
        this.f2420f = bArr;
        this.f2423c = bArr.length;
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        if (this.f2421a == this.f2423c && this.f2418d) {
            b(a());
        }
        if (this.f2421a >= this.f2423c) {
            return -1;
        }
        byte[] bArr = this.f2420f;
        int i2 = this.f2421a;
        this.f2421a = i2 + 1;
        return bArr[i2] & 255;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        }
        if (this.f2421a >= this.f2423c) {
            if (this.f2421a == this.f2423c && this.f2418d) {
                b(a());
            }
            if (this.f2421a >= this.f2423c) {
                return -1;
            }
        }
        if (this.f2421a + i3 > this.f2423c) {
            i3 = this.f2423c - this.f2421a;
        }
        if (i3 <= 0) {
            return 0;
        }
        System.arraycopy(this.f2420f, this.f2421a, bArr, i2, i3);
        this.f2421a += i3;
        return i3;
    }

    @Override // java.io.InputStream
    public synchronized long skip(long j2) {
        if (this.f2421a + j2 > this.f2423c) {
            j2 = this.f2423c - this.f2421a;
        }
        if (j2 < 0) {
            return 0L;
        }
        this.f2421a = (int) (this.f2421a + j2);
        return j2;
    }

    @Override // java.io.InputStream
    public synchronized int available() {
        return this.f2423c - this.f2421a;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.InputStream
    public void mark(int i2) {
        this.f2422b = this.f2421a;
    }

    @Override // java.io.InputStream
    public synchronized void reset() {
        this.f2421a = this.f2422b;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    public synchronized void a(byte[] bArr) {
        this.f2419e.add(bArr);
        notify();
    }

    private void b(byte[] bArr) throws IOException {
        if (this.f2418d) {
            if (bArr == null) {
                throw new IOException("TIMEOUT on wait for next buffer. Expected more buffers, Log file likely truncated.");
            }
            this.f2420f = bArr;
            this.f2421a = 0;
            this.f2423c = bArr.length;
            this.f2422b = 0;
        }
    }

    public synchronized void a(boolean z2) {
        this.f2418d = z2;
        if (z2) {
            return;
        }
        notify();
    }

    public synchronized byte[] a() {
        if (this.f2419e.isEmpty() && this.f2418d) {
            try {
                wait(3000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        byte[] bArr = null;
        if (!this.f2419e.isEmpty()) {
            bArr = (byte[]) this.f2419e.get(0);
            this.f2419e.remove(0);
        } else if (this.f2418d) {
            Logger.getLogger(a.class.getName()).log(Level.WARNING, "TIMEOUT on wait for next buffer. Expected more buffers, did not get it in 3000 ms. Log file likely truncated.");
        }
        return bArr;
    }
}
