package com.efiAnalytics.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aU.class */
public class aU extends aS {

    /* renamed from: a, reason: collision with root package name */
    aV f10801a = new aV(this);

    /* renamed from: l, reason: collision with root package name */
    private double f10802l = 0.0d;

    /* renamed from: m, reason: collision with root package name */
    private double f10803m = 0.0d;

    /* renamed from: n, reason: collision with root package name */
    private int f10804n = 4;

    /* renamed from: o, reason: collision with root package name */
    private int f10805o = -1;

    /* renamed from: b, reason: collision with root package name */
    Stroke f10806b = new BasicStroke(2.0f, 1, 1);

    /* renamed from: i, reason: collision with root package name */
    ArrayList f10807i = new ArrayList();

    /* renamed from: p, reason: collision with root package name */
    private Color f10808p = Color.darkGray;

    /* renamed from: q, reason: collision with root package name */
    private Color f10809q = Color.YELLOW;

    /* renamed from: r, reason: collision with root package name */
    private boolean f10810r = true;

    /* renamed from: s, reason: collision with root package name */
    private Color f10811s = null;

    /* renamed from: j, reason: collision with root package name */
    aX[] f10812j = null;

    /* renamed from: k, reason: collision with root package name */
    double f10813k = 0.0d;

    public aU() {
        addMouseListener(new aW(this));
    }

    @Override // com.efiAnalytics.ui.aS
    public void a(Graphics graphics) {
        ((Graphics2D) graphics).setStroke(this.f10806b);
        for (int i2 = 0; i2 < this.f10801a.a(); i2++) {
            graphics.setColor(c(i2));
            ArrayList arrayListA = this.f10801a.a(i2);
            int[] iArrA = a(arrayListA);
            graphics.drawPolyline(iArrA, b(arrayListA), iArrA.length);
        }
        e(graphics);
    }

    private void e(Graphics graphics) {
        if (this.f10811s == null) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (this.f10812j != null) {
            Rectangle rectangleK = k();
            graphics2D.setColor(this.f10809q);
            for (aX aXVar : this.f10812j) {
                if (aXVar.b() > 0.0d) {
                    int iF = f(aXVar.a());
                    graphics.drawLine(iF, rectangleK.f12373y, iF, rectangleK.f12373y + rectangleK.height);
                    graphics.drawString("Gap: " + bH.W.b(aXVar.a(), 3) + " to " + bH.W.b(aXVar.c(), 3), iF + graphics.getFont().getSize(), rectangleK.f12373y + (graphics.getFont().getSize() * 3));
                }
            }
        }
        graphics2D.setStroke(this.f10806b);
        graphics.setColor(this.f10811s);
        for (int i2 = 0; i2 < this.f10801a.a(); i2++) {
            ArrayList arrayListA = this.f10801a.a(i2);
            int[] iArrA = a(arrayListA);
            int[] iArrB = b(arrayListA);
            for (int i3 = 0; i3 < iArrA.length; i3++) {
                if (!((aZ) arrayListA.get(i3)).a()) {
                    graphics.drawLine(iArrA[i3], iArrB[i3] - 1, iArrA[i3], iArrB[i3] + 1);
                }
            }
        }
    }

    private int[] a(ArrayList arrayList) {
        int[] iArr = new int[arrayList.size()];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = f(((aZ) arrayList.get(i2)).getX());
        }
        return iArr;
    }

    private int[] b(ArrayList arrayList) {
        int[] iArr = new int[arrayList.size()];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = g(((aZ) arrayList.get(i2)).getY());
        }
        return iArr;
    }

    @Override // com.efiAnalytics.ui.aS
    public void b(Graphics graphics) {
        graphics.setColor(x());
        Rectangle rectangleK = k();
        graphics.drawRect(rectangleK.f12372x, rectangleK.f12373y, rectangleK.width - 1, rectangleK.height - 1);
        int iU = u();
        graphics.setFont(l());
        for (int i2 = 1; i2 < iU; i2++) {
            int i3 = rectangleK.f12373y + (i2 * (rectangleK.height / iU));
            graphics.drawLine(rectangleK.f12372x, i3, rectangleK.width, i3);
            graphics.drawString(bH.W.a((((i() - h()) / iU) * (iU - i2)) + h()) + " " + m(), rectangleK.f12372x + 2, i3 - 1);
        }
        if (w()) {
            int iV = v();
            if (iV <= 0) {
                iV = (rectangleK.width * iU) / rectangleK.height;
            }
            for (int i4 = 1; i4 < iV; i4++) {
                int i5 = i4 * (rectangleK.width / iV);
                graphics.drawLine(i5, rectangleK.f12373y, i5, rectangleK.f12373y + rectangleK.height);
            }
        }
    }

    public void a(int i2, aZ aZVar) {
        this.f10801a.a(i2, aZVar);
        d();
    }

    public void a(int i2, int i3, aZ aZVar) {
        this.f10801a.a(i2, i3, aZVar);
        d();
    }

    private int f(double d2) {
        Rectangle rectangleK = k();
        return (int) Math.round(((((d2 - a(d2)) - this.f10802l) / ((this.f10803m - this.f10802l) - this.f10813k)) * rectangleK.width) + rectangleK.f12372x);
    }

    public double a(double d2) {
        if (this.f10812j == null) {
            double d3 = 0.0d;
            int i2 = 0;
            double d4 = Double.NaN;
            for (int i3 = 0; i3 < this.f10801a.a(0).size(); i3++) {
                aZ aZVar = (aZ) this.f10801a.a(0).get(i3);
                if (aZVar.f12394x > 0.0d && Double.isNaN(d4)) {
                    d4 = aZVar.f12394x;
                } else if (aZVar.f12394x - d4 > 0.0d) {
                    d3 += aZVar.f12394x - d4;
                    d4 = aZVar.f12394x;
                    i2++;
                }
            }
            if (i2 <= 0) {
                this.f10812j = new aX[0];
                return 0.0d;
            }
            double d5 = d3 / i2;
            ArrayList arrayList = new ArrayList();
            double d6 = Double.NaN;
            double d7 = d5 * 10.0d;
            Iterator it = this.f10801a.a(0).iterator();
            while (it.hasNext()) {
                aZ aZVar2 = (aZ) it.next();
                if (Double.isNaN(d6) || d6 == aZVar2.f12394x) {
                    d6 = aZVar2.f12394x;
                } else if (aZVar2.f12394x - d6 > d7) {
                    arrayList.add(new aX(this, d6, aZVar2.f12394x));
                    d6 = aZVar2.f12394x;
                } else {
                    d6 = aZVar2.f12394x;
                }
            }
            this.f10812j = (aX[]) arrayList.toArray(new aX[arrayList.size()]);
            if (this.f10813k == 0.0d) {
                for (aX aXVar : this.f10812j) {
                    this.f10813k += aXVar.b();
                }
            }
        }
        double dB = 0.0d;
        for (aX aXVar2 : this.f10812j) {
            if (aXVar2.a() >= d2) {
                break;
            }
            dB += aXVar2.b();
        }
        return dB;
    }

    private int g(double d2) {
        Rectangle rectangleK = k();
        return (rectangleK.height + rectangleK.f12373y) - ((int) Math.round(((d2 - h()) / (i() - h())) * rectangleK.height));
    }

    public void a() {
        this.f10801a.b();
        this.f10812j = null;
        this.f10813k = 0.0d;
        d();
    }

    public double b() {
        return this.f10802l;
    }

    public void b(double d2) {
        this.f10802l = d2;
        d();
    }

    public double t() {
        return this.f10803m;
    }

    public void e(double d2) {
        this.f10803m = d2;
        d();
    }

    public int u() {
        return this.f10804n;
    }

    public void f(int i2) {
        this.f10804n = i2;
        d();
    }

    public int v() {
        return this.f10805o;
    }

    @Override // com.efiAnalytics.ui.aS
    public String a(int i2) {
        return bH.W.a(g(i2)) + " " + r();
    }

    public double g(int i2) {
        Rectangle rectangleK = k();
        if (i2 < rectangleK.f12372x) {
            return this.f10802l;
        }
        if (i2 > rectangleK.f12372x + rectangleK.width) {
            return this.f10803m;
        }
        double dA = this.f10802l + (((this.f10803m - this.f10802l) - a(this.f10803m)) * ((i2 - rectangleK.f12372x) / rectangleK.width));
        for (aX aXVar : this.f10812j) {
            if (dA <= aXVar.a()) {
                break;
            }
            dA += aXVar.b();
        }
        return dA;
    }

    @Override // com.efiAnalytics.ui.aS
    public String b(int i2) {
        return bH.W.a(h(i2)) + " " + s();
    }

    public double h(int i2) {
        Rectangle rectangleK = k();
        if (i2 < rectangleK.f12373y) {
            return h();
        }
        if (i2 > rectangleK.f12373y + rectangleK.height) {
            return i();
        }
        return h() + ((i() - h()) * (1.0d - ((i2 - rectangleK.f12373y) / rectangleK.height)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(double d2, double d3) {
        Iterator it = this.f10807i.iterator();
        while (it.hasNext()) {
            ((aY) it.next()).a(d2, d3);
        }
    }

    public void a(aY aYVar) {
        this.f10807i.add(aYVar);
    }

    public boolean w() {
        return this.f10810r;
    }

    public Color x() {
        return this.f10808p;
    }

    @Override // com.efiAnalytics.ui.aS
    public void c() {
        this.f10801a.b();
        this.f10812j = null;
        this.f10813k = 0.0d;
        d();
    }

    public void a(Color color) {
        this.f10811s = color;
        d();
    }
}
