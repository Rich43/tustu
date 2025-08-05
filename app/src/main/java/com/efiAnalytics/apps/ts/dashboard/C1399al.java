package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.C1605cp;
import com.efiAnalytics.ui.bV;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.al, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/al.class */
class C1399al implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9473a;

    C1399al(C1391ad c1391ad) {
        this.f9473a = c1391ad;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        Color colorA = bV.a((Component) actionEvent.getSource(), "Choose Needle Color", this.f9473a.a().getNeedleColor());
        if (colorA != null) {
            ((C1605cp) actionEvent.getSource()).a(colorA);
            this.f9473a.a().setNeedleColor(colorA);
            this.f9473a.a().repaint();
        }
    }
}
