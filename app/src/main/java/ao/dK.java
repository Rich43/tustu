package ao;

import g.C1733k;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dK.class */
class dK implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    C0804hg f5524a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bP f5525b;

    dK(bP bPVar, C0804hg c0804hg) {
        this.f5525b = bPVar;
        this.f5524a = null;
        this.f5524a = c0804hg;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) throws NumberFormatException {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) itemEvent.getSource();
        if (jCheckBoxMenuItem.getName().equals("graphBackColor")) {
            h.i.c("graphBackColor", jCheckBoxMenuItem.getText());
            this.f5524a.a(h.i.a("graphBackColor", Color.white));
        }
        if (jCheckBoxMenuItem.getName().startsWith("graphForeColor")) {
            h.i.c(jCheckBoxMenuItem.getName(), jCheckBoxMenuItem.getText());
            this.f5524a.a(h.i.a(jCheckBoxMenuItem.getName(), Color.black), Integer.parseInt(C1733k.a(jCheckBoxMenuItem.getName(), "graphForeColor", "")));
            this.f5525b.f5347a.o();
        }
    }
}
