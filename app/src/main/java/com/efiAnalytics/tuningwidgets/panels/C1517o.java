package com.efiAnalytics.tuningwidgets.panels;

import javax.swing.SwingUtilities;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/o.class */
class C1517o implements G.aN {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1516n f10495a;

    C1517o(C1516n c1516n) {
        this.f10495a = c1516n;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        SwingUtilities.invokeLater(new RunnableC1518p(this));
    }
}
