package z;

import G.J;
import bH.C;
import gnu.io.RXTXCommDriver;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.tftp.TFTP;

/* loaded from: TunerStudioMS.jar:z/g.class */
class g extends Thread {

    /* renamed from: a, reason: collision with root package name */
    J f14113a;

    /* renamed from: b, reason: collision with root package name */
    boolean f14114b;

    /* renamed from: d, reason: collision with root package name */
    private boolean f14115d;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1901e f14116c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(C1901e c1901e, J j2) {
        super("COMM Watchdog");
        this.f14116c = c1901e;
        this.f14113a = null;
        this.f14114b = true;
        this.f14115d = false;
        this.f14113a = j2;
    }

    @Override // java.lang.Thread
    public void start() {
        this.f14114b = true;
        super.start();
    }

    public void a() {
        this.f14114b = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean z2 = false;
        while (this.f14114b) {
            try {
                try {
                    if (!this.f14115d && (z2 || (this.f14116c.q() && System.currentTimeMillis() - this.f14116c.f432Q > 2500))) {
                        C.b("Comm Thread not heard from in " + ((System.currentTimeMillis() - this.f14116c.f432Q) / 1000.0d) + " seconds, Reporting Thread where about.");
                        if (this.f14116c.f14095e != null) {
                            C.c("Watchdog Report on Comm Thread. the following stack reports the current Location, not killing yet");
                            a("comm", this.f14116c.f14095e);
                        }
                    }
                    int i2 = this.f14116c.e() != null ? this.f14116c.e().i() : 0;
                    long jCurrentTimeMillis = System.currentTimeMillis() - this.f14116c.f432Q;
                    long jCurrentTimeMillis2 = this.f14116c.f433R - System.currentTimeMillis();
                    if (!this.f14115d && (z2 || ((this.f14116c.q() && jCurrentTimeMillis > TFTP.DEFAULT_TIMEOUT + i2) || (this.f14116c.q() && jCurrentTimeMillis2 < 0)))) {
                        C.b("Comm Thread not heard from in " + ((System.currentTimeMillis() - this.f14116c.f432Q) / 1000.0d) + " seconds, resetting. Expected to hear from Comm Thread in: " + jCurrentTimeMillis2 + "ms. , try again=" + z2);
                        C1901e.b(this.f14116c, System.currentTimeMillis());
                        long jCurrentTimeMillis3 = System.currentTimeMillis();
                        C.c("Declaring Comm Thread stale, begining reinitialization");
                        if (this.f14116c.f14095e != null) {
                            C.c("Watchdog Report on Comm Thread. the following stack reports the current Location");
                            a("comm", this.f14116c.f14095e);
                        }
                        if (this.f14116c.f14092b != null) {
                            try {
                                C.c("Watchdog closing port");
                                this.f14116c.c();
                                C.c("Watchdog closed port, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis3));
                            } catch (Exception e2) {
                                C.d("Exception caught while trying to clean up port lock");
                                Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                            }
                        }
                        C.c("New Driver instance, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis3));
                        C1901e.f14093c = new RXTXCommDriver();
                        C.c("Watchdog re-initializing driver, Time:" + (System.currentTimeMillis() - jCurrentTimeMillis3));
                        C1901e.f14093c.initialize();
                        C.c("Watchdog calling goOffline(), Time:" + (System.currentTimeMillis() - jCurrentTimeMillis3));
                        this.f14113a.c();
                        Thread.sleep(500L);
                        C.c("Watchdog calling goOnline(), Time:" + (System.currentTimeMillis() - jCurrentTimeMillis3));
                        this.f14113a.d();
                        C.c("Watchdog succeeded. Time:" + (System.currentTimeMillis() - jCurrentTimeMillis3));
                        z2 = false;
                        sleep(10000L);
                        C1901e.c(this.f14116c, System.currentTimeMillis());
                    }
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e3) {
                        Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                } catch (Throwable th) {
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e4) {
                        Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    }
                    throw th;
                }
            } catch (Exception e5) {
                C.b("CommWatchDog failed to restart " + this.f14116c.e().u() + " on port " + this.f14116c.e().s() + ", will try again.");
                Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                z2 = true;
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e6) {
                    Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                }
            }
        }
    }

    public boolean b() {
        return this.f14115d;
    }

    public void a(boolean z2) {
        this.f14115d = z2;
    }

    void a(String str, Thread thread) {
        try {
            StackTraceElement[] stackTrace = thread.getStackTrace();
            Exception exc = new Exception("Stack for " + str + " Thread:");
            exc.setStackTrace(stackTrace);
            Logger.getLogger(C1901e.class.getName()).log(Level.INFO, (String) null, (Throwable) exc);
        } catch (Exception e2) {
            C.b("Failed to dump stack of " + str + " thread.");
        }
    }
}
