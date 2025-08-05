package W;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: W.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/j.class */
public class C0184j implements bH.Q {

    /* renamed from: a, reason: collision with root package name */
    public String f2134a;

    /* renamed from: e, reason: collision with root package name */
    private String f2135e;

    /* renamed from: f, reason: collision with root package name */
    private float f2136f;

    /* renamed from: g, reason: collision with root package name */
    private float f2137g;

    /* renamed from: h, reason: collision with root package name */
    private float f2138h;

    /* renamed from: i, reason: collision with root package name */
    private float f2139i;

    /* renamed from: j, reason: collision with root package name */
    private String f2140j;

    /* renamed from: k, reason: collision with root package name */
    private String f2141k;

    /* renamed from: l, reason: collision with root package name */
    private int f2142l;

    /* renamed from: m, reason: collision with root package name */
    private int f2143m;

    /* renamed from: n, reason: collision with root package name */
    private String f2144n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f2145o;

    /* renamed from: p, reason: collision with root package name */
    private int f2146p;

    /* renamed from: q, reason: collision with root package name */
    private float f2147q;

    /* renamed from: r, reason: collision with root package name */
    private float f2148r;

    /* renamed from: s, reason: collision with root package name */
    private float f2149s;

    /* renamed from: t, reason: collision with root package name */
    private int f2150t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f2151u;

    /* renamed from: v, reason: collision with root package name */
    private float f2152v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f2153w;

    /* renamed from: x, reason: collision with root package name */
    private int f2154x;

    /* renamed from: b, reason: collision with root package name */
    List f2155b;

    /* renamed from: c, reason: collision with root package name */
    InterfaceC0185k f2156c;

    /* renamed from: y, reason: collision with root package name */
    private String f2157y;

    /* renamed from: d, reason: collision with root package name */
    bH.K f2158d;

    /* renamed from: z, reason: collision with root package name */
    private static int f2159z = 1000;

    /* renamed from: A, reason: collision with root package name */
    private static int f2160A = 250000;

    /* renamed from: B, reason: collision with root package name */
    private Map f2161B;

    /* renamed from: C, reason: collision with root package name */
    private boolean f2162C;

    public C0184j() {
        this.f2134a = null;
        this.f2135e = null;
        this.f2136f = Float.NaN;
        this.f2137g = Float.NaN;
        this.f2138h = Float.NaN;
        this.f2139i = Float.NaN;
        this.f2140j = null;
        this.f2141k = null;
        this.f2142l = -1;
        this.f2143m = -1;
        this.f2144n = null;
        this.f2145o = false;
        this.f2146p = 0;
        this.f2147q = 1.0f;
        this.f2148r = 0.0f;
        this.f2149s = 0.0f;
        this.f2150t = 0;
        this.f2151u = false;
        this.f2152v = Float.POSITIVE_INFINITY;
        this.f2153w = false;
        this.f2154x = 2;
        this.f2155b = new ArrayList();
        this.f2157y = null;
        this.f2161B = null;
        this.f2162C = true;
        this.f2158d = new bH.K();
    }

    public C0184j(String str) {
        this.f2134a = null;
        this.f2135e = null;
        this.f2136f = Float.NaN;
        this.f2137g = Float.NaN;
        this.f2138h = Float.NaN;
        this.f2139i = Float.NaN;
        this.f2140j = null;
        this.f2141k = null;
        this.f2142l = -1;
        this.f2143m = -1;
        this.f2144n = null;
        this.f2145o = false;
        this.f2146p = 0;
        this.f2147q = 1.0f;
        this.f2148r = 0.0f;
        this.f2149s = 0.0f;
        this.f2150t = 0;
        this.f2151u = false;
        this.f2152v = Float.POSITIVE_INFINITY;
        this.f2153w = false;
        this.f2154x = 2;
        this.f2155b = new ArrayList();
        this.f2157y = null;
        this.f2161B = null;
        this.f2162C = true;
        this.f2158d = new bH.K();
        this.f2134a = str;
    }

    public C0184j(String str, int i2) {
        this.f2134a = null;
        this.f2135e = null;
        this.f2136f = Float.NaN;
        this.f2137g = Float.NaN;
        this.f2138h = Float.NaN;
        this.f2139i = Float.NaN;
        this.f2140j = null;
        this.f2141k = null;
        this.f2142l = -1;
        this.f2143m = -1;
        this.f2144n = null;
        this.f2145o = false;
        this.f2146p = 0;
        this.f2147q = 1.0f;
        this.f2148r = 0.0f;
        this.f2149s = 0.0f;
        this.f2150t = 0;
        this.f2151u = false;
        this.f2152v = Float.POSITIVE_INFINITY;
        this.f2153w = false;
        this.f2154x = 2;
        this.f2155b = new ArrayList();
        this.f2157y = null;
        this.f2161B = null;
        this.f2162C = true;
        int i3 = i2 < f2159z ? f2159z : i2;
        this.f2158d = new bH.K(i3 > f2160A ? f2160A : i3);
        this.f2134a = str;
    }

    public String a() {
        return this.f2134a;
    }

    public void a(String str) {
        this.f2134a = str;
    }

    public boolean b(String str) {
        return a(Float.parseFloat(str));
    }

    public boolean a(float f2) {
        int iV;
        int iV2;
        float fD = (f2 + this.f2148r) * this.f2147q;
        boolean zA = this.f2158d.a(fD);
        try {
            if (!Float.isNaN(fD)) {
                if (this.f2153w && (iV2 = (v() - 1) - this.f2154x) >= 0) {
                    fD = d(iV2);
                }
                if (this.f2162C && (iV = v() - 1) > 0 && d(iV - 1) > fD) {
                    this.f2162C = false;
                }
                if (Float.isNaN(this.f2136f) || fD < this.f2136f) {
                    d(fD);
                }
                if (fD < 1.0E7f && (Float.isNaN(this.f2137g) || fD > this.f2137g)) {
                    e(fD);
                }
                this.f2151u = true;
            }
        } catch (Exception e2) {
        }
        return zA;
    }

    public boolean a(int i2, float f2) {
        return this.f2158d.a(i2, f2);
    }

    public void b(int i2, float f2) {
        this.f2158d.b(i2, f2);
    }

    public boolean b(float f2) {
        return this.f2158d.a(f2);
    }

    public String a(int i2) {
        return c(c(i2));
    }

    public String b(int i2) {
        if (o()) {
            i2 = (this.f2158d.a() - 1) - i2;
        }
        return c(this.f2158d.a(i2) + this.f2149s);
    }

    public String b() {
        float fE = e();
        String strC = c(fE);
        float fH = h();
        if (fH == fE) {
            return strC;
        }
        switch (this.f2146p) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return c(0.0f) + " [" + c(fH) + "]";
            default:
                return strC + " [" + c(fH) + "]";
        }
    }

    public String d() {
        float f2 = f();
        String strC = c(f2);
        float fG = g();
        if (fG == f2) {
            return strC;
        }
        switch (this.f2146p) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return c(1.0f) + " [" + c(fG) + "]";
            default:
                return strC + " [" + c(fG) + "]";
        }
    }

    public String c(float f2) {
        if (this.f2161B != null && f2 >= 1.0E7f && this.f2161B.containsKey(Float.valueOf(f2))) {
            return (String) this.f2161B.get(Float.valueOf(f2));
        }
        if (this.f2146p == 0) {
            return Float.isNaN(f2) ? "NaN" : (this.f2153w || this.f2142l >= 0) ? bH.W.b(f2, this.f2142l) : f2 > 999999.0f ? bH.W.b(f2, 0) : bH.W.b(f2, 3);
        }
        if (this.f2146p == 4) {
            return f2 == 0.0f ? "Off" : "On";
        }
        if (this.f2146p == 5) {
            return f2 == 0.0f ? "No" : "Yes";
        }
        if (this.f2146p == 7) {
            return f2 == 0.0f ? "Inactive" : "Active";
        }
        if (this.f2146p == 6) {
            return f2 == 0.0f ? "Low" : "High";
        }
        if (this.f2146p == 8) {
            return f2 == 0.0f ? "False" : "True";
        }
        if (this.f2146p == 3) {
            return DateFormat.getDateInstance().format(k(f2));
        }
        if (this.f2146p == 1) {
            return "0x" + Integer.toHexString((int) f2).toUpperCase();
        }
        if (this.f2146p == 2) {
            return this.f2142l > 0 ? "0b" + bH.W.a(Integer.toBinaryString((int) f2), '0', this.f2142l) : "0b" + Integer.toBinaryString((int) f2);
        }
        if (this.f2146p == 255 && this.f2156c != null) {
            return this.f2156c.a(f2);
        }
        bH.C.b("Column " + this.f2134a + " set to invalid DisplayType. Setting to the default float type.");
        f(0);
        return c(f2);
    }

    public float c(int i2) {
        return Float.valueOf(d(i2)).floatValue();
    }

    public float d(int i2) {
        if (o()) {
            i2 = (this.f2158d.a() - 1) - i2;
        }
        try {
            return this.f2153w ? a(i2, this.f2154x).floatValue() : this.f2158d.a(i2 + this.f2150t) + this.f2149s;
        } catch (Exception e2) {
            return Float.NaN;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x008d A[Catch: Exception -> 0x009e, TryCatch #0 {Exception -> 0x009e, blocks: (B:7:0x0012, B:9:0x0019, B:11:0x0027, B:13:0x003a, B:21:0x005e, B:23:0x006f, B:24:0x007d, B:25:0x0083, B:17:0x0052, B:26:0x008d), top: B:32:0x0012 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Float a(int r5, int r6) {
        /*
            r4 = this;
            r0 = r4
            int r0 = r0.f2150t
            if (r0 == 0) goto Le
            r0 = r5
            r1 = r4
            int r1 = r1.f2150t
            int r0 = r0 + r1
            r5 = r0
        Le:
            r0 = r6
            if (r0 <= 0) goto L8d
            r0 = r4
            java.util.Map r0 = r0.f2161B     // Catch: java.lang.Exception -> L9e
            if (r0 == 0) goto L27
            r0 = r4
            bH.K r0 = r0.f2158d     // Catch: java.lang.Exception -> L9e
            r1 = r5
            float r0 = r0.a(r1)     // Catch: java.lang.Exception -> L9e
            r1 = 1259902592(0x4b189680, float:1.0E7)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L8d
        L27:
            r0 = r5
            r1 = r6
            int r0 = r0 + r1
            r1 = 1
            int r0 = r0 + r1
            r8 = r0
            r0 = r8
            r1 = r4
            bH.K r1 = r1.f2158d     // Catch: java.lang.Exception -> L9e
            int r1 = r1.a()     // Catch: java.lang.Exception -> L9e
            if (r0 <= r1) goto L43
            r0 = r4
            bH.K r0 = r0.f2158d     // Catch: java.lang.Exception -> L9e
            int r0 = r0.a()     // Catch: java.lang.Exception -> L9e
            r8 = r0
        L43:
            r0 = 0
            r9 = r0
            r0 = 0
            r10 = r0
            r0 = r6
            r1 = r5
            if (r0 <= r1) goto L52
            r0 = 0
            goto L55
        L52:
            r0 = r5
            r1 = r6
            int r0 = r0 - r1
        L55:
            r11 = r0
        L57:
            r0 = r11
            r1 = r8
            if (r0 >= r1) goto L83
            r0 = r4
            bH.K r0 = r0.f2158d     // Catch: java.lang.Exception -> L9e
            r1 = r11
            float r0 = r0.a(r1)     // Catch: java.lang.Exception -> L9e
            r7 = r0
            r0 = r7
            boolean r0 = java.lang.Float.isNaN(r0)     // Catch: java.lang.Exception -> L9e
            if (r0 != 0) goto L7d
            r0 = r10
            r1 = r7
            r2 = r4
            float r2 = r2.f2149s     // Catch: java.lang.Exception -> L9e
            float r1 = r1 + r2
            float r0 = r0 + r1
            r10 = r0
            int r9 = r9 + 1
        L7d:
            int r11 = r11 + 1
            goto L57
        L83:
            r0 = r10
            r1 = r9
            float r1 = (float) r1     // Catch: java.lang.Exception -> L9e
            float r0 = r0 / r1
            r7 = r0
            goto L9b
        L8d:
            r0 = r4
            bH.K r0 = r0.f2158d     // Catch: java.lang.Exception -> L9e
            r1 = r5
            float r0 = r0.a(r1)     // Catch: java.lang.Exception -> L9e
            r1 = r4
            float r1 = r1.f2149s     // Catch: java.lang.Exception -> L9e
            float r0 = r0 + r1
            r7 = r0
        L9b:
            goto Lae
        L9e:
            r8 = move-exception
            r0 = r4
            bH.K r0 = r0.f2158d
            r1 = r5
            float r0 = r0.a(r1)
            r1 = r4
            float r1 = r1.f2149s
            float r0 = r0 + r1
            r7 = r0
        Lae:
            r0 = r7
            java.lang.Float r0 = java.lang.Float.valueOf(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: W.C0184j.a(int, int):java.lang.Float");
    }

    public void d(float f2) {
        if (Float.isInfinite(-f2)) {
            return;
        }
        this.f2136f = f2;
    }

    public float e() {
        if (!Float.isNaN(this.f2138h)) {
            return this.f2138h;
        }
        if (this.f2146p == 4 || this.f2146p == 5 || this.f2146p == 6 || this.f2146p == 7) {
            return -0.1f;
        }
        if (Float.isNaN(this.f2136f) && this.f2158d.a() > 0) {
            for (int i2 = 0; i2 < this.f2158d.a(); i2++) {
                float fC = c(i2);
                if (fC < this.f2136f || Float.isNaN(this.f2136f)) {
                    this.f2136f = fC;
                }
            }
        }
        return this.f2136f;
    }

    public float f() {
        if (!Float.isNaN(this.f2139i)) {
            return this.f2139i;
        }
        if (this.f2146p == 4 || this.f2146p == 5 || this.f2146p == 6 || this.f2146p == 7) {
            return 1.1f;
        }
        if (Float.isNaN(this.f2137g) && this.f2158d.a() > 0) {
            for (int i2 = 0; i2 < this.f2158d.a(); i2++) {
                float fC = c(i2);
                if (((this.f2161B == null || fC < 1.0E7f) && fC > this.f2137g) || Float.isNaN(this.f2137g)) {
                    this.f2137g = fC;
                }
            }
        }
        return this.f2137g;
    }

    public float g() {
        if (this.f2146p == 4 || this.f2146p == 5 || this.f2146p == 6 || this.f2146p == 7) {
            return 1.0f;
        }
        if (Float.isNaN(this.f2137g) && this.f2158d.a() > 0) {
            for (int i2 = 0; i2 < this.f2158d.a(); i2++) {
                float fC = c(i2);
                if ((this.f2161B == null || fC < 1.0E7f) && (fC > this.f2137g || Float.isNaN(this.f2137g))) {
                    this.f2137g = fC;
                }
            }
        }
        return this.f2137g;
    }

    public float h() {
        if (this.f2146p == 4 || this.f2146p == 5 || this.f2146p == 6 || this.f2146p == 7) {
            return 0.0f;
        }
        if (Float.isNaN(this.f2136f) && this.f2158d.a() > 0) {
            for (int i2 = 0; i2 < this.f2158d.a(); i2++) {
                float fC = c(i2);
                if (fC < this.f2136f || Float.isNaN(this.f2136f)) {
                    this.f2136f = fC;
                }
            }
        }
        return this.f2136f;
    }

    public void e(float f2) {
        if (Float.isInfinite(f2)) {
            return;
        }
        if (this.f2161B == null || f2 < 1.0E7f) {
            this.f2137g = f2;
        }
    }

    public void f(float f2) {
        this.f2139i = f2;
    }

    public void g(float f2) {
        this.f2138h = f2;
    }

    public int i() {
        return this.f2158d.a();
    }

    public String j() {
        return this.f2140j;
    }

    public double b(int i2, int i3) {
        double dC = 0.0d;
        for (int i4 = 0; i4 <= i3 - i2; i4++) {
            dC = ((dC * i4) + c(i2 + i4)) / (i4 + 1);
        }
        return dC;
    }

    public double c(int i2, int i3) {
        double dC = Double.POSITIVE_INFINITY;
        for (int i4 = i2; i4 <= i3; i4++) {
            if (dC > c(i4)) {
                dC = c(i4);
            }
        }
        return dC;
    }

    public double d(int i2, int i3) {
        double d2 = Double.NEGATIVE_INFINITY;
        for (int i4 = i2; i4 <= i3; i4++) {
            float fC = c(i4);
            if ((this.f2161B == null || fC < 1.0E7f) && d2 < fC) {
                d2 = fC;
            }
        }
        return d2;
    }

    public double a(int i2, int i3, float f2, float f3) {
        double dC = 0.0d;
        for (int i4 = 0; i4 <= i3 - i2; i4++) {
            float fC = c(i2 + i4);
            if (fC >= f2 && fC <= f3) {
                dC = ((dC * i4) + c(i2 + i4)) / (i4 + 1);
            }
        }
        return dC;
    }

    public void c(String str) {
        this.f2140j = str;
    }

    public String k() {
        return this.f2141k;
    }

    public void d(String str) {
        this.f2141k = str;
    }

    public boolean l() {
        return !(this.f2141k == null || this.f2141k.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER) == -1) || (this.f2140j != null && this.f2140j.indexOf("logVal") == -1);
    }

    public int m() {
        return this.f2142l;
    }

    public void e(int i2) {
        this.f2142l = i2;
    }

    public String n() {
        return this.f2144n;
    }

    public void e(String str) {
        this.f2144n = str;
    }

    public boolean o() {
        return this.f2145o;
    }

    public void a(boolean z2) {
        this.f2145o = z2;
    }

    public int p() {
        return this.f2146p;
    }

    public void f(int i2) {
        this.f2146p = i2;
    }

    public void h(float f2) {
        this.f2147q = f2;
    }

    public void i(float f2) {
        this.f2148r = f2;
    }

    private static Date k(float f2) {
        return new Date(((long) f2) * 1000);
    }

    protected void e(int i2, int i3) {
        this.f2158d.a(i2, i3);
        this.f2136f = Float.NaN;
        this.f2137g = Float.NaN;
        for (int i4 = 0; i4 < this.f2158d.a(); i4++) {
            float fA = this.f2158d.a(i4);
            if (!Float.isNaN(fA)) {
                if (Float.isNaN(this.f2136f) || fA < this.f2136f) {
                    d(fA);
                }
                if (Float.isNaN(this.f2137g) || fA > this.f2137g) {
                    e(fA);
                }
            }
        }
    }

    public boolean q() {
        return this.f2151u;
    }

    @Override // bH.Q
    public String c() {
        return a();
    }

    public boolean r() {
        return this.f2153w;
    }

    public void b(boolean z2) {
        this.f2153w = z2;
        t();
    }

    public int s() {
        return this.f2154x;
    }

    public void g(int i2) {
        this.f2154x = i2;
    }

    public void t() {
        this.f2136f = Float.NaN;
        this.f2137g = Float.NaN;
    }

    public void j(float f2) {
        this.f2149s = f2;
        C();
    }

    public void a(InterfaceC0183i interfaceC0183i) {
        this.f2155b.add(interfaceC0183i);
    }

    public void b(InterfaceC0183i interfaceC0183i) {
        this.f2155b.remove(interfaceC0183i);
    }

    private void C() {
        Iterator it = this.f2155b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0183i) it.next()).a(this.f2134a);
        }
    }

    public String u() {
        return this.f2135e;
    }

    public void f(String str) {
        this.f2135e = str;
    }

    public int v() {
        return this.f2158d.a();
    }

    public void w() {
        this.f2158d.b();
    }

    public void a(InterfaceC0185k interfaceC0185k) {
        this.f2156c = interfaceC0185k;
    }

    public boolean x() {
        return this.f2158d.a() == 0;
    }

    public void h(int i2) {
        this.f2158d.a(i2, i2 + 1);
    }

    public String y() {
        return this.f2157y;
    }

    public void g(String str) {
        this.f2157y = str;
    }

    public void a(Map map) {
        this.f2161B = map;
        t();
    }

    public boolean z() {
        return this.f2162C;
    }

    public int A() {
        return this.f2150t;
    }

    public void i(int i2) {
        this.f2150t = i2;
    }

    public int B() {
        return this.f2143m;
    }

    public void j(int i2) {
        this.f2143m = i2;
    }
}
