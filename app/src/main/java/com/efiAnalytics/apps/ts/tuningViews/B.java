package com.efiAnalytics.apps.ts.tuningViews;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/B.class */
class B implements InterfaceC1440m {

    /* renamed from: a, reason: collision with root package name */
    J f9671a;

    /* renamed from: b, reason: collision with root package name */
    String f9672b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ z f9673c;

    B(z zVar, J j2, String str) {
        this.f9673c = zVar;
        this.f9671a = j2;
        this.f9672b = str;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.InterfaceC1440m
    public boolean a(String str, String str2) {
        if (str2 == null || str2.trim().isEmpty()) {
            return false;
        }
        int iF = this.f9673c.f9882a.f(this.f9672b);
        if (iF >= 0) {
            this.f9673c.f9882a.setTitleAt(iF, str2);
            return true;
        }
        bH.C.c("Could not find tab titled " + this.f9672b + " to rename to: " + str2);
        return false;
    }
}
