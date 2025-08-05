package com.efiAnalytics.ui;

import W.C0184j;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bN.class */
public class bN extends JComponent implements FocusListener {

    /* renamed from: af, reason: collision with root package name */
    private int f10918af;

    /* renamed from: u, reason: collision with root package name */
    static final float[] f10938u = {8.0f};

    /* renamed from: x, reason: collision with root package name */
    static int f10944x = 0;

    /* renamed from: y, reason: collision with root package name */
    static int f10945y = 1;

    /* renamed from: z, reason: collision with root package name */
    static int f10946z = 2;

    /* renamed from: A, reason: collision with root package name */
    static int f10947A = 3;

    /* renamed from: B, reason: collision with root package name */
    static int f10948B = 4;

    /* renamed from: C, reason: collision with root package name */
    static int f10949C = 5;

    /* renamed from: G, reason: collision with root package name */
    private double f10888G = 1.0d;

    /* renamed from: H, reason: collision with root package name */
    private G.dh f10889H = new G.B(1.0d);

    /* renamed from: I, reason: collision with root package name */
    private ArrayList f10890I = new ArrayList();

    /* renamed from: J, reason: collision with root package name */
    private ArrayList f10891J = new ArrayList();

    /* renamed from: K, reason: collision with root package name */
    private double f10892K = 100.0d;

    /* renamed from: L, reason: collision with root package name */
    private double f10893L = Double.MIN_VALUE;

    /* renamed from: M, reason: collision with root package name */
    private double f10894M = 0.0d;

    /* renamed from: N, reason: collision with root package name */
    private double f10895N = 100.0d;

    /* renamed from: O, reason: collision with root package name */
    private double f10896O = Double.MIN_VALUE;

    /* renamed from: P, reason: collision with root package name */
    private double f10897P = 0.0d;

    /* renamed from: Q, reason: collision with root package name */
    private double f10898Q = Double.MAX_VALUE;

    /* renamed from: R, reason: collision with root package name */
    private int f10899R = 0;

    /* renamed from: S, reason: collision with root package name */
    private int f10900S = 0;

    /* renamed from: T, reason: collision with root package name */
    private int f10901T = 10;

    /* renamed from: U, reason: collision with root package name */
    private int f10902U = 10;

    /* renamed from: V, reason: collision with root package name */
    private String f10903V = null;

    /* renamed from: W, reason: collision with root package name */
    private boolean f10904W = false;

    /* renamed from: a, reason: collision with root package name */
    int f10905a = eJ.a(8);

    /* renamed from: b, reason: collision with root package name */
    int f10906b = eJ.a(10);

    /* renamed from: c, reason: collision with root package name */
    ArrayList f10907c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    bP f10908d = new bP(this, -1, -1);

    /* renamed from: X, reason: collision with root package name */
    private double f10909X = Double.MIN_VALUE;

    /* renamed from: Y, reason: collision with root package name */
    private double f10910Y = Double.NaN;

    /* renamed from: Z, reason: collision with root package name */
    private boolean f10911Z = true;

    /* renamed from: aa, reason: collision with root package name */
    private boolean f10912aa = false;

    /* renamed from: ab, reason: collision with root package name */
    private boolean f10913ab = false;

    /* renamed from: ac, reason: collision with root package name */
    private double f10914ac = 0.0d;

    /* renamed from: ad, reason: collision with root package name */
    private int f10915ad = 10000;

    /* renamed from: ae, reason: collision with root package name */
    private long f10916ae = 0;

    /* renamed from: e, reason: collision with root package name */
    double f10917e = 0.01d;

    /* renamed from: f, reason: collision with root package name */
    ArrayList f10919f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    Color f10920g = Color.YELLOW;

    /* renamed from: h, reason: collision with root package name */
    Color f10921h = Color.GRAY;

    /* renamed from: i, reason: collision with root package name */
    Color f10922i = new Color(50, 60, 255);

    /* renamed from: j, reason: collision with root package name */
    Color f10923j = Color.RED;

    /* renamed from: k, reason: collision with root package name */
    Color f10924k = Color.GREEN;

    /* renamed from: l, reason: collision with root package name */
    Color f10925l = Color.MAGENTA;

    /* renamed from: m, reason: collision with root package name */
    ArrayList f10926m = new ArrayList();

    /* renamed from: ag, reason: collision with root package name */
    private Insets f10927ag = null;

    /* renamed from: ah, reason: collision with root package name */
    private Font f10928ah = new Font("Arial Unicode MS", 0, eJ.a(10));

    /* renamed from: ai, reason: collision with root package name */
    private Font f10929ai = new Font("Arial Unicode MS", 1, eJ.a(11));

    /* renamed from: aj, reason: collision with root package name */
    private Font f10930aj = new Font("Arial Unicode MS", 1, eJ.a(12));

    /* renamed from: n, reason: collision with root package name */
    ArrayList f10931n = new ArrayList();

    /* renamed from: o, reason: collision with root package name */
    ArrayList f10932o = new ArrayList();

    /* renamed from: p, reason: collision with root package name */
    Image f10933p = null;

    /* renamed from: q, reason: collision with root package name */
    boolean f10934q = true;

    /* renamed from: r, reason: collision with root package name */
    Stroke f10935r = new BasicStroke(eJ.a(1), 0, 2, 0.0f, new float[]{1.0f, 2.0f}, 0.0f);

    /* renamed from: s, reason: collision with root package name */
    Stroke f10936s = new BasicStroke(eJ.a(1), 0, 2, 0.0f, new float[]{2.0f, 2.0f}, 0.0f);

    /* renamed from: t, reason: collision with root package name */
    Stroke f10937t = new BasicStroke(eJ.a(2));

    /* renamed from: v, reason: collision with root package name */
    Stroke f10939v = new BasicStroke(eJ.a(3.0f), 0, 0, 10.0f, f10938u, 0.0f);

    /* renamed from: w, reason: collision with root package name */
    Stroke f10940w = new BasicStroke(eJ.a(3));

    /* renamed from: ak, reason: collision with root package name */
    private boolean f10941ak = false;

    /* renamed from: al, reason: collision with root package name */
    private boolean f10942al = false;

    /* renamed from: am, reason: collision with root package name */
    private boolean f10943am = false;

    /* renamed from: D, reason: collision with root package name */
    Rectangle f10950D = null;

    /* renamed from: an, reason: collision with root package name */
    private C0184j f10951an = null;

    /* renamed from: ao, reason: collision with root package name */
    private C0184j f10952ao = null;

    /* renamed from: ap, reason: collision with root package name */
    private boolean f10953ap = false;

    /* renamed from: aq, reason: collision with root package name */
    private double f10954aq = Double.NaN;

    /* renamed from: ar, reason: collision with root package name */
    private double f10955ar = Double.NaN;

    /* renamed from: as, reason: collision with root package name */
    private bT f10956as = null;

    /* renamed from: E, reason: collision with root package name */
    long f10957E = -1;

    /* renamed from: F, reason: collision with root package name */
    double f10958F = 0.0d;

    public bN() {
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        bO bOVar = new bO(this);
        addMouseListener(bOVar);
        addMouseMotionListener(bOVar);
        setFocusable(true);
        addFocusListener(this);
        L();
    }

    private void L() {
        this.f10919f.add(Color.GREEN);
        this.f10919f.add(Color.RED);
        this.f10919f.add(Color.CYAN);
        this.f10919f.add(Color.YELLOW);
        this.f10919f.add(Color.MAGENTA);
        this.f10919f.add(Color.WHITE);
        this.f10919f.add(Color.GRAY);
        this.f10919f.add(Color.ORANGE);
        this.f10919f.add(Color.PINK);
        this.f10919f.add(Color.BLUE);
    }

    public void a(String str) {
        this.f10926m.add(str);
    }

    public void a() {
        this.f10926m.clear();
    }

    public Color a(int i2) {
        return this.f10890I.size() > 1 ? (Color) this.f10919f.get(i2 % this.f10919f.size()) : this.f10920g;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        Stroke stroke = ((Graphics2D) graphics).getStroke();
        graphics.drawImage(b(), 0, 0, null);
        try {
            b(graphics);
        } catch (Exception e2) {
            e2.printStackTrace();
            bH.C.c("Curve, error painting highlight");
        }
        if (I()) {
            c(graphics);
        }
        if (!isEnabled()) {
            graphics.setColor(new Color(0, 0, 0, 128));
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paint(graphics);
        if (this.f10950D != null) {
            graphics.setColor(Color.YELLOW);
            ((Graphics2D) graphics).setStroke(this.f10935r);
            graphics.drawRect(this.f10950D.f12372x, this.f10950D.f12373y, this.f10950D.width, this.f10950D.height);
            ((Graphics2D) graphics).setStroke(stroke);
        }
        if (hasFocus()) {
            graphics.setColor(Color.LIGHT_GRAY);
            ((Graphics2D) graphics).setStroke(this.f10935r);
            graphics.drawRect(eJ.a(2), eJ.a(2), getWidth() - eJ.a(5), getHeight() - eJ.a(5));
            ((Graphics2D) graphics).setStroke(stroke);
        }
    }

    private void a(Graphics graphics) {
        g(graphics);
        e(graphics);
        f(graphics);
        d(graphics);
    }

    protected Image b() {
        if (this.f10933p == null || this.f10933p.getWidth(null) != getWidth() || this.f10933p.getHeight(null) != getHeight()) {
            this.f10933p = createImage(getWidth(), getHeight());
            this.f10934q = true;
        }
        if (this.f10934q) {
            a(this.f10933p.getGraphics());
            this.f10934q = false;
        }
        return this.f10933p;
    }

    private void b(Graphics graphics) {
        bP bPVar;
        if (this.f10909X == Double.MIN_VALUE || Double.isNaN(this.f10909X)) {
            return;
        }
        if (Double.isNaN(this.f10910Y)) {
            bPVar = a((bQ) this.f10890I.get(0), this.f10909X);
        } else {
            bPVar = new bP(this);
            bPVar.a(this.f10909X);
            bPVar.b(this.f10910Y);
        }
        if (Double.isNaN(this.f10910Y)) {
            for (int i2 = 1; i2 < this.f10890I.size(); i2++) {
                bP bPVarA = a((bQ) this.f10890I.get(i2), this.f10909X);
                if (bPVarA.e() > bPVar.e()) {
                    bPVar = bPVarA;
                }
            }
        }
        graphics.setColor(this.f10924k);
        if (Double.isNaN(this.f10910Y)) {
            graphics.drawLine(bPVar.d(), bPVar.f(), bPVar.d(), getHeight() - F().bottom);
        } else {
            graphics.drawLine(bPVar.d(), F().top, bPVar.d(), getHeight() - F().bottom);
            graphics.drawLine(F().left, bPVar.f(), getWidth() - F().right, bPVar.f());
        }
        graphics.fillOval(bPVar.d() - (this.f10905a / 2), bPVar.f() - (this.f10905a / 2), this.f10905a, this.f10905a);
    }

    private void c(Graphics graphics) {
        if (H() >= i() && h() > i() && Math.abs((p() - H()) / (h() - i())) >= this.f10917e) {
            try {
                bP bPVarA = a((bQ) this.f10890I.get(0), H());
                for (int i2 = 1; i2 < this.f10890I.size(); i2++) {
                    bP bPVarA2 = a((bQ) this.f10890I.get(i2), this.f10909X);
                    if (bPVarA2.e() > bPVarA.e()) {
                        bPVarA = bPVarA2;
                    }
                }
                graphics.setColor(this.f10924k);
                ((Graphics2D) graphics).setStroke(this.f10936s);
                graphics.drawOval(bPVarA.d() - (this.f10905a / 2), bPVarA.f() - (this.f10905a / 2), this.f10905a, this.f10905a);
                graphics.drawLine(bPVarA.d(), bPVarA.f(), bPVarA.d(), getHeight() - F().bottom);
            } catch (Exception e2) {
            }
        }
    }

    private void d(Graphics graphics) {
        bP bPVarE = e(x(), w());
        if (bPVarE == null) {
            return;
        }
        String[] strArr = {b(x()) + ": " + bH.W.b(bPVarE.c(), this.f10899R), c(x()) + ": " + bH.W.b(bPVarE.e(), this.f10900S)};
        FontMetrics fontMetrics = getFontMetrics(this.f10928ah);
        int iStringWidth = fontMetrics.stringWidth(strArr[0]);
        Rectangle rectangleC = c(fontMetrics.stringWidth(strArr[1]) > iStringWidth ? fontMetrics.stringWidth(strArr[1]) : iStringWidth, fontMetrics.getHeight());
        graphics.setColor(Color.DARK_GRAY);
        graphics.fill3DRect(rectangleC.f12372x, rectangleC.f12373y, rectangleC.width, rectangleC.height, false);
        graphics.setColor(getForeground());
        graphics.setFont(this.f10928ah);
        graphics.drawString(strArr[0], ((int) rectangleC.getX()) + eJ.a(2), (((int) rectangleC.getY()) + fontMetrics.getHeight()) - eJ.a(2));
        graphics.drawString(strArr[1], ((int) rectangleC.getX()) + eJ.a(2), (((int) rectangleC.getY()) + (2 * fontMetrics.getHeight())) - eJ.a(2));
    }

    private Rectangle c(int i2, int i3) {
        int iA = eJ.a(10);
        int iA2 = eJ.a(8);
        bP bPVarE = e(x(), w());
        bP bPVarE2 = e(x(), w() + 1);
        bP bPVarE3 = e(x(), w() - 1);
        Rectangle rectangle = new Rectangle((bPVarE.d() + (getWidth() - bPVarE.d() > i2 + iA ? iA : -(i2 + iA))) - 2, ((bPVarE.f() + ((i3 * 2) + iA2 < bPVarE.f() ? -(i3 + iA2) : iA2)) - i3) + 2, i2 + 4, i3 * 2);
        if ((!a(bPVarE2, rectangle) && !a(bPVarE3, rectangle)) || rectangle.getY() + rectangle.getHeight() + (iA2 * 2) >= getHeight()) {
            return rectangle;
        }
        rectangle.setLocation((int) rectangle.getX(), (int) (rectangle.getY() + rectangle.getHeight() + (iA2 * 2)));
        return rectangle;
    }

    private boolean a(bP bPVar, Rectangle rectangle) {
        int iA = eJ.a(5);
        return bPVar != null && ((double) bPVar.d()) > rectangle.getX() && ((double) bPVar.d()) < (rectangle.getX() + rectangle.getWidth()) + ((double) iA) && ((double) bPVar.f()) > rectangle.getY() && ((double) bPVar.f()) < (rectangle.getY() + rectangle.getHeight()) + ((double) iA);
    }

    private void e(Graphics graphics) {
        ((Graphics2D) graphics).setStroke(this.f10937t);
        if (!this.f10953ap || this.f10951an == null || this.f10952ao == null) {
            return;
        }
        bP bPVar = new bP(this);
        bPVar.f10964c = 0;
        bPVar.f10965d = 0;
        int[] iArr = new int[this.f10951an.i()];
        int[] iArr2 = new int[this.f10952ao.i()];
        int i2 = 0;
        boolean zR = this.f10952ao.r();
        int iS = this.f10952ao.s();
        this.f10952ao.g(2);
        this.f10955ar = Double.NaN;
        this.f10954aq = Double.NaN;
        for (int i3 = 0; i3 < this.f10951an.i(); i3++) {
            if (this.f10956as == null || this.f10956as.a(i3)) {
                bPVar.a(this.f10951an.d(i3));
                bPVar.b(this.f10952ao.d(i3));
                int iD = bPVar.d();
                int iF = bPVar.f();
                if (bPVar.c() >= this.f10894M && bPVar.c() <= this.f10892K && bPVar.e() >= this.f10897P && bPVar.e() <= this.f10895N) {
                    iArr[i2] = iD;
                    iArr2[i2] = iF;
                    i2++;
                    if (Double.isNaN(this.f10954aq) || bPVar.c() < this.f10954aq) {
                        this.f10954aq = bPVar.c();
                    }
                    if (Double.isNaN(this.f10955ar) || bPVar.c() > this.f10955ar) {
                        this.f10955ar = bPVar.c();
                    }
                }
            }
        }
        this.f10952ao.b(zR);
        this.f10952ao.g(iS);
        graphics.setColor(this.f10925l);
        graphics.drawPolyline(iArr, iArr2, i2);
    }

    private void f(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i2 = 0; i2 < this.f10890I.size(); i2++) {
            if (this.f10941ak && this.f10891J.size() > i2) {
                bQ bQVar = (bQ) this.f10891J.get(i2);
                for (int i3 = 1; i3 < bQVar.size(); i3++) {
                    if (bQVar != null) {
                        graphics2D.setStroke(this.f10939v);
                        bP bPVar = (bP) bQVar.get(i3);
                        bP bPVar2 = (bP) bQVar.get(i3 - 1);
                        graphics.setColor(a(i2).darker().darker());
                        graphics.drawLine(bPVar2.d(), bPVar2.f(), bPVar.d(), bPVar.f());
                    }
                }
                for (int i4 = 0; i4 < bQVar.size(); i4++) {
                    bP bPVar3 = (bP) bQVar.get(i4);
                    graphics.setColor(Color.GRAY);
                    graphics.fillOval(bPVar3.d() - (this.f10905a / 2), bPVar3.f() - (this.f10905a / 2), this.f10905a, this.f10905a);
                }
            }
        }
        int size = this.f10890I.size();
        int i5 = size > 1 ? size + 1 : size;
        for (int i6 = 0; i6 < i5; i6++) {
            for (int i7 = 0; i7 < this.f10890I.size(); i7++) {
                bQ bQVar2 = (bQ) this.f10890I.get(i7);
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setStroke(a(i7, size, i6));
                for (int i8 = 1; i8 < bQVar2.size(); i8++) {
                    bP bPVar4 = (bP) bQVar2.get(i8);
                    bP bPVar5 = (bP) bQVar2.get(i8 - 1);
                    graphics.setColor(a(i7));
                    graphics.drawLine(bPVar5.d(), bPVar5.f(), bPVar4.d(), bPVar4.f());
                }
            }
        }
        for (int i9 = 0; i9 < this.f10890I.size(); i9++) {
            bQ bQVar3 = (bQ) this.f10890I.get(i9);
            for (int i10 = 0; i10 < bQVar3.size(); i10++) {
                bP bPVar6 = (bP) bQVar3.get(i10);
                if (this.f10907c.contains(bPVar6)) {
                    graphics.setColor(this.f10923j);
                    graphics.fillOval(bPVar6.d() - (this.f10906b / 2), bPVar6.f() - (this.f10906b / 2), this.f10906b, this.f10906b);
                } else {
                    graphics.setColor(this.f10922i);
                    graphics.fillOval(bPVar6.d() - (this.f10905a / 2), bPVar6.f() - (this.f10905a / 2), this.f10905a, this.f10905a);
                }
            }
        }
    }

    private Stroke a(int i2, int i3, int i4) {
        if (i3 <= 1) {
            return this.f10940w;
        }
        float fA = eJ.a(6.0f);
        return new BasicStroke(eJ.a(3.0f), 2, 2, 10.0f, new float[]{fA, fA * i3}, (i2 + i4) * fA);
    }

    private void g(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int iR = R();
        int height = (getHeight() - F().top) - F().bottom;
        graphics.setColor(getForeground());
        FontMetrics fontMetrics = getFontMetrics(this.f10928ah);
        FontMetrics fontMetrics2 = getFontMetrics(this.f10929ai);
        if (E() != null && !E().equals("")) {
            FontMetrics fontMetrics3 = getFontMetrics(this.f10930aj);
            int width = (getWidth() - fontMetrics3.stringWidth(E())) / 2;
            graphics.setFont(this.f10930aj);
            graphics.drawString(E(), width, fontMetrics3.getHeight() - eJ.a(3));
        }
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setFont(this.f10928ah);
        for (int i2 = 0; i2 < this.f10901T; i2++) {
            graphics.setColor(this.f10921h);
            int i3 = F().left + ((iR * i2) / (this.f10901T - 1));
            graphics.drawLine(i3, F().top, i3, height + F().top);
            double dC = T() ? ((bP) m(0).get(i2)).c() : this.f10894M + ((i2 * (this.f10892K - this.f10894M)) / (this.f10901T - 1));
            graphics.setColor(getForeground());
            String strB = bH.W.b(dC, this.f10899R);
            int iA = 0;
            if (i2 == 0) {
                iA = eJ.a(5);
            } else if (i2 == this.f10901T - 1) {
                iA = -(fontMetrics.stringWidth(strB) / 8);
            }
            graphics.drawString(strB, (i3 - (fontMetrics.stringWidth(strB) / 2)) + iA, F().top + height + fontMetrics.getHeight());
        }
        if (this.f10902U > 1) {
            for (int i4 = 0; i4 < this.f10902U; i4++) {
                int i5 = F().top + ((height * i4) / (this.f10902U - 1));
                graphics.drawLine(F().left, i5, iR + F().left, i5);
                String strB2 = bH.W.b(this.f10897P + ((((this.f10902U - 1) - i4) * (this.f10895N - this.f10897P)) / (this.f10902U - 1)), this.f10900S);
                int iA2 = 0;
                if (i4 == 0) {
                    iA2 = eJ.a(5);
                } else if (i4 == this.f10902U - 1) {
                    iA2 = eJ.a(-5);
                }
                graphics.drawString(strB2, F().left - fontMetrics.stringWidth(strB2), i5 + (fontMetrics.getHeight() / 3) + iA2);
            }
        } else {
            int i6 = F().top;
            graphics.drawLine(F().left, i6, iR + F().left, i6);
            int i7 = F().top + height;
            graphics.drawLine(F().left, i7, iR + F().left, i7);
        }
        graphics.setFont(this.f10929ai);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i8 = 0; i8 < this.f10890I.size(); i8++) {
            String strB3 = b(i8);
            if (strB3 != null && !strB3.isEmpty() && !arrayList2.contains(strB3)) {
                arrayList2.add(strB3);
            }
            String strC = c(i8);
            if (strC != null && !strC.isEmpty() && !arrayList.contains(strC)) {
                arrayList.add(strC);
            }
        }
        int iMax = Math.max(arrayList2.size(), arrayList.size());
        int i9 = 1;
        String str = null;
        int i10 = 0;
        while (true) {
            if (i10 >= iMax) {
                break;
            }
            String strC2 = c(i10);
            if (0 != 0 && !str.equals(strC2)) {
                i9 = iMax;
                break;
            } else {
                i9 = 1;
                i10++;
            }
        }
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        for (int i11 = 0; i11 < i9; i11++) {
            String strB4 = b(i11);
            String strC3 = c(i11);
            int iStringWidth = (int) ((F().left + (((i11 + 0.5d) * iR) / i9)) - (fontMetrics2.stringWidth(strB4) / 2));
            if (strB4 != null) {
                if (i9 > 1) {
                    graphics.setColor(a(i11));
                }
                graphics.drawString(strB4, iStringWidth, F().top + height + fontMetrics.getHeight() + this.f10929ai.getSize() + eJ.a(2));
            }
            if (strC3 != null) {
                if (i9 > 1) {
                    graphics.setColor(a(i11));
                }
                int length = (int) ((F().top + (((i11 + 0.5d) * height) / i9)) - ((strC3.length() * ((int) (fontMetrics2.getHeight() * 0.85d))) / 2));
                for (int i12 = 0; i12 < strC3.length(); i12++) {
                    String str2 = strC3.charAt(i12) + "";
                    graphics.drawString(str2, ((F().left - fontMetrics2.stringWidth(str2)) / 2) - eJ.a(10), length);
                    length += (int) (fontMetrics2.getHeight() * 0.85d);
                }
            }
        }
        if (this.f10926m.isEmpty()) {
            return;
        }
        graphics.setFont(this.f10928ah);
        int width2 = ((getWidth() - F().left) - F().right) / this.f10926m.size();
        for (int i13 = 0; i13 < this.f10926m.size(); i13++) {
            int i14 = (i13 * width2) + (width2 / 2);
            String str3 = (String) this.f10926m.get(i13);
            int iStringWidth2 = (F().left + i14) - (graphics.getFontMetrics().stringWidth(str3) / 2);
            int height2 = getHeight() - eJ.a(4);
            graphics.setColor(a(i13));
            graphics.drawString(str3, iStringWidth2, height2);
        }
    }

    private bP a(bQ bQVar, double d2) {
        bP bPVar = null;
        bP bPVar2 = null;
        if (d2 <= this.f10894M) {
            bP bPVar3 = new bP(this);
            bPVar3.f10964c = 0;
            bPVar3.b(((bP) bQVar.get(0)).e());
            if (T()) {
                bPVar3.a(((bP) bQVar.get(0)).c());
            } else {
                bPVar3.a(this.f10894M);
            }
            return bPVar3;
        }
        if (d2 >= this.f10892K) {
            bP bPVar4 = new bP(this);
            bPVar4.f10964c = bQVar.size() - 1;
            bPVar4.b(((bP) bQVar.get(bQVar.size() - 1)).e());
            if (T()) {
                bPVar4.a(((bP) bQVar.get(bQVar.size() - 1)).c());
            } else {
                bPVar4.a(this.f10892K);
            }
            return bPVar4;
        }
        if (d2 <= ((bP) bQVar.get(0)).c()) {
            bP bPVar5 = new bP(this);
            bPVar5.f10964c = 0;
            bPVar5.b(((bP) bQVar.get(0)).e());
            bPVar5.a(d2);
            return bPVar5;
        }
        if (d2 >= ((bP) bQVar.get(bQVar.size() - 1)).c()) {
            bP bPVar6 = new bP(this);
            bPVar6.f10964c = bQVar.size() - 1;
            bPVar6.b(((bP) bQVar.get(bQVar.size() - 1)).e());
            bPVar6.a(d2);
            return bPVar6;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= bQVar.size()) {
                break;
            }
            bPVar2 = (bP) bQVar.get(i2);
            if (bPVar2.c() > d2) {
                if (i2 > 0) {
                    bPVar = (bP) bQVar.get(i2 - 1);
                    break;
                }
                bPVar = bPVar2;
            }
            i2++;
        }
        bP bPVar7 = new bP(this, bPVar.a(), bPVar.b());
        bPVar7.a(d2);
        double dC = (d2 - bPVar.c()) / (bPVar2.c() - bPVar.c());
        bPVar7.b((bPVar.e() * (1.0d - dC)) + (bPVar2.e() * dC));
        return bPVar7;
    }

    public String b(int i2) {
        String strA = m(i2).a();
        if (strA == null && i2 > 0) {
            strA = m(0).a();
        }
        if (strA == null) {
            strA = "X";
        }
        return strA;
    }

    public String c(int i2) {
        bQ bQVarM = m(i2);
        if (bQVarM.b() == null || bQVarM.b().isEmpty()) {
            bQVarM = m(0);
        }
        return bQVarM.b() == null ? Constants._TAG_Y : bQVarM.b();
    }

    public boolean c() {
        if (e()) {
            return false;
        }
        boolean z2 = false;
        Iterator it = this.f10907c.iterator();
        while (it.hasNext()) {
            bP bPVar = (bP) it.next();
            if (bPVar == null) {
                return false;
            }
            double dC = bPVar.c() + g();
            if (bPVar != null && f10944x == a(dC, bPVar.a(), bPVar.b())) {
                bPVar.a(dC);
                o();
                f(bPVar.a(), bPVar.b());
                repaint();
                z2 = true;
            }
        }
        return z2;
    }

    public boolean d() {
        if (e()) {
            return false;
        }
        boolean z2 = false;
        Iterator it = this.f10907c.iterator();
        while (it.hasNext()) {
            bP bPVar = (bP) it.next();
            if (bPVar == null) {
                return false;
            }
            double dC = bPVar.c() - g();
            if (bPVar != null && f10944x == a(dC, bPVar.a(), bPVar.b())) {
                bPVar.a(dC);
                o();
                f(bPVar.a(), bPVar.b());
                repaint();
                z2 = true;
            }
        }
        return z2;
    }

    private bP M() {
        bP bPVar = null;
        Iterator it = this.f10890I.iterator();
        while (it.hasNext()) {
            bP bPVarA = a((bQ) it.next());
            if (bPVar == null || (bPVarA != null && bPVarA.c() > bPVar.c())) {
                bPVar = bPVarA;
            }
        }
        return bPVar;
    }

    private bP a(bQ bQVar) {
        bP bPVar = null;
        Iterator it = bQVar.iterator();
        while (it.hasNext()) {
            bP bPVar2 = (bP) it.next();
            if (bPVar == null || bPVar2.c() > bPVar.c()) {
                bPVar = bPVar2;
            }
        }
        return bPVar;
    }

    private bP N() {
        bP bPVar = null;
        Iterator it = this.f10890I.iterator();
        while (it.hasNext()) {
            bP bPVarB = b((bQ) it.next());
            if (bPVar == null || (bPVarB != null && bPVarB.e() > bPVar.e())) {
                bPVar = bPVarB;
            }
        }
        return bPVar;
    }

    private bP b(bQ bQVar) {
        bP bPVar = null;
        Iterator it = bQVar.iterator();
        while (it.hasNext()) {
            bP bPVar2 = (bP) it.next();
            if (bPVar == null || bPVar2.e() > bPVar.e()) {
                bPVar = bPVar2;
            }
        }
        return bPVar;
    }

    private bP O() {
        bP bPVar = null;
        Iterator it = this.f10890I.iterator();
        while (it.hasNext()) {
            bP bPVarC = c((bQ) it.next());
            if (bPVar == null || (bPVarC != null && bPVarC.e() < bPVar.e())) {
                bPVar = bPVarC;
            }
        }
        return bPVar;
    }

    private bP c(bQ bQVar) {
        bP bPVar = null;
        Iterator it = bQVar.iterator();
        while (it.hasNext()) {
            bP bPVar2 = (bP) it.next();
            if (bPVar == null || bPVar2.e() < bPVar.e()) {
                bPVar = bPVar2;
            }
        }
        return bPVar;
    }

    private boolean P() {
        bP bPVarM = M();
        if (bPVarM == null) {
            return false;
        }
        if (s()) {
            double d2 = bPVarM.c() > 0.0d ? 1.15d : 0.8695652173913044d;
            if (bPVarM.c() * d2 <= q() && (bPVarM.c() < h() / 2.0d || bPVarM.c() > h() * 0.95d)) {
                double dC = (bPVarM.c() * d2) + g();
                if (dC > q()) {
                    b(q());
                } else {
                    b(dC);
                }
            }
        }
        bP bPVarE = e(x(), w());
        if (bPVarE == null || f10944x == a(bPVarE.c(), this.f10908d.a(), this.f10908d.b())) {
            return false;
        }
        if (bPVarE.c() > h()) {
            bPVarE.a(h());
        } else if (bPVarE.c() < i()) {
            bPVarE.a(i());
        }
        o();
        f(x(), w());
        repaint();
        return true;
    }

    public boolean e() {
        return P() || Q();
    }

    private boolean Q() {
        bP bPVarN = N();
        bP bPVarO = O();
        if (bPVarN == null) {
            return false;
        }
        if (t()) {
            double d2 = bPVarN.e() > 0.0d ? 1.2d : 0.8333333333333334d;
            if (bPVarN.e() * d2 <= r() && (bPVarN.e() < j() / 2.0d || bPVarN.e() > j() - (g(bPVarN.b()) * 2.0d))) {
                double dE = (bPVarN.e() * d2) + g(bPVarN.b());
                if (dE > r()) {
                    d(r());
                } else {
                    d(dE);
                }
            }
            double d3 = bPVarO.e() < 0.0d ? 1.2d : 0.8333333333333334d;
            if (bPVarO.e() * d3 >= y() && (bPVarO.e() > k() / 2.0d || bPVarO.e() < k() + (g(bPVarO.b()) * 2.0d))) {
                double dE2 = (bPVarO.e() * d3) - g(bPVarO.b());
                if (dE2 < y()) {
                    e(y());
                } else {
                    e(dE2);
                }
            }
        }
        bP bPVarE = e(x(), w());
        if (bPVarE == null || f10944x == b(bPVarE.e(), this.f10908d.a(), this.f10908d.b())) {
            return false;
        }
        if (bPVarE.e() > j()) {
            d(bPVarE.e() + ((j() - k()) * 0.1d));
        } else if (bPVarE.e() < k()) {
            bPVarE.b(k());
        }
        o();
        f(x(), w());
        repaint();
        return true;
    }

    public int d(int i2) {
        return ((bQ) this.f10890I.get(i2)).size();
    }

    public double a(int i2, int i3) {
        return ((bP) ((bQ) this.f10890I.get(i2)).get(i3)).f10962a;
    }

    private int R() {
        return (getWidth() - F().left) - F().right;
    }

    public boolean e(int i2) {
        if (Q()) {
            return false;
        }
        boolean z2 = false;
        Iterator it = this.f10907c.iterator();
        while (it.hasNext()) {
            bP bPVar = (bP) it.next();
            if (bPVar == null) {
                return false;
            }
            double d2 = d(bPVar.a(), bPVar.b());
            double dE = bPVar.e() + (g(bPVar.b()) * i2);
            if (dE > d2) {
                dE = d2;
            }
            if (bPVar != null && f10944x == b(dE, bPVar.a(), bPVar.b())) {
                bPVar.b(dE);
                o();
                f(bPVar.a(), bPVar.b());
                repaint();
                z2 = true;
            }
        }
        return z2;
    }

    public boolean f(int i2) {
        if (Q()) {
            return false;
        }
        boolean z2 = false;
        Iterator it = this.f10907c.iterator();
        while (it.hasNext()) {
            bP bPVar = (bP) it.next();
            if (bPVar == null) {
                return false;
            }
            double dE = bPVar.e() - (g(bPVar.b()) * i2);
            if (dE < k()) {
                dE = k();
            }
            if (bPVar != null && f10944x == b(dE, bPVar.a(), bPVar.b())) {
                bPVar.b(dE);
                o();
                f(bPVar.a(), bPVar.b());
                repaint();
                z2 = true;
            }
        }
        return z2;
    }

    public void a(int i2, double d2, double d3) {
        bQ bQVarM = m(i2);
        bQVarM.add(new bP(this, d2, d3, i2, bQVarM.size()));
        o();
        e();
    }

    public void f() {
        this.f10890I.clear();
    }

    public void a(int i2, int i3, double d2) {
        bP bPVar = (bP) m(i2).get(i3);
        boolean z2 = bPVar.c() != d2;
        bPVar.a(d2);
        o();
        if (z2) {
            g(i2, i3);
        }
    }

    private double m(double d2) {
        return Math.rint(d2 * 1.0E7d) / 1.0E7d;
    }

    public void b(int i2, int i3, double d2) {
        double dM = m(d2);
        bP bPVar = (bP) m(i2).get(i3);
        boolean z2 = bPVar.e() != dM;
        bPVar.b(dM);
        o();
        if (z2) {
            h(i2, i3);
        }
    }

    public double g() {
        return this.f10888G;
    }

    public void a(double d2) {
        this.f10888G = d2;
        String str = "" + d2;
        this.f10899R = 0;
        if (d2 >= 1.0d) {
            return;
        }
        int i2 = 1;
        while (true) {
            if (str.charAt(i2) != '0' && str.charAt(i2) != '.') {
                return;
            }
            this.f10899R++;
            i2++;
        }
    }

    public double g(int i2) {
        return this.f10889H.a(i2);
    }

    public void a(G.dh dhVar) {
        this.f10889H = dhVar;
        String str = "" + dhVar.a();
        this.f10900S = 0;
        if (dhVar.a() >= 1.0d) {
            return;
        }
        int i2 = 1;
        while (true) {
            if (str.charAt(i2) != '0' && str.charAt(i2) != '.') {
                return;
            }
            this.f10900S++;
            i2++;
        }
    }

    public double h() {
        return this.f10892K;
    }

    public void b(double d2) {
        if (this.f10893L == Double.MIN_VALUE) {
            g(d2);
        }
        if (d2 > this.f10893L) {
            d2 = this.f10893L;
        }
        this.f10892K = d2;
        o();
    }

    public double i() {
        return this.f10894M;
    }

    public void c(double d2) {
        this.f10894M = d2;
        o();
    }

    public double j() {
        return this.f10895N;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double d(int i2, int i3) {
        return Double.isNaN(((bP) ((bQ) this.f10890I.get(i2)).get(i3)).g()) ? j() : ((bP) ((bQ) this.f10890I.get(i2)).get(i3)).g();
    }

    public void d(double d2) {
        if (this.f10896O == Double.MIN_VALUE) {
            h(d2);
        }
        if (d2 > this.f10896O) {
            d2 = this.f10896O;
        }
        this.f10895N = d2;
        o();
    }

    public double k() {
        return this.f10897P;
    }

    public void e(double d2) {
        this.f10897P = d2;
        o();
    }

    public void h(int i2) {
        this.f10901T = i2;
        o();
    }

    public void i(int i2) {
        this.f10902U = i2;
        o();
    }

    public void a(String str, int i2) {
        m(i2).a(str);
        o();
    }

    public void b(String str, int i2) {
        m(i2).b(str);
        o();
    }

    private bP e(int i2, int i3) {
        if (i3 < 0 || i2 < 0 || i2 >= this.f10890I.size() || i3 >= m(i2).size()) {
            return null;
        }
        return (bP) m(i2).get(i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(int i2, int i3) {
        if (i2 < 0 || i3 < 0) {
            return;
        }
        g(i2, i3);
        h(i2, i3);
    }

    private void g(int i2, int i3) {
        bP bPVarE = e(i2, i3);
        Iterator it = this.f10931n.iterator();
        while (it.hasNext()) {
            ((bS) it.next()).a(i2, i3, bPVarE.c());
        }
    }

    private void h(int i2, int i3) {
        bP bPVarE = e(i2, i3);
        Iterator it = this.f10931n.iterator();
        while (it.hasNext()) {
            ((bS) it.next()).b(i2, i3, bPVarE.e());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void S() {
        bP[] bPVarArr = (bP[]) this.f10907c.toArray(new bP[this.f10907c.size()]);
        Iterator it = this.f10932o.iterator();
        while (it.hasNext()) {
            ((bR) it.next()).a(bPVarArr);
        }
    }

    public void a(bS bSVar) {
        this.f10931n.add(bSVar);
    }

    public void a(bR bRVar) {
        this.f10932o.add(bRVar);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension dimension = (this.f10890I.size() == 0 || ((bQ) this.f10890I.get(0)).size() < 3) ? new Dimension(eJ.a(120), eJ.a(90)) : ((bQ) this.f10890I.get(0)).size() > 12 ? new Dimension(eJ.a(450), eJ.a(370)) : new Dimension(eJ.a(70) + (((bQ) this.f10890I.get(0)).size() * eJ.a(48)), (int) (Math.log(((bQ) this.f10890I.get(0)).size()) * eJ.a(126)));
        if (T() && this.f10901T > 7) {
            dimension.width = this.f10901T * eJ.a(38);
        }
        return dimension;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension preferredSize = getPreferredSize();
        preferredSize.height /= 2;
        preferredSize.width /= 2;
        return preferredSize;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        if (i2 != getX() || i3 != getY() || i4 != getWidth() || i5 != getHeight()) {
            this.f10933p = null;
            this.f10934q = true;
        }
        super.setBounds(i2, i3, i4, i5);
        getHeight();
    }

    private int a(double d2, int i2, int i3) {
        if (this.f10904W) {
            return f10949C;
        }
        bP bPVarE = e(i2, i3);
        bP bPVar = new bP(this, i2, i3);
        bPVar.b(bPVarE.e());
        bPVar.a(d2);
        return a(bPVar, i2, i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int b(int i2, int i3, int i4) {
        if (this.f10904W) {
            return f10949C;
        }
        bP bPVarE = e(i3, i4);
        bP bPVar = new bP(this, i3, i4);
        bPVar.b(bPVarE.e());
        bPVar.b(i2);
        return a(bPVar, i3, i4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int c(int i2, int i3, int i4) {
        bP bPVarE = e(i3, i4);
        bP bPVar = new bP(this, i3, i4);
        bPVar.a(bPVarE.c());
        bPVar.c(i2);
        return a(bPVar, i3, i4);
    }

    private int b(double d2, int i2, int i3) {
        bP bPVarE = e(i2, i3);
        bP bPVar = new bP(this, i2, i3);
        bPVar.a(bPVarE.c());
        bPVar.b(d2);
        return a(bPVar, i2, i3);
    }

    private int a(bP bPVar, int i2, int i3) {
        if (bPVar.c() > h()) {
            return f10948B;
        }
        if (bPVar.c() < i()) {
            return f10947A;
        }
        if (bPVar.e() > d(i2, i3)) {
            return f10945y;
        }
        if (bPVar.e() < k()) {
            return f10946z;
        }
        if (i3 + 1 < m(i2).size()) {
            if (bPVar.c() >= e(i2, i3 + 1).c()) {
                return f10949C;
            }
        }
        if (i3 > 0) {
            if (bPVar.c() <= e(i2, i3 - 1).c()) {
                return f10949C;
            }
        }
        return f10944x;
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            getComponent(i2).setEnabled(z2);
        }
    }

    public boolean l() {
        return this.f10904W;
    }

    public void a(boolean z2) {
        this.f10904W = z2;
    }

    public void a(Rectangle rectangle) {
        this.f10907c.clear();
        Iterator it = this.f10890I.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((bQ) it.next()).iterator();
            while (it2.hasNext()) {
                bP bPVar = (bP) it2.next();
                if (rectangle.contains(bPVar.d(), bPVar.f())) {
                    this.f10907c.add(bPVar);
                }
            }
        }
        o();
        S();
    }

    public void a(InterfaceC1648ef[] interfaceC1648efArr) {
        this.f10907c.clear();
        boolean z2 = false;
        if (interfaceC1648efArr != null) {
            for (InterfaceC1648ef interfaceC1648ef : interfaceC1648efArr) {
                if (interfaceC1648ef.a() < 0) {
                    z2 = true;
                    for (int i2 = 0; i2 < this.f10890I.size(); i2++) {
                        bP bPVarE = e(i2, interfaceC1648ef.b());
                        if (bPVarE != null && !this.f10907c.contains(bPVarE)) {
                            this.f10907c.add(bPVarE);
                            if (!this.f10943am) {
                                break;
                            }
                        }
                    }
                } else {
                    bP bPVarE2 = e(interfaceC1648ef.a(), interfaceC1648ef.b());
                    if (bPVarE2 != null && !this.f10907c.contains(bPVarE2)) {
                        z2 = true;
                        this.f10907c.add(bPVarE2);
                        if (!this.f10943am) {
                            break;
                        }
                    }
                }
            }
        }
        if (z2) {
            o();
            S();
        }
    }

    public void b(boolean z2) {
        if (x() >= 0) {
            if (w() == m(x()).size() - 1) {
                this.f10908d.f10964c = 0;
                m();
            } else {
                this.f10908d.f10964c++;
            }
        }
        if (z2 || !this.f10943am) {
            this.f10907c.clear();
        }
        bP bPVarE = e(this.f10908d.f10965d, this.f10908d.f10964c);
        if (bPVarE != null && !this.f10907c.contains(bPVarE)) {
            this.f10907c.add(bPVarE);
        }
        o();
        S();
    }

    public void m() {
        if (x() == this.f10890I.size() - 1) {
            this.f10908d.f10965d = 0;
        } else {
            this.f10908d.f10964c++;
        }
        o();
        S();
    }

    public void n() {
        if (x() <= 0) {
            this.f10908d.f10965d = this.f10890I.size() - 1;
        } else {
            this.f10908d.f10964c--;
        }
        o();
        S();
    }

    public void c(boolean z2) {
        if (x() < 0) {
            this.f10908d.f10965d = 0;
        }
        if (w() <= 0) {
            n();
            this.f10908d.f10964c = m(x()).size() - 1;
        } else {
            this.f10908d.f10964c--;
        }
        if (z2 || !this.f10943am) {
            this.f10907c.clear();
        }
        bP bPVarE = e(this.f10908d.f10965d, this.f10908d.f10964c);
        if (bPVarE != null && !this.f10907c.contains(bPVarE)) {
            this.f10907c.add(bPVarE);
        }
        o();
        S();
    }

    public void o() {
        this.f10934q = true;
    }

    public double p() {
        return this.f10909X;
    }

    public void f(double d2) {
        this.f10909X = d2;
    }

    public double q() {
        return this.f10893L;
    }

    public void g(double d2) {
        this.f10893L = d2;
    }

    public double r() {
        return this.f10896O;
    }

    public void h(double d2) {
        this.f10896O = d2;
    }

    public boolean s() {
        return this.f10913ab;
    }

    public void d(boolean z2) {
        this.f10913ab = z2;
    }

    public boolean t() {
        return this.f10912aa;
    }

    public void e(boolean z2) {
        this.f10912aa = z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public bQ m(int i2) {
        while (this.f10890I.size() <= i2) {
            this.f10890I.add(new bQ(this));
        }
        return (bQ) this.f10890I.get(i2);
    }

    public void b(int i2, int i3) {
        this.f10908d.f10965d = i2;
        this.f10908d.f10964c = i3;
        if (!this.f10942al) {
            this.f10907c.clear();
        }
        this.f10907c.add(((bQ) this.f10890I.get(i2)).get(i3));
        o();
        repaint();
    }

    public void u() {
        this.f10891J.clear();
        Iterator it = this.f10890I.iterator();
        while (it.hasNext()) {
            this.f10891J.add(((bQ) it.next()).clone());
        }
    }

    public void f(boolean z2) {
        this.f10941ak = z2;
    }

    public void v() {
        for (int i2 = 0; i2 < this.f10891J.size(); i2++) {
            for (int i3 = 0; i3 < ((bQ) this.f10891J.get(i2)).size(); i3++) {
                ((bP) ((bQ) this.f10890I.get(i2)).get(i3)).a(((bP) ((bQ) this.f10891J.get(i2)).get(i3)).c());
                ((bP) ((bQ) this.f10890I.get(i2)).get(i3)).b(((bP) ((bQ) this.f10891J.get(i2)).get(i3)).e());
                f(i2, i3);
            }
        }
    }

    public int w() {
        if (this.f10907c.isEmpty()) {
            return -1;
        }
        return this.f10908d.b();
    }

    public int x() {
        if (this.f10907c.isEmpty()) {
            return -1;
        }
        return this.f10908d.a();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int[] j(int i2) {
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < this.f10907c.size(); i3++) {
            if (((bP) this.f10907c.get(i3)).a() == i2) {
                arrayList.add(Integer.valueOf(((bP) this.f10907c.get(i3)).b()));
            }
        }
        int[] iArr = new int[arrayList.size()];
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            iArr[i4] = ((Integer) arrayList.get(i4)).intValue();
        }
        return iArr;
    }

    public double y() {
        return this.f10898Q;
    }

    public void i(double d2) {
        this.f10898Q = d2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean T() {
        bQ bQVarM = m(0);
        return bQVarM != null && bQVarM.size() == this.f10901T && this.f10904W;
    }

    public void g(boolean z2) {
        this.f10942al = z2;
    }

    public void c(int i2, int i3, double d2) {
        ((bP) ((bQ) this.f10890I.get(i2)).get(i3)).c(d2);
    }

    public void h(boolean z2) {
        this.f10943am = z2;
    }

    public int z() {
        return this.f10907c.size();
    }

    public void j(double d2) {
        bP bPVar;
        Iterator it = this.f10907c.iterator();
        while (it.hasNext() && (bPVar = (bP) it.next()) != null) {
            if (bPVar != null && f10944x == b(d2, bPVar.a(), bPVar.b())) {
                bPVar.b(d2);
                f(bPVar.a(), bPVar.b());
            }
        }
        o();
        repaint();
    }

    public void k(double d2) {
        this.f10910Y = d2;
    }

    public void i(boolean z2) {
        this.f10953ap = z2;
    }

    public C0184j A() {
        return this.f10951an;
    }

    public void a(C0184j c0184j) {
        this.f10951an = c0184j;
    }

    public C0184j B() {
        return this.f10952ao;
    }

    public void b(C0184j c0184j) {
        this.f10952ao = c0184j;
    }

    public void a(bT bTVar) {
        this.f10956as = bTVar;
    }

    public int C() {
        bQ bQVar = (bQ) this.f10890I.get(0);
        int size = bQVar.size() - 1;
        while (size >= 0) {
            if (((bP) bQVar.get(size)).c() < this.f10954aq) {
                return size < bQVar.size() - 1 ? size : bQVar.size() - 1;
            }
            size--;
        }
        return 0;
    }

    public int D() {
        bQ bQVar = (bQ) this.f10890I.get(0);
        for (int i2 = 0; i2 < bQVar.size() - 1; i2++) {
            if (((bP) bQVar.get(i2)).c() > this.f10955ar) {
                if (i2 > 0) {
                    return i2;
                }
                return 0;
            }
        }
        if (bQVar.size() > 0) {
            return bQVar.size() - 1;
        }
        return 0;
    }

    public String E() {
        return this.f10903V;
    }

    public void b(String str) {
        this.f10903V = str;
        o();
    }

    public Insets F() {
        if (this.f10927ag == null) {
            int i2 = this.f10926m.isEmpty() ? 35 : 55;
            if (E() == null || E().equals("")) {
                this.f10927ag = eJ.a(10, 50, i2, 17);
            } else {
                this.f10927ag = eJ.a(22, 50, i2, 17);
            }
        }
        return this.f10927ag;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        repaint();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        repaint();
    }

    public int G() {
        return this.f10915ad;
    }

    public void k(int i2) {
        this.f10915ad = i2;
    }

    public double H() {
        return this.f10914ac;
    }

    public void l(double d2) {
        this.f10914ac = d2;
        this.f10916ae = System.currentTimeMillis();
    }

    public boolean I() {
        return this.f10911Z;
    }

    public void j(boolean z2) {
        this.f10911Z = z2;
    }

    public long J() {
        return this.f10916ae;
    }

    public int K() {
        return this.f10918af;
    }

    public void l(int i2) {
        this.f10918af = i2;
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }
}
