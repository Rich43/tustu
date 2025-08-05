package bt;

import com.efiAnalytics.ui.fy;
import s.C1818g;

/* renamed from: bt.bx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bx.class */
class C1342bx implements fy {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1337bs f9073a;

    C1342bx(C1337bs c1337bs) {
        this.f9073a = c1337bs;
    }

    @Override // com.efiAnalytics.ui.fy
    public boolean a(double d2, double d3) {
        G.bQ bQVar;
        String[] strArrB;
        String strB;
        G.bQ bQVar2;
        String[] strArrB2;
        String strB2;
        if (d2 > this.f9073a.f9059d.r()) {
            String text = this.f9073a.f9062g.getText();
            if (text.isEmpty()) {
                text = this.f9073a.f9059d.aJ();
            }
            String str = text + " " + C1818g.b("value") + " " + d2 + " " + C1818g.b("is too high.") + "\n" + C1818g.b("Must be less than:") + " " + this.f9073a.f9059d.r();
            if ((this.f9073a.f9059d.s() instanceof G.bQ) && (strArrB2 = (bQVar2 = (G.bQ) this.f9073a.f9059d.s()).b()) != null && strArrB2.length == 1 && strArrB2[0].equals(bQVar2.c().trim()) && (strB2 = G.bL.b(this.f9073a.f9056a, strArrB2[0])) != null) {
                str = str + "\n\n" + C1818g.b("Note!") + "\n" + C1818g.b("This limit can be adjusted at:") + "\n" + strB2;
            }
            com.efiAnalytics.ui.bV.d(str, this.f9073a.f9061f);
            return false;
        }
        if (d2 >= this.f9073a.f9059d.q()) {
            return true;
        }
        String text2 = this.f9073a.f9062g.getText();
        if (text2.isEmpty()) {
            text2 = this.f9073a.f9059d.aJ();
        }
        String str2 = text2 + " " + C1818g.b("value") + " " + d2 + " " + C1818g.b("is too low.") + "\n" + C1818g.b("Must be greater than:") + " " + this.f9073a.f9059d.q();
        if ((this.f9073a.f9059d.t() instanceof G.bQ) && (strArrB = (bQVar = (G.bQ) this.f9073a.f9059d.t()).b()) != null && strArrB.length == 1 && strArrB[0].equals(bQVar.c().trim()) && (strB = G.bL.b(this.f9073a.f9056a, strArrB[0])) != null) {
            str2 = str2 + "\n\n" + C1818g.b("Note!") + "\n" + C1818g.b("This limit can be adjusted at:") + "\n" + strB;
        }
        com.efiAnalytics.ui.bV.d(str2, this.f9073a.f9061f);
        return false;
    }

    @Override // com.efiAnalytics.ui.fy
    public boolean b(double d2, double d3) {
        G.bQ bQVar;
        String[] strArrB;
        String strB;
        G.bQ bQVar2;
        String[] strArrB2;
        String strB2;
        if (d2 > this.f9073a.f9058c.r()) {
            String text = this.f9073a.f9060e.getText();
            if (text.isEmpty()) {
                text = this.f9073a.f9058c.aJ();
            }
            String str = text + " " + C1818g.b("value") + " " + d2 + " " + C1818g.b("is too high.") + "\n" + C1818g.b("Must be less than:") + " " + this.f9073a.f9058c.r();
            if ((this.f9073a.f9058c.s() instanceof G.bQ) && (strArrB2 = (bQVar2 = (G.bQ) this.f9073a.f9058c.s()).b()) != null && strArrB2.length == 1 && strArrB2[0].equals(bQVar2.c().trim()) && (strB2 = G.bL.b(this.f9073a.f9056a, strArrB2[0])) != null) {
                str = str + "\n\n" + C1818g.b("Note!") + "\n" + C1818g.b("This limit can be adjusted at:") + "\n" + strB2;
            }
            com.efiAnalytics.ui.bV.d(str, this.f9073a.f9061f);
            return false;
        }
        if (d2 >= this.f9073a.f9058c.q()) {
            return true;
        }
        String text2 = this.f9073a.f9060e.getText();
        if (text2.isEmpty()) {
            text2 = this.f9073a.f9058c.aJ();
        }
        String str2 = text2 + " " + C1818g.b("value") + " " + d2 + " " + C1818g.b("is too low.") + "\n" + C1818g.b("Must be greater than:") + " " + this.f9073a.f9058c.q();
        if ((this.f9073a.f9058c.t() instanceof G.bQ) && (strArrB = (bQVar = (G.bQ) this.f9073a.f9058c.t()).b()) != null && strArrB.length == 1 && strArrB[0].equals(bQVar.c().trim()) && (strB = G.bL.b(this.f9073a.f9056a, strArrB[0])) != null) {
            str2 = str2 + "\n\n" + C1818g.b("Note!") + "\n" + C1818g.b("This limit can be adjusted at:") + "\n" + strB;
        }
        com.efiAnalytics.ui.bV.d(str2, this.f9073a.f9061f);
        return false;
    }
}
