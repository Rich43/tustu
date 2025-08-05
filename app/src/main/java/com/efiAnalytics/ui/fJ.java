package com.efiAnalytics.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fJ.class */
class fJ extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f11641a = true;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ fI f11642b;

    fJ(fI fIVar) {
        this.f11642b = fIVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f11641a) {
            this.f11642b.repaint();
            try {
                sleep(30L);
            } catch (InterruptedException e2) {
                Logger.getLogger(fI.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void a() {
        this.f11641a = false;
    }
}
