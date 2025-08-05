package bt;

/* loaded from: TunerStudioMS.jar:bt/aS.class */
class aS extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f8772a;

    /* renamed from: b, reason: collision with root package name */
    boolean f8773b;

    /* renamed from: c, reason: collision with root package name */
    boolean f8774c;

    /* renamed from: d, reason: collision with root package name */
    long f8775d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C1303al f8776e;

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8772a) {
            try {
                this.f8772a = false;
                Thread.sleep(this.f8775d);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
                return;
            }
        }
        this.f8776e.f8875D = null;
        if (this.f8773b) {
            this.f8773b = false;
            this.f8776e.h();
        }
        if (this.f8774c) {
            this.f8774c = false;
            this.f8776e.h();
        }
    }
}
