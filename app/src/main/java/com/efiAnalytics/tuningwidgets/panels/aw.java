package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aw.class */
class aw implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ SelectableTablePanel f10428a;

    aw(SelectableTablePanel selectableTablePanel) {
        this.f10428a = selectableTablePanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JButton jButton = (JButton) actionEvent.getSource();
        this.f10428a.a(jButton.getX(), jButton.getY() + jButton.getHeight());
    }
}
