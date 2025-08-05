package aI;

import bH.C;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aI/g.class */
class g extends Thread {

    /* renamed from: a, reason: collision with root package name */
    h f2453a;

    /* renamed from: b, reason: collision with root package name */
    int f2454b;

    /* renamed from: c, reason: collision with root package name */
    int f2455c;

    /* renamed from: d, reason: collision with root package name */
    long f2456d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ e f2457e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    g(e eVar, h hVar, int i2, int i3, long j2) {
        super("ratioEntry");
        this.f2457e = eVar;
        setDaemon(true);
        this.f2453a = hVar;
        this.f2454b = i2;
        this.f2455c = i3;
        this.f2456d = j2;
    }

    void a() {
        try {
            int iB = this.f2453a.b();
            this.f2457e.f2443c += this.f2455c;
            int i2 = iB - this.f2457e.f2444d;
            float f2 = iB / this.f2457e.f2443c;
            float f3 = i2 / this.f2455c;
            if (this.f2457e.f2446f) {
                C.c("Read Dir Block: " + this.f2454b + ", compressed size:" + this.f2455c + "bytes, uncompressed size " + i2 + "bytes, Compression Ratio: " + f3 + ", Overall Compression Ratio:" + f2);
            } else {
                C.c("Read Dir Block: " + this.f2454b + ", data read: " + i2 + "bytes");
            }
            if (this.f2457e.f2442b != null) {
                this.f2457e.f2442b.a(iB, this.f2456d);
            }
            this.f2457e.f2444d = iB;
        } catch (IOException e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(20L);
        } catch (InterruptedException e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        a();
    }
}
