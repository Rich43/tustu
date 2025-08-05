package ao;

import W.C0188n;

/* renamed from: ao.eo, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/eo.class */
class RunnableC0731eo implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0188n f5649a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0729em f5650b;

    RunnableC0731eo(C0729em c0729em, C0188n c0188n) {
        this.f5650b = c0729em;
        this.f5649a = c0188n;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f5650b.f5641b.a(this.f5649a);
        this.f5650b.f5641b.a(this.f5649a.a(h.g.a().a("Time")));
        this.f5650b.f5646g.a(this.f5649a);
        this.f5650b.c();
    }
}
