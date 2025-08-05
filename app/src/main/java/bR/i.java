package bR;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bR/i.class */
class i extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f7514a = 300;

    /* renamed from: b, reason: collision with root package name */
    long f7515b = 0;

    /* renamed from: c, reason: collision with root package name */
    final Object f7516c = new Object();

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ h f7517d;

    i(h hVar) {
        this.f7517d = hVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            synchronized (this.f7516c) {
                try {
                    this.f7516c.wait(this.f7514a);
                } catch (InterruptedException e2) {
                    Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                if (System.currentTimeMillis() > this.f7515b) {
                    ArrayList<j> arrayList = new ArrayList();
                    synchronized (this.f7517d.f7512b) {
                        arrayList.addAll(this.f7517d.f7512b);
                        this.f7517d.f7512b.clear();
                    }
                    for (j jVar : arrayList) {
                        if (jVar.b() == null) {
                            this.f7517d.b(jVar.a());
                        } else {
                            this.f7517d.b(jVar.a(), jVar.b());
                        }
                    }
                }
            }
        }
    }

    public void a() {
        this.f7515b = System.currentTimeMillis() + this.f7514a;
        synchronized (this.f7516c) {
            this.f7516c.notify();
        }
    }

    public void a(j jVar) {
        a();
        this.f7517d.f7512b.add(jVar);
    }
}
