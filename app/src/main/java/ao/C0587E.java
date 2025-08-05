package ao;

import W.C0184j;
import W.C0188n;
import g.C1727e;
import k.C1756d;

/* renamed from: ao.E, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/E.class */
public class C0587E {

    /* renamed from: a, reason: collision with root package name */
    private String f5072a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f5073b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f5074c = null;

    /* renamed from: d, reason: collision with root package name */
    private float f5075d = 0.0f;

    /* renamed from: e, reason: collision with root package name */
    private int f5076e = 1;

    /* renamed from: f, reason: collision with root package name */
    private String f5077f = null;

    /* renamed from: g, reason: collision with root package name */
    private int f5078g = 0;

    public boolean a(C0188n c0188n, int i2) {
        float fC;
        boolean zA;
        C0184j c0184jA = c0188n.a(this.f5072a);
        if (this.f5076e != 64 && c0184jA == null) {
            return false;
        }
        if (this.f5076e == 64) {
            String strB = null;
            try {
                strB = C1727e.b(c0188n, this.f5074c, i2);
                zA = C1756d.a().a(strB).d() != 0.0d;
            } catch (V.h e2) {
                bH.C.b("Unable to evaluate filter condition, not filtering:" + this.f5074c + "\n\t" + strB);
                return false;
            } catch (ax.U e3) {
                bH.C.b("Unable to evaluate filter condition, not filtering:" + this.f5074c + "\n\t" + strB);
                return false;
            }
        } else {
            float fC2 = c0184jA.c(i2);
            if (this.f5073b == null || this.f5073b.equals("")) {
                fC = this.f5075d;
            } else {
                C0184j c0184jA2 = c0188n.a(this.f5073b);
                if (c0184jA2 == null) {
                    return false;
                }
                fC = c0184jA2.c(i2);
            }
            zA = a(fC2, fC, this.f5076e);
        }
        if (zA) {
            c(a() + 1);
        }
        return zA;
    }

    public boolean a(double d2, double d3, int i2) {
        int i3;
        return i2 == 1 ? d2 == d3 : i2 == 2 ? d2 > d3 : i2 == 16 ? d2 >= d3 : i2 == 4 ? d2 < d3 : i2 == 8 ? d2 <= d3 : i2 == 32 && (i3 = (int) d3) == (((int) d2) & i3);
    }

    public void a(String str) {
        this.f5072a = str;
    }

    public void b(String str) {
        this.f5075d = Float.parseFloat(str);
    }

    public void a(float f2) {
        this.f5075d = f2;
    }

    public void a(int i2) {
        this.f5075d = i2;
    }

    public void b(int i2) {
        this.f5076e = i2;
    }

    public String toString() {
        return "Field=" + this.f5072a + "\noperator=" + this.f5076e + "\ncompareField=" + this.f5073b + "\ncompareValue=" + this.f5075d;
    }

    public void c(String str) {
        this.f5074c = str;
    }

    public int a() {
        return this.f5078g;
    }

    public void c(int i2) {
        this.f5078g = i2;
    }

    public String b() {
        return (this.f5077f == null || this.f5077f.equals("")) ? this.f5076e == 64 ? this.f5074c : this.f5072a != null ? this.f5072a : "Undefined" : this.f5077f;
    }

    public void d(String str) {
        this.f5077f = str;
    }
}
