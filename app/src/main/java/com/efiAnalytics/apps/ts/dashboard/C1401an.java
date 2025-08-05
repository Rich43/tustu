package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.C1605cp;
import com.efiAnalytics.ui.bV;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.an, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/an.class */
class C1401an implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9475a;

    C1401an(C1391ad c1391ad) {
        this.f9475a = c1391ad;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        Color colorA = bV.a((Component) actionEvent.getSource(), "Choose Critical Limits Color", this.f9475a.a().getCriticalColor());
        if (colorA != null) {
            ((C1605cp) actionEvent.getSource()).a(colorA);
            this.f9475a.a().setCriticalColor(colorA);
            this.f9475a.a().repaint();
        }
    }
}
