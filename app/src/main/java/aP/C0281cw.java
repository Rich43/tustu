package aP;

import ao.C0645bi;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.cw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cw.class */
class C0281cw implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3175a;

    C0281cw(bZ bZVar) {
        this.f3175a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f3175a.f3027b.p().q();
        }
        boolean state = ((JCheckBoxMenuItem) itemEvent.getSource()).getState();
        h.i.c("holdGraphCentered", "" + state);
        C0645bi.a().c().e(state);
    }
}
