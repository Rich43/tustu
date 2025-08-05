package com.efiAnalytics.apps.ts.tuningViews;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/X.class */
class X extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f9764a = true;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ J f9765b;

    X(J j2) {
        this.f9765b = j2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f9764a) {
            this.f9765b.repaint();
            try {
                sleep(30L);
            } catch (InterruptedException e2) {
                Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void a() {
        this.f9764a = false;
    }
}
