package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: aP.hp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hp.class */
class C0408hp implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0406hn f3607a;

    C0408hp(C0406hn c0406hn) {
        this.f3607a = c0406hn;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            this.f3607a.a(2);
            this.f3607a.f3599j.requestFocus();
        }
    }
}
