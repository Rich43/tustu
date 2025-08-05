package aY;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;

/* loaded from: TunerStudioMS.jar:aY/d.class */
class d implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0475a f4046a;

    d(C0475a c0475a) {
        this.f4046a = c0475a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JToggleButton) actionEvent.getSource()).isSelected()) {
            this.f4046a.a();
        } else {
            this.f4046a.b();
        }
    }
}
