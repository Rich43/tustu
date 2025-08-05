package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/dA.class */
class dA implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3179a;

    dA(C0308dx c0308dx) {
        this.f3179a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.f13302J, Boolean.toString(false));
        try {
            C1798a.a().e();
        } catch (V.a e2) {
            Logger.getLogger(C0308dx.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f3179a.r();
    }
}
