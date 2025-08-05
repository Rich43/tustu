package r;

import com.efiAnalytics.apps.ts.dashboard.Z;
import java.io.File;
import javax.swing.SwingUtilities;
import v.C1883c;

/* renamed from: r.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/e.class */
class C1802e extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ File f13432a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1799b f13433b;

    C1802e(C1799b c1799b, File file) {
        this.f13433b = c1799b;
        this.f13432a = file;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Z zA = new C1883c(C1807j.G()).a(this.f13432a.getAbsolutePath());
            if (this.f13433b.f13428h != null) {
                zA.d(this.f13433b.f13428h.c());
            }
            this.f13433b.f13423b.a(zA);
            this.f13433b.f13423b.e(true);
            SwingUtilities.invokeLater(this.f13433b.f13429g);
        } catch (V.a e2) {
            e2.printStackTrace();
        }
    }
}
