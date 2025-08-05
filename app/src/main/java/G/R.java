package G;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: TunerStudioMS.jar:G/R.class */
public class R implements aI, InterfaceC0110cp {

    /* renamed from: b, reason: collision with root package name */
    private C0045ae f477b;

    /* renamed from: c, reason: collision with root package name */
    private String f478c;

    /* renamed from: d, reason: collision with root package name */
    private Map f479d;

    /* renamed from: e, reason: collision with root package name */
    private C0054an f480e;

    /* renamed from: f, reason: collision with root package name */
    private J f481f;

    /* renamed from: g, reason: collision with root package name */
    private String f482g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f483h;

    /* renamed from: i, reason: collision with root package name */
    private HashMap f484i;

    /* renamed from: j, reason: collision with root package name */
    private Map f485j;

    /* renamed from: k, reason: collision with root package name */
    private Y f486k;

    /* renamed from: l, reason: collision with root package name */
    private final Map f487l;

    /* renamed from: m, reason: collision with root package name */
    private C0111cq f488m;

    /* renamed from: n, reason: collision with root package name */
    private HashMap f489n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f490o;

    /* renamed from: a, reason: collision with root package name */
    public static int f491a = -1;

    /* renamed from: p, reason: collision with root package name */
    private String f492p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f493q;

    public R() {
        this.f477b = new C0045ae();
        this.f478c = "";
        this.f479d = new HashMap();
        this.f480e = null;
        this.f481f = null;
        this.f482g = "";
        this.f483h = false;
        this.f484i = null;
        this.f485j = null;
        this.f486k = null;
        this.f487l = new HashMap();
        this.f488m = null;
        this.f489n = new HashMap();
        this.f490o = false;
        this.f492p = null;
        this.f493q = false;
        this.f480e = new C0054an(this);
    }

    private R(Y y2) {
        this.f477b = new C0045ae();
        this.f478c = "";
        this.f479d = new HashMap();
        this.f480e = null;
        this.f481f = null;
        this.f482g = "";
        this.f483h = false;
        this.f484i = null;
        this.f485j = null;
        this.f486k = null;
        this.f487l = new HashMap();
        this.f488m = null;
        this.f489n = new HashMap();
        this.f490o = false;
        this.f492p = null;
        this.f493q = false;
        this.f486k = y2;
        this.f487l.clear();
        this.f487l.put("Primary", y2);
    }

    public R a(Y y2) {
        R r2 = new R(y2);
        r2.a(this.f477b.a(r2.ab()));
        r2.f489n = new HashMap();
        r2.f488m = this.f488m;
        r2.f481f = this.f481f;
        r2.f480e = null;
        r2.f478c = this.f478c;
        r2.f482g = this.f482g;
        r2.f479d = this.f479d;
        return r2;
    }

    public R a() {
        R r2 = new R(new Y(this));
        r2.a(this.f477b.a(r2.ab()));
        r2.f489n = new HashMap();
        r2.f488m = this.f488m;
        r2.f481f = this.f481f;
        r2.f480e = null;
        r2.f478c = this.f478c;
        r2.f482g = this.f482g;
        r2.f479d = this.f479d;
        return r2;
    }

    public boolean b() {
        String strI = i();
        return strI != null && (strI.startsWith("MSII Rev 2.8") || strI.startsWith("MSII Rev 2.9") || strI.startsWith("MSII Rev 3") || strI.startsWith("MS3 Format") || strI.startsWith("MegaShift") || strI.startsWith("MShift") || strI.startsWith("Trans") || d("CAN_COMMANDS") != null || d("CAN_COMMANDS_OFF") != null || d("SERIAL") != null || d("SERIAL_OFF") != null);
    }

    public void a(String str) {
        if (this.f478c != null && !this.f478c.isEmpty()) {
            C0125h.a().b(this.f478c);
        }
        this.f478c = str;
        if (this.f478c == null || this.f478c.isEmpty()) {
            return;
        }
        C0125h.a().a(this);
    }

    @Override // G.aI, G.InterfaceC0110cp
    public String c() {
        return this.f478c;
    }

    public void a(bD bDVar) {
        this.f477b.a(bDVar);
    }

    public void a(InterfaceC0131n interfaceC0131n) {
        this.f480e.a(interfaceC0131n);
    }

    public void b(InterfaceC0131n interfaceC0131n) {
        this.f480e.b(interfaceC0131n);
    }

    public void d() {
        if (this.f480e != null) {
            this.f480e.b();
        }
    }

    public void a(bT bTVar) {
        this.f480e.a(bTVar);
    }

    public void b(bT bTVar) {
        this.f480e.b(bTVar);
    }

    public void c(InterfaceC0131n interfaceC0131n) {
    }

    public void d(InterfaceC0131n interfaceC0131n) {
    }

    public bD e() {
        return this.f477b.t();
    }

    public void a(C0052al c0052al) {
        this.f477b.m().add(c0052al);
    }

    public boolean a(C0043ac c0043ac) {
        return this.f477b.a(c0043ac);
    }

    public boolean b(C0043ac c0043ac) {
        return this.f477b.n().remove(c0043ac);
    }

    public ArrayList f() {
        return this.f477b.m();
    }

    public ArrayList g() {
        return this.f477b.n();
    }

    public void a(aM aMVar) {
        aM aMVar2 = (aM) ak().put(aMVar.aJ(), aMVar);
        if (aMVar2 != null) {
            this.f477b.G().remove(aMVar2);
        }
        this.f477b.G().add(aMVar);
    }

    public void b(String str) {
        aM aMVar = (aM) ak().remove(str);
        if (aMVar != null) {
            this.f477b.G().remove(aMVar);
            ((List) this.f479d.get(Integer.valueOf(aMVar.d()))).remove(aMVar);
        }
    }

    @Override // G.aI
    public aM c(String str) {
        return (aM) ak().get(str);
    }

    public void a(C0135r c0135r) {
        m().put(c0135r.aJ(), c0135r);
    }

    @Override // G.aI
    public C0135r d(String str) {
        return (C0135r) m().get(str);
    }

    @Override // G.aI
    public Y h() {
        return p();
    }

    public void a(F f2) {
        f2.l(c());
        this.f477b.a(f2);
    }

    public void e(String str) {
        this.f477b.b(str);
    }

    public String i() {
        String strX = this.f477b.x();
        String strW = this.f477b.w();
        return (strX == null || strW == null || strW.isEmpty()) ? strX : strW + strX;
    }

    public void j() {
        b(new Y(this));
    }

    private HashMap ak() {
        return this.f477b.a();
    }

    public String[] k() {
        Set setKeySet = ak().keySet();
        return (String[]) setKeySet.toArray(new String[setKeySet.size()]);
    }

    public Iterator a(int i2) {
        if (this.f479d.isEmpty()) {
            for (int i3 = -1; i3 < O().g(); i3++) {
                this.f479d.put(Integer.valueOf(i3), new ArrayList());
            }
            for (aM aMVar : this.f477b.G()) {
                ((ArrayList) this.f479d.get(Integer.valueOf(aMVar.d()))).add(aMVar);
            }
        }
        return ((ArrayList) this.f479d.get(Integer.valueOf(i2))).iterator();
    }

    public Collection l() {
        return this.f477b.G();
    }

    public HashMap m() {
        return this.f477b.b();
    }

    public Iterator n() {
        return this.f477b.c().values().iterator();
    }

    public void a(C0072be c0072be) {
        this.f477b.c().put(c0072be.aJ(), c0072be);
    }

    public C0072be f(String str) {
        return (C0072be) this.f477b.c().get(str);
    }

    public Iterator o() {
        return this.f477b.d().values().iterator();
    }

    public void a(C0076bi c0076bi) {
        this.f477b.d().put(c0076bi.aJ(), c0076bi);
    }

    public Y p() {
        return this.f486k;
    }

    public void b(Y y2) {
        this.f486k = y2;
        this.f487l.clear();
        this.f487l.put("Primary", y2);
        y2.a((InterfaceC0042ab) this.f480e);
        y2.a((InterfaceC0071bd) this.f480e);
    }

    public void a(aH aHVar) {
        this.f477b.f().put(aHVar.aJ(), aHVar);
        this.f477b.e().add(aHVar);
    }

    public Iterator q() {
        return this.f477b.f().values().iterator();
    }

    public int r() {
        return this.f477b.f().size();
    }

    @Override // G.InterfaceC0110cp
    public String[] s() {
        ArrayList arrayList = new ArrayList();
        for (aH aHVar : this.f477b.f().values()) {
            if (aHVar.s()) {
                arrayList.add(aHVar.aJ());
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    @Override // G.aI
    public aH g(String str) {
        return (aH) this.f477b.f().get(str);
    }

    public aH b(int i2) {
        try {
            return (aH) this.f477b.e().get(i2);
        } catch (IndexOutOfBoundsException e2) {
            return null;
        }
    }

    public void a(C0069bb c0069bb) {
        this.f477b.o().add(c0069bb);
    }

    public void b(C0069bb c0069bb) {
        this.f477b.p().add(c0069bb);
    }

    public ArrayList t() {
        return this.f477b.o();
    }

    public ArrayList u() {
        return this.f477b.p();
    }

    public String[] v() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f477b.o().iterator();
        while (it.hasNext()) {
            C0069bb c0069bb = (C0069bb) it.next();
            if (c0069bb.s()) {
                arrayList.add(c0069bb.aJ());
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public C0069bb h(String str) {
        Iterator it = this.f477b.o().iterator();
        while (it.hasNext()) {
            C0069bb c0069bb = (C0069bb) it.next();
            if (c0069bb.aJ().equals(str)) {
                return c0069bb;
            }
        }
        return null;
    }

    public C0069bb i(String str) {
        Iterator it = this.f477b.p().iterator();
        while (it.hasNext()) {
            C0069bb c0069bb = (C0069bb) it.next();
            if (c0069bb.aJ().equals(str)) {
                return c0069bb;
            }
        }
        return null;
    }

    public void a(InterfaceC0120cz interfaceC0120cz) {
        this.f480e.a(interfaceC0120cz);
    }

    public void b(InterfaceC0120cz interfaceC0120cz) {
        this.f480e.b(interfaceC0120cz);
    }

    public void a(String str, String[] strArr) {
        this.f477b.i().put(str, strArr);
    }

    public String[] j(String str) {
        return (String[]) this.f477b.i().get(str);
    }

    public void a(String[] strArr) {
        a("FrontPage", strArr);
    }

    public void a(C0048ah c0048ah) {
        this.f477b.g().put(c0048ah.aJ(), c0048ah);
    }

    public C0048ah k(String str) {
        return (C0048ah) this.f477b.g().get(str);
    }

    public void a(C0051ak c0051ak) {
        this.f477b.h().add(c0051ak);
    }

    public int w() {
        int i2 = 0;
        Iterator it = this.f477b.h().iterator();
        while (it.hasNext()) {
            if (((C0051ak) it.next()).k()) {
                i2++;
            }
        }
        return i2;
    }

    public void a(C0096cb c0096cb) {
        this.f477b.s().add(c0096cb);
    }

    public List x() {
        return this.f477b.s();
    }

    public boolean y() {
        return !this.f477b.s().isEmpty();
    }

    public void l(String str) throws V.g {
        if (!str.equals(C0096cb.f1069e) && !str.equals(C0096cb.f1070f) && !str.equals(C0096cb.f1071g) && !str.equals(C0096cb.f1072h) && !str.equals(C0096cb.f1073i) && !str.equals(C0096cb.f1074j) && !str.equals(C0096cb.f1075k) && !str.equals(C0096cb.f1076l)) {
            throw new V.g("Unsupported Standard Logger '" + str + "'. Supported types: " + C0096cb.f1069e + ", " + C0096cb.f1070f + ", " + C0096cb.f1071g + ", " + C0096cb.f1072h + ", " + C0096cb.f1073i + ", " + C0096cb.f1074j + ", " + C0096cb.f1075k + ", " + C0096cb.f1076l + " ");
        }
        this.f477b.q().add(str);
    }

    public boolean z() {
        return !this.f477b.q().isEmpty();
    }

    public boolean m(String str) {
        return this.f477b.q().contains(str);
    }

    public C0051ak n(String str) {
        C0051ak c0051ak = null;
        Iterator it = this.f477b.h().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            C0051ak c0051ak2 = (C0051ak) it.next();
            if (c0051ak2.aJ().equals(str)) {
                c0051ak = c0051ak2;
                break;
            }
        }
        return c0051ak;
    }

    public Iterator A() {
        if (this.f477b.h() != null) {
            return this.f477b.h().iterator();
        }
        return null;
    }

    public Iterator B() {
        if (this.f477b.g() == null || this.f477b.g().values() == null) {
            return null;
        }
        return this.f477b.g().values().iterator();
    }

    public void a(String str, String str2) {
        aM aMVarC = c(str);
        if (aMVarC == null) {
            bH.C.b(str + " not in current configuration. Value ignored.");
        } else {
            aMVarC.a(this.f486k, str2);
        }
    }

    public void a(String str, double[][] dArr) {
        aM aMVarC = c(str);
        if (aMVarC == null) {
            bH.C.b(str + " not in current configuration. Value ignored.");
        } else {
            aMVarC.a(this.f486k, dArr);
        }
    }

    public J C() {
        return this.f481f;
    }

    public void b(String str, String str2) {
        this.f489n.put(str, str2);
    }

    public String o(String str) {
        return (String) this.f489n.get(str);
    }

    public void D() {
        this.f489n.clear();
    }

    public void p(String str) {
        this.f489n.put(str, "");
    }

    public Iterator E() {
        return this.f489n.keySet().iterator();
    }

    public void a(J j2) {
        this.f481f = j2;
        j2.b(O());
        this.f481f.a((aG) this.f480e);
        this.f481f.a((InterfaceC0124g) this.f480e);
        this.f481f.a((InterfaceC0044ad) this.f480e);
        d(this.f481f);
    }

    public void b(J j2) {
        this.f481f.a(j2);
        this.f481f = j2;
    }

    public J c(J j2) {
        this.f481f = j2;
        j2.a((aG) this.f480e);
        j2.a((InterfaceC0124g) this.f480e);
        j2.a((InterfaceC0044ad) this.f480e);
        d(j2);
        return j2;
    }

    private void d(J j2) {
        for (aM aMVar : ak().values()) {
            if (!aMVar.B()) {
                for (int i2 = 0; i2 < aMVar.y(); i2++) {
                    if (aMVar.d() >= 0) {
                        j2.a(this.f478c, aMVar.d(), aMVar.g() + i2, false);
                    }
                }
            }
        }
    }

    public String F() {
        return this.f482g;
    }

    public void q(String str) {
        this.f482g = str;
    }

    public C0111cq G() {
        if (this.f488m == null) {
            this.f488m = new C0111cq();
        }
        return this.f488m;
    }

    public void H() {
        this.f480e.d();
    }

    public void I() {
        this.f480e.e();
        C().a(this.f477b.v());
    }

    public void c(int i2) {
        this.f477b.v().E(i2);
    }

    public String J() {
        return this.f477b.v().av() + "";
    }

    @Override // G.aI
    public R K() {
        return this;
    }

    public void a(cU cUVar) {
        this.f480e.a(cUVar);
    }

    public void b(cU cUVar) {
        this.f480e.b(cUVar);
    }

    public void a(dk dkVar) {
        this.f477b.j().put(dkVar.b(), dkVar);
        this.f477b.F().add(dkVar.b());
    }

    public void a(dc dcVar) {
        this.f477b.k().put(dcVar.b(), dcVar);
    }

    public dk r(String str) {
        return (dk) this.f477b.j().get(str);
    }

    public dc s(String str) {
        return (dc) this.f477b.k().get(str);
    }

    public Iterator L() {
        return this.f477b.F().iterator();
    }

    public Iterator M() {
        return this.f477b.k().keySet().iterator();
    }

    public void a(dn dnVar) {
        this.f477b.l().put(dnVar.c(), dnVar);
    }

    public dn t(String str) {
        return (dn) this.f477b.l().get(str);
    }

    public Iterator N() {
        return this.f477b.l().keySet().iterator();
    }

    public String u(String str) {
        String str2 = (String) this.f477b.u().get(str);
        return str2 != null ? str2 : str;
    }

    public void c(String str, String str2) {
        this.f477b.u().put(str, str2);
    }

    public F O() {
        return this.f477b.v();
    }

    public String P() {
        return this.f477b.v().y();
    }

    public String Q() {
        return this.f477b.y();
    }

    public void v(String str) {
        this.f477b.c(str);
    }

    public void a(Thread thread) {
        this.f480e.a(thread);
    }

    public void b(Thread thread) {
        this.f480e.b(thread);
    }

    @Override // G.aI
    public boolean R() {
        return C() != null && C().q();
    }

    public boolean S() {
        return O() == null || (O().F() && !O().ap());
    }

    void T() {
        this.f480e.f();
        this.f483h = true;
    }

    public boolean U() {
        return this.f477b.A();
    }

    public void a(boolean z2) {
        this.f477b.a(z2);
    }

    public void d(String str, String str2) throws V.g {
        if (str2.length() > 1500) {
            throw new V.g("Context Help length is " + str2.length() + ", this should be 1500 or less characters in length.");
        }
        this.f477b.r().put(str, str2);
    }

    public String w(String str) {
        String str2 = (String) this.f477b.r().get(str);
        if (str2 == null && this.f485j != null) {
            str2 = (String) this.f485j.get(str);
        }
        return str2;
    }

    public void a(Map map) {
        this.f485j = map;
    }

    public String V() {
        return this.f477b.z();
    }

    public void x(String str) {
        this.f477b.d(str);
    }

    public void y(String str) {
        this.f477b.a(str);
    }

    public boolean W() {
        return this.f483h;
    }

    public void X() {
        for (aH aHVar : this.f477b.e()) {
            if (aHVar.u()) {
                aHVar.c(true);
            }
        }
    }

    public void Y() {
        for (aH aHVar : this.f477b.e()) {
            if (aHVar.u()) {
                aHVar.c(false);
            }
        }
    }

    protected C0103ci a(int i2, int i3) {
        al();
        for (C0103ci c0103ci : (List) this.f484i.get(Integer.valueOf(i2))) {
            if (c0103ci.a() > i3) {
                return c0103ci;
            }
        }
        return null;
    }

    private void al() {
        if (this.f484i == null) {
            this.f484i = new HashMap();
            for (int i2 = 0; i2 < this.f477b.v().g(); i2++) {
                ArrayList arrayList = new ArrayList();
                this.f484i.put(Integer.valueOf(i2), arrayList);
                C0103ci c0103ci = null;
                Iterator itA = a(i2);
                while (itA.hasNext()) {
                    aM aMVar = (aM) itA.next();
                    if (aMVar.M()) {
                        if (c0103ci == null || c0103ci.a() + c0103ci.b() < aMVar.g()) {
                            c0103ci = new C0103ci(aMVar.g(), aMVar.y());
                            arrayList.add(c0103ci);
                        } else {
                            c0103ci.b(Math.max(c0103ci.b(), (aMVar.g() - c0103ci.a()) + aMVar.y()));
                            c0103ci.a(Math.min(c0103ci.a(), aMVar.g()));
                        }
                    }
                }
            }
        }
    }

    protected C0103ci b(int i2, int i3) {
        al();
        C0103ci c0103ci = null;
        for (C0103ci c0103ci2 : (List) this.f484i.get(Integer.valueOf(i2))) {
            if (c0103ci2.a() > i3) {
                break;
            }
            c0103ci = c0103ci2;
        }
        return c0103ci;
    }

    public void z(String str) {
        this.f477b.f().remove(str);
        for (int i2 = 0; i2 < this.f477b.e().size(); i2++) {
            if (((aH) this.f477b.e().get(i2)).aJ().equals(str)) {
                this.f477b.e().remove(i2);
                return;
            }
        }
    }

    public void b(C0048ah c0048ah) {
        this.f477b.g().remove(c0048ah.aJ());
    }

    public void A(String str) {
        this.f477b.e(str);
    }

    public String Z() {
        String strB = this.f477b.B();
        return (strB == null || strB.isEmpty()) ? P() : strB;
    }

    public void aa() {
        this.f479d.clear();
    }

    public C0045ae ab() {
        return this.f477b;
    }

    public void a(C0045ae c0045ae) {
        this.f477b = c0045ae;
    }

    @Override // G.aI
    public String ac() {
        return c();
    }

    public void a(C0141x c0141x) {
        this.f477b.a(c0141x);
    }

    public List ad() {
        return this.f477b.C();
    }

    public C0141x B(String str) {
        for (C0141x c0141x : this.f477b.C()) {
            if (c0141x.a().equals(str)) {
                return c0141x;
            }
        }
        return null;
    }

    public boolean ae() {
        return this.f490o;
    }

    public void b(boolean z2) {
        this.f490o = z2;
    }

    public void a(C0073bf c0073bf) {
        this.f477b.a(c0073bf);
    }

    public List af() {
        return this.f477b.D();
    }

    public void a(bO bOVar) {
        this.f477b.a(bOVar);
    }

    public bO C(String str) {
        return this.f477b.f(str);
    }

    public Collection ag() {
        return this.f477b.E();
    }

    public void ah() {
        this.f480e.e();
    }

    public boolean ai() {
        return O().al().equals("basicRequestReply") && (C() instanceof bQ.l);
    }

    public void D(String str) {
        this.f492p = str;
    }

    public boolean aj() {
        return this.f493q;
    }

    public void c(boolean z2) {
        this.f493q = z2;
    }
}
