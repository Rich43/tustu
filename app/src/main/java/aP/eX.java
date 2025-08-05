package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/eX.class */
class eX implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3305a;

    eX(C0308dx c0308dx) {
        this.f3305a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        G.R rC = G.T.a().c();
        boolean state = ((bA.c) actionEvent.getSource()).getState();
        if (rC != null) {
            if (state) {
                rC.I();
                rC.C().c();
            } else if (aE.a.A().J()) {
                com.efiAnalytics.ui.bV.d("In read only mode!\nYou are currently using a Temporary Project that is intended for view only.\nPlease use a project you created specifically for your controller to connect.", cZ.a().c());
            } else {
                C0338f.a().a(rC);
            }
        }
    }
}
