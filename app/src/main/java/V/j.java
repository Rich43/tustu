package V;

/* loaded from: TunerStudioMS.jar:V/j.class */
public class j extends Exception {

    /* renamed from: a, reason: collision with root package name */
    private int f1910a;

    /* renamed from: b, reason: collision with root package name */
    private double f1911b;

    /* renamed from: c, reason: collision with root package name */
    private double f1912c;

    /* renamed from: d, reason: collision with root package name */
    private int f1913d;

    /* renamed from: e, reason: collision with root package name */
    private int f1914e;

    public j(String str, int i2, double d2, double d3, int i3, int i4) {
        super(str);
        this.f1910a = 0;
        this.f1911b = 0.0d;
        this.f1912c = 0.0d;
        this.f1913d = -1;
        this.f1914e = -1;
        this.f1910a = i2;
        this.f1912c = d3;
        this.f1911b = d2;
        this.f1913d = i3;
        this.f1914e = i4;
    }

    public j(String str, int i2, double d2, double d3) {
        super(str);
        this.f1910a = 0;
        this.f1911b = 0.0d;
        this.f1912c = 0.0d;
        this.f1913d = -1;
        this.f1914e = -1;
        this.f1910a = i2;
        this.f1912c = d3;
        this.f1911b = d2;
    }

    public j() {
        this.f1910a = 0;
        this.f1911b = 0.0d;
        this.f1912c = 0.0d;
        this.f1913d = -1;
        this.f1914e = -1;
    }

    public int a() {
        return this.f1910a;
    }

    public double b() {
        return this.f1911b;
    }

    public double c() {
        return this.f1912c;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String str = "Value out of bounds. " + this.f1911b;
        return this.f1910a == 1 ? str + " exceeds maximum allowed value " + this.f1912c : str + " is below minimum allowed value " + this.f1912c;
    }

    public int d() {
        return this.f1913d;
    }

    public int e() {
        return this.f1914e;
    }
}
