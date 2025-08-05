package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.ui.C1606cq;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/BasicReadoutGaugePainter.class */
public class BasicReadoutGaugePainter implements GaugePainter {
    int lastWidth = 0;
    int lastHeight = 0;
    String lastDisplayVal = "";
    final String name = "Basic Readout";
    private String fontName = null;
    int maxStringWidth = 0;
    transient Font titleFont = null;
    transient Font valueFont = null;
    transient Color backColor = null;
    Image backgroundImg = null;
    int lastDigits = 0;

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return "Basic Readout";
    }

    private void checkFontFamily(Gauge gauge) {
        if (this.fontName == null) {
            Font fontA = C1606cq.a().a(gauge.getFontFamily(), 1, 12);
            if (fontA.canDisplayUpTo(gauge.title()) >= 0 || fontA.canDisplayUpTo(gauge.units()) >= 0) {
                this.fontName = "Arial Unicode MS";
            } else {
                this.fontName = gauge.getFontFamily();
            }
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        checkFontFamily(gauge);
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (gauge.isAntialiasingOn()) {
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        int width = gauge.getWidth();
        int height = gauge.getHeight();
        if (this.backgroundImg == null || this.backgroundImg.getWidth(null) != width || this.backgroundImg.getHeight(null) != height) {
            this.backgroundImg = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height, 3);
            paintBackground(this.backgroundImg.getGraphics(), gauge);
        }
        graphics.drawImage(this.backgroundImg, 0, 0, null);
        paintValues(graphics, gauge);
        paintBorder(graphics, gauge);
    }

    private void paintBackground(Graphics graphics, Gauge gauge) {
        int width = gauge.getWidth();
        int height = gauge.getHeight();
        this.lastWidth = width;
        this.lastHeight = height;
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.backColor = getCurrentBackColor(gauge);
        if (this.backColor != null && this.backColor.getAlpha() > 0) {
            graphics.setColor(this.backColor);
            if (gauge.getBorderWidth() > 0) {
                graphics.fillRect(0, 0, width, height);
            } else {
                graphics.fillRoundRect(0, 0, width, height, gauge.getHeight() / 4, gauge.getHeight() / 4);
            }
        }
        if (gauge.backgroundImage() != null) {
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.drawImage(gauge.backgroundImage(), 0, 0, width, height, null);
        }
    }

    private Color getCurrentBackColor(Gauge gauge) {
        return (gauge.getValue() > gauge.highCritical() || gauge.getValue() < gauge.lowCritical()) ? gauge.getCriticalColor() : (gauge.getValue() > gauge.highWarning() || gauge.getValue() < gauge.lowWarning()) ? gauge.getWarnColor() : gauge.getBackColor();
    }

    private void paintValues(Graphics graphics, Gauge gauge) {
        String displayValue = gauge.getDisplayValue();
        if (displayValue.length() != this.lastDigits) {
            this.titleFont = null;
            this.valueFont = null;
            this.lastDigits = displayValue.length();
        }
        Font valueFont = getValueFont(gauge);
        Font titleFont = getTitleFont(gauge);
        int borderWidth = gauge.title().equals("") && gauge.units().equals("") ? gauge.getBorderWidth() + ((gauge.getHeight() - (2 * gauge.getBorderWidth())) / 2) + ((int) (valueFont.getSize() * 0.37d)) : gauge.getBorderWidth() + ((gauge.getHeight() - (2 * gauge.getBorderWidth())) / 2) + ((int) (valueFont.getSize() * 0.2d));
        graphics.setFont(valueFont);
        graphics.setColor(gauge.getFontColor());
        graphics.drawString(displayValue, (gauge.getWidth() - graphics.getFontMetrics().stringWidth(displayValue)) / 2, borderWidth);
        this.lastDisplayVal = displayValue;
        graphics.setFont(titleFont);
        String strTitle = gauge.title();
        if (gauge.units() != null && !gauge.units().equals("")) {
            strTitle = strTitle + " (" + gauge.units() + ")";
        }
        int width = (gauge.getWidth() - graphics.getFontMetrics().stringWidth(strTitle)) / 2;
        graphics.drawString(strTitle, width < gauge.getBorderWidth() ? gauge.getBorderWidth() : width, borderWidth + ((int) (graphics.getFont().getSize() * 1.1d)));
    }

    private Font getValueFont(Gauge gauge) {
        if (this.valueFont == null) {
            setFonts(gauge);
        }
        return this.valueFont;
    }

    private Font getTitleFont(Gauge gauge) {
        if (this.titleFont == null) {
            setFonts(gauge);
        }
        return this.titleFont;
    }

    private void setFonts(Gauge gauge) {
        int i2;
        int height = gauge.getHeight();
        double dPow = 0.79d;
        if (gauge.getHeight() > gauge.getWidth()) {
            dPow = Math.pow(0.79d, 2.0d) * (gauge.getWidth() / gauge.getHeight());
        }
        if (gauge.title().equals("") && gauge.units().equals("")) {
            dPow += 0.19d;
        }
        int borderWidth = height - (gauge.getBorderWidth() * 2);
        int fontSizeAdjustment = ((int) (borderWidth * dPow)) + ((gauge.getFontSizeAdjustment() * borderWidth) / 30);
        int i3 = borderWidth / 4;
        if (fontSizeAdjustment < 0) {
            fontSizeAdjustment = 1;
            i2 = 1;
        } else if (i3 < 10) {
            int i4 = fontSizeAdjustment + i3;
            if (i4 > 10 * 2) {
                i2 = 10;
                fontSizeAdjustment = i4 - 10;
            } else {
                i2 = i4 / 2;
                fontSizeAdjustment = i4 / 2;
            }
        } else {
            i2 = borderWidth / 4;
        }
        int i5 = gauge.isItalicFont() ? 3 : 1;
        String displayValue = gauge.getDisplayValue();
        int width = (int) ((gauge.getWidth() - (2 * gauge.getBorderWidth())) * 0.98d);
        do {
            int i6 = fontSizeAdjustment;
            fontSizeAdjustment--;
            this.valueFont = C1606cq.a().a(gauge.getFontFamily(), i5, i6);
        } while (gauge.getFontMetrics(this.valueFont).stringWidth(displayValue) > width);
        String strTitle = gauge.title();
        if (gauge.units() != null && !gauge.units().equals("")) {
            strTitle = strTitle + " (" + gauge.units() + ")";
        }
        do {
            int i7 = i2;
            i2--;
            this.titleFont = C1606cq.a().a(this.fontName, i5, i7);
        } while (gauge.getFontMetrics(this.titleFont).stringWidth(strTitle) > width);
    }

    private void paintBorder(Graphics graphics, Gauge gauge) {
        int width = gauge.getWidth();
        int height = gauge.getHeight();
        graphics.setColor(gauge.getTrimColor());
        int i2 = 0;
        while (i2 < gauge.getBorderWidth()) {
            graphics.draw3DRect(i2, i2, (width - (2 * i2)) - 1, (height - (2 * i2)) - 1, i2 < gauge.getBorderWidth() / 2);
            i2++;
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
        this.fontName = null;
        this.titleFont = null;
        this.valueFont = null;
        this.backgroundImg = null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public void initialize(Gauge gauge) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public boolean isShapeLockedToAspect() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public boolean requiresBackgroundRepaint(AbstractC1420s abstractC1420s) {
        return (abstractC1420s.getWidth() == this.lastWidth && abstractC1420s.getHeight() == this.lastHeight && this.backColor != null && this.backColor.equals(getCurrentBackColor((Gauge) abstractC1420s))) ? false : true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        if (abstractC1420s.isAntialiasingOn()) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        paintValues(graphics, (Gauge) abstractC1420s);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s) {
        paintBackground(graphics, (Gauge) abstractC1420s);
        paintBorder(graphics, (Gauge) abstractC1420s);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public Area areaPainted(AbstractC1420s abstractC1420s) {
        return ((Gauge) abstractC1420s).getBackColor().getAlpha() > 50 ? new Area(new Rectangle2D.Double(0.0d, 0.0d, r0.getWidth(), r0.getHeight())) : new Area();
    }
}
