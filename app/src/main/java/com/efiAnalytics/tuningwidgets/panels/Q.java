package com.efiAnalytics.tuningwidgets.panels;

import G.C0072be;
import G.bL;
import W.C0184j;
import W.C0188n;
import bt.InterfaceC1357p;
import bt.bN;
import i.C1743c;
import i.InterfaceC1741a;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/Q.class */
public class Q implements bN, InterfaceC1741a {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1357p f10285a;

    /* renamed from: b, reason: collision with root package name */
    G.R f10286b;

    /* renamed from: c, reason: collision with root package name */
    C0072be f10287c;

    /* renamed from: d, reason: collision with root package name */
    String f10288d;

    /* renamed from: i, reason: collision with root package name */
    private S f10292i;

    /* renamed from: e, reason: collision with root package name */
    String f10289e = null;

    /* renamed from: f, reason: collision with root package name */
    int f10290f = -1000;

    /* renamed from: g, reason: collision with root package name */
    String f10291g = "UNINITIALIZED";

    /* renamed from: h, reason: collision with root package name */
    Runnable f10293h = new R(this);

    public Q(G.R r2, C0072be c0072be, InterfaceC1357p interfaceC1357p) {
        this.f10288d = null;
        this.f10292i = null;
        this.f10286b = r2;
        this.f10285a = interfaceC1357p;
        this.f10287c = c0072be;
        c();
        this.f10288d = bL.j(r2, c0072be.d());
        if (this.f10288d == null && c0072be.d() != null && c0072be.d().equals("rpm") && r2.g("RpmHiRes") != null) {
            this.f10288d = bL.j(r2, "RpmHiRes");
        }
        if (this.f10288d == null && c0072be.g() != null && r2.g(c0072be.g()) != null) {
            this.f10288d = c0072be.g();
        }
        if (this.f10288d == null) {
            bH.C.b("No Data Log field defined for X axis of table: " + c0072be.aJ());
        }
        if (r2.c("LKUP_PARAMETER") == null || r2.c("LOAD_AXIS_SCALE") == null) {
            return;
        }
        this.f10292i = new S(this);
        try {
            G.aR.a().a(r2.c(), "LKUP_PARAMETER", this.f10292i);
            G.aR.a().a(r2.c(), "LOAD_AXIS_SCALE", this.f10292i);
        } catch (V.a e2) {
            Logger.getLogger(Q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public void a(boolean z2) {
        if (z2) {
            C1743c.a().a(this);
        } else {
            C1743c.a().b(this);
        }
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        if (i2 - this.f10290f == 1) {
            double d2 = C1743c.a().e().b(this.f10288d) != null ? r0.d(i2) : Double.NaN;
            c();
            this.f10285a.a((C1743c.a().e().b(this.f10289e) != null ? C1743c.a().e().b(this.f10289e) : C1743c.a().e().b("MAP") != null ? C1743c.a().e().b("MAP") : null) != null ? r11.d(i2) : Double.NaN, d2);
        } else {
            b(i2);
        }
        this.f10290f = i2;
    }

    public void b(int i2) {
        int iC = i2 - this.f10285a.c();
        if (iC < 0) {
            iC = 0;
        }
        c();
        C0184j c0184jB = C1743c.a().e().b(this.f10288d);
        C0184j c0184jB2 = C1743c.a().e().b(this.f10289e);
        if (c0184jB2 == null && C1743c.a().e().b("MAP") != null) {
            c0184jB2 = C1743c.a().e().b("MAP");
        }
        if (C1743c.a().e() == null || c0184jB == null || c0184jB2 == null || i2 < iC) {
            return;
        }
        float[] fArr = new float[(i2 - iC) + 1];
        float[] fArr2 = new float[(i2 - iC) + 1];
        for (int i3 = iC; i3 <= i2; i3++) {
            double d2 = c0184jB.d(i3);
            double d3 = c0184jB2.d(i3);
            fArr[(fArr.length - 1) - (i3 - iC)] = (float) d2;
            fArr2[(fArr2.length - 1) - (i3 - iC)] = (float) d3;
        }
        this.f10285a.a(fArr2, fArr);
    }

    @Override // bt.bN
    public void a() {
        a(true);
    }

    @Override // bt.bN
    public void b() {
        a(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        G.aM aMVarC = this.f10286b.c(this.f10287c.b());
        C0188n c0188nE = C1743c.a().e();
        if (c0188nE == null) {
            this.f10291g = "UNINITIALIZED";
        } else {
            if (this.f10291g != null && !this.f10291g.equals("UNINITIALIZED") && aMVarC.o() != null && this.f10291g != null && this.f10291g.equals(aMVarC.o()) && this.f10292i == null) {
                return;
            }
            if (aMVarC != null) {
                this.f10291g = aMVarC.o();
            } else {
                this.f10291g = null;
            }
        }
        this.f10289e = bL.j(this.f10286b, this.f10287c.f());
        if (this.f10289e == null && this.f10287c.h() != null && this.f10286b.g(this.f10287c.h()) != null) {
            this.f10289e = this.f10287c.h();
        }
        if (this.f10289e == null) {
            this.f10289e = "MAP";
        }
        if (c0188nE != null) {
            C0184j c0184jA = c0188nE.a(this.f10289e);
            G.aM aMVarC2 = this.f10286b.c("LKUP_PARAMETER");
            G.aM aMVarC3 = this.f10286b.c("LOAD_AXIS_SCALE");
            if (aMVarC2 != null && aMVarC3 != null && this.f10287c.f() != null && this.f10287c.f().equals("Load")) {
                try {
                    String strF = aMVarC2.f(this.f10286b.h());
                    String strF2 = aMVarC3.f(this.f10286b.h());
                    if (strF != null && strF.contains("TPS") && c0188nE.a("TPS") != null) {
                        this.f10289e = "TPS";
                    } else if (strF == null || !strF.contains("MAP") || strF2 == null) {
                        if (c0188nE.a("Fuel Load") != null) {
                            this.f10289e = "Fuel Load";
                        } else {
                            this.f10289e = "MAP";
                        }
                    } else if (strF2.toLowerCase().contains("psi") && c0188nE.a("PRESS_BOOST") != null) {
                        this.f10289e = "PRESS_BOOST";
                    } else if (strF2.toLowerCase().contains("psi") && c0188nE.a("BOOST_PSI") != null) {
                        this.f10289e = "BOOST_PSI";
                    } else if (!strF2.toLowerCase().contains("kpa") || c0188nE.a("MAP") == null) {
                        this.f10289e = "Fuel Load";
                    } else {
                        this.f10289e = "MAP";
                    }
                } catch (Exception e2) {
                    Logger.getLogger(Q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    this.f10289e = "Fuel Load";
                }
            } else if (c0184jA == null || ((this.f10289e.equals("Load") && c0184jA.e() == c0184jA.f()) || (aMVarC != null && aMVarC.o() != null && !aMVarC.o().isEmpty() && c0184jA.n() != null && !c0184jA.n().isEmpty() && !c0184jA.n().equalsIgnoreCase(aMVarC.o())))) {
                if (aMVarC.o().equalsIgnoreCase("kpa") && c0188nE.b("MAP") != null) {
                    this.f10289e = "MAP";
                } else if (aMVarC.o().equalsIgnoreCase("TPS") && c0188nE.b("TPS") != null) {
                    this.f10289e = "TPS";
                } else if (aMVarC.o().equalsIgnoreCase("TPS") && c0188nE.b("TPS_Pct") != null) {
                    this.f10289e = "TPS_Pct";
                } else if (aMVarC.o().equalsIgnoreCase("psi") && c0188nE.b("Boost psi") != null) {
                    this.f10289e = "Boost psi";
                } else if (aMVarC.o().equalsIgnoreCase("psi") && c0188nE.b("Boost") != null) {
                    this.f10289e = "Boost";
                } else if (aMVarC.o().equalsIgnoreCase("psig") && c0188nE.b("PRESS_BOOST") != null) {
                    this.f10289e = "PRESS_BOOST";
                } else if (aMVarC.o().equalsIgnoreCase("psig") && c0188nE.b("BOOST_PSI") != null) {
                    this.f10289e = "BOOST_PSI";
                }
            }
        }
        if (this.f10289e == null) {
            bH.C.b("No Data Log field defined for Y axis of table: " + this.f10287c.aJ());
        }
    }
}
