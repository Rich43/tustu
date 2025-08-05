package k;

import G.C0126i;
import L.C0151h;
import L.C0154k;
import L.C0156m;
import L.C0157n;
import L.ab;
import ax.Q;
import h.C1737b;
import java.util.HashMap;
import java.util.Map;

/* renamed from: k.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:k/d.class */
public class C1756d {

    /* renamed from: b, reason: collision with root package name */
    private static C1756d f12898b = null;

    /* renamed from: a, reason: collision with root package name */
    Map f12899a = new HashMap();

    public static C1756d a() {
        if (f12898b == null) {
            f12898b = new C1756d();
            C1753a.a(C0157n.a());
            Q.a(C0157n.a());
            C1753a.a(new C1757e());
            if (C1737b.a().a("advancedMathFunctions")) {
                C0154k.a(C0157n.a());
                C1753a.a(C0157n.a());
                C0151h.a(true);
                C0154k.a(ab.a());
                C1753a.a(ab.a());
                ab.a().a(true);
                C0126i.f1248c = true;
            } else {
                C1753a.a(new C0156m());
                C0154k.a(new C0156m());
                C0151h.a(false);
            }
        }
        return f12898b;
    }

    public C1753a a(String str) {
        C1753a c1753a = (C1753a) this.f12899a.get(str);
        if (c1753a == null) {
            c1753a = new C1753a(str);
            this.f12899a.put(str, c1753a);
        }
        return c1753a;
    }
}
