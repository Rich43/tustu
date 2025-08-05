package bD;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bD/H.class */
class H extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f6630a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ G f6631b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    H(G g2) {
        super("Remote table wait");
        this.f6631b = g2;
        this.f6630a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f6630a) {
            this.f6631b.repaint();
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0957c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        this.f6631b.repaint();
    }
}
