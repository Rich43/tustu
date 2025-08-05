package m;

import com.efiAnalytics.ui.bV;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:m/b.class */
class b implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ List f12908a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1760a f12909b;

    b(C1760a c1760a, List list) {
        this.f12909b = c1760a;
        this.f12908a = list;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            d.a(bV.c(), this.f12908a, this.f12909b.f12906a);
        } catch (IOException e2) {
            Logger.getLogger(C1760a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
