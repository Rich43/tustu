package aP;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import r.C1798a;
import s.C1818g;

/* renamed from: aP.gr, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gr.class */
class C0383gr implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3494a;

    C0383gr(C0308dx c0308dx) {
        this.f3494a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String str;
        double dA = C1798a.a().a(C1798a.f13404bH, C1798a.f13405bI);
        String strA = "z";
        while (true) {
            str = strA;
            if (bH.H.a(str) || str.length() <= 0) {
                break;
            } else {
                strA = com.efiAnalytics.ui.bV.a((Component) this.f3494a.f3270h, true, "Maximum MegaBytes for a Projects Restore points.", "" + dA);
            }
        }
        if (!bH.H.a(str) || str.length() <= 0) {
            return;
        }
        if (actionEvent.getSource() instanceof gS) {
            ((gS) actionEvent.getSource()).setText(C1818g.b("Maximum Disk Space in MB") + ": " + str);
        }
        C1798a.a().b(C1798a.f13404bH, str);
    }
}
