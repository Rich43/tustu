package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.cs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cs.class */
class C0682cs implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5502a;

    C0682cs(bP bPVar) {
        this.f5502a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5502a.a((JCheckBoxMenuItem) itemEvent.getSource());
    }
}
