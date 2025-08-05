package G;

import bH.C0995c;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: G.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/m.class */
public class C0130m extends Q {

    /* renamed from: i, reason: collision with root package name */
    private F f1289i;

    /* renamed from: a, reason: collision with root package name */
    private int f1281a = 0;

    /* renamed from: b, reason: collision with root package name */
    private ArrayList f1282b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private List f1283c = null;

    /* renamed from: d, reason: collision with root package name */
    private int f1284d = -1;

    /* renamed from: e, reason: collision with root package name */
    private int[] f1285e = null;

    /* renamed from: f, reason: collision with root package name */
    private int f1286f = -1;

    /* renamed from: g, reason: collision with root package name */
    private int f1287g = 1;

    /* renamed from: h, reason: collision with root package name */
    private String f1288h = null;

    /* renamed from: j, reason: collision with root package name */
    private String f1290j = null;

    /* renamed from: k, reason: collision with root package name */
    private String f1291k = null;

    /* renamed from: l, reason: collision with root package name */
    private int f1292l = -1;

    /* renamed from: m, reason: collision with root package name */
    private int f1293m = -1;

    /* renamed from: n, reason: collision with root package name */
    private boolean f1294n = false;

    /* renamed from: o, reason: collision with root package name */
    private boolean f1295o = false;

    /* renamed from: p, reason: collision with root package name */
    private int f1296p = -1;

    /* renamed from: q, reason: collision with root package name */
    private int f1297q = -1;

    /* renamed from: r, reason: collision with root package name */
    private int f1298r = -1;

    /* renamed from: s, reason: collision with root package name */
    private boolean f1299s = false;

    /* renamed from: t, reason: collision with root package name */
    private boolean f1300t = false;

    /* renamed from: u, reason: collision with root package name */
    private boolean f1301u = true;

    /* renamed from: v, reason: collision with root package name */
    private int f1302v = 0;

    /* renamed from: w, reason: collision with root package name */
    private C0132o f1303w = null;

    /* renamed from: x, reason: collision with root package name */
    private Runnable f1304x = null;

    /* renamed from: y, reason: collision with root package name */
    private String f1305y = null;

    public C0130m(F f2) {
        this.f1289i = null;
        this.f1289i = f2;
    }

    public static C0130m a(C0130m c0130m) {
        C0130m c0130m2 = new C0130m(c0130m.v());
        c0130m2.e(c0130m.o());
        c0130m2.b(c0130m.p());
        c0130m2.f(c0130m.q());
        c0130m2.d(c0130m.n());
        c0130m2.e(c0130m.f1294n);
        c0130m2.w(c0130m.aK());
        c0130m2.f1282b = c0130m.f1282b;
        return c0130m2;
    }

    public static C0130m a(F f2, int i2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(6);
        c0130m.v("Burn");
        c0130m.e(i2);
        return c0130m;
    }

    public static C0130m b(F f2, int i2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(256);
        c0130m.j(i2);
        c0130m.v("Update runtime channels");
        return c0130m;
    }

    public static C0130m a(F f2, String str) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(2048);
        c0130m.a(str);
        c0130m.b(new cF());
        c0130m.v("Read PC Variable");
        return c0130m;
    }

    public static C0130m a(F f2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(2048);
        c0130m.a("ALL_PC_VARS");
        c0130m.b(new cF());
        c0130m.v("READ_PC_VARIABLES");
        return c0130m;
    }

    public static C0130m a(F f2, String str, String str2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(4096);
        StringBuilder sb = new StringBuilder();
        sb.append(str).append("=").append(str2).append("~");
        c0130m.a(sb.toString());
        c0130m.v("WRITE_PC_VARIABLES");
        return c0130m;
    }

    public static C0130m c(F f2, int i2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(512);
        c0130m.j(i2);
        c0130m.v("Staop DAQ");
        return c0130m;
    }

    public static C0130m a(int[] iArr) {
        C0130m c0130m = new C0130m(null);
        c0130m.d(16);
        c0130m.b(iArr);
        c0130m.v("RawWrite");
        return c0130m;
    }

    public static C0130m a(F f2, int[] iArr) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(16);
        c0130m.b(iArr);
        c0130m.v("Raw Write");
        return c0130m;
    }

    public static C0130m b(F f2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(8192);
        c0130m.b(new int[]{0});
        c0130m.v("Activate Server Turbo Baud");
        return c0130m;
    }

    public static C0130m c(F f2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(8192);
        c0130m.b(new int[]{1});
        c0130m.v("Deactivate Server Turbo Baud");
        return c0130m;
    }

    public static C0130m d(F f2) {
        C0130m c0130m = new C0130m(f2);
        H hQ = f2.Q();
        c0130m.d(16);
        c0130m.b(C0995c.b(hQ.d()));
        c0130m.v("Activate Turbo Baud");
        return c0130m;
    }

    public static C0130m e(F f2) {
        C0130m c0130m = new C0130m(f2);
        H hR = f2.R();
        c0130m.d(16);
        c0130m.b(C0995c.b(hR.d()));
        c0130m.d(true);
        c0130m.v("Deactivate Turbo Baud");
        return c0130m;
    }

    public static List b(F f2, String str) {
        G gB = f2.b(str);
        ArrayList arrayList = new ArrayList();
        Iterator it = gB.iterator();
        while (it.hasNext()) {
            H h2 = (H) it.next();
            C0130m c0130m = new C0130m(f2);
            c0130m.d(16);
            c0130m.b(C0995c.b(h2.d()));
            arrayList.add(c0130m);
            c0130m.v("CustomCommand");
        }
        return arrayList;
    }

    public static C0130m a(F f2, List list) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(64);
        c0130m.a(list);
        c0130m.v("Chained Instruction");
        return c0130m;
    }

    public static C0130m a(F f2, int i2, int i3, int[] iArr) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(5);
        c0130m.e(i2);
        c0130m.f(i3);
        c0130m.b(iArr);
        c0130m.v("Write Chunk");
        return c0130m;
    }

    public static C0130m a(F f2, String str, int[] iArr) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(8);
        c0130m.d(str);
        c0130m.b(iArr);
        c0130m.v("Write Reference Table");
        return c0130m;
    }

    public static C0130m a(F f2, int i2, int i3, int i4) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(4);
        c0130m.e(i2);
        c0130m.f(i3);
        c0130m.b(new int[]{i4});
        c0130m.v("Write byte");
        return c0130m;
    }

    public static C0130m f(F f2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(128);
        c0130m.b(C0995c.b(f2.O().d()));
        c0130m.v("Retrieve Config Error");
        return c0130m;
    }

    public static C0130m a(F f2, int i2, int i3) throws V.g {
        String strJ = f2.J();
        if (strJ == null) {
            return null;
        }
        try {
            byte[] bArrD = f2.e(strJ).d();
            int[] iArrA = C0995c.a(i3, new int[2], true);
            C0130m c0130mA = a(f2, new int[]{bArrD[0], bArrD[1], i2, 0, 0, iArrA[0], iArrA[1]});
            c0130mA.v("Table CRC Call");
            return c0130mA;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.g("Unable to parse table CRC instruction: " + strJ);
        }
    }

    public static C0130m a(String str, String str2) {
        C0130m c0130m = new C0130m(null);
        c0130m.d(7);
        c0130m.c(str2);
        c0130m.b(str);
        c0130m.v("Test Connection");
        return c0130m;
    }

    public static C0130m g(F f2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(3);
        c0130m.v("Read All Tune Data");
        return c0130m;
    }

    public static C0130m d(F f2, int i2) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(2);
        c0130m.e(i2);
        c0130m.v("Read Page " + (i2 + 1));
        return c0130m;
    }

    public static C0130m b(F f2, int i2, int i3, int i4) {
        C0130m c0130m = new C0130m(f2);
        c0130m.d(1);
        c0130m.e(i2);
        c0130m.f(i3);
        c0130m.b(i4);
        c0130m.v("Read Chunk " + (i2 + 1));
        return c0130m;
    }

    public void a(InterfaceC0131n interfaceC0131n) {
        this.f1282b.remove(interfaceC0131n);
    }

    public void b(InterfaceC0131n interfaceC0131n) {
        this.f1282b.add(interfaceC0131n);
    }

    public void b(double d2) {
        Iterator it = this.f1282b.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC0131n) it.next()).a(d2);
            } catch (Exception e2) {
                bH.C.b("Exception in notifyProgressListenersUpdate, Continuing");
                e2.printStackTrace();
            }
        }
    }

    public void b(C0132o c0132o) {
        this.f1303w = c0132o;
        if (c0132o.a() == 3) {
            c(c0132o);
        } else {
            c(c0132o);
        }
    }

    public void c(C0132o c0132o) {
        Iterator it = this.f1282b.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC0131n) it.next()).a(c0132o);
            } catch (Exception e2) {
                bH.C.b("Exception in notifyProgressListenersComplete, Continuing");
                e2.printStackTrace();
            }
        }
    }

    public List a() {
        return this.f1283c;
    }

    public void a(List list) {
        this.f1283c = list;
    }

    public void a(int i2) {
        this.f1293m = i2;
    }

    public int b() {
        return this.f1293m;
    }

    public boolean c() {
        return this.f1295o;
    }

    public void a(boolean z2) {
        this.f1295o = z2;
    }

    public boolean d() {
        return this.f1299s;
    }

    public void b(boolean z2) {
        this.f1299s = z2;
    }

    public int f() {
        return this.f1297q;
    }

    public void b(int i2) {
        this.f1297q = i2;
    }

    public boolean g() {
        return this.f1301u;
    }

    public void c(boolean z2) {
        this.f1301u = z2;
    }

    public int h() {
        return this.f1302v;
    }

    public void c(int i2) {
        this.f1302v = i2;
    }

    private void j(int i2) {
        this.f1298r = i2;
    }

    public int i() {
        return this.f1298r;
    }

    public void d(boolean z2) {
        this.f1300t = z2;
    }

    public C0132o j() {
        return this.f1303w;
    }

    public Runnable k() {
        return this.f1304x;
    }

    public String l() {
        return this.f1305y;
    }

    public void a(String str) {
        this.f1305y = str;
    }

    public void m() {
        Iterator it = this.f1282b.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC0131n) it.next()).e();
            } catch (Exception e2) {
                bH.C.b("Exception in notifyProgressListenersBegin, Continuing");
                e2.printStackTrace();
            }
        }
    }

    public int n() {
        return this.f1281a;
    }

    public void d(int i2) {
        this.f1281a = i2;
    }

    public int o() {
        return this.f1284d;
    }

    public void e(int i2) {
        this.f1284d = i2;
    }

    public int[] p() {
        return this.f1285e;
    }

    public void b(int[] iArr) {
        this.f1285e = iArr;
    }

    public int q() {
        return this.f1286f;
    }

    public void f(int i2) {
        this.f1286f = i2;
    }

    public int r() {
        if (this.f1285e == null) {
            return 0;
        }
        return this.f1285e.length;
    }

    public String s() {
        return this.f1290j;
    }

    public void b(String str) {
        this.f1290j = str;
    }

    public String t() {
        return this.f1291k;
    }

    public void c(String str) {
        this.f1291k = str;
    }

    public void g(int i2) {
        this.f1296p = i2;
    }

    public String u() {
        return this.f1288h;
    }

    public void d(String str) {
        this.f1288h = str;
    }

    public void h(int i2) {
        this.f1287g = i2;
    }

    public F v() {
        return this.f1289i;
    }

    public void h(F f2) {
        this.f1289i = f2;
    }

    public int w() {
        return this.f1292l;
    }

    public void i(int i2) {
        this.f1292l = i2;
    }

    public boolean x() {
        return this.f1294n;
    }

    public void e(boolean z2) {
        this.f1294n = z2;
    }

    @Override // G.Q
    public String toString() {
        String str = "commandType=" + this.f1281a + ", page=" + this.f1284d + ", offset=" + this.f1286f;
        if (this.f1285e != null) {
            str = str + " payload=" + C0995c.b(this.f1285e);
        }
        return str;
    }
}
