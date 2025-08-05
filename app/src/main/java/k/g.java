package K;

import bH.C;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:K/g.class */
class g extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f1508a;

    /* renamed from: b, reason: collision with root package name */
    int f1509b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ f f1510c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    g(f fVar) {
        super("Timed Page read: " + fVar.f1505c);
        this.f1510c = fVar;
        this.f1508a = true;
        this.f1509b = 0;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() throws IOException {
        while (this.f1508a) {
            try {
                int iA = (int) this.f1510c.f1506d.a();
                if (iA > 0) {
                    wait(iA);
                }
            } catch (Exception e2) {
                try {
                    sleep(1000L);
                } catch (InterruptedException e3) {
                }
                if (this.f1509b >= 3) {
                    C.b("TimedRefresh: Too many errors, quitting.");
                    this.f1508a = false;
                }
                this.f1509b++;
            }
            this.f1510c.b();
        }
    }

    public void a() {
        this.f1508a = false;
    }
}
