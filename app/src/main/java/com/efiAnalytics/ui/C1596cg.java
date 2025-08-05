package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.ui.cg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cg.class */
public class C1596cg extends JPanel {

    /* renamed from: b, reason: collision with root package name */
    public static int f11244b = 1;

    /* renamed from: c, reason: collision with root package name */
    public static int f11245c = 2;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f11243a = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    int f11246d = f11244b;

    /* renamed from: e, reason: collision with root package name */
    double f11247e = 0.0d;

    /* renamed from: f, reason: collision with root package name */
    double f11248f = 100.0d;

    /* renamed from: g, reason: collision with root package name */
    double f11249g = 0.0d;

    /* renamed from: h, reason: collision with root package name */
    double f11250h = 100.0d;

    /* renamed from: i, reason: collision with root package name */
    int f11251i = eJ.a(5);

    /* renamed from: j, reason: collision with root package name */
    int f11252j = eJ.a(13);

    /* renamed from: k, reason: collision with root package name */
    Insets f11253k = eJ.a(new Insets(5, 5, 5, 5));

    /* renamed from: l, reason: collision with root package name */
    Color f11254l = Color.BLUE;

    /* renamed from: m, reason: collision with root package name */
    Rectangle f11255m = null;

    /* renamed from: n, reason: collision with root package name */
    Rectangle f11256n = null;

    public C1596cg() {
        C1597ch c1597ch = new C1597ch(this);
        addMouseMotionListener(c1597ch);
        addMouseListener(c1597ch);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        a(graphics);
    }

    private void a(Graphics graphics) {
        graphics.clearRect(0, 0, getWidth(), getHeight());
        Rectangle rectangleD = d();
        graphics.setColor(Color.lightGray);
        graphics.draw3DRect(rectangleD.f12372x - 1, rectangleD.f12373y - 1, rectangleD.width + 2, rectangleD.height + 2, false);
        Point pointB = b();
        Point pointC = c();
        graphics.setColor(this.f11254l);
        if (this.f11246d != f11245c) {
            graphics.setColor(Color.gray);
            graphics.draw3DRect(rectangleD.f12372x + 1, rectangleD.f12373y + (rectangleD.height / 2), rectangleD.width - 2, 1, false);
        }
        if (this.f11246d == f11245c) {
            this.f11256n = new Rectangle(pointB.f12370x - (this.f11252j / 2), this.f11252j / 2, pointB.f12370x + (this.f11251i / 2), pointB.f12371y + (this.f11252j / 2));
            this.f11255m = new Rectangle(pointC.f12370x - (this.f11252j / 2), this.f11252j / 2, pointC.f12370x + (this.f11251i / 2), pointC.f12371y + (this.f11252j / 2));
        } else {
            this.f11256n = new Rectangle(pointB.f12370x - (this.f11251i / 2), (pointB.f12371y - 1) - (this.f11252j / 2), this.f11251i, pointB.f12371y + (this.f11252j / 2));
            this.f11255m = new Rectangle(pointC.f12370x - (this.f11251i / 2), (pointC.f12371y - 1) - (this.f11252j / 2), this.f11251i, pointC.f12371y + (this.f11252j / 2));
        }
        graphics.setColor(Color.lightGray);
        graphics.fill3DRect(this.f11256n.f12372x, this.f11256n.f12373y, this.f11256n.width, this.f11256n.height, true);
        graphics.fill3DRect(this.f11255m.f12372x, this.f11255m.f12373y, this.f11255m.width, this.f11255m.height, true);
    }

    private Point b() {
        return e(this.f11249g);
    }

    private Point c() {
        return e(this.f11250h);
    }

    private Point e(double d2) {
        return f((d2 - this.f11247e) / (this.f11248f - this.f11247e));
    }

    private Point f(double d2) {
        Rectangle rectangleD = d();
        return this.f11246d == f11245c ? new Point(rectangleD.f12372x + (rectangleD.width / 2), (rectangleD.f12373y + rectangleD.height) - ((int) (rectangleD.height * d2))) : new Point(rectangleD.f12372x + ((int) (rectangleD.width * d2)), rectangleD.f12373y + (rectangleD.height / 2));
    }

    private Rectangle d() {
        Rectangle rectangle;
        if (this.f11246d == f11245c) {
            int i2 = this.f11252j;
            rectangle = new Rectangle(this.f11253k.left + ((((getWidth() - i2) - this.f11253k.right) - this.f11253k.left) / 2), this.f11253k.top, i2, (getHeight() - this.f11253k.top) - this.f11253k.bottom);
        } else {
            int width = (getWidth() - this.f11253k.right) - this.f11253k.left;
            int i3 = this.f11252j;
            rectangle = new Rectangle(this.f11253k.left, this.f11253k.top + ((((getHeight() - i3) - this.f11253k.top) - this.f11253k.bottom) / 2), width, i3);
        }
        return rectangle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double a(int i2, int i3) {
        Rectangle rectangleD = d();
        double d2 = this.f11246d == f11245c ? 1.0d - ((i3 - rectangleD.f12373y) / rectangleD.height) : (i2 - rectangleD.f12372x) / rectangleD.width;
        return d2 >= 1.0d ? this.f11248f : d2 <= 0.0d ? this.f11247e : this.f11247e + (d2 * (this.f11248f - this.f11247e));
    }

    public void a(InterfaceC1664ev interfaceC1664ev) {
        this.f11243a.add(interfaceC1664ev);
    }

    public void a(double d2) {
        this.f11247e = d2;
    }

    public void b(double d2) {
        this.f11248f = d2;
    }

    public double a() {
        return this.f11246d == f11245c ? (this.f11248f - this.f11247e) / ((getHeight() - this.f11253k.top) - this.f11253k.bottom) : (this.f11248f - this.f11247e) / ((getWidth() - this.f11253k.left) - this.f11253k.right);
    }

    public void c(double d2) {
        if (d2 < this.f11247e) {
            d2 = this.f11247e;
        }
        if (d2 >= this.f11250h) {
            d(d2 + a());
        }
        this.f11249g = d2;
        repaint();
        e();
    }

    public void d(double d2) {
        if (d2 > this.f11248f) {
            d2 = this.f11248f;
        }
        if (d2 < this.f11249g) {
            c(d2 - a());
        }
        this.f11250h = d2;
        repaint();
        f();
    }

    private void e() {
        double d2 = this.f11249g;
        Iterator it = this.f11243a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1664ev) it.next()).a(d2);
        }
    }

    private void f() {
        double d2 = this.f11250h;
        Iterator it = this.f11243a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1664ev) it.next()).b(d2);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return this.f11246d == f11245c ? new Dimension(this.f11253k.left + this.f11252j + this.f11253k.right, this.f11253k.top + this.f11251i + this.f11253k.bottom + eJ.a(100)) : new Dimension(this.f11253k.left + this.f11251i + this.f11253k.right + eJ.a(100), this.f11253k.top + this.f11252j + this.f11253k.bottom);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}
