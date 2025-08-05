package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/h.class */
class h implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TableCellCrossHair f9860a;

    h(TableCellCrossHair tableCellCrossHair) {
        this.f9860a = tableCellCrossHair;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws IllegalArgumentException {
        try {
            this.f9860a.setSelectedTable(actionEvent.getActionCommand());
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this.f9860a.f9825c);
        }
    }
}
