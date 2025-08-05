package K;

import G.C0079bl;
import G.C0083bp;
import G.C0084bq;
import G.C0085br;
import G.C0088bu;
import G.C0094c;
import G.C0137t;
import G.R;
import G.aC;
import bH.S;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:K/i.class */
public class i extends C0088bu {

    /* renamed from: a, reason: collision with root package name */
    private String f1515a;

    /* renamed from: f, reason: collision with root package name */
    private R f1516f;

    /* renamed from: g, reason: collision with root package name */
    private double[] f1517g = {-40.0d, -29.0d, -18.0d, -7.0d, 4.0d, 16.0d, 27.0d, 38.0d, 54.0d, 71.0d};

    /* renamed from: h, reason: collision with root package name */
    private double[] f1518h = {-40.0d, -20.0d, 0.0d, 20.0d, 40.0d, 60.0d, 80.0d, 100.0d, 130.0d, 160.0d};

    /* renamed from: i, reason: collision with root package name */
    private String[] f1519i = {"cltGauge"};

    public i(R r2, int i2) {
        this.f1515a = "";
        this.f1516f = null;
        if (i2 > 0) {
            this.f1515a = i2 + "";
        }
        this.f1516f = r2;
        boolean zA = C0137t.a(r2);
        C0079bl c0079bl = new C0079bl();
        c0079bl.a(aC.f530a + this.f1515a);
        c0079bl.d(aC.f556A);
        c0079bl.b(240.0d);
        c0079bl.a(0.0d);
        c0079bl.g(13);
        c0079bl.a(new C0094c("Enrichment"));
        c0079bl.c(new C0094c("Coolant Temp"));
        if (zA) {
            c0079bl.a(this.f1517g);
            c0079bl.d(90.0d);
            c0079bl.c(-50.0d);
        } else {
            c0079bl.a(this.f1518h);
            c0079bl.d(200.0d);
            c0079bl.c(-60.0d);
        }
        if (r2.c("tempTable") != null) {
            c0079bl.c("tempTable");
            c0079bl.a((double[]) null);
        }
        c0079bl.h(13);
        c0079bl.a(this.f1519i);
        a(c0079bl);
        C0084bq c0084bq = new C0084bq();
        C0085br c0085br = new C0085br();
        c0085br.a(aC.f557B);
        c0084bq.a(c0085br);
        C0085br c0085br2 = new C0085br();
        c0085br2.a(aC.f555z);
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
        C0088bu c0088bu = new C0088bu();
        c0088bu.i(0);
        String str = aC.f531b + this.f1515a;
        String str2 = aC.f532c + this.f1515a;
        String str3 = aC.f533d + this.f1515a;
        String str4 = aC.f534e + this.f1515a;
        if (a(r2, str) || a(r2, str2) || a(r2, str3) || a(r2, str4)) {
            C0088bu c0088bu2 = new C0088bu();
            c0088bu2.s("Cranking Pulsewidth");
            if (a(r2, str)) {
                C0083bp c0083bp = new C0083bp();
                c0083bp.e("Priming Pulse");
                c0083bp.a(str);
                c0088bu2.a(c0083bp);
            }
            if (a(r2, str2)) {
                C0083bp c0083bp2 = new C0083bp();
                c0083bp2.e(zA ? "Pulsewidth at -40" + S.a() + "C" : "Pulsewidth at -40" + S.a() + PdfOps.F_TOKEN);
                c0083bp2.a(str2);
                c0088bu2.a(c0083bp2);
            }
            if (a(r2, str3)) {
                C0083bp c0083bp3 = new C0083bp();
                c0083bp3.e(zA ? "Pulsewidth at 71" + S.a() + "C" : "Pulsewidth at 160" + S.a() + PdfOps.F_TOKEN);
                c0083bp3.a(str3);
                c0088bu2.a(c0083bp3);
            }
            if (a(r2, str4)) {
                C0083bp c0083bp4 = new C0083bp();
                c0083bp4.e("Flood Clear Threshold");
                c0083bp4.a(str4);
                c0088bu2.a(c0083bp4);
            }
            c0088bu.a(c0088bu2);
        }
        String str5 = aC.f535f + this.f1515a;
        String str6 = aC.f536g + this.f1515a;
        if (a(r2, str5) || a(r2, str5)) {
            C0088bu c0088bu3 = new C0088bu();
            c0088bu3.s("Afterstart Enrichment");
            if (a(r2, str5)) {
                C0083bp c0083bp5 = new C0083bp();
                c0083bp5.e("Additive Enrichment");
                c0083bp5.a(str5);
                c0088bu3.a(c0083bp5);
            }
            if (a(r2, str6)) {
                C0083bp c0083bp6 = new C0083bp();
                c0083bp6.e("Number of Ignition Cycles");
                c0083bp6.a(str6);
                c0088bu3.a(c0083bp6);
            }
            c0088bu.a(c0088bu3);
        }
        if (c0088bu.L()) {
            a(c0088bu);
        }
    }

    private boolean a(R r2, String str) {
        return r2.c(str) != null;
    }
}
