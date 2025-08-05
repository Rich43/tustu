package ao;

import W.C0188n;

/* renamed from: ao.eh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/eh.class */
class C0724eh extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0188n f5630a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0718eb f5631b;

    C0724eh(C0718eb c0718eb, C0188n c0188n) {
        this.f5631b = c0718eb;
        this.f5630a = c0188n;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f5631b.f5617c.a(this.f5630a);
        this.f5631b.f5617c.a(this.f5630a.a(h.g.a().a("Time")));
        this.f5631b.c();
    }
}
