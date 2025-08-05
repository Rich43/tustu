package com.efiAnalytics.apps.ts.dashboard;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/X.class */
class X extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f9420a = 0;

    /* renamed from: b, reason: collision with root package name */
    long f9421b = 50;

    /* renamed from: c, reason: collision with root package name */
    boolean f9422c = false;

    /* renamed from: d, reason: collision with root package name */
    boolean f9423d = true;

    /* renamed from: e, reason: collision with root package name */
    long f9424e = -1;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ C1425x f9425f;

    public X(C1425x c1425x) {
        this.f9425f = c1425x;
        setName("GaugeCluster PaintThrottle");
        setDaemon(true);
    }

    public void a() {
        if (this.f9425f.L() || this.f9421b <= 10) {
            return;
        }
        if (this.f9421b > 150) {
            this.f9421b -= 30;
            return;
        }
        if (this.f9421b < 16) {
            this.f9421b = 10L;
            return;
        }
        if (this.f9421b < 20) {
            this.f9421b = 15L;
        } else if (this.f9421b < 23) {
            this.f9421b = 19L;
        } else {
            this.f9421b -= 8;
        }
    }

    public void b() {
        if (this.f9421b < 250) {
            this.f9421b += 3;
        }
    }

    public void c() {
        this.f9422c = true;
    }

    private void d() {
        if (this.f9425f.z() && this.f9425f.isShowing()) {
            this.f9425f.repaint();
            this.f9422c = false;
            this.f9420a = System.currentTimeMillis();
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f9423d) {
            try {
                synchronized (this) {
                    if (this.f9425f.f9570an != null) {
                        wait(this.f9421b / 2);
                    } else {
                        wait(this.f9421b);
                    }
                }
                if (this.f9423d && this.f9422c) {
                    d();
                } else if (this.f9424e > 0 && this.f9424e < System.currentTimeMillis()) {
                    bH.C.c("Calling followup Paint");
                    this.f9424e = -1L;
                    this.f9425f.f9558aj = null;
                    this.f9425f.f9560al = null;
                    d();
                }
            } catch (Exception e2) {
                System.out.println("PaintThrottle Thread died");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        this.f9423d = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(long j2) {
        this.f9424e = j2;
    }
}
