package ao;

import W.C0188n;

/* renamed from: ao.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/x.class */
class RunnableC0826x implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0188n f6187a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0823u f6188b;

    RunnableC0826x(C0823u c0823u, C0188n c0188n) {
        this.f6188b = c0823u;
        this.f6187a = c0188n;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f6188b.c(this.f6187a);
    }
}
