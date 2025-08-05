package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.ey, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ey.class */
class C0336ey implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3332a;

    C0336ey(C0308dx c0308dx) {
        this.f3332a = c0308dx;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) itemEvent.getSource();
        C1798a.a().b(C1798a.f13361aQ, "" + jCheckBoxMenuItem.getState());
        if (jCheckBoxMenuItem.getState()) {
            ((C0293dh) cZ.a().c()).e();
        }
    }
}
