package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.ai, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/ai.class */
class C1492ai implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1488ae f10401a;

    C1492ai(C1488ae c1488ae) {
        this.f10401a = c1488ae;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            this.f10401a.d();
        } catch (V.a e2) {
            bV.d(e2.getLocalizedMessage(), this.f10401a.f10395h);
        }
    }
}
