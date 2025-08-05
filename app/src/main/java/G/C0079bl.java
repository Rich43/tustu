package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: G.bl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bl.class */
public class C0079bl extends C0088bu implements Serializable, Cloneable {

    /* renamed from: a, reason: collision with root package name */
    private dh f944a = new B(0.0d);

    /* renamed from: f, reason: collision with root package name */
    private dh f945f = new B(100.0d);

    /* renamed from: g, reason: collision with root package name */
    private int f946g = 10;

    /* renamed from: h, reason: collision with root package name */
    private ArrayList f947h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private ArrayList f948i = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    private ArrayList f949j = new ArrayList();

    /* renamed from: k, reason: collision with root package name */
    private String f950k = null;

    /* renamed from: l, reason: collision with root package name */
    private ArrayList f951l = new ArrayList();

    /* renamed from: m, reason: collision with root package name */
    private dh f952m = new B(0.0d);

    /* renamed from: n, reason: collision with root package name */
    private dh f953n = new B(100.0d);

    /* renamed from: o, reason: collision with root package name */
    private int f954o = 10;

    /* renamed from: p, reason: collision with root package name */
    private double[] f955p = null;

    /* renamed from: q, reason: collision with root package name */
    private ArrayList f956q = new ArrayList();

    /* renamed from: r, reason: collision with root package name */
    private String f957r = null;

    /* renamed from: s, reason: collision with root package name */
    private ArrayList f958s = new ArrayList();

    /* renamed from: t, reason: collision with root package name */
    private String[] f959t = null;

    /* renamed from: u, reason: collision with root package name */
    private boolean f960u = false;

    /* renamed from: v, reason: collision with root package name */
    private boolean f961v = false;

    /* renamed from: w, reason: collision with root package name */
    private boolean f962w = false;

    /* renamed from: x, reason: collision with root package name */
    private boolean f963x = false;

    /* renamed from: y, reason: collision with root package name */
    private A f964y = null;

    /* renamed from: z, reason: collision with root package name */
    private boolean f965z = false;

    /* renamed from: A, reason: collision with root package name */
    private boolean f966A = false;

    /* renamed from: B, reason: collision with root package name */
    private String f967B = null;

    /* renamed from: C, reason: collision with root package name */
    private String f968C = null;

    /* renamed from: D, reason: collision with root package name */
    private String f969D = null;

    /* renamed from: E, reason: collision with root package name */
    private int f970E = 5;

    /* renamed from: F, reason: collision with root package name */
    private dh f971F = null;

    public int a() {
        return this.f970E;
    }

    public double b() {
        return this.f944a.a();
    }

    public void a(double d2) {
        this.f944a = new B(d2);
    }

    public void a(dh dhVar) {
        this.f944a = dhVar;
    }

    public double c() {
        return this.f945f.a();
    }

    public void b(dh dhVar) {
        this.f945f = dhVar;
    }

    public void b(double d2) {
        this.f945f = new B(d2);
    }

    public int d() {
        return this.f947h.size();
    }

    public String a(int i2) {
        return i2 >= this.f948i.size() ? "1" : (String) this.f948i.get(i2);
    }

    public String b(int i2) {
        return (String) this.f947h.get(i2);
    }

    public void a(String str) {
        this.f947h.add(str);
        this.f948i.add("1");
    }

    public void a(String str, String str2) {
        this.f947h.add(str);
        this.f948i.add(str2);
    }

    public String f() {
        return this.f950k;
    }

    public void b(String str) {
        this.f950k = str;
    }

    public cZ c(int i2) {
        if (this.f951l.size() > i2) {
            return (cZ) this.f951l.get(i2);
        }
        return null;
    }

    public int g() {
        return this.f951l.size();
    }

    public void a(cZ cZVar) {
        this.f951l.add(cZVar);
    }

    public double h() {
        return this.f952m.a();
    }

    public void c(double d2) {
        c(new B(d2));
    }

    public void c(dh dhVar) {
        this.f952m = dhVar;
    }

    public double i() {
        return this.f953n.a();
    }

    public void d(dh dhVar) {
        this.f953n = dhVar;
    }

    public void d(double d2) {
        d(new B(d2));
    }

    public int j() {
        return this.f956q.size();
    }

    public String d(int i2) {
        return (String) this.f956q.get(i2);
    }

    public void c(String str) {
        this.f956q.add(str);
    }

    public void b(cZ cZVar) {
        this.f949j.add(cZVar);
    }

    public cZ e(int i2) {
        return (cZ) this.f949j.get(i2);
    }

    public int k() {
        return this.f949j.size();
    }

    public String l() {
        return this.f957r;
    }

    public void d(String str) {
        this.f957r = str;
    }

    public int m() {
        return this.f958s.size();
    }

    public cZ f(int i2) {
        if (this.f958s.size() > i2) {
            return (cZ) this.f958s.get(i2);
        }
        return null;
    }

    public void c(cZ cZVar) {
        this.f958s.add(cZVar);
    }

    public int o() {
        return this.f946g;
    }

    public void g(int i2) {
        this.f946g = i2;
    }

    public int p() {
        return this.f954o;
    }

    public void h(int i2) {
        this.f954o = i2;
    }

    public String q() {
        if (r() == null || r().length == 0) {
            return null;
        }
        return r()[0];
    }

    public void e(String str) {
        if (r() == null || r().length == 0) {
            a(new String[1]);
        }
        r()[0] = str;
    }

    public String[] r() {
        return this.f959t;
    }

    public void a(String[] strArr) {
        this.f959t = strArr;
    }

    public double[] s() {
        return this.f955p;
    }

    public void a(double[] dArr) {
        this.f955p = dArr;
    }

    /* renamed from: t, reason: merged with bridge method [inline-methods] */
    public C0079bl clone() {
        C0079bl c0079bl = new C0079bl();
        c0079bl.f944a = this.f944a;
        c0079bl.f945f = this.f945f;
        c0079bl.f946g = this.f946g;
        c0079bl.f947h = this.f947h;
        c0079bl.f948i = this.f948i;
        c0079bl.f950k = this.f950k;
        c0079bl.f951l = this.f951l;
        c0079bl.f952m = this.f952m;
        c0079bl.f953n = this.f953n;
        c0079bl.f954o = this.f954o;
        c0079bl.f955p = this.f955p;
        c0079bl.f956q = this.f956q;
        c0079bl.f957r = this.f957r;
        c0079bl.f958s = this.f958s;
        c0079bl.f959t = this.f959t;
        c0079bl.f949j = this.f949j;
        c0079bl.f965z = this.f965z;
        c0079bl.v(aJ());
        c0079bl.c(this.f961v);
        c0079bl.a(this.f960u);
        c0079bl.b(this.f962w);
        c0079bl.h(this.f969D);
        c0079bl.f(this.f967B);
        c0079bl.g(this.f968C);
        c0079bl.f(this.f966A);
        c0079bl.z(aa());
        c0079bl.s(M());
        return c0079bl;
    }

    public boolean u() {
        return this.f960u;
    }

    public void a(boolean z2) {
        this.f960u = z2;
    }

    public boolean v() {
        return this.f962w;
    }

    public void b(boolean z2) {
        this.f962w = z2;
    }

    public boolean w() {
        return this.f961v;
    }

    public void c(boolean z2) {
        this.f961v = z2;
    }

    @Override // G.C0088bu
    public List e() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f947h.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        Iterator it2 = this.f956q.iterator();
        while (it2.hasNext()) {
            arrayList.add(it2.next());
        }
        return arrayList;
    }

    public boolean x() {
        return this.f963x;
    }

    public void d(boolean z2) {
        this.f963x = z2;
    }

    public A y() {
        return this.f964y;
    }

    public void a(A a2) {
        this.f964y = a2;
    }

    @Override // G.C0088bu
    public String i(String str) {
        if (this.f947h.contains(str) || this.f956q.contains(str)) {
            return aH();
        }
        return null;
    }

    public void e(boolean z2) {
        this.f965z = z2;
    }

    public boolean z() {
        return this.f965z;
    }

    public boolean A() {
        return this.f966A;
    }

    public void f(boolean z2) {
        this.f966A = z2;
    }

    public String B() {
        return this.f967B;
    }

    public void f(String str) {
        this.f967B = str;
    }

    public String C() {
        return this.f968C;
    }

    public void g(String str) {
        this.f968C = str;
    }

    public String D() {
        return this.f969D;
    }

    public void h(String str) {
        this.f969D = str;
    }

    public boolean E() {
        return (this.f971F == null || this.f971F.a() == 0.0d) ? false : true;
    }

    public void e(dh dhVar) {
        this.f971F = dhVar;
    }
}
