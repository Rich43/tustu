package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;

/* renamed from: br.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/t.class */
class C1256t implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1255s f8528a;

    C1256t(C1255s c1255s) {
        this.f8528a = c1255s;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8528a.a(((JToggleButton) actionEvent.getSource()).isSelected());
    }
}
