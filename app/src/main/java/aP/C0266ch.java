package aP;

import ao.C0804hg;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.ch, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ch.class */
class C0266ch implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3160a;

    C0266ch(bZ bZVar) {
        this.f3160a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12293N, h.i.f12295P);
            C0804hg.a().c(true);
            this.f3160a.f3027b.e(h.i.f12295P);
            C0804hg.a().c(true);
        }
    }
}
