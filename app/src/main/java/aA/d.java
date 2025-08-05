package aA;

import az.C0940a;
import com.efiAnalytics.ui.bV;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aA/d.class */
class d extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0940a f2253a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ a f2254b;

    d(a aVar, C0940a c0940a) {
        this.f2254b = aVar;
        this.f2253a = c0940a;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(200L);
            D.c cVarA = h.a().a(this.f2254b.f2243b);
            if (cVarA.b() != null) {
                this.f2254b.f2243b = cVarA.b();
            }
            if (cVarA.a() != 0) {
                bV.c(D.c.b(cVarA.a()), this.f2254b.f2242a);
            } else if (this.f2254b.f2242a != null) {
                this.f2254b.f2242a.dispose();
            }
        } catch (InterruptedException e2) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } finally {
            this.f2253a.dispose();
        }
    }
}
