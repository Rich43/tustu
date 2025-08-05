package f;

import bH.C;
import bH.C1005m;
import java.util.Date;

/* renamed from: f.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:f/c.class */
public abstract class AbstractC1721c {

    /* renamed from: b, reason: collision with root package name */
    private boolean f12166b = false;

    /* renamed from: a, reason: collision with root package name */
    protected static C1720b f12167a = null;

    public synchronized C1720b a() {
        return f12167a;
    }

    public void a(C1720b c1720b) {
        f12167a = c1720b;
    }

    public C1719a a(String str, C1720b c1720b) throws h {
        String strA = C1005m.a(e.a() + c1720b.a() + "&appId=" + str);
        C1719a c1719a = new C1719a();
        c1719a.a(strA);
        return c1719a;
    }

    public C1722d a(C1719a c1719a) {
        C1722d c1722d = new C1722d();
        if (c1719a == null) {
            c1722d.a(c1719a.f());
            c1722d.a("Invalid Activation." + c1719a.g());
        } else if (c1719a.f() == 0 || c1719a.f() == 1) {
            try {
                if (b(c1719a)) {
                    if (c1719a.h().before(new Date())) {
                        c1722d.a(1);
                        c1722d.a("Valid Activation.");
                    } else {
                        c1722d.a(0);
                        c1722d.a("Valid Activation.");
                    }
                    c1722d.a(c1719a);
                } else {
                    a("Compare Failed: " + ((Object) c1719a));
                    c1722d.a(2);
                    c1722d.a("Invalid Activation.");
                }
            } catch (g e2) {
                a("InsufficientIdentifiers: " + c1719a.toString());
                c1722d.a(4);
                c1722d.a("No identifiers available.");
            }
        } else if (c1719a.f() == 5) {
            c1722d.a(5);
            c1722d.a("Current Activation Count: " + c1719a.e());
            c1722d.a(c1719a);
        } else if (c1719a.f() == 6) {
            c1722d.a(6);
            c1722d.a(c1719a.g());
        } else {
            c1722d.a(2);
            c1722d.a("Invalid activation data.");
        }
        return c1722d;
    }

    protected boolean b(C1719a c1719a) {
        C1720b c1720bA = f12167a;
        if (c1720bA == null) {
            C.a("hid not set, but they should");
            a("No Local Identifiers");
            c1720bA = a();
        }
        if (c1720bA.h() != null && !c1720bA.h().equals("") && c1719a.j().equals(c1720bA.h())) {
            return true;
        }
        if (c1720bA.c() != null && !c1720bA.c().equals("") && c1719a.b().equals(c1720bA.c())) {
            return true;
        }
        if (c1720bA.d() == null || c1720bA.d().equals("") || !c1719a.c().equals(c1720bA.d())) {
            return (c1720bA.b() == null || c1720bA.b().equals("") || !c1719a.a().equals(c1720bA.b())) ? false : true;
        }
        return true;
    }

    public abstract C1719a b();

    public C1722d c() {
        C1719a c1719aB = b();
        if (c1719aB != null) {
            return a(c1719aB);
        }
        C1722d c1722d = new C1722d();
        c1722d.a(2);
        c1722d.a(C1722d.f12168a);
        return c1722d;
    }

    protected void a(String str) {
        if (this.f12166b) {
            C.c("AM: " + str);
        }
    }
}
