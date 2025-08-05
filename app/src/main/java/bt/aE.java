package bt;

/* loaded from: TunerStudioMS.jar:bt/aE.class */
class aE implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f8746a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1303al f8747b;

    aE(C1303al c1303al, int i2) {
        this.f8747b = c1303al;
        this.f8746a = i2;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f8747b.f8868w.f().c() != this.f8746a) {
            this.f8747b.f8868w.a(this.f8746a);
        }
    }
}
