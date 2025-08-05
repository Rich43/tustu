package G;

import java.io.Serializable;

/* renamed from: G.ak, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ak.class */
public class C0051ak extends Q implements bH.Q, Serializable {

    /* renamed from: b, reason: collision with root package name */
    private cZ f768b = new C0094c("");

    /* renamed from: c, reason: collision with root package name */
    private cZ f769c = new C0094c("");

    /* renamed from: d, reason: collision with root package name */
    private String f770d = "";

    /* renamed from: e, reason: collision with root package name */
    private C0128k f771e = C0128k.f1261l;

    /* renamed from: f, reason: collision with root package name */
    private C0128k f772f = C0128k.f1250a;

    /* renamed from: g, reason: collision with root package name */
    private C0128k f773g = C0128k.f1259j;

    /* renamed from: h, reason: collision with root package name */
    private C0128k f774h = C0128k.f1259j;

    /* renamed from: i, reason: collision with root package name */
    private String f775i = "";

    /* renamed from: j, reason: collision with root package name */
    private String f776j = "";

    /* renamed from: a, reason: collision with root package name */
    boolean f777a = true;

    public cZ a() {
        return this.f768b;
    }

    public String b() {
        try {
            return this.f768b.a();
        } catch (V.g e2) {
            bH.C.a(e2.getMessage());
            return "Bad On Expression";
        }
    }

    public void a(cZ cZVar) {
        this.f768b = cZVar;
    }

    public cZ d() {
        return this.f769c;
    }

    public void b(cZ cZVar) {
        this.f769c = cZVar;
    }

    public String e() {
        try {
            return this.f769c.a();
        } catch (V.g e2) {
            bH.C.a(e2.getMessage());
            return "Bad Off Expression";
        }
    }

    public String f() {
        return this.f770d;
    }

    public void a(String str) {
        this.f770d = str;
    }

    public C0128k g() {
        return this.f771e;
    }

    public void a(C0128k c0128k) {
        this.f771e = c0128k;
    }

    public C0128k h() {
        return this.f772f;
    }

    public void b(C0128k c0128k) {
        this.f772f = c0128k;
    }

    public C0128k i() {
        return this.f773g;
    }

    public void c(C0128k c0128k) {
        this.f773g = c0128k;
    }

    public C0128k j() {
        return this.f774h;
    }

    public void d(C0128k c0128k) {
        this.f774h = c0128k;
    }

    @Override // bH.Q
    public String c() {
        return ((Object) a()) + aJ();
    }

    public void a(boolean z2) {
        this.f777a = z2;
    }

    public boolean k() {
        return this.f777a;
    }

    public void b(String str) {
        this.f775i = str;
    }

    public String l() {
        return this.f775i;
    }

    public String m() {
        return this.f776j;
    }
}
