package bu;

import W.C0188n;

/* renamed from: bu.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bu/a.class */
public class C1368a {

    /* renamed from: a, reason: collision with root package name */
    private double f9142a = Double.NaN;

    /* renamed from: b, reason: collision with root package name */
    private double f9143b = Double.NaN;

    /* renamed from: c, reason: collision with root package name */
    private double f9144c = Double.NaN;

    /* renamed from: d, reason: collision with root package name */
    private double f9145d = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    private double f9146e = Double.NaN;

    /* renamed from: f, reason: collision with root package name */
    private double f9147f = Double.NaN;

    /* renamed from: g, reason: collision with root package name */
    private double f9148g = Double.NaN;

    /* renamed from: h, reason: collision with root package name */
    private double f9149h = Double.NaN;

    /* renamed from: i, reason: collision with root package name */
    private double f9150i = Double.NaN;

    /* renamed from: j, reason: collision with root package name */
    private String f9151j = "";

    /* renamed from: k, reason: collision with root package name */
    private boolean f9152k = false;

    public double a() {
        return this.f9143b;
    }

    public void a(double d2) {
        this.f9143b = d2;
    }

    public double b() {
        return this.f9145d;
    }

    public void b(double d2) {
        this.f9145d = d2;
    }

    public double c() {
        return this.f9146e;
    }

    public void c(double d2) {
        this.f9146e = d2;
    }

    public double d() {
        return this.f9148g;
    }

    public void d(double d2) {
        this.f9148g = d2;
    }

    public double e() {
        return this.f9149h;
    }

    public void e(double d2) {
        this.f9149h = d2;
    }

    public double f() {
        return this.f9150i;
    }

    public void f(double d2) {
        this.f9150i = d2;
    }

    public static C1368a a(C0188n c0188n) {
        C1368a c1368a = new C1368a();
        int i2 = 0;
        if (c0188n != null) {
            String strF = c0188n.f("60ftET");
            if (strF == null) {
                strF = c0188n.f("60ftETGen");
                if (strF != null) {
                    i2 = 0 + 1;
                }
            }
            if (strF != null) {
                c1368a.a(b(strF));
            }
            String strF2 = c0188n.f("330ftET");
            if (strF2 == null) {
                strF2 = c0188n.f("330ftETGen");
                if (strF2 != null) {
                    i2++;
                }
            }
            if (strF2 != null) {
                c1368a.g(b(strF2));
            }
            String strF3 = c0188n.f("660ftET");
            if (strF3 == null) {
                strF3 = c0188n.f("660ftETGen");
                if (strF3 != null) {
                    i2++;
                }
            }
            if (strF3 != null) {
                c1368a.b(b(strF3));
            }
            String strF4 = c0188n.f("660ftMPH");
            if (strF4 == null) {
                strF4 = c0188n.f("660ftMPHGen");
                if (strF4 != null) {
                    i2++;
                }
            }
            if (strF4 != null) {
                c1368a.c(b(strF4));
            }
            String strF5 = c0188n.f("1000ftET");
            if (strF5 == null) {
                strF5 = c0188n.f("1000ftETGen");
                if (strF5 != null) {
                    i2++;
                }
            }
            if (strF5 != null) {
                c1368a.h(b(strF5));
            }
            String strF6 = c0188n.f("1320ftET");
            if (strF6 == null) {
                strF6 = c0188n.f("1320ftETGen");
            }
            if (strF6 != null) {
                c1368a.d(b(strF6));
            }
            String strF7 = c0188n.f("1320ftMPH");
            if (strF7 == null) {
                strF7 = c0188n.f("1320ftMPHGen");
            }
            if (strF7 != null) {
                c1368a.e(b(strF7));
            }
            String strF8 = c0188n.f("rolloutTime");
            if (strF8 != null) {
                c1368a.i(b(strF8));
            }
            String strF9 = c0188n.f("DensityAltitude");
            if (strF9 != null) {
                c1368a.f(b(strF9));
            }
            String strF10 = c0188n.f("TimeSlipNotes");
            if (strF10 != null) {
                c1368a.a(strF10);
            }
            if (i2 > 2) {
                c1368a.a(true);
            }
        }
        return c1368a;
    }

    public C0188n b(C0188n c0188n) {
        c0188n.a("60ftET", Double.toString(a()));
        c0188n.a("330ftET", Double.toString(g()));
        c0188n.a("660ftET", Double.toString(b()));
        c0188n.a("660ftMPH", Double.toString(c()));
        c0188n.a("1000ftET", Double.toString(i()));
        c0188n.a("1320ftET", Double.toString(d()));
        c0188n.a("1320ftMPH", Double.toString(e()));
        c0188n.a("DensityAltitude", Double.toString(f()));
        c0188n.a("TimeSlipNotes", j());
        return c0188n;
    }

    private static double b(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e2) {
            return Double.NaN;
        }
    }

    public double g() {
        return this.f9144c;
    }

    public void g(double d2) {
        this.f9144c = d2;
    }

    public boolean h() {
        return (Double.isNaN(this.f9143b) && Double.isNaN(this.f9144c) && Double.isNaN(this.f9146e) && Double.isNaN(this.f9147f) && Double.isNaN(this.f9148g) && Double.isNaN(this.f9149h) && Double.isNaN(this.f9150i)) ? false : true;
    }

    public double i() {
        return this.f9147f;
    }

    public void h(double d2) {
        this.f9147f = d2;
    }

    public String j() {
        return this.f9151j;
    }

    public void a(String str) {
        this.f9151j = str;
    }

    public double k() {
        return this.f9142a;
    }

    public void i(double d2) {
        this.f9142a = d2;
    }

    public boolean l() {
        return this.f9152k;
    }

    public void a(boolean z2) {
        this.f9152k = z2;
    }
}
