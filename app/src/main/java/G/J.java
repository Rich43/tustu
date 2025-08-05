package G;

import bH.C0995c;
import bH.C1018z;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:G/J.class */
public abstract class J implements Serializable {

    /* renamed from: T, reason: collision with root package name */
    static P f435T;

    /* renamed from: f, reason: collision with root package name */
    private String f444f;

    /* renamed from: a, reason: collision with root package name */
    private static HashMap f403a = new HashMap();

    /* renamed from: O, reason: collision with root package name */
    protected static boolean f430O = false;

    /* renamed from: b, reason: collision with root package name */
    private List f404b = new CopyOnWriteArrayList();

    /* renamed from: p, reason: collision with root package name */
    final ConcurrentLinkedQueue f405p = new ConcurrentLinkedQueue();

    /* renamed from: q, reason: collision with root package name */
    ArrayList f406q = new ArrayList();

    /* renamed from: r, reason: collision with root package name */
    protected ArrayList f407r = new ArrayList();

    /* renamed from: s, reason: collision with root package name */
    ArrayList f408s = new ArrayList();

    /* renamed from: t, reason: collision with root package name */
    ArrayList f409t = new ArrayList();

    /* renamed from: u, reason: collision with root package name */
    List f410u = Collections.synchronizedList(new ArrayList());

    /* renamed from: v, reason: collision with root package name */
    ArrayList f411v = new ArrayList();

    /* renamed from: w, reason: collision with root package name */
    ArrayList f412w = new ArrayList();

    /* renamed from: x, reason: collision with root package name */
    ArrayList f413x = new ArrayList();

    /* renamed from: y, reason: collision with root package name */
    ArrayList f414y = new ArrayList();

    /* renamed from: z, reason: collision with root package name */
    ArrayList f415z = new ArrayList();

    /* renamed from: A, reason: collision with root package name */
    protected ArrayList f416A = new ArrayList();

    /* renamed from: B, reason: collision with root package name */
    ArrayList f417B = new ArrayList();

    /* renamed from: C, reason: collision with root package name */
    protected ArrayList f418C = new ArrayList();

    /* renamed from: D, reason: collision with root package name */
    protected int f419D = 0;

    /* renamed from: E, reason: collision with root package name */
    protected boolean f420E = false;

    /* renamed from: F, reason: collision with root package name */
    protected boolean f421F = false;

    /* renamed from: G, reason: collision with root package name */
    protected boolean f422G = true;

    /* renamed from: H, reason: collision with root package name */
    protected boolean f423H = true;

    /* renamed from: I, reason: collision with root package name */
    protected boolean f424I = false;

    /* renamed from: J, reason: collision with root package name */
    boolean f425J = false;

    /* renamed from: K, reason: collision with root package name */
    boolean f426K = true;

    /* renamed from: L, reason: collision with root package name */
    int f427L = -2;

    /* renamed from: M, reason: collision with root package name */
    F f428M = null;

    /* renamed from: N, reason: collision with root package name */
    int f429N = 0;

    /* renamed from: P, reason: collision with root package name */
    long f431P = 0;

    /* renamed from: Q, reason: collision with root package name */
    protected long f432Q = System.currentTimeMillis();

    /* renamed from: R, reason: collision with root package name */
    protected long f433R = Long.MAX_VALUE;

    /* renamed from: S, reason: collision with root package name */
    String f434S = null;

    /* renamed from: U, reason: collision with root package name */
    long f436U = System.nanoTime();

    /* renamed from: V, reason: collision with root package name */
    long f437V = 0;

    /* renamed from: c, reason: collision with root package name */
    private boolean f438c = true;

    /* renamed from: W, reason: collision with root package name */
    protected boolean f439W = false;

    /* renamed from: d, reason: collision with root package name */
    private boolean f440d = false;

    /* renamed from: X, reason: collision with root package name */
    C0123f f441X = new C0123f();

    /* renamed from: Y, reason: collision with root package name */
    C0123f f442Y = new C0123f();

    /* renamed from: e, reason: collision with root package name */
    private long f443e = 0;

    /* renamed from: Z, reason: collision with root package name */
    int f445Z = 0;

    /* renamed from: aa, reason: collision with root package name */
    protected int[] f446aa = null;

    /* renamed from: ab, reason: collision with root package name */
    int f447ab = 15;

    /* renamed from: ac, reason: collision with root package name */
    int f448ac = 50;

    /* renamed from: ad, reason: collision with root package name */
    protected HashMap f449ad = new HashMap();

    /* renamed from: ae, reason: collision with root package name */
    CRC32 f450ae = new CRC32();

    /* renamed from: af, reason: collision with root package name */
    int f451af = 0;

    /* renamed from: ag, reason: collision with root package name */
    long f452ag = System.nanoTime();

    /* renamed from: ah, reason: collision with root package name */
    long f453ah = 0;

    /* renamed from: ai, reason: collision with root package name */
    int f454ai = 0;

    /* renamed from: aj, reason: collision with root package name */
    InterfaceC0139v f455aj = new M(this);

    protected J(F f2) {
        this.f444f = "";
        b(f2);
        this.f444f = f2.u();
        if (f435T == null) {
            f435T = new P(this);
            f435T.start();
        }
    }

    public static void c(String str) {
        J j2 = (J) f403a.get(str);
        if (j2 != null) {
            j2.c();
        }
        f403a.remove(str);
    }

    protected void a(J j2) {
        j2.f406q = this.f406q;
        j2.f408s = this.f408s;
        j2.f409t = this.f409t;
        j2.f410u = this.f410u;
        j2.f411v = this.f411v;
        j2.f412w = this.f412w;
        j2.f413x = this.f413x;
        j2.f416A = this.f416A;
        j2.f417B = this.f417B;
        j2.f449ad = this.f449ad;
        j2.f404b = this.f404b;
        j2.f414y = this.f414y;
    }

    public void t() {
        this.f445Z = 0;
        this.f446aa = new int[this.f416A.size()];
        int i2 = 0;
        Iterator it = this.f416A.iterator();
        while (it.hasNext()) {
            F f2 = (F) it.next();
            this.f445Z += f2.n();
            this.f446aa[i2] = f2.n();
            i2++;
        }
    }

    public abstract String n();

    public abstract void c();

    /* JADX INFO: Access modifiers changed from: protected */
    public void u() {
        Iterator it = this.f416A.iterator();
        while (it.hasNext()) {
            ((F) it.next()).t(-2);
        }
    }

    public void v() {
        if (x() || (e().B() >= 0 && e().x(e().B()))) {
            f("Going offline before all changes were permanently saved to the Controller.\nThese writes will be lost!");
            bH.C.d("Going offline before all changes were permanently saved to the Controller.");
            this.f427L = -2;
        }
        this.f405p.clear();
    }

    protected abstract InputStream i();

    protected void a(F f2) {
        int iF = f(f2);
        if (iF < 0) {
            bH.C.c("Skip Burn, last write page: " + iF);
            return;
        }
        C0130m c0130mA = C0130m.a(f2, iF);
        bH.C.c("Burn Page anonymous: " + (this.f427L + 1));
        b(c0130mA);
    }

    private int f(F f2) {
        int iA = -1;
        if (this.f405p.size() > 0) {
            synchronized (this.f405p) {
                Iterator it = this.f405p.iterator();
                while (it.hasNext()) {
                    C0130m c0130m = (C0130m) it.next();
                    if (c0130m.v() != null && c0130m.v().equals(f2) && (c0130m.n() == 5 || c0130m.n() == 4)) {
                        iA = c0130m.o();
                    }
                }
            }
        }
        if (iA < 0) {
            iA = f2.B();
        }
        if (iA < 0) {
            iA = f2.A();
        }
        if (iA < 0) {
            iA = this.f427L;
        }
        return iA;
    }

    public void b(C0130m c0130m) throws IOException {
        if (f430O) {
            b("Received Instruction: " + c0130m.aJ() + ", Page: " + (c0130m.o() + 1));
        }
        if (!this.f421F || !this.f422G) {
            if (c0130m.n() != 7) {
                return;
            }
            try {
                c(c0130m);
            } catch (C0129l e2) {
                Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (V.b e3) {
                Logger.getLogger(J.class.getName()).log(Level.SEVERE, "Timeout", (Throwable) e3);
            }
        }
        if (c0130m.n() == 5 || c0130m.n() == 4) {
            if (c0130m.o() == -1 || c0130m.q() < 0) {
                bH.C.c("Impossible Write Chunk Instruction!!!");
            }
            synchronized (this.f405p) {
                Iterator it = this.f405p.iterator();
                while (it.hasNext()) {
                    C0130m c0130m2 = (C0130m) it.next();
                    int iQ = c0130m2.q() + c0130m2.r();
                    int iQ2 = c0130m.q() + c0130m.r();
                    if (c0130m2.o() == c0130m.o() && c0130m2.q() >= c0130m.q() && iQ <= iQ2) {
                        it.remove();
                        bH.C.c("removed expired instruction");
                    }
                }
            }
        }
        if (c0130m.x() && this.f427L >= 0) {
            b(C0130m.a(c0130m.v(), this.f427L));
        }
        if (c0130m.n() == 6) {
            if (c0130m.o() == this.f427L) {
                if (f430O) {
                    bH.C.c("CommManager got a burn for page " + (this.f427L + 1) + ", cleared lastWritePage");
                }
                this.f427L = -2;
                this.f428M = null;
                bH.C.d("Queueing burn to page:" + (c0130m.o() + 1));
                c0130m.b(new K(this));
            } else {
                bH.C.d("skip burn to page:" + (c0130m.o() + 1) + ", lastWritePage = " + this.f427L);
            }
        } else if (c0130m.n() == 5 || c0130m.n() == 4 || (c0130m.n() == 16 && c0130m.o() >= 0)) {
            this.f427L = c0130m.o();
            this.f428M = c0130m.v();
            d(this.f428M.u(), this.f427L);
        } else if (c0130m.n() == 3) {
        }
        synchronized (this) {
            int i2 = this.f451af;
            this.f451af = i2 + 1;
            c0130m.g(i2);
            this.f405p.add(c0130m);
            notify();
        }
    }

    public boolean w() {
        return !this.f405p.isEmpty();
    }

    public boolean c(int i2) {
        Iterator it = this.f405p.iterator();
        while (it.hasNext()) {
            C0130m c0130m = (C0130m) it.next();
            if (c0130m.o() == i2 && (c0130m.n() == 5 || c0130m.n() == 4)) {
                return true;
            }
        }
        return false;
    }

    public boolean x() {
        int i2 = 0;
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f405p.iterator();
        while (it.hasNext()) {
            C0130m c0130m = (C0130m) it.next();
            if (c0130m.n() == 5 || c0130m.n() == 4) {
                if (!arrayList.contains(Integer.valueOf(c0130m.o() + 1))) {
                    arrayList.add(Integer.valueOf(c0130m.o() + 1));
                }
                i2++;
            }
        }
        if (i2 <= 0) {
            bH.C.c("No Remaining Queue Write instructions");
            return false;
        }
        StringBuilder sb = new StringBuilder();
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            sb.append(it2.next()).append(", ");
        }
        bH.C.c("Queued Write instructions:" + i2 + " to page(s) " + sb.toString());
        return true;
    }

    public abstract boolean b();

    /* JADX INFO: Access modifiers changed from: protected */
    public void y() throws IOException {
        while (!this.f405p.isEmpty()) {
            C0130m c0130m = (C0130m) this.f405p.poll();
            o(c0130m.v() == null ? e().u() : c0130m.v().u());
            try {
                if (e().D() == null && e().al().equals("basicRequestReply")) {
                    a(25L);
                }
                if (c0130m.c()) {
                    cP cPVarD = c0130m.v().D();
                    c0130m.v().a((cP) null);
                    cT cTVarC = c0130m.v().C();
                    c0130m.v().a((cT) null);
                    try {
                        c(c0130m);
                        c0130m.v().a(cTVarC);
                        c0130m.v().a(cPVarD);
                    } catch (Throwable th) {
                        c0130m.v().a(cTVarC);
                        c0130m.v().a(cPVarD);
                        throw th;
                    }
                } else {
                    c(c0130m);
                }
            } catch (Exception e2) {
                if (b()) {
                    bH.C.d("Exception caught processing instruction, but stop has already been initiated. Doing nothing.");
                    if (e2 instanceof C0129l) {
                        throw new IOException(e2.getMessage());
                    }
                } else {
                    bH.C.a("Could not process CommInstruction. writing last " + this.f419D + " controller interactions to the log followed by the stack trace");
                    G();
                    Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    C0132o c0132o = new C0132o(c0130m);
                    c0132o.a(3);
                    c0132o.a("Could not process CommInstruction.\nError written to log.\n" + e2.getMessage());
                    c0130m.b(c0132o);
                    if (e2 instanceof C0129l) {
                        throw new IOException(e2.getMessage());
                    }
                }
            }
        }
        try {
            if (e().D() == null && e().al().equals("basicRequestReply")) {
                Thread.sleep(e().h());
            }
        } catch (InterruptedException e3) {
        }
    }

    protected void c(C0130m c0130m) throws C0129l, IOException, V.b {
        while (this.f424I) {
            a(100L);
        }
        if (f430O) {
            b("Processing Instruction: " + c0130m.aJ() + ", Page: " + (c0130m.o() + 1));
        }
        c0130m.m();
        switch (c0130m.n()) {
            case 1:
                n(c0130m);
                break;
            case 2:
                m(c0130m);
                break;
            case 3:
                l(c0130m);
                bH.C.c("Read All Data");
                break;
            case 4:
                a(c0130m.v(), c0130m.o());
                f(c0130m);
                break;
            case 5:
                a(c0130m.v(), c0130m.o());
                c0130m.b(0.2d);
                e(c0130m);
                break;
            case 6:
                try {
                    if (c0130m.v() != null && c0130m.v().x(c0130m.o())) {
                        d(c0130m);
                    } else if (!x()) {
                        a(e().u(), true);
                    }
                    break;
                } catch (Exception e2) {
                    try {
                        d(c0130m);
                        break;
                    } catch (V.d e3) {
                        throw new C0129l(e3.getMessage());
                    }
                }
                break;
            case 7:
                t(c0130m);
                break;
            case 8:
                s(c0130m);
                break;
            case 16:
                k(c0130m);
                break;
            case 32:
                g(c0130m);
                break;
            case 64:
                q(c0130m);
                break;
            case 128:
                u(c0130m);
                break;
            case 256:
                o(c0130m);
                break;
            case 512:
                p(c0130m);
                break;
            case 1024:
                c0130m.k().run();
                break;
            case 2048:
                i(c0130m);
                break;
            case 4096:
                h(c0130m);
                break;
            case 8192:
                j(c0130m);
                break;
            default:
                bH.C.b("Attempted to process CommInstruction, but type is not supported by this driver:" + c0130m.n());
                break;
        }
        if (f430O) {
            b("Processing Complete: " + c0130m.aJ());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void z() throws IOException, V.b {
        int iNanoTime;
        if (N() || M() >= System.currentTimeMillis() || e().av() <= 0) {
            c(30L);
        } else {
            F fE = e();
            c(fE);
            if (fE.Q() != null && fE.V() && !fE.W()) {
                try {
                    c(C0130m.d(fE));
                    int iA = (int) fE.S().a();
                    a(iA);
                    fE.i(true);
                    bH.C.d("Activated Turbo Baud, Runtime: " + iA);
                } catch (C0129l e2) {
                    Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            boolean z2 = f430O;
            boolean z3 = f430O;
            byte[][] bArr = new byte[this.f416A.size()][1];
            for (int i2 = 0; i2 < bArr.length; i2++) {
                try {
                    F f2 = (F) this.f416A.get(i2);
                    o(f2.u());
                    if (f2.F() && !f2.ap() && f2.o() != null) {
                        if (f2.ac() && f2.ad() >= 0) {
                            if (I()) {
                                bH.C.c("Real OchDelay for " + f2.u() + ": 1, timeout=" + f2.au());
                            }
                            long jNanoTime = System.nanoTime();
                            bArr[i2] = this.f441X.b(f2.n());
                            try {
                                a(f2, bArr[i2], 0, f2.ad(), 1);
                                iNanoTime = (int) ((System.nanoTime() - jNanoTime) / 1000000);
                                if (I()) {
                                    bH.C.c("time to read scattered outpc: " + iNanoTime + " ms.");
                                }
                            } catch (V.b e3) {
                                C0113cs.a().a(f2.E().K());
                                throw e3;
                            } catch (V.d e4) {
                                bH.C.c("Controller reported Comms error, skipping runtime data read. " + e4.getMessage());
                                b(f2.u(), 2);
                                if (e4.a() != 147) {
                                    throw e4;
                                }
                                C0113cs.a().a(f2.E().K());
                                throw e4;
                            }
                        } else if (f2.N()) {
                            int iT = ((F) this.f416A.get(0)).D() == null ? ((F) this.f416A.get(0)).t() / bArr.length : 0;
                            if (I()) {
                                bH.C.c("Real OchDelay for " + f2.u() + ": " + iT + ", timeout=" + f2.au());
                            }
                            long jNanoTime2 = System.nanoTime();
                            bArr[i2] = this.f441X.b(f2.n());
                            try {
                                List<C0140w> listL = f2.L();
                                if (listL == null || listL.isEmpty()) {
                                    b(f2, bArr[i2], 0, this.f446aa[i2], iT);
                                } else {
                                    for (C0140w c0140w : listL) {
                                        if (((F) this.f416A.get(0)).ar()) {
                                            b(f2, bArr[i2], c0140w.a(), c0140w.c(), 0);
                                        } else {
                                            b(f2, bArr[i2], c0140w.a(), c0140w.c(), 1 + (c0140w.c() / 36));
                                        }
                                    }
                                }
                                iNanoTime = (int) ((System.nanoTime() - jNanoTime2) / 1000000);
                                if (I()) {
                                    bH.C.c("Blocked time to read outpc: " + iNanoTime + " ms.");
                                }
                            } catch (V.d e5) {
                                bH.C.c("Controller reported Comms error, skipping runtime data read. " + e5.getMessage());
                                b(f2.u(), 2);
                                throw e5;
                            }
                        } else {
                            long jNanoTime3 = System.nanoTime();
                            byte[] bArrA = f2.o().a(0, f2.n(), null);
                            try {
                                int iT2 = ((F) this.f416A.get(0)).D() == null ? ((F) this.f416A.get(0)).t() / bArr.length : 0;
                                if (I()) {
                                    bH.C.c("Real OchDelay for " + f2.u() + ": " + f2.t() + ", timeout=" + f2.au());
                                }
                                byte[] bArrA2 = a(bArrA, iT2, f2.au(), this.f446aa[i2], (C0130m) null);
                                if (bArrA2.length != this.f446aa[i2]) {
                                    bH.C.c("unexpected runtime response size: " + bArrA2.length + ", expected: " + this.f446aa[i2]);
                                    V.b bVar = new V.b("");
                                    bVar.b(this.f446aa[i2]);
                                    bVar.a(bArrA2.length);
                                    throw bVar;
                                }
                                bArr[i2] = this.f441X.b(bArrA2.length);
                                System.arraycopy(bArrA2, 0, bArr[i2], 0, bArrA2.length);
                                iNanoTime = (int) ((System.nanoTime() - jNanoTime3) / 1000000);
                                if (I()) {
                                    bH.C.c("old style time to read outpc: " + iNanoTime + " ms.");
                                }
                                if (i2 < bArr.length - 1) {
                                    a(1L);
                                }
                            } catch (V.d e6) {
                                if (f2.D() != null && f2.D().c(e6.a())) {
                                    for (byte[] bArr2 : bArr) {
                                        this.f441X.a(bArr2);
                                    }
                                    return;
                                }
                                if (i2 != 0) {
                                    b(f2.u(), 2);
                                    throw new V.b("Failed to communicate with CAN device.\n" + e6.getMessage());
                                }
                                bH.C.c("Controller reported Comms error, skipping runtime data read. " + e6.getMessage());
                                b(f2.u(), 2);
                                throw new V.b("Failed to read runtime data.\n" + e6.getMessage());
                            }
                        }
                        if (i2 == 0) {
                            this.f447ab = iNanoTime;
                        }
                        if (this.f446aa[i2] != bArr[i2].length) {
                            bH.C.b("Och is " + bArr[i2].length + ", expected:" + this.f446aa[i2] + ", turn on Comm debug for more data.");
                            b(f2.u(), 4);
                            for (byte[] bArr3 : bArr) {
                                this.f441X.a(bArr3);
                            }
                            return;
                        }
                    }
                } catch (Throwable th) {
                    for (byte[] bArr4 : bArr) {
                        this.f441X.a(bArr4);
                    }
                    throw th;
                }
            }
            for (int i3 = 0; i3 < this.f416A.size(); i3++) {
                if (((F) this.f416A.get(i3)).F() && !((F) this.f416A.get(i3)).ap()) {
                    if (i3 <= 0 && !b(((F) this.f416A.get(i3)).u(), bArr[i3])) {
                        throw new V.b("Invalid runtime data received.");
                    }
                    a(((F) this.f416A.get(i3)).u(), bArr[i3]);
                }
            }
            for (byte[] bArr5 : bArr) {
                this.f441X.a(bArr5);
            }
            int iAv = e().av() == 0 ? 1000 : (int) ((1.0E9d / e().av()) - (System.nanoTime() - this.f452ag));
            if (this.f431P > 0) {
                this.f431P = iAv < 0 ? 0L : (int) Math.floor((this.f431P + iAv) / 2.0d);
            } else {
                this.f431P = iAv < 0 ? 0L : iAv;
            }
            if (e().av() < 8 && this.f431P < 25000000 && e().D() == null) {
                this.f431P = 25000000L;
            } else if (e().av() < 12 && this.f431P < 15000000 && e().D() == null) {
                this.f431P = 12000000L;
            } else if (e().av() < 18 && this.f431P < 6000000 && e().D() == null) {
                this.f431P = 5000000L;
            } else if (e().av() < 25 && this.f431P < 2000000 && e().D() == null) {
                this.f431P = 2000000L;
            } else if (this.f431P < 0) {
                this.f431P = 0L;
            }
            this.f431P += e().z() * 1000000;
            if (this.f431P > 0) {
                long jNanoTime4 = System.nanoTime();
                long jNanoTime5 = this.f431P;
                do {
                    long j2 = jNanoTime5 % 1000000;
                    a((int) (jNanoTime5 / 1000000), (int) j2);
                    jNanoTime5 = j2 - (System.nanoTime() - jNanoTime4);
                    if (jNanoTime5 <= 0) {
                        break;
                    }
                } while (!w());
            }
        }
        this.f452ag = System.nanoTime();
        if (this.f407r.isEmpty()) {
            return;
        }
        this.f406q.addAll(this.f407r);
        this.f407r.clear();
    }

    private byte[] a(F f2, byte[] bArr, int i2, int i3, int i4) throws V.b, V.d {
        int i5 = 0;
        byte[] bArrA = this.f441X.a(f2.ad());
        List<C0140w> listAb = f2.ab();
        while (i5 < i3) {
            int iG = f2.G(0);
            int i6 = iG < i3 - i5 ? iG : i3 - i5;
            byte[] bArrA2 = f2.Z().a(i2 + i5, i6, null);
            if (i3 > iG && bArrA2.length == 1) {
                bH.C.b("blockingFactor smaller than ochBlockSize, but ochCommand does not support blocking. Will Attempt reading entire outpc");
                i6 = i3;
            }
            byte[] bArrA3 = a(bArrA2, i4, f2.au(), i6, (C0130m) null);
            if (bArrA3.length != i6) {
                V.d dVar = new V.d("Unexpected runtime response size, expected: " + i6 + ", received: " + bArrA3.length);
                dVar.a(147);
                throw dVar;
            }
            System.arraycopy(bArrA3, 0, bArrA, i2 + i5, bArrA3.length);
            i5 += i6;
        }
        int iC = 0;
        for (C0140w c0140w : listAb) {
            System.arraycopy(bArrA, iC, bArr, c0140w.a(), c0140w.c());
            iC += c0140w.c();
        }
        return bArr;
    }

    private byte[] b(F f2, byte[] bArr, int i2, int i3, int i4) throws V.b, V.d {
        int i5 = 0;
        while (true) {
            int i6 = i5;
            if (i6 >= i3) {
                return bArr;
            }
            int iG = f2.G(0);
            int i7 = iG < i3 - i6 ? iG : i3 - i6;
            byte[] bArrA = f2.o().a(i2 + i6, i7, null);
            if (i3 > iG && bArrA.length == 1) {
                bH.C.b("blockingFactor smaller than ochBlockSize, but ochCommand does not support blocking. Will Attempt reading entire outpc");
                i7 = i3;
            }
            byte[] bArrA2 = a(bArrA, i4, f2.au(), i7, (C0130m) null);
            if (bArrA2.length != i7) {
                throw new V.d("Unexpected runtime response size, expected: " + i7 + ", received: " + bArrA2.length);
            }
            System.arraycopy(bArrA2, 0, bArr, i2 + i6, bArrA2.length);
            i5 = i6 + i7;
        }
    }

    public abstract void d();

    protected void b(F f2) {
        if (this.f416A.contains(f2)) {
            return;
        }
        this.f416A.add(f2);
        if (f2.l() != null) {
            N n2 = new N(this);
            n2.a(f2);
            this.f449ad.put(f2.u(), n2);
        }
        t();
    }

    protected F d(String str) {
        if (str == null || str.equals("")) {
            return (F) this.f416A.get(0);
        }
        Iterator it = this.f416A.iterator();
        while (it.hasNext()) {
            F f2 = (F) it.next();
            if (f2.aJ().equals(str)) {
                return f2;
            }
        }
        if (this.f416A.isEmpty()) {
            return null;
        }
        return (F) this.f416A.get(0);
    }

    public F e() {
        return (F) this.f416A.get(0);
    }

    protected void a(F f2, int i2) throws C0129l, V.b {
        try {
            if (this.f423H && f2.A() >= 0 && f2.B() >= 0 && f2.x(f2.A()) && f2.A() != i2) {
                bH.C.c("Activate Page, burn page " + (f2.A() + 1) + " new active page=" + (i2 + 1));
                d(C0130m.a(f2, f2.A()));
            }
            if (f2.m(i2) == null) {
                f2.t(i2);
                return;
            }
            if (f2.A() == i2) {
                return;
            }
            bH.C.d("Activating page: " + (i2 + 1));
            f2.t(i2);
            byte[] bArrM = f2.m(i2);
            if (bArrM == null) {
                return;
            }
            a(bArrM, f2.h());
        } catch (V.b e2) {
            throw e2;
        } catch (Exception e3) {
            Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e3);
            throw new C0129l("Activating page" + i2 + ": " + e3.getMessage());
        }
    }

    protected void d(C0130m c0130m) throws V.d {
        F fV = c0130m.v();
        if (fV == null) {
            fV = e();
        }
        if (c0130m.o() == -2) {
            c0130m.e(fV.A());
        }
        if (c0130m.o() < 0) {
            bH.C.b("Burn requested for page:" + (c0130m.o() + 1));
            return;
        }
        if (fV.B() < 0) {
            bH.C.c("Request to burn page, but no writes have been performed. Ignoring burn. page: " + (c0130m.o() + 1));
            return;
        }
        byte[] bArrD = fV.e(c0130m.o()).d();
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        if (bArrD == null || bArrD.length == 0) {
            bH.C.d("Burn Command empty for page " + (c0130m.o() + 1) + ", bypassing burn and verification.");
            fV.u(-2);
            c0132o.a(2);
            c0132o.b(fV.u());
            c0130m.b(1.0d);
            c0130m.b(c0132o);
            c(fV.u(), c0130m.o());
            if (x()) {
                return;
            }
            this.f427L = -2;
            a(e().u(), true);
            return;
        }
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (e().D() != null) {
                a(bArrD, 1L, fV.h() > 0 ? fV.h() : 50, 0, (C0130m) null);
            } else {
                b(bArrD, -1);
                if (fV.h() > 0) {
                    a(fV.h());
                }
            }
            bH.C.c("Burn time:" + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
            fV.u(-2);
            c0132o.a(1);
            c0132o.b(fV.u());
            bH.C.c("burned page " + (c0130m.o() + 1));
        } catch (V.d e2) {
            throw e2;
        } catch (Exception e3) {
            c0132o.a(3);
            c0132o.a("Device burn failed!\nError: " + e3.getMessage());
            bH.C.c("burned page FAILED! " + (c0130m.o() + 1));
            Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        a(true);
        c0130m.b(1.0d);
        c0130m.b(c0132o);
        c(fV.u(), c0130m.o());
        a(false);
        boolean zR = true;
        try {
            zR = r(c0130m);
        } catch (C0129l e4) {
            aB.a().b(fV.u(), "Serial Communication error during page burn.\nNo Response from Controller.\n\nPossible loss of data that has been written to the Controller.\nGoing offline.");
            c();
            E();
            c0132o.a(3);
        }
        if (zR) {
            return;
        }
        f("Burn Page failed CRC Check.\nCheck Log for full data dump of local and controller Page " + (c0130m.o() + 1) + " data.");
        c0132o.a(3);
        F();
        bH.C.c("Writing the last " + this.f419D + " comm interactions including the burn, crc and page read to the log file here:");
        G();
    }

    private void q(C0130m c0130m) {
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        try {
            C0067b c0067b = new C0067b();
            for (C0130m c0130m2 : c0130m.a()) {
                c0130m2.b(c0067b);
                c(c0130m2);
                if (c0067b.a() == null || c0067b.a().a() == 3) {
                    c0132o.a(3);
                    c0132o.a(c0067b.b());
                    c0130m.b(c0132o);
                    return;
                }
                c0130m2.a(c0067b);
            }
            c0132o.a(1);
            c0132o.a(c0067b.a().e());
            c0132o.a(c0067b.a().d());
        } catch (Exception e2) {
            c0132o.a(3);
            c0132o.a(e2.getMessage());
        }
        c0130m.b(c0132o);
    }

    protected void e(C0130m c0130m) throws V.b {
        F fV = c0130m.v();
        byte[] bArrA = fV.j(c0130m.o()).a(c0130m.q(), c0130m.r(), c0130m.p());
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        try {
            a(bArrA);
            c0132o.a(1);
            fV.u(c0130m.o());
        } catch (V.b | V.d e2) {
            if (e2 instanceof V.d) {
                bH.C.b("Retrying Controller Rejected Write after " + fV.i() + "ms wait");
            }
            try {
                a(fV.i());
                a(bArrA);
                c0132o.a(1);
                fV.u(c0130m.o());
            } catch (Exception e3) {
                if (e2 instanceof V.b) {
                    throw ((V.b) e2);
                }
                if (e2 instanceof V.d) {
                    bH.C.b("Retrying Controller Rejected Write after " + fV.i() + "ms wait");
                }
                bH.C.b("Controller Rejected Write again, Comm Manager will report error for additional handling.");
                c0132o.a(3);
                c0132o.a("Device write failed!\nError: " + e2.getMessage());
                c0130m.b(c0132o);
                return;
            }
        } catch (Exception e4) {
            c0132o.a(3);
            c0132o.a("Device write failed!\nError: " + e4.getMessage());
            c0130m.b(c0132o);
            return;
        }
        c0130m.b(1.0d);
        c0130m.b(c0132o);
        ((N) this.f449ad.get(fV.u())).a(c0130m.o(), c0130m.q(), c0130m.p(), false);
    }

    public abstract void a(boolean z2);

    protected void a(int[] iArr, int[] iArr2, int i2, int i3) {
        String str = "Controller page " + (i2 + 1) + " Does not match Local store:\n                 Controller " + C0995c.d(a(iArr2)) + "                      Local Data Store " + C0995c.d(a(iArr)) + "\n" + C0995c.a(i3, iArr2, iArr);
        O o2 = new O(this);
        o2.a(str);
        f435T.a(o2);
    }

    protected byte[] a(int[] iArr) {
        byte[] bArrA = C0995c.a(iArr);
        this.f450ae.reset();
        this.f450ae.update(bArrA);
        return C0995c.a((int) this.f450ae.getValue(), this.f441X.a(4), true);
    }

    private boolean r(C0130m c0130m) {
        F fV = c0130m.v();
        boolean zB = true;
        if (c(fV, c0130m.o())) {
            zB = b(c0130m.v(), c0130m.o());
            if (!zB) {
                try {
                    a(((N) this.f449ad.get(fV.u())).b(c0130m.o()), a(fV, c0130m.o(), (C0130m) null), c0130m.o(), 0);
                    bH.C.d("Retrying CRC call to see if it agrees after read:");
                    b(c0130m.v(), c0130m.o());
                } catch (C0129l e2) {
                    bH.C.c("Failed during CRC check. Here is the stack:");
                    Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
                }
            }
        }
        return zB;
    }

    public int[] a(String str, int i2) {
        return ((N) this.f449ad.get(str)).a(i2);
    }

    public void a(String str, int i2, int i3, boolean z2) {
        ((N) this.f449ad.get(str)).a(i2, i3, z2);
    }

    protected void f(C0130m c0130m) {
        F fV = c0130m.v();
        byte[] bArrA = fV.i(c0130m.o()).a(c0130m.q(), c0130m.r(), c0130m.p());
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        try {
            if (fV.C() == null) {
                a(bArrA);
            } else {
                a(bArrA, 2L, fV.i(), 1, (C0130m) null);
                a(3L);
            }
            c0132o.a(1);
            this.f432Q = System.currentTimeMillis();
            fV.u(c0130m.o());
        } catch (Exception e2) {
            c0132o.a(3);
            c0132o.a("Device write failed!\nError: " + e2.getMessage());
            Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
        }
        ((N) this.f449ad.get(fV.u())).a(c0130m.o(), c0130m.q(), c0130m.p(), false);
        c0130m.b(1.0d);
        c0130m.b(c0132o);
    }

    private void s(C0130m c0130m) {
        F fV = c0130m.v();
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        try {
            if (fV.B() >= 0) {
                c(C0130m.a(fV, fV.A()));
            }
            byte[] bArrA = this.f441X.a(2);
            byte[] bArrA2 = fV.a(c0130m.u(), c0130m.p());
            if (fV.C() != null) {
                a(bArrA2, c0130m);
            } else {
                bArrA[0] = bArrA2[0];
                bArrA[1] = bArrA2[1];
                byte[] bArrA3 = this.f441X.a(bArrA2.length - 2);
                for (int i2 = 0; i2 < bArrA3.length; i2++) {
                    bArrA3[i2] = bArrA2[i2 + 2];
                }
                a(bArrA);
                if (fV.h() > 0) {
                    Thread.sleep(fV.h());
                }
                a(bArrA3, c0130m);
            }
            c0132o.a(1);
        } catch (Exception e2) {
            c0132o.a(3);
            c0132o.a("Error: " + e2.getMessage());
        }
        c0130m.b(1.0d);
        c0130m.b(c0132o);
    }

    protected void g(C0130m c0130m) {
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        c0130m.m();
        try {
            if (b(c0130m.v(), c0130m.o())) {
                c0132o.a(1);
            } else {
                c0132o.a(2);
                c0132o.a("CRC Mismatch between Controller and local store");
            }
        } catch (C0129l e2) {
            c0132o.a(3);
            c0132o.a("Error during Page CRC check.\n" + e2.getMessage());
            Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
        }
        c0130m.b(1.0d);
        c0130m.b(c0132o);
    }

    protected boolean b(F f2, int i2) {
        return a(f2, i2, 0, f2.f(i2));
    }

    protected boolean a(F f2, int i2, int i3, int i4) throws C0129l {
        boolean z2;
        N n2 = (N) this.f449ad.get(f2.u());
        if (!c(f2, i2)) {
            return false;
        }
        byte[] bArrA = f2.l(i2).a(0, f2.f(i2), null);
        try {
            byte[] bArrA2 = a(bArrA, 20L, f2.i(), 4, (C0130m) null);
            byte[] bArrA3 = C0995c.a(n2.a(i2));
            this.f450ae.reset();
            this.f450ae.update(bArrA3);
            byte[] bArrA4 = C0995c.a((int) this.f450ae.getValue(), this.f441X.a(4), true);
            if (!C0995c.c(bArrA2, bArrA4)) {
                bH.C.d("CRC Mismatch, will need to refresh page from controller ");
                bH.C.d("   CRC from controller page " + (i2 + 1) + CallSiteDescriptor.TOKEN_DELIMITER + C0995c.d(bArrA2));
                bH.C.d("   Local Data CRC for page " + (i2 + 1) + ": " + C0995c.d(bArrA4));
                z2 = false;
            } else if (f430O) {
                bH.C.d("CRC matches fine. ");
                bH.C.c("   CRC from controller page " + (i2 + 1) + CallSiteDescriptor.TOKEN_DELIMITER + C0995c.d(bArrA2));
                bH.C.c("   Local Data CRC for page " + (i2 + 1) + ": " + C0995c.d(bArrA4));
                z2 = true;
            } else {
                bH.C.c("CRC matches for page " + (i2 + 1) + CallSiteDescriptor.TOKEN_DELIMITER + C0995c.d(bArrA2));
                z2 = true;
            }
            return z2;
        } catch (V.b e2) {
            throw new C0129l("Timeout during CRC, command:" + C0995c.d(bArrA) + "Error:" + e2.getMessage());
        } catch (V.d e3) {
            throw new C0129l("Controller Reported error during read: " + e3.getMessage());
        } catch (IOException e4) {
            throw new C0129l("Error reading from Controller, command:" + C0995c.d(bArrA) + "Error:" + e4.getMessage());
        }
    }

    protected void h(C0130m c0130m) {
    }

    protected void i(C0130m c0130m) {
    }

    protected void j(C0130m c0130m) {
    }

    protected void k(C0130m c0130m) {
        F fV = c0130m.v();
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        try {
            int[] iArrP = c0130m.p();
            byte[] bArrA = this.f441X.a(iArrP.length);
            for (int i2 = 0; i2 < bArrA.length; i2++) {
                bArrA[i2] = (byte) iArrP[i2];
            }
            int i3 = 0;
            do {
                try {
                    try {
                        int iF = c0130m.f() >= 0 ? c0130m.f() : -1;
                        if (e().D() != null) {
                            e().D().a(false);
                        }
                        byte[] bArrA2 = c0130m.b() == -1 ? a(bArrA, c0130m.w(), fV != null ? fV.i() : c0130m.w(), iF, c0130m) : a(bArrA, c0130m.w(), c0130m.b(), iF, c0130m);
                        if (c0130m.o() >= 0) {
                            this.f427L = c0130m.o();
                            if (fV != null) {
                                d(fV.u(), this.f427L);
                            }
                        }
                        if (c0130m.d() && bArrA2 != null) {
                            byte[] bArr = new byte[bArrA2.length];
                            System.arraycopy(bArrA2, 0, bArr, 0, bArr.length);
                            c0132o.a(bArr);
                        } else if (bArrA2 != null) {
                            c0132o.a(C0995c.b(bArrA2));
                        }
                        c0132o.a(1);
                        if (e().D() != null) {
                            e().D().a(true);
                        }
                    } catch (V.b e2) {
                        i3++;
                        if (i3 >= 3) {
                            throw e2;
                        }
                        bH.C.d("Timeout on raw write, retrying:" + i3);
                        if (e().D() != null) {
                            e().D().a(true);
                        }
                    }
                    if (c0132o.a() == 1) {
                        break;
                    }
                } catch (Throwable th) {
                    if (e().D() != null) {
                        e().D().a(true);
                    }
                    throw th;
                }
            } while (c0130m.g());
        } catch (Exception e3) {
            c0132o.a(3);
            c0132o.a("Controller Instruction failed!\n\nError: \n" + e3.getMessage());
        }
        c0130m.b(1.0d);
        c0130m.b(c0132o);
        if (f430O) {
            b("All processing complete for " + c0130m.aJ());
        }
    }

    public boolean c(F f2, int i2) {
        return K() && f2 != null && f2.k(i2);
    }

    protected void l(C0130m c0130m) {
        F fV = c0130m.v();
        int[][] iArr = new int[fV.g()][0];
        c0130m.b(0.0d);
        C0130m c0130m2 = new C0130m(fV);
        cE cEVar = new cE(fV, c0130m);
        c0130m2.b(cEVar);
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (e().D() == null) {
                a(10L);
            }
            cEVar.a(i2);
            long jCurrentTimeMillis = System.currentTimeMillis();
            try {
                if (!c(fV, i2)) {
                    iArr[i2] = a(fV, i2, c0130m2);
                    bH.C.c("Read page time: " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
                } else if (b(fV, i2)) {
                    iArr[i2] = ((N) this.f449ad.get(c0130m.v().u())).b(i2);
                    bH.C.d("CrC matched skipped controller read on page:" + (i2 + 1));
                } else if (f430O && c(fV, i2)) {
                    int[] iArrB = ((N) this.f449ad.get(fV.u())).b(i2);
                    iArr[i2] = a(fV, i2, c0130m2);
                    bH.C.c("Read page time: " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
                    a(iArrB, iArr[i2], i2, 0);
                } else {
                    iArr[i2] = a(fV, i2, c0130m2);
                    bH.C.c("Read page time: " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
                    bH.C.d("Checksum page " + (i2 + 1) + " data read: " + C0995c.d(a(iArr[i2])));
                }
            } catch (C0129l e2) {
                if (fV.i() < fV.m()) {
                    fV.c(fV.i() + 50);
                }
                bH.C.d("Timeout reading page " + (i2 + 1) + ", increased blockReadTimeout to " + fV.i() + ", trying once more.");
                try {
                    Thread.sleep(fV.i() * 2);
                } catch (InterruptedException e3) {
                    Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e3);
                }
                try {
                    iArr[i2] = a(fV, i2, c0130m2);
                } catch (C0129l e4) {
                    C0132o c0132o = new C0132o();
                    c0132o.a(3);
                    c0132o.a("Serial Failure, Unable to get all pages of data after multiple attempts.");
                    c0130m.b(c0132o);
                    return;
                }
            }
        }
        C0132o c0132o2 = new C0132o();
        c0132o2.a(1);
        c0132o2.a(iArr);
        c0130m.b(c0132o2);
    }

    protected void m(C0130m c0130m) throws C0129l, IOException, V.b {
        int[] iArrA;
        F fV = c0130m.v();
        int iO = c0130m.o();
        int[] iArr = new int[fV.g()];
        c0130m.b(0.0d);
        try {
            if (c(fV, iO)) {
                a(fV, iO);
                if (b(fV, iO)) {
                    iArrA = ((N) this.f449ad.get(c0130m.v().u())).b(iO);
                    bH.C.d("CrC matched skipped controller read on page:" + iO);
                } else if (f430O && c(fV, iO)) {
                    int[] iArrB = ((N) this.f449ad.get(fV.u())).b(iO);
                    iArrA = a(fV, iO, c0130m);
                    a(iArrB, iArrA, iO, 0);
                } else {
                    iArrA = a(fV, iO, c0130m);
                    bH.C.d("Checksum of data read: " + C0995c.d(a(iArrA)));
                }
            } else {
                iArrA = a(fV, iO, c0130m);
            }
        } catch (C0129l e2) {
            if (fV.i() > fV.m()) {
                bH.C.d("Timeout reading page " + (iO + 1) + ", blockReadTimeout=" + fV.i() + ", giving up..");
                throw e2;
            }
            fV.c(fV.i() + 50);
            bH.C.d("Timeout reading page " + (iO + 1) + ", increased blockReadTimeout to " + fV.i() + ", trying once more.");
            try {
                Thread.sleep(fV.i());
            } catch (InterruptedException e3) {
                Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e3);
            }
            iArrA = a(fV, iO, c0130m);
        }
        c0130m.b((iO + 1.0d) / iArrA.length);
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        c0132o.a(1);
        c0132o.a(iArrA);
        c0130m.b(c0132o);
    }

    protected int[] a(F f2, int i2, C0130m c0130m) throws C0129l, IOException, V.b {
        int i3;
        byte[] bArrA;
        try {
            a(f2, i2);
            System.currentTimeMillis();
            if (f2.aG() == null || f2.aG()[i2] <= 0 || f2.f(i2) < f2.aG()[i2]) {
                if (f2.D() != null) {
                    i3 = f2.i() > 0 ? f2.t() : 50;
                } else {
                    i3 = f2.i() > 300 ? 150 : f2.i() / 2;
                }
                bArrA = a(f2.g(i2), i3, f2.i(), f2.f(i2), c0130m);
                if (bArrA == null || f2.f(i2) != bArrA.length) {
                    throw new IOException("Response size mis-match! Expected:" + f2.f(i2) + ", received:" + bArrA.length);
                }
            } else {
                int i4 = 0;
                int iG = f2.G(i2);
                bArrA = this.f441X.a(f2.f(i2));
                while (i4 < f2.f(i2)) {
                    int iF = i4 + iG < f2.f(i2) ? iG : f2.f(i2) - i4;
                    byte[] bArrA2 = a(f2.a(i2, i4, iF), iF / 20 > 20 ? iF / 20 : 20, f2.i(), iF, (C0130m) null);
                    if (bArrA2 == null || iF != bArrA2.length) {
                        throw new IOException("Response size mis-match! Expected:" + f2.f(i2) + ", received:" + bArrA.length);
                    }
                    System.arraycopy(bArrA2, 0, bArrA, i4, bArrA2.length);
                    i4 += iF;
                    if (e().D() == null) {
                        a(5L);
                    }
                    c0130m.b(i4 / f2.f(i2));
                }
            }
            N n2 = (N) this.f449ad.get(f2.u());
            int[] iArrB = C0995c.b(bArrA);
            n2.a(i2, 0, iArrB, true);
            return iArrB;
        } catch (C0129l e2) {
            Logger.getLogger(J.class.getName()).log(Level.INFO, "Error Reading Page", (Throwable) e2);
            throw e2;
        } catch (V.b e3) {
            Logger.getLogger(J.class.getName()).log(Level.INFO, "Error Reading Page", (Throwable) e3);
            throw e3;
        } catch (Exception e4) {
            throw new C0129l("Error Reading data page " + (i2 + 1) + "\nReported Error: " + e4.getMessage());
        }
    }

    protected void n(C0130m c0130m) throws C0129l, IOException, V.b {
        int[] iArrA;
        F fV = c0130m.v();
        int iO = c0130m.o();
        int iQ = c0130m.q();
        int iF = c0130m.f();
        c0130m.b(0.0d);
        try {
            if (c(fV, iO)) {
                a(fV, iO);
                if (b(fV, iO)) {
                    N n2 = (N) this.f449ad.get(c0130m.v().u());
                    iArrA = new int[c0130m.f()];
                    System.arraycopy(n2.a(iO), c0130m.q(), iArrA, 0, iArrA.length);
                    bH.C.d("CrC matched skipped controller read on page:" + iO);
                } else if (f430O && c(fV, iO)) {
                    int[] iArrB = ((N) this.f449ad.get(fV.u())).b(iO);
                    iArrA = a(fV, iO, iQ, iF, c0130m);
                    int[] iArr = new int[iArrA.length];
                    System.arraycopy(iArrB, iQ, iArr, 0, iArrA.length);
                    a(iArr, iArrA, iO, 0);
                } else {
                    iArrA = a(fV, iO, iQ, iF, c0130m);
                    bH.C.d("Checksum of data read: " + C0995c.d(a(iArrA)));
                }
            } else {
                iArrA = a(fV, iO, iQ, iF, c0130m);
            }
        } catch (C0129l e2) {
            if (fV.i() > fV.m()) {
                bH.C.d("Timeout reading chunk " + (iO + 1) + ", blockReadTimeout=" + fV.i() + ", giving up..");
                throw e2;
            }
            fV.c(fV.i() + 50);
            bH.C.d("Timeout reading chunk " + (iO + 1) + ", increased blockReadTimeout to " + fV.i() + ", trying once more.");
            try {
                Thread.sleep(fV.i());
            } catch (InterruptedException e3) {
                Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e3);
            }
            iArrA = a(fV, iO, iQ, iF, c0130m);
        }
        c0130m.b((iO + 1.0d) / iArrA.length);
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        c0132o.a(1);
        c0132o.a(iArrA);
        c0130m.b(c0132o);
    }

    protected int[] a(F f2, int i2, int i3, int i4, C0130m c0130m) throws C0129l, IOException, V.b {
        int i5;
        byte[] bArrA;
        if (!f2.B(i2)) {
            int[] iArr = new int[i4];
            System.arraycopy(a(f2, i2, c0130m), i3, iArr, 0, i4);
            return iArr;
        }
        try {
            a(f2, i2);
            System.currentTimeMillis();
            if (f2.aG() == null || f2.aG()[i2] <= 0 || i4 < f2.aG()[i2]) {
                if (f2.D() != null) {
                    i5 = f2.i() > 0 ? f2.t() : 30;
                } else {
                    i5 = f2.i() > 300 ? 150 : f2.i() / 3;
                }
                bArrA = a(f2.a(i2, i3, i4), i5, f2.i(), i4, c0130m);
                if (bArrA == null || i4 != bArrA.length) {
                    throw new IOException("Response size mis-match! Expected:" + f2.f(i2) + ", received:" + bArrA.length);
                }
            } else {
                int i6 = 0;
                int iG = f2.G(i2);
                bArrA = this.f441X.a(i4);
                while (i6 < i4) {
                    int i7 = i4 - i6 > iG ? iG : i4 - i6;
                    byte[] bArrA2 = a(f2.a(i2, i3 + i6, i7), f2.t() / 3 > (e().D() == null ? 50 : 10) ? f2.t() / 3 : r21, f2.i(), i7, (C0130m) null);
                    if (bArrA2 == null || i7 != bArrA2.length) {
                        throw new IOException("Response size mis-match! Expected:" + f2.f(i2) + ", received:" + bArrA.length);
                    }
                    System.arraycopy(bArrA2, 0, bArrA, i6, bArrA2.length);
                    i6 += i7;
                    if (e().D() == null) {
                        a(5L);
                    }
                    c0130m.b(i6 / i4);
                }
            }
            N n2 = (N) this.f449ad.get(f2.u());
            int[] iArrB = C0995c.b(bArrA);
            n2.a(i2, i3, iArrB, true);
            return iArrB;
        } catch (C0129l e2) {
            Logger.getLogger(J.class.getName()).log(Level.INFO, "Error Reading Page", (Throwable) e2);
            throw e2;
        } catch (V.b e3) {
            Logger.getLogger(J.class.getName()).log(Level.INFO, "Error Reading Page", (Throwable) e3);
            throw e3;
        } catch (Exception e4) {
            throw new C0129l("Error Reading data page " + (i2 + 1) + "\nReported Error: " + e4.getMessage());
        }
    }

    protected C0132o o(C0130m c0130m) {
        return new C0132o();
    }

    protected C0132o p(C0130m c0130m) {
        C0132o c0132o = new C0132o();
        c0132o.a(1);
        return c0132o;
    }

    protected abstract C0132o a(C0130m c0130m);

    private void t(C0130m c0130m) {
        c0130m.b(0.1d);
        C0132o c0132oA = a(c0130m);
        c0130m.b(1.0d);
        if (c0132oA == null) {
            c0132oA = new C0132o();
            c0132oA.a(3);
        }
        c0130m.b(c0132oA);
    }

    protected String a(byte[] bArr, long j2) {
        try {
            byte[] bArrB = b(bArr, j2);
            if (bArrB == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < bArrB.length && bArrB[i2] != 0 && bArrB[i2] != 255; i2++) {
                sb.append((char) bArrB[i2]);
            }
            return sb.toString();
        } catch (V.d e2) {
            bH.C.c("Controller reported Comms error during read. " + e2.getMessage());
            return null;
        }
    }

    protected synchronized void a(long j2, int i2) {
        long jNanoTime = System.nanoTime();
        long j3 = (j2 * 1000000) + i2;
        if (j3 <= 0) {
            return;
        }
        if (j3 < this.f453ah) {
            double d2 = j3 / this.f453ah;
            if (j3 < 500000 || Math.random() > d2) {
                return;
            }
        } else {
            long j4 = j3 - this.f453ah;
            j2 = j4 / 1000000;
            i2 = ((int) j4) % 1000000;
        }
        try {
            if (this.f439W) {
                bH.C.c("nap:" + j2 + "ms, " + i2 + " nanos");
            }
            Thread.sleep(j2, i2);
        } catch (Exception e2) {
            bH.C.c("nap Failed");
            e2.printStackTrace();
        }
        this.f453ah = (((((System.nanoTime() - (j2 * 1000000)) - i2) - jNanoTime) + this.f453ah) + this.f453ah) / 3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void c(long j2) {
        if (j2 <= 0) {
            return;
        }
        try {
            if (this.f439W) {
                bH.C.c("nap:" + j2);
            }
            wait(j2);
        } catch (Exception e2) {
            bH.C.c("nap Failed");
            e2.printStackTrace();
        }
    }

    protected void a(long j2) {
        if (j2 <= 0) {
            return;
        }
        try {
            Thread.sleep(j2);
        } catch (Exception e2) {
        }
    }

    private void a(byte[] bArr) {
        b(bArr, -1L);
    }

    private void a(byte[] bArr, C0130m c0130m) throws V.d {
        try {
            a(bArr, -1L, -1L, -1, c0130m);
        } catch (V.b e2) {
            bH.C.a("Write timeout, this shouldn't happen");
            Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private void a(byte[] bArr, int i2) {
        b(bArr, -1L);
        a(i2);
    }

    protected byte[] b(byte[] bArr, long j2) {
        return a(bArr, j2, j2, -1, (C0130m) null);
    }

    protected abstract byte[] a(byte[] bArr, long j2, long j3, int i2, C0130m c0130m, InputStream inputStream);

    protected abstract boolean k();

    protected synchronized byte[] b(byte[] bArr, long j2, long j3, int i2, C0130m c0130m) throws V.b, V.d {
        cT cTVarC = ((F) this.f416A.get(0)).C();
        cP cPVarD = ((F) this.f416A.get(0)).D();
        if (cTVarC != null && (k() || ((F) this.f416A.get(0)).G() || bArr.length > 1)) {
            bArr = cTVarC.a(bArr);
        }
        InputStream inputStreamI = i();
        long jI = j3 > ((long) e().i()) ? j3 : e().i();
        if (j2 == -1) {
            j2 = e().t();
        }
        byte[] bArrA = a(bArr, j2, jI, cPVarD.b(), c0130m, inputStreamI);
        int iA = cPVarD.a(bArrA, i2);
        byte[] bArrA2 = a(null, 0L, jI, iA, c0130m, inputStreamI);
        if (bArrA == null || bArrA2 == null) {
            V.b bVar = new V.b("Time out waiting for response to command \n" + C0995c.d(bArr));
            bVar.b(iA);
            bVar.a(0);
            throw bVar;
        }
        byte[] bArrA3 = this.f442Y.a(bArrA.length + bArrA2.length);
        System.arraycopy(bArrA, 0, bArrA3, 0, bArrA.length);
        System.arraycopy(bArrA2, 0, bArrA3, bArrA.length, bArrA2.length);
        if (cPVarD.a(bArr, bArrA3)) {
            if (!cPVarD.b(bArrA3)) {
                if (e().O() != null) {
                }
                if (inputStreamI.available() > 0) {
                    bH.C.b("OverRun condition! No bytes expected from controller, found " + inputStreamI.available());
                }
                return cPVarD.a(bArrA3);
            }
            a(true);
            cO.a().a(e() != null ? e().u() : "", cPVarD.c(bArrA3));
            a(false);
            V.d dVar = new V.d("Config Error Reported: " + cPVarD.c(bArrA3));
            dVar.a(cPVarD.d(bArrA3));
            throw dVar;
        }
        if (cPVarD.d() instanceof J.i) {
            J.i iVar = (J.i) cPVarD.d();
            bH.C.c("call failed protocol validation, running stats: " + iVar.f() + " failed validation, " + iVar.e() + " successful.");
        }
        c("Response packet reported failure.", bArrA3);
        String strU = e() != null ? e().u() : "";
        int[] iArrB = C0995c.b(bArr);
        a(true);
        int[] iArrB2 = C0995c.b(bArrA3);
        cO.a().a(cPVarD.b(cPVarD.d(bArrA3)), strU, cPVarD.d(bArrA3), cPVarD.c(bArrA3), iArrB, iArrB2);
        a(false);
        V.d dVar2 = new V.d("Response packet failed validation: " + cPVarD.c());
        dVar2.a(iArrB2);
        dVar2.b(cPVarD.c(bArrA3));
        dVar2.a(cPVarD.d(bArrA3));
        G();
        throw dVar2;
    }

    protected synchronized byte[] a(byte[] bArr, long j2, long j3, int i2, C0130m c0130m) throws V.b, V.d {
        cP cPVarD = ((F) this.f416A.get(0)).D();
        boolean z2 = c0130m == null || c0130m.g();
        if (cPVarD == null || ((c0130m != null && c0130m.c()) || !(k() || ((F) this.f416A.get(0)).G()))) {
            return a(bArr, j2, j3, i2, c0130m, i());
        }
        if (j2 == j3) {
            j2 = 1;
        }
        try {
            return b(bArr, j2, j3, i2, c0130m);
        } catch (V.d e2) {
            if (!z2 || !cPVarD.a(e2.a())) {
                if (!cPVarD.b(e2.a())) {
                    throw e2;
                }
                bH.C.a("Critical Protocol Error, going offline. " + e2.toString());
                c();
                if (e2.b() != null) {
                    a(true);
                    cO.a().a(e() != null ? e().u() : "", e2.b());
                    a(false);
                }
                throw e2;
            }
            int i3 = e2.a() == 133 ? 50 : 20;
            if (e2.a() == 138) {
                i3 = 100;
            }
            bH.C.c("Protocol reported error after write 0x" + Integer.toHexString(e2.a()) + ", waiting " + i3 + "ms and retry ");
            a(i3);
            try {
                byte[] bArrB = b(bArr, j2, j3, i2, c0130m);
                bH.C.c("Protocol retry successful on 2nd try! ");
                return bArrB;
            } catch (V.d e3) {
                try {
                    bH.C.c("Protocol reported error on 2nd attempt after write 0x" + Integer.toHexString(e2.a()) + ", waiting " + i3 + "ms and retry ");
                    a(i3);
                    byte[] bArrB2 = b(bArr, j2, j3, i2, c0130m);
                    bH.C.c("Protocol retry successful on 3rd try! ");
                    return bArrB2;
                } catch (V.d e4) {
                    bH.C.c("Protocol retry FAILED!!! Failed 3 attempts, giving up.");
                    e3.a(e3.getMessage() + ", 3 attempts made.");
                    e2.b(3);
                    throw e3;
                }
            }
        }
    }

    protected boolean a(ArrayList arrayList) {
        if (arrayList.size() <= 14 || !C0995c.a(arrayList)) {
            return false;
        }
        byte[] bArrA = this.f441X.a(arrayList.size());
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            bArrA[i2] = ((Byte) arrayList.get(i2)).byteValue();
        }
        e(new String(bArrA));
        return true;
    }

    protected void e(String str) {
        bH.C.c("Settings Error detected! Writing the last " + this.f419D + " comm interactions with the controller to the log file here:");
        G();
        cO.a().a(e().u(), str);
    }

    public abstract boolean r();

    public void a(cH cHVar) {
        if (this.f411v.contains(cHVar)) {
            return;
        }
        this.f411v.add(cHVar);
    }

    protected void a(C0130m c0130m, double d2) {
        c0130m.b(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void f(String str) {
        this.f432Q = System.currentTimeMillis() + 10000000;
        Iterator it = this.f411v.iterator();
        while (it.hasNext()) {
            ((cH) it.next()).a(str);
        }
        this.f432Q = System.currentTimeMillis();
    }

    public void a(aV aVVar) {
        this.f409t.add(aVVar);
    }

    public void a(aG aGVar) {
        if (this.f404b.contains(aGVar)) {
            return;
        }
        this.f404b.add(aGVar);
    }

    public void a(InterfaceC0044ad interfaceC0044ad) {
        this.f412w.add(interfaceC0044ad);
    }

    public void b(aG aGVar) {
        this.f404b.remove(aGVar);
    }

    public void a(aF aFVar) {
        this.f406q.add(aFVar);
    }

    public void b(aF aFVar) {
        this.f407r.add(aFVar);
    }

    public void c(aF aFVar) {
        this.f407r.remove(aFVar);
        this.f406q.remove(aFVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(String str, byte[] bArr) {
        for (int i2 = 0; i2 < this.f406q.size(); i2++) {
            try {
                ((aF) this.f406q.get(i2)).a(str, bArr);
            } catch (Exception e2) {
                bH.C.b("Exception during notifyOchRecived, finish remaining");
                Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void b(String str, int i2) {
        for (int i3 = 0; i3 < this.f408s.size(); i3++) {
            try {
                ((aE) this.f408s.get(i3)).a(str, i2);
            } catch (Exception e2) {
                bH.C.b("Exception during notifyOchRecived, finish remaining");
                Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
            }
        }
    }

    protected boolean a(String str, String str2, bS bSVar) {
        boolean z2 = true;
        for (aG aGVar : this.f404b) {
            try {
            } catch (Exception e2) {
                bH.C.b("Exception while notifiying EcuOnlineListener of going online.\nlistener: " + ((Object) aGVar));
                Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
            }
            if (!aGVar.a(str, bSVar)) {
                c();
                z2 = false;
                break;
            }
            continue;
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void A() {
        Iterator it = this.f416A.iterator();
        while (it.hasNext()) {
            F f2 = (F) it.next();
            g(f2.u());
            f2.w(-1);
        }
        b(e().u(), e().u() + " Offline");
    }

    protected void g(String str) {
        Iterator it = this.f404b.iterator();
        while (it.hasNext()) {
            try {
                ((aG) it.next()).a(str);
            } catch (Exception e2) {
            }
        }
    }

    protected boolean b(String str, byte[] bArr) {
        Iterator it = this.f413x.iterator();
        while (it.hasNext()) {
            if (!((aD) it.next()).a(str, bArr)) {
                return false;
            }
        }
        return true;
    }

    protected boolean B() {
        Iterator it = this.f414y.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC0049ai) it.next()).a(this.f455aj)) {
                return false;
            }
        }
        return true;
    }

    public void a(InterfaceC0138u interfaceC0138u) {
        if (this.f415z.contains(interfaceC0138u)) {
            return;
        }
        this.f415z.add(interfaceC0138u);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void h(String str) {
        Iterator it = this.f415z.iterator();
        while (it.hasNext()) {
            ((InterfaceC0138u) it.next()).a(str);
        }
    }

    public void a(aD aDVar) {
        this.f413x.add(aDVar);
    }

    public void a(InterfaceC0049ai interfaceC0049ai) {
        this.f414y.add(interfaceC0049ai);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void i(String str) {
        Iterator it = this.f409t.iterator();
        while (it.hasNext()) {
            ((aV) it.next()).b(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void j(String str) {
        Iterator it = this.f409t.iterator();
        while (it.hasNext()) {
            ((aV) it.next()).c(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void k(String str) {
        Iterator it = this.f409t.iterator();
        while (it.hasNext()) {
            ((aV) it.next()).d(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void l(String str) {
        Iterator it = this.f409t.iterator();
        while (it.hasNext()) {
            ((aV) it.next()).e(str);
        }
    }

    protected void c(String str, int i2) {
        try {
            synchronized (this.f410u) {
                for (InterfaceC0124g interfaceC0124g : this.f410u) {
                    if (interfaceC0124g != null) {
                        interfaceC0124g.b(str, i2);
                    }
                }
            }
        } catch (Exception e2) {
            bH.C.a("Failed to notify all burn listeners. Stack to follow.");
            Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
        }
    }

    protected void d(String str, int i2) {
        try {
            synchronized (this.f410u) {
                F fD = d(str);
                if (fD == null || fD.x(i2)) {
                    for (InterfaceC0124g interfaceC0124g : this.f410u) {
                        if (interfaceC0124g != null) {
                            interfaceC0124g.a(str, i2);
                        }
                    }
                }
            }
        } catch (Exception e2) {
            bH.C.a("Failed to notify all burn listeners. Stack to follow.");
            Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
        }
    }

    public boolean C() {
        F fD = d(L());
        return this.f427L >= 0 && (fD == null || fD.x(this.f427L));
    }

    public J.h D() {
        if (e().D() instanceof J.f) {
            return ((J.f) e().D()).d();
        }
        return null;
    }

    protected void a(String str, boolean z2) {
        synchronized (this.f410u) {
            Iterator it = this.f410u.iterator();
            while (it.hasNext()) {
                ((InterfaceC0124g) it.next()).a(str, z2);
            }
        }
    }

    protected void E() {
        Iterator it = this.f412w.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC0044ad) it.next()).b();
            } catch (Exception e2) {
                bH.C.b("EcuDataSyncErrorListener had unhandled Exception, it was caught here.");
                Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
            }
        }
    }

    protected void F() {
        Iterator it = this.f412w.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC0044ad) it.next()).a();
            } catch (Exception e2) {
                bH.C.b("EcuDataSyncErrorListener had unhandled Exception, it was caught here.");
                Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
            }
        }
    }

    protected void b(String str, String str2) {
        aB.a().a(str, str2);
    }

    public void a(InterfaceC0124g interfaceC0124g) {
        if (this.f410u.contains(interfaceC0124g)) {
            return;
        }
        this.f410u.add(interfaceC0124g);
    }

    public boolean b(InterfaceC0124g interfaceC0124g) {
        return this.f410u.remove(interfaceC0124g);
    }

    public void b(boolean z2) {
        this.f423H = z2;
    }

    public abstract boolean q();

    protected void b(String str) {
        if (I()) {
            O o2 = new O(this);
            o2.a(str);
            f435T.a(o2);
        }
    }

    protected void c(String str, byte[] bArr) {
        if (this.f419D > 0 || I()) {
            O o2 = new O(this);
            o2.a(str);
            o2.a(bArr);
            f435T.a(o2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean d(int i2) throws C0106cl, IOException, V.b {
        int i3 = 0;
        int i4 = i2 > 1000 ? 1000 : i2;
        int iA = -1;
        Iterator it = this.f416A.iterator();
        while (it.hasNext()) {
            F f2 = (F) it.next();
            f2.n(false);
            byte[] bArrB = null;
            this.f420E = false;
            if (i3 > 0) {
                try {
                    if (((F) this.f416A.get(0)).D() != null) {
                        cP cPVarD = ((F) this.f416A.get(0)).D();
                        cT cTVarC = ((F) this.f416A.get(0)).C();
                        f2.a(cPVarD);
                        f2.a(cTVarC);
                        if (cTVarC != null) {
                            ((F) this.f416A.get(0)).d(true);
                        }
                    }
                    boolean z2 = false;
                    if (cN.a().b() != null) {
                        if (iA > 1) {
                            try {
                                J.c.a().a(f2, iA);
                                cN.a().b().a(this.f455aj, f2, f2.x());
                            } catch (C0129l e2) {
                                z2 = true;
                                if (I()) {
                                    bH.C.c("CommException during ProtocolInitialization of " + f2.u() + ": " + e2.getLocalizedMessage());
                                }
                            } catch (V.b e3) {
                                z2 = true;
                                if (I()) {
                                    bH.C.c("CommTimeoutException during ProtocolInitialization of " + f2.u() + ": " + e3.getLocalizedMessage());
                                }
                            }
                        } else {
                            z2 = true;
                        }
                    }
                    if (z2) {
                        if (((F) this.f416A.get(0)).G(0) < f2.G(0)) {
                            f2.I(((F) this.f416A.get(0)).G(0));
                        }
                        if (((F) this.f416A.get(0)).ax() < f2.ax()) {
                            f2.H(((F) this.f416A.get(0)).ax());
                        }
                    }
                    try {
                        bArrB = b(f2.p().d(), i4);
                    } catch (V.d e4) {
                    }
                    if (bArrB == null) {
                        f("Failed to communicate with Device :" + f2.u() + ", \ndisabling so other controllers can go online.\nSetttings Changes Made to this controller during this session will not be sent\nas there is no communication with the controller.");
                        f2.n(true);
                        bH.C.d("temporarily disabled CAN Device, signature is null.");
                    }
                } catch (Exception e5) {
                    f("Failed to communicate with Device :" + f2.u() + ", \ndisabling so other controllers can go online.\nSetttings Changes Made to this controller during this session will not be sent\nas there is no communication with the controller.");
                    bH.C.d("temporarily disabled CAN Device, error trying to get signature.");
                    f2.n(true);
                }
            } else {
                g(f2);
                boolean zG = f2.G();
                f2.d(false);
                try {
                    if (cN.a().b() != null) {
                        cN.a().b().a();
                        try {
                            iA = cN.a().b().a(this.f455aj);
                            if (iA > 0) {
                                this.f420E = true;
                            }
                            if (iA > 1) {
                                J.c.a().a(f2, iA);
                                cN.a().b().a(this.f455aj, f2, f2.x());
                            }
                            f2.d(zG);
                        } catch (C0129l e6) {
                            if (I()) {
                                bH.C.c("CommException during ProtocolInitialization: " + e6.getLocalizedMessage());
                            }
                        } catch (V.b e7) {
                            if (I()) {
                                bH.C.c("CommTimeoutException during ProtocolInitialization: " + e7.getLocalizedMessage());
                            }
                        }
                    }
                    try {
                        bArrB = b(f2.p().d(), i4);
                        if (bArrB != null && bArrB.length > 0) {
                            byte[] bArr = new byte[bArrB.length];
                            System.arraycopy(bArrB, 0, bArr, 0, bArr.length);
                            bArrB = bArr;
                        }
                        f2.d(zG);
                        if (cN.a().b() != null && !this.f420E) {
                            try {
                                int iA2 = cN.a().b().a(this.f455aj);
                                if (iA2 > 0) {
                                    this.f420E = true;
                                }
                                if (iA2 > 1) {
                                    cN.a().b().a(this.f455aj, f2, f2.x());
                                }
                            } catch (C0129l e8) {
                                if (I()) {
                                    bH.C.c("CommException during ProtocolInitialization: " + e8.getLocalizedMessage());
                                }
                            } catch (V.b e9) {
                                if (I()) {
                                    bH.C.c("CommTimeoutException during ProtocolInitialization: " + e9.getLocalizedMessage());
                                }
                            }
                        }
                    } catch (V.d e10) {
                        throw new V.b("Protocol Error communicating with main controller: " + e10.getLocalizedMessage());
                    }
                } finally {
                    f2.d(zG);
                }
            }
            if (!B()) {
                throw new C0106cl("GoOnlineApprover rejected.");
            }
            if (0 != 0) {
                bH.C.d("Device Removed, no further interrogation.");
            }
            if (0 == 0 && b(bArrB)) {
                bH.C.d("Sig Bytes:" + C0995c.d(bArrB));
                String strH = null;
                if (bArrB.length == 1 && bArrB[0] == 20) {
                    bArrB = (((int) bArrB[0]) + "").getBytes();
                }
                if (f2.q() == null || e().q().d() == null) {
                    strH = "Bowling & Grippo MS1 Base Code " + ((Object) bArrB);
                } else {
                    try {
                        try {
                            byte[] bArrB2 = b(f2.q().d(), i4);
                            if (bArrB2 == null || bArrB2.length != 2) {
                                strH = bH.W.h(bH.W.k(new String(bArrB2)));
                            } else {
                                strH = (bH.W.a("" + Integer.toHexString(C0995c.a(bArrB2[1])), '0', 2) + bH.W.a("" + Integer.toHexString(C0995c.a(bArrB2[0])), '0', 2)).toUpperCase();
                            }
                        } catch (V.b e11) {
                            if (I()) {
                                bH.C.c("CommTimeoutException during versionInfo: " + e11.getLocalizedMessage());
                            }
                            strH = (bArrB == null || bArrB.length <= 0) ? "Timeout" : "Serial Signature: " + new String(bArrB);
                        }
                    } catch (V.d e12) {
                        Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e12);
                    }
                }
                f2.m(strH);
                e().E(e().av());
                a(true);
                this.f421F = true;
                u();
                bS bSVarB = bS.b(bArrB);
                bSVarB.b(strH);
                bH.C.d("Communicating with sig:" + bSVarB.b() + ", " + bSVarB.c());
                if (0 == 0) {
                    if (!a(f2.u(), strH, bSVarB)) {
                        this.f420E = false;
                        return true;
                    }
                    this.f420E = true;
                    cP cPVarD2 = e().D();
                    if (cPVarD2 != null) {
                        cPVarD2.d().a();
                    }
                    c(f2);
                }
                a(false);
            } else if (bArrB == null || !C0995c.c(f2.p().d(), bArrB)) {
                if (bArrB == null || bArrB.length <= 0) {
                    return false;
                }
                if (bArrB.length > 1) {
                    bH.C.b("Unsupported Controller Firmware: " + new String(bArrB));
                    b(f2.u(), "Invalid data received from controller.");
                }
                if (i3 <= 0) {
                    return false;
                }
                f("Failed to communicate with Device :" + f2.u() + ", \ndisabling so we can go online.\nSetttings Changes Made to this controller during this session will not be sent\nas there is no communication with the controller.");
                it.remove();
            }
            i3++;
        }
        return true;
    }

    protected boolean c(F f2) throws IOException, V.b {
        if (f2.Q() == null || !f2.U() || !p() || f2.W() || f2.S() == null) {
            if (f2.U() || !p() || !f2.W() || f2.T()) {
                return false;
            }
            d(f2);
            return false;
        }
        try {
            c(C0130m.d(f2));
            int iA = (int) f2.S().a();
            a(iA);
            f2.i(true);
            bH.C.d("Activated Turbo Baud: " + iA);
            return true;
        } catch (C0129l e2) {
            Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return false;
        }
    }

    protected void d(F f2) throws IOException, V.b {
        if (f2.R() != null && p() && f2.W()) {
            try {
                c(C0130m.e(f2));
                bH.C.d("Deactivated Full Time Turbo Baud");
            } catch (C0129l e2) {
                Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            int iR = f2.r();
            a(iR);
            bH.C.d("Deactivated Turbo Baud, returned to: " + iR);
            f2.i(false);
        }
    }

    private void g(F f2) {
        if (!p() || f2.S() == null) {
            return;
        }
        if (f2.U()) {
            int i2 = this.f454ai;
            this.f454ai = i2 + 1;
            if (i2 % 2 != 0) {
                int iA = (int) f2.S().a();
                a(iA);
                f2.i(true);
                if (I()) {
                    bH.C.d("Trying Turbo Baud: " + iA);
                    return;
                }
                return;
            }
        }
        int iR = f2.r();
        a(iR);
        f2.i(false);
        if (I()) {
            bH.C.d("Trying normal baud: " + iR);
        }
    }

    protected void m(String str) {
        if (this.f419D > 0 && this.f418C.size() > this.f419D) {
            this.f418C.remove(0);
        }
        this.f418C.add(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void G() {
        if (this.f419D <= 0 || I()) {
            return;
        }
        bH.C.d("------------------   Begin Historical Commands ---------------------------");
        Iterator it = this.f418C.iterator();
        while (it.hasNext()) {
            System.out.println((String) it.next());
            it.remove();
        }
        bH.C.d("-------------------   End Historical Commands ----------------------------");
    }

    protected void a(String str, int[] iArr) {
        if (this.f419D > 0 || I()) {
            O o2 = new O(this);
            o2.a(str);
            o2.a(iArr);
            f435T.a(o2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a() {
        long jNanoTime = System.nanoTime() - this.f436U;
        int i2 = (int) ((jNanoTime % NativeMediaPlayer.ONE_SECOND) / 1000000);
        return ((int) ((jNanoTime % 60000000000000L) / 60000000000L)) + CallSiteDescriptor.TOKEN_DELIMITER + bH.W.a(((int) ((jNanoTime % 60000000000L) / NativeMediaPlayer.ONE_SECOND)) + "", '0', 2) + "." + bH.W.a(i2 + "", '0', 3);
    }

    public boolean b(byte[] bArr) {
        if (bArr != null) {
            return X.a().a(bS.b(bArr));
        }
        return false;
    }

    public boolean H() {
        if (this.f417B.size() <= 0) {
            return true;
        }
        L l2 = new L(this);
        if (this.f424I) {
            return true;
        }
        boolean z2 = f430O;
        Iterator it = this.f417B.iterator();
        while (it.hasNext()) {
            bN bNVar = (bN) it.next();
            if (C1018z.i().c() != bNVar.a(l2)) {
                bH.C.b("Firmware Validation failed, trying once more.");
                if (C1018z.i().c() != bNVar.a(l2)) {
                    if (bNVar.b()) {
                        c();
                        bH.C.a("Validation failed. Can not connect.");
                    } else {
                        bH.C.a("Validation failed. Will try again when connected.");
                    }
                    f430O = z2;
                    return false;
                }
            }
        }
        f430O = z2;
        return true;
    }

    public static boolean I() {
        return f430O;
    }

    public static void e(boolean z2) {
        f430O = z2;
    }

    public boolean n(String str) {
        return ((F) this.f416A.get(0)).u().equals(str);
    }

    public void a(bN bNVar) {
        this.f417B.add(bNVar);
    }

    public void J() {
        this.f417B.clear();
    }

    public boolean K() {
        return this.f438c;
    }

    public void f(boolean z2) {
        this.f438c = z2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String L() {
        return this.f444f;
    }

    protected void o(String str) {
        this.f444f = str;
    }

    public void e(int i2) {
        this.f419D = i2;
    }

    public long M() {
        return this.f443e;
    }

    public synchronized void d(long j2) {
        this.f443e = j2;
        bH.C.c("Ignore runtime reads for: " + (j2 - System.currentTimeMillis()));
        if (j2 < System.currentTimeMillis()) {
            notify();
        }
    }

    public boolean N() {
        return this.f440d;
    }

    public void g(boolean z2) {
        this.f440d = z2;
    }

    private void u(C0130m c0130m) throws C0129l {
        C0132o c0132o = new C0132o();
        try {
            String strA = a(C0995c.a(c0130m.p()), c0130m.b() >= 0 ? c0130m.b() : e().i());
            c0132o.a(1);
            c0132o.a(strA);
        } catch (V.b e2) {
            c0132o.a(3);
            c0132o.a("Timeout");
        } catch (Exception e3) {
            throw new C0129l("Error Reading String \nReported Error: " + e3.getMessage());
        }
        c0130m.b(c0132o);
    }

    protected abstract boolean p();

    protected abstract boolean a(int i2);

    public abstract boolean a(Thread thread);

    public boolean e(F f2) {
        return this.f416A.contains(f2);
    }

    public boolean O() {
        return this.f422G;
    }
}
