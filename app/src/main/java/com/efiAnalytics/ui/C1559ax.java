package com.efiAnalytics.ui;

/* renamed from: com.efiAnalytics.ui.ax, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ax.class */
class C1559ax implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10854a;

    C1559ax(BinTableView binTableView) {
        this.f10854a = binTableView;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f10854a.b("cellColorMin", "" + this.f10854a.f10674w.e());
        this.f10854a.b("cellColorMax", "" + this.f10854a.f10675x.e());
    }
}
