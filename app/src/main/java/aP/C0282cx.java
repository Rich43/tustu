package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSplitPane;

/* renamed from: aP.cx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cx.class */
class C0282cx implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3176a;

    C0282cx(bZ bZVar) {
        this.f3176a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f3176a.f3027b.p().q();
        }
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12298S, JSplitPane.TOP);
            this.f3176a.f3027b.p().i();
            this.f3176a.f3027b.p().repaint();
        }
    }
}
