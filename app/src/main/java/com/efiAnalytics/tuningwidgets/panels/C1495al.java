package com.efiAnalytics.tuningwidgets.panels;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.al, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/al.class */
class C1495al extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1488ae f10404a;

    C1495al(C1488ae c1488ae) {
        this.f10404a = c1488ae;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C1488ae.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f10404a.c();
        this.f10404a.f10391d.validate();
        this.f10404a.f10393f.validate();
        bH.C.c("updated");
    }
}
