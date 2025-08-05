package com.efiAnalytics.apps.ts.tuningViews;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/W.class */
class W implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ V f9763a;

    W(V v2) {
        this.f9763a = v2;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f9763a.d();
        if (this.f9763a.f9762a.w() == null || this.f9763a.f9762a.w().isEmpty()) {
            this.f9763a.f9762a.f9698L = false;
        } else {
            this.f9763a.f9762a.h();
        }
    }
}
