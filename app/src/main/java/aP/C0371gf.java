package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gf.class */
class C0371gf implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3482a;

    C0371gf(C0308dx c0308dx) {
        this.f3482a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13391bu, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
    }
}
