package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gp.class */
class C0381gp implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3492a;

    C0381gp(C0308dx c0308dx) {
        this.f3492a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13402bF, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
    }
}
