package bQ;

import G.C0129l;
import bH.C;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bQ/n.class */
class n extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f7457a;

    /* renamed from: g, reason: collision with root package name */
    private boolean f7458g;

    /* renamed from: b, reason: collision with root package name */
    boolean f7459b;

    /* renamed from: c, reason: collision with root package name */
    int f7460c;

    /* renamed from: d, reason: collision with root package name */
    int f7461d;

    /* renamed from: e, reason: collision with root package name */
    boolean f7462e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ l f7463f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    n(l lVar) {
        super("XCP CommDriverConnect " + Math.random());
        this.f7463f = lVar;
        this.f7457a = !a();
        this.f7458g = false;
        this.f7459b = false;
        this.f7460c = 0;
        this.f7461d = 1000;
        this.f7462e = false;
        setDaemon(true);
        try {
            if (this.f7457a) {
                setPriority(10);
            }
        } catch (Exception e2) {
            C.a("Failed to set Comm Thread Priority");
        }
        int i2 = l.f7453am;
        l.f7453am = i2 + 1;
        if (i2 > 1) {
            C.a("New Com Thread created: " + getName() + ", Multiple Comm Threads: " + l.f7453am);
        } else {
            C.d("Com Thread created: " + getName());
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws Exception {
        long jCurrentTimeMillis = 0;
        HashMap map = new HashMap();
        while (!this.f7458g) {
            A.f fVarA = this.f7463f.a();
            if (fVarA == null) {
                this.f7463f.f("No ControllerInterface Set! Cannot go online.");
            } else if (fVarA.k() == 0) {
                try {
                    fVarA.f();
                } catch (C0129l e2) {
                    String str = "##################  Unable to connect to: " + fVarA.n() + " Will continue trying.";
                    if (map.get(str) == null) {
                        C.c(str);
                        map.put(str, str);
                    }
                    this.f7463f.c(fVarA.p());
                }
            }
            if (this.f7463f.f421F) {
                map.clear();
                try {
                    if (this.f7462e) {
                        this.f7463f.f();
                        this.f7462e = false;
                    } else if (!this.f7458g && this.f7463f.w()) {
                        this.f7463f.b("processing Instructions");
                        this.f7463f.y();
                        C.c("Processing, skip sync");
                        this.f7463f.f7454an = bN.k.v();
                    } else if (!this.f7458g) {
                        try {
                            long jCurrentTimeMillis2 = System.currentTimeMillis();
                            this.f7463f.b("Sending Sync");
                            if (bN.k.v() - this.f7463f.f7454an > 2.0f) {
                                C.c("WARNING!!!!! No Sync sent for: " + (bN.k.v() - this.f7463f.f7454an) + "s.");
                            }
                            this.f7463f.f7454an = bN.k.v();
                            this.f7463f.g();
                            long jCurrentTimeMillis3 = System.currentTimeMillis() - jCurrentTimeMillis2;
                            if (jCurrentTimeMillis3 > 2000) {
                                C.b("WARNING!!!!!!!! Time to sync in ms: " + jCurrentTimeMillis3);
                            }
                        } catch (Exception e3) {
                            if (System.currentTimeMillis() - this.f7463f.f7439d.k() > this.f7461d * 4) {
                                this.f7463f.b("Warning: Sync Error, raising exception: " + e3.getLocalizedMessage());
                                C.d("Connection Check failed, forcing offline.");
                                this.f7460c = 10;
                                throw e3;
                            }
                            this.f7463f.b("Sync Error, Ignoring received data " + ((int) (System.currentTimeMillis() - this.f7463f.f7439d.k())) + "ms ago. Error: " + e3.getLocalizedMessage());
                            C.c("Exception: " + e3.getMessage());
                        }
                    }
                    this.f7460c = 0;
                    if (!this.f7463f.w()) {
                        int iCurrentTimeMillis = this.f7461d - ((int) (System.currentTimeMillis() - jCurrentTimeMillis));
                        if (iCurrentTimeMillis > this.f7461d) {
                            iCurrentTimeMillis = this.f7461d;
                        }
                        if (iCurrentTimeMillis > 0) {
                            this.f7463f.c(this.f7461d);
                        } else {
                            C.c("Skipping Nap.");
                        }
                        jCurrentTimeMillis = System.currentTimeMillis();
                    }
                } catch (Exception e4) {
                    this.f7460c = 0;
                    this.f7463f.f421F = false;
                    this.f7463f.f422G = true;
                    if (this.f7463f.f418C.size() > 0) {
                        C.c("Writing the last " + this.f7463f.f419D + " comm interactions with the controller to the log file here:");
                        this.f7463f.G();
                    }
                    this.f7463f.A();
                    this.f7463f.f7435ao.g();
                    this.f7463f.u();
                    this.f7463f.v();
                    C.d("Went offline 2");
                    this.f7463f.f420E = false;
                    if (l.f430O) {
                        Logger.getLogger(l.class.getName()).log(Level.INFO, "Inside read error", (Throwable) e4);
                    }
                    if (this.f7463f.f7435ao != null) {
                        this.f7463f.c(this.f7463f.f7435ao.p());
                    } else if (!this.f7458g) {
                        this.f7463f.c(500L);
                    }
                }
            } else if (fVarA.k() == 3) {
                try {
                    if (this.f7463f.d(200)) {
                    }
                } catch (V.b e5) {
                    if (fVarA.k() == 3) {
                        C.d("Connected, but timing out. Box on? " + e5.getLocalizedMessage());
                        fVarA.g();
                    }
                } catch (IOException e6) {
                    C.d("IO Exception, closing connection.");
                    try {
                        fVarA.g();
                    } catch (Exception e7) {
                    }
                }
                this.f7463f.c(fVarA.p());
            } else {
                if (map.get("###############    Not Connected, not going online.") == null) {
                    C.d("###############    Not Connected, not going online.");
                    map.put("###############    Not Connected, not going online.", "###############    Not Connected, not going online.");
                }
                this.f7463f.c(fVarA.p());
            }
        }
        l.f7453am--;
        l.f7455ap.remove(this);
        if (this.f7459b && this.f7463f.a() != null && !this.f7463f.b(this.f7463f.a())) {
            this.f7463f.f7435ao.g();
        }
        C.d(this.f7463f.e().u() + " ComThread stopped " + getName());
    }

    public boolean a() {
        return System.getProperty("os.name", "Windows").startsWith("Android");
    }

    public void a(boolean z2) {
        long jCurrentTimeMillis = System.currentTimeMillis() + 1000;
        while (this.f7462e && System.currentTimeMillis() < jCurrentTimeMillis && this.f7463f.f7435ao != null && this.f7463f.f7435ao.k() == 3) {
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e2) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        this.f7458g = true;
        this.f7459b = z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        this.f7462e = true;
    }
}
