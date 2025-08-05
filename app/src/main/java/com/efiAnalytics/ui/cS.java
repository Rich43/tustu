package com.efiAnalytics.ui;

import bH.C0997e;
import bH.C1018z;
import java.awt.Component;
import java.awt.Frame;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cS.class */
public class cS extends C1018z {

    /* renamed from: d, reason: collision with root package name */
    private static cS f11158d = null;

    /* renamed from: e, reason: collision with root package name */
    private cT f11159e = null;

    /* renamed from: f, reason: collision with root package name */
    private int f11160f = 0;

    private cS() {
    }

    public static cS a() {
        return a((bH.B) null);
    }

    public static cS a(bH.B b2) {
        if (f11158d == null) {
            f11158d = new cS();
        }
        if (b2 != null) {
            f11158d.f7066a = b2;
            C1018z.b(b2);
        }
        return f11158d;
    }

    @Override // bH.C1018z
    public boolean b() {
        return this.f7069c - 1926 == 40;
    }

    @Override // bH.C1018z
    public int c() {
        return this.f7069c;
    }

    public void a(Component component) {
        if ((this.f7066a == null || !a(this.f7066a.f())) && this.f11160f > 0) {
            b(component);
        }
    }

    public void b(Component component) {
        this.f11159e = new cT(this, c(component));
        this.f11159e.a();
    }

    private Frame c(Component component) {
        while (component != null && !(component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }

    @Override // bH.C1018z
    public boolean a(String str) {
        return a(str, -1);
    }

    @Override // bH.C1018z
    public boolean a(String str, int i2) {
        String strD;
        String strA;
        String strA2;
        String strA3;
        if (str == null || str.trim().equals("") || this.f7066a.a() == null || this.f7066a.a().trim().equals("") || this.f7066a.e() == null || this.f7066a.b() == null || this.f7066a.b().trim().equals("") || this.f7066a.c() == null || this.f7066a.c().trim().equals("") || (strD = this.f7066a.d()) == null || strD.trim().equals("") || strD.indexOf("@") < 1 || strD.indexOf(".") < 0 || strD.lastIndexOf(".") < strD.indexOf("@")) {
            return false;
        }
        if (i2 == 1) {
            String strA4 = C0997e.a(this.f7066a.b(), this.f7066a.c(), this.f7066a.a(), this.f7066a.d());
            return strA4 != null && strA4.equals(str);
        }
        boolean z2 = false;
        if (a(this.f7066a.l(), 2) && (strA3 = C0997e.a(this.f7066a.b(), this.f7066a.c(), this.f7066a.a(), this.f7066a.e(), this.f7066a.d())) != null && strA3.equals(str)) {
            z2 = true;
        }
        if (!z2 && a(this.f7066a.l(), 3) && (strA2 = C0997e.a(this.f7066a.b(), this.f7066a.c(), this.f7066a.a(), this.f7066a.e(), this.f7066a.d(), bH.W.a("1", '0', 2), bH.W.a("2015", '0', 4))) != null && strA2.equals(str)) {
            z2 = true;
        }
        if (!z2 && a(this.f7066a.l(), 4) && (strA = C0997e.a(this.f7066a.b(), this.f7066a.c(), this.f7066a.a(), this.f7066a.e(), this.f7066a.d(), bH.W.a("1", '0', 2), bH.W.a("2015", '0', 4), this.f7066a.h())) != null && strA.equals(str)) {
            z2 = true;
        }
        return z2;
    }

    private boolean a(int[] iArr, int i2) {
        for (int i3 : iArr) {
            if (i3 == i2) {
                return true;
            }
        }
        return false;
    }

    public void d() {
        aN.a(this.f7066a.j());
    }

    public void e() {
        if (this.f7066a != null) {
            this.f7066a.i();
        }
        if (this.f11159e == null || this.f11160f <= 0) {
            g();
        } else {
            this.f11159e.f11161a = true;
        }
    }

    public void f() {
        g();
    }

    void g() {
        if (this.f11159e != null) {
            this.f11159e.dispose();
        }
    }

    @Override // bH.C1018z
    public boolean h() {
        return super.h() || C1018z.i().h();
    }

    static /* synthetic */ int c(cS cSVar) {
        int i2 = cSVar.f11160f;
        cSVar.f11160f = i2 - 1;
        return i2;
    }
}
