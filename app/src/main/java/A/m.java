package A;

import G.C0129l;
import G.C0130m;
import G.F;
import G.aU;
import G.bS;
import G.cP;
import G.cT;
import bH.C;
import bH.C0995c;
import bH.I;
import bH.W;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:A/m.class */
public class m extends t {

    /* renamed from: a, reason: collision with root package name */
    bS f36a;

    /* renamed from: b, reason: collision with root package name */
    C0129l f37b;

    /* renamed from: c, reason: collision with root package name */
    V.b f38c;

    /* renamed from: d, reason: collision with root package name */
    n f39d;

    /* renamed from: ak, reason: collision with root package name */
    private String f40ak;

    public m(F f2) {
        super(f2);
        this.f36a = null;
        this.f37b = null;
        this.f38c = null;
        this.f39d = null;
        this.f40ak = "ECU";
    }

    public void f() {
        if (this.f39d != null && this.f39d.isAlive()) {
            this.f39d.f41a = false;
            this.f39d.interrupt();
        }
        this.f39d = new n(this);
        this.f39d.start();
    }

    public void g() {
        if (this.f39d == null || !this.f39d.isAlive()) {
            return;
        }
        this.f39d.f41a = false;
    }

    public bS a(f fVar, List list) throws C0129l, V.b {
        this.f36a = null;
        this.f37b = null;
        this.f38c = null;
        int i2 = 25000;
        if (!I.a()) {
            i2 = 25000;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.f39d.a(fVar, list);
        do {
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e2) {
                Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (this.f37b != null || this.f38c != null || this.f36a != null || System.currentTimeMillis() - jCurrentTimeMillis >= i2) {
                break;
            }
        } while (this.f39d.f44d);
        if (this.f37b == null && this.f38c == null && System.currentTimeMillis() - jCurrentTimeMillis > i2 - 1) {
            this.f37b = new C0129l("Timeout trying to connect to: " + fVar.n());
            f();
        }
        if (this.f37b != null) {
            throw this.f37b;
        }
        if (this.f38c != null) {
            throw this.f38c;
        }
        return this.f36a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Finally extract failed */
    public synchronized bS b(f fVar, List list) {
        String strA;
        bS bSVar = null;
        a(fVar);
        try {
            try {
                try {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    while (System.currentTimeMillis() - jCurrentTimeMillis > 15000 && fVar.k() != 0) {
                        wait(100L);
                    }
                    super.h();
                    C.c("!!!!!! Time to Connect = " + (System.currentTimeMillis() - jCurrentTimeMillis));
                    try {
                        wait(1500L);
                    } catch (InterruptedException e2) {
                        Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                    Iterator it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        byte[] bArr = (byte[]) it.next();
                        byte[] bArrA = null;
                        try {
                            C.c("Calling " + fVar.n() + " Command: " + C0995c.d(bArr));
                            bArrA = super.a(bArr, 40L, 600L, -1, (C0130m) null);
                        } catch (V.b e3) {
                            if (!it.hasNext()) {
                                C.d("Timeout on port: " + fVar.n() + ", completed this port query");
                                throw e3;
                            }
                            C.d("Timeout on port: " + fVar.n());
                        }
                        if (bArrA != null) {
                        }
                        if (bArrA != null && bArrA.length != 0) {
                            byte[] bArr2 = bArrA;
                            if (C0995c.c(bArr2, bArr)) {
                                C.d("Echo back of query command. Assuming nothing found.");
                                throw new C0129l("Echo back of query command. Assuming nothing found.");
                            }
                            if (bArr2.length >= 3 && (bArr2[0] & 224) == 224 && (bArr2[1] & 240) == 0 && bArr2[2] == 62) {
                                C.c("Found Controller with no firmware loaded");
                                bSVar = new bS();
                                bSVar.a(bArr2);
                                try {
                                    int iS = s();
                                    strA = b(iS).a();
                                    if (strA.equals(ae.o.f4404t)) {
                                        strA = this.f40ak;
                                    }
                                    ae.m mVarA = ae.o.a(iS);
                                    if (mVarA != null && mVarA.b() != ae.o.f4405u) {
                                        bSVar.a(mVarA);
                                    }
                                } catch (Exception e4) {
                                    strA = this.f40ak;
                                }
                                bSVar.b(strA + " Bootload / No Firmware.");
                            } else if (bArr2.length != 1 || bArr2[0] >= 1) {
                                if (bArr2.length == 3 && bArr2[0] == 1) {
                                    C.c("found signature:" + W.k(new String(bArr2)) + ", using queryCommand:" + new String(bArr));
                                    bSVar = new bS();
                                    bSVar.a(new byte[]{bArr2[1]});
                                    bSVar.b("BigStuff3 Firmware: " + ((int) bArr2[1]));
                                    bSVar.b();
                                } else {
                                    C.c("found signature:" + W.k(new String(bArr2)) + ", using queryCommand:" + new String(bArr));
                                    bSVar = new bS();
                                    bSVar.a(bArr2);
                                    if (bSVar.b().startsWith("$GP")) {
                                        C.d("Looks like a GPS Dongle.");
                                        bSVar.a("GPS Packet");
                                        bSVar.b("GPS Dongle");
                                    } else {
                                        try {
                                            byte[] bArrA2 = super.a(aU.a().a(bSVar), 250L, 550L, -1, (C0130m) null);
                                            if (bArrA2 != null && bArrA2.length > 0) {
                                                bSVar.b(new String(C0995c.c(bArrA2)));
                                            }
                                            bSVar.a(ae.o.b(bSVar));
                                        } catch (V.b e5) {
                                            bSVar.b(bSVar.b());
                                        }
                                        byte[] bArrB = aU.a().b(bSVar);
                                        if (bArrB != null) {
                                            boolean zG = e().G();
                                            try {
                                                try {
                                                    e().a(new J.f());
                                                    e().a(new J.g());
                                                    e().d(true);
                                                    byte[] bArrB2 = super.b(bArrB, 250L, 550L, -1, (C0130m) null);
                                                    if (bArrB2 != null && bArrB2.length > 0) {
                                                        int iA = C0995c.a(bArrB2, 0, bArrB2.length, true, false);
                                                        ae.m mVarA2 = ae.o.a(iA);
                                                        if (mVarA2.b() != ae.o.f4405u) {
                                                            bSVar.a(mVarA2);
                                                        } else {
                                                            C.b("Unknown monitor version: " + iA + ", relying on signature.");
                                                        }
                                                    }
                                                    e().a((cP) null);
                                                    e().a((cT) null);
                                                    e().d(zG);
                                                } catch (Throwable th) {
                                                    e().a((cP) null);
                                                    e().a((cT) null);
                                                    e().d(zG);
                                                    throw th;
                                                }
                                            } catch (Exception e6) {
                                                C.c("No response to Monitor Version Command: " + C0995c.d(bArrB));
                                                e().a((cP) null);
                                                e().a((cT) null);
                                                e().d(zG);
                                            }
                                        }
                                    }
                                }
                            }
                            if (bSVar != null) {
                                C.d("Found Device, discontinue search on " + fVar.n());
                                break;
                            }
                        }
                    }
                    fVar.g();
                    long jCurrentTimeMillis2 = System.currentTimeMillis();
                    while (System.currentTimeMillis() - jCurrentTimeMillis2 > 15000 && fVar.k() != 0) {
                        try {
                            wait(100L);
                        } catch (InterruptedException e7) {
                            Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                        }
                    }
                } catch (C0129l e8) {
                    this.f37b = e8;
                    fVar.g();
                    long jCurrentTimeMillis3 = System.currentTimeMillis();
                    while (System.currentTimeMillis() - jCurrentTimeMillis3 > 15000 && fVar.k() != 0) {
                        try {
                            wait(100L);
                        } catch (InterruptedException e9) {
                            Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e9);
                        }
                    }
                } catch (V.b e10) {
                    C.d("Timeout on: " + fVar.n());
                    this.f38c = e10;
                    fVar.g();
                    long jCurrentTimeMillis4 = System.currentTimeMillis();
                    while (System.currentTimeMillis() - jCurrentTimeMillis4 > 15000 && fVar.k() != 0) {
                        try {
                            wait(100L);
                        } catch (InterruptedException e11) {
                            Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e11);
                        }
                    }
                }
            } catch (IOException e12) {
                C.d("IOException trying to communicate with: " + fVar.n());
                this.f37b = new C0129l("IOException trying to communicate with: " + fVar.n());
                fVar.g();
                long jCurrentTimeMillis5 = System.currentTimeMillis();
                while (System.currentTimeMillis() - jCurrentTimeMillis5 > 15000 && fVar.k() != 0) {
                    try {
                        wait(100L);
                    } catch (InterruptedException e13) {
                        Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e13);
                    }
                }
            } catch (Exception e14) {
                e14.printStackTrace();
                fVar.g();
                long jCurrentTimeMillis6 = System.currentTimeMillis();
                while (System.currentTimeMillis() - jCurrentTimeMillis6 > 15000 && fVar.k() != 0) {
                    try {
                        wait(100L);
                    } catch (InterruptedException e15) {
                        Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e15);
                    }
                }
            }
            this.f36a = bSVar;
            this.f39d.a();
            return bSVar;
        } catch (Throwable th2) {
            fVar.g();
            long jCurrentTimeMillis7 = System.currentTimeMillis();
            while (System.currentTimeMillis() - jCurrentTimeMillis7 > 15000 && fVar.k() != 0) {
                try {
                    wait(100L);
                } catch (InterruptedException e16) {
                    Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e16);
                }
            }
            throw th2;
        }
    }

    public void a(String str) {
        this.f40ak = str;
    }

    private int s() {
        try {
            byte[] bArrA = super.a(new byte[]{-73}, 450L, 350L, 6, (C0130m) null);
            if (bArrA == null || bArrA[0] != -36) {
                return -1;
            }
            return C0995c.a(bArrA, 1, 2, true, false);
        } catch (V.b e2) {
            Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return -1;
        } catch (V.d e3) {
            Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            return -1;
        } catch (IOException e4) {
            Logger.getLogger(m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            return -1;
        }
    }

    public ae.m b(int i2) {
        return ae.o.a(i2);
    }
}
