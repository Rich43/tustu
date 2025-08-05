package aW;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aW/h.class */
class h extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f3982a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ e f3983b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    h(e eVar) {
        super("IS Blink");
        this.f3983b = eVar;
        this.f3982a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (this.f3982a) {
            Color background = this.f3983b.f3975c.getBackground();
            this.f3983b.f3975c.setBackground(Color.YELLOW);
            try {
                wait(500L);
            } catch (InterruptedException e2) {
                Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            this.f3983b.f3975c.setBackground(background);
            try {
                if (this.f3982a) {
                    wait(800L);
                }
            } catch (InterruptedException e3) {
                Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }

    public synchronized void a() {
        this.f3982a = false;
        notify();
    }
}
