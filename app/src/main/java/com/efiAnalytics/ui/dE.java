package com.efiAnalytics.ui;

import java.awt.Component;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dE.class */
public class dE extends Thread {

    /* renamed from: b, reason: collision with root package name */
    private int f11320b;

    /* renamed from: c, reason: collision with root package name */
    private Component f11321c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11322d;

    /* renamed from: a, reason: collision with root package name */
    long f11323a;

    /* renamed from: e, reason: collision with root package name */
    private long f11324e;

    public dE(Component component) {
        super("LazyPaintThrottle");
        this.f11320b = 100;
        this.f11321c = null;
        this.f11322d = true;
        this.f11323a = Long.MAX_VALUE;
        this.f11324e = 50L;
        setDaemon(true);
        this.f11321c = component;
    }

    public void a() {
        this.f11323a = System.currentTimeMillis() + this.f11320b;
        if (b() && isAlive()) {
            return;
        }
        start();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f11322d) {
            try {
                Thread.sleep(c());
            } catch (InterruptedException e2) {
            }
            if (System.currentTimeMillis() >= this.f11323a) {
                this.f11323a = Long.MAX_VALUE;
                this.f11321c.repaint();
            }
        }
    }

    public void a(int i2) {
        this.f11320b = i2;
    }

    public boolean b() {
        return this.f11322d;
    }

    public long c() {
        return this.f11324e;
    }
}
