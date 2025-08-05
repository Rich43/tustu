package bH;

import java.util.ArrayList;
import java.util.List;

/* renamed from: bH.z, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/z.class */
public class C1018z {

    /* renamed from: d, reason: collision with root package name */
    private static C1018z f7065d = null;

    /* renamed from: a, reason: collision with root package name */
    protected B f7066a = null;

    /* renamed from: b, reason: collision with root package name */
    List f7067b = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    private boolean f7068e = false;

    /* renamed from: c, reason: collision with root package name */
    public int f7069c = -1;

    protected C1018z() {
    }

    public static C1018z i() {
        return b(null);
    }

    public static C1018z b(B b2) {
        if (f7065d == null) {
            f7065d = new C1018z();
        }
        if (b2 != null) {
            f7065d.f7066a = b2;
        }
        return f7065d;
    }

    public boolean b() {
        return this.f7069c - 1926 == 40;
    }

    public void a(String str, String str2, String str3) {
        this.f7067b.add(new A(this, str, str2, str3));
    }

    public List j() {
        return this.f7067b;
    }

    public int c() {
        return this.f7069c;
    }

    public boolean a(String str) {
        return a(str, -1);
    }

    public boolean a(String str, int i2) {
        String strD;
        String strA;
        String strA2;
        if (str == null || str.trim().equals("") || this.f7066a.a() == null || this.f7066a.a().trim().equals("") || this.f7066a.e() == null || this.f7066a.b() == null || this.f7066a.b().trim().equals("") || this.f7066a.c() == null || this.f7066a.c().trim().equals("") || (strD = this.f7066a.d()) == null || strD.trim().equals("") || strD.indexOf("@") < 1 || strD.indexOf(".") < 0 || strD.lastIndexOf(".") < strD.indexOf("@")) {
            return false;
        }
        if (i2 == 1) {
            String strA3 = C0997e.a(this.f7066a.b(), this.f7066a.c(), this.f7066a.a(), this.f7066a.d());
            return strA3 != null && strA3.equals(str);
        }
        boolean z2 = false;
        if (a(this.f7066a.l(), 2) && (strA2 = C0997e.a(this.f7066a.b(), this.f7066a.c(), this.f7066a.a(), this.f7066a.e(), this.f7066a.d())) != null && strA2.equals(str)) {
            z2 = true;
        }
        if (!z2 && a(this.f7066a.l(), 3) && (strA = C0997e.a(this.f7066a.b(), this.f7066a.c(), this.f7066a.a(), this.f7066a.e(), this.f7066a.d(), W.a("1", '0', 2), W.a("2015", '0', 4))) != null && strA.equals(str)) {
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

    public boolean h() {
        return this.f7068e;
    }

    public void a(boolean z2) {
        this.f7068e = z2;
    }
}
