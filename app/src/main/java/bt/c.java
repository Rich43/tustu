package bT;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bT/c.class */
class c extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f7563a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ b f7564b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    c(b bVar) {
        super("DAQ List Slave Processor");
        this.f7564b = bVar;
        this.f7563a = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (this.f7563a) {
            try {
                wait();
                this.f7564b.e();
            } catch (InterruptedException e2) {
                Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public synchronized void a() {
        notify();
    }

    public synchronized void b() {
        this.f7563a = false;
        notify();
    }
}
