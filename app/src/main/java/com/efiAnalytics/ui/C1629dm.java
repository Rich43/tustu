package com.efiAnalytics.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;

/* renamed from: com.efiAnalytics.ui.dm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dm.class */
public class C1629dm {

    /* renamed from: b, reason: collision with root package name */
    JComponent f11430b;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f11429a = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    Image f11431c = null;

    /* renamed from: d, reason: collision with root package name */
    int f11432d = 2;

    /* renamed from: e, reason: collision with root package name */
    Font f11433e = null;

    /* renamed from: f, reason: collision with root package name */
    Font f11434f = null;

    /* renamed from: g, reason: collision with root package name */
    Font f11435g = null;

    /* renamed from: h, reason: collision with root package name */
    Insets f11436h = null;

    /* renamed from: i, reason: collision with root package name */
    Stroke f11437i = new BasicStroke(2.0f, 1, 2);

    public C1629dm(JComponent jComponent) {
        this.f11430b = null;
        this.f11430b = jComponent;
    }

    public void a(Graphics graphics) {
        d(graphics);
        c(graphics);
    }

    private void c(Graphics graphics) {
        Insets insetsC = c();
        for (int i2 = 0; i2 < this.f11429a.size(); i2++) {
            InterfaceC1614cy interfaceC1614cy = (InterfaceC1614cy) this.f11429a.get(i2);
            double[] dArrB = b(interfaceC1614cy);
            int[] iArr = new int[dArrB.length + 2];
            int[] iArr2 = new int[dArrB.length + 2];
            int width = this.f11430b.getWidth();
            int height = (this.f11430b.getHeight() - insetsC.top) - insetsC.bottom;
            for (int i3 = 1; i3 < iArr2.length - 1; i3++) {
                iArr[i3] = (width - insetsC.right) - ((i3 - 1) * this.f11432d);
                double dC = (dArrB[i3 - 1] - interfaceC1614cy.c()) / (interfaceC1614cy.d() - interfaceC1614cy.c());
                if (dC > 1.0d) {
                    iArr2[i3] = insetsC.top;
                } else if (dC < 0.0d) {
                    iArr2[i3] = this.f11430b.getHeight() - insetsC.bottom;
                } else {
                    iArr2[i3] = (this.f11430b.getHeight() - ((int) (height * dC))) - insetsC.bottom;
                }
            }
            iArr[0] = iArr[1];
            iArr[iArr.length - 1] = iArr[iArr.length - 2];
            iArr2[0] = height + insetsC.top;
            iArr2[iArr2.length - 1] = height + insetsC.top;
            graphics.setColor(interfaceC1614cy.e());
            ((Graphics2D) graphics).setStroke(this.f11437i);
            graphics.drawPolyline(iArr, iArr2, iArr.length);
        }
        Font fontD = d();
        Color color = new Color(0, 0, 0, 128);
        int height2 = (this.f11430b.getHeight() - insetsC.bottom) - fontD.getSize();
        for (int i4 = 0; i4 < this.f11429a.size(); i4++) {
            InterfaceC1614cy interfaceC1614cy2 = (InterfaceC1614cy) this.f11429a.get(i4);
            String strA = bH.W.a(interfaceC1614cy2.a(0));
            int width2 = ((this.f11430b.getWidth() - graphics.getFontMetrics(fontD).stringWidth(strA)) - insetsC.right) - 6;
            if (strA != null) {
                graphics.setColor(color);
                graphics.fillRect(width2, height2 - fontD.getSize(), graphics.getFontMetrics(fontD).stringWidth(strA), fontD.getSize());
                graphics.setColor(interfaceC1614cy2.e());
                graphics.drawString(strA, width2, height2);
            }
            height2 -= fontD.getSize();
        }
    }

    private double[] b(InterfaceC1614cy interfaceC1614cy) {
        double[] dArr;
        Insets insetsC = c();
        int width = 1 + (((this.f11430b.getWidth() - insetsC.left) - insetsC.right) / this.f11432d);
        synchronized (interfaceC1614cy) {
            int size = (interfaceC1614cy.size() <= width || width <= 0) ? interfaceC1614cy.size() : width;
            dArr = new double[size];
            for (int i2 = 0; i2 < size; i2++) {
                dArr[i2] = interfaceC1614cy.a(i2);
            }
        }
        if (dArr.length == 0) {
            dArr = new double[]{0.0d};
        }
        return dArr;
    }

    private void d(Graphics graphics) {
        if (this.f11431c == null || this.f11430b.getWidth() != this.f11431c.getWidth(null) || this.f11430b.getHeight() != this.f11431c.getHeight(null)) {
            BufferedImage bufferedImageCreateCompatibleImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(this.f11430b.getWidth(), this.f11430b.getHeight(), 2);
            bufferedImageCreateCompatibleImage.getGraphics();
            this.f11431c = bufferedImageCreateCompatibleImage;
            a(this.f11432d + (this.f11430b.getWidth() / this.f11432d));
        }
        b(graphics);
    }

    protected void b(Graphics graphics) {
        graphics.setColor(this.f11430b.getBackground());
        graphics.fillRect(0, 0, this.f11430b.getWidth(), this.f11430b.getHeight());
        Insets insetsC = c();
        int i2 = 40;
        double height = 1.0d;
        boolean z2 = false;
        if (this.f11430b.getHeight() < 160) {
            i2 = 20;
            z2 = true;
        }
        int i3 = 2;
        while (true) {
            int i4 = i3;
            if (this.f11430b.getHeight() / i4 <= i2) {
                break;
            }
            height = (this.f11430b.getHeight() / i4) / this.f11430b.getHeight();
            i3 = i4 * 2;
        }
        boolean z3 = true;
        for (double d2 = height; d2 <= 1.0d; d2 += height) {
            z3 = z2 ? !z3 : true;
            a(graphics, d2, z3);
        }
        Font fontE = e();
        int iStringWidth = 3 + insetsC.left;
        for (int i5 = 0; i5 < this.f11429a.size(); i5++) {
            InterfaceC1614cy interfaceC1614cy = (InterfaceC1614cy) this.f11429a.get(i5);
            String strB = bH.W.b(interfaceC1614cy.d(), interfaceC1614cy.b());
            graphics.setColor(interfaceC1614cy.e());
            graphics.drawString(strB, iStringWidth, fontE.getSize() + insetsC.top);
            iStringWidth = iStringWidth + this.f11430b.getFontMetrics(fontE).stringWidth(strB) + 4;
        }
        Font fontD = d();
        int size = insetsC.top + fontD.getSize();
        for (int i6 = 0; i6 < this.f11429a.size(); i6++) {
            InterfaceC1614cy interfaceC1614cy2 = (InterfaceC1614cy) this.f11429a.get(i6);
            String strF = interfaceC1614cy2.f();
            if (interfaceC1614cy2.g() != null && !interfaceC1614cy2.g().equals("")) {
                strF = strF + "(" + interfaceC1614cy2.g() + ")";
            }
            int width = ((this.f11430b.getWidth() - graphics.getFontMetrics(fontD).stringWidth(strF)) - insetsC.right) - 4;
            graphics.setColor(interfaceC1614cy2.e());
            if (strF != null) {
                graphics.drawString(strF, width, size);
            }
            size += fontD.getSize();
        }
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawRect(insetsC.left, insetsC.top, ((this.f11430b.getWidth() - insetsC.left) - insetsC.right) - 1, ((this.f11430b.getHeight() - insetsC.top) - insetsC.bottom) - 1);
    }

    private void a(Graphics graphics, double d2, boolean z2) {
        Insets insetsC = c();
        int i2 = insetsC.left;
        int width = (this.f11430b.getWidth() - insetsC.left) - insetsC.right;
        int iRound = ((int) Math.round(((this.f11430b.getHeight() - insetsC.top) - insetsC.bottom) * d2)) + insetsC.top;
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawLine(i2, iRound, width + i2, iRound);
        int iStringWidth = 3 + i2;
        Font fontE = e();
        graphics.setFont(fontE);
        for (int i3 = 0; i3 < this.f11429a.size(); i3++) {
            if (z2) {
                InterfaceC1614cy interfaceC1614cy = (InterfaceC1614cy) this.f11429a.get(i3);
                String strB = bH.W.b(interfaceC1614cy.d() - (d2 * (interfaceC1614cy.d() - interfaceC1614cy.c())), interfaceC1614cy.b());
                graphics.setColor(interfaceC1614cy.e());
                graphics.drawString(strB, iStringWidth, iRound - 2);
                iStringWidth = iStringWidth + this.f11430b.getFontMetrics(fontE).stringWidth(strB) + 4;
            }
        }
    }

    private void a(int i2) {
        Iterator it = this.f11429a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1614cy) it.next()).c(i2);
        }
    }

    public void a() {
        this.f11431c = null;
        this.f11433e = null;
        this.f11434f = null;
        this.f11436h = null;
    }

    public void a(InterfaceC1614cy interfaceC1614cy) {
        this.f11429a.add(interfaceC1614cy);
    }

    public int b() {
        return this.f11429a.size();
    }

    private Font d() {
        if (this.f11433e == null) {
            this.f11433e = new Font(this.f11430b.getFont().getName(), 1, this.f11430b.getFont().getSize());
        }
        return this.f11433e;
    }

    private Font e() {
        if (this.f11434f == null) {
            this.f11434f = new Font(d().getName(), 0, d().getSize());
        }
        return this.f11434f;
    }

    public Insets c() {
        if (this.f11436h == null) {
            this.f11436h = this.f11430b.getInsets();
        }
        return this.f11436h;
    }
}
