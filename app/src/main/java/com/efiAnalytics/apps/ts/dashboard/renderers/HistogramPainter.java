package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.aH;
import com.efiAnalytics.ui.C1606cq;
import com.efiAnalytics.ui.eJ;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/HistogramPainter.class */
public class HistogramPainter implements GaugePainter {
    private transient Image backImage = null;
    aH historicalData = null;
    int scaleMultiplier = 2;
    g lineStroke = new g(2.0f);
    g gridLineStroke = new g(1.0f);
    g dashedLineStroke = new g(1.0f, 0, 2, 0.0f, new float[]{6.0f, 4.0f}, 0.0f);

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return "Line Graph";
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Gauge gauge = (Gauge) abstractC1420s;
        graphics.drawImage(getOffImage(graphics, gauge), 0, 0, null);
        drawHistogramLine(graphics, gauge);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
        this.backImage = null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public void initialize(Gauge gauge) {
        this.historicalData = gauge.setCaptureHistoricalData(true);
    }

    private Image getOffImage(Graphics graphics, Gauge gauge) {
        if (this.backImage == null) {
            this.backImage = paintBackground(graphics, gauge);
            this.historicalData.b(2 + (gauge.getWidth() / this.scaleMultiplier));
        }
        return this.backImage;
    }

    private Image paintBackground(Graphics graphics, Gauge gauge) {
        BufferedImage bufferedImageCreateCompatibleImage;
        Graphics graphics2;
        GraphicsConfiguration defaultConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        if (gauge.getWidth() <= 0 || gauge.getHeight() <= 0) {
            return null;
        }
        if (gauge.isAntialiasingOn()) {
            bufferedImageCreateCompatibleImage = defaultConfiguration.createCompatibleImage(gauge.getWidth(), gauge.getHeight(), 3);
            graphics2 = bufferedImageCreateCompatibleImage.getGraphics();
            Graphics2D graphics2D = (Graphics2D) graphics2;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            bufferedImageCreateCompatibleImage = defaultConfiguration.createCompatibleImage(gauge.getWidth(), gauge.getHeight(), 2);
            graphics2 = bufferedImageCreateCompatibleImage.getGraphics();
        }
        graphics2.setColor(gauge.getBackColor());
        graphics2.fillRect(0, 0, gauge.getWidth(), gauge.getHeight());
        if (gauge.backgroundImage() != null) {
            graphics2.drawImage(gauge.backgroundImage(), 0, 0, gauge.getWidth(), gauge.getHeight(), null);
        }
        drawBorder(graphics2, gauge);
        drawGridLines(graphics2, gauge);
        drawTitle(graphics2, gauge);
        return bufferedImageCreateCompatibleImage;
    }

    private void drawTitle(Graphics graphics, Gauge gauge) {
        Font lineFont = getLineFont(gauge);
        graphics.setFont(lineFont);
        String strTitle = gauge.title();
        if (!gauge.units().equals("")) {
            strTitle = strTitle + "(" + gauge.units() + ")";
        }
        if (strTitle.length() > 0) {
            graphics.drawString(strTitle, (gauge.getWidth() - gauge.getFontMetrics(lineFont).stringWidth(strTitle)) / 2, lineFont.getSize());
        }
    }

    private void drawHistogramLine(Graphics graphics, Gauge gauge) {
        double[] lineValues = getLineValues(gauge);
        int[] iArr = new int[lineValues.length];
        int[] iArr2 = new int[lineValues.length];
        int width = gauge.getWidth();
        int height = gauge.getHeight() - (2 * gauge.getBorderWidth());
        if (gauge.isAntialiasingOn()) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        for (int i2 = 0; i2 < iArr2.length; i2++) {
            iArr[i2] = (width - gauge.getBorderWidth()) - (i2 * this.scaleMultiplier);
            double dMin = (lineValues[i2] - gauge.min()) / (gauge.max() - gauge.min());
            if (dMin > 1.0d) {
                iArr2[i2] = gauge.getBorderWidth();
            } else if (dMin < 0.0d) {
                iArr2[i2] = gauge.getHeight() - gauge.getBorderWidth();
            } else {
                iArr2[i2] = (gauge.getHeight() - ((int) (height * dMin))) - gauge.getBorderWidth();
            }
        }
        graphics.setColor(gauge.getNeedleColor());
        ((Graphics2D) graphics).setStroke(this.lineStroke);
        graphics.drawPolyline(iArr, iArr2, iArr.length);
    }

    private double[] getLineValues(Gauge gauge) {
        double[] dArr;
        int width = 1 + ((gauge.getWidth() - (2 * gauge.getBorderWidth())) / this.scaleMultiplier);
        synchronized (this.historicalData) {
            int size = (this.historicalData.size() <= width || width <= 0) ? this.historicalData.size() : width;
            dArr = new double[size];
            for (int i2 = 0; i2 < size; i2++) {
                dArr[i2] = this.historicalData.a(i2);
            }
        }
        if (dArr.length == 0) {
            dArr = new double[]{0.0d};
        }
        return dArr;
    }

    private void drawBorder(Graphics graphics, Gauge gauge) {
        int width = gauge.getWidth();
        int height = gauge.getHeight();
        graphics.setColor(gauge.getTrimColor());
        int i2 = 0;
        while (i2 < gauge.getBorderWidth()) {
            graphics.draw3DRect(i2, i2, (width - (2 * i2)) - 1, (height - (2 * i2)) - 1, i2 < gauge.getBorderWidth() / 2);
            i2++;
        }
    }

    private void drawGridLines(Graphics graphics, Gauge gauge) {
        int i2 = 40;
        double height = 1.0d;
        boolean z2 = false;
        if (gauge.getHeight() < 150) {
            i2 = 20;
            z2 = true;
        }
        int i3 = 2;
        while (true) {
            int i4 = i3;
            if (gauge.getHeight() / i4 <= i2) {
                break;
            }
            height = (gauge.getHeight() / i4) / gauge.getHeight();
            i3 = i4 * 2;
        }
        boolean z3 = true;
        for (double d2 = height; d2 <= 1.0d; d2 += height) {
            z3 = z2 ? !z3 : true;
            drawAGridLine(graphics, gauge, d2, z3);
        }
        graphics.drawString(gauge.formatDouble(gauge.max(), gauge.getLabelDigits()), 3 + gauge.getBorderWidth(), getLineFont(gauge).getSize() + gauge.getBorderWidth());
    }

    private void drawAGridLine(Graphics graphics, Gauge gauge, double d2, boolean z2) {
        int borderWidth = gauge.getBorderWidth();
        int width = gauge.getWidth() - (2 * borderWidth);
        int iRound = ((int) Math.round((gauge.getHeight() - (gauge.getBorderWidth() * 2)) * d2)) + gauge.getBorderWidth();
        graphics.setColor(Color.LIGHT_GRAY);
        ((Graphics2D) graphics).setStroke(z2 ? this.gridLineStroke : this.dashedLineStroke);
        graphics.drawLine(borderWidth, iRound, width + borderWidth, iRound);
        graphics.setColor(gauge.getFontColor());
        if (z2) {
            String str = gauge.formatDouble(gauge.max() - (d2 * (gauge.max() - gauge.min())), gauge.getLabelDigits());
            graphics.setFont(getLineFont(gauge));
            graphics.drawString(str, 3 + gauge.getBorderWidth(), iRound - 2);
        }
    }

    private Font getLineFont(Gauge gauge) {
        return C1606cq.a().a(gauge.getFontFamily(), gauge.isItalicFont() ? 3 : 1, (gauge.getHeight() < eJ.a(168) ? eJ.a(12) : gauge.getHeight() / 14) + gauge.getFontSizeAdjustment());
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter
    public boolean isShapeLockedToAspect() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public boolean requiresBackgroundRepaint(AbstractC1420s abstractC1420s) {
        return (this.backImage != null && abstractC1420s.getWidth() == this.backImage.getWidth(null) && abstractC1420s.getHeight() == this.backImage.getHeight(null)) ? false : true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        drawHistogramLine(graphics, (Gauge) abstractC1420s);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s) {
        graphics.drawImage(getOffImage(graphics, (Gauge) abstractC1420s), 0, 0, null);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public Area areaPainted(AbstractC1420s abstractC1420s) {
        return ((Gauge) abstractC1420s).getBackColor().getAlpha() > 40 ? new Area(new Rectangle2D.Double(0.0d, 0.0d, r0.getWidth(), r0.getHeight())) : new Area();
    }
}
