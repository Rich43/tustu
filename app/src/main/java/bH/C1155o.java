package bh;

import com.efiAnalytics.tuningwidgets.panels.ay;

/* renamed from: bh.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/o.class */
class C1155o implements ay {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1154n f8126a;

    C1155o(C1154n c1154n) {
        this.f8126a = c1154n;
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.ay
    public void panelSelectionChanged(String str, String str2) {
        this.f8126a.a("selectionTable1", str2);
    }
}
