package bh;

import com.efiAnalytics.tuningwidgets.panels.ay;

/* renamed from: bh.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/p.class */
class C1156p implements ay {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1154n f8127a;

    C1156p(C1154n c1154n) {
        this.f8127a = c1154n;
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.ay
    public void panelSelectionChanged(String str, String str2) {
        this.f8127a.a("selectionTable2", str2);
    }
}
