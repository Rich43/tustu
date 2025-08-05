package com.efiAnalytics.tuningwidgets.panels;

import G.C0083bp;
import G.C0088bu;
import bt.C1273A;
import bt.C1277E;
import bt.C1324bf;
import bt.C1345d;
import bt.C1366y;
import bt.aZ;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.sun.corba.se.impl.util.Version;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aF.class */
public class aF extends C1345d implements G.aN, com.efiAnalytics.ui.aO, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    C1366y f10333a;

    /* renamed from: b, reason: collision with root package name */
    G.R f10334b;

    /* renamed from: c, reason: collision with root package name */
    C1324bf f10335c;

    /* renamed from: d, reason: collision with root package name */
    JPanel f10336d;

    /* renamed from: e, reason: collision with root package name */
    C1273A f10337e = null;

    /* renamed from: f, reason: collision with root package name */
    bt.aT f10338f = null;

    /* renamed from: g, reason: collision with root package name */
    private boolean f10339g;

    public aF(G.R r2) {
        this.f10334b = null;
        this.f10335c = null;
        this.f10336d = null;
        this.f10339g = true;
        this.f10334b = r2;
        this.f10335c = g();
        this.f10336d = j();
        this.f10339g = r2.O().an();
        setLayout(new BorderLayout());
        add("North", this.f10336d);
        add("East", this.f10335c);
        G.aR aRVarA = G.aR.a();
        try {
            aRVarA.a(r2.c(), "reqFuel", this);
            aRVarA.a(r2.c(), "alternate", this);
            aRVarA.a(r2.c(), "nCylinders", this);
            aRVarA.a(r2.c(), "nInjectors", this);
            aRVarA.a(r2.c(), JSplitPane.DIVIDER, this);
        } catch (V.a e2) {
            bH.C.a("Unable to register all parameters.", e2, this);
        }
        n();
        c();
    }

    protected boolean a(String str) throws NumberFormatException {
        G.aM aMVarC = this.f10334b.c("nCylinders");
        G.aM aMVarC2 = this.f10334b.c(JSplitPane.DIVIDER);
        G.aM aMVarC3 = this.f10334b.c("alternate");
        try {
            int iJ = (int) aMVarC.j(this.f10334b.p());
            double d2 = Double.parseDouble(str);
            double dJ = aMVarC.j(this.f10334b.p()) / d2;
            int i2 = (int) dJ;
            double d3 = aMVarC3.j(this.f10334b.p()) > 0.0d ? 2.0d : 1.0d;
            if (this.f10339g) {
                if (iJ % d2 != 0.0d || (d3 == 2.0d && d2 % 2.0d != 0.0d)) {
                    if (d3 != 2.0d || (iJ % d2 == 0.0d && d2 % 2.0d == 0.0d)) {
                        bV.d(iJ + " cyl is not valid with " + ((int) d2) + " squirts.\nAdjust squirts so that:\ncyl / squirts produces a whole number\n", this);
                        return false;
                    }
                    bV.d("Can not alternate " + iJ + " cyl with " + ((int) d2) + " squirts.\nEither set to Simultaneous or adjust squirts so that:\ncyl / squirts produces a whole number with no remainder,\nsquirts is an even number and evenly divisible into the number of cylinders.\n", this);
                    return false;
                }
                if (Math.abs(dJ - i2) > 0.001d) {
                    bV.d(str + " Injections Per Cycle not valid for " + iJ + " cylinders.\n(Number of Cylinders) / (Injections Per Cycle) must produce a whole number.\nPlease correct.\n", this);
                    return false;
                }
            }
            aMVarC2.a(this.f10334b.p(), i2);
            return true;
        } catch (V.g e2) {
            bH.C.a("Unable to retrive values for nCylinders or divider", e2, this);
            return false;
        } catch (V.j e3) {
            bH.C.a("Can not set value", e3, this);
            return false;
        }
    }

    public void a() {
        C1277E c1277e = new C1277E(this.f10334b);
        aG aGVar = new aG(this);
        Window windowB = bV.b(this);
        C1497an c1497an = new C1497an(windowB, this.f10334b, c1277e, aGVar);
        bV.a((Component) windowB, (Component) c1497an);
        c1497an.setVisible(true);
    }

    protected void b(String str) {
        this.f10337e.getText();
        this.f10337e.setText(str);
        this.f10338f.j();
        b();
    }

    private C1324bf g() {
        C0088bu c0088bu = new C0088bu();
        C.a.a();
        c0088bu.v("injControl");
        a(c0088bu, c("Control Algorithm"), "algorithm");
        a(c0088bu, c("Injector Staging"), "alternate");
        a(c0088bu, c("Engine Stroke"), "twoStroke");
        a(c0088bu, c("Number of Cylinders"), "nCylinders");
        a(c0088bu, c("Injector Port Type"), "injType");
        a(c0088bu, c("Number of Injectors"), "nInjectors");
        a(c0088bu, c("Engine Type"), "engineType");
        C1324bf c1324bf = new C1324bf(this.f10334b, c0088bu);
        JPanel jPanelH = h();
        jPanelH.add(BorderLayout.CENTER, new aZ(c("Squirts Per Engine Cycle")));
        this.f10333a = new C1366y();
        this.f10333a.addItem("1");
        this.f10333a.addItem("2");
        this.f10333a.addItem("3");
        this.f10333a.addItem("4");
        this.f10333a.addItem("5");
        this.f10333a.addItem("6");
        this.f10333a.addItem("7");
        this.f10333a.addItem("8");
        this.f10333a.a(new aH(this));
        jPanelH.add("East", this.f10333a);
        c1324bf.add(jPanelH, 1);
        return c1324bf;
    }

    private C0083bp a(C0088bu c0088bu, String str, String str2) {
        C0083bp c0083bp = new C0083bp();
        c0083bp.e(str);
        c0083bp.a(str2);
        c0088bu.a(c0083bp);
        return c0083bp;
    }

    private String c(String str) {
        return C1818g.b(this.f10334b.u(str));
    }

    private JPanel h() {
        aK aKVar = new aK(this);
        aKVar.setLayout(new BorderLayout(3, 3));
        return aKVar;
    }

    private JPanel j() {
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), c("Calculate Required Fuel")));
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout());
        JButton jButton = new JButton(c("Required Fuel..."));
        jButton.setMnemonic(82);
        jButton.addActionListener(new aI(this));
        jPanel2.add(jButton);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(0, 1, 3, 3));
        this.f10337e = new C1273A("");
        this.f10337e.a(new aJ(this));
        C0083bp c0083bp = new C0083bp();
        c0083bp.e("");
        c0083bp.a("reqFuel");
        this.f10338f = new bt.aT(this.f10334b, c0083bp);
        this.f10338f.c(false);
        try {
            G.aR.a().a(this.f10334b.c(), "reqFuel", this.f10338f);
        } catch (V.a e2) {
            bH.C.a("Error subscribing to reqFuel", e2, this);
        }
        jPanel3.add(this.f10337e);
        jPanel3.add(this.f10338f);
        jPanel.add("East", jPanel3);
        return jPanel;
    }

    private void k() throws NumberFormatException {
        b();
    }

    public void b() throws NumberFormatException {
        G.aM aMVarC = this.f10334b.c("nInjectors");
        G.aM aMVarC2 = this.f10334b.c(JSplitPane.DIVIDER);
        G.aM aMVarC3 = this.f10334b.c("reqFuel");
        G.aM aMVarC4 = this.f10334b.c("alternate");
        try {
            String text = this.f10337e.getText();
            if (text.equals("")) {
                text = Version.BUILD;
            }
            double d2 = Double.parseDouble(text);
            double dJ = aMVarC4.j(this.f10334b.p()) + 1.0d;
            double dJ2 = aMVarC2.j(this.f10334b.p());
            aMVarC3.a(this.f10334b.p(), ((d2 * dJ) * dJ2) / aMVarC.j(this.f10334b.p()));
        } catch (V.g e2) {
            e2.printStackTrace();
        } catch (V.j e3) {
            bV.d("ReqFuel " + e3.getMessage(), this);
            try {
                if (e3.a() == 2) {
                    aMVarC3.a(this.f10334b.h(), aMVarC3.q());
                } else if (e3.a() == 1) {
                    aMVarC3.a(this.f10334b.h(), aMVarC3.r());
                }
            } catch (V.g e4) {
                bH.C.a("Error trying to set reqFuel.");
                e4.printStackTrace();
            } catch (V.j e5) {
                bH.C.a("reset reqFuel but it is still out of bounds? This shouldn't happen.");
                e5.printStackTrace();
            }
            this.f10338f.i();
        }
    }

    @Override // G.aN
    public void a(String str, String str2) throws NumberFormatException {
        if (str2.startsWith("reqFuel")) {
            c();
            return;
        }
        if (str2.startsWith("alternate")) {
            if (m()) {
                k();
            }
        } else if (str2.startsWith("nCylinders")) {
            if (m()) {
                k();
            }
        } else if (str2.startsWith(JSplitPane.DIVIDER)) {
            k();
        } else if (str2.startsWith("nInjectors") && m()) {
            k();
        }
    }

    private boolean m() {
        return a(this.f10333a.a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean n() {
        G.aM aMVarC = this.f10334b.c("nCylinders");
        G.aM aMVarC2 = this.f10334b.c(JSplitPane.DIVIDER);
        try {
            this.f10333a.a("" + ((int) (aMVarC.j(this.f10334b.p()) / aMVarC2.j(this.f10334b.p()))));
            return true;
        } catch (V.g e2) {
            bH.C.a("Unable to retrive values for nCylinders or divider", e2, this);
            return true;
        }
    }

    public void c() {
        G.aM aMVarC = this.f10334b.c("nInjectors");
        G.aM aMVarC2 = this.f10334b.c(JSplitPane.DIVIDER);
        G.aM aMVarC3 = this.f10334b.c("reqFuel");
        G.aM aMVarC4 = this.f10334b.c("alternate");
        try {
            this.f10337e.setText(bH.W.b((aMVarC3.j(this.f10334b.p()) * aMVarC.j(this.f10334b.p())) / ((aMVarC4.j(this.f10334b.p()) + 1.0d) * aMVarC2.j(this.f10334b.p())), 1));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.efiAnalytics.ui.aO
    public void d() {
        try {
            this.f10334b.p().d();
        } catch (V.g e2) {
            bH.C.a("Error performing redo:", e2, this);
        }
    }

    @Override // com.efiAnalytics.ui.aO
    public void e() {
        try {
            this.f10334b.p().c();
        } catch (V.g e2) {
            bH.C.a("Error performing undo:", e2, this);
        }
    }

    @Override // com.efiAnalytics.ui.aO
    public void f() {
        this.f10334b.I();
    }

    @Override // com.efiAnalytics.ui.aO
    public void i() {
        f();
        this.f10338f.close();
        this.f10335c.close();
        G.aR.a().a(this);
        l();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        G.aR.a().a(this);
    }
}
