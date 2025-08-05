package bt;

/* loaded from: TunerStudioMS.jar:bt/aN.class */
class aN extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f8765a;

    /* renamed from: b, reason: collision with root package name */
    boolean f8766b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1303al f8767c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    aN(C1303al c1303al) {
        super("CurvePaint");
        this.f8767c = c1303al;
        this.f8765a = Math.round(1000.0d / C1365x.a());
        this.f8766b = false;
        setDaemon(true);
    }

    public void a() {
        if (!isAlive()) {
            start();
        }
        this.f8766b = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.currentThread();
            Thread.sleep(2 * this.f8765a);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        while (this.f8767c.isDisplayable()) {
            try {
                Thread.currentThread();
                Thread.sleep(this.f8765a);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            if (this.f8766b) {
                this.f8767c.f8861p.repaint();
                this.f8766b = false;
            }
        }
        this.f8767c.f8872A = null;
    }
}
