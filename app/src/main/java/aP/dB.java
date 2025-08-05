package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/dB.class */
class dB implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3180a;

    dB(C0308dx c0308dx) {
        this.f3180a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C1798a.a().b(C1798a.cn, actionEvent.getActionCommand());
        try {
            C1798a.a().e();
        } catch (V.a e2) {
            Logger.getLogger(C0308dx.class.getName()).log(Level.WARNING, "Error saving user properties", (Throwable) e2);
        }
        this.f3180a.r();
    }
}
