package bQ;

import G.C0129l;
import bH.C;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bQ/b.class */
class b implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f7390a;

    b(a aVar) {
        this.f7390a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        int i2;
        try {
            this.f7390a.f7383a.m();
        } catch (Exception e2) {
            C.b("Reset Comms failed to stop DAQ");
        }
        try {
            this.f7390a.f7383a.g();
        } catch (C0129l e3) {
            Logger.getLogger(a.class.getName()).log(Level.WARNING, "Error on SYNC during Comms Reset!", (Throwable) e3);
        } catch (V.b e4) {
            C.b("Timeout on SYNC during Comms Reset!");
        } catch (bN.o e5) {
            Logger.getLogger(a.class.getName()).log(Level.WARNING, "Error on SYNC during Comms Reset!", (Throwable) e5);
        }
        int i3 = 0;
        boolean z2 = false;
        do {
            try {
                this.f7390a.f7383a.l();
                z2 = true;
            } catch (Exception e6) {
                C.a("Failed to restart the DAQ, going offline.");
            }
            if (z2) {
                break;
            }
            i2 = i3;
            i3++;
        } while (i2 < 4);
        if (i3 >= 4) {
            try {
                C.a("CommHealthMonitor: Failed to restart DAQ after 4 attempts, closing connection.");
                this.f7390a.f7383a.a().g();
            } catch (Exception e7) {
            }
        }
    }
}
