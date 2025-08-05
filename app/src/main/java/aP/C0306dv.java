package aP;

import az.C0940a;
import java.awt.Component;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;

/* renamed from: aP.dv, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dv.class */
class C0306dv extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f3250a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ String f3251b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ String f3252c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ String f3253d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ String f3254e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ String f3255f;

    /* renamed from: g, reason: collision with root package name */
    final /* synthetic */ C0940a f3256g;

    /* renamed from: h, reason: collision with root package name */
    final /* synthetic */ C0305du f3257h;

    C0306dv(C0305du c0305du, String str, String str2, String str3, String str4, String str5, String str6, C0940a c0940a) {
        this.f3257h = c0305du;
        this.f3250a = str;
        this.f3251b = str2;
        this.f3252c = str3;
        this.f3253d = str4;
        this.f3254e = str5;
        this.f3255f = str6;
        this.f3256g = c0940a;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        try {
            try {
            } catch (V.a e3) {
                bH.C.a("Failed to save Registration Information.", e3, cZ.a().c());
                return;
            } finally {
                this.f3256g.setVisible(false);
                this.f3256g.dispose();
            }
        } catch (V.a e4) {
        }
        if (this.f3257h.f3249a.a(this.f3250a, this.f3251b, this.f3252c, this.f3253d, this.f3254e)) {
            com.efiAnalytics.ui.bV.d("The registration information provided is not valid.\nPlease obtain a valid registion. ", this.f3257h.f3249a.rootPane);
            com.efiAnalytics.ui.aN.a("https://www.efianalytics.com/register/register.jsp?appName=" + C1798a.f13268b + bH.W.b(this.f3254e, "(Beta)", ""));
            return;
        }
        C1798a.a().b(C1798a.f13285s, "");
        String strC = C1798a.a().c(C1798a.cF, (String) null);
        C1798a.a().d(C1798a.cC, this.f3250a);
        C1798a.a().d(C1798a.cD, this.f3251b);
        C1798a.a().d(C1798a.cF, this.f3252c);
        C1798a.a().d(C1798a.cE, this.f3253d);
        C1798a.a().d(C1798a.f13280n, this.f3254e);
        if (this.f3255f != null) {
            C1798a.a().d(C1798a.f13281o, this.f3255f);
        }
        C1798a.a().b(C1798a.cL, "false");
        if (strC != null && !strC.isEmpty()) {
            C1798a.a().d(C1798a.cI, strC);
        }
        C1798a.a().i();
        if (this.f3257h.f3249a.u() && com.efiAnalytics.ui.bV.a("All " + C1798a.f13268b + " " + this.f3254e + " features will be enabled after restarting " + C1798a.f13268b + ".\n\nWould you like to restart Now?", (Component) cZ.a().c(), true)) {
            C0338f.a().d((Window) cZ.a().c());
        }
    }
}
