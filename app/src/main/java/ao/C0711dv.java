package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dv, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dv.class */
class C0711dv implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5571a;

    C0711dv(bP bPVar) {
        this.f5571a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5571a.a("timeGapsOn", ((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
