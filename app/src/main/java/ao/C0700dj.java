package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dj.class */
class C0700dj implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5557a;

    C0700dj(bP bPVar) {
        this.f5557a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5557a.a(((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
