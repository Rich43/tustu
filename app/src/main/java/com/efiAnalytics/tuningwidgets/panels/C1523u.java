package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/u.class */
class C1523u implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1516n f10502a;

    C1523u(C1516n c1516n) {
        this.f10502a = c1516n;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            this.f10502a.d();
        } catch (V.a e2) {
            bV.d(e2.getLocalizedMessage(), this.f10502a.f10490i);
        }
    }
}
