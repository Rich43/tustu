package com.efiAnalytics.ui;

import bH.C1018z;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eM.class */
public class eM extends JComponent implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    eT f11492a;

    /* renamed from: g, reason: collision with root package name */
    eQ f11519g;

    /* renamed from: j, reason: collision with root package name */
    private eR f11491j = null;

    /* renamed from: k, reason: collision with root package name */
    private Color f11493k = Color.RED;

    /* renamed from: l, reason: collision with root package name */
    private Color f11494l = new Color(32, 32, 255);

    /* renamed from: m, reason: collision with root package name */
    private boolean f11495m = true;

    /* renamed from: n, reason: collision with root package name */
    private int f11496n = 1;

    /* renamed from: o, reason: collision with root package name */
    private int f11497o = 1;

    /* renamed from: p, reason: collision with root package name */
    private String f11498p = "Selected";

    /* renamed from: q, reason: collision with root package name */
    private String f11499q = "Starting";

    /* renamed from: r, reason: collision with root package name */
    private double f11500r = Double.NaN;

    /* renamed from: s, reason: collision with root package name */
    private double f11501s = Double.NaN;

    /* renamed from: t, reason: collision with root package name */
    private int f11502t = 8;

    /* renamed from: u, reason: collision with root package name */
    private int f11503u = 4;

    /* renamed from: v, reason: collision with root package name */
    private int f11504v = 25;

    /* renamed from: w, reason: collision with root package name */
    private int f11505w = 0;

    /* renamed from: x, reason: collision with root package name */
    private int f11506x = 0;

    /* renamed from: y, reason: collision with root package name */
    private int f11507y = 0;

    /* renamed from: b, reason: collision with root package name */
    String f11508b = "X";

    /* renamed from: c, reason: collision with root package name */
    String f11509c = Constants._TAG_Y;

    /* renamed from: d, reason: collision with root package name */
    String f11510d = com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG;

    /* renamed from: z, reason: collision with root package name */
    private boolean f11511z = true;

    /* renamed from: A, reason: collision with root package name */
    private boolean f11512A = false;

    /* renamed from: B, reason: collision with root package name */
    private boolean f11513B = true;

    /* renamed from: C, reason: collision with root package name */
    private ArrayList f11514C = new ArrayList();

    /* renamed from: D, reason: collision with root package name */
    private ArrayList f11515D = new ArrayList();

    /* renamed from: E, reason: collision with root package name */
    private long f11516E = System.currentTimeMillis();

    /* renamed from: e, reason: collision with root package name */
    Image f11517e = null;

    /* renamed from: f, reason: collision with root package name */
    Font f11518f = new Font("Arial Unicode MS", 1, eJ.a(13));

    /* renamed from: h, reason: collision with root package name */
    boolean f11520h = true;

    /* renamed from: F, reason: collision with root package name */
    private bH.aa f11521F = null;

    /* renamed from: G, reason: collision with root package name */
    private boolean f11522G = true;

    /* renamed from: i, reason: collision with root package name */
    Stroke f11523i = new BasicStroke(1.0f, 0, 2, 0.0f, new float[]{1.0f, 2.0f}, 0.0f);

    public eM() {
        this.f11492a = null;
        this.f11519g = null;
        if (C1018z.i().b()) {
            this.f11492a = new eT(this);
            eP ePVar = new eP(this);
            addMouseListener(ePVar);
            addMouseMotionListener(ePVar);
            addMouseWheelListener(ePVar);
            this.f11519g = new eQ(this);
        }
        addMouseListener(new eN(this));
        setFocusable(true);
        addFocusListener(this);
        setFont(new Font("SansSerif", 1, 12));
    }

    private String a(String str) {
        if (this.f11521F != null) {
            str = this.f11521F.a(str);
        }
        return str;
    }

    @Override // java.awt.Component
    public void setSize(int i2, int i3) {
        if (this.f11491j == null || this.f11491j.a() != i2 || this.f11491j.b() != i3) {
            this.f11491j = new eR(i2, i3);
        }
        z();
    }

    public eR a() {
        return this.f11491j;
    }

    public void a(eR eRVar) {
        this.f11491j = eRVar;
        this.f11492a.a(eRVar);
        eRVar.a(new eO(this));
        this.f11508b = a(eRVar.g());
        this.f11509c = a(eRVar.f());
        this.f11510d = a(eRVar.h());
        z();
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
        if (getHeight() > 390) {
            this.f11518f = new Font("Arial Unicode MS", 1, Math.round(getHeight() / 30));
            setFont(this.f11518f);
        }
        this.f11492a.c();
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        repaint();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        repaint();
    }

    private void a(Graphics graphics, eZ eZVar, int i2, int i3) {
        if (this.f11491j.c(i2, i3)) {
            return;
        }
        graphics.setColor(Color.YELLOW);
        int iH = H();
        graphics.fillOval(eZVar.f11567a - (iH / 2), eZVar.f11568b - (iH / 2), iH, iH);
    }

    private void a(Graphics graphics, boolean z2) {
        if (z2) {
            ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        long jNanoTime = System.nanoTime();
        graphics.drawImage(A(), 0, 0, null);
        h(graphics);
        double dNanoTime = (System.nanoTime() - jNanoTime) / 1000000.0d;
        double dCurrentTimeMillis = 1000.0d / (System.currentTimeMillis() - this.f11516E);
        this.f11516E = System.currentTimeMillis();
        if (hasFocus()) {
            graphics.setColor(getForeground());
            ((Graphics2D) graphics).setStroke(this.f11523i);
            graphics.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
        }
        if (isEnabled()) {
            return;
        }
        graphics.setColor(Color.lightGray);
        graphics.draw3DRect(0, 0, getWidth() - 1, getHeight() - 1, false);
        graphics.draw3DRect(1, 1, getWidth() - 3, getHeight() - 3, false);
        if (isEnabled()) {
            return;
        }
        graphics.setColor(new Color(64, 64, 64, 160));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    public void a(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        a(graphics, w());
        boolean z2 = (m() > 180 && l() < 180) || (m() < 180 && l() > 180);
        boolean z3 = (l() < 90 || l() > 270) && m() > 90 && m() < 270;
        if (z2) {
            if (z3) {
                e(graphics);
                f(graphics);
                a(graphics, this.f11491j.a(this.f11496n), this.f11491j.b(this.f11497o));
                d(graphics);
                g(graphics);
            } else {
                e(graphics);
                a(graphics, this.f11491j.a(this.f11496n), this.f11491j.b(this.f11497o));
                d(graphics);
                f(graphics);
                g(graphics);
            }
        } else if (z3) {
            f(graphics);
            d(graphics);
            a(graphics, this.f11491j.a(this.f11496n), this.f11491j.b(this.f11497o));
            g(graphics);
            e(graphics);
        } else {
            d(graphics);
            a(graphics, this.f11491j.a(this.f11496n), this.f11491j.b(this.f11497o));
            g(graphics);
            f(graphics);
            e(graphics);
        }
        b(graphics);
    }

    public void b(Graphics graphics) {
        a(graphics, false);
        String strB = bH.W.b(this.f11491j.d(this.f11497o, this.f11496n), D());
        String strB2 = bH.W.b(this.f11491j.b(this.f11497o), B());
        String strB3 = bH.W.b(this.f11491j.a(this.f11496n), C());
        graphics.setColor(this.f11493k);
        graphics.setFont(this.f11518f);
        graphics.drawString(this.f11508b + ": " + strB3, 5, getFont().getSize());
        graphics.drawString(this.f11509c + ": " + strB2, 5, getFont().getSize() * 2);
        graphics.drawString(this.f11498p + " " + this.f11491j.h() + ": " + strB, 5, getFont().getSize() * 3);
        if (!this.f11491j.c(this.f11497o, this.f11496n)) {
            graphics.drawString(this.f11499q + " " + this.f11510d + ": " + bH.W.b(this.f11491j.b(this.f11497o, this.f11496n), D()), 5, getFont().getSize() * 4);
        }
        if (this.f11513B) {
            eZ eZVarA = this.f11492a.a(this.f11491j.a(this.f11496n), this.f11491j.b(this.f11497o), this.f11491j.d(this.f11497o, this.f11496n));
            eZVarA.f11568b = (eZVarA.f11568b - G()) - graphics.getFont().getSize();
            a(graphics, eZVarA, strB, false, Color.orange);
        }
        if (this.f11512A) {
            eZ eZVarA2 = this.f11492a.a(this.f11491j.a(this.f11496n), this.f11491j.k() - (0.05d * (this.f11491j.l() - this.f11491j.k())), this.f11491j.r());
            a(graphics, this.f11492a.a(this.f11491j.i() - (0.05d * (this.f11491j.j() - this.f11491j.i())), this.f11491j.b(this.f11497o), this.f11491j.r()), strB2, false, Color.orange);
            a(graphics, eZVarA2, strB3, false, Color.orange);
        }
    }

    public void c(Graphics graphics) {
        a(graphics, false);
        String strB = bH.W.b(this.f11491j.a(this.f11501s, this.f11500r), D());
        String strB2 = bH.W.b(this.f11501s, B());
        String strB3 = bH.W.b(this.f11500r, C());
        graphics.setColor(this.f11494l);
        graphics.setFont(this.f11518f);
        String str = this.f11508b + ": " + strB3;
        String str2 = this.f11509c + ": " + strB2;
        String str3 = this.f11510d + ": " + strB;
        FontMetrics fontMetrics = getFontMetrics(this.f11518f);
        int iStringWidth = fontMetrics.stringWidth(str);
        int iStringWidth2 = fontMetrics.stringWidth(str2) > iStringWidth ? fontMetrics.stringWidth(str2) : iStringWidth;
        int iStringWidth3 = fontMetrics.stringWidth(str3) > iStringWidth2 ? fontMetrics.stringWidth(str3) : iStringWidth2;
        graphics.drawString(str, getWidth() - (iStringWidth3 + 5), getFont().getSize());
        graphics.drawString(str2, getWidth() - (iStringWidth3 + 5), getFont().getSize() * 2);
        graphics.drawString(str3, getWidth() - (iStringWidth3 + 5), getFont().getSize() * 3);
    }

    private void a(Graphics graphics, eZ eZVar, String str, boolean z2, Color color) {
        Color color2 = new Color(64, 64, 64, 192);
        int iStringWidth = eZVar.f11567a - ((getFontMetrics(graphics.getFont()).stringWidth(str) + 2) / 2);
        int size = eZVar.f11568b - ((graphics.getFont().getSize() + 2) / 2);
        if (z2) {
            size = eZVar.f11568b + (2 * graphics.getFont().getSize()) + 2;
        }
        graphics.setColor(color2);
        graphics.fill3DRect(iStringWidth, size, getFontMetrics(graphics.getFont()).stringWidth(str) + 3, graphics.getFont().getSize() + 3, false);
        graphics.setColor(color);
        graphics.drawString(str, iStringWidth + 2, size + ((int) (graphics.getFont().getSize() * 0.9d)) + 1);
    }

    private void d(Graphics graphics) {
        eZ[][] eZVarArrB = this.f11492a.b();
        if (l() < 90 || l() > 270) {
            for (int iP = this.f11491j.p() - 1; iP >= 0; iP--) {
                for (int i2 = 1; i2 < this.f11491j.o(); i2++) {
                    if (iP > 0) {
                        Polygon polygon = new Polygon();
                        polygon.addPoint(eZVarArrB[iP - 1][i2 - 1].f11567a, eZVarArrB[iP - 1][i2 - 1].f11568b);
                        polygon.addPoint(eZVarArrB[iP - 1][i2].f11567a, eZVarArrB[iP - 1][i2].f11568b);
                        polygon.addPoint(eZVarArrB[iP][i2].f11567a, eZVarArrB[iP][i2].f11568b);
                        polygon.addPoint(eZVarArrB[iP][i2 - 1].f11567a, eZVarArrB[iP][i2 - 1].f11568b);
                        if (p()) {
                            if (0 != 0) {
                                C1677fh.a((Graphics2D) graphics, polygon, new double[]{this.f11491j.d(iP - 1, i2 - 1), this.f11491j.d(iP - 1, i2), this.f11491j.d(iP, i2), this.f11491j.d(iP, i2 - 1)}, this.f11491j.m(), this.f11491j.n());
                            } else {
                                graphics.setColor(C1677fh.a((((this.f11491j.d(iP - 1, i2 - 1) + this.f11491j.d(iP - 1, i2)) + this.f11491j.d(iP, i2)) + this.f11491j.d(iP, i2 - 1)) / 4.0d, this.f11491j.m(), this.f11491j.n()));
                                graphics.fillPolygon(polygon);
                            }
                        }
                        graphics.setColor(getForeground());
                        graphics.drawPolygon(polygon);
                    }
                    a(graphics, eZVarArrB[iP][i2], iP, i2);
                }
            }
            return;
        }
        for (int i3 = 1; i3 < this.f11491j.o(); i3++) {
            for (int i4 = 0; i4 < this.f11491j.p(); i4++) {
                if (i4 > 0) {
                    Polygon polygon2 = new Polygon();
                    polygon2.addPoint(eZVarArrB[i4 - 1][i3 - 1].f11567a, eZVarArrB[i4 - 1][i3 - 1].f11568b);
                    polygon2.addPoint(eZVarArrB[i4 - 1][i3].f11567a, eZVarArrB[i4 - 1][i3].f11568b);
                    polygon2.addPoint(eZVarArrB[i4][i3].f11567a, eZVarArrB[i4][i3].f11568b);
                    polygon2.addPoint(eZVarArrB[i4][i3 - 1].f11567a, eZVarArrB[i4][i3 - 1].f11568b);
                    if (p()) {
                        if (0 != 0) {
                            C1677fh.a((Graphics2D) graphics, polygon2, new double[]{this.f11491j.d(i4 - 1, i3 - 1), this.f11491j.d(i4 - 1, i3), this.f11491j.d(i4, i3), this.f11491j.d(i4, i3 - 1)}, this.f11491j.m(), this.f11491j.n());
                        } else {
                            graphics.setColor(C1677fh.a((((this.f11491j.d(i4 - 1, i3 - 1) + this.f11491j.d(i4 - 1, i3)) + this.f11491j.d(i4, i3)) + this.f11491j.d(i4, i3 - 1)) / 4.0d, this.f11491j.m(), this.f11491j.n()));
                            graphics.fillPolygon(polygon2);
                        }
                    }
                    graphics.setColor(getForeground());
                    graphics.drawPolygon(polygon2);
                }
                a(graphics, eZVarArrB[i4][i3], i4, i3);
            }
        }
    }

    private void e(Graphics graphics) {
        eZ eZVarA = this.f11492a.a(this.f11491j.i(), this.f11491j.k(), this.f11491j.r());
        eZ eZVarA2 = this.f11492a.a(this.f11491j.j(), this.f11491j.k(), this.f11491j.r());
        eZ eZVarA3 = this.f11492a.a(this.f11491j.i(), this.f11491j.l(), this.f11491j.r());
        graphics.setColor(getForeground());
        graphics.drawLine(eZVarA.f11567a, eZVarA.f11568b, eZVarA2.f11567a, eZVarA2.f11568b);
        graphics.drawLine(eZVarA.f11567a, eZVarA.f11568b, eZVarA3.f11567a, eZVarA3.f11568b);
    }

    private void f(Graphics graphics) {
        this.f11492a.a(this.f11491j.i(), this.f11491j.k(), this.f11491j.r());
        eZ eZVarA = this.f11492a.a(this.f11491j.j(), this.f11491j.k(), this.f11491j.r());
        eZ eZVarA2 = this.f11492a.a(this.f11491j.i(), this.f11491j.l(), this.f11491j.r());
        eZ eZVarA3 = this.f11492a.a(this.f11491j.j(), this.f11491j.l(), b(this.f11491j));
        eZ eZVarA4 = this.f11492a.a(this.f11491j.j(), this.f11491j.l(), this.f11491j.r());
        eZ eZVarA5 = this.f11492a.a(this.f11491j.j(), this.f11491j.k(), b(this.f11491j));
        eZ eZVarA6 = this.f11492a.a(this.f11491j.i(), this.f11491j.l(), b(this.f11491j));
        graphics.setColor(getForeground());
        graphics.drawLine(eZVarA3.f11567a, eZVarA3.f11568b, eZVarA5.f11567a, eZVarA5.f11568b);
        graphics.drawLine(eZVarA3.f11567a, eZVarA3.f11568b, eZVarA6.f11567a, eZVarA6.f11568b);
        graphics.drawLine(eZVarA4.f11567a, eZVarA4.f11568b, eZVarA.f11567a, eZVarA.f11568b);
        graphics.drawLine(eZVarA4.f11567a, eZVarA4.f11568b, eZVarA2.f11567a, eZVarA2.f11568b);
        graphics.drawLine(eZVarA4.f11567a, eZVarA4.f11568b, eZVarA3.f11567a, eZVarA3.f11568b);
        graphics.drawLine(eZVarA5.f11567a, eZVarA5.f11568b, eZVarA.f11567a, eZVarA.f11568b);
        graphics.drawLine(eZVarA6.f11567a, eZVarA6.f11568b, eZVarA2.f11567a, eZVarA2.f11568b);
    }

    private double b(eR eRVar) {
        double dN = eRVar.n() + ((eRVar.q() - eRVar.r()) * 0.05d);
        return dN < eRVar.q() ? dN : eRVar.q();
    }

    private void g(Graphics graphics) {
        if (I()) {
            eZ eZVarA = this.f11492a.a(this.f11491j.a(this.f11496n), this.f11491j.b(this.f11497o), this.f11491j.d(this.f11497o, this.f11496n));
            graphics.setColor(this.f11493k);
            int iG = G() / 2;
            graphics.fill3DRect(eZVarA.f11567a - iG, eZVarA.f11568b - iG, G(), G(), true);
        }
    }

    private void a(Graphics graphics, double d2, double d3) {
        a(graphics, d2, d3, this.f11493k);
    }

    private void a(Graphics graphics, double d2, double d3, Color color) {
        if (I()) {
            eZ eZVarA = this.f11492a.a(d2, d3, this.f11491j.r());
            eZ eZVarA2 = this.f11492a.a(d2, this.f11491j.k(), this.f11491j.r());
            eZ eZVarA3 = this.f11492a.a(this.f11491j.i(), d3, this.f11491j.r());
            eZ eZVarA4 = this.f11492a.a(d2, d3, this.f11491j.a(d3, d2));
            graphics.setColor(color);
            graphics.drawLine(eZVarA2.f11567a, eZVarA2.f11568b, eZVarA.f11567a, eZVarA.f11568b);
            graphics.drawLine(eZVarA3.f11567a, eZVarA3.f11568b, eZVarA.f11567a, eZVarA.f11568b);
            graphics.drawLine(eZVarA.f11567a, eZVarA.f11568b, eZVarA4.f11567a, eZVarA4.f11568b);
        }
    }

    private boolean F() {
        return (Double.isNaN(this.f11500r) || Double.isNaN(this.f11501s)) ? false : true;
    }

    private void h(Graphics graphics) {
        if (F()) {
            double dA = this.f11500r > this.f11491j.a(0) ? this.f11500r : this.f11491j.a(0);
            double dB = this.f11501s > this.f11491j.b(0) ? this.f11501s : this.f11491j.b(0);
            double dA2 = this.f11491j.a(this.f11501s, this.f11500r);
            eZ eZVarA = this.f11492a.a(dA, dB, dA2);
            graphics.setColor(this.f11494l);
            int iG = G() / 2;
            graphics.fillOval(eZVarA.f11567a - iG, eZVarA.f11568b - iG, G(), G());
            c(graphics);
            eZ eZVar = new eZ();
            eZVar.f11567a = eZVarA.f11567a;
            eZVar.f11568b = eZVarA.f11568b;
            eZVar.f11568b = (eZVar.f11568b - G()) - graphics.getFont().getSize();
            a(graphics, eZVar, bH.W.b(dA2, D()), this.f11500r < this.f11491j.a(this.f11496n) || this.f11501s < this.f11491j.b(this.f11497o), Color.CYAN);
        }
    }

    private int G() {
        return getHeight() > 360 ? getHeight() / 45 : this.f11502t;
    }

    private int H() {
        return (G() * 4) / 5;
    }

    private boolean I() {
        return this.f11496n >= 0 && this.f11496n < this.f11491j.o() && this.f11497o >= 0 && this.f11497o < this.f11491j.p();
    }

    public void b() {
        eZ[][] eZVarArrB = this.f11492a.b();
        int i2 = eZVarArrB[0][eZVarArrB[0].length - 1].f11567a - eZVarArrB[0][0].f11567a;
        int i3 = eZVarArrB[0][eZVarArrB[0].length - 1].f11568b - eZVarArrB[0][0].f11568b;
        if (!E()) {
            f();
        } else if (Math.abs(i2) > Math.abs(i3)) {
            if (i2 > 0) {
                f();
            } else {
                g();
            }
        } else if (i3 > 0) {
            h();
        } else {
            i();
        }
        repaint();
    }

    public void c() {
        eZ[][] eZVarArrB = this.f11492a.b();
        int i2 = eZVarArrB[0][eZVarArrB[0].length - 1].f11567a - eZVarArrB[0][0].f11567a;
        int i3 = eZVarArrB[0][eZVarArrB[0].length - 1].f11568b - eZVarArrB[0][0].f11568b;
        if (!E()) {
            g();
        } else if (Math.abs(i2) > Math.abs(i3)) {
            if (i2 < 0) {
                f();
            } else {
                g();
            }
        } else if (i3 < 0) {
            h();
        } else {
            i();
        }
        repaint();
    }

    public void d() {
        eZ[][] eZVarArrB = this.f11492a.b();
        int i2 = eZVarArrB[0][eZVarArrB[0].length - 1].f11567a - eZVarArrB[0][0].f11567a;
        int i3 = eZVarArrB[0][eZVarArrB[0].length - 1].f11568b - eZVarArrB[0][0].f11568b;
        if (!E()) {
            h();
        } else if (Math.abs(i2) < Math.abs(i3)) {
            if (i2 < 0) {
                f();
            } else {
                g();
            }
        } else if (eZVarArrB[eZVarArrB.length - 1][0].f11568b - eZVarArrB[0][0].f11568b < 0) {
            h();
        } else {
            i();
        }
        repaint();
    }

    public void e() {
        eZ[][] eZVarArrB = this.f11492a.b();
        int i2 = eZVarArrB[0][eZVarArrB[0].length - 1].f11567a - eZVarArrB[0][0].f11567a;
        int i3 = eZVarArrB[0][eZVarArrB[0].length - 1].f11568b - eZVarArrB[0][0].f11568b;
        if (!E()) {
            i();
        } else if (Math.abs(i2) < Math.abs(i3)) {
            if (i2 > 0) {
                f();
            } else {
                g();
            }
        } else if (eZVarArrB[eZVarArrB.length - 1][0].f11568b - eZVarArrB[0][0].f11568b > 0) {
            h();
        } else {
            i();
        }
        repaint();
    }

    public void f() {
        if (this.f11496n < this.f11491j.o() - 1) {
            this.f11496n++;
            z();
            J();
        }
    }

    public void g() {
        if (this.f11496n > 0) {
            this.f11496n--;
            z();
            J();
        }
    }

    public void h() {
        if (this.f11497o < this.f11491j.p() - 1) {
            this.f11497o++;
            z();
            J();
        }
    }

    public void i() {
        if (this.f11497o > 0) {
            this.f11497o--;
            z();
            J();
        }
    }

    public void j() {
        if (this.f11496n < 0 || this.f11497o < 0) {
            return;
        }
        a(this.f11497o, this.f11496n, this.f11491j.s());
    }

    public void a(int i2) {
        if (this.f11496n < 0 || this.f11497o < 0) {
            return;
        }
        a(this.f11497o, this.f11496n, i2 * this.f11491j.s());
    }

    public void k() {
        if (this.f11496n < 0 || this.f11497o < 0) {
            return;
        }
        a(this.f11497o, this.f11496n, -this.f11491j.s());
    }

    public void b(int i2) {
        if (this.f11496n < 0 || this.f11497o < 0) {
            return;
        }
        a(this.f11497o, this.f11496n, -(i2 * this.f11491j.s()));
    }

    public void a(int i2, int i3, double d2) {
        this.f11491j.a(i2, i3, this.f11491j.d(i2, i3) + d2);
        z();
    }

    public void a(double d2, int i2) {
        if (F()) {
            double dB = this.f11491j.b(this.f11501s);
            double dA = this.f11491j.a(this.f11500r);
            int i3 = (int) dB;
            int i4 = (int) dA;
            int i5 = i3 < this.f11491j.a() - 1 ? i3 + 1 : i3;
            int i6 = i4 < this.f11491j.b() - 1 ? i4 + 1 : i4;
            double d3 = dB - i3;
            double d4 = dA - i4;
            double d5 = (1.0d - d4) * (1.0d - d3);
            double d6 = d4 * (1.0d - d3);
            double d7 = (1.0d - d4) * d3;
            double d8 = d4 * d3;
            if (d5 >= d2) {
                a(i3, i4, i2 * this.f11491j.s());
            }
            if (d6 >= d2 && i4 != i6) {
                a(i3, i6, i2 * this.f11491j.s());
            }
            if (d7 >= d2 && i3 != i5) {
                a(i5, i4, i2 * this.f11491j.s());
            }
            if (d8 < d2 || i4 == i6 || i3 == i5) {
                return;
            }
            a(i5, i6, i2 * this.f11491j.s());
        }
    }

    public void a(InterfaceC1649eg interfaceC1649eg) {
        this.f11515D.add(interfaceC1649eg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void J() {
        Iterator it = this.f11515D.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC1649eg) it.next()).a(q(), r(), a().d(r(), q()));
            } catch (Exception e2) {
                bH.C.c("Exception generated by SelctedPointListener, caught and continued:");
                e2.printStackTrace();
            }
        }
    }

    public void c(int i2) {
        this.f11492a.b((i2 * 3.141592653589793d) / 180.0d);
        z();
        K();
        j(i2);
    }

    public int l() {
        return (int) ((this.f11492a.d() * 180.0d) / 3.141592653589793d);
    }

    public int m() {
        return (int) ((this.f11492a.e() * 180.0d) / 3.141592653589793d);
    }

    public void d(int i2) {
        this.f11492a.c((i2 * 3.141592653589793d) / 180.0d);
        z();
        K();
        k(i2);
    }

    public void a(eU eUVar) {
        this.f11514C.add(eUVar);
    }

    private void j(int i2) {
        Iterator it = this.f11514C.iterator();
        while (it.hasNext()) {
            ((eU) it.next()).c(i2);
        }
    }

    private void k(int i2) {
        Iterator it = this.f11514C.iterator();
        while (it.hasNext()) {
            ((eU) it.next()).d(i2);
        }
    }

    protected eZ n() {
        return this.f11492a.a(this.f11491j.a(this.f11496n), this.f11491j.b(this.f11497o), this.f11491j.d(this.f11497o, this.f11496n));
    }

    public double o() {
        if (q() < 0 || r() < 0) {
            return 0.0d;
        }
        return this.f11491j.d(r(), q());
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(150, 150);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(250, 250);
    }

    public boolean p() {
        return this.f11495m;
    }

    public void a(boolean z2) {
        z();
        this.f11495m = z2;
    }

    public int q() {
        return this.f11496n;
    }

    public void e(int i2) {
        this.f11496n = i2;
        z();
        J();
    }

    public int r() {
        return this.f11497o;
    }

    public void f(int i2) {
        this.f11497o = i2;
        z();
        J();
    }

    public Color s() {
        return this.f11493k;
    }

    public void a(Color color) {
        this.f11493k = color;
    }

    public Color t() {
        return this.f11494l;
    }

    public void b(Color color) {
        this.f11494l = color;
        z();
    }

    public boolean u() {
        return this.f11512A;
    }

    public void b(boolean z2) {
        this.f11512A = z2;
    }

    public boolean v() {
        return this.f11513B;
    }

    public void c(boolean z2) {
        this.f11513B = z2;
    }

    public void a(bH.aa aaVar) {
        this.f11521F = aaVar;
        this.f11498p = a("Selected");
        this.f11499q = a("Starting");
        if (this.f11491j != null) {
            this.f11508b = a(this.f11491j.g());
            this.f11509c = a(this.f11491j.f());
            this.f11510d = a(this.f11491j.h());
        }
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    public boolean w() {
        return this.f11511z;
    }

    public void d(boolean z2) {
        this.f11511z = z2;
    }

    public double x() {
        return this.f11500r;
    }

    public void a(double d2) {
        this.f11500r = d2;
    }

    public double y() {
        return this.f11501s;
    }

    public void b(double d2) {
        this.f11501s = d2;
    }

    public void z() {
        this.f11520h = true;
    }

    public Image A() {
        if (this.f11517e == null || this.f11517e.getWidth(null) != getWidth() || this.f11517e.getHeight(null) != getHeight()) {
            this.f11517e = createImage(getWidth(), getHeight());
            this.f11520h = true;
        }
        if (this.f11520h) {
            a(this.f11517e.getGraphics());
            this.f11520h = false;
        }
        return this.f11517e;
    }

    public void c(double d2) {
        this.f11492a.d(d2);
    }

    @Override // java.awt.Component
    public void repaint() {
        if (this.f11519g == null) {
            this.f11519g = new eQ(this);
        }
        this.f11519g.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void K() {
        super.repaint();
    }

    public int B() {
        return this.f11505w;
    }

    public void g(int i2) {
        this.f11505w = i2;
    }

    public int C() {
        return this.f11506x;
    }

    public void h(int i2) {
        this.f11506x = i2;
    }

    public int D() {
        return this.f11507y;
    }

    public void i(int i2) {
        this.f11507y = i2;
    }

    public void e(boolean z2) {
        this.f11492a.a(z2);
        z();
    }

    public boolean E() {
        return this.f11522G;
    }

    public void f(boolean z2) {
        this.f11522G = z2;
    }
}
