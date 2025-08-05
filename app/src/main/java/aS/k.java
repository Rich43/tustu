package aS;

import G.R;
import aP.cZ;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import java.util.logging.Level;
import java.util.logging.Logger;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aS/k.class */
class k extends Thread {

    /* renamed from: d, reason: collision with root package name */
    private boolean f3925d;

    /* renamed from: a, reason: collision with root package name */
    int f3926a;

    /* renamed from: e, reason: collision with root package name */
    private C1425x f3927e;

    /* renamed from: b, reason: collision with root package name */
    R f3928b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ g f3929c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    k(g gVar, R r2) {
        super("Set PC Message");
        this.f3929c = gVar;
        this.f3925d = true;
        this.f3926a = 1000;
        this.f3927e = null;
        this.f3928b = null;
        this.f3928b = r2;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean z2 = false;
        while (!z2) {
            try {
                Thread.sleep(this.f3926a);
            } catch (InterruptedException e2) {
                Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            z2 = this.f3928b.C() == null || !this.f3928b.C().x();
            if (z2 && a()) {
                this.f3927e = cZ.a().b();
                this.f3927e.l(C1818g.b("Settings Changed that Require a Power Cycle to Take Effect."));
            }
        }
    }

    public boolean a() {
        return this.f3925d;
    }

    public void a(boolean z2) {
        this.f3925d = z2;
    }

    public C1425x b() {
        return this.f3927e;
    }
}
