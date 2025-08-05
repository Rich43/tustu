package ao;

/* renamed from: ao.cy, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cy.class */
class C0688cy implements bW.b {

    /* renamed from: a, reason: collision with root package name */
    long f5508a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bP f5509b;

    C0688cy(bP bPVar) {
        this.f5509b = bPVar;
    }

    @Override // bW.b
    public void a(long j2, long j3) {
        if (this.f5508a == 0) {
            this.f5508a = System.currentTimeMillis();
        }
        C0636b.a().a(j2 / j3);
        C0636b.a().b((j2 / 1024) + "kbytes of " + (j3 / 1024) + "kbytes downloaded at " + bH.W.b((j2 / 1024.0d) / ((System.currentTimeMillis() - this.f5508a) / 1000.0d), 1) + " kbytes/s");
    }

    @Override // bW.b
    public void a() {
        C0636b.a().b("Update complete, application will now restart.");
    }
}
