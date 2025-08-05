package bt;

/* loaded from: TunerStudioMS.jar:bt/T.class */
class T extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f8706a;

    /* renamed from: b, reason: collision with root package name */
    boolean f8707b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1290R f8708c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public T(C1290R c1290r) {
        super("BinTablePaintThrottle");
        this.f8708c = c1290r;
        this.f8706a = Math.round(1000.0d / C1365x.a());
        this.f8707b = false;
        setDaemon(true);
        start();
    }

    public void a() {
        if (!isAlive()) {
            start();
        }
        this.f8707b = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.currentThread();
            Thread.sleep(2 * this.f8706a);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        while (this.f8708c.isDisplayable()) {
            try {
                Thread.currentThread();
                Thread.sleep(this.f8706a);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            if (this.f8707b) {
                this.f8708c.repaint();
                this.f8707b = false;
            }
        }
        this.f8708c.f8701e = null;
    }
}
