package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/RoundAnalogGaugePainter.class */
public class RoundAnalogGaugePainter extends AnalogGaugePainter {
    String name = "Circle Analog Gauge";

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected int getGaugeWidth(Gauge gauge) {
        return getGaugeSize(gauge);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected int getGaugeHeight(Gauge gauge) {
        return getGaugeSize(gauge);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return this.name;
    }

    protected int getGaugeSize(Gauge gauge) {
        return gauge.getHeight() > gauge.getWidth() ? gauge.getWidth() : gauge.getHeight();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected int getXOffset(Gauge gauge) {
        int gaugeSize = getGaugeSize(gauge);
        if (gauge.getWidth() > gaugeSize) {
            return (gauge.getWidth() - gaugeSize) / 2;
        }
        return 0;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected int getYOffset(Gauge gauge) {
        int gaugeSize = getGaugeSize(gauge);
        if (gauge.getHeight() > gaugeSize) {
            return (gauge.getHeight() - gaugeSize) / 2;
        }
        return 0;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public boolean isShapeLockedToAspect() {
        return true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected void drawWarningBars(Graphics2D graphics2D, Gauge gauge) {
        int gaugeSize = (int) (getGaugeSize(gauge) * 0.033d);
        int iRound = (int) Math.round(getAngleFromValue(gauge, gauge.highWarning()));
        int iRound2 = (int) Math.round(getAngleFromValue(gauge, gauge.highCritical()));
        int iRound3 = (int) Math.round(getAngleFromValue(gauge, gauge.max()));
        int iRound4 = (int) Math.round(getAngleFromValue(gauge, gauge.lowWarning()));
        int iRound5 = gauge.lowCritical() < gauge.min() ? (int) Math.round(getAngleFromValue(gauge, gauge.min())) : (int) Math.round(getAngleFromValue(gauge, gauge.lowCritical()));
        int iRound6 = (int) Math.round(getAngleFromValue(gauge, gauge.min()));
        int borderWidth = gauge.getBorderWidth() + (gaugeSize / 2) + (gaugeSize / 2);
        graphics2D.setStroke(new BasicStroke(gaugeSize, 0, 0));
        if (gauge.highWarning() < gauge.max()) {
            graphics2D.setColor(gauge.getWarnColor());
            graphics2D.drawArc(borderWidth, borderWidth, getGaugeWidth(gauge) - (2 * borderWidth), getGaugeHeight(gauge) - (2 * borderWidth), iRound, iRound2 - iRound);
        }
        if (gauge.highCritical() < gauge.max()) {
            graphics2D.setColor(gauge.getCriticalColor());
            graphics2D.drawArc(borderWidth, borderWidth, getGaugeWidth(gauge) - (2 * borderWidth), getGaugeHeight(gauge) - (2 * borderWidth), iRound2, iRound3 - iRound2);
        }
        if (gauge.lowWarning() > gauge.min()) {
            graphics2D.setColor(gauge.getWarnColor());
            graphics2D.drawArc(borderWidth, borderWidth, getGaugeWidth(gauge) - (2 * borderWidth), getGaugeHeight(gauge) - (2 * borderWidth), iRound5, iRound4 - iRound5);
        }
        if (gauge.lowCritical() > gauge.min()) {
            graphics2D.setColor(gauge.getCriticalColor());
            graphics2D.drawArc(borderWidth, borderWidth, getGaugeWidth(gauge) - (2 * borderWidth), getGaugeHeight(gauge) - (2 * borderWidth), iRound5, iRound6 - iRound5);
        }
    }
}
