package com.efiAnalytics.tuningwidgets.panels;

import G.C0134q;
import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/h.class */
class C1510h implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1509g f10459a;

    C1510h(C1509g c1509g) {
        this.f10459a = c1509g;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f10459a.f10453j.isSelected()) {
            try {
                S.e.a().a(this.f10459a.f10444a.c(), this.f10459a.f10445b);
                S.e.a().a(this.f10459a.f10444a.c(), this.f10459a.f10446c);
            } catch (C0134q e2) {
                bV.d("No Configuration Found: " + this.f10459a.f10444a.c(), this.f10459a.f10453j);
            }
        } else {
            S.e.a().a(this.f10459a.f10444a.c(), this.f10459a.f10445b.a());
            S.e.a().a(this.f10459a.f10444a.c(), this.f10459a.f10446c.a());
        }
        this.f10459a.e();
    }
}
