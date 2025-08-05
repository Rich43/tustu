package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gm.class */
class C0378gm implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3489a;

    C0378gm(C0308dx c0308dx) {
        this.f3489a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13400bD, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
    }
}
