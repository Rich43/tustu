package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/dM.class */
class dM implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3191a;

    dM(C0308dx c0308dx) {
        this.f3191a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        aE.a aVarA = aE.a.A();
        if (aVarA != null) {
            bA.c cVar = (bA.c) actionEvent.getSource();
            aVarA.a(cVar.getState());
            G.R rC = G.T.a().c();
            if (rC != null) {
                rC.O().b(cVar.getState());
            }
        }
    }
}
