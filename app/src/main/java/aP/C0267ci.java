package aP;

import ao.C0804hg;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.ci, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ci.class */
class C0267ci implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3161a;

    C0267ci(bZ bZVar) {
        this.f3161a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12293N, h.i.f12294O);
            C0804hg.a().c(true);
            this.f3161a.f3027b.e(h.i.f12294O);
            C0804hg.a().c(true);
        }
    }
}
