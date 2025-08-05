package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;

/* loaded from: TunerStudioMS.jar:br/Q.class */
class Q implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ P f8391a;

    Q(P p2) {
        this.f8391a = p2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8391a.a(((JToggleButton) actionEvent.getSource()).isSelected());
    }
}
