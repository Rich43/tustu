package T;

import G.C0102ch;
import G.R;
import G.Y;
import G.aM;
import V.g;
import bH.C;
import bH.C0995c;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:T/e.class */
class e {

    /* renamed from: a, reason: collision with root package name */
    String f1872a;

    /* renamed from: b, reason: collision with root package name */
    Y f1873b;

    /* renamed from: c, reason: collision with root package name */
    R f1874c;

    /* renamed from: g, reason: collision with root package name */
    private String f1875g = null;

    /* renamed from: h, reason: collision with root package name */
    private boolean f1876h = false;

    /* renamed from: d, reason: collision with root package name */
    List f1877d = null;

    /* renamed from: i, reason: collision with root package name */
    private List f1878i = null;

    /* renamed from: e, reason: collision with root package name */
    boolean f1879e = false;

    /* renamed from: f, reason: collision with root package name */
    Map f1880f = new HashMap();

    e(String str, R r2) {
        this.f1872a = str;
        this.f1873b = r2.h();
        this.f1874c = r2;
    }

    public boolean a(Y y2) {
        if (this.f1877d == null) {
            this.f1877d = b();
        }
        if (this.f1878i != null && this.f1879e) {
            for (aM aMVar : this.f1878i) {
                try {
                } catch (g e2) {
                    C.a(e2.getLocalizedMessage());
                }
                if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_ARRAY) || aMVar.i().equals("string")) {
                    if (!C0995c.a(aMVar.i(y2), aMVar.i(this.f1873b), aMVar.u(), aMVar.A())) {
                        return false;
                    }
                } else if (aMVar.j(y2) != aMVar.j(this.f1873b)) {
                    return false;
                }
            }
            return true;
        }
        for (C0102ch c0102ch : this.f1877d) {
            for (int iB = c0102ch.b(); iB < c0102ch.b() + c0102ch.c(); iB++) {
                if (y2.b(c0102ch.a(), iB) != this.f1873b.b(c0102ch.a(), iB)) {
                    if (!c0102ch.d()) {
                        return false;
                    }
                    boolean z2 = false;
                    for (aM aMVar2 : a(c0102ch)) {
                        if (iB >= aMVar2.g() && iB < aMVar2.g() + aMVar2.y()) {
                            z2 = true;
                            try {
                                if (!aMVar2.f(y2).equals(aMVar2.f(this.f1873b))) {
                                    return false;
                                }
                            } catch (g e3) {
                            }
                        }
                    }
                    if (!z2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private List a(C0102ch c0102ch) {
        List arrayList = (List) this.f1880f.get(c0102ch);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f1880f.put(c0102ch, arrayList);
            Iterator itA = this.f1874c.a(c0102ch.a());
            while (itA.hasNext()) {
                aM aMVar = (aM) itA.next();
                if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_BITS) && aMVar.g() <= c0102ch.b() + c0102ch.c() && aMVar.g() + aMVar.y() > c0102ch.b()) {
                    arrayList.add(aMVar);
                }
            }
        }
        return arrayList;
    }

    private List b() {
        ArrayList<C0102ch> arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.f1873b.e(); i2++) {
            int[] iArrB = this.f1873b.b(i2);
            C0102ch c0102ch = null;
            for (int i3 = 0; i3 < iArrB.length; i3++) {
                if (c0102ch == null && iArrB[i3] != Y.f517j) {
                    c0102ch = new C0102ch(i2);
                    c0102ch.a(i3);
                } else if (c0102ch != null && (iArrB[i3] == Y.f517j || i3 == iArrB.length - 1)) {
                    c0102ch.b(i3 - c0102ch.b());
                    arrayList.add(c0102ch);
                    c0102ch = null;
                }
            }
        }
        this.f1879e = false;
        for (C0102ch c0102ch2 : arrayList) {
            Iterator itA = this.f1874c.a(c0102ch2.a());
            while (itA.hasNext()) {
                aM aMVar = (aM) itA.next();
                if (aMVar.a(c0102ch2.a(), c0102ch2.b(), c0102ch2.c()) && aMVar.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                    this.f1879e = true;
                    c0102ch2.a(true);
                }
            }
        }
        return arrayList;
    }

    public String a() {
        return this.f1875g;
    }

    public void a(String str) {
        this.f1875g = str;
    }

    public void a(List list) {
        this.f1878i = list;
    }
}
