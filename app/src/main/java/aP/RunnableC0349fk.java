package aP;

/* renamed from: aP.fk, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/fk.class */
class RunnableC0349fk implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3381a;

    RunnableC0349fk(C0308dx c0308dx) {
        this.f3381a = c0308dx;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f3381a.f3270h.validate();
        this.f3381a.f3275m = new gO(this.f3381a);
        this.f3381a.f3275m.start();
    }
}
