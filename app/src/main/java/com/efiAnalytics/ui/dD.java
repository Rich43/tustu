package com.efiAnalytics.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dD.class */
public class dD extends Thread {

    /* renamed from: d, reason: collision with root package name */
    private int f11313d;

    /* renamed from: e, reason: collision with root package name */
    private int f11314e;

    /* renamed from: a, reason: collision with root package name */
    boolean f11315a;

    /* renamed from: f, reason: collision with root package name */
    private Component f11316f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f11317g;

    /* renamed from: b, reason: collision with root package name */
    List f11318b;

    /* renamed from: c, reason: collision with root package name */
    Object f11319c;

    public dD(Component component) {
        super("Generic Paint Throttle");
        this.f11313d = 100;
        this.f11314e = 0;
        this.f11315a = false;
        this.f11316f = null;
        this.f11317g = true;
        this.f11318b = new ArrayList();
        this.f11319c = new Object();
        setDaemon(true);
        this.f11316f = component;
    }

    public void a(dC dCVar) {
        this.f11318b.add(dCVar);
    }

    private void c() {
        Iterator it = this.f11318b.iterator();
        while (it.hasNext()) {
            ((dC) it.next()).a();
        }
    }

    public void a() {
        if (this.f11315a) {
            return;
        }
        this.f11315a = true;
        if (b() && isAlive()) {
            return;
        }
        try {
            start();
        } catch (Exception e2) {
            bH.C.a("Failed to start PaintThrottle Thread: " + e2.getLocalizedMessage());
        }
    }

    public void a(int i2) {
        this.f11314e = i2;
        a();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f11317g) {
            try {
                synchronized (this.f11319c) {
                    this.f11319c.wait(this.f11313d);
                    if (this.f11314e > 0) {
                        this.f11319c.wait(this.f11314e);
                        this.f11314e = 0;
                    }
                }
            } catch (InterruptedException e2) {
            }
            if (this.f11315a) {
                c();
                this.f11315a = false;
                this.f11316f.repaint();
            }
        }
    }

    public void b(int i2) {
        this.f11313d = i2;
    }

    public boolean b() {
        return this.f11317g;
    }

    public void a(boolean z2) {
        this.f11317g = z2;
    }
}
