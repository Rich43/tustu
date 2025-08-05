package aP;

import as.InterfaceC0846a;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.io.File;

/* loaded from: TunerStudioMS.jar:aP/cW.class */
class cW implements InterfaceC0846a, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3132a;

    cW(bZ bZVar) {
        this.f3132a = bZVar;
    }

    @Override // as.InterfaceC0846a
    public void a(File file) {
        C0338f.a().a(file);
        this.f3132a.r();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f3132a.f3057F = null;
    }
}
