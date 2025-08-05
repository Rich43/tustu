package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dl.class */
class C0702dl implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5559a;

    C0702dl(bP bPVar) {
        this.f5559a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        h.i.c(h.i.f12315aj, Boolean.toString(((JCheckBoxMenuItem) itemEvent.getItem()).getState()));
    }
}
