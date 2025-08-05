package S;

import G.C0126i;
import G.C0134q;
import G.R;
import G.T;
import G.aI;
import ax.U;
import bH.C;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

/* loaded from: TunerStudioMS.jar:S/h.class */
public class h implements a {

    /* renamed from: h, reason: collision with root package name */
    private static Timer f1837h = null;

    /* renamed from: c, reason: collision with root package name */
    private String f1830c = "";

    /* renamed from: d, reason: collision with root package name */
    private String f1831d = "";

    /* renamed from: e, reason: collision with root package name */
    private String f1832e = "";

    /* renamed from: f, reason: collision with root package name */
    private int f1833f = -1;

    /* renamed from: a, reason: collision with root package name */
    boolean f1834a = false;

    /* renamed from: b, reason: collision with root package name */
    List f1835b = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private boolean f1836g = false;

    /* renamed from: i, reason: collision with root package name */
    private final List f1838i = new ArrayList();

    public void a(d dVar) {
        this.f1835b.add(dVar);
    }

    @Override // S.a
    public void c(String str) throws C0134q {
        C0126i.b(this.f1830c, str, this.f1831d);
        if (!f()) {
            C0126i.b(this.f1830c, str, this.f1832e);
        }
        h();
    }

    @Override // S.a
    public void d(String str) {
        C0126i.a(this.f1830c, str, this.f1831d);
        C0126i.a(this.f1830c, str, this.f1832e);
    }

    private void h() {
        this.f1838i.clear();
    }

    public String d() {
        return this.f1831d;
    }

    public void e(String str) {
        this.f1831d = str;
        h();
    }

    public String e() {
        return this.f1832e;
    }

    public void f(String str) {
        this.f1832e = str;
        h();
    }

    public boolean f() {
        return this.f1833f >= 0;
    }

    @Override // S.a
    public boolean b() {
        return this.f1836g;
    }

    public void b(boolean z2) {
        try {
            if (z2) {
                i();
                C.c("EventTrigger " + a() + " Triggered. expression: " + d());
                if (f()) {
                    if (this.f1833f > 0) {
                        k().schedule(new i(this), this.f1833f);
                    } else {
                        k().schedule(new i(this), 10L);
                    }
                }
            } else {
                if (f()) {
                    C.c("EventTrigger " + a() + " Reset after resetDelay (ms): " + g());
                } else {
                    C.c("EventTrigger " + a() + " Reset. expression: " + e());
                }
                j();
            }
            this.f1836g = z2;
        } catch (Exception e2) {
            C.a("FAILED to notify and set triggers");
            e2.printStackTrace();
        }
    }

    private void i() {
        Iterator it = this.f1835b.iterator();
        while (it.hasNext()) {
            ((d) it.next()).a();
        }
    }

    private void j() {
        Iterator it = this.f1835b.iterator();
        while (it.hasNext()) {
            ((d) it.next()).b();
        }
    }

    @Override // S.a
    public String a() {
        return this.f1830c;
    }

    public void g(String str) {
        this.f1830c = str;
    }

    private static Timer k() {
        if (f1837h == null) {
            f1837h = new Timer();
        }
        return f1837h;
    }

    @Override // S.a
    public boolean a(String str, byte[] bArr) {
        double dA;
        R rC = T.a().c(str);
        if (d() != null && !d().equals("") && !this.f1838i.contains(str)) {
            try {
                dA = C0126i.a(d(), rC, bArr);
            } catch (Exception e2) {
                C.b(e2.getLocalizedMessage() + ". Trigger Condition not valid for currently loaded config: " + d());
                dA = 0.0d;
                this.f1838i.add(str);
            }
            if (dA != 0.0d) {
                b(true);
                C.c(this.f1830c + ": Trigger Expression: " + d() + " = " + dA);
            }
        }
        return this.f1836g;
    }

    @Override // S.a
    public boolean a(String str) throws NumberFormatException {
        double dA;
        if (d() != null && !d().equals("") && !this.f1838i.contains(str)) {
            try {
                dA = C0126i.a(d(), (aI) T.a().c(str));
            } catch (U e2) {
                C.b(e2.getLocalizedMessage() + ": Bad Trigger Condition: " + e());
                this.f1838i.add(str);
                dA = 0.0d;
            }
            if (dA != 0.0d) {
                b(true);
                C.c(this.f1830c + ": Trigger Expression: " + d() + " = " + dA);
            }
        }
        return this.f1836g;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0042  */
    @Override // S.a
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean b(java.lang.String r6, byte[] r7) {
        /*
            r5 = this;
            G.T r0 = G.T.a()
            r1 = r6
            G.R r0 = r0.c(r1)
            r8 = r0
            r0 = r5
            java.lang.String r0 = r0.e()
            if (r0 == 0) goto La8
            r0 = r5
            boolean r0 = r0.f()
            if (r0 != 0) goto La8
            r0 = r5
            java.lang.String r0 = r0.e()
            java.lang.String r1 = ""
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto La8
            r0 = r5
            java.lang.String r0 = r0.e()     // Catch: ax.U -> L48
            r1 = r8
            r2 = r7
            double r0 = G.C0126i.a(r0, r1, r2)     // Catch: ax.U -> L48
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L42
            r0 = r5
            java.lang.String r0 = r0.d()     // Catch: ax.U -> L48
            r1 = r8
            r2 = r7
            double r0 = G.C0126i.a(r0, r1, r2)     // Catch: ax.U -> L48
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L42
            r0 = 1
            goto L43
        L42:
            r0 = 0
        L43:
            r9 = r0
            goto L7b
        L48:
            r10 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r1 = r10
            java.lang.String r1 = r1.getLocalizedMessage()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = ". Trigger Reset Condition not valid for current config: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r5
            java.lang.String r1 = r1.e()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            bH.C.b(r0)
            r0 = r5
            java.util.List r0 = r0.f1838i
            r1 = r6
            boolean r0 = r0.add(r1)
            r0 = 0
            r9 = r0
            r0 = 0
            return r0
        L7b:
            r0 = r9
            if (r0 == 0) goto La8
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "Reset Ex: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r5
            java.lang.String r1 = r1.e()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = " = "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r9
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            bH.C.c(r0)
            r0 = r5
            r1 = 0
            r0.b(r1)
        La8:
            r0 = r5
            boolean r0 = r0.f1836g
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: S.h.b(java.lang.String, byte[]):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x004d  */
    @Override // S.a
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean b(java.lang.String r6) {
        /*
            r5 = this;
            G.T r0 = G.T.a()
            r1 = r6
            G.R r0 = r0.c(r1)
            r7 = r0
            r0 = r5
            java.lang.String r0 = r0.e()
            if (r0 == 0) goto La2
            r0 = r5
            boolean r0 = r0.f()
            if (r0 != 0) goto La2
            r0 = r5
            java.lang.String r0 = r0.e()
            java.lang.String r1 = ""
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto La2
            r0 = r5
            java.util.List r0 = r0.f1838i
            r1 = r6
            boolean r0 = r0.contains(r1)
            if (r0 != 0) goto La2
            r0 = r5
            java.lang.String r0 = r0.e()     // Catch: ax.U -> L52
            r1 = r7
            double r0 = G.C0126i.a(r0, r1)     // Catch: ax.U -> L52
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L4d
            r0 = r5
            java.lang.String r0 = r0.d()     // Catch: ax.U -> L52
            r1 = r7
            double r0 = G.C0126i.a(r0, r1)     // Catch: ax.U -> L52
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L4d
            r0 = 1
            goto L4e
        L4d:
            r0 = 0
        L4e:
            r8 = r0
            goto L77
        L52:
            r9 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r1 = r9
            java.lang.String r1 = r1.getLocalizedMessage()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = ": Bad Trigger Reset Condition: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r5
            java.lang.String r1 = r1.e()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            bH.C.b(r0)
            r0 = 0
            r8 = r0
        L77:
            r0 = r8
            if (r0 == 0) goto La2
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "Reset Ex: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r5
            java.lang.String r1 = r1.e()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = " = "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r8
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            bH.C.c(r0)
            r0 = r5
            r1 = 0
            r0.b(r1)
        La2:
            r0 = r5
            boolean r0 = r0.f1836g
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: S.h.b(java.lang.String):boolean");
    }

    @Override // S.a
    public boolean c() {
        return this.f1834a;
    }

    @Override // S.a
    public void a(boolean z2) {
        this.f1834a = z2;
    }

    public int g() {
        return this.f1833f;
    }

    public void a(int i2) {
        this.f1833f = i2;
    }
}
