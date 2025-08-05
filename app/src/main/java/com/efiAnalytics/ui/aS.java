package com.efiAnalytics.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aS.class */
public abstract class aS extends JComponent {

    /* renamed from: a, reason: collision with root package name */
    private boolean f10776a = true;

    /* renamed from: b, reason: collision with root package name */
    private Color f10777b = Color.GRAY;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f10778c = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private String f10779i = "";

    /* renamed from: j, reason: collision with root package name */
    private String f10780j = "";

    /* renamed from: k, reason: collision with root package name */
    private String f10781k = "";

    /* renamed from: l, reason: collision with root package name */
    private Font f10782l = new Font("Arial Unicode MS", 0, eJ.a(20));

    /* renamed from: m, reason: collision with root package name */
    private Font f10783m = new Font("Arial Unicode MS", 0, eJ.a(11));

    /* renamed from: n, reason: collision with root package name */
    private double f10784n = 0.0d;

    /* renamed from: o, reason: collision with root package name */
    private double f10785o = 0.0d;

    /* renamed from: p, reason: collision with root package name */
    private boolean f10786p = true;

    /* renamed from: q, reason: collision with root package name */
    private Color f10787q = Color.lightGray;

    /* renamed from: r, reason: collision with root package name */
    private Color f10788r = Color.WHITE;

    /* renamed from: d, reason: collision with root package name */
    protected Rectangle f10789d = null;

    /* renamed from: s, reason: collision with root package name */
    private String f10790s = "";

    /* renamed from: t, reason: collision with root package name */
    private int f10791t = -1;

    /* renamed from: u, reason: collision with root package name */
    private int f10792u = -1;

    /* renamed from: v, reason: collision with root package name */
    private boolean f10793v = true;

    /* renamed from: e, reason: collision with root package name */
    Image f10794e = null;

    /* renamed from: f, reason: collision with root package name */
    boolean f10795f = true;

    /* renamed from: g, reason: collision with root package name */
    Stroke f10796g = new BasicStroke(1.0f);

    /* renamed from: w, reason: collision with root package name */
    private int f10797w = 5;

    /* renamed from: x, reason: collision with root package name */
    private int f10798x = 5;

    /* renamed from: h, reason: collision with root package name */
    int f10799h = eJ.a(25);

    public aS() {
        setBackground(Color.BLACK);
        setForeground(Color.lightGray);
        aT aTVar = new aT(this);
        addMouseListener(aTVar);
        addMouseMotionListener(aTVar);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f10794e == null || getWidth() != this.f10794e.getWidth(null) || getHeight() != this.f10794e.getHeight(null)) {
            this.f10794e = createImage(getWidth(), getHeight());
            this.f10795f = true;
        }
        if (this.f10795f) {
            Graphics graphics2 = this.f10794e.getGraphics();
            graphics2.clearRect(0, 0, this.f10794e.getWidth(null), this.f10794e.getHeight(null));
            c(graphics2);
            if (e()) {
                b(graphics2);
            }
            a(graphics2);
        }
        graphics.drawImage(this.f10794e, 0, 0, this);
        d(graphics);
    }

    protected void c(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        int iStringWidth = getFontMetrics(g()).stringWidth(f());
        graphics.setFont(g());
        graphics.setColor(getForeground());
        graphics.drawString(f(), (getWidth() - iStringWidth) / 2, g().getSize());
    }

    public void d(Graphics graphics) {
        if (!n() || p() < 0 || o() < 0) {
            return;
        }
        ((Graphics2D) graphics).setStroke(this.f10796g);
        Rectangle rectangleK = k();
        if (rectangleK.contains(p(), o())) {
            graphics.setColor(q());
            graphics.drawLine(p(), rectangleK.f12373y, p(), rectangleK.height + rectangleK.f12373y);
            graphics.drawLine(rectangleK.f12372x, o(), rectangleK.width + rectangleK.f12372x, o());
            graphics.drawString("X: " + a(p()), rectangleK.f12372x + this.f10799h, rectangleK.f12373y + graphics.getFont().getSize());
            String[] strArrSplit = b(o()).split("\n");
            int size = rectangleK.f12373y + (graphics.getFont().getSize() * 2);
            int i2 = rectangleK.f12372x + this.f10799h;
            graphics.drawString("Y:", i2, size);
            int iStringWidth = i2 + graphics.getFontMetrics(graphics.getFont()).stringWidth("Y: ");
            for (String str : strArrSplit) {
                graphics.drawString(str, iStringWidth, size);
                size += graphics.getFont().getSize();
            }
        }
    }

    public abstract void c();

    public abstract void a(Graphics graphics);

    public abstract void b(Graphics graphics);

    public abstract String a(int i2);

    public abstract String b(int i2);

    public void a(int i2, Color color) {
        if (this.f10778c.size() <= i2) {
            c(i2);
        }
        this.f10778c.set(i2, color);
        d();
    }

    public void d() {
        this.f10795f = true;
    }

    public Color c(int i2) {
        if (this.f10778c.size() <= i2) {
            for (int size = this.f10778c.size(); size <= i2; size++) {
                this.f10778c.add(f(size));
            }
        }
        return (Color) this.f10778c.get(i2);
    }

    private Color f(int i2) {
        switch (i2 % 4) {
            case 0:
                return Color.CYAN;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.RED;
            case 3:
                return Color.YELLOW;
            default:
                return Color.ORANGE;
        }
    }

    public boolean e() {
        return this.f10776a;
    }

    public String f() {
        return this.f10779i;
    }

    public void b(String str) {
        this.f10779i = str;
        d();
    }

    public Font g() {
        return this.f10782l;
    }

    public double h() {
        return this.f10784n;
    }

    public void c(double d2) {
        this.f10784n = d2;
        d();
    }

    public double i() {
        return this.f10785o;
    }

    public void d(double d2) {
        this.f10785o = d2;
        d();
    }

    public boolean j() {
        return this.f10786p;
    }

    public void a(boolean z2) {
        this.f10786p = z2;
        d();
    }

    public Rectangle k() {
        if (this.f10789d == null) {
            this.f10789d = new Rectangle(this.f10797w, this.f10782l.getSize() + 5, (getWidth() - 5) - this.f10797w, ((getHeight() - this.f10782l.getSize()) - 5) - this.f10798x);
        }
        return this.f10789d;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
        this.f10789d = null;
        d();
    }

    public Font l() {
        return this.f10783m;
    }

    public String m() {
        return this.f10790s;
    }

    public void c(String str) {
        this.f10790s = str;
        d();
    }

    public boolean n() {
        return this.f10793v;
    }

    public int o() {
        return this.f10792u;
    }

    public int p() {
        return this.f10791t;
    }

    public Color q() {
        return this.f10788r;
    }

    public String r() {
        return this.f10780j;
    }

    public void d(String str) {
        this.f10780j = str;
        d();
    }

    public String s() {
        return this.f10781k;
    }

    public void e(String str) {
        this.f10781k = str;
        d();
    }

    public void d(int i2) {
        this.f10797w = i2;
        this.f10789d = null;
    }

    public void e(int i2) {
        this.f10798x = i2;
        this.f10789d = null;
    }
}
