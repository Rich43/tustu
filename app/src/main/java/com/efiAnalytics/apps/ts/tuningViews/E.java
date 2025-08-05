package com.efiAnalytics.apps.ts.tuningViews;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/E.class */
class E implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f9679a;

    E(D d2) {
        this.f9679a = d2;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f9679a.f9678a.f9676a.w() == null || this.f9679a.f9678a.f9676a.w().isEmpty()) {
            this.f9679a.f9678a.f9676a.b(false);
        } else {
            this.f9679a.f9678a.f9676a.h();
        }
    }
}
