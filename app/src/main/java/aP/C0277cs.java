package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.cs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cs.class */
class C0277cs implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3171a;

    C0277cs(bZ bZVar) {
        this.f3171a = bZVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        h.i.c(h.i.f12311af, Boolean.toString(jCheckBoxMenuItem.isSelected()));
        this.f3171a.f3027b.p().b(jCheckBoxMenuItem.isSelected());
        this.f3171a.f3027b.p().i();
        this.f3171a.f3027b.p().repaint();
    }
}
