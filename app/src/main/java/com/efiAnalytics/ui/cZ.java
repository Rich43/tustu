package com.efiAnalytics.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cZ.class */
public class cZ extends aS {

    /* renamed from: k, reason: collision with root package name */
    private final ArrayList f11168k = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    private bH.aa f11169l = null;

    /* renamed from: m, reason: collision with root package name */
    private double f11170m = 0.0d;

    /* renamed from: n, reason: collision with root package name */
    private double f11171n = 0.0d;

    /* renamed from: o, reason: collision with root package name */
    private double f11172o = Double.NaN;

    /* renamed from: p, reason: collision with root package name */
    private double f11173p = Double.NaN;

    /* renamed from: q, reason: collision with root package name */
    private final List f11174q = new ArrayList();

    /* renamed from: r, reason: collision with root package name */
    private final List f11175r = new ArrayList();

    /* renamed from: s, reason: collision with root package name */
    private String f11176s = "";

    /* renamed from: t, reason: collision with root package name */
    private final List f11177t = new ArrayList();

    /* renamed from: u, reason: collision with root package name */
    private final List f11178u = new ArrayList();

    /* renamed from: v, reason: collision with root package name */
    private String f11179v = "";

    /* renamed from: w, reason: collision with root package name */
    private int f11180w = -1;

    /* renamed from: x, reason: collision with root package name */
    private int f11181x = -1;

    /* renamed from: a, reason: collision with root package name */
    int f11182a = eJ.a(4);

    /* renamed from: b, reason: collision with root package name */
    C1617da f11183b = new C1617da(this);

    /* renamed from: i, reason: collision with root package name */
    Stroke f11184i = new BasicStroke(2.0f);

    /* renamed from: y, reason: collision with root package name */
    private boolean f11185y = true;

    /* renamed from: j, reason: collision with root package name */
    C1618db f11186j = new C1618db(this);

    /* renamed from: z, reason: collision with root package name */
    private double f11187z = Double.NaN;

    /* renamed from: A, reason: collision with root package name */
    private String f11188A = "";

    /* renamed from: B, reason: collision with root package name */
    private boolean f11189B = true;

    public cZ() {
        d(25);
        a(false);
    }

    public ArrayList f(int i2) {
        while (this.f11168k.size() <= i2) {
            this.f11168k.add(new ArrayList());
        }
        return (ArrayList) this.f11168k.get(i2);
    }

    @Override // com.efiAnalytics.ui.aS
    public void a(Graphics graphics) {
        Rectangle rectangleK = k();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.clipRect(rectangleK.f12372x, rectangleK.f12373y - 1, rectangleK.width, rectangleK.height + 2);
        graphics2D.setStroke(this.f11184i);
        for (int i2 = 0; i2 < this.f11168k.size(); i2++) {
            ArrayList arrayList = (ArrayList) this.f11168k.get(i2);
            int[] iArr = new int[arrayList.size()];
            int[] iArr2 = new int[arrayList.size()];
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                double x2 = ((Point2D) arrayList.get(i3)).getX();
                double y2 = 1.0d - ((((Point2D) arrayList.get(i3)).getY() - g(i2)) / (h(i2) - g(i2)));
                iArr[i3] = rectangleK.f12372x + ((int) Math.round(rectangleK.width * ((x2 - t()) / (b() - t()))));
                iArr2[i3] = rectangleK.f12373y + ((int) Math.round(rectangleK.height * y2));
            }
            if (i2 == 0) {
                a(graphics, rectangleK, iArr, iArr2);
            }
            graphics.setColor(c(i2));
            if (this.f11185y) {
                graphics.drawPolyline(iArr, iArr2, iArr.length);
            } else {
                for (int i4 = 0; i4 < iArr.length; i4++) {
                    graphics2D.fillOval(iArr[i4] - 2, iArr2[i4] - 2, 4, 4);
                }
            }
        }
        a(graphics, rectangleK);
    }

    private void a(Graphics graphics, Rectangle rectangle, int[] iArr, int[] iArr2) {
        double y2 = 0.0d;
        if (w() <= 0 || x() <= w()) {
            return;
        }
        int i2 = -1;
        int i3 = -1;
        double d2 = Double.NaN;
        double d3 = Double.NaN;
        double d4 = 0.0d;
        double x2 = Double.NaN;
        double y3 = Double.NaN;
        int length = iArr.length - 1;
        while (true) {
            if (length <= 1) {
                break;
            }
            if (iArr[length] > w() && i3 < 0 && iArr[length] <= x()) {
                i3 = length;
            }
            if (iArr[length] < w()) {
                i2 = length + 1;
                break;
            }
            if (i3 > 0) {
                double y4 = ((Point2D) ((ArrayList) this.f11168k.get(0)).get(length)).getY();
                if (Double.isNaN(d2) || d2 > y4) {
                    d2 = y4;
                }
                if (Double.isNaN(d3) || d3 < y4) {
                    d3 = y4;
                }
                d4 += y4;
                if (!Double.isNaN(x2)) {
                    y2 += ((y3 + ((Point2D) ((ArrayList) this.f11168k.get(0)).get(length)).getY()) / 2.0d) * (x2 - ((Point2D) ((ArrayList) this.f11168k.get(0)).get(length)).getX());
                }
                x2 = ((Point2D) ((ArrayList) this.f11168k.get(0)).get(length)).getX();
                y3 = ((Point2D) ((ArrayList) this.f11168k.get(0)).get(length)).getY();
            }
            length--;
        }
        if (i3 > i2 && i2 >= 0) {
            int[] iArr3 = new int[(i3 - i2) + 3];
            int[] iArr4 = new int[iArr3.length];
            iArr3[0] = iArr[i2];
            iArr4[0] = rectangle.f12373y + rectangle.height;
            System.arraycopy(iArr, i2, iArr3, 1, iArr3.length - 2);
            System.arraycopy(iArr2, i2, iArr4, 1, iArr4.length - 2);
            iArr3[iArr3.length - 1] = iArr3[iArr3.length - 2];
            iArr4[iArr4.length - 1] = rectangle.f12373y + rectangle.height;
            Color colorC = c(0);
            graphics.setColor(new Color(colorC.getRed(), colorC.getGreen(), colorC.getBlue(), 92));
            graphics.fillPolygon(iArr3, iArr4, iArr3.length);
            graphics.setColor(colorC);
        }
        this.f11186j.a((i3 - i2) + 1);
        if (this.f11186j.b() > 0) {
            this.f11186j.c(d3);
            this.f11186j.b(d2);
            this.f11186j.d(d4 / this.f11186j.b());
            this.f11186j.a(y2);
            this.f11183b.a(graphics, this.f11186j);
        }
    }

    private void a(Graphics graphics, Rectangle rectangle) {
        if (w() > 0) {
            graphics.setColor(Color.lightGray);
            graphics.fill3DRect(w() - (this.f11182a / 2), rectangle.f12373y, this.f11182a, rectangle.height, true);
            graphics.setColor(q());
            graphics.setFont(getFont());
            String strA = a(w());
            int iStringWidth = graphics.getFontMetrics(graphics.getFont()).stringWidth(strA);
            graphics.drawString(strA, w() < (this.f10789d.f12372x + iStringWidth) + this.f11182a ? w() + this.f11182a : (w() - this.f11182a) - iStringWidth, this.f10789d.f12373y + (this.f10789d.height / 2));
        }
        if (x() > 0) {
            graphics.setColor(Color.lightGray);
            int iX = x() - (this.f11182a / 2);
            graphics.fill3DRect(iX, rectangle.f12373y, this.f11182a, rectangle.height, true);
            graphics.setColor(q());
            graphics.setFont(getFont());
            String strA2 = a(x());
            int iStringWidth2 = graphics.getFontMetrics(graphics.getFont()).stringWidth(strA2);
            graphics.drawString(strA2, getWidth() > (iX + iStringWidth2) + this.f11182a ? x() + this.f11182a : (x() - this.f11182a) - iStringWidth2, this.f10789d.f12373y + (this.f10789d.height / 2));
        }
        if (Double.isNaN(this.f11187z)) {
            return;
        }
        int iRound = rectangle.f12372x + ((int) Math.round(rectangle.width * ((this.f11187z - t()) / (b() - t()))));
        graphics.setColor(Color.GREEN);
        graphics.drawLine(iRound, this.f10789d.f12373y, iRound, this.f10789d.f12373y + this.f10789d.height);
        if (this.f11188A == null || this.f11188A.isEmpty()) {
            return;
        }
        graphics.setFont(l());
        graphics.drawString(this.f11188A, iRound + 5, this.f10789d.f12373y + graphics.getFont().getSize());
    }

    private String A() {
        String str = (this.f11176s == null && this.f11176s.isEmpty()) ? "" : this.f11176s + " vs ";
        for (int i2 = 0; i2 < this.f11177t.size(); i2++) {
            String str2 = (String) this.f11177t.get(i2);
            if (!str.isEmpty() && !str2.isEmpty()) {
                str = str + ", ";
            }
            str = str + str2;
        }
        return str;
    }

    private boolean B() {
        Iterator it = this.f11177t.iterator();
        while (it.hasNext()) {
            if (!((String) it.next()).equals("")) {
                return false;
            }
        }
        return true;
    }

    @Override // com.efiAnalytics.ui.aS
    protected void c(Graphics graphics) {
        Font font;
        if (!this.f11189B || this.f11168k.isEmpty() || B()) {
            super.c(graphics);
            return;
        }
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        String strA = A();
        if (strA.isEmpty()) {
            strA = f();
        }
        Rectangle rectangleK = k();
        Font fontG = g();
        while (true) {
            font = fontG;
            if (getFontMetrics(font).stringWidth(strA) <= rectangleK.width || font.getSize() <= 1) {
                break;
            } else {
                fontG = new Font(font.getFamily(), font.getStyle(), font.getSize() - 1);
            }
        }
        int iStringWidth = k().f12372x + ((k().width - getFontMetrics(font).stringWidth(strA)) / 2);
        graphics.setFont(font);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean z2 = false;
        if (C()) {
            String str = this.f11176s + " vs ";
            graphics.setColor(getForeground());
            graphics.drawString(str, iStringWidth, font.getSize());
            int iStringWidth2 = iStringWidth + getFontMetrics(font).stringWidth(str);
            for (int i2 = 0; i2 < this.f11168k.size(); i2++) {
                String str2 = (String) this.f11177t.get(i2);
                if (!str2.isEmpty() && z2) {
                    graphics.setColor(getForeground());
                    graphics.drawString(", ", iStringWidth2, font.getSize());
                    iStringWidth2 += getFontMetrics(font).stringWidth(", ");
                }
                if (!str2.isEmpty()) {
                    graphics.setColor(c(i2));
                    graphics.drawString(str2, iStringWidth2, font.getSize());
                    iStringWidth2 += getFontMetrics(font).stringWidth(str2);
                    z2 = true;
                }
            }
        }
    }

    private boolean C() {
        for (String str : this.f11177t) {
            if (str != null && !str.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.efiAnalytics.ui.aS
    public void b(Graphics graphics) {
        graphics.setColor(getForeground());
        int size = l().getSize();
        boolean zD = D();
        String[][] strArr = new String[this.f11168k.size()][4];
        int i2 = 0;
        for (int i3 = 0; i3 < this.f11168k.size(); i3++) {
            for (int i4 = 1; i4 < 4; i4++) {
                double dI = zD ? (((i() - h()) / 4) * (4 - i4)) + h() : (((h(i3) - g(i3)) / 4) * (4 - i4)) + g(i3);
                if (Double.isNaN(dI)) {
                    strArr[i3][i4] = "";
                } else {
                    strArr[i3][i4] = bH.W.a(dI);
                    int iStringWidth = getFontMetrics(l()).stringWidth(strArr[i3][i4] + "0");
                    if (iStringWidth > i2) {
                        i2 = iStringWidth;
                    }
                }
            }
        }
        d(i2 + 4);
        e(l().getSize() + 4);
        Rectangle rectangleK = k();
        graphics.drawRect(rectangleK.f12372x, rectangleK.f12373y - 1, rectangleK.width, rectangleK.height + 2);
        graphics.setFont(l());
        for (int i5 = 0; i5 < this.f11168k.size(); i5++) {
            if (zD) {
                if (i5 == 0) {
                    String strA = bH.W.a(i());
                    int iStringWidth2 = getFontMetrics(l()).stringWidth(strA);
                    graphics.setColor(c(i5));
                    graphics.drawString(strA, (rectangleK.f12372x - iStringWidth2) - 2, rectangleK.f12373y + (size * i5));
                }
            } else if (!Double.isNaN(h(i5))) {
                String strA2 = bH.W.a(h(i5));
                int iStringWidth3 = getFontMetrics(l()).stringWidth(strA2);
                graphics.setColor(c(i5));
                graphics.drawString(strA2, (rectangleK.f12372x - iStringWidth3) - 2, rectangleK.f12373y + (size * i5));
            }
            if (zD) {
                if (i5 == 0) {
                    String strA3 = bH.W.a(h());
                    int iStringWidth4 = getFontMetrics(l()).stringWidth(strA3);
                    graphics.setColor(c(i5));
                    graphics.drawString(strA3, (rectangleK.f12372x - iStringWidth4) - 2, (rectangleK.f12373y + rectangleK.height) - (size * i5));
                }
            } else if (!Double.isNaN(g(i5))) {
                String strA4 = bH.W.a(g(i5));
                int iStringWidth5 = getFontMetrics(l()).stringWidth(strA4);
                graphics.setColor(c(i5));
                graphics.drawString(strA4, (rectangleK.f12372x - iStringWidth5) - 2, (rectangleK.f12373y + rectangleK.height) - (size * i5));
            }
            if (!zD || i5 == 0) {
                for (int i6 = 1; i6 < 4; i6++) {
                    int i7 = rectangleK.f12373y + (i6 * (rectangleK.height / 4)) + (i5 * size);
                    if (i6 == 0) {
                        graphics.setColor(getForeground());
                        graphics.drawLine(rectangleK.f12372x, i7, rectangleK.f12372x + rectangleK.width, i7);
                    }
                    String str = strArr[i5][i6];
                    int iStringWidth6 = getFontMetrics(l()).stringWidth(str);
                    graphics.setColor(c(i5));
                    graphics.drawString(str, (rectangleK.f12372x - iStringWidth6) - 2, i7 - 1);
                }
            }
        }
        graphics.drawString(bH.W.a(t()), rectangleK.f12372x, rectangleK.f12373y + rectangleK.height + size + 1);
        String strA5 = bH.W.a(this.f11170m);
        graphics.drawString(strA5, (rectangleK.f12372x + rectangleK.width) - getFontMetrics(l()).stringWidth(strA5), rectangleK.f12373y + rectangleK.height + size + 1);
    }

    private boolean D() {
        boolean z2;
        if (Double.isNaN(this.f11172o) || Double.isNaN(this.f11173p)) {
            double dH = h(0);
            double dG = g(0);
            for (int i2 = 1; i2 < this.f11168k.size(); i2++) {
                double dH2 = Double.isNaN(h(i2)) ? dH : h(i2);
                double dG2 = Double.isNaN(g(i2)) ? dG : g(i2);
                if (!Double.isNaN(dG) && dG2 != dG) {
                    return false;
                }
                if (!Double.isNaN(dH) && dH2 != dH) {
                    return false;
                }
                if (!Double.isNaN(dH2)) {
                    dH = dH2;
                }
                if (!Double.isNaN(dG2)) {
                    dG = dG2;
                }
            }
            z2 = true;
        } else {
            z2 = true;
        }
        return z2;
    }

    @Override // com.efiAnalytics.ui.aS
    public void d(double d2) {
        a(0, d2);
    }

    public void a(int i2, double d2) {
        while (this.f11174q.size() <= i2) {
            this.f11174q.add(Double.valueOf(Double.NaN));
        }
        this.f11174q.set(i2, Double.valueOf(d2));
        if (i2 == 0) {
            super.d(d2);
        }
    }

    @Override // com.efiAnalytics.ui.aS
    public void c(double d2) {
        b(0, d2);
    }

    public void b(int i2, double d2) {
        while (this.f11175r.size() <= i2) {
            this.f11175r.add(Double.valueOf(Double.NaN));
        }
        this.f11175r.set(i2, Double.valueOf(d2));
        if (i2 == 0) {
            super.c(d2);
        }
    }

    public double g(int i2) {
        if (!Double.isNaN(this.f11173p)) {
            return this.f11173p;
        }
        while (i2 >= this.f11175r.size()) {
            this.f11175r.add(Double.valueOf(0.0d));
        }
        return ((Double) this.f11175r.get(i2)).doubleValue();
    }

    @Override // com.efiAnalytics.ui.aS
    public double h() {
        if (!Double.isNaN(this.f11173p)) {
            return this.f11173p;
        }
        double d2 = Double.MAX_VALUE;
        Iterator it = this.f11175r.iterator();
        while (it.hasNext()) {
            double dDoubleValue = ((Double) it.next()).doubleValue();
            if (dDoubleValue < d2) {
                d2 = dDoubleValue;
            }
        }
        return d2;
    }

    public double h(int i2) {
        if (!Double.isNaN(this.f11172o)) {
            return this.f11172o;
        }
        while (i2 >= this.f11174q.size()) {
            this.f11174q.add(Double.valueOf(0.0d));
        }
        return ((Double) this.f11174q.get(i2)).doubleValue();
    }

    @Override // com.efiAnalytics.ui.aS
    public double i() {
        if (!Double.isNaN(this.f11172o)) {
            return this.f11172o;
        }
        double d2 = 0.0d;
        Iterator it = this.f11174q.iterator();
        while (it.hasNext()) {
            double dDoubleValue = ((Double) it.next()).doubleValue();
            if (dDoubleValue > d2) {
                d2 = dDoubleValue;
            }
        }
        return d2;
    }

    private void b(Point2D point2D, int i2) {
        if (!j() || point2D.getY() <= h(i2) - (h(i2) * 0.06d)) {
            return;
        }
        a(i2, point2D.getY() + (point2D.getY() * 0.06d));
    }

    public void a(Point2D point2D, int i2) {
        f(i2).add(point2D);
        b(point2D, i2);
        d();
    }

    public void a() {
        c();
    }

    @Override // com.efiAnalytics.ui.aS
    public String a(int i2) {
        Rectangle rectangleK = k();
        int i3 = i2 - rectangleK.f12372x;
        if (i3 < 0 || i3 > rectangleK.width) {
            return "";
        }
        double dT = t() + ((b() - t()) * (i3 / rectangleK.width));
        return this.f11176s.length() > 0 ? this.f11176s + ": " + bH.W.a(dT) : bH.W.a(dT);
    }

    @Override // com.efiAnalytics.ui.aS
    public String b(int i2) {
        String strI;
        double dA;
        Rectangle rectangleK = k();
        if (i2 < rectangleK.f12373y) {
            return bH.W.a(h());
        }
        if (i2 > rectangleK.f12373y + rectangleK.height) {
            return bH.W.a(i());
        }
        String str = "";
        boolean zD = D();
        int size = zD ? 1 : this.f11168k.size();
        for (int i3 = 0; i3 < size; i3++) {
            if (zD) {
                strI = "Value";
                dA = m(i2);
            } else {
                strI = i(i3);
                dA = a(i2, i3);
            }
            if (strI.length() > 0) {
                if (!str.isEmpty()) {
                    str = str + "\n";
                }
                str = str + strI + ": " + bH.W.a(dA);
            }
        }
        return str;
    }

    private double a(int i2, int i3) {
        Rectangle rectangleK = k();
        return g(i3) + ((h(i3) - g(i3)) * (1.0d - ((i2 - rectangleK.f12373y) / rectangleK.height)));
    }

    private double m(int i2) {
        Rectangle rectangleK = k();
        return h() + ((i() - h()) * (1.0d - ((i2 - rectangleK.f12373y) / rectangleK.height)));
    }

    @Override // com.efiAnalytics.ui.aS
    public void c() {
        this.f11168k.clear();
        this.f11183b.a();
        this.f11187z = Double.NaN;
        d();
    }

    public double b() {
        return this.f11170m;
    }

    public void a(double d2) {
        this.f11170m = d2;
    }

    public double t() {
        return this.f11171n;
    }

    public void b(double d2) {
        this.f11171n = d2;
    }

    public void a(String str) {
        this.f11176s = str;
    }

    public String i(int i2) {
        return this.f11177t.size() > i2 ? (String) this.f11177t.get(i2) : "";
    }

    public String j(int i2) {
        while (this.f11178u.size() < i2 + 1) {
            this.f11178u.add("");
        }
        return (String) this.f11178u.get(i2);
    }

    public String a(int i2, String str) {
        while (this.f11178u.size() < i2 + 1) {
            this.f11178u.add("");
        }
        return (String) this.f11178u.set(i2, str);
    }

    public void b(int i2, String str) {
        while (this.f11177t.size() <= i2) {
            this.f11177t.add("");
        }
        this.f11177t.set(i2, str);
    }

    public void b(boolean z2) {
        this.f11185y = z2;
    }

    public void u() {
        this.f11180w = -1;
        this.f11181x = -1;
    }

    public boolean v() {
        return this.f11180w >= 0;
    }

    public int w() {
        return this.f11180w;
    }

    public void k(int i2) {
        if (this.f11181x == -1 || this.f11181x > i2) {
            this.f11180w = i2;
        }
    }

    public int x() {
        return this.f11181x;
    }

    public void l(int i2) {
        this.f11181x = i2;
    }

    public String y() {
        return this.f11179v;
    }

    public void f(String str) {
        this.f11179v = str;
    }

    public void a(bH.aa aaVar) {
        this.f11169l = aaVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String h(String str) {
        return this.f11169l != null ? this.f11169l.a(str) : str;
    }

    public void e(double d2) {
        this.f11187z = d2;
    }

    public void g(String str) {
        this.f11188A = str;
    }

    public void c(boolean z2) {
        this.f11189B = z2;
    }

    public void z() {
        for (int i2 = 0; i2 < this.f11174q.size(); i2++) {
            this.f11174q.set(i2, Double.valueOf(0.0d));
        }
        for (int i3 = 0; i3 < this.f11175r.size(); i3++) {
            this.f11175r.set(i3, Double.valueOf(0.0d));
        }
    }
}
