package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/e.class */
class e extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f9853a = true;

    /* renamed from: b, reason: collision with root package name */
    long f9854b = -1;

    /* renamed from: c, reason: collision with root package name */
    int f9855c = 100;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ BurnButtonTv f9856d;

    e(BurnButtonTv burnButtonTv) {
        this.f9856d = burnButtonTv;
    }

    @Override // java.lang.Thread
    public void start() {
        this.f9854b = System.currentTimeMillis();
        super.start();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            if (!this.f9856d.f9818i && System.currentTimeMillis() >= this.f9854b + this.f9855c) {
                this.f9853a = false;
                SwingUtilities.invokeLater(this.f9856d.f9819j);
                return;
            } else {
                try {
                    Thread.sleep(this.f9855c / 2);
                } catch (InterruptedException e2) {
                    Logger.getLogger(BurnButtonTv.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        }
    }

    public void a() {
        this.f9854b = System.currentTimeMillis();
    }
}
