package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.ds, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ds.class */
class C0708ds implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5568a;

    C0708ds(bP bPVar) {
        this.f5568a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5568a.a(h.i.f12300U, ((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
