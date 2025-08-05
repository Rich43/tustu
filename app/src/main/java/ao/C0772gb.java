package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: ao.gb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gb.class */
class C0772gb implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5971a;

    C0772gb(fX fXVar) {
        this.f5971a = fXVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5971a.f5788ab.a((String) this.f5971a.f5784X.getSelectedItem());
        this.f5971a.f5788ab.b();
    }
}
