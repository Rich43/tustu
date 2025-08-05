package A;

import G.C0129l;
import G.F;
import G.R;
import G.T;
import G.bS;
import bH.C;
import bH.aa;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.java2d.marlin.MarlinConst;

/* loaded from: TunerStudioMS.jar:A/j.class */
public class j {

    /* renamed from: m, reason: collision with root package name */
    private static j f17m = null;

    /* renamed from: e, reason: collision with root package name */
    static boolean f18e = false;

    /* renamed from: a, reason: collision with root package name */
    F f13a = null;

    /* renamed from: b, reason: collision with root package name */
    m f14b = null;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f15c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    k f16d = null;

    /* renamed from: n, reason: collision with root package name */
    private aa f19n = null;

    /* renamed from: o, reason: collision with root package name */
    private String f20o = "ECU";

    /* renamed from: p, reason: collision with root package name */
    private String f21p = "Q;S;\\x0d;";

    /* renamed from: q, reason: collision with root package name */
    private boolean f22q = false;

    /* renamed from: r, reason: collision with root package name */
    private String f23r = "Waiting for addition search items.";

    /* renamed from: f, reason: collision with root package name */
    HashMap f24f = new HashMap();

    /* renamed from: s, reason: collision with root package name */
    private w f25s = null;

    /* renamed from: g, reason: collision with root package name */
    int f26g = 0;

    /* renamed from: h, reason: collision with root package name */
    boolean f27h = false;

    /* renamed from: i, reason: collision with root package name */
    int f28i = 0;

    /* renamed from: j, reason: collision with root package name */
    int f29j = 0;

    /* renamed from: k, reason: collision with root package name */
    String f30k = null;

    /* renamed from: l, reason: collision with root package name */
    ArrayList f31l = null;

    private j() {
    }

    public static j a() {
        if (f17m == null) {
            f17m = new j();
        }
        return f17m;
    }

    public void a(List list) {
        f18e = false;
        if (this.f16d == null || !this.f16d.isAlive()) {
            this.f16d = new k(this, list);
            this.f16d.start();
            return;
        }
        f18e = true;
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (this.f16d != null && this.f16d.isAlive() && System.currentTimeMillis() - jCurrentTimeMillis < MarlinConst.statDump) {
        }
        f18e = false;
        this.f16d = new k(this, list);
        this.f16d.start();
        C.c("SearchThread already exists, killed and restarted");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(List list) {
        boolean z2;
        boolean zQ = false;
        R rC = T.a().c();
        this.f24f.clear();
        if (rC != null) {
            zQ = rC.C().q();
            rC.C().c();
        }
        f18e = false;
        this.f13a = new F();
        this.f14b = new m(this.f13a);
        this.f14b.a(this.f20o);
        this.f14b.f();
        List listE = e(this.f21p);
        this.f30k = f("Scanning Ports") + ": ";
        this.f26g = 0;
        this.f27h = false;
        this.f28i = 0;
        this.f29j = 0;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            this.f28i += ((x) it.next()).c();
        }
        d(f("Beginning Device Scan"));
        for (int i2 = 0; i2 < list.size() && !f18e; i2++) {
            x xVar = (x) list.get(i2);
            f fVarD = xVar.d();
            if (fVarD == null) {
                C.b("Null ControllerInterface for searchInterface, skippings");
            } else {
                for (String str : ((x) list.get(i2)).b()) {
                    Object obj = ((x) list.get(i2)).b(str).get(0);
                    try {
                        fVarD.a(str, obj);
                    } catch (s e2) {
                        C.d("Invalid Search Setting: " + str + "=" + obj + ", " + e2.getLocalizedMessage());
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                this.f31l = new ArrayList();
                List listB = ((x) list.get(i2)).b();
                for (int i3 = 0; i3 < listB.size() && !f18e; i3++) {
                    String str2 = (String) listB.get(i3);
                    for (Object obj2 : ((x) list.get(i2)).b(str2)) {
                        try {
                            fVarD.a(str2, obj2);
                        } catch (s e4) {
                            Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        }
                        if (listB.size() > 1) {
                            for (String str3 : ((x) list.get(i2)).b()) {
                                for (Object obj3 : ((x) list.get(i2)).b(str3)) {
                                    if (obj3 != null && !str3.equals(str2) && !this.f31l.contains(obj2.toString() + obj3) && !this.f31l.contains(obj3.toString() + obj2)) {
                                        try {
                                            fVarD.a(str3, obj3);
                                        } catch (s e5) {
                                            C.d("Invalid Search Setting: " + str3 + "=" + obj3 + ", " + e5.getLocalizedMessage());
                                        }
                                        if (f18e) {
                                            break;
                                        } else {
                                            a(xVar, fVarD, listE, obj2, obj3);
                                        }
                                    }
                                }
                            }
                        } else {
                            a(xVar, fVarD, listE, "", obj2);
                        }
                        if (!f18e) {
                        }
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception e6) {
                    }
                }
                a(xVar);
                boolean z3 = false;
                while (true) {
                    z2 = z3;
                    if (!this.f22q || list.size() - 1 != i2) {
                        break;
                    }
                    try {
                        C.d("Waiting for addtional search Items.");
                        d(f(this.f23r));
                        Thread.sleep(1000L);
                    } catch (InterruptedException e7) {
                        Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                    }
                    z3 = true;
                }
                if (z2 && list.size() > i2 + 1) {
                    this.f28i += ((x) list.get(i2 + 1)).c();
                }
            }
        }
        if (this.f27h) {
            a(1.0d);
        } else {
            e();
        }
        this.f14b.g();
        this.f14b.c();
        if (zQ) {
            try {
                C.d("Detect Finished, going back online.");
                rC.C().d();
            } catch (C0129l e8) {
                Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
            }
        }
    }

    private void a(x xVar, f fVar, List list, Object obj, Object obj2) {
        this.f31l.add(obj.toString() + obj2);
        this.f29j++;
        a(this.f29j / this.f28i);
        if (this.f25s != null && this.f25s.c(obj2.toString())) {
            C.c("Not Searching " + fVar.n() + " it is marked as a bad device setting");
            return;
        }
        if (a(xVar, fVar)) {
            C.d("distinctSetting already found for: " + fVar.n());
            return;
        }
        bS bSVarA = null;
        try {
            try {
                try {
                    if (this.f25s != null) {
                        this.f25s.a(obj.toString());
                    }
                    d(this.f30k + fVar.n());
                    try {
                        Thread.sleep(200L);
                    } catch (Exception e2) {
                    }
                    bSVarA = this.f14b.a(fVar, list);
                    C.c("Finished Check:" + fVar.n() + "\nResult:" + ((Object) bSVarA));
                    if (this.f25s != null) {
                        this.f25s.b(obj.toString());
                    }
                } catch (C0129l e3) {
                    C.d(fVar.n() + " Controller not found");
                    d(this.f30k + fVar.n() + " " + f("not found"));
                    this.f26g++;
                    if (this.f25s != null) {
                        this.f25s.b(obj.toString());
                    }
                } catch (Exception e4) {
                    e4.printStackTrace();
                    if (this.f25s != null) {
                        this.f25s.b(obj.toString());
                    }
                }
            } catch (V.b e5) {
                C.d("no Controller found on " + fVar.n());
                if (this.f25s != null) {
                    this.f25s.b(obj.toString());
                }
            }
            if (bSVarA == null || bSVarA.b() == null || bSVarA.b().equals("")) {
                return;
            }
            this.f26g++;
            ArrayList arrayList = new ArrayList();
            for (r rVar : fVar.l()) {
                arrayList.add(new c(rVar.c(), fVar.a(rVar.c())));
            }
            if (!a(fVar.n(), fVar.h(), arrayList, bSVarA)) {
                b();
            }
            b(xVar, fVar);
            this.f27h = true;
        } catch (Throwable th) {
            if (this.f25s != null) {
                this.f25s.b(obj.toString());
            }
            throw th;
        }
    }

    private boolean a(String str, String str2, List list, bS bSVar) {
        boolean z2 = true;
        Iterator it = this.f15c.iterator();
        while (it.hasNext()) {
            if (!((o) it.next()).a(str, str2, list, bSVar)) {
                z2 = false;
            }
        }
        return z2;
    }

    private void e() {
        Iterator it = this.f15c.iterator();
        while (it.hasNext()) {
            o oVar = (o) it.next();
            oVar.b(1.0d);
            oVar.a();
        }
    }

    private void a(double d2) {
        if (this.f22q) {
            d2 *= 0.9d;
        }
        Iterator it = this.f15c.iterator();
        while (it.hasNext()) {
            ((o) it.next()).b(d2);
        }
    }

    private void d(String str) {
        Iterator it = this.f15c.iterator();
        while (it.hasNext()) {
            ((o) it.next()).a(str);
        }
    }

    private void a(x xVar) {
        Iterator it = this.f15c.iterator();
        while (it.hasNext()) {
            ((o) it.next()).a(xVar);
        }
    }

    private List e(String str) {
        ArrayList arrayList = new ArrayList();
        if (str != null && !str.equals("")) {
            StringTokenizer stringTokenizer = new StringTokenizer(str, ";");
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                if (strNextToken.contains("$tsCanId")) {
                    arrayList.add(F.d(strNextToken, 0));
                } else {
                    arrayList.add(F.d(strNextToken, -1));
                }
            }
        }
        return arrayList;
    }

    public void a(o oVar) {
        if (this.f15c.contains(oVar)) {
            return;
        }
        this.f15c.add(oVar);
    }

    public void b(o oVar) {
        this.f15c.remove(oVar);
    }

    public void b() {
        f18e = true;
    }

    public void a(w wVar) {
        this.f25s = wVar;
    }

    private String f(String str) {
        return this.f19n != null ? this.f19n.a(str) : str;
    }

    private boolean a(x xVar, f fVar) {
        for (String str : xVar.a()) {
            if (a(fVar.h(), str).contains(fVar.a(str))) {
                return true;
            }
        }
        return false;
    }

    private void b(x xVar, f fVar) {
        for (String str : xVar.a()) {
            a(fVar.h(), str).add(fVar.a(str));
        }
    }

    private List a(String str, String str2) {
        HashMap map = (HashMap) this.f24f.get(str);
        if (map == null) {
            map = new HashMap();
            this.f24f.put(str, map);
        }
        List arrayList = (List) map.get(str2);
        if (arrayList == null) {
            arrayList = new ArrayList();
            map.put(str2, arrayList);
        }
        return arrayList;
    }

    public void a(String str) {
        this.f20o = str;
    }

    public void b(String str) {
        this.f21p = str;
    }

    public void c() {
        this.f22q = true;
    }

    public void d() {
        this.f22q = false;
    }

    public void c(String str) {
        this.f23r = str;
    }
}
