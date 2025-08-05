package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.cz, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cz.class */
class C0284cz implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3178a;

    C0284cz(bZ bZVar) {
        this.f3178a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f3178a.f3027b.p().q();
        }
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12298S, "withLabels");
            this.f3178a.f3027b.p().i();
            this.f3178a.f3027b.p().repaint();
        }
    }
}
