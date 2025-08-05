package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.da, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/da.class */
class C0691da implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5548a;

    C0691da(bP bPVar) {
        this.f5548a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        h.i.c(h.i.f12313ah, Boolean.toString(jCheckBoxMenuItem.isSelected()));
        C0625ap.g(jCheckBoxMenuItem.isSelected());
        int iB = h.i.b("lineTraceSize", h.i.f12310ae);
        if (jCheckBoxMenuItem.isSelected() && iB < 2) {
            this.f5548a.f5347a.p().c(2);
            h.i.c("lineTraceSize", "2");
        }
        if (jCheckBoxMenuItem.isSelected()) {
            this.f5548a.f5347a.p().b(true);
            h.i.c(h.i.f12311af, Boolean.toString(true));
        }
        this.f5548a.f5347a.p().i();
        this.f5548a.f5347a.p().repaint();
    }
}
