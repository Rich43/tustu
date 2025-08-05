package aP;

import az.C0940a;
import bH.C1018z;
import java.awt.Component;
import java.awt.Window;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* renamed from: aP.du, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/du.class */
class C0305du implements bH.N {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0293dh f3249a;

    C0305du(C0293dh c0293dh) {
        this.f3249a = c0293dh;
    }

    @Override // bH.B
    public String a() {
        return C1798a.f13268b;
    }

    @Override // bH.B
    public String b() {
        return C1798a.a().c(C1798a.cC, "");
    }

    @Override // bH.B
    public String c() {
        return C1798a.a().c(C1798a.cD, "");
    }

    @Override // bH.B
    public String d() {
        return C1798a.a().c(C1798a.cE, "");
    }

    @Override // bH.B
    public String e() {
        String strC = C1798a.a().c(C1798a.f13280n, (String) null);
        return bH.W.b(strC == null ? bH.W.b(C1798a.f13269c, C0293dh.f3230g, "") : strC, C0293dh.f3229f, "").trim();
    }

    @Override // bH.B
    public String f() {
        return "";
    }

    @Override // bH.B
    public String h() {
        return C1798a.a().c(C1798a.f13281o, "");
    }

    @Override // bH.B
    public void i() {
        this.f3249a.r();
    }

    @Override // bH.B
    public String j() {
        return this.f3249a.q();
    }

    @Override // bH.N
    public void a(String str, String str2, String str3, String str4, String str5, String str6) {
        C0940a c0940a = new C0940a(cZ.a().c(), C1818g.d());
        com.efiAnalytics.ui.bV.a((Window) cZ.a().c(), (Component) c0940a);
        c0940a.setVisible(true);
        new C0306dv(this, str, str2, str3, str4, str5, str6, c0940a).start();
    }

    @Override // bH.B
    public String[] k() {
        String[] strArr = C1798a.f0do;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = bH.W.b(bH.W.b(strArr[i2], C0293dh.f3229f, ""), C1806i.f13440b, "");
        }
        if (C1018z.i().h()) {
            String[] strArr2 = new String[strArr.length * 2];
            for (int i3 = 0; i3 < strArr.length; i3++) {
                strArr2[i3 * 2] = strArr[i3];
                strArr2[(i3 * 2) + 1] = strArr[i3] + C1806i.f13442d;
            }
            strArr = strArr2;
        }
        return strArr;
    }

    @Override // bH.B
    public int[] l() {
        return C1798a.f13268b.equals(C1806i.f13458s) ? new int[]{3, 4} : new int[]{2, 3};
    }

    @Override // bH.B
    public boolean b(String str, String str2, String str3, String str4, String str5, String str6) {
        if (str4 != null && !str4.trim().equals("") && str4.indexOf("@") >= 1 && str4.indexOf(".") >= 0 && str4.lastIndexOf(".") >= str4.indexOf("@")) {
            return false;
        }
        com.efiAnalytics.ui.bV.d("Invalid Email Address", this.f3249a.rootPane);
        return true;
    }

    @Override // bH.B
    public String m() {
        return C1818g.b(C1798a.f13282p);
    }

    @Override // bH.B
    public boolean a(String str) {
        return C1798a.a().b(str);
    }
}
