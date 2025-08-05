package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* renamed from: G.be, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/be.class */
public class C0072be extends C0088bu implements Serializable, Cloneable {

    /* renamed from: a, reason: collision with root package name */
    private String f892a = "";

    /* renamed from: f, reason: collision with root package name */
    private String f893f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f894g = null;

    /* renamed from: h, reason: collision with root package name */
    private String f895h = null;

    /* renamed from: i, reason: collision with root package name */
    private String f896i = null;

    /* renamed from: j, reason: collision with root package name */
    private String f897j = null;

    /* renamed from: k, reason: collision with root package name */
    private String f898k = null;

    /* renamed from: l, reason: collision with root package name */
    private String f899l = null;

    /* renamed from: m, reason: collision with root package name */
    private String f900m = null;

    /* renamed from: n, reason: collision with root package name */
    private String f901n = null;

    /* renamed from: o, reason: collision with root package name */
    private String[] f902o = new String[2];

    /* renamed from: p, reason: collision with root package name */
    private cZ f903p = null;

    /* renamed from: q, reason: collision with root package name */
    private cZ f904q = null;

    /* renamed from: r, reason: collision with root package name */
    private int f905r = -1;

    /* renamed from: s, reason: collision with root package name */
    private boolean f906s = false;

    /* renamed from: t, reason: collision with root package name */
    private boolean f907t = false;

    /* renamed from: u, reason: collision with root package name */
    private boolean f908u = false;

    public String a() {
        return this.f893f;
    }

    public void a(String str) {
        this.f893f = str;
    }

    public String b() {
        return this.f894g;
    }

    public void b(String str) {
        this.f894g = str;
    }

    public String c() {
        return this.f895h;
    }

    public void c(String str) {
        this.f895h = str;
    }

    public String d() {
        return this.f896i;
    }

    public void d(String str) {
        this.f896i = str;
    }

    public String f() {
        return this.f897j;
    }

    public void e(String str) {
        this.f897j = str;
    }

    public String g() {
        return this.f899l;
    }

    public void f(String str) {
        this.f899l = str;
    }

    public String h() {
        return this.f900m;
    }

    public void g(String str) {
        this.f900m = str;
    }

    public void a(String[] strArr) {
        this.f902o = strArr;
    }

    public int i() {
        return this.f905r;
    }

    public void a(int i2) {
        this.f905r = i2;
    }

    /* renamed from: j, reason: merged with bridge method [inline-methods] */
    public C0072be clone() {
        C0072be c0072be = new C0072be();
        c0072be.f892a = this.f892a;
        c0072be.f893f = this.f893f;
        c0072be.f894g = this.f894g;
        c0072be.f895h = this.f895h;
        c0072be.f896i = this.f896i;
        c0072be.f897j = this.f897j;
        c0072be.f898k = this.f898k;
        c0072be.f899l = this.f899l;
        c0072be.f900m = this.f900m;
        c0072be.f901n = this.f901n;
        c0072be.f902o = this.f902o;
        c0072be.f905r = this.f905r;
        c0072be.z(aa());
        c0072be.v(aJ());
        c0072be.s(M());
        return c0072be;
    }

    public boolean k() {
        return this.f906s;
    }

    public void a(boolean z2) {
        this.f906s = z2;
    }

    public String l() {
        return this.f892a;
    }

    public void h(String str) {
        this.f892a = str;
    }

    @Override // G.C0088bu
    public List e() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.f893f);
        arrayList.add(this.f894g);
        arrayList.add(this.f895h);
        return arrayList;
    }

    @Override // G.C0088bu
    public String i(String str) {
        if (b().equals(str) || a().equals(str) || c().equals(str)) {
            return aH();
        }
        return null;
    }

    public void b(boolean z2) {
        this.f907t = z2;
    }

    public void c(boolean z2) {
        this.f908u = z2;
    }

    public boolean m() {
        return this.f907t;
    }

    public boolean o() {
        return this.f908u;
    }

    public cZ p() {
        return this.f903p;
    }

    public void a(cZ cZVar) {
        this.f903p = cZVar;
    }

    public cZ q() {
        return this.f904q;
    }

    public void b(cZ cZVar) {
        this.f904q = cZVar;
    }
}
