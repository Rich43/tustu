package av;

/* renamed from: av.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/k.class */
class C0872k extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f6296a = true;

    /* renamed from: b, reason: collision with root package name */
    boolean f6297b = false;

    /* renamed from: c, reason: collision with root package name */
    boolean f6298c = false;

    /* renamed from: d, reason: collision with root package name */
    boolean f6299d = false;

    /* renamed from: e, reason: collision with root package name */
    long f6300e = 250;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ C0871j f6301f;

    C0872k(C0871j c0871j) {
        this.f6301f = c0871j;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f6296a) {
            try {
                this.f6296a = false;
                Thread.sleep(this.f6300e);
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        this.f6301f.f6294a = null;
        if (this.f6297b) {
            this.f6297b = false;
            this.f6301f.g();
        }
        if (this.f6298c) {
            this.f6298c = false;
            this.f6301f.c();
        }
        if (this.f6299d) {
            this.f6299d = false;
            this.f6301f.b();
        }
    }

    public void a() {
        this.f6297b = true;
        d();
    }

    public void b() {
        this.f6298c = true;
        d();
    }

    public void c() {
        this.f6299d = true;
        d();
    }

    private void d() {
        this.f6296a = true;
    }
}
