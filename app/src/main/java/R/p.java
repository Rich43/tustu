package r;

import w.C1888a;
import w.C1889b;
import w.C1890c;

/* loaded from: TunerStudioMS.jar:r/p.class */
public class p {

    /* renamed from: a, reason: collision with root package name */
    private static p f13504a = null;

    private p() {
    }

    public static p a() {
        if (f13504a == null) {
            f13504a = new p();
        }
        return f13504a;
    }

    public o b() {
        o c1890c = null;
        try {
            c1890c = C1798a.f13270d.contains("VEMSDashFilter") ? new C1890c() : (C1798a.f13268b.equals(C1798a.f13337as) || C1798a.f13268b.equals(C1798a.f13338at)) ? new C1888a() : new C1889b();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return c1890c;
    }
}
