package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cA.class */
public class cA extends JComponent {

    /* renamed from: c, reason: collision with root package name */
    private cD f11049c;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f11050a;

    /* renamed from: b, reason: collision with root package name */
    dD f11051b;

    /* renamed from: d, reason: collision with root package name */
    private String f11052d;

    /* renamed from: e, reason: collision with root package name */
    private String f11053e;

    /* renamed from: f, reason: collision with root package name */
    private String f11054f;

    /* renamed from: g, reason: collision with root package name */
    private Image f11055g;

    public cA() {
        this.f11049c = null;
        this.f11050a = new ArrayList();
        this.f11051b = null;
        this.f11052d = "X Axis";
        this.f11053e = "Y Axis";
        this.f11054f = "Z Axis";
        this.f11055g = null;
        this.f11051b = new dD(this);
        this.f11051b.b(1000);
        setToolTipText("Hey");
    }

    public cA(cD cDVar) {
        this();
        a(cDVar);
    }

    @Override // javax.swing.JComponent
    public String getToolTipText(MouseEvent mouseEvent) {
        return "<html>" + this.f11052d + ": " + this.f11049c.a((int) ((this.f11049c.a() / getWidth()) * mouseEvent.getX())) + "<br>" + this.f11053e + ": " + this.f11049c.b((int) ((this.f11049c.b() / getHeight()) * mouseEvent.getY())) + "<br>" + this.f11054f + ": " + (((int) (this.f11049c.a(r0, r0) * 10000.0d)) / 10000.0d) + "</html>";
    }

    public void a() {
        this.f11051b.a();
    }

    private Image b() {
        if (this.f11055g == null || this.f11055g.getWidth(null) != getWidth() || this.f11055g.getHeight(null) != getHeight()) {
            this.f11055g = createImage(getWidth(), getHeight());
        }
        return this.f11055g;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        Image imageB = b();
        a(imageB.getGraphics());
        graphics.drawImage(imageB, 0, 0, null);
    }

    public void a(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        if (this.f11049c == null) {
            return;
        }
        int width = getWidth() / this.f11049c.a();
        int height = getHeight() / this.f11049c.b();
        for (int i2 = 0; i2 < this.f11049c.b(); i2++) {
            for (int i3 = 0; i3 < this.f11049c.a(); i3++) {
                graphics.setColor(a(this.f11049c.a(i3, i2)));
                graphics.fillRect(i3 * width, i2 * height, width, height);
            }
        }
        graphics.setColor(Color.darkGray);
        graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    public Color a(double d2) {
        Color background = getBackground();
        Color colorB = null;
        double dA = 0.5d;
        int i2 = 0;
        while (true) {
            if (i2 >= this.f11050a.size()) {
                break;
            }
            if (d2 < ((cC) this.f11050a.get(i2)).f11057b) {
                colorB = ((cC) this.f11050a.get(i2)).b();
                if (i2 > 0) {
                    background = ((cC) this.f11050a.get(i2 - 1)).b();
                    dA = (d2 - ((cC) this.f11050a.get(i2 - 1)).a()) / (((cC) this.f11050a.get(i2)).a() - ((cC) this.f11050a.get(i2 - 1)).a());
                }
            } else {
                i2++;
            }
        }
        return colorB == null ? ((cC) this.f11050a.get(this.f11050a.size() - 1)).b() : new Color((int) ((colorB.getRed() * dA) + (background.getRed() * (1.0d - dA))), (int) ((colorB.getGreen() * dA) + (background.getGreen() * (1.0d - dA))), (int) ((colorB.getBlue() * dA) + (background.getBlue() * (1.0d - dA))));
    }

    public void a(double d2, Color color) {
        cC cCVar = new cC(this);
        cCVar.a(d2);
        cCVar.a(color);
        if (this.f11050a.size() <= 0) {
            this.f11050a.add(cCVar);
            return;
        }
        for (int size = this.f11050a.size(); size > 0; size++) {
            if (d2 > ((cC) this.f11050a.get(size - 1)).f11057b) {
                this.f11050a.add(size, cCVar);
                return;
            }
        }
    }

    public void a(cD cDVar) {
        this.f11049c = cDVar;
        cDVar.a(new cB(this));
    }

    public void a(String str) {
        this.f11052d = str;
    }

    public void b(String str) {
        this.f11053e = str;
    }

    public void c(String str) {
        this.f11054f = str;
    }
}
