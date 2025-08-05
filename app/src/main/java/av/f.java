package aV;

import java.util.ArrayList;
import java.util.List;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aV/f.class */
public class f implements A.i {

    /* renamed from: b, reason: collision with root package name */
    private static f f3944b = null;

    /* renamed from: c, reason: collision with root package name */
    private List f3945c = new ArrayList();

    /* renamed from: a, reason: collision with root package name */
    A.q f3946a;

    private f() {
        this.f3946a = null;
        A.q qVar = new A.q();
        qVar.a(aD.a.f2298d);
        qVar.b(C1818g.b("RS232 Serial Communication or virtual communication using serial com port that are already setup on this computer."));
        qVar.a(aD.a.class);
        this.f3945c.add(qVar);
        this.f3946a = qVar;
        if (C1806i.a().a("GD;';LFDS-0DSL;")) {
            A.q qVar2 = new A.q();
            qVar2.a(aC.a.f2289e);
            qVar2.b(C1818g.b("Direct Bluetooth connection."));
            qVar2.a(aC.a.class);
            this.f3945c.add(qVar2);
        }
        if (C1806i.a().a(";'[PGS0P;'G0[F;")) {
            A.q qVar3 = new A.q();
            qVar3.a(aB.b.f2274c);
            qVar3.b(C1818g.b("FTDI D2XX Direct USB."));
            qVar3.a(aB.b.class);
            this.f3945c.add(qVar3);
        }
        if (C1806i.a().a("LKFDS;LK;lkfs;lk")) {
            A.q qVar4 = new A.q();
            qVar4.a(B.l.f168b);
            qVar4.b(C1818g.b("TCP/IP - WiFi / Ethernet"));
            qVar4.a(B.l.class);
            this.f3945c.add(qVar4);
        }
    }

    public static f c() {
        if (f3944b == null) {
            f3944b = new f();
        }
        return f3944b;
    }

    @Override // A.i
    public List a() {
        return this.f3945c;
    }

    @Override // A.i
    public A.f a(String str, String str2) {
        for (A.q qVar : this.f3945c) {
            if (qVar.a().equals(str)) {
                return qVar.c(str2);
            }
        }
        return null;
    }

    @Override // A.i
    public A.f a(String str, B.i iVar, String str2) {
        return a(str, str2);
    }

    @Override // A.i
    public A.q b() {
        return this.f3946a;
    }
}
