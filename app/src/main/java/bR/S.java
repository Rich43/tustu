package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:br/S.class */
class S implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ P f8393a;

    S(P p2) {
        this.f8393a = p2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBox) actionEvent.getSource()).isSelected()) {
            this.f8393a.f8390q = "15";
            this.f8393a.a(this.f8393a.f8390q);
        } else {
            this.f8393a.f8390q = "Manually";
            this.f8393a.a(this.f8393a.f8390q);
        }
    }
}
