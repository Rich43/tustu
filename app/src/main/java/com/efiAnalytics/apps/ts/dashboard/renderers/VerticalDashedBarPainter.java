package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/VerticalDashedBarPainter.class */
public class VerticalDashedBarPainter implements GaugePainter {
    int inset = 10;
    int barCount = 20;
    transient Image backImage = null;
    int lastWidth = 0;
    int lastHeight = 0;

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        graphics.drawImage(getBackImage(gauge), 0, 0, null);
        paintBar(graphics, gauge, false);
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
        int height = gauge.getHeight() - (2 * this.inset);
        this.barCount = height / 7;
        double d2 = height / this.barCount;
        paintBackground(graphics, gauge);
        int height2 = gauge.isCounterClockwise() ? this.inset + ((int) d2) : (gauge.getHeight() - this.inset) - ((int) d2);
        graphics.setColor(new Color(25, 25, 25));
        for (int i2 = 0; i2 < this.barCount; i2++) {
            int i3 = (int) (i2 * d2);
            if (i3 - (d2 / 4.0d) < height * 1.0d) {
                if (gauge.isCounterClockwise()) {
                    graphics.fillRect(this.inset, height2 + i3, gauge.getWidth() - (2 * this.inset), (int) (d2 * 0.7d));
                } else {
                    graphics.fillRect(this.inset, height2 - i3, gauge.getWidth() - (2 * this.inset), (int) (d2 * 0.7d));
                }
            }
        }
        return this.backImage;
    }

    protected void paintBar(Graphics graphics, Gauge gauge, boolean z2) {
        double smoothedValue = z2 ? 1.0d : (gauge.getSmoothedValue() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighWarning = (gauge.highWarning() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighCritical = (gauge.highCritical() - gauge.min()) / (gauge.max() - gauge.min());
        int height = gauge.getHeight() - (2 * this.inset);
        this.barCount = height / 7;
        double d2 = height / this.barCount;
        int height2 = gauge.isCounterClockwise() ? this.inset + ((int) d2) : (gauge.getHeight() - this.inset) - ((int) d2);
        graphics.setColor(getFaded(gauge.getFontColor(), z2));
        for (int i2 = 0; i2 < this.barCount; i2++) {
            int i3 = (int) (i2 * d2);
            if (i3 - (d2 / 4.0d) < height * smoothedValue) {
                if (i3 > dHighWarning * height) {
                    graphics.setColor(getFaded(gauge.getWarnColor(), z2));
                }
                if (i3 > dHighCritical * height) {
                    graphics.setColor(getFaded(gauge.getCriticalColor(), z2));
                }
                if (gauge.isCounterClockwise()) {
                    graphics.fillRect(this.inset, height2 + i3, gauge.getWidth() - (2 * this.inset), (int) (d2 * 0.7d));
                } else {
                    graphics.fillRect(this.inset, height2 - i3, gauge.getWidth() - (2 * this.inset), (int) (d2 * 0.7d));
                }
            }
        }
    }

    private Color getFaded(Color color, boolean z2) {
        return z2 ? new Color(color.getRed(), color.getGreen(), color.getBlue(), 35) : color;
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
        paintBar(graphics, gauge, true);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
        this.backImage = null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return "Vertical Dashed Bar Gauge";
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
        return (abstractC1420s.getWidth() == this.lastWidth && abstractC1420s.getHeight() == this.lastHeight) ? false : true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        paintBar(graphics, (Gauge) abstractC1420s, false);
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
