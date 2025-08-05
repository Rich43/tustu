package bH;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* renamed from: bH.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/j.class */
class C1002j extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f7050a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1000h f7051b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1002j(C1000h c1000h) {
        super("DeadLockMonitor");
        this.f7051b = c1000h;
        this.f7050a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (this.f7050a) {
            try {
                this.f7051b.f7046a = false;
                SwingUtilities.invokeLater(this.f7051b.f7047b);
                wait(10000L);
                if (!this.f7051b.f7046a) {
                    this.f7051b.c();
                    wait(600000L);
                }
            } catch (InterruptedException e2) {
                Logger.getLogger(C1000h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f7050a = false;
    }
}
