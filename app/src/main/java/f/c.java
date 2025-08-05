package F;

/* loaded from: TunerStudioMS.jar:F/c.class */
class c {

    /* renamed from: b, reason: collision with root package name */
    private d f284b = d.IDLE;

    /* renamed from: c, reason: collision with root package name */
    private String f285c = "";

    /* renamed from: d, reason: collision with root package name */
    private long f286d;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ b f287a;

    public boolean a() {
        return this.f284b == d.LEASE;
    }

    public boolean b() {
        return this.f284b == d.IDLE;
    }

    public boolean c() {
        return this.f284b == d.INOFFERING;
    }

    public c(b bVar) {
        this.f287a = bVar;
    }

    public void a(d dVar) {
        this.f284b = dVar;
    }

    public String d() {
        return this.f285c;
    }

    public void a(String str) {
        this.f285c = str;
    }

    public void a(long j2) {
        this.f286d = j2;
    }

    public long e() {
        return this.f286d;
    }
}
