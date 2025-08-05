package com.efiAnalytics.apps.ts.dashboard;

import G.C0094c;
import G.cX;
import G.cY;
import G.cZ;
import com.efiAnalytics.ui.C1606cq;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/DashLabel.class */
public class DashLabel extends AbstractC1420s implements Serializable {

    /* renamed from: b, reason: collision with root package name */
    cX f9271b;

    /* renamed from: a, reason: collision with root package name */
    protected cZ f9268a = new C0094c("Label");

    /* renamed from: d, reason: collision with root package name */
    private Color f9269d = null;

    /* renamed from: f, reason: collision with root package name */
    private Color f9270f = Color.DARK_GRAY;

    /* renamed from: g, reason: collision with root package name */
    private Font f9272g = null;

    /* renamed from: c, reason: collision with root package name */
    Dimension f9273c = null;

    public DashLabel() {
        this.f9271b = null;
        setRelativeX(0.02d);
        setRelativeY(0.02d);
        setRelativeWidth(0.12d);
        setRelativeHeight(0.035d);
        this.f9271b = new C1416o(this);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void setRunDemo(boolean z2) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isRunDemo() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void goDead() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void invalidatePainter() {
        this.f9272g = null;
    }

    public String getText() {
        return this.f9268a.toString();
    }

    public void setText(String str) {
        try {
            this.f9268a = cY.a().a(this.f9271b, str);
        } catch (V.g e2) {
            Logger.getLogger(DashLabel.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isMustPaint() {
        return false;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (getBackgroundColor() != null && getBackgroundColor().getAlpha() > 0) {
            graphics.setColor(getBackgroundColor());
            graphics.fill3DRect(0, 0, getWidth(), getHeight(), true);
        }
        try {
            drawText(graphics, this.f9268a.a(), getTextColor());
        } catch (V.g e2) {
            Logger.getLogger(DashLabel.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void subscribeToOutput() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void unsubscribeToOutput() {
    }

    private void drawText(Graphics graphics, String str, Color color) {
        if (isAntialiasingOn()) {
            ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        graphics.setColor(color);
        Font textFont = getTextFont();
        graphics.setFont(textFont);
        graphics.drawString(str, (getWidth() - getFontMetrics(textFont).stringWidth(str)) / 2, (int) ((getHeight() / 2) + (textFont.getSize() * 0.36d)));
    }

    private boolean sameSize() {
        return this.f9273c != null && this.f9273c.width == getWidth() && this.f9273c.height == getHeight();
    }

    protected Font getTextFont() {
        String strA;
        if (this.f9272g != null && sameSize()) {
            return this.f9272g;
        }
        this.f9273c = getSize();
        int height = (getHeight() - getInsets().top) - getInsets().bottom;
        int width = (getWidth() - getInsets().left) - getInsets().right;
        String fontFamily = getFontFamily();
        try {
            strA = this.f9268a.a();
        } catch (V.g e2) {
            Logger.getLogger(DashLabel.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            strA = "Error";
        }
        int i2 = isItalicFont() ? 2 : 0;
        Font fontA = C1606cq.a().a(fontFamily, i2, height);
        if (fontA.canDisplayUpTo(strA) >= 0) {
            fontA = C1606cq.a().a("", i2 + 1, height);
        }
        while (getFontMetrics(fontA).stringWidth(strA) >= width && height >= 1) {
            height--;
            fontA = C1606cq.a().a(fontFamily, i2, height);
        }
        this.f9272g = fontA;
        return this.f9272g;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isComponentPaintedAt(int i2, int i3) {
        return i2 >= 0 && i2 < getWidth() && i3 >= 0 && i3 < getHeight();
    }

    public Color getBackgroundColor() {
        return this.f9269d;
    }

    public void setBackgroundColor(Color color) {
        this.f9269d = color;
    }

    public Color getTextColor() {
        return this.f9270f;
    }

    public void setTextColor(Color color) {
        this.f9270f = color;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void paintBackground(Graphics graphics) {
        paint(graphics);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean requiresBackgroundRepaint() {
        return this.f9272g == null;
    }

    @Override // java.awt.Container, java.awt.Component
    public void invalidate() {
        invalidatePainter();
        super.invalidate();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void updateGauge(Graphics graphics) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public Area areaPainted() {
        return new Area(new Rectangle2D.Double(0.0d, 0.0d, getWidth(), getHeight()));
    }
}
