package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: com.efiAnalytics.ui.da, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/da.class */
class C1617da {

    /* renamed from: f, reason: collision with root package name */
    String f11397f;

    /* renamed from: g, reason: collision with root package name */
    String f11398g;

    /* renamed from: h, reason: collision with root package name */
    String f11399h;

    /* renamed from: i, reason: collision with root package name */
    String f11400i;

    /* renamed from: l, reason: collision with root package name */
    final /* synthetic */ cZ f11403l;

    /* renamed from: a, reason: collision with root package name */
    BufferedImage f11392a = null;

    /* renamed from: b, reason: collision with root package name */
    int f11393b = eJ.a(25);

    /* renamed from: c, reason: collision with root package name */
    int f11394c = eJ.a(25);

    /* renamed from: d, reason: collision with root package name */
    Font f11395d = new Font("Arial Unicode MS", 1, eJ.a(18));

    /* renamed from: e, reason: collision with root package name */
    Font f11396e = new Font("Arial Unicode MS", 0, eJ.a(14));

    /* renamed from: j, reason: collision with root package name */
    int f11401j = 0;

    /* renamed from: k, reason: collision with root package name */
    List f11402k = new ArrayList();

    C1617da(cZ cZVar) {
        this.f11403l = cZVar;
        this.f11397f = this.f11403l.h("Area");
        this.f11398g = this.f11403l.h("Minimum");
        this.f11399h = this.f11403l.h("Maximum");
        this.f11400i = this.f11403l.h("Average");
    }

    public void a(Graphics graphics, C1618db c1618db) {
        Rectangle rectangleK = this.f11403l.k();
        String strB = bH.W.b(c1618db.a());
        String strB2 = bH.W.b(c1618db.c());
        String strB3 = bH.W.b(c1618db.d());
        String strB4 = bH.W.b(c1618db.e());
        graphics.setFont(this.f11395d);
        int size = this.f11395d.getSize() / 2;
        int iStringWidth = graphics.getFontMetrics().stringWidth(strB);
        if (this.f11392a == null || this.f11392a.getWidth() < iStringWidth * 1.1d) {
            this.f11402k = a(this.f11402k);
            this.f11401j = 0;
            Iterator it = this.f11402k.iterator();
            while (it.hasNext()) {
                int iStringWidth2 = graphics.getFontMetrics().stringWidth((String) it.next());
                if (this.f11401j < iStringWidth2) {
                    this.f11401j = iStringWidth2;
                }
            }
            int i2 = (int) (iStringWidth * 1.5d);
            if (this.f11401j > i2) {
                i2 = (int) (this.f11401j * 1.1d);
            }
            int height = ((graphics.getFontMetrics(this.f11396e).getHeight() + graphics.getFontMetrics(this.f11395d).getHeight()) * 4) + (graphics.getFontMetrics(this.f11395d).getHeight() / 2);
            this.f11392a = new BufferedImage(i2, height, 2);
            Graphics2D graphics2DCreateGraphics = this.f11392a.createGraphics();
            graphics2DCreateGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2DCreateGraphics.setColor(new Color(128, 128, 128, 128));
            graphics2DCreateGraphics.fillRoundRect(0, 0, i2, height, i2 / 6, i2 / 6);
            int size2 = this.f11396e.getSize() + (this.f11395d.getSize() / 4);
            graphics2DCreateGraphics.setFont(this.f11396e);
            graphics2DCreateGraphics.setColor(Color.WHITE);
            for (String str : this.f11402k) {
                graphics2DCreateGraphics.drawString(str, (this.f11392a.getWidth() - graphics2DCreateGraphics.getFontMetrics().stringWidth(str)) / 2, size2);
                size2 += this.f11396e.getSize() + this.f11395d.getSize() + size;
            }
        }
        this.f11394c = rectangleK.f12373y + eJ.a(5);
        this.f11393b = (rectangleK.width - this.f11392a.getWidth()) - eJ.a(5);
        graphics.drawImage(this.f11392a, rectangleK.f12372x + this.f11393b, this.f11394c, null);
        graphics.setColor(this.f11403l.c(0));
        int size3 = this.f11394c + this.f11396e.getSize() + this.f11395d.getSize() + (this.f11395d.getSize() / 3);
        graphics.drawString(strB, rectangleK.f12372x + this.f11393b + ((this.f11392a.getWidth() - iStringWidth) / 2), size3);
        int size4 = size3 + this.f11396e.getSize() + this.f11395d.getSize() + size;
        graphics.drawString(strB2, rectangleK.f12372x + this.f11393b + ((this.f11392a.getWidth() - graphics.getFontMetrics().stringWidth(strB2)) / 2), size4);
        int size5 = size4 + this.f11396e.getSize() + this.f11395d.getSize() + size;
        graphics.drawString(strB3, rectangleK.f12372x + this.f11393b + ((this.f11392a.getWidth() - graphics.getFontMetrics().stringWidth(strB3)) / 2), size5);
        graphics.drawString(strB4, rectangleK.f12372x + this.f11393b + ((this.f11392a.getWidth() - graphics.getFontMetrics().stringWidth(strB4)) / 2), size5 + this.f11396e.getSize() + this.f11395d.getSize() + size);
    }

    private List a(List list) {
        list.clear();
        if (this.f11403l.y().isEmpty() || this.f11403l.j(0).isEmpty()) {
            list.add(this.f11397f);
        } else {
            list.add(this.f11397f + " (" + this.f11403l.j(0) + " " + this.f11403l.y() + ")");
        }
        if (this.f11403l.j(0).isEmpty()) {
            list.add(this.f11398g);
            list.add(this.f11399h);
            list.add(this.f11400i);
        } else {
            list.add(this.f11398g + " (" + this.f11403l.j(0) + ")");
            list.add(this.f11399h + " (" + this.f11403l.j(0) + ")");
            list.add(this.f11400i + " (" + this.f11403l.j(0) + ")");
        }
        return list;
    }

    public void a() {
        this.f11402k.clear();
        this.f11392a = null;
    }
}
