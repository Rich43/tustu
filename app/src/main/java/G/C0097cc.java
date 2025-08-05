package G;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: G.cc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cc.class */
public class C0097cc implements Serializable {

    /* renamed from: d, reason: collision with root package name */
    private String f1096d = "";

    /* renamed from: e, reason: collision with root package name */
    private cZ f1097e = null;

    /* renamed from: a, reason: collision with root package name */
    BigInteger f1098a = BigInteger.ZERO;

    /* renamed from: b, reason: collision with root package name */
    int f1099b = -1;

    /* renamed from: c, reason: collision with root package name */
    int f1100c = 0;

    /* renamed from: f, reason: collision with root package name */
    private dh f1101f = new B(1.0d);

    /* renamed from: g, reason: collision with root package name */
    private dh f1102g = new B(0.0d);

    /* renamed from: h, reason: collision with root package name */
    private String f1103h = "";

    /* renamed from: i, reason: collision with root package name */
    private int f1104i = 0;

    /* renamed from: j, reason: collision with root package name */
    private String f1105j = null;

    /* renamed from: k, reason: collision with root package name */
    private double f1106k = Double.NaN;

    /* renamed from: l, reason: collision with root package name */
    private long f1107l = Long.MAX_VALUE;

    /* renamed from: m, reason: collision with root package name */
    private boolean f1108m = false;

    public void a(int i2, int i3) {
        this.f1099b = i2;
        this.f1100c = i3;
        this.f1098a = BigInteger.valueOf(((long) Math.pow(2.0d, i2)) - 1).shiftLeft(i3);
    }

    public double a(BigInteger bigInteger) {
        this.f1106k = (bigInteger.and(this.f1098a).shiftRight(this.f1100c).doubleValue() + j()) * c();
        return this.f1106k;
    }

    public String b() {
        return this.f1096d;
    }

    public void b(String str) {
        this.f1096d = str;
    }

    public double c() {
        return this.f1101f.a();
    }

    public void a(dh dhVar) {
        this.f1101f = dhVar;
    }

    public void a(double d2) {
        this.f1101f = new B(d2);
    }

    public String d() {
        return this.f1103h;
    }

    public void c(String str) {
        this.f1103h = str;
    }

    public int e() {
        return this.f1104i;
    }

    public void a(int i2) {
        this.f1104i = i2;
    }

    public int f() {
        return (1 + this.f1099b) / 8;
    }

    public void b(int i2) {
        this.f1107l = i2;
    }

    public String g() {
        try {
            return this.f1097e != null ? this.f1097e.a() : this.f1096d;
        } catch (V.g e2) {
            Logger.getLogger(C0097cc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return this.f1096d;
        }
    }

    public void d(String str) {
        this.f1097e = new C0094c(str);
    }

    public void a(cZ cZVar) {
        this.f1097e = cZVar;
    }

    public void e(String str) {
        this.f1105j = str;
    }

    public String h() {
        return this.f1105j;
    }

    public boolean i() {
        return this.f1108m;
    }

    public void a(boolean z2) {
        this.f1108m = z2;
    }

    public void b(dh dhVar) {
        this.f1102g = dhVar;
    }

    public void b(double d2) {
        this.f1102g = new B(d2);
    }

    public double j() {
        return this.f1102g.a();
    }
}
