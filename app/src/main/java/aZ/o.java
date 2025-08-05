package az;

import bH.C;
import bH.C1005m;
import bH.C1008p;
import bH.W;
import bH.Z;
import bH.aa;
import com.efiAnalytics.ui.bV;
import f.AbstractC1721c;
import f.C1719a;
import f.C1720b;
import f.C1722d;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:az/o.class */
public class o extends AbstractC1721c {

    /* renamed from: d, reason: collision with root package name */
    private InterfaceC0943d f6497d;

    /* renamed from: e, reason: collision with root package name */
    private aa f6498e;

    /* renamed from: f, reason: collision with root package name */
    private static boolean f6499f = false;

    /* renamed from: g, reason: collision with root package name */
    private static o f6501g = null;

    /* renamed from: c, reason: collision with root package name */
    private boolean f6496c = false;

    /* renamed from: b, reason: collision with root package name */
    Map f6500b = new HashMap();

    private o(InterfaceC0943d interfaceC0943d, aa aaVar) {
        this.f6497d = interfaceC0943d;
        this.f6498e = aaVar;
    }

    public static o a(InterfaceC0943d interfaceC0943d, aa aaVar) {
        if (f6501g == null) {
            f6501g = new o(interfaceC0943d, aaVar);
        } else if (interfaceC0943d != null) {
            C.d("Updating ai & t");
            f6501g.f6497d = interfaceC0943d;
            f6501g.f6498e = aaVar;
        }
        return f6501g;
    }

    public static synchronized o d() {
        if (f6501g == null) {
            C.a("gupam");
            f6501g = new o(null, null);
        }
        return f6501g;
    }

    public C1719a e() throws C0941b, C0944e {
        if (!C1005m.b()) {
            throw new C0944e(C0942c.a(this.f6498e));
        }
        try {
            return a("TunerStudioMS", h());
        } catch (f.h e2) {
            throw new C0941b(this.f6498e.a("Invalid activation data.") + " " + e2.getMessage());
        } catch (IOException e3) {
            throw new C0944e(C0942c.b(this.f6498e));
        }
    }

    @Override // f.AbstractC1721c
    public C1719a b() {
        String strC = this.f6497d.c();
        C1719a c1719a = (C1719a) this.f6500b.get(this.f6497d.f());
        if (strC == null || strC.trim().equals("")) {
            return null;
        }
        if (c1719a != null && c1719a.f() == 0) {
            return c1719a;
        }
        try {
            C1719a c1719a2 = new C1719a(strC);
            c1719a2.i(strC);
            this.f6500b.put(this.f6497d.f(), c1719a2);
            return c1719a2;
        } catch (Exception e2) {
            return null;
        }
    }

    public void c(C1719a c1719a) {
        try {
            this.f6497d.a(c1719a.i());
        } catch (V.a e2) {
            bV.d("Unable to save Activation Data:\n" + e2.getMessage(), this.f6497d.i());
        }
    }

    @Override // f.AbstractC1721c
    public synchronized C1720b a() {
        if (super.a() == null) {
            try {
                C1720b c1720b = new C1720b();
                new o(null, null).b(c1720b);
                d().a(c1720b);
                return super.a();
            } catch (f.g e2) {
                C.a("Error ISID");
            }
        }
        return super.a();
    }

    public synchronized C1720b b(C1720b c1720b) throws f.g {
        if (super.a() != null) {
            if (this.f6496c) {
                C.c("Returning cached HW");
            }
            return super.a();
        }
        if (this.f6496c) {
            C.c("Gathering HWID!!!!!");
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        Z z2 = new Z();
        z2.a();
        int i2 = 0;
        p pVar = new p(this, c1720b);
        q qVar = new q(this, c1720b);
        r rVar = new r(this, c1720b);
        s sVar = new s(this, c1720b);
        pVar.start();
        qVar.start();
        rVar.start();
        sVar.start();
        while (true) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e2) {
                Logger.getLogger(o.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (System.currentTimeMillis() - jCurrentTimeMillis >= 4000 || (qVar.a() && rVar.a() && sVar.a())) {
                break;
            }
        }
        if (this.f6496c) {
            C.c("Time to get: " + z2.d());
            z2.e();
            z2.a();
        }
        String strB = rVar.b();
        if (strB != null && strB.length() > 0) {
            c1720b.a(strB);
            if (this.f6496c) {
                C.c("m:" + strB);
            }
            i2 = 0 + 1;
        }
        String strB2 = sVar.b();
        if (strB2 != null && !strB2.isEmpty()) {
            if (this.f6496c) {
                C.c("mbId:" + strB2);
            }
            c1720b.c(strB2);
            i2++;
        }
        String strB3 = qVar.b();
        if (strB3 != null && strB3.length() > 0) {
            if (this.f6496c) {
                C.c("hdId:" + strB3);
            }
            c1720b.b(strB3);
            i2++;
        }
        if (!pVar.a() && (i2 < 2 || f6499f)) {
            do {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e3) {
                    Logger.getLogger(o.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            } while (!pVar.a());
        }
        String strB4 = pVar.b();
        if (strB4 != null && strB4.length() > 5) {
            if (this.f6496c) {
                C.c("cpuId:" + strB4);
            }
            c1720b.i(strB4);
            i2++;
        }
        if (i2 == 0) {
            throw new f.g("No Identifiers available.");
        }
        C.c("hid: " + z2.d());
        super.a(c1720b);
        return c1720b;
    }

    public void f() {
        C1719a c1719aB = b();
        if (c1719aB != null && c1719aB.h().before(new Date(System.currentTimeMillis() + 1209600000)) && C1005m.b()) {
            try {
                C1719a c1719aE = e();
                if (a(c1719aE).a() == 0) {
                    c(c1719aE);
                }
            } catch (C0941b e2) {
            } catch (C0944e e3) {
            } catch (f.g e4) {
            }
        }
    }

    public synchronized C1722d g() {
        return a(this.f6497d);
    }

    public synchronized C1722d a(InterfaceC0943d interfaceC0943d) {
        C1722d c1722dC;
        InterfaceC0943d interfaceC0943d2 = this.f6497d;
        this.f6497d = interfaceC0943d;
        try {
            c1722dC = c();
            if (this.f6496c) {
                C.c("*********** vORA");
            }
            if (f6499f && c1722dC.a() == 0) {
                c1722dC.a(1);
            }
        } catch (Throwable th) {
            this.f6497d = interfaceC0943d2;
            throw th;
        }
        if (c1722dC.a() == 0) {
            C1719a c1719aB = b();
            if (c1719aB != null && c1719aB.h().before(new Date(System.currentTimeMillis() + 604800000)) && C1005m.b()) {
                try {
                    C1719a c1719aE = e();
                    if (a(c1719aE).a() == 0) {
                        c(c1719aE);
                    }
                } catch (C0941b e2) {
                } catch (C0944e e3) {
                } catch (f.g e4) {
                }
            }
            this.f6497d = interfaceC0943d2;
            return c1722dC;
        }
        if (c1722dC.a() == 1) {
            if (C1005m.b()) {
                try {
                    C1719a c1719aE2 = e();
                    c1722dC = a(c1719aE2);
                    if (c1722dC.a() != 0) {
                        this.f6497d = interfaceC0943d2;
                        return c1722dC;
                    }
                    c(c1719aE2);
                    this.f6497d = interfaceC0943d2;
                    return c1722dC;
                } catch (C0941b e5) {
                    c1722dC.a(2);
                    c1722dC.a(e5.getMessage());
                    C1722d c1722d = c1722dC;
                    this.f6497d = interfaceC0943d2;
                    return c1722d;
                } catch (C0944e e6) {
                    C.d("Network Error");
                } catch (f.g e7) {
                    a("MISSING_IDENTIFIERS, Expired");
                    if (C1008p.a()) {
                        c1722dC.a(this.f6498e.a(C0942c.f6473f));
                        c1722dC.a(4);
                    } else {
                        c1722dC.a(this.f6498e.a(C0942c.f6473f));
                        c1722dC.a(4);
                    }
                    C1722d c1722d2 = c1722dC;
                    this.f6497d = interfaceC0943d2;
                    return c1722d2;
                }
            }
            C1722d c1722d3 = c1722dC;
            this.f6497d = interfaceC0943d2;
            return c1722d3;
        }
        if (!C1005m.b()) {
            if (this.f6497d.j()) {
                C1722d c1722dA = a(this.f6497d, (String) null);
                this.f6497d = interfaceC0943d2;
                return c1722dA;
            }
            c1722dC.a(2);
            c1722dC.a("Unable to connecto Activativon Server");
            this.f6497d = interfaceC0943d2;
            return c1722dC;
        }
        a("Connected to Internet");
        try {
            C1719a c1719aE3 = e();
            c1722dC = a(c1719aE3);
            if (c1722dC.a() != 0) {
                this.f6497d = interfaceC0943d2;
                return c1722dC;
            }
            c(c1719aE3);
            this.f6497d = interfaceC0943d2;
            return c1722dC;
        } catch (C0941b e8) {
            a("MISSING_IDENTIFIERS " + e8.getLocalizedMessage());
            c1722dC.a(2);
            c1722dC.a(e8.getMessage());
            C1722d c1722d4 = c1722dC;
            this.f6497d = interfaceC0943d2;
            return c1722d4;
        } catch (C0944e e9) {
            C.c("key: kjsdaiie");
            e9.printStackTrace();
            if (this.f6497d.j()) {
                C1722d c1722dA2 = a(this.f6497d, "");
                this.f6497d = interfaceC0943d2;
                return c1722dA2;
            }
            c1722dC.a(2);
            c1722dC.a("Unable to connect to Activativon Server");
            C1722d c1722d5 = c1722dC;
            this.f6497d = interfaceC0943d2;
            return c1722d5;
        } catch (f.g e10) {
            a("MISSING_IDENTIFIERS");
            if (C1008p.a()) {
                c1722dC.a(this.f6498e.a(C0942c.f6473f));
                c1722dC.a(4);
            } else {
                c1722dC.a(this.f6498e.a(C0942c.f6474g));
                c1722dC.a(4);
            }
            C1722d c1722d6 = c1722dC;
            this.f6497d = interfaceC0943d2;
            return c1722d6;
        }
        this.f6497d = interfaceC0943d2;
        throw th;
    }

    public C1722d a(InterfaceC0943d interfaceC0943d, String str) throws HeadlessException {
        if (bV.a((str == null || str.equals("")) ? C0942c.d(this.f6498e) : str + "\n" + C0942c.d(this.f6498e), (Component) interfaceC0943d.i(), true)) {
            a(interfaceC0943d);
            return null;
        }
        try {
            C0945f c0945f = new C0945f(interfaceC0943d.i(), interfaceC0943d, this.f6498e, h());
            c0945f.pack();
            bV.b((Window) c0945f);
            c0945f.setResizable(false);
            c0945f.setVisible(true);
            C1719a c1719aB = c0945f.b();
            if (c1719aB == null) {
                return null;
            }
            C1722d c1722dA = a(c1719aB);
            if (c1722dA.a() == 0) {
                c(c1719aB);
                bV.d(this.f6498e.a(C0942c.f6468a), c0945f);
            }
            return c1722dA;
        } catch (C0941b e2) {
            C1722d c1722d = new C1722d();
            c1722d.a(2);
            c1722d.a(e2.getMessage());
            return c1722d;
        } catch (f.g e3) {
            C1722d c1722d2 = new C1722d();
            if (C1008p.a()) {
                c1722d2.a(this.f6498e.a(C0942c.f6473f));
                c1722d2.a(4);
            } else {
                c1722d2.a(this.f6498e.a(C0942c.f6473f));
                c1722d2.a(4);
            }
            return c1722d2;
        }
    }

    @Override // f.AbstractC1721c
    public C1722d c() {
        C1719a c1719aB = b();
        if (c1719aB != null && c1719aB.d().equals(this.f6497d.d())) {
            return a(c1719aB);
        }
        C1722d c1722d = new C1722d();
        c1722d.a(2);
        c1722d.a(C1722d.f12168a);
        a("MISSING_IDENTIFIERS: " + ((Object) c1719aB));
        return c1722d;
    }

    public synchronized C1720b h() throws f.g {
        C1720b c1720bA = a();
        c1720bA.d(this.f6497d.d());
        c1720bA.f(this.f6497d.e());
        c1720bA.e(this.f6497d.h());
        c1720bA.g(this.f6497d.f());
        c1720bA.h(W.b(this.f6497d.g(), "(Beta)", ""));
        C1720b c1720bB = b(c1720bA);
        if (0 != 0) {
            c1720bB.d("RZ7R7P6F7WPA2IM36A6M");
        }
        return c1720bB;
    }

    public static void a(boolean z2) {
        f6499f = z2;
    }
}
