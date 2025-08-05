package com.efiAnalytics.tuningwidgets.panels;

import G.C0083bp;
import G.C0088bu;
import bt.C1273A;
import bt.C1277E;
import bt.C1324bf;
import bt.C1345d;
import bt.C1366y;
import bt.aZ;
import com.efiAnalytics.ui.bV;
import com.sun.corba.se.impl.util.Version;
import com.sun.javafx.animation.TickCalculation;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.icepdf.core.util.PdfOps;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/az.class */
public class az extends C1345d implements G.aN, com.efiAnalytics.ui.aO {

    /* renamed from: a, reason: collision with root package name */
    C1366y f10430a;

    /* renamed from: b, reason: collision with root package name */
    String f10431b;

    /* renamed from: c, reason: collision with root package name */
    G.R f10432c;

    /* renamed from: d, reason: collision with root package name */
    C1324bf f10433d;

    /* renamed from: e, reason: collision with root package name */
    C1324bf f10434e;

    /* renamed from: f, reason: collision with root package name */
    C1324bf f10435f;

    /* renamed from: g, reason: collision with root package name */
    C1324bf f10436g;

    /* renamed from: h, reason: collision with root package name */
    JPanel f10437h;

    /* renamed from: i, reason: collision with root package name */
    C1273A f10438i = null;

    /* renamed from: j, reason: collision with root package name */
    bt.aT f10439j = null;

    public az(G.R r2, int i2) {
        this.f10431b = "";
        this.f10432c = null;
        this.f10433d = null;
        this.f10434e = null;
        this.f10435f = null;
        this.f10436g = null;
        this.f10437h = null;
        if (i2 != -1) {
            this.f10431b = i2 + "";
        }
        this.f10432c = r2;
        this.f10433d = g();
        this.f10434e = j();
        this.f10435f = k();
        this.f10436g = m();
        this.f10437h = n();
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("North", this.f10437h);
        jPanel.add("South", this.f10436g);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("North", this.f10434e);
        jPanel2.add("South", this.f10435f);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        add("West", jPanel);
        add("East", this.f10433d);
        G.aR aRVarA = G.aR.a();
        try {
            aRVarA.a(r2.c(), "reqFuel" + this.f10431b, this);
            aRVarA.a(r2.c(), "alternate" + this.f10431b, this);
            aRVarA.a(r2.c(), "nCylinders" + this.f10431b, this);
            aRVarA.a(r2.c(), "nInjectors" + this.f10431b, this);
            aRVarA.a(r2.c(), JSplitPane.DIVIDER + this.f10431b, this);
            aRVarA.a(r2.c(), "injOpen" + this.f10431b, this);
            aRVarA.a(r2.c(), "battFac" + this.f10431b, this);
        } catch (V.a e2) {
            bH.C.a("Unable to register all parameters.", e2, this);
        }
        p();
        c();
    }

    protected void a(String str) {
        G.aM aMVarC = this.f10432c.c("nCylinders" + this.f10431b);
        G.aM aMVarC2 = this.f10432c.c(JSplitPane.DIVIDER + this.f10431b);
        G.aM aMVarC3 = this.f10432c.c("alternate" + this.f10431b);
        try {
            int iJ = (int) aMVarC.j(this.f10432c.p());
            double dJ = 1.0E-4d + (aMVarC.j(this.f10432c.p()) / Double.parseDouble(str));
            int i2 = (int) dJ;
            double d2 = aMVarC3.j(this.f10432c.p()) > 0.0d ? 2.0d : 1.0d;
            if (Math.abs(dJ - i2) <= 0.001d && iJ != 0 && (d2 * (dJ - 1.0E-4d)) / iJ <= 1.0d) {
                aMVarC2.a(this.f10432c.p(), i2);
            } else {
                bV.d("Injections Per Cycle not valid for " + iJ + " cylinders.\n(Number of Cylinders) / (Injections Per Cycle) must produce a whole number.\nPlease correct.\nWhen set to Alternating, Squirts Per Engine Cycle must be a multiple of 2.", this);
                p();
            }
        } catch (V.g e2) {
            bH.C.a("Unable to retrive values for nCylinders or divider", e2, this);
        } catch (V.j e3) {
            bH.C.a("Can not set value", e3, this);
        }
    }

    public void a() {
        C1277E c1277e = new C1277E(this.f10432c);
        aA aAVar = new aA(this);
        Window windowB = bV.b(this);
        C1497an c1497an = new C1497an(windowB, this.f10432c, c1277e, aAVar);
        bV.a((Component) windowB, (Component) c1497an);
        c1497an.setVisible(true);
    }

    protected void b(String str) {
        this.f10438i.getText();
        this.f10438i.setText(str);
        this.f10439j.j();
        try {
            b();
        } catch (V.j e2) {
            bV.d(e2.getMessage(), this);
            this.f10439j.i();
        }
    }

    private C1324bf g() {
        C0088bu c0088bu = new C0088bu();
        c0088bu.v("injControl");
        a(c0088bu, c("Control Algorithm"), "algorithm" + this.f10431b);
        a(c0088bu, c("Injector Staging"), "alternate" + this.f10431b);
        a(c0088bu, c("Engine Stroke"), "twoStroke" + this.f10431b);
        a(c0088bu, c("Number of Cylinders"), "nCylinders" + this.f10431b);
        a(c0088bu, c("Injector Port Type"), "injType" + this.f10431b);
        a(c0088bu, c("Number of Injectors"), "nInjectors" + this.f10431b);
        a(c0088bu, c("MAP Type"), "mapType" + this.f10431b);
        a(c0088bu, c("Engine Type"), "engineType" + this.f10431b);
        C1324bf c1324bf = new C1324bf(this.f10432c, c0088bu);
        JPanel jPanelH = h();
        jPanelH.add(BorderLayout.CENTER, new aZ("Squirts Per Engine Cycle"));
        this.f10430a = new C1366y();
        this.f10430a.addItem("1");
        this.f10430a.addItem("2");
        this.f10430a.addItem("3");
        this.f10430a.addItem("4");
        this.f10430a.addItem("5");
        this.f10430a.addItem("6");
        this.f10430a.addItem("7");
        this.f10430a.addItem("8");
        this.f10430a.a(new aB(this));
        jPanelH.add("East", this.f10430a);
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

    private JPanel h() {
        aE aEVar = new aE(this);
        aEVar.setLayout(new BorderLayout(3, 3));
        return aEVar;
    }

    private C1324bf j() {
        C0088bu c0088bu = new C0088bu();
        c0088bu.v("injCharacteristics");
        a(c0088bu, c("Injector Opening Time"), "injOpen" + this.f10431b);
        a(c0088bu, c("Battery Voltage Correction"), "battFac" + this.f10431b);
        a(c0088bu, c("PWM Current Limit"), "injPwmP" + this.f10431b);
        a(c0088bu, c("PWM Time"), "injPwmT" + this.f10431b);
        return new C1324bf(this.f10432c, c0088bu);
    }

    private C1324bf k() {
        C0088bu c0088bu = new C0088bu();
        c0088bu.v("injfastIdle");
        String str = "fastIdleT" + this.f10431b;
        G.aM aMVarC = this.f10432c.c(str);
        String strO = PdfOps.F_TOKEN;
        if (aMVarC != null) {
            strO = aMVarC.o();
        }
        a(c0088bu, c("Fast Idle Threshold") + " (" + bH.S.a() + strO + ")", str);
        return new C1324bf(this.f10432c, c0088bu);
    }

    private C1324bf m() {
        C0088bu c0088bu = new C0088bu();
        c0088bu.v("baroCorrection");
        a(c0088bu, c("Barometric Correction"), "baroCorr" + this.f10431b);
        return new C1324bf(this.f10432c, c0088bu);
    }

    private JPanel n() {
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), c("Calculate Required Fuel")));
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout());
        JButton jButton = new JButton(c("Required Fuel..."));
        jButton.setMnemonic(82);
        jButton.addActionListener(new aC(this));
        jPanel2.add(jButton);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(0, 1, 3, 3));
        this.f10438i = new C1273A("");
        this.f10438i.a(new aD(this));
        C0083bp c0083bp = new C0083bp();
        c0083bp.e("");
        c0083bp.a("reqFuel" + this.f10431b);
        this.f10439j = new bt.aT(this.f10432c, c0083bp);
        this.f10439j.c(false);
        try {
            G.aR.a().a(this.f10432c.c(), "reqFuel" + this.f10431b, this.f10439j);
        } catch (V.a e2) {
            bH.C.a("Error subscribing to reqFuel" + this.f10431b, e2, this);
        }
        jPanel3.add(this.f10438i);
        jPanel3.add(this.f10439j);
        jPanel.add("East", jPanel3);
        return jPanel;
    }

    private void o() throws NumberFormatException {
        try {
            b();
        } catch (V.j e2) {
            bV.d(e2.getMessage(), this);
            e();
        }
    }

    public void b() throws V.j, NumberFormatException {
        G.aM aMVarC = this.f10432c.c("nInjectors" + this.f10431b);
        G.aM aMVarC2 = this.f10432c.c(JSplitPane.DIVIDER + this.f10431b);
        G.aM aMVarC3 = this.f10432c.c("reqFuel" + this.f10431b);
        G.aM aMVarC4 = this.f10432c.c("alternate" + this.f10431b);
        try {
            String text = this.f10438i.getText();
            if (text.equals("")) {
                text = Version.BUILD;
            }
            double d2 = Double.parseDouble(text);
            double dJ = aMVarC4.j(this.f10432c.p()) + 1.0d;
            double dJ2 = aMVarC2.j(this.f10432c.p());
            aMVarC3.a(this.f10432c.p(), ((d2 * dJ) * dJ2) / aMVarC.j(this.f10432c.p()));
        } catch (V.g e2) {
            e2.printStackTrace();
        }
    }

    private String c(String str) {
        return C1818g.b(this.f10432c.u(str));
    }

    @Override // G.aN
    public void a(String str, String str2) throws NumberFormatException {
        if (str2.startsWith("reqFuel")) {
            c();
            return;
        }
        if (str2.startsWith("alternate")) {
            if (p()) {
                o();
                return;
            }
            return;
        }
        if (str2.startsWith("nCylinders")) {
            if (p()) {
                o();
                return;
            }
            return;
        }
        if (str2.startsWith("nInjectors")) {
            if (p()) {
                o();
                return;
            }
            return;
        }
        if (str2.startsWith(JSplitPane.DIVIDER)) {
            if (p()) {
                o();
                return;
            }
            return;
        }
        if (str2.startsWith("injOpen" + this.f10431b)) {
            try {
                double dJ = this.f10432c.c("injOpen" + this.f10431b).j(this.f10432c.p());
                if (dJ < 0.899d || dJ > 1.301d) {
                    bV.d("WARNING!!\n\nInjector Open Time is not within a normal range.\nThe expected value is between 0.9 and 1.3 ms.\nThe typical value is 1.0", this);
                }
                return;
            } catch (V.g e2) {
                bV.d("Error testing Injector Open Time", this);
                return;
            }
        }
        if (str2.startsWith("battFac" + this.f10431b)) {
            try {
                double dJ2 = this.f10432c.c("battFac" + this.f10431b).j(this.f10432c.p());
                if (dJ2 < 0.099d || dJ2 > 0.201d) {
                    bV.d("WARNING!!\n\nBattery Voltage Correction is not within a normal range.\nThe expected value is between 0.1 and 0.3 ms/V.\nThe typical value is 0.1", this);
                }
            } catch (V.g e3) {
                bV.d("Error testing Battery Correction Factor", this);
            }
        }
    }

    private boolean p() {
        G.aM aMVarC = this.f10432c.c("nCylinders" + this.f10431b);
        G.aM aMVarC2 = this.f10432c.c(JSplitPane.DIVIDER + this.f10431b);
        G.aM aMVarC3 = this.f10432c.c("rpmk" + this.f10431b);
        G.aM aMVarC4 = this.f10432c.c("twoStroke" + this.f10431b);
        try {
            int iJ = (int) aMVarC.j(this.f10432c.p());
            double dJ = 1.0E-4d + (aMVarC.j(this.f10432c.p()) / aMVarC2.j(this.f10432c.p()));
            int i2 = (int) dJ;
            if (Math.abs(dJ - i2) > 0.001d) {
                bV.d("Injections Per Cycle not valid for " + iJ + " cylinders.\n(Number of Cylinders) / (Injections Per Cycle) must produce a whole number.", this);
                return false;
            }
            this.f10430a.a("" + i2);
            aMVarC3.a(this.f10432c.p(), (aMVarC4.j(this.f10432c.p()) != 0.0d ? TickCalculation.TICKS_PER_SECOND : 12000) / iJ);
            return true;
        } catch (V.g e2) {
            bH.C.a("Unable to retrive values for nCylinders or divider", e2, this);
            return true;
        } catch (V.j e3) {
            bH.C.a("Value Out Of Bounds for rpmk, this shouldn't happen.\nPlease report it.", e3, this);
            return true;
        }
    }

    public void c() {
        G.aM aMVarC = this.f10432c.c("nInjectors" + this.f10431b);
        G.aM aMVarC2 = this.f10432c.c(JSplitPane.DIVIDER + this.f10431b);
        try {
            this.f10438i.setText(bH.W.b((this.f10432c.c("reqFuel" + this.f10431b).j(this.f10432c.p()) * aMVarC.j(this.f10432c.p())) / ((this.f10432c.c("alternate" + this.f10431b).j(this.f10432c.p()) + 1.0d) * aMVarC2.j(this.f10432c.p())), 1));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.efiAnalytics.ui.aO
    public void d() {
        try {
            this.f10432c.p().d();
        } catch (V.g e2) {
            bH.C.a("Error performing redo:", e2, this);
        }
    }

    @Override // com.efiAnalytics.ui.aO
    public void e() {
        try {
            this.f10432c.p().c();
        } catch (V.g e2) {
            bH.C.a("Error performing undo:", e2, this);
        }
    }

    @Override // com.efiAnalytics.ui.aO
    public void f() {
        this.f10432c.I();
    }

    @Override // com.efiAnalytics.ui.aO
    public void i() {
        f();
        this.f10439j.close();
        this.f10433d.close();
        this.f10434e.close();
        this.f10435f.close();
        this.f10436g.close();
        G.aR.a().a(this);
        l();
    }
}
