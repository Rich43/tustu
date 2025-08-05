package ao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/fJ.class */
class fJ implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5708a;

    fJ(C0764fu c0764fu) {
        this.f5708a = c0764fu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strA = com.efiAnalytics.ui.bV.a("{Number of X Sections}", true, C0764fu.f5872O, true, (Component) this.f5708a.i(), new String[]{this.f5708a.i().p() + ""});
        if (strA == null) {
            return;
        }
        try {
            double d2 = Double.parseDouble(strA);
            if (d2 < 1.0d) {
                d2 = 1.0d;
            }
            if (d2 > C0764fu.f5874Q) {
                com.efiAnalytics.ui.bV.d("Maximum value is " + C0764fu.f5874Q + ", using that.", this.f5708a.i());
                d2 = C0764fu.f5874Q;
            }
            int iRound = (int) Math.round(d2);
            this.f5708a.a(C0764fu.f5872O, "" + iRound);
            this.f5708a.i().f(iRound);
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Invalid value: " + strA, this.f5708a.i());
        }
        this.f5708a.h();
    }
}
