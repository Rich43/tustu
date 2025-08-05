package ao;

import az.C0940a;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ao/dS.class */
class dS extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f5534a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ String f5535b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ String f5536c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ String f5537d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C0940a f5538e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ dQ f5539f;

    dS(dQ dQVar, String str, String str2, String str3, String str4, C0940a c0940a) {
        this.f5539f = dQVar;
        this.f5534a = str;
        this.f5535b = str2;
        this.f5536c = str3;
        this.f5537d = str4;
        this.f5538e = c0940a;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e2) {
            Logger.getLogger(bP.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        try {
            h.i.d("firstName", this.f5534a);
            h.i.d("lastName", this.f5535b);
            h.i.d("registrationKeyV2", this.f5536c);
            h.i.d("userEmail", this.f5537d);
            h.i.c("quadraticInterpolation", "false");
            h.i.c("valid", "false");
            if (this.f5539f.f5532a.B()) {
                h.i.h();
                h.i.g();
                this.f5539f.f5532a.E();
            }
        } catch (V.a e3) {
            bH.C.a("Failed to save Registration Information.", e3, C0645bi.a().b());
        } finally {
            this.f5538e.setVisible(false);
            this.f5538e.dispose();
        }
    }
}
