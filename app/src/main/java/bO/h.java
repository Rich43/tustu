package bO;

/* loaded from: TunerStudioMS.jar:bO/h.class */
public class h {

    /* renamed from: a, reason: collision with root package name */
    private int f7367a = 1;

    /* renamed from: b, reason: collision with root package name */
    private int f7368b = 4;

    /* renamed from: c, reason: collision with root package name */
    private int f7369c = 1;

    /* renamed from: d, reason: collision with root package name */
    private int f7370d = 255;

    /* renamed from: e, reason: collision with root package name */
    private final m f7371e = new m();

    /* renamed from: f, reason: collision with root package name */
    private int f7372f = 1;

    public int a() {
        return this.f7368b;
    }

    public void a(int i2) {
        this.f7368b = i2;
    }

    public int b() {
        return this.f7369c;
    }

    public void b(int i2) {
        this.f7369c = i2;
    }

    public int c() {
        return this.f7370d;
    }

    public void c(int i2) {
        this.f7370d = i2;
    }

    public m d() {
        return this.f7371e;
    }

    public int e() {
        return this.f7372f;
    }

    public void d(int i2) {
        this.f7372f = i2;
    }

    public int f() {
        return this.f7367a;
    }

    public void e(int i2) {
        this.f7367a = i2;
    }

    public String toString() {
        return "granularityOdtEntrySize=" + this.f7367a + ", maxOdtEntrySizeDAQ=" + this.f7368b + ", granularityOdtEntrySizeStim=" + this.f7369c + ", maxOdtEntrySizeStim=" + this.f7370d + ", timestampTicks=" + this.f7372f + ", timestampMode=" + this.f7371e.toString();
    }
}
