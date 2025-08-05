package aP;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/* renamed from: aP.ie, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ie.class */
class C0424ie implements CaretListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0423id f3728a;

    C0424ie(C0423id c0423id) {
        this.f3728a = c0423id;
    }

    @Override // javax.swing.event.CaretListener
    public void caretUpdate(CaretEvent caretEvent) {
        if (caretEvent.getMark() > caretEvent.getDot()) {
            this.f3728a.a();
        }
    }
}
