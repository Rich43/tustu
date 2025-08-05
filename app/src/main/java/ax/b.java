package aX;

import aW.q;
import com.efiAnalytics.ui.bV;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aX/b.class */
public class b implements q {

    /* renamed from: a, reason: collision with root package name */
    a f4000a = null;

    @Override // aW.q
    public void a(aW.a aVar, String str) {
        if (!aC.b.a()) {
            bV.d(C1818g.b("Bluetooth is not enabled on this computer."), aVar);
            return;
        }
        this.f4000a = new a();
        this.f4000a.a(aVar);
        this.f4000a.a(bV.b(aVar));
    }
}
