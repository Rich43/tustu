package com.efiAnalytics.ui;

/* renamed from: com.efiAnalytics.ui.di, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/di.class */
class RunnableC1625di implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1624dh f11422a;

    RunnableC1625di(C1624dh c1624dh) {
        this.f11422a = c1624dh;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f11422a.f11421d.showPopup();
        this.f11422a.f11421d.requestFocusInWindow();
    }
}
