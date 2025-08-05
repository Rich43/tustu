package aM;

import com.efiAnalytics.ui.C1616d;
import com.efiAnalytics.ui.bV;
import java.awt.Component;

/* loaded from: TunerStudioMS.jar:aM/b.class */
class b implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f2621a;

    b(a aVar) {
        this.f2621a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f2621a.f2616c != null) {
            this.f2621a.f2616c.dispose();
        }
        this.f2621a.f2616c = new C1616d(this.f2621a.f2620f, this.f2621a.a("TeamViewer Launcher"), this.f2621a.f2617g);
        bV.a(this.f2621a.f2620f, (Component) this.f2621a.f2616c);
        this.f2621a.f2616c.setVisible(true);
    }
}
