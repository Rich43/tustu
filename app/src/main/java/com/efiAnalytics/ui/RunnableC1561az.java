package com.efiAnalytics.ui;

/* renamed from: com.efiAnalytics.ui.az, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/az.class */
class RunnableC1561az implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f10856a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ int f10857b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ BinTableView f10858c;

    RunnableC1561az(BinTableView binTableView, int i2, int i3) {
        this.f10858c = binTableView;
        this.f10856a = i2;
        this.f10857b = i3;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f10858c.changeSelection(this.f10856a, this.f10857b, false, false);
    }
}
