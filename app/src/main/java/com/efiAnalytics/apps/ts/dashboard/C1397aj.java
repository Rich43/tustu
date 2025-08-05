package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.C1605cp;
import com.efiAnalytics.ui.bV;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.aj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aj.class */
class C1397aj implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9471a;

    C1397aj(C1391ad c1391ad) {
        this.f9471a = c1391ad;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        Color colorA = bV.a((Component) actionEvent.getSource(), "Choose Border Color", this.f9471a.a().getTrimColor());
        if (colorA != null) {
            ((C1605cp) actionEvent.getSource()).a(colorA);
            this.f9471a.a().setBackColor(colorA);
            this.f9471a.a().repaint();
        }
    }
}
