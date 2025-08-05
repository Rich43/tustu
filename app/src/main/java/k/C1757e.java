package k;

import L.S;
import ax.AbstractC0902e;
import ax.C0885A;
import ax.ab;
import ax.ac;
import java.util.List;

/* renamed from: k.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:k/e.class */
public class C1757e extends AbstractC0902e {
    @Override // ax.AbstractC0902e
    public ac a(String str, List list) throws C0885A {
        ac acVarC = null;
        if (str.equalsIgnoreCase("smoothInt")) {
            acVarC = b(str, list);
        } else if (str.equalsIgnoreCase("table")) {
            acVarC = c(str, list);
        }
        return acVarC;
    }

    private ac b(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 0);
        }
        return new C1758f((ab) list.get(0));
    }

    private ac c(String str, List list) throws C0885A {
        if (list.size() != 2) {
            throw new C0885A(str, list.size(), 2);
        }
        return new S((ab) list.get(0), (ab) list.get(1));
    }
}
