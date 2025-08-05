package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSplitPane;

/* renamed from: aP.cy, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cy.class */
class C0283cy implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3177a;

    C0283cy(bZ bZVar) {
        this.f3177a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f3177a.f3027b.p().q();
        }
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12298S, JSplitPane.BOTTOM);
            this.f3177a.f3027b.p().i();
            this.f3177a.f3027b.p().repaint();
        }
    }
}
