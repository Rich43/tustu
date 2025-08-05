package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gu, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gu.class */
class C0386gu implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3497a;

    C0386gu(C0308dx c0308dx) {
        this.f3497a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13389bs, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
        this.f3497a.r();
    }
}
