package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import r.C1798a;
import r.C1806i;

/* renamed from: com.efiAnalytics.ui.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/w.class */
public class C1705w extends JPanel implements TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    protected BinTableView f11762a;

    /* renamed from: b, reason: collision with root package name */
    C1540ae f11763b;

    /* renamed from: c, reason: collision with root package name */
    C1540ae f11764c;

    /* renamed from: d, reason: collision with root package name */
    JTableHeader f11765d;

    /* renamed from: e, reason: collision with root package name */
    JPanel f11766e;

    /* renamed from: f, reason: collision with root package name */
    C1546ak f11767f;

    /* renamed from: g, reason: collision with root package name */
    JPanel f11768g;

    /* renamed from: h, reason: collision with root package name */
    JPanel f11769h;

    /* renamed from: i, reason: collision with root package name */
    JCheckBox f11770i;

    /* renamed from: j, reason: collision with root package name */
    int f11771j;

    /* renamed from: w, reason: collision with root package name */
    private int f11772w;

    /* renamed from: x, reason: collision with root package name */
    private final ArrayList f11773x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f11774y;

    /* renamed from: z, reason: collision with root package name */
    private int f11775z;

    /* renamed from: k, reason: collision with root package name */
    ArrayList f11776k;

    /* renamed from: A, reason: collision with root package name */
    private boolean f11777A;

    /* renamed from: B, reason: collision with root package name */
    private final ArrayList f11778B;

    /* renamed from: l, reason: collision with root package name */
    C1543ah f11779l;

    /* renamed from: m, reason: collision with root package name */
    C1543ah f11780m;

    /* renamed from: n, reason: collision with root package name */
    int f11781n;

    /* renamed from: o, reason: collision with root package name */
    JPanel f11782o;

    /* renamed from: p, reason: collision with root package name */
    JPanel f11783p;

    /* renamed from: C, reason: collision with root package name */
    private int f11784C;

    /* renamed from: D, reason: collision with root package name */
    private boolean f11785D;

    /* renamed from: q, reason: collision with root package name */
    JButton f11786q;

    /* renamed from: r, reason: collision with root package name */
    JButton f11787r;

    /* renamed from: s, reason: collision with root package name */
    JButton f11788s;

    /* renamed from: t, reason: collision with root package name */
    JButton f11789t;

    /* renamed from: u, reason: collision with root package name */
    int f11790u;

    /* renamed from: E, reason: collision with root package name */
    private final List f11791E;

    /* renamed from: v, reason: collision with root package name */
    JPanel f11792v;

    /* renamed from: F, reason: collision with root package name */
    private boolean f11793F;

    public C1705w(C1701s c1701s) {
        this();
        a(c1701s);
    }

    public C1705w() {
        this.f11762a = null;
        this.f11763b = null;
        this.f11764c = null;
        this.f11765d = null;
        this.f11766e = null;
        this.f11767f = null;
        this.f11769h = null;
        this.f11770i = null;
        this.f11771j = -1;
        this.f11772w = 0;
        this.f11773x = new ArrayList();
        this.f11774y = true;
        this.f11775z = -1;
        this.f11776k = new ArrayList();
        this.f11777A = true;
        this.f11778B = new ArrayList();
        this.f11779l = new C1543ah(this);
        this.f11780m = new C1543ah(this);
        this.f11781n = 0;
        this.f11782o = new JPanel();
        this.f11783p = new JPanel();
        this.f11784C = 0;
        this.f11785D = false;
        this.f11786q = null;
        this.f11787r = null;
        this.f11788s = null;
        this.f11789t = null;
        this.f11790u = 0;
        this.f11791E = new ArrayList();
        this.f11792v = new JPanel();
        this.f11793F = false;
        if (UIManager.getLookAndFeel().toString().contains("Nimbus")) {
            this.f11790u = 2;
        } else {
            this.f11790u = 1;
        }
        setLayout(new BorderLayout(1, 1));
        JPanel jPanel = new JPanel();
        add(BorderLayout.CENTER, jPanel);
        jPanel.setLayout(new BorderLayout(1, 1));
        this.f11766e = new JPanel();
        this.f11766e.setLayout(new BorderLayout());
        this.f11762a = new BinTableView(new C1701s());
        this.f11771j = this.f11762a.Q() + 3;
        this.f11762a.setDoubleBuffered(true);
        this.f11762a.setRowHeight(this.f11771j);
        this.f11766e.add(BorderLayout.CENTER, this.f11762a);
        this.f11765d = this.f11762a.createDefaultTableHeader();
        this.f11765d.setFont(new Font(this.f11762a.getFont().getFamily(), 0, this.f11762a.Q() - 2));
        this.f11765d.setReorderingAllowed(false);
        this.f11765d.setBackground(Color.lightGray);
        this.f11765d.setForeground(Color.BLACK);
        this.f11765d.addMouseListener(new C1706x(this));
        this.f11762a.setTableHeader(this.f11765d);
        this.f11765d.setMinimumSize(new Dimension(10, this.f11771j));
        this.f11765d.setPreferredSize(new Dimension(10, this.f11771j));
        this.f11766e.add("South", this.f11765d);
        this.f11765d.setFocusable(false);
        jPanel.add(BorderLayout.CENTER, this.f11766e);
        jPanel.add("North", a());
        this.f11763b = new C1540ae(this, ((C1701s) this.f11762a.getModel()).x(), new String[]{" "});
        this.f11763b.setFont(this.f11762a.getFont());
        this.f11763b.setBackground(Color.lightGray);
        this.f11763b.setForeground(Color.BLACK);
        this.f11763b.setEnabled(false);
        this.f11763b.addMouseListener(new I(this));
        this.f11763b.setPreferredSize(new Dimension(this.f11762a.j(), 0));
        this.f11763b.setRowHeight(this.f11771j);
        this.f11763b.setSelectionMode(0);
        this.f11763b.setDefaultRenderer(String.class, new C1544ai(this));
        this.f11763b.setFocusable(false);
        this.f11792v.setLayout(new BorderLayout());
        this.f11792v.add(BorderLayout.CENTER, this.f11763b);
        ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/xy16.png")));
        if (BinTableView.S()) {
            this.f11788s = new JButton(null, imageIcon);
            this.f11788s.setToolTipText(c("Toggle Re-bin X & Y"));
        } else {
            this.f11788s = new C1542ag(this, null, imageIcon);
            this.f11788s.setToolTipText(c("Upgrade to enable X-Y Re-Bin"));
        }
        this.f11792v.add("South", this.f11788s);
        this.f11788s.addActionListener(new L(this));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(1, 2));
        this.f11779l.setLayout(new GridLayout(0, 1, 2, 1));
        jPanel2.add("West", this.f11779l);
        jPanel2.add("East", this.f11792v);
        jPanel.add("West", jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        this.f11780m.setLayout(new GridLayout(1, 0, 2, 3));
        this.f11782o.setLayout(new BorderLayout());
        this.f11782o.add("North", this.f11780m);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(2));
        this.f11787r = new JButton(c("Apply New X&Y, Z Unchanged"));
        this.f11787r.addActionListener(new M(this));
        jPanel4.add(this.f11787r);
        this.f11786q = new JButton(c("Apply New X&Y values, Interpolate Z"));
        this.f11786q.addActionListener(new N(this));
        jPanel4.add(this.f11786q);
        this.f11782o.add(BorderLayout.CENTER, jPanel4);
        jPanel3.add("West", this.f11783p);
        jPanel3.add(BorderLayout.CENTER, this.f11782o);
        this.f11781n = jPanel3.getPreferredSize().height;
        jPanel.add("South", jPanel3);
        g(false);
    }

    public void c() {
        if (this.f11769h == null) {
            this.f11769h = new JPanel();
            this.f11762a.e(true);
            this.f11769h.setLayout(new FlowLayout(2));
            this.f11770i = new JCheckBox(c("Follow Mode"));
            this.f11770i.addActionListener(new O(this));
            this.f11762a.a(new P(this));
            this.f11769h.add(this.f11770i);
            this.f11767f.add(BorderLayout.CENTER, this.f11769h);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public boolean requestFocusInWindow() {
        return this.f11762a.requestFocusInWindow();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v250, types: [javax.swing.JButton] */
    /* JADX WARN: Type inference failed for: r0v251, types: [javax.swing.JButton] */
    /* JADX WARN: Type inference failed for: r0v252, types: [javax.swing.JButton] */
    private JToolBar a() {
        int iA = eJ.a(22);
        this.f11767f = new C1546ak(this, "Edit table values");
        this.f11767f.setFloatable(false);
        this.f11767f.setLayout(new BorderLayout());
        this.f11768g = new JPanel();
        this.f11768g.setLayout(new GridLayout(1, 0, eJ.a(3), eJ.a(3)));
        MediaTracker mediaTracker = new MediaTracker(this.f11768g);
        this.f11767f.add("East", this.f11768g);
        try {
            this.f11789t = new JButton(new ImageIcon(cO.a().a(cO.f11121K, this.f11768g, 22)));
            this.f11789t.setFocusable(false);
            this.f11789t.addActionListener(new Q(this));
            this.f11789t.setPreferredSize(new Dimension(iA, iA));
            this.f11768g.add(this.f11789t);
            JLabel jLabel = new JLabel(" ");
            jLabel.setPreferredSize(new Dimension(iA, iA));
            this.f11768g.add(jLabel);
            this.f11789t.setVisible(false);
        } catch (V.a e2) {
            bV.d(c("Failed to create Resize Button.") + "\n" + e2.getLocalizedMessage(), this);
        }
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/export_sm.png"));
        mediaTracker.addImage(image, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image, 1);
        JButton jButton = new JButton(null, new ImageIcon(eJ.a(image)));
        jButton.setFocusable(false);
        jButton.setToolTipText("Export Table");
        jButton.addActionListener(new R(this));
        jButton.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton);
        Image image2 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/import_sm.png"));
        mediaTracker.addImage(image2, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image2, 1);
        JButton jButton2 = new JButton(null, new ImageIcon(eJ.a(image2)));
        jButton2.setFocusable(false);
        jButton2.setToolTipText("Import Table");
        jButton2.addActionListener(new C1707y(this));
        jButton2.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton2);
        this.f11778B.add(jButton2);
        Image image3 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/equal_sign_16.png"));
        mediaTracker.addImage(image3, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image3, 1);
        JButton jButton3 = new JButton(null, new ImageIcon(eJ.a(image3)));
        jButton3.setFocusable(false);
        jButton3.setToolTipText("Set to - Key: =");
        jButton3.addActionListener(new C1708z(this));
        jButton3.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton3);
        this.f11778B.add(jButton3);
        Image image4 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/up_16.png"));
        mediaTracker.addImage(image4, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image4, 1);
        JButton jButton4 = new JButton(null, new ImageIcon(eJ.a(image4)));
        jButton4.setFocusable(false);
        jButton4.setToolTipText("Increment - Key: > or ,");
        jButton4.addActionListener(new A(this));
        jButton4.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton4);
        this.f11778B.add(jButton4);
        Image image5 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/down_16.png"));
        mediaTracker.addImage(image5, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image5, 1);
        JButton jButton5 = new JButton(null, new ImageIcon(eJ.a(image5)));
        jButton5.setFocusable(false);
        jButton5.setToolTipText("Decrement - Key: < or .");
        jButton5.addActionListener(new B(this));
        jButton5.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton5);
        this.f11778B.add(jButton5);
        Image image6 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/minus_sign_16.png"));
        mediaTracker.addImage(image6, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image6, 1);
        JButton jButton6 = new JButton(null, new ImageIcon(eJ.a(image6)));
        jButton6.setFocusable(false);
        jButton6.setToolTipText("Decrease by - Key: -");
        jButton6.addActionListener(new C(this));
        jButton6.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton6);
        this.f11778B.add(jButton6);
        Image image7 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/plus_sign_16.png"));
        mediaTracker.addImage(image7, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image7, 1);
        JButton jButton7 = new JButton(null, new ImageIcon(eJ.a(image7)));
        jButton7.setFocusable(false);
        jButton7.setToolTipText("Increase by - Key: +");
        jButton7.addActionListener(new D(this));
        jButton7.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton7);
        this.f11778B.add(jButton7);
        Image image8 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/times_sign_16.png"));
        mediaTracker.addImage(image8, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image8, 1);
        JButton jButton8 = new JButton(null, new ImageIcon(eJ.a(image8)));
        jButton8.setFocusable(false);
        jButton8.setToolTipText("Scale by - Key: *");
        jButton8.addActionListener(new E(this));
        jButton8.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton8);
        this.f11778B.add(jButton8);
        Image image9 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/interpolate.png"));
        mediaTracker.addImage(image9, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image9, 1);
        JButton jButton9 = new JButton(null, new ImageIcon(eJ.a(image9)));
        jButton9.setFocusable(false);
        jButton9.setToolTipText("Interpolate - Key: /");
        jButton9.addActionListener(new F(this));
        jButton9.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton9);
        this.f11778B.add(jButton9);
        Image image10 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/interpolateHorizontal.png"));
        mediaTracker.addImage(image10, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image10, 1);
        ImageIcon imageIcon = new ImageIcon(eJ.a(image10));
        C1542ag jButton10 = BinTableView.S() ? new JButton(null, imageIcon) : new C1542ag(this, null, imageIcon);
        jButton10.setFocusable(false);
        jButton10.setToolTipText("Interpolate Horizontal - Key: H");
        jButton10.addActionListener(new G(this));
        jButton10.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton10);
        this.f11778B.add(jButton10);
        Image image11 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/interpolateVertical.png"));
        mediaTracker.addImage(image11, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image11, 1);
        ImageIcon imageIcon2 = new ImageIcon(eJ.a(image11));
        C1542ag jButton11 = BinTableView.S() ? new JButton(null, imageIcon2) : new C1542ag(this, null, imageIcon2);
        jButton11.setFocusable(false);
        jButton11.setToolTipText("Interpolate Vertical - Key: V");
        jButton11.addActionListener(new H(this));
        jButton11.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton11);
        this.f11778B.add(jButton11);
        Image image12 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/smooth.png"));
        mediaTracker.addImage(image12, 1);
        a(mediaTracker);
        mediaTracker.removeImage(image12, 1);
        ImageIcon imageIcon3 = new ImageIcon(eJ.a(image12));
        C1542ag jButton12 = BinTableView.S() ? new JButton(null, imageIcon3) : new C1542ag(this, null, imageIcon3);
        jButton12.setFocusable(false);
        jButton12.setToolTipText("Smooth Cells - Key: s, " + c("Right click to set smoothing factor"));
        jButton12.addMouseListener(new J(this));
        jButton12.setPreferredSize(new Dimension(iA, iA));
        this.f11768g.add(jButton12);
        this.f11778B.add(jButton12);
        return this.f11767f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        C1580br c1580br = new C1580br();
        this.f11762a.a(c1580br);
        this.f11789t.add(c1580br);
        c1580br.show(this.f11789t, 0, this.f11789t.getHeight());
    }

    private synchronized void a(MediaTracker mediaTracker) {
        try {
            mediaTracker.waitForAll(250L);
        } catch (InterruptedException e2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        String strA;
        if (!BinTableView.S() || (strA = bV.a((Component) this, true, c("Smoothing Factor") + " (0.0 - 1.0) " + c("High for more smoothing."), this.f11762a.M() + "")) == null || strA.equals("")) {
            return;
        }
        float fM = this.f11762a.M();
        try {
            fM = Float.parseFloat(strA);
            this.f11762a.a(fM);
        } catch (Exception e2) {
            bV.d(c("Invalid Smoothing Factor") + " " + fM, this);
        }
    }

    private String c(String str) {
        if (this.f11762a.K() != null) {
            str = this.f11762a.K().a(str);
        }
        return str;
    }

    public void d() throws NumberFormatException {
        if (t()) {
            if (this.f11785D) {
                C1701s c1701sG = g();
                String[] strArrB = this.f11780m.b();
                String[] strArrB2 = this.f11779l.b();
                c1701sG.c(strArrB);
                double d2 = Double.parseDouble(strArrB[0]);
                a(d2, d2);
                for (int i2 = 0; i2 < strArrB2.length; i2++) {
                    a(i2, strArrB2[i2]);
                    try {
                        double d3 = Double.parseDouble(strArrB2[i2]);
                        b(d3, d3);
                    } catch (NumberFormatException e2) {
                        e2.printStackTrace();
                    }
                }
                this.f11765d.resizeAndRepaint();
            } else {
                C1701s c1701sG2 = g();
                C1701s c1701sB = C1677fh.b(c1701sG2);
                String[] strArrB3 = this.f11780m.b();
                String[] strArrB4 = this.f11779l.b();
                c1701sG2.c(strArrB3);
                for (int i3 = 0; i3 < strArrB4.length; i3++) {
                    a(i3, strArrB4[i3]);
                }
                C1677fh.b(c1701sB, c1701sG2);
                this.f11765d.resizeAndRepaint();
            }
            this.f11762a.repaint(1000L);
            this.f11762a.validate();
        }
    }

    private boolean t() {
        String[] strArrB = this.f11780m.b();
        String[] strArrB2 = this.f11779l.b();
        double d2 = Double.NaN;
        for (int i2 = 0; i2 < strArrB.length; i2++) {
            try {
                double d3 = Double.parseDouble(strArrB[i2]);
                if (i2 == 0) {
                    d2 = d3;
                } else if (d2 >= d3) {
                    bV.d(c("Invalid value at column:") + " " + (i2 + 1) + "\n" + c("New X values should be low to high from left to right.") + "\n" + c("Please correct before applying new X & Y values."), this);
                    return false;
                }
            } catch (NumberFormatException e2) {
                bV.d(c("Non-Numeric value on X Axis on column:") + " " + (i2 + 1) + "\n" + c("Please correct before applying new X & Y values."), this);
                return false;
            }
        }
        if (!g().I()) {
            return true;
        }
        for (int i3 = 0; i3 < strArrB2.length; i3++) {
            try {
                double d4 = Double.parseDouble(strArrB2[i3]);
                if (i3 == 0) {
                    d2 = d4;
                } else if (d2 < d4) {
                    bV.d(c("Invalid value at row:") + " " + (i3 + 1) + "\n" + c("New Y values should be low to high from bottom to top.") + "\n" + c("Please correct before applying new X & Y values."), this);
                    return false;
                }
            } catch (NumberFormatException e3) {
                bV.d(c("Non-Numeric value on Y Axis on row:") + " " + (i3 + 1) + "\n" + c("Please correct before applying new X & Y values."), this);
                return false;
            }
        }
        return true;
    }

    public void e() {
        if (m()) {
            boolean z2 = !v();
            u();
        }
    }

    private void u() {
        C1705w c1705w = new C1705w(g());
        JDialog jDialogB = bV.b(c1705w, this, c("Re-Bin Table"), (InterfaceC1565bc) null);
        c1705w.g(true);
        c1705w.f(this.f11785D);
        c1705w.h().a(h().b());
        c1705w.h().b(h().c());
        c1705w.f11780m.b(h().c());
        c1705w.b(this.f11772w);
        c1705w.h().c(h().N());
        Point locationOnScreen = getLocationOnScreen();
        Dimension size = getSize();
        c1705w.setPreferredSize(size);
        c1705w.f11776k = this.f11776k;
        if (this.f11785D) {
        }
        c1705w.h().f(this.f11762a.U());
        c1705w.b(false);
        jDialogB.pack();
        int width = jDialogB.getInsets().left + jDialogB.getInsets().right + this.f11763b.getWidth();
        int height = (jDialogB.getHeight() - c1705w.getHeight()) + this.f11781n + this.f11771j;
        jDialogB.setLocation(locationOnScreen.f12370x - width, locationOnScreen.f12371y - jDialogB.getInsets().top);
        jDialogB.setSize(size.width + width, size.height + height);
        jDialogB.setVisible(true);
        jDialogB.setSize(size.width + width, size.height + height + 2);
    }

    private void g(boolean z2) {
        this.f11782o.setVisible(z2);
        this.f11779l.setVisible(z2);
        this.f11783p.setVisible(z2);
        this.f11788s.setEnabled(!z2);
        validate();
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        C1685fp.a((Container) this, z2);
        this.f11765d.setEnabled(z2);
        repaint();
        this.f11763b.repaint();
    }

    private boolean v() {
        return this.f11782o.isVisible();
    }

    public void a(bH.aa aaVar) {
        this.f11762a.a(aaVar);
    }

    public JTable f() {
        return this.f11763b;
    }

    public void a(InterfaceC1698p interfaceC1698p) {
        this.f11773x.add(interfaceC1698p);
    }

    private void a(int i2, String str, String str2) {
        Iterator it = this.f11773x.iterator();
        while (it.hasNext()) {
            ((InterfaceC1698p) it.next()).a(i2, str, str2);
        }
    }

    private void b(int i2, String str, String str2) {
        Iterator it = this.f11773x.iterator();
        while (it.hasNext()) {
            ((InterfaceC1698p) it.next()).b(i2, str, str2);
        }
        if (this.f11764c == null || !(this.f11764c.getModel() instanceof C1645ec)) {
            return;
        }
        ((C1645ec) this.f11764c.getModel()).a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(int i2) throws NumberFormatException {
        String str;
        if (m() && g().G()) {
            TableModel model = f().getModel();
            String strV = g().v();
            if (i2 < model.getRowCount()) {
                if (model.getValueAt(i2, 0) != null) {
                    str = "" + bH.W.b((Double.parseDouble(model.getValueAt(i2, 0).toString()) - 100.0d) * 0.145038d, 2);
                } else {
                    str = "";
                }
                String str2 = "" + ((Double.parseDouble(bV.a((Component) this, true, "Set " + strV + " Value", str)) / 0.145038d) + 100.0d);
                if (str2 != null && !str2.equals("")) {
                    a(i2, str2);
                }
                requestFocusInWindow();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(int i2) throws NumberFormatException {
        if (m() && g().G()) {
            TableModel model = f().getModel();
            String strV = g().v();
            if (i2 < model.getRowCount()) {
                String strA = bV.a((Component) this, true, "Set " + strV + " Value", model.getValueAt(i2, 0) != null ? model.getValueAt(i2, 0).toString() : "");
                if (strA != null && !strA.equals("")) {
                    a(i2, strA);
                }
                requestFocusInWindow();
            }
        }
    }

    public void a(int i2, String str) throws NumberFormatException {
        JTable jTableF = f();
        TableModel model = jTableF.getModel();
        C1701s c1701s = (C1701s) this.f11762a.getModel();
        String string = model.getValueAt(i2, 0).toString();
        double d2 = Double.parseDouble(str);
        double d3 = 0.0d;
        try {
            d3 = Double.parseDouble(string);
        } catch (Exception e2) {
        }
        if (b(d2, d3)) {
            model.setValueAt(str, i2, 0);
            c1701s.b(str, i2);
            b(i2, str, string);
            jTableF.repaint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(int i2) throws NumberFormatException {
        if (m()) {
            String strA = bV.a((Component) this, true, "Set " + g().w() + " Value", (String) this.f11765d.getColumnModel().getColumn(i2).getHeaderValue());
            if (strA != null && !strA.equals("")) {
                b(i2, strA);
            }
            requestFocusInWindow();
        }
    }

    public void b(int i2, String str) throws NumberFormatException {
        String string = this.f11765d.getColumnModel().getColumn(i2).getHeaderValue().toString();
        C1701s c1701s = (C1701s) this.f11762a.getModel();
        if (str.equals(string)) {
            return;
        }
        double d2 = Double.parseDouble(str);
        double d3 = 0.0d;
        try {
            d3 = Double.parseDouble(this.f11765d.getColumnModel().getColumn(i2).getHeaderValue().toString());
        } catch (Exception e2) {
        }
        if (a(d2, d3)) {
            this.f11765d.getColumnModel().getColumn(i2).setHeaderValue(str);
            c1701s.a(str, i2);
            this.f11765d.resizeAndRepaint();
            a(i2, str, string);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public Insets getInsets() {
        Insets insets = super.getInsets();
        insets.bottom += 4;
        insets.left += 4;
        insets.top += 4;
        insets.right += 4;
        return insets;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        try {
            super.paint(graphics);
        } catch (Exception e2) {
        }
        if (isEnabled()) {
            return;
        }
        graphics.setColor(new Color(64, 64, 64, 80));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    public void a(C1701s c1701s) {
        if (g() != null) {
            g().removeTableModelListener(this);
        }
        c1701s.addTableModelListener(this);
        this.f11762a.setModel(c1701s);
        k();
    }

    public C1701s g() {
        return (C1701s) this.f11762a.getModel();
    }

    public BinTableView h() {
        return this.f11762a;
    }

    public void a(String str) {
        this.f11767f.setToolTipText(str);
    }

    public void b(String str) {
        this.f11767f.a(str);
        this.f11767f.repaint();
    }

    public boolean i() {
        return this.f11762a.d();
    }

    public void j() {
        this.f11762a.e();
    }

    public void k() {
        this.f11762a.repaint();
        String[] strArrA = g().a();
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        boolean z2 = this.f11779l.getComponentCount() == 0;
        if (z2) {
            this.f11779l.removeAll();
        }
        defaultTableModel.addColumn("");
        int length = 0;
        for (int i2 = 0; i2 < strArrA.length && strArrA[i2] != null; i2++) {
            if (strArrA[i2].length() > length) {
                length = strArrA[i2].length();
            }
        }
        if (strArrA.length / 2 == g().getRowCount()) {
            for (int i3 = 0; i3 < strArrA.length; i3 = i3 + 1 + 1) {
                defaultTableModel.addRow(new String[]{strArrA[i3]});
            }
        } else {
            for (String str : strArrA) {
                defaultTableModel.addRow(new String[]{str});
            }
        }
        if (z2) {
            this.f11780m.a(new S(this, g().b(), false));
            this.f11779l.a(new S(this, g().a(), true));
        }
        this.f11783p.setMinimumSize(new Dimension(this.f11779l.getParent().getWidth(), 20));
        this.f11783p.setPreferredSize(new Dimension(this.f11779l.getParent().getWidth(), 20));
        this.f11763b.setModel(defaultTableModel);
        if (this.f11764c != null && (this.f11764c.getModel() instanceof C1645ec)) {
            ((C1645ec) this.f11764c.getModel()).a(defaultTableModel);
        }
        defaultTableModel.fireTableStructureChanged();
        TableColumnModel columnModel = this.f11762a.getColumnModel();
        for (int i4 = 0; i4 < columnModel.getColumnCount(); i4++) {
            columnModel.getColumn(i4).setHeaderValue(this.f11762a.getModel().getColumnName(i4));
        }
        this.f11765d.repaint();
        ((C1701s) this.f11762a.getModel()).C();
        if (isVisible()) {
            try {
                super.validate();
            } catch (Exception e2) {
            }
        }
    }

    @Override // java.awt.Component
    public void repaint() {
        super.repaint();
        if (this.f11763b != null) {
            this.f11763b.repaint();
        }
    }

    private void g(int i2) {
        if (this.f11793F) {
            return;
        }
        int iRound = (int) Math.round(i2 * 0.08d);
        int iA = iRound < eJ.a(22) ? eJ.a(22) : iRound;
        for (int i3 = 0; i3 < this.f11767f.getComponentCount(); i3++) {
            Component component = this.f11767f.getComponent(i3);
            if (component instanceof JButton) {
                ((JButton) component).setPreferredSize(new Dimension(iA, iA));
            } else if (component instanceof JPanel) {
                a((JPanel) component, iA);
            }
        }
    }

    private void a(JPanel jPanel, int i2) {
        for (int i3 = 0; i3 < jPanel.getComponentCount(); i3++) {
            Component component = jPanel.getComponent(i3);
            if (component instanceof JButton) {
                JButton jButton = (JButton) component;
                jButton.setPreferredSize(new Dimension(i2, i2));
                jButton.setMinimumSize(new Dimension(i2, i2));
            } else if (component instanceof JPanel) {
                a((JPanel) component, i2);
            }
        }
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        g(i5);
        if (this.f11762a == null) {
            super.setBounds(i2, i3, i4, i5);
            return;
        }
        int rowCount = this.f11779l.isShowing() ? this.f11762a.getModel().getRowCount() + 2 : this.f11762a.getModel().getRowCount() + 1;
        int i6 = (int) ((i5 - 3) / (rowCount * 1.22d));
        int i7 = i6 < 1 ? 1 : i6;
        int columnCount = this.f11780m.isShowing() ? this.f11762a.getModel().getColumnCount() + 2 : this.f11762a.getModel().getColumnCount() + 1;
        int iRound = ((int) Math.round((i4 / columnCount) / y())) - this.f11790u;
        int i8 = iRound < 1 ? 1 : iRound;
        int i9 = i8 < i7 ? i8 : i7;
        if (i7 - rowCount > i8) {
            this.f11784C = rowCount;
        } else if (i8 - (rowCount * y()) > i7) {
            this.f11784C = (int) (-Math.round(columnCount * y()));
        } else {
            this.f11784C = 0;
        }
        this.f11762a.e(i9);
        this.f11779l.a(i9 > 3 ? i9 - 2 : i9);
        this.f11780m.a(i9 > 2 ? i9 - 1 : i9);
        TableColumnModel columnModel = this.f11762a.getColumnModel();
        int iRound2 = Math.round(i4 / columnCount);
        for (int i10 = 0; i10 < columnModel.getColumnCount(); i10++) {
            columnModel.getColumn(i10).setMaxWidth(iRound2);
        }
        this.f11763b.a(iRound2);
        this.f11771j = ((((i5 - getInsets().top) - getInsets().bottom) - this.f11767f.getPreferredSize().height) - (this.f11782o.isVisible() ? this.f11782o.getHeight() : 0)) / (this.f11762a.getModel().getRowCount() + 1);
        if (this.f11771j > 0) {
            this.f11763b.setRowHeight(this.f11771j);
            this.f11762a.setRowHeight(this.f11771j);
            this.f11763b.setFont(this.f11762a.getFont());
            int height = ((((i5 - this.f11767f.getPreferredSize().height) - (this.f11782o.isVisible() ? this.f11782o.getHeight() : 0)) - (this.f11771j * this.f11762a.getModel().getRowCount())) - getInsets().top) - getInsets().bottom;
            this.f11765d.setMinimumSize(new Dimension(10, height));
            this.f11765d.setPreferredSize(new Dimension(10, height));
            this.f11788s.setPreferredSize(new Dimension(10, height));
            if (this.f11779l.a() != null) {
                this.f11779l.a().a(iRound2, this.f11771j);
            }
            if (this.f11780m.a() != null) {
                this.f11780m.a().a(iRound2, this.f11771j);
            }
        }
        if (this.f11764c != null) {
            this.f11764c.a(iRound2);
            this.f11764c.setRowHeight(this.f11763b.getRowHeight());
            this.f11764c.setFont(w());
        }
        l();
        super.setBounds(i2, i3, i4, i5);
        try {
            k();
        } catch (Exception e2) {
            bH.C.c("BinTablePanel::refresh error caught");
        }
    }

    public void l() {
        this.f11763b.setFont(this.f11762a.getFont());
        this.f11763b.repaint();
    }

    private Font w() {
        Font font = this.f11763b.getFont();
        return font.getSize() > 0 ? new Font(font.getFamily(), font.getStyle(), font.getSize() - 1) : new Font(font.getFamily(), font.getStyle(), 1);
    }

    @Override // java.awt.Component
    public void setSize(Dimension dimension) {
        super.setSize(dimension.width, dimension.height);
    }

    public void a(boolean z2) {
        this.f11774y = z2;
    }

    public boolean m() {
        return this.f11777A;
    }

    public void b(boolean z2) {
        this.f11777A = z2;
        this.f11788s.setEnabled(z2);
        this.f11788s.setToolTipText(z2 ? "Edit X & Y Axis" : "X & Y are read only in this view.");
    }

    @Override // java.awt.Container, java.awt.Component
    public void validate() {
        if (this.f11762a != null) {
            boolean z2 = this.f11762a.g() && isEnabled();
            Iterator it = this.f11778B.iterator();
            while (it.hasNext()) {
                ((Component) it.next()).setEnabled(z2);
            }
        }
        super.validate();
    }

    public void tableChanged(TableModelEvent tableModelEvent) {
        if (tableModelEvent.getColumn() == -1) {
            int firstRow = tableModelEvent.getFirstRow();
            JTable jTableF = f();
            try {
                jTableF.getModel().setValueAt(((C1701s) this.f11762a.getModel()).a()[tableModelEvent.getFirstRow()], firstRow, 0);
            } catch (IndexOutOfBoundsException e2) {
                bH.C.c("Calling reFresh for Y Axis.");
                k();
            }
            jTableF.repaint();
        }
    }

    public void c(boolean z2) {
        this.f11762a.a(z2);
    }

    public int n() {
        return this.f11784C;
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    public void a(InterfaceC1548am interfaceC1548am) {
        this.f11762a.a(interfaceC1548am);
        this.f11789t.setVisible(interfaceC1548am.a());
    }

    public void d(boolean z2) {
        if (!C1806i.a().a("645fds645fds  fdsd098532#@") || !C1798a.a().c(C1798a.cc, C1798a.cd)) {
            z2 = false;
        }
        if (z2 && (this.f11764c == null || !this.f11764c.isVisible())) {
            if (this.f11764c == null) {
                x();
            }
            this.f11792v.remove(this.f11763b);
            this.f11792v.add(BorderLayout.CENTER, this.f11764c);
            this.f11764c.setVisible(true);
        } else if (!z2 && this.f11764c != null && this.f11764c.isVisible()) {
            this.f11764c.setVisible(false);
            this.f11792v.remove(this.f11764c);
            this.f11792v.add(BorderLayout.CENTER, this.f11763b);
        }
        if (this.f11764c != null) {
            validate();
        }
    }

    private void x() {
        double dA = C1798a.a().a(C1798a.f13327ai, 100.0d);
        this.f11764c = new C1540ae(this, ((C1701s) this.f11762a.getModel()).y(), new String[]{" "});
        this.f11764c.a(new C1547al(this));
        this.f11764c.setFont(w());
        this.f11764c.setBackground(Color.lightGray);
        this.f11764c.setForeground(Color.BLACK);
        this.f11764c.setEnabled(false);
        this.f11764c.addMouseListener(new K(this));
        this.f11764c.setPreferredSize(new Dimension(this.f11762a.j(), 0));
        this.f11764c.setRowHeight(this.f11771j);
        this.f11764c.setSelectionMode(0);
        C1547al c1547al = new C1547al(this);
        c1547al.a(2);
        this.f11764c.setDefaultRenderer(String.class, c1547al);
        this.f11764c.setDefaultRenderer(Double.class, c1547al);
        this.f11764c.setFocusable(false);
        C1645ec c1645ec = new C1645ec();
        c1645ec.b(-dA);
        c1645ec.a(0.145038d);
        this.f11764c.setModel(c1645ec);
        this.f11764c.a(this.f11763b.getWidth());
        this.f11764c.setRowHeight(this.f11763b.getRowHeight());
        this.f11764c.setFont(w());
    }

    public void a(String str, int i2) {
        while (this.f11791E.size() < i2) {
            this.f11791E.add("");
        }
        this.f11791E.add(i2, str);
    }

    public void o() {
        this.f11791E.clear();
    }

    public int p() {
        return this.f11775z;
    }

    public void e(boolean z2) {
        this.f11793F = z2;
    }

    public void a(fy fyVar) {
        this.f11776k.add(fyVar);
    }

    private boolean a(double d2, double d3) {
        Iterator it = this.f11776k.iterator();
        while (it.hasNext()) {
            if (!((fy) it.next()).a(d2, d3)) {
                return false;
            }
        }
        return true;
    }

    private boolean b(double d2, double d3) {
        Iterator it = this.f11776k.iterator();
        while (it.hasNext()) {
            if (!((fy) it.next()).b(d2, d3)) {
                return false;
            }
        }
        return true;
    }

    public int q() {
        return this.f11772w;
    }

    public int r() {
        return this.f11762a.c();
    }

    public void a(int i2) {
        this.f11780m.b(i2);
        this.f11762a.b(i2);
    }

    public void b(int i2) {
        if (this.f11772w != i2) {
            this.f11772w = i2;
            this.f11779l.b(i2);
        }
    }

    public void a(InterfaceC1662et interfaceC1662et) throws NumberFormatException {
        this.f11762a.a(interfaceC1662et);
    }

    public void c(int i2) {
        a(false);
        int rowCount = ((3 + i2) * (this.f11762a.getRowCount() + 1)) + this.f11767f.getPreferredSize().height;
        int iY = ((int) (y() * i2)) * (this.f11762a.getColumnCount() + 1);
        setMinimumSize(new Dimension(iY, rowCount));
        setPreferredSize(new Dimension(iY, rowCount));
        this.f11775z = i2;
        invalidate();
        doLayout();
    }

    private double y() {
        return UIManager.getLookAndFeel().toString().contains("Nimbus") ? (z() > 3 || this.f11772w != 0) ? 3.75d : 2.65d : (z() > 3 || this.f11772w != 0) ? 3.27d : 2.65d;
    }

    private int z() {
        double dDoubleValue = 0.0d;
        TableModel model = this.f11762a.getModel();
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();
        for (int i2 = 0; i2 < rowCount; i2++) {
            for (int i3 = 0; i3 < columnCount; i3++) {
                Double d2 = (Double) model.getValueAt(i2, i3);
                if (d2 != null && Math.abs(d2.doubleValue()) > dDoubleValue) {
                    dDoubleValue = d2.doubleValue();
                }
            }
        }
        int iLog10 = (int) Math.log10(dDoubleValue);
        if (iLog10 == 0) {
            iLog10 = 1;
        }
        return iLog10 + this.f11762a.b();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        if (this.f11775z <= 0 || this.f11774y) {
            return new Dimension(((int) (y() * this.f11762a.getFont().getSize())) * (this.f11762a.getColumnCount() + 1), ((3 + this.f11762a.getFont().getSize()) * (this.f11762a.getRowCount() + 1)) + this.f11767f.getPreferredSize().height + 2);
        }
        return new Dimension(((int) (y() * this.f11775z)) * (this.f11762a.getColumnCount() + 1), ((4 + this.f11775z) * (this.f11762a.getRowCount() + 1)) + this.f11767f.getPreferredSize().height + 3);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension preferredSize = getPreferredSize();
        preferredSize.width /= 2;
        preferredSize.height /= 2;
        return preferredSize;
    }

    public void f(boolean z2) {
        this.f11785D = z2;
        if (z2) {
            this.f11786q.setText("Apply X & Y Axis Values");
            this.f11787r.setVisible(false);
        }
    }
}
