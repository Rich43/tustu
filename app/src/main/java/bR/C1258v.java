package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/* renamed from: br.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/v.class */
class C1258v implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1255s f8530a;

    C1258v(C1255s c1255s) {
        this.f8530a = c1255s;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBox) actionEvent.getSource()).isSelected()) {
            this.f8530a.f8526o = "15";
            this.f8530a.a(this.f8530a.f8526o);
        } else {
            this.f8530a.f8526o = "Manually";
            this.f8530a.a(this.f8530a.f8526o);
        }
    }
}
