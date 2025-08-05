package K;

import G.C0079bl;
import G.C0083bp;
import G.C0084bq;
import G.C0085br;
import G.C0088bu;
import G.C0094c;
import G.R;
import G.aC;
import G.aM;

/* loaded from: TunerStudioMS.jar:K/a.class */
public class a extends C0088bu {

    /* renamed from: a, reason: collision with root package name */
    private String f1495a;

    /* renamed from: f, reason: collision with root package name */
    private R f1496f;

    public a(R r2, int i2, boolean z2) {
        this.f1495a = "";
        this.f1496f = null;
        if (i2 > 0) {
            this.f1495a = i2 + "";
        }
        this.f1496f = r2;
        s("Accel Enrichment Wizard");
        aM aMVarC = r2.c(aC.f537h + this.f1495a);
        aM aMVarC2 = r2.c(aC.f538i + this.f1495a);
        aM aMVarC3 = r2.c(aC.f548s + this.f1495a);
        aM aMVarC4 = r2.c(aC.f549t + this.f1495a);
        aM aMVarC5 = r2.c(aC.f547r + this.f1495a);
        aM aMVarC6 = r2.c(aC.f550u + this.f1495a);
        C0088bu c0088bu = new C0088bu();
        c0088bu.v("accelCurves");
        c0088bu.i(0);
        if (aMVarC3 != null && aMVarC4 != null) {
            C0079bl c0079bl = new C0079bl();
            c0079bl.s("MAP Based AE");
            c0079bl.v("mapDotCurve");
            c0079bl.c(aMVarC3.aJ());
            c0079bl.a(aMVarC4.aJ());
            c0079bl.g(aMVarC4.b());
            c0079bl.h(aMVarC3.b() + 2);
            c0079bl.a(new C0094c("PW Adder"));
            c0079bl.c(new C0094c("Rate"));
            c0079bl.d(500.0d);
            c0079bl.c(aMVarC3.q());
            c0079bl.b(true);
            if (aMVarC4.r() > 20.0d) {
                c0079bl.b(20.0d);
            } else {
                c0079bl.b(aMVarC4.r());
            }
            c0079bl.a(aMVarC4.q());
            c0079bl.d(aC.f551v);
            if (aMVarC != null) {
                c0079bl.u("mapProportion" + this.f1495a + " > 0");
            } else if (aMVarC2 != null) {
                c0079bl.u("tpsProportion" + this.f1495a + " <100");
            }
            c0088bu.a(c0079bl);
        }
        if (aMVarC5 != null && aMVarC6 != null) {
            C0079bl c0079bl2 = new C0079bl();
            c0079bl2.s("TPS Based AE");
            c0079bl2.v("tpsDotCurve");
            c0079bl2.c(aMVarC5.aJ());
            c0079bl2.a(aMVarC6.aJ());
            c0079bl2.g(aMVarC6.b());
            c0079bl2.h(aMVarC5.b() + 2);
            c0079bl2.a(new C0094c("PW Adder"));
            c0079bl2.c(new C0094c("Rate"));
            c0079bl2.b(true);
            if (aMVarC5.r() > 2000.0d) {
                c0079bl2.d(2000.0d);
            } else {
                c0079bl2.d(aMVarC5.r());
            }
            c0079bl2.c(aMVarC5.q());
            if (aMVarC6.r() > 20.0d) {
                c0079bl2.b(20.0d);
            } else {
                c0079bl2.b(aMVarC6.r());
            }
            c0079bl2.a(aMVarC6.q());
            c0079bl2.d(aC.f552w);
            if (aMVarC3 == null || aMVarC4 == null) {
                c0079bl2.c(true);
            }
            if (aMVarC2 != null) {
                c0079bl2.u("tpsProportion" + this.f1495a + " > 0");
            } else if (aMVarC != null) {
                c0079bl2.u("mapProportion" + this.f1495a + " == 0");
            }
            c0088bu.a(c0079bl2);
        }
        a(c0088bu);
        C0088bu c0088bu2 = new C0088bu();
        c0088bu2.i(0);
        if (aMVarC != null) {
            C0083bp c0083bp = new C0083bp();
            c0083bp.e("Throttle Position vs Manifold Pressure Accel Enrichment Strategy");
            c0083bp.a(aMVarC.aJ());
            c0083bp.c(true);
            c0088bu2.a(c0083bp);
        }
        if (aMVarC2 != null) {
            C0083bp c0083bp2 = new C0083bp();
            c0083bp2.e("Throttle Position vs Manifold Pressure Accel Enrichment Strategy, Percent TPS Driven");
            c0083bp2.a(aMVarC2.aJ());
            c0088bu2.a(c0083bp2);
        }
        a(c0088bu2);
        if (z2) {
            C0084bq c0084bq = new C0084bq();
            C0085br c0085br = new C0085br();
            c0085br.a(aC.f551v);
            c0085br.a(0.0d);
            c0085br.b(100.0d);
            c0085br.b("kPa/s");
            c0085br.b(true);
            c0085br.a(false);
            c0085br.a(0.0d);
            c0084bq.a(c0085br);
            C0085br c0085br2 = new C0085br();
            c0085br2.a(aC.f552w);
            c0085br2.a(0.0d);
            c0085br2.b(10.0d);
            c0085br2.b(true);
            c0085br2.a(true);
            if (aMVarC5 != null) {
                c0085br2.b(aMVarC5.o());
            }
            c0084bq.a(c0085br2);
            if (r2.g(aC.f554y) != null) {
                C0085br c0085br3 = new C0085br();
                c0085br3.a(aC.f554y);
                c0084bq.a(c0085br3);
            } else if (r2.g(aC.f554y + "1") != null) {
                C0085br c0085br4 = new C0085br();
                c0085br4.a(aC.f554y + "1");
                c0084bq.a(c0085br4);
            } else {
                C0085br c0085br5 = new C0085br();
                c0085br5.a(aC.f553x);
                c0084bq.a(c0085br5);
            }
            a(c0084bq);
        }
        C0088bu c0088bu3 = new C0088bu();
        c0088bu3.i(0);
        C0088bu c0088bu4 = new C0088bu();
        c0088bu4.s(" ");
        C0083bp c0083bp3 = new C0083bp();
        c0083bp3.a(aC.f544o + this.f1495a);
        c0083bp3.e("MAPdot Threshold");
        c0088bu4.a(c0083bp3);
        C0083bp c0083bp4 = new C0083bp();
        c0083bp4.a(aC.f541l + this.f1495a);
        c0083bp4.e("Accel Time");
        c0088bu4.a(c0083bp4);
        C0083bp c0083bp5 = new C0083bp();
        c0083bp5.a(aC.f546q + this.f1495a);
        c0083bp5.e("Accel Taper Time");
        c0088bu4.a(c0083bp5);
        C0083bp c0083bp6 = new C0083bp();
        c0083bp6.a(aC.f545p + this.f1495a);
        c0083bp6.e("End Pulsewidth");
        c0088bu4.a(c0083bp6);
        c0088bu3.a(c0088bu4);
        C0088bu c0088bu5 = new C0088bu();
        c0088bu5.s(" ");
        C0083bp c0083bp7 = new C0083bp();
        c0083bp7.a(aC.f540k + this.f1495a);
        c0083bp7.e("TPSdot Threshold");
        c0088bu5.a(c0083bp7);
        C0083bp c0083bp8 = new C0083bp();
        c0083bp8.a(aC.f542m + this.f1495a);
        c0083bp8.e("Decel Fuel Amount");
        c0088bu5.a(c0083bp8);
        C0083bp c0083bp9 = new C0083bp();
        c0083bp9.a(aC.f539j + this.f1495a);
        c0083bp9.e("Cold Accel Enrichment");
        c0088bu5.a(c0083bp9);
        C0083bp c0083bp10 = new C0083bp();
        c0083bp10.a(aC.f543n + this.f1495a);
        c0083bp10.e("Cold Accel Multiplier");
        c0088bu5.a(c0083bp10);
        c0088bu3.a(c0088bu5);
        a(c0088bu3);
    }
}
