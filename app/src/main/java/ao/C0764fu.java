package ao;

import W.C0184j;
import W.C0188n;
import aw.C0878a;
import aw.C0880c;
import com.efiAnalytics.ui.C1642e;
import com.efiAnalytics.ui.InterfaceC1662et;
import g.C1729g;
import g.C1733k;
import h.C1737b;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import org.apache.commons.math3.geometry.VectorFormat;

/* renamed from: ao.fu, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fu.class */
public class C0764fu extends JPanel implements bE.k {

    /* renamed from: p, reason: collision with root package name */
    JPanel f5875p;

    /* renamed from: q, reason: collision with root package name */
    C0880c f5876q;

    /* renamed from: S, reason: collision with root package name */
    private com.efiAnalytics.ui.dE f5879S;

    /* renamed from: v, reason: collision with root package name */
    JButton f5884v;

    /* renamed from: A, reason: collision with root package name */
    private static String f5857A = "xField";

    /* renamed from: B, reason: collision with root package name */
    private static String f5858B = "yField";

    /* renamed from: C, reason: collision with root package name */
    private static String f5859C = "zField";

    /* renamed from: D, reason: collision with root package name */
    private static String f5860D = "Hits";

    /* renamed from: H, reason: collision with root package name */
    private static String f5864H = "showFilterPanel";

    /* renamed from: I, reason: collision with root package name */
    private static String f5865I = "showRangeScale";

    /* renamed from: o, reason: collision with root package name */
    public static String f5866o = "Current View";

    /* renamed from: J, reason: collision with root package name */
    private static String f5867J = "Save Current View As";

    /* renamed from: K, reason: collision with root package name */
    private static String f5868K = "Delete View";

    /* renamed from: L, reason: collision with root package name */
    private static String f5869L = "Minimum Dot Size";

    /* renamed from: M, reason: collision with root package name */
    private static String f5870M = "Maximum Dot Size";

    /* renamed from: N, reason: collision with root package name */
    private static String f5871N = "Number of Y Sections";

    /* renamed from: O, reason: collision with root package name */
    private static String f5872O = "Number of X Sections";

    /* renamed from: P, reason: collision with root package name */
    private static String f5873P = "Maximum Number of Z Gradients";

    /* renamed from: Q, reason: collision with root package name */
    private static int f5874Q = 30;

    /* renamed from: w, reason: collision with root package name */
    private bE.m f5839w = new bE.m();

    /* renamed from: a, reason: collision with root package name */
    JToolBar f5840a = new JToolBar();

    /* renamed from: b, reason: collision with root package name */
    JToolBar f5841b = new JToolBar();

    /* renamed from: c, reason: collision with root package name */
    C1642e f5842c = new C1642e();

    /* renamed from: d, reason: collision with root package name */
    C1642e f5843d = new C1642e();

    /* renamed from: e, reason: collision with root package name */
    C1642e f5844e = new C1642e();

    /* renamed from: f, reason: collision with root package name */
    C0717ea f5845f = new C0717ea(this);

    /* renamed from: g, reason: collision with root package name */
    JLabel f5846g = new JLabel(" ", 0);

    /* renamed from: h, reason: collision with root package name */
    JLabel f5847h = new JLabel(" ", 0);

    /* renamed from: i, reason: collision with root package name */
    com.efiAnalytics.ui.fE f5848i = new com.efiAnalytics.ui.fE();

    /* renamed from: j, reason: collision with root package name */
    C0878a f5849j = null;

    /* renamed from: k, reason: collision with root package name */
    JPanel f5850k = null;

    /* renamed from: l, reason: collision with root package name */
    JToggleButton f5851l = new JToggleButton("Scales");

    /* renamed from: m, reason: collision with root package name */
    JButton f5852m = new JButton("Saved Views");

    /* renamed from: n, reason: collision with root package name */
    JButton f5853n = new JButton("Pop");

    /* renamed from: x, reason: collision with root package name */
    private InterfaceC1662et f5854x = null;

    /* renamed from: y, reason: collision with root package name */
    private int f5855y = 0;

    /* renamed from: z, reason: collision with root package name */
    private int f5856z = 0;

    /* renamed from: E, reason: collision with root package name */
    private String f5861E = "Hits";

    /* renamed from: F, reason: collision with root package name */
    private String f5862F = null;

    /* renamed from: G, reason: collision with root package name */
    private String f5863G = null;

    /* renamed from: R, reason: collision with root package name */
    private String f5877R = "zColorKey";

    /* renamed from: r, reason: collision with root package name */
    ArrayList f5878r = new ArrayList();

    /* renamed from: T, reason: collision with root package name */
    private boolean f5880T = false;

    /* renamed from: s, reason: collision with root package name */
    JButton f5881s = null;

    /* renamed from: t, reason: collision with root package name */
    JButton f5882t = null;

    /* renamed from: u, reason: collision with root package name */
    JButton f5883u = null;

    public C0764fu() {
        this.f5876q = null;
        this.f5879S = null;
        this.f5884v = null;
        setLayout(new BorderLayout());
        this.f5879S = new com.efiAnalytics.ui.dE(this.f5839w);
        this.f5879S.a(250);
        this.f5839w.a(this);
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
        this.f5875p = new JPanel();
        this.f5875p.setLayout(new BorderLayout());
        try {
            Image imageA = com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11151ao, this, 20);
            this.f5853n.setText("");
            this.f5853n.setIcon(new ImageIcon(imageA));
        } catch (V.a e2) {
            this.f5853n.setText("Popout");
            Logger.getLogger(C0764fu.class.getName()).log(Level.WARNING, "Failed to load Scatter plot full screen image.", (Throwable) e2);
        }
        if (C1737b.a().a("fdsahoirew098rew3284lksafd")) {
            this.f5840a.add(this.f5853n);
        }
        this.f5840a.add(this.f5852m);
        this.f5840a.add(new JLabel(" X Axis:"));
        this.f5840a.add(this.f5842c);
        this.f5840a.add(new JLabel(" Y Axis:"));
        this.f5840a.add(this.f5843d);
        this.f5840a.add(new JLabel(" Z Axis:"));
        this.f5840a.add(this.f5844e);
        this.f5840a.add(this.f5851l);
        Dimension dimensionA = com.efiAnalytics.ui.eJ.a(140, getFont().getSize() + 4);
        this.f5842c.setPreferredSize(dimensionA);
        this.f5843d.setPreferredSize(dimensionA);
        this.f5844e.setPreferredSize(dimensionA);
        this.f5852m.addActionListener(new C0765fv(this));
        this.f5842c.addItemListener(new fG(this));
        this.f5843d.addItemListener(new fL(this));
        this.f5844e.addItemListener(new fM(this));
        this.f5851l.addActionListener(new fN(this));
        this.f5853n.addActionListener(new fO(this));
        this.f5875p.add("North", this.f5840a);
        add("North", this.f5875p);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, this.f5839w);
        jPanel.add("North", this.f5846g);
        jPanel.add("West", this.f5848i);
        jPanel.add("South", this.f5847h);
        this.f5846g.setOpaque(true);
        this.f5848i.setOpaque(true);
        this.f5847h.setOpaque(true);
        this.f5846g.setBackground(Color.BLACK);
        this.f5848i.setBackground(Color.BLACK);
        this.f5847h.setBackground(Color.BLACK);
        this.f5846g.setForeground(Color.WHITE);
        this.f5848i.setForeground(Color.WHITE);
        this.f5847h.setForeground(Color.WHITE);
        this.f5846g.setFont(new Font("Times", 1, com.efiAnalytics.ui.eJ.a(18)));
        add(BorderLayout.CENTER, jPanel);
        this.f5839w.a(this.f5845f, 0);
        this.f5839w.addMouseListener(new fT(this));
        this.f5876q = new C0880c(null);
        this.f5876q.a(new fU(this));
        this.f5841b.add(this.f5876q);
        this.f5875p.add(BorderLayout.CENTER, this.f5841b);
        a(new fV(this));
        a(true);
        this.f5884v = new JButton(null, new ImageIcon(com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/zoomout.gif")))));
        this.f5839w.add(this.f5884v);
        this.f5884v.setVisible((this.f5876q.a() && this.f5876q.d()) ? false : true);
        this.f5884v.addActionListener(new fP(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        jPopupMenu.add(f5867J).addActionListener(new fQ(this));
        JMenu jMenu = new JMenu("Delete Saved View");
        fR fRVar = new fR(this);
        List<String> listA = C0762fs.a();
        for (String str : listA) {
            if (!str.equals(f5866o)) {
                jMenu.add(str).addActionListener(fRVar);
            }
        }
        jPopupMenu.add((JMenuItem) jMenu);
        jPopupMenu.addSeparator();
        C0766fw c0766fw = new C0766fw(this);
        for (String str2 : listA) {
            if (!str2.equals(f5866o)) {
                jPopupMenu.add(str2).addActionListener(c0766fw);
            }
        }
        this.f5852m.add(jPopupMenu);
        jPopupMenu.show(this.f5852m, 0, this.f5852m.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        C0762fs.b(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t() {
        h.i.c("userParameter_Scatter Plot View Name", this.f5846g.getText());
        C1729g c1729g = new C1729g(C1733k.a(this), VectorFormat.DEFAULT_PREFIX + "Scatter Plot View Name}", false, "       Save current Histogram View As", true);
        if (c1729g.f12195a) {
            String strA = c1729g.a();
            C0761fr c0761frA = a();
            c0761frA.a(strA);
            C0762fs.a(c0761frA);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(String str) {
        C0761fr c0761frA = null;
        try {
            c0761frA = C0762fs.a(str);
        } catch (V.a e2) {
            Logger.getLogger(C0764fu.class.getName()).log(Level.WARNING, "Scatter Plot View " + str + " cannot be loaded.", (Throwable) e2);
        }
        if (c0761frA == null) {
            com.efiAnalytics.ui.bV.d("Scatter Plot View " + str + " cannot be loaded.", this);
        } else {
            a(c0761frA);
        }
    }

    private void a(C0761fr c0761fr) {
        this.f5842c.b(c0761fr.b());
        this.f5876q.b(c0761fr.e(), c0761fr.f());
        this.f5876q.b(c0761fr.g());
        this.f5843d.b(c0761fr.c());
        this.f5876q.a(c0761fr.h(), c0761fr.i());
        this.f5876q.a(c0761fr.j());
        this.f5844e.b(c0761fr.d());
        this.f5876q.c(c0761fr.k(), c0761fr.l());
        this.f5876q.c(c0761fr.m());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void u() {
        C0759fp c0759fpA = C0645bi.a().a(this);
        c0759fpA.a(this.f5845f);
        c0759fpA.a(this.f5839w.n(), this.f5839w.o());
        if (!c0759fpA.isVisible()) {
            c0759fpA.setVisible(true);
        }
        c0759fpA.b(this.f5839w.getWidth(), this.f5839w.getHeight());
    }

    public C0761fr a() {
        C0761fr c0761fr = new C0761fr(this.f5846g.getText());
        c0761fr.b(this.f5842c.getSelectedItem() == null ? " " : this.f5842c.getSelectedItem().toString());
        c0761fr.a(this.f5876q.b());
        c0761fr.b(this.f5876q.c());
        c0761fr.a(this.f5876q.a());
        c0761fr.c(this.f5843d.getSelectedItem() == null ? " " : this.f5843d.getSelectedItem().toString());
        c0761fr.c(this.f5876q.e());
        c0761fr.d(this.f5876q.f());
        c0761fr.b(this.f5876q.d());
        c0761fr.d(this.f5844e.getSelectedItem() == null ? " " : this.f5844e.getSelectedItem().toString());
        c0761fr.e(this.f5876q.h());
        c0761fr.f(this.f5876q.i());
        c0761fr.c(this.f5876q.g());
        return c0761fr;
    }

    public void a(boolean z2) {
        if (this.f5849j == null) {
            this.f5850k = new JPanel();
            this.f5850k.setBackground(Color.BLACK);
            this.f5850k.setLayout(new BorderLayout());
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            this.f5881s = new JButton(null, new ImageIcon(com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/icon_close_11x11.gif")), jPanel)));
            this.f5881s.setFocusable(false);
            this.f5881s.setToolTipText("Close");
            this.f5881s.addActionListener(new C0767fx(this));
            this.f5881s.setPreferredSize(new Dimension(com.efiAnalytics.ui.eJ.a(14), com.efiAnalytics.ui.eJ.a(14)));
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new BorderLayout());
            jPanel2.add("North", this.f5881s);
            jPanel.add("East", jPanel2);
            this.f5850k.add("North", jPanel);
            this.f5882t = new JButton(null, new ImageIcon(com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/filter-16.png")), jPanel2)));
            this.f5882t.setFocusable(false);
            this.f5882t.setToolTipText("Show Filters");
            this.f5882t.addActionListener(new C0768fy(this));
            this.f5882t.setPreferredSize(new Dimension(com.efiAnalytics.ui.eJ.a(18), com.efiAnalytics.ui.eJ.a(18)));
            this.f5839w.add(this.f5882t);
            this.f5839w.setLayout(new fS(this));
            this.f5883u = new JButton(null, new ImageIcon(com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/edit-icon.png")), jPanel2)));
            this.f5883u.setFocusable(false);
            this.f5883u.setToolTipText("Show Filters");
            this.f5883u.addActionListener(new C0769fz(this));
            this.f5883u.setPreferredSize(new Dimension(com.efiAnalytics.ui.eJ.a(18), com.efiAnalytics.ui.eJ.a(18)));
            jPanel.add("West", this.f5883u);
            this.f5849j = new C0878a(null);
            this.f5849j.a(new fA(this));
            C0595M.a().a(this.f5849j);
            this.f5850k.add(BorderLayout.CENTER, this.f5849j);
            add("East", this.f5850k);
        }
        if (z2) {
        }
        this.f5850k.setVisible(z2);
        this.f5882t.setVisible(!z2);
        a(f5864H, "" + z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v() {
        if (this.f5849j.a().isEmpty()) {
            this.f5882t.setBackground(getBackground());
        } else {
            this.f5882t.setBackground(Color.RED);
        }
    }

    public void b() {
        b(this.f5851l.isSelected());
    }

    public void b(boolean z2) {
        this.f5851l.setSelected(z2);
        this.f5876q.setVisible(z2);
        a(f5865I, "" + z2);
    }

    public void a(InterfaceC0797h interfaceC0797h) {
        this.f5878r.add(interfaceC0797h);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w() {
        String string = this.f5842c.getSelectedItem() == null ? "" : this.f5842c.getSelectedItem().toString();
        Iterator it = this.f5878r.iterator();
        while (it.hasNext()) {
            ((InterfaceC0797h) it.next()).a(string);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void x() {
        String string = this.f5843d.getSelectedItem() == null ? "" : this.f5843d.getSelectedItem().toString();
        Iterator it = this.f5878r.iterator();
        while (it.hasNext()) {
            ((InterfaceC0797h) it.next()).b(string);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y() {
        String string = this.f5844e.getSelectedItem() == null ? "" : this.f5844e.getSelectedItem().toString();
        Iterator it = this.f5878r.iterator();
        while (it.hasNext()) {
            ((InterfaceC0797h) it.next()).c(string);
        }
    }

    public void a(C0188n c0188n) {
        this.f5845f.a(c0188n);
        if (c0188n == null || c0188n.size() <= 0) {
            return;
        }
        b(c0188n);
        this.f5884v.setVisible((this.f5876q.a() && this.f5876q.d()) ? false : true);
        bH.C.c("Dataset size: " + c0188n.d());
        this.f5845f.a((String) this.f5842c.getSelectedItem());
        this.f5845f.b((String) this.f5843d.getSelectedItem());
        this.f5845f.c((String) this.f5844e.getSelectedItem());
    }

    public void c() {
        if (this.f5876q.a()) {
            this.f5876q.b(this.f5845f.a(), this.f5845f.b());
        }
        if (this.f5876q.d()) {
            this.f5876q.a(this.f5845f.c(), this.f5845f.d());
        }
        if (this.f5876q.g()) {
            this.f5876q.c(this.f5845f.h(), this.f5845f.i());
        }
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        int i6 = this.f5842c.getPreferredSize().width;
        int iA = (i4 - com.efiAnalytics.ui.eJ.a(260)) / 3;
        Dimension dimension = i6 < iA ? new Dimension(i6, 20) : new Dimension(iA, 20);
        this.f5842c.setMinimumSize(dimension);
        this.f5843d.setMinimumSize(dimension);
        this.f5844e.setMinimumSize(dimension);
        super.setBounds(i2, i3, i4, i5);
    }

    @Override // java.awt.Component
    public void repaint() {
        super.repaint();
        if (this.f5839w != null) {
            this.f5839w.a();
        }
    }

    private void b(C0188n c0188n) {
        this.f5880T = true;
        a(this.f5842c, c0188n, " ");
        a(this.f5843d, c0188n, " ");
        a(this.f5844e, c0188n, f5860D);
        e();
        this.f5880T = false;
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
        if (this.f5842c.getSelectedItem() == null || this.f5843d.getSelectedItem() == null || ((String) this.f5842c.getSelectedItem()).length() <= 0 || ((String) this.f5843d.getSelectedItem()).length() <= 0) {
            return;
        }
        this.f5846g.setText((this.f5844e.getSelectedItem() == null || this.f5844e.getSelectedItem().toString().equals(" ")) ? this.f5843d.getSelectedItem() + " vs " + this.f5842c.getSelectedItem() : this.f5843d.getSelectedItem() + " vs " + this.f5842c.getSelectedItem() + " vs " + this.f5844e.getSelectedItem().toString());
        this.f5847h.setText(this.f5842c.getSelectedItem().toString());
        this.f5848i.setText(this.f5843d.getSelectedItem().toString());
        this.f5839w.a(this.f5842c.getSelectedItem().toString());
        this.f5839w.b(this.f5843d.getSelectedItem().toString());
        if (this.f5844e.getSelectedItem() != null) {
            this.f5839w.c(this.f5844e.getSelectedItem().toString());
        } else {
            this.f5839w.c("");
        }
    }

    public void a(int i2) {
        if (i2 <= this.f5856z || this.f5845f.f5608a == null || i2 >= this.f5855y || this.f5845f.f5609b == null || i2 >= this.f5845f.f5609b.i()) {
            this.f5839w.l();
            return;
        }
        bE.q qVarA = this.f5845f.a(i2);
        this.f5839w.a(qVarA.getX(), qVarA.getY());
        this.f5839w.repaint();
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f5854x = interfaceC1662et;
    }

    private boolean a(C1642e c1642e, String str) {
        for (int i2 = 0; i2 < c1642e.getItemCount(); i2++) {
            if (c1642e.a(i2).equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void e() {
        if (this.f5854x != null) {
            String strA = this.f5854x.a(f5857A);
            String strA2 = this.f5854x.a(f5858B);
            if (strA == null || !a(this.f5842c, strA)) {
                strA = null;
            } else {
                this.f5842c.b(strA);
            }
            if (strA2 == null || !a(this.f5843d, strA2)) {
                strA2 = null;
            } else {
                this.f5843d.b(strA2);
            }
            if ((strA == null || strA2 == null) && this.f5862F != null && this.f5863G != null) {
                this.f5842c.b(this.f5862F);
                this.f5843d.b(this.f5863G);
            }
            String strA3 = this.f5854x.a(f5859C);
            if (strA3 == null && this.f5861E != null) {
                strA3 = this.f5861E;
            }
            if (strA3 != null) {
                this.f5844e.b(strA3);
            }
            String strA4 = this.f5854x.a(this.f5877R);
            if (strA4 != null) {
                try {
                    d(Integer.parseInt(strA4));
                } catch (NumberFormatException e2) {
                    d(1);
                }
            }
            String strA5 = this.f5854x.a(f5864H);
            a(strA5 != null && Boolean.parseBoolean(strA5));
            String strA6 = this.f5854x.a(f5865I);
            b(strA6 != null && Boolean.parseBoolean(strA6));
            try {
                String strA7 = this.f5854x.a(f5869L);
                if (strA7 != null) {
                    this.f5839w.d((int) Math.round(Double.parseDouble(strA7)));
                }
            } catch (NumberFormatException e3) {
            }
            try {
                String strA8 = this.f5854x.a(f5870M);
                if (strA8 != null) {
                    this.f5839w.e((int) Math.round(Double.parseDouble(strA8)));
                }
            } catch (NumberFormatException e4) {
            }
            try {
                String strA9 = this.f5854x.a(f5872O);
                if (strA9 != null) {
                    this.f5839w.f(Integer.parseInt(strA9));
                }
            } catch (Exception e5) {
            }
            try {
                String strA10 = this.f5854x.a(f5871N);
                if (strA10 != null) {
                    this.f5839w.g(Integer.parseInt(strA10));
                }
            } catch (Exception e6) {
            }
            try {
                String strA11 = this.f5854x.a(f5873P);
                if (strA11 != null) {
                    this.f5839w.k(Integer.parseInt(strA11));
                }
            } catch (Exception e7) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, String str2) {
        if (this.f5854x != null) {
            this.f5854x.a(str, str2);
        }
    }

    public int f() {
        return this.f5855y;
    }

    public void b(int i2) {
        this.f5855y = i2;
        this.f5839w.d();
        this.f5879S.a();
    }

    public int g() {
        return this.f5856z;
    }

    public void c(int i2) {
        this.f5856z = i2;
        this.f5839w.d();
        this.f5879S.a();
    }

    public void a(String str) {
        this.f5862F = str;
    }

    public void b(String str) {
        this.f5863G = str;
    }

    public void c(String str) {
        this.f5861E = str;
    }

    public void a(Component component, int i2, int i3) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenu jMenu = new JMenu("Z Axis Color Mode");
        String strA = this.f5854x.a(this.f5877R);
        if (strA == null || strA.equals("")) {
            strA = "" + this.f5839w.k();
        }
        C0811i c0811i = new C0811i();
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Color to Max Value for Point", strA.equals("" + bE.m.f6722a));
        c0811i.a(jCheckBoxMenuItem);
        jCheckBoxMenuItem.addItemListener(new fB(this));
        jMenu.add((JMenuItem) jCheckBoxMenuItem);
        JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem("Color to Average Value for Point", strA.equals("" + bE.m.f6723b));
        c0811i.a(jCheckBoxMenuItem2);
        jCheckBoxMenuItem2.addItemListener(new fC(this));
        jMenu.add((JMenuItem) jCheckBoxMenuItem2);
        JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem("Color to Min Value for Point", strA.equals("" + bE.m.f6724c));
        c0811i.a(jCheckBoxMenuItem3);
        jCheckBoxMenuItem3.addItemListener(new fD(this));
        jMenu.add((JMenuItem) jCheckBoxMenuItem3);
        jPopupMenu.add((JMenuItem) jMenu);
        if (!this.f5876q.a() || !this.f5876q.d()) {
            JMenuItem jMenuItem = new JMenuItem("Default X & Y Scale");
            jMenuItem.addActionListener(new fE(this));
            jPopupMenu.add(jMenuItem);
        }
        JMenuItem jMenuItem2 = new JMenuItem(f5869L);
        jMenuItem2.addActionListener(new fF(this));
        jPopupMenu.add(jMenuItem2);
        JMenuItem jMenuItem3 = new JMenuItem(f5870M);
        jMenuItem3.addActionListener(new fH(this));
        jPopupMenu.add(jMenuItem3);
        JMenuItem jMenuItem4 = new JMenuItem(f5871N);
        jMenuItem4.addActionListener(new fI(this));
        jPopupMenu.add(jMenuItem4);
        JMenuItem jMenuItem5 = new JMenuItem(f5872O);
        jMenuItem5.addActionListener(new fJ(this));
        jPopupMenu.add(jMenuItem5);
        JMenuItem jMenuItem6 = new JMenuItem(f5873P);
        jMenuItem6.addActionListener(new fK(this));
        jPopupMenu.add(jMenuItem6);
        jPopupMenu.show(component, i2, i3);
    }

    public void d(int i2) {
        this.f5839w.c(i2);
        this.f5839w.d();
        this.f5839w.repaint();
        a(this.f5877R, i2 + "");
    }

    public void h() {
        this.f5839w.d();
        this.f5839w.repaint();
        if (C0645bi.a().j() == null || !C0645bi.a().j().getParent().equals(this)) {
            return;
        }
        C0645bi.a().j().c().d();
        C0645bi.a().j().c().repaint();
    }

    @Override // bE.k
    public void a(double d2, double d3, double d4, double d5) {
        bH.C.c("new range selected. xMin:" + d2 + ", xMax:" + d3 + ", yMin:" + d4 + ", yMax:" + d5);
        this.f5876q.b(d2, d3);
        this.f5876q.a(d4, d5);
        this.f5876q.b(false);
        this.f5876q.a(false);
        this.f5884v.setVisible(true);
        repaint();
        if (C0645bi.a().j() == null || !C0645bi.a().j().getParent().equals(this)) {
            return;
        }
        C0645bi.a().j().c().a();
    }

    public bE.m i() {
        return this.f5839w;
    }
}
