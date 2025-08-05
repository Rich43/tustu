package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/t.class */
class C1522t implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1516n f10501a;

    C1522t(C1516n c1516n) {
        this.f10501a = c1516n;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            this.f10501a.e();
        } catch (V.a e2) {
            bV.d(e2.getLocalizedMessage(), this.f10501a.f10490i);
        }
    }
}
