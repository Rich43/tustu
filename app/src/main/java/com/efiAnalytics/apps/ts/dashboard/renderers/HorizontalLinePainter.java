package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.Graphics;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/HorizontalLinePainter.class */
public class HorizontalLinePainter extends HorizontalBarPainter {
    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.HorizontalBarPainter
    protected void paintBar(Graphics graphics, Gauge gauge) {
        double smoothedValue = (gauge.getSmoothedValue() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighWarning = (gauge.highWarning() - gauge.min()) / (gauge.max() - gauge.min());
        double dHighCritical = (gauge.highCritical() - gauge.min()) / (gauge.max() - gauge.min());
        int width = ((int) ((gauge.getWidth() - (gauge.getBorderWidth() * 2)) * smoothedValue)) + gauge.getBorderWidth();
        if (width < gauge.getBorderWidth()) {
            width = gauge.getBorderWidth();
        } else if (width > gauge.getWidth() - gauge.getBorderWidth()) {
            width = gauge.getWidth() - gauge.getBorderWidth();
        }
        graphics.setColor(gauge.getBackColor());
        graphics.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        int width2 = gauge.getWidth() / 50;
        int i2 = width2 < 1 ? 1 : width2;
        int i3 = i2 > 20 ? 20 : i2;
        if (gauge.getSmoothedValue() > gauge.getHighCritical() || gauge.getSmoothedValue() < gauge.getLowCritical()) {
            graphics.setColor(gauge.getCriticalColor());
        } else if (gauge.getSmoothedValue() > gauge.getHighWarning() || gauge.getSmoothedValue() < gauge.getLowWarning()) {
            graphics.setColor(gauge.getWarnColor());
        } else {
            graphics.setColor(gauge.getNeedleColor());
        }
        graphics.fillRect(width - (i3 / 2), 0, i3, gauge.getHeight());
        double dAbs = Math.abs((gauge.getHistoricalPeakValue() - gauge.min()) / (gauge.max() - gauge.min()));
        if (!gauge.isShowHistory() || dAbs - smoothedValue <= 0.008d) {
            return;
        }
        if (dAbs > dHighCritical) {
            graphics.setColor(gauge.getCriticalColor());
        } else if (dAbs > dHighWarning) {
            graphics.setColor(gauge.getWarnColor());
        } else {
            graphics.setColor(gauge.getFontColor());
        }
        graphics.drawRect((((int) ((gauge.getWidth() - (gauge.getBorderWidth() * 2)) * dAbs)) + gauge.getBorderWidth()) - (i3 / 2), 0, i3, gauge.getHeight());
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.HorizontalBarPainter, com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return "Horizontal Line Gauge";
    }
}
