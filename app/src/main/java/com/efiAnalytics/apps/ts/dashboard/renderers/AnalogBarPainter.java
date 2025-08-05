package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/AnalogBarPainter.class */
public class AnalogBarPainter extends RoundAnalogGaugePainter {
    static String NAME = "Analog Bar Gauge";
    private g needleStroke = null;

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.RoundAnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter, com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return NAME;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.AnalogGaugePainter
    protected Image paintBackground(Graphics graphics, Gauge gauge, int i2) {
        if (parentGauge() == null) {
            setParentGauge(gauge);
        }
        Image imagePaintBackground = super.paintBackground(graphics, gauge, currentState(gauge));
        Graphics2D graphics2D = (Graphics2D) imagePaintBackground.getGraphics();
        double sweepAngle = gauge.getSweepAngle();
        int iRound = Math.round(getNeedleWidth(gauge));
        int borderWidth = gauge.getBorderWidth() + (iRound / 2);
        int borderWidth2 = gauge.getBorderWidth() + (iRound / 2);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(getNeedleStroke(gauge));
        graphics2D.setColor(gauge.getFontColor());
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
        double value = (gauge.getValue() - gauge.min()) / (gauge.max() - gauge.min());
        double d3 = d2;
        if (d3 < 1.0d) {
            d3 = 1.0d;
        }
        graphics.setColor(getNeedleColor(gauge));
        graphics.fillPolygon(createNeedlePolygon(gauge, d3));
        if (gauge.isShowHistory()) {
            double historicalPeakValue = (gauge.getHistoricalPeakValue() - gauge.min()) / (gauge.max() - gauge.min());
            double sweepBeginDegree = (gauge.getSweepBeginDegree() + gauge.getSweepAngle()) - (gauge.getSweepAngle() * historicalPeakValue);
            if (historicalPeakValue - value > 0.01d) {
                graphics.setColor(getNeedleColor(gauge, gauge.getHistoricalPeakValue()));
                graphics.drawPolygon(createNeedlePolygon(gauge, sweepBeginDegree));
            }
        }
    }

    protected Polygon createNeedlePolygon(Gauge gauge, double d2) {
        int gaugeSize = getGaugeSize(gauge);
        double borderWidth = 1.005d - ((2.0d * gauge.getBorderWidth()) / gaugeSize);
        Polygon polygon = new Polygon();
        Point point = getPoint(gaugeSize, gaugeSize, d2 - 4.0d, gauge.getWidth() / 2, gauge.getHeight() / 2, borderWidth, gauge.isCounterClockwise());
        Point point2 = getPoint(gaugeSize, gaugeSize, d2 + 4.0d, gauge.getWidth() / 2, gauge.getHeight() / 2, borderWidth, gauge.isCounterClockwise());
        Point point3 = getPoint(gaugeSize, gaugeSize, d2 + 1.0d, gauge.getWidth() / 2, gauge.getHeight() / 2, getOuterEdge(gauge) + 0.02d, gauge.isCounterClockwise());
        Point point4 = getPoint(gaugeSize, gaugeSize, d2 - 1.0d, gauge.getWidth() / 2, gauge.getHeight() / 2, getOuterEdge(gauge) + 0.02d, gauge.isCounterClockwise());
        polygon.addPoint(point.f12370x, point.f12371y);
        polygon.addPoint(point2.f12370x, point2.f12371y);
        polygon.addPoint(point3.f12370x, point3.f12371y);
        polygon.addPoint(point4.f12370x, point4.f12371y);
        return polygon;
    }

    public float getNeedleWidthPercent() {
        return 0.09f;
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
