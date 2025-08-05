package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dq.class */
class C0706dq implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5565a;

    C0706dq(bP bPVar) {
        this.f5565a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5565a.f5347a.b(((JCheckBoxMenuItem) itemEvent.getSource()).getState());
        h.i.c("showTuningConsole", "" + ((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
