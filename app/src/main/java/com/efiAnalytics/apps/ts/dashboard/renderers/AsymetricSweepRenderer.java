package com.efiAnalytics.apps.ts.dashboard.renderers;

import bH.W;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.aO;
import com.efiAnalytics.ui.C1606cq;
import com.efiAnalytics.ui.dL;
import com.efiAnalytics.ui.eJ;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/AsymetricSweepRenderer.class */
public class AsymetricSweepRenderer implements aO, GaugePainter {
    int barThickness = eJ.a(10);
    private GradientPaint outerGradient = null;
    double lastValue = 0.0d;
    transient Image bottomOffImage = null;
    transient Image topOffImage = null;
    transient List numberPositions = null;
    g tickStroke = new g(eJ.a(4), 1, 1);
    private b barShape = null;
    HashMap darkColorCache = new HashMap();

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public void initialize(Gauge gauge) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public boolean isShapeLockedToAspect() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return "Asymetric Sweep Gauge";
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Image bottomOffImage = getBottomOffImage(gauge);
        Graphics2D graphics2D2 = (Graphics2D) bottomOffImage.getGraphics();
        graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(bottomOffImage, 0, 0, null);
        paintNeedle(graphics2D, gauge, false);
        if (1 != 0) {
            graphics.drawImage(getTopOffImage(gauge), 0, 0, null);
        } else {
            paintNumbers(graphics2D, gauge);
            paintBorder(graphics2D, gauge);
        }
    }

    private void paintNumbers(Graphics2D graphics2D, Gauge gauge) {
        if (gauge.getFontColor().getAlpha() > 0) {
            List<c> numberPositions = getNumberPositions(gauge);
            float f2 = getBarShape(gauge).f9530j * 0.08f;
            int iRound = Math.round((getBarShape(gauge).f9530j - gauge.getBorderWidth()) * 0.38f) + gauge.getFontSizeAdjustment();
            int i2 = iRound < 1 ? 1 : iRound;
            Font fontA = C1606cq.a().a(gauge.getFontFamily(), gauge.isItalicFont() ? 2 : 0, i2);
            graphics2D.setFont(fontA);
            graphics2D.setColor(gauge.getFontColor());
            Color colorDarker = gauge.getTrimColor().darker();
            float[] fArr = new float[4];
            float[] fArr2 = new float[4];
            for (c cVar : numberPositions) {
                float fB = ((float) getBarShape(gauge).b(cVar.a())) + (gauge.getBorderWidth() / 2);
                float fD = getBarShape(gauge).d(getBarShape(gauge).f9523c, fB) - (gauge.getBorderWidth() / 2);
                fArr[0] = fB;
                fArr2[0] = fD - f2;
                fArr[1] = fB - (f2 / 3.0f);
                fArr2[1] = fD;
                fArr[2] = fB + (f2 / 3.0f);
                fArr2[2] = fD;
                fArr[3] = fB;
                fArr2[3] = fD - f2;
                dL dLVar = new dL(fArr, fArr2, fArr.length);
                graphics2D.setColor(colorDarker);
                graphics2D.setStroke(this.tickStroke);
                graphics2D.draw(dLVar);
                graphics2D.fill(dLVar);
                String strA = W.a(cVar.b(), gauge.getLabelDigits());
                float fStringWidth = fB - (graphics2D.getFontMetrics(fontA).stringWidth(strA) / 2.0f);
                float f3 = fD - ((getBarShape(gauge).f9530j - i2) / 2);
                TextLayout textLayout = new TextLayout(strA, fontA, graphics2D.getFontRenderContext());
                AffineTransform affineTransform = new AffineTransform();
                affineTransform.translate(fStringWidth, f3);
                Shape outline = textLayout.getOutline(affineTransform);
                graphics2D.setColor(gauge.getFontColor());
                graphics2D.drawString(strA, fStringWidth, f3);
                graphics2D.setColor(gauge.getTrimColor());
                graphics2D.setStroke(new BasicStroke(i2 / 30.0f));
                graphics2D.draw(outline);
            }
        }
    }

    private void paintNeedle(Graphics2D graphics2D, Gauge gauge, boolean z2) {
        double dMin = z2 ? gauge.min() : gauge.getSmoothedValue();
        double dMin2 = (dMin - gauge.min()) / (gauge.max() - gauge.min());
        this.lastValue = dMin;
        float[] fArr = {0.0f, 0.8f};
        Color[] colorArr = {gauge.getNeedleColor().darker(), getBrighter(gauge.getNeedleColor())};
        float fA = eJ.a(450);
        int iRound = ((float) gauge.getWidth()) > fA ? 20 + Math.round((gauge.getWidth() - fA) / 22.4f) : 20;
        float width = (gauge.getWidth() - (2.5f * gauge.getBorderWidth())) / iRound;
        float[] fArr2 = new float[5];
        float[] fArr3 = new float[5];
        for (int i2 = 0; i2 < iRound; i2++) {
            float f2 = i2 / iRound;
            float f3 = (i2 + 1.0f) / iRound;
            float width2 = gauge.isCounterClockwise() ? ((gauge.getWidth() - gauge.getBorderWidth()) - (i2 * width)) - (width * 0.065f) : gauge.getBorderWidth() + (i2 * width) + (width * 0.065f);
            float width3 = gauge.isCounterClockwise() ? ((gauge.getWidth() - gauge.getBorderWidth()) - ((i2 + 1) * width)) + (width * 0.065f) : (gauge.getBorderWidth() + ((i2 + 1) * width)) - (width * 0.065f);
            float fD = getBarShape(gauge).d(this.barShape.f9522b, width2);
            float fD2 = getBarShape(gauge).d(this.barShape.f9522b, width3);
            float fD3 = getBarShape(gauge).d(this.barShape.f9523c, width2);
            float fD4 = getBarShape(gauge).d(this.barShape.f9523c, width3);
            fArr2[0] = width2;
            fArr3[0] = fD;
            fArr2[1] = width3;
            fArr3[1] = fD2;
            fArr2[2] = width3;
            fArr3[2] = fD4;
            fArr2[3] = width2;
            fArr3[3] = fD3;
            fArr2[4] = width2;
            fArr3[4] = fD;
            dL dLVar = new dL(fArr2, fArr3, fArr2.length);
            if (f3 < dMin2) {
                if (0 != 0) {
                    Point2D.Float r0 = new Point2D.Float((width3 + width2) / 2.0f, (2.0f * (fD3 + fD)) / 3.0f);
                    graphics2D.setPaint(new RadialGradientPaint(r0, width3 - width2, r0, fArr, colorArr, MultipleGradientPaint.CycleMethod.NO_CYCLE));
                } else {
                    graphics2D.setColor(getNeedleColor(gauge, f3));
                }
                graphics2D.fill(dLVar);
            } else {
                if (z2) {
                    graphics2D.setColor(getDarker(getDarker(getDarker(getDarker(getNeedleColor(gauge, f3))))));
                    graphics2D.fill(dLVar);
                }
                if (dMin2 > (width2 - gauge.getBorderWidth()) / (gauge.getWidth() - (2.0f * gauge.getBorderWidth()))) {
                    float borderWidth = gauge.getBorderWidth() + (((float) dMin2) * (gauge.getWidth() - (2.0f * gauge.getBorderWidth())));
                    float f4 = borderWidth < width3 ? borderWidth : width3;
                    fArr2[1] = f4;
                    fArr3[1] = getBarShape(gauge).d(this.barShape.f9522b, f4);
                    fArr2[2] = f4;
                    fArr3[2] = getBarShape(gauge).d(this.barShape.f9523c, f4);
                    graphics2D.setColor(getNeedleColor(gauge, f3));
                    graphics2D.fill(new dL(fArr2, fArr3, fArr2.length));
                }
                if (!z2) {
                    return;
                }
            }
        }
    }

    private Color getNeedleColor(Gauge gauge, double d2) {
        double dMin = gauge.min() + (d2 * (gauge.max() - gauge.min()));
        return ((dMin >= gauge.lowCritical() || gauge.getValue() >= gauge.lowCritical()) && dMin <= gauge.highCritical()) ? ((dMin >= gauge.lowWarning() || gauge.getValue() >= gauge.lowWarning()) && dMin <= gauge.highWarning()) ? gauge.getNeedleColor() : gauge.getWarnColor() : gauge.getCriticalColor();
    }

    protected Image getBottomOffImage(Gauge gauge) {
        if (this.bottomOffImage == null || this.bottomOffImage.getWidth(null) != gauge.getWidth() || this.bottomOffImage.getHeight(null) != gauge.getHeight()) {
            this.bottomOffImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(gauge.getWidth(), gauge.getHeight(), 3);
            Graphics2D graphics2D = (Graphics2D) this.bottomOffImage.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paintBackground((Graphics) graphics2D, gauge);
        }
        return this.bottomOffImage;
    }

    protected Image getTopOffImage(Gauge gauge) {
        if (this.topOffImage == null || this.topOffImage.getWidth(null) != gauge.getWidth() || this.topOffImage.getHeight(null) != gauge.getHeight()) {
            this.topOffImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(gauge.getWidth(), gauge.getHeight(), 3);
            Graphics2D graphics2D = (Graphics2D) this.topOffImage.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paintNumbers(graphics2D, gauge);
            paintBorder(graphics2D, gauge);
        }
        return this.topOffImage;
    }

    protected Image paintBackground(Graphics graphics, Gauge gauge) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(0, 0, 0, 0));
        graphics2D.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        if (gauge.getBackColor() != null && gauge.getBackColor().getAlpha() > 0) {
            graphics2D.setColor(gauge.getBackColor());
            graphics2D.fill(getBarShape(gauge));
        }
        paintNeedle(graphics2D, gauge, true);
        return this.bottomOffImage;
    }

    private void paintBorder(Graphics2D graphics2D, Gauge gauge) {
        Color trimColor = gauge.getTrimColor();
        if (gauge.getBorderWidth() <= 0 || trimColor.getAlpha() <= 0) {
            return;
        }
        if (this.outerGradient == null) {
            Color darker = getDarker(trimColor);
            this.outerGradient = new GradientPaint(gauge.getWidth() / 4, 0.0f, getBrighter(trimColor), gauge.getWidth() / 2, 40.0f, darker, true);
        }
        graphics2D.setPaint(this.outerGradient);
        graphics2D.setStroke(new BasicStroke(gauge.getBorderWidth(), 1, 1, 10.0f, null, 0.0f));
        graphics2D.draw(getBarShape(gauge));
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
        Color colorDarker = (Color) this.darkColorCache.get(color);
        if (colorDarker == null) {
            colorDarker = (color.getBlue() + color.getRed()) + color.getGreen() > 650 ? color.darker().darker() : color.darker();
            this.darkColorCache.put(color, colorDarker);
        }
        return colorDarker;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private List getNumberPositions(Gauge gauge) {
        int i2;
        if (this.numberPositions == null) {
            float f2 = getBarShape(gauge).f9530j * 0.65f;
            this.numberPositions = new ArrayList();
            int width = gauge.getWidth() / getBarShape(gauge).f9530j;
            float f3 = gauge.max() > 3000.0d ? 1000.0f : gauge.max() > 500.0d ? 100.0f : 1.0f;
            double dPow = Math.pow(10.0d, (int) (Math.round(Math.log10(gauge.max() - gauge.min())) - 1));
            long jRound = Math.round((gauge.max() - gauge.min()) / dPow);
            while (true) {
                i2 = (int) jRound;
                if (i2 <= 0 || gauge.getWidth() / i2 >= f2) {
                    break;
                }
                dPow *= 2.0d;
                jRound = (gauge.max() - gauge.min()) / dPow;
            }
            for (int i3 = 1; i3 < i2; i3++) {
                float fMin = (float) (gauge.min() + (i3 * dPow));
                this.numberPositions.add(new c(this, fMin / f3, fMin / ((float) (gauge.max() - gauge.min()))));
            }
        }
        return this.numberPositions;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
        this.barShape = null;
        this.outerGradient = null;
        this.bottomOffImage = null;
        this.topOffImage = null;
        this.numberPositions = null;
    }

    private b getBarShape(Gauge gauge) {
        if (this.barShape == null) {
            this.barShape = new b(this, gauge);
            this.barShape.a(gauge.getWidth(), gauge.getHeight());
        }
        return this.barShape;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aO
    public boolean isComponentPaintedAt(int i2, int i3) {
        if (this.barShape != null) {
            return this.barShape.contains(i2, i3);
        }
        return true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public boolean requiresBackgroundRepaint(AbstractC1420s abstractC1420s) {
        return (this.topOffImage != null && abstractC1420s.getWidth() == this.topOffImage.getWidth(null) && abstractC1420s.getHeight() == this.topOffImage.getHeight(null)) ? false : true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (gauge.isAntialiasingOn()) {
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        paintNeedle(graphics2D, gauge, false);
        if (1 != 0) {
            graphics.drawImage(getTopOffImage(gauge), 0, 0, null);
        } else {
            paintNumbers(graphics2D, gauge);
            paintBorder(graphics2D, gauge);
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (0 != 0) {
            Image bottomOffImage = getBottomOffImage(gauge);
            Graphics2D graphics2D2 = (Graphics2D) bottomOffImage.getGraphics();
            graphics2D2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(bottomOffImage, 0, 0, null);
        } else {
            paintBackground(graphics, (Gauge) abstractC1420s);
        }
        paintBorder(graphics2D, gauge);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public Area areaPainted(AbstractC1420s abstractC1420s) {
        return new Area(getBarShape((Gauge) abstractC1420s));
    }
}
