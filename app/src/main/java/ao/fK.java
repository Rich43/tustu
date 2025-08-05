package ao;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/fK.class */
class fK implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5709a;

    fK(C0764fu c0764fu) {
        this.f5709a = c0764fu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strA = com.efiAnalytics.ui.bV.a("{Maximum Number of Z Gradients}", true, C0764fu.f5873P, true, (Component) this.f5709a.i(), new String[]{this.f5709a.i().r() + ""});
        if (strA == null) {
            return;
        }
        try {
            double d2 = Double.parseDouble(strA);
            if (d2 < 1.0d) {
                d2 = 1.0d;
            }
            int iRound = (int) Math.round(d2);
            this.f5709a.a(C0764fu.f5873P, "" + iRound);
            this.f5709a.i().k(iRound);
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Invalid value: " + strA, this.f5709a.i());
        }
        this.f5709a.h();
    }
}
