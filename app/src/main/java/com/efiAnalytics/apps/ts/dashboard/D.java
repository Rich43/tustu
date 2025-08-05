package com.efiAnalytics.apps.ts.dashboard;

import java.io.File;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/D.class */
class D implements aK {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9267a;

    D(C1425x c1425x) {
        this.f9267a = c1425x;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aK
    public void a(File file) {
        if (file == null || !file.exists()) {
            this.f9267a.i((String) null);
            this.f9267a.k(false);
        } else {
            this.f9267a.i(file.getAbsolutePath());
            this.f9267a.k(false);
        }
    }
}
