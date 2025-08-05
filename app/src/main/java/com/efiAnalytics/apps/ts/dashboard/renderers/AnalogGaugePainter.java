package com.efiAnalytics.apps.ts.dashboard.renderers;

import bH.W;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.aO;
import com.efiAnalytics.ui.C1606cq;
import com.efiAnalytics.ui.eJ;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/AnalogGaugePainter.class */
public class AnalogGaugePainter implements aO, GaugePainter {
    private final String name = "Analog Gauge";
    transient Polygon needleShape = null;
    transient Polygon historyNeedleShape = null;
    int lastWidth = 0;
    int lastHeight = 0;
    double lastValue = 0.0d;
    double lastHistoryValue = 0.0d;
    int counter = 0;
    int maxMajorTicks = 12;
    int maxFaceNumber = 999;
    int digitsTrimmed = 0;
    transient Image normalOffImage = null;
    transient Image criticalOffImage = null;
    transient Image warnOffImage = null;
    transient long lastPaintTime = System.nanoTime();
    boolean lastPaintColored = false;
    private Gauge parentGauge = null;
    private transient GradientPaint outerGradient = null;
    private transient GradientPaint innerGradient = null;
    private int limitRattle = 0;
    int lastBgState = -1;
    g needleStroke = new g(eJ.a(3), 1, 1);
    g historyNeedleStroke = new g(eJ.a(1), 1, 1);

    private Image getOffImage(Graphics graphics, Gauge gauge) {
        int iCurrentState = currentState(gauge);
        this.lastBgState = iCurrentState;
        if (iCurrentState == 0) {
            if (this.normalOffImage == null) {
                this.normalOffImage = paintBackground(graphics, gauge, iCurrentState);
            }
            return this.normalOffImage;
        }
        if (iCurrentState == 2) {
            if (this.criticalOffImage == null) {
                this.criticalOffImage = paintBackground(graphics, gauge, iCurrentState);
            }
            return this.criticalOffImage;
        }
        if (this.warnOffImage == null) {
            this.warnOffImage = paintBackground(graphics, gauge, iCurrentState);
        }
        return this.warnOffImage;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        if (parentGauge() == null) {
            setParentGauge(gauge);
        }
        this.lastPaintTime = System.nanoTime();
        if (gauge.isAntialiasingOn()) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        graphics.drawImage(getOffImage(graphics, gauge), getXOffset(gauge), getYOffset(gauge), null);
        drawStrings(graphics, gauge);
        drawWarningIndicator(graphics, gauge);
        double smoothedValue = (getSmoothedValue(gauge) - gauge.min()) / (gauge.max() - gauge.min());
        if (gauge.isPegLimits() && smoothedValue > 1.02d) {
            int i2 = this.limitRattle;
            this.limitRattle = i2 + 1;
            smoothedValue = i2 % 2 == 0 ? 1.02d : 1.015d;
        } else if (gauge.isPegLimits() && smoothedValue < -0.02d) {
            int i3 = this.limitRattle;
            this.limitRattle = i3 + 1;
            smoothedValue = i3 % 2 == 0 ? -0.02d : -0.015d;
        }
        double sweepBeginDegree = (gauge.getSweepBeginDegree() + gauge.getSweepAngle()) - (gauge.getSweepAngle() * smoothedValue);
        graphics.setColor(gauge.getNeedleColor());
        if (gauge.max() > gauge.min()) {
            drawNeedle(graphics, gauge, sweepBeginDegree);
        }
        this.lastWidth = getGaugeWidth(gauge);
        this.lastHeight = getGaugeHeight(gauge);
    }

    protected double getSmoothedValue(Gauge gauge) {
        return gauge.getSmoothedValue();
    }

    protected Color getFaceColor(Gauge gauge, int i2) {
        return i2 == 2 ? gauge.getCriticalColor() : gauge.getBackColor();
    }

    protected double getMajorTickSpacing(Gauge gauge) {
        double majorTicks = gauge.getMajorTicks();
        if (majorTicks <= 0.0d) {
            majorTicks = Math.pow(10.0d, (int) (Math.round(Math.log10(gauge.max() - gauge.min())) - 1));
        }
        double dMax = gauge.max();
        double dMin = gauge.min();
        while (true) {
            int i2 = (int) ((dMax - dMin) / majorTicks);
            if (i2 <= 0 || gauge.getSweepAngle() / i2 >= 18) {
                break;
            }
            majorTicks *= 2.0d;
            dMax = gauge.max();
            dMin = gauge.min();
        }
        return majorTicks;
    }

    protected void drawStrings(Graphics graphics, Gauge gauge) {
        int gaugeHeight = ((getGaugeHeight(gauge) - (2 * gauge.getBorderWidth())) / 12) + gauge.getFontSizeAdjustment();
        String fontFamily = gauge.getFontFamily();
        if (!gauge.isDisplayValueAt180()) {
            Font fontA = C1606cq.a().a(fontFamily, gauge.isItalicFont() ? 2 : 0, gaugeHeight);
            if (fontA.canDisplayUpTo(gauge.title()) >= 0) {
                fontA = C1606cq.a().a("", gauge.isItalicFont() ? 3 : 1, gaugeHeight);
                fontFamily = "";
            }
            boolean z2 = false;
            int i2 = gauge.isItalicFont() ? 2 : 0;
            while (graphics.getFontMetrics(fontA).stringWidth(gauge.title()) > getGaugeWidth(gauge) * 0.5d) {
                gaugeHeight = (int) (gaugeHeight * 0.8d);
                fontA = C1606cq.a().a(fontFamily, i2, gaugeHeight);
                z2 = true;
            }
            Font fontA2 = z2 ? fontA : C1606cq.a().a(fontFamily, i2, (int) (gaugeHeight * 0.85d));
            graphics.setFont(fontA2);
            graphics.setColor(gauge.getFontColor());
            String displayValue = gauge.units().equals("") ? gauge.getDisplayValue() : gauge.getDisplayValue() + "(" + gauge.units() + ")";
            Point point = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), gauge.getSweepBeginDegree() + (gauge.getSweepAngle() / 2), getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, (getOuterEdge(gauge) - 0.6d) + (Double.isNaN(gauge.getRelativeBorderWidth2()) ? 0.0d : gauge.getRelativeBorderWidth2()), gauge.isCounterClockwise());
            int iStringWidth = point.f12370x - (graphics.getFontMetrics().stringWidth(displayValue) / 2);
            int i3 = point.f12371y;
            graphics.drawString(displayValue, iStringWidth + getXOffset(gauge), i3 + getYOffset(gauge));
            graphics.setFont(fontA);
            graphics.drawString(gauge.title(), (point.f12370x - (graphics.getFontMetrics().stringWidth(gauge.title()) / 2)) + getXOffset(gauge), i3 + ((int) (1.2d * fontA.getSize())) + getYOffset(gauge));
            graphics.setFont(fontA2);
            if (this.digitsTrimmed <= 0 || gauge.max() <= this.maxFaceNumber) {
                return;
            }
            String str = "x1";
            for (int i4 = 0; i4 < this.digitsTrimmed; i4++) {
                str = str + "0";
            }
            graphics.setFont(C1606cq.a().a(fontFamily, gauge.isItalicFont() ? 2 : 0, (int) (gaugeHeight * 0.6d)));
            graphics.drawString(str, (point.f12370x - (graphics.getFontMetrics().stringWidth(str) / 2)) + getXOffset(gauge), (point.f12371y - ((int) (fontA2.getSize() * 1.3d))) + getYOffset(gauge));
            return;
        }
        int i5 = gauge.isItalicFont() ? 2 : 0;
        Font fontA3 = C1606cq.a().a(fontFamily, i5, (int) (gaugeHeight * 0.85d));
        if (fontA3.canDisplayUpTo(gauge.title()) >= 0) {
            i5 = gauge.isItalicFont() ? 3 : 1;
            fontA3 = C1606cq.a().a("", i5, gaugeHeight);
            fontFamily = "";
        }
        boolean z3 = false;
        String strTitle = gauge.title();
        String strSubstring = null;
        int sweepBeginDegree = ((gauge.getSweepBeginDegree() + (gauge.getSweepAngle() / 2)) + 180) % 360;
        if (strTitle.length() > 13 && strTitle.indexOf(" ", strTitle.length() / 3) != -1) {
            strSubstring = strTitle.substring(strTitle.indexOf(" ", strTitle.length() / 3) + 1);
            strTitle = strTitle.substring(0, strTitle.indexOf(" ", strTitle.length() / 3));
        }
        while (graphics.getFontMetrics(fontA3).stringWidth(strTitle) > getGaugeWidth(gauge) * 0.5d) {
            gaugeHeight = (int) (gaugeHeight * 0.9d);
            fontA3 = C1606cq.a().a(fontFamily, i5, gaugeHeight);
            z3 = true;
        }
        Font fontA4 = z3 ? fontA3 : C1606cq.a().a(fontFamily, i5, (int) (gaugeHeight * 0.85d));
        graphics.setFont(fontA4);
        graphics.setColor(gauge.getFontColor());
        graphics.setFont(C1606cq.a().a(fontFamily, i5, ((getGaugeHeight(gauge) - (2 * gauge.getBorderWidth())) / 14) + gauge.getFontSizeAdjustment()));
        String displayValue2 = gauge.getDisplayValue();
        double relativeBorderWidth2 = Double.isNaN(gauge.getRelativeBorderWidth2()) ? 0.0d : gauge.getRelativeBorderWidth2();
        Point point2 = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), sweepBeginDegree, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, (getOuterEdge(gauge) - 0.55d) + relativeBorderWidth2, gauge.isCounterClockwise());
        int iStringWidth2 = point2.f12370x - (graphics.getFontMetrics().stringWidth(displayValue2) / 2);
        int i6 = point2.f12371y;
        graphics.drawString(displayValue2, iStringWidth2 + getXOffset(gauge), i6 + getYOffset(gauge));
        if (gauge.units() != null && !gauge.units().equals("")) {
            graphics.drawString(gauge.units(), (point2.f12370x - (graphics.getFontMetrics().stringWidth(gauge.units()) / 2)) + getXOffset(gauge), i6 + ((int) (r0.getSize() * 1.1d)) + getYOffset(gauge));
        }
        if (strSubstring == null) {
            Point point3 = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), gauge.getSweepBeginDegree() + (gauge.getSweepAngle() / 2), getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, (getOuterEdge(gauge) - 0.66d) + relativeBorderWidth2, gauge.isCounterClockwise());
            graphics.setFont(fontA3);
            graphics.drawString(gauge.title(), (point3.f12370x - (graphics.getFontMetrics().stringWidth(gauge.title()) / 2)) + getXOffset(gauge), point3.f12371y + getYOffset(gauge));
            graphics.setFont(fontA4);
            if (this.digitsTrimmed <= 0 || gauge.max() <= this.maxFaceNumber) {
                return;
            }
            String str2 = "x1";
            for (int i7 = 0; i7 < this.digitsTrimmed; i7++) {
                str2 = str2 + "0";
            }
            graphics.setFont(C1606cq.a().a(fontFamily, i5, (int) (gaugeHeight * 0.6d)));
            graphics.drawString(str2, (point3.f12370x - (graphics.getFontMetrics().stringWidth(str2) / 2)) + getXOffset(gauge), (point3.f12371y - ((int) (fontA4.getSize() * 1.3d))) + getYOffset(gauge));
            return;
        }
        int gaugeHeight2 = getGaugeHeight(gauge);
        Point point4 = getPoint(getGaugeWidth(gauge), gaugeHeight2, gauge.getSweepBeginDegree() + (gauge.getSweepAngle() / 2), gaugeHeight2 / 2, gaugeHeight2 / 2, (getOuterEdge(gauge) - 0.6d) + relativeBorderWidth2, gauge.isCounterClockwise());
        graphics.setFont(fontA3);
        int iStringWidth3 = point4.f12370x - (graphics.getFontMetrics().stringWidth(strTitle) / 2);
        int i8 = point4.f12371y;
        graphics.drawString(strTitle, iStringWidth3 + getXOffset(gauge), i8 + getYOffset(gauge));
        graphics.drawString(strSubstring, (point4.f12370x - (graphics.getFontMetrics().stringWidth(strSubstring) / 2)) + getXOffset(gauge), i8 + ((int) (fontA3.getSize() * 1.1d)) + getYOffset(gauge));
        graphics.setFont(fontA4);
        if (this.digitsTrimmed <= 0 || gauge.max() <= this.maxFaceNumber) {
            return;
        }
        String str3 = "x1";
        for (int i9 = 0; i9 < this.digitsTrimmed; i9++) {
            str3 = str3 + "0";
        }
        graphics.setFont(C1606cq.a().a(fontFamily, i5, (int) (gaugeHeight * 0.6d)));
        graphics.drawString(str3, (point4.f12370x - (graphics.getFontMetrics().stringWidth(str3) / 2)) + getXOffset(gauge), (point4.f12371y - ((int) (fontA4.getSize() * 1.3d))) + getYOffset(gauge));
    }

    protected int getXOffset(Gauge gauge) {
        return 0;
    }

    protected int getYOffset(Gauge gauge) {
        return 0;
    }

    protected double getAngleOffsetToMajorTick(Gauge gauge) {
        return -(gauge.getSweepAngle() * ((getMinMajorTickValue(gauge) - gauge.min()) / (gauge.max() - gauge.min())));
    }

    protected double getMinMajorTickValue(Gauge gauge) {
        return gauge.max() > 2.0d ? Math.round(gauge.min()) : Math.round(gauge.min() * 10.0d) / 10.0d;
    }

    protected void drawNumbers(Graphics graphics, Gauge gauge) {
        double angleOffsetToMajorTick = getAngleOffsetToMajorTick(gauge);
        double outerEdge = getOuterEdge(gauge);
        double majorTickSpacing = getMajorTickSpacing(gauge);
        double dMax = (gauge.max() - gauge.min()) / majorTickSpacing;
        double sweepAngle = gauge.getSweepAngle() / dMax;
        if (angleOffsetToMajorTick < 0.0d) {
            dMax -= 1.0d;
        }
        graphics.setColor(gauge.getFontColor());
        graphics.setFont(C1606cq.a().a(gauge.getFontFamily(), gauge.isItalicFont() ? 2 : 0, ((getGaugeHeight(gauge) - (2 * gauge.getBorderWidth())) / 14) + gauge.getFontSizeAdjustment()));
        this.digitsTrimmed = 10;
        for (int i2 = 0; i2 <= dMax; i2++) {
            double sweepBeginDegree = ((angleOffsetToMajorTick + gauge.getSweepBeginDegree()) + gauge.getSweepAngle()) - (sweepAngle * i2);
            String strB = gauge.max() > 2.0d ? W.b(gauge.min() + (i2 * majorTickSpacing), 0) : W.b(gauge.min() + (i2 * majorTickSpacing), 1);
            if (gauge.max() > this.maxFaceNumber) {
                if (i2 == 1) {
                    int length = strB.length();
                    strB = trimRightZeros(strB, this.digitsTrimmed);
                    this.digitsTrimmed = length - strB.length();
                } else if (i2 > 1 && this.digitsTrimmed > 0) {
                    strB = trimRightZeros(strB, this.digitsTrimmed);
                }
            }
            Point point = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), sweepBeginDegree, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, outerEdge - (0.14d + (strB.length() * 0.02d)), gauge.isCounterClockwise());
            graphics.drawString(strB, point.f12370x - (graphics.getFontMetrics().stringWidth(strB) / 2), point.f12371y + (graphics.getFont().getSize() / 2));
        }
    }

    protected int getGaugeWidth(Gauge gauge) {
        return gauge.getWidth();
    }

    protected int getGaugeHeight(Gauge gauge) {
        return gauge.getHeight();
    }

    private String trimRightZeros(String str, int i2) {
        for (int i3 = 0; i3 < i2 && str.endsWith("0"); i3++) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    protected double getOuterEdge(Gauge gauge) {
        return 0.983d - ((2.0d * gauge.getBorderWidth()) / getGaugeWidth(gauge));
    }

    protected double getNumberOfMinorTickPerMajor(Gauge gauge) {
        if (gauge.getHeight() > 450 || gauge.getWidth() > 450) {
            return 5.0d;
        }
        if (gauge.getHeight() > 325) {
            return 4.0d;
        }
        return (gauge.getHeight() > 175 || gauge.getWidth() > 175) ? 2.0d : 1.0d;
    }

    protected void drawTickMarks(Graphics graphics, Gauge gauge) {
        double angleOffsetToMajorTick = getAngleOffsetToMajorTick(gauge);
        double numberOfMinorTickPerMajor = getNumberOfMinorTickPerMajor(gauge);
        double outerEdge = getOuterEdge(gauge);
        double dMax = (gauge.max() - gauge.min()) / getMajorTickSpacing(gauge);
        double sweepAngle = gauge.getSweepAngle() / dMax;
        if (angleOffsetToMajorTick < 0.0d) {
            dMax -= 1.0d;
        }
        double d2 = dMax * numberOfMinorTickPerMajor;
        double d3 = sweepAngle / numberOfMinorTickPerMajor;
        graphics.setColor(gauge.getFontColor());
        ((Graphics2D) graphics).setStroke(new BasicStroke(2.0f));
        for (int i2 = 0; i2 <= d2; i2++) {
            if (i2 % numberOfMinorTickPerMajor > 0.0d) {
                double sweepBeginDegree = ((angleOffsetToMajorTick + gauge.getSweepBeginDegree()) + gauge.getSweepAngle()) - (d3 * i2);
                Point point = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), sweepBeginDegree, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, outerEdge - 0.01d, gauge.isCounterClockwise());
                Point point2 = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), sweepBeginDegree, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, outerEdge - 0.04d, gauge.isCounterClockwise());
                graphics.drawLine(point.f12370x, point.f12371y, point2.f12370x, point2.f12371y);
            }
        }
        double d4 = outerEdge - 0.07d;
        graphics.setColor(gauge.getFontColor());
        ((Graphics2D) graphics).setStroke(new BasicStroke(1.0f));
        for (int i3 = 0; i3 <= dMax; i3++) {
            double sweepBeginDegree2 = ((angleOffsetToMajorTick + gauge.getSweepBeginDegree()) + gauge.getSweepAngle()) - (sweepAngle * i3);
            Point point3 = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), sweepBeginDegree2 - 0.6d, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, outerEdge, gauge.isCounterClockwise());
            Point point4 = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), sweepBeginDegree2 - 0.6d, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, d4, gauge.isCounterClockwise());
            Point point5 = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), sweepBeginDegree2 + 0.6d, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, outerEdge, gauge.isCounterClockwise());
            Point point6 = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), sweepBeginDegree2 + 0.6d, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, d4, gauge.isCounterClockwise());
            Polygon polygon = new Polygon();
            polygon.addPoint(point4.f12370x, point4.f12371y);
            polygon.addPoint(point3.f12370x, point3.f12371y);
            polygon.addPoint(point5.f12370x, point5.f12371y);
            polygon.addPoint(point6.f12370x, point6.f12371y);
            graphics.fillPolygon(polygon);
        }
    }

    protected void drawWarningBars(Graphics2D graphics2D, Gauge gauge) {
    }

    protected double getAngleFromValue(Gauge gauge, double d2) {
        double sweepBeginDegree = (gauge.getSweepBeginDegree() + gauge.getSweepAngle()) - (gauge.getSweepAngle() * ((d2 - gauge.min()) / (gauge.max() - gauge.min())));
        return gauge.isCounterClockwise() ? 180.0d - sweepBeginDegree : sweepBeginDegree;
    }

    protected void drawWarningIndicator(Graphics graphics, Gauge gauge) {
        if (gauge.getValue() <= gauge.lowWarning() || gauge.getValue() >= gauge.highWarning()) {
            float gaugeHeight = ((getGaugeHeight(gauge) - (2.0f * gauge.getBorderWidth())) + 1.0f) / 8.0f;
            graphics.setColor(gauge.getWarnColor());
            graphics.fillArc(Math.round(((getGaugeWidth(gauge) - gaugeHeight) / 2.0f) + getXOffset(gauge)), Math.round(((getGaugeHeight(gauge) - gaugeHeight) / 2.0f) + getYOffset(gauge)) - 1, Math.round(gaugeHeight), Math.round(gaugeHeight), gauge.getStartAngle(), gauge.getFaceAngle());
        }
    }

    private Polygon getNeedleShape(Gauge gauge, double d2) {
        double outerEdge = getOuterEdge(gauge);
        double d3 = gauge.isCounterClockwise() ? 180.0d - d2 : d2;
        int gaugeWidth = getGaugeWidth(gauge) / 2;
        int gaugeHeight = getGaugeHeight(gauge) / 2;
        int borderWidth = getGaugeWidth(gauge) > getGaugeHeight(gauge) ? gaugeHeight - (2 * gauge.getBorderWidth()) : gaugeWidth - (2 * gauge.getBorderWidth());
        Point point = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), d2 - 0.25d, gaugeWidth, gaugeHeight, outerEdge - 0.07d, gauge.isCounterClockwise());
        Point point2 = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), d2 + 0.25d, gaugeWidth, gaugeHeight, outerEdge - 0.07d, gauge.isCounterClockwise());
        Point point3 = getPoint((borderWidth / 7) + 1, (d3 + 164.0d) % 360.0d, gaugeWidth, gaugeHeight);
        Point point4 = getPoint(borderWidth / 5, (d3 - 180.0d) % 360.0d, gaugeWidth, gaugeHeight);
        Point point5 = getPoint((borderWidth / 7) + 1, (d3 - 164.0d) % 360.0d, gaugeWidth, gaugeHeight);
        if (gauge.isCounterClockwise()) {
            point5 = point3;
            point3 = point5;
        }
        this.needleShape = new Polygon();
        this.needleShape.addPoint(point.f12370x + getXOffset(gauge), point.f12371y + getYOffset(gauge));
        this.needleShape.addPoint(point2.f12370x + getXOffset(gauge), point2.f12371y + getYOffset(gauge));
        this.needleShape.addPoint(point3.f12370x + getXOffset(gauge), point3.f12371y + getYOffset(gauge));
        this.needleShape.addPoint(point4.f12370x + getXOffset(gauge), point4.f12371y + getYOffset(gauge));
        this.needleShape.addPoint(point5.f12370x + getXOffset(gauge), point5.f12371y + getYOffset(gauge));
        return this.needleShape;
    }

    protected void drawNeedle(Graphics graphics, Gauge gauge, double d2) {
        int gaugeWidth = getGaugeWidth(gauge) / 2;
        int gaugeHeight = getGaugeHeight(gauge) / 2;
        int iRound = Math.round((gauge.getShortestSize() - (2 * gauge.getBorderWidth())) * 0.955f);
        if (gauge.needleImage(iRound) == null || iRound <= 0) {
            this.needleShape = getNeedleShape(gauge, d2);
            graphics.fillPolygon(this.needleShape);
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setStroke(this.needleStroke);
            if (gauge.isShowHistory()) {
                if (this.lastHistoryValue != gauge.getHistoricalPeakValue() || this.historyNeedleShape == null) {
                    double angleFromValue = getAngleFromValue(gauge, gauge.getHistoricalPeakValue());
                    this.historyNeedleShape = getNeedleShape(gauge, gauge.isCounterClockwise() ? 180.0d - angleFromValue : angleFromValue);
                    this.lastHistoryValue = gauge.getHistoricalPeakValue();
                }
                if ((gauge.getHistoricalPeakValue() - gauge.getValue()) / (gauge.max() - gauge.min()) > 0.008d) {
                    graphics.setColor(gauge.getNeedleColor());
                    graphics2D.setStroke(this.historyNeedleStroke);
                    graphics.drawPolygon(this.historyNeedleShape);
                    graphics2D.setStroke(this.needleStroke);
                }
            }
            Color needleColor = gauge.getNeedleColor();
            graphics.setColor((needleColor.getRed() + needleColor.getGreen()) + needleColor.getBlue() > 384 ? needleColor.darker() : needleColor.brighter().brighter());
            Point point = getPoint(getGaugeWidth(gauge), getGaugeHeight(gauge), d2, gaugeWidth, gaugeHeight, getOuterEdge(gauge) - 0.05d, gauge.isCounterClockwise());
            graphics.drawLine(gaugeWidth + getXOffset(gauge), gaugeHeight + getYOffset(gauge), point.f12370x + getXOffset(gauge), point.f12371y + getYOffset(gauge));
            return;
        }
        Image imageNeedleImage = gauge.needleImage(iRound);
        double d3 = gauge.isCounterClockwise() ? 180.0d - d2 : d2;
        double d4 = gauge.isCounterClockwise() ? ((((-90.0d) + d3) * 2.0d) * 3.141592653589793d) / 360.0d : (((d3 - 90.0d) * 2.0d) * 3.141592653589793d) / 360.0d;
        Graphics2D graphics2D2 = (Graphics2D) graphics;
        graphics2D2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (gauge.isShowHistory()) {
            if (this.lastHistoryValue != gauge.getHistoricalPeakValue() || this.historyNeedleShape == null) {
                double angleFromValue2 = getAngleFromValue(gauge, gauge.getHistoricalPeakValue());
                this.historyNeedleShape = getNeedleShape(gauge, gauge.isCounterClockwise() ? 180.0d - angleFromValue2 : angleFromValue2);
                this.lastHistoryValue = gauge.getHistoricalPeakValue();
            }
            if ((gauge.getHistoricalPeakValue() - gauge.getValue()) / (gauge.max() - gauge.min()) > 0.008d && this.historyNeedleStroke != null) {
                graphics.setColor(gauge.getNeedleColor());
                graphics2D2.setStroke(this.historyNeedleStroke);
                graphics.drawPolygon(this.historyNeedleShape);
                graphics2D2.setStroke(this.needleStroke);
            }
        }
        AffineTransform transform = graphics2D2.getTransform();
        AffineTransform affineTransform = (AffineTransform) transform.clone();
        affineTransform.rotate(-d4, gauge.getWidth() / 2, gauge.getHeight() / 2);
        affineTransform.translate((gauge.getWidth() - imageNeedleImage.getWidth(null)) / 2.0f, (gauge.getHeight() - imageNeedleImage.getHeight(null)) / 2.0f);
        graphics2D2.setTransform(affineTransform);
        graphics2D2.drawImage(imageNeedleImage, 0, 0, gauge);
        graphics2D2.setTransform(transform);
    }

    protected Image paintBackground(Graphics graphics, Gauge gauge, int i2) {
        BufferedImage bufferedImageCreateCompatibleImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(getGaugeWidth(gauge), getGaugeHeight(gauge), 3);
        Graphics graphics2 = bufferedImageCreateCompatibleImage.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics2;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Graphics2D graphics2D2 = (Graphics2D) graphics2;
        graphics2.setColor(new Color(0, 0, 0, 0));
        graphics2.fillRect(0, 0, getGaugeWidth(gauge), getGaugeHeight(gauge));
        this.lastPaintTime = System.nanoTime();
        if (i2 == 2) {
            paintBgImage(graphics2D2, gauge);
        }
        graphics2.setColor(gauge.getBackColor());
        graphics2.setColor(getFaceColor(gauge, i2));
        graphics2.fillArc(gauge.getBorderWidth(), gauge.getBorderWidth(), getGaugeWidth(gauge) - (2 * gauge.getBorderWidth()), getGaugeHeight(gauge) - (2 * gauge.getBorderWidth()), gauge.getStartAngle(), gauge.getFaceAngle());
        if (i2 != 2) {
            paintBgImage(graphics2D2, gauge);
        }
        drawWarningBars((Graphics2D) graphics2, gauge);
        drawTickMarks(graphics2, gauge);
        drawBorder(graphics2, gauge);
        if (gauge.max() > gauge.min()) {
            drawNumbers(graphics2, gauge);
        }
        return bufferedImageCreateCompatibleImage;
    }

    protected Color getBrighter(Color color) {
        int blue = color.getBlue() + color.getRed() + color.getGreen();
        if (blue >= 180) {
            return color.brighter();
        }
        int i2 = blue + 250;
        return new Color(i2 / 3, i2 / 3, i2 / 3);
    }

    protected Color getDarker(Color color) {
        return (color.getBlue() + color.getRed()) + color.getGreen() > 650 ? color.darker().darker() : color.darker();
    }

    protected void drawBorder(Graphics graphics, Gauge gauge) {
        Color trimColor = gauge.getTrimColor();
        if (gauge.getBorderWidth() <= 0 || trimColor.getAlpha() <= 0) {
            return;
        }
        Color darker = getDarker(trimColor);
        Color color = new Color(darker.getRed(), darker.getGreen(), darker.getBlue(), trimColor.getAlpha());
        Color brighter = getBrighter(trimColor);
        if (this.outerGradient == null) {
            this.outerGradient = new GradientPaint(0.0f, 92.0f, color, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, brighter, true);
        }
        ((Graphics2D) graphics).setPaint(this.outerGradient);
        ((Graphics2D) graphics).setStroke(new BasicStroke(gauge.getBorderWidth() / 2));
        graphics.drawArc(gauge.getBorderWidth() / 4, gauge.getBorderWidth() / 4, getGaugeWidth(gauge) - (gauge.getBorderWidth() / 2), getGaugeHeight(gauge) - (gauge.getBorderWidth() / 2), gauge.getStartAngle(), gauge.getFaceAngle());
        if (this.innerGradient == null) {
            this.innerGradient = new GradientPaint(92.0f, 0.0f, brighter, getGaugeWidth(gauge) / 2, getGaugeHeight(gauge) / 2, color, true);
        }
        ((Graphics2D) graphics).setPaint(this.innerGradient);
        ((Graphics2D) graphics).setStroke(new BasicStroke((gauge.getBorderWidth() + 4) / 2));
        graphics.drawArc((3 * gauge.getBorderWidth()) / 4, (3 * gauge.getBorderWidth()) / 4, (getGaugeWidth(gauge) - gauge.getBorderWidth()) - (gauge.getBorderWidth() / 2), (getGaugeHeight(gauge) - gauge.getBorderWidth()) - (gauge.getBorderWidth() / 2), gauge.getStartAngle(), gauge.getFaceAngle());
    }

    protected Point getPoint(int i2, double d2, int i3, int i4) {
        double d3 = ((d2 * 2.0d) * 3.141592653589793d) / 360.0d;
        return new Point((int) (i3 + (i2 * Math.cos(d3))), (int) (i4 - (i2 * Math.sin(d3))));
    }

    protected Point getPoint(int i2, int i3, double d2, int i4, int i5, double d3, boolean z2) {
        double d4 = z2 ? (((180.0d - d2) * 2.0d) * 3.141592653589793d) / 360.0d : ((d2 * 2.0d) * 3.141592653589793d) / 360.0d;
        float fSqrt = (float) (Math.sqrt((Math.pow(i2 / 2.0d, 2.0d) * Math.pow(i3 / 2.0d, 2.0d)) / (Math.pow((i2 / 2.0d) * Math.sin(d4), 2.0d) + Math.pow((i3 / 2.0d) * Math.cos(d4), 2.0d))) * d3);
        return new Point(i4 + Math.round((float) (fSqrt * Math.cos(d4))), i5 - Math.round((float) (fSqrt * Math.sin(d4))));
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public boolean requiresBackgroundRepaint(AbstractC1420s abstractC1420s) {
        return (this.lastBgState == currentState((Gauge) abstractC1420s) && abstractC1420s.getWidth() == this.lastWidth && abstractC1420s.getHeight() == this.lastHeight) ? false : true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        if (gauge.isAntialiasingOn()) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        drawStrings(graphics, gauge);
        drawWarningIndicator(graphics, gauge);
        double smoothedValue = (getSmoothedValue(gauge) - gauge.min()) / (gauge.max() - gauge.min());
        if (gauge.isPegLimits() && smoothedValue > 1.02d) {
            int i2 = this.limitRattle;
            this.limitRattle = i2 + 1;
            smoothedValue = i2 % 2 == 0 ? 1.02d : 1.015d;
        } else if (gauge.isPegLimits() && smoothedValue < -0.02d) {
            int i3 = this.limitRattle;
            this.limitRattle = i3 + 1;
            smoothedValue = i3 % 2 == 0 ? -0.02d : -0.015d;
        }
        double sweepBeginDegree = (gauge.getSweepBeginDegree() + gauge.getSweepAngle()) - (gauge.getSweepAngle() * smoothedValue);
        graphics.setColor(gauge.getNeedleColor());
        if (gauge.max() > gauge.min()) {
            drawNeedle(graphics, gauge, sweepBeginDegree);
        }
        this.lastWidth = gauge.getWidth();
        this.lastHeight = gauge.getHeight();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        if (parentGauge() == null) {
            setParentGauge(gauge);
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.lastPaintTime = System.nanoTime();
        if (getGaugeWidth(gauge) > 0) {
            graphics.drawImage(getOffImage(graphics, gauge), getXOffset(gauge), getYOffset(gauge), null);
        }
    }

    public String getName() {
        return "Analog Gauge";
    }

    public void invalidate() {
        this.normalOffImage = null;
        this.criticalOffImage = null;
        this.warnOffImage = null;
        this.historyNeedleShape = null;
        this.outerGradient = null;
        this.innerGradient = null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public void initialize(Gauge gauge) {
    }

    public boolean isShapeLockedToAspect() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aO
    public boolean isComponentPaintedAt(int i2, int i3) {
        if (parentGauge() == null) {
            return true;
        }
        return new Arc2D.Double(new Rectangle2D.Double((parentGauge().getWidth() - getGaugeWidth(parentGauge())) / 2, (parentGauge().getHeight() - getGaugeHeight(parentGauge())) / 2, getGaugeWidth(parentGauge()), getGaugeHeight(parentGauge())), parentGauge().getStartAngle(), parentGauge().getFaceAngle(), 2).contains(i2, i3);
    }

    public Gauge parentGauge() {
        return this.parentGauge;
    }

    public void setParentGauge(Gauge gauge) {
        this.parentGauge = gauge;
    }

    public int currentState(Gauge gauge) {
        if (gauge.getValue() <= gauge.lowWarning() || gauge.getValue() >= gauge.highWarning()) {
            return gauge.getValue() > gauge.highCritical() ? 2 : 1;
        }
        return 0;
    }

    private void paintBgImage(Graphics2D graphics2D, Gauge gauge) {
        if (gauge.backgroundImage() != null) {
            graphics2D.clip(new Arc2D.Double(new Rectangle2D.Double(0.0d, 0.0d, getGaugeWidth(parentGauge()), getGaugeHeight(parentGauge())), gauge.getStartAngle(), gauge.getFaceAngle(), 2));
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.drawImage(gauge.backgroundImage(), 1, 1, getGaugeWidth(gauge) - 2, getGaugeHeight(gauge) - 2, null);
            graphics2D.setClip(0, 0, gauge.getWidth(), gauge.getHeight());
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public Area areaPainted(AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        Arc2D.Double r0 = new Arc2D.Double(new Rectangle2D.Double(getXOffset(gauge), getYOffset(gauge), getGaugeWidth(gauge), getGaugeHeight(gauge)), gauge.getStartAngle(), gauge.getFaceAngle(), 2);
        Area area = new Area();
        if (gauge.getBackColor().getAlpha() > 50) {
            area.add(new Area(r0));
            int gaugeWidth = getGaugeWidth(gauge) / 2;
            int gaugeHeight = getGaugeHeight(gauge) / 2;
            int borderWidth = (gaugeWidth - (2 * gauge.getBorderWidth())) / 5;
            int borderWidth2 = (gaugeHeight - (2 * gauge.getBorderWidth())) / 5;
            area.add(new Area(new Arc2D.Double(gaugeWidth - borderWidth, gaugeHeight - borderWidth2, 2 * borderWidth, 2 * borderWidth2, 0.0d, 360.0d, 2)));
        }
        return area;
    }
}
