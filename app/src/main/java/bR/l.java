package bR;

import G.C0116cv;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bR/l.class */
class l extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f7524a = 300;

    /* renamed from: b, reason: collision with root package name */
    long f7525b = 0;

    /* renamed from: c, reason: collision with root package name */
    final Object f7526c = new Object();

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ k f7527d;

    l(k kVar) {
        this.f7527d = kVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            synchronized (this.f7526c) {
                try {
                    this.f7526c.wait(this.f7524a);
                } catch (InterruptedException e2) {
                    Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                if (System.currentTimeMillis() > this.f7525b) {
                    ArrayList<m> arrayList = new ArrayList();
                    synchronized (this.f7527d.f7522b) {
                        arrayList.addAll(this.f7527d.f7522b);
                        this.f7527d.f7522b.clear();
                    }
                    for (m mVar : arrayList) {
                        if (mVar.e()) {
                            C0116cv.a(mVar.a(), mVar.b());
                        } else {
                            C0116cv.a(mVar.a(), mVar.b(), mVar.c(), mVar.d());
                        }
                    }
                }
            }
        }
    }

    public void a() {
        this.f7525b = System.currentTimeMillis() + this.f7524a;
        synchronized (this.f7526c) {
            this.f7526c.notify();
        }
    }

    public void a(m mVar) {
        a();
        this.f7527d.f7522b.add(mVar);
    }
}
