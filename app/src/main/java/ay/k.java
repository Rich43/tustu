package aY;

import G.C0113cs;
import G.R;
import G.T;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aY/k.class */
class k implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ j f4065a;

    k(j jVar) {
        this.f4065a = jVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        aE.a aVarA = aE.a.A();
        if (aVarA != null) {
            aVarA.q(actionEvent.getActionCommand());
            R rC = T.a().c();
            if (rC != null) {
                rC.c(Integer.parseInt(actionEvent.getActionCommand()));
                C0113cs.a().a(rC);
            }
        }
    }
}
