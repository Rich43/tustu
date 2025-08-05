package bt;

import G.C0072be;
import G.C0076bi;
import G.C0126i;
import ai.C0512b;
import ai.C0514d;
import ai.InterfaceC0515e;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.ui.C1582bt;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.eM;
import com.efiAnalytics.ui.eR;
import com.efiAnalytics.ui.eS;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.apache.commons.net.nntp.NNTPReply;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bt/U.class */
public class U extends JPanel implements G.aN, InterfaceC0515e, InterfaceC1565bc, eS {

    /* renamed from: q, reason: collision with root package name */
    private G.R f8709q;

    /* renamed from: a, reason: collision with root package name */
    JSplitPane f8710a;

    /* renamed from: b, reason: collision with root package name */
    eM f8711b;

    /* renamed from: c, reason: collision with root package name */
    C0072be f8712c;

    /* renamed from: d, reason: collision with root package name */
    C1582bt f8713d;

    /* renamed from: e, reason: collision with root package name */
    G.aM f8714e;

    /* renamed from: f, reason: collision with root package name */
    G.aM f8715f;

    /* renamed from: g, reason: collision with root package name */
    G.aM f8716g;

    /* renamed from: h, reason: collision with root package name */
    bM f8717h;

    /* renamed from: i, reason: collision with root package name */
    String f8718i;

    /* renamed from: j, reason: collision with root package name */
    InterfaceC1662et f8719j;

    /* renamed from: k, reason: collision with root package name */
    String f8720k;

    /* renamed from: l, reason: collision with root package name */
    String f8721l;

    /* renamed from: r, reason: collision with root package name */
    private int f8722r;

    /* renamed from: s, reason: collision with root package name */
    private int f8723s;

    /* renamed from: t, reason: collision with root package name */
    private int f8724t;

    /* renamed from: m, reason: collision with root package name */
    n.n f8725m;

    /* renamed from: n, reason: collision with root package name */
    C1425x f8726n;

    /* renamed from: o, reason: collision with root package name */
    C1425x f8727o;

    /* renamed from: p, reason: collision with root package name */
    static String f8728p = "_histogram";

    /* renamed from: u, reason: collision with root package name */
    private bN f8729u;

    public U(G.R r2, C0076bi c0076bi, boolean z2) {
        this(r2, c0076bi, z2, null);
    }

    public U(G.R r2, C0076bi c0076bi, boolean z2, InterfaceC1662et interfaceC1662et) {
        this.f8710a = new JSplitPane();
        this.f8711b = null;
        this.f8712c = null;
        this.f8713d = null;
        this.f8714e = null;
        this.f8715f = null;
        this.f8716g = null;
        this.f8717h = null;
        this.f8718i = "";
        this.f8719j = null;
        this.f8720k = null;
        this.f8721l = null;
        this.f8722r = eJ.a(300);
        this.f8723s = eJ.a(800);
        this.f8724t = eJ.a(NNTPReply.POSTING_NOT_ALLOWED);
        this.f8725m = null;
        this.f8726n = null;
        this.f8727o = null;
        this.f8729u = null;
        this.f8719j = interfaceC1662et == null ? new X(this) : interfaceC1662et;
        this.f8713d = new C1582bt(C1806i.a().a("oijfdsaoij98oi43"), new V(this));
        this.f8709q = r2;
        try {
            this.f8712c = (C0072be) r2.e().c(c0076bi.a());
            this.f8711b = this.f8713d.h();
        } catch (Exception e2) {
            bH.C.b("Unable to get 3D table layout by name: " + c0076bi.a());
        }
        a(r2, c0076bi);
        this.f8718i = c0076bi.aJ();
        setLayout(new BorderLayout());
        this.f8710a.setOrientation(1);
        this.f8713d.b(c0076bi.c());
        this.f8713d.a(c0076bi.b());
        if (C1798a.a().a(C1798a.f13418bV, C1798a.f13419bW) && (this.f8719j.a("colorTheme") == null || this.f8719j.a("colorTheme").isEmpty())) {
            this.f8719j.a("colorTheme", C1582bt.f11031l);
        }
        this.f8713d.a(this.f8719j);
        this.f8713d.d();
        if (z2) {
            this.f8726n = new C1425x(r2);
            this.f8726n.setName(this.f8718i + "_Cluster");
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f8726n.X());
            if (C1806i.a().a("64865e43s5hjhcurd")) {
                this.f8725m = new n.n();
                this.f8725m.setTabPlacement(3);
                this.f8710a.setTopComponent(this.f8725m);
                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BorderLayout());
                jPanel.add(BorderLayout.CENTER, this.f8726n);
                this.f8725m.addTab(C1818g.b("Standard"), jPanel);
                this.f8726n.setMinimumSize(new Dimension(1, 150));
                this.f8727o = new C1425x(r2);
                this.f8727o.setName(this.f8718i + "_HistogramCluster");
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f8727o.X());
                JPanel jPanel2 = new JPanel();
                jPanel2.setLayout(new BorderLayout());
                jPanel2.add(BorderLayout.CENTER, this.f8727o);
                this.f8725m.addTab(C1818g.b("Histogram"), jPanel2);
                this.f8727o.setMinimumSize(new Dimension(1, 150));
            } else {
                this.f8710a.setTopComponent(this.f8726n);
            }
            try {
                this.f8726n.a(new C1388aa().a(r2, aE.a.A(), c0076bi.aJ(), 2));
                this.f8726n.b(new C1388aa().a(r2, c0076bi.aJ(), 2));
                this.f8726n.n(aE.a.A().m());
                Component[] components = this.f8726n.getComponents();
                for (int i2 = 0; i2 < components.length; i2++) {
                    if (components[i2] instanceof Gauge) {
                        Gauge gauge = (Gauge) components[i2];
                        if (gauge.getOutputChannel().equals("veTuneValue")) {
                            this.f8711b.a(new Z(this, gauge));
                            gauge.setCurrentOutputChannelValue("veTuneValue", this.f8711b.o());
                        }
                    }
                }
                if (this.f8727o != null) {
                    this.f8727o.a(new C1388aa().a(r2, aE.a.A(), c0076bi.aJ() + f8728p, 4));
                    this.f8727o.b(new C1388aa().a(r2, c0076bi.aJ() + f8728p, 4));
                    this.f8727o.n(aE.a.A().m());
                    Component[] components2 = this.f8727o.getComponents();
                    for (int i3 = 0; i3 < components2.length; i3++) {
                        if (components2[i3] instanceof Gauge) {
                            Gauge gauge2 = (Gauge) components2[i3];
                            if (gauge2.getOutputChannel().equals("veTuneValue")) {
                                this.f8711b.a(new Z(this, gauge2));
                                gauge2.setCurrentOutputChannelValue("veTuneValue", this.f8711b.o());
                            }
                        }
                    }
                    this.f8725m.g(this.f8719j.b("selectedDash", C1818g.b("Standard")));
                }
            } catch (Exception e3) {
                com.efiAnalytics.ui.bV.d("Error setting gauges, check log for detail.\nMessage:\n" + e3.getMessage(), this);
                e3.printStackTrace();
            }
            this.f8710a.setBottomComponent(this.f8713d);
            this.f8710a.setDividerSize(eJ.a(5));
            this.f8710a.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new W(this));
            this.f8710a.setDividerLocation(Integer.parseInt(this.f8719j.b("tableTuningSplitPanePosition", "" + this.f8722r)));
            add(BorderLayout.CENTER, this.f8710a);
        } else {
            add(BorderLayout.CENTER, this.f8713d);
        }
        this.f8717h = new bM(this, this.f8713d);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f8717h);
        if (!C1806i.a().a("oijfdsaoij98oi43")) {
        }
    }

    protected C1582bt b() {
        return this.f8713d;
    }

    public void c() {
        if (this.f8729u == null) {
            this.f8729u = new Y(this);
        } else {
            this.f8729u.b();
        }
        this.f8729u.a();
    }

    public void d() {
        if (this.f8729u != null) {
            this.f8729u.b();
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f8717h);
        d();
        G.aR.a().a(this);
        if (this.f8726n != null && this.f8726n.getComponentCount() > 0) {
            this.f8726n.f();
            new C1388aa().a(this.f8726n, aE.a.A(), this.f8718i);
            this.f8726n.c();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f8726n.X());
        }
        if (this.f8727o != null && this.f8727o.getComponentCount() > 0) {
            this.f8726n.f();
            new C1388aa().a(this.f8727o, aE.a.A(), this.f8718i + f8728p);
            this.f8727o.c();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f8727o.X());
        }
        if (this.f8725m != null) {
            this.f8719j.a("selectedDash", this.f8725m.getTitleAt(this.f8725m.getSelectedIndex()));
        }
    }

    public void a(G.R r2, C0076bi c0076bi) {
        try {
            G.aR aRVarA = G.aR.a();
            aRVarA.a(this);
            d();
            eR eRVar = new eR();
            C0072be c0072be = (C0072be) r2.e().c(c0076bi.a());
            if (c0072be.p() != null) {
                try {
                    eRVar.b(c0072be.p().a());
                } catch (V.g e2) {
                    Logger.getLogger(U.class.getName()).log(Level.WARNING, "Unable to resolve X Axis Label", (Throwable) e2);
                    eRVar.b(c0072be.d());
                }
            } else {
                eRVar.b(c0072be.d());
            }
            if (c0072be.q() != null) {
                try {
                    eRVar.a(c0072be.q().a());
                } catch (V.g e3) {
                    Logger.getLogger(U.class.getName()).log(Level.WARNING, "Unable to resolve X Axis Label", (Throwable) e3);
                    eRVar.a(c0072be.d());
                }
            } else {
                eRVar.a(c0072be.f());
            }
            this.f8714e = r2.c(c0072be.c());
            this.f8715f = r2.c(c0072be.b());
            this.f8716g = r2.c(c0072be.a());
            C0126i.a(this.f8709q.c(), this.f8716g, this);
            C0126i.a(this.f8709q.c(), this.f8715f, this);
            C0126i.a(this.f8709q.c(), this.f8714e, this);
            eRVar.c(this.f8714e.o());
            eRVar.d(this.f8714e.q());
            eRVar.c(this.f8714e.r());
            eRVar.e(this.f8714e.A());
            eRVar.a(this.f8715f.b(), this.f8716g.b());
            try {
                eRVar.a(this.f8714e.i(r2.p()));
                double[] dArrA = a(this.f8716g.i(r2.p()));
                for (int i2 = 0; i2 < dArrA.length; i2++) {
                    eRVar.a(i2, dArrA[i2]);
                }
                double[] dArrA2 = a(this.f8715f.i(r2.p()));
                for (int i3 = 0; i3 < dArrA2.length; i3++) {
                    eRVar.b(i3, dArrA2[i3]);
                }
                if (C1806i.a().a("oijfdsaoij98oi43")) {
                    eRVar.c();
                }
            } catch (V.g e4) {
                bH.C.a("Error mapping 3D view to Parameter. ", e4, this);
            }
            try {
                aRVarA.a(r2.c(), this.f8716g.aJ(), this);
                aRVarA.a(r2.c(), this.f8715f.aJ(), this);
                aRVarA.a(r2.c(), this.f8714e.aJ(), this);
            } catch (V.a e5) {
                com.efiAnalytics.ui.bV.d("Error subscribing for parameter changes.", this);
                e5.printStackTrace();
            }
            c();
            this.f8711b.a(eRVar);
            this.f8711b.g(this.f8715f.u());
            this.f8711b.h(this.f8716g.u());
            this.f8711b.i(this.f8714e.u());
            eRVar.a(this);
        } catch (V.a e6) {
            Logger.getLogger(U.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
        }
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (str2.equals(this.f8714e.aJ())) {
            e();
            this.f8711b.z();
            this.f8711b.repaint();
            return;
        }
        if (str2.equals(this.f8716g.aJ())) {
            f();
            this.f8711b.z();
            this.f8711b.repaint();
            return;
        }
        if (str2.equals(this.f8715f.aJ())) {
            g();
            this.f8711b.z();
            this.f8711b.repaint();
            return;
        }
        this.f8711b.a().c(this.f8714e.o());
        this.f8711b.a().d(this.f8714e.q());
        this.f8711b.a().c(this.f8714e.r());
        this.f8711b.a().e(this.f8714e.A());
        this.f8711b.g(this.f8715f.u());
        this.f8711b.h(this.f8716g.u());
        this.f8711b.i(this.f8714e.u());
        e();
        f();
        g();
        this.f8711b.z();
        this.f8711b.repaint();
    }

    public void a(double d2) {
        this.f8711b.b(d2);
        if (this.f8713d.e()) {
            this.f8713d.f();
        }
        this.f8711b.repaint();
    }

    public void b(double d2) {
        if (this.f8712c == null) {
            return;
        }
        this.f8711b.a(d2);
        if (this.f8713d.e()) {
            this.f8713d.f();
        }
        this.f8711b.repaint();
    }

    private double[] a(double[][] dArr) {
        double[] dArr2 = new double[dArr.length];
        for (int i2 = 0; i2 < dArr.length; i2++) {
            dArr2[i2] = dArr[i2][0];
        }
        return dArr2;
    }

    @Override // com.efiAnalytics.ui.eS
    public void a(int i2, int i3, double d2) {
        try {
            this.f8714e.a(this.f8709q.p(), d2, i2, i3);
        } catch (V.g e2) {
            e2.printStackTrace();
        } catch (V.j e3) {
            e3.printStackTrace();
        }
    }

    @Override // com.efiAnalytics.ui.eS
    public void a(int i2, double d2) {
        this.f8711b.a().a(i2, d2);
    }

    @Override // com.efiAnalytics.ui.eS
    public void b(int i2, double d2) {
        this.f8711b.a().b(i2, d2);
    }

    private void e() {
        try {
            double[][] dArrI = this.f8714e.i(this.f8709q.p());
            eR eRVarA = this.f8711b.a();
            for (int i2 = 0; i2 < dArrI.length; i2++) {
                for (int i3 = 0; i3 < dArrI[0].length; i3++) {
                    if (dArrI[i2][i3] != eRVarA.d(i2, i3)) {
                        eRVarA.a(i2, i3, dArrI[i2][i3]);
                    }
                }
            }
        } catch (V.g e2) {
            e2.printStackTrace();
        }
    }

    private void f() {
        try {
            eR eRVarA = this.f8711b.a();
            double[] dArrA = a(this.f8716g.i(this.f8709q.p()));
            for (int i2 = 0; i2 < dArrA.length; i2++) {
                eRVarA.a(i2, dArrA[i2]);
            }
        } catch (V.g e2) {
            e2.printStackTrace();
            bH.C.a("X Axis failed on update, it may now be out of sync.", e2, null);
        }
    }

    private void g() {
        try {
            eR eRVarA = this.f8711b.a();
            double[] dArrA = a(this.f8715f.i(this.f8709q.p()));
            for (int i2 = 0; i2 < dArrA.length; i2++) {
                eRVarA.b(i2, dArrA[i2]);
            }
        } catch (V.g e2) {
            e2.printStackTrace();
            bH.C.a("X Axis failed on update, it may now be out of sync.", e2, null);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return this.f8726n != null ? new Dimension(eJ.a(this.f8723s), eJ.a(this.f8724t)) : new Dimension(eJ.a(this.f8723s - this.f8722r), eJ.a(this.f8724t - 125));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override // java.awt.Container, java.awt.Component
    public void validate() {
        super.validate();
        this.f8713d.h().z();
    }

    @Override // ai.InterfaceC0515e
    public C0512b a() {
        return new C0512b(C1818g.b("3D Table Usage"), C0514d.a("/help/3D_Table_Editor.htm"));
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        C1685fp.a((Container) this, z2);
        super.setEnabled(z2);
    }

    public void a(bN bNVar) {
        if (C1806i.a().a(" a09kmfds098432lkg89vlk")) {
            d();
            this.f8729u = bNVar;
        }
    }

    public void a(double d2, double d3) {
        b(d3);
        a(d2);
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }
}
