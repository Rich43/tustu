package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dm.class */
class C0703dm implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5560a;

    C0703dm(bP bPVar) {
        this.f5560a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5560a.c(((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
