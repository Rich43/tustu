package aN;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/* loaded from: TunerStudioMS.jar:aN/c.class */
class c implements CaretListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f2635a;

    c(a aVar) {
        this.f2635a = aVar;
    }

    @Override // javax.swing.event.CaretListener
    public void caretUpdate(CaretEvent caretEvent) throws IllegalArgumentException {
        if (caretEvent.getMark() != caretEvent.getDot()) {
            this.f2635a.a();
        } else {
            this.f2635a.f2631e.setText("");
        }
    }
}
