package ao;

import W.C0184j;
import W.C0188n;
import ar.C0836c;
import ar.C0837d;
import com.efiAnalytics.ui.C1580br;
import com.efiAnalytics.ui.InterfaceC1579bq;
import g.C1733k;
import h.C1737b;
import i.InterfaceC1741a;
import i.InterfaceC1742b;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import k.C1753a;

/* renamed from: ao.ap, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ap.class */
public class C0625ap extends JComponent implements InterfaceC0640bd, InterfaceC0758fo, InterfaceC0763ft, InterfaceC0813k, InterfaceC0814l, InterfaceC1741a, InterfaceC1742b {

    /* renamed from: a, reason: collision with root package name */
    Image f5229a;

    /* renamed from: j, reason: collision with root package name */
    Color f5238j;

    /* renamed from: o, reason: collision with root package name */
    C0804hg f5244o;

    /* renamed from: ad, reason: collision with root package name */
    private int f5262ad;

    /* renamed from: ai, reason: collision with root package name */
    private boolean f5279ai;

    /* renamed from: aa, reason: collision with root package name */
    private static final int f5252aa = com.efiAnalytics.ui.eJ.a(100);

    /* renamed from: ab, reason: collision with root package name */
    private static final int f5253ab = com.efiAnalytics.ui.eJ.a(160);

    /* renamed from: B, reason: collision with root package name */
    static final int f5260B = com.efiAnalytics.ui.eJ.a(20);

    /* renamed from: af, reason: collision with root package name */
    private static boolean f5269af = false;

    /* renamed from: R, reason: collision with root package name */
    public static String f5285R = "Auto";

    /* renamed from: S, reason: collision with root package name */
    public static String f5286S = "BruteForce";

    /* renamed from: am, reason: collision with root package name */
    private static final String f5290am = h.i.d();

    /* renamed from: b, reason: collision with root package name */
    Image f5230b = null;

    /* renamed from: c, reason: collision with root package name */
    Image f5231c = null;

    /* renamed from: d, reason: collision with root package name */
    Image f5232d = null;

    /* renamed from: e, reason: collision with root package name */
    Image f5233e = null;

    /* renamed from: f, reason: collision with root package name */
    Image f5234f = null;

    /* renamed from: g, reason: collision with root package name */
    Image f5235g = null;

    /* renamed from: h, reason: collision with root package name */
    Image f5236h = null;

    /* renamed from: i, reason: collision with root package name */
    Color f5237i = Color.white;

    /* renamed from: k, reason: collision with root package name */
    HashMap f5239k = new HashMap();

    /* renamed from: l, reason: collision with root package name */
    C0188n f5240l = null;

    /* renamed from: m, reason: collision with root package name */
    HashMap f5241m = new HashMap();

    /* renamed from: n, reason: collision with root package name */
    HashMap f5242n = null;

    /* renamed from: Y, reason: collision with root package name */
    private C0184j f5243Y = null;

    /* renamed from: p, reason: collision with root package name */
    int f5245p = 0;

    /* renamed from: q, reason: collision with root package name */
    int f5246q = 0;

    /* renamed from: r, reason: collision with root package name */
    int f5247r = 100;

    /* renamed from: s, reason: collision with root package name */
    double f5248s = 2.0d;

    /* renamed from: Z, reason: collision with root package name */
    private int f5249Z = 0;

    /* renamed from: t, reason: collision with root package name */
    int f5250t = -1;

    /* renamed from: u, reason: collision with root package name */
    int f5251u = com.efiAnalytics.ui.eJ.a(80);

    /* renamed from: v, reason: collision with root package name */
    boolean f5254v = true;

    /* renamed from: w, reason: collision with root package name */
    boolean f5255w = false;

    /* renamed from: x, reason: collision with root package name */
    boolean f5256x = true;

    /* renamed from: y, reason: collision with root package name */
    boolean f5257y = true;

    /* renamed from: z, reason: collision with root package name */
    ArrayList f5258z = new ArrayList();

    /* renamed from: A, reason: collision with root package name */
    int f5259A = 0;

    /* renamed from: ac, reason: collision with root package name */
    private C0588F f5261ac = null;

    /* renamed from: C, reason: collision with root package name */
    ArrayList f5263C = new ArrayList();

    /* renamed from: D, reason: collision with root package name */
    String f5264D = null;

    /* renamed from: E, reason: collision with root package name */
    String f5265E = null;

    /* renamed from: F, reason: collision with root package name */
    String f5266F = null;

    /* renamed from: G, reason: collision with root package name */
    String f5267G = null;

    /* renamed from: ae, reason: collision with root package name */
    private String f5268ae = null;

    /* renamed from: H, reason: collision with root package name */
    List f5270H = new ArrayList();

    /* renamed from: I, reason: collision with root package name */
    Stroke f5271I = new BasicStroke(1.0f);

    /* renamed from: J, reason: collision with root package name */
    int f5272J = 16000;

    /* renamed from: K, reason: collision with root package name */
    boolean f5273K = false;

    /* renamed from: L, reason: collision with root package name */
    boolean f5274L = true;

    /* renamed from: M, reason: collision with root package name */
    boolean f5275M = false;

    /* renamed from: ag, reason: collision with root package name */
    private int f5276ag = Integer.MIN_VALUE;

    /* renamed from: ah, reason: collision with root package name */
    private int f5277ah = Integer.MAX_VALUE;

    /* renamed from: N, reason: collision with root package name */
    Color f5278N = new Color(128, 128, 128, 128);

    /* renamed from: aj, reason: collision with root package name */
    private boolean f5280aj = true;

    /* renamed from: ak, reason: collision with root package name */
    private C0812j f5281ak = null;

    /* renamed from: O, reason: collision with root package name */
    int f5282O = 10;

    /* renamed from: P, reason: collision with root package name */
    C0594L f5283P = null;

    /* renamed from: Q, reason: collision with root package name */
    C0594L f5284Q = null;

    /* renamed from: T, reason: collision with root package name */
    C1753a f5287T = null;

    /* renamed from: al, reason: collision with root package name */
    private boolean f5288al = true;

    /* renamed from: U, reason: collision with root package name */
    boolean f5289U = false;

    /* renamed from: V, reason: collision with root package name */
    String f5291V = null;

    /* renamed from: an, reason: collision with root package name */
    private boolean f5292an = false;

    /* renamed from: W, reason: collision with root package name */
    boolean f5293W = false;

    /* renamed from: X, reason: collision with root package name */
    int f5294X = -1;

    public C0625ap(C0804hg c0804hg) {
        this.f5229a = null;
        this.f5238j = null;
        this.f5244o = null;
        this.f5262ad = 2;
        this.f5279ai = true;
        this.f5244o = c0804hg;
        this.f5229a = Toolkit.getDefaultToolkit().getImage(h.i.f12280A);
        setFocusable(true);
        this.f5262ad = h.i.a("numberOfOverlays", h.i.f12274u);
        this.f5279ai = h.i.a("holdGraphCentered", h.i.f12277x);
        Font font = UIManager.getFont("Label.font");
        int iA = com.efiAnalytics.ui.eJ.a(11);
        setFont(new Font("SansSerif", 0, font != null ? Math.round(font.getSize2D() * (iA / com.efiAnalytics.ui.eJ.a())) : iA));
        String strC = h.i.c("startUpBackgroundColor");
        if (strC == null || strC.isEmpty()) {
            return;
        }
        this.f5238j = h.i.a("startUpBackgroundColor", Color.lightGray);
    }

    public void a(String str) {
        this.f5268ae = str;
    }

    public void a(C0184j c0184j) {
        this.f5243Y = c0184j;
        if (c0184j == null || c0184j.m() >= 0) {
            return;
        }
        c0184j.e(3);
    }

    public void a(int i2, int i3) {
        if (C1737b.a().a("searchLogFiles")) {
            C1580br c1580br = new C1580br();
            a(c1580br);
            add(c1580br);
            c1580br.show(this, i2, i3);
        }
    }

    public InterfaceC1579bq a(InterfaceC1579bq interfaceC1579bq) {
        JMenu jMenu = new JMenu("Marks");
        if (this.f5241m == null || this.f5241m.isEmpty()) {
            JMenuItem jMenuItem = new JMenuItem("No Marks in Current Data Log");
            jMenuItem.setEnabled(false);
            jMenu.add(jMenuItem);
        } else {
            for (Object obj : C1733k.a(this.f5241m.keySet().toArray())) {
                int iIntValue = ((Integer) obj).intValue();
                JMenuItem jMenuItem2 = new JMenuItem(this.f5244o.r().b(iIntValue));
                jMenuItem2.setActionCommand("jumpTo:" + iIntValue);
                jMenuItem2.addActionListener(new C0626aq(this));
                jMenu.add(jMenuItem2);
            }
        }
        jMenu.addSeparator();
        JMenuItem jMenuItem3 = new JMenuItem("Add mark at cursor position");
        jMenuItem3.addActionListener(new aB(this));
        jMenu.add(jMenuItem3);
        if (this.f5241m.get(this.f5246q + "") != null) {
            JMenuItem jMenuItem4 = new JMenuItem("Delete Mark: " + ((String) this.f5241m.get(this.f5246q + "")));
            jMenuItem4.addActionListener(new aG(this));
            jMenu.add(jMenuItem4);
        }
        interfaceC1579bq.add(jMenu);
        if (this.f5242n != null && !this.f5242n.isEmpty()) {
            JMenu jMenu2 = new JMenu(this.f5283P == null ? "Time Gaps" : "Timeslip lines");
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(this.f5283P == null ? "Show Time Gap Lines" : "Show Timeslip lines", this.f5257y);
            jCheckBoxMenuItem.addItemListener(new aH(this));
            jMenu2.add((JMenuItem) jCheckBoxMenuItem);
            jMenu2.addSeparator();
            for (Object obj2 : C1733k.a(this.f5242n.keySet().toArray())) {
                int iIntValue2 = ((Integer) obj2).intValue();
                JMenuItem jMenuItem5 = new JMenuItem(this.f5244o.r().d(iIntValue2));
                jMenuItem5.setActionCommand("jumpTo:" + iIntValue2);
                jMenuItem5.addActionListener(new aI(this));
                jMenu2.add(jMenuItem5);
            }
            interfaceC1579bq.add(jMenu2);
        }
        JMenu jMenu3 = new JMenu("Log Editing");
        boolean zA = C1737b.a().a("fileEditing");
        if (y() != null) {
            JMenuItem jMenuItem6 = new JMenuItem("Save Selected to File");
            jMenuItem6.setActionCommand("exportSelectedLog:" + y().b() + "," + y().a());
            jMenuItem6.addActionListener(new aJ(this));
            jMenu3.add(jMenuItem6);
            jMenuItem6.setEnabled(zA);
        }
        if (y() != null) {
            JMenuItem jMenuItem7 = new JMenuItem("Crop to Selected");
            jMenuItem7.setActionCommand("cropToSelectedLog:" + y().b() + "," + y().a());
            jMenuItem7.addActionListener(new aK(this));
            jMenu3.add(jMenuItem7);
            jMenuItem7.setEnabled(zA);
        }
        if (y() != null) {
            JMenuItem jMenuItem8 = new JMenuItem("Delete Selected (Delete)");
            jMenuItem8.setActionCommand("deleteSelectedFromLog:" + y().b() + "," + y().a());
            jMenuItem8.addActionListener(new aL(this));
            jMenu3.add(jMenuItem8);
            jMenuItem8.setEnabled(zA);
        }
        JMenuItem jMenuItem9 = new JMenuItem("Delete Before Cursor Position");
        jMenuItem9.setActionCommand("deleteBefore");
        jMenuItem9.addActionListener(new aM(this));
        jMenu3.add(jMenuItem9);
        jMenuItem9.setEnabled(zA);
        JMenuItem jMenuItem10 = new JMenuItem("Delete After Cursor Position");
        jMenuItem10.setActionCommand("deleteAfter");
        jMenuItem10.addActionListener(new C0627ar(this));
        jMenu3.add(jMenuItem10);
        jMenuItem10.setEnabled(zA);
        interfaceC1579bq.add(jMenu3);
        if (this.f5246q > 0) {
            JMenuItem jMenuItem11 = new JMenuItem("Set Current Time to 0 (CTRL+0)");
            jMenuItem11.setActionCommand("setTimeOffsetToCurrent:");
            jMenuItem11.addActionListener(new C0628as(this));
            interfaceC1579bq.add(jMenuItem11);
        }
        interfaceC1579bq.addSeparator();
        if (this.f5264D != null) {
            JMenuItem jMenuItem12 = new JMenuItem("Repeat Search (F3)");
            jMenuItem12.addActionListener(new C0629at(this));
            interfaceC1579bq.add(jMenuItem12);
            interfaceC1579bq.addSeparator();
        }
        if (this.f5239k != null) {
            JMenuItem jMenuItem13 = new JMenuItem("Expression Search");
            jMenuItem13.setActionCommand("searchExpression:");
            jMenuItem13.setName(this.f5267G);
            jMenuItem13.addActionListener(new C0630au(this));
            interfaceC1579bq.add(jMenuItem13);
            for (Object obj3 : C1733k.b(this.f5239k.keySet().toArray())) {
                for (C0184j c0184j : (List) this.f5239k.get(obj3)) {
                    if (c0184j.a() != null && !c0184j.a().trim().equals("")) {
                        JMenu jMenu4 = new JMenu(c0184j.a());
                        JMenuItem jMenuItem14 = new JMenuItem("Jump to Max : " + a(c0184j.g(), c0184j.m()));
                        jMenuItem14.setActionCommand("searchFor:" + c0184j.g());
                        jMenuItem14.setName(c0184j.a());
                        jMenuItem14.addActionListener(new C0631av(this));
                        jMenu4.add(jMenuItem14);
                        JMenuItem jMenuItem15 = new JMenuItem("Jump to Min : " + a(c0184j.h(), c0184j.m()));
                        jMenuItem15.setActionCommand("searchFor:" + c0184j.h());
                        jMenuItem15.setName(c0184j.a());
                        jMenuItem15.addActionListener(new C0632aw(this));
                        jMenu4.add(jMenuItem15);
                        JMenuItem jMenuItem16 = new JMenuItem("Search for specific " + c0184j.a());
                        jMenuItem16.setActionCommand("userValue:" + c0184j.a());
                        jMenuItem16.setName(c0184j.a());
                        jMenuItem16.addActionListener(new C0633ax(this));
                        jMenu4.add(jMenuItem16);
                        JMenuItem jMenuItem17 = new JMenuItem("Search for " + c0184j.a() + " Greater than");
                        jMenuItem17.setActionCommand("searchGreaterThan:" + c0184j.a());
                        jMenuItem17.setName(c0184j.a());
                        jMenuItem17.addActionListener(new C0634ay(this));
                        jMenu4.add(jMenuItem17);
                        JMenuItem jMenuItem18 = new JMenuItem("Search for " + c0184j.a() + " Less than");
                        jMenuItem18.setActionCommand("searchLessThan:" + c0184j.a());
                        jMenuItem18.setName(c0184j.a());
                        jMenuItem18.addActionListener(new C0635az(this));
                        jMenu4.add(jMenuItem18);
                        JMenu jMenu5 = new JMenu("Value offsets");
                        JMenuItem jMenuItem19 = new JMenuItem("Shift logged value of by specific amount.");
                        jMenuItem19.setActionCommand("shiftValueBy" + c0184j.a());
                        jMenuItem19.setName(c0184j.a());
                        jMenuItem19.addActionListener(new aA(this));
                        jMenu5.add(jMenuItem19);
                        JMenuItem jMenuItem20 = new JMenuItem("Shift shift current value to zero.");
                        jMenuItem20.setActionCommand("setCurrentvalueToZero" + c0184j.a());
                        jMenuItem20.setName(c0184j.a());
                        jMenuItem20.addActionListener(new aC(this));
                        jMenu5.add(jMenuItem20);
                        jMenu4.add((JMenuItem) jMenu5);
                        JMenu jMenu6 = new JMenu("Index offsets");
                        C0590H c0590h = new C0590H();
                        c0590h.a(c0184j.A());
                        c0590h.a(new aD(this, c0184j));
                        jMenu6.add(c0590h);
                        jMenu6.add("Reset Index Offset").addActionListener(new aE(this, c0184j));
                        jMenu4.add((JMenuItem) jMenu6);
                        interfaceC1579bq.add(jMenu4);
                    }
                }
            }
        }
        return interfaceC1579bq;
    }

    public void a(String str, String str2) throws NumberFormatException {
        if (str2.equals("searchExpression:")) {
            g();
            l();
            return;
        }
        if (str2.startsWith("searchFor:")) {
            b(str, C1733k.a(str2, "searchFor:", ""));
            l();
            return;
        }
        if (str2.startsWith("searchGreaterThan:")) {
            String strA = C1733k.a(str2, "searchGreaterThan:", "");
            String strA2 = C1733k.a("{Search " + strA + " Greater Than}", true, "Search for log record greater than value.", true, (Component) this);
            if (strA2 != null && !strA2.equals("") && !c(strA, strA2)) {
                C1733k.a(strA + " = " + strA2 + " not found in current log file", this);
            }
            l();
            return;
        }
        if (str2.startsWith("searchLessThan:")) {
            String strA3 = C1733k.a(str2, "searchLessThan:", "");
            String strA4 = C1733k.a("{Search " + strA3 + " Less Than}", true, "Search for log record less than value.", true, (Component) this);
            if (strA4 != null && !strA4.equals("") && !d(strA3, strA4)) {
                C1733k.a(strA3 + " = " + strA4 + " not found in current log file", this);
            }
            l();
            return;
        }
        if (str2.startsWith("jumpTo:")) {
            this.f5244o.c(Integer.parseInt(C1733k.a(str2, "jumpTo:", "")));
            l();
            return;
        }
        if (str2.startsWith("userValue:")) {
            String strA5 = C1733k.a(str2, "userValue:", "");
            String strA6 = C1733k.a("{Search " + strA5 + "}", true, "Search for log record.", true, (Component) this);
            if (strA6 == null || strA6.equals("") || b(strA5, strA6)) {
                return;
            }
            C1733k.a(strA5 + " = " + strA6 + " not found in current log file", this);
            return;
        }
        if (str2.startsWith("shiftValueBy")) {
            String strA7 = C1733k.a(str2, "shiftValueBy", "");
            String strA8 = C1733k.a("{Amount to shift Logged Value of " + strA7 + "}", true, "Shift " + strA7 + " value by amount", true, (Component) this);
            if (strA8 == null || strA8.equals("")) {
                return;
            }
            C0184j c0184jA = this.f5244o.r().a(strA7);
            if (!bH.H.a(strA8)) {
                com.efiAnalytics.ui.bV.d("Value must be numeric", this);
                return;
            } else if (c0184jA == null) {
                com.efiAnalytics.ui.bV.d("Field \"" + strA7 + "\"not found!", this);
                return;
            } else {
                c0184jA.j(Float.parseFloat(strA8));
                repaint();
                return;
            }
        }
        if (str2.startsWith("setCurrentvalueToZero")) {
            String strA9 = C1733k.a(str2, "setCurrentvalueToZero", "");
            C0184j c0184jA2 = this.f5244o.r().a(strA9);
            if (c0184jA2 == null) {
                com.efiAnalytics.ui.bV.d("Field \"" + strA9 + "\"not found!", this);
                return;
            } else {
                c0184jA2.j(-c0184jA2.d(this.f5244o.p()));
                repaint();
                return;
            }
        }
        if (str2.startsWith("setTimeOffsetToCurrent:")) {
            t();
            return;
        }
        if (str2.startsWith("exportSelectedLog:")) {
            e();
            return;
        }
        if (str2.startsWith("cropToSelectedLog:")) {
            d();
            return;
        }
        if (str2.startsWith("deleteSelectedFromLog:")) {
            c();
        } else if (str2.startsWith("deleteBefore")) {
            E();
        } else if (str2.startsWith("deleteAfter")) {
            D();
        }
    }

    private void f(int i2, int i3) {
        this.f5244o.r().a(i2, i3);
        i();
        repaint();
        this.f5244o.b(this.f5244o.r());
    }

    private void D() {
        if (C1737b.a().a("fileEditing")) {
            f(this.f5244o.p() + 1, this.f5244o.r().d());
        }
    }

    private void E() {
        if (C1737b.a().a("fileEditing")) {
            f(0, this.f5244o.p());
        }
    }

    public void c() {
        if (!C1737b.a().a("fileEditing") || y() == null) {
            return;
        }
        f(y().b(), y().a());
    }

    public void d() {
        if (!C1737b.a().a("fileEditing") || y() == null) {
            return;
        }
        int iB = y().b();
        int iA = y().a();
        f(iA, this.f5244o.r().d());
        f(0, iB);
        a((C0812j) null);
        this.f5244o.c(iA - (iB / 2));
        i();
        repaint();
    }

    public void e() {
        if (!C1737b.a().a("fileEditing") || y() == null) {
            return;
        }
        int iB = y().b();
        int iA = y().a();
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR != null) {
            C0188n c0188n = new C0188n();
            Iterator it = c0188nR.iterator();
            while (it.hasNext()) {
                C0184j c0184j = (C0184j) it.next();
                C0184j c0184j2 = new C0184j();
                c0184j2.a(c0184j.a());
                c0184j2.e(c0184j.n());
                c0184j2.f(c0184j.p());
                c0184j2.e(c0184j.m());
                c0184j2.b(false);
                for (int i2 = iB; i2 <= iA; i2++) {
                    c0184j2.a(c0184j.c(i2));
                }
                c0188n.add(c0184j2);
            }
            if (c0188nR.h()) {
                for (String str : c0188nR.i()) {
                    c0188n.a(str, c0188nR.f(str));
                }
            }
            C0636b.a().a(c0188n, C0645bi.a().b().w(), C0645bi.a().b());
        }
    }

    @Override // ao.InterfaceC0763ft
    public void f() throws NumberFormatException {
        if (this.f5264D != null && this.f5264D.equals("searchGreaterThan:")) {
            c(this.f5265E, this.f5266F);
            return;
        }
        if (this.f5264D != null && this.f5264D.equals("searchLessThan:")) {
            d(this.f5265E, this.f5266F);
            return;
        }
        if (this.f5264D != null && this.f5264D.equals("searchExpression:")) {
            if (this.f5287T == null) {
                g();
                return;
            } else {
                a(this.f5287T);
                return;
            }
        }
        if (this.f5264D == null || this.f5265E == null) {
            a(200, 100);
        } else {
            b(this.f5265E, this.f5266F);
        }
    }

    public boolean g() {
        String strC = this.f5287T == null ? "" : this.f5287T.c();
        C0188n c0188nR = this.f5244o.r();
        do {
            strC = com.efiAnalytics.ui.bV.a((Component) this, false, "Search Expression", strC);
            if (strC == null || strC.length() == 0) {
                return false;
            }
            try {
                this.f5287T = new C1753a(strC);
                this.f5287T.a(c0188nR, 0);
            } catch (ax.U e2) {
                com.efiAnalytics.ui.bV.d("Invalid Expression:\n" + strC + "\n\n" + e2.getLocalizedMessage(), this);
                this.f5287T = null;
            }
        } while (this.f5287T == null);
        return a(this.f5287T);
    }

    public boolean a(C1753a c1753a) {
        this.f5264D = "searchExpression:";
        int i2 = this.f5246q + 1;
        C0188n c0188nR = this.f5244o.r();
        try {
            boolean z2 = c1753a.a(c0188nR, this.f5246q) != 0.0d;
            while (i2 < c0188nR.d()) {
                double dA = c1753a.a(c0188nR, i2);
                if (dA != 0.0d && !z2) {
                    this.f5244o.c(i2);
                    i();
                    repaint();
                    return true;
                }
                if (dA == 0.0d) {
                    z2 = false;
                }
                i2++;
                if (i2 == this.f5246q) {
                    com.efiAnalytics.ui.bV.d("No Results found.", this);
                    return false;
                }
                if (i2 == c0188nR.d()) {
                    i2 = 0;
                }
            }
            return true;
        } catch (ax.U e2) {
            com.efiAnalytics.ui.bV.d("Search Failed", this);
            e2.printStackTrace();
            return true;
        }
    }

    public boolean b(String str, String str2) throws NumberFormatException {
        C0184j c0184jC = c(str);
        if (c0184jC == null) {
            return false;
        }
        this.f5265E = str;
        this.f5266F = str2;
        this.f5264D = "userValue:";
        int i2 = this.f5246q + 1;
        float f2 = Float.parseFloat(str2);
        while (i2 < c0184jC.v()) {
            double dC = -1000000.0d;
            try {
                dC = c0184jC.c(i2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (f2 == dC) {
                this.f5244o.c(i2);
                l();
                return true;
            }
            if (i2 == this.f5246q) {
                return false;
            }
            i2 = i2 == c0184jC.v() - 1 ? 0 : i2 + 1;
        }
        return false;
    }

    public boolean c(String str, String str2) throws NumberFormatException {
        C0184j c0184jC = c(str);
        if (c0184jC == null) {
            return false;
        }
        this.f5265E = str;
        this.f5266F = str2;
        this.f5264D = "searchGreaterThan:";
        int i2 = this.f5246q + 1;
        float f2 = Float.parseFloat(str2);
        boolean z2 = f2 < c0184jC.c(this.f5246q);
        while (i2 < c0184jC.v()) {
            double dC = -1000000.0d;
            try {
                dC = c0184jC.c(i2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (!z2 && dC > f2) {
                this.f5244o.c(i2);
                l();
                return true;
            }
            if (z2 && dC <= f2) {
                z2 = false;
            }
            if (i2 == this.f5246q - 1) {
                return false;
            }
            i2 = i2 == c0184jC.v() - 1 ? 0 : i2 + 1;
        }
        return false;
    }

    public boolean d(String str, String str2) throws NumberFormatException {
        C0184j c0184jC = c(str);
        if (c0184jC == null) {
            return false;
        }
        this.f5265E = str;
        this.f5266F = str2;
        this.f5264D = "searchLessThan:";
        int i2 = this.f5246q + 1;
        float f2 = Float.parseFloat(str2);
        boolean z2 = f2 > c0184jC.c(this.f5246q);
        while (i2 < c0184jC.v()) {
            double dC = -1000000.0d;
            try {
                dC = c0184jC.c(i2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (!z2 && dC < f2) {
                this.f5244o.c(i2);
                l();
                return true;
            }
            if (z2 && dC >= f2) {
                z2 = false;
            }
            if (i2 == this.f5246q - 1) {
                return false;
            }
            i2 = i2 == c0184jC.v() - 1 ? 0 : i2 + 1;
        }
        return false;
    }

    private C0184j c(String str) {
        if (this.f5239k == null) {
            return null;
        }
        for (Object obj : C1733k.b(this.f5239k.keySet().toArray())) {
            for (C0184j c0184j : (List) this.f5239k.get(obj)) {
                if (c0184j != null && c0184j.a() != null && c0184j.a().equals(str)) {
                    return c0184j;
                }
            }
        }
        return null;
    }

    public void a(Image image) {
        this.f5229a = image;
    }

    public void c(int i2) {
        this.f5270H.clear();
        this.f5270H.add(new BasicStroke(i2));
        float f2 = i2 * 2;
        float f3 = i2 * 3;
        float f4 = i2 * 4;
        float f5 = i2 * 5;
        float f6 = i2 * 7;
        this.f5270H.add(new BasicStroke(i2, 2, 0, 10.0f, new float[]{f3, f3, f3}, 0.0f));
        this.f5270H.add(new BasicStroke(i2, 2, 0, 10.0f, new float[]{f6, f2, i2, f2}, 0.0f));
        this.f5270H.add(new BasicStroke(i2, 2, 0, 10.0f, new float[]{f6, f2, i2, f2, i2, f2}, 0.0f));
        float[] fArr = {f6, f2, i2, f2, i2, f2, i2, f2};
        this.f5270H.add(new BasicStroke(i2, 2, 0, 10.0f, fArr, 0.0f));
        float[] fArr2 = {f6, f2, i2, f2, i2, f2, i2, f2, i2, f2};
        this.f5270H.add(new BasicStroke(i2, 2, 0, 10.0f, fArr, 0.0f));
        this.f5273K = true;
    }

    private Stroke k(int i2) {
        if (((int) (getWidth() / this.f5248s)) + 1 > this.f5272J || this.f5270H.isEmpty()) {
            return this.f5271I;
        }
        if (f5269af) {
            return (Stroke) this.f5270H.get(i2 % this.f5270H.size());
        }
        if (this.f5273K) {
            return (Stroke) this.f5270H.get(0);
        }
        c(1);
        return (Stroke) this.f5270H.get(0);
    }

    public void b(boolean z2) {
        this.f5275M = z2;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void update(Graphics graphics) {
        if (this.f5255w) {
            return;
        }
        if (this.f5239k.isEmpty()) {
            g(graphics);
            return;
        }
        Image imageH = H();
        if (this.f5231c == null || this.f5231c.getHeight(null) != getHeight()) {
            this.f5231c = createImage(this.f5251u + 2, getHeight());
        }
        Graphics graphics2 = this.f5231c.getGraphics();
        if (this.f5250t == -1) {
            this.f5250t = (int) ((this.f5246q - A()) * this.f5248s);
        }
        if (getWidth() - this.f5250t < this.f5251u) {
            graphics2.drawImage(imageH, (0 - this.f5250t) + this.f5251u, 0, null);
            graphics.drawImage(this.f5231c, this.f5250t - this.f5251u, 0, null);
        } else {
            graphics2.drawImage(imageH, 0 - this.f5250t, 0, null);
            graphics.drawImage(this.f5231c, this.f5250t, 0, null);
        }
        c(graphics);
    }

    public void a(Graphics graphics) {
        int height;
        if (this.f5239k.size() <= 0 || (height = (getHeight() - (((getHeight() - f5260B) / this.f5239k.size()) * this.f5239k.size())) - f5260B) <= 0) {
            return;
        }
        graphics.setColor(getBackground());
        graphics.fillRect(0, (getHeight() - f5260B) - height, getWidth(), height);
    }

    @Override // java.awt.Component
    public void paintAll(Graphics graphics) {
        System.out.println("paintAll");
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        System.out.println("paintComponent");
    }

    @Override // java.awt.Container
    public void paintComponents(Graphics graphics) {
        System.out.println("paintComponents");
    }

    @Override // javax.swing.JComponent
    public void paintImmediately(Rectangle rectangle) {
        System.out.println("paintImmediately");
    }

    private Image F() {
        if (this.f5235g == null || this.f5235g.getWidth(null) != getWidth() || this.f5235g.getHeight(null) != getHeight()) {
            this.f5235g = createImage(getWidth(), getHeight());
        }
        return this.f5235g;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f5293W) {
            Image imageF = F();
            b(imageF.getGraphics());
            graphics.drawImage(imageF, 0, 0, null);
        } else {
            try {
                b(graphics);
            } catch (Exception e2) {
                bH.C.c("Graph paint error");
                e2.printStackTrace();
            }
        }
    }

    public void b(Graphics graphics) {
        if (getWidth() < 0 || getHeight() < 0) {
            return;
        }
        if (this.f5239k.isEmpty()) {
            g(graphics);
            return;
        }
        if (this.f5255w) {
            return;
        }
        Image imageH = H();
        if (imageH != null) {
            graphics.drawImage(imageH, 0, 0, null);
            if (this.f5230b == null || this.f5254v) {
            }
        }
        c(graphics);
        h(graphics);
        if (this.f5281ak != null) {
            a(graphics, this.f5281ak.b(), this.f5281ak.a());
        }
    }

    private void c(Graphics graphics) {
        long jNanoTime = System.nanoTime();
        if (this.f5274L) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        long jNanoTime2 = System.nanoTime() - jNanoTime;
        int iA = (int) ((this.f5246q - A()) * this.f5248s);
        if (iA > getWidth() || iA < 0) {
            n();
            i();
            repaint();
        }
        this.f5250t = iA;
        long jNanoTime3 = System.nanoTime() - jNanoTime;
        Object[] objArrB = C1733k.b(this.f5239k.keySet().toArray());
        int length = objArrB.length;
        int size = getFont().getSize();
        long jNanoTime4 = System.nanoTime() - jNanoTime;
        graphics.setColor(getForeground());
        graphics.drawLine(iA, 0, iA, getHeight());
        graphics.setFont(new Font(getFont().getName(), 1, getFont().getSize()));
        if (v()) {
            this.f5251u = f5253ab;
        } else {
            this.f5251u = f5252aa;
        }
        int i2 = getWidth() - iA < this.f5251u ? iA - this.f5251u : iA + 3;
        long jNanoTime5 = System.nanoTime() - jNanoTime;
        for (int i3 = 0; i3 < objArrB.length; i3++) {
            int i4 = 0;
            int i5 = 0;
            for (C0184j c0184j : (List) this.f5239k.get(objArrB[i3])) {
                try {
                    if (c0184j.a() != null) {
                        int height = (getHeight() - f5260B) / length;
                        int iA2 = (height + (i3 * height)) - com.efiAnalytics.ui.eJ.a(5);
                        String strB = b(c0184j.a(this.f5246q), 12);
                        if (v() && this.f5240l.a(c0184j.a()) != null && this.f5246q - this.f5247r > 0 && this.f5246q - this.f5247r < this.f5240l.d()) {
                            strB = strB + " : " + this.f5240l.a(c0184j.a()).a(this.f5246q - this.f5247r);
                        }
                        graphics.setColor(getBackground());
                        int iStringWidth = i2 > iA ? i2 : (iA - getFontMetrics(getFont()).stringWidth(strB)) - com.efiAnalytics.ui.eJ.a(3);
                        String strG = G();
                        int iE = e(objArrB[i3].toString());
                        int i6 = iE == -1 ? i3 : iE;
                        String strA = c0184j.a();
                        if (c0184j.r()) {
                            strA = strA + "(s" + c0184j.s() + ")";
                        }
                        if (strG.equals(JSplitPane.TOP)) {
                            int i7 = height + ((i3 - 1) * height);
                            graphics.fillRect(iStringWidth, i7 + ((i4 - i5) * size) + 2, getFontMetrics(getFont()).stringWidth(strB), size);
                            graphics.setColor(b(i6, i4));
                            graphics.drawString(strB, iStringWidth, i7 + (((1 + i4) - i5) * size));
                        } else if (strG.equals("withLabels")) {
                            int i8 = height + ((i3 - 1) * height);
                            if ((i4 - i5) * size < height) {
                                int iD = (d(strA) - getFontMetrics(getFont()).stringWidth(strB)) - com.efiAnalytics.ui.eJ.a(8);
                                graphics.fillRect(iD, i8 + ((i4 - i5) * size) + 2, getFontMetrics(getFont()).stringWidth(strB), size);
                                graphics.setColor(b(i6, i4));
                                graphics.drawString(strB, iD, i8 + (((1 + i4) - i5) * size));
                            }
                        } else {
                            graphics.fillRect(iStringWidth, ((iA2 - ((i4 - i5) * size)) - size) + 2, getFontMetrics(getFont()).stringWidth(strB), size);
                            graphics.setColor(b(i6, i4));
                            graphics.drawString(strB, iStringWidth, iA2 - ((i4 - i5) * size));
                        }
                    } else {
                        i5++;
                    }
                } catch (Exception e2) {
                    Logger.getLogger(C0625ap.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    e2.printStackTrace();
                }
                i4++;
            }
        }
        long jNanoTime6 = System.nanoTime() - jNanoTime;
        if (h() != null) {
            float fC = this.f5246q >= 0 ? h().c(this.f5246q) : -h().c(-this.f5246q);
            String strC = (h().n() == null || h().n().length() <= 0) ? bH.W.c(fC, 3) : bH.W.c(fC, 3) + h().n();
            if (v() && this.f5240l.a(h().a()) != null && this.f5246q - this.f5247r > 0 && this.f5246q - this.f5247r < this.f5240l.d()) {
                strC = strC + " : " + this.f5240l.a(h().a()).a(this.f5246q - this.f5247r);
            }
            graphics.setColor(getBackground());
            int iStringWidth2 = i2 > iA ? i2 : (iA - getFontMetrics(getFont()).stringWidth(strC)) - com.efiAnalytics.ui.eJ.a(3);
            graphics.fillRect(iStringWidth2, (getHeight() - size) - 7, getFontMetrics(getFont()).stringWidth(strC), size);
            graphics.setColor(getForeground());
            graphics.drawString(strC, iStringWidth2, getHeight() - com.efiAnalytics.ui.eJ.a(8));
        }
    }

    public C0184j h() {
        return (this.f5291V == null || this.f5244o.r() == null || this.f5244o.r().a(this.f5291V) == null) ? this.f5243Y : this.f5244o.r().a(this.f5291V);
    }

    public void b(String str) {
        this.f5291V = str;
    }

    private String G() {
        return h.i.e(h.i.f12298S, h.i.f12299T);
    }

    private Image H() {
        if (!this.f5254v && this.f5230b != null) {
            return this.f5230b;
        }
        if (this.f5230b == null || this.f5230b.getWidth(null) != getWidth() || this.f5230b.getHeight(null) != getHeight()) {
            this.f5230b = createImage(getWidth(), getHeight());
        }
        d(this.f5230b.getGraphics());
        return this.f5230b;
    }

    private int I() {
        if (A() > 0) {
            return 0;
        }
        if (this.f5294X < 0) {
            this.f5294X = J();
        }
        return this.f5294X;
    }

    private int J() {
        int i2 = 0;
        if (h.i.a(h.i.f12290K, true)) {
            for (List<C0184j> list : this.f5239k.values()) {
                if (list != null) {
                    for (C0184j c0184j : list) {
                        FontMetrics fontMetrics = getFontMetrics(new Font(getFont().getName(), 1, getFont().getSize()));
                        String str = "Max = " + c0184j.d();
                        String str2 = "Min = " + c0184j.b();
                        String strN = c0184j.n();
                        if (strN != null && !strN.isEmpty()) {
                            str = str + " (" + strN + ")";
                            str2 = str2 + " (" + strN + ")";
                        }
                        int iStringWidth = fontMetrics.stringWidth(str);
                        if (iStringWidth > i2) {
                            i2 = iStringWidth;
                        }
                        int iStringWidth2 = fontMetrics.stringWidth(str2);
                        if (iStringWidth2 > i2) {
                            i2 = iStringWidth2;
                        }
                    }
                }
            }
            i2 += 10;
            if (this.f5248s > 1.0d) {
                int iRound = (int) Math.round(this.f5248s);
                int i3 = 0;
                do {
                    i3 += iRound;
                } while (i3 < i2);
                i2 = i3;
            }
        }
        return i2;
    }

    private void d(Graphics graphics) {
        if (h() != null) {
            this.f5245p = h().i();
        }
        Object[] objArrB = C1733k.b(this.f5239k.keySet().toArray());
        this.f5263C.clear();
        for (int i2 = 0; i2 < objArrB.length; i2++) {
            int width = getWidth();
            int height = (getHeight() - f5260B) / objArrB.length;
            int i3 = 0;
            if (this.f5232d == null || this.f5232d.getWidth(null) != width || this.f5232d.getHeight(null) != height) {
                if (width <= 0 || height <= 0) {
                    return;
                } else {
                    this.f5232d = createImage(width, height);
                }
            }
            int iE = e(objArrB[i2].toString());
            int i4 = iE == -1 ? i2 : iE;
            List<C0184j> list = (List) this.f5239k.get(objArrB[i2]);
            for (C0184j c0184j : list) {
                C0184j c0184jA = null;
                if (this.f5256x && this.f5240l != null && this.f5240l.a(c0184j.a()) != null) {
                    c0184jA = this.f5240l.a(c0184j.a());
                }
                this.f5232d = a(c0184j, this.f5232d, i3, b(i4, i3), c0184jA);
                i3++;
            }
            int i5 = 0;
            int i6 = 0;
            for (C0184j c0184j2 : list) {
                C0184j c0184jA2 = null;
                if (this.f5256x && this.f5240l != null && this.f5240l.a(c0184j2.a()) != null) {
                    c0184jA2 = this.f5240l.a(c0184j2.a());
                }
                if (c0184j2 == null || c0184j2.a() == null || c0184j2.a().isEmpty()) {
                    i6++;
                } else {
                    this.f5232d = a(c0184j2, this.f5232d, i2, i5 - i6, b(i4, i5), c0184jA2);
                }
                i5++;
            }
            graphics.drawImage(this.f5232d, 0, i2 * height, null);
        }
        a(graphics);
        graphics.drawImage(b(h()), 0, getHeight() - f5260B, null);
        if (this.f5241m != null) {
            e(graphics);
        }
        if (this.f5242n != null) {
            f(graphics);
        }
        d((int) (getWidth() / this.f5248s));
        this.f5254v = false;
    }

    private Graphics e(Graphics graphics) {
        if (this.f5241m != null) {
            int i2 = 0;
            Iterator it = this.f5241m.keySet().iterator();
            while (it.hasNext()) {
                int iIntValue = ((Integer) it.next()).intValue();
                if (iIntValue > A() && iIntValue < A() + (getWidth() / this.f5248s)) {
                    i2++;
                }
            }
            if (i2 < getWidth() / com.efiAnalytics.ui.eJ.a(10)) {
                Iterator it2 = this.f5241m.keySet().iterator();
                while (it2.hasNext()) {
                    int iIntValue2 = ((Integer) it2.next()).intValue();
                    if (iIntValue2 > A() && iIntValue2 < A() + (getWidth() / this.f5248s)) {
                        graphics.setColor(Color.red);
                        int iA = (int) ((iIntValue2 - A()) * this.f5248s);
                        graphics.drawLine(iA, 0, iA, getHeight());
                    }
                }
            }
        }
        return graphics;
    }

    public void c(boolean z2) {
        this.f5257y = z2;
    }

    private Graphics f(Graphics graphics) {
        HashMap mapC = this.f5240l == null ? null : this.f5240l.c();
        if (mapC != null && this.f5257y) {
            Iterator it = mapC.keySet().iterator();
            while (it.hasNext()) {
                int iIntValue = ((Integer) it.next()).intValue() + this.f5247r;
                if (iIntValue > A() && iIntValue < A() + (getWidth() / this.f5248s)) {
                    graphics.setColor(Color.yellow.darker());
                    int iA = (int) ((iIntValue - A()) * this.f5248s);
                    graphics.drawLine(iA, 0, iA, getHeight());
                }
            }
        }
        if (this.f5242n != null && this.f5257y) {
            Iterator it2 = this.f5242n.keySet().iterator();
            while (it2.hasNext()) {
                int iIntValue2 = ((Integer) it2.next()).intValue();
                if (iIntValue2 > A() && iIntValue2 < A() + (getWidth() / this.f5248s)) {
                    graphics.setColor(Color.yellow);
                    int iA2 = (int) ((iIntValue2 - A()) * this.f5248s);
                    graphics.drawLine(iA2, 0, iA2, getHeight());
                }
            }
        }
        return graphics;
    }

    private Image b(C0184j c0184j) {
        if (this.f5233e == null || this.f5233e.getWidth(null) != getWidth()) {
            this.f5233e = createImage(getWidth(), f5260B);
        }
        if (c0184j == null) {
            return this.f5233e;
        }
        Graphics graphics = this.f5233e.getGraphics();
        graphics.setColor(getBackground());
        graphics.setFont(getFont());
        graphics.fillRect(0, 0, this.f5233e.getWidth(null), this.f5233e.getHeight(null));
        if (this.f5274L) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        graphics.setColor(getForeground());
        int i2 = f5260B - 1;
        int i3 = i2 - (f5260B / 2);
        int i4 = i2 - (f5260B / 4);
        graphics.drawLine(0, i2, getWidth() - 3, i2);
        int width = getWidth() - 2;
        for (int i5 = 0; i5 <= 4; i5++) {
            int i6 = (i5 * width) / 4;
            graphics.drawLine(i6, i3, i6, i2);
            graphics.drawLine(i6 + 1, i3, i6 + 1, i2);
        }
        for (int i7 = 0; i7 <= 12; i7++) {
            int i8 = (i7 * width) / 12;
            graphics.drawLine(i8, i4, i8, i2);
        }
        String strB = bH.W.b(A() >= 0 ? c0184j.c(A()) : -c0184j.c(-A()), 3);
        if (c0184j.n() != null && !c0184j.n().isEmpty()) {
            strB = strB + c0184j.n();
        }
        graphics.drawString(strB, com.efiAnalytics.ui.eJ.a(3), getFont().getSize());
        int iA = (int) (A() + (getWidth() / this.f5248s));
        if (iA > c0184j.i()) {
            float fD = (c0184j.d(c0184j.i() - 1) - c0184j.c(A())) * (iA / c0184j.i());
        } else {
            if (iA == c0184j.i()) {
                iA = c0184j.i() - 1;
            }
            String strB2 = bH.W.b(c0184j.c(iA), 3);
            if (c0184j.n() != null && !c0184j.n().isEmpty()) {
                strB2 = strB2 + c0184j.n();
            }
            graphics.drawString(strB2, (width - getFontMetrics(getFont()).stringWidth(strB2)) - 3, getFont().getSize());
        }
        if (C1737b.a().a("4e345hggsdhd4812hfd43-0gdk")) {
            String strA = c0184j.a();
            int iStringWidth = graphics.getFontMetrics().stringWidth(strA);
            int width2 = ((getWidth() / 2) - iStringWidth) - com.efiAnalytics.ui.eJ.a(15);
            graphics.drawString(strA, width2, graphics.getFont().getSize() + 2);
            Rectangle rectangle = new Rectangle(width2, getHeight() - f5260B, iStringWidth, f5260B);
            aP aPVar = new aP();
            aPVar.a(rectangle);
            aPVar.a(c0184j);
            aPVar.a(aP.f5139b);
            this.f5263C.add(aPVar);
        }
        return this.f5233e;
    }

    private Image a(C0184j c0184j, Image image, int i2, Color color, C0184j c0184j2) {
        double dC;
        long jNanoTime = System.nanoTime();
        Graphics graphics = image.getGraphics();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int width2 = A() >= 0 ? ((int) (getWidth() / this.f5248s)) + 1 : ((int) (getWidth() / this.f5248s)) + 1 + A();
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (this.f5274L) {
        }
        if (this.f5275M && width2 < this.f5272J) {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        graphics2D.setStroke(k(i2));
        if (i2 == 0) {
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, width - 1, height - 1);
            graphics.setColor(Color.lightGray);
            if (h.i.a("showGraphHalfMark", false)) {
                int i3 = 0;
                while (true) {
                    int i4 = i3;
                    if (i4 >= width) {
                        break;
                    }
                    graphics.drawLine(i4, height / 2, i4 + 3, height / 2);
                    i3 = i4 + 3 + 10;
                }
                graphics.setFont(new Font(getFont().getName(), 0, getFont().getSize() - com.efiAnalytics.ui.eJ.a(2)));
                graphics.drawString("50%", 3, (height / 2) - 1);
            }
            graphics.drawRect(0, 0, width - 1, height - 1);
        }
        graphics.setColor(color);
        if (c0184j.i() - A() < width2 && A() >= 0) {
            width2 = c0184j.i() - A();
        } else if (c0184j.i() - A() < width2) {
            width2 = c0184j.i();
        }
        if (c0184j.a() == null || c0184j.a().equals("") || width2 < 0) {
            return image;
        }
        int[] iArr = new int[width2];
        int[] iArr2 = new int[width2];
        double dE = c0184j.e();
        double dF = c0184j.f();
        boolean z2 = A() < 0;
        int i5 = 0;
        while (true) {
            int i6 = i5;
            if (i6 >= width2) {
                break;
            }
            iArr[i6 / 1] = !z2 ? ((int) (i6 * this.f5248s)) * 1 : (((int) (i6 * this.f5248s)) * 1) + I();
            try {
                dC = c0184j.c(i6 + (!z2 ? A() : 0));
            } catch (Exception e2) {
                dC = Double.NaN;
                e2.printStackTrace();
            }
            iArr2[i6 / 1] = (int) ((dF == dE ? 0.5d : 1.0d - ((dC - dE) / (dF - dE))) * height);
            i5 = i6 + 1;
        }
        graphics.drawPolyline(iArr, iArr2, width2);
        if (c0184j2 != null && v()) {
            int iA = A() - this.f5247r;
            for (int i7 = 0; i7 < width2; i7++) {
                iArr[i7] = (int) (i7 * this.f5248s);
                iArr2[i7] = (int) ((1.0d - ((((i7 + iA < 0 || i7 + iA >= c0184j2.v()) ? 0.0d : c0184j2.c(i7 + iA)) - dE) / (dF - dE))) * height);
            }
            graphics.setColor(graphics.getColor().darker().darker());
            graphics.drawPolyline(iArr, iArr2, width2);
        }
        if ((System.nanoTime() - jNanoTime) / 1000000.0d > 100.0d && width2 < this.f5272J) {
            this.f5272J /= 2;
            bH.C.d("Downgraded maxRecordForFancyLines to " + this.f5272J);
        }
        return image;
    }

    private Image a(C0184j c0184j, Image image, int i2, int i3, Color color, C0184j c0184j2) {
        Graphics graphics = image.getGraphics();
        if (this.f5274L) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setStroke(k(i3));
        }
        graphics.setFont(new Font(getFont().getName(), 1, getFont().getSize()));
        this.f5282O = getFont().getSize();
        image.getWidth(null);
        int height = image.getHeight(null);
        String str = "Max = " + c0184j.d();
        String str2 = "Min = " + c0184j.b();
        String strN = c0184j.n();
        if (strN != null && !strN.isEmpty()) {
            str = str + " (" + strN + ")";
            str2 = str2 + " (" + strN + ")";
        }
        if (c0184j.a() != null) {
            if (h.i.a(h.i.f12290K, true)) {
                graphics.setColor(getBackground());
                String strA = c0184j.a();
                if (c0184j.l()) {
                    strA = strA + f5290am;
                }
                if (c0184j.r()) {
                    strA = strA + "(s" + c0184j.s() + ")";
                }
                if (h.i.a(h.i.f12291L, h.i.f12292M)) {
                    graphics.fillRect(5, (i3 * this.f5282O) + 1, getFontMetrics(getFont()).stringWidth(str), this.f5282O);
                    graphics.fillRect(5, ((height - this.f5282O) - 5) - (i3 * this.f5282O), getFontMetrics(getFont()).stringWidth(str2), this.f5282O);
                    graphics.setColor(color);
                    graphics.drawString(str, 5, this.f5282O + (i3 * this.f5282O));
                    graphics.drawString(str2, 5, (height - 5) - (i3 * this.f5282O));
                }
                int iD = d(strA);
                Rectangle rectangle = new Rectangle(iD, (i2 * image.getHeight(null)) + (i3 * this.f5282O) + 1, getFontMetrics(getFont()).stringWidth(strA), this.f5282O);
                aP aPVar = new aP();
                aPVar.a(rectangle);
                aPVar.a(c0184j);
                this.f5263C.add(aPVar);
                graphics.setColor(getBackground());
                graphics.fillRect(rectangle.f12372x, (i3 * this.f5282O) + 1, rectangle.width, rectangle.height);
                graphics.setColor(color);
                graphics.drawString(strA, iD, this.f5282O + (i3 * this.f5282O));
            }
            if (h.i.a("showGraphHalfMark", false)) {
                graphics.setFont(new Font(getFont().getName(), 0, getFont().getSize() - com.efiAnalytics.ui.eJ.a(2)));
                graphics.drawString(a((c0184j.f() + c0184j.e()) / 2.0f, c0184j.m()), 28 + (i3 * 35), (height / 2) - 1);
            }
        }
        return image;
    }

    private int d(String str) {
        int iStringWidth = getFontMetrics(getFont()).stringWidth(str) + com.efiAnalytics.ui.eJ.a(10);
        return iStringWidth > com.efiAnalytics.ui.eJ.a(100) ? (getWidth() - iStringWidth) - com.efiAnalytics.ui.eJ.a(10) : getWidth() - com.efiAnalytics.ui.eJ.a(100);
    }

    private String a(float f2, int i2) {
        return a(f2 + "", i2);
    }

    private String a(String str, int i2) {
        return i2 >= 0 ? bH.W.a(str, i2) : str.length() > str.indexOf(".") + 4 ? str.substring(0, str.indexOf(".") + 4) : str;
    }

    private String b(String str, int i2) {
        if (str.length() > i2) {
            str = str.substring(0, i2 - 1);
        }
        return str;
    }

    private void g(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        if (this.f5238j != null) {
            graphics.setColor(this.f5238j);
            graphics.fillRect(0, 0, width, height);
        }
        if (this.f5288al) {
            int iA = com.efiAnalytics.ui.eJ.a(this.f5229a.getWidth(null));
            int iA2 = com.efiAnalytics.ui.eJ.a(this.f5229a.getHeight(null));
            double d2 = (width * 0.9d) / iA;
            double d3 = (height * 0.8d) / iA2;
            if (d3 < d2) {
                d2 = d3;
            }
            if (d2 > 1.0d) {
                d2 = 1.0d;
            }
            int i2 = (int) (iA * d2);
            int i3 = (int) (iA2 * d2);
            graphics.drawImage(this.f5229a, (width - i2) / 2, (height - i3) / 2, i2, i3, this);
            graphics.setColor(UIManager.getColor("Label.foreground"));
            if (this.f5268ae != null) {
                graphics.setFont(getFont());
                graphics.drawString(this.f5268ae, 30, getHeight() - 10);
            }
        }
    }

    public void i() {
        this.f5254v = true;
    }

    public boolean j() {
        return this.f5289U;
    }

    public void k() {
        this.f5289U = false;
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        if (i2 != 32) {
            return true;
        }
        repaint(500L);
        return true;
    }

    @Override // ao.InterfaceC0640bd
    public void a(String str, C0184j c0184j, int i2) {
        if (c0184j == null) {
            a(str, "", i2);
            return;
        }
        List copyOnWriteArrayList = (List) this.f5239k.get(str);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList();
        }
        if (copyOnWriteArrayList.size() <= i2 || copyOnWriteArrayList.get(i2) == null) {
            a(copyOnWriteArrayList, i2);
            copyOnWriteArrayList.add(i2, c0184j);
        } else {
            copyOnWriteArrayList.set(i2, c0184j);
        }
        this.f5239k.put(str, copyOnWriteArrayList);
        this.f5289U = true;
        i();
        l();
    }

    public void l() {
        if (this.f5261ac == null || this.f5261ac.b()) {
            this.f5261ac = new C0588F(this, 150L);
        } else {
            this.f5261ac.a();
        }
    }

    public boolean m() {
        return this.f5279ai;
    }

    public void e(boolean z2) {
        this.f5279ai = z2;
    }

    private List a(List list, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (list.size() < i3 + 1 || list.get(i3) == null) {
                list.add(new C0184j());
            }
        }
        return list;
    }

    protected void n() {
        this.f5294X = -1;
        if ((this.f5279ai && this.f5246q - (getWidth() / (2.0d * this.f5248s)) > 0.0d) || this.f5246q - (getWidth() / this.f5248s) > 0.0d) {
            this.f5249Z = (int) (this.f5246q - (getWidth() / (2.0d * this.f5248s)));
        } else if (this.f5280aj && h.i.a(h.i.f12291L, true)) {
            this.f5249Z = (int) Math.round((-I()) / this.f5248s);
        } else {
            this.f5249Z = 0;
        }
        this.f5250t = (int) ((this.f5246q - A()) * this.f5248s);
        a((C0812j) null);
    }

    public int o() {
        int width = (int) (getWidth() / (2.0d * this.f5248s));
        if (this.f5244o.r() != null) {
            return width <= this.f5244o.r().d() ? width : this.f5244o.r().d();
        }
        return 0;
    }

    public boolean a(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            if (c0184j.a() != null && !c0184j.a().equals("")) {
                return false;
            }
        }
        return true;
    }

    @Override // ao.InterfaceC0640bd
    public void a(String str, String str2, int i2) {
        List list = (List) this.f5239k.get(str);
        if (list != null && i2 < list.size()) {
            list.set(i2, new C0184j());
        }
        if (a(list)) {
            this.f5239k.remove(str);
        }
        this.f5289U = true;
        i();
        l();
    }

    public void p() {
        this.f5239k.clear();
        this.f5249Z = 0;
        i();
        repaint(500L);
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        this.f5246q = i2;
        a((C0812j) null);
        if (this.f5279ai) {
            q();
        } else {
            repaint();
        }
    }

    @Override // ao.InterfaceC0758fo
    public void b(double d2) {
        if (this.f5248s != d2) {
            this.f5248s = d2;
            n();
        }
        i();
        repaint();
    }

    public void q() {
        n();
        i();
        repaint();
    }

    @Override // java.awt.Container, java.awt.Component
    public void validate() {
        i();
        super.validate();
    }

    protected void d(int i2) {
        Iterator it = this.f5258z.iterator();
        while (it.hasNext()) {
            InterfaceC0747fd interfaceC0747fd = (InterfaceC0747fd) it.next();
            if (interfaceC0747fd != null) {
                interfaceC0747fd.a(i2);
            }
        }
    }

    public void a(InterfaceC0747fd interfaceC0747fd) {
        this.f5258z.add(interfaceC0747fd);
    }

    public void b(Color color) {
        i();
        repaint();
        super.setForeground(getForeground());
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color) {
        i();
        super.setBackground(color);
        repaint();
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color, int i2) {
        i();
        repaint();
    }

    public Color b(int i2, int i3) {
        return aO.a().a(i2, i3);
    }

    private int e(String str) {
        try {
            return Integer.parseInt(str.substring(5));
        } catch (Exception e2) {
            return -1;
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        this.f5237i = color;
        super.setBackground(color);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setForeground(Color color) {
        super.setForeground(color);
    }

    public int e(int i2) {
        int iA;
        if (h() == null) {
            iA = (int) (A() + (i2 / this.f5248s));
        } else {
            iA = (int) (((double) A()) + (((double) i2) / this.f5248s) < ((double) h().i()) ? A() + (i2 / this.f5248s) : h().i() - 1);
        }
        if (iA < 0) {
            iA = 0;
        }
        return iA;
    }

    private int l(int i2) {
        return (int) Math.round((i2 - A()) * this.f5248s);
    }

    public void a(HashMap map) {
        this.f5241m = map;
    }

    public void b(HashMap map) {
        this.f5242n = map;
    }

    /* JADX WARN: Code restructure failed: missing block: B:98:0x0268, code lost:
    
        ao.C0645bi.a().g().c(r13 - r15);
     */
    @Override // i.InterfaceC1742b
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(double r8) {
        /*
            Method dump skipped, instructions count: 661
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ao.C0625ap.a(double):void");
    }

    @Override // i.InterfaceC1742b
    public void b(C0188n c0188n) {
        this.f5240l = c0188n;
        repaint();
    }

    @Override // i.InterfaceC1742b
    public void a() {
    }

    private String K() {
        String strE = h.i.e(h.i.f12339aF, f5285R);
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR == null || c0188nR.size() == 0) {
            return null;
        }
        if (strE.equals(f5285R)) {
            strE = c0188nR.a("Launch timer") != null ? "[Launch timer] < 2 && [Launch timer] < [Launch timer-1] && [TPS] > 70 && [TPS-1] > 70" : c0188nR.a("Launch retard timer") != null ? "[Launch retard timer] < 2 && [Launch retard timer] < [Launch retard timer-1] && [TPS] > 70 && [TPS-1] > 70" : (c0188nR.a("Time after launch") == null || c0188nR.a("Throttle position") == null) ? c0188nR.a("Tmr_Enable") != null ? "[Tmr_Enable] == 1 && [Tmr_Enable-1] == 0" : (c0188nR.a("TWO_STEP_MODE") == null || c0188nR.a("TWO_STEP_MODE") == null) ? f5286S : "[TWO_STEP_MODE] == 0 && [THREE_STEP_MODE] == 0 &&([TWO_STEP_MODE-1] == 1 || [THREE_STEP_MODE-1] == 1)" : "[Time after launch] > 0 && [Throttle position] > 80";
        }
        return strE;
    }

    @Override // i.InterfaceC1742b
    public void a(C0188n c0188n) {
        if (c0188n != null) {
            bH.C.c("DataSet Set!! size: " + c0188n.size());
        }
        if (C() == null) {
            this.f5243Y = c0188n.a(h.g.a().a("Time"));
        }
        if (C() != null) {
        }
        if (c0188n != null && !c0188n.isEmpty()) {
            setBackground(h.i.a("graphBackColor", Color.black));
            setForeground(h.i.a("graphForeColor", Color.CYAN));
        } else {
            this.f5239k.clear();
            this.f5243Y = null;
            i();
            repaint(500L);
        }
    }

    public void r() {
        String strA = com.efiAnalytics.ui.bV.a((Component) this, false, "MARK Label ", "");
        if (strA == null || strA.length() <= 0) {
            return;
        }
        this.f5241m.put(Integer.valueOf(this.f5246q), "MARK: " + strA);
        repaint();
    }

    public void s() {
        this.f5241m.remove(Integer.valueOf(this.f5246q));
    }

    public void t() {
        if (C() != null && (h() == null || C().equals(h()))) {
            f(this.f5246q);
        } else if (h() != null) {
            C0184j c0184jH = h();
            c0184jH.j(0.0f);
            c0184jH.j(-c0184jH.c(this.f5244o.p()));
        }
    }

    public void f(int i2) {
        if (C() != null) {
            C().j(0.0f);
            C().j(-this.f5243Y.c(i2));
            if (C1737b.a().a("timeslipData") && this.f5283P != null) {
                this.f5283P.a(i2);
            }
            if (this.f5240l != null && this.f5240l.a(h.g.a().a("Time")) != null) {
                C0184j c0184jA = this.f5240l.a(h.g.a().a("Time"));
                int i3 = i2 - this.f5247r;
                if (i3 >= 0 && i3 < c0184jA.i()) {
                    c0184jA.j(0.0f);
                    c0184jA.j(-c0184jA.c(i3));
                }
            }
            if (C1737b.a().a("timeslipData") && this.f5284Q != null) {
                this.f5284Q.a(i2 - this.f5247r);
            }
            i();
            repaint();
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public void invalidate() {
        super.invalidate();
        i();
    }

    @Override // ao.InterfaceC0640bd
    public void d(boolean z2) {
        this.f5255w = z2;
        if (this.f5255w) {
            return;
        }
        repaint();
    }

    public boolean u() {
        return this.f5239k == null || this.f5239k.isEmpty();
    }

    @Override // ao.InterfaceC0814l
    public void a(boolean z2) {
        this.f5256x = z2;
        i();
        repaint();
    }

    public void g(int i2) {
        this.f5247r = i2;
    }

    @Override // ao.InterfaceC0814l
    public void b(int i2) {
        g(i2);
        i();
        repaint();
    }

    public boolean v() {
        return this.f5256x && this.f5240l != null;
    }

    @Override // i.InterfaceC1742b
    public void b() {
    }

    public void c(int i2, int i3) {
        d(e(i2), e(i3));
    }

    public void d(int i2, int i3) {
        a(getGraphics(), i2, i3);
    }

    public void a(Graphics graphics, int i2, int i3) {
        Image imageL = L();
        Graphics graphics2 = imageL.getGraphics();
        graphics2.drawImage(this.f5230b, 0, 0, null);
        c(graphics2);
        int iL = l(i2);
        int iL2 = l(i3);
        b(graphics2, i2, i3);
        graphics2.setColor(Color.white);
        int height = (getHeight() - f5260B) + com.efiAnalytics.ui.eJ.a(3);
        graphics2.fillRect(iL, height, iL2 - iL, f5260B - com.efiAnalytics.ui.eJ.a(8));
        graphics2.setColor(Color.gray);
        graphics2.drawLine(iL, 0, iL, getHeight());
        graphics2.drawLine(iL2, 0, iL2, getHeight());
        if (C() != null) {
            String str = bH.W.c(Math.abs(C().c(i3) - C().c(i2)), 3) + "s.";
            graphics2.setFont(getFont());
            int iStringWidth = graphics2.getFontMetrics().stringWidth(str);
            int iA = iL2 - iL > iStringWidth + com.efiAnalytics.ui.eJ.a(10) ? iL2 - iL : iStringWidth + com.efiAnalytics.ui.eJ.a(10);
            graphics2.setColor(Color.white);
            graphics2.fillRect(iL, height, iA, f5260B - com.efiAnalytics.ui.eJ.a(8));
            graphics2.setColor(Color.gray);
            graphics2.drawString(str, iL + com.efiAnalytics.ui.eJ.a(5), height + com.efiAnalytics.ui.eJ.a(10));
        }
        graphics.drawImage(imageL, 0, 0, null);
        a(new C0812j(i2, i3));
    }

    private Image L() {
        if (this.f5236h == null || this.f5236h.getWidth(null) != getWidth() || this.f5236h.getHeight(null) != getHeight()) {
            this.f5236h = createImage(getWidth(), getHeight());
        }
        return this.f5236h;
    }

    public void b(Graphics graphics, int i2, int i3) {
        ArrayList arrayList = new ArrayList();
        Object[] objArrB = C1733k.b(this.f5239k.keySet().toArray());
        int length = 0;
        for (Object obj : objArrB) {
            for (C0184j c0184j : (List) this.f5239k.get(obj)) {
                if (c0184j.a() != null && c0184j.a().length() > length - 6) {
                    length = c0184j.a().length() + 6;
                }
            }
        }
        int i4 = length;
        for (Object obj2 : objArrB) {
            for (C0184j c0184j2 : (List) this.f5239k.get(obj2)) {
                if (c0184j2.a() != null && !c0184j2.a().trim().equals("")) {
                    arrayList.add(c0184j2.a() + ": " + bH.W.a(bH.W.b(c0184j2.b(i2, i3), -1), ' ', i4 - c0184j2.a().length()) + bH.W.a(bH.W.b(c0184j2.c(i2, i3), c0184j2.m()), ' ', 8) + bH.W.a(bH.W.b(c0184j2.d(i2, i3), c0184j2.m()), ' ', 8) + bH.W.a(bH.W.b(c0184j2.c(i3) - c0184j2.c(i2), c0184j2.m()), ' ', 8));
                }
            }
        }
        int i5 = 0;
        Font font = new Font("Monospaced", 0, getFont().getSize());
        FontMetrics fontMetrics = getFontMetrics(font);
        for (int i6 = 0; i6 < arrayList.size(); i6++) {
            int iStringWidth = fontMetrics.stringWidth((String) arrayList.get(i6));
            if (iStringWidth > i5) {
                i5 = iStringWidth;
            }
        }
        int i7 = i5 + 5;
        int width = getWidth() - i7;
        int height = (fontMetrics.getHeight() * (arrayList.size() + 1)) + com.efiAnalytics.ui.eJ.a(5);
        int height2 = (getHeight() - height) - f5260B;
        Image imageG = g(i7, height);
        Graphics graphics2 = imageG.getGraphics();
        if (bH.I.b() && this.f5274L) {
            Graphics2D graphics2D = (Graphics2D) graphics2;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        graphics2.setColor(Color.white);
        graphics2.setFont(font);
        graphics2.drawString(bH.W.a("Averages", ' ', i4 + 1) + bH.W.a("Min", ' ', 8) + bH.W.a("Max", ' ', 8) + bH.W.a("Delta", ' ', 8), 6, fontMetrics.getHeight());
        for (int i8 = 0; i8 < arrayList.size(); i8++) {
            graphics2.drawString((String) arrayList.get(i8), 0, (2 + i8) * fontMetrics.getHeight());
        }
        graphics.drawImage(imageG, width - 2, height2 - 2, null);
    }

    private Image g(int i2, int i3) {
        if (this.f5234f == null || this.f5234f.getWidth(null) != i2 || this.f5234f.getHeight(null) != i3) {
            this.f5234f = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(i2, i3, 3);
        }
        Graphics2D graphics2D = (Graphics2D) this.f5234f.getGraphics();
        graphics2D.setBackground(new Color(255, 255, 255, 0));
        graphics2D.clearRect(0, 0, this.f5234f.getWidth(null), this.f5234f.getHeight(null));
        graphics2D.setColor(new Color(64, 64, 64, 182));
        graphics2D.fillRect(0, 0, i2, i3);
        return this.f5234f;
    }

    public int w() {
        return this.f5276ag;
    }

    public aP e(int i2, int i3) {
        if (!C1737b.a().a("fieldSmoothing")) {
            return null;
        }
        Iterator it = this.f5263C.iterator();
        while (it.hasNext()) {
            aP aPVar = (aP) it.next();
            if (aPVar.a(i2, i3)) {
                return aPVar;
            }
        }
        return null;
    }

    public void a(aP aPVar) {
        b(aPVar);
    }

    private void b(aP aPVar) {
        if (C1737b.a().a("searchLogFiles") && aPVar.c().equals(aP.f5138a)) {
            C1580br c1580br = new C1580br();
            C0601S.a(c1580br, aPVar.b(), this);
            add(c1580br);
            c1580br.show(this, aPVar.a().f12372x, aPVar.a().f12373y + aPVar.a().height);
            return;
        }
        if (C1737b.a().a("4e345hggsdhd4812hfd43-0gdk") && aPVar.c().equals(aP.f5139b)) {
            C1580br c1580br2 = new C1580br();
            C0601S.b(c1580br2, aPVar.b(), this);
            add(c1580br2);
            c1580br2.show(this, aPVar.a().f12372x, aPVar.a().f12373y + aPVar.a().height);
        }
    }

    public void h(int i2) {
        this.f5276ag = i2;
    }

    public int x() {
        return this.f5277ah;
    }

    public void i(int i2) {
        this.f5277ah = i2;
    }

    private void h(Graphics graphics) {
        if (C() == null) {
            return;
        }
        if (w() > 0) {
            graphics.setColor(this.f5278N);
            int iW = (int) (w() * this.f5248s);
            graphics.fillRect(0, 0, iW, getHeight());
            String str = "Low Record: " + w();
            int iStringWidth = graphics.getFontMetrics().stringWidth(str) + com.efiAnalytics.ui.eJ.a(50);
            int iA = iW > iStringWidth ? (iW - iStringWidth) + com.efiAnalytics.ui.eJ.a(45) : com.efiAnalytics.ui.eJ.a(45);
            graphics.setColor(Color.white);
            graphics.drawString(str, iA, getHeight() - com.efiAnalytics.ui.eJ.a(5));
        }
        if (x() < C().i()) {
            graphics.setColor(this.f5278N);
            int i2 = (int) ((C().i() - x()) * this.f5248s);
            graphics.fillRect(getWidth() - i2, 0, i2, getHeight());
            String str2 = "High Record: " + x();
            int iStringWidth2 = graphics.getFontMetrics().stringWidth(str2) + com.efiAnalytics.ui.eJ.a(50);
            int width = i2 > iStringWidth2 ? (getWidth() - i2) + com.efiAnalytics.ui.eJ.a(5) : (getWidth() - iStringWidth2) + com.efiAnalytics.ui.eJ.a(5);
            graphics.setColor(Color.white);
            graphics.drawString(str2, width, getHeight() - com.efiAnalytics.ui.eJ.a(5));
        }
    }

    public void a(C0812j c0812j) {
        this.f5281ak = c0812j;
    }

    public C0812j y() {
        return this.f5281ak;
    }

    public void j(int i2) {
        this.f5244o.b(getWidth() / i2, false);
    }

    public int z() {
        return (int) (getWidth() / this.f5248s);
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        boolean z2 = C0645bi.a().c() != null && C0645bi.a().c().equals(this) && h.i.a(h.i.f12300U, h.i.f12301V) && this.f5244o.r() != null && Math.abs(this.f5244o.r().d() - z()) < 3;
        super.setBounds(i2, i3, i4, i5);
        if (z2) {
            SwingUtilities.invokeLater(new aF(this));
        }
    }

    public int A() {
        return this.f5249Z;
    }

    public void f(boolean z2) {
        this.f5288al = z2;
    }

    public C0836c B() {
        C0836c c0836c = new C0836c("Current View");
        Set setKeySet = this.f5239k.keySet();
        String[] strArrB = C1733k.b((String[]) setKeySet.toArray(new String[setKeySet.size()]));
        for (int i2 = 0; i2 < strArrB.length; i2++) {
            List list = (List) this.f5239k.get(strArrB[i2]);
            for (int i3 = 0; i3 < list.size(); i3++) {
                C0184j c0184j = (C0184j) list.get(i3);
                String str = strArrB[i2] + "." + i3;
                c0836c.a((c0184j.a() == null || c0184j.a().trim().equals("")) ? new C0837d(str, " ") : new C0837d(str, h.g.a().d(c0184j.a())));
            }
        }
        return c0836c;
    }

    public static void g(boolean z2) {
        f5269af = z2;
    }

    public void h(boolean z2) {
        this.f5280aj = z2;
    }

    public C0184j C() {
        return this.f5243Y;
    }

    public void i(boolean z2) {
        this.f5292an = z2;
    }
}
