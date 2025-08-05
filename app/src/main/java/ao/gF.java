package ao;

import java.awt.Component;

/* loaded from: TunerStudioMS.jar:ao/gF.class */
class gF extends Thread {

    /* renamed from: c, reason: collision with root package name */
    private int f5898c;

    /* renamed from: d, reason: collision with root package name */
    private Component f5899d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f5900e;

    /* renamed from: a, reason: collision with root package name */
    long f5901a;

    /* renamed from: f, reason: collision with root package name */
    private long f5902f;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ fX f5903b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public gF(fX fXVar, Component component) {
        super("LazyPaintThrottle");
        this.f5903b = fXVar;
        this.f5898c = 100;
        this.f5899d = null;
        this.f5900e = true;
        this.f5901a = Long.MAX_VALUE;
        this.f5902f = 30L;
        setDaemon(true);
        this.f5899d = component;
    }

    public void a() {
        this.f5901a = System.currentTimeMillis() + this.f5898c;
        if (b() && isAlive()) {
            return;
        }
        start();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f5900e) {
            try {
                Thread.sleep(c());
            } catch (InterruptedException e2) {
            }
            if (System.currentTimeMillis() >= this.f5901a) {
                this.f5901a = Long.MAX_VALUE;
                try {
                    this.f5903b.p();
                } catch (Exception e3) {
                    bH.C.a(e3);
                }
            }
        }
    }

    public void a(int i2) {
        this.f5898c = i2;
    }

    public boolean b() {
        return this.f5900e;
    }

    public long c() {
        return this.f5902f;
    }
}
