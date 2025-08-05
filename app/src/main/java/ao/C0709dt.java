package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dt, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dt.class */
class C0709dt implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5569a;

    C0709dt(bP bPVar) {
        this.f5569a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5569a.a("RPM_USE_FORMULA", ((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
