package com.efiAnalytics.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: com.efiAnalytics.ui.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/n.class */
class C1696n extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f11730a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1642e f11731b;

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (true) {
            try {
                wait(this.f11730a - System.currentTimeMillis());
                if (System.currentTimeMillis() >= this.f11730a) {
                    this.f11731b.a();
                    this.f11730a = Long.MAX_VALUE;
                    bH.C.c("Reset List");
                }
            } catch (InterruptedException e2) {
                Logger.getLogger(C1621de.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }
}
