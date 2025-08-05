package bB;

/* loaded from: TunerStudioMS.jar:bB/a.class */
public class a implements r {

    /* renamed from: a, reason: collision with root package name */
    private double f6528a;

    /* renamed from: b, reason: collision with root package name */
    private double f6529b;

    /* renamed from: c, reason: collision with root package name */
    private String f6530c;

    /* renamed from: d, reason: collision with root package name */
    private int f6531d;

    public a() {
        this.f6528a = Double.NaN;
        this.f6529b = Double.NaN;
        this.f6530c = "";
        this.f6531d = -1;
    }

    public a(String str) {
        this.f6528a = Double.NaN;
        this.f6529b = Double.NaN;
        this.f6530c = "";
        this.f6531d = -1;
        this.f6530c = str;
    }

    @Override // bB.r
    public double a() {
        return this.f6528a;
    }

    @Override // bB.r
    public void a(double d2) {
        this.f6528a = d2;
    }

    @Override // bB.r
    public double b() {
        return this.f6529b;
    }

    @Override // bB.r
    public void b(double d2) {
        this.f6529b = d2;
    }

    @Override // bB.r
    public boolean c() {
        return Double.isNaN(this.f6528a);
    }

    @Override // bB.r
    public boolean d() {
        return Double.isNaN(this.f6529b);
    }

    @Override // bB.r
    public String e() {
        return this.f6530c;
    }

    public void a(String str) {
        this.f6530c = str;
    }

    @Override // bB.r
    public int f() {
        return this.f6531d;
    }

    @Override // bB.r
    public void a(int i2) {
        this.f6531d = i2;
    }
}
