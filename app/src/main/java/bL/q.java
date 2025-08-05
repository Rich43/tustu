package bL;

import bH.C;
import com.efiAnalytics.ui.C1562b;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;

/* loaded from: TunerStudioMS.jar:bL/q.class */
public class q {

    /* renamed from: f, reason: collision with root package name */
    private C1701s f7184f;

    /* renamed from: g, reason: collision with root package name */
    private C1701s f7185g;

    /* renamed from: h, reason: collision with root package name */
    private C1701s f7186h;

    /* renamed from: i, reason: collision with root package name */
    private C1701s f7187i;

    /* renamed from: a, reason: collision with root package name */
    i f7188a;

    /* renamed from: b, reason: collision with root package name */
    g f7189b;

    /* renamed from: c, reason: collision with root package name */
    g f7190c;

    /* renamed from: d, reason: collision with root package name */
    r f7191d = new r(this);

    /* renamed from: j, reason: collision with root package name */
    private C1562b[][] f7192j = (C1562b[][]) null;

    /* renamed from: e, reason: collision with root package name */
    C1589c f7193e = null;

    /* renamed from: k, reason: collision with root package name */
    private long f7194k = -1;

    /* renamed from: l, reason: collision with root package name */
    private long f7195l = -1;

    /* renamed from: m, reason: collision with root package name */
    private float f7196m = 1.0f;

    /* renamed from: n, reason: collision with root package name */
    private float f7197n = 0.0f;

    public q(C1701s c1701s, C1701s c1701s2, C1701s c1701s3, C1701s c1701s4, C1589c c1589c) {
        this.f7184f = null;
        this.f7185g = null;
        this.f7186h = null;
        this.f7187i = null;
        this.f7188a = null;
        this.f7189b = null;
        this.f7190c = null;
        this.f7185g = c1701s2;
        this.f7186h = c1701s3;
        this.f7187i = c1701s4;
        this.f7184f = c1701s;
        c1701s4.addTableModelListener(this.f7191d);
        int iCeil = ((int) Math.ceil(C1677fh.c(c1701s4))) / 6;
        this.f7188a = new i(c1701s, iCeil);
        double d2 = C1677fh.d(c1701s3);
        this.f7189b = new g(iCeil, 100.0d);
        this.f7190c = new g(iCeil, d2);
    }

    public void a(p pVar) throws V.a {
        double dA;
        if (this.f7195l == -1) {
            this.f7195l = pVar.g();
            this.f7189b.a(pVar.c(), pVar.g());
            this.f7188a.a(c(), pVar.g());
            return;
        }
        this.f7195l = pVar.g();
        try {
            this.f7189b.a(pVar.c(), pVar.g());
            this.f7188a.a(c(), pVar.g());
            this.f7190c.a(pVar.j(), pVar.g());
            if (this.f7185g.D() != null) {
                this.f7192j = this.f7185g.D();
            } else {
                this.f7192j = this.f7185g.a(this.f7186h, this.f7193e);
            }
            double dB = C1677fh.b(this.f7185g.b(), pVar.a());
            double rowCount = (this.f7185g.getRowCount() - C1677fh.a(this.f7185g.a(), pVar.b())) - 1.0d;
            double dB2 = C1677fh.b(this.f7186h.b(), pVar.h());
            double rowCount2 = (this.f7186h.getRowCount() - C1677fh.a(this.f7186h.a(), pVar.i())) - 1.0d;
            if (!pVar.e()) {
                long jG = pVar.g() - ((int) Math.round(C1677fh.a(this.f7187i, pVar.a(), pVar.b())));
                C1701s c1701sA = this.f7188a.a(jG, 100);
                double d2 = pVar.d();
                double dA2 = this.f7189b.a(jG, 100);
                if (c1701sA == null || Double.isNaN(dA2)) {
                    C.c("filtering record, no historic table close enough.");
                } else {
                    if (dA2 < 50.0d) {
                        dA2 += 100.0d;
                    }
                    for (int i2 = 0; i2 < this.f7185g.getRowCount(); i2++) {
                        Thread.yield();
                        for (int i3 = 0; i3 < this.f7185g.getColumnCount(); i3++) {
                            if (i3 - dB > -1.0d && i3 - dB < 1.0d && i2 - rowCount > -1.0d && i2 - rowCount < 1.0d) {
                                double dAbs = dB - ((double) i3) >= 0.0d ? Math.abs((dB - i3) - 1.0d) : Math.abs((1.0d + dB) - i3);
                                double dAbs2 = rowCount - ((double) i2) >= 0.0d ? Math.abs((rowCount - i2) - 1.0d) : Math.abs((1.0d + rowCount) - i2);
                                double d3 = (dAbs == 0.0d || dAbs2 == 0.0d) ? 0.005d : dAbs * dAbs2;
                                double dDoubleValue = this.f7197n + c1701sA.e(i2, i3).doubleValue();
                                double dA3 = this.f7197n + c1701sA.a(rowCount, dB);
                                if (pVar.k()) {
                                    dA = this.f7190c.a(jG, 100);
                                    if (Double.isNaN(dA)) {
                                        C.d("Target Lambda unavailable, waiting for data.");
                                        return;
                                    }
                                } else {
                                    dA = this.f7186h.a(rowCount2, dB2);
                                }
                                this.f7192j[i2][i3].a(Double.valueOf((((((dDoubleValue * (dA2 / 100.0d)) * d2) / dA) + (((dA3 * (dA2 / 100.0d)) * d2) / dA)) / 2.0d) - this.f7197n), d3);
                            }
                        }
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Error calculating VE values. Debug info written to log file.");
        }
    }

    public C1701s a() {
        return this.f7185g;
    }

    public C1701s b() {
        return this.f7186h;
    }

    public void a(C1701s c1701s) {
        this.f7186h = c1701s;
    }

    public C1701s c() {
        return this.f7184f;
    }

    void a(float f2) {
        this.f7197n = f2;
    }
}
