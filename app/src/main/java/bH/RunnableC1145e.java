package bh;

/* renamed from: bh.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/e.class */
class RunnableC1145e implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1142b f8107a;

    RunnableC1145e(C1142b c1142b) {
        this.f8107a = c1142b;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f8107a.y();
        this.f8107a.n().e();
        this.f8107a.n().c(this.f8107a.n().r().d() - 1);
    }
}
