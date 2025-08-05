package ao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/fH.class */
class fH implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5706a;

    fH(C0764fu c0764fu) {
        this.f5706a = c0764fu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strA = com.efiAnalytics.ui.bV.a("{Maximum Dot Size}", true, C0764fu.f5870M, true, (Component) this.f5706a.i(), new String[]{this.f5706a.i().o() + ""});
        if (strA == null) {
            return;
        }
        try {
            double d2 = Double.parseDouble(strA);
            if (d2 < 0.0d) {
                d2 = 0.0d;
            }
            if (d2 > 25.0d) {
                com.efiAnalytics.ui.bV.d("Maximum value is 40, using that.", this.f5706a.i());
                d2 = 40.0d;
            }
            int iRound = (int) Math.round(d2);
            this.f5706a.a(C0764fu.f5870M, "" + iRound);
            this.f5706a.i().e(iRound);
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Invalid value: " + strA, this.f5706a.i());
        }
        this.f5706a.h();
    }
}
