package A;

import G.C0106cl;
import G.C0129l;
import G.J;
import bH.C;
import bH.I;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:A/u.class */
class u extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f69a;

    /* renamed from: b, reason: collision with root package name */
    long f70b;

    /* renamed from: c, reason: collision with root package name */
    int f71c;

    /* renamed from: d, reason: collision with root package name */
    int f72d;

    /* renamed from: e, reason: collision with root package name */
    int f73e;

    /* renamed from: l, reason: collision with root package name */
    private boolean f74l;

    /* renamed from: f, reason: collision with root package name */
    boolean f75f;

    /* renamed from: g, reason: collision with root package name */
    boolean f76g;

    /* renamed from: h, reason: collision with root package name */
    final Object f77h;

    /* renamed from: i, reason: collision with root package name */
    String f78i;

    /* renamed from: j, reason: collision with root package name */
    boolean f79j;

    /* renamed from: k, reason: collision with root package name */
    final /* synthetic */ t f80k;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public u(t tVar) {
        super("COMM Thread" + (Math.random() * 100000.0d));
        this.f80k = tVar;
        this.f69a = 0L;
        this.f70b = 30L;
        this.f71c = 0;
        this.f72d = 0;
        this.f73e = 60;
        this.f74l = false;
        this.f75f = true;
        this.f76g = !b();
        this.f77h = new Object();
        this.f78i = "";
        this.f79j = false;
        setDaemon(true);
        try {
            if (this.f76g) {
                setPriority(10);
            }
        } catch (Exception e2) {
            C.a("Failed to set Comm Thread Priority");
        }
        int i2 = t.f67o;
        t.f67o = i2 + 1;
        if (i2 > 1) {
            C.a("New Com Thread created: " + getName() + ", Multiple Comm Threads: " + t.f67o);
        } else {
            C.d("Com Thread created: " + getName());
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws Exception {
        HashMap map = new HashMap();
        t.f68al.put(this, this.f80k.a());
        C.d("Comm Manager for " + this.f80k.e().u() + " Started, thread ID: " + getName());
        if (this.f80k.e() != null) {
            this.f73e = this.f80k.e().t();
        }
        while (!this.f74l) {
            t.a(this.f80k, System.currentTimeMillis());
            try {
                boolean z2 = false;
                if (this.f80k.q() && this.f80k.j()) {
                    map.clear();
                    try {
                        yield();
                        if (!this.f74l && this.f80k.w()) {
                            this.f80k.y();
                        } else if (this.f74l || this.f80k.N()) {
                            this.f80k.c(200L);
                        } else {
                            try {
                                if (J.I() && this.f80k.f54f) {
                                    boolean unused = t.f430O = false;
                                    this.f80k.z();
                                    boolean unused2 = t.f430O = true;
                                } else {
                                    this.f80k.z();
                                }
                                this.f71c = 0;
                                this.f72d++;
                            } catch (Exception e2) {
                                this.f72d = 0;
                                if (J.I()) {
                                    C.c("Comm Exception Caught: " + e2.getMessage());
                                    Logger.getLogger(t.class.getName()).log(Level.WARNING, "Comm Timeout Caught", (Throwable) e2);
                                }
                                if ((e2 instanceof V.b) || (e2 instanceof IOException)) {
                                    this.f80k.b(this.f80k.e().u(), 1);
                                    if (!this.f80k.e().W() && !this.f80k.f55b.r()) {
                                        this.f71c = 10;
                                        C.d("Connection Check failed, forcing offline.");
                                    }
                                } else {
                                    this.f80k.b(this.f80k.e().u(), 2);
                                }
                                if (!(e2 instanceof V.d)) {
                                    Thread.sleep(this.f80k.e().i() / 2);
                                }
                                this.f80k.u();
                                this.f71c++;
                                if (0 != 0 || this.f71c > 1) {
                                    this.f80k.g();
                                    this.f80k.f421F = false;
                                    this.f80k.A();
                                    this.f80k.u();
                                    this.f80k.e().E(this.f80k.e().av());
                                    this.f80k.v();
                                    C.c("Went offline");
                                    this.f80k.f420E = false;
                                    this.f71c = 0;
                                    this.f80k.G();
                                    if (!this.f74l) {
                                        throw e2;
                                    }
                                    sleep(this.f80k.f55b.p());
                                }
                            }
                        }
                    } catch (Exception e3) {
                        this.f80k.f421F = false;
                        if (this.f80k.f418C.size() > 0) {
                            C.c("Writing the last " + this.f80k.f419D + " comm interactions with the controller to the log file here:");
                            this.f80k.G();
                        }
                        this.f80k.A();
                        this.f80k.f55b.g();
                        this.f80k.u();
                        this.f80k.v();
                        C.d("Went offline 2");
                        this.f80k.f420E = false;
                        if (t.f430O) {
                            Logger.getLogger(t.class.getName()).log(Level.INFO, "Inside read error", (Throwable) e3);
                        }
                        if (this.f80k.f55b != null) {
                            Thread.sleep(this.f80k.f55b.p());
                        } else if (!this.f74l) {
                            Thread.sleep(500L);
                        }
                    }
                } else {
                    try {
                        try {
                            if (J.I() || !this.f78i.equals(this.f80k.f55b.n())) {
                                this.f78i = this.f80k.f55b.n();
                                C.d("Connecting to controller on: " + this.f78i);
                            }
                            this.f80k.h();
                            this.f80k.e().i(false);
                            int iO = 250 + this.f80k.a().o();
                            this.f80k.d(this.f80k.e().i() / 2 > iO ? this.f80k.e().i() / 2 : iO);
                        } catch (C0129l e4) {
                            String str = "Port not valid: " + e4.getMessage();
                            if (map.get(str) == null) {
                                C.c(str);
                                map.put(str, str);
                            }
                            this.f80k.f420E = false;
                        } catch (Exception e5) {
                            if (J.I() && !(e5 instanceof V.b)) {
                                C.c("Error trying to go online: " + e5.getMessage());
                            }
                            if (I.d() ? !(e5 instanceof C0106cl) : ((e5 instanceof V.b) || (e5 instanceof C0106cl)) ? false : true) {
                                this.f80k.f55b.g();
                            }
                            if (e5 instanceof C0106cl) {
                                z2 = true;
                            }
                            this.f80k.f420E = false;
                        }
                    } catch (NullPointerException e6) {
                        if (J.I()) {
                            C.c("Error trying to go online, Connection seems bad, closing it: " + e6.getMessage());
                            Logger.getLogger(t.class.getName()).log(Level.INFO, e6.toString(), (Throwable) e6);
                        }
                        this.f80k.f420E = false;
                        this.f80k.f55b.g();
                    }
                    if (!this.f80k.f421F) {
                        if (this.f80k.f55b != null) {
                            if (!z2) {
                                this.f80k.h(this.f80k.f55b.n());
                            }
                            Thread.sleep(this.f80k.f55b.p());
                        } else if (!this.f74l) {
                            synchronized (this.f77h) {
                                this.f77h.wait(3000L);
                            }
                        }
                    }
                }
            } catch (Exception e7) {
                if (!(e7 instanceof V.b)) {
                    C.c("CommThread Exception:");
                    Logger.getLogger(t.class.getName()).log(Level.INFO, "Outside Read Error", (Throwable) e7);
                }
            }
        }
        t.f67o--;
        t.f68al.remove(this);
        if (this.f79j && this.f80k.f55b != null && !this.f80k.b(this.f80k.a())) {
            this.f80k.f55b.g();
        }
        C.d(this.f80k.e().u() + " ComThread stopped " + getName());
    }

    public boolean a() {
        return this.f74l;
    }

    public void a(boolean z2) {
        this.f74l = true;
        this.f79j = z2;
        synchronized (this.f77h) {
            this.f77h.notify();
        }
    }

    public boolean b() {
        return System.getProperty("os.name", "Windows").startsWith("Android");
    }
}
