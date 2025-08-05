package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: aP.hq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hq.class */
class C0409hq implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0406hn f3608a;

    C0409hq(C0406hn c0406hn) {
        this.f3608a = c0406hn;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            this.f3608a.a(1);
            this.f3608a.f3599j.requestFocus();
        }
    }
}
