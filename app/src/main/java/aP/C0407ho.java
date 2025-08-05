package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: aP.ho, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ho.class */
class C0407ho implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0406hn f3606a;

    C0407ho(C0406hn c0406hn) {
        this.f3606a = c0406hn;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            this.f3606a.a(3);
            this.f3606a.f3599j.requestFocus();
        }
    }
}
