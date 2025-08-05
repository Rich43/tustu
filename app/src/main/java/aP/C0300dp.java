package aP;

/* renamed from: aP.dp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dp.class */
class C0300dp implements bW.b {

    /* renamed from: a, reason: collision with root package name */
    long f3243a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0293dh f3244b;

    C0300dp(C0293dh c0293dh) {
        this.f3244b = c0293dh;
    }

    @Override // bW.b
    public void a(long j2, long j3) {
        if (this.f3243a == 0) {
            this.f3243a = System.currentTimeMillis();
        }
        C0338f.a().a(j2 / j3);
        C0338f.a().f((j2 / 1024) + " kbytes of " + (j3 / 1024) + " kbytes downloaded at " + bH.W.b((j2 / 1024.0d) / ((System.currentTimeMillis() - this.f3243a) / 1000.0d), 1) + " kbytes/s");
    }

    @Override // bW.b
    public void a() {
        C0338f.a().f("Update complete, application will now restart.");
    }
}
