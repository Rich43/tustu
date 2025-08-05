package bt;

import G.C0075bh;
import G.C0088bu;
import G.C0126i;
import bF.C0971b;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bt/bS.class */
class bS implements G.aN, bF.B {

    /* renamed from: a, reason: collision with root package name */
    G.R f8952a;

    /* renamed from: b, reason: collision with root package name */
    G.aM f8953b;

    /* renamed from: d, reason: collision with root package name */
    bF.y f8955d;

    /* renamed from: i, reason: collision with root package name */
    final /* synthetic */ bO f8960i;

    /* renamed from: c, reason: collision with root package name */
    List f8954c = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    long f8956e = System.currentTimeMillis();

    /* renamed from: f, reason: collision with root package name */
    int f8957f = 3000;

    /* renamed from: g, reason: collision with root package name */
    bT f8958g = null;

    /* renamed from: h, reason: collision with root package name */
    boolean f8959h = false;

    bS(bO bOVar, G.R r2, bF.y yVar, String str) throws V.a {
        this.f8960i = bOVar;
        this.f8952a = r2;
        this.f8955d = yVar;
        C0088bu c0088buC = r2.e().c(str);
        if (c0088buC != null && !(c0088buC instanceof C0075bh)) {
            throw new V.a(str + " not defined as a 1DTable");
        }
        C0075bh c0075bh = (C0075bh) c0088buC;
        if (c0075bh.b() > 0) {
            this.f8953b = r2.c(c0075bh.d(0));
            G.aR.a().a(r2.c(), this.f8953b.aJ(), this);
            C0126i.a(r2.c(), this.f8953b, this);
        }
        for (int i2 = 0; i2 < c0075bh.a(); i2++) {
            G.aM aMVarC = r2.c(c0075bh.b(i2));
            G.aR.a().a(r2.c(), aMVarC.aJ(), this);
            this.f8954c.add(aMVarC);
            C0126i.a(r2.c(), aMVarC, this);
        }
        yVar.a(this);
        yVar.a(c0075bh.i());
    }

    public void a() {
        G.aR.a().a(this);
        this.f8955d.b(this);
    }

    private void b(String str) {
        if (this.f8958g != null) {
            this.f8958g.a(str);
            return;
        }
        this.f8958g = new bT(this);
        this.f8958g.a(str);
        this.f8958g.start();
    }

    @Override // G.aN
    public void a(String str, String str2) {
        b(str2);
    }

    public void a(String str) {
        synchronized (this) {
            this.f8959h = true;
            if (this.f8953b == null || !str.equals(this.f8953b.aJ())) {
                for (int i2 = 0; i2 < this.f8954c.size(); i2++) {
                    if (((G.aM) this.f8954c.get(i2)).aJ().equals(str)) {
                        try {
                            double[][] dArrI = ((G.aM) this.f8954c.get(i2)).i(this.f8952a.h());
                            for (int i3 = 0; i3 < dArrI.length; i3++) {
                                try {
                                    int i4 = this.f8955d.g() ? i2 + 1 : i2;
                                    if (this.f8955d.f()) {
                                        this.f8955d.a((Object) Double.valueOf(dArrI[i3][0]), i3, i4);
                                    } else {
                                        this.f8955d.a((Object) Double.valueOf(dArrI[i3][0]), i4, i3);
                                    }
                                } catch (Exception e2) {
                                    Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                                }
                            }
                        } catch (V.g e3) {
                            Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                        }
                    }
                }
                this.f8959h = false;
            } else if (this.f8955d.g()) {
                try {
                    double[][] dArrI2 = this.f8953b.i(this.f8952a.h());
                    for (int i5 = 0; i5 < dArrI2.length; i5++) {
                        try {
                            if (this.f8955d.f()) {
                                this.f8955d.setValueAt(Double.valueOf(dArrI2[i5][0]), i5, 0);
                            } else {
                                this.f8955d.setValueAt(Double.valueOf(dArrI2[i5][0]), 0, i5);
                            }
                        } catch (Exception e4) {
                            Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        }
                    }
                } catch (V.g e5) {
                    Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
                this.f8959h = false;
            } else if (this.f8955d.h()) {
                try {
                    double[][] dArrI3 = this.f8953b.i(this.f8952a.h());
                    int iA = this.f8955d.a() - 1;
                    for (int i6 = 0; i6 < dArrI3.length; i6++) {
                        try {
                            if (this.f8955d.f()) {
                                this.f8955d.setValueAt(Double.valueOf(dArrI3[i6][0]), i6, iA);
                            } else {
                                this.f8955d.setValueAt(Double.valueOf(dArrI3[i6][0]), iA, i6);
                            }
                        } catch (Exception e6) {
                            Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                        }
                    }
                } catch (V.g e7) {
                    Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                }
                this.f8959h = false;
            } else {
                try {
                    bF.x xVarC = this.f8955d.c();
                    if (xVarC instanceof C0971b) {
                        C0971b c0971b = (C0971b) xVarC;
                        String[] strArrC = this.f8953b.c(this.f8952a.h());
                        for (int i7 = 0; i7 < strArrC.length; i7++) {
                            c0971b.a(i7, strArrC[i7]);
                        }
                    }
                } catch (V.g e8) {
                    Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
                }
                this.f8959h = false;
            }
        }
    }

    @Override // bF.B
    public void a(int i2, int i3, double d2) {
        G.aM aMVar;
        synchronized (this) {
            if (!this.f8959h) {
                if (this.f8955d.g() && i2 == 0) {
                    aMVar = this.f8953b;
                } else if (this.f8955d.h() && i2 == this.f8955d.a() - 1) {
                    aMVar = this.f8953b;
                } else if (this.f8955d.g()) {
                    i2--;
                    aMVar = (G.aM) this.f8954c.get(i2);
                } else {
                    aMVar = (G.aM) this.f8954c.get(i2);
                }
                try {
                    try {
                        if (d2 != G.Y.f517j) {
                            aMVar.a(this.f8952a.h(), d2, i3, 0);
                            double d3 = aMVar.i(this.f8952a.h())[i3][0];
                            if (this.f8955d.g() && !aMVar.aJ().equals(this.f8953b.aJ())) {
                                i2++;
                            }
                            if (this.f8955d.f()) {
                                this.f8955d.a((Object) Double.valueOf(d3), i3, i2);
                            } else {
                                this.f8955d.a((Object) Double.valueOf(d3), i2, i3);
                            }
                        }
                    } catch (V.g e2) {
                        Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                } catch (V.j e3) {
                    if (this.f8955d.g() && !aMVar.aJ().equals(this.f8953b.aJ())) {
                        i2++;
                    }
                    if (e3.a() == 1) {
                        bH.C.c("Param: " + aMVar.aJ() + " value: " + d2 + " exceeds maximum: " + e3.c());
                        if (this.f8955d.f()) {
                            this.f8955d.setValueAt(Double.valueOf(e3.c()), i3, i2);
                        } else {
                            this.f8955d.setValueAt(Double.valueOf(e3.c()), i2, i3);
                        }
                    } else if (e3.a() == 2) {
                        bH.C.c("Param: " + aMVar.aJ() + " value: " + d2 + " below minimum: " + e3.c());
                        if (this.f8955d.f()) {
                            this.f8955d.setValueAt(Double.valueOf(e3.c()), i3, i2);
                        } else {
                            this.f8955d.setValueAt(Double.valueOf(e3.c()), i2, i3);
                        }
                    }
                }
            }
        }
    }
}
