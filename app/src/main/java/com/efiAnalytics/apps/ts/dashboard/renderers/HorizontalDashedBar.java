package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/HorizontalDashedBar.class */
public class HorizontalDashedBar implements GaugePainter {
    static String NAME = "Horizontal Dashed Bar Gauge";
    int inset = 10;
    int barCount = 20;
    transient Image backImage = null;
    int lastWidth = 0;
    int lastHeight = 0;

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        graphics.drawImage(getBackImage(gauge), 0, 0, null);
        paintBar(graphics, gauge);
    }

    protected Image getBackImage(Gauge gauge) {
        if (this.backImage != null && this.backImage.getWidth(null) == gauge.getWidth() && this.backImage.getHeight(null) == gauge.getHeight()) {
            return this.backImage;
        }
        this.backImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(gauge.getWidth(), gauge.getHeight(), 2);
        Graphics graphics = this.backImage.getGraphics();
        graphics.setColor(new Color(0, 0, 0, 0));
        graphics.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        this.inset = gauge.getBorderWidth() + 5;
        int width = gauge.getWidth() - (2 * this.inset);
        this.barCount = width / 7;
        double d2 = width / this.barCount;
        int width2 = (gauge.getWidth() - this.inset) - ((int) d2);
        graphics.setColor(new Color(25, 25, 25));
        for (int i2 = 0; i2 < this.barCount; i2++) {
            int i3 = (int) (i2 * d2);
            if (i3 - (d2 / 4.0d) < width * 1.0d) {
                graphics.fillRect(width2 - i3, this.inset, (int) (d2 * 0.7d), gauge.getHeight() - (2 * this.inset));
            }
        }
        paintBackground(graphics, gauge);
        return this.backImage;
    }

    protected void paintBar(Graphics graphics, Gauge gauge) {
        double smoothedValue = (gauge.getSmoothedValue() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighWarning = (gauge.highWarning() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighCritical = (gauge.highCritical() - gauge.min()) / (gauge.max() - gauge.min());
        int width = gauge.getWidth() - (2 * this.inset);
        this.barCount = width / 7;
        double d2 = width / this.barCount;
        int width2 = gauge.isCounterClockwise() ? (gauge.getWidth() - this.inset) - ((int) d2) : this.inset;
        graphics.setColor(gauge.getFontColor());
        for (int i2 = 0; i2 < this.barCount; i2++) {
            int i3 = (int) (i2 * d2);
            if (i3 - (d2 / 4.0d) < width * smoothedValue) {
                if (i3 > dHighWarning * width) {
                    graphics.setColor(gauge.getWarnColor());
                }
                if (i3 > dHighCritical * width) {
                    graphics.setColor(gauge.getCriticalColor());
                }
                if (gauge.isCounterClockwise()) {
                    graphics.fillRect(width2 - i3, this.inset, (int) (d2 * 0.7d), gauge.getHeight() - (2 * this.inset));
                } else {
                    graphics.fillRect(width2 + i3, this.inset, (int) (d2 * 0.7d), gauge.getHeight() - (2 * this.inset));
                }
            }
        }
    }

    protected void paintBackground(Graphics graphics, Gauge gauge) {
        this.lastWidth = gauge.getWidth();
        this.lastHeight = gauge.getHeight();
        graphics.setColor(gauge.getBackColor());
        graphics.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        if (gauge.backgroundImage() != null) {
            graphics.drawImage(gauge.backgroundImage(), 0, 0, gauge.getWidth(), gauge.getHeight(), null);
        }
        graphics.setColor(gauge.getTrimColor());
        int i2 = 0;
        while (i2 < gauge.getBorderWidth()) {
            graphics.draw3DRect(i2, i2, (gauge.getWidth() - 1) - (2 * i2), (gauge.getHeight() - 1) - (2 * i2), gauge.getBorderWidth() / 2 >= i2);
            i2++;
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
        this.backImage = null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return NAME;
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
        return (abstractC1420s.getWidth() == this.lastWidth && abstractC1420s.getHeight() == this.lastWidth) ? false : true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        paintBar(graphics, (Gauge) abstractC1420s);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s) {
        paintBackground(graphics, (Gauge) abstractC1420s);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public Area areaPainted(AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        return new Area(new Rectangle2D.Double(0.0d, 0.0d, gauge.getWidth(), gauge.getHeight()));
    }
}
