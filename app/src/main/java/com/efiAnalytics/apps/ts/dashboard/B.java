package com.efiAnalytics.apps.ts.dashboard;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/B.class */
class B implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9265a;

    B(C1425x c1425x) {
        this.f9265a = c1425x;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException, HeadlessException {
        this.f9265a.a(Integer.parseInt(actionEvent.getActionCommand()));
    }
}
