package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/dO.class */
class dO implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3193a;

    dO(C0308dx c0308dx) {
        this.f3193a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        G.J.e(((bA.c) actionEvent.getSource()).getState());
    }
}
