package com.efiAnalytics.tunerStudio.panels;

import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import s.C1818g;

/* renamed from: com.efiAnalytics.tunerStudio.panels.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/f.class */
class C1457f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1452a f10109a;

    C1457f(C1452a c1452a) {
        this.f10109a = c1452a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strB = bV.b((Component) actionEvent.getSource(), C1818g.b("Find CAN Device definition file"), new String[]{"ini", "ecu"}, "", "");
        if (strB != null) {
            this.f10109a.b(strB);
            this.f10109a.f10052g.validate();
        }
    }
}
