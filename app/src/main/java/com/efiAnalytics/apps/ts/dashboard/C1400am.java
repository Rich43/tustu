package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.C1605cp;
import com.efiAnalytics.ui.bV;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.am, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/am.class */
class C1400am implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9474a;

    C1400am(C1391ad c1391ad) {
        this.f9474a = c1391ad;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        Color colorA = bV.a((Component) actionEvent.getSource(), "Choose Warning Color", this.f9474a.a().getWarnColor());
        if (colorA != null) {
            ((C1605cp) actionEvent.getSource()).a(colorA);
            this.f9474a.a().setWarnColor(colorA);
            this.f9474a.a().repaint();
        }
    }
}
