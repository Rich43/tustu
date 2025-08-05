package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/eE.class */
class eE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3285a;

    eE(C0308dx c0308dx) {
        this.f3285a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strC = C1798a.a().c(C1798a.cF, "");
        if (strC.isEmpty()) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Registration Key not found."), this.f3285a.f3270h);
        } else {
            com.efiAnalytics.ui.aN.a("https://www.efianalytics.com/register/upgradeSoftware.jsp?regKey=" + strC);
        }
    }
}
