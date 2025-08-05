package W;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:W/as.class */
public class as extends OutputStream {

    /* renamed from: a, reason: collision with root package name */
    OutputStream f2101a;

    /* renamed from: b, reason: collision with root package name */
    final List f2102b = Collections.synchronizedList(new LinkedList());

    /* renamed from: c, reason: collision with root package name */
    int f2103c = 500;

    /* renamed from: d, reason: collision with root package name */
    IOException f2104d = null;

    /* renamed from: e, reason: collision with root package name */
    au f2105e = null;

    public as(OutputStream outputStream) {
        this.f2101a = outputStream;
    }

    private void b() throws IOException {
        if (this.f2105e == null) {
            this.f2105e = new au(this);
            this.f2105e.start();
        }
        int i2 = 0;
        while (this.f2102b.size() >= this.f2103c) {
            try {
                Thread.sleep(100L);
                bH.C.c("Queue full, blocking to shrink.");
            } catch (InterruptedException e2) {
                Logger.getLogger(as.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            int i3 = i2;
            i2++;
            if (i3 > 5) {
                throw new IOException("Slow Write Speed causing queue over flow. ");
            }
        }
    }

    @Override // java.io.OutputStream
    public synchronized void write(int i2) throws IOException {
        try {
            b();
            this.f2102b.add(new at(this, i2));
            this.f2105e.a();
        } catch (IOException e2) {
            Logger.getLogger(as.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new IOException("Slow I/O caused full queue and Iterruption adding write.");
        }
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr) throws IOException {
        try {
            b();
            synchronized (this.f2102b) {
                this.f2102b.add(new at(this, bArr));
            }
        } catch (IOException e2) {
            Logger.getLogger(as.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new IOException("Slow I/O caused full queue and Iterruption adding write.");
        }
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
        try {
            b();
            if (bArr.length != i3) {
                byte[] bArr2 = new byte[i3];
                System.arraycopy(bArr, i2, bArr2, 0, i3);
                bArr = bArr2;
            }
            this.f2102b.add(new at(this, bArr));
        } catch (IOException e2) {
            Logger.getLogger(as.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new IOException("Slow I/O caused full queue and Iterruption adding write.");
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public synchronized void flush() throws IOException {
        b();
        this.f2105e.a();
    }

    public synchronized void a() throws IOException {
        int i2 = 0;
        while (this.f2102b.size() > 0) {
            bH.C.c("QueuingOutputStream: Flushing Queue, size=" + this.f2102b.size());
            this.f2105e.a();
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e2) {
                Logger.getLogger(as.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            int i3 = i2;
            i2++;
            if (i3 > 100) {
                throw new IOException("Unable to flush OutputStream queu in a reasonable amount of time.");
            }
        }
        this.f2101a.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            a();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.f2105e.a(false);
        this.f2101a.close();
    }
}
