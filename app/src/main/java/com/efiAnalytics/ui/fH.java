package com.efiAnalytics.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fH.class */
public class fH extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f11636a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ fG f11637b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    fH(fG fGVar) {
        super("WaitBar");
        this.f11637b = fGVar;
        this.f11636a = true;
        setDaemon(true);
    }

    public void a() {
        this.f11636a = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f11636a) {
            try {
                Thread.sleep(25L);
            } catch (InterruptedException e2) {
                Logger.getLogger(fG.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            this.f11637b.f11634c += 4;
            this.f11637b.repaint();
        }
    }
}
