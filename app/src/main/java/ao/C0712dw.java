package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dw.class */
class C0712dw implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5572a;

    C0712dw(bP bPVar) {
        this.f5572a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5572a.a(h.i.f12284E, ((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
