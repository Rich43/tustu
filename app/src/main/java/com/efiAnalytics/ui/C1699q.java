package com.efiAnalytics.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/* renamed from: com.efiAnalytics.ui.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/q.class */
public class C1699q extends aS {

    /* renamed from: i, reason: collision with root package name */
    private double f11732i = 0.7d;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f11733a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    ArrayList f11734b = new ArrayList();

    @Override // com.efiAnalytics.ui.aS
    public void a(Graphics graphics) {
        synchronized (this.f11733a) {
            if (this.f11733a.size() == 0) {
                return;
            }
            Rectangle rectangleK = k();
            int iT = t();
            double width = (rectangleK.getWidth() - iT) / this.f11733a.size();
            int i2 = (int) (width * this.f11732i);
            graphics.setColor(c(0));
            int iRound = rectangleK.height - ((int) Math.round(rectangleK.height * ((0.0d - h()) / (i() - h()))));
            for (int i3 = 0; i3 < this.f11733a.size(); i3++) {
                double dDoubleValue = ((Double) this.f11733a.get(i3)).doubleValue() / (i() - h());
                int i4 = dDoubleValue >= 0.0d ? ((int) (rectangleK.height * dDoubleValue)) - 2 : ((int) (rectangleK.height * dDoubleValue)) - 2;
                if (dDoubleValue >= 0.0d) {
                    graphics.fill3DRect(rectangleK.f12372x + ((int) ((i3 * width) + iT)), ((rectangleK.f12373y + iRound) - i4) - 2, i2, i4, true);
                } else {
                    graphics.fill3DRect(rectangleK.f12372x + ((int) ((i3 * width) + iT)), (rectangleK.f12373y + iRound) - 2, i2, -i4, true);
                }
            }
        }
    }

    private int t() {
        return getFontMetrics(l()).stringWidth(bH.W.a(i()) + m()) + 6;
    }

    private double u() {
        return (k().getWidth() - (getFontMetrics(l()).stringWidth(bH.W.a(i()) + m()) + 6)) / this.f11733a.size();
    }

    @Override // com.efiAnalytics.ui.aS
    public void b(Graphics graphics) {
        graphics.setColor(getForeground());
        Rectangle rectangleK = k();
        graphics.drawRect(rectangleK.f12372x, rectangleK.f12373y, rectangleK.width - 1, rectangleK.height - 1);
        graphics.setFont(l());
        for (int i2 = 1; i2 < 4; i2++) {
            int i3 = rectangleK.f12373y + (i2 * (rectangleK.height / 4));
            graphics.drawLine(rectangleK.f12372x, i3, rectangleK.width, i3);
            graphics.drawString(bH.W.a((((i() - h()) / 4) * (4 - i2)) + h(), 3) + " " + m(), rectangleK.f12372x + 2, i3 - 1);
        }
        if (h() < 0.0d) {
            int iRound = ((this.f10789d.f12373y + this.f10789d.height) - 1) - ((int) Math.round(this.f10789d.height * ((0.0d - h()) / (i() - h()))));
            graphics.drawLine(rectangleK.f12372x, iRound, rectangleK.width, iRound);
            graphics.drawString("0.0 " + m(), rectangleK.f12372x + 2, iRound - 1);
        }
    }

    private void e(double d2) {
        if (!j() || d2 <= i() - (i() * 0.06d)) {
            return;
        }
        d(d2 + (d2 * 0.06d));
    }

    private void f(double d2) {
        if (!j() || d2 >= h() - (h() * 0.06d)) {
            return;
        }
        c(d2 + (d2 * 0.06d));
    }

    public void a(double d2, int i2) {
        this.f11733a.add(i2, Double.valueOf(d2));
        e(d2);
        f(d2);
        d();
    }

    public void a(double d2) {
        this.f11733a.add(Double.valueOf(d2));
        e(d2);
        f(d2);
        d();
    }

    public void a() {
        this.f11733a.clear();
        d();
    }

    public void b() {
        this.f11734b.clear();
    }

    public void a(String str) {
        this.f11734b.add(str);
    }

    @Override // com.efiAnalytics.ui.aS
    public String a(int i2) {
        Rectangle rectangleK = k();
        double dU = u();
        if (i2 < rectangleK.f12372x + t() || i2 > rectangleK.f12372x + rectangleK.width) {
            return "";
        }
        int i3 = ((int) (((i2 - r0) - this.f10789d.f12372x) / dU)) + 1;
        return (this.f11734b.size() < i3 || this.f11734b.get(i3 - 1) == null) ? "Bar: " + i3 : (r() == null || r().trim().isEmpty()) ? (String) this.f11734b.get(i3 - 1) : ((String) this.f11734b.get(i3 - 1)) + "(" + r() + ")";
    }

    @Override // com.efiAnalytics.ui.aS
    public String b(int i2) {
        Rectangle rectangleK = k();
        if (i2 < rectangleK.f12373y) {
            return bH.W.a(h());
        }
        if (i2 > rectangleK.f12373y + rectangleK.height) {
            return bH.W.a(i());
        }
        return bH.W.a(h() + ((i() - h()) * (1.0d - ((i2 - rectangleK.f12373y) / rectangleK.height))), 3) + " " + s();
    }

    @Override // com.efiAnalytics.ui.aS
    public void c() {
        this.f11733a.clear();
        d();
    }

    public void b(double d2) {
        this.f11732i = d2;
    }
}
