package com.efiAnalytics.apps.ts.dashboard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/P.class */
class P implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    String f9388a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1425x f9389b;

    P(C1425x c1425x, String str) {
        this.f9389b = c1425x;
        this.f9388a = "";
        this.f9388a = str;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Component componentG = this.f9389b.g();
        if (componentG instanceof Indicator) {
            this.f9389b.a((Indicator) componentG, actionEvent.getActionCommand(), this.f9388a);
        }
    }
}
