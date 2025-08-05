package aP;

import java.awt.Frame;
import r.C1798a;
import s.C1818g;

/* renamed from: aP.dw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dw.class */
class C0307dw extends Thread {

    /* renamed from: a, reason: collision with root package name */
    Frame f3258a;

    /* renamed from: b, reason: collision with root package name */
    boolean f3259b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0293dh f3260c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0307dw(C0293dh c0293dh, boolean z2, Frame frame) {
        super("UpdateThread");
        this.f3260c = c0293dh;
        this.f3258a = null;
        this.f3259b = false;
        setDaemon(true);
        this.f3259b = z2;
        this.f3258a = frame;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            if (!this.f3260c.f3233c) {
                this.f3260c.f3233c = true;
                C0404hl.a().a(C1818g.b("Checking for updates"));
                if (!this.f3259b) {
                    Thread.currentThread();
                    Thread.sleep(3000L);
                }
                boolean zL = this.f3260c.l();
                if (this.f3260c.f3232i || zL || !this.f3259b) {
                    C0404hl.a().a("Update Check Completed");
                } else {
                    this.f3260c.m();
                    C0404hl.a().a(C1818g.b("No updates available."));
                }
                C1798a.a().b(C1798a.f13291y, "false");
                C1798a.a().e();
                h.i.g();
            }
        } catch (V.a e2) {
            if (this.f3259b) {
                com.efiAnalytics.ui.bV.d(e2.getMessage(), this.f3258a);
                C0404hl.a().a("");
            }
        } catch (Exception e3) {
            com.efiAnalytics.ui.bV.d("Error occured in Autoupdate " + e3.getMessage(), this.f3258a);
        } finally {
            this.f3260c.f3233c = false;
        }
    }
}
