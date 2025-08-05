package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.di, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/di.class */
class C0699di implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5556a;

    C0699di(bP bPVar) {
        this.f5556a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5556a.b(((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
