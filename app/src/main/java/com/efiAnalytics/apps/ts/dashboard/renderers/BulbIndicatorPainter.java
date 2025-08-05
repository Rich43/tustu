package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.ui.C1606cq;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/BulbIndicatorPainter.class */
public class BulbIndicatorPainter implements IndicatorPainter {
    Font textFont = null;
    int minimumBulbSize = 14;
    int lastWidth = -1;
    int lastHeight = -1;
    double lastValue = Double.NaN;

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return "Bulb Indicator";
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Indicator indicator = (Indicator) abstractC1420s;
        this.lastWidth = abstractC1420s.getWidth();
        this.lastHeight = abstractC1420s.getHeight();
        this.lastValue = indicator.getValue();
        if (indicator.getValue() == 0.0d) {
            drawBulb(graphics, indicator, indicator.getOffBackgroundColor());
            drawText(graphics, indicator.offText(), indicator, indicator.getOffTextColor());
        } else {
            drawBulb(graphics, indicator, indicator.getOnBackgroundColor());
            drawText(graphics, indicator.onText(), indicator, indicator.getOnTextColor());
        }
    }

    private void drawBulb(Graphics graphics, Indicator indicator, Color color) {
        if (color == null || color.getAlpha() <= 0) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int height = indicator.getHeight() - getTextFont(indicator).getSize();
        int width = (indicator.getWidth() - height) / 2;
        graphics.setColor(color.darker());
        graphics.fillOval(width, 0, height, height);
        graphics.setColor(color.darker());
        graphics.fillOval(width + 1, 1, height - 2, height - 2);
        graphics.setColor(color);
        graphics.fillOval(width + 2, 2, height - 4, height - 4);
    }

    private void drawText(Graphics graphics, String str, Indicator indicator, Color color) {
        if (color == null || str == null) {
            return;
        }
        graphics.setColor(color);
        Font textFont = getTextFont(indicator);
        graphics.setFont(textFont);
        graphics.drawString(str, (indicator.getWidth() - indicator.getFontMetrics(textFont).stringWidth(str)) / 2, indicator.getHeight() - ((int) (textFont.getSize() * 0.1d)));
    }

    protected Font getTextFont(Indicator indicator) {
        if (this.textFont != null) {
            return this.textFont;
        }
        int height = ((indicator.getHeight() - indicator.getInsets().top) - indicator.getInsets().bottom) - this.minimumBulbSize;
        int i2 = height > 20 ? 20 : height;
        int width = (indicator.getWidth() - indicator.getInsets().left) - indicator.getInsets().right;
        String fontFamily = indicator.getFontFamily();
        int i3 = indicator.isItalicFont() ? 2 : 0;
        Font fontA = C1606cq.a().a(fontFamily, i3, i2);
        if (fontA.canDisplayUpTo(indicator.onText()) >= 0 && fontA.canDisplayUpTo(indicator.offText()) >= 0) {
            fontA = C1606cq.a().a("", i3, i2);
        }
        while (true) {
            FontMetrics fontMetrics = indicator.getFontMetrics(fontA);
            if ((fontMetrics.stringWidth(indicator.onText()) < width && fontMetrics.stringWidth(indicator.offText()) < width) || i2 < 1) {
                break;
            }
            i2--;
            fontA = C1606cq.a().a(fontFamily, i3, i2);
        }
        this.textFont = fontA;
        return this.textFont;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
        this.textFont = null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.IndicatorPainter
    public boolean isMustPaint() {
        return true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public boolean requiresBackgroundRepaint(AbstractC1420s abstractC1420s) {
        return (abstractC1420s.getWidth() == this.lastWidth && abstractC1420s.getHeight() == this.lastHeight && ((Indicator) abstractC1420s).getValue() == this.lastValue) ? false : true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s) {
        paintGauge(graphics, abstractC1420s);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public Area areaPainted(AbstractC1420s abstractC1420s) {
        Indicator indicator = (Indicator) abstractC1420s;
        Area area = new Area();
        if ((indicator.getOffBackgroundColor() != null && indicator.getOffBackgroundColor().getAlpha() > 0) || (indicator.getOnBackgroundColor() != null && indicator.getOnBackgroundColor().getAlpha() > 0)) {
            int height = indicator.getHeight() - getTextFont(indicator).getSize();
            area.add(new Area(new Arc2D.Double((indicator.getWidth() - height) / 2, 0.0d, height, height, 0.0d, 360.0d, 2)));
        }
        return area;
    }
}
