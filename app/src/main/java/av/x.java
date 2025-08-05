package aV;

import G.C0113cs;
import bH.C;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aV/x.class */
public class x {

    /* renamed from: a, reason: collision with root package name */
    public static String f3954a = "GPS";

    /* renamed from: b, reason: collision with root package name */
    private static x f3955b = null;

    /* renamed from: c, reason: collision with root package name */
    private aK.a f3956c = null;

    /* renamed from: d, reason: collision with root package name */
    private aK.d f3957d = null;

    /* renamed from: e, reason: collision with root package name */
    private boolean f3958e = false;

    private x() {
    }

    public static x a() {
        if (f3955b == null) {
            f3955b = new x();
        }
        return f3955b;
    }

    public aK.a b() {
        if (this.f3956c == null) {
            this.f3956c = aK.a.b();
        }
        return this.f3956c;
    }

    public boolean c() {
        return C1798a.a().c(C1798a.f13369aY, false);
    }

    public void a(boolean z2) {
        C1798a.a().b(C1798a.f13369aY, Boolean.toString(z2));
    }

    public void d() {
        aK.a aVarB = b();
        if (aVarB.g()) {
            aVarB.f();
        }
        List listA = f.c().a();
        String strA = a(f3954a);
        A.f fVarC = null;
        Iterator it = listA.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            A.q qVar = (A.q) it.next();
            if (strA != null && qVar.a().equals(strA)) {
                try {
                    fVarC = qVar.c(f3954a);
                    break;
                } catch (IllegalAccessException e2) {
                    Logger.getLogger(x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                } catch (InstantiationException e3) {
                    Logger.getLogger(x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        }
        if (fVarC == null) {
            C.d("GPS not initialized, missing critical configuration parameters.");
            return;
        }
        for (A.r rVar : fVarC.l()) {
            if (rVar.a() != 5) {
                Object objA = a(f3954a, rVar);
                if (objA != null) {
                    try {
                        fVarC.a(rVar.c(), objA);
                    } catch (A.s e4) {
                        Logger.getLogger(x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    }
                } else {
                    C.b("No values set for GPS setting: " + rVar.c());
                }
            }
        }
        C.d("Initializing GPS Interface: " + fVarC.h());
        aVarB.a(fVarC);
        I.c.a().a(true);
        I.d.a().d();
        aVarB.a(g());
        i();
        aVarB.d();
        this.f3958e = true;
    }

    public void e() {
        aK.a aVarB = b();
        if (aVarB == null || !this.f3958e) {
            return;
        }
        aVarB.f();
        aVarB.b(g());
        if (aVarB.a() != null) {
            try {
                aVarB.a().g();
            } catch (Exception e2) {
            }
        }
        this.f3958e = false;
    }

    private void i() {
        C0113cs.a().a(I.h.f1365c, new y(this));
        C0113cs.a().a(I.h.f1363a, new z(this));
        C0113cs.a().a(I.h.f1364b, new A(this));
    }

    private String b(String str, A.r rVar) {
        return str + "_" + rVar.c();
    }

    public void a(String str, String str2) {
        C1798a.a().b(str + "_ControllerInterface", str2);
    }

    public String a(String str) {
        return C1798a.a().c(str + "_ControllerInterface", (String) null);
    }

    public void a(String str, A.r rVar, Object obj) {
        C1798a.a().b(b(str, rVar), obj.toString());
    }

    public Object a(String str, A.r rVar) {
        if (rVar.a() != 2 && rVar.a() != 3) {
            return C1798a.a().c(b(str, rVar), (String) null);
        }
        int iC = C1798a.a().c(b(str, rVar), Integer.MIN_VALUE);
        if (iC == Integer.MIN_VALUE) {
            return null;
        }
        return Integer.valueOf(iC);
    }

    public void f() {
        e();
    }

    public aK.d g() {
        if (this.f3957d == null) {
            this.f3957d = new aK.d();
        }
        return this.f3957d;
    }

    public boolean h() {
        return this.f3958e;
    }
}
