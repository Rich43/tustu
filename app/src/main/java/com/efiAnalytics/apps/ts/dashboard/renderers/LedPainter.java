package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/LedPainter.class */
public class LedPainter implements GaugePainter {
    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return "LED Readout (Not Ready)";
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        graphics.drawString("Not finished", 0, graphics.getFont().getSize());
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
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
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
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
