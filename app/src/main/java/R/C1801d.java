package r;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: r.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/d.class */
class C1801d implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1799b f13431a;

    C1801d(C1799b c1799b) {
        this.f13431a = c1799b;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        boolean z2 = itemEvent.getStateChange() == 1;
        this.f13431a.f13427f.setEnabled(z2);
        this.f13431a.f13425d.setEnabled(z2);
        this.f13431a.f13424c.setEnabled(!z2);
    }
}
