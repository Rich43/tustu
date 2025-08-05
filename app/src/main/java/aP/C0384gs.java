package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gs.class */
class C0384gs implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3495a;

    C0384gs(C0308dx c0308dx) {
        this.f3495a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13385bo, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
    }
}
