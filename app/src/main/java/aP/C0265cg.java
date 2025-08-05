package aP;

import ao.C0804hg;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.cg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cg.class */
class C0265cg implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3159a;

    C0265cg(bZ bZVar) {
        this.f3159a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12293N, h.i.f12296Q);
            C0804hg.a().c(true);
            this.f3159a.f3027b.e(h.i.f12296Q);
        }
    }
}
