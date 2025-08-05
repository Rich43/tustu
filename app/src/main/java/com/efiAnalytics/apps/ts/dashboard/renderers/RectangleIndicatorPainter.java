package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.ui.C1606cq;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/RectangleIndicatorPainter.class */
public class RectangleIndicatorPainter implements IndicatorPainter {
    static String NAME = "Basic Rectangle Indicator";
    transient Font textFont = null;
    transient Image offImage = null;
    int lastWidth = -1;
    int lastHeight = -1;
    double lastValue = Double.NaN;
    Area onImageArea = null;
    Area offImageArea = null;
    private boolean maskImages;

    public RectangleIndicatorPainter() {
        this.maskImages = false;
        this.maskImages = C1798a.a().c(C1798a.f13330al, false);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public String getName() {
        return NAME;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
        Indicator indicator = (Indicator) abstractC1420s;
        this.lastWidth = abstractC1420s.getWidth();
        this.lastHeight = abstractC1420s.getHeight();
        this.lastValue = indicator.getValue();
        if (indicator.getValue() == 0.0d) {
            drawBackground(graphics, indicator, indicator.getOffBackgroundColor());
            if (indicator.offImage() != null) {
                Image imageOffImage = indicator.offImage();
                float imageScaleRatio = getImageScaleRatio(indicator, imageOffImage);
                int width = (int) (imageOffImage.getWidth(null) * imageScaleRatio);
                int height = (int) (imageOffImage.getHeight(null) * imageScaleRatio);
                graphics.drawImage(imageOffImage, (indicator.getWidth() - width) / 2, (indicator.getHeight() - height) / 2, width, height, null);
                if (this.maskImages && this.offImageArea == null) {
                    this.offImageArea = getImageOutline(indicator, imageOffImage, imageScaleRatio);
                }
            }
            drawText(graphics, indicator.offText(), indicator, indicator.getOffTextColor());
            return;
        }
        drawBackground(graphics, indicator, indicator.getOnBackgroundColor());
        if (indicator.onImage() != null) {
            Image imageOnImage = indicator.onImage();
            float imageScaleRatio2 = getImageScaleRatio(indicator, imageOnImage);
            int width2 = (int) (imageOnImage.getWidth(null) * imageScaleRatio2);
            int height2 = (int) (imageOnImage.getHeight(null) * imageScaleRatio2);
            graphics.drawImage(imageOnImage, (indicator.getWidth() - width2) / 2, (indicator.getHeight() - height2) / 2, width2, height2, null);
            if (this.maskImages && this.onImageArea == null) {
                this.onImageArea = getImageOutline(indicator, imageOnImage, imageScaleRatio2);
            }
        }
        drawText(graphics, indicator.onText(), indicator, indicator.getOnTextColor());
    }

    private float getImageScaleRatio(Indicator indicator, Image image) {
        if (image == null) {
            return 1.0f;
        }
        float width = indicator.getWidth() / image.getWidth(null);
        float height = indicator.getHeight() / image.getHeight(null);
        return width < height ? width : height;
    }

    private void drawBackground(Graphics graphics, Indicator indicator, Color color) {
        if (color == null || color.getAlpha() <= 0) {
            return;
        }
        graphics.setColor(color);
        graphics.fill3DRect(0, 0, indicator.getWidth(), indicator.getHeight(), true);
        graphics.draw3DRect(1, 1, indicator.getWidth() - 3, indicator.getHeight() - 3, true);
    }

    private void drawText(Graphics graphics, String str, Indicator indicator, Color color) {
        if (indicator.isAntialiasingOn()) {
            ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        graphics.setColor(color);
        Font textFont = getTextFont(indicator);
        graphics.setFont(textFont);
        graphics.drawString(str, (indicator.getWidth() - indicator.getFontMetrics(textFont).stringWidth(str)) / 2, (int) ((indicator.getHeight() / 2) + (textFont.getSize() * 0.36d)));
    }

    protected Font getTextFont(Indicator indicator) {
        if (this.textFont != null) {
            return this.textFont;
        }
        int height = (int) (((indicator.getHeight() - indicator.insets().top) - indicator.insets().bottom) * 0.98d);
        int width = (indicator.getWidth() - indicator.insets().left) - indicator.insets().right;
        String fontFamily = indicator.getFontFamily();
        int i2 = indicator.isItalicFont() ? 2 : 0;
        Font fontA = C1606cq.a().a(fontFamily, i2, height);
        if (fontA.canDisplayUpTo(indicator.onText()) >= 0 && fontA.canDisplayUpTo(indicator.offText()) >= 0) {
            fontA = C1606cq.a().a("", i2, height);
        }
        while (true) {
            FontMetrics fontMetrics = indicator.getFontMetrics(fontA);
            if ((fontMetrics.stringWidth(indicator.onText()) < width && fontMetrics.stringWidth(indicator.offText()) < width) || height < 1) {
                break;
            }
            height--;
            fontA = C1606cq.a().a(fontFamily, i2, height);
        }
        this.textFont = fontA;
        return this.textFont;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void invalidate() {
        this.textFont = null;
        this.offImage = null;
        this.onImageArea = null;
        this.offImageArea = null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.IndicatorPainter
    public boolean isMustPaint() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public boolean requiresBackgroundRepaint(AbstractC1420s abstractC1420s) {
        return (abstractC1420s.getWidth() == this.lastWidth && abstractC1420s.getHeight() == this.lastHeight && ((Indicator) abstractC1420s).getValue() == this.lastValue && this.textFont != null) ? false : true;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void updateGauge(Graphics graphics, AbstractC1420s abstractC1420s) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public void paintBackground(Graphics graphics, AbstractC1420s abstractC1420s) {
        paintGauge(graphics, abstractC1420s);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.renderers.d
    public Area areaPainted(AbstractC1420s abstractC1420s) {
        Indicator indicator = (Indicator) abstractC1420s;
        Color offBackgroundColor = indicator.getValue() == 0.0d ? indicator.getOffBackgroundColor() : indicator.getOnBackgroundColor();
        return (offBackgroundColor == null || offBackgroundColor.getAlpha() <= 0) ? (indicator.getValue() != 0.0d || this.offImageArea == null) ? this.onImageArea != null ? (Area) this.onImageArea.clone() : new Area(new Rectangle2D.Double(0.0d, 0.0d, abstractC1420s.getWidth(), abstractC1420s.getHeight())) : (Area) this.offImageArea.clone() : new Area(new Rectangle2D.Double(0.0d, 0.0d, abstractC1420s.getWidth(), abstractC1420s.getHeight()));
    }

    private Area getImageOutline(Indicator indicator, Image image, double d2) {
        int width = (int) (image.getWidth(indicator) * d2);
        int height = (int) (image.getHeight(indicator) * d2);
        if (width <= 0 || height <= 0) {
            return null;
        }
        BufferedImage bufferedImage = new BufferedImage(indicator.getWidth(), indicator.getHeight(), 6);
        bufferedImage.getGraphics().drawImage(image, (indicator.getWidth() - width) / 2, (indicator.getHeight() - height) / 2, width, height, null);
        return getImageOutline(bufferedImage);
    }

    private Area getImageOutline(BufferedImage bufferedImage) {
        Area area = new Area();
        int i2 = -1;
        int i3 = -1;
        if (bufferedImage.getHeight() >= 256 || bufferedImage.getWidth() >= 256) {
            int i4 = -1;
            int i5 = -1;
            int i6 = -1;
            int i7 = -1;
            int height = bufferedImage.getHeight() / 2;
            int width = bufferedImage.getWidth() / 2;
            int i8 = 0;
            while (true) {
                if (i8 >= bufferedImage.getWidth()) {
                    break;
                }
                if (getAlpha(bufferedImage.getRGB(i8, height)) != 0) {
                    i6 = i8;
                    break;
                }
                i8++;
            }
            int width2 = bufferedImage.getWidth() - 1;
            while (true) {
                if (width2 <= 0) {
                    break;
                }
                if (getAlpha(bufferedImage.getRGB(width2, height)) != 0) {
                    i7 = width2;
                    break;
                }
                width2--;
            }
            int i9 = 0;
            while (true) {
                if (i9 >= bufferedImage.getHeight()) {
                    break;
                }
                if (getAlpha(bufferedImage.getRGB(width, i9)) != 0) {
                    i4 = i9;
                    break;
                }
                i9++;
            }
            int height2 = bufferedImage.getHeight() - 1;
            while (true) {
                if (height2 <= 0) {
                    break;
                }
                if (getAlpha(bufferedImage.getRGB(width, height2)) != 0) {
                    i5 = height2;
                    break;
                }
                height2--;
            }
            if (i7 > 0 && i5 > 0) {
                area.add(new Area(new Rectangle(i6, i4, (i7 - i6) + 1, (i5 - i4) + 1)));
            }
        } else {
            for (int i10 = 0; i10 < bufferedImage.getHeight(); i10++) {
                for (int i11 = 0; i11 < bufferedImage.getWidth(); i11++) {
                    if (getAlpha(bufferedImage.getRGB(i11, i10)) != 0) {
                        if (i2 == -1) {
                            i2 = i11;
                            i3 = i10;
                        } else if (i11 == bufferedImage.getWidth() - 1) {
                            area.add(new Area(new Rectangle(i2, i3, (i11 - i2) + 1, (i10 - i3) + 1)));
                            i2 = -1;
                            i3 = -1;
                        }
                    } else if (i2 >= 0 && i3 >= 0) {
                        area.add(new Area(new Rectangle(i2, i3, (i11 - i2) + 1, (i10 - i3) + 1)));
                        i2 = -1;
                        i3 = -1;
                    }
                }
            }
        }
        if (area.getBounds().isEmpty()) {
            return null;
        }
        return area;
    }

    private int getAlpha(int i2) {
        return (i2 >> 24) & 255;
    }
}
