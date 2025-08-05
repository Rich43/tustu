package com.efiAnalytics.tuningwidgets.panels;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/q.class */
class C1519q extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Runnable f10497a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1516n f10498b;

    C1519q(C1516n c1516n, Runnable runnable) {
        this.f10498b = c1516n;
        this.f10497a = runnable;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C1516n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        SwingUtilities.invokeLater(this.f10497a);
        bH.C.c("updated");
    }
}
