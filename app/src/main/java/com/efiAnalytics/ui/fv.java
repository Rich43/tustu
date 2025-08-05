package com.efiAnalytics.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fv.class */
class fv extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1688fs f11715a;

    fv(C1688fs c1688fs) {
        this.f11715a = c1688fs;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(150L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C1688fs.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (this.f11715a.f11711k != null) {
            this.f11715a.f11711k.requestFocus();
        }
    }
}
