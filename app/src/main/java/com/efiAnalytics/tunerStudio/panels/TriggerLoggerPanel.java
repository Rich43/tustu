package com.efiAnalytics.tunerStudio.panels;

import G.C0096cb;
import G.C0097cc;
import G.C0098cd;
import G.C0101cg;
import G.aG;
import G.bS;
import W.C0184j;
import W.C0188n;
import aj.C0520a;
import aj.C0522c;
import aj.InterfaceC0521b;
import ao.aO;
import bq.C1219a;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.ui.C1681fl;
import com.efiAnalytics.ui.C1683fn;
import com.efiAnalytics.ui.C1699q;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.aS;
import com.efiAnalytics.ui.aU;
import com.efiAnalytics.ui.aZ;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cZ;
import com.efiAnalytics.ui.eJ;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/TriggerLoggerPanel.class */
public class TriggerLoggerPanel extends JPanel implements G.S, aG, InterfaceC0521b, n.g {

    /* renamed from: a, reason: collision with root package name */
    C1466o f9976a;

    /* renamed from: ak, reason: collision with root package name */
    private boolean f10023ak;

    /* renamed from: T, reason: collision with root package name */
    G.R f10026T;

    /* renamed from: Y, reason: collision with root package name */
    JScrollPane f10032Y;

    /* renamed from: Z, reason: collision with root package name */
    boolean f10033Z;

    /* renamed from: G, reason: collision with root package name */
    static String f10011G = C1818g.b("Raw Log Page");

    /* renamed from: H, reason: collision with root package name */
    static String f10012H = C1818g.b("Data View");

    /* renamed from: I, reason: collision with root package name */
    static String f10013I = C1818g.b("Gauges");

    /* renamed from: J, reason: collision with root package name */
    static String f10014J = C1818g.b("Set Y-Axis Min / Max");

    /* renamed from: K, reason: collision with root package name */
    static String f10015K = "IgnitionLoggerCluster";

    /* renamed from: L, reason: collision with root package name */
    static String f10016L = "splitPanePosition";

    /* renamed from: M, reason: collision with root package name */
    static String f10017M = "scrollBarPosition";

    /* renamed from: N, reason: collision with root package name */
    static String f10018N = "selectedGaugeTab";

    /* renamed from: O, reason: collision with root package name */
    static String f10019O = "zoom";

    /* renamed from: P, reason: collision with root package name */
    static String f10020P = "pollingDelay";

    /* renamed from: Q, reason: collision with root package name */
    public static boolean f10021Q = false;

    /* renamed from: S, reason: collision with root package name */
    public static int f10025S = 250;

    /* renamed from: am, reason: collision with root package name */
    private static String f10027am = "Record#";

    /* renamed from: U, reason: collision with root package name */
    static float f10028U = 0.03125f;

    /* renamed from: W, reason: collision with root package name */
    static float f10030W = 1.0f;

    /* renamed from: b, reason: collision with root package name */
    JTextArea f9977b = new JTextArea();

    /* renamed from: c, reason: collision with root package name */
    al f9978c = new al(this);

    /* renamed from: d, reason: collision with root package name */
    JTable f9979d = new JTable(this.f9978c);

    /* renamed from: ah, reason: collision with root package name */
    private ai f9980ah = new ai(this);

    /* renamed from: e, reason: collision with root package name */
    C1699q f9981e = new C1699q();

    /* renamed from: f, reason: collision with root package name */
    cZ f9982f = new cZ();

    /* renamed from: g, reason: collision with root package name */
    boolean f9983g = true;

    /* renamed from: h, reason: collision with root package name */
    JPanel f9984h = new C1681fl();

    /* renamed from: i, reason: collision with root package name */
    JPanel f9985i = new JPanel();

    /* renamed from: j, reason: collision with root package name */
    JComboBox f9986j = new JComboBox();

    /* renamed from: k, reason: collision with root package name */
    List f9987k = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    List f9988l = new ArrayList();

    /* renamed from: m, reason: collision with root package name */
    int f9989m = 3;

    /* renamed from: n, reason: collision with root package name */
    JComboBox f9990n = new JComboBox();

    /* renamed from: o, reason: collision with root package name */
    JCheckBox f9991o = new JCheckBox();

    /* renamed from: p, reason: collision with root package name */
    JSplitPane f9992p = new JSplitPane();

    /* renamed from: q, reason: collision with root package name */
    aU f9993q = new aU();

    /* renamed from: r, reason: collision with root package name */
    JPanel f9994r = new JPanel();

    /* renamed from: s, reason: collision with root package name */
    JPanel f9995s = new JPanel();

    /* renamed from: t, reason: collision with root package name */
    C1425x f9996t = null;

    /* renamed from: u, reason: collision with root package name */
    HashMap f9997u = new HashMap();

    /* renamed from: v, reason: collision with root package name */
    aZ.d f9998v = null;

    /* renamed from: w, reason: collision with root package name */
    boolean f9999w = false;

    /* renamed from: x, reason: collision with root package name */
    ao f10000x = new ao(this);

    /* renamed from: y, reason: collision with root package name */
    aS f10001y = null;

    /* renamed from: ai, reason: collision with root package name */
    private C0188n f10002ai = null;

    /* renamed from: z, reason: collision with root package name */
    double f10003z = 1.0d;

    /* renamed from: A, reason: collision with root package name */
    int f10004A = 0;

    /* renamed from: B, reason: collision with root package name */
    int f10005B = 0;

    /* renamed from: aj, reason: collision with root package name */
    private InterfaceC1662et f10006aj = null;

    /* renamed from: C, reason: collision with root package name */
    com.efiAnalytics.tuningwidgets.panels.I f10007C = new com.efiAnalytics.tuningwidgets.panels.I();

    /* renamed from: D, reason: collision with root package name */
    n.n f10008D = new n.n();

    /* renamed from: E, reason: collision with root package name */
    boolean f10009E = true;

    /* renamed from: F, reason: collision with root package name */
    T f10010F = new T();

    /* renamed from: R, reason: collision with root package name */
    aE.a f10022R = null;

    /* renamed from: al, reason: collision with root package name */
    private boolean f10024al = f10021Q;

    /* renamed from: V, reason: collision with root package name */
    float f10029V = f10028U;

    /* renamed from: X, reason: collision with root package name */
    float f10031X = f10030W;

    /* renamed from: aa, reason: collision with root package name */
    ax f10034aa = null;

    /* renamed from: ab, reason: collision with root package name */
    C0520a f10035ab = new C0520a();

    /* renamed from: ac, reason: collision with root package name */
    aj f10036ac = null;

    /* renamed from: ad, reason: collision with root package name */
    boolean f10037ad = false;

    /* renamed from: ae, reason: collision with root package name */
    int f10038ae = 1;

    /* renamed from: af, reason: collision with root package name */
    C0188n f10039af = new C0188n();

    /* renamed from: ag, reason: collision with root package name */
    int f10040ag = 0;

    public TriggerLoggerPanel(boolean z2) {
        this.f9976a = null;
        this.f10023ak = false;
        this.f10026T = null;
        this.f10033Z = true;
        this.f10023ak = z2;
        this.f10033Z = z2;
        this.f10026T = G.T.a().c();
        this.f9976a = new C1466o(this);
        this.f9993q.a(Color.yellow);
        this.f9993q.d(3.0d);
        this.f9993q.c(-1.0d);
        this.f9993q.f(4);
        this.f9993q.a(new an(this));
        this.f9993q.addMouseWheelListener(new ah(this));
        this.f9981e.addMouseWheelListener(new ah(this));
        this.f9982f.addMouseWheelListener(new ah(this));
        am amVar = new am(this);
        this.f9982f.addMouseListener(amVar);
        this.f9982f.addMouseMotionListener(amVar);
        this.f9982f.a(C1818g.d());
        this.f9982f.c(!this.f9983g);
        this.f10007C.a(new V(this));
        setLayout(new BorderLayout());
        this.f9977b.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f9977b.setColumns(35);
        this.f9977b.setTabSize(5);
        this.f9977b.setEditable(false);
        this.f9977b.setFont(UIManager.getFont("TextField.font"));
        this.f10032Y = new JScrollPane(this.f9979d);
        JScrollPane jScrollPane = new JScrollPane(this.f9977b);
        this.f10008D.addTab(f10013I, this.f9995s);
        if (this.f10033Z) {
            this.f10008D.addTab(f10012H, this.f10032Y);
            this.f9979d.setRowHeight(getFont().getSize() + eJ.a(4));
        }
        this.f10008D.addTab(f10011G, jScrollPane);
        this.f10008D.setTabPlacement(3);
        add("North", this.f9976a);
        add(BorderLayout.CENTER, this.f9992p);
        this.f9994r.setLayout(new BorderLayout());
        this.f9995s.setLayout(new BorderLayout());
        this.f9992p.setRightComponent(this.f9994r);
        add("South", this.f10000x);
        if (z2) {
            this.f9992p.setLeftComponent(this.f10008D);
        } else {
            this.f9992p.setLeftComponent(jScrollPane);
        }
        this.f9992p.setOneTouchExpandable(true);
        this.f9992p.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new Z(this));
        aa aaVar = new aa(this);
        this.f9986j.addItem(" ");
        this.f9986j.addItemListener(aaVar);
        this.f9986j.addItemListener(new ab(this));
        if (!this.f9983g) {
            for (int i2 = 0; i2 < this.f9989m; i2++) {
                JComboBox jComboBox = new JComboBox();
                jComboBox.addItem(" ");
                jComboBox.addItemListener(aaVar);
                this.f9987k.add(jComboBox);
            }
        }
        this.f9985i.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0, eJ.a(10), eJ.a(5)));
        this.f9985i.add(BorderLayout.CENTER, jPanel);
        this.f9991o.setText("Show Lines");
        this.f9991o.addActionListener(new ac(this));
        this.f9990n.addItem(1);
        this.f9990n.addItem(2);
        this.f9990n.addItem(3);
        this.f9990n.addItem(4);
        this.f9990n.addItem(5);
        this.f9990n.addItem(10);
        this.f9990n.addItem(15);
        this.f9990n.addItem(20);
        this.f9990n.addItem(30);
        this.f9990n.addItem(40);
        this.f9990n.addActionListener(new ad(this));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("West", new JLabel(C1818g.b("X Axis") + ": "));
        jPanel2.add(BorderLayout.CENTER, this.f9986j);
        jPanel.add(jPanel2);
        for (int i3 = 0; i3 < this.f9987k.size(); i3++) {
            JComboBox jComboBox2 = (JComboBox) this.f9987k.get(i3);
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new BorderLayout());
            jPanel3.add("West", new JLabel(C1818g.b("Y Axis") + " " + (i3 + 1) + ": "));
            jPanel3.add(BorderLayout.CENTER, jComboBox2);
            jPanel.add(jPanel3);
        }
        if (this.f9983g) {
            jPanel.add(new JPanel());
        }
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add("West", new JLabel(C1818g.b("Chart Options") + ": "));
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new GridLayout(1, 0));
        jPanel5.add(this.f9991o);
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BorderLayout());
        jPanel6.add(BorderLayout.CENTER, new JLabel(C1818g.b("Page Overlays") + ": ", 4));
        jPanel6.add("East", this.f9990n);
        jPanel5.add(jPanel6);
        jPanel4.add(BorderLayout.CENTER, jPanel5);
        this.f9985i.add("East", jPanel4);
        this.f10010F.a(new ae(this));
    }

    public void a(File file) {
        this.f9976a.a(file.getAbsolutePath());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() {
        C0096cb c0096cbH;
        String str;
        C0101cg c0101cgH;
        if (this.f10002ai == null || (c0096cbH = h(this.f10002ai)) == null || (str = (String) this.f9986j.getSelectedItem()) == null || str.isEmpty()) {
            return;
        }
        C0101cg c0101cgH2 = c0096cbH.h(str);
        if (c0101cgH2 != null && c0101cgH2.d().equals(str)) {
            this.f9982f.e(c0101cgH2.c());
            this.f9982f.g(c0101cgH2.b());
        }
        Iterator it = this.f9987k.iterator();
        while (it.hasNext()) {
            String str2 = (String) ((JComboBox) it.next()).getSelectedItem();
            if (str2 != null && !str2.trim().isEmpty() && (c0101cgH = c0096cbH.h(str2)) != null && c0101cgH.d().equals(str)) {
                this.f9982f.e(c0101cgH.c());
                this.f9982f.g(c0101cgH.b());
            }
        }
    }

    protected aj.d a() {
        C0096cb c0096cbJ = this.f9976a.j();
        return c0096cbJ != null ? b(c0096cbJ.n()) : b("Standard");
    }

    protected aj.d b(String str) {
        aj.d dVar = (aj.d) this.f9997u.get(str);
        if (dVar == null) {
            if (str.equals("UDP_Stream")) {
                C0096cb c0096cbJ = this.f9976a.j();
                aj.k kVar = new aj.k(p());
                bH.C.d("Got high Speed logger for IP: " + kVar.m());
                kVar.d(c0096cbJ.o());
                kVar.a(this.f10026T);
                kVar.a(this);
                dVar = kVar;
                this.f9997u.put("UDP_Stream", dVar);
            } else {
                aj.d dVar2 = new aj.d();
                dVar2.a(this.f10026T);
                dVar2.a(this);
                dVar = dVar2;
                this.f9997u.put("Standard", dVar);
            }
        } else if (str.equals("UDP_Stream")) {
            ((aj.k) dVar).d(this.f9976a.j().o());
            ((aj.k) dVar).h(p());
            dVar.a(this.f10026T);
        }
        return dVar;
    }

    private String p() {
        String strD = C1798a.a().d(C1798a.f13372bb);
        if (strD == null || strD.isEmpty()) {
            return B.n.a(this.f10026T);
        }
        if (!strD.equals("localhost") && !strD.equals("127.0.0.1")) {
            return strD;
        }
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e2) {
            Logger.getLogger(TriggerLoggerPanel.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        if (this.f10037ad || this.f9976a.j() == null) {
            return;
        }
        String str = "Logger." + this.f9976a.j().g() + ".";
        if (this.f9986j.getSelectedItem() != null) {
            b(str + "X", this.f9986j.getSelectedItem().toString());
        }
        if (!this.f9983g) {
            for (int i2 = 0; i2 < this.f9987k.size(); i2++) {
                JComboBox jComboBox = (JComboBox) this.f9987k.get(i2);
                if (jComboBox.getSelectedItem() != null) {
                    b(str + Constants._TAG_Y + (i2 + 1), jComboBox.getSelectedItem().toString());
                }
            }
            return;
        }
        for (int i3 = 0; i3 < this.f9988l.size(); i3++) {
            C1683fn c1683fn = (C1683fn) this.f9988l.get(i3);
            if (c1683fn.d()) {
                b(str + "Y_" + c1683fn.b(), Boolean.toString(true));
            } else {
                b(str + "Y_" + c1683fn.b(), Boolean.toString(false));
            }
        }
    }

    private void r() {
        if (this.f9976a.j() == null) {
            return;
        }
        this.f10037ad = true;
        String str = "Logger." + this.f9976a.j().g() + ".";
        String strQ = f10027am;
        if (this.f9976a.j().q() != null && !this.f9976a.j().q().isEmpty()) {
            strQ = this.f9976a.j().q();
        }
        this.f9986j.setSelectedItem(a(str + "X", strQ));
        if (this.f9983g) {
            for (int i2 = 0; i2 < this.f9988l.size(); i2++) {
                C1683fn c1683fn = (C1683fn) this.f9988l.get(i2);
                c1683fn.a(Boolean.parseBoolean(a(str + "Y_" + c1683fn.b(), "true")));
            }
        } else {
            for (int i3 = 0; i3 < this.f9987k.size(); i3++) {
                ((JComboBox) this.f9987k.get(i3)).setSelectedItem(a(str + Constants._TAG_Y + (i3 + 1), " "));
            }
        }
        this.f10037ad = false;
    }

    private C1425x e(G.R r2) {
        this.f10022R = aE.a.A();
        com.efiAnalytics.apps.ts.dashboard.Z zA = new C1388aa().a(r2, aE.a.A(), f10015K, 5);
        C1425x c1425x = new C1425x(r2);
        c1425x.a(zA);
        c1425x.setName(f10015K);
        c1425x.b(new C1388aa().a(r2, f10015K, 5));
        c1425x.n(aE.a.A().m());
        c1425x.setMinimumSize(eJ.a(1, 150));
        return c1425x;
    }

    public void c() throws NumberFormatException {
        if (this.f10006aj == null) {
            return;
        }
        this.f9992p.setDividerLocation(Integer.parseInt(a(f10016L, "" + this.f10008D.getPreferredSize().width)));
        this.f10008D.g(a(f10018N, f10013I));
        this.f9976a.a(Integer.parseInt(a(f10020P, "1000")));
        this.f9991o.setSelected(Boolean.getBoolean(a("Show Lines", "false")));
    }

    public void d() {
        if (this.f10006aj == null) {
            return;
        }
        b(f10016L, "" + this.f9992p.getDividerLocation());
        b(f10019O, this.f10003z + "");
        b(f10018N, this.f10008D.getTitleAt(this.f10008D.getSelectedIndex()));
        b(f10020P, this.f9976a.o() + "");
        b("Show Lines", Boolean.toString(this.f9991o.isSelected()));
        b(f10017M, Double.toString(this.f10000x.f10078a.getMaximum() - this.f10000x.f10078a.getMinimum() <= 0 ? 0.0d : (this.f10000x.f10078a.getValue() - this.f10000x.f10078a.getMinimum()) / (this.f10000x.f10078a.getMaximum() - this.f10000x.f10078a.getMinimum())));
    }

    private String a(String str, String str2) {
        String strA = null;
        if (this.f10006aj != null) {
            strA = this.f10006aj.a(str);
        }
        return (strA == null || strA.equals("")) ? str2 : strA;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str, String str2) {
        if (this.f10006aj != null) {
            this.f10006aj.a(str, str2);
        }
    }

    public void e() throws V.a {
        C0096cb c0096cbJ = this.f9976a.j();
        try {
            c0096cbJ.c();
            a().b(c0096cbJ.f() == -1 ? 10 : c0096cbJ.f());
            a().a(c0096cbJ);
            a().f(c0096cbJ.d());
            a().c(c0096cbJ.i());
            try {
                a().a(c0096cbJ.h());
                a().b(c0096cbJ.j());
                a().c(c0096cbJ.k());
                a().g(c0096cbJ.e());
                this.f9999w = false;
                this.f10009E = true;
                a().b();
                this.f9976a.b(true);
            } catch (V.g e2) {
                e2.printStackTrace();
                throw new V.a(e2.getMessage());
            }
        } catch (V.g e3) {
            throw new V.a("Failed to reset logger fields:\n" + e3.getLocalizedMessage());
        }
    }

    public void f() {
        a().c();
        this.f9976a.b(false);
        if (this.f9980ah.size() > 0) {
            this.f10000x.a(true);
        }
    }

    @Override // G.S
    public void a(G.R r2) throws IllegalArgumentException {
        d(r2);
        this.f10009E = true;
    }

    public void d(G.R r2) throws IllegalArgumentException {
        a().a();
        a().a(r2);
        ArrayList arrayListA = new C0522c().a(r2);
        if (this.f10023ak) {
            arrayListA.addAll(r2.x());
        }
        this.f9976a.a(arrayListA);
        if (this.f9976a.j() != null) {
            a(this.f9976a.j());
            try {
                a(e(r2));
                c();
            } catch (V.a e2) {
                bH.C.a(C1818g.b("Problem creating Logger Gauge Cluster"), e2, this);
            }
        }
    }

    private void a(C1425x c1425x) {
        if (this.f9996t != null) {
            this.f9996t.c();
        }
        this.f9995s.removeAll();
        this.f9995s.add(BorderLayout.CENTER, c1425x);
        this.f9996t = c1425x;
    }

    protected void a(C0096cb c0096cb) throws IllegalArgumentException {
        this.f9980ah.clear();
        try {
            c0096cb.c();
        } catch (V.g e2) {
            e2.printStackTrace();
        }
        this.f10002ai = null;
        this.f9977b.setText("");
        this.f9978c.getDataVector().clear();
        this.f9978c.fireTableDataChanged();
        this.f9976a.n();
        a().a();
        if (c0096cb.d().equals(C0096cb.f1065a)) {
            a((aS) this.f9993q, false);
            this.f9993q.b(C1818g.b(c0096cb.g()));
            this.f9993q.c(c0096cb.a());
            this.f9993q.a();
            this.f9993q.repaint();
            C0097cc c0097ccA = c0096cb.b().a("ToothTime");
            this.f9993q.d(c0097ccA != null ? c0097ccA.d() : "");
            return;
        }
        if (!c0096cb.d().equals(C0096cb.f1068d)) {
            a((aS) this.f9981e, false);
            this.f9981e.b(C1818g.b(c0096cb.g()));
            this.f9981e.c(c0096cb.a());
            this.f9981e.a();
            this.f9981e.d(0.0d);
            C0097cc c0097ccA2 = c0096cb.b().a("ToothTime");
            if (c0097ccA2 == null) {
                c0097ccA2 = c0096cb.b().a("TriggerTime");
            }
            this.f9981e.e(c0097ccA2 != null ? c0097ccA2.d() : "");
            this.f9981e.repaint();
            return;
        }
        a((aS) this.f9982f, true);
        if (this.f9983g) {
            this.f9982f.b("");
        } else if (c0096cb != null) {
            this.f9982f.b(C1818g.b(c0096cb.g()));
        } else {
            this.f9982f.b(C1818g.b("Generic Data Mode"));
        }
        this.f9982f.b(this.f9991o.isSelected());
        this.f9982f.c(c0096cb.a());
        this.f9982f.a();
        this.f9982f.repaint();
        this.f10037ad = true;
        this.f9986j.removeAllItems();
        this.f9986j.addItem(f10027am);
        for (int i2 = 0; i2 < c0096cb.b().i(); i2++) {
            C0097cc c0097ccA3 = c0096cb.b().a(i2);
            if (!c0097ccA3.i()) {
                this.f9986j.addItem(c0097ccA3.g());
            }
        }
        if (this.f9983g) {
            this.f9984h.removeAll();
            this.f9988l.clear();
            for (int i3 = 0; i3 < c0096cb.b().i(); i3++) {
                C0097cc c0097ccA4 = c0096cb.b().a(i3);
                if (!c0097ccA4.i()) {
                    C1683fn c1683fn = new C1683fn(c0097ccA4.g(), aO.a().a(i3));
                    this.f9984h.add(c1683fn);
                    this.f9988l.add(c1683fn);
                    c1683fn.a(new af(this));
                }
            }
            SwingUtilities.invokeLater(new ag(this));
            b(c0096cb);
            r();
        } else {
            for (JComboBox jComboBox : this.f9987k) {
                jComboBox.removeAllItems();
                jComboBox.addItem(" ");
                for (int i4 = 0; i4 < c0096cb.b().i(); i4++) {
                    C0097cc c0097ccA5 = c0096cb.b().a(i4);
                    if (!c0097ccA5.i()) {
                        jComboBox.addItem(c0097ccA5.g());
                    }
                }
            }
        }
        b(c0096cb);
        r();
        this.f10037ad = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(C0096cb c0096cb) {
        this.f10037ad = true;
        String string = this.f9986j.getSelectedItem().toString();
        if (this.f9983g) {
            this.f9984h.removeAll();
            this.f9988l.clear();
            int i2 = 0;
            for (int i3 = 0; i3 < c0096cb.b().i(); i3++) {
                C0097cc c0097ccA = c0096cb.b().a(i3);
                if (!c0097ccA.i() && !c0097ccA.g().equals(string)) {
                    int i4 = i2;
                    i2++;
                    C1683fn c1683fn = new C1683fn(c0097ccA.g(), aO.a().a(i4));
                    this.f9984h.add(c1683fn);
                    this.f9988l.add(c1683fn);
                    c1683fn.a(new W(this));
                }
            }
            this.f9984h.validate();
            this.f9994r.doLayout();
        } else {
            for (JComboBox jComboBox : this.f9987k) {
                jComboBox.removeAllItems();
                jComboBox.addItem(" ");
                for (int i5 = 0; i5 < c0096cb.b().i(); i5++) {
                    C0097cc c0097ccA2 = c0096cb.b().a(i5);
                    if (!c0097ccA2.i() && !c0097ccA2.g().equals(string)) {
                        jComboBox.addItem(c0097ccA2.g());
                    }
                }
            }
        }
        r();
        this.f10037ad = false;
    }

    public void a(int i2) {
        b("Standard").a(i2);
    }

    void g() {
        try {
            this.f9998v.a();
        } catch (Exception e2) {
        }
        this.f9998v = null;
    }

    private void a(aS aSVar, boolean z2) {
        if (this.f10001y != null) {
            this.f9994r.remove(this.f10001y);
            this.f10001y.c();
        }
        this.f10001y = aSVar;
        this.f9994r.add(BorderLayout.CENTER, aSVar);
        if (z2) {
            this.f9994r.add("South", this.f9985i);
            if (this.f9983g) {
                this.f9994r.add("North", this.f9984h);
            }
            if (aSVar instanceof cZ) {
                ((cZ) aSVar).c(!this.f9983g);
            }
            this.f9994r.validate();
        } else {
            this.f9994r.remove(this.f9984h);
            this.f9994r.remove(this.f9985i);
            this.f9994r.validate();
        }
        this.f9994r.doLayout();
    }

    @Override // G.S
    public void b(G.R r2) {
        d();
        if (this.f9996t == null || this.f9996t.getComponentCount() <= 0) {
            return;
        }
        this.f9996t.f();
        new C1388aa().a(this.f9996t, this.f10022R, f10015K);
        this.f9996t.c();
    }

    @Override // G.S
    public void c(G.R r2) {
    }

    private C0096cb h(C0188n c0188n) {
        Iterator it = this.f9976a.h().iterator();
        while (it.hasNext()) {
            C0096cb c0096cb = (C0096cb) it.next();
            if (a(c0188n, c0096cb.b())) {
                return c0096cb;
            }
        }
        return null;
    }

    private boolean a(C0188n c0188n, C0098cd c0098cd) {
        Iterator it = c0098cd.j().iterator();
        while (it.hasNext()) {
            if (c0188n.a((String) it.next()) == null) {
                return false;
            }
        }
        return true;
    }

    public void a(ArrayList arrayList) {
        this.f10009E = false;
        if (arrayList.size() == 0) {
            bV.d("Data set empty, can't load it.", this);
            return;
        }
        C0096cb c0096cbH = h((C0188n) arrayList.get(0));
        if (c0096cbH == null) {
            bV.d("Unable to identify log file type.", this);
            return;
        }
        this.f9976a.b(c0096cbH.g());
        this.f9980ah.clear();
        int i2 = 0;
        int iD = this.f9980ah.d();
        boolean z2 = true;
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            if (i2 == iD) {
                bV.d("Data set size exceeds current maximum of " + iD + ".\nOnly the first " + iD + " will be loaded.", this);
                break;
            }
            C0188n c0188n = (C0188n) it.next();
            if (z2) {
                a(c0188n);
                z2 = false;
            } else {
                this.f9980ah.add(c0188n);
            }
            i2++;
        }
        b(0);
        o();
    }

    public void b(int i2) {
        try {
            i(this.f9980ah.a(i2));
        } catch (V.a e2) {
            bH.C.a("Unable to display page " + i2, e2, this);
        }
    }

    @Override // aj.InterfaceC0521b
    public void a(C0188n c0188n) {
        C0096cb c0096cbJ = this.f9976a.j();
        if (this.f10009E && this.f9976a.j().l()) {
            this.f10009E = false;
            if (c0096cbJ == null || c0096cbJ.l()) {
                return;
            }
        }
        this.f9980ah.add(c0188n);
        if (this.f10036ac == null || !this.f10036ac.isAlive()) {
            this.f10036ac = new aj(this);
            this.f10036ac.start();
        }
        this.f10036ac.a();
        try {
            g(c0188n);
        } catch (IOException e2) {
            if (!this.f9999w) {
                bH.C.a("Unable to write data to log file.", e2, this);
                this.f9999w = true;
            }
        }
        if (c0096cbJ == null || c0096cbJ.l()) {
            return;
        }
        SwingUtilities.invokeLater(new X(this));
    }

    @Override // aj.InterfaceC0521b
    public void b(C0188n c0188n) {
        if (this.f10009E) {
            this.f10009E = false;
        }
        this.f9980ah.add(c0188n);
        i(this.f9980ah.e());
        try {
            g(c0188n);
        } catch (IOException e2) {
            if (this.f9999w) {
                return;
            }
            bH.C.a("Unable to write data to log file.", e2, this);
            this.f9999w = true;
        }
    }

    protected void h() {
        this.f10000x.a(this.f9980ah.size());
        this.f10000x.b(this.f9980ah.c() + 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(C0188n c0188n) {
        if (c0188n == null) {
            return;
        }
        this.f10002ai = c0188n;
        Iterator it = c0188n.iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            if (this.f10007C.a(c0184j.a())) {
                c0184j.b(true);
                c0184j.g(this.f10007C.b(c0184j.a()));
            }
        }
        if (c0188n.d() != this.f10005B) {
            this.f10005B = c0188n.d();
            if (this.f10005B > 1000) {
                this.f10031X = 1000.0f / this.f10005B;
                this.f10029V = this.f10031X / 32.0f;
            } else {
                this.f10031X = f10030W;
                this.f10029V = f10028U;
            }
            if (this.f10003z > this.f10031X) {
                this.f10003z = this.f10031X;
            } else if (this.f10003z < this.f10029V) {
                this.f10003z = this.f10029V;
            }
            this.f10000x.a();
        }
        h();
        try {
            m(c0188n);
        } catch (V.a e2) {
            bH.C.a("There was an error reading this log file.", e2, this);
        }
        d(c0188n);
        this.f10035ab.a(0, c0188n);
    }

    private C0188n j(C0188n c0188n) {
        Iterator it = c0188n.iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            C0184j c0184jA = this.f10039af.a(c0184j.a());
            if (c0184jA == null) {
                c0184jA = new C0184j(c0184j.a());
                this.f10039af.a(c0184jA);
            }
            c0184jA.b(c0184j.r());
            c0184jA.g(c0184j.s());
            for (int i2 = 0; i2 < c0184j.i(); i2++) {
                c0184jA.a(c0184j.d(i2));
                while (c0184jA.v() > this.f10038ae * c0184j.v()) {
                    c0184jA.h(0);
                }
            }
        }
        C0188n c0188n2 = this.f10039af;
        int iD = this.f10039af.d() - 1;
        StringBuilder sbAppend = new StringBuilder().append("End of read ");
        int i3 = this.f10040ag;
        this.f10040ag = i3 + 1;
        c0188n2.a(iD, sbAppend.append(i3).toString());
        return this.f10039af;
    }

    protected void c(C0188n c0188n) {
        C0184j[] c0184jArr;
        if (this.f10038ae > 1) {
            if (a().f()) {
                c0188n = j(c0188n);
            } else {
                this.f10039af.clear();
                int iD = this.f10000x.d() - 1;
                int iMax = Math.max(0, iD - this.f10038ae);
                while (iMax <= iD) {
                    int i2 = iMax;
                    iMax++;
                    c0188n = j((C0188n) this.f9980ah.get(i2));
                }
            }
        }
        if (!(this.f10001y instanceof cZ)) {
            if (this.f10001y instanceof C1699q) {
                String string = this.f9986j.getSelectedItem() != null ? this.f9986j.getSelectedItem().toString() : "";
                int iD2 = (int) (c0188n.d() * this.f10003z);
                if (c0188n.a(string) != null) {
                    this.f9981e.a();
                    this.f9981e.d(r0.f());
                    this.f9981e.c(r0.e());
                    for (int i3 = this.f10004A; i3 < iD2 + this.f10004A && i3 < c0188n.d(); i3++) {
                        this.f9981e.a(r0.c(i3));
                    }
                    this.f9981e.repaint();
                    return;
                }
                return;
            }
            return;
        }
        String string2 = this.f9986j.getSelectedItem() != null ? this.f9986j.getSelectedItem().toString() : "";
        int iD3 = (int) (c0188n.d() * this.f10003z);
        C0184j c0184jA = c0188n.a(string2);
        if (c0184jA != null) {
            double dF = c0184jA.f() - c0184jA.e();
            double dE = c0184jA.e() + ((this.f10000x.f10078a.getMaximum() - this.f10000x.f10078a.getMinimum() == 0 ? 0.0d : (this.f10000x.f10078a.getValue() - this.f10000x.f10078a.getMinimum()) / (this.f10000x.f10078a.getMaximum() - this.f10000x.f10078a.getMinimum())) * (1.0d - this.f10003z) * dF);
            this.f9982f.a(dE + (this.f10003z * dF));
            this.f9982f.b(dE);
            this.f9982f.a(c0184jA.a());
        } else {
            if (c0184jA != null) {
                return;
            }
            this.f9982f.a(this.f10004A + iD3);
            this.f9982f.b(this.f10004A);
            this.f9982f.a(f10027am);
        }
        if (this.f9983g) {
            c0184jArr = new C0184j[this.f9988l.size()];
            for (int i4 = 0; i4 < this.f9988l.size(); i4++) {
                C1683fn c1683fn = (C1683fn) this.f9988l.get(i4);
                if (c1683fn.d()) {
                    c0184jArr[i4] = c0188n.a(c1683fn.b());
                } else {
                    c0184jArr[i4] = null;
                }
            }
        } else {
            c0184jArr = new C0184j[this.f9987k.size()];
            for (int i5 = 0; i5 < this.f9987k.size(); i5++) {
                JComboBox jComboBox = (JComboBox) this.f9987k.get(i5);
                c0184jArr[i5] = c0188n.a(jComboBox.getSelectedItem() != null ? jComboBox.getSelectedItem().toString() : "");
            }
        }
        if (a(c0184jArr)) {
            this.f9982f.a();
            this.f9982f.d(0.0d);
            this.f9982f.c(0.0d);
            this.f9982f.a(0.0d);
            this.f9982f.b(0.0d);
            this.f9982f.repaint();
            return;
        }
        this.f9982f.a();
        o();
        for (int i6 = 0; i6 < c0184jArr.length; i6++) {
            C0184j c0184j = c0184jArr[i6];
            if (c0184j != null) {
                double dH = 0.8d * (this.f9982f.h(i6) - this.f9982f.g(i6));
                double dH2 = this.f9982f.h(i6) - dH;
                double dG = dH + this.f9982f.g(i6);
                double dC = this.f10010F.c(c0184j.a());
                double d2 = this.f10010F.d(c0184j.a());
                if (Double.isNaN(d2) && Double.isNaN(this.f9982f.h(i6))) {
                    this.f9982f.a(i6, c0184j.f());
                } else if (Double.isNaN(d2) && (this.f9982f.h(i6) < c0184j.f() || this.f9982f.h(i6) < dH2)) {
                    this.f9982f.a(i6, c0184j.f());
                } else if (Double.isNaN(d2) && this.f9982f.h(i6) > c0184j.f() * 2.0f) {
                    this.f9982f.a(i6, (c0184j.f() * 3.0f) / 2.0f);
                } else if (!Double.isNaN(d2)) {
                    this.f9982f.a(i6, d2);
                }
                if (Double.isNaN(dC) && Double.isNaN(this.f9982f.g(i6))) {
                    this.f9982f.b(i6, c0184j.e());
                } else if (Double.isNaN(dC) && (this.f9982f.g(i6) > c0184j.e() || this.f9982f.g(i6) > dG)) {
                    this.f9982f.b(i6, c0184j.e());
                } else if (!Double.isNaN(dC)) {
                    this.f9982f.b(i6, dC);
                }
                this.f9982f.b(i6, c0184j.a());
                for (int i7 = 0; i7 < c0188n.d(); i7++) {
                    this.f9982f.a(new Point2D.Float(c0184jA != null ? c0184jA.c(i7) : i7, c0184j.c(i7)), i6);
                }
            } else {
                this.f9982f.b(i6, "");
                this.f9982f.b(i6, Double.NaN);
                this.f9982f.a(i6, Double.NaN);
            }
        }
        this.f9982f.repaint();
    }

    private boolean a(C0184j[] c0184jArr) {
        for (C0184j c0184j : c0184jArr) {
            if (c0184j != null) {
                return false;
            }
        }
        return true;
    }

    protected void d(C0188n c0188n) {
        if (this.f9976a.j().d().equals(C0096cb.f1068d)) {
            c(c0188n);
        } else if (m()) {
            f(c0188n);
        } else {
            e(c0188n);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x02fb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void e(W.C0188n r9) {
        /*
            Method dump skipped, instructions count: 2491
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.efiAnalytics.tunerStudio.panels.TriggerLoggerPanel.e(W.n):void");
    }

    protected void f(C0188n c0188n) {
        if (this.f10001y == null) {
            return;
        }
        if (this.f10001y.equals(this.f9981e)) {
            int iD = (int) (c0188n.d() * this.f10003z);
            C0184j c0184j = (C0184j) c0188n.get(0);
            this.f9981e.a();
            if (c0184j.f() < this.f9981e.i() / 2.0d) {
                this.f9981e.d(0.0d);
            }
            for (int i2 = this.f10004A; i2 < iD + this.f10004A && i2 < c0188n.d(); i2++) {
                this.f9981e.a(c0184j.c(i2));
            }
            this.f9981e.repaint();
            return;
        }
        if (this.f10001y.equals(this.f9993q)) {
            int iD2 = (int) (c0188n.d() * this.f10003z);
            C0184j c0184jA = c0188n.a("Time");
            C0184j c0184jA2 = c0188n.a("ToothTime");
            C0184j c0184jA3 = c0188n.a("PriLevel");
            C0184j c0184jA4 = c0188n.a("SecLevel");
            C0184j c0184jA5 = c0188n.a("Sync");
            C0184j c0184jA6 = c0188n.a("Trigger");
            this.f9993q.a();
            double dC = c0184jA.c(this.f10004A) > 0.0f ? c0184jA.c(this.f10004A) : k(c0188n);
            double dC2 = c0184jA.c((iD2 + this.f10004A) - 1) > 0.0f ? c0184jA.c((iD2 + this.f10004A) - 1) : l(c0188n);
            this.f9993q.b(dC);
            this.f9993q.e(dC2);
            if (c0184jA6.f() == 0.0f && c0184jA4.e() == c0184jA4.f()) {
                this.f9993q.d(2.0d);
            } else {
                this.f9993q.d(3.0d);
            }
            boolean z2 = false;
            boolean z3 = false;
            System.currentTimeMillis();
            double dC3 = -1.0d;
            double dC4 = -1.0d;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            float fC = -1.0f;
            boolean z4 = false;
            int i8 = 0;
            for (int i9 = 2; i9 < c0188n.d(); i9++) {
                if (c0184jA2.c(i9) == 0.0d) {
                    i7++;
                    if (!z4) {
                        i8++;
                    }
                } else {
                    i7 = 0;
                    z4 = true;
                }
                if (i7 <= 2) {
                    if (!z2 && c0184jA3.f() != c0184jA3.e() && c0184jA2.c(i9) > 0.0d) {
                        if (dC3 != -1.0d && dC3 != c0184jA3.c(i9)) {
                            z2 = true;
                        }
                        dC3 = c0184jA3.c(i9);
                    }
                    if (!z3 && c0184jA4.f() != c0184jA4.e() && c0184jA2.c(i9) > 0.0d) {
                        if (dC4 != -1.0d && fC != -1.0f && fC != c0184jA4.c(i9)) {
                            z3 = true;
                        }
                        fC = c0184jA4.c(i9);
                    }
                    dC4 = c0184jA4.c(i9);
                    if (c0184jA4.c(i9) == 1.0f) {
                        i3++;
                    } else {
                        i4++;
                    }
                    if (c0184jA3.c(i9) == 1.0f) {
                        i5++;
                    } else {
                        i6++;
                    }
                }
            }
            int i10 = i3 < i4 ? 1 : 0;
            int i11 = i5 > i6 ? 1 : 0;
            double d2 = -1.0d;
            double d3 = -1.0d;
            double d4 = -1.0d;
            for (int i12 = this.f10004A; i12 < iD2 + this.f10004A && i12 < c0188n.d(); i12++) {
                if (z2 || c0184jA6.c(i12) == 0.0f) {
                    double dC5 = c0184jA3.c(i12);
                    if (d2 == -1.0d) {
                        if (z2) {
                            aZ aZVar = new aZ(this.f9993q.b(), dC5);
                            aZVar.a(true);
                            this.f9993q.a(0, aZVar);
                            this.f9993q.a(0, new aZ(c0184jA.c(i12), dC5));
                        } else {
                            this.f9993q.a(0, new aZ(c0184jA.c(i12), i11 == 1 ? 0 : 1));
                        }
                    } else if (z2) {
                        aZ aZVar2 = new aZ(c0184jA.c(i12), d2);
                        aZVar2.a(true);
                        this.f9993q.a(0, aZVar2);
                        this.f9993q.a(0, new aZ(c0184jA.c(i12), dC5));
                    } else if (i12 == (iD2 + this.f10004A) - 1 || i12 == c0188n.d() - 1) {
                        if (!z2) {
                            this.f9993q.a(0, new aZ(c0184jA.c(i12), i11 == 1 ? 0 : 1));
                        }
                    } else if (dC5 == i11) {
                        int i13 = i11 == 1 ? 0 : 1;
                        aZ aZVar3 = new aZ(c0184jA.c(i12), i13);
                        aZVar3.a(true);
                        this.f9993q.a(0, aZVar3);
                        this.f9993q.a(0, new aZ(c0184jA.c(i12), i11));
                        aZ aZVar4 = new aZ(c0184jA.c(i12), i13);
                        aZVar4.a(true);
                        this.f9993q.a(0, aZVar4);
                    }
                    d2 = dC5;
                }
                if (z3 || c0184jA6.c(i12) == 1.0f) {
                    double dC6 = c0184jA4.c(i12);
                    double d5 = dC6 + 1.5d;
                    if (d3 == -1.0d) {
                        if (z3) {
                            double d6 = d5 == 1.5d ? 1.5d : 1.0d + 1.5d;
                            this.f9993q.a(1, new aZ(this.f9993q.b(), d6));
                            aZ aZVar5 = new aZ(c0184jA.c(i12), d6);
                            aZVar5.a(true);
                            this.f9993q.a(1, aZVar5);
                            this.f9993q.a(1, new aZ(c0184jA.c(i12), d5));
                        } else {
                            double d7 = d5 == 1.5d ? 1.0d + 1.5d : 1.5d;
                            aZ aZVar6 = new aZ(this.f9993q.b(), d7);
                            aZVar6.a(true);
                            this.f9993q.a(1, aZVar6);
                            aZ aZVar7 = new aZ(c0184jA.c(i12), d7);
                            aZVar7.a(true);
                            this.f9993q.a(1, aZVar7);
                            this.f9993q.a(1, new aZ(c0184jA.c(i12), d5));
                            aZ aZVar8 = new aZ(c0184jA.c(i12), d7);
                            aZVar8.a(true);
                            this.f9993q.a(1, aZVar8);
                        }
                    } else if (z3) {
                        aZ aZVar9 = new aZ(c0184jA.c(i12), d4);
                        aZVar9.a(true);
                        this.f9993q.a(1, aZVar9);
                        this.f9993q.a(1, new aZ(c0184jA.c(i12), d5));
                    } else if (i12 == (iD2 + this.f10004A) - 1 || i12 == c0188n.d() - 1) {
                        if (!z3) {
                            aZ aZVar10 = new aZ(c0184jA.c(i12), i10 + 1.5d);
                            aZVar10.a(true);
                            this.f9993q.a(1, aZVar10);
                        }
                    } else if (c0184jA6.c(i12) == 1.0f || (i10 != dC6 && d5 != d4)) {
                        double d8 = d5 == 1.5d ? 1.0d + 1.5d : 1.5d;
                        aZ aZVar11 = new aZ(c0184jA.c(i12), d8);
                        aZVar11.a(true);
                        this.f9993q.a(1, aZVar11);
                        this.f9993q.a(1, new aZ(c0184jA.c(i12), d5));
                        aZ aZVar12 = new aZ(c0184jA.c(i12), d8);
                        aZVar12.a(true);
                        this.f9993q.a(1, aZVar12);
                    }
                    d4 = d5;
                    d3 = dC6;
                }
                if (c0184jA5.c(i12) == 0.0f && c0184jA2.c(i12) > 0.0f) {
                    aZ aZVar13 = new aZ(c0184jA.c(i12), -0.75d);
                    aZVar13.a(true);
                    this.f9993q.a(2, aZVar13);
                    aZ aZVar14 = new aZ(c0184jA.c(i12), -0.5d);
                    aZVar14.a(true);
                    this.f9993q.a(2, aZVar14);
                    aZ aZVar15 = new aZ(c0184jA.c(i12), -0.75d);
                    aZVar15.a(true);
                    this.f9993q.a(2, aZVar15);
                } else if (i12 == this.f10004A || i12 == (iD2 + this.f10004A) - 1) {
                    aZ aZVar16 = new aZ(c0184jA.c(i12), -0.75d);
                    aZVar16.a(true);
                    this.f9993q.a(2, aZVar16);
                }
            }
            if (c0184jA6.f() == 1.0f) {
                if (!z3) {
                    double d9 = i10 + 1.5d;
                    aZ aZVar17 = new aZ(this.f9993q.b(), d9);
                    aZVar17.a(true);
                    this.f9993q.a(1, aZVar17);
                    aZ aZVar18 = new aZ(this.f9993q.t(), d9);
                    aZVar18.a(true);
                    this.f9993q.a(1, aZVar18);
                } else if (d4 > 0.0d) {
                    aZ aZVar19 = new aZ(this.f9993q.t(), d4);
                    aZVar19.a(true);
                    this.f9993q.a(1, aZVar19);
                } else {
                    double dA = a(c0188n, this.f9993q.t());
                    if (dA >= 0.0d) {
                        d4 = dA == 0.0d ? 1.0d + 1.5d : 1.5d;
                    } else {
                        double dB = b(c0188n, this.f9993q.t());
                        if (dB >= 0.0d) {
                            d4 = dB == 0.0d ? 1.5d : 1.0d + 1.5d;
                        }
                    }
                    if (d4 > 0.0d) {
                        aZ aZVar20 = new aZ(this.f9993q.b(), d4);
                        aZVar20.a(true);
                        this.f9993q.a(1, aZVar20);
                        aZ aZVar21 = new aZ(this.f9993q.t(), d4);
                        aZVar21.a(true);
                        this.f9993q.a(1, aZVar21);
                    }
                }
            }
            int iD3 = c0188n.d();
            int i14 = (iD3 - i7) - this.f10004A;
            if (i7 > 0) {
                double dT = (this.f9993q.t() - this.f9993q.b()) / i14;
                int i15 = (iD2 + this.f10004A) - (iD3 - (i7 - i8));
                double d10 = (i8 - this.f10004A) * dT;
                double d11 = i15 * dT;
                if (d10 > 0.0d) {
                    this.f9993q.b(this.f9993q.b() - d10);
                    this.f9993q.e(this.f9993q.b() + (iD2 * dT));
                }
                if (d11 > 0.0d) {
                    this.f9993q.e(this.f9993q.t() + d11);
                }
            }
            this.f9993q.repaint();
        }
    }

    private double k(C0188n c0188n) {
        C0184j c0184jA = c0188n.a("Time");
        for (int i2 = 0; i2 < c0188n.d(); i2++) {
            if (c0184jA.c(i2) != 0.0f) {
                return c0184jA.c(i2);
            }
        }
        return 0.0d;
    }

    private double l(C0188n c0188n) {
        C0184j c0184jA = c0188n.a("Time");
        for (int iD = c0188n.d() - 1; iD >= 0; iD--) {
            if (c0184jA.c(iD) != 0.0f) {
                return c0184jA.c(iD);
            }
        }
        return 0.0d;
    }

    private double a(C0188n c0188n, double d2) {
        C0184j c0184jA = c0188n.a("Time");
        C0184j c0184jA2 = c0188n.a("SecLevel");
        C0184j c0184jA3 = c0188n.a("Trigger");
        for (int i2 = 0; i2 < c0188n.d(); i2++) {
            if (c0184jA.c(i2) > d2 && c0184jA3.c(i2) == 1.0f) {
                return c0184jA2.c(i2);
            }
        }
        return -1.0d;
    }

    private double b(C0188n c0188n, double d2) {
        C0184j c0184jA = c0188n.a("Time");
        C0184j c0184jA2 = c0188n.a("SecLevel");
        C0184j c0184jA3 = c0188n.a("Trigger");
        for (int iD = c0188n.d() - 1; iD >= 0; iD--) {
            if (c0184jA.c(iD) < d2 && c0184jA3.c(iD) == 1.0f) {
                return c0184jA2.c(iD);
            }
        }
        return -1.0d;
    }

    protected void i() {
        C0184j c0184jA;
        C0184j c0184jA2;
        C0184j c0184jA3;
        this.f10004A = this.f10000x.c();
        if (this.f10002ai != null) {
            if (this.f9976a.j().d().equals(C0096cb.f1068d)) {
                if (this.f9986j.getSelectedItem() != null && (c0184jA3 = this.f10002ai.a(this.f9986j.getSelectedItem().toString())) != null) {
                    this.f9982f.f(c0184jA3.n());
                }
                if (this.f9983g) {
                    for (int i2 = 0; i2 < this.f9988l.size(); i2++) {
                        C1683fn c1683fn = (C1683fn) this.f9988l.get(i2);
                        if (c1683fn.d() && (c0184jA = this.f10002ai.a(c1683fn.b())) != null) {
                            this.f9982f.a(i2, c0184jA.n());
                            this.f9982f.a(i2, c1683fn.c());
                        }
                    }
                } else {
                    for (int i3 = 0; i3 < this.f9987k.size(); i3++) {
                        JComboBox jComboBox = (JComboBox) this.f9987k.get(i3);
                        if (jComboBox.getSelectedItem() != null && (c0184jA2 = this.f10002ai.a(jComboBox.getSelectedItem().toString())) != null) {
                            this.f9982f.a(i3, c0184jA2.n());
                        }
                    }
                }
            }
            d(this.f10002ai);
        }
    }

    private void m(C0188n c0188n) throws V.a {
        if (this.f9977b.isDisplayable() || this.f9979d.isDisplayable()) {
            if (c0188n.d() == 0) {
                this.f9977b.setText("Empty read, No Data Received from Controller");
                this.f9978c.getDataVector().clear();
                this.f9978c.fireTableDataChanged();
            } else {
                this.f9977b.setText("");
                Vector vector = new Vector();
                Vector vector2 = new Vector();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(c0188n.g() + "\n");
                for (int i2 = 0; i2 < c0188n.size(); i2++) {
                    C0184j c0184j = (C0184j) c0188n.get(i2);
                    stringBuffer.append(c0184j.a());
                    vector.add(c0184j.a());
                    if (i2 < c0188n.size() - 1) {
                        stringBuffer.append("\t");
                    }
                }
                stringBuffer.append("\n");
                for (int i3 = 0; i3 < c0188n.size(); i3++) {
                    try {
                        stringBuffer.append(((C0184j) c0188n.get(i3)).n());
                        if (i3 < c0188n.size() - 1) {
                            stringBuffer.append("\t");
                        }
                    } catch (Exception e2) {
                        throw new V.a("Invalid units row in log file.");
                    }
                }
                stringBuffer.append("\n");
                if (this.f10033Z) {
                    for (int i4 = 0; i4 < c0188n.d(); i4++) {
                        Vector vector3 = new Vector();
                        vector2.add(vector3);
                        for (int i5 = 0; i5 < c0188n.size(); i5++) {
                            vector3.add(Float.valueOf(((C0184j) c0188n.get(i5)).d(i4)));
                        }
                    }
                    synchronized (this.f9978c) {
                        this.f9978c.setDataVector(vector2, vector);
                    }
                }
                for (int i6 = 0; i6 < c0188n.d(); i6++) {
                    for (int i7 = 0; i7 < c0188n.size(); i7++) {
                        try {
                            stringBuffer.append(((C0184j) c0188n.get(i7)).c(i6) + "");
                            if (i7 < c0188n.size() - 1) {
                                stringBuffer.append("\t");
                            }
                        } catch (Exception e3) {
                            throw new V.a("Invalid data found at record: " + i6 + "\nThis file does not appear valid.");
                        }
                    }
                    stringBuffer.append("\n");
                }
                this.f9977b.setText(stringBuffer.toString());
            }
            this.f9977b.setCaretPosition(0);
        }
    }

    public void g(C0188n c0188n) {
        String strM = this.f9976a.m();
        if (this.f9976a.f() || !(strM == null || strM.equals(""))) {
            if (this.f9998v == null && (c0188n.isEmpty() || ((C0184j) c0188n.get(0)).x())) {
                return;
            }
            if (this.f9998v == null) {
                String strP = this.f10026T.P();
                if (strP == null || strP.isEmpty()) {
                    strP = this.f10026T.i();
                }
                String str = "Firmware: " + strP;
                if (c0188n.g() != null && !c0188n.g().equals("")) {
                    str = str + "\nHeader: " + c0188n.g();
                }
                c0188n.d(str);
                this.f9998v = aZ.d.a(c0188n, strM, ',');
            }
            this.f9998v.a(c0188n);
        }
    }

    protected void a(double d2) {
        if (this.f10003z / 2.0d > this.f10029V) {
            a(this.f10003z / 2.0d, d2);
        } else {
            a(this.f10029V, d2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int s() {
        return (int) Math.round(this.f10005B * this.f10003z);
    }

    public void a(double d2, double d3) {
        double dS;
        double dB = this.f10000x.b();
        if (d2 == this.f10003z) {
            return;
        }
        if (d2 > this.f10003z) {
            this.f10003z = d2;
            dS = (-((s() * d3) / this.f10005B)) / 2.0d;
        } else {
            dS = ((s() * d3) / this.f10005B) / 2.0d;
            this.f10003z = d2;
        }
        b(f10019O, d2 + "");
        this.f10000x.a();
        this.f10000x.a(dB + dS);
        i();
    }

    protected void b(double d2) {
        if (this.f10003z * 2.0d < this.f10031X) {
            a(this.f10003z * 2.0d, d2);
        } else {
            a(this.f10031X, d2);
        }
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f10006aj = interfaceC1662et;
        this.f10007C.a(interfaceC1662et);
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        this.f10009E = true;
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        this.f9976a.l();
    }

    public boolean j() {
        return this.f10023ak;
    }

    public void k() {
        this.f9980ah.clear();
        this.f10039af.clear();
        this.f9977b.setText("");
        this.f9978c.getDataVector().clear();
        this.f9978c.fireTableDataChanged();
        this.f10001y.c();
        this.f10001y.repaint();
        this.f10000x.a(false);
        h();
    }

    public boolean l() {
        return this.f9980ah.size() > 0;
    }

    public boolean m() {
        return this.f10024al;
    }

    public void a(boolean z2) {
        this.f10024al = z2;
    }

    protected ai n() {
        return this.f9980ah;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t() {
        if (this.f10034aa != null && this.f10034aa.f10101a) {
            this.f10034aa.a();
            this.f10000x.b(true);
        } else {
            this.f10034aa = new ax(this);
            this.f10034aa.start();
            this.f10000x.b(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2, int i3) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        jPopupMenu.add(f10014J).addActionListener(new Y(this));
        List<String> listU = u();
        if (!listU.isEmpty()) {
            C1219a c1219a = new C1219a(C1818g.b("Field Smoothing"));
            jPopupMenu.add((JMenuItem) c1219a);
            for (String str : listU) {
                C0184j c0184jA = this.f10002ai.a(str);
                if (c0184jA != null) {
                    C1219a c1219a2 = new C1219a(str);
                    this.f10007C.a(c1219a2, c0184jA, this);
                    c1219a.add((JMenuItem) c1219a2);
                }
            }
        }
        this.f9982f.add(jPopupMenu);
        jPopupMenu.show(this.f9982f, i2, i3);
    }

    private List u() {
        ArrayList arrayList = new ArrayList();
        if (this.f9986j.getSelectedItem() != null && !this.f9986j.getSelectedItem().toString().trim().isEmpty()) {
            arrayList.add(this.f9986j.getSelectedItem().toString());
        }
        if (this.f9983g) {
            for (C1683fn c1683fn : this.f9988l) {
                if (c1683fn.d()) {
                    arrayList.add(c1683fn.b());
                }
            }
        } else {
            for (JComboBox jComboBox : this.f9987k) {
                if (jComboBox.getSelectedItem() != null && !jComboBox.getSelectedItem().toString().trim().isEmpty()) {
                    arrayList.add(jComboBox.getSelectedItem().toString());
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v() {
        if (this.f10002ai != null) {
            this.f10010F.a(this.f10002ai);
            new bB.l(C1818g.d(), this.f10010F).a(bV.b(this));
        }
    }

    @Override // n.g
    public void b() {
        if (a() == null || !a().f() || a().h() == null || !a().h().r()) {
            return;
        }
        f();
    }
}
