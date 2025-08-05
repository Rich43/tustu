package t;

import java.io.File;

/* renamed from: t.aa, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/aa.class */
class C1828aa implements com.efiAnalytics.apps.ts.dashboard.aK {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13776a;

    C1828aa(C1875w c1875w) {
        this.f13776a = c1875w;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aK
    public void a(File file) {
        if (file != null) {
            this.f13776a.f13910a.g(file.getAbsolutePath());
        } else {
            this.f13776a.f13910a.g((String) null);
        }
    }
}
