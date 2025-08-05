package aP;

import aj.C0522c;
import br.C1232J;
import java.util.ArrayList;
import java.util.Iterator;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aP/gZ.class */
class gZ implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ G.R f3474a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aE.a f3475b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ gW f3476c;

    gZ(gW gWVar, G.R r2, aE.a aVar) {
        this.f3476c = gWVar;
        this.f3474a = r2;
        this.f3475b = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        ArrayList arrayListA = new C0522c().a(this.f3474a);
        Iterator it = this.f3476c.f3460k.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof aE.e) {
                ((aE.e) next).a(this.f3475b, this.f3474a);
            }
        }
        if (this.f3476c.f3454e.getTabCount() > 1) {
            this.f3476c.f3454e.a(gW.f3463n, arrayListA.size() > 0 || (C1806i.a().a("pokfr09i0943") && this.f3474a.y()));
        }
        try {
            ArrayList arrayListA2 = C1232J.a().a(this.f3474a);
            this.f3476c.f3454e.a(gW.f3465p, arrayListA2.size() > 0);
            if (C1806i.a().a("-0ofdspok54sg")) {
                aS.l.a().a(this.f3474a, arrayListA2);
            }
        } catch (V.g e2) {
            bH.C.c("Could not get supported VE Analyze Tables");
            e2.printStackTrace();
        }
        this.f3476c.f3454e.a(gW.f3467r, C1806i.a().a(" OKFDS09IFDSOK"));
        this.f3476c.f3454e.a(gW.f3466q, C1806i.a().a("12-0epofsraouvdlkw09fgk"));
        this.f3476c.f3454e.a(gW.f3468s, C1806i.a().a(";'GD;';'G"));
    }
}
