package aK;

import G.C0129l;
import G.J;
import bH.C;
import bH.Z;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aK/b.class */
class b extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f2557a = 0;

    /* renamed from: b, reason: collision with root package name */
    boolean f2558b = true;

    /* renamed from: c, reason: collision with root package name */
    h f2559c = null;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ a f2560d;

    public b(a aVar) {
        this.f2560d = aVar;
        if (a.f2550r != null) {
            C.a("Attempting to create second connection!");
        }
        setDaemon(true);
        b unused = a.f2550r = this;
        setName("GPSThread:" + System.currentTimeMillis());
    }

    public synchronized void a() {
        this.f2557a++;
        if (this.f2560d.f2552t.k() == 2) {
            C.d("Connecting to GPS Adapter, waiting...");
        } else if (this.f2560d.f2552t.k() == 0) {
            this.f2560d.f2552t.f();
            this.f2557a = 0;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f2558b = true;
            try {
                String strA = null;
                long j2 = 100;
                int i2 = 0;
                new Z().a();
                while (this.f2558b) {
                    if (this.f2560d.f2552t != null && this.f2560d.f2552t.k() == 3) {
                        try {
                            if (this.f2559c == null) {
                                this.f2559c = new h(this.f2560d.f2552t.i());
                            }
                            strA = this.f2559c.a();
                            this.f2560d.c(strA);
                            if (strA == null || strA.isEmpty()) {
                                C.a("Resetting GPS Connection");
                                c();
                                a();
                            } else {
                                this.f2560d.a(strA);
                            }
                            j2 = this.f2560d.f2545m == a.f2537b ? 5L : 20L;
                            i2 = 0;
                        } catch (IOException e2) {
                            int i3 = i2;
                            i2++;
                            if (i3 > 3) {
                                C.d("GPS Connection Error, resetting Connection.");
                                C.a(e2);
                                c();
                                j2 = 500;
                            } else {
                                j2 = 30;
                            }
                        } catch (Exception e3) {
                            if (J.I()) {
                                C.a(e3);
                                C.a("Failed Sentence: " + strA);
                            }
                            j2 = 500;
                        }
                    } else if (this.f2560d.f2552t != null) {
                        try {
                            if (this.f2557a == 0) {
                                try {
                                    synchronized (this.f2560d.f2555e) {
                                        this.f2560d.f2555e.wait(1000 + this.f2560d.f2552t.p());
                                    }
                                } catch (InterruptedException e4) {
                                }
                            }
                            this.f2559c = null;
                            a();
                        } catch (C0129l e5) {
                            C.d("Failed to connect to GPS: " + e5.getMessage());
                            j2 = this.f2557a <= 3 ? 3000L : 10000L;
                        }
                    } else {
                        j2 = 1000;
                    }
                    try {
                        synchronized (this.f2560d.f2555e) {
                            this.f2560d.f2555e.wait(j2);
                        }
                    } catch (InterruptedException e6) {
                        Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                    }
                }
                try {
                    this.f2560d.f2552t.g();
                    this.f2560d.c();
                } catch (Exception e7) {
                }
                b unused = a.f2550r = null;
                synchronized (this.f2560d.f2555e) {
                    this.f2560d.f2555e.notify();
                }
            } catch (RuntimeException e8) {
                C.a(e8);
                throw e8;
            }
        } catch (Throwable th) {
            try {
                this.f2560d.f2552t.g();
                this.f2560d.c();
            } catch (Exception e9) {
            }
            b unused2 = a.f2550r = null;
            synchronized (this.f2560d.f2555e) {
                this.f2560d.f2555e.notify();
                throw th;
            }
        }
    }

    public void b() {
        this.f2558b = false;
        synchronized (this.f2560d.f2555e) {
            this.f2560d.f2555e.notify();
        }
        if (this.f2560d.f2552t != null) {
            this.f2560d.f2552t.g();
        }
        try {
            synchronized (this.f2560d.f2555e) {
                this.f2560d.f2555e.wait(100L);
            }
        } catch (InterruptedException e2) {
        }
    }

    private void c() {
        try {
            this.f2560d.f2552t.g();
            this.f2559c = null;
            this.f2560d.c();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
