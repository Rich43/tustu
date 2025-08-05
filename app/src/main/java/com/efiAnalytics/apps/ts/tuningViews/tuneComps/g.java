package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/g.class */
class g implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TableCellCrossHair f9859a;

    g(TableCellCrossHair tableCellCrossHair) {
        this.f9859a = tableCellCrossHair;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9859a.showSelectPopup(this.f9859a.f9826d.getX(), this.f9859a.f9826d.getY() + this.f9859a.f9826d.getHeight());
    }
}
