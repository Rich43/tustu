package y;

import G.C0113cs;
import G.InterfaceC0109co;
import G.InterfaceC0139v;
import G.R;
import G.aT;
import bH.C;
import bH.C1018z;
import sun.java2d.marlin.MarlinConst;

/* renamed from: y.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:y/b.class */
public class C1894b extends f implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    R f14034a;

    /* renamed from: b, reason: collision with root package name */
    aT f14035b;

    /* renamed from: c, reason: collision with root package name */
    boolean f14036c;

    /* renamed from: d, reason: collision with root package name */
    InterfaceC0139v f14037d;

    /* renamed from: e, reason: collision with root package name */
    long f14038e;

    /* renamed from: f, reason: collision with root package name */
    int f14039f;

    /* renamed from: g, reason: collision with root package name */
    int f14040g;

    /* renamed from: h, reason: collision with root package name */
    int f14041h;

    /* renamed from: i, reason: collision with root package name */
    int f14042i;

    /* renamed from: v, reason: collision with root package name */
    private static boolean f14043v = false;

    /* renamed from: j, reason: collision with root package name */
    boolean f14044j;

    public static void a(boolean z2) {
        f14043v = z2;
    }

    public C1894b(R r2, aT aTVar) {
        super(r2.c(), aTVar);
        this.f14034a = null;
        this.f14035b = null;
        this.f14036c = false;
        this.f14037d = null;
        this.f14038e = 0L;
        this.f14039f = 0;
        this.f14040g = 50;
        this.f14041h = 4;
        this.f14042i = 0;
        this.f14044j = false;
        this.f14035b = aTVar;
        this.f14034a = r2;
        try {
            C0113cs.a().a(r2.c(), "rpm", this);
        } catch (V.a e2) {
            C.a("OutputChannel 'rpm' not found!", e2, null);
        }
    }

    @Override // y.f, G.bN
    public int a(InterfaceC0139v interfaceC0139v) {
        this.f14037d = interfaceC0139v;
        return 1966;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (!f14043v && !this.f14036c && this.f14039f >= this.f14040g && System.currentTimeMillis() - this.f14038e > MarlinConst.statDump && d2 == 0.0d && this.f14037d != null) {
            if (!this.f14034a.C().a(Thread.currentThread())) {
                C0113cs.a().a(false);
                this.f14044j = true;
                return;
            }
            try {
                try {
                    if (super.a(this.f14037d) != C1018z.i().c()) {
                        this.f14042i++;
                        if (this.f14042i >= this.f14041h) {
                            C.a(C.a.f223d);
                            this.f14035b.a(this.f14034a.c(), C.a.f223d);
                            this.f14042i = 0;
                            this.f14034a.C().c();
                        }
                    } else {
                        this.f14042i = 0;
                    }
                    if (this.f14044j) {
                        C0113cs.a().a(true);
                        this.f14044j = false;
                        return;
                    }
                } catch (Exception e2) {
                    C.c("Handled Exception 111199");
                    if (this.f14044j) {
                        C0113cs.a().a(true);
                        this.f14044j = false;
                        return;
                    }
                }
            } catch (Throwable th) {
                if (!this.f14044j) {
                    throw th;
                }
                C0113cs.a().a(true);
                this.f14044j = false;
                return;
            }
        }
        if (d2 == 0.0d) {
            this.f14039f++;
        } else {
            this.f14039f = 0;
        }
    }
}
