package bc;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bc.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/c.class */
class C1056c extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f7867a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1054a f7868b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1056c(C1054a c1054a) {
        super("LoaderOption Blink");
        this.f7868b = c1054a;
        this.f7867a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (this.f7867a) {
            Color background = this.f7868b.f7863c.getBackground();
            this.f7868b.f7863c.setBackground(Color.YELLOW);
            try {
                wait(500L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C1056c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            this.f7868b.f7863c.setBackground(background);
            try {
                if (this.f7867a) {
                    wait(800L);
                }
            } catch (InterruptedException e3) {
                Logger.getLogger(C1054a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }

    public synchronized void a() {
        this.f7867a = false;
        notify();
    }
}
