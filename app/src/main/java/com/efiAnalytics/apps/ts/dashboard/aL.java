package com.efiAnalytics.apps.ts.dashboard;

import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aL.class */
class aL extends Thread implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    boolean f9449a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ Indicator f9450b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aL(Indicator indicator, boolean z2) {
        super("Indicator Demo");
        this.f9450b = indicator;
        this.f9449a = false;
        this.f9449a = z2;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        do {
            try {
                if (this.f9449a) {
                    this.f9450b.setValue(1.0d);
                    this.f9450b.callRepaint();
                }
                Thread.sleep(2000L);
                this.f9450b.setValue(0.0d);
                if (this.f9449a) {
                    Thread.sleep(1000L);
                }
                this.f9450b.callRepaint();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        } while (this.f9449a);
        this.f9450b.f9365p = null;
    }
}
