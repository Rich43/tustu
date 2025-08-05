package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/VerticalBarPainter.class */
public class VerticalBarPainter implements GaugePainter {
    String name = "Vertical Bar Gauge";
    transient Image offImage = null;
    static final transient Color transparent = new Color(255, 255, 255, 0);

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
        graphics.drawImage(offImage, 0, gauge.getHeight(), gauge.getWidth(), -gauge.getHeight(), null);
    }

    protected void paintBar(Graphics graphics, Gauge gauge) {
        double smoothedValue = (gauge.getSmoothedValue() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighWarning = (gauge.highWarning() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighCritical = (gauge.highCritical() - gauge.min()) / (gauge.max() - gauge.min());
        int height = gauge.getHeight() - ((int) (gauge.getHeight() * smoothedValue));
        graphics.setColor(gauge.getBackColor());
        graphics.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        graphics.setColor(gauge.getFontColor());
        graphics.fillRect(0, height, gauge.getWidth(), gauge.getHeight() - height);
        if (smoothedValue > dHighWarning) {
            graphics.setColor(gauge.getWarnColor());
            graphics.fillRect(0, height, gauge.getWidth(), (int) (gauge.getHeight() * (smoothedValue - dHighWarning)));
        }
        if (smoothedValue > dHighCritical) {
            graphics.setColor(gauge.getCriticalColor());
            graphics.fillRect(0, height, gauge.getWidth(), (int) (gauge.getHeight() * (smoothedValue - dHighCritical)));
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
            return;
        }
        Image offImage = getOffImage(gauge);
        Graphics graphics2 = offImage.getGraphics();
        graphics2.setColor(transparent);
        ((Graphics2D) graphics2).setComposite(AlphaComposite.getInstance(2));
        graphics2.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        paintBar(graphics2, gauge);
        graphics.drawImage(offImage, 0, gauge.getHeight(), gauge.getWidth(), -gauge.getHeight(), null);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s) {
        paintBorder(graphics, (Gauge) abstractC1420s);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public Area areaPainted(AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        return new Area(new Rectangle2D.Double(0.0d, 0.0d, gauge.getWidth(), gauge.getHeight()));
    }
}
