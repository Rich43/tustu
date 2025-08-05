package aP;

import G.C0040a;
import G.C0113cs;
import bl.C1195q;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;
import y.C1893a;

/* loaded from: TunerStudioMS.jar:aP/jv.class */
public class jv implements G.S {

    /* renamed from: e, reason: collision with root package name */
    private iC f3809e = new iC();

    /* renamed from: a, reason: collision with root package name */
    ac.g f3810a = new ac.g();

    /* renamed from: b, reason: collision with root package name */
    jD f3811b = null;

    /* renamed from: f, reason: collision with root package name */
    private static jv f3812f = null;

    /* renamed from: c, reason: collision with root package name */
    public static boolean f3813c = true;

    /* renamed from: d, reason: collision with root package name */
    public static boolean f3814d = true;

    private jv() {
        jC jCVar = new jC(this);
        G.aB.a().a((G.aT) jCVar);
        G.aB.a().a((G.bM) jCVar);
    }

    public static jv a() {
        if (f3812f == null) {
            f3812f = new jv();
        }
        return f3812f;
    }

    @Override // G.S
    public void a(G.R r2) {
    }

    @Override // G.S
    public void b(G.R r2) {
        r2.C().b(bS.e());
        C1195q.a().b();
    }

    @Override // G.S
    public void c(G.R r2) {
        r2.C().a(new jE(this));
        r2.C().a((G.aG) cZ.a().b());
        if (f3814d && r2.C().n(r2.c())) {
            r2.C().a(new aO(r2.c()));
        }
        ArrayList arrayListA = C1893a.a(new jC(this)).a(r2);
        if (arrayListA != null) {
            Iterator it = arrayListA.iterator();
            while (it.hasNext()) {
                r2.C().a((G.bN) it.next());
            }
        }
        if (C1798a.a().a(C1798a.cm, "true").equals("false")) {
            r2.C().b(false);
        }
        if (C1798a.a().a(C1798a.f13346aB, C1798a.f13347aC).equals("true")) {
            r2.C();
            G.J.e(true);
        }
        r2.C().e(C1798a.a().b(C1798a.f13348aD, 0));
        r2.b(this.f3809e);
        r2.a(this.f3809e);
        r2.C().a(new jw(this));
        if (f3813c) {
            r2.a(new hC());
        } else {
            r2.a(new C0040a());
        }
        if (r2.O().D() == null) {
            aF.a aVar = new aF.a(r2);
            r2.C().a((G.aD) aVar);
            r2.C().a((G.aG) aVar);
        }
        if (C1798a.f13268b.equals(C1798a.f13337as) && r2.i().length() < 3 && r2.g("Vbatt") != null) {
            r2.C().a(new H.b());
            H.a aVar2 = new H.a();
            r2.C().a(aVar2);
            try {
                C0113cs.a().a(r2.c(), "Vbatt", aVar2);
            } catch (V.a e2) {
                bH.C.a("Failed to subscribe OnlineApprover to Vbatt");
            }
        }
        if (C1798a.f13268b.equals(C1798a.f13337as) || C1798a.f13268b.equals(C1798a.f13338at)) {
            r2.C().a(new jx(this));
        }
        if (r2.c("tsCanId") != null) {
            try {
                G.aR.a().a(r2.c(), "tsCanId", new jB(this));
            } catch (V.a e3) {
                Logger.getLogger(jv.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        r2.C().a(this.f3810a);
        r2.C().a(bS.e());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public jD c() {
        if (this.f3811b == null || !this.f3811b.isAlive()) {
            this.f3811b = new jD(this);
            this.f3811b.start();
        }
        return this.f3811b;
    }

    public void a(iC iCVar) {
        this.f3809e = iCVar;
    }

    public void b() {
        while (this.f3811b != null && !this.f3811b.f3770a.isEmpty()) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e2) {
                Logger.getLogger(jv.class.getName()).log(Level.WARNING, "Not really", (Throwable) e2);
            }
        }
    }
}
