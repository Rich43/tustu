package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/ax.class */
class ax implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ SelectableTablePanel f10429a;

    ax(SelectableTablePanel selectableTablePanel) {
        this.f10429a = selectableTablePanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10429a.a(actionEvent.getActionCommand());
    }
}
