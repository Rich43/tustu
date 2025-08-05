package ao;

import W.C0188n;
import W.C0189o;
import java.io.IOException;

/* renamed from: ao.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/g.class */
class C0770g extends Thread {

    /* renamed from: a, reason: collision with root package name */
    C0189o f5890a;

    /* renamed from: b, reason: collision with root package name */
    C0188n f5891b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0636b f5892c;

    C0770g(C0636b c0636b, C0189o c0189o, C0188n c0188n) {
        this.f5892c = c0636b;
        this.f5890a = c0189o;
        this.f5891b = c0188n;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            try {
                this.f5890a.a(this.f5891b);
                this.f5890a.b(this.f5891b);
                try {
                    this.f5890a.a();
                } catch (Exception e2) {
                }
            } catch (Throwable th) {
                try {
                    this.f5890a.a();
                } catch (Exception e3) {
                }
                throw th;
            }
        } catch (IOException e4) {
            com.efiAnalytics.ui.bV.d("Error Saving Log File:\n" + e4.getMessage(), C0645bi.a().b());
            e4.printStackTrace();
            try {
                this.f5890a.a();
            } catch (Exception e5) {
            }
        }
        bH.C.c("Wrote to file. " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
    }
}
