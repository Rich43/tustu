package com.efiAnalytics.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPasswordField;

/* renamed from: com.efiAnalytics.ui.cf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cf.class */
final class C1595cf extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JPasswordField f11242a;

    C1595cf(JPasswordField jPasswordField) {
        this.f11242a = jPasswordField;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            sleep(300L);
        } catch (InterruptedException e2) {
            Logger.getLogger(bV.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        bH.C.c("Password Focus: " + this.f11242a.requestFocusInWindow());
    }
}
