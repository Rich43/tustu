package br;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/* loaded from: TunerStudioMS.jar:br/X.class */
class X implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ P f8398a;

    X(P p2) {
        this.f8398a = p2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        ((CardLayout) this.f8398a.f8377d.getLayout()).show(this.f8398a.f8377d, ((JRadioButton) actionEvent.getSource()).getActionCommand());
    }
}
