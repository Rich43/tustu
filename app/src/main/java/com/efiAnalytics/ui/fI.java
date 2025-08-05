package com.efiAnalytics.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.UIManager;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fI.class */
public class fI extends JPanel {

    /* renamed from: b, reason: collision with root package name */
    private Image f11638b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f11639c = "";

    /* renamed from: a, reason: collision with root package name */
    fJ f11640a = null;

    protected Image a() {
        if (this.f11638b == null) {
            try {
                this.f11638b = cO.a().a(cO.f11106v);
            } catch (V.a e2) {
                Logger.getLogger(fI.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return this.f11638b;
    }

    @Override // javax.swing.JComponent
    public void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);
        if (this.f11640a == null || !this.f11640a.f11641a) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Image imageA = a();
        if (imageA != null) {
            int width = imageA.getWidth(null) / 2;
            int height = imageA.getHeight(null) / 2;
            graphics.drawImage(imageA, (getWidth() - width) / 2, (getHeight() - height) / 2, width, height, null);
        }
        graphics.setFont(UIManager.getFont("Label.font"));
        graphics.drawString(this.f11639c, (getWidth() - getFontMetrics(graphics.getFont()).stringWidth(this.f11639c)) / 2, getHeight() - graphics.getFont().getSize());
    }

    public void b() {
        c();
        this.f11640a = new fJ(this);
        this.f11640a.start();
    }

    public void c() {
        if (this.f11640a != null) {
            this.f11640a.a();
            this.f11640a = null;
        }
        repaint();
    }

    public void a(String str) {
        this.f11639c = str;
    }
}
