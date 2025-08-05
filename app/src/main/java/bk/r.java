package bk;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bk/r.class */
class r extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f8234a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ q f8235b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    r(q qVar) {
        super("Toggle Blink");
        this.f8235b = qVar;
        this.f8234a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8234a) {
            this.f8235b.setOpaque(!this.f8235b.isOpaque());
            this.f8235b.repaint();
            try {
                Thread.sleep(750L);
            } catch (InterruptedException e2) {
                Logger.getLogger(q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        this.f8235b.setOpaque(false);
        this.f8235b.repaint();
    }
}
