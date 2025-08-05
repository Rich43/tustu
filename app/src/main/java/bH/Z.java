package bH;

/* loaded from: TunerStudioMS.jar:bH/Z.class */
public class Z {

    /* renamed from: a, reason: collision with root package name */
    private long f7032a = 0;

    /* renamed from: b, reason: collision with root package name */
    private float f7033b = -1.0f;

    /* renamed from: c, reason: collision with root package name */
    private long f7034c = -1;

    /* renamed from: d, reason: collision with root package name */
    private int f7035d = 0;

    public void a() {
        this.f7035d++;
        this.f7032a = System.nanoTime();
    }

    public long b() {
        this.f7034c = System.nanoTime() - this.f7032a;
        return this.f7034c;
    }

    public float a(long j2) {
        return j2 / 1000000.0f;
    }

    public float c() {
        return a(this.f7034c);
    }

    public float d() {
        b();
        return a(this.f7034c);
    }

    public void e() {
        this.f7032a = 0L;
        this.f7033b = -1.0f;
        this.f7034c = -1L;
        this.f7035d = 0;
    }
}
