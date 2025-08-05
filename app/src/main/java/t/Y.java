package t;

import java.io.File;

/* loaded from: TunerStudioMS.jar:t/Y.class */
class Y implements com.efiAnalytics.apps.ts.dashboard.aK {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13710a;

    Y(C1875w c1875w) {
        this.f13710a = c1875w;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aK
    public void a(File file) {
        if (file != null) {
            this.f13710a.f13910a.e(file.getAbsolutePath());
        } else {
            this.f13710a.f13910a.e((String) null);
        }
    }
}
