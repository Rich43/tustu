package bM;

/* loaded from: TunerStudioMS.jar:bM/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    protected int f7207a = 0;

    /* renamed from: c, reason: collision with root package name */
    private double f7208c = 14.7d;

    /* renamed from: d, reason: collision with root package name */
    private double f7209d = 14.7d;

    /* renamed from: b, reason: collision with root package name */
    protected double f7210b = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    private int f7211e;

    c(int i2) {
        this.f7211e = i2;
    }

    public int a() {
        return this.f7207a;
    }

    public double b() {
        return this.f7208c;
    }

    public void a(double d2) {
        this.f7208c = d2;
    }

    public double c() {
        return this.f7210b;
    }

    public void b(double d2) {
        if (Double.isNaN(this.f7210b)) {
            this.f7210b = d2;
        } else {
            this.f7210b = ((this.f7210b * this.f7207a) + d2) / (this.f7207a + 1);
        }
        this.f7207a++;
    }

    public int d() {
        return this.f7211e;
    }

    public void a(int i2) {
        this.f7211e = i2;
    }

    public double e() {
        return this.f7209d / this.f7207a;
    }

    public void c(double d2) {
        this.f7209d += d2;
    }

    public void d(double d2) {
        this.f7209d = d2 * this.f7207a;
    }
}
