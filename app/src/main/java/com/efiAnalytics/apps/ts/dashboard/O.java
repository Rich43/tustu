package com.efiAnalytics.apps.ts.dashboard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/O.class */
class O implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    String f9386a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1425x f9387b;

    O(C1425x c1425x, String str) {
        this.f9387b = c1425x;
        this.f9386a = "";
        this.f9386a = str;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Component componentG = this.f9387b.g();
        if (componentG instanceof Gauge) {
            this.f9387b.a((Gauge) componentG, actionEvent.getActionCommand(), this.f9386a);
        }
    }
}
