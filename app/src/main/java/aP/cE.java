package aP;

import ao.C0636b;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/cE.class */
class cE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3114a;

    cE(bZ bZVar) {
        this.f3114a = bZVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C0636b.a().a(com.efiAnalytics.ui.bV.a(this.f3114a.getParent()));
    }
}
