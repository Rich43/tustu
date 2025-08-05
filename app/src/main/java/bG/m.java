package bG;

import bH.S;
import bH.W;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:bG/m.class */
public class m extends JComponent {

    /* renamed from: c, reason: collision with root package name */
    public static int f6948c = 1;

    /* renamed from: d, reason: collision with root package name */
    public static int f6949d = 0;

    /* renamed from: e, reason: collision with root package name */
    public static int f6950e = 2;

    /* renamed from: a, reason: collision with root package name */
    double f6941a = 0.93d;

    /* renamed from: b, reason: collision with root package name */
    double f6942b = 0.07d;

    /* renamed from: p, reason: collision with root package name */
    private double f6943p = 0.12d;

    /* renamed from: q, reason: collision with root package name */
    private int f6944q = 135;

    /* renamed from: r, reason: collision with root package name */
    private double f6945r = 0.0d;

    /* renamed from: s, reason: collision with root package name */
    private int f6946s = 135;

    /* renamed from: t, reason: collision with root package name */
    private l f6947t = new C0986a(60, 2);

    /* renamed from: u, reason: collision with root package name */
    private int f6951u = f6948c;

    /* renamed from: v, reason: collision with root package name */
    private boolean f6952v = true;

    /* renamed from: f, reason: collision with root package name */
    Stroke f6953f = new BasicStroke(1.0f, 2, 0, 50.0f, null, 0.0f);

    /* renamed from: g, reason: collision with root package name */
    Stroke f6954g = new BasicStroke(2.0f, 2, 0, 50.0f, null, 0.0f);

    /* renamed from: h, reason: collision with root package name */
    Stroke f6955h = new BasicStroke(3.0f);

    /* renamed from: i, reason: collision with root package name */
    Color f6956i = Color.GREEN;

    /* renamed from: j, reason: collision with root package name */
    Color f6957j = Color.BLUE;

    /* renamed from: k, reason: collision with root package name */
    Color f6958k = Color.RED;

    /* renamed from: w, reason: collision with root package name */
    private boolean f6959w = false;

    /* renamed from: l, reason: collision with root package name */
    ArrayList f6960l = new ArrayList();

    /* renamed from: m, reason: collision with root package name */
    HashMap f6961m = new HashMap();

    /* renamed from: n, reason: collision with root package name */
    ArrayList f6962n = new ArrayList();

    /* renamed from: o, reason: collision with root package name */
    List f6963o = new ArrayList();

    public m() {
        setBackground(Color.WHITE);
        n nVar = new n(this);
        addMouseListener(nVar);
        addMouseMotionListener(nVar);
    }

    public void a(j jVar) {
        this.f6963o.add(jVar);
    }

    private void a(List list) {
        Iterator it = this.f6963o.iterator();
        while (it.hasNext()) {
            ((j) it.next()).a(list);
        }
    }

    public void a(p pVar) {
        this.f6960l.add(pVar);
    }

    public void b(p pVar) {
        this.f6960l.remove(pVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        double dC = c();
        Iterator it = this.f6960l.iterator();
        while (it.hasNext()) {
            ((p) it.next()).a(dC);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        d(graphics);
        graphics.setColor(getForeground());
        int iM = m();
        int i2 = iM - ((int) (iM * this.f6942b));
        double width = (getWidth() - i2) / 2;
        double height = (getHeight() - i2) / 2;
        double width2 = (getWidth() - iM) / 2;
        double height2 = (getHeight() - iM) / 2;
        ArrayList arrayListA = e().a();
        if (arrayListA.size() > 0) {
            double dL = l() - (360.0d - (((k) arrayListA.get(arrayListA.size() - 1)).a() + ((k) arrayListA.get(arrayListA.size() - 1)).b()));
            graphics2D.setStroke(this.f6953f);
            ArrayList arrayListA2 = e().a();
            n();
            for (int i3 = 0; i3 < arrayListA2.size(); i3++) {
                k kVar = (k) arrayListA2.get(i3);
                double d2 = dL;
                double dL2 = (l() + kVar.a()) - dL;
                double dB = kVar.b();
                graphics.setColor(getForeground());
                graphics2D.setStroke(this.f6953f);
                a(graphics, width, height, i2, i2, d2, dL2);
                double d3 = d2 + dL2;
                boolean zContains = this.f6962n.contains(Integer.valueOf(i3));
                if (zContains) {
                    graphics.setColor(this.f6958k);
                    graphics2D.setStroke(this.f6954g);
                }
                a(graphics, width2, height2, iM, iM, d3, dB);
                o oVarB = b(i3);
                oVarB.c(((d3 + dB) % 360.0d) - dB);
                oVarB.d((d3 + dB) % 360.0d);
                oVarB.a(i2 / 2);
                oVarB.b(iM / 2);
                Point2D point2DA = a(i2, i2, d3, getWidth() / 2.0d, getHeight() / 2.0d, 1.0d, false);
                Point2D point2DA2 = a(iM, iM, d3, getWidth() / 2.0d, getHeight() / 2.0d, 1.0d, false);
                a(graphics, point2DA.getX(), point2DA.getY(), point2DA2.getX(), point2DA2.getY());
                Point2D point2DA3 = a(i2, i2, d3 + dB, getWidth() / 2.0d, getHeight() / 2.0d, 1.0d, false);
                Point2D point2DA4 = a(iM, iM, d3 + dB, getWidth() / 2.0d, getHeight() / 2.0d, 1.0d, false);
                a(graphics, point2DA3.getX(), point2DA3.getY(), point2DA4.getX(), point2DA4.getY());
                if (zContains) {
                    String str = kVar.a() + "°";
                    int iStringWidth = getFontMetrics(graphics.getFont()).stringWidth(str) / 2;
                    int size = graphics.getFont().getSize() / 2;
                    Point2D point2DA5 = a(i2, i2, d3, getWidth() / 2.0d, getHeight() / 2.0d, 0.9d, false);
                    graphics.drawString(str, ((int) point2DA5.getX()) - iStringWidth, ((int) point2DA5.getY()) + size);
                    String str2 = (kVar.a() + dB) + "°";
                    int iStringWidth2 = getFontMetrics(graphics.getFont()).stringWidth(str2) / 2;
                    Point2D point2DA6 = a(iM, iM, d3 + dB, getWidth() / 2.0d, getHeight() / 2.0d, 1.09d, false);
                    graphics.drawString(str2, ((int) point2DA6.getX()) - iStringWidth2, ((int) point2DA6.getY()) + size);
                }
                dL = l() + kVar.a() + kVar.b();
            }
        } else {
            graphics2D.setStroke(this.f6953f);
            a(graphics, width, height, i2, i2, 0.0d, 360.0d);
        }
        graphics2D.setStroke(this.f6954g);
        b(graphics);
        c(graphics);
        a(graphics);
        if (isEnabled()) {
            return;
        }
        graphics.setColor(new Color(230, 230, 230, 165));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    public void a(Graphics graphics) {
        int iM = (int) (m() * (this.f6941a - 0.3d));
        ((Graphics2D) graphics).setStroke(new BasicStroke((int) (iM * 0.05d)));
        graphics.drawArc(Math.round((getWidth() - iM) / 2), Math.round((getHeight() - iM) / 2), iM, iM, 60, 60);
        Polygon polygon = new Polygon();
        if (this.f6952v) {
            Point2D point2DA = a(iM, iM, 60 + 1, getWidth() / 2, getHeight() / 2, 1.14d, false);
            polygon.addPoint(Math.round((float) point2DA.getX()), Math.round((float) point2DA.getY()));
            Point2D point2DA2 = a(iM, iM, 60 - 10, getWidth() / 2, getHeight() / 2, 1.009d, false);
            polygon.addPoint(Math.round((float) point2DA2.getX()), Math.round((float) point2DA2.getY()));
            Point2D point2DA3 = a(iM, iM, 60 + 1, getWidth() / 2, getHeight() / 2, 0.87d, false);
            polygon.addPoint(Math.round((float) point2DA3.getX()), Math.round((float) point2DA3.getY()));
        } else {
            Point2D point2DA4 = a(iM, iM, 60 - 1, getWidth() / 2, getHeight() / 2, 1.14d, false);
            polygon.addPoint(Math.round((float) point2DA4.getX()), Math.round((float) point2DA4.getY()));
            Point2D point2DA5 = a(iM, iM, 60 + 10, getWidth() / 2, getHeight() / 2, 1.009d, false);
            polygon.addPoint(Math.round((float) point2DA5.getX()), Math.round((float) point2DA5.getY()));
            Point2D point2DA6 = a(iM, iM, 60 - 1, getWidth() / 2, getHeight() / 2, 0.87d, false);
            polygon.addPoint(Math.round((float) point2DA6.getX()), Math.round((float) point2DA6.getY()));
        }
        graphics.fillPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
    }

    private void c(Graphics graphics) {
        int iM = (int) (m() * this.f6943p);
        Polygon polygon = new Polygon();
        Point2D point2DA = a(iM, iM, l() + 0, getWidth() / 2, getHeight() / 2, 1.0d, false);
        polygon.addPoint(Math.round((float) point2DA.getX()), Math.round((float) point2DA.getY()));
        int i2 = 0 + 60;
        Point2D point2DA2 = a(iM, iM, l() + i2, getWidth() / 2, getHeight() / 2, 1.0d, false);
        polygon.addPoint(Math.round((float) point2DA2.getX()), Math.round((float) point2DA2.getY()));
        int i3 = i2 + 60;
        Point2D point2DA3 = a(iM, iM, l() + i3, getWidth() / 2, getHeight() / 2, 1.0d, false);
        polygon.addPoint(Math.round((float) point2DA3.getX()), Math.round((float) point2DA3.getY()));
        int i4 = i3 + 60;
        Point2D point2DA4 = a(iM, iM, l() + i4, getWidth() / 2, getHeight() / 2, 1.0d, false);
        polygon.addPoint(Math.round((float) point2DA4.getX()), Math.round((float) point2DA4.getY()));
        int i5 = i4 + 60;
        Point2D point2DA5 = a(iM, iM, l() + i5, getWidth() / 2, getHeight() / 2, 1.0d, false);
        polygon.addPoint(Math.round((float) point2DA5.getX()), Math.round((float) point2DA5.getY()));
        int i6 = i5 + 60;
        Point2D point2DA6 = a(iM, iM, l() + i6, getWidth() / 2, getHeight() / 2, 1.0d, false);
        polygon.addPoint(Math.round((float) point2DA6.getX()), Math.round((float) point2DA6.getY()));
        int i7 = i6 + 60;
        graphics.setColor(getForeground());
        graphics.fillPolygon(polygon);
    }

    public void b(Graphics graphics) {
        graphics.setColor(getForeground());
        double d2 = 1.0d + (0.03d * 2.0d);
        double d3 = 1.0d + (0.03d * 2.0d) + 0.25d;
        int iM = m();
        Polygon polygon = new Polygon();
        Point2D point2DA = a(iM, iM, this.f6944q - (5.0d / 2.0d), getWidth() / 2, getHeight() / 2, d2, false);
        polygon.addPoint(Math.round((float) point2DA.getX()), Math.round((float) point2DA.getY()));
        Point2D point2DA2 = a(iM, iM, this.f6944q + (5.0d / 2.0d), getWidth() / 2, getHeight() / 2, d2, false);
        polygon.addPoint(Math.round((float) point2DA2.getX()), Math.round((float) point2DA2.getY()));
        Point2D point2DA3 = a(iM, iM, this.f6944q + (5.0d / 2.3d), getWidth() / 2, getHeight() / 2, d3, false);
        polygon.addPoint(Math.round((float) point2DA3.getX()), Math.round((float) point2DA3.getY()));
        Point2D point2DA4 = a(iM, iM, this.f6944q - (5.0d / 2.3d), getWidth() / 2, getHeight() / 2, d3, false);
        polygon.addPoint(Math.round((float) point2DA4.getX()), Math.round((float) point2DA4.getY()));
        graphics.drawPolygon(polygon);
        Polygon polygon2 = new Polygon();
        Point2D point2DA5 = a(iM, iM, this.f6944q - (2.0d / 2.0d), getWidth() / 2, getHeight() / 2, d2 - 0.03d, false);
        polygon2.addPoint(Math.round((float) point2DA5.getX()), Math.round((float) point2DA5.getY()));
        Point2D point2DA6 = a(iM, iM, this.f6944q + (2.0d / 2.0d), getWidth() / 2, getHeight() / 2, d2 - 0.03d, false);
        polygon2.addPoint(Math.round((float) point2DA6.getX()), Math.round((float) point2DA6.getY()));
        Point2D point2DA7 = a(iM, iM, this.f6944q + (2.0d / 2.0d), getWidth() / 2, getHeight() / 2, d2, false);
        polygon2.addPoint(Math.round((float) point2DA7.getX()), Math.round((float) point2DA7.getY()));
        Point2D point2DA8 = a(iM, iM, this.f6944q - (2.0d / 2.0d), getWidth() / 2, getHeight() / 2, d2, false);
        polygon2.addPoint(Math.round((float) point2DA8.getX()), Math.round((float) point2DA8.getY()));
        graphics.fillPolygon(polygon2);
    }

    public void a(Integer num) {
        if (this.f6962n.contains(num)) {
            return;
        }
        this.f6962n.add(num);
        repaint();
        a(this.f6962n);
    }

    public void b(Integer num) {
        if (this.f6962n.remove(num)) {
            a(this.f6962n);
            repaint();
        }
    }

    public void c(Integer num) {
        if (this.f6962n.contains(num)) {
            b(num);
        } else {
            a(num);
        }
    }

    protected Point2D a(int i2, int i3, double d2, double d3, double d4, double d5, boolean z2) {
        double d6 = z2 ? (((180.0d - d2) * 2.0d) * 3.141592653589793d) / 360.0d : ((d2 * 2.0d) * 3.141592653589793d) / 360.0d;
        double dSqrt = Math.sqrt((Math.pow(i2 / 2.0d, 2.0d) * Math.pow(i3 / 2.0d, 2.0d)) / (Math.pow((i2 / 2.0d) * Math.sin(d6), 2.0d) + Math.pow((i3 / 2.0d) * Math.cos(d6), 2.0d))) * d5;
        return new Point2D.Double(d3 + (dSqrt * Math.cos(d6)), d4 - (dSqrt * Math.sin(d6)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double b(int i2, int i3) {
        Point point = new Point(getWidth() / 2, getHeight() / 2);
        double dAtan2 = Math.atan2(Math.abs(i3 - point.f12371y), i2 - point.f12370x);
        if (i3 > point.f12371y) {
            dAtan2 = 6.283185307179586d - dAtan2;
        }
        return Math.toDegrees(dAtan2);
    }

    public int a(int i2, int i3) {
        double dB = b(i2, i3);
        double dSqrt = Math.sqrt(Math.pow(i2 - (getWidth() / 2), 2.0d) + Math.pow(i3 - (getHeight() / 2), 2.0d));
        for (o oVar : this.f6961m.values()) {
            if (oVar.a(dSqrt, dB)) {
                return oVar.b();
            }
        }
        return -1;
    }

    private double l() {
        return (((-c()) + f()) + this.f6946s) - e().b();
    }

    public double a() {
        return 360.0d - c();
    }

    public int b() {
        k kVar = null;
        ArrayList arrayListA = e().a();
        double dA = 360.0d - a();
        if (dA > 360.0d) {
            dA -= 360.0d;
        }
        if (dA < 0.0d) {
            dA += 360.0d;
        }
        for (int i2 = 0; i2 < arrayListA.size(); i2++) {
            k kVar2 = (k) arrayListA.get(i2);
            if (d() == f6948c) {
                if (kVar2.a() > dA) {
                    return kVar == null ? i2 - 1 : i2 - 1;
                }
            } else if (kVar2.a() > dA) {
                return kVar == null ? i2 - 1 : i2 - 1;
            }
            kVar = kVar2;
        }
        return arrayListA.size() - 1;
    }

    public double c() {
        return this.f6945r;
    }

    public void a(double d2) {
        this.f6945r = d2 % 360.0d;
    }

    private int m() {
        return (int) (getWidth() > getHeight() ? Math.round(getHeight() * this.f6941a) : Math.round(getWidth() * this.f6941a));
    }

    public int d() {
        return this.f6951u;
    }

    public void a(int i2) {
        this.f6951u = i2;
    }

    public l e() {
        return this.f6947t;
    }

    public void a(l lVar) {
        this.f6947t = lVar;
    }

    private void a(Graphics graphics, double d2, double d3, double d4, double d5) {
        ((Graphics2D) graphics).draw(new Line2D.Double(d2, d3, d4, d5));
    }

    private void a(Graphics graphics, double d2, double d3, int i2, int i3, double d4, double d5) {
        if (0 == 0) {
            ((Graphics2D) graphics).draw(new Arc2D.Double(d2, d3, i2, i3, d4, d5, 0));
        } else {
            Point2D point2DA = a(i2, i3, d4, getWidth() / 2.0d, getHeight() / 2.0d, 1.0d, false);
            Point2D point2DA2 = a(i2, i3, d4 + d5, getWidth() / 2.0d, getHeight() / 2.0d, 1.0d, false);
            a(graphics, point2DA.getX(), point2DA.getY(), point2DA2.getX(), point2DA2.getY());
        }
    }

    public double f() {
        if (d() == f6948c) {
            return 0.0d;
        }
        return -((k) this.f6947t.a().get(0)).b();
    }

    private void d(Graphics graphics) {
        graphics.setColor(this.f6957j);
        ((Graphics2D) graphics).setStroke(this.f6955h);
        int iM = (int) (m() * this.f6943p * 1.35d);
        double width = (getWidth() - iM) / 2;
        double height = (getHeight() - iM) / 2;
        double d2 = this.f6944q;
        double dC = this.f6944q - c();
        Point point = new Point(getWidth() / 2, getHeight() / 2);
        Point2D point2DA = a(m(), m(), d2, getWidth() / 2, getHeight() / 2, 1.0d, false);
        Point2D point2DA2 = a(m(), m(), dC, getWidth() / 2, getHeight() / 2, 1.0d, false);
        a(graphics, point.getX(), point.getY(), point2DA.getX(), point2DA.getY());
        a(graphics, point.getX(), point.getY(), point2DA2.getX(), point2DA2.getY());
        a(graphics, width, height, iM, iM, this.f6944q, -c());
        String str = W.a(c()) + S.a();
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int iStringWidth = fontMetrics.stringWidth(str);
        Point2D point2DA3 = Math.abs(c()) < ((double) 33) ? a(m(), m(), d2 - 45.0d, getWidth() / 2, getHeight() / 2, 0.43d, false) : a(m(), m(), (d2 + dC) / 2.0d, getWidth() / 2, getHeight() / 2, 0.43d, false);
        graphics.drawString(str, ((int) point2DA3.getX()) - (iStringWidth / 2), ((int) point2DA3.getY()) + (fontMetrics.getHeight() / 2));
    }

    public boolean g() {
        return this.f6959w;
    }

    public void a(boolean z2) {
        this.f6959w = z2;
        if (z2) {
            this.f6941a = 0.85d;
        } else {
            this.f6941a = 0.93d;
        }
    }

    private o b(int i2) {
        o oVar = (o) this.f6961m.get(Integer.valueOf(i2));
        if (oVar == null) {
            oVar = new o(this, i2);
            this.f6961m.put(Integer.valueOf(i2), oVar);
        }
        return oVar;
    }

    private void n() {
        for (o oVar : this.f6961m.values()) {
            oVar.b(oVar.a());
        }
    }

    public Integer[] h() {
        return (Integer[]) this.f6962n.toArray(new Integer[this.f6962n.size()]);
    }

    public void i() {
        this.f6962n.clear();
        a(this.f6962n);
    }

    public void j() {
        for (int i2 = 0; i2 < e().a().size(); i2++) {
            a(Integer.valueOf(i2));
        }
    }
}
