package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gn.class */
class C0379gn implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3490a;

    C0379gn(C0308dx c0308dx) {
        this.f3490a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13398bB, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
    }
}
