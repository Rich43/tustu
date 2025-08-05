package com.efiAnalytics.tunerStudio.panels;

/* renamed from: com.efiAnalytics.tunerStudio.panels.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/t.class */
class C1471t extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1466o f10145a;

    C1471t(C1466o c1466o) {
        this.f10145a = c1466o;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f10145a.f10137k.e();
            this.f10145a.f10137k.f10000x.a(false);
        } catch (V.a e2) {
            bH.C.a("Unable to start Logging.", e2, this.f10145a.getParent());
        }
    }
}
