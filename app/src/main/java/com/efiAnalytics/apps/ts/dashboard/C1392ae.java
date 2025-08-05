package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.ae, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/ae.class */
class C1392ae implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9466a;

    C1392ae(C1391ad c1391ad) {
        this.f9466a = c1391ad;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9466a.f9463a.setOutputChannel((String) ((JComboBox) actionEvent.getSource()).getSelectedItem());
        try {
            this.f9466a.f9463a.subscribeToOutput();
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this.f9466a.f9463a);
        }
    }
}
