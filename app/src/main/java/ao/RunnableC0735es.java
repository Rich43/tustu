package ao;

/* renamed from: ao.es, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/es.class */
class RunnableC0735es implements Runnable {

    /* renamed from: c, reason: collision with root package name */
    private boolean f5658c = true;

    /* renamed from: d, reason: collision with root package name */
    private double f5659d = 0.0d;

    /* renamed from: a, reason: collision with root package name */
    boolean f5660a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0729em f5661b;

    RunnableC0735es(C0729em c0729em) {
        this.f5661b = c0729em;
    }

    @Override // java.lang.Runnable
    public void run() {
        a(true);
        if (this.f5660a) {
            this.f5661b.f5642c.a(0.0d);
            this.f5661b.f5642c.b(this.f5661b.f5643d != null ? this.f5661b.f5643d.d() : 0.0d);
            this.f5661b.f5642c.c(0.0d);
            this.f5661b.f5642c.d(this.f5661b.f5643d != null ? this.f5661b.f5643d.d() : 0.0d);
        }
        this.f5661b.f5641b.a(a());
    }

    public void a(boolean z2) {
        this.f5658c = z2;
    }

    public double a() {
        return this.f5659d;
    }

    public void a(double d2) {
        this.f5659d = d2;
        if (d2 < 0.2d) {
            this.f5660a = false;
        } else if (d2 == 1.0d) {
            this.f5660a = true;
        }
    }

    public boolean b() {
        return this.f5658c;
    }
}
