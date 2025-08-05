package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: aP.cp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cp.class */
class C0274cp implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3168a;

    C0274cp(bZ bZVar) {
        this.f3168a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        h.i.c(h.i.f12290K, "true");
        h.i.c(h.i.f12291L, "false");
        this.f3168a.f3027b.o();
    }
}
