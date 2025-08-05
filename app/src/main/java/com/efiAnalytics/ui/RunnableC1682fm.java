package com.efiAnalytics.ui;

/* renamed from: com.efiAnalytics.ui.fm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fm.class */
class RunnableC1682fm implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1681fl f11681a;

    RunnableC1682fm(C1681fl c1681fl) {
        this.f11681a = c1681fl;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f11681a.getParent().invalidate();
        this.f11681a.getParent().validate();
        this.f11681a.doLayout();
    }
}
