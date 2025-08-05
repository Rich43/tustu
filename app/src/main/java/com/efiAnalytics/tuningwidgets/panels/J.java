package com.efiAnalytics.tuningwidgets.panels;

import W.C0184j;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/J.class */
class J implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0184j f10258a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ Component f10259b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ I f10260c;

    J(I i2, C0184j c0184j, Component component) {
        this.f10260c = i2;
        this.f10258a = c0184j;
        this.f10259b = component;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBoxMenuItem) actionEvent.getSource()).isSelected()) {
            this.f10260c.a(this.f10258a, this.f10258a.s());
        } else {
            this.f10260c.a(this.f10258a);
        }
        this.f10259b.repaint();
    }
}
