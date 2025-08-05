package bt;

/* renamed from: bt.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/u.class */
class C1362u extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f9131a;

    /* renamed from: b, reason: collision with root package name */
    boolean f9132b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1360s f9133c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1362u(C1360s c1360s) {
        super("LiveGraph PaintThrottle");
        this.f9133c = c1360s;
        this.f9131a = 120L;
        this.f9132b = false;
        setDaemon(true);
    }

    public void a() {
        if (!isAlive()) {
            start();
        }
        this.f9132b = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.currentThread();
            Thread.sleep(2 * this.f9131a);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        while (this.f9133c.isDisplayable()) {
            try {
                Thread.currentThread();
                Thread.sleep(this.f9131a);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            if (this.f9132b) {
                this.f9133c.repaint();
                this.f9132b = false;
            }
        }
        this.f9133c.f9118a = null;
    }
}
