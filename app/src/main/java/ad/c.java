package aD;

import G.T;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aD/c.class */
class c extends Thread {

    /* renamed from: a, reason: collision with root package name */
    String[] f2324a;

    /* renamed from: b, reason: collision with root package name */
    List f2325b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ a f2326c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c(a aVar) {
        super("PortChangeMonitor");
        this.f2326c = aVar;
        this.f2324a = null;
        this.f2325b = new ArrayList();
    }

    public void a(a aVar) {
        this.f2325b.add(aVar);
    }

    public void a() {
        for (a aVar : this.f2325b) {
            aVar.a(aVar.l());
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Thread.sleep(2500L);
            } catch (InterruptedException e2) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (A.d.a().b() && (T.a().c() == null || !T.a().c().R())) {
                String[] strArrB = this.f2326c.f2314t.b();
                if (this.f2324a != null) {
                    if (this.f2324a.length != strArrB.length) {
                        a();
                    } else {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= strArrB.length) {
                                break;
                            }
                            if (!strArrB[i2].equals(this.f2324a[i2])) {
                                a();
                                break;
                            }
                            i2++;
                        }
                    }
                }
                this.f2324a = strArrB;
            }
        }
    }
}
