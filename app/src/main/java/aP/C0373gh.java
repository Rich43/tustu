package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gh.class */
class C0373gh implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3484a;

    C0373gh(C0308dx c0308dx) {
        this.f3484a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13393bw, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
    }
}
