package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dk, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dk.class */
class C0701dk implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5558a;

    C0701dk(bP bPVar) {
        this.f5558a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f5558a.f5347a.p().q();
        }
        boolean state = ((JCheckBoxMenuItem) itemEvent.getSource()).getState();
        h.i.c("holdGraphCentered", "" + state);
        C0645bi.a().c().e(state);
    }
}
