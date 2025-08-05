package z;

import G.C0129l;
import G.J;
import bH.C;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: z.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:z/f.class */
class C1902f extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f14106a;

    /* renamed from: b, reason: collision with root package name */
    long f14107b;

    /* renamed from: c, reason: collision with root package name */
    int f14108c;

    /* renamed from: d, reason: collision with root package name */
    int f14109d;

    /* renamed from: e, reason: collision with root package name */
    int f14110e;

    /* renamed from: g, reason: collision with root package name */
    private boolean f14111g;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ C1901e f14112f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1902f(C1901e c1901e) {
        super("COMM Thread" + (Math.random() * 100000.0d));
        this.f14112f = c1901e;
        this.f14106a = 0L;
        this.f14107b = 30L;
        this.f14108c = 0;
        this.f14109d = 0;
        this.f14110e = 60;
        this.f14111g = false;
        try {
            setPriority(10);
        } catch (Exception e2) {
            C.a("Failed to set Comm Thread Priority");
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws Exception {
        C.d("Comm Manager for " + this.f14112f.e().u() + " Started");
        if (this.f14112f.e() != null) {
            this.f14110e = this.f14112f.e().t();
        }
        while (!this.f14111g) {
            C1901e.a(this.f14112f, System.currentTimeMillis());
            try {
                if (!this.f14112f.f14098g) {
                    if (!this.f14112f.q() || this.f14112f.f14092b == null) {
                        this.f14112f.a(this.f14112f.e().r() + "");
                        this.f14112f.e().i(false);
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        int i2 = 0;
                        try {
                            try {
                                try {
                                    this.f14112f.o();
                                    if (this.f14112f.d(600)) {
                                        setPriority(10);
                                    }
                                } catch (V.b e2) {
                                    if (0 != 0 || J.I()) {
                                        C.c("Timeout trying to go online: " + e2.getMessage());
                                    }
                                    this.f14112f.f420E = false;
                                    i2 = 1000;
                                } catch (IOException e3) {
                                    C.c("Port turned not valid: " + e3.getMessage());
                                    this.f14112f.f420E = false;
                                    this.f14112f.R();
                                    i2 = 3000;
                                    this.f14112f.h();
                                }
                            } catch (C0129l e4) {
                                C.c("Port not valid: " + e4.getMessage());
                                this.f14112f.f420E = false;
                                this.f14112f.R();
                                i2 = 3000;
                            }
                        } catch (Exception e5) {
                            if (0 != 0 || J.I()) {
                                C.c("Error trying to go online: " + e5.getMessage());
                            }
                            this.f14112f.f420E = false;
                            i2 = 3000;
                        }
                        if (!this.f14112f.f421F && i2 > 0) {
                            long jCurrentTimeMillis2 = i2 - (System.currentTimeMillis() - jCurrentTimeMillis);
                            if (jCurrentTimeMillis2 > 0) {
                                Thread.sleep(jCurrentTimeMillis2);
                            }
                        }
                    } else {
                        try {
                            yield();
                            if (this.f14112f.w()) {
                                this.f14112f.y();
                            } else {
                                try {
                                    if (J.I() && this.f14112f.f14105n) {
                                        boolean unused = C1901e.f430O = false;
                                        this.f14112f.z();
                                        boolean unused2 = C1901e.f430O = true;
                                    } else {
                                        this.f14112f.z();
                                    }
                                    this.f14108c = 0;
                                    this.f14109d++;
                                } catch (Exception e6) {
                                    C.c("Exception Caught during comms, type:" + e6.getClass().getName() + ", message: " + e6.getMessage());
                                    Thread.sleep(this.f14112f.e().i() / 2);
                                    this.f14109d = 0;
                                    this.f14112f.u();
                                    this.f14108c++;
                                    boolean z2 = false;
                                    if ((e6 instanceof V.d) && this.f14112f.e().D() != null) {
                                        z2 = !this.f14112f.e().D().a(((V.d) e6).a());
                                    }
                                    if (0 != 0 || this.f14108c > 3 || this.f14112f.f14098g || z2) {
                                        this.f14112f.Q();
                                        this.f14112f.h();
                                        this.f14112f.f421F = false;
                                        this.f14112f.A();
                                        this.f14112f.u();
                                        this.f14112f.e().E(this.f14112f.e().av());
                                        this.f14112f.v();
                                        C.c("Went offline");
                                        this.f14112f.f420E = false;
                                        this.f14108c = 0;
                                        C.c("Writing the last " + this.f14112f.f419D + " comm interactions with the controller to the log file here:");
                                        this.f14112f.G();
                                        if (!this.f14111g) {
                                            throw e6;
                                        }
                                        sleep(1000L);
                                    }
                                }
                            }
                        } catch (Exception e7) {
                            this.f14112f.f421F = false;
                            if (this.f14112f.f418C.size() > 0) {
                                C.c("Writing the last " + this.f14112f.f419D + " comm interactions with the controller to the log file here:");
                                this.f14112f.G();
                            }
                            this.f14112f.A();
                            this.f14112f.u();
                            this.f14112f.v();
                            C.d("Went offline 2");
                            this.f14112f.f420E = false;
                            if (C1901e.f430O) {
                                Logger.getLogger(C1901e.class.getName()).log(Level.INFO, (String) null, (Throwable) e7);
                            }
                            if (e7 instanceof V.b) {
                                sleep(500L);
                            } else {
                                sleep(3000L);
                            }
                        }
                    }
                }
            } catch (Exception e8) {
                C.c("CommThread Exception:");
                Logger.getLogger(C1901e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
            }
        }
        C.c(this.f14112f.e().u() + " ComThread stopped");
    }

    public boolean a() {
        return this.f14111g;
    }

    public void b() {
        this.f14111g = true;
    }
}
