package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.cl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cl.class */
class C0270cl implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3164a;

    C0270cl(bZ bZVar) {
        this.f3164a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12286G, h.i.f12287H);
        }
    }
}
