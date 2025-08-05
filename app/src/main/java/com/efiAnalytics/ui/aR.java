package com.efiAnalytics.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aR.class */
public class aR extends JComponent {

    /* renamed from: a, reason: collision with root package name */
    int f10759a = eJ.a(3);

    /* renamed from: b, reason: collision with root package name */
    Stroke f10760b = new BasicStroke(this.f10759a);

    /* renamed from: h, reason: collision with root package name */
    private double f10761h = Double.NaN;

    /* renamed from: i, reason: collision with root package name */
    private double f10762i = Double.NaN;

    /* renamed from: j, reason: collision with root package name */
    private double f10763j = Double.NaN;

    /* renamed from: k, reason: collision with root package name */
    private double f10764k = Double.NaN;

    /* renamed from: l, reason: collision with root package name */
    private double f10765l = Double.NaN;

    /* renamed from: m, reason: collision with root package name */
    private double f10766m = Double.NaN;

    /* renamed from: n, reason: collision with root package name */
    private double f10767n = Double.NaN;

    /* renamed from: o, reason: collision with root package name */
    private double f10768o = Double.NaN;

    /* renamed from: p, reason: collision with root package name */
    private Color f10769p = Color.LIGHT_GRAY;

    /* renamed from: c, reason: collision with root package name */
    Color f10770c = Color.RED;

    /* renamed from: d, reason: collision with root package name */
    Color f10771d = new Color(0, 0, 255, 220);

    /* renamed from: e, reason: collision with root package name */
    Color f10772e = Color.CYAN;

    /* renamed from: q, reason: collision with root package name */
    private String f10773q = null;

    /* renamed from: f, reason: collision with root package name */
    double f10774f = 0.1d;

    /* renamed from: g, reason: collision with root package name */
    Font f10775g = new Font(Font.DIALOG, 0, 12);

    public aR() {
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        int iRound;
        int iRound2;
        Graphics2D graphics2D = (Graphics2D) graphics;
        Insets insets = getInsets();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(this.f10760b);
        Stroke stroke = graphics2D.getStroke();
        int width = (getWidth() - insets.left) - insets.right;
        int height = (getHeight() - insets.top) - insets.bottom;
        int i2 = (width * 2) / 3;
        int i3 = (height * 2) / 3;
        boolean zB = b();
        if (zB) {
            graphics.setColor(this.f10770c);
        } else {
            graphics.setColor(this.f10769p);
        }
        graphics.fillRect(insets.left, insets.top, width, height);
        if (this.f10773q != null && !this.f10773q.isEmpty()) {
            int iStringWidth = getFontMetrics(this.f10775g).stringWidth(this.f10773q);
            graphics.setFont(this.f10775g);
            graphics.setColor(Color.BLACK);
            graphics.drawString(this.f10773q, (getWidth() - iStringWidth) / 2, this.f10775g.getSize());
        }
        if (!Double.isNaN(this.f10765l) && !Double.isNaN(this.f10766m)) {
            if (zB) {
                graphics.setColor(this.f10772e);
            } else {
                graphics.setColor(this.f10771d);
            }
            if (this.f10765l < this.f10767n) {
                iRound = (int) (insets.left + Math.round(0.5d * ((this.f10765l - this.f10761h) / (this.f10767n - this.f10761h)) * width));
            } else {
                iRound = (int) (insets.left + Math.round((0.5d + (0.5d * ((this.f10765l - this.f10767n) / (this.f10762i - this.f10767n)))) * width));
            }
            if (this.f10766m < this.f10768o) {
                iRound2 = (int) (insets.top + Math.round((1.0d - (0.5d * ((this.f10766m - this.f10763j) / (this.f10768o - this.f10763j)))) * height));
            } else {
                iRound2 = (int) (insets.top + Math.round((0.5d - (0.5d * ((this.f10766m - this.f10768o) / (this.f10764k - this.f10768o)))) * height));
            }
            graphics.drawOval(iRound - (i2 / 2), iRound2 - (i3 / 2), i2, i3);
            graphics.drawLine(insets.left, iRound2, iRound - (i2 / 2), iRound2);
            graphics.drawLine(iRound + (i2 / 2) + 1, iRound2, getWidth() - insets.right, iRound2);
            if (iRound2 - (i3 / 2) > insets.top) {
                graphics.drawLine(iRound, insets.top, iRound, iRound2 - (i3 / 2));
            }
            if (iRound2 + (i3 / 2) < getHeight() - insets.bottom) {
                graphics.drawLine(iRound, iRound2 + (i3 / 2) + 1, iRound, getHeight() - insets.bottom);
            }
        }
        graphics2D.setStroke(stroke);
        super.paint(graphics);
    }

    private boolean b() {
        return Math.abs(0.5d - ((this.f10765l > this.f10767n ? 1 : (this.f10765l == this.f10767n ? 0 : -1)) < 0 ? 0.5d * ((this.f10765l - this.f10761h) / (this.f10767n - this.f10761h)) : 0.5d + ((this.f10765l - this.f10767n) / (this.f10762i - this.f10767n)))) < this.f10774f && Math.abs(0.5d - ((this.f10766m > this.f10768o ? 1 : (this.f10766m == this.f10768o ? 0 : -1)) < 0 ? 1.0d - (0.5d * ((this.f10766m - this.f10763j) / (this.f10768o - this.f10763j))) : 0.5d - (0.5d * ((this.f10766m - this.f10768o) / (this.f10764k - this.f10768o))))) < this.f10774f;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        this.f10775g = new Font(Font.DIALOG, 0, (i5 * 4) / 5);
        super.setBounds(i2, i3, i4, i5);
    }

    public void a(double d2) {
        this.f10761h = d2;
    }

    public void b(double d2) {
        this.f10762i = d2;
    }

    public void c(double d2) {
        this.f10763j = d2;
    }

    public void d(double d2) {
        this.f10764k = d2;
    }

    public void e(double d2) {
        this.f10765l = d2;
    }

    public void f(double d2) {
        this.f10766m = d2;
    }

    public void g(double d2) {
        this.f10767n = d2;
    }

    public void h(double d2) {
        this.f10768o = d2;
    }

    public void a(Color color) {
        this.f10769p = color;
    }

    public void a(String str) {
        this.f10773q = str;
    }

    public void a() {
        this.f10761h = Double.NaN;
        this.f10762i = Double.NaN;
        this.f10763j = Double.NaN;
        this.f10764k = Double.NaN;
        this.f10765l = Double.NaN;
        this.f10766m = Double.NaN;
        this.f10767n = Double.NaN;
        this.f10768o = Double.NaN;
        this.f10773q = null;
        this.f10769p = Color.LIGHT_GRAY;
        setBackground(this.f10769p);
        repaint();
    }
}
