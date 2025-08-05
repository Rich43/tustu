package bH;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bH.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/n.class */
final class C1006n extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ V f7056a;

    C1006n(V v2) {
        this.f7056a = v2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        long jCurrentTimeMillis = System.currentTimeMillis() + 3000;
        while (!this.f7056a.a() && jCurrentTimeMillis > System.currentTimeMillis()) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C1005m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        C1005m.f7055b = System.currentTimeMillis();
        C1005m.f7054a = this.f7056a.a();
    }
}
