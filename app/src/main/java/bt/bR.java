package bt;

/* loaded from: TunerStudioMS.jar:bt/bR.class */
class bR extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f8946a;

    /* renamed from: b, reason: collision with root package name */
    boolean f8947b;

    /* renamed from: c, reason: collision with root package name */
    boolean f8948c;

    /* renamed from: d, reason: collision with root package name */
    boolean f8949d;

    /* renamed from: e, reason: collision with root package name */
    long f8950e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ bQ f8951f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    bR(bQ bQVar) {
        super("zValThrottle");
        this.f8951f = bQVar;
        this.f8946a = true;
        this.f8947b = false;
        this.f8948c = false;
        this.f8949d = false;
        this.f8950e = 150L;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8946a) {
            try {
                this.f8946a = false;
                Thread.sleep(this.f8950e);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
                return;
            }
        }
        this.f8951f.f8942f = null;
        if (this.f8947b) {
            this.f8947b = false;
            this.f8951f.g();
        }
        if (this.f8948c) {
            this.f8948c = false;
            this.f8951f.c();
        }
        if (this.f8949d) {
            this.f8949d = false;
            this.f8951f.b();
        }
    }

    public void a() {
        this.f8947b = true;
        d();
    }

    public void b() {
        this.f8948c = true;
        d();
    }

    public void c() {
        this.f8949d = true;
        d();
    }

    private void d() {
        this.f8946a = true;
    }
}
