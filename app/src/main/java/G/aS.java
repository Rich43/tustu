package G;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:G/aS.class */
public class aS extends C0088bu {

    /* renamed from: a, reason: collision with root package name */
    private final ArrayList f638a = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    private final ArrayList f639f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private final ArrayList f640g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    private final ArrayList f641h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private String f642i = null;

    /* renamed from: j, reason: collision with root package name */
    private String f643j = null;

    /* renamed from: k, reason: collision with root package name */
    private String f644k = null;

    /* renamed from: l, reason: collision with root package name */
    private String f645l = null;

    /* renamed from: m, reason: collision with root package name */
    private String f646m = null;

    /* renamed from: n, reason: collision with root package name */
    private String f647n = null;

    /* renamed from: o, reason: collision with root package name */
    private String f648o = null;

    /* renamed from: p, reason: collision with root package name */
    private String f649p = null;

    /* renamed from: q, reason: collision with root package name */
    private String f650q = null;

    /* renamed from: r, reason: collision with root package name */
    private String f651r = null;

    /* renamed from: s, reason: collision with root package name */
    private String f652s = null;

    /* renamed from: t, reason: collision with root package name */
    private String f653t = null;

    /* renamed from: u, reason: collision with root package name */
    private String f654u = null;

    /* renamed from: v, reason: collision with root package name */
    private boolean f655v = false;

    /* renamed from: w, reason: collision with root package name */
    private boolean f656w = false;

    /* renamed from: x, reason: collision with root package name */
    private final List f657x = new ArrayList();

    /* renamed from: y, reason: collision with root package name */
    private final List f658y = new ArrayList();

    public Iterator a() {
        return this.f638a.iterator();
    }

    public void a(String str) {
        this.f638a.add(str);
    }

    public Iterator b() {
        return this.f639f.iterator();
    }

    public void a(Character ch) {
        this.f639f.add(ch);
    }

    public Iterator c() {
        return this.f640g.iterator();
    }

    public void b(Character ch) {
        this.f640g.add(ch);
    }

    public void b(String str) {
        this.f641h.add(str);
    }

    public String a(int i2) {
        return i2 < this.f641h.size() ? (String) this.f641h.get(i2) : "";
    }

    public String d() {
        return this.f642i;
    }

    public void c(String str) {
        this.f642i = str;
    }

    public String f() {
        return this.f644k;
    }

    public void d(String str) {
        this.f644k = str;
    }

    public String g() {
        return this.f645l;
    }

    public void e(String str) {
        this.f645l = str;
    }

    public String h() {
        return this.f647n;
    }

    public void f(String str) {
        this.f647n = str;
    }

    public String i() {
        return this.f648o;
    }

    public void g(String str) {
        this.f648o = str;
    }

    public String j() {
        return this.f649p;
    }

    public void h(String str) {
        this.f649p = str;
    }

    public String k() {
        return this.f650q;
    }

    public void a_(String str) {
        this.f650q = str;
    }

    public String l() {
        return this.f651r;
    }

    public void j(String str) {
        this.f651r = str;
    }

    public String m() {
        return this.f652s;
    }

    public void k(String str) {
        this.f652s = str;
    }

    @Override // G.C0088bu
    public boolean n() {
        return true;
    }

    public String o() {
        return this.f643j;
    }

    public void l(String str) {
        this.f643j = str;
    }

    public boolean p() {
        return this.f655v;
    }

    public void a(boolean z2) {
        this.f655v = z2;
    }

    public boolean q() {
        return this.f656w;
    }

    public void b(boolean z2) {
        this.f656w = z2;
    }

    public void m(String str) throws V.g {
        if (str.equals("filter32BitChannels")) {
            b(true);
        } else {
            if (!str.equals("extendedDataInSize")) {
                throw new V.g("Unknown PortEditor Option: " + str + ", Known Options: filter32BitChannels, extendedDataInSize");
            }
            a(true);
        }
    }

    public String r() {
        return this.f646m;
    }

    public void n(String str) {
        this.f646m = str;
    }

    public int s() {
        return this.f638a.size();
    }

    public String t() {
        return this.f653t;
    }

    public void o(String str) {
        this.f653t = str;
    }

    public String u() {
        return this.f654u;
    }

    public void p(String str) {
        this.f654u = str;
    }

    public List v() {
        return this.f657x;
    }

    public void q(String str) {
        this.f657x.add(str);
    }

    public List w() {
        return this.f658y;
    }

    public void r(String str) {
        this.f658y.add(str);
    }
}
