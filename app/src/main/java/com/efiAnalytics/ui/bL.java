package com.efiAnalytics.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bL.class */
class bL implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f10886a;

    bL(C1582bt c1582bt) {
        this.f10886a = c1582bt;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JMenuItem jMenuItem = (JMenuItem) actionEvent.getSource();
        Point location = jMenuItem.getLocation();
        location.f12371y += jMenuItem.getHeight();
        this.f10886a.a(location);
    }
}
