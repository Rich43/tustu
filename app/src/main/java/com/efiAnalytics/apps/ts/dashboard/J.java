package com.efiAnalytics.apps.ts.dashboard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/J.class */
class J implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Component f9375a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1425x f9376b;

    J(C1425x c1425x, Component component) {
        this.f9376b = c1425x;
        this.f9375a = component;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9376b.f9653ba = ((JCheckBoxMenuItem) actionEvent.getSource()).getState();
        if (this.f9376b.f9653ba) {
            C1403b c1403b = new C1403b();
            c1403b.a((int) this.f9376b.H(), (int) this.f9376b.I());
            if (c1403b.a(this.f9375a) != null) {
                this.f9376b.b(r0.b());
                this.f9376b.a(r0.a());
            }
        }
        if (this.f9376b.getParent() != null) {
            this.f9376b.getParent().doLayout();
        }
        this.f9376b.i(this.f9376b.J());
        this.f9376b.doLayout();
    }
}
