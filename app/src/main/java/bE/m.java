package bE;

import bH.W;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.eJ;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.UIManager;

/* loaded from: TunerStudioMS.jar:bE/m.class */
public class m extends JComponent implements l {

    /* renamed from: a, reason: collision with root package name */
    public static int f6722a = 0;

    /* renamed from: b, reason: collision with root package name */
    public static int f6723b = 1;

    /* renamed from: c, reason: collision with root package name */
    public static int f6724c = 2;

    /* renamed from: x, reason: collision with root package name */
    private int f6725x = f6723b;

    /* renamed from: d, reason: collision with root package name */
    ArrayList f6726d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    ArrayList f6727e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    ArrayList f6728f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    Image f6729g = null;

    /* renamed from: h, reason: collision with root package name */
    Image f6730h = null;

    /* renamed from: y, reason: collision with root package name */
    private Insets f6731y = eJ.a(new Insets(5, 25, 15, 5));

    /* renamed from: z, reason: collision with root package name */
    private Insets f6732z = eJ.a(new Insets(20, 20, 0, 55));

    /* renamed from: A, reason: collision with root package name */
    private Color f6733A = Color.darkGray;

    /* renamed from: B, reason: collision with root package name */
    private Color f6734B = Color.WHITE;

    /* renamed from: C, reason: collision with root package name */
    private boolean f6735C = true;

    /* renamed from: D, reason: collision with root package name */
    private boolean f6736D = false;

    /* renamed from: E, reason: collision with root package name */
    private boolean f6737E = true;

    /* renamed from: F, reason: collision with root package name */
    private float[] f6738F = {2.0f, 5.0f, 2.0f, 5.0f};

    /* renamed from: i, reason: collision with root package name */
    Stroke f6739i = new BasicStroke(1.0f, 0, 0, 10.0f, this.f6738F, 0.0f);

    /* renamed from: j, reason: collision with root package name */
    Stroke f6740j = new BasicStroke(eJ.a(2));

    /* renamed from: k, reason: collision with root package name */
    Stroke f6741k = new BasicStroke(eJ.a(3));

    /* renamed from: l, reason: collision with root package name */
    Stroke f6742l = new BasicStroke(eJ.a(5));

    /* renamed from: G, reason: collision with root package name */
    private int f6743G = -1;

    /* renamed from: H, reason: collision with root package name */
    private int f6744H = -1;

    /* renamed from: I, reason: collision with root package name */
    private boolean f6745I = true;

    /* renamed from: m, reason: collision with root package name */
    Stroke f6746m = new BasicStroke(eJ.a(1));

    /* renamed from: n, reason: collision with root package name */
    double f6747n = Double.NaN;

    /* renamed from: o, reason: collision with root package name */
    double f6748o = Double.NaN;

    /* renamed from: J, reason: collision with root package name */
    private int f6749J = -1;

    /* renamed from: K, reason: collision with root package name */
    private int f6750K = -1;

    /* renamed from: L, reason: collision with root package name */
    private int f6751L = -1;

    /* renamed from: p, reason: collision with root package name */
    int f6752p = -1;

    /* renamed from: q, reason: collision with root package name */
    int f6753q = -1;

    /* renamed from: r, reason: collision with root package name */
    int f6754r = -1;

    /* renamed from: M, reason: collision with root package name */
    private int f6755M = 4;

    /* renamed from: N, reason: collision with root package name */
    private int f6756N = 4;

    /* renamed from: O, reason: collision with root package name */
    private int f6757O = Integer.MAX_VALUE;

    /* renamed from: P, reason: collision with root package name */
    private int f6758P = eJ.a(1);

    /* renamed from: Q, reason: collision with root package name */
    private int f6759Q = eJ.a(7);

    /* renamed from: R, reason: collision with root package name */
    private boolean f6760R = false;

    /* renamed from: s, reason: collision with root package name */
    int[][] f6761s = (int[][]) null;

    /* renamed from: t, reason: collision with root package name */
    int f6762t = 0;

    /* renamed from: u, reason: collision with root package name */
    int f6763u = Integer.MAX_VALUE;

    /* renamed from: S, reason: collision with root package name */
    private String f6764S = "X";

    /* renamed from: T, reason: collision with root package name */
    private String f6765T = Constants._TAG_Y;

    /* renamed from: U, reason: collision with root package name */
    private String f6766U = com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG;

    /* renamed from: v, reason: collision with root package name */
    double[][] f6767v = (double[][]) null;

    /* renamed from: w, reason: collision with root package name */
    Point f6768w = null;

    /* renamed from: V, reason: collision with root package name */
    private final List f6769V = new ArrayList();

    public m() {
        this.f6727e.add(Color.cyan);
        this.f6727e.add(Color.red);
        this.f6727e.add(Color.yellow);
        this.f6727e.add(Color.magenta);
        b();
        setBackground(Color.black);
        o oVar = new o(this);
        addMouseMotionListener(oVar);
        addMouseListener(oVar);
        addMouseWheelListener(oVar);
    }

    public void b() {
        Font font = UIManager.getFont("Label.font");
        int iA = eJ.a(11);
        if (font != null) {
            iA = Math.round(font.getSize2D() * (iA / eJ.a()));
        }
        setFont(new Font("SansSerif", 0, iA));
        this.f6732z = new Insets(iA * 2, iA * 2, 0, iA * 5);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        Graphics graphics2 = t().getGraphics();
        graphics2.setFont(getFont());
        graphics2.drawImage(s(), 0, 0, this);
        a(graphics2);
        b(graphics2);
        graphics.drawImage(this.f6730h, 0, 0, this);
        paintChildren(graphics);
    }

    private void a(Graphics graphics) {
        Point pointM = m();
        if (pointM != null) {
            int iA = eJ.a(14);
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(Color.BLACK);
            graphics2D.setStroke(this.f6742l);
            graphics.drawOval(pointM.f12370x - (iA / 2), pointM.f12371y - (iA / 2), iA, iA);
            graphics2D.setStroke(this.f6741k);
            graphics.setColor(Color.WHITE);
            graphics.drawOval(pointM.f12370x - (iA / 2), pointM.f12371y - (iA / 2), iA, iA);
        }
    }

    private void b(Graphics graphics) {
        if (!j() || h() < 0 || i() < 0) {
            return;
        }
        graphics.setFont(getFont());
        graphics.setColor(this.f6734B);
        ((Graphics2D) graphics).setStroke(this.f6746m);
        Rectangle rectangleC = c();
        if (rectangleC.contains(h(), i())) {
            int iA = eJ.a(150);
            graphics.setColor(g());
            graphics.drawLine(h(), rectangleC.f12373y, h(), rectangleC.height + rectangleC.f12373y);
            graphics.drawLine(rectangleC.f12372x, i(), rectangleC.width + rectangleC.f12372x, i());
            q qVarA = a((p) this.f6726d.get(0), h(), i());
            graphics.drawString(this.f6764S + " : " + W.b(qVarA.getX(), this.f6749J), (this.f6731y.left + iA) - graphics.getFontMetrics().stringWidth(this.f6764S), this.f6731y.top + graphics.getFont().getSize());
            graphics.drawString(this.f6765T + " : " + W.b(qVarA.getY(), this.f6750K), (this.f6731y.left + iA) - graphics.getFontMetrics().stringWidth(this.f6765T), this.f6731y.top + (graphics.getFont().getSize() * 2));
            int iH = h();
            int i2 = i();
            int iStringWidth = (this.f6731y.left + iA) - graphics.getFontMetrics().stringWidth("Hits");
            if (this.f6761s != null && this.f6761s.length > iH && this.f6761s[0].length > i2) {
                graphics.drawString("Hits : " + a(iH, i2), iStringWidth, this.f6731y.top + (graphics.getFont().getSize() * 3));
            }
            if (!Double.isNaN(qVarA.a()) && !Double.isInfinite(qVarA.a())) {
                graphics.drawString(this.f6766U + " : " + W.b(qVarA.a(), this.f6751L), (this.f6731y.left + iA) - graphics.getFontMetrics().stringWidth(this.f6766U), this.f6731y.top + (graphics.getFont().getSize() * 4));
            }
            Point point = this.f6768w;
            if (point != null) {
                graphics.drawRect(point.f12370x, point.f12371y, h() - point.f12370x, i() - point.f12371y);
            }
        }
    }

    private int a(int i2, int i3) {
        int i4 = 0;
        for (int i5 = i2 - 2; i5 <= i2 + 2 && i5 < this.f6761s.length; i5++) {
            for (int i6 = i3 - 2; i6 <= i3 + 2 && i6 < this.f6761s[0].length; i6++) {
                if (this.f6761s[i5][i6] > i4) {
                }
                i4 += this.f6761s[i5][i6];
            }
        }
        return i4;
    }

    private double b(int i2, int i3) {
        double d2 = Double.NaN;
        int i4 = 0;
        for (int i5 = i2 - 2; i5 <= i2 + 2 && i5 < this.f6767v.length; i5++) {
            for (int i6 = i3 - 2; i6 <= i3 + 2 && i6 < this.f6767v[0].length; i6++) {
                if (this.f6767v[i5][i6] != Double.NEGATIVE_INFINITY) {
                    i4++;
                }
            }
        }
        for (int i7 = i2 - 2; i7 <= i2 + 2 && i7 < this.f6767v.length; i7++) {
            for (int i8 = i3 - 2; i8 <= i3 + 2 && i8 < this.f6767v[0].length; i8++) {
                if (k() == f6723b) {
                    if (Double.isNaN(d2) && this.f6767v[i7][i8] != Double.NEGATIVE_INFINITY) {
                        d2 = this.f6767v[i7][i8] / i4;
                    } else if (this.f6767v[i7][i8] != Double.NEGATIVE_INFINITY) {
                        d2 += this.f6767v[i7][i8] / i4;
                    }
                } else if (k() == f6724c) {
                    if (this.f6767v[i7][i8] != Double.NEGATIVE_INFINITY && (Double.isNaN(d2) || d2 > this.f6767v[i7][i8])) {
                        d2 = this.f6767v[i7][i8];
                    }
                } else if (this.f6767v[i7][i8] != Double.NEGATIVE_INFINITY && (Double.isNaN(d2) || d2 < this.f6767v[i7][i8])) {
                    d2 = this.f6767v[i7][i8];
                }
            }
        }
        return d2;
    }

    public Rectangle c() {
        return new Rectangle(this.f6731y.left, this.f6731y.top, ((getWidth() - this.f6731y.left) - this.f6731y.right) + 1, ((getHeight() - this.f6731y.top) - this.f6731y.bottom) + 1);
    }

    private void c(Graphics graphics) {
        for (int i2 = 0; i2 < this.f6726d.size(); i2++) {
            graphics.setColor(a(i2));
            a(graphics, (p) this.f6726d.get(i2));
        }
    }

    private void a(Graphics graphics, p pVar) {
        System.nanoTime();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setStroke(this.f6740j);
        this.f6761s = new int[getWidth()][getHeight()];
        int i2 = 0;
        Rectangle rectangleC = c();
        rectangleC.height++;
        rectangleC.width++;
        if (this.f6767v == null || this.f6767v.length != getWidth() || this.f6767v[0].length != getHeight()) {
            this.f6767v = new double[getWidth()][getHeight()];
        }
        this.f6767v = a(this.f6767v);
        ArrayList arrayList = new ArrayList();
        for (int iF = pVar.f(); iF < pVar.e(); iF++) {
            q qVarA = pVar.a(iF);
            if (!qVarA.b()) {
                Point pointA = a(pVar, qVarA.getX(), qVarA.getY());
                if (rectangleC.contains(pointA)) {
                    int[] iArr = this.f6761s[pointA.f12370x];
                    int i3 = pointA.f12371y;
                    iArr[i3] = iArr[i3] + 1;
                    if (i2 < this.f6761s[pointA.f12370x][pointA.f12371y]) {
                        i2 = this.f6761s[pointA.f12370x][pointA.f12371y];
                    }
                    arrayList.add(qVarA);
                }
            }
        }
        this.f6763u = i2;
        b(graphics, pVar);
        Point point = null;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            q qVar = (q) it.next();
            Point pointA2 = a(pVar, qVar.getX(), qVar.getY());
            int iA = eJ.a(this.f6761s[pointA2.f12370x][pointA2.f12371y] <= this.f6759Q ? this.f6758P + (this.f6761s[pointA2.f12370x][pointA2.f12371y] / 3) : this.f6759Q);
            if (!Double.isNaN(pVar.i()) && !Double.isNaN(pVar.h())) {
                if (k() == f6723b) {
                    if (this.f6767v[pointA2.f12370x][pointA2.f12371y] == Double.NEGATIVE_INFINITY) {
                        this.f6767v[pointA2.f12370x][pointA2.f12371y] = qVar.a() / this.f6761s[pointA2.f12370x][pointA2.f12371y];
                    } else {
                        this.f6767v[pointA2.f12370x][pointA2.f12371y] = this.f6767v[pointA2.f12370x][pointA2.f12371y] + (qVar.a() / this.f6761s[pointA2.f12370x][pointA2.f12371y]);
                    }
                } else if (k() == f6724c) {
                    if (qVar.a() < this.f6767v[pointA2.f12370x][pointA2.f12371y] || this.f6767v[pointA2.f12370x][pointA2.f12371y] == Double.NEGATIVE_INFINITY) {
                        this.f6767v[pointA2.f12370x][pointA2.f12371y] = qVar.a();
                    }
                } else if (qVar.a() > this.f6767v[pointA2.f12370x][pointA2.f12371y]) {
                    this.f6767v[pointA2.f12370x][pointA2.f12371y] = qVar.a();
                }
            }
            graphics.setColor(a(pVar, this.f6767v[pointA2.f12370x][pointA2.f12371y], a(pointA2.f12370x, pointA2.f12371y)));
            graphics2D.fillOval(pointA2.f12370x - (iA / 2), pointA2.f12371y - (iA / 2), iA, iA);
            if (this.f6760R && point != null) {
                graphics.drawLine(point.f12370x, point.f12371y, pointA2.f12370x, pointA2.f12371y);
            }
            point = pointA2;
        }
    }

    private double[][] a(double[][] dArr) {
        for (double[] dArr2 : dArr) {
            for (int i2 = 0; i2 < dArr[0].length; i2++) {
                dArr2[i2] = Double.NEGATIVE_INFINITY;
            }
        }
        return dArr;
    }

    private void b(Graphics graphics, p pVar) {
        this.f6769V.clear();
        int iA = eJ.a(12);
        int height = (int) (getHeight() * 0.6d);
        int height2 = (int) (getHeight() * 0.2d);
        int height3 = getHeight() <= eJ.a(25) ? 1 : getHeight() / eJ.a(25);
        if (height3 > this.f6757O - 1) {
            height3 = this.f6757O - 1;
        }
        if (height3 < 1) {
            return;
        }
        int width = (getWidth() - iA) - 5;
        double dI = (Double.isNaN(pVar.i()) || Double.isNaN(pVar.h())) ? this.f6763u / (height3 + 1) : (pVar.i() - pVar.h()) / (height3 + 1);
        double d2 = dI / 2.0d;
        int i2 = height / (height3 + 1);
        graphics.setFont(getFont());
        for (int i3 = 0; i3 <= height3; i3++) {
            int i4 = (height2 + height) - ((i3 + 1) * i2);
            double d3 = (i3 / height3) * this.f6763u;
            double dI2 = (Double.isNaN(pVar.i()) || Double.isNaN(pVar.h())) ? d3 : ((i3 / height3) * (pVar.i() - pVar.h())) + pVar.h();
            String strB = W.b(dI2, this.f6754r);
            int iStringWidth = graphics.getFontMetrics().stringWidth(strB);
            Color colorA = a(pVar, dI2, (int) d3);
            graphics.setColor(colorA);
            graphics.fillRect(width, i4, iA, i2);
            if (i3 == 0) {
                this.f6769V.add(new n(this, dI2, dI2 + dI, colorA));
                graphics.setColor(a(0));
                graphics.drawString(strB, (width - iStringWidth) - 3, i4 + i2);
            } else if (i3 == height3) {
                this.f6769V.add(new n(this, dI2 - dI, dI2, colorA));
                graphics.setColor(a(0));
                graphics.drawString(strB, (width - iStringWidth) - 3, (i4 + i2) - (i2 - graphics.getFont().getSize()));
            } else {
                this.f6769V.add(new n(this, dI2 - d2, dI2 + d2, colorA));
                graphics.setColor(a(0));
                graphics.drawString(strB, (width - iStringWidth) - 3, (i4 + i2) - ((i2 - graphics.getFont().getSize()) / 2));
            }
        }
    }

    private Color a(p pVar, double d2, int i2) {
        if (!Double.isNaN(pVar.i()) && !Double.isNaN(pVar.h())) {
            if (!this.f6769V.isEmpty() && this.f6757O < Integer.MAX_VALUE) {
                for (n nVar : this.f6769V) {
                    if (d2 >= nVar.a() && d2 <= nVar.b()) {
                        return nVar.c();
                    }
                }
            }
            return C1677fh.b(d2, pVar.h(), pVar.i(), 0);
        }
        if (this.f6761s == null) {
            return getForeground();
        }
        if (i2 > this.f6763u) {
            i2 = this.f6763u;
        }
        if (!this.f6769V.isEmpty() && this.f6757O < Integer.MAX_VALUE) {
            for (n nVar2 : this.f6769V) {
                if (i2 > nVar2.a() && i2 <= nVar2.b()) {
                    return nVar2.c();
                }
            }
        }
        return C1677fh.b(i2, this.f6762t, this.f6763u, 0);
    }

    private Image s() {
        if (this.f6729g == null) {
            this.f6729g = createImage(getWidth(), getHeight());
            Graphics graphics = this.f6729g.getGraphics();
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, getWidth(), getHeight());
            d(graphics);
            c(graphics);
        }
        return this.f6729g;
    }

    private Image t() {
        if (this.f6730h != null && (this.f6730h.getWidth(null) != getWidth() || this.f6730h.getHeight(null) != getHeight())) {
            d();
        }
        if (this.f6730h == null) {
            this.f6730h = createImage(getWidth(), getHeight());
        }
        return this.f6730h;
    }

    private void d(Graphics graphics) {
        graphics.setColor(f());
        Insets insetsE = e();
        int width = (getWidth() - insetsE.left) - insetsE.right;
        int height = (getHeight() - insetsE.top) - insetsE.bottom;
        graphics.drawRect(insetsE.left, insetsE.top, width, height);
        graphics.setFont(getFont());
        int size = graphics.getFont().getSize();
        FontMetrics fontMetrics = graphics.getFontMetrics(getFont());
        ((Graphics2D) graphics).setStroke(this.f6739i);
        for (int i2 = 0; i2 <= this.f6756N; i2++) {
            int i3 = insetsE.top + ((i2 * height) / this.f6756N);
            graphics.setColor(this.f6733A);
            graphics.drawLine(insetsE.left, i3, insetsE.left + width, i3);
            if (this.f6735C && this.f6726d.size() > 0) {
                int size2 = this.f6737E ? 1 : this.f6726d.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    p pVar = (p) this.f6726d.get(i4);
                    String strB = W.b(pVar.d() - (((pVar.d() - pVar.c()) * i2) / this.f6756N), this.f6753q);
                    int iStringWidth = fontMetrics.stringWidth(strB);
                    int i5 = i3 + (size / 2);
                    if (i2 == this.f6756N) {
                        i5 = i3;
                    }
                    if (i2 == 0) {
                        i5 = i3 + size;
                    }
                    graphics.setColor(a(i4));
                    graphics.drawString(strB, (this.f6731y.left - iStringWidth) - eJ.a(5), i5);
                }
            }
        }
        for (int i6 = 0; i6 <= this.f6755M; i6++) {
            int i7 = insetsE.left + ((i6 * width) / this.f6755M);
            graphics.setColor(this.f6733A);
            graphics.drawLine(i7, insetsE.top, i7, insetsE.top + height);
            try {
                if (this.f6735C && this.f6726d.size() > 0) {
                    int size3 = this.f6737E ? 1 : this.f6726d.size();
                    for (int i8 = 0; i8 < size3; i8++) {
                        String strB2 = W.b(a((p) this.f6726d.get(i8), i7, insetsE.top + height).getX(), this.f6752p);
                        int iStringWidth2 = fontMetrics.stringWidth(strB2);
                        int i9 = i7 - (iStringWidth2 / 2);
                        if (i6 == this.f6755M) {
                            i9 = i7 - iStringWidth2;
                        }
                        if (i6 == 0) {
                            i9 = i7;
                        }
                        graphics.setColor(a(i8));
                        graphics.drawString(strB2, i9, insetsE.top + height + (size * (i8 + 1)));
                    }
                }
            } catch (Exception e2) {
            }
        }
    }

    private void u() {
        if (this.f6737E) {
            this.f6731y = new Insets(this.f6732z.top, this.f6732z.left + (eJ.a(25) * this.f6726d.size()), this.f6732z.bottom + (eJ.a(25) * this.f6726d.size()), this.f6732z.right);
        } else {
            this.f6731y = new Insets(this.f6732z.top, this.f6732z.left + eJ.a(25), this.f6732z.bottom + eJ.a(25), this.f6732z.right);
        }
    }

    public void a(p pVar) {
        this.f6726d.add(pVar);
        pVar.a(this);
        u();
    }

    public void a(p pVar, int i2) {
        if (this.f6726d.size() > i2) {
            ((p) this.f6726d.get(i2)).b(this);
            this.f6726d.set(i2, pVar);
        } else {
            this.f6726d.add(i2, pVar);
        }
        pVar.a(this);
        u();
    }

    public Color a(int i2) {
        return (Color) this.f6727e.get(i2 % this.f6727e.size());
    }

    public Point a(p pVar, double d2, double d3) {
        return new Point((int) (Math.round(((d2 - pVar.a()) / (pVar.b() - pVar.a())) * ((getWidth() - this.f6731y.left) - this.f6731y.right)) + this.f6731y.left), (getHeight() - this.f6731y.bottom) - ((int) Math.round(((d3 - pVar.c()) / (pVar.d() - pVar.c())) * ((getHeight() - this.f6731y.top) - this.f6731y.bottom))));
    }

    public q a(p pVar, int i2, int i3) {
        double dA;
        double d2;
        double dB = Double.NaN;
        if (i2 < this.f6731y.left) {
            dA = pVar.a();
        } else if (i2 > getWidth() - this.f6731y.right) {
            dA = pVar.b();
        } else {
            int i4 = i2 - this.f6731y.left;
            dA = pVar.a() + ((i4 / ((getWidth() - this.f6731y.left) - this.f6731y.right)) * (pVar.b() - pVar.a()));
        }
        if (i3 < this.f6731y.top) {
            d2 = pVar.d();
        } else if (i3 > getHeight() - this.f6731y.bottom) {
            d2 = pVar.c();
        } else {
            int i5 = i3 - this.f6731y.top;
            d2 = pVar.d() - ((i5 / ((getHeight() - this.f6731y.top) - this.f6731y.bottom)) * (pVar.d() - pVar.c()));
        }
        if (this.f6767v != null && this.f6767v.length > i2 && this.f6767v[0].length > i3) {
            dB = b(i2, i3);
        }
        return new b(dA, d2, dB);
    }

    public void d() {
        this.f6729g = null;
        this.f6730h = null;
    }

    public Insets e() {
        return this.f6731y;
    }

    public Color f() {
        return this.f6733A;
    }

    public p b(int i2) {
        if (i2 >= this.f6726d.size()) {
            return null;
        }
        return (p) this.f6726d.get(i2);
    }

    public Color g() {
        return this.f6734B;
    }

    public int h() {
        return this.f6743G;
    }

    public int i() {
        return this.f6744H;
    }

    public boolean j() {
        return this.f6745I;
    }

    @Override // bE.l
    public void a() {
        d();
        repaint();
    }

    public void a(String str) {
        this.f6764S = str;
    }

    public void b(String str) {
        this.f6765T = str;
    }

    public int k() {
        return this.f6725x;
    }

    public void c(int i2) {
        this.f6725x = i2;
    }

    public void c(String str) {
        this.f6766U = str;
    }

    public void a(k kVar) {
        this.f6728f.add(kVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(double d2, double d3, double d4, double d5) {
        Iterator it = this.f6728f.iterator();
        while (it.hasNext()) {
            ((k) it.next()).a(d2, d3, d4, d5);
        }
    }

    public void a(double d2, double d3) {
        if (b(0) != null) {
            Point pointA = a(b(0), d2, d3);
            this.f6747n = d2;
            this.f6748o = d3;
            this.f6743G = pointA.f12370x;
            this.f6744H = pointA.f12371y;
        }
    }

    public void l() {
        this.f6747n = Double.NaN;
    }

    public Point m() {
        if (Double.isNaN(this.f6747n)) {
            return null;
        }
        return a(b(0), this.f6747n, this.f6748o);
    }

    public int n() {
        return this.f6758P;
    }

    public void d(int i2) {
        this.f6758P = i2;
    }

    public int o() {
        return this.f6759Q;
    }

    public void e(int i2) {
        this.f6759Q = i2;
    }

    public void a(double d2) {
        double dA = ((p) this.f6726d.get(0)).a();
        double dB = ((p) this.f6726d.get(0)).b();
        double dC = ((p) this.f6726d.get(0)).c();
        double d3 = ((p) this.f6726d.get(0)).d();
        q qVarA = a((p) this.f6726d.get(0), h(), i());
        double x2 = dB - qVarA.getX();
        double x3 = qVarA.getX() - dA;
        double y2 = d3 - qVarA.getY();
        double y3 = qVarA.getY() - dC;
        double x4 = qVarA.getX() + (x2 / d2);
        a(qVarA.getX() - (x3 / d2), x4, qVarA.getY() - (y3 / d2), qVarA.getY() + (y2 / d2));
    }

    public int p() {
        return this.f6755M;
    }

    public void f(int i2) {
        this.f6755M = i2;
    }

    public int q() {
        return this.f6756N;
    }

    public void g(int i2) {
        this.f6756N = i2;
    }

    public void h(int i2) {
        this.f6749J = i2;
    }

    public void i(int i2) {
        this.f6750K = i2;
    }

    public void j(int i2) {
        this.f6751L = i2;
    }

    public int r() {
        return this.f6757O;
    }

    public void k(int i2) {
        this.f6757O = i2;
    }
}
