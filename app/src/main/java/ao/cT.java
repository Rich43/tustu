package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cT.class */
class cT implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5476a;

    cT(bP bPVar) {
        this.f5476a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12293N, h.i.f12295P);
            this.f5476a.f5347a.e(h.i.f12295P);
        }
    }
}
