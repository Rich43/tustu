package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dp.class */
class C0705dp implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5564a;

    C0705dp(bP bPVar) {
        this.f5564a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5564a.d(((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
