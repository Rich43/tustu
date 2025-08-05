package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* renamed from: G.ae, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ae.class */
public class C0045ae implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private List f707a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private HashMap f708b = new LinkedHashMap();

    /* renamed from: c, reason: collision with root package name */
    private HashMap f709c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    private HashMap f710d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    private HashMap f711e = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    private List f712f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private HashMap f713g = new HashMap();

    /* renamed from: h, reason: collision with root package name */
    private HashMap f714h = new HashMap();

    /* renamed from: i, reason: collision with root package name */
    private List f715i = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    private HashMap f716j = new HashMap();

    /* renamed from: k, reason: collision with root package name */
    private HashMap f717k = new HashMap();

    /* renamed from: l, reason: collision with root package name */
    private List f718l = new ArrayList();

    /* renamed from: m, reason: collision with root package name */
    private HashMap f719m = new HashMap();

    /* renamed from: n, reason: collision with root package name */
    private HashMap f720n = new HashMap();

    /* renamed from: o, reason: collision with root package name */
    private ArrayList f721o = new ArrayList();

    /* renamed from: p, reason: collision with root package name */
    private ArrayList f722p = new ArrayList();

    /* renamed from: q, reason: collision with root package name */
    private ArrayList f723q = new ArrayList();

    /* renamed from: r, reason: collision with root package name */
    private ArrayList f724r = new ArrayList();

    /* renamed from: s, reason: collision with root package name */
    private ArrayList f725s = new ArrayList();

    /* renamed from: t, reason: collision with root package name */
    private HashMap f726t = new HashMap();

    /* renamed from: u, reason: collision with root package name */
    private ArrayList f727u = new ArrayList();

    /* renamed from: v, reason: collision with root package name */
    private ArrayList f728v = new ArrayList();

    /* renamed from: w, reason: collision with root package name */
    private ArrayList f729w = new ArrayList();

    /* renamed from: x, reason: collision with root package name */
    private HashMap f730x = new HashMap();

    /* renamed from: y, reason: collision with root package name */
    private bD f731y = null;

    /* renamed from: z, reason: collision with root package name */
    private Map f732z = new HashMap();

    /* renamed from: A, reason: collision with root package name */
    private F f733A = null;

    /* renamed from: B, reason: collision with root package name */
    private String f734B = "";

    /* renamed from: C, reason: collision with root package name */
    private String f735C = null;

    /* renamed from: D, reason: collision with root package name */
    private String f736D = "";

    /* renamed from: E, reason: collision with root package name */
    private String f737E = null;

    /* renamed from: F, reason: collision with root package name */
    private boolean f738F = false;

    /* renamed from: G, reason: collision with root package name */
    private String f739G = null;

    public C0045ae a(C0045ae c0045ae) {
        c0045ae.f708b = new HashMap();
        c0045ae.f707a = new ArrayList();
        for (aM aMVar : this.f708b.values()) {
            if (aMVar instanceof bZ) {
                bZ bZVarQ = ((bZ) aMVar).Q();
                c0045ae.f708b.put(aMVar.aJ(), bZVarQ);
                c0045ae.f707a.add(bZVarQ);
            } else {
                c0045ae.f708b.put(aMVar.aJ(), aMVar);
                c0045ae.f707a.add(aMVar);
            }
        }
        c0045ae.f709c = this.f709c;
        c0045ae.f710d = this.f710d;
        c0045ae.f711e = this.f711e;
        c0045ae.f713g = this.f713g;
        c0045ae.f714h = this.f714h;
        c0045ae.f715i = this.f715i;
        c0045ae.f716j = this.f716j;
        c0045ae.f722p = this.f722p;
        c0045ae.f723q = this.f723q;
        c0045ae.f724r = this.f724r;
        c0045ae.f733A = this.f733A;
        c0045ae.f735C = this.f735C;
        c0045ae.f734B = this.f734B;
        c0045ae.f731y = this.f731y;
        return c0045ae;
    }

    public HashMap a() {
        return this.f708b;
    }

    public HashMap b() {
        return this.f709c;
    }

    public HashMap c() {
        return this.f710d;
    }

    public HashMap d() {
        return this.f711e;
    }

    public List e() {
        return this.f712f;
    }

    public HashMap f() {
        return this.f713g;
    }

    public HashMap g() {
        return this.f714h;
    }

    public List h() {
        return this.f715i;
    }

    public HashMap i() {
        return this.f716j;
    }

    public HashMap j() {
        return this.f717k;
    }

    public HashMap k() {
        return this.f719m;
    }

    public HashMap l() {
        return this.f720n;
    }

    public ArrayList m() {
        return this.f721o;
    }

    public ArrayList n() {
        return this.f722p;
    }

    public ArrayList o() {
        return this.f723q;
    }

    public ArrayList p() {
        return this.f724r;
    }

    public ArrayList q() {
        return this.f725s;
    }

    public HashMap r() {
        return this.f726t;
    }

    public ArrayList s() {
        return this.f727u;
    }

    public bD t() {
        return this.f731y;
    }

    public void a(bD bDVar) {
        this.f731y = bDVar;
    }

    public Map u() {
        return this.f732z;
    }

    public F v() {
        return this.f733A;
    }

    public void a(F f2) {
        this.f733A = f2;
    }

    public String w() {
        return this.f734B;
    }

    public void a(String str) {
        this.f734B = str;
    }

    public String x() {
        return this.f735C;
    }

    public void b(String str) {
        this.f735C = str;
    }

    public String y() {
        return this.f736D;
    }

    public void c(String str) {
        this.f736D = str;
    }

    public String z() {
        return this.f737E;
    }

    public void d(String str) {
        this.f737E = str;
    }

    public boolean A() {
        return this.f738F;
    }

    public void a(boolean z2) {
        this.f738F = z2;
    }

    public String B() {
        return this.f739G;
    }

    public void e(String str) {
        this.f739G = str;
    }

    public boolean a(C0043ac c0043ac) {
        boolean z2 = false;
        for (int i2 = 0; i2 < this.f722p.size(); i2++) {
            if (((C0043ac) this.f722p.get(i2)).aJ().equals(c0043ac.aJ())) {
                this.f722p.set(i2, c0043ac);
                z2 = true;
            }
        }
        if (!z2) {
            this.f722p.add(c0043ac);
        }
        return z2;
    }

    public void a(C0141x c0141x) {
        this.f728v.add(c0141x);
    }

    public List C() {
        return this.f728v;
    }

    public List D() {
        return this.f729w;
    }

    public void a(C0073bf c0073bf) {
        this.f729w.add(c0073bf);
    }

    public void a(bO bOVar) {
        this.f730x.put(bOVar.aJ(), bOVar);
    }

    public bO f(String str) {
        return (bO) this.f730x.get(str);
    }

    public Collection E() {
        return this.f730x.values();
    }

    public List F() {
        return this.f718l;
    }

    public List G() {
        return this.f707a;
    }
}
