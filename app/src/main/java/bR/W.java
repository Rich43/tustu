package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:br/W.class */
class W implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ P f8397a;

    W(P p2) {
        this.f8397a = p2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8397a.b((JComponent) actionEvent.getSource());
    }
}
