package com.efiAnalytics.ui;

/* renamed from: com.efiAnalytics.ui.fe, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fe.class */
class RunnableC1674fe implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1672fc f11666a;

    RunnableC1674fe(C1672fc c1672fc) {
        this.f11666a = c1672fc;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f11666a.requestFocus();
        this.f11666a.toFront();
    }
}
