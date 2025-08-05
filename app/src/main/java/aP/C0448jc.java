package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.jc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/jc.class */
class C0448jc implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iX f3782a;

    C0448jc(iX iXVar) {
        this.f3782a = iXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3782a.a(actionEvent.getActionCommand());
    }
}
