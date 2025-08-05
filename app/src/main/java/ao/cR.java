package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cR.class */
class cR implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5474a;

    cR(bP bPVar) {
        this.f5474a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12293N, h.i.f12296Q);
            this.f5474a.f5347a.e(h.i.f12296Q);
        }
    }
}
