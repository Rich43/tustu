package br;

import G.dk;
import com.efiAnalytics.ui.C1589c;
import java.util.HashMap;

/* renamed from: br.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/r.class */
public class C1254r {

    /* renamed from: a, reason: collision with root package name */
    private static C1254r f8510a = null;

    /* renamed from: b, reason: collision with root package name */
    private HashMap f8511b = new HashMap();

    private C1254r() {
    }

    public static C1254r a() {
        if (f8510a == null) {
            f8510a = new C1254r();
        }
        return f8510a;
    }

    public C1250n a(G.R r2, dk dkVar, String str, C1589c c1589c) {
        String str2 = r2.c() + "." + str;
        C1250n c1250n = (C1250n) this.f8511b.get(str2);
        if (c1250n == null) {
            c1250n = new C1250n(r2, dkVar, str, c1589c);
            this.f8511b.put(str2, c1250n);
        }
        return c1250n;
    }

    public C1250n a(G.R r2, dk dkVar, C1589c c1589c) {
        return a(r2, dkVar, dkVar.b(), c1589c);
    }

    public void b() {
        this.f8511b.clear();
    }
}
