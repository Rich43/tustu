package com.efiAnalytics.tuningwidgets.panels;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/T.class */
class T extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ S f10307a;

    T(S s2) {
        this.f10307a = s2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e2) {
            Logger.getLogger(Q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        SwingUtilities.invokeLater(this.f10307a.f10295a.f10293h);
    }
}
