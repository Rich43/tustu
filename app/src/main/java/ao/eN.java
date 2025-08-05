package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/eN.class */
class eN implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5592a;

    eN(C0737eu c0737eu) {
        this.f5592a = c0737eu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        h.i.c("veAnalysisBit6Bit7Filter", ((JCheckBoxMenuItem) itemEvent.getSource()).getState() + "");
    }
}
