package G;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: G.ao, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ao.class */
class C0055ao extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0054an f799a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0055ao(C0054an c0054an, String str) {
        super(str);
        this.f799a = c0054an;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException {
        try {
            Thread.currentThread();
            Thread.sleep(1500L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0054an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f799a.h();
    }
}
