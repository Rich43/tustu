package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: ao.ha, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ha.class */
class C0798ha implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0615af f6042a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ gS f6043b;

    C0798ha(gS gSVar, C0615af c0615af) {
        this.f6043b = gSVar;
        this.f6042a = c0615af;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            this.f6042a.a(this.f6043b.e((String) itemEvent.getItem()));
        }
    }
}
