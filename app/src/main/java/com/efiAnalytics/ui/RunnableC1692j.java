package com.efiAnalytics.ui;

/* renamed from: com.efiAnalytics.ui.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/j.class */
class RunnableC1692j implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1691i f11724a;

    RunnableC1692j(C1691i c1691i) {
        this.f11724a = c1691i;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f11724a.f11723d.showPopup();
        this.f11724a.f11723d.requestFocusInWindow();
    }
}
