package ao;

import i.InterfaceC1741a;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:ao/gP.class */
public class gP extends JComponent {

    /* renamed from: a, reason: collision with root package name */
    Dimension f5936a = new Dimension(8, 14);

    /* renamed from: b, reason: collision with root package name */
    Point f5937b = new Point();

    /* renamed from: c, reason: collision with root package name */
    int f5938c = 0;

    /* renamed from: d, reason: collision with root package name */
    double f5939d = 0.0d;

    /* renamed from: e, reason: collision with root package name */
    int f5940e = 0;

    /* renamed from: f, reason: collision with root package name */
    int f5941f = 100;

    /* renamed from: g, reason: collision with root package name */
    Insets f5942g = new Insets(10, 7, 10, 7);

    /* renamed from: h, reason: collision with root package name */
    ArrayList f5943h = new ArrayList();

    public gP() {
        gQ gQVar = new gQ(this);
        addMouseListener(gQVar);
        addMouseMotionListener(gQVar);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(Color.lightGray);
        graphics.draw3DRect(0, 0, getWidth() - 1, getHeight() - 1, false);
        graphics.draw3DRect(getInsets().left, (getHeight() / 2) - 2, (getWidth() - getInsets().left) - getInsets().right, 3, false);
        graphics.setColor(Color.black);
        graphics.drawLine(getInsets().left + 1, (getHeight() / 2) - 1, (getWidth() - getInsets().left) - 2, (getHeight() / 2) - 1);
        graphics.setColor(Color.lightGray);
        this.f5937b.setLocation(c(), d());
        graphics.fill3DRect(this.f5937b.f12370x, this.f5937b.f12371y, this.f5936a.width, this.f5936a.height, true);
    }

    private int c() {
        return (((int) (((getWidth() - getInsets().left) - getInsets().right) * this.f5939d)) + getInsets().left) - (this.f5936a.width / 2);
    }

    private int d() {
        return (getHeight() / 2) - (this.f5936a.height / 2);
    }

    public void a(int i2) {
        this.f5940e = i2;
    }

    public void b(int i2) {
        this.f5941f = i2;
    }

    public int a() {
        return ((int) ((this.f5941f - this.f5940e) * b())) + this.f5940e;
    }

    public void c(int i2) {
        a((i2 - this.f5940e) / (this.f5941f - this.f5940e));
        repaint();
    }

    public void a(double d2) {
        this.f5939d = d2;
        this.f5938c = (int) ((this.f5941f - this.f5940e) * d2);
    }

    public double b() {
        return this.f5939d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(int i2) {
        if (i2 - getInsets().left < 0) {
            this.f5938c = 0;
        } else if (i2 >= getWidth() - getInsets().right) {
            this.f5938c = (getWidth() - getInsets().right) - getInsets().left;
        } else {
            this.f5938c = i2 - getInsets().left;
        }
        this.f5939d = this.f5938c / ((getWidth() - getInsets().right) - getInsets().left);
        e(a());
    }

    private void e(int i2) {
        Iterator it = this.f5943h.iterator();
        while (it.hasNext()) {
            ((InterfaceC1741a) it.next()).a(i2);
        }
    }

    public void a(InterfaceC1741a interfaceC1741a) {
        this.f5943h.add(interfaceC1741a);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(((getInsets().left + this.f5941f) - this.f5940e) + getInsets().right, getInsets().top + getInsets().bottom + 3);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(getInsets().left + 50 + getInsets().right, getInsets().top + getInsets().bottom + 3);
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public Insets getInsets() {
        return this.f5942g;
    }
}
