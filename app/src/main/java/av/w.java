package aV;

import bH.I;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aV/w.class */
public class w implements A.i {

    /* renamed from: a, reason: collision with root package name */
    private static A.i f3951a = null;

    /* renamed from: b, reason: collision with root package name */
    private List f3952b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private A.q f3953c;

    private w() {
        this.f3953c = null;
        if (C1806i.a().a("fdsp[pp[ds';'")) {
            A.q qVar = new A.q();
            qVar.a(aD.a.f2298d);
            qVar.b(C1818g.b("JSSC RS232"));
            qVar.a(aD.a.class);
            this.f3952b.add(qVar);
            this.f3953c = qVar;
        }
        if (C1806i.a().a("LKFDS;LK;lkfs;lk") && (I.d() || I.b())) {
            aD.a.f2316v = true;
        }
        if (C1806i.a().a("GD;';LFDS-0DSL;")) {
            A.q qVar2 = new A.q();
            qVar2.a(aC.a.f2289e);
            qVar2.b(C1818g.b("Direct Bluetooth Connection via Bluecove"));
            qVar2.a(aC.a.class);
            this.f3952b.add(qVar2);
        }
        if (C1806i.a().a(";'[PGS0P;'G0[F;") && !I.e()) {
            A.q qVar3 = new A.q();
            qVar3.a(aB.b.f2274c);
            qVar3.b(C1818g.b("FTDI USB D2XX"));
            qVar3.a(aB.b.class);
            this.f3952b.add(qVar3);
            if (!I.d()) {
            }
        }
        if (C1806i.a().a("98fg54lklk") || C1806i.a().a("HF-05[P54;'FD") || C1806i.a().a("HF-0[PEPHF0H;LJGPO0")) {
            A.q qVar4 = new A.q();
            qVar4.a(B.o.f184b);
            qVar4.b(C1818g.b("UDP WiFi / Ethernet"));
            qVar4.a(B.o.class);
            this.f3952b.add(qVar4);
            A.q qVar5 = new A.q();
            qVar5.a(B.l.f168b);
            qVar5.b(C1818g.b("TCP/IP - WiFi / Ethernet"));
            qVar5.a(B.l.class);
            this.f3952b.add(qVar5);
            if (C1806i.a().a("H;';'0FD;RE")) {
                A.q qVar6 = new A.q();
                qVar6.a(B.a.f90c);
                qVar6.b(C1818g.b(B.a.f90c));
                qVar6.a(B.a.class);
                qVar6.a(B.b.c());
                this.f3952b.add(qVar6);
            }
        } else if (C1806i.a().a("LKFDS;LK;lkfs;lk")) {
            A.q qVar7 = new A.q();
            qVar7.a(B.l.f168b);
            qVar7.b(C1818g.b("TCP/IP - WiFi / Ethernet"));
            qVar7.a(B.l.class);
            this.f3952b.add(qVar7);
        }
        if (this.f3953c != null || this.f3952b.isEmpty()) {
            return;
        }
        this.f3953c = (A.q) this.f3952b.get(0);
    }

    public static A.i c() {
        if (f3951a == null) {
            f3951a = new w();
        }
        return f3951a;
    }

    public static void a(A.i iVar) {
        f3951a = iVar;
    }

    @Override // A.i
    public List a() {
        return this.f3952b;
    }

    @Override // A.i
    public A.f a(String str, String str2) {
        for (A.q qVar : this.f3952b) {
            if (qVar.a().equals(str)) {
                return qVar.c(str2);
            }
        }
        return null;
    }

    @Override // A.i
    public A.f a(String str, B.i iVar, String str2) {
        if (a(str, iVar)) {
            return a(B.a.f90c, iVar, str2);
        }
        for (A.q qVar : this.f3952b) {
            if (qVar.a().equals(str)) {
                A.f fVarC = qVar.c(str2);
                if (fVarC instanceof B.a) {
                    ((B.a) fVarC).a(iVar, str2);
                }
                return fVarC;
            }
        }
        return null;
    }

    public boolean a(String str, B.i iVar) {
        if (iVar == null || iVar.i() == null || !iVar.i().equals(bT.o.f7604a)) {
            return false;
        }
        return (str.equals(B.l.f168b) || str.equals(B.o.f184b)) && d();
    }

    public boolean d() {
        Iterator it = this.f3952b.iterator();
        while (it.hasNext()) {
            if (((A.q) it.next()).a().equals(B.a.f90c)) {
                return true;
            }
        }
        return false;
    }

    @Override // A.i
    public A.q b() {
        return this.f3953c;
    }
}
