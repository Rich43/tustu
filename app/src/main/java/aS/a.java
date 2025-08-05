package aS;

import G.C0113cs;
import G.R;
import G.S;
import G.aH;
import G.cH;
import G.cO;
import aP.cZ;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aS/a.class */
public class a implements S, cH {

    /* renamed from: d, reason: collision with root package name */
    private static int f3893d = 0;

    /* renamed from: e, reason: collision with root package name */
    private static int f3894e = 1;

    /* renamed from: c, reason: collision with root package name */
    R f3896c;

    /* renamed from: a, reason: collision with root package name */
    HashMap f3891a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    long f3892b = 0;

    /* renamed from: f, reason: collision with root package name */
    private int f3895f = f3893d;

    /* renamed from: g, reason: collision with root package name */
    private boolean f3897g = true;

    private aH d(R r2) {
        this.f3896c = r2;
        if (r2.g("Config Error!OC") != null) {
            return r2.g("Config Error!OC");
        }
        if (r2.g("Config ErrorOC") != null) {
            return r2.g("Config ErrorOC");
        }
        return null;
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        b bVar = (b) this.f3891a.get(r2.c());
        cZ.a().b().ac();
        if (bVar != null) {
            C0113cs.a().a(bVar);
        }
    }

    @Override // G.S
    public void c(R r2) {
        aH aHVarD = d(r2);
        b bVar = new b(this, r2.c());
        this.f3891a.put(r2.c(), bVar);
        cO.a().a(r2.c(), bVar);
        r2.C().a(bVar);
        if (aHVarD != null) {
            try {
                C0113cs.a().a(r2.c(), aHVarD.aJ(), bVar);
            } catch (V.a e2) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        if (r2.g("seconds") != null) {
            try {
                C0113cs.a().a(r2.c(), "seconds", new e(this, bVar));
            } catch (V.a e3) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }

    @Override // G.cH
    public void a(String str) {
        this.f3892b = System.currentTimeMillis();
    }
}
