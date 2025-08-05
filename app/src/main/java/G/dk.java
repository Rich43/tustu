package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:G/dk.class */
public class dk implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f1197a = null;

    /* renamed from: i, reason: collision with root package name */
    private String f1198i = null;

    /* renamed from: j, reason: collision with root package name */
    private String f1199j = null;

    /* renamed from: k, reason: collision with root package name */
    private String f1200k = null;

    /* renamed from: l, reason: collision with root package name */
    private String f1201l = null;

    /* renamed from: m, reason: collision with root package name */
    private String f1202m = null;

    /* renamed from: n, reason: collision with root package name */
    private String f1203n = null;

    /* renamed from: o, reason: collision with root package name */
    private String f1204o = null;

    /* renamed from: p, reason: collision with root package name */
    private String f1205p = null;

    /* renamed from: q, reason: collision with root package name */
    private String f1206q = null;

    /* renamed from: r, reason: collision with root package name */
    private String f1207r = null;

    /* renamed from: s, reason: collision with root package name */
    private String f1208s = null;

    /* renamed from: t, reason: collision with root package name */
    private float f1209t = 0.0f;

    /* renamed from: b, reason: collision with root package name */
    List f1210b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f1211c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    ArrayList f1212d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    ArrayList f1213e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    ArrayList f1214f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    ArrayList f1215g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    ArrayList f1216h = new ArrayList();

    public String b() {
        return this.f1197a;
    }

    public void b(String str) {
        this.f1197a = str;
    }

    public String c() {
        return this.f1198i;
    }

    public void c(String str) {
        this.f1198i = str;
        i(str);
    }

    public String d() {
        return this.f1201l;
    }

    public void d(String str) {
        this.f1201l = str;
    }

    public String e() {
        return this.f1202m;
    }

    public void e(String str) {
        this.f1202m = str;
    }

    public String f() {
        return this.f1204o;
    }

    public void f(String str) {
        this.f1204o = str;
    }

    public String g() {
        return this.f1205p;
    }

    public void g(String str) {
        this.f1205p = str;
    }

    public String h() {
        return this.f1206q;
    }

    public void h(String str) {
        this.f1206q = str;
    }

    public void a(aW aWVar) {
        this.f1211c.add(aWVar);
    }

    public Iterator i() {
        return this.f1211c.iterator();
    }

    public void i(String str) {
        if (this.f1212d.contains(str)) {
            return;
        }
        this.f1212d.add(str);
    }

    public Iterator j() {
        return this.f1212d.iterator();
    }

    public void j(String str) {
        if (this.f1213e.contains(str)) {
            return;
        }
        this.f1213e.add(str);
    }

    public Iterator k() {
        return this.f1213e.iterator();
    }

    public void k(String str) {
        if (this.f1215g.contains(str)) {
            return;
        }
        this.f1215g.add(str);
    }

    public Iterator l() {
        return this.f1215g.iterator();
    }

    public void l(String str) {
        if (this.f1214f.contains(str)) {
            return;
        }
        this.f1214f.add(str);
    }

    public Iterator m() {
        return this.f1214f.iterator();
    }

    public String n() {
        return this.f1199j;
    }

    public void m(String str) {
        this.f1199j = str;
    }

    public float o() {
        return this.f1209t;
    }

    public void a(float f2) {
        this.f1209t = f2;
    }

    public void n(String str) throws V.g {
        if (!str.equals("disableMaxPercentLimit")) {
            throw new V.g("Unknown Option: " + str);
        }
        this.f1210b.add(str);
    }

    public boolean o(String str) {
        return this.f1210b.contains(str);
    }

    public String p() {
        return this.f1207r != null ? this.f1207r : g();
    }

    public void p(String str) {
        this.f1207r = str;
    }

    public String q() {
        return this.f1208s != null ? this.f1208s : h();
    }

    public void q(String str) {
        this.f1208s = str;
    }

    public String r() {
        return this.f1203n;
    }

    public void r(String str) {
        this.f1203n = str;
    }

    public String s() {
        return this.f1203n == null ? this.f1198i : this.f1203n;
    }

    public String t() {
        return this.f1200k;
    }

    public void s(String str) {
        this.f1200k = str;
    }

    public void t(String str) {
        this.f1216h.add(str);
    }

    public Iterator u() {
        return this.f1216h.iterator();
    }
}
