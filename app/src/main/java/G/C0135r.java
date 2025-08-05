package G;

import java.io.Serializable;

/* renamed from: G.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/r.class */
public class C0135r extends Q implements bH.Q, Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f1315a = null;

    /* renamed from: b, reason: collision with root package name */
    private boolean f1316b = false;

    /* renamed from: c, reason: collision with root package name */
    private String f1317c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f1318d = false;

    /* renamed from: e, reason: collision with root package name */
    private bH.Q f1319e = null;

    public C0135r() {
    }

    public C0135r(String str) {
        v(str);
    }

    public String a() {
        return b() ? this.f1315a + " (Default)" : this.f1315a;
    }

    public void a(String str) {
        this.f1315a = str;
    }

    public boolean b() {
        return this.f1316b;
    }

    public void a(boolean z2) {
        this.f1316b = z2;
    }

    public void b(String str) {
        this.f1317c = str;
    }

    @Override // G.Q
    public String toString() {
        return a() != null ? a() : aJ();
    }

    public boolean equals(Object obj) {
        if (obj instanceof C0135r) {
            C0135r c0135r = (C0135r) obj;
            return c0135r.aJ().equals(aJ()) && c0135r.f1315a.equals(this.f1315a);
        }
        if (obj instanceof String) {
            return ((String) obj).equals(aJ());
        }
        return false;
    }

    @Override // bH.Q
    public String c() {
        return this.f1319e != null ? this.f1319e.c() : a() + aJ();
    }

    public void a(bH.Q q2) {
        this.f1319e = q2;
    }

    public void b(boolean z2) {
        this.f1318d = z2;
    }
}
