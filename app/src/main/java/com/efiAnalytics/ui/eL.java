package com.efiAnalytics.ui;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eL.class */
class eL implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f11488a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ boolean f11489b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ eK f11490c;

    eL(eK eKVar, int i2, boolean z2) {
        this.f11490c = eKVar;
        this.f11488a = i2;
        this.f11489b = z2;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f11490c.setEnabledAt(this.f11488a, this.f11489b);
    }
}
