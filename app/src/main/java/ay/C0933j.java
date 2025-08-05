package ay;

import bH.C;
import bH.C1005m;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ay.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/j.class */
class C0933j extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f6456a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0932i f6457b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0933j(C0932i c0932i) {
        super("Internet Monitor");
        this.f6457b = c0932i;
        this.f6456a = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean zA;
        while (this.f6456a) {
            if ((!this.f6457b.f6453d || this.f6457b.f6454e) && this.f6457b.f6453d != (zA = C1005m.a())) {
                this.f6457b.f6453d = zA;
                this.f6457b.b(this.f6457b.f6453d);
                C.d("Internet Available: " + this.f6457b.f6453d);
            }
            try {
                Thread.sleep(60000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0932i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void a() {
        this.f6456a = false;
    }
}
