package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/aH.class */
class aH implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0625ap f5122a;

    aH(C0625ap c0625ap) {
        this.f5122a = c0625ap;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5122a.c(((JCheckBoxMenuItem) itemEvent.getSource()).getState());
        this.f5122a.l();
    }
}
