package q;

import bH.C;
import bt.C1273A;
import bt.C1366y;
import bt.aZ;
import bt.bI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/* loaded from: TunerStudioMS.jar:q/g.class */
final class g implements Runnable {
    g() {
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            h.a().a(JLabel.class, 75);
            h.a().a(C1273A.class, 50);
            h.a().a(C1366y.class, 35);
            h.a().a(aZ.class, 120);
            h.a().a(bI.class, 20);
        } catch (Exception e2) {
            C.a("Seed UI Cache failed!");
            Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
