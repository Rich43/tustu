package ao;

import java.awt.Frame;

/* loaded from: TunerStudioMS.jar:ao/dX.class */
class dX extends Thread {

    /* renamed from: a, reason: collision with root package name */
    Frame f5545a;

    /* renamed from: b, reason: collision with root package name */
    boolean f5546b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ bP f5547c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    dX(bP bPVar, boolean z2, Frame frame) {
        super("UpdateThread");
        this.f5547c = bPVar;
        this.f5545a = null;
        this.f5546b = false;
        setDaemon(true);
        this.f5546b = z2;
        this.f5545a = frame;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            try {
                if (!bP.f5364r) {
                    bP.f5364r = true;
                    if (!this.f5546b) {
                        Thread.sleep(2000L);
                    }
                    boolean zB = this.f5547c.b();
                    if (!this.f5547c.f5375I && !zB && this.f5546b) {
                        this.f5547c.g();
                    }
                    h.i.c(h.i.f12278y, "false");
                    h.i.g();
                }
                bP.f5364r = false;
            } catch (Exception e2) {
                com.efiAnalytics.ui.bV.d("Error occured in Autoupdate " + e2.getMessage(), this.f5545a);
                e2.printStackTrace();
                bP.f5364r = false;
            }
        } catch (Throwable th) {
            bP.f5364r = false;
            throw th;
        }
    }
}
