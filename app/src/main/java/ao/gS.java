package ao;

import W.C0184j;
import W.C0188n;
import ar.C0836c;
import ar.C0839f;
import g.C1729g;
import g.C1733k;
import h.C1737b;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:ao/gS.class */
public class gS extends JPanel implements InterfaceC0642bf, gO, InterfaceC0813k, MouseMotionListener {

    /* renamed from: e, reason: collision with root package name */
    public static final int f5949e = com.efiAnalytics.ui.eJ.a(14);

    /* renamed from: h, reason: collision with root package name */
    C0804hg f5952h;

    /* renamed from: k, reason: collision with root package name */
    C0618ai f5956k;

    /* renamed from: n, reason: collision with root package name */
    C0802he f5959n;

    /* renamed from: p, reason: collision with root package name */
    public static String f5961p;

    /* renamed from: a, reason: collision with root package name */
    int f5945a = h.i.a("numberOfGraphs", h.i.f12273t);

    /* renamed from: b, reason: collision with root package name */
    int f5946b = h.i.a("numberOfOverlays", h.i.f12274u);

    /* renamed from: c, reason: collision with root package name */
    int f5947c = 0;

    /* renamed from: d, reason: collision with root package name */
    int f5948d = 0;

    /* renamed from: f, reason: collision with root package name */
    Dimension f5950f = null;

    /* renamed from: g, reason: collision with root package name */
    Font f5951g = new Font("Arial Unicode MS", 1, com.efiAnalytics.ui.eJ.a(11));

    /* renamed from: i, reason: collision with root package name */
    boolean f5953i = false;

    /* renamed from: j, reason: collision with root package name */
    long f5954j = 0;

    /* renamed from: r, reason: collision with root package name */
    private C0803hf f5955r = null;

    /* renamed from: l, reason: collision with root package name */
    ArrayList f5957l = new ArrayList();

    /* renamed from: m, reason: collision with root package name */
    JPanel f5958m = new JPanel();

    /* renamed from: o, reason: collision with root package name */
    C0800hc f5960o = new C0800hc(this);

    /* renamed from: q, reason: collision with root package name */
    final String f5962q = "Save Current As";

    public gS(C0804hg c0804hg) {
        this.f5952h = null;
        this.f5956k = null;
        this.f5952h = c0804hg;
        setLayout(new BorderLayout());
        f5961p = h.i.d();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(this.f5958m, "North");
        JPanel jPanel2 = new JPanel();
        jPanel2.setPreferredSize(com.efiAnalytics.ui.eJ.a(1, 1));
        jPanel.add(jPanel2, BorderLayout.CENTER);
        this.f5959n = new C0802he(this, jPanel);
        add(this.f5959n, BorderLayout.CENTER);
        gT gTVar = new gT(this);
        addMouseMotionListener(this);
        addMouseListener(gTVar);
        this.f5959n.addMouseMotionListener(this);
        this.f5959n.addMouseListener(gTVar);
        jPanel.addMouseMotionListener(this);
        jPanel.addMouseListener(gTVar);
        this.f5956k = new C0618ai("Quick Views");
        this.f5956k.a(new gU(this));
        this.f5956k.setFont(this.f5951g);
        this.f5956k.a(new Dimension(com.efiAnalytics.ui.eJ.a(90), com.efiAnalytics.ui.eJ.a(25)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public C0803hf f() {
        if (this.f5955r == null || !this.f5955r.isAlive()) {
            this.f5955r = new C0803hf(this);
            this.f5955r.start();
        }
        return this.f5955r;
    }

    public void c() {
        JPopupMenu jPopupMenu = new JPopupMenu("menuFieldGroups");
        gV gVVar = new gV(this);
        this.f5956k.add(jPopupMenu);
        jPopupMenu.setFont(this.f5951g);
        jPopupMenu.add("Save Current As").addActionListener(gVVar);
        List<C0836c> listB = C0839f.a().b();
        JMenu jMenu = new JMenu("Delete Group");
        gW gWVar = new gW(this);
        for (C0836c c0836c : listB) {
            if (!c0836c.b().equals(Action.DEFAULT)) {
                JMenuItem jMenuItem = new JMenuItem(c0836c.b());
                jMenu.add(jMenuItem);
                jMenuItem.addActionListener(gWVar);
            }
        }
        jPopupMenu.add((JMenuItem) jMenu);
        jPopupMenu.add("Clear current Selections (CTRL+Delete)").addActionListener(new gX(this));
        jPopupMenu.addSeparator();
        for (C0836c c0836c2 : listB) {
            JMenuItem jMenuItem2 = new JMenuItem(c0836c2.b());
            jMenuItem2.addActionListener(gVVar);
            jMenuItem2.setToolTipText(c0836c2.a());
            jPopupMenu.add(jMenuItem2);
        }
        jPopupMenu.show(this.f5956k, 0, this.f5956k.getHeight());
    }

    public void a(String str) {
        if (!str.equals("Save Current As")) {
            d(str);
            return;
        }
        C1729g c1729g = new C1729g(C1733k.a(this), "{Field Group Name}", false, "       Save current Field Selection As", true);
        if (c1729g.f12195a) {
            b(c1729g.a());
        }
    }

    public void b(String str) {
        String[] strArrE = h.i.e("graph");
        h.i.c("FIELD_GROUP_NAME_" + str, str);
        for (int i2 = 0; i2 < strArrE.length; i2++) {
            if (strArrE[i2].indexOf(".") > 0 && strArrE[i2].length() < 10) {
                h.i.c("FIELD_SELECTED_GROUP_" + str + "_" + strArrE[i2], h.i.a(strArrE[i2], " "));
            }
        }
    }

    public void c(String str) {
        if (new C0736et(C1733k.a(this), "Are you sure you want to delete the field group " + str, true).f5662a) {
            C0839f.a().b(str);
        }
    }

    public void d(String str) {
        C0839f.a().a(str);
    }

    @Override // ao.InterfaceC0642bf
    public void a() {
        C0836c c0836cC;
        a(true);
        try {
            this.f5958m.removeAll();
        } catch (Exception e2) {
        }
        Iterator it = this.f5957l.iterator();
        while (it.hasNext()) {
            aO.a().b((C0801hd) it.next());
        }
        this.f5957l.clear();
        this.f5945a = h.i.a("numberOfGraphs", h.i.f12273t);
        this.f5946b = h.i.a("numberOfOverlays", h.i.f12274u);
        this.f5958m.setLayout(new BoxLayout(this.f5958m, 1));
        this.f5958m.add(this.f5956k);
        gY gYVar = new gY(this);
        C0188n c0188nR = this.f5952h.r();
        if (c0188nR == null || c0188nR.isEmpty()) {
            return;
        }
        String[] strArrA = new String[c0188nR.size()];
        for (int i2 = 0; i2 < c0188nR.size(); i2++) {
            C0184j c0184j = (C0184j) c0188nR.get(i2);
            if (c0184j.l()) {
                strArrA[i2] = c0184j.a() + f5961p;
            } else {
                strArrA[i2] = c0184j.a();
            }
        }
        if (h.i.a(h.i.f12284E, h.i.f12285F)) {
            strArrA = bH.R.a(strArrA);
        }
        for (int i3 = 0; i3 < this.f5945a; i3++) {
            C0643bg c0643bg = new C0643bg("Graph " + (i3 + 1));
            this.f5952h.a(c0643bg);
            this.f5958m.add(c0643bg);
            for (int i4 = 0; i4 < this.f5946b; i4++) {
                C0801hd c0801hd = new C0801hd(this, i3, i4);
                c0801hd.addKeyListener(gYVar);
                c0801hd.setFont(this.f5951g);
                this.f5957l.add(c0801hd);
                c0801hd.a(aO.a().a(i3, i4));
                aO.a().a(c0801hd);
                c0801hd.a(false);
                c0801hd.addKeyListener(this.f5960o);
                c0801hd.setName("graph" + i3 + "." + i4);
                c0801hd.a(" ");
                c0801hd.addItemListener(new gZ(this));
                for (String str : strArrA) {
                    c0801hd.a(str);
                }
                if (C1737b.a().a("fieldSmoothing")) {
                    JPanel jPanel = new JPanel();
                    jPanel.setLayout(new BorderLayout(com.efiAnalytics.ui.eJ.a(3), com.efiAnalytics.ui.eJ.a(3)));
                    jPanel.add(BorderLayout.CENTER, c0801hd);
                    C0615af c0615af = new C0615af();
                    c0801hd.addItemListener(new C0798ha(this, c0615af));
                    c0615af.setFocusable(false);
                    jPanel.add("East", c0615af);
                    c0643bg.add(jPanel);
                } else {
                    c0643bg.add(c0801hd);
                }
                String strG = C0839f.a().g();
                if (strG != null && (c0836cC = C0839f.a().c(strG)) != null) {
                    String strC = c0836cC.c(e(c0801hd.getName()));
                    if (strC.equals(" ")) {
                        a(strC, c0801hd);
                    } else {
                        if (strC.contains("Field.")) {
                            strC = h.g.a().a(strC);
                        }
                        C0184j c0184jA = this.f5952h.r().a(strC);
                        if (c0184jA != null) {
                            if (c0184jA.l()) {
                                c0801hd.b(strC + f5961p);
                            } else {
                                c0801hd.b(strC);
                            }
                            a(strC, c0801hd);
                        } else {
                            c0801hd.b(" ");
                            a(" ", c0801hd);
                        }
                    }
                }
            }
        }
        a(false);
        SwingUtilities.invokeLater(new RunnableC0799hb(this));
        invalidate();
        setBackground(getBackground());
        if (this.f5954j == 0) {
            d();
            this.f5955r = f();
            this.f5955r.b();
            this.f5954j = System.currentTimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String e(String str) {
        return str.endsWith(f5961p) ? str.substring(0, str.length() - 1) : str;
    }

    protected void a(String str, C0801hd c0801hd) {
        String strE = e(str);
        String strE2 = h.g.a().e(strE);
        if (strE2 == null || strE2.equals("")) {
            h.i.c(c0801hd.getName(), strE);
        } else {
            h.i.c(c0801hd.getName(), "Field." + strE2);
        }
        if (strE == null || strE.equals(" ")) {
            a(c0801hd.getName(), (C0184j) null);
        } else {
            a(c0801hd.getName(), this.f5952h.r().a(strE));
        }
        C0184j c0184jA = this.f5952h.r().a(strE);
        String strY = null;
        if (C0804hg.a().r() != null && C0804hg.a().r().a(strE) != null && C0804hg.a().r().a(strE).y() != null) {
            strY = C0804hg.a().r().a(strE).y();
        }
        if (c0184jA == null || c0184jA.u() == null || c0184jA.a().equals(c0184jA.u())) {
            if (strY == null) {
                c0801hd.setToolTipText(null);
                return;
            } else {
                c0801hd.setToolTipText("Field Description: " + strY);
                return;
            }
        }
        String str2 = "Standardized Name: " + c0184jA.a() + ", Name in File: " + c0184jA.u();
        if (strY != null) {
            str2 = str2 + "\nField Description: " + strY;
        }
        c0801hd.setToolTipText(str2);
    }

    protected void a(String str, C0184j c0184j) {
        C0641be.a().a(str, c0184j);
    }

    protected void a(boolean z2) {
        C0641be.a().a(z2);
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public Insets getInsets() {
        return new Insets(com.efiAnalytics.ui.eJ.a(3), com.efiAnalytics.ui.eJ.a(3), com.efiAnalytics.ui.eJ.a(3), f5949e);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setForeground(Color color) {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            components[i2].setForeground(color.darker());
            components[i2].repaint();
        }
        super.setForeground(color);
        repaint();
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        super.setBackground(color);
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            getComponent(i2).setBackground(color);
        }
        repaint();
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color) {
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color, int i2) {
        int i3 = 0;
        Component[] components = getComponents();
        for (int i4 = 0; i4 < components.length; i4++) {
            if (components[i4] instanceof Container) {
                Component[] components2 = ((Container) components[i4]).getComponents();
                for (int i5 = 0; i5 < components2.length; i5++) {
                    if (components2[i5] instanceof C0801hd) {
                        if (i3 == i2) {
                            C0801hd c0801hd = (C0801hd) components2[i5];
                            c0801hd.a(color);
                            c0801hd.repaint();
                            return;
                        }
                        i3++;
                    }
                }
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public void c(boolean z2) {
        this.f5953i = z2;
        C1733k.b(this);
        Frame frameA = C1733k.a(this);
        if (frameA != null) {
            frameA.validate();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        if (!this.f5953i) {
            return super.getMinimumSize();
        }
        this.f5950f = super.getMinimumSize();
        Dimension minimumSize = super.getMinimumSize();
        Insets insets = getInsets();
        minimumSize.width = this.f5948d + insets.left + insets.right;
        return minimumSize;
    }

    @Override // java.awt.Component
    public void setSize(int i2, int i3) {
        setBounds(getX(), getY(), i2, i3);
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        if (this.f5953i) {
            i2 = f5949e - super.getMinimumSize().width;
            i4 = super.getMinimumSize().width;
        }
        if (this.f5953i && getWidth() >= super.getMinimumSize().width) {
            i2 = getX();
        }
        super.setBounds(i2, i3, i4, i5);
        a(false);
    }

    public void d() {
        while (getLocation().f12370x < 0) {
            Point location = getLocation();
            location.f12370x += 4;
            super.setBounds(location.f12370x, location.f12371y, getSize().width, getSize().height);
            try {
                Thread.sleep(2L);
            } catch (InterruptedException e2) {
                Logger.getLogger(gS.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void e() {
        if (this.f5953i) {
            boolean z2 = false;
            while (getLocation().f12370x > (f5949e - getSize().width) + 2) {
                z2 = true;
                Point location = getLocation();
                location.f12370x -= 4;
                try {
                    Thread.sleep(2L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(gS.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                super.setBounds(location.f12370x, location.f12371y, getSize().width, getSize().height);
            }
            if (z2) {
                C0645bi.a().c().requestFocus();
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(Color.lightGray);
        graphics.draw3DRect(getSize().width - 8, 5, 3, getSize().height - 10, true);
    }

    @Override // ao.gO
    public void b(boolean z2) {
        c(z2);
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.f5955r != null) {
            this.f5955r.a();
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        if (this.f5955r != null) {
            this.f5955r.a();
        }
    }

    @Override // java.awt.Component
    public void repaint() {
        super.repaint();
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            getComponent(i2).repaint();
        }
    }

    @Override // ao.InterfaceC0642bf
    public void b() {
        Iterator it = this.f5957l.iterator();
        while (it.hasNext()) {
            ((C0801hd) it.next()).setSelectedItem(" ");
        }
    }

    @Override // java.awt.Component
    public boolean isVisible() {
        return true;
    }
}
