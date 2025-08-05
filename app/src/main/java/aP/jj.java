package aP;

/* loaded from: TunerStudioMS.jar:aP/jj.class */
class jj implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f3792a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ int f3793b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0450je f3794c;

    jj(C0450je c0450je, int i2, int i3) {
        this.f3794c = c0450je;
        this.f3792a = i2;
        this.f3793b = i3;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f3794c.changeSelection(this.f3792a, this.f3793b, false, false);
    }
}
