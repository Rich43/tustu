package bG;

/* loaded from: TunerStudioMS.jar:bG/o.class */
class o {

    /* renamed from: b, reason: collision with root package name */
    private double f6968b = Double.NaN;

    /* renamed from: c, reason: collision with root package name */
    private double f6969c = Double.NaN;

    /* renamed from: d, reason: collision with root package name */
    private double f6970d = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    private double f6971e = Double.NaN;

    /* renamed from: f, reason: collision with root package name */
    private int f6972f;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ m f6973a;

    o(m mVar, int i2) {
        this.f6973a = mVar;
        this.f6972f = i2;
    }

    public double a() {
        return this.f6968b;
    }

    public void a(double d2) {
        this.f6968b = d2;
    }

    public void b(double d2) {
        this.f6969c = d2;
    }

    public void c(double d2) {
        this.f6970d = d2;
    }

    public void d(double d2) {
        this.f6971e = d2;
    }

    public int b() {
        return this.f6972f;
    }

    public boolean a(double d2, double d3) {
        return d2 > this.f6968b && d2 < this.f6969c && d3 > this.f6970d && d3 < (360.0d + this.f6971e) % 360.0d;
    }
}
