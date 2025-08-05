package com.efiAnalytics.apps.ts.dashboard;

import G.C0048ah;
import G.C0051ak;
import G.C0113cs;
import G.C0126i;
import G.InterfaceC0109co;
import G.bS;
import bH.C1018z;
import com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter;
import com.efiAnalytics.apps.ts.dashboard.renderers.IndicatorPainter;
import com.efiAnalytics.ui.C1580br;
import com.efiAnalytics.ui.C1609ct;
import com.efiAnalytics.ui.C1630dn;
import com.efiAnalytics.ui.InterfaceC1535a;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1579bq;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dQ;
import com.efiAnalytics.ui.dR;
import com.efiAnalytics.ui.eJ;
import d.InterfaceC1712d;
import i.C1743c;
import i.InterfaceC1741a;
import i.InterfaceC1749i;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import r.C1798a;
import r.C1799b;
import r.C1806i;
import r.C1807j;
import r.C1810m;
import s.C1818g;
import t.C1837aj;
import t.C1875w;
import v.C1883c;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/x.class */
public class C1425x extends JComponent implements G.aF, G.aG, InterfaceC1390ac, InterfaceC1565bc {

    /* renamed from: aj, reason: collision with root package name */
    private Image f9558aj;

    /* renamed from: ak, reason: collision with root package name */
    private Image f9559ak;

    /* renamed from: al, reason: collision with root package name */
    private Image f9560al;

    /* renamed from: a, reason: collision with root package name */
    Color f9561a;

    /* renamed from: b, reason: collision with root package name */
    Vector f9562b;

    /* renamed from: c, reason: collision with root package name */
    long f9563c;

    /* renamed from: d, reason: collision with root package name */
    double f9564d;

    /* renamed from: e, reason: collision with root package name */
    float f9565e;

    /* renamed from: f, reason: collision with root package name */
    X f9566f;

    /* renamed from: g, reason: collision with root package name */
    W f9567g;

    /* renamed from: h, reason: collision with root package name */
    Q f9568h;

    /* renamed from: am, reason: collision with root package name */
    private int f9569am;

    /* renamed from: an, reason: collision with root package name */
    private Window f9570an;

    /* renamed from: ao, reason: collision with root package name */
    private Container f9571ao;

    /* renamed from: i, reason: collision with root package name */
    boolean f9572i;

    /* renamed from: ap, reason: collision with root package name */
    private String f9573ap;

    /* renamed from: aq, reason: collision with root package name */
    private Image f9574aq;

    /* renamed from: ar, reason: collision with root package name */
    private String f9575ar;

    /* renamed from: as, reason: collision with root package name */
    private Dimension f9576as;

    /* renamed from: at, reason: collision with root package name */
    private boolean f9577at;

    /* renamed from: au, reason: collision with root package name */
    private T f9578au;

    /* renamed from: av, reason: collision with root package name */
    private boolean f9579av;

    /* renamed from: aw, reason: collision with root package name */
    private boolean f9580aw;

    /* renamed from: j, reason: collision with root package name */
    W.ap f9581j;

    /* renamed from: ax, reason: collision with root package name */
    private boolean f9582ax;

    /* renamed from: ay, reason: collision with root package name */
    private boolean f9583ay;

    /* renamed from: k, reason: collision with root package name */
    G.R f9584k;

    /* renamed from: az, reason: collision with root package name */
    private boolean f9585az;

    /* renamed from: l, reason: collision with root package name */
    S f9586l;

    /* renamed from: aA, reason: collision with root package name */
    private boolean f9587aA;

    /* renamed from: aB, reason: collision with root package name */
    private Z f9588aB;

    /* renamed from: aC, reason: collision with root package name */
    private Z f9589aC;

    /* renamed from: aD, reason: collision with root package name */
    private String f9590aD;

    /* renamed from: aE, reason: collision with root package name */
    private C1875w f9591aE;

    /* renamed from: aF, reason: collision with root package name */
    private ArrayList f9592aF;

    /* renamed from: aG, reason: collision with root package name */
    private boolean f9593aG;

    /* renamed from: aH, reason: collision with root package name */
    private String f9594aH;

    /* renamed from: aI, reason: collision with root package name */
    private String f9595aI;

    /* renamed from: aJ, reason: collision with root package name */
    private Color f9596aJ;

    /* renamed from: m, reason: collision with root package name */
    boolean f9597m;

    /* renamed from: aK, reason: collision with root package name */
    private String f9601aK;

    /* renamed from: aN, reason: collision with root package name */
    private boolean f9639aN;

    /* renamed from: aO, reason: collision with root package name */
    private boolean f9640aO;

    /* renamed from: aP, reason: collision with root package name */
    private boolean f9641aP;

    /* renamed from: aQ, reason: collision with root package name */
    private boolean f9642aQ;

    /* renamed from: aR, reason: collision with root package name */
    private boolean f9643aR;

    /* renamed from: Z, reason: collision with root package name */
    N f9644Z;

    /* renamed from: aS, reason: collision with root package name */
    private boolean f9645aS;

    /* renamed from: aT, reason: collision with root package name */
    private boolean f9646aT;

    /* renamed from: aU, reason: collision with root package name */
    private final ArrayList f9647aU;

    /* renamed from: aV, reason: collision with root package name */
    private final ArrayList f9648aV;

    /* renamed from: aW, reason: collision with root package name */
    private final ArrayList f9649aW;

    /* renamed from: aX, reason: collision with root package name */
    private final List f9650aX;

    /* renamed from: aY, reason: collision with root package name */
    private float f9651aY;

    /* renamed from: aZ, reason: collision with root package name */
    private float f9652aZ;

    /* renamed from: ba, reason: collision with root package name */
    private boolean f9653ba;

    /* renamed from: bb, reason: collision with root package name */
    private double f9654bb;

    /* renamed from: bc, reason: collision with root package name */
    private double f9655bc;

    /* renamed from: aa, reason: collision with root package name */
    long f9656aa;

    /* renamed from: bd, reason: collision with root package name */
    private boolean f9658bd;

    /* renamed from: ac, reason: collision with root package name */
    HashMap f9659ac;

    /* renamed from: ad, reason: collision with root package name */
    List f9660ad;

    /* renamed from: ae, reason: collision with root package name */
    InterfaceC1741a f9661ae;

    /* renamed from: af, reason: collision with root package name */
    InterfaceC1749i f9662af;

    /* renamed from: ag, reason: collision with root package name */
    int f9663ag;

    /* renamed from: ah, reason: collision with root package name */
    Stroke f9664ah;

    /* renamed from: n, reason: collision with root package name */
    public static final String f9598n = C1818g.b("Stretch");

    /* renamed from: o, reason: collision with root package name */
    public static final String f9599o = C1818g.b("Tile");

    /* renamed from: p, reason: collision with root package name */
    public static final String f9600p = C1818g.b(BorderLayout.CENTER);

    /* renamed from: q, reason: collision with root package name */
    public static final String f9602q = C1818g.b("Bring Forward");

    /* renamed from: r, reason: collision with root package name */
    public static final String f9603r = C1818g.b("Send Backward");

    /* renamed from: s, reason: collision with root package name */
    public static final String f9604s = C1818g.b("Bring to Front");

    /* renamed from: t, reason: collision with root package name */
    public static final String f9605t = C1818g.b("Send to Back");

    /* renamed from: u, reason: collision with root package name */
    public static final String f9606u = C1818g.b("Gauge Properties");

    /* renamed from: v, reason: collision with root package name */
    public static final String f9607v = C1818g.b("Add Gauge");

    /* renamed from: w, reason: collision with root package name */
    public static final String f9608w = C1818g.b("Add Indicator");

    /* renamed from: x, reason: collision with root package name */
    public static final String f9609x = C1818g.b("Add Label");

    /* renamed from: y, reason: collision with root package name */
    public static final String f9610y = C1818g.b("Add Settings Panel");

    /* renamed from: z, reason: collision with root package name */
    public static final String f9611z = C1818g.b("Add Tuning Table");

    /* renamed from: A, reason: collision with root package name */
    public static final String f9612A = C1818g.b("Remove From Dash");

    /* renamed from: B, reason: collision with root package name */
    public static final String f9613B = C1818g.b("Start Gauge Demo");

    /* renamed from: C, reason: collision with root package name */
    public static final String f9614C = C1818g.b("Stop Gauge Demo");

    /* renamed from: D, reason: collision with root package name */
    public static final String f9615D = C1818g.b("Set Background Color");

    /* renamed from: E, reason: collision with root package name */
    public static final String f9616E = C1818g.b("Set Background Dither Color");

    /* renamed from: F, reason: collision with root package name */
    public static final String f9617F = C1818g.b("Set Background Image");

    /* renamed from: G, reason: collision with root package name */
    public static final String f9618G = C1818g.b("Clear Background Image");

    /* renamed from: H, reason: collision with root package name */
    public static final String f9619H = C1818g.b("Export / Save As");

    /* renamed from: I, reason: collision with root package name */
    public static final String f9620I = C1818g.b("Save Dashboard");

    /* renamed from: J, reason: collision with root package name */
    public static final String f9621J = C1818g.b("Load Dashboard");

    /* renamed from: K, reason: collision with root package name */
    public static final String f9622K = C1818g.b("Reload Default Gauges");

    /* renamed from: L, reason: collision with root package name */
    public static final String f9623L = C1818g.b("Load a Saved Gauge");

    /* renamed from: M, reason: collision with root package name */
    public static final String f9624M = C1818g.b("Save this Gauge");

    /* renamed from: N, reason: collision with root package name */
    public static final String f9625N = C1818g.b("Designer Mode");

    /* renamed from: O, reason: collision with root package name */
    public static final String f9626O = C1818g.b("Copy");

    /* renamed from: P, reason: collision with root package name */
    public static final String f9627P = C1818g.b("Paste");

    /* renamed from: Q, reason: collision with root package name */
    public static final String f9628Q = C1818g.b("Paste Other");

    /* renamed from: R, reason: collision with root package name */
    public static final String f9629R = C1818g.b("Paste Gauge Theme");

    /* renamed from: S, reason: collision with root package name */
    public static final String f9630S = C1818g.b("Paste Gauge Size");

    /* renamed from: T, reason: collision with root package name */
    public static final String f9631T = C1818g.b("Antialiasing Enabled");

    /* renamed from: U, reason: collision with root package name */
    public static final String f9632U = C1818g.b("Group Selected Gauges");

    /* renamed from: V, reason: collision with root package name */
    public static final String f9633V = C1818g.b("Un-Group Gauges");

    /* renamed from: W, reason: collision with root package name */
    public static final String f9634W = C1818g.b("Show Grid");

    /* renamed from: X, reason: collision with root package name */
    public static final String f9635X = C1818g.b("Force Aspect Ratio");

    /* renamed from: aL, reason: collision with root package name */
    private static String f9636aL = "fullScreenIndex";

    /* renamed from: Y, reason: collision with root package name */
    public static final String f9637Y = f9625N;

    /* renamed from: aM, reason: collision with root package name */
    private static String f9638aM = ((Object) C1807j.y()) + File.separator + ".tempGauge";

    /* renamed from: ab, reason: collision with root package name */
    public static int f9657ab = 3500;

    /* renamed from: ai, reason: collision with root package name */
    static int f9665ai = 0;

    public C1425x(G.R r2) {
        this();
        a(r2);
    }

    public C1425x() {
        this.f9558aj = null;
        this.f9559ak = null;
        this.f9560al = null;
        this.f9561a = new Color(0, 255, 0, 0);
        this.f9562b = new Vector();
        this.f9563c = System.currentTimeMillis();
        this.f9564d = 0.0d;
        this.f9565e = 1.0f;
        this.f9566f = null;
        this.f9567g = new W(this);
        this.f9568h = new Q(this);
        this.f9569am = eJ.a(7);
        this.f9570an = null;
        this.f9571ao = null;
        this.f9572i = false;
        this.f9573ap = "";
        this.f9574aq = null;
        this.f9575ar = null;
        this.f9576as = null;
        this.f9577at = false;
        this.f9578au = null;
        this.f9579av = true;
        this.f9580aw = false;
        this.f9581j = null;
        this.f9582ax = false;
        this.f9583ay = false;
        this.f9584k = null;
        this.f9585az = false;
        this.f9586l = null;
        this.f9587aA = false;
        this.f9588aB = null;
        this.f9589aC = null;
        this.f9590aD = null;
        this.f9591aE = null;
        this.f9592aF = new ArrayList();
        this.f9593aG = true;
        this.f9594aH = null;
        this.f9595aI = null;
        this.f9596aJ = Color.gray;
        this.f9597m = C1806i.a().a("sesrhsfghyuf754");
        this.f9601aK = f9599o;
        this.f9639aN = false;
        this.f9640aO = false;
        this.f9641aP = false;
        this.f9642aQ = true;
        this.f9643aR = false;
        this.f9644Z = new N(this);
        this.f9645aS = false;
        this.f9646aT = true;
        this.f9647aU = new ArrayList();
        this.f9648aV = new ArrayList();
        this.f9649aW = new ArrayList();
        this.f9650aX = new ArrayList();
        this.f9651aY = 0.32f;
        this.f9652aZ = 24.0f;
        this.f9653ba = false;
        this.f9654bb = 16.0d;
        this.f9655bc = 9.0d;
        this.f9656aa = -1L;
        this.f9658bd = true;
        this.f9659ac = new HashMap();
        this.f9660ad = new ArrayList();
        this.f9661ae = new C1426y(this);
        this.f9662af = new F(this);
        this.f9663ag = eJ.a(2);
        this.f9664ah = new BasicStroke(this.f9663ag);
        setFocusable(true);
        if (C1018z.i().b()) {
            setLayout(new Y());
            addMouseListener(this.f9568h);
            addMouseMotionListener(this.f9568h);
            setDoubleBuffered(true);
            setFocusable(true);
            this.f9639aN = C1798a.a().a(C1798a.ck, "false").equals("true");
            this.f9652aZ = C1798a.a().a(C1798a.f13350aF, this.f9652aZ);
            this.f9651aY = C1798a.a().a(C1798a.f13351aG, this.f9651aY);
            this.f9566f = new X(this);
            this.f9566f.start();
        }
        setDoubleBuffered(false);
        setOpaque(false);
    }

    public void a(G.R r2) {
        if (this.f9584k != null) {
            this.f9584k.C().c(this);
            this.f9584k.C().b((G.aG) this);
        }
        this.f9584k = r2;
        if (this.f9584k != null) {
            r2.C().a((G.aF) this);
            r2.C().a((G.aG) this);
        }
        if (C1806i.a().a(" a09kmfds098432lkg89vlk")) {
            C1743c.a().a(this.f9661ae);
            C1743c.a().a(this.f9662af);
        }
        if (r2.C().q()) {
            ab();
        } else {
            k(C1818g.b("Off Line"));
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1390ac
    public void a(AbstractC1420s abstractC1420s) {
        if (this.f9566f != null && (this.f9584k == null || this.f9582ax || this.f9583ay)) {
            this.f9566f.c();
        }
        if (this.f9585az) {
            return;
        }
        repaint();
    }

    public void b(AbstractC1420s abstractC1420s) {
        if (this.f9566f == null || !this.f9566f.isAlive()) {
            this.f9566f = new X(this);
            this.f9566f.start();
        }
        super.add(abstractC1420s);
        if (C1806i.a().a("09BDPO;L,;l;ldpo;l5 ") && (abstractC1420s instanceof InterfaceC1712d)) {
            abstractC1420s.addMouseListener(this.f9644Z);
        }
        abstractC1420s.setGaugeContainer(this);
        if (isEnabled()) {
            if (abstractC1420s instanceof Gauge) {
                Gauge gauge = (Gauge) abstractC1420s;
                gauge.setValue(gauge.max());
            }
            if (abstractC1420s instanceof InterfaceC1421t) {
                abstractC1420s.setFocusable(true);
                abstractC1420s.enableInputMethods(true);
            } else {
                abstractC1420s.addMouseListener(this.f9567g);
                abstractC1420s.addMouseMotionListener(this.f9567g);
                abstractC1420s.setFocusable(false);
            }
            if (this.f9658bd) {
                abstractC1420s.subscribeToOutput();
            }
        }
        abstractC1420s.setAntialiasingOn(M());
    }

    public void a() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            c((AbstractC1420s) it.next());
        }
        this.f9559ak = null;
        repaint();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void c(AbstractC1420s abstractC1420s) {
        abstractC1420s.removeMouseListener(this.f9567g);
        abstractC1420s.removeMouseMotionListener(this.f9567g);
        remove(abstractC1420s);
        if (abstractC1420s instanceof InterfaceC0109co) {
            C0113cs.a().a((InterfaceC0109co) abstractC1420s);
        }
    }

    @Override // java.awt.Container
    public void removeAll() {
        Component[] components = getComponents();
        int componentCount = getComponentCount();
        for (int i2 = 0; i2 < componentCount; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                c((AbstractC1420s) components[i2]);
            }
        }
        super.removeAll();
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        super.setBackground(color);
        this.f9558aj = null;
        this.f9574aq = null;
        if (getParent() != null) {
            getParent().setBackground(color);
        }
    }

    private boolean ah() {
        return getComponentCount() > 0 && !(getComponent(0) instanceof HtmlDisplay);
    }

    private void ai() {
        Iterator it = this.f9660ad.iterator();
        while (it.hasNext()) {
            ((aN) it.next()).a();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        long jNanoTime = System.nanoTime();
        boolean z2 = D() && ah();
        if (z2) {
            a(graphics, D(), z2);
        } else {
            Image imageAC = aC();
            if (imageAC == null) {
                return;
            }
            a(imageAC.getGraphics(), D(), z2);
            graphics.drawImage(imageAC, 0, 0, null);
        }
        b(graphics);
        if (L()) {
            String str = "Width: " + getWidth() + ", Height: " + getHeight();
            int iStringWidth = getFontMetrics(graphics.getFont()).stringWidth(str) + 8;
            int width = (getWidth() - iStringWidth) - 10;
            int size = getFont().getSize();
            graphics.setColor(Color.DARK_GRAY);
            graphics.fillRect(width, 0, iStringWidth, size);
            graphics.setColor(Color.WHITE);
            graphics.drawString(str, width + 4, size);
        }
        a(graphics);
        long jNanoTime2 = System.nanoTime() - this.f9563c;
        double dNanoTime = (((System.nanoTime() - jNanoTime) / 1000000.0d) * 1000000.0d) / jNanoTime2;
        if (getComponentCount() > 0) {
            if ((getComponent(0) instanceof Gauge) || (getComponent(0) instanceof Indicator)) {
                float fRound = Math.round(1.0E9d / jNanoTime2) * 5.0f;
                this.f9564d = ((fRound * this.f9564d) + (1.0E9d / jNanoTime2)) / (fRound + 1.0f);
                this.f9565e = ((fRound * this.f9565e) + (((float) dNanoTime) * 100.0f)) / (fRound + 1.0f);
            }
            float f2 = this.f9577at ? 0.12f : s() ? 0.9f : this.f9651aY;
            float fB = s() ? 95.0f : this.f9652aZ;
            if (K.c.a().b() > 5.0d && fB > 1.05d / K.c.a().b()) {
                fB = (float) (1.05d * K.c.a().b());
            }
            if (dNanoTime > f2 || this.f9564d > fB + 5.0f) {
                this.f9566f.b();
            } else if (this.f9564d <= fB) {
                this.f9566f.a();
            }
        }
        if (this.f9639aN) {
            graphics.setFont(new Font("Arial Unicode MS", 1, eJ.a(13)));
            graphics.setColor(Color.CYAN);
            graphics.drawString(bH.W.b(this.f9564d, 1) + " fps, Core%: " + bH.W.b(this.f9565e, 1), 5, 15);
        }
        this.f9563c = System.nanoTime();
    }

    private void a(Graphics graphics, boolean z2) {
        if ((z2 || L()) && (this.f9601aK.equals(f9600p) || this.f9574aq == null || this.f9573ap == null || this.f9573ap.equals(""))) {
            Color background = getBackground();
            if (this.f9596aJ == null || !this.f9597m || this.f9596aJ.getAlpha() <= 0) {
                graphics.setColor(getBackground());
            } else {
                ((Graphics2D) graphics).setPaint(new GradientPaint(new Point2D.Float(getWidth() / 3, (getHeight() * 5) / 6), background, new Point2D.Float(getWidth() / 3, getHeight() / 6), this.f9596aJ, true));
            }
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }
        if (this.f9573ap != null && !this.f9573ap.equals("") && this.f9574aq == null) {
            this.f9574aq = Toolkit.getDefaultToolkit().getImage(this.f9573ap);
        }
        if ((z2 || L()) && this.f9574aq != null && this.f9574aq.getWidth(null) > 0) {
            if (this.f9601aK.equals(f9598n)) {
                graphics.drawImage(this.f9574aq, 0, 0, getWidth(), getHeight(), null);
            } else if (this.f9601aK.equals(f9600p)) {
                if (getWidth() > this.f9574aq.getWidth(null)) {
                    graphics.drawImage(this.f9574aq, (getWidth() - this.f9574aq.getWidth(null)) / 2, (getHeight() - this.f9574aq.getHeight(null)) / 2, null);
                } else {
                    double width = getWidth() / this.f9574aq.getWidth(null);
                    graphics.drawImage(this.f9574aq, 0, (getHeight() - ((int) (this.f9574aq.getHeight(null) * width))) / 2, (int) (this.f9574aq.getWidth(null) * width), (int) (this.f9574aq.getHeight(null) * width), null);
                }
            } else if (!this.f9601aK.equals(f9599o) || this.f9574aq.getWidth(null) <= 0) {
                bH.C.c("Shouldn't be here. backgroundImage.getWidth(null)=" + this.f9574aq.getWidth(null) + "\n\tbackgroundImageStyle=" + this.f9601aK + "\n");
            } else {
                int width2 = this.f9574aq.getWidth(null);
                int height = this.f9574aq.getHeight(null);
                for (int i2 = 0; i2 * width2 < getWidth(); i2++) {
                    for (int i3 = 0; i3 * height < getHeight(); i3++) {
                        graphics.drawImage(this.f9574aq, i2 * width2, i3 * height, null);
                    }
                }
            }
        }
        if (this.f9642aQ && L()) {
            int iA = eJ.a(10);
            Color background2 = getBackground();
            graphics.setColor(new Color(255 - background2.getRed(), 255 - background2.getGreen(), 255 - background2.getBlue(), 64));
            for (int i4 = 1; i4 * iA < getWidth(); i4++) {
                graphics.drawLine(i4 * iA, 0, i4 * iA, getHeight());
            }
            for (int i5 = 1; i5 * iA < getHeight(); i5++) {
                graphics.drawLine(0, i5 * iA, getWidth(), i5 * iA);
            }
        }
    }

    private void a(Graphics graphics, boolean z2, boolean z3) {
        try {
            if (z3) {
                Image imageAD = aD();
                Graphics2D graphics2D = (Graphics2D) imageAD.getGraphics();
                if (aj()) {
                    graphics2D.setColor(this.f9561a);
                    graphics2D.setComposite(AlphaComposite.getInstance(2));
                    graphics2D.fillRect(0, 0, imageAD.getWidth(null), imageAD.getHeight(null));
                    graphics2D.setComposite(AlphaComposite.getInstance(3));
                    if (imageAD.getWidth(null) < 0) {
                        System.out.print("break 214312");
                    }
                    a((Graphics) graphics2D, true);
                    c(graphics2D);
                    this.f9646aT = false;
                }
                graphics.drawImage(imageAD, 0, 0, null);
                d(graphics);
            } else {
                a(graphics, z2);
                b(graphics, z2);
            }
        } catch (Exception e2) {
            bH.C.c("Exception in GaugeCluster paint");
            e2.printStackTrace();
        }
    }

    @Override // javax.swing.JComponent
    public void paintChildren(Graphics graphics) {
        b(graphics, !this.f9585az);
        if (!this.f9585az) {
            super.paintChildren(graphics);
        }
        ai();
    }

    private void a(Graphics graphics) {
        if (this.f9568h.a() != null) {
            Rectangle rectangleA = this.f9568h.a();
            graphics.setColor(getBackground());
            graphics.setXORMode(Color.YELLOW);
            int iAbs = Math.abs(rectangleA.width);
            int iAbs2 = Math.abs(rectangleA.height);
            int i2 = rectangleA.width < 0 ? rectangleA.f12372x + rectangleA.width : rectangleA.f12372x;
            int i3 = rectangleA.height < 0 ? rectangleA.f12373y + rectangleA.height : rectangleA.f12373y;
            ((Graphics2D) graphics).setStroke(new BasicStroke(eJ.a(1), 0, 2, 0.0f, new float[]{2.0f, 1.0f}, 0.0f));
            graphics.drawRect(i2, i3, iAbs, iAbs2);
        }
    }

    private void b(Graphics graphics) {
        String strAa;
        String strB;
        Font font;
        FontMetrics fontMetrics;
        if (L()) {
            return;
        }
        boolean z2 = false;
        if (this.f9584k == null || this.f9584k.C().q() || (this.f9656aa <= System.currentTimeMillis() && !C1810m.a().b())) {
            strAa = aa();
            strB = this.f9595aI;
        } else {
            strAa = null;
            strB = C1818g.b("Log Playback");
            z2 = true;
        }
        if ((strAa == null || strAa.equals("")) && (strB == null || strB.equals(""))) {
            return;
        }
        if (getComponentCount() <= 0 || !this.f9582ax) {
            if (this.f9560al == null || this.f9560al.getWidth(null) != getWidth() || this.f9560al.getHeight(null) != getHeight()) {
                this.f9560al = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), 3);
                Graphics2D graphics2D = (Graphics2D) this.f9560al.getGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                if (strAa != null && !strAa.equals("")) {
                    graphics2D.setColor(new Color(64, 64, 64, 160));
                    graphics2D.fillRect(0, 0, getWidth(), getHeight());
                }
                graphics2D.setColor(new Color(210, 210, 210, 239));
                int iA = z2 ? eJ.a(16) : eJ.a(28);
                if (strAa == null || strAa.equals("")) {
                    strAa = strB;
                }
                String[] strArrSplit = strAa.split("\n");
                while (true) {
                    font = new Font("Times", 1, iA);
                    graphics2D.setFont(font);
                    fontMetrics = getFontMetrics(font);
                    iA--;
                    if (fontMetrics.stringWidth(strArrSplit[0]) <= getWidth() - 4 && iA != 0) {
                        break;
                    }
                }
                int i2 = iA / 2;
                int height = z2 ? 2 : getHeight() / 12;
                int iStringWidth = fontMetrics.stringWidth(strArrSplit[0]) + i2;
                Font font2 = new Font(font.getFamily(), 0, (int) (font.getSize() * 0.6d));
                if (strArrSplit.length > 1) {
                    for (int i3 = 1; i3 < strArrSplit.length; i3++) {
                        int iStringWidth2 = getFontMetrics(font2).stringWidth(strArrSplit[i3]) + i2;
                        if (iStringWidth2 > iStringWidth) {
                            iStringWidth = iStringWidth2;
                        }
                    }
                }
                graphics2D.fillRect((((getWidth() - iStringWidth) + i2) / 2) - (i2 / 2), height, iStringWidth, (int) (fontMetrics.getHeight() + (i2 / 2) + (fontMetrics.getHeight() * 0.6d * (strArrSplit.length - 0.5d))));
                graphics2D.setColor(new Color(16, 16, 32));
                for (int i4 = 0; i4 < strArrSplit.length; i4++) {
                    if (i4 == 1) {
                        graphics2D.setFont(font2);
                    }
                    graphics2D.drawString(strArrSplit[i4], (getWidth() - graphics2D.getFontMetrics().stringWidth(strArrSplit[i4])) / 2, height + fontMetrics.getHeight());
                    height += graphics2D.getFontMetrics().getHeight();
                }
            }
            graphics.drawImage(this.f9560al, 0, 0, null);
        }
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        if (!aE.a.A().E().c().equals(str)) {
            return true;
        }
        this.f9564d = 6.0d;
        ab();
        S s2 = this.f9586l;
        if (s2 != null) {
            s2.a();
        }
        this.f9585az = true;
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        if (aE.a.A().E() != null && aE.a.A().E().c().equals(str)) {
            b();
            this.f9585az = false;
        }
        G.R rC = G.T.a().c(str);
        if (rC == null || rC.C().O()) {
            k(C1818g.b("Off Line"));
        } else {
            k(C1818g.b("Connected to Dash Echo server that is Off Line"));
        }
    }

    public void b() {
        if (C1798a.a().a(C1798a.f13308P, C1798a.f13309Q) && C1743c.a().d()) {
            return;
        }
        this.f9582ax = false;
        AbstractC1420s[] abstractC1420sArrJ = j();
        if (!B()) {
            this.f9583ay = false;
            for (AbstractC1420s abstractC1420s : abstractC1420sArrJ) {
                if (abstractC1420s instanceof Gauge) {
                    Gauge gauge = (Gauge) abstractC1420s;
                    if (gauge.min() >= 0.0d || gauge.max() <= 0.0d) {
                        gauge.setValue(gauge.min());
                    } else {
                        gauge.setValue(0.0d);
                    }
                    gauge.invalidate();
                } else if (abstractC1420s instanceof Indicator) {
                    Indicator indicator = (Indicator) abstractC1420s;
                    if (indicator.getEcuConfigurationName() != null && !indicator.getEcuConfigurationName().equals(C0113cs.f1154a)) {
                        indicator.setValue(0.0d);
                    }
                    indicator.setRunDemo(false);
                }
            }
            return;
        }
        for (AbstractC1420s abstractC1420s2 : abstractC1420sArrJ) {
            if (abstractC1420s2 instanceof Gauge) {
                ((Gauge) abstractC1420s2).invalidate();
            } else if (abstractC1420s2 instanceof Indicator) {
                Indicator indicator2 = (Indicator) abstractC1420s2;
                if (indicator2.getEcuConfigurationName() != null && !indicator2.getEcuConfigurationName().equals(C0113cs.f1154a)) {
                    indicator2.goDead();
                } else if (indicator2.isRunDemo()) {
                    indicator2.setRunDemo(false);
                }
            }
        }
        this.f9583ay = true;
        if (this.f9586l != null) {
            this.f9586l.f9396b = true;
        }
        this.f9586l = new S(this);
        this.f9586l.f9395a = false;
        this.f9586l.start();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void update(Graphics graphics) {
        bH.C.c("Update Called");
        paint(graphics);
    }

    private boolean a(Component component, int i2) {
        Component[] components = getComponents();
        for (int i3 = i2; i3 < components.length; i3++) {
            if (!components[i3].equals(component) && a(components[i3], component)) {
                return true;
            }
        }
        return false;
    }

    private boolean a(Component component, Component component2) {
        return new Rectangle(component.getX(), component.getY(), component.getWidth(), component.getHeight()).intersects(new Rectangle(component2.getX(), component2.getY(), component2.getWidth(), component2.getHeight())) && !component2.equals(component);
    }

    private void b(Graphics graphics, boolean z2) {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                AbstractC1420s abstractC1420s = (AbstractC1420s) components[i2];
                if (!D() && abstractC1420s.isMustPaint()) {
                    e(true);
                    bH.C.c("Deactivated fast paint " + getName());
                }
                if (abstractC1420s.isDirty() || z2 || L() || abstractC1420s.isRunDemo() || abstractC1420s.requiresBackgroundRepaint()) {
                    Graphics graphicsCreate = graphics.create(abstractC1420s.getX(), abstractC1420s.getY(), abstractC1420s.getWidth(), abstractC1420s.getHeight());
                    graphicsCreate.setClip(f(abstractC1420s));
                    abstractC1420s.paint(graphicsCreate);
                    graphicsCreate.dispose();
                    abstractC1420s.setDirty(false);
                }
                if (this.f9592aF.contains(abstractC1420s)) {
                    a(graphics, abstractC1420s);
                }
            }
        }
    }

    private boolean aj() {
        if (this.f9646aT) {
            return true;
        }
        Component[] components = getComponents();
        if (components.length == 0) {
            return true;
        }
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= components.length) {
                break;
            }
            if (components[i2] instanceof AbstractC1420s) {
                AbstractC1420s abstractC1420s = (AbstractC1420s) components[i2];
                if (0 == 0 && abstractC1420s.requiresBackgroundRepaint()) {
                    z2 = true;
                    break;
                }
            }
            i2++;
        }
        return z2;
    }

    private void c(Graphics graphics) {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                AbstractC1420s abstractC1420s = (AbstractC1420s) components[i2];
                Graphics graphicsCreate = graphics.create(abstractC1420s.getX(), abstractC1420s.getY(), abstractC1420s.getWidth(), abstractC1420s.getHeight());
                abstractC1420s.paintBackground(graphicsCreate);
                graphicsCreate.dispose();
                abstractC1420s.validate();
            }
        }
    }

    private void d(Graphics graphics) {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                AbstractC1420s abstractC1420s = (AbstractC1420s) components[i2];
                if (abstractC1420s.getWidth() > 0 && abstractC1420s.getHeight() > 0) {
                    Graphics2D graphics2D = (Graphics2D) graphics.create(abstractC1420s.getX(), abstractC1420s.getY(), abstractC1420s.getWidth(), abstractC1420s.getHeight());
                    graphics2D.setClip(f(abstractC1420s));
                    abstractC1420s.updateGauge(graphics2D);
                    graphics2D.dispose();
                    abstractC1420s.setDirty(false);
                }
                if (this.f9592aF.contains(abstractC1420s)) {
                    a(graphics, abstractC1420s);
                }
            }
        }
        ai();
    }

    private Area f(AbstractC1420s abstractC1420s) {
        Area area = (Area) this.f9659ac.get(abstractC1420s);
        if (area == null || L()) {
            area = new Area(new Rectangle(0, 0, abstractC1420s.getWidth(), abstractC1420s.getHeight()));
            boolean z2 = false;
            for (AbstractC1420s abstractC1420s2 : j()) {
                if (z2) {
                    Area areaAreaPainted = abstractC1420s2.areaPainted();
                    int x2 = abstractC1420s2.getX() - abstractC1420s.getX();
                    int y2 = abstractC1420s2.getY() - abstractC1420s.getY();
                    AffineTransform affineTransform = new AffineTransform();
                    affineTransform.translate(x2, y2);
                    areaAreaPainted.transform(affineTransform);
                    area.subtract(areaAreaPainted);
                } else if (abstractC1420s2.equals(abstractC1420s)) {
                    z2 = true;
                }
            }
            this.f9659ac.put(abstractC1420s, area);
        }
        return area;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
        this.f9659ac.clear();
    }

    public void c() {
        for (Object obj : getComponents()) {
            if (obj instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) obj).close();
            }
        }
        removeAll();
        i(C1798a.a().a(C1798a.f13368aX, f9618G));
        j(f9600p);
        this.f9574aq = eJ.a(this.f9574aq);
        this.f9575ar = "";
        if (this.f9584k != null) {
            this.f9584k.C().c(this);
            this.f9584k.C().b((G.aG) this);
        }
        if (this.f9566f != null) {
            this.f9566f.e();
            this.f9566f = null;
        }
        e(false);
        C1743c.a().b(this.f9661ae);
        C1743c.a().b(this.f9662af);
    }

    private void a(Graphics graphics, AbstractC1420s abstractC1420s) {
        if (L()) {
            int x2 = abstractC1420s.getX();
            int y2 = abstractC1420s.getY();
            int width = abstractC1420s.getWidth();
            int height = abstractC1420s.getHeight();
            int i2 = this.f9569am - (this.f9663ag / 2);
            if (width <= 0 || height <= 0) {
                return;
            }
            if (M()) {
                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2D.setStroke(this.f9664ah);
            }
            graphics.setFont(getFont());
            graphics.setColor(Color.yellow);
            graphics.drawLine(x2 + i2, y2 + i2, (x2 + width) - i2, (y2 + height) - i2);
            graphics.drawLine(x2 + i2, (y2 + height) - i2, (x2 + width) - i2, y2 + i2);
            graphics.fillRect(x2, y2, this.f9569am, this.f9569am);
            graphics.fillRect(x2, (y2 + height) - this.f9569am, this.f9569am, this.f9569am);
            graphics.fillRect((x2 + width) - this.f9569am, y2, this.f9569am, this.f9569am);
            graphics.setColor(Color.red);
            graphics.fillRect((x2 + width) - this.f9569am, (y2 + height) - this.f9569am, this.f9569am, this.f9569am);
            String str = "X: " + x2 + ", Y:" + y2 + ", Width: " + width + ", Height: " + height;
            graphics.setColor(Color.DARK_GRAY);
            graphics.fillRect(x2 + 10, y2, graphics.getFontMetrics().stringWidth(str) + 10, graphics.getFont().getSize() + 2);
            graphics.setColor(Color.WHITE);
            graphics.drawString(str, x2 + eJ.a(15), (y2 + graphics.getFont().getSize()) - 1);
        }
    }

    public void a(Component component, int i2, int i3) {
        if (!C1798a.a().a(C1798a.da, C1798a.db) && 0 == 0) {
            JPopupMenu jPopupMenuD = d();
            add(jPopupMenuD);
            if (component != null) {
                jPopupMenuD.show(component, i2, i3);
            } else {
                jPopupMenuD.show(this, i2, i3);
            }
        }
    }

    public void a(boolean z2) {
        this.f9646aT = z2;
    }

    public JPopupMenu d() {
        C1580br c1580br = new C1580br();
        a((InterfaceC1579bq) c1580br);
        return c1580br;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:138:0x07d7 A[PHI: r13
  0x07d7: PHI (r13v11 int) = (r13v10 int), (r13v12 int) binds: [B:131:0x07a4, B:133:0x07ae] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Type inference failed for: r0v347, types: [javax.swing.JMenu] */
    /* JADX WARN: Type inference failed for: r7v0, types: [com.efiAnalytics.ui.bq] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.efiAnalytics.ui.InterfaceC1579bq a(com.efiAnalytics.ui.InterfaceC1579bq r7) {
        /*
            Method dump skipped, instructions count: 3352
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.efiAnalytics.apps.ts.dashboard.C1425x.a(com.efiAnalytics.ui.bq):com.efiAnalytics.ui.bq");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ak() {
        Iterator it = this.f9592aF.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof SingleChannelDashComponent) {
                SingleChannelDashComponent singleChannelDashComponent = (SingleChannelDashComponent) abstractC1420s;
                try {
                    C0126i.a(singleChannelDashComponent.getEcuConfigurationName(), singleChannelDashComponent.getOutputChannel());
                } catch (V.g e2) {
                    Logger.getLogger(C1425x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        }
    }

    private boolean al() {
        Iterator it = this.f9592aF.iterator();
        while (it.hasNext()) {
            if (it.next() instanceof Indicator) {
                return true;
            }
        }
        return false;
    }

    private boolean am() {
        Iterator it = this.f9592aF.iterator();
        while (it.hasNext()) {
            if (it.next() instanceof Gauge) {
                return true;
            }
        }
        return false;
    }

    public void b(boolean z2) {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            Component component = getComponent(i2);
            if (component instanceof AbstractC1420s) {
                ((AbstractC1420s) component).setRunDemo(z2);
            }
            if ((component instanceof Gauge) && z2) {
                Gauge gauge = (Gauge) component;
                gauge.setValue(gauge.min());
            }
        }
        if (!z2 || this.f9582ax) {
            if (z2) {
                return;
            }
            this.f9582ax = false;
            b();
            return;
        }
        this.f9582ax = z2;
        if (this.f9582ax) {
            if (this.f9586l != null) {
                this.f9586l.a();
            }
            this.f9583ay = false;
            this.f9586l = new S(this);
            this.f9586l.start();
        }
    }

    public void e() {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            Component component = getComponent(i2);
            if (!(component instanceof Gauge) && (component instanceof Indicator)) {
                Indicator indicator = (Indicator) component;
                indicator.setOffText(C1818g.b(indicator.getOffText().toString()));
                indicator.setOnText(C1818g.b(indicator.getOnText().toString()));
            }
        }
    }

    public void f() {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            Component component = getComponent(i2);
            if (!(component instanceof Gauge) && (component instanceof Indicator)) {
            }
        }
    }

    public Component g() {
        if (this.f9592aF.size() > 0) {
            return (Component) this.f9592aF.get(0);
        }
        return null;
    }

    public void b(String str) {
        if (str.equals(f9607v)) {
            m();
        } else if (str.equals(f9608w)) {
            p();
        } else if (str.equals(f9609x)) {
            q();
        } else if (str.equals(f9610y)) {
            n();
        } else if (str.equals(f9611z)) {
            o();
        } else if (str.equals(f9615D)) {
            ay();
        } else if (str.equals(f9616E)) {
            az();
        } else if (str.equals(f9617F)) {
            aA();
        } else if (str.equals(f9619H)) {
            at();
        } else if (str.equals(f9620I)) {
            h();
        } else if (str.equals(f9621J)) {
            k();
        } else if (str.equals(f9623L)) {
            ax();
        } else if (str.equals(f9618G)) {
            l();
        } else if (str.equals(f9613B)) {
            b(true);
        } else if (str.equals(f9614C)) {
            b(false);
        } else if (str.equals(f9626O)) {
            ap();
        } else if (str.equals(f9627P)) {
            aq();
        } else if (str.equals(f9629R)) {
            as();
        } else if (str.equals(f9630S)) {
            ar();
        } else if (str.equals(f9622K)) {
            aF();
        }
        Component componentG = g();
        if (componentG == null) {
            repaint();
            return;
        }
        if (str.equals(f9602q)) {
            a(componentG);
        } else if (str.equals(f9603r)) {
            b(componentG);
        } else if (str.equals(f9604s)) {
            d(componentG);
        } else if (str.equals(f9605t)) {
            c(componentG);
        } else if (str.equals(f9632U)) {
            ao();
        } else if (str.equals(f9633V)) {
            an();
        } else if (componentG instanceof AbstractC1420s) {
            if (str.equals(f9612A)) {
                a();
            } else if (str.equals(f9624M)) {
                a(r());
            } else if (componentG instanceof Gauge) {
                Gauge gauge = (Gauge) componentG;
                if (str.equals(f9606u)) {
                    d((AbstractC1420s) gauge);
                }
            }
        }
        this.f9559ak = null;
        repaint();
    }

    private void an() {
        Iterator it = this.f9592aF.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setGroupId(0);
            }
        }
    }

    private void ao() {
        int iRandom = (int) (Math.random() * 2.147483647E9d);
        Iterator it = this.f9592aF.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setGroupId(iRandom);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ap() {
        a(r(), f9638aM);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void aq() {
        try {
            a(f9638aM, 0.06d, 0.06d);
        } catch (V.a e2) {
            bH.C.d("no valid DashboardComponent");
        }
    }

    private void ar() {
        try {
            ArrayList arrayListB = new C1883c(C1807j.G()).b(new File(f9638aM).getAbsolutePath());
            if (arrayListB.isEmpty()) {
                bV.d("No Component Copied", this);
                return;
            }
            AbstractC1420s abstractC1420s = (AbstractC1420s) arrayListB.get(0);
            Iterator it = r().iterator();
            while (it.hasNext()) {
                AbstractC1420s abstractC1420s2 = (AbstractC1420s) it.next();
                abstractC1420s2.setRelativeHeight(abstractC1420s.getRelativeHeight());
                abstractC1420s2.setRelativeWidth(abstractC1420s.getRelativeWidth());
            }
            doLayout();
            a(true);
            repaint();
        } catch (V.a e2) {
            bH.C.d("no valid DashboardComponent");
        }
    }

    private void as() {
        try {
            Gauge gaugeA = C1388aa.a(new File(f9638aM));
            if (gaugeA != null) {
                Iterator it = r().iterator();
                while (it.hasNext()) {
                    AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
                    if (abstractC1420s instanceof Gauge) {
                        C1388aa.a(gaugeA, (Gauge) abstractC1420s);
                    }
                }
            } else {
                bV.d(C1818g.b("You must copy a Gauge before applying the Theme."), this);
            }
        } catch (V.a e2) {
            bH.C.d("no valid DashboardComponent");
        }
    }

    private void at() {
        String[] strArr = {C1798a.co};
        String strAd = ad() == null ? "*." + C1798a.co : ad();
        String strA = bV.a((Component) this, C1818g.b("Save Dashboard Layout"), strArr, "*." + C1798a.co, C1807j.j().getAbsolutePath(), false);
        if (strA == null) {
            return;
        }
        if (!strA.toLowerCase().endsWith("." + C1798a.co)) {
            strA = strA + "." + C1798a.co;
        }
        d(strA);
        C1807j.e(new File(strA));
    }

    public void c(String str) {
        if (ad() == null) {
            at();
        } else {
            a(ad(), str);
            this.f9593aG = true;
        }
    }

    public void h() {
        if (ad() == null) {
            at();
        } else {
            a(ad(), (String) null);
            this.f9593aG = true;
        }
    }

    public void d(String str) {
        a(str, (String) null);
    }

    public void a(String str, String str2) {
        if (str == null || str.trim().equals("")) {
            return;
        }
        bH.Z z2 = new bH.Z();
        z2.a();
        C1883c c1883c = new C1883c(C1807j.G());
        try {
            boolean z3 = this.f9582ax;
            b(false);
            if (z3) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(C1425x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            Z zI = i();
            if (str2 != null) {
                zI.b(str2);
            }
            synchronized (this) {
                c1883c.a(str, zI);
            }
            bH.C.c("Time to save dashboard: " + z2.d() + "ms. file: " + str);
        } catch (V.a e3) {
            bV.d("Error saving Dashboard.\n" + e3.getMessage() + "\nCheck Log for more details", this);
        }
    }

    private void a(ArrayList arrayList) {
        String[] strArr = {C1798a.cq};
        if (aE.a.A() == null) {
            bV.d("There is no project open.\nPlease open a project first.", this);
        }
        String strA = bV.a((Component) this, "Save Gauge", strArr, "*." + C1798a.cq, C1807j.j().getAbsolutePath(), false);
        if (strA == null) {
            return;
        }
        if (!strA.toLowerCase().endsWith("." + C1798a.cq)) {
            strA = strA + "." + C1798a.cq;
        }
        C1807j.e(new File(strA).getParentFile());
        a(arrayList, strA);
    }

    private void a(ArrayList arrayList, String str) {
        try {
            new C1883c(C1807j.G()).a(str, arrayList);
        } catch (V.a e2) {
            bV.d("Error saving Gauge.\n" + e2.getMessage() + "\nCheck Log for more details", this);
        }
    }

    public Z i() {
        b(false);
        Z z2 = new Z();
        z2.a(getBackground());
        String strJ = J();
        if (strJ != null && !strJ.equals("")) {
            strJ = C1807j.c(strJ);
        }
        z2.a(strJ);
        z2.c(K());
        z2.b(C());
        z2.b(au());
        z2.b(this.f9653ba);
        z2.b(I());
        z2.a(H());
        z2.a(getComponents());
        z2.a(this.f9641aP);
        return z2;
    }

    public AbstractC1420s[] j() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (getComponent(i2) instanceof AbstractC1420s) {
                arrayList.add(getComponent(i2));
            }
        }
        AbstractC1420s[] abstractC1420sArr = new AbstractC1420s[arrayList.size()];
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            abstractC1420sArr[i3] = (AbstractC1420s) arrayList.get(i3);
        }
        return abstractC1420sArr;
    }

    private String au() {
        try {
            return this.f9584k.i();
        } catch (Exception e2) {
            bH.C.b("Could not get Firmware signature");
            return null;
        }
    }

    public boolean e(String str) throws HeadlessException {
        if (this.f9593aG) {
            return true;
        }
        int iShowConfirmDialog = JOptionPane.showConfirmDialog(this, (getName() == null || getName().equals("") || !C.a.a().c().getLanguage().equals(C1818g.c().getLanguage())) ? C1818g.b("This Project's dashboard layout has been modified.") + "\n" + C1818g.b("Would you like to save the changes to this project?") : "The dashboard layout for " + getName() + " has been modified.\nWould you like to save the changes to this project?", C1818g.b("Save Dashboard?"), 1, 3);
        if (iShowConfirmDialog == 2) {
            return false;
        }
        if (iShowConfirmDialog != 0) {
            return iShowConfirmDialog == 1 ? true : true;
        }
        f();
        if (str == null || str.equals("")) {
            h();
            return true;
        }
        d(str);
        return true;
    }

    protected void k() {
        a((InterfaceC1535a) null);
    }

    public void a(InterfaceC1535a interfaceC1535a) {
        C1799b c1799b = new C1799b();
        new C1388aa();
        String[] strArrD = G.T.a().d();
        String strC = this.f9584k.c();
        String[] strArr = new String[strArrD.length];
        if (this.f9588aB != null) {
            c1799b.a(C1818g.b(Action.DEFAULT), this.f9588aB);
        }
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            G.R rC = G.T.a().c(strArrD[i2]);
            if (rC.S()) {
                Z zB = b(rC);
                if (this.f9588aB == null) {
                    if (ag() != null) {
                        c1799b.a(ag());
                    } else {
                        c1799b.a();
                    }
                    if (!strC.equals(rC.c())) {
                        zB.d(rC.c());
                    }
                    c1799b.a(rC.c(), zB);
                }
            }
            strArr[i2] = rC.i();
        }
        c1799b.a(strArr, r.p.a().b());
        R r2 = new R(this, c1799b, strArr);
        Window windowB = bV.b(this);
        Window window = windowB;
        if (!(window instanceof Dialog) && !(window instanceof Frame)) {
            window = null;
        }
        C1609ct c1609ct = new C1609ct(window, c1799b, "Select Dashboard", r2, 7);
        if (interfaceC1535a != null) {
            c1609ct.a(interfaceC1535a);
        }
        bV.a((Component) windowB, (Component) c1609ct);
        c1609ct.setVisible(true);
    }

    public void f(String str) {
        Z zA;
        C1883c c1883c = new C1883c(C1807j.G());
        try {
            zA = c1883c.a(str);
        } catch (Exception e2) {
            e2.printStackTrace();
            bV.d("Error Loading Dashboard file.\n" + str + "\nloading default gauge cluster.", this);
            zA = new C1388aa().a(this.f9584k, "FrontPage", 1);
            c1883c.a(str, zA);
        }
        String strI = this.f9584k.i();
        r.o oVarB = r.p.a().b();
        if (C1806i.a().a("GD;';'RE;'GD;'DG") || strI == null || oVarB.a(strI, zA.d()) || C1388aa.a(this.f9584k, zA)) {
            this.f9593aG = true;
        } else {
            if (!bV.a("Warning: Gauge Cluster firmware signature '" + zA.d() + "'\ndoes not match current firmware '" + strI + "'.\nYou may need to reset gauge output channels.\n \nContinue Loading?", (Component) this, true)) {
                k();
                this.f9575ar = str;
                h();
                return;
            }
            k(false);
        }
        this.f9575ar = str;
        a(zA);
        validate();
    }

    public void a(Z z2) {
        G.R rC;
        if (C1806i.a().a("0rewporewo430932") && (rC = G.T.a().c()) != null) {
            new C1388aa().b(rC, z2);
        }
        g(false);
        g(z2.h());
        a(z2.i());
        b(z2.j());
        if (getParent() != null) {
            getParent().doLayout();
        }
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                c((AbstractC1420s) components[i2]);
            }
        }
        Component[] componentArrC = z2.c();
        j(z2.f());
        String str = "";
        int i3 = 0;
        for (int i4 = 0; i4 < componentArrC.length; i4++) {
            try {
                if (componentArrC[i4] != null) {
                    b((AbstractC1420s) componentArrC[i4]);
                } else {
                    bH.C.b("A null gauge was tried to be added?\nThis is wierd.");
                }
            } catch (V.a e2) {
                int i5 = i3;
                i3++;
                if (i5 < 5) {
                    str = str + e2.getMessage() + "\n";
                }
            }
        }
        setBackground(z2.a());
        i(z2.b());
        j(z2.e());
        a(z2.g());
        b();
        invalidate();
        validate();
        this.f9559ak = null;
        repaint();
        if (!str.equals("")) {
            String str2 = str + "\n\n" + C1818g.b("To correct this:") + "\n" + C1818g.b("- right click on any Gauge or indicator with a red line through it.") + "\n" + C1818g.b("- Select a valid Gauge or Indicator Template from the menu.");
        }
        e();
        av();
    }

    private void av() {
        e(aw());
        bH.C.c("High Speed Paint: " + (!D()));
    }

    private boolean aw() {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (a(components[i2], i2)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        if (i2 != 32) {
            return super.imageUpdate(image, i2, i3, i4, i5, i6);
        }
        this.f9558aj = null;
        aC().getGraphics();
        return true;
    }

    private void ax() {
        String strA = bV.a((Component) this, C1818g.b("Open Gauge Layout"), new String[]{C1798a.cq}, "*." + C1798a.co, C1807j.h().getAbsolutePath(), true);
        if (strA == null) {
            return;
        }
        try {
            o(strA);
        } catch (V.a e2) {
            bV.d("Error loading Gauge.\n" + e2.getMessage() + "\nCheck Log for more details", this);
        }
    }

    private void o(String str) {
        a(str, 0.0d, 0.0d);
    }

    private void a(String str, double d2, double d3) {
        ArrayList arrayListB = new C1883c(C1807j.G()).b(str);
        for (int i2 = 0; i2 < arrayListB.size(); i2++) {
            try {
                if (d2 != 0.0d && ((AbstractC1420s) arrayListB.get(i2)).getRelativeX() + d2 < 0.97d) {
                    ((AbstractC1420s) arrayListB.get(i2)).setRelativeX(((AbstractC1420s) arrayListB.get(i2)).getRelativeX() + d2);
                } else if (d2 != 0.0d) {
                    ((AbstractC1420s) arrayListB.get(i2)).setRelativeX(((AbstractC1420s) arrayListB.get(i2)).getRelativeX() - d2);
                }
                if (d3 != 0.0d && ((AbstractC1420s) arrayListB.get(i2)).getRelativeY() + d3 < 0.97d) {
                    ((AbstractC1420s) arrayListB.get(i2)).setRelativeY(((AbstractC1420s) arrayListB.get(i2)).getRelativeY() + d3);
                } else if (d3 != 0.0d) {
                    ((AbstractC1420s) arrayListB.get(i2)).setRelativeY(((AbstractC1420s) arrayListB.get(i2)).getRelativeY() - d3);
                }
                if (arrayListB.get(i2) instanceof Gauge) {
                    Gauge gauge = (Gauge) arrayListB.get(i2);
                    if (gauge.getGroupId() > 0) {
                        gauge.setGroupId(gauge.getGroupId() + 1);
                    }
                }
                b((AbstractC1420s) arrayListB.get(i2));
                ((AbstractC1420s) arrayListB.get(i2)).goDead();
            } catch (V.a e2) {
                bV.d(e2.getMessage(), this);
            }
        }
        this.f9592aF.clear();
        Iterator it = arrayListB.iterator();
        while (it.hasNext()) {
            this.f9592aF.add((AbstractC1420s) it.next());
        }
        aG();
        repaint();
        validate();
    }

    private void ay() {
        setBackground(bV.a(this, C1818g.b("Choose Background Color"), getBackground()));
        k(false);
    }

    private void az() {
        a(bV.a(this, C1818g.b("Choose Background Dither Color"), C()));
        k(false);
        this.f9558aj = null;
    }

    public void l() {
        this.f9574aq = null;
        this.f9573ap = null;
        this.f9558aj = null;
        repaint();
        k(false);
    }

    private void aA() {
        C1837aj c1837aj = new C1837aj(bV.b(this), C1807j.G(), "Select Indicator Off Image");
        bV.a(this, c1837aj);
        c1837aj.a(new D(this));
        c1837aj.setVisible(true);
    }

    public void d(AbstractC1420s abstractC1420s) {
        if (abstractC1420s instanceof Gauge) {
            new C1391ad((Gauge) abstractC1420s).a(this);
        }
    }

    public void m() {
        String strB = C1818g.b("Add Gauges");
        ArrayList arrayList = new ArrayList();
        arrayList.add(C1807j.j());
        arrayList.add(C1807j.h());
        C1408g c1408g = new C1408g(bV.a(this), arrayList, strB);
        c1408g.a(new dQ(C1798a.a().f13332an, strB));
        C1875w.a(c1408g, this);
        c1408g.a(new E(this));
        c1408g.setVisible(true);
    }

    public void n() {
        if (C1806i.a().a("LKMFSLKFDSLK;LKM09")) {
            DashTuningPanel dashTuningPanel = new DashTuningPanel();
            try {
                b((AbstractC1420s) dashTuningPanel);
                a((AbstractC1420s) dashTuningPanel, true);
            } catch (V.a e2) {
            }
            validate();
        }
    }

    public void o() {
        if (C1806i.a().a("LKMFSLKFDSLK;LKM09")) {
            SelectableTableComponent selectableTableComponent = new SelectableTableComponent();
            try {
                b((AbstractC1420s) selectableTableComponent);
                a((AbstractC1420s) selectableTableComponent, true);
            } catch (V.a e2) {
            }
            validate();
        }
    }

    public void p() {
        Indicator indicator = new Indicator();
        try {
            b((AbstractC1420s) indicator);
            a((AbstractC1420s) indicator, true);
        } catch (V.a e2) {
        }
        validate();
    }

    public void q() {
        DashLabel dashLabel = new DashLabel();
        try {
            b((AbstractC1420s) dashLabel);
            a((AbstractC1420s) dashLabel, true);
        } catch (V.a e2) {
        }
        validate();
    }

    public void a(Component component) {
        int componentZOrder = getComponentZOrder(component);
        if (componentZOrder < 0 || componentZOrder >= getComponentCount() - 1) {
            return;
        }
        add(component, componentZOrder + 1);
    }

    public void b(Component component) {
        int componentZOrder = getComponentZOrder(component);
        if (componentZOrder <= 0 || componentZOrder >= getComponentCount() - 1) {
            return;
        }
        add(component, componentZOrder - 1);
    }

    public void c(Component component) {
        add(component, 0);
    }

    public void d(Component component) {
        add(component, getComponentCount() - 1);
    }

    public ArrayList r() {
        return this.f9592aF;
    }

    public void a(Indicator indicator, String str, String str2) {
        C0051ak c0051akN;
        if (str2.equals(C0113cs.f1154a)) {
            c0051akN = I.d.a().c(str);
        } else {
            G.T tA = G.T.a();
            c0051akN = ((str2 == null || str2.length() == 0) ? tA.c() : tA.c(str2)).n(str);
        }
        indicator.setEcuConfigurationName(str2);
        indicator.setOutputChannel(c0051akN.f());
        indicator.setOnText(c0051akN.a().toString());
        indicator.setOffText(c0051akN.d().toString());
        indicator.setShortClickAction(c0051akN.l());
        indicator.setLongClickAction(c0051akN.m());
        try {
            indicator.subscribeToOutput();
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this);
        }
        indicator.setDirty(true);
        indicator.repaint();
        this.f9593aG = false;
    }

    private List a(Gauge gauge) {
        int groupId = gauge.getGroupId();
        ArrayList arrayList = new ArrayList();
        if (groupId == Gauge.f9343Q) {
            arrayList.add(gauge);
            return arrayList;
        }
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (getComponent(i2) instanceof Gauge) {
                Gauge gauge2 = (Gauge) getComponent(i2);
                if (gauge2.getGroupId() == groupId && !arrayList.contains(gauge2)) {
                    arrayList.add(gauge2);
                }
            }
        }
        return arrayList;
    }

    public void a(Gauge gauge, String str, String str2) {
        G.T tA = G.T.a();
        C0048ah c0048ahA = str2.equals(C0113cs.f1154a) ? I.c.a().a(str) : ((str2 == null || str2.length() == 0) ? tA.c() : tA.c(str2)).k(str);
        if (c0048ahA == null) {
            bH.C.c("Gauge:" + str + " not found in current configuration.");
            return;
        }
        for (Gauge gauge2 : a(gauge)) {
            if (!gauge2.title().isEmpty()) {
                gauge2.setTitle(c0048ahA.k().toString());
            }
            gauge2.setEcuConfigurationName(str2);
            gauge2.setUnits(c0048ahA.j().toString());
            gauge2.setMinVP(c0048ahA.b());
            gauge2.setMaxVP(c0048ahA.e());
            if (gauge2.isGoingDead()) {
                gauge2.setValue(c0048ahA.d());
            }
            gauge2.setShortClickAction(c0048ahA.r());
            gauge2.setLongClickAction(c0048ahA.s());
            if (gauge2.getValue() > c0048ahA.d() || gauge2.getValue() < c0048ahA.a()) {
                gauge2.setValue(c0048ahA.a());
                gauge2.invalidate();
            }
            gauge2.setLowWarningVP(c0048ahA.f());
            gauge2.setLowCriticalVP(c0048ahA.o());
            gauge2.setHighWarningVP(c0048ahA.g());
            gauge2.setHighCriticalVP(c0048ahA.h());
            gauge2.setOutputChannel(c0048ahA.i());
            gauge2.setValueDigitsVP(c0048ahA.m());
            gauge2.setLabelDigits(c0048ahA.n());
            try {
                gauge2.subscribeToOutput();
            } catch (V.a e2) {
                bV.d(e2.getMessage(), this);
            }
            gauge2.invalidate();
            gauge2.repaint();
        }
        this.f9593aG = false;
        a(gauge, str);
    }

    private void a(AbstractC1420s abstractC1420s, String str) {
        int i2 = -1;
        int i3 = 0;
        while (true) {
            if (i3 >= getComponentCount()) {
                break;
            }
            if (getComponent(i3).equals(abstractC1420s)) {
                i2 = i3;
                break;
            }
            i3++;
        }
        boolean zA = A();
        Iterator it = this.f9649aW.iterator();
        while (it.hasNext()) {
            ((aG) it.next()).a(i2, str, zA);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void aB() {
        Iterator it = this.f9649aW.iterator();
        while (it.hasNext()) {
            ((aG) it.next()).a();
        }
    }

    public void a(aG aGVar) {
        this.f9649aW.add(aGVar);
    }

    public void g(String str) {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                try {
                    ((Gauge) abstractC1420s).setGaugePainter((GaugePainter) Class.forName(str).newInstance());
                    Gauge gauge = (Gauge) abstractC1420s;
                    gauge.setBackgroundImageFileName(null);
                    gauge.setNeedleImageFileName(null);
                } catch (Exception e2) {
                    bV.d("Error loading Gauge Painter: " + str, this);
                    return;
                }
            }
        }
    }

    public void h(String str) {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                try {
                    ((Indicator) abstractC1420s).setPainter((IndicatorPainter) Class.forName(str).newInstance());
                    abstractC1420s.repaint();
                } catch (Exception e2) {
                    bV.d("Error loading Indicator Painter: " + str, this);
                }
            }
        }
    }

    private Image aC() {
        if (this.f9558aj == null || this.f9558aj.getWidth(null) != getWidth() || this.f9558aj.getHeight(null) != getHeight()) {
            this.f9558aj = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), 3);
            Graphics2D graphics2D = (Graphics2D) this.f9558aj.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            a(this.f9558aj.getGraphics(), true, false);
        }
        return this.f9558aj;
    }

    private Image aD() {
        if (this.f9559ak == null || this.f9559ak.getWidth(null) != getWidth() || this.f9559ak.getHeight(null) != getHeight()) {
            this.f9559ak = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), 3);
            Graphics2D graphics2D = (Graphics2D) this.f9559ak.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            a((Graphics) graphics2D, true);
            c(graphics2D);
        }
        return this.f9559ak;
    }

    public boolean s() {
        return this.f9570an != null || this.f9643aR;
    }

    public void t() throws HeadlessException {
        if (aE()) {
            if (this.f9570an == null) {
                bH.C.c("FullScreen");
                y();
            } else {
                bH.C.c("End FullScreen");
                u();
            }
        }
    }

    public void a(InterfaceC1422u interfaceC1422u) {
        this.f9650aX.add(interfaceC1422u);
    }

    public void b(InterfaceC1422u interfaceC1422u) {
        this.f9650aX.remove(interfaceC1422u);
    }

    private boolean aE() {
        Iterator it = this.f9650aX.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1422u) it.next()).a()) {
                return false;
            }
        }
        return true;
    }

    public void u() {
        if (this.f9571ao == null || this.f9570an == null) {
            return;
        }
        this.f9571ao.add(this);
        super.doLayout();
        this.f9571ao.validate();
        if (bH.I.c()) {
        }
        this.f9570an.dispose();
        this.f9570an = null;
        this.f9572i = false;
        if (this.f9558aj != null) {
            this.f9558aj.flush();
        }
    }

    public Window v() {
        if (this.f9571ao == null || this.f9570an == null) {
            return null;
        }
        this.f9571ao.add(this);
        super.doLayout();
        this.f9571ao.validate();
        try {
            return this.f9570an;
        } finally {
            this.f9570an = null;
            this.f9572i = false;
            if (this.f9558aj != null) {
                this.f9558aj.flush();
            }
        }
    }

    public void w() {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                this.f9592aF.add((AbstractC1420s) components[i2]);
            }
        }
        repaint();
    }

    public void a(Rectangle rectangle) {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                AbstractC1420s abstractC1420s = (AbstractC1420s) components[i2];
                Rectangle bounds = abstractC1420s.getBounds();
                if (bounds.f12372x > rectangle.f12372x && bounds.f12373y > rectangle.f12373y && bounds.f12372x + bounds.width < rectangle.f12372x + rectangle.width && bounds.f12373y + bounds.height < rectangle.f12373y + rectangle.height && !this.f9592aF.contains(abstractC1420s)) {
                    this.f9592aF.add(abstractC1420s);
                }
            }
        }
        aG();
    }

    public boolean e(AbstractC1420s abstractC1420s) {
        return this.f9592aF.contains(abstractC1420s);
    }

    public int x() {
        try {
            return Integer.parseInt(c(f9636aL, "-1"));
        } catch (NumberFormatException e2) {
            return -1;
        }
    }

    public void y() throws HeadlessException {
        a(x());
    }

    public void a(Window window) {
        if (this.f9640aO) {
            if (s()) {
                u();
            }
            this.f9571ao = getParent();
            this.f9570an = window;
            JPanel jPanel = new JPanel();
            jPanel.setDoubleBuffered(true);
            jPanel.setLayout(new bz.b());
            jPanel.setBackground(getBackground());
            jPanel.add(this);
            this.f9570an.add(BorderLayout.CENTER, jPanel);
            this.f9570an.validate();
            this.f9570an.doLayout();
            this.f9558aj = null;
            repaint();
            validate();
            requestFocus();
            this.f9571ao.repaint();
        }
    }

    public void a(int i2) throws HeadlessException {
        GraphicsDevice defaultScreenDevice;
        Rectangle rectangleA;
        if (!this.f9640aO || s()) {
            return;
        }
        this.f9571ao = getParent();
        JDialog jDialog = new JDialog(bV.a(this));
        jDialog.setUndecorated(true);
        this.f9570an = jDialog;
        this.f9570an.setLayout(new BorderLayout());
        if (bH.I.b() || i2 < 0) {
            defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            Point location = MouseInfo.getPointerInfo().getLocation();
            rectangleA = C1630dn.a(location.f12370x, location.f12371y);
        } else {
            defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[i2];
            rectangleA = C1630dn.a(i2);
        }
        this.f9570an.setBounds(rectangleA.f12372x, rectangleA.f12373y, rectangleA.width, rectangleA.height);
        JPanel jPanel = new JPanel();
        jPanel.setDoubleBuffered(true);
        jPanel.setLayout(new bz.b());
        jPanel.setBackground(getBackground());
        jPanel.add(this);
        this.f9570an.add(BorderLayout.CENTER, jPanel);
        if (bH.I.c()) {
            bH.J.a(this.f9570an);
            bH.J.b(this.f9570an);
        } else if (!bH.I.a() && defaultScreenDevice.isFullScreenSupported()) {
            defaultScreenDevice.setFullScreenWindow(this.f9570an);
        }
        this.f9570an.setVisible(true);
        this.f9570an.validate();
        this.f9571ao.repaint();
        b(f9636aL, "" + i2);
    }

    private void b(String str, String str2) {
        if (this.f9581j != null) {
            this.f9581j.a(str, str2);
        }
    }

    private String c(String str, String str2) {
        return this.f9581j != null ? this.f9581j.b(str, str2) : str2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public AbstractC1420s a(int i2, int i3) {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            int x2 = (abstractC1420s.getX() + abstractC1420s.getWidth()) - this.f9569am;
            int y2 = (abstractC1420s.getY() + abstractC1420s.getHeight()) - this.f9569am;
            int x3 = abstractC1420s.getX() + abstractC1420s.getWidth();
            int y3 = abstractC1420s.getY() + abstractC1420s.getHeight();
            if (i2 >= x2 && i2 <= x3 && i3 >= y2 && i3 <= y3) {
                return abstractC1420s;
            }
        }
        Component[] components = getComponents();
        for (int componentCount = getComponentCount() - 1; componentCount >= 0; componentCount--) {
            if (components[componentCount] instanceof AbstractC1420s) {
                AbstractC1420s abstractC1420s2 = (AbstractC1420s) components[componentCount];
                int x4 = abstractC1420s2.getX();
                int y4 = abstractC1420s2.getY();
                int x5 = abstractC1420s2.getX() + abstractC1420s2.getWidth();
                int y5 = abstractC1420s2.getY() + abstractC1420s2.getHeight();
                if (abstractC1420s2 instanceof aO) {
                    aO aOVar = (aO) abstractC1420s2;
                    if (i2 >= x4 && i2 <= x5 && i3 >= y4 && i3 <= y5 && aOVar.isComponentPaintedAt(i2 - x4, i3 - y4)) {
                        return abstractC1420s2;
                    }
                } else if (i2 >= x4 && i2 <= x5 && i3 >= y4 && i3 <= y5) {
                    return abstractC1420s2;
                }
            }
        }
        return null;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return this.f9576as != null ? this.f9576as : super.getPreferredSize();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return this.f9576as != null ? this.f9576as : super.getMinimumSize();
    }

    public void a(dR dRVar) {
        this.f9647aU.add(dRVar);
    }

    private void d(String str, String str2) {
        Iterator it = this.f9647aU.iterator();
        while (it.hasNext()) {
            try {
                ((dR) it.next()).a(str, str2);
            } catch (Exception e2) {
                bH.C.b("Problem encountered while notifying PropertyChangedListener. Ignored and continued.");
                e2.printStackTrace();
            }
        }
    }

    public boolean z() {
        return this.f9579av;
    }

    public void c(boolean z2) {
        this.f9579av = z2;
    }

    @Override // G.aF
    public void a(String str, byte[] bArr) {
        if (!str.equals(this.f9584k.c()) || this.f9566f == null) {
            return;
        }
        this.f9566f.c();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        bH.C.c("Closing Dashboard " + getName());
        c();
    }

    public boolean A() {
        return this.f9645aS;
    }

    public void d(boolean z2) {
        this.f9645aS = z2;
    }

    public boolean B() {
        return C1798a.a().c(C1798a.f13385bo, true);
    }

    public Color C() {
        return this.f9596aJ;
    }

    public void a(Color color) {
        this.f9596aJ = color;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
        super.repaint(j2, i2, i3, i4, i5);
    }

    @Override // java.awt.Component
    public void repaint() {
        super.repaint();
    }

    public boolean D() {
        return this.f9580aw || getComponentCount() == 0;
    }

    public void e(boolean z2) {
        this.f9580aw = z2;
    }

    private void aF() {
        a(b(this.f9584k));
        Iterator it = this.f9649aW.iterator();
        while (it.hasNext()) {
            ((aG) it.next()).a();
        }
    }

    public void E() {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                c((AbstractC1420s) components[i2]);
            }
        }
        i((String) null);
        a((Image) null);
    }

    public void a(W.ap apVar) {
        this.f9581j = apVar;
    }

    public boolean F() {
        return this.f9587aA;
    }

    public void f(boolean z2) {
        this.f9585az = z2;
    }

    public boolean G() {
        return this.f9653ba;
    }

    public void g(boolean z2) {
        this.f9653ba = z2;
    }

    public double H() {
        return this.f9654bb;
    }

    public void a(double d2) {
        this.f9654bb = d2;
    }

    public double I() {
        return this.f9655bc;
    }

    public void b(double d2) {
        this.f9655bc = d2;
    }

    public synchronized void h(boolean z2) {
        if (this.f9658bd ^ z2) {
            if (z2) {
                for (Component component : getComponents()) {
                    if (component instanceof AbstractC1420s) {
                        try {
                            ((AbstractC1420s) component).subscribeToOutput();
                        } catch (V.a e2) {
                        }
                    }
                }
            } else {
                for (Component component2 : getComponents()) {
                    if (component2 instanceof AbstractC1420s) {
                        try {
                            ((AbstractC1420s) component2).unsubscribeToOutput();
                        } catch (V.a e3) {
                            bH.C.b("Failed to subscribe DashComponent: " + e3.getLocalizedMessage());
                        }
                    }
                }
            }
        }
        this.f9658bd = z2;
    }

    public String J() {
        return this.f9573ap == null ? "" : this.f9573ap;
    }

    public void a(Image image) {
        this.f9574aq = image;
    }

    public void i(String str) {
        this.f9573ap = str;
        this.f9574aq = null;
        this.f9558aj = null;
    }

    public String K() {
        return this.f9601aK;
    }

    public void j(String str) {
        if (str != null) {
            if (str.equals(f9600p) || str.equals(f9599o) || str.equals(f9598n)) {
                this.f9601aK = str;
                this.f9558aj = null;
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setMinimumSize(Dimension dimension) {
        this.f9576as = dimension;
    }

    public boolean L() {
        return this.f9577at;
    }

    public void i(boolean z2) {
        if (z2) {
            this.f9593aG = false;
        }
        this.f9577at = z2;
        this.f9558aj = null;
        this.f9559ak = null;
        repaint();
        d(f9637Y, "" + z2);
    }

    public boolean M() {
        return this.f9641aP;
    }

    public void N() {
        this.f9592aF.clear();
        aG();
    }

    public void a(AbstractC1420s abstractC1420s, boolean z2) {
        if (z2) {
            this.f9592aF.clear();
        }
        if (!this.f9592aF.contains(abstractC1420s) || z2) {
            if (!this.f9592aF.contains(abstractC1420s)) {
                this.f9592aF.add(abstractC1420s);
            }
            if (abstractC1420s instanceof Gauge) {
                Gauge gauge = (Gauge) abstractC1420s;
                if (gauge.getGroupId() != Gauge.f9343Q) {
                    b(gauge.getGroupId());
                }
            }
        } else {
            this.f9592aF.remove(abstractC1420s);
        }
        aG();
    }

    private void b(int i2) {
        for (int i3 = 0; i3 < getComponentCount(); i3++) {
            if (getComponent(i3) instanceof Gauge) {
                Gauge gauge = (Gauge) getComponent(i3);
                if (gauge.getGroupId() == i2 && gauge.getGroupId() != Gauge.f9343Q && !this.f9592aF.contains(gauge)) {
                    this.f9592aF.add(gauge);
                }
            }
        }
    }

    public void j(boolean z2) {
        this.f9641aP = z2;
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (getComponent(i2) instanceof AbstractC1420s) {
                ((AbstractC1420s) getComponent(i2)).setAntialiasingOn(z2);
            }
        }
        this.f9558aj = null;
        repaint();
        d("antiAliasingOn", "" + z2);
    }

    public C1875w O() {
        if (this.f9591aE == null) {
            this.f9591aE = new C1875w();
            a((InterfaceC1407f) this.f9591aE);
        }
        return this.f9591aE;
    }

    public void P() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setLocation(abstractC1420s.getLocation().f12370x + 1, abstractC1420s.getLocation().f12371y);
            abstractC1420s.updateRelativeBoundsToCurrent();
        }
        a(true);
    }

    public void Q() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setLocation(abstractC1420s.getLocation().f12370x - 1, abstractC1420s.getLocation().f12371y);
            abstractC1420s.updateRelativeBoundsToCurrent();
        }
        a(true);
    }

    public void R() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setLocation(abstractC1420s.getLocation().f12370x, abstractC1420s.getLocation().f12371y - 1);
            abstractC1420s.updateRelativeBoundsToCurrent();
        }
        a(true);
    }

    public void S() {
        Iterator it = r().iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setLocation(abstractC1420s.getLocation().f12370x, abstractC1420s.getLocation().f12371y + 1);
            abstractC1420s.updateRelativeBoundsToCurrent();
        }
        a(true);
    }

    public void T() {
        Iterator it = this.f9592aF.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setSize(abstractC1420s.getWidth() + 1, abstractC1420s.getHeight());
            abstractC1420s.updateRelativeBoundsToCurrent();
            abstractC1420s.validate();
        }
        a(true);
    }

    public void U() {
        Iterator it = this.f9592aF.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setSize(abstractC1420s.getWidth() - 1, abstractC1420s.getHeight());
            abstractC1420s.updateRelativeBoundsToCurrent();
            abstractC1420s.validate();
        }
    }

    public void V() {
        Iterator it = this.f9592aF.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setSize(abstractC1420s.getWidth(), abstractC1420s.getHeight() + 1);
            abstractC1420s.updateRelativeBoundsToCurrent();
            abstractC1420s.validate();
        }
    }

    public void W() {
        Iterator it = this.f9592aF.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setSize(abstractC1420s.getWidth(), abstractC1420s.getHeight() - 1);
            abstractC1420s.updateRelativeBoundsToCurrent();
            abstractC1420s.validate();
        }
    }

    public T X() {
        if (this.f9578au == null) {
            this.f9578au = new T(this, this);
        }
        return this.f9578au;
    }

    public void Y() {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                if (g() == null) {
                    a((AbstractC1420s) components[i2], true);
                    repaint();
                    return;
                } else if (!r().contains(components[i2])) {
                    continue;
                } else if (components.length > i2 + 1 && (components[i2 + 1] instanceof AbstractC1420s)) {
                    a((AbstractC1420s) components[i2 + 1], true);
                    repaint();
                    return;
                } else if (components[0] instanceof AbstractC1420s) {
                    a((AbstractC1420s) components[0], true);
                    repaint();
                    return;
                }
            }
        }
    }

    public void Z() {
        Component[] components = getComponents();
        for (int length = components.length - 1; length >= 0; length--) {
            if (components[length] instanceof AbstractC1420s) {
                if (g() == null) {
                    a((AbstractC1420s) components[length], true);
                    repaint();
                    return;
                } else if (!r().contains(components[length])) {
                    continue;
                } else if (length > 0 && (components[length - 1] instanceof AbstractC1420s)) {
                    a((AbstractC1420s) components[length - 1], true);
                    repaint();
                    return;
                } else if (components[components.length - 1] instanceof AbstractC1420s) {
                    a((AbstractC1420s) components[components.length - 1], true);
                    repaint();
                    return;
                }
            }
        }
    }

    public void a(InterfaceC1407f interfaceC1407f) {
        this.f9648aV.add(interfaceC1407f);
    }

    private void aG() {
        Iterator it = this.f9648aV.iterator();
        while (it.hasNext()) {
            ((InterfaceC1407f) it.next()).a(r());
        }
    }

    public String aa() {
        return this.f9594aH;
    }

    public void k(String str) {
        if (F()) {
            return;
        }
        a(str, false);
    }

    public void a(String str, boolean z2) {
        this.f9594aH = str;
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (getComponent(i2) instanceof AbstractC1420s) {
                ((AbstractC1420s) getComponent(i2)).setDirty(true);
            }
        }
        this.f9558aj = null;
        this.f9560al = null;
        repaint();
        if (str == null || str.length() <= 0) {
            c(true);
        } else {
            c(!z2);
        }
    }

    public void ab() {
        k((String) null);
    }

    public void l(String str) {
        this.f9595aI = str;
        this.f9558aj = null;
        this.f9560al = null;
        repaint();
    }

    public void ac() {
        l((String) null);
    }

    public String ad() {
        return this.f9575ar;
    }

    public void m(String str) {
        this.f9575ar = str;
    }

    public boolean ae() {
        return this.f9593aG;
    }

    public void k(boolean z2) {
        this.f9593aG = z2;
    }

    public boolean af() {
        return this.f9640aO && C1806i.a().a("d67nhtrbd4es8j");
    }

    public void l(boolean z2) {
        this.f9640aO = z2;
    }

    public Z b(G.R r2) {
        if (this.f9588aB != null) {
            return this.f9588aB;
        }
        C1388aa c1388aa = new C1388aa();
        try {
            this.f9589aC = c1388aa.a(r2, "FrontPage", 1);
        } catch (V.a e2) {
            Logger.getLogger(C1425x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            this.f9589aC = c1388aa.a(r2, "FrontPage", 2, 4);
        }
        return this.f9589aC;
    }

    public void b(Z z2) {
        this.f9588aB = z2;
    }

    public String ag() {
        return this.f9590aD;
    }

    public void n(String str) {
        this.f9590aD = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(InterfaceC1712d interfaceC1712d) {
        if (interfaceC1712d.getShortClickAction() == null || interfaceC1712d.getShortClickAction().isEmpty()) {
            return false;
        }
        try {
            d.g.a().c(interfaceC1712d.getShortClickAction());
            return true;
        } catch (d.e e2) {
            bV.d(e2.getLocalizedMessage(), this.f9571ao);
            bH.C.a("Unable to fire Action: " + e2.getLocalizedMessage());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(InterfaceC1712d interfaceC1712d) {
        if (interfaceC1712d.getLongClickAction() == null || interfaceC1712d.getLongClickAction().isEmpty()) {
            return;
        }
        try {
            d.g.a().c(interfaceC1712d.getLongClickAction());
        } catch (d.e e2) {
            bV.d(e2.getLocalizedMessage(), this.f9571ao);
            Logger.getLogger(C1425x.class.getName()).log(Level.SEVERE, "Unable to fire Action", (Throwable) e2);
        }
    }
}
