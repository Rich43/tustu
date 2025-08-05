package aP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import r.C1806i;

/* renamed from: aP.by, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/by.class */
public class C0256by {

    /* renamed from: d, reason: collision with root package name */
    private static C0256by f3103d = null;

    /* renamed from: a, reason: collision with root package name */
    static String f3104a = U.a.f1885a;

    /* renamed from: b, reason: collision with root package name */
    static String f3105b = U.a.f1886b;

    /* renamed from: c, reason: collision with root package name */
    List f3106c = new ArrayList();

    private C0256by() {
    }

    public static C0256by a() {
        if (f3103d == null) {
            f3103d = new C0256by();
        }
        return f3103d;
    }

    public void b() {
        if (this.f3106c.isEmpty()) {
            S.j jVarA = S.b.a().a("", f3104a);
            jVarA.a(new C0257bz(this));
            this.f3106c.add(jVarA);
            S.j jVarA2 = S.b.a().a("", f3105b);
            jVarA2.a(new bA(this));
            this.f3106c.add(jVarA2);
        }
        if (C1806i.a().a("f(*&rew0987LKJ098342")) {
            Iterator it = p.x.a().b().iterator();
            while (it.hasNext()) {
                S.n nVar = (S.n) it.next();
                bH.C.d("User Trigger " + nVar.a() + " active: " + nVar.c());
            }
        }
    }
}
