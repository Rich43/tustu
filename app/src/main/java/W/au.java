package W;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:W/au.class */
class au extends Thread {

    /* renamed from: c, reason: collision with root package name */
    private boolean f2109c;

    /* renamed from: a, reason: collision with root package name */
    int f2110a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ as f2111b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    au(as asVar) {
        super("Queue Write");
        this.f2111b = asVar;
        this.f2109c = true;
        this.f2110a = 0;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (b()) {
            while (!this.f2111b.f2102b.isEmpty()) {
                try {
                    synchronized (this.f2111b.f2102b) {
                        at atVar = (at) this.f2111b.f2102b.remove(0);
                        if (atVar.c()) {
                            this.f2111b.f2101a.write(atVar.b());
                            this.f2111b.f2101a.flush();
                        } else {
                            this.f2111b.f2101a.write(atVar.a());
                        }
                    }
                } catch (IOException e2) {
                    this.f2111b.f2104d = e2;
                    this.f2109c = false;
                } catch (Exception e3) {
                    Logger.getLogger(as.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
            try {
                if (this.f2111b.f2101a != null) {
                    this.f2111b.f2101a.flush();
                }
            } catch (Exception e4) {
                this.f2111b.f2102b.clear();
                int i2 = this.f2110a;
                this.f2110a = i2 + 1;
                if (i2 > 5) {
                    bH.C.d("Queued Log Writer max error count exceeded, letting die.");
                    this.f2109c = false;
                }
            }
            try {
                wait(500L);
            } catch (InterruptedException e5) {
                Logger.getLogger(as.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
            }
        }
    }

    public synchronized void a() {
        notify();
    }

    public boolean b() {
        return this.f2109c;
    }

    public void a(boolean z2) {
        this.f2109c = z2;
    }
}
