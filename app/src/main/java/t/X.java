package t;

import java.io.File;

/* loaded from: TunerStudioMS.jar:t/X.class */
class X implements com.efiAnalytics.apps.ts.dashboard.aK {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13709a;

    X(C1875w c1875w) {
        this.f13709a = c1875w;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aK
    public void a(File file) {
        if (file != null) {
            this.f13709a.f13910a.d(file.getAbsolutePath());
        } else {
            this.f13709a.f13910a.d((String) null);
        }
    }
}
