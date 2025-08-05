package aP;

import G.C0135r;
import W.C0196v;
import W.C0200z;
import bH.C1011s;
import com.efiAnalytics.ui.C1634ds;
import com.efiAnalytics.ui.InterfaceC1604co;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: aP.ib, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ib.class */
public class C0421ib implements com.efiAnalytics.ui.fR {

    /* renamed from: f, reason: collision with root package name */
    Window f3719f;

    /* renamed from: a, reason: collision with root package name */
    aE f3713a = new aE();

    /* renamed from: b, reason: collision with root package name */
    C0224at f3714b = new C0224at();

    /* renamed from: c, reason: collision with root package name */
    aJ f3715c = new aJ();

    /* renamed from: d, reason: collision with root package name */
    C0220ap f3716d = new C0220ap();

    /* renamed from: h, reason: collision with root package name */
    private aE.a f3717h = null;

    /* renamed from: e, reason: collision with root package name */
    C1634ds f3718e = new C1634ds("Project Properties");

    /* renamed from: g, reason: collision with root package name */
    ArrayList f3720g = new ArrayList();

    public C0421ib(Window window) {
        this.f3719f = null;
        this.f3719f = window;
        try {
            this.f3718e.a(this);
            this.f3714b.b(true);
            this.f3718e.a(this.f3714b, "Configuration");
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new FlowLayout(1));
            jPanel.add(this.f3713a);
            this.f3718e.a(new C0422ic(this, jPanel), "Settings");
            this.f3718e.a(this.f3716d, "CAN Devices");
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Failed to create Option tabs, see log for details.", window);
            bH.C.a("Failed to create Option tabs, see log for details.");
            e2.printStackTrace();
        }
    }

    public void a() {
        this.f3718e.a(this.f3719f, "Project Properties");
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b() {
        try {
            if (!this.f3714b.e()) {
                this.f3718e.a((Component) this.f3714b);
                return false;
            }
            File fileH = this.f3714b.h();
            if (!fileH.equals(this.f3717h.j())) {
                this.f3717h.l(fileH.getName().toLowerCase().endsWith(".ecu") ? "mainController.ecu" : "mainController.ini");
                C1011s.a(fileH, this.f3717h.j());
                aE.a.A().remove("firmwareDescription");
            }
            this.f3717h.p(this.f3714b.c());
            this.f3717h.a(this.f3713a.b());
            G.R rC = G.T.a().c();
            if (rC != null) {
                G.aM aMVarC = rC.c("tsCanId");
                if (aMVarC != null) {
                    try {
                        aMVarC.a(rC.p(), this.f3716d.b());
                    } catch (V.g e2) {
                        Logger.getLogger(C0421ib.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    } catch (V.j e3) {
                        Logger.getLogger(C0421ib.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                }
            }
            this.f3717h.a(this.f3716d.b());
            this.f3717h.b(this.f3716d.c());
            if (!this.f3716d.d()) {
                this.f3718e.a((Component) this.f3716d);
                return false;
            }
            aE.d[] dVarArrA = this.f3716d.a();
            this.f3717h.H();
            if (dVarArrA != null) {
                for (int i2 = 0; i2 < dVarArrA.length; i2++) {
                    this.f3717h.a(dVarArrA[i2]);
                    G.R rC2 = G.T.a().c(dVarArrA[i2].a());
                    if (rC2 != null) {
                        try {
                            rC2.c("tsCanId").a(rC2.p(), dVarArrA[i2].e());
                        } catch (V.g e4) {
                            Logger.getLogger(C0421ib.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        } catch (V.j e5) {
                            Logger.getLogger(C0421ib.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                        }
                    }
                }
            }
            String strA = C0200z.a(fileH);
            if (cZ.a().h() != null) {
                cZ.a().h().c(strA);
            } else if (cZ.a().b() != null) {
                cZ.a().b().c(strA);
            }
            aE.g gVarI = this.f3717h.i();
            gVarI.setProperty("firstGearRatio", this.f3715c.a());
            gVarI.setProperty("secondGearRatio", this.f3715c.b());
            gVarI.setProperty("thirdGearRatio", this.f3715c.c());
            gVarI.setProperty("fourthGearRatio", this.f3715c.d());
            gVarI.setProperty("fifthGearRatio", this.f3715c.e());
            gVarI.setProperty("sixthGearRatio", this.f3715c.f());
            gVarI.setProperty("weight", this.f3715c.j());
            gVarI.setProperty("transmissionType", this.f3715c.g() ? "Automatic" : "Manual");
            gVarI.setProperty("converterStall", this.f3715c.h());
            gVarI.setProperty("finalDriveRatio", this.f3715c.i());
            gVarI.a(this.f3717h.h());
            this.f3717h.b();
            e();
            d();
            return true;
        } catch (V.a e6) {
            bH.C.a("Unable to save changes. Error message:\n", e6, this.f3714b);
            e6.printStackTrace();
            return false;
        }
    }

    @Override // com.efiAnalytics.ui.fR
    public void c() {
        d();
    }

    private void d() {
        this.f3716d.e();
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean a(Container container) {
        return true;
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b(Container container) {
        return true;
    }

    public void a(aE.a aVar) {
        this.f3717h = aVar;
        this.f3714b.a(aVar.j());
        this.f3714b.g(aVar.u());
        this.f3714b.c(aVar.t());
        this.f3714b.e(aVar.B());
        if (aVar.E().P() != null) {
            this.f3714b.b(aVar.E().P());
        } else {
            this.f3714b.b(C1818g.b("Serial Signature") + ": " + aVar.E().i());
        }
        this.f3713a.a(new W.I().a(C0196v.a().b(aVar.j().getAbsolutePath()), aVar.j().getAbsolutePath()));
        G.R rC = G.T.a().c();
        this.f3718e.a("CAN Devices", rC.b());
        HashMap mapM = rC.m();
        for (String str : mapM.keySet()) {
            if (mapM.get(str) == null || !(str.equals("") || str.equals("DEFAULT"))) {
                this.f3713a.a(str);
            } else {
                this.f3713a.a((C0135r) mapM.get(str));
            }
        }
        aE.g gVarI = aVar.i();
        this.f3715c.a(gVarI.getProperty("firstGearRatio", ""));
        this.f3715c.b(gVarI.getProperty("secondGearRatio", ""));
        this.f3715c.c(gVarI.getProperty("thirdGearRatio", ""));
        this.f3715c.d(gVarI.getProperty("fourthGearRatio", ""));
        this.f3715c.e(gVarI.getProperty("fifthGearRatio", ""));
        this.f3715c.f(gVarI.getProperty("sixthGearRatio", ""));
        this.f3715c.i(gVarI.getProperty("weight", ""));
        this.f3715c.a(gVarI.getProperty("transmissionType", "").equals("Automatic"));
        this.f3715c.g(gVarI.getProperty("converterStall", ""));
        this.f3715c.h(gVarI.getProperty("finalDriveRatio", ""));
        this.f3716d.a(aVar);
    }

    public void a(String str) {
        this.f3718e.a(str);
    }

    public void a(InterfaceC1604co interfaceC1604co) {
        this.f3720g.add(interfaceC1604co);
    }

    private void e() {
        Iterator it = this.f3720g.iterator();
        while (it.hasNext()) {
            ((InterfaceC1604co) it.next()).a();
        }
    }
}
