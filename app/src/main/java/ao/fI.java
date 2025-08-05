package ao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/fI.class */
class fI implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5707a;

    fI(C0764fu c0764fu) {
        this.f5707a = c0764fu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strA = com.efiAnalytics.ui.bV.a("{Number of Y Sections}", true, C0764fu.f5871N, true, (Component) this.f5707a.i(), new String[]{this.f5707a.i().q() + ""});
        if (strA == null) {
            return;
        }
        try {
            double d2 = Double.parseDouble(strA);
            if (d2 < 1.0d) {
                d2 = 1.0d;
            }
            if (d2 > C0764fu.f5874Q) {
                com.efiAnalytics.ui.bV.d("Maximum value is " + C0764fu.f5874Q + ", using that.", this.f5707a.i());
                d2 = C0764fu.f5874Q;
            }
            int iRound = (int) Math.round(d2);
            this.f5707a.a(C0764fu.f5871N, "" + iRound);
            this.f5707a.i().g(iRound);
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Invalid value: " + strA, this.f5707a.i());
        }
        this.f5707a.h();
    }
}
