package ao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/fF.class */
class fF implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5704a;

    fF(C0764fu c0764fu) {
        this.f5704a = c0764fu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strA = com.efiAnalytics.ui.bV.a("{Minimum Dot Size}", true, C0764fu.f5869L, true, (Component) this.f5704a.i(), new String[]{this.f5704a.i().n() + ""});
        if (strA == null) {
            return;
        }
        try {
            double d2 = Double.parseDouble(strA);
            if (d2 < 0.0d) {
                d2 = 0.0d;
            }
            if (d2 > 25.0d) {
                com.efiAnalytics.ui.bV.d("Maximum value is 25, using that.", this.f5704a.i());
                d2 = 25.0d;
            }
            int iRound = (int) Math.round(d2);
            this.f5704a.a(C0764fu.f5869L, "" + iRound);
            this.f5704a.i().d(iRound);
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Invalid value: " + strA, this.f5704a.i());
        }
        this.f5704a.h();
    }
}
