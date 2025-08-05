package aI;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aI/f.class */
class f extends Thread implements h {

    /* renamed from: a, reason: collision with root package name */
    OutputStream f2447a;

    /* renamed from: b, reason: collision with root package name */
    a f2448b;

    /* renamed from: c, reason: collision with root package name */
    boolean f2449c;

    /* renamed from: d, reason: collision with root package name */
    bK.c f2450d;

    /* renamed from: e, reason: collision with root package name */
    IOException f2451e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ e f2452f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    f(e eVar, File file) {
        super("CompessedWriter");
        this.f2452f = eVar;
        this.f2447a = null;
        this.f2448b = null;
        this.f2449c = false;
        this.f2450d = null;
        this.f2451e = null;
        setDaemon(true);
        this.f2447a = eVar.a(file);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            bK.c.a(this.f2450d, this.f2447a);
        } catch (IOException e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            this.f2451e = e2;
        }
    }

    @Override // aI.h
    public void a(byte[] bArr) throws IOException {
        if (this.f2451e != null) {
            throw this.f2451e;
        }
        if (this.f2449c) {
            this.f2448b.a(bArr);
            return;
        }
        this.f2448b = new a(bArr);
        this.f2448b.a(true);
        this.f2450d = new bK.c(this.f2448b);
        start();
    }

    @Override // java.lang.Thread
    public void start() {
        this.f2449c = true;
        super.start();
    }

    @Override // aI.h
    public void a() {
        try {
            if (this.f2448b != null) {
                this.f2448b.a(false);
                for (int i2 = 0; i2 < 50 && this.f2448b.available() > 0; i2++) {
                    try {
                        Thread.sleep(20L);
                    } catch (InterruptedException e2) {
                        Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
            }
            this.f2447a.close();
        } catch (IOException e3) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    @Override // aI.h
    public int b() {
        while (this.f2448b != null && this.f2448b.available() > 0) {
            try {
                sleep(4L);
            } catch (InterruptedException e2) {
                Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        if (this.f2450d != null) {
            return this.f2450d.a();
        }
        return 0;
    }
}
