package aP;

import bt.C1337bs;
import com.efiAnalytics.ui.C1686fq;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import r.C1798a;

/* renamed from: aP.gy, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gy.class */
class C0390gy implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3501a;

    C0390gy(C0308dx c0308dx) {
        this.f3501a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        bA.c cVar = (bA.c) actionEvent.getSource();
        C1798a.a().b(C1798a.cc, Boolean.toString(cVar.getState()));
        if (cVar.isSelected()) {
            C1686fq c1686fq = new C1686fq(cZ.a().c(), true, "Base atmospheric barometric pressure in KPa", C1798a.a().c(C1798a.f13327ai, "100"), com.efiAnalytics.ui.bV.a());
            c1686fq.setVisible(true);
            String strA = c1686fq.a();
            if (strA == null || strA.isEmpty()) {
                C1798a.a().b(C1798a.f13327ai, "100");
                C1337bs.a(100.0d);
                return;
            }
            try {
                C1337bs.a(Double.parseDouble(strA));
                C1798a.a().b(C1798a.f13327ai, strA);
            } catch (NumberFormatException e2) {
                C1798a.a().b(C1798a.f13327ai, "100");
                C1337bs.a(100.0d);
            }
        }
    }
}
