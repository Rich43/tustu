package com.efiAnalytics.apps.ts.tuningViews;

import bH.C1018z;
import bH.Z;
import com.efiAnalytics.apps.ts.dashboard.aO;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.BurnButtonTv;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.SelectableTable;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.TableCellCrossHair;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.TuneSettingsPanel;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.TuneViewGaugeCluster;
import com.efiAnalytics.ui.C1580br;
import com.efiAnalytics.ui.C1609ct;
import com.efiAnalytics.ui.C1615cz;
import com.efiAnalytics.ui.C1630dn;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1579bq;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import n.InterfaceC1761a;
import org.apache.commons.net.nntp.NNTPReply;
import r.C1798a;
import r.C1806i;
import r.C1807j;
import s.C1818g;
import sun.security.tools.policytool.ToolWindow;
import v.C1887g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/J.class */
public class J extends JPanel implements InterfaceC1565bc, InterfaceC1761a, n.g {

    /* renamed from: e, reason: collision with root package name */
    public static final String f9699e = C1818g.b("Edit Enabled Condition");

    /* renamed from: f, reason: collision with root package name */
    public static final String f9700f = C1818g.b("Edit Tuning View Name");

    /* renamed from: g, reason: collision with root package name */
    public static final String f9701g = C1818g.b("Bring Forward");

    /* renamed from: h, reason: collision with root package name */
    public static final String f9702h = C1818g.b("Send Backward");

    /* renamed from: i, reason: collision with root package name */
    public static final String f9703i = C1818g.b("Bring to Front");

    /* renamed from: j, reason: collision with root package name */
    public static final String f9704j = C1818g.b("Send to Back");

    /* renamed from: k, reason: collision with root package name */
    public static final String f9705k = C1818g.b("Gauge Properties");

    /* renamed from: l, reason: collision with root package name */
    public static final String f9706l = C1818g.b("Add Settings Panel");

    /* renamed from: m, reason: collision with root package name */
    public static final String f9707m = C1818g.b("Add Tuning Table");

    /* renamed from: n, reason: collision with root package name */
    public static final String f9708n = C1818g.b("Add Gauge Cluster");

    /* renamed from: o, reason: collision with root package name */
    public static final String f9709o = C1818g.b("Add Table Cell Cross Hair");

    /* renamed from: p, reason: collision with root package name */
    public static final String f9710p = C1818g.b("Add Burn Button");

    /* renamed from: q, reason: collision with root package name */
    public static final String f9711q = C1818g.b("Remove From Tuning View");

    /* renamed from: r, reason: collision with root package name */
    public static final String f9712r = C1818g.b("Export / Save As");

    /* renamed from: s, reason: collision with root package name */
    public static final String f9713s = C1818g.b("Save Tuning View");

    /* renamed from: t, reason: collision with root package name */
    public static final String f9714t = C1818g.b("Load Tuning View");

    /* renamed from: u, reason: collision with root package name */
    public static final String f9715u = C1818g.b("Designer Mode");

    /* renamed from: v, reason: collision with root package name */
    public static final String f9716v = C1818g.b("Copy");

    /* renamed from: w, reason: collision with root package name */
    public static final String f9717w = C1818g.b("Paste");

    /* renamed from: x, reason: collision with root package name */
    public static final String f9718x = C1818g.b("Show Grid");

    /* renamed from: y, reason: collision with root package name */
    public static final String f9719y = f9715u;

    /* renamed from: M, reason: collision with root package name */
    private static String f9720M = ((Object) C1807j.A()) + ".tempView";

    /* renamed from: a, reason: collision with root package name */
    T f9685a = new T(this);

    /* renamed from: b, reason: collision with root package name */
    Q f9686b = new Q(this);

    /* renamed from: C, reason: collision with root package name */
    private int f9687C = eJ.a(8);

    /* renamed from: D, reason: collision with root package name */
    private Window f9688D = null;

    /* renamed from: E, reason: collision with root package name */
    private Container f9689E = null;

    /* renamed from: c, reason: collision with root package name */
    boolean f9690c = false;

    /* renamed from: F, reason: collision with root package name */
    private Dimension f9691F = null;

    /* renamed from: G, reason: collision with root package name */
    private boolean f9692G = false;

    /* renamed from: H, reason: collision with root package name */
    private R f9693H = null;

    /* renamed from: d, reason: collision with root package name */
    List f9694d = new ArrayList();

    /* renamed from: I, reason: collision with root package name */
    private String f9695I = null;

    /* renamed from: J, reason: collision with root package name */
    private boolean f9696J = false;

    /* renamed from: K, reason: collision with root package name */
    private ArrayList f9697K = new ArrayList();

    /* renamed from: L, reason: collision with root package name */
    private boolean f9698L = true;

    /* renamed from: N, reason: collision with root package name */
    private boolean f9721N = true;

    /* renamed from: O, reason: collision with root package name */
    private boolean f9722O = true;

    /* renamed from: P, reason: collision with root package name */
    private String f9723P = null;

    /* renamed from: Q, reason: collision with root package name */
    private F f9724Q = null;

    /* renamed from: R, reason: collision with root package name */
    private Image f9725R = null;

    /* renamed from: z, reason: collision with root package name */
    X f9726z = null;

    /* renamed from: S, reason: collision with root package name */
    private String f9727S = "";

    /* renamed from: A, reason: collision with root package name */
    int f9728A = eJ.a(2);

    /* renamed from: B, reason: collision with root package name */
    Stroke f9729B = new BasicStroke(this.f9728A);

    public J() {
        setFocusable(true);
        if (C1018z.i().b()) {
            setLayout(new C1439l());
            addMouseListener(this.f9686b);
            addMouseMotionListener(this.f9686b);
            setDoubleBuffered(true);
            setFocusable(true);
        }
    }

    public void c() {
        try {
            a((TuneViewComponent) new TuneSettingsPanel());
        } catch (V.a e2) {
        }
        validate();
    }

    public void a(TuneViewComponent tuneViewComponent) {
        super.add(tuneViewComponent);
        if (G.T.a().c() != null) {
            tuneViewComponent.initializeComponents();
        }
        tuneViewComponent.addMouseListener(this.f9685a);
        tuneViewComponent.addMouseMotionListener(this.f9685a);
        tuneViewComponent.enableEditMode(B());
        if (B()) {
            this.f9697K.clear();
            this.f9697K.add(tuneViewComponent);
            repaint();
        }
        if (tuneViewComponent instanceof TuneViewGaugeCluster) {
            ((TuneViewGaugeCluster) tuneViewComponent).addFullScreenRequestListener(new K(this));
        }
        if (B()) {
            d(tuneViewComponent);
        }
    }

    public void d() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            it.remove();
            b(tuneViewComponent);
        }
        repaint();
    }

    public void b(TuneViewComponent tuneViewComponent) {
        tuneViewComponent.removeMouseListener(this.f9685a);
        tuneViewComponent.removeMouseMotionListener(this.f9685a);
        remove(tuneViewComponent);
        tuneViewComponent.close();
        this.f9697K.remove(tuneViewComponent);
        repaint();
    }

    @Override // java.awt.Container
    public void removeAll() {
        Component[] components = getComponents();
        int componentCount = getComponentCount();
        for (int i2 = 0; i2 < componentCount; i2++) {
            if (components[i2] instanceof TuneViewComponent) {
                b((TuneViewComponent) components[i2]);
            }
        }
        super.removeAll();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (B()) {
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, getWidth(), getHeight());
            if (this.f9722O) {
                Color color = UIManager.getColor("Button.disabledText");
                if (color == null) {
                    color = Color.GRAY;
                }
                graphics.setColor(color);
                int i2 = 0;
                while (true) {
                    int i3 = i2;
                    if (i3 >= getWidth()) {
                        break;
                    }
                    graphics.drawLine(i3, 0, i3, getHeight());
                    i2 = i3 + 10;
                }
                int i4 = 0;
                while (true) {
                    int i5 = i4;
                    if (i5 >= getHeight()) {
                        break;
                    }
                    graphics.drawLine(0, i5, getWidth(), i5);
                    i4 = i5 + 10;
                }
            }
            paintChildren(graphics);
            Iterator it = this.f9697K.iterator();
            while (it.hasNext()) {
                a(graphics, (TuneViewComponent) it.next());
            }
            a(graphics);
        } else {
            super.paint(graphics);
        }
        if (this.f9726z != null && this.f9726z.f9764a) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Image imageX = x();
            if (imageX != null) {
                int width = imageX.getWidth(null) / 2;
                int height = imageX.getHeight(null) / 2;
                graphics.drawImage(imageX, (getWidth() - width) / 2, (getHeight() - height) / 2, width, height, null);
            }
        }
        if (this.f9695I != null) {
            graphics.setFont(UIManager.getFont("TabbedPane.font"));
            graphics.setColor(UIManager.getColor("Label.foreground"));
            graphics.drawString(this.f9695I, (getWidth() - graphics.getFontMetrics().stringWidth(this.f9695I)) / 2, getHeight() / 2);
        }
    }

    private void a(Graphics graphics) {
        if (this.f9686b.a() != null) {
            Rectangle rectangleA = this.f9686b.a();
            graphics.setColor(Color.YELLOW);
            graphics.setXORMode(getBackground());
            int iAbs = Math.abs(rectangleA.width);
            int iAbs2 = Math.abs(rectangleA.height);
            int i2 = rectangleA.width < 0 ? rectangleA.f12372x + rectangleA.width : rectangleA.f12372x;
            int i3 = rectangleA.height < 0 ? rectangleA.f12373y + rectangleA.height : rectangleA.f12373y;
            ((Graphics2D) graphics).setStroke(new BasicStroke(1.0f, 0, 2, 0.0f, new float[]{2.0f, 1.0f}, 0.0f));
            graphics.drawRect(i2, i3, iAbs, iAbs2);
        }
    }

    private void a(Graphics graphics, TuneViewComponent tuneViewComponent) {
        if (B()) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x2 = tuneViewComponent.getX();
            int y2 = tuneViewComponent.getY();
            int width = tuneViewComponent.getWidth();
            int height = tuneViewComponent.getHeight();
            int i2 = this.f9687C - (this.f9728A / 2);
            if (width <= 0 || height <= 0) {
                return;
            }
            graphics2D.setStroke(this.f9729B);
            graphics.setColor(Color.yellow);
            graphics.drawLine(x2 + i2, y2 + i2, (x2 + width) - i2, (y2 + height) - i2);
            graphics.drawLine(x2 + i2, (y2 + height) - i2, (x2 + width) - i2, y2 + i2);
            graphics.fillRect(x2, y2, this.f9687C, this.f9687C);
            graphics.fillRect(x2, (y2 + height) - this.f9687C, this.f9687C, this.f9687C);
            graphics.fillRect((x2 + width) - this.f9687C, y2, this.f9687C, this.f9687C);
            if (this.f9697K.size() == 1) {
                graphics.setColor(Color.red);
            }
            graphics.fillRect((x2 + width) - this.f9687C, (y2 + height) - this.f9687C, this.f9687C, this.f9687C);
            String str = "X: " + x2 + ", Y:" + y2 + ", Width: " + width + ", Height: " + height;
            graphics.setColor(Color.DARK_GRAY);
            graphics.fillRect(x2 + 10, y2, graphics.getFontMetrics().stringWidth(str) + 10, graphics.getFont().getSize() + 2);
            graphics.setColor(Color.WHITE);
            graphics.drawString(str, x2 + 15, (y2 + graphics.getFont().getSize()) - 1);
        }
    }

    public void a(Component component, int i2, int i3) {
        if (C1798a.a().a(C1798a.da, C1798a.db)) {
            return;
        }
        JPopupMenu jPopupMenuE = e();
        add(jPopupMenuE);
        if (component != null) {
            jPopupMenuE.show(component, i2, i3);
        } else {
            jPopupMenuE.show(this, i2, i3);
        }
    }

    public JPopupMenu e() {
        C1580br c1580br = new C1580br();
        a((InterfaceC1579bq) c1580br);
        return c1580br;
    }

    public InterfaceC1579bq a(InterfaceC1579bq interfaceC1579bq) {
        S s2 = new S(this);
        if (C1806i.a().a("43wunjt58j7tjtht")) {
            JMenu jMenu = new JMenu(C1818g.b("Load / Save"));
            interfaceC1579bq.add(jMenu);
            jMenu.add(f9714t).addActionListener(s2);
            jMenu.add(f9713s).addActionListener(s2);
            jMenu.add(f9712r).addActionListener(s2);
        }
        if (B()) {
            JMenu jMenu2 = new JMenu(C1818g.b("Designer Menu"));
            jMenu2.add(f9700f).addActionListener(s2);
            jMenu2.add(f9699e).addActionListener(s2);
            JMenu jMenu3 = new JMenu(C1818g.b(ToolWindow.NEW_POLICY_FILE));
            jMenu3.add(f9706l).addActionListener(s2);
            jMenu3.add(f9707m).addActionListener(s2);
            jMenu3.add(f9708n).addActionListener(s2);
            jMenu3.add(f9709o).addActionListener(s2);
            if (!C1806i.a().a("OIJFDSFDSAPOFS")) {
                jMenu3.add(f9710p).addActionListener(s2);
            }
            jMenu2.add((JMenuItem) jMenu3);
            if (this.f9697K.size() > 0) {
                jMenu2.add(f9711q).addActionListener(s2);
            }
            if (r().size() > 0) {
                JMenu jMenu4 = new JMenu(C1818g.b("Order"));
                jMenu4.add(f9701g).addActionListener(s2);
                jMenu4.add(f9702h).addActionListener(s2);
                jMenu4.add(f9703i).addActionListener(s2);
                jMenu4.add(f9704j).addActionListener(s2);
                jMenu2.add((JMenuItem) jMenu4);
            }
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(f9718x);
            jCheckBoxMenuItem.setState(this.f9722O);
            jCheckBoxMenuItem.addActionListener(new L(this));
            jMenu2.add((JMenuItem) jCheckBoxMenuItem);
            interfaceC1579bq.add(jMenu2);
        }
        if (P()) {
            JMenuItem jMenuItem = new JMenuItem(C1818g.b("Full Screen"));
            jMenuItem.setSelected(s());
            jMenuItem.addActionListener(new M(this));
            interfaceC1579bq.add(jMenuItem);
        }
        if (C1806i.a().a(";LFDS;LFDS0943;L")) {
            JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem(f9715u);
            jCheckBoxMenuItem2.setState(B());
            jCheckBoxMenuItem2.addActionListener(new N(this));
            interfaceC1579bq.add(jCheckBoxMenuItem2);
            if (!C1806i.a().a("sesrhsfghyuf754")) {
                jCheckBoxMenuItem2.setEnabled(false);
            }
        }
        return interfaceC1579bq;
    }

    public Component f() {
        if (this.f9697K.size() > 0) {
            return (Component) this.f9697K.get(0);
        }
        return null;
    }

    public F g() {
        if (!this.f9696J && this.f9724Q != null) {
            return this.f9724Q;
        }
        F f2 = new F();
        f2.b(getName());
        f2.d(this.f9727S);
        if (G.T.a().c() != null) {
            f2.a(G.T.a().c().i());
        }
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (getComponent(i2) instanceof TuneViewComponent) {
                f2.add((TuneViewComponent) getComponent(i2));
            }
        }
        try {
            f2.a(new C1615cz().a(this, 800, 450));
        } catch (V.a e2) {
            Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (IOException e3) {
            Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        return f2;
    }

    public void a(String str) {
        if (str.equals(f9706l)) {
            c();
        } else if (str.equals(f9707m)) {
            n();
        } else if (str.equals(f9708n)) {
            o();
        } else if (str.equals(f9709o)) {
            p();
        } else if (str.equals(f9710p)) {
            q();
        } else if (str.equals(f9712r)) {
            Q();
        } else if (str.equals(f9713s)) {
            h();
        } else if (str.equals(f9714t)) {
            l();
        } else if (str.equals(f9700f)) {
            j();
        } else if (str.equals(f9699e)) {
            k();
        }
        Component componentF = f();
        if (componentF == null) {
            repaint();
            return;
        }
        if (str.equals(f9701g)) {
            a(componentF);
        } else if (str.equals(f9702h)) {
            b(componentF);
        } else if (str.equals(f9703i)) {
            d(componentF);
        } else if (str.equals(f9704j)) {
            c(componentF);
        } else if ((componentF instanceof TuneViewComponent) && str.equals(f9711q)) {
            d();
        }
        repaint();
    }

    private void Q() {
        String[] strArr = {C1798a.cp};
        String strW = w() == null ? "*." + C1798a.cp : w();
        String strA = bV.a((Component) this, C1818g.b("Save Tuning View"), strArr, "*." + C1798a.cp, C1807j.k().getAbsolutePath(), false);
        if (strA == null) {
            return;
        }
        if (!strA.toLowerCase().endsWith("." + C1798a.cp.toLowerCase())) {
            strA = strA + "." + C1798a.cp;
        }
        b(strA);
        C1807j.e(new File(strA));
    }

    public void h() {
        if (w() == null) {
            Q();
        } else {
            a(w(), (String) null);
            this.f9698L = true;
        }
    }

    public void b(String str) {
        a(str, (String) null);
    }

    public void a(String str, String str2) {
        if (str == null || str.trim().equals("")) {
            return;
        }
        C1887g c1887g = new C1887g();
        try {
            F fG = g();
            File file = new File(str);
            synchronized (this) {
                c1887g.a(fG, file);
            }
        } catch (V.a e2) {
            bV.d("Error saving Tuning View.\n" + e2.getMessage() + "\nCheck Log for more details", this);
        }
    }

    public TuneViewComponent[] i() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (getComponent(i2) instanceof TuneViewComponent) {
                arrayList.add(getComponent(i2));
            }
        }
        TuneViewComponent[] tuneViewComponentArr = new TuneViewComponent[arrayList.size()];
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            tuneViewComponentArr[i3] = (TuneViewComponent) arrayList.get(i3);
        }
        return tuneViewComponentArr;
    }

    public void j() {
        String strShowInputDialog = JOptionPane.showInputDialog(this, "Change Tuning View Name", getName());
        if (strShowInputDialog != null) {
            setName(strShowInputDialog);
        }
    }

    public void k() {
        String strShowInputDialog = JOptionPane.showInputDialog(this, "Tuning View Enable Condition", A() != null ? A() : "");
        if (strShowInputDialog != null) {
            d(strShowInputDialog);
        }
    }

    protected void l() {
        m();
    }

    public void m() {
        C1429b c1429b = new C1429b();
        String[] strArrD = G.T.a().d();
        String[] strArr = new String[strArrD.length];
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            G.R rC = G.T.a().c(strArrD[i2]);
            arrayList.add(rC);
            strArr[i2] = rC.i();
        }
        V v2 = new V(this, c1429b, strArr);
        new O(this, arrayList, c1429b, this).start();
        Window windowB = bV.b(this);
        Window window = windowB;
        if (!(window instanceof Dialog) && !(window instanceof Frame)) {
            window = null;
        }
        C1609ct c1609ct = new C1609ct(window, c1429b, "Select Tuning View", v2, 7);
        if (v2 != null) {
            c1609ct.a(v2);
        }
        c1609ct.setSize(640, NNTPReply.AUTHENTICATION_REQUIRED);
        bV.a((Component) windowB, (Component) c1609ct);
        c1609ct.setVisible(true);
    }

    public void a(F f2) {
        this.f9724Q = f2;
        if (this.f9696J) {
            R();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void R() {
        close();
        Z z2 = new Z();
        z2.a();
        Iterator it = this.f9724Q.iterator();
        while (it.hasNext()) {
            try {
                a((TuneViewComponent) it.next());
            } catch (V.a e2) {
                Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        d(this.f9724Q.e());
        validate();
        repaint();
        bH.C.d("Loaded TuningView Components for " + getName() + " in " + z2.d() + "ms.");
    }

    public void n() {
        try {
            a((TuneViewComponent) new SelectableTable());
        } catch (V.a e2) {
        }
        validate();
    }

    public void o() {
        try {
            a((TuneViewComponent) new TuneViewGaugeCluster());
        } catch (V.a e2) {
        }
        validate();
    }

    public void p() {
        try {
            a((TuneViewComponent) new TableCellCrossHair());
        } catch (V.a e2) {
        }
        validate();
    }

    public void q() {
        try {
            a((TuneViewComponent) new BurnButtonTv());
        } catch (V.a e2) {
        }
        validate();
    }

    public void a(Component component) {
        int componentZOrder = getComponentZOrder(component);
        if (componentZOrder <= 0 || componentZOrder >= getComponentCount() - 1) {
            return;
        }
        add(component, componentZOrder - 1);
    }

    public void b(Component component) {
        int componentZOrder = getComponentZOrder(component);
        if (componentZOrder < 0 || componentZOrder >= getComponentCount() - 1) {
            return;
        }
        add(component, componentZOrder + 1);
    }

    public void c(Component component) {
        add(component, getComponentCount() - 1);
    }

    public void d(Component component) {
        add(component, 0);
    }

    public ArrayList r() {
        return this.f9697K;
    }

    public boolean s() {
        return this.f9688D != null;
    }

    public void t() throws HeadlessException {
        if (this.f9688D == null) {
            bH.C.c("FullScreen");
            v();
        } else {
            bH.C.c("End FullScreen");
            u();
        }
    }

    public void u() {
        if (this.f9689E == null || this.f9688D == null) {
            return;
        }
        this.f9689E.add(this);
        doLayout();
        this.f9689E.validate();
        if (bH.I.c()) {
        }
        this.f9688D.dispose();
        this.f9688D = null;
        this.f9690c = false;
    }

    public void a(Rectangle rectangle) {
        Component[] components = getComponents();
        TuneViewComponent tuneViewComponent = null;
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof TuneViewComponent) {
                TuneViewComponent tuneViewComponent2 = (TuneViewComponent) components[i2];
                Rectangle bounds = tuneViewComponent2.getBounds();
                if (bounds.f12372x > rectangle.f12372x && bounds.f12373y > rectangle.f12373y && bounds.f12372x + bounds.width < rectangle.f12372x + rectangle.width && bounds.f12373y + bounds.height < rectangle.f12373y + rectangle.height) {
                    a(tuneViewComponent2, false);
                    if (tuneViewComponent == null) {
                        tuneViewComponent = tuneViewComponent2;
                    }
                }
            }
        }
        if (tuneViewComponent != null) {
            tuneViewComponent.requestFocus();
        }
    }

    public boolean c(TuneViewComponent tuneViewComponent) {
        return this.f9697K.contains(tuneViewComponent);
    }

    public void v() throws HeadlessException {
        if (!this.f9721N || s()) {
            return;
        }
        this.f9689E = getParent();
        this.f9688D = new U(this, bV.a(this));
        this.f9688D.setLayout(new BorderLayout());
        GraphicsDevice defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Point location = MouseInfo.getPointerInfo().getLocation();
        Rectangle rectangleA = C1630dn.a(location.f12370x, location.f12371y);
        this.f9688D.setBounds(rectangleA.f12372x, rectangleA.f12373y, rectangleA.width, rectangleA.height);
        JPanel jPanel = new JPanel();
        jPanel.setDoubleBuffered(true);
        jPanel.setLayout(new GridLayout(1, 1));
        jPanel.add(this);
        this.f9688D.add(BorderLayout.CENTER, jPanel);
        if (bH.I.c()) {
            bH.J.a(this.f9688D);
            bH.J.b(this.f9688D);
        } else if (!bH.I.a() && defaultScreenDevice.isFullScreenSupported()) {
            defaultScreenDevice.setFullScreenWindow(this.f9688D);
        }
        this.f9688D.setVisible(true);
        this.f9688D.validate();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public TuneViewComponent a(int i2, int i3) {
        Iterator it = this.f9697K.iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            int x2 = (tuneViewComponent.getX() + tuneViewComponent.getWidth()) - this.f9687C;
            int y2 = (tuneViewComponent.getY() + tuneViewComponent.getHeight()) - this.f9687C;
            int x3 = tuneViewComponent.getX() + tuneViewComponent.getWidth();
            int y3 = tuneViewComponent.getY() + tuneViewComponent.getHeight();
            if (i2 >= x2 && i2 <= x3 && i3 >= y2 && i3 <= y3) {
                return tuneViewComponent;
            }
        }
        Component[] components = getComponents();
        for (int componentCount = getComponentCount() - 1; componentCount >= 0; componentCount--) {
            if (components[componentCount] instanceof TuneViewComponent) {
                TuneViewComponent tuneViewComponent2 = (TuneViewComponent) components[componentCount];
                int x4 = tuneViewComponent2.getX();
                int y4 = tuneViewComponent2.getY();
                int x5 = tuneViewComponent2.getX() + tuneViewComponent2.getWidth();
                int y5 = tuneViewComponent2.getY() + tuneViewComponent2.getHeight();
                if (tuneViewComponent2 instanceof aO) {
                    aO aOVar = (aO) tuneViewComponent2;
                    if (i2 >= x4 && i2 <= x5 && i3 >= y4 && i3 <= y5 && aOVar.isComponentPaintedAt(i2 - x4, i3 - y4)) {
                        return tuneViewComponent2;
                    }
                } else if (i2 >= x4 && i2 <= x5 && i3 >= y4 && i3 <= y5) {
                    return tuneViewComponent2;
                }
            }
        }
        return null;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return this.f9691F != null ? this.f9691F : super.getPreferredSize();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return this.f9691F != null ? this.f9691F : super.getMinimumSize();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        bH.C.c("Closing TuneViewPanel " + getName());
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (getComponent(i2) instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) getComponent(i2)).close();
            }
        }
        removeAll();
    }

    @Override // java.awt.Component
    public String getName() {
        String name = super.getName();
        if (name == null || name.isEmpty()) {
            name = "TuningPanel" + ((int) (Math.random() * 200.0d));
        }
        return name;
    }

    public String w() {
        return this.f9723P;
    }

    public void c(String str) {
        this.f9723P = str;
    }

    @Override // n.InterfaceC1761a
    public boolean a() {
        this.f9696J = true;
        if (this.f9724Q != null && getComponentCount() == 0) {
            SwingUtilities.invokeLater(new P(this));
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(M());
        return true;
    }

    @Override // n.g
    public void b() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(M());
    }

    protected Image x() {
        if (this.f9725R == null) {
            try {
                this.f9725R = cO.a().a(cO.f11107w);
                this.f9725R = eJ.a(this.f9725R, this);
            } catch (V.a e2) {
                Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return this.f9725R;
    }

    public void y() {
        z();
        this.f9726z = new X(this);
        this.f9726z.start();
        this.f9695I = null;
    }

    public void z() {
        if (this.f9726z != null) {
            this.f9726z.a();
            this.f9726z = null;
        }
        repaint();
    }

    public String A() {
        return this.f9727S;
    }

    public void d(String str) {
        this.f9727S = str;
    }

    public void e(String str) {
        this.f9695I = str;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setMinimumSize(Dimension dimension) {
        this.f9691F = dimension;
    }

    public boolean B() {
        return this.f9692G;
    }

    public void a(boolean z2) {
        if (z2) {
            this.f9698L = false;
        }
        this.f9692G = z2;
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (getComponent(i2) instanceof TuneViewComponent) {
                ((TuneViewComponent) getComponent(i2)).enableEditMode(z2);
            }
        }
        validate();
        repaint();
    }

    public void C() {
        this.f9697K.clear();
    }

    public void a(TuneViewComponent tuneViewComponent, boolean z2) {
        if (z2) {
            this.f9697K.clear();
        }
        if (this.f9697K.contains(tuneViewComponent) && !z2) {
            this.f9697K.remove(tuneViewComponent);
        } else {
            this.f9697K.add(tuneViewComponent);
            tuneViewComponent.requestFocus();
        }
    }

    public void D() {
        Iterator it = this.f9697K.iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            tuneViewComponent.setSize(tuneViewComponent.getWidth() + 1, tuneViewComponent.getHeight());
            tuneViewComponent.updateRelativeBoundsToCurrent();
            tuneViewComponent.validate();
        }
    }

    public void E() {
        Iterator it = this.f9697K.iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            tuneViewComponent.setSize(tuneViewComponent.getWidth() - 1, tuneViewComponent.getHeight());
            tuneViewComponent.updateRelativeBoundsToCurrent();
            tuneViewComponent.validate();
        }
    }

    public void F() {
        Iterator it = this.f9697K.iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            tuneViewComponent.setSize(tuneViewComponent.getWidth(), tuneViewComponent.getHeight() + 1);
            tuneViewComponent.updateRelativeBoundsToCurrent();
            tuneViewComponent.validate();
        }
    }

    public void G() {
        Iterator it = this.f9697K.iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            tuneViewComponent.setSize(tuneViewComponent.getWidth(), tuneViewComponent.getHeight() - 1);
            tuneViewComponent.updateRelativeBoundsToCurrent();
            tuneViewComponent.validate();
        }
    }

    public void H() {
        Iterator it = this.f9697K.iterator();
        while (it.hasNext()) {
            ((TuneViewComponent) it.next()).validate();
        }
    }

    public void I() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            tuneViewComponent.setLocation(tuneViewComponent.getLocation().f12370x + 1, tuneViewComponent.getLocation().f12371y);
            tuneViewComponent.updateRelativeBoundsToCurrent();
        }
    }

    public void J() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            tuneViewComponent.setLocation(tuneViewComponent.getLocation().f12370x - 1, tuneViewComponent.getLocation().f12371y);
            tuneViewComponent.updateRelativeBoundsToCurrent();
        }
    }

    public void K() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            tuneViewComponent.setLocation(tuneViewComponent.getLocation().f12370x, tuneViewComponent.getLocation().f12371y - 1);
            tuneViewComponent.updateRelativeBoundsToCurrent();
        }
    }

    public void L() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            tuneViewComponent.setLocation(tuneViewComponent.getLocation().f12370x, tuneViewComponent.getLocation().f12371y + 1);
            tuneViewComponent.updateRelativeBoundsToCurrent();
        }
    }

    public R M() {
        if (this.f9693H == null) {
            this.f9693H = new R(this, this);
        }
        return this.f9693H;
    }

    public void N() {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof TuneViewComponent) {
                if (f() == null) {
                    a((TuneViewComponent) components[i2], true);
                    repaint();
                    return;
                } else if (!r().contains(components[i2])) {
                    continue;
                } else if (components.length > i2 + 1 && (components[i2 + 1] instanceof TuneViewComponent)) {
                    a((TuneViewComponent) components[i2 + 1], true);
                    repaint();
                    return;
                } else if (components[0] instanceof TuneViewComponent) {
                    a((TuneViewComponent) components[0], true);
                    repaint();
                    return;
                }
            }
        }
    }

    public boolean O() {
        for (TuneViewComponent tuneViewComponent : i()) {
            if (tuneViewComponent.isDirty()) {
                return false;
            }
        }
        return this.f9698L;
    }

    public void b(boolean z2) {
        for (TuneViewComponent tuneViewComponent : i()) {
            tuneViewComponent.setClean(z2);
        }
        this.f9698L = z2;
        this.f9695I = null;
    }

    public boolean P() {
        return this.f9721N && C1806i.a().a("d67nhtrbd4es8j");
    }

    public void a(InterfaceC1440m interfaceC1440m) {
        if (this.f9694d.contains(interfaceC1440m)) {
            return;
        }
        this.f9694d.add(interfaceC1440m);
    }

    private boolean b(String str, String str2) {
        Iterator it = this.f9694d.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1440m) it.next()).a(str, str2)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.awt.Component
    public void setName(String str) {
        if (b(getName(), str)) {
            super.setName(str);
        }
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }
}
