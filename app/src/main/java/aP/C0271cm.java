package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.cm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cm.class */
class C0271cm implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3165a;

    C0271cm(bZ bZVar) {
        this.f3165a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12286G, h.i.f12288I);
        }
    }
}
