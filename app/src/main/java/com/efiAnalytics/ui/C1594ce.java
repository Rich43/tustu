package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/* renamed from: com.efiAnalytics.ui.ce, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ce.class */
final class C1594ce implements ActionListener {
    C1594ce() {
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
                return;
            }
            parent = component.getParent();
        }
    }
}
