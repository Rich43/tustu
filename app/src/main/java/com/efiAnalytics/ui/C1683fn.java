package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.UIManager;

/* renamed from: com.efiAnalytics.ui.fn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fn.class */
public class C1683fn extends JComponent {

    /* renamed from: c, reason: collision with root package name */
    private int f11682c;

    /* renamed from: d, reason: collision with root package name */
    private String f11683d;

    /* renamed from: e, reason: collision with root package name */
    private Color f11684e;

    /* renamed from: a, reason: collision with root package name */
    Color f11685a;

    /* renamed from: f, reason: collision with root package name */
    private boolean f11686f;

    /* renamed from: g, reason: collision with root package name */
    private String f11687g;

    /* renamed from: b, reason: collision with root package name */
    List f11688b;

    public C1683fn(String str, Color color) {
        this();
        this.f11683d = str;
        this.f11684e = color;
    }

    public C1683fn() {
        this.f11682c = -1;
        this.f11683d = "";
        this.f11684e = null;
        this.f11685a = Color.lightGray;
        this.f11686f = false;
        this.f11687g = "";
        this.f11688b = new ArrayList();
        addMouseListener(new C1684fo(this));
        setForeground(UIManager.getColor("Label.foreground"));
        setBackground(UIManager.getColor("Label.background"));
        a();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        a(graphics);
    }

    public void a(ActionListener actionListener) {
        this.f11688b.add(actionListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        ActionEvent actionEvent = new ActionEvent(this, 1001, e(), EventQueue.getMostRecentEventTime(), 0);
        Iterator it = this.f11688b.iterator();
        while (it.hasNext()) {
            ((ActionListener) it.next()).actionPerformed(actionEvent);
        }
    }

    private int g() {
        if (this.f11682c < 0) {
            this.f11682c = eJ.a(8);
        }
        return this.f11682c;
    }

    public void a() {
        Font font = UIManager.getFont("Label.font");
        int iA = eJ.a(11);
        if (font != null) {
            iA = Math.round(font.getSize2D() * (iA / eJ.a()));
        }
        setFont(new Font("SansSerif", 1, iA));
    }

    public void a(Graphics graphics) {
        FontMetrics fontMetrics = getFontMetrics(getFont());
        Color background = getBackground();
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String strB = b();
        graphics.setColor(background);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(getForeground());
        graphics.setFont(getFont());
        graphics.drawString(strB, (getWidth() - fontMetrics.stringWidth(strB)) - g(), (getHeight() - ((getHeight() - getFont().getSize()) / 2)) - 2);
        b(graphics);
        graphics.setColor(this.f11685a);
        graphics.draw3DRect(0, 0, getWidth() - 1, getHeight() - 1, false);
    }

    private void b(Graphics graphics) {
        if (this.f11686f) {
            graphics.setColor(c());
        } else {
            Color background = getBackground();
            if (background.getRed() + background.getBlue() + background.getGreen() < 38) {
                graphics.setColor(Color.DARK_GRAY.darker());
            } else {
                graphics.setColor(Color.GRAY);
            }
        }
        int height = (3 * (getHeight() - 2)) / 4;
        int iA = eJ.a(1) + ((getHeight() - height) / 2);
        graphics.fill3DRect(iA, iA, height, height, d());
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(h() + getFontMetrics(getFont()).stringWidth(b()) + g(), getFont().getSize() + eJ.a(8));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(h() + getFontMetrics(getFont()).stringWidth(b()) + g(), getFont().getSize() + eJ.a(8));
    }

    private int h() {
        int height = (3 * (getHeight() - 2)) / 4;
        return height + eJ.a(1) + (((getHeight() - 2) - height) / 2);
    }

    public String b() {
        return this.f11683d;
    }

    public Color c() {
        return this.f11684e;
    }

    public boolean d() {
        return this.f11686f;
    }

    public void a(boolean z2) {
        this.f11686f = z2;
    }

    public String e() {
        return this.f11687g;
    }
}
