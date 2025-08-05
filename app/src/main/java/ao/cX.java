package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cX.class */
class cX implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5480a;

    cX(bP bPVar) {
        this.f5480a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f5480a.f5347a.p().q();
        }
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12298S, "withLabels");
            this.f5480a.f5347a.p().i();
            this.f5480a.f5347a.p().repaint();
        }
    }
}
