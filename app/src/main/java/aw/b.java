package aW;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:aW/b.class */
class b implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f3969a;

    b(a aVar) {
        this.f3969a = aVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (this.f3969a.f3965e == null || this.f3969a.b() == null || !this.f3969a.f3965e.h().equals(this.f3969a.b().h())) {
            this.f3969a.d();
        }
    }
}
