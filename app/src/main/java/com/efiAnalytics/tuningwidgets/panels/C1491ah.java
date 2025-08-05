package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.ah, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/ah.class */
class C1491ah implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1488ae f10400a;

    C1491ah(C1488ae c1488ae) {
        this.f10400a = c1488ae;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            this.f10400a.e();
        } catch (V.a e2) {
            bV.d(e2.getLocalizedMessage(), this.f10400a.f10395h);
        }
    }
}
