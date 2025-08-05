package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.ui.cN;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/HorizontalBarPainter.class */
public class HorizontalBarPainter implements GaugePainter {
    static final transient Color transparent = new Color(255, 255, 255, 0);
    String name = "Horizontal Bar Gauge";
    transient Image offImage = null;
    transient BufferedImage bgOffImage = null;

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        if (!gauge.isCounterClockwise()) {
            paintBar(graphics, gauge);
            paintBorder(graphics, gauge);
            return;
        }
        Image offImage = getOffImage(gauge);
        Graphics graphics2 = offImage.getGraphics();
        graphics2.setColor(transparent);
        ((Graphics2D) graphics2).setComposite(AlphaComposite.getInstance(2));
        graphics2.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        paintBar(graphics2, gauge);
        paintBorder(graphics2, gauge);
        graphics.drawImage(offImage, gauge.getWidth(), 0, -gauge.getWidth(), gauge.getHeight(), null);
    }

    protected void paintBar(Graphics graphics, Gauge gauge) {
        double smoothedValue = (gauge.getSmoothedValue() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighWarning = (gauge.highWarning() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighCritical = (gauge.highCritical() - gauge.min()) / (gauge.max() - gauge.min());
        int width = (int) (gauge.getWidth() * smoothedValue);
        graphics.setColor(gauge.getBackColor());
        graphics.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        graphics.setColor(gauge.getFontColor());
        graphics.fillRect(0, 0, width, gauge.getHeight());
        if (smoothedValue > dHighWarning) {
            graphics.setColor(gauge.getWarnColor());
            int width2 = (int) (gauge.getWidth() * dHighWarning);
            graphics.fillRect(width2, 0, width - width2, gauge.getHeight());
        }
        if (smoothedValue > dHighCritical) {
            graphics.setColor(gauge.getCriticalColor());
            int width3 = (int) (gauge.getWidth() * dHighCritical);
            graphics.fillRect(width3, 0, width - width3, gauge.getHeight());
        }
        double dAbs = Math.abs((gauge.getHistoricalPeakValue() - gauge.min()) / (gauge.max() - gauge.min()));
        if (gauge.isShowHistory() && dAbs - smoothedValue > 0.008d) {
            if (dAbs > dHighCritical) {
                graphics.setColor(gauge.getCriticalColor());
            } else if (dAbs > dHighWarning) {
                graphics.setColor(gauge.getWarnColor());
            } else {
                graphics.setColor(gauge.getFontColor());
            }
            int width4 = (int) (gauge.getWidth() * dAbs);
            graphics.drawLine(width4, 0, width4, gauge.getHeight());
        }
        if (gauge.backgroundImage() != null) {
            Image imageBackgroundImage = gauge.backgroundImage();
            if (this.bgOffImage == null || this.bgOffImage.getWidth() != gauge.getWidth() || this.bgOffImage.getHeight() != gauge.getHeight()) {
                this.bgOffImage = cN.a(cN.a(imageBackgroundImage), gauge.getWidth(), gauge.getHeight(), RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            }
            graphics.drawImage(this.bgOffImage, 0, 0, gauge);
        }
    }

    protected void paintBorder(Graphics graphics, Gauge gauge) {
        graphics.setColor(gauge.getTrimColor());
        int i2 = 0;
        while (i2 < gauge.getBorderWidth()) {
            graphics.draw3DRect(i2, i2, (gauge.getWidth() - 1) - (2 * i2), (gauge.getHeight() - 1) - (2 * i2), gauge.getBorderWidth() / 2 >= i2);
            i2++;
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
        this.offImage = null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return this.name;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public void initialize(Gauge gauge) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public boolean isShapeLockedToAspect() {
        return false;
    }

    protected Image getOffImage(Gauge gauge) {
        if (this.offImage == null) {
            this.offImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(gauge.getWidth(), gauge.getHeight(), 3);
        }
        return this.offImage;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public boolean requiresBackgroundRepaint(AbstractC1420s abstractC1420s) {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        if (!gauge.isCounterClockwise()) {
            paintBar(graphics, gauge);
            paintBorder(graphics, gauge);
            return;
        }
        Image offImage = getOffImage(gauge);
        Graphics graphics2 = offImage.getGraphics();
        graphics2.setColor(transparent);
        ((Graphics2D) graphics2).setComposite(AlphaComposite.getInstance(2));
        graphics2.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        paintBar(graphics2, gauge);
        paintBorder(graphics2, gauge);
        graphics.drawImage(offImage, gauge.getWidth(), 0, -gauge.getWidth(), gauge.getHeight(), null);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public Area areaPainted(AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        return new Area(new Rectangle2D.Double(0.0d, 0.0d, gauge.getWidth(), gauge.getHeight()));
    }
}
