package ao;

import W.C0184j;
import W.C0188n;
import aw.C0878a;
import com.efiAnalytics.ui.BinTableView;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.C1621de;
import com.efiAnalytics.ui.C1642e;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.C1705w;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.InterfaceC1662et;
import g.C1729g;
import g.C1733k;
import h.C1737b;
import i.InterfaceC1741a;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import k.C1756d;

/* loaded from: TunerStudioMS.jar:ao/fX.class */
public class fX extends JPanel implements InterfaceC1741a {

    /* renamed from: q, reason: collision with root package name */
    JSlider f5741q;

    /* renamed from: r, reason: collision with root package name */
    JSlider f5742r;

    /* renamed from: x, reason: collision with root package name */
    JPanel f5754x;

    /* renamed from: al, reason: collision with root package name */
    private gF f5756al;

    /* renamed from: D, reason: collision with root package name */
    JButton f5762D;

    /* renamed from: F, reason: collision with root package name */
    JButton f5764F;

    /* renamed from: H, reason: collision with root package name */
    Cdo f5766H;

    /* renamed from: I, reason: collision with root package name */
    Cdo f5767I;

    /* renamed from: J, reason: collision with root package name */
    JButton f5768J;

    /* renamed from: K, reason: collision with root package name */
    Cdo f5769K;

    /* renamed from: L, reason: collision with root package name */
    Cdo f5770L;

    /* renamed from: M, reason: collision with root package name */
    JLabel f5771M;

    /* renamed from: N, reason: collision with root package name */
    JLabel f5772N;

    /* renamed from: P, reason: collision with root package name */
    public static String f5774P = "Current View";

    /* renamed from: an, reason: collision with root package name */
    private static String f5775an = "Save Current View As";

    /* renamed from: ao, reason: collision with root package name */
    private static String f5776ao = "Delete View";

    /* renamed from: Q, reason: collision with root package name */
    static String f5777Q = "Color based on Value";

    /* renamed from: R, reason: collision with root package name */
    static String f5778R = "Color based on Hit Weight";

    /* renamed from: S, reason: collision with root package name */
    static String f5779S = "No Color";

    /* renamed from: T, reason: collision with root package name */
    static String f5780T = "TableGenColorMode";

    /* renamed from: U, reason: collision with root package name */
    static String f5781U = "TableGenDisplayMode";

    /* renamed from: V, reason: collision with root package name */
    JScrollPane f5782V;

    /* renamed from: W, reason: collision with root package name */
    com.efiAnalytics.ui.dD f5783W;

    /* renamed from: X, reason: collision with root package name */
    JComboBox f5784X;

    /* renamed from: a, reason: collision with root package name */
    JPanel f5725a = new JPanel();

    /* renamed from: b, reason: collision with root package name */
    C1642e f5726b = new C1642e();

    /* renamed from: c, reason: collision with root package name */
    C1642e f5727c = new C1642e();

    /* renamed from: d, reason: collision with root package name */
    C1642e f5728d = new C1642e();

    /* renamed from: e, reason: collision with root package name */
    C1642e f5729e = new C1642e();

    /* renamed from: f, reason: collision with root package name */
    C1621de f5730f = new C1621de();

    /* renamed from: g, reason: collision with root package name */
    C1621de f5731g = new C1621de();

    /* renamed from: h, reason: collision with root package name */
    C1621de f5732h = new C1621de();

    /* renamed from: i, reason: collision with root package name */
    C1621de f5733i = new C1621de();

    /* renamed from: j, reason: collision with root package name */
    C1621de f5734j = new C1621de();

    /* renamed from: k, reason: collision with root package name */
    gG f5735k = new gG(this);

    /* renamed from: l, reason: collision with root package name */
    C1705w f5736l = new C1705w(this.f5735k);

    /* renamed from: m, reason: collision with root package name */
    JLabel f5737m = new JLabel(" ", 0);

    /* renamed from: n, reason: collision with root package name */
    JLabel f5738n = new JLabel(" ", 0);

    /* renamed from: o, reason: collision with root package name */
    JLabel f5739o = new JLabel("0.00", 0);

    /* renamed from: p, reason: collision with root package name */
    JLabel f5740p = new JLabel("0.00", 0);

    /* renamed from: s, reason: collision with root package name */
    com.efiAnalytics.ui.fE f5743s = new com.efiAnalytics.ui.fE();

    /* renamed from: t, reason: collision with root package name */
    C0878a f5744t = null;

    /* renamed from: u, reason: collision with root package name */
    JPanel f5745u = null;

    /* renamed from: v, reason: collision with root package name */
    JToggleButton f5746v = new JToggleButton("Scales");

    /* renamed from: af, reason: collision with root package name */
    private InterfaceC1662et f5747af = null;

    /* renamed from: ag, reason: collision with root package name */
    private int f5748ag = 100000000;

    /* renamed from: ah, reason: collision with root package name */
    private int f5749ah = 0;

    /* renamed from: ai, reason: collision with root package name */
    private String f5750ai = null;

    /* renamed from: aj, reason: collision with root package name */
    private String f5751aj = null;

    /* renamed from: ak, reason: collision with root package name */
    private String f5752ak = null;

    /* renamed from: w, reason: collision with root package name */
    HashMap f5753w = new HashMap();

    /* renamed from: y, reason: collision with root package name */
    ArrayList f5755y = new ArrayList();

    /* renamed from: am, reason: collision with root package name */
    private boolean f5757am = false;

    /* renamed from: z, reason: collision with root package name */
    JButton f5758z = null;

    /* renamed from: A, reason: collision with root package name */
    JButton f5759A = null;

    /* renamed from: B, reason: collision with root package name */
    JButton f5760B = null;

    /* renamed from: C, reason: collision with root package name */
    JButton f5761C = null;

    /* renamed from: E, reason: collision with root package name */
    C1621de f5763E = new C1621de();

    /* renamed from: G, reason: collision with root package name */
    JPanel f5765G = new JPanel();

    /* renamed from: O, reason: collision with root package name */
    gH f5773O = new gH(this);

    /* renamed from: Y, reason: collision with root package name */
    JButton f5785Y = new JButton("Toggle Expand");

    /* renamed from: Z, reason: collision with root package name */
    JPanel f5786Z = new JPanel();

    /* renamed from: aa, reason: collision with root package name */
    JPanel f5787aa = new JPanel();

    /* renamed from: ab, reason: collision with root package name */
    C0652bp f5788ab = new C0652bp(this);

    /* renamed from: ac, reason: collision with root package name */
    int f5789ac = com.efiAnalytics.ui.eJ.a(14);

    /* renamed from: ad, reason: collision with root package name */
    boolean f5790ad = true;

    /* renamed from: ae, reason: collision with root package name */
    int f5791ae = -10;

    public fX() throws NumberFormatException {
        this.f5741q = null;
        this.f5742r = null;
        this.f5756al = null;
        this.f5762D = null;
        setLayout(new BorderLayout());
        this.f5756al = new gF(this, this.f5736l);
        this.f5783W = new com.efiAnalytics.ui.dD(this.f5736l);
        this.f5756al.a(250);
        this.f5736l.h().a(1);
        this.f5736l.e(true);
        this.f5736l.setName("Histogram");
        this.f5736l.c(this.f5789ac);
        this.f5736l.h().g(false);
        this.f5736l.f(true);
        this.f5736l.h().f(BinTableView.f10633g);
        this.f5736l.h().h(false);
        this.f5736l.h().getSelectionModel().addListSelectionListener(new fY(this));
        this.f5736l.h().getColumnModel().addColumnModelListener(new C0780gj(this));
        this.f5736l.a(new C0791gu(this));
        this.f5736l.h().a(new com.efiAnalytics.ui.dQ(h.i.f(), "TableGenBinTablePrefs_"));
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        jPanel.setBorder(BorderFactory.createTitledBorder("Histogram View Settings"));
        this.f5764F = new JButton("Saved Histogram Views");
        this.f5764F.addActionListener(new C0796gz(this));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 1));
        jPanel2.add(this.f5764F);
        jPanel.add(jPanel2);
        this.f5754x = new JPanel();
        this.f5754x.setLayout(new BorderLayout());
        this.f5725a.setLayout(new GridLayout(0, 1, 3, 3));
        this.f5725a.add(a(" X Axis:", this.f5726b));
        this.f5725a.add(a(" Y Axis:", this.f5727c));
        this.f5725a.add(a(" Z Axis:", this.f5728d));
        this.f5725a.add(new JLabel("Z Axis Delta (Optional)", 0));
        this.f5725a.setBorder(BorderFactory.createTitledBorder("Axis Fields"));
        this.f5725a.add(this.f5729e);
        this.f5726b.addItemListener(new gA(this));
        this.f5727c.addItemListener(new gB(this));
        this.f5728d.addItemListener(new gC(this));
        this.f5729e.addItemListener(new gD(this));
        this.f5725a.add(new JLabel(" "));
        for (int i2 = 0; i2 < 5; i2++) {
            this.f5730f.addItem(Integer.valueOf(i2));
        }
        this.f5725a.add(a(" X Digits: ", this.f5730f));
        this.f5730f.addActionListener(new gE(this));
        for (int i3 = 0; i3 < 5; i3++) {
            this.f5731g.addItem(Integer.valueOf(i3));
        }
        this.f5725a.add(a(" Y Digits: ", this.f5731g));
        this.f5731g.addActionListener(new fZ(this));
        for (int i4 = 0; i4 < 5; i4++) {
            this.f5732h.addItem(Integer.valueOf(i4));
        }
        this.f5730f.setEnabled(false);
        this.f5731g.setEnabled(false);
        this.f5732h.setEnabled(false);
        this.f5725a.add(a(" Z Digits: ", this.f5732h));
        this.f5732h.addActionListener(new C0771ga(this));
        this.f5784X = new JComboBox();
        this.f5784X.addItem(C0652bp.f5428e);
        this.f5784X.addItem(C0652bp.f5427d);
        this.f5784X.addItem(C0652bp.f5429f);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add("West", this.f5784X);
        add("North", jPanel3);
        this.f5784X.addItemListener(new C0772gb(this));
        this.f5786Z.setLayout(new BorderLayout());
        this.f5786Z.add(BorderLayout.CENTER, this.f5737m);
        this.f5786Z.add("East", this.f5784X);
        this.f5785Y.addActionListener(new C0773gc(this));
        try {
            Image imageA = com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11151ao, this, 18);
            this.f5785Y.setText("");
            this.f5785Y.setIcon(new ImageIcon(imageA));
            this.f5786Z.add("West", this.f5785Y);
            Dimension dimensionA = com.efiAnalytics.ui.eJ.a(18, 18);
            this.f5785Y.setMinimumSize(dimensionA);
            this.f5785Y.setPreferredSize(dimensionA);
        } catch (V.a e2) {
            Logger.getLogger(C0764fu.class.getName()).log(Level.WARNING, "Failed to load Scatter plot full screen image.", (Throwable) e2);
        }
        this.f5785Y.setToolTipText("Toggle Expand");
        this.f5765G.setLayout(new BorderLayout());
        this.f5787aa.setLayout(new BorderLayout());
        this.f5787aa.add(BorderLayout.CENTER, this.f5736l);
        this.f5782V = new JScrollPane(this.f5787aa);
        this.f5765G.add(BorderLayout.CENTER, this.f5782V);
        this.f5765G.add("North", this.f5786Z);
        this.f5765G.add("West", this.f5743s);
        this.f5765G.add("South", this.f5738n);
        this.f5737m.setFont(new Font("Times", 1, com.efiAnalytics.ui.eJ.a(18)));
        add(BorderLayout.CENTER, this.f5765G);
        jPanel.add(this.f5725a);
        JPanel jPanel4 = new JPanel();
        jPanel4.setBorder(BorderFactory.createTitledBorder("Table Dimensions"));
        jPanel4.setLayout(new BorderLayout());
        this.f5766H = new Cdo("", 1);
        this.f5767I = new Cdo("", 1);
        C0774gd c0774gd = new C0774gd(this);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new GridLayout(2, 2));
        jPanel5.add(new JLabel("Rows", 0));
        jPanel5.add(new JLabel("Columns", 0));
        this.f5766H.addFocusListener(c0774gd);
        this.f5767I.addFocusListener(c0774gd);
        jPanel5.add(this.f5766H);
        jPanel5.add(this.f5767I);
        C0775ge c0775ge = new C0775ge(this);
        this.f5766H.addKeyListener(c0775ge);
        this.f5767I.addKeyListener(c0775ge);
        jPanel4.add(jPanel5, BorderLayout.CENTER);
        this.f5768J = new JButton("Resize Table");
        this.f5768J.addActionListener(new C0776gf(this));
        this.f5766H.setEnabled(false);
        this.f5767I.setEnabled(false);
        this.f5768J.setEnabled(false);
        jPanel4.add(this.f5768J, "South");
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new GridLayout(1, 1));
        jPanel6.add(jPanel4);
        jPanel.add(jPanel6);
        JPanel jPanel7 = new JPanel();
        jPanel7.setBorder(BorderFactory.createTitledBorder("Table Display Options"));
        jPanel7.setLayout(new GridLayout(0, 1, 4, 4));
        this.f5733i.a(f5779S);
        this.f5733i.a(f5777Q);
        this.f5733i.a(f5778R);
        this.f5733i.addActionListener(new C0777gg(this));
        jPanel7.add(this.f5733i);
        this.f5769K = new Cdo("Auto", 1);
        this.f5770L = new Cdo("Auto", 1);
        C0778gh c0778gh = new C0778gh(this);
        this.f5769K.addKeyListener(c0778gh);
        this.f5770L.addKeyListener(c0778gh);
        JPanel jPanel8 = new JPanel();
        jPanel8.setLayout(new GridLayout(1, 4));
        this.f5771M = new JLabel("Color Min", 0);
        jPanel8.add(this.f5771M);
        jPanel8.add(this.f5769K);
        this.f5772N = new JLabel("Color Max", 0);
        jPanel8.add(this.f5772N);
        jPanel8.add(this.f5770L);
        this.f5769K.addFocusListener(c0774gd);
        this.f5770L.addFocusListener(c0774gd);
        jPanel7.add(jPanel8);
        this.f5734j.a("Weighted Averages (Default)");
        this.f5734j.a("Minimum Values");
        this.f5734j.a("Maximum Values");
        this.f5734j.addActionListener(new C0779gi(this));
        jPanel7.add(this.f5734j);
        Dimension dimensionA2 = com.efiAnalytics.ui.eJ.a(35, 20);
        JPanel jPanel9 = new JPanel();
        jPanel9.setLayout(new BorderLayout());
        this.f5741q = new JSlider(0, 0, 100, 0);
        this.f5741q.addChangeListener(new C0781gk(this));
        jPanel9.add(BorderLayout.CENTER, this.f5741q);
        this.f5739o.setMinimumSize(dimensionA2);
        this.f5739o.setPreferredSize(dimensionA2);
        JPanel jPanel10 = new JPanel();
        jPanel10.setLayout(new BorderLayout());
        jPanel10.add(BorderLayout.CENTER, this.f5739o);
        jPanel10.add("East", new com.efiAnalytics.ui.cF("Minimum Individual Hit Weight - the minimum weight of each hit before it will be included. This can be used to filter data that hit the edgest of the cell. Valid values are 0.0 to 1.0 where the closer to 1, the more centered a hit needs to be before it is included. 0 all hits will be counted with the associated weighting, a value of 1 requires a direct center hit to be included.", null));
        jPanel9.add("East", jPanel10);
        jPanel7.add(jPanel9);
        JPanel jPanel11 = new JPanel();
        jPanel11.setLayout(new BorderLayout());
        this.f5742r = new JSlider(0, 0, 1000, 0);
        this.f5742r.addChangeListener(new C0782gl(this));
        jPanel11.add(BorderLayout.CENTER, this.f5742r);
        this.f5740p.setMinimumSize(dimensionA2);
        this.f5740p.setPreferredSize(dimensionA2);
        JPanel jPanel12 = new JPanel();
        jPanel12.setLayout(new BorderLayout());
        jPanel12.add(BorderLayout.CENTER, this.f5740p);
        jPanel12.add("East", new com.efiAnalytics.ui.cF("Minimum Total Hit Weight - The minimum cumulative weight a cell must have before it is displayed. This can be used to filter cells that are displaying a value that doesn't have much data generating the value, so less certainty.", null));
        jPanel11.add("East", jPanel12);
        jPanel7.add(jPanel11);
        jPanel.add(jPanel7);
        JPanel jPanel13 = new JPanel();
        jPanel13.setBorder(BorderFactory.createTitledBorder("Populate X & Y Axis"));
        jPanel13.setLayout(new GridLayout(0, 1));
        this.f5763E.a("Don't Generate X & Y Axis");
        this.f5763E.a("Auto-Generate axis High at top");
        this.f5763E.a("Auto-Generate axis Low at top");
        jPanel13.add(this.f5763E);
        this.f5763E.addActionListener(new C0783gm(this));
        this.f5762D = new JButton("Import From Loaded Tune");
        this.f5762D.addActionListener(new C0784gn(this));
        jPanel13.add(this.f5762D);
        jPanel.add(jPanel13);
        jPanel.add(new JLabel(" "));
        jPanel.add(this.f5773O);
        new JButton("Generate histogram").addActionListener(new C0785go(this));
        JPanel jPanel14 = new JPanel();
        jPanel14.setLayout(new BorderLayout());
        jPanel14.add(BorderLayout.CENTER, jPanel);
        Component jPanel15 = new JPanel();
        jPanel15.setPreferredSize(com.efiAnalytics.ui.eJ.a(16, 20));
        jPanel15.setMinimumSize(com.efiAnalytics.ui.eJ.a(16, 20));
        jPanel14.add("East", jPanel15);
        JScrollPane jScrollPane = new JScrollPane(jPanel14);
        jScrollPane.setHorizontalScrollBarPolicy(31);
        add("West", jScrollPane);
        a(true);
        b().c(1);
        C1589c c1589c = new C1589c();
        c1589c.f(0.0d);
        c1589c.f();
        c1589c.d(Double.MAX_VALUE);
        c1589c.a(0.0d);
        c1589c.g(Double.MAX_VALUE);
        c1589c.c(0.0d);
        b().a(c1589c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        if (this.f5736l.p() > 2) {
            this.f5736l.c(1);
        } else {
            this.f5736l.c(this.f5789ac);
        }
        o();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double i() {
        return this.f5741q.getValue() / 100.0d;
    }

    public double a() {
        return Math.pow(this.f5742r.getValue() / 20.0d, 2.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        double d2 = Double.NaN;
        double d3 = Double.NaN;
        try {
            d2 = Double.parseDouble(this.f5769K.getText());
        } catch (NumberFormatException e2) {
        }
        try {
            d3 = Double.parseDouble(this.f5770L.getText());
        } catch (NumberFormatException e3) {
        }
        this.f5736l.h().h(d2);
        this.f5736l.h().g(d3);
        b().C();
        a(C0804hg.a().p());
        this.f5783W.a();
    }

    public gG b() {
        return (gG) this.f5736l.g();
    }

    private void k() {
        if (this.f5763E.getSelectedItem().equals("Don't Generate X & Y Axis") || b().c() == null || b().getRowCount() <= 0 || b().getColumnCount() <= 0) {
            return;
        }
        C0184j c0184jA = b().c().a((String) this.f5726b.getSelectedItem());
        C0184j c0184jA2 = b().c().a((String) this.f5727c.getSelectedItem());
        if (c0184jA != null) {
            b().a(c0184jA.e(), c0184jA.f());
        }
        if (c0184jA2 != null) {
            if (this.f5763E.getSelectedItem().equals("Auto-Generate axis High at top")) {
                b().b(c0184jA2.e(), c0184jA2.f());
            } else {
                b().b(c0184jA2.f(), c0184jA2.e());
            }
        }
        try {
            this.f5736l.k();
        } catch (Exception e2) {
        }
    }

    public void b(int i2) {
        if (i2 == BinTableView.f10633g || i2 == BinTableView.f10632f || i2 == BinTableView.f10631e) {
            this.f5736l.h().f(i2);
            this.f5783W.a();
            b(f5780T, i2 + "");
            if (this.f5733i.getSelectedIndex() != i2) {
                this.f5733i.setSelectedIndex(i2);
            }
        }
        this.f5769K.setEnabled(i2 == BinTableView.f10632f);
        this.f5770L.setEnabled(i2 == BinTableView.f10632f);
        this.f5771M.setEnabled(i2 == BinTableView.f10632f);
        this.f5772N.setEnabled(i2 == BinTableView.f10632f);
    }

    public void c(int i2) {
        bH.C.c("##### display mode: " + i2);
        if (i2 != 1 && i2 != 3 && i2 != 2) {
            bH.C.b("Invalid mode for histogram display: " + i2);
            i2 = 1;
        }
        if (i2 == this.f5736l.g().E() && this.f5736l.g().E() == this.f5734j.getSelectedIndex() + 1) {
            return;
        }
        this.f5736l.g().c(i2);
        b(f5781U, i2 + "");
        if (this.f5734j.getSelectedIndex() + 1 != i2) {
            this.f5734j.setSelectedIndex(i2 - 1);
        }
        this.f5783W.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        jPopupMenu.add(f5775an).addActionListener(new C0786gp(this));
        JMenu jMenu = new JMenu("Delete Saved View");
        C0787gq c0787gq = new C0787gq(this);
        List<String> listA = C0809hl.a();
        for (String str : listA) {
            if (!str.equals(f5774P)) {
                jMenu.add(str).addActionListener(c0787gq);
            }
        }
        jPopupMenu.add((JMenuItem) jMenu);
        jPopupMenu.addSeparator();
        C0788gr c0788gr = new C0788gr(this);
        for (String str2 : listA) {
            if (!str2.equals(f5774P)) {
                jPopupMenu.add(str2).addActionListener(c0788gr);
            }
        }
        this.f5764F.add(jPopupMenu);
        jPopupMenu.show(this.f5764F, 0, this.f5764F.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() {
        try {
            C0809hl.a(c());
        } catch (V.a e2) {
            Logger.getLogger(fX.class.getName()).log(Level.SEVERE, "Ouch", (Throwable) e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n() {
        h.i.c("userParameter_Histogram View Name", this.f5737m.getText());
        C1729g c1729g = new C1729g(C1733k.a(this), "{Histogram View Name}", false, "       Save current Histogram View As", true);
        if (c1729g.f12195a) {
            String strA = c1729g.a();
            C0810hm c0810hmC = c();
            c0810hmC.e(strA);
            try {
                C0809hl.a(c0810hmC);
            } catch (V.a e2) {
                e2.printStackTrace();
                com.efiAnalytics.ui.bV.d(e2.getLocalizedMessage(), this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        if (com.efiAnalytics.ui.bV.a("Are you sure you want to delete the view: " + str, (Component) this, true)) {
            C0809hl.b(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) {
        try {
            C0810hm c0810hmA = C0809hl.a(str);
            if (c0810hmA != null) {
                a(c0810hmA);
            }
        } catch (V.a e2) {
            e2.printStackTrace();
            com.efiAnalytics.ui.bV.d(e2.getLocalizedMessage(), this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() {
        if (this.f5726b.getSelectedItem() == null || this.f5727c.getSelectedItem() == null || this.f5728d.getSelectedItem() == null || b().a() == null || b().a()[0] == null) {
            return;
        }
        C0188n c0188nR = C0804hg.a().r();
        C0184j c0184jA = c0188nR.a(this.f5726b.getSelectedItem().toString());
        C0184j c0184jA2 = c0188nR.a(this.f5728d.getSelectedItem().toString());
        if (c0184jA != null && c0184jA2 != null) {
            this.f5790ad = false;
            this.f5756al.a();
            return;
        }
        C1589c c1589c = new C1589c();
        c1589c.f(0.0d);
        c1589c.f();
        c1589c.d(Double.MAX_VALUE);
        c1589c.a(i());
        c1589c.b(a());
        c1589c.g(Double.MAX_VALUE);
        c1589c.c(0.0d);
        b().a(c1589c);
        this.f5736l.h().l();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void p() {
        if (C1737b.a().a("tableGenerator")) {
            this.f5790ad = true;
            long jCurrentTimeMillis = System.currentTimeMillis();
            k();
            C1589c c1589c = new C1589c();
            c1589c.f(0.0d);
            c1589c.f();
            c1589c.d(Double.MAX_VALUE);
            c1589c.a(i());
            c1589c.b(a());
            c1589c.g(Double.MAX_VALUE);
            c1589c.c(0.0d);
            b().a(c1589c);
            if (this.f5726b.getSelectedItem() == null || this.f5727c.getSelectedItem() == null || this.f5728d.getSelectedItem() == null || b().a() == null || b().a()[0] == null) {
                return;
            }
            C0188n c0188nR = C0804hg.a().r();
            C0184j c0184jA = c0188nR.a(this.f5726b.getSelectedItem().toString());
            C0184j c0184jA2 = c0188nR.a(this.f5727c.getSelectedItem().toString());
            C0184j c0184jA3 = c0188nR.a(this.f5728d.getSelectedItem().toString());
            C0184j c0184jA4 = this.f5729e.getSelectedItem() != null ? c0188nR.a(this.f5729e.getSelectedItem().toString()) : null;
            this.f5744t.a();
            int i2 = 0;
            if (c0184jA == null || c0184jA3 == null) {
                return;
            }
            for (int i3 = this.f5749ah; i3 < c0188nR.d() && i3 < this.f5748ag && this.f5790ad; i3++) {
                if (a(c0188nR, i3)) {
                    i2++;
                } else {
                    float fD = c0184jA.d(i3);
                    float fD2 = c0184jA2 != null ? c0184jA2.d(i3) : Float.NaN;
                    double d2 = c0184jA4 == null ? c0184jA3.d(i3) : c0184jA3.d(i3) - c0184jA4.d(i3);
                    if (!Double.isNaN(d2)) {
                        double dB = C1677fh.b(b().b(), fD);
                        double rowCount = b().H() ? (b().getRowCount() - 1) - C1677fh.a(b().a(), fD2) : (b().getRowCount() - 1) - C1677fh.b(b().a(), fD2);
                        double d3 = b().getColumnCount() == 1 ? 0.0d : dB < ((double) b().getColumnCount()) - 1.000001d ? dB + 1.0E-6d : dB - 1.0E-6d;
                        double d4 = b().getRowCount() == 1 ? 0.0d : rowCount < ((double) b().getRowCount()) - 1.000001d ? rowCount + 1.0E-6d : rowCount - 1.0E-6d;
                        b().D()[(int) Math.floor(d4)][(int) Math.floor(d3)].a(Double.valueOf(d2), (Math.ceil(d3) - d3) * (b().getRowCount() > 1 ? Math.ceil(d4) - d4 : 1.0d));
                        b().D()[(int) Math.floor(d4)][(int) Math.ceil(d3)].a(Double.valueOf(d2), (d3 - Math.floor(d3)) * (b().getRowCount() > 1 ? Math.ceil(d4) - d4 : 1.0d));
                        if (b().getColumnCount() > 1) {
                            b().D()[(int) Math.ceil(d4)][(int) Math.floor(d3)].a(Double.valueOf(d2), (Math.ceil(d3) - d3) * (d4 - Math.floor(d4)));
                        }
                        if (b().getRowCount() > 1 && b().getColumnCount() > 1) {
                            b().D()[(int) Math.ceil(d4)][(int) Math.ceil(d3)].a(Double.valueOf(d2), Math.abs(d3 - Math.floor(d3)) * Math.abs(d4 - Math.floor(d4)));
                        }
                    }
                }
            }
            if (b().getRowCount() == 1) {
                if (!C1685fp.a(this.f5787aa.getComponents(), this.f5788ab)) {
                    this.f5787aa.removeAll();
                    this.f5787aa.add("South", this.f5736l);
                    this.f5787aa.add(BorderLayout.CENTER, this.f5788ab);
                }
                this.f5788ab.setVisible(true);
                this.f5788ab.b();
            } else if (C1685fp.a(this.f5787aa.getComponents(), this.f5788ab)) {
                this.f5787aa.removeAll();
                this.f5787aa.add(BorderLayout.CENTER, this.f5736l);
            }
            this.f5787aa.setPreferredSize(this.f5736l.getPreferredSize());
            this.f5736l.setVisible(true);
            this.f5785Y.setVisible(b().getRowCount() > 24 || b().getColumnCount() > 24);
            this.f5784X.setVisible(b().getRowCount() == 1);
            if (b().d() == null || b().d().n() == null) {
                this.f5788ab.c("");
            } else {
                this.f5788ab.c(b().d().n());
            }
            this.f5788ab.c();
            for (String str : b().b()) {
                this.f5788ab.b(str);
            }
            bH.C.d("Time to process Histogram Data: " + (System.currentTimeMillis() - jCurrentTimeMillis));
            try {
                C0809hl.a(c());
            } catch (V.a e2) {
                e2.printStackTrace();
            }
            SwingUtilities.invokeLater(new RunnableC0789gs(this, c0188nR, i2 + ""));
        }
    }

    public C0810hm c() {
        C0810hm c0810hm = new C0810hm();
        c0810hm.e(f5774P);
        c0810hm.b(b().getColumnCount());
        c0810hm.a(b().getRowCount());
        c0810hm.c(this.f5730f.getSelectedIndex());
        c0810hm.d(this.f5731g.getSelectedIndex());
        c0810hm.e(this.f5732h.getSelectedIndex());
        if (this.f5726b.getSelectedItem() != null) {
            c0810hm.a(h.g.a().d(this.f5726b.getSelectedItem().toString()));
        } else {
            c0810hm.a("");
        }
        if (this.f5727c.getSelectedItem() != null) {
            c0810hm.b(h.g.a().d(this.f5727c.getSelectedItem().toString()));
        } else {
            c0810hm.b("");
        }
        if (this.f5728d.getSelectedItem() != null) {
            c0810hm.c(h.g.a().d(this.f5728d.getSelectedItem().toString()));
        } else {
            c0810hm.c("");
        }
        if (this.f5729e.getSelectedItem() != null) {
            c0810hm.d(h.g.a().d(this.f5729e.getSelectedItem().toString()));
        } else {
            c0810hm.d("");
        }
        c0810hm.f(this.f5788ab.a());
        c0810hm.b(b().a());
        c0810hm.a(b().b());
        c0810hm.f(this.f5733i.getSelectedIndex());
        c0810hm.g(this.f5734j.getSelectedIndex() + 1);
        c0810hm.g((String) this.f5763E.getSelectedItem());
        c0810hm.h(this.f5741q.getValue());
        c0810hm.i(this.f5742r.getValue());
        return c0810hm;
    }

    public void a(C0810hm c0810hm) {
        if (b().getRowCount() != c0810hm.a() || b().getColumnCount() != c0810hm.b()) {
            b().a(c0810hm.a(), c0810hm.b());
        }
        b().e(c0810hm.g());
        b().c(c0810hm.f());
        this.f5736l.h().b(c0810hm.j());
        this.f5736l.b(c0810hm.k());
        this.f5736l.h().a(c0810hm.l());
        this.f5730f.setSelectedItem(Integer.valueOf(c0810hm.j()));
        this.f5731g.setSelectedItem(Integer.valueOf(c0810hm.k()));
        this.f5732h.setSelectedItem(Integer.valueOf(c0810hm.l()));
        this.f5726b.setSelectedItem(h.g.a().a(c0810hm.c()));
        this.f5727c.setSelectedItem(h.g.a().a(c0810hm.d()));
        this.f5728d.setSelectedItem(h.g.a().a(c0810hm.e()));
        this.f5729e.setSelectedItem(h.g.a().a(c0810hm.h()));
        this.f5766H.setText("" + c0810hm.a());
        this.f5767I.setText("" + c0810hm.b());
        this.f5788ab.a(c0810hm.m());
        b(c0810hm.n());
        this.f5734j.setSelectedIndex(c0810hm.o() - 1);
        e(c0810hm.p());
        this.f5741q.setValue(c0810hm.q());
        this.f5742r.setValue(c0810hm.r());
        this.f5736l.k();
        o();
    }

    private boolean a(C0188n c0188n, int i2) {
        for (bx.j jVar : this.f5744t.a()) {
            try {
            } catch (ax.U e2) {
                this.f5744t.a(jVar.a(), false);
                bH.C.d("Filter: " + jVar.a() + " caused errors with this log, disabling.");
                e2.printStackTrace();
            }
            if (C1756d.a().a(c(jVar.c())).a(c0188n, i2) != 0.0d) {
                return true;
            }
        }
        return false;
    }

    private String c(String str) {
        String strC = (String) this.f5753w.get(str);
        if (strC == null) {
            strC = h.g.a().c(str);
            this.f5753w.put(str, strC);
        }
        return strC;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        int iRound = (int) Math.round(this.f5766H.e());
        int iRound2 = (int) Math.round(this.f5767I.e());
        if (iRound <= 0 || iRound2 <= 0) {
            return;
        }
        if (iRound == this.f5736l.g().getRowCount() && iRound2 == this.f5736l.g().getColumnCount()) {
            return;
        }
        gG gGVar = new gG(this);
        gGVar.f5905a = this.f5735k.f5905a;
        gGVar.f5906b = this.f5735k.f5906b;
        gGVar.f5907c = this.f5735k.f5907c;
        gGVar.a(iRound, iRound2);
        C1677fh.c(this.f5736l.g(), gGVar);
        gGVar.c(this.f5734j.getSelectedIndex() + 1);
        this.f5736l.setVisible(false);
        this.f5788ab.setVisible(false);
        this.f5736l.a(gGVar);
        gGVar.a(C0804hg.a().r());
        this.f5735k = gGVar;
        o();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        hn hnVar = C0645bi.a().e().f5149g;
        if (hnVar == null || hnVar.m() == null) {
            com.efiAnalytics.ui.bV.d("No Tune Loaded.", this);
            return;
        }
        hF hFVarM = hnVar.m();
        JPopupMenu jPopupMenu = new JPopupMenu();
        String[] strArr = new String[hFVarM.d()];
        int i2 = 0;
        Iterator itC = hFVarM.c();
        while (itC.hasNext()) {
            strArr[i2] = (String) itC.next();
            i2++;
        }
        C0790gt c0790gt = new C0790gt(this);
        for (String str : C1733k.b(strArr)) {
            jPopupMenu.add(str).addActionListener(c0790gt);
        }
        this.f5762D.add(jPopupMenu);
        jPopupMenu.show(this.f5762D, 0, this.f5762D.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        bH.C.c("Import: " + str);
        hH hHVarB = C0645bi.a().e().f5149g.m().b(str);
        C1701s c1701sG = this.f5736l.g();
        if (c1701sG.getColumnCount() != hHVarB.getColumnCount() || c1701sG.getRowCount() != hHVarB.getRowCount()) {
            c1701sG.a(hHVarB.getRowCount(), hHVarB.getColumnCount());
        }
        C1677fh.c(hHVarB, c1701sG);
        this.f5767I.setText(c1701sG.getColumnCount() + "");
        this.f5766H.setText(c1701sG.getRowCount() + "");
        e("Don't Generate X & Y Axis");
        o();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(String str) {
        this.f5763E.setSelectedItem(str);
        b("autoGenerateXY", "" + str);
    }

    private JPanel a(String str, Component component) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel(str);
        jLabel.setMinimumSize(new Dimension(80, 20));
        jPanel.add("West", jLabel);
        jPanel.add(component, BorderLayout.CENTER);
        return jPanel;
    }

    public void a(boolean z2) {
        if (this.f5744t == null) {
            this.f5745u = new JPanel();
            this.f5745u.setLayout(new BorderLayout());
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            this.f5760B = new JButton(null, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/edit-icon.png"))));
            this.f5760B.setFocusable(false);
            this.f5760B.setToolTipText("Show Filters");
            this.f5760B.addActionListener(new C0792gv(this));
            this.f5760B.setPreferredSize(new Dimension(18, 18));
            jPanel.add("West", this.f5760B);
            this.f5744t = new C0878a(null);
            this.f5744t.a(new C0793gw(this));
            this.f5745u.add("North", jPanel);
            C0595M.a().a(this.f5744t);
            this.f5745u.add(BorderLayout.CENTER, this.f5744t);
            add("East", this.f5745u);
        }
        if (z2) {
        }
    }

    public void a(InterfaceC0797h interfaceC0797h) {
        this.f5755y.add(interfaceC0797h);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        String string = this.f5726b.getSelectedItem() == null ? "" : this.f5726b.getSelectedItem().toString();
        Iterator it = this.f5755y.iterator();
        while (it.hasNext()) {
            ((InterfaceC0797h) it.next()).a(string);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t() {
        String string = this.f5727c.getSelectedItem() == null ? "" : this.f5727c.getSelectedItem().toString();
        Iterator it = this.f5755y.iterator();
        while (it.hasNext()) {
            ((InterfaceC0797h) it.next()).b(string);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void u() {
        String string = this.f5728d.getSelectedItem() == null ? "" : this.f5728d.getSelectedItem().toString();
        Iterator it = this.f5755y.iterator();
        while (it.hasNext()) {
            ((InterfaceC0797h) it.next()).c(string);
        }
    }

    public void a(C0188n c0188n) {
        b().a(c0188n);
        this.f5753w.clear();
        b(c0188n);
        bH.C.c("Dataset size: " + c0188n.d());
        b().a((String) this.f5726b.getSelectedItem());
        b().b((String) this.f5727c.getSelectedItem());
        b().c((String) this.f5728d.getSelectedItem());
        try {
            C0810hm c0810hmA = C0809hl.a(f5774P);
            if (c0810hmA != null) {
                SwingUtilities.invokeLater(new RunnableC0794gx(this, c0810hmA));
            }
        } catch (V.a e2) {
            e2.printStackTrace();
        }
        if (c0188n != null) {
            this.f5730f.setEnabled(true);
            this.f5731g.setEnabled(true);
            this.f5732h.setEnabled(true);
            this.f5766H.setEnabled(true);
            this.f5767I.setEnabled(true);
            this.f5768J.setEnabled(true);
        }
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        int i6 = this.f5726b.getPreferredSize().width;
        int i7 = (i4 - 200) / 3;
        Dimension dimension = i6 < i7 ? new Dimension(i6, 20) : new Dimension(i7, 20);
        this.f5726b.setMinimumSize(dimension);
        this.f5727c.setMinimumSize(dimension);
        this.f5728d.setMinimumSize(dimension);
        super.setBounds(i2, i3, i4, i5);
    }

    private void b(C0188n c0188n) {
        this.f5757am = true;
        a(this.f5726b, c0188n, " ");
        a(this.f5727c, c0188n, " ");
        a(this.f5728d, c0188n, " ");
        a(this.f5729e, c0188n, " ");
        this.f5757am = false;
    }

    private void a(C1642e c1642e, C0188n c0188n, String str) {
        if (c0188n == null) {
            return;
        }
        String str2 = (String) c1642e.getSelectedItem();
        boolean z2 = false;
        String[] strArrB = c1642e.b();
        String[] strArrA = new String[c0188n.size()];
        boolean z3 = strArrB.length == strArrA.length + 1;
        for (int i2 = 0; i2 < c0188n.size(); i2++) {
            strArrA[i2] = ((C0184j) c0188n.get(i2)).a();
            if (z3 && !strArrA[i2].equals(strArrB[i2 + 1])) {
                z3 = false;
            }
        }
        if (!z3) {
            c1642e.removeAllItems();
            if (c1642e.getItemCount() == 0) {
                c1642e.a(str);
            }
            if (h.i.a(h.i.f12284E, h.i.f12285F)) {
                strArrA = bH.R.a(strArrA);
            }
            for (int i3 = 0; i3 < strArrA.length; i3++) {
                if (!z2 && strArrA[i3].equals(str2)) {
                    z2 = true;
                }
                int i4 = i3 + 1;
                c1642e.a(strArrA[i3]);
            }
        }
        if (!z2) {
            try {
                c1642e.b(str2);
            } catch (Exception e2) {
                c1642e.b(str);
            }
        }
    }

    protected void d() throws IllegalArgumentException {
        if (this.f5726b.getSelectedItem() == null || this.f5727c.getSelectedItem() == null || this.f5728d.getSelectedItem() == null || ((String) this.f5726b.getSelectedItem()).length() <= 0 || ((String) this.f5727c.getSelectedItem()).length() <= 0 || ((String) this.f5728d.getSelectedItem()).length() <= 0) {
            return;
        }
        String str = this.f5726b.getSelectedItem() + " vs. " + this.f5727c.getSelectedItem() + " vs. ";
        this.f5737m.setText((this.f5729e.getSelectedItem() == null || this.f5729e.getSelectedItem().toString().isEmpty()) ? str + this.f5728d.getSelectedItem() : str + "( " + this.f5728d.getSelectedItem() + " - " + this.f5729e.getSelectedItem() + " )");
        this.f5738n.setText(this.f5726b.getSelectedItem().toString());
        this.f5743s.setText(this.f5727c.getSelectedItem().toString());
        this.f5736l.g().e(this.f5726b.getSelectedItem().toString());
        this.f5736l.g().d(this.f5727c.getSelectedItem().toString());
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f5747af = interfaceC1662et;
        e();
    }

    public void e() {
        if (this.f5747af != null) {
            this.f5763E.setSelectedItem(a("autoGenerateXY", "Don't Generate X & Y Axis"));
        }
        b(Integer.parseInt(a(f5780T, "2")));
        c(Integer.parseInt(a(f5781U, "1")));
    }

    private String a(String str, String str2) {
        String strA;
        if (this.f5747af != null && (strA = this.f5747af.a(str)) != null) {
            return strA;
        }
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str, String str2) {
        if (this.f5747af != null) {
            this.f5747af.a(str, str2);
        }
    }

    public void d(int i2) {
        this.f5748ag = i2;
        o();
    }

    public void e(int i2) {
        this.f5749ah = i2;
        o();
    }

    public String f() {
        return (String) this.f5729e.getSelectedItem();
    }

    public boolean g() {
        return (b().a() == null || b().a()[0] == null || b().b() == null || b().b()[0] == null) ? false : true;
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        SwingUtilities.invokeLater(new RunnableC0795gy(this, i2));
    }
}
