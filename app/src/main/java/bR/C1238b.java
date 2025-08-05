package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* renamed from: br.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/b.class */
class C1238b implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1237a f8442a;

    C1238b(C1237a c1237a) {
        this.f8442a = c1237a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8442a.a((JButton) actionEvent.getSource());
    }
}
