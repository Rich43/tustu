package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/M.class */
class M implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9379a;

    M(C1425x c1425x) {
        this.f9379a = c1425x;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        boolean state = ((JCheckBoxMenuItem) actionEvent.getSource()).getState();
        this.f9379a.j(state);
        C1798a.a().b("dashAntiAliasingOn", state + "");
        if (!this.f9379a.f9593aG || this.f9379a.ad() == null) {
            this.f9379a.f9593aG = false;
        } else {
            this.f9379a.h();
        }
    }
}
