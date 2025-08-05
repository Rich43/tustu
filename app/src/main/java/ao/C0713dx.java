package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dx.class */
class C0713dx implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5573a;

    C0713dx(bP bPVar) {
        this.f5573a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f5573a.h(((JCheckBoxMenuItem) itemEvent.getSource()).getText());
        }
    }
}
