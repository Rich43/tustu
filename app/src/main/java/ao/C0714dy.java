package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dy, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dy.class */
class C0714dy implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5574a;

    C0714dy(bP bPVar) {
        this.f5574a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f5574a.h(null);
        }
    }
}
