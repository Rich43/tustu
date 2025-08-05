package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/fZ.class */
class fZ implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3370a;

    fZ(C0308dx c0308dx) {
        this.f3370a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        bA.c cVar = (bA.c) actionEvent.getSource();
        if (cVar.getState()) {
            C1798a.a().b(C1798a.f13352aH, cVar.getActionCommand());
            cZ.a().i().c();
            C0338f.a().x();
            this.f3370a.e();
        }
    }
}
