package ac;

import G.C0043ac;
import G.aH;
import bH.C0995c;
import bH.W;

/* loaded from: TunerStudioMS.jar:ac/q.class */
public class q {

    /* renamed from: a, reason: collision with root package name */
    private String f4240a = null;

    /* renamed from: b, reason: collision with root package name */
    private C0043ac f4241b = null;

    /* renamed from: c, reason: collision with root package name */
    private aH f4242c = null;

    /* renamed from: d, reason: collision with root package name */
    private int f4243d = 0;

    /* renamed from: e, reason: collision with root package name */
    private String f4244e = null;

    /* renamed from: f, reason: collision with root package name */
    private int f4245f = 0;

    /* renamed from: g, reason: collision with root package name */
    private double[] f4246g = new double[1];

    /* renamed from: h, reason: collision with root package name */
    private int f4247h = 0;

    /* renamed from: i, reason: collision with root package name */
    private String f4248i = null;

    /* renamed from: j, reason: collision with root package name */
    private String f4249j = null;

    public double a(byte[] bArr) {
        this.f4246g[(((this.f4247h + this.f4246g.length) - this.f4245f) - 1) % this.f4246g.length] = c().b(bArr);
        double d2 = this.f4246g[this.f4247h % this.f4246g.length];
        this.f4247h++;
        return d2;
    }

    public String a(double d2) {
        if (this.f4241b.e() == 0) {
            return W.c(d2, e());
        }
        if (this.f4241b.e() == 1) {
            return "0x" + W.a(Integer.toHexString(C0995c.a((byte) d2)), '0', 2).toUpperCase();
        }
        return (d2 != 0.0d || this.f4241b.h() == null || this.f4241b.h().length() <= 0) ? (d2 == 0.0d || this.f4241b.i() == null || this.f4241b.h().length() <= 0) ? W.c(d2, e()) : this.f4241b.i() : this.f4241b.h();
    }

    public String a() {
        return this.f4240a;
    }

    public void a(String str) {
        this.f4240a = str;
    }

    public C0043ac b() {
        return this.f4241b;
    }

    public void a(C0043ac c0043ac) {
        this.f4241b = c0043ac;
    }

    public aH c() {
        return this.f4242c;
    }

    public void a(aH aHVar) {
        this.f4242c = aHVar;
    }

    public int d() {
        return this.f4243d;
    }

    public void a(int i2) {
        this.f4243d = i2;
    }

    public int e() {
        return b().d();
    }

    public String f() {
        return (this.f4241b.e() == 0 || this.f4241b.j() == null) ? this.f4242c.e() != null ? this.f4242c.e() : "" : this.f4241b.j();
    }

    public String g() {
        return this.f4244e;
    }

    public void b(String str) {
        this.f4244e = str;
    }

    public int h() {
        return this.f4245f;
    }

    public void b(int i2) {
        this.f4245f = i2;
    }

    public void c(int i2) {
        this.f4246g = new double[i2 + 1];
        this.f4247h = 0;
        for (int i3 = 0; i3 < this.f4246g.length; i3++) {
            this.f4246g[i3] = Double.NaN;
        }
    }

    public String i() {
        return this.f4248i;
    }

    public void c(String str) {
        this.f4248i = str;
    }

    public String j() {
        return this.f4249j;
    }

    public void d(String str) {
        this.f4249j = str;
    }
}
