package com.efiAnalytics.ui;

import G.C0129l;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

/* renamed from: com.efiAnalytics.ui.ep, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ep.class */
public class C1658ep extends JLabel implements G.aG, G.aV {

    /* renamed from: a, reason: collision with root package name */
    int f11596a;

    /* renamed from: b, reason: collision with root package name */
    Insets f11597b;

    /* renamed from: c, reason: collision with root package name */
    Color f11598c;

    /* renamed from: d, reason: collision with root package name */
    Color f11599d;

    /* renamed from: e, reason: collision with root package name */
    Color f11600e;

    /* renamed from: f, reason: collision with root package name */
    Color f11601f;

    /* renamed from: m, reason: collision with root package name */
    private String f11602m;

    /* renamed from: g, reason: collision with root package name */
    Stroke f11603g;

    /* renamed from: h, reason: collision with root package name */
    Dimension f11604h;

    /* renamed from: i, reason: collision with root package name */
    dD f11605i;

    /* renamed from: j, reason: collision with root package name */
    long f11606j;

    /* renamed from: k, reason: collision with root package name */
    int f11607k;

    /* renamed from: l, reason: collision with root package name */
    long f11608l;

    public C1658ep() {
        this("");
    }

    public C1658ep(String str) {
        this.f11596a = 0;
        this.f11597b = eJ.a(new Insets(1, 1, 1, 1));
        this.f11598c = Color.red;
        this.f11599d = new Color(96, 0, 0);
        this.f11600e = Color.green;
        this.f11601f = new Color(0, 96, 0);
        this.f11602m = null;
        this.f11603g = new BasicStroke(eJ.a(2.0f));
        this.f11604h = new Dimension(eJ.a(20), eJ.a(18));
        this.f11605i = null;
        this.f11606j = System.currentTimeMillis();
        this.f11607k = 100;
        this.f11608l = System.currentTimeMillis();
        this.f11602m = str;
        setMinimumSize(this.f11604h);
        setPreferredSize(this.f11604h);
        setToolTipText("<html>" + str + "<br>Red: sending<br>Green: receiving</html>");
        super.setDoubleBuffered(true);
        this.f11605i = new dD(this);
        this.f11605i.b(1);
        setOpaque(true);
        addMouseListener(new C1661es(this));
    }

    public void a() {
        if (this.f11605i != null) {
            this.f11605i.a(false);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        int height = getHeight() / 2;
        int height2 = (getHeight() - height) / 2;
        int width = (getWidth() - this.f11597b.left) - this.f11597b.right;
        int i2 = (int) (height * 0.8d);
        graphics.setColor(Color.GRAY);
        graphics.fillRect(this.f11597b.left, height2, width, height);
        if ((this.f11596a & 4) > 0) {
            graphics.setColor(this.f11598c);
        } else {
            graphics.setColor(this.f11599d);
        }
        int width2 = ((getWidth() / 2) - i2) - 1;
        int i3 = height2 + ((height - i2) / 2);
        graphics.fillOval(width2, i3, i2, i2);
        if ((this.f11596a & 2) > 0 || this.f11606j > System.currentTimeMillis()) {
            graphics.setColor(this.f11600e);
        } else {
            graphics.setColor(this.f11601f);
        }
        graphics.fillOval((getWidth() / 2) + 1, i3, i2, i2);
        if ((this.f11596a & 1) != 1) {
            int height3 = getHeight() - 1;
            graphics.setColor(Color.red);
            graphics2D.setStroke(this.f11603g);
            graphics.drawOval((getWidth() - height3) / 2, 0, height3, height3);
            int i4 = (int) (((height3 / 2) * 1.414213d) / 2.0d);
            int width3 = getWidth() / 2;
            int height4 = getHeight() / 2;
            graphics.drawLine(width3 - i4, height4 - i4, width3 + i4, height4 + i4);
        }
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) {
        this.f11596a |= 1;
        setToolTipText(getToolTipText());
        this.f11605i.a();
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        if (str.equals(this.f11602m)) {
            this.f11596a = 0;
            setToolTipText(getToolTipText());
            this.f11605i.a();
        }
    }

    @Override // G.aV
    public void b(String str) {
        if (str.equals(this.f11602m)) {
            this.f11596a |= 4;
            this.f11605i.a();
        }
    }

    @Override // G.aV
    public void c(String str) {
        if (str.equals(this.f11602m)) {
            this.f11596a &= -5;
            this.f11605i.a(200);
        }
    }

    @Override // G.aV
    public void d(String str) {
        if (str.equals(this.f11602m)) {
            this.f11596a |= 2;
            this.f11605i.a();
        }
    }

    @Override // G.aV
    public void e(String str) {
        if (str.equals(this.f11602m)) {
            this.f11596a &= -3;
            this.f11606j = System.currentTimeMillis() + 5;
            this.f11605i.a();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public Insets getInsets() {
        return this.f11597b;
    }

    public String b() {
        return this.f11602m;
    }

    public void c() {
        G.R rC = G.T.a().c(this.f11602m);
        if (rC != null) {
            JPopupMenu jPopupMenu = new JPopupMenu();
            if (rC.R()) {
                jPopupMenu.add(f("Work Offline")).addActionListener(new C1659eq(this));
            } else {
                jPopupMenu.add(f("Go Online")).addActionListener(new C1660er(this));
            }
            add(jPopupMenu);
            jPopupMenu.show(this, 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        G.R rC = G.T.a().c(this.f11602m);
        if (rC != null) {
            rC.C().c();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        G.R rC = G.T.a().c(this.f11602m);
        if (rC != null) {
            try {
                rC.C().d();
            } catch (C0129l e2) {
                Logger.getLogger(C1658ep.class.getName()).log(Level.INFO, "Cannot go online", (Throwable) e2);
            }
        }
    }

    private String f(String str) {
        bH.aa aaVarA = bV.a();
        return aaVarA != null ? aaVarA.a(str) : str;
    }
}
