package bb;

/* renamed from: bb.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/q.class */
class RunnableC1052q implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f7808a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1051p f7809b;

    RunnableC1052q(C1051p c1051p, String str) {
        this.f7809b = c1051p;
        this.f7808a = str;
    }

    @Override // java.lang.Runnable
    public void run() throws IllegalArgumentException {
        this.f7809b.b(this.f7808a);
    }
}
