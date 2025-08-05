package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/fN.class */
class fN implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3358a;

    fN(C0308dx c0308dx) {
        this.f3358a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13373bc, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
    }
}
