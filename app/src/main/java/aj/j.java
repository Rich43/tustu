package aj;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aj/j.class */
class j extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f4564a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ d f4565b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    j(d dVar) {
        super("SendCaller");
        this.f4565b = dVar;
        this.f4564a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() throws NumberFormatException, IOException {
        while (this.f4564a) {
            try {
                wait();
                if (this.f4564a) {
                    this.f4565b.e();
                } else {
                    this.f4565b.k();
                }
            } catch (InterruptedException e2) {
                Logger.getLogger(d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public synchronized void a() {
        notify();
    }

    public synchronized void b() {
        this.f4564a = false;
        notify();
    }
}
