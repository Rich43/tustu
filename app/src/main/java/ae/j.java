package ae;

/* loaded from: TunerStudioMS.jar:ae/j.class */
class j implements u {

    /* renamed from: a, reason: collision with root package name */
    double f4369a;

    /* renamed from: b, reason: collision with root package name */
    double f4370b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0502f f4371c;

    j(C0502f c0502f, double d2, double d3) {
        this.f4371c = c0502f;
        this.f4369a = 0.0d;
        this.f4370b = 1.0d;
        this.f4369a = d2;
        this.f4370b = d3;
    }

    @Override // ae.u
    public void a(double d2) {
        this.f4371c.a(this.f4369a + (d2 * this.f4370b));
    }

    @Override // ae.u
    public void a(String str) {
        this.f4371c.a(str);
    }

    @Override // ae.u
    public boolean b(String str) {
        return this.f4371c.b(str);
    }
}
