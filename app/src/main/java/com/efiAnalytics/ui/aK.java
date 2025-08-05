package com.efiAnalytics.ui;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aK.class */
class aK extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f10743a;

    /* renamed from: b, reason: collision with root package name */
    long f10744b;

    /* renamed from: c, reason: collision with root package name */
    boolean f10745c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ BinTableView f10746d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aK(BinTableView binTableView) {
        super("BinTablePaintThrottle");
        this.f10746d = binTableView;
        this.f10743a = 0L;
        this.f10744b = Math.round(1000.0d / this.f10746d.f10670ad);
        this.f10745c = false;
        setDaemon(true);
        start();
    }

    public void a() {
        if (System.currentTimeMillis() - this.f10743a > this.f10744b) {
            b();
        } else {
            this.f10745c = true;
        }
    }

    private void b() {
        this.f10743a = System.currentTimeMillis();
        this.f10746d.repaint();
        this.f10745c = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(this.f10744b);
            if (this.f10745c) {
                b();
            }
        } catch (Exception e2) {
            System.out.println("PaintThrottle Thread died");
        }
    }
}
