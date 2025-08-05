package aP;

import W.C0188n;
import ac.C0491c;
import ao.C0625ap;
import ao.C0645bi;
import ao.C0653bq;
import ao.C0656bt;
import ao.C0659bw;
import ao.C0804hg;
import ao.C0811i;
import as.C0847b;
import as.C0852g;
import ay.C0924a;
import bh.C1142b;
import bh.C1162v;
import bt.C1345d;
import bu.C1368a;
import com.efiAnalytics.ui.AbstractC1600ck;
import com.efiAnalytics.ui.C1685fp;
import g.C1733k;
import h.C1737b;
import i.C1743c;
import i.InterfaceC1741a;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import n.InterfaceC1761a;
import r.C1798a;
import r.C1806i;
import r.C1807j;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/bZ.class */
public class bZ extends C1345d implements aE.e, ac.f, InterfaceC1761a, n.g {

    /* renamed from: b, reason: collision with root package name */
    C1142b f3027b;

    /* renamed from: d, reason: collision with root package name */
    public static String f3029d = C1818g.b("Start Data Logging");

    /* renamed from: e, reason: collision with root package name */
    public static String f3030e = C1818g.b("Start capturing a new Data Log to a file.");

    /* renamed from: f, reason: collision with root package name */
    public static String f3031f = C1818g.b("Open Data Log");

    /* renamed from: g, reason: collision with root package name */
    public static String f3032g = C1818g.b("Compare Log");

    /* renamed from: h, reason: collision with root package name */
    public static String f3033h = C1818g.b("Open 2nd Data Log");

    /* renamed from: i, reason: collision with root package name */
    public static String f3034i = C1818g.b("Open an existing data log file.");

    /* renamed from: j, reason: collision with root package name */
    public static String f3035j = C1818g.b("Open TS-Dash Log");

    /* renamed from: k, reason: collision with root package name */
    public static String f3036k = C1818g.b("Only enabled if a TS Dash is detected on the same subnet. You are then able to download and open a log directly from your Dashboard.");

    /* renamed from: l, reason: collision with root package name */
    public static String f3037l = C1818g.b("Stop Data Logging");

    /* renamed from: m, reason: collision with root package name */
    public static String f3038m = C1818g.b("Stop capturing Log to a file.");

    /* renamed from: n, reason: collision with root package name */
    public static String f3039n = C1818g.b("Log Viewing Settings.");

    /* renamed from: o, reason: collision with root package name */
    public static String f3040o = C1818g.b("View Hot Key List.");

    /* renamed from: p, reason: collision with root package name */
    public static String f3041p = C1818g.b("View in MegaLogViewer");

    /* renamed from: q, reason: collision with root package name */
    public static String f3042q = C1818g.b("When checked, log file values will be published to the entire application if offline.");

    /* renamed from: r, reason: collision with root package name */
    JButton f3043r;

    /* renamed from: s, reason: collision with root package name */
    JButton f3044s;

    /* renamed from: t, reason: collision with root package name */
    JButton f3045t;

    /* renamed from: u, reason: collision with root package name */
    JButton f3046u;

    /* renamed from: v, reason: collision with root package name */
    JButton f3047v;

    /* renamed from: w, reason: collision with root package name */
    JButton f3048w;

    /* renamed from: x, reason: collision with root package name */
    JButton f3049x;

    /* renamed from: y, reason: collision with root package name */
    JButton f3050y;

    /* renamed from: z, reason: collision with root package name */
    JCheckBox f3051z;

    /* renamed from: A, reason: collision with root package name */
    JLabel f3052A;

    /* renamed from: B, reason: collision with root package name */
    JLabel f3053B;

    /* renamed from: a, reason: collision with root package name */
    G.R f3026a = null;

    /* renamed from: c, reason: collision with root package name */
    ao.bK f3028c = null;

    /* renamed from: C, reason: collision with root package name */
    String f3054C = "";

    /* renamed from: D, reason: collision with root package name */
    File f3055D = null;

    /* renamed from: E, reason: collision with root package name */
    C1162v f3056E = new C1162v();

    /* renamed from: F, reason: collision with root package name */
    C0852g f3057F = null;

    public bZ() {
        this.f3027b = null;
        this.f3043r = null;
        this.f3044s = null;
        this.f3045t = null;
        this.f3046u = null;
        this.f3047v = null;
        this.f3048w = null;
        this.f3049x = null;
        this.f3050y = null;
        this.f3051z = null;
        this.f3052A = null;
        this.f3053B = null;
        C0659bw c0659bw = new C0659bw(C0804hg.a());
        C0645bi.a().a(c0659bw);
        C0804hg.a().a((InterfaceC1741a) c0659bw);
        this.f3027b = new C1142b();
        this.f3027b.setOpaque(true);
        this.f3027b.p().f(false);
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu(C1818g.b("File"));
        JMenuItem jMenuItem = new JMenuItem(C1818g.b("Open Log"));
        jMenuItem.addActionListener(new C0259ca(this));
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        JToolBar jToolBar = new JToolBar();
        try {
            this.f3043r = new JButton(f3029d, new ImageIcon(com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11098n, this)));
            this.f3043r.setFocusable(false);
            this.f3043r.setToolTipText(f3030e);
            this.f3043r.addActionListener(new C0269ck(this));
            jToolBar.add(this.f3043r);
            this.f3044s = new JButton(f3037l, new ImageIcon(com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11100p, this)));
            this.f3044s.setFocusable(false);
            this.f3044s.setToolTipText(f3038m);
            this.f3044s.addActionListener(new C0280cv(this));
            jToolBar.add(this.f3044s);
            ImageIcon imageIcon = new ImageIcon(com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11102r, this));
            this.f3045t = new JButton(f3031f, imageIcon);
            this.f3045t.setFocusable(false);
            this.f3045t.setToolTipText(f3034i);
            this.f3045t.addActionListener(new cG(this));
            jToolBar.add(this.f3045t);
            this.f3046u = new JButton(f3032g, imageIcon);
            this.f3046u.setFocusable(false);
            this.f3046u.setToolTipText(f3033h);
            this.f3046u.addActionListener(new cQ(this));
            jToolBar.add(this.f3046u);
            this.f3047v = new JButton(f3035j, imageIcon);
            this.f3047v.setFocusable(false);
            this.f3047v.setToolTipText(f3036k);
            this.f3047v.addActionListener(new cS(this));
            jToolBar.add(this.f3047v);
            this.f3047v.setVisible(false);
            jToolBar.add(new JLabel("     "));
            this.f3050y = new JButton(null, new ImageIcon(com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11113C, this)));
            this.f3050y.setFocusable(false);
            this.f3050y.setToolTipText(f3041p);
            this.f3050y.addActionListener(new cT(this));
            jToolBar.add(this.f3050y);
            jToolBar.add(new JLabel("     "));
            this.f3048w = new JButton(null, new ImageIcon(com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11103s, this)));
            this.f3048w.setFocusable(false);
            this.f3048w.setToolTipText(f3039n);
            this.f3048w.addActionListener(new cU(this));
            jToolBar.add(this.f3048w);
            jToolBar.add(new JLabel("     "));
            this.f3049x = new JButton(null, new ImageIcon(com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11090f, this)));
            this.f3049x.setFocusable(false);
            this.f3049x.setToolTipText(f3040o);
            this.f3049x.addActionListener(new cV(this));
            jToolBar.add(this.f3049x);
            this.f3051z = new JCheckBox(C1818g.b("Full Application Playback"));
            if (C1806i.a().a(" a09kmfds098432lkg89vlk")) {
                this.f3051z.setToolTipText(C1818g.b(f3042q));
                this.f3051z.setSelected(C1798a.a().c(C1798a.f13308P, C1798a.f13309Q));
                this.f3051z.addActionListener(new C0260cb(this));
                if (1 != 0) {
                    this.f3051z.setSelected(true);
                } else {
                    jToolBar.add(this.f3051z);
                }
            } else {
                this.f3051z.setSelected(false);
            }
            jToolBar.add(new JLabel("     "));
            this.f3052A = new JLabel();
            jToolBar.add(this.f3052A);
            jToolBar.add(new JLabel("     "));
            this.f3053B = new JLabel();
            jToolBar.add(this.f3053B);
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d(e2.getMessage(), this);
        }
        jPanel.add("South", jToolBar);
        add("North", jPanel);
        add(BorderLayout.CENTER, this.f3027b);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 1));
        C0645bi.a().a(jPanel2);
        add(jPanel2, "South");
        e();
        C0491c.a().a(this);
        C1743c.a().a(this.f3056E);
        C1743c.a().a(new C0261cc(this));
        C0924a.c().a(new cX(this));
        if (C0924a.c().e()) {
            return;
        }
        C0924a.c().g();
    }

    public void e() {
        RunnableC0262cd runnableC0262cd = new RunnableC0262cd(this);
        if (SwingUtilities.isEventDispatchThread()) {
            runnableC0262cd.run();
        } else {
            SwingUtilities.invokeLater(runnableC0262cd);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() throws IllegalArgumentException {
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR != null) {
            C1368a c1368aA = C1368a.a(c0188nR);
            if (!c1368aA.h()) {
                this.f3053B.setText("");
                this.f3053B.setToolTipText(null);
                return;
            }
            String str = "";
            String str2 = "";
            double dA = c1368aA.a();
            if (!Double.isNaN(dA) && dA > 0.0d) {
                str = str + "60ft: " + dA;
                str2 = str2 + "60 ft: " + dA + "<br>";
            }
            double dG = c1368aA.g();
            if (!Double.isNaN(dG) && dG > 0.0d) {
                str = str + " 330ft: " + dG;
                str2 = str2 + "330 ft: " + dG + "<br>";
            }
            double dB = c1368aA.b();
            if (!Double.isNaN(dB) && dB > 0.0d) {
                str = str + " 660ft: " + dB;
                str2 = str2 + "660 ft: " + dB + "<br>";
            }
            double dC = c1368aA.c();
            if (!Double.isNaN(dC) && dC > 0.0d) {
                str = str + " 660MPH: " + dC;
                str2 = str2 + "660 MPH: " + dC + "<br>";
            }
            double d2 = c1368aA.d();
            if (!Double.isNaN(d2) && d2 > 0.0d) {
                str = str + " 1320ft: " + d2;
                str2 = str2 + "1320 ft: " + d2 + "<br>";
            }
            double dE = c1368aA.e();
            if (!Double.isNaN(dE) && dE > 0.0d) {
                str = str + " 1320MPH: " + dE;
                str2 = str2 + "1320 MPH: " + dE + "<br>";
            }
            double dF = c1368aA.f();
            if (!Double.isNaN(dF) && dF > 0.0d) {
                str2 = str2 + "DA: " + dF + "<br>";
            }
            if (str2.length() > 0) {
                this.f3053B.setText(str);
                this.f3053B.setToolTipText("<html>" + str2 + "</html>");
            } else {
                this.f3053B.setText("");
                this.f3053B.setToolTipText(null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        if (this.f3043r != null) {
            new C0264cf(this, new RunnableC0263ce(this)).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        this.f3027b.j();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() throws NumberFormatException {
        JPopupMenu jPopupMenu = new JPopupMenu();
        a(jPopupMenu);
        this.f3048w.add(jPopupMenu);
        jPopupMenu.show(this.f3048w, 0, this.f3048w.getHeight());
    }

    private void a(JPopupMenu jPopupMenu) throws NumberFormatException {
        JMenu jMenu = new JMenu(C1818g.b("Graphing View Layout"));
        C0811i c0811i = new C0811i();
        String strA = h.i.a(h.i.f12293N, h.i.f12297R);
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(C1818g.b("2 Table View (Default)"), h.i.f12296Q.equals(strA));
        jCheckBoxMenuItem.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/layout2Tables.png"))));
        jCheckBoxMenuItem.addItemListener(new C0265cg(this));
        c0811i.a(jCheckBoxMenuItem);
        jMenu.add((JMenuItem) jCheckBoxMenuItem);
        JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem(C1818g.b("1 Table View"), h.i.f12295P.equals(strA));
        jCheckBoxMenuItem2.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/layout1Table.png"))));
        jCheckBoxMenuItem2.addItemListener(new C0266ch(this));
        c0811i.a(jCheckBoxMenuItem2);
        jMenu.add((JMenuItem) jCheckBoxMenuItem2);
        JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem(C1818g.b("No Table View"), h.i.f12294O.equals(strA));
        jCheckBoxMenuItem3.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/layout1Table.png"))));
        jCheckBoxMenuItem3.addItemListener(new C0267ci(this));
        c0811i.a(jCheckBoxMenuItem3);
        jMenu.add((JMenuItem) jCheckBoxMenuItem3);
        jPopupMenu.add((JMenuItem) jMenu);
        if (C1737b.a().a("fileEditing") && this.f3054C.toLowerCase().endsWith(".mlg")) {
            JMenuItem jMenuItem = new JMenuItem("Export Data to " + C1798a.cs);
            jMenuItem.addActionListener(new C0268cj(this));
            jMenuItem.setEnabled(!ac.r.a());
            jPopupMenu.add(jMenuItem);
        }
        jPopupMenu.add(n());
        jPopupMenu.addSeparator();
        JMenu jMenu2 = new JMenu(C1818g.b("Mouse Wheel Action"));
        C0811i c0811i2 = new C0811i();
        String strA2 = h.i.a(h.i.f12286G, h.i.f12289J);
        JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem(C1818g.b("Zoom Graph In / Out (Default)"), h.i.f12287H.equals(strA2));
        jCheckBoxMenuItem4.addItemListener(new C0270cl(this));
        c0811i2.a(jCheckBoxMenuItem4);
        jMenu2.add((JMenuItem) jCheckBoxMenuItem4);
        JCheckBoxMenuItem jCheckBoxMenuItem5 = new JCheckBoxMenuItem(C1818g.b("Page Data Forward / Back"), h.i.f12288I.equals(strA2));
        jCheckBoxMenuItem5.addItemListener(new C0271cm(this));
        c0811i2.a(jCheckBoxMenuItem5);
        jMenu2.add((JMenuItem) jCheckBoxMenuItem5);
        jPopupMenu.add((JMenuItem) jMenu2);
        JCheckBoxMenuItem jCheckBoxMenuItem6 = new JCheckBoxMenuItem(C1818g.b("Show 50% Graph Line"));
        jCheckBoxMenuItem6.setState(h.i.a("showGraphHalfMark", false));
        jCheckBoxMenuItem6.addItemListener(new C0272cn(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem6);
        C0811i c0811i3 = new C0811i();
        boolean zA = h.i.a(h.i.f12290K, true);
        boolean zA2 = h.i.a(h.i.f12291L, h.i.f12292M);
        JMenu jMenu3 = new JMenu("Graph Text Options");
        JCheckBoxMenuItem jCheckBoxMenuItem7 = new JCheckBoxMenuItem(C1818g.b("Show Graph Text"));
        jCheckBoxMenuItem7.setState(zA && zA2);
        jCheckBoxMenuItem7.addItemListener(new C0273co(this));
        jMenu3.add((JMenuItem) jCheckBoxMenuItem7);
        c0811i3.a(jCheckBoxMenuItem7);
        JCheckBoxMenuItem jCheckBoxMenuItem8 = new JCheckBoxMenuItem(C1818g.b("Show Graph Text without Min/Max"));
        jCheckBoxMenuItem8.setState(zA && !zA2);
        jCheckBoxMenuItem8.addItemListener(new C0274cp(this));
        jMenu3.add((JMenuItem) jCheckBoxMenuItem8);
        c0811i3.a(jCheckBoxMenuItem8);
        JCheckBoxMenuItem jCheckBoxMenuItem9 = new JCheckBoxMenuItem(C1818g.b("No Text, Values only"));
        jCheckBoxMenuItem9.setState((zA || zA2) ? false : true);
        jCheckBoxMenuItem9.addItemListener(new C0275cq(this));
        jMenu3.add((JMenuItem) jCheckBoxMenuItem9);
        c0811i3.a(jCheckBoxMenuItem9);
        jPopupMenu.add((JMenuItem) jMenu3);
        C0276cr c0276cr = new C0276cr(this);
        C0811i c0811i4 = new C0811i();
        int iB = h.i.b("lineTraceSize", h.i.f12310ae);
        JMenu jMenu4 = new JMenu("Trace Line Style");
        JMenu jMenu5 = new JMenu("Line width");
        int i2 = 1;
        while (i2 < 6) {
            String str = i2 + "";
            if (i2 == 1) {
                str = str + " (Default)";
            }
            JCheckBoxMenuItem jCheckBoxMenuItem10 = new JCheckBoxMenuItem(str, iB == i2);
            jCheckBoxMenuItem10.setActionCommand("" + i2);
            jCheckBoxMenuItem10.addItemListener(c0276cr);
            c0811i4.a(jCheckBoxMenuItem10);
            jMenu5.add((JMenuItem) jCheckBoxMenuItem10);
            i2++;
        }
        jMenu4.add((JMenuItem) jMenu5);
        JCheckBoxMenuItem jCheckBoxMenuItem11 = new JCheckBoxMenuItem("Antialias Line", h.i.a(h.i.f12311af, h.i.f12312ag));
        jMenu4.add((JMenuItem) jCheckBoxMenuItem11);
        jCheckBoxMenuItem11.addActionListener(new C0277cs(this));
        boolean zA3 = h.i.a(h.i.f12313ah, h.i.f12314ai);
        C0625ap.g(zA3);
        JCheckBoxMenuItem jCheckBoxMenuItem12 = new JCheckBoxMenuItem("Patterned Graph Lines", zA3);
        jMenu4.add((JMenuItem) jCheckBoxMenuItem12);
        jCheckBoxMenuItem12.addActionListener(new C0278ct(this));
        jPopupMenu.add((JMenuItem) jMenu4);
        JMenuItem jMenuItem2 = new JMenuItem(C1818g.b("Field Limits editor"));
        jMenuItem2.addActionListener(new C0279cu(this));
        jPopupMenu.add(jMenuItem2);
        JCheckBoxMenuItem jCheckBoxMenuItem13 = new JCheckBoxMenuItem(C1818g.b("Keep Graph Centered"));
        jCheckBoxMenuItem13.setState(h.i.a("holdGraphCentered", h.i.f12277x));
        jCheckBoxMenuItem13.addItemListener(new C0281cw(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem13);
        C0811i c0811i5 = new C0811i();
        JMenu jMenu6 = new JMenu(C1818g.b("Trace Value Positions"));
        String strE = h.i.e(h.i.f12298S, h.i.f12299T);
        JCheckBoxMenuItem jCheckBoxMenuItem14 = new JCheckBoxMenuItem(C1818g.b("Show Cursor Values At Top"), strE.equals(JSplitPane.TOP));
        jCheckBoxMenuItem14.addItemListener(new C0282cx(this));
        c0811i5.a(jCheckBoxMenuItem14);
        jMenu6.add((JMenuItem) jCheckBoxMenuItem14);
        JCheckBoxMenuItem jCheckBoxMenuItem15 = new JCheckBoxMenuItem(C1818g.b("Show Cursor Values At Bottom"), strE.equals(JSplitPane.BOTTOM));
        jCheckBoxMenuItem15.addItemListener(new C0283cy(this));
        c0811i5.a(jCheckBoxMenuItem15);
        jMenu6.add((JMenuItem) jCheckBoxMenuItem15);
        JCheckBoxMenuItem jCheckBoxMenuItem16 = new JCheckBoxMenuItem(C1818g.b("Show Cursor Values by Field Name"), strE.equals("withLabels"));
        jCheckBoxMenuItem16.addItemListener(new C0284cz(this));
        c0811i5.a(jCheckBoxMenuItem16);
        jMenu6.add((JMenuItem) jCheckBoxMenuItem16);
        jPopupMenu.add((JMenuItem) jMenu6);
        JCheckBoxMenuItem jCheckBoxMenuItem17 = new JCheckBoxMenuItem(C1818g.b("Alphabetize Field Lists"));
        jCheckBoxMenuItem17.setState(h.i.a(h.i.f12284E, h.i.f12285F));
        jCheckBoxMenuItem17.addItemListener(new cA(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem17);
        JCheckBoxMenuItem jCheckBoxMenuItem18 = new JCheckBoxMenuItem(C1818g.b("Scale to Log Length on load"));
        jCheckBoxMenuItem18.setState(h.i.a(h.i.f12300U, h.i.f12301V));
        jCheckBoxMenuItem18.addItemListener(new cB(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem18);
        JMenu jMenu7 = new JMenu("Maximum Number of Graphs");
        jPopupMenu.add((JMenuItem) jMenu7);
        C0811i c0811i6 = new C0811i();
        int i3 = Integer.parseInt(h.i.b("numberOfGraphs", "" + h.i.f12273t));
        int iB2 = h.i.b("numberOfGraphs", i3);
        for (int i4 = 0; i4 < h.i.f12272s; i4++) {
            JCheckBoxMenuItem jCheckBoxMenuItem19 = new JCheckBoxMenuItem(i4 + 1 == i3 ? "Up to " + (i4 + 1) + " Graphs (Default)" : "Up to " + (i4 + 1) + " Graphs");
            c0811i6.a(jCheckBoxMenuItem19);
            jCheckBoxMenuItem19.setActionCommand("" + (i4 + 1));
            if (i4 + 1 == iB2) {
                jCheckBoxMenuItem19.setSelected(true);
            } else {
                jCheckBoxMenuItem19.setSelected(false);
            }
            jCheckBoxMenuItem19.addActionListener(new cC(this));
            jMenu7.add((JMenuItem) jCheckBoxMenuItem19);
        }
        JMenu jMenu8 = new JMenu("Maximum Traces Per Graph");
        jPopupMenu.add((JMenuItem) jMenu8);
        C0811i c0811i7 = new C0811i();
        int i5 = Integer.parseInt(h.i.b("numberOfOverlays", "" + h.i.f12274u));
        int iB3 = h.i.b("numberOfOverlays", i5);
        for (int i6 = 1; i6 < h.i.f12271r; i6++) {
            JCheckBoxMenuItem jCheckBoxMenuItem20 = new JCheckBoxMenuItem(i6 + 1 == i5 ? "Up to " + (i6 + 1) + " Traces (Default)" : "Up to " + (i6 + 1) + " Traces");
            c0811i7.a(jCheckBoxMenuItem20);
            jCheckBoxMenuItem20.setActionCommand("" + (i6 + 1));
            if (i6 + 1 == iB3) {
                jCheckBoxMenuItem20.setSelected(true);
            } else {
                jCheckBoxMenuItem20.setSelected(false);
            }
            jCheckBoxMenuItem20.addActionListener(new cD(this));
            jMenu8.add((JMenuItem) jCheckBoxMenuItem20);
        }
        JMenuItem jMenuItem3 = new JMenuItem(C1818g.b("Select Displayed Fields"));
        jMenuItem3.addActionListener(new cE(this));
        jMenuItem3.setEnabled(C1737b.a().a("selectableFields"));
        jPopupMenu.add(jMenuItem3);
        JMenuItem jCheckBoxMenuItem21 = new JCheckBoxMenuItem(C1818g.b("Quick Trace Selection"));
        boolean zEquals = h.i.a("fieldSelectionStyle", "standardSelection").equals("selectFromDash");
        jCheckBoxMenuItem21.setSelected(zEquals);
        jCheckBoxMenuItem21.addActionListener(new cF(this));
        jPopupMenu.add(jCheckBoxMenuItem21);
        if (!zEquals) {
            JMenuItem jCheckBoxMenuItem22 = new JCheckBoxMenuItem(C1818g.b("Hiding Field Select"));
            jCheckBoxMenuItem22.setSelected(h.i.a("hideSelector", h.i.f12270q));
            jCheckBoxMenuItem22.addActionListener(new cH(this));
            jPopupMenu.add(jCheckBoxMenuItem22);
        }
        JCheckBoxMenuItem jCheckBoxMenuItem23 = new JCheckBoxMenuItem("Keep Index in sync with MegaLogViewer");
        jCheckBoxMenuItem23.setState(h.i.a(h.i.f12337aD, h.i.f12338aE));
        jCheckBoxMenuItem23.addItemListener(new cI(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem23);
        if (C1806i.a().a("timeslipData")) {
            bA.e eVar = new bA.e("View / Edit Timeslip", true);
            eVar.a(new cJ(this));
            eVar.addActionListener(new cK(this));
            jPopupMenu.add((JMenuItem) eVar);
            JMenu jMenu9 = new JMenu("Drag Timeslip Preferences");
            JCheckBoxMenuItem jCheckBoxMenuItem24 = new JCheckBoxMenuItem("Show Time Slips");
            jCheckBoxMenuItem24.setState(h.i.a(h.i.f12335aB, h.i.f12336aC));
            jCheckBoxMenuItem24.setToolTipText("<html>When checked, key Timeslip event data<br>will displayed on graph as yellow vertical bars.");
            jCheckBoxMenuItem24.addActionListener(new cL(this));
            jMenu9.add((JMenuItem) jCheckBoxMenuItem24);
            JCheckBoxMenuItem jCheckBoxMenuItem25 = new JCheckBoxMenuItem("Generate Time Slips");
            jCheckBoxMenuItem25.setState(h.i.a(h.i.f12333az, h.i.f12334aA));
            jCheckBoxMenuItem25.setToolTipText("<html>When checked, Timeslip data will be generated from the<br>launch condition if no time slip data has been entered.");
            jCheckBoxMenuItem25.addActionListener(new cM(this));
            jMenu9.add((JMenuItem) jCheckBoxMenuItem25);
            JCheckBoxMenuItem jCheckBoxMenuItem26 = new JCheckBoxMenuItem("Jump to Launch");
            jCheckBoxMenuItem26.setState(h.i.a(h.i.f12331ax, h.i.f12332ay));
            jCheckBoxMenuItem26.setToolTipText("<html>When checked, upon opening a log file the cursor<br>will jump to the launch point of the log.");
            jCheckBoxMenuItem26.addActionListener(new cN(this));
            jMenu9.add((JMenuItem) jCheckBoxMenuItem26);
            jPopupMenu.add((JMenuItem) jMenu9);
        }
        C1685fp.a(jPopupMenu);
    }

    private JMenu n() {
        JMenu jMenu = new JMenu("Settings import / export");
        JMenuItem jMenuItem = new JMenuItem("Export Settings to file");
        jMenuItem.addActionListener(new cO(this));
        jMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem("Import Settings file");
        jMenuItem2.addActionListener(new cP(this));
        jMenu.add(jMenuItem2);
        return jMenu;
    }

    public void f() {
        ArrayList arrayList = new ArrayList();
        C0656bt c0656bt = new C0656bt("FIELD_GROUP_NAME_", "Quick View Tabs");
        c0656bt.a("All Quick View tabs on this PC");
        c0656bt.b("FIELD_SELECTED_GROUP_");
        arrayList.add(c0656bt);
        C0656bt c0656bt2 = new C0656bt("FIELD_MIN_MAX_", "Field Min/Max Settings");
        c0656bt2.a("Export set Min & Max values and autoscale settings.");
        arrayList.add(c0656bt2);
        C0656bt c0656bt3 = new C0656bt(h.i.f12298S, "Viewing preferences");
        c0656bt3.b("numberOfGraphs");
        c0656bt3.b("numberOfOverlays");
        c0656bt3.b("numberOfOverlayGraphs");
        c0656bt3.a("Export number of graphs, traces per graph, Trace Value Position, etc.");
        arrayList.add(c0656bt3);
        C0653bq c0653bq = new C0653bq(arrayList, true);
        c0653bq.a(com.efiAnalytics.ui.bV.b(this));
        List listB = c0653bq.b();
        if (listB.isEmpty()) {
            return;
        }
        String strA = C1733k.a(this, "Export Settings", new String[]{"settings"}, "LogViewerSettings_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".settings");
        if (strA == null || strA.equals("")) {
            return;
        }
        if (!strA.toLowerCase().endsWith("settings")) {
            strA = strA + ".settings";
        }
        Properties properties = new Properties();
        Iterator<Object> it = h.i.f12258e.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (a(str, listB)) {
                properties.setProperty(str, h.i.f12258e.getProperty(str));
            }
        }
        File file = new File(strA);
        if (!file.exists() || com.efiAnalytics.ui.bV.a("The file " + file.getName() + " already exists.\n\nOverwrite?", (Component) this, true)) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                properties.store(fileOutputStream, "Embedded LogViewer Settings");
                fileOutputStream.close();
            } catch (Exception e2) {
                C1733k.a("Unable to save settings to " + strA + "\nSee log for more detail.", this);
                e2.printStackTrace();
            }
        }
    }

    public void g() {
        String strA = C1733k.a(this, "Import Settings", new String[]{"settings"}, "*.settings");
        if (strA == null || strA.equals("")) {
            return;
        }
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(strA);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (Exception e2) {
            C1733k.a("Unable to open settings file " + strA + "\nSee log for more detail.", this);
            e2.printStackTrace();
        }
        ArrayList arrayList = new ArrayList();
        if (a(properties, "FIELD_GROUP_NAME_")) {
            C0656bt c0656bt = new C0656bt("FIELD_GROUP_NAME_", "Quick View Tabs");
            c0656bt.a("All Quick View tabs on this PC");
            c0656bt.b("FIELD_SELECTED_GROUP_");
            arrayList.add(c0656bt);
        }
        if (a(properties, "FIELD_MIN_MAX_")) {
            C0656bt c0656bt2 = new C0656bt("FIELD_MIN_MAX_", "Field Min/Max Settings");
            c0656bt2.a("Import set Min & Max values and autoscale settings.");
            arrayList.add(c0656bt2);
        }
        if (a(properties, h.i.f12298S)) {
            C0656bt c0656bt3 = new C0656bt(h.i.f12298S, "Viewing preferences");
            c0656bt3.b("numberOfGraphs");
            c0656bt3.b("numberOfOverlays");
            c0656bt3.b("numberOfOverlayGraphs");
            c0656bt3.a("Import number of graphs, traces per graph, Trace Value Position, etc.");
            arrayList.add(c0656bt3);
        }
        if (arrayList.isEmpty()) {
            com.efiAnalytics.ui.bV.d("There are no settings in this file to import.", this);
            return;
        }
        C0653bq c0653bq = new C0653bq(arrayList, false);
        c0653bq.a(com.efiAnalytics.ui.bV.b(this));
        List listB = c0653bq.b();
        if (listB.isEmpty()) {
            return;
        }
        if (c0653bq.a()) {
            for (String str : h.i.f12258e.stringPropertyNames()) {
                if (a(str, listB)) {
                    h.i.d(str);
                }
            }
        } else if (JOptionPane.showConfirmDialog(this, "Warning!!!!\nAny Quick Views settings of the same name will be overridden\n\nContinue?") != 0) {
            return;
        }
        Iterator<Object> it = properties.keySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (a(str2, listB)) {
                h.i.f12258e.setProperty(str2, properties.getProperty(str2));
            }
        }
        C1733k.a("The Application will now restart for changes to take effect.", C0645bi.a().b());
        C0338f.a().d(com.efiAnalytics.ui.bV.c());
    }

    private boolean a(Properties properties, String str) {
        Iterator<String> it = properties.stringPropertyNames().iterator();
        while (it.hasNext()) {
            if (it.next().startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean a(String str, List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (str.startsWith((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    public void a(String str, boolean z2) {
        h.i.c(str, z2 + "");
        if (this.f3027b.l() == null || !C1733k.a(C1818g.b("Log file must be reloaded for change to take effect.") + "\n" + C1818g.b("Reload now?"), (Component) this, true)) {
            return;
        }
        this.f3027b.a(new String[]{this.f3027b.l()}, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() throws IllegalArgumentException {
        String[] strArrA = a("Open Log File");
        if (this.f3027b.s() != null) {
            this.f3027b.s().c();
        }
        if (strArrA != null && strArrA.length > 0) {
            File[] fileArr = new File[strArrA.length];
            for (int i2 = 0; i2 < fileArr.length; i2++) {
                fileArr[i2] = new File(strArrA[i2]);
            }
            a(fileArr);
        }
        h.i.g();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p() throws IllegalArgumentException {
        String[] strArrA = a("Compare Log File");
        if (strArrA != null && strArrA.length > 0) {
            c(strArrA[0]);
        }
        h.i.g();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        if (this.f3057F == null) {
            if (C0847b.a() == null) {
                C0847b.a(new cY(this));
            }
            cW cWVar = new cW(this);
            this.f3057F = new C0852g(com.efiAnalytics.ui.bV.c(), cWVar, false);
            this.f3057F.a(cWVar);
            int iB = h.i.b(h.i.f12320am, -1);
            int iB2 = h.i.b(h.i.f12321an, -1);
            if (iB2 <= 0 || iB <= 0) {
                this.f3057F.pack();
            } else {
                this.f3057F.setSize(iB, iB2);
            }
            com.efiAnalytics.ui.bV.a(com.efiAnalytics.ui.bV.c(), (Component) this.f3057F);
            this.f3057F.setVisible(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        if (this.f3057F != null) {
            this.f3057F.dispose();
            this.f3057F = null;
        }
    }

    public void a(File[] fileArr) throws IllegalArgumentException {
        if (fileArr == null || fileArr.length <= 0 || !fileArr[0].exists()) {
            return;
        }
        C0804hg.a().j();
        if (this.f3027b.s() != null) {
            this.f3027b.s().c();
        }
        C0804hg.a().j();
        this.f3052A.setText(fileArr[0].getName());
        this.f3054C = fileArr[0].getName();
        this.f3055D = fileArr[0];
        String[] strArr = new String[fileArr.length];
        for (int i2 = 0; i2 < fileArr.length; i2++) {
            strArr[i2] = fileArr[i2].getAbsolutePath();
        }
        strArr[0] = fileArr[0].getAbsolutePath();
        C0804hg.a().a(strArr);
        a(strArr, false);
        new cR(this).start();
    }

    public String[] a(String str) {
        String[] strArrA = com.efiAnalytics.ui.bV.a((Component) this, str, bH.W.c(h.i.a("fileExtensions", "msl;csv;mlg;"), ";"), (String) null, aE.a.A() == null ? h.i.e("lastFileDir", C1807j.u()) : aE.a.A().L().getAbsolutePath(), true, (AbstractC1600ck) null, true);
        if (strArrA != null && strArrA.length >= 1 && strArrA[0] != null && strArrA[0].lastIndexOf(File.separator) != -1) {
            h.i.c("lastFileDir", strArrA[0].substring(0, strArrA[0].lastIndexOf(File.separator)));
        }
        return strArrA;
    }

    public void a(String[] strArr, boolean z2) throws IllegalArgumentException {
        new File(strArr[0]);
        this.f3027b.a(strArr, z2);
        C0804hg.a().a(strArr);
        String strSubstring = strArr[0].substring(strArr[0].lastIndexOf(File.separator) + 1);
        this.f3054C = strSubstring;
        this.f3055D = new File(strArr[0]);
        this.f3052A.setText(strSubstring);
    }

    public void c(String str) throws IllegalArgumentException {
        this.f3027b.c(str);
        this.f3052A.setText(this.f3054C + " - " + new File(str).getName());
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
        this.f3026a = r2;
        this.f3027b.a(r2);
    }

    @Override // aE.e
    public void e_() {
        this.f3027b.close();
        C0188n c0188n = new C0188n();
        C0804hg.a().b(c0188n);
        this.f3027b.c(c0188n);
        this.f3026a = null;
        h.i.g();
        this.f3056E.a();
        this.f3052A.setText("");
    }

    @Override // n.InterfaceC1761a
    public boolean a() {
        bH.C.c("Activate Analysis Tabs");
        if (this.f3028c == null) {
            this.f3028c = ao.bK.a();
        }
        this.f3027b.p().f(true);
        j();
        C0645bi.a().f().a(false);
        return true;
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }

    @Override // n.g
    public void b() {
        if (this.f3028c != null) {
            bY.a().b(this.f3028c);
            this.f3028c.b();
        }
        if (!C1798a.a().c(C1798a.f13308P, C1798a.f13309Q)) {
            C0804hg.a().j();
        }
        C0645bi.a().f().a(true);
        bH.C.c("Deactivate Analysis Tabs");
    }

    @Override // ac.f
    public void a(File file) throws IllegalArgumentException {
        j();
        String name = file.getName();
        this.f3054C = name;
        this.f3055D = file;
        this.f3052A.setText(name);
    }

    @Override // ac.f
    public void c() {
    }

    @Override // ac.f
    public void d() {
        j();
    }

    @Override // ac.f
    public void b(String str) {
    }

    protected File h() {
        return this.f3055D;
    }
}
