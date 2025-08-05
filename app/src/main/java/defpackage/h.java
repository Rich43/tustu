package defpackage;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:h.class */
final class h extends Thread {
    h() {
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        new i(this).start();
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() - jCurrentTimeMillis < 60000) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(TunerStudio.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        Runtime.getRuntime().halt(0);
    }
}
