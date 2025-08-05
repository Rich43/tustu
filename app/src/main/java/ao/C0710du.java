package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.du, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/du.class */
class C0710du implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5570a;

    C0710du(bP bPVar) {
        this.f5570a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5570a.a("fillNaN", ((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
