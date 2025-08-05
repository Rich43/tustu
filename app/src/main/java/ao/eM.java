package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/eM.class */
class eM implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5591a;

    eM(C0737eu c0737eu) {
        this.f5591a = c0737eu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        h.i.c("veAnalysisDisableOverrunFilter", ((JCheckBoxMenuItem) itemEvent.getSource()).getState() + "");
    }
}
