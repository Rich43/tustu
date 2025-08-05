package i;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: i.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:i/g.class */
class C1747g extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f12356a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1746f f12357b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1747g(C1746f c1746f) {
        super("MLV_PipeMonitor");
        this.f12357b = c1746f;
        this.f12356a = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (this.f12356a) {
            try {
                wait(C1746f.f12349a);
            } catch (InterruptedException e2) {
                Logger.getLogger(C1746f.class.getName()).log(Level.SEVERE, "Shouldn't happen.", (Throwable) e2);
            }
            if (!this.f12357b.f12355g) {
                this.f12357b.e();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f12356a = false;
    }
}
