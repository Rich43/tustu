package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/AnalogMovingBarGaugePainter.class */
public class AnalogMovingBarGaugePainter extends RoundAnalogGaugePainter {
    static String NAME = "Analog Moving Bar Gauge";
    private g needleStroke = null;

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.RoundAnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return NAME;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected Image paintBackground(Graphics graphics, Gauge gauge, int i2) {
        Image imagePaintBackground = super.paintBackground(graphics, gauge, currentState(gauge));
        Graphics2D graphics2D = (Graphics2D) imagePaintBackground.getGraphics();
        if (parentGauge() == null) {
            setParentGauge(gauge);
        }
        double sweepAngle = gauge.getSweepAngle();
        int iRound = Math.round(getNeedleWidth(gauge));
        int borderWidth = gauge.getBorderWidth() + (iRound / 2);
        int borderWidth2 = gauge.getBorderWidth() + (iRound / 2);
        if (gauge.isAntialiasingOn()) {
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        graphics2D.setStroke(getNeedleStroke(gauge));
        graphics2D.setColor(gauge.getTrimColor());
        if (gauge.isCounterClockwise()) {
            graphics2D.drawArc(borderWidth, borderWidth2, (getGaugeWidth(gauge) - iRound) - (2 * gauge.getBorderWidth()), (getGaugeHeight(gauge) - iRound) - (2 * gauge.getBorderWidth()), (180 - gauge.getSweepBeginDegree()) - gauge.getSweepAngle(), (int) Math.round(sweepAngle));
        } else {
            graphics2D.drawArc(borderWidth, borderWidth2, (getGaugeWidth(gauge) - iRound) - (2 * gauge.getBorderWidth()), (getGaugeHeight(gauge) - iRound) - (2 * gauge.getBorderWidth()), gauge.getSweepBeginDegree() + gauge.getSweepAngle(), 0 - ((int) Math.round(sweepAngle)));
        }
        return imagePaintBackground;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected void drawWarningIndicator(Graphics graphics, Gauge gauge) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.RoundAnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected void drawWarningBars(Graphics2D graphics2D, Gauge gauge) {
    }

    protected Color getNeedleColor(Gauge gauge) {
        return getNeedleColor(gauge, gauge.getValue());
    }

    protected Color getNeedleColor(Gauge gauge, double d2) {
        return (d2 <= gauge.lowWarning() || d2 >= gauge.highWarning()) ? d2 > gauge.highCritical() ? gauge.getCriticalColor() : gauge.getWarnColor() : gauge.getNeedleColor();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected void drawStrings(Graphics graphics, Gauge gauge) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected double getOuterEdge(Gauge gauge) {
        return gauge.getWidth() > gauge.getHeight() ? (0.95d - getNeedleWidthPercent()) - ((2.0d * gauge.getBorderWidth()) / getGaugeHeight(gauge)) : (0.95d - getNeedleWidthPercent()) - ((2.0d * gauge.getBorderWidth()) / getGaugeWidth(gauge));
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected double getNumberOfMinorTickPerMajor(Gauge gauge) {
        return 1.0d;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected void drawNeedle(Graphics graphics, Gauge gauge, double d2) {
        double smoothedValue = (getSmoothedValue(gauge) - gauge.min()) / (gauge.max() - gauge.min());
        double sweepAngle = gauge.getSweepAngle() * smoothedValue;
        double historicalPeakValue = (gauge.getHistoricalPeakValue() - gauge.min()) / (gauge.max() - gauge.min());
        double sweepAngle2 = gauge.getSweepAngle() * historicalPeakValue;
        if (sweepAngle < 1.0d) {
            sweepAngle = 1.0d;
        }
        int iRound = Math.round(getNeedleWidth(gauge));
        int width = ((gauge.getWidth() - getGaugeWidth(gauge)) / 2) + gauge.getBorderWidth() + (iRound / 2);
        int height = ((gauge.getHeight() - getGaugeHeight(gauge)) / 2) + gauge.getBorderWidth() + (iRound / 2);
        ((Graphics2D) graphics).setStroke(getNeedleStroke(gauge));
        graphics.setColor(getNeedleColor(gauge));
        if (gauge.isCounterClockwise()) {
            graphics.drawArc(width, height, (getGaugeWidth(gauge) - iRound) - (2 * gauge.getBorderWidth()), (getGaugeHeight(gauge) - iRound) - (2 * gauge.getBorderWidth()), (180 - gauge.getSweepBeginDegree()) - gauge.getSweepAngle(), (int) Math.round(sweepAngle));
            if (historicalPeakValue - smoothedValue > 0.01d) {
                graphics.setColor(getNeedleColor(gauge, gauge.getHistoricalPeakValue()));
                graphics.drawArc(width, height, (getGaugeWidth(gauge) - iRound) - (2 * gauge.getBorderWidth()), (getGaugeHeight(gauge) - iRound) - (2 * gauge.getBorderWidth()), ((180 - gauge.getSweepBeginDegree()) - gauge.getSweepAngle()) + ((int) Math.round(sweepAngle2)), 1);
                return;
            }
            return;
        }
        graphics.drawArc(width, height, (getGaugeWidth(gauge) - iRound) - (2 * gauge.getBorderWidth()), (getGaugeHeight(gauge) - iRound) - (2 * gauge.getBorderWidth()), gauge.getSweepBeginDegree() + gauge.getSweepAngle(), 0 - ((int) Math.round(sweepAngle)));
        if (historicalPeakValue - smoothedValue > 0.01d) {
            graphics.setColor(getNeedleColor(gauge, gauge.getHistoricalPeakValue()));
            graphics.drawArc(width, height, (getGaugeWidth(gauge) - iRound) - (2 * gauge.getBorderWidth()), (getGaugeHeight(gauge) - iRound) - (2 * gauge.getBorderWidth()), (gauge.getSweepBeginDegree() + gauge.getSweepAngle()) - ((int) Math.round(sweepAngle2)), 1);
        }
    }

    public float getNeedleWidthPercent() {
        return 0.07f;
    }

    public float getNeedleWidth(Gauge gauge) {
        return getGaugeSize(gauge) * getNeedleWidthPercent();
    }

    public Stroke getNeedleStroke(Gauge gauge) {
        if (this.needleStroke == null) {
            this.needleStroke = new g(getNeedleWidth(gauge), 0, 2);
        }
        return this.needleStroke;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
        super.invalidate();
        this.needleStroke = null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.RoundAnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public boolean isShapeLockedToAspect() {
        return true;
    }
}
