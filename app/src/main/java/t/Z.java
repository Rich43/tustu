package t;

import java.io.File;

/* loaded from: TunerStudioMS.jar:t/Z.class */
class Z implements com.efiAnalytics.apps.ts.dashboard.aK {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13711a;

    Z(C1875w c1875w) {
        this.f13711a = c1875w;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aK
    public void a(File file) {
        if (file != null) {
            this.f13711a.f13910a.f(file.getAbsolutePath());
        } else {
            this.f13711a.f13910a.f((String) null);
        }
    }
}
