package aP;

import ao.C0625ap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.ct, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ct.class */
class C0278ct implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3172a;

    C0278ct(bZ bZVar) {
        this.f3172a = bZVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        h.i.c(h.i.f12313ah, Boolean.toString(jCheckBoxMenuItem.isSelected()));
        C0625ap.g(jCheckBoxMenuItem.isSelected());
        int iB = h.i.b("lineTraceSize", h.i.f12310ae);
        if (jCheckBoxMenuItem.isSelected() && iB < 2) {
            this.f3172a.f3027b.p().c(2);
            h.i.c("lineTraceSize", "2");
        }
        if (jCheckBoxMenuItem.isSelected()) {
            this.f3172a.f3027b.p().b(true);
            h.i.c(h.i.f12311af, Boolean.toString(true));
        }
        this.f3172a.f3027b.p().i();
        this.f3172a.f3027b.p().repaint();
    }
}
