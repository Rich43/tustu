package com.efiAnalytics.tunerStudio.panels;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/I.class */
class I implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ H f9944a;

    I(H h2) {
        this.f9944a = h2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Component parent = (Component) actionEvent.getSource();
        while (true) {
            Component component = parent;
            if (component == null) {
                return;
            }
            if (component instanceof JDialog) {
                ((JDialog) component).dispose();
            }
            parent = component.getParent();
        }
    }
}
