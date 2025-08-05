package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

/* renamed from: br.A, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/A.class */
class C1223A implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1255s f8335a;

    C1223A(C1255s c1255s) {
        this.f8335a = c1255s;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8335a.b((JComponent) actionEvent.getSource());
    }
}
