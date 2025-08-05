package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dV.class */
class dV implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    C0804hg f5542a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bP f5543b;

    dV(bP bPVar, C0804hg c0804hg) {
        this.f5543b = bPVar;
        this.f5542a = null;
        this.f5542a = c0804hg;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        h.i.c("playbackSpeed", ((JCheckBoxMenuItem) itemEvent.getSource()).getName());
        this.f5542a.a(h.i.a("playbackSpeed", 1.0d), false);
    }
}
