package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gl.class */
class C0377gl implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3488a;

    C0377gl(C0308dx c0308dx) {
        this.f3488a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13396bz, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
    }
}
