package L;

import G.aI;
import ax.AbstractC0902e;
import ax.C0885A;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.List;

/* renamed from: L.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/h.class */
public class C0151h extends AbstractC0902e {

    /* renamed from: b, reason: collision with root package name */
    private aI f1659b;

    /* renamed from: c, reason: collision with root package name */
    private static boolean f1660c = true;

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1661a = C0157n.a().f1675a;

    public C0151h(aI aIVar) {
        this.f1659b = aIVar;
    }

    @Override // ax.AbstractC0902e
    public ax.ac a(String str, List list) throws C0885A {
        ax.ac c0149f = null;
        if (str.equalsIgnoreCase("timeNow")) {
            c0149f = b(str, list);
        }
        if (str.equalsIgnoreCase("systemTime")) {
            c0149f = c(str, list);
        } else if (str.equalsIgnoreCase("table")) {
            c0149f = d(str, list);
        } else if (str.equalsIgnoreCase("getWorkingLocalCanId")) {
            c0149f = e(str, list);
        } else if (str.equalsIgnoreCase("getChannelValueByOffset")) {
            c0149f = h(str, list);
        } else if (str.equalsIgnoreCase("getChannelScaleByOffset")) {
            c0149f = i(str, list);
        } else if (str.equalsIgnoreCase("getChannelTranslateByOffset")) {
            c0149f = j(str, list);
        } else if (str.equalsIgnoreCase("getChannelDigitsByOffset")) {
            c0149f = k(str, list);
        } else if (str.equalsIgnoreCase("getChannelMinByOffset")) {
            c0149f = l(str, list);
        } else if (str.equalsIgnoreCase("getChannelMaxByOffset")) {
            c0149f = m(str, list);
        } else if (str.equalsIgnoreCase(Constants.ELEMNAME_COUNTER_STRING)) {
            c0149f = q(str, list);
        } else if (str.equalsIgnoreCase("arrayValue")) {
            c0149f = n(str, list);
        } else if (f1660c && str.equalsIgnoreCase("tableLookup")) {
            c0149f = o(str, list);
        } else if (str.equalsIgnoreCase("tableLookup")) {
            c0149f = new C0150g(str);
        } else if (str.equalsIgnoreCase("getLogTime")) {
            c0149f = p(str, list);
        } else if (str.equalsIgnoreCase("isOnline")) {
            c0149f = f(str, list);
        } else if (str.equalsIgnoreCase("updateValueWhen")) {
            c0149f = g(str, list);
        } else if (str.equalsIgnoreCase("isAdvancedMathAvaliable") || str.equalsIgnoreCase("isAdvancedMathAvailable")) {
            c0149f = f1660c ? new C0149f(1.0d) : new C0149f(0.0d);
        } else if (str.equalsIgnoreCase("persistentAccumulate")) {
            c0149f = f1660c ? r(str, list) : new C0150g(str);
        }
        return c0149f;
    }

    public static void a(boolean z2) {
        f1660c = z2;
    }

    private ax.ac b(String str, List list) throws C0885A {
        if (list.size() != 0) {
            throw new C0885A(str, list.size(), 0);
        }
        return new X();
    }

    private ax.ac c(String str, List list) throws C0885A {
        if (list.size() != 0) {
            throw new C0885A(str, list.size(), 0);
        }
        return new R();
    }

    private ax.ac d(String str, List list) throws C0885A {
        if (list.size() != 2) {
            throw new C0885A(str, list.size(), 2);
        }
        return new S((ax.ab) list.get(0), (ax.ab) list.get(1));
    }

    private ax.ac e(String str, List list) throws C0885A {
        if (list.isEmpty()) {
            return new ad();
        }
        throw new C0885A(str, list.size(), 0);
    }

    private ax.ac f(String str, List list) throws C0885A {
        if (list.isEmpty()) {
            return new D(this.f1659b);
        }
        throw new C0885A(str, list.size(), 0);
    }

    private ax.ac g(String str, List list) throws C0885A {
        if (list.size() < 2 || list.size() > 3) {
            throw new C0885A(str, list.size(), 2);
        }
        return new ac(list);
    }

    private ax.ac h(String str, List list) throws C0885A {
        if (list.size() == 1) {
            return new C0162s(this.f1659b, (ax.ab) list.get(0));
        }
        if (list.size() == 2) {
            return new C0162s((ax.ab) list.get(0), (ax.ab) list.get(1));
        }
        throw new C0885A(str, list.size(), 1);
    }

    private ax.ac i(String str, List list) throws C0885A {
        if (list.size() == 1) {
            return new C0166w(this.f1659b, (ax.ab) list.get(0));
        }
        if (list.size() == 2) {
            return new C0166w((ax.ab) list.get(0), (ax.ab) list.get(1));
        }
        throw new C0885A(str, list.size(), 1);
    }

    private ax.ac j(String str, List list) throws C0885A {
        if (list.size() == 1) {
            return new C0167x(this.f1659b, (ax.ab) list.get(0));
        }
        if (list.size() == 2) {
            return new C0167x((ax.ab) list.get(0), (ax.ab) list.get(1));
        }
        throw new C0885A(str, list.size(), 1);
    }

    private ax.ac k(String str, List list) throws C0885A {
        if (list.size() == 1) {
            return new C0163t(this.f1659b, (ax.ab) list.get(0));
        }
        if (list.size() == 2) {
            return new C0163t((ax.ab) list.get(0), (ax.ab) list.get(1));
        }
        throw new C0885A(str, list.size(), 1);
    }

    private ax.ac l(String str, List list) throws C0885A {
        if (list.size() == 1) {
            return new C0165v(this.f1659b, (ax.ab) list.get(0));
        }
        if (list.size() == 2) {
            return new C0165v((ax.ab) list.get(0), (ax.ab) list.get(1));
        }
        throw new C0885A(str, list.size(), 1);
    }

    private ax.ac m(String str, List list) throws C0885A {
        if (list.size() == 1) {
            return new C0164u(this.f1659b, (ax.ab) list.get(0));
        }
        if (list.size() == 2) {
            return new C0164u((ax.ab) list.get(0), (ax.ab) list.get(1));
        }
        throw new C0885A(str, list.size(), 1);
    }

    private ax.ac n(String str, List list) throws C0885A {
        if (list.size() == 2) {
            return new C0147d(this.f1659b, (ax.ab) list.get(0), (ax.ab) list.get(1));
        }
        throw new C0885A(str, list.size(), 2);
    }

    private ax.ac o(String str, List list) throws C0885A {
        if (list.size() == 5) {
            return new T(this.f1659b, (ax.ab) list.get(0), (ax.ab) list.get(1), (ax.ab) list.get(2), (ax.ab) list.get(3), (ax.ab) list.get(4));
        }
        if (list.size() == 3) {
            return new U(this.f1659b, (ax.ab) list.get(0), (ax.ab) list.get(1), (ax.ab) list.get(2));
        }
        throw new C0885A("Proper usage: tableLookup(array.zArray, array.xArray, array.yArray, xChannel, yChannel), or for 1D tables: tableLookup(array.ValueArrayName, array.LookupArrayName, LookupChannelName)" + str, list.size(), 5);
    }

    private ax.ac p(String str, List list) throws C0885A {
        if (list.isEmpty()) {
            return new C0168y();
        }
        throw new C0885A(str, list.size(), 0);
    }

    private ax.ac q(String str, List list) throws C0885A {
        if (list.isEmpty()) {
            return new L(this.f1661a);
        }
        throw new C0885A(str, list.size(), 0);
    }

    private ax.ac r(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        String string = ((ax.ab) list.get(0)).toString();
        G.R rC = G.T.a().c();
        if (rC != null && this.f1659b.c().equals(rC.c())) {
            rC.c();
        }
        C0145b c0145b = new C0145b((ax.ab) list.get(0), this.f1661a, new C0153j(this, new C0152i(this), string));
        I.k.a().a(this.f1659b.c(), string, c0145b);
        return c0145b;
    }

    public void a(aI aIVar) {
        this.f1659b = aIVar;
    }
}
