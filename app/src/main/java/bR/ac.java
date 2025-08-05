package br;

/* loaded from: TunerStudioMS.jar:br/ac.class */
class ac extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f8409a;

    /* renamed from: b, reason: collision with root package name */
    boolean f8410b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ P f8411c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ac(P p2, int i2) {
        super("VE ApplyThread");
        this.f8411c = p2;
        this.f8409a = -1;
        this.f8410b = true;
        this.f8409a = i2;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8410b) {
            try {
                Thread.sleep(this.f8409a * 1000);
                this.f8411c.a();
            } catch (Exception e2) {
                bH.C.c("Exception in the apply Thread, handled but here is the stack:");
                e2.printStackTrace();
            }
        }
        while (((ag) this.f8411c.f8389p.get(0)).f8425a.b()) {
            this.f8411c.a();
        }
    }

    public void a() {
        this.f8410b = false;
    }
}
