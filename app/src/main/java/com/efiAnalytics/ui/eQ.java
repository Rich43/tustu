package com.efiAnalytics.ui;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eQ.class */
class eQ extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f11531a;

    /* renamed from: b, reason: collision with root package name */
    boolean f11532b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ eM f11533c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    eQ(eM eMVar) {
        super("3D Paint Throttle");
        this.f11533c = eMVar;
        this.f11531a = 120L;
        this.f11532b = false;
        setDaemon(true);
    }

    public void a() {
        if (!isAlive()) {
            start();
        }
        this.f11532b = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.currentThread();
            Thread.sleep(2 * this.f11531a);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        while (this.f11533c.isDisplayable()) {
            try {
                Thread.currentThread();
                Thread.sleep(this.f11531a);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            if (this.f11532b) {
                this.f11533c.K();
                this.f11532b = false;
            }
        }
        this.f11533c.f11519g = null;
    }
}
