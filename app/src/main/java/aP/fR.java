package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/fR.class */
class fR implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3362a;

    fR(C0308dx c0308dx) {
        this.f3362a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13412bP, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
    }
}
