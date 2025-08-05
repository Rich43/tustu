package L;

import ax.AbstractC0902e;
import ax.C0885A;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:L/ab.class */
public class ab extends AbstractC0902e {

    /* renamed from: a, reason: collision with root package name */
    private static ab f1633a = null;

    /* renamed from: b, reason: collision with root package name */
    private boolean f1634b = false;

    /* renamed from: c, reason: collision with root package name */
    private Map f1635c = new HashMap();

    private ab() {
    }

    public static ab a() {
        if (f1633a == null) {
            f1633a = new ab();
        }
        return f1633a;
    }

    public void a(AbstractC0161r abstractC0161r) {
        this.f1635c.put(abstractC0161r.a().toLowerCase(), abstractC0161r);
    }

    @Override // ax.AbstractC0902e
    public ax.ac a(String str, List list) throws C0885A {
        ax.ac c0149f = null;
        if (str.equalsIgnoreCase("isUltraFunctionsAvaliable") || str.equalsIgnoreCase("isUltraFunctionsAvailable")) {
            c0149f = this.f1634b ? new C0149f(1.0d) : new C0149f(0.0d);
        } else if (a(str) && !this.f1634b) {
            c0149f = new C0150g(str);
        } else if (str.equalsIgnoreCase("timeToExceed")) {
            c0149f = b(str, list);
        } else if (this.f1635c.containsKey(str.toLowerCase())) {
            AbstractC0161r abstractC0161r = (AbstractC0161r) this.f1635c.get(str.toLowerCase());
            if (list.size() < abstractC0161r.b() || list.size() > abstractC0161r.c()) {
                throw new C0885A(str, list.size(), abstractC0161r.b());
            }
            c0149f = abstractC0161r.a(list);
        }
        return c0149f;
    }

    private boolean a(String str) {
        return str.equalsIgnoreCase("timeToExceed");
    }

    private ax.ac b(String str, List list) throws C0885A {
        if (list.size() < 2 || list.size() > 4) {
            throw new C0885A(str, list.size(), 2);
        }
        return new Y(list);
    }

    public boolean b() {
        return this.f1634b;
    }

    public void a(boolean z2) {
        this.f1634b = z2;
    }
}
