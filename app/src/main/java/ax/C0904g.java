package ax;

import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.List;

/* renamed from: ax.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/g.class */
class C0904g extends AbstractC0902e {
    C0904g() {
    }

    @Override // ax.AbstractC0902e
    public ac a(String str, List list) throws C0885A {
        ac acVarQ = null;
        if (str.equalsIgnoreCase("sin")) {
            acVarQ = b(str, list);
        } else if (str.equalsIgnoreCase("cos")) {
            acVarQ = c(str, list);
        } else if (str.equalsIgnoreCase("tan")) {
            acVarQ = d(str, list);
        } else if (str.equalsIgnoreCase("asin")) {
            acVarQ = e(str, list);
        } else if (str.equalsIgnoreCase("acos")) {
            acVarQ = f(str, list);
        } else if (str.equalsIgnoreCase("atan")) {
            acVarQ = g(str, list);
        } else if (str.equalsIgnoreCase("abs")) {
            acVarQ = h(str, list);
        } else if (str.equalsIgnoreCase("pow")) {
            acVarQ = i(str, list);
        } else if (str.equalsIgnoreCase("ceil")) {
            acVarQ = j(str, list);
        } else if (str.equalsIgnoreCase(Keywords.FUNC_FLOOR_STRING)) {
            acVarQ = k(str, list);
        } else if (str.equalsIgnoreCase(Keywords.FUNC_ROUND_STRING)) {
            acVarQ = l(str, list);
        } else if (str.equalsIgnoreCase("recip")) {
            acVarQ = m(str, list);
        } else if (str.equalsIgnoreCase("log")) {
            acVarQ = n(str, list);
        } else if (str.equalsIgnoreCase("log10")) {
            acVarQ = o(str, list);
        } else if (str.equalsIgnoreCase("sqrt")) {
            acVarQ = p(str, list);
        } else if (str.equalsIgnoreCase("exp")) {
            acVarQ = q(str, list);
        }
        return acVarQ;
    }

    private ac b(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new ah((ab) list.get(0));
    }

    private ac c(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0912o((ab) list.get(0));
    }

    private ac d(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new ak((ab) list.get(0));
    }

    private ac e(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0900c((ab) list.get(0));
    }

    private ac f(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0899b((ab) list.get(0));
    }

    private ac g(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0901d((ab) list.get(0));
    }

    private ac h(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0898a((ab) list.get(0));
    }

    private ac i(String str, List list) throws C0885A {
        if (list.size() != 2) {
            throw new C0885A(str, list.size(), 2);
        }
        return new ae((ab) list.get(0), (ab) list.get(1));
    }

    private ac j(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0913p((ab) list.get(0));
    }

    private ac k(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0922y((ab) list.get(0));
    }

    private ac l(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new ag((ab) list.get(0));
    }

    private ac m(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new af((ab) list.get(0));
    }

    private ac n(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0897M((ab) list.get(0));
    }

    private ac o(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new N((ab) list.get(0));
    }

    private ac p(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new ai((ab) list.get(0));
    }

    private ac q(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0919v((ab) list.get(0));
    }
}
