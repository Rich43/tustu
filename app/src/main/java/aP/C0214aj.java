package aP;

/* renamed from: aP.aj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/aj.class */
class C0214aj extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ G.R f2912a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0207ac f2913b;

    C0214aj(C0207ac c0207ac, G.R r2) {
        this.f2913b = c0207ac;
        this.f2912a = r2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f2912a.C().c();
    }
}
