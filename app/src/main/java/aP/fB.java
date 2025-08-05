package aP;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:aP/fB.class */
class fB implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3346a;

    fB(C0308dx c0308dx) {
        this.f3346a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        ((JCheckBoxMenuItem) actionEvent.getSource()).setSelected(true);
        this.f3346a.f3264c.a((Frame) this.f3346a.f3270h, actionEvent.getActionCommand());
    }
}
