package ao;

import az.C0940a;
import bH.C0997e;
import h.C1737b;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Window;
import java.util.Properties;
import javax.swing.JOptionPane;

/* loaded from: TunerStudioMS.jar:ao/dQ.class */
class dQ implements bH.N {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5532a;

    dQ(bP bPVar) {
        this.f5532a = bPVar;
    }

    @Override // bH.B
    public String a() {
        return h.i.f12255b;
    }

    @Override // bH.B
    public String b() {
        return h.i.e("firstName", "");
    }

    @Override // bH.B
    public String c() {
        return h.i.e("lastName", "");
    }

    @Override // bH.B
    public String d() {
        return h.i.e("userEmail", "");
    }

    @Override // bH.B
    public String e() {
        return bH.W.b(bH.W.b(h.i.f12256c, C1737b.f12222b, ""), C1737b.f12224d, "");
    }

    @Override // bH.B
    public String f() {
        return h.i.e("registrationKeyV2", "");
    }

    public String g() {
        return h.i.e("registrationKey", "");
    }

    @Override // bH.B
    public String h() {
        return "";
    }

    @Override // bH.B
    public void i() throws HeadlessException {
        this.f5532a.v();
    }

    @Override // bH.B
    public String j() {
        return this.f5532a.x();
    }

    @Override // bH.N
    public void a(String str, String str2, String str3, String str4, String str5, String str6) {
        dR dRVar = new dR(this);
        bH.W.b(h.i.f12256c, " Lite!", "");
        C0940a c0940a = new C0940a(C0645bi.a().b(), dRVar);
        com.efiAnalytics.ui.bV.a((Window) C0645bi.a().b(), (Component) c0940a);
        c0940a.setVisible(true);
        new dS(this, str, str2, str3, str4, c0940a).start();
    }

    @Override // bH.B
    public String[] k() {
        String[] strArr = new String[1];
        strArr[0] = bH.W.b(h.i.f12256c, C1737b.f12222b, "");
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = bH.W.b(bH.W.b(strArr[i2], C1737b.f12222b, ""), C1737b.f12224d, "");
        }
        return strArr;
    }

    @Override // bH.B
    public int[] l() {
        return new int[]{3};
    }

    @Override // bH.B
    public boolean b(String str, String str2, String str3, String str4, String str5, String str6) {
        String strA = C0997e.a(str, str2, "MegaLogViewer", str4);
        if (strA == null || !strA.equals(str3) || this.f5532a.f(str3)) {
            return false;
        }
        String str7 = hD.f6004b;
        Properties properties = new Properties();
        properties.setProperty(hD.f6003a, str4);
        if (JOptionPane.showConfirmDialog(this.f5532a.f5347a, hD.a(properties, str7), "Upgrade Registration", 0) != 0) {
            return true;
        }
        com.efiAnalytics.ui.aN.a("https://www.efianalytics.com/register/upgradeMlvRegistrationKey.jsp?email=" + str4);
        return true;
    }

    @Override // bH.B
    public String m() {
        return "";
    }

    @Override // bH.B
    public boolean a(String str) {
        return false;
    }
}
