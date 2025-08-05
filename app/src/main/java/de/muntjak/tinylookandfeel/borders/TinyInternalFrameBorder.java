package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.TinyLookAndFeel;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.JInternalFrame;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyInternalFrameBorder.class */
public class TinyInternalFrameBorder extends AbstractBorder implements UIResource {
    private static final HashMap cache = new HashMap();
    public static Color frameUpperColor;
    public static Color frameLowerColor;
    public static Color disabledUpperColor;
    public static Color disabledLowerColor;
    private JInternalFrame frame;
    private boolean isPalette;
    private int titleHeight;
    private boolean isActive;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyInternalFrameBorder$CaptionKey.class */
    private static class CaptionKey {
        private boolean isActive;
        private int titleHeight;

        CaptionKey(boolean z2, int i2) {
            this.isActive = z2;
            this.titleHeight = i2;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof CaptionKey)) {
                return false;
            }
            CaptionKey captionKey = (CaptionKey) obj;
            return this.isActive == captionKey.isActive && this.titleHeight == captionKey.titleHeight;
        }

        public int hashCode() {
            return (this.isActive ? 1 : 2) * this.titleHeight;
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        this.frame = (JInternalFrame) component;
        this.frame.setOpaque(false);
        this.isActive = this.frame.isSelected();
        this.isPalette = this.frame.getClientProperty("isPalette") == Boolean.TRUE;
        if (this.isPalette) {
            this.titleHeight = 21;
        } else {
            this.titleHeight = 25;
        }
        if (this.isActive) {
            graphics.setColor(Theme.frameBorderColor.getColor());
        } else {
            graphics.setColor(Theme.frameBorderDisabledColor.getColor());
        }
        drawXpBorder(graphics, i2, i3, i4, i5);
        ColorUIResource color = this.isActive ? Theme.frameCaptionColor.getColor() : Theme.frameCaptionDisabledColor.getColor();
        graphics.setColor(color);
        if (TinyLookAndFeel.controlPanelInstantiated) {
            drawXpCaptionNoCache(graphics, i2, i3, i4, i5, color);
        } else {
            drawXpCaption(graphics, i2, i3, i4, i5, color);
        }
    }

    private void drawXpBorder(Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.drawLine(i2, i3 + 6, i2, (i3 + i5) - 1);
        graphics.drawLine(i2 + 2, i3 + this.titleHeight, i2 + 2, (i3 + i5) - 3);
        graphics.drawLine((i2 + i4) - 1, i3 + 6, (i2 + i4) - 1, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 3, i3 + this.titleHeight, (i2 + i4) - 3, (i3 + i5) - 3);
        graphics.drawLine(i2 + 2, (i3 + i5) - 3, (i2 + i4) - 3, (i3 + i5) - 3);
        graphics.drawLine(i2, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
        if (this.isActive) {
            graphics.setColor(Theme.frameCaptionColor.getColor());
        } else {
            graphics.setColor(Theme.frameCaptionDisabledColor.getColor());
        }
        graphics.drawLine(i2 + 1, i3 + this.titleHeight, i2 + 1, (i3 + i5) - 2);
        graphics.drawLine((i2 + i4) - 2, i3 + this.titleHeight, (i2 + i4) - 2, (i3 + i5) - 2);
        graphics.drawLine(i2 + 1, (i3 + i5) - 2, (i2 + i4) - 2, (i3 + i5) - 2);
        ColorUIResource color = this.isActive ? Theme.frameBorderColor.getColor() : Theme.frameBorderDisabledColor.getColor();
        graphics.setColor(ColorRoutines.getAlphaColor(color, 82));
        graphics.drawLine(i2, i3 + 3, i2, i3 + 3);
        graphics.drawLine((i2 + i4) - 1, i3 + 3, (i2 + i4) - 1, i3 + 3);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 156));
        graphics.drawLine(i2, i3 + 4, i2, i3 + 4);
        graphics.drawLine((i2 + i4) - 1, i3 + 4, (i2 + i4) - 1, i3 + 4);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 215));
        graphics.drawLine(i2, i3 + 5, i2, i3 + 5);
        graphics.drawLine((i2 + i4) - 1, i3 + 5, (i2 + i4) - 1, i3 + 5);
    }

    private void drawXpCaption(Graphics graphics, int i2, int i3, int i4, int i5, Color color) {
        ColorUIResource color2;
        if (this.isPalette) {
            drawXpPaletteCaption(graphics, i2, i3, i4, i5, color);
            return;
        }
        int value = Theme.frameSpreadDarkDisabled.getValue();
        int value2 = Theme.frameSpreadLightDisabled.getValue();
        if (this.isActive) {
            color2 = Theme.frameBorderColor.getColor();
            value = Theme.frameSpreadDark.getValue();
            value2 = Theme.frameSpreadLight.getValue();
        } else {
            color2 = Theme.frameBorderDisabledColor.getColor();
        }
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 82));
        graphics.drawLine(i2 + 3, i3, i2 + 3, i3);
        graphics.drawLine((i2 + i4) - 4, i3, (i2 + i4) - 4, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 156));
        graphics.drawLine(i2 + 4, i3, i2 + 4, i3);
        graphics.drawLine((i2 + i4) - 5, i3, (i2 + i4) - 5, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 215));
        graphics.drawLine(i2 + 5, i3, i2 + 5, i3);
        graphics.drawLine((i2 + i4) - 6, i3, (i2 + i4) - 6, i3);
        int i6 = i3 + 1;
        Color colorDarken = ColorRoutines.darken(color, 4 * value);
        graphics.setColor(colorDarken);
        graphics.drawLine(i2 + 3, i6, i2 + 5, i6);
        graphics.drawLine((i2 + i4) - 6, i6, (i2 + i4) - 4, i6);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 139));
        graphics.drawLine(i2 + 2, i6, i2 + 2, i6);
        graphics.drawLine((i2 + i4) - 3, i6, (i2 + i4) - 3, i6);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 23));
        graphics.drawLine(i2 + 1, i6, i2 + 1, i6);
        graphics.drawLine((i2 + i4) - 2, i6, (i2 + i4) - 2, i6);
        int i7 = i6 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 4, i7, i2 + 5, i7);
        graphics.drawLine((i2 + i4) - 6, i7, (i2 + i4) - 5, i7);
        graphics.setColor(color);
        graphics.drawLine(i2 + 3, i7, i2 + 3, i7);
        graphics.drawLine((i2 + i4) - 4, i7, (i2 + i4) - 4, i7);
        Color colorDarken2 = ColorRoutines.darken(color, 6 * value);
        graphics.setColor(colorDarken2);
        graphics.drawLine(i2 + 2, i7, i2 + 2, i7);
        graphics.drawLine((i2 + i4) - 3, i7, (i2 + i4) - 3, i7);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken2, 139));
        graphics.drawLine(i2 + 1, i7, i2 + 1, i7);
        graphics.drawLine((i2 + i4) - 2, i7, (i2 + i4) - 2, i7);
        int i8 = i7 + 1;
        graphics.setColor(color);
        graphics.drawLine(i2 + 2, i8, i2 + 2, i8);
        graphics.drawLine((i2 + i4) - 3, i8, (i2 + i4) - 3, i8);
        graphics.setColor(ColorRoutines.darken(color, 6 * value));
        graphics.drawLine(i2 + 1, i8, i2 + 1, i8);
        graphics.drawLine((i2 + i4) - 2, i8, (i2 + i4) - 2, i8);
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 3, i8, i2 + 3, i8);
        graphics.drawLine((i2 + i4) - 4, i8, (i2 + i4) - 4, i8);
        graphics.setColor(ColorRoutines.lighten(color, 7 * value2));
        graphics.drawLine(i2 + 4, i8, i2 + 4, i8);
        graphics.drawLine((i2 + i4) - 5, i8, (i2 + i4) - 5, i8);
        graphics.setColor(ColorRoutines.lighten(color, 3 * value2));
        graphics.drawLine(i2 + 5, i8, i2 + 5, i8);
        graphics.drawLine((i2 + i4) - 6, i8, (i2 + i4) - 6, i8);
        graphics.setColor(color);
        graphics.drawLine(i2 + 6, i8, i2 + 6, i8);
        graphics.drawLine((i2 + i4) - 7, i8, (i2 + i4) - 7, i8);
        int i9 = i8 + 1;
        graphics.setColor(ColorRoutines.darken(color, 2 * value));
        graphics.drawLine(i2 + 5, i9, i2 + 6, i9);
        graphics.drawLine(((i2 + i2) + i4) - 7, i9, (i2 + i4) - 6, i9);
        graphics.setColor(ColorRoutines.darken(color, 6 * value));
        graphics.drawLine(i2 + 1, i9, i2 + 1, i9);
        graphics.drawLine((i2 + i4) - 2, i9, (i2 + i4) - 2, i9);
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 2, i9, i2 + 2, i9);
        graphics.drawLine((i2 + i4) - 3, i9, (i2 + i4) - 3, i9);
        graphics.setColor(ColorRoutines.lighten(color, 5 * value2));
        graphics.drawLine(i2 + 3, i9, i2 + 3, i9);
        graphics.drawLine((i2 + i4) - 4, i9, (i2 + i4) - 4, i9);
        graphics.setColor(color);
        graphics.drawLine(i2 + 4, i9, i2 + 4, i9);
        graphics.drawLine((i2 + i4) - 5, i9, (i2 + i4) - 5, i9);
        int i10 = i9 + 1;
        graphics.setColor(ColorRoutines.darken(color, 4 * value));
        graphics.drawLine(i2 + 1, i10, i2 + 1, i10);
        graphics.drawLine((i2 + i4) - 2, i10, (i2 + i4) - 2, i10);
        CaptionKey captionKey = new CaptionKey(this.isActive, this.titleHeight);
        Object obj = cache.get(captionKey);
        if (obj != null) {
            graphics.drawImage((Image) obj, i2 + 6, i3, (i2 + i4) - 6, i3 + 5, 0, 0, 1, 5, this.frame);
            graphics.drawImage((Image) obj, i2 + 1, i3 + 5, (i2 + i4) - 1, i3 + this.titleHeight, 0, 5, 1, this.titleHeight, this.frame);
            if (this.isActive) {
                frameUpperColor = ColorRoutines.darken(color, 4 * value);
                frameLowerColor = ColorRoutines.lighten(color, 10 * value2);
                return;
            } else {
                disabledUpperColor = ColorRoutines.darken(color, 4 * value);
                disabledLowerColor = ColorRoutines.lighten(color, 10 * value2);
                return;
            }
        }
        BufferedImage bufferedImage = new BufferedImage(1, this.titleHeight, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        graphics2.setColor(color2);
        graphics2.drawLine(0, 0, 1, 0);
        graphics2.setColor(ColorRoutines.darken(color, 4 * value));
        graphics2.drawLine(0, 1, 1, 1);
        graphics2.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics2.drawLine(0, 2, 1, 2);
        graphics2.setColor(color);
        graphics2.drawLine(0, 3, 1, 3);
        graphics2.setColor(ColorRoutines.darken(color, 2 * value));
        graphics2.drawLine(0, 4, 1, 4);
        graphics2.setColor(ColorRoutines.darken(color, 4 * value));
        graphics2.drawLine(0, 5, 1, 5);
        graphics2.setColor(ColorRoutines.darken(color, 4 * value));
        graphics2.drawLine(0, 6, 1, 6);
        graphics2.setColor(ColorRoutines.darken(color, 3 * value));
        graphics2.drawLine(0, 7, 1, 7);
        graphics2.drawLine(0, 8, 1, 8);
        graphics2.drawLine(0, 9, 1, 9);
        graphics2.setColor(ColorRoutines.darken(color, 2 * value));
        graphics2.drawLine(0, 10, 1, 10);
        graphics2.drawLine(0, 11, 1, 11);
        graphics2.setColor(ColorRoutines.darken(color, value));
        graphics2.drawLine(0, 12, 1, 12);
        graphics2.setColor(color);
        graphics2.drawLine(0, 13, 1, 13);
        graphics2.drawLine(0, 14, 1, 14);
        graphics2.setColor(ColorRoutines.lighten(color, 2 * value2));
        graphics2.drawLine(0, 15, 1, 15);
        graphics2.setColor(ColorRoutines.lighten(color, 4 * value2));
        graphics2.drawLine(0, 16, 1, 16);
        graphics2.setColor(ColorRoutines.lighten(color, 5 * value2));
        graphics2.drawLine(0, 17, 1, 17);
        graphics2.setColor(ColorRoutines.lighten(color, 6 * value2));
        graphics2.drawLine(0, 18, 1, 18);
        graphics2.setColor(ColorRoutines.lighten(color, 8 * value2));
        graphics2.drawLine(0, 19, 1, 19);
        graphics2.setColor(ColorRoutines.lighten(color, 9 * value2));
        graphics2.drawLine(0, 20, 1, 20);
        graphics2.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics2.drawLine(0, 21, 1, 21);
        graphics2.setColor(ColorRoutines.lighten(color, 4 * value2));
        graphics2.drawLine(0, 22, 1, 22);
        graphics2.setColor(ColorRoutines.darken(color, 2 * value));
        graphics2.drawLine(0, 23, 1, 23);
        if (this.isActive) {
            graphics2.setColor(Theme.frameLightColor.getColor());
        } else {
            graphics2.setColor(Theme.frameLightDisabledColor.getColor());
        }
        graphics2.drawLine(0, 24, 1, 24);
        graphics2.dispose();
        graphics.drawImage(bufferedImage, i2 + 6, i3, (i2 + i4) - 6, i3 + 5, 0, 0, 1, 5, this.frame);
        graphics.drawImage(bufferedImage, i2 + 1, i3 + 5, (i2 + i4) - 1, i3 + this.titleHeight, 0, 5, 1, this.titleHeight, this.frame);
        cache.put(captionKey, bufferedImage);
    }

    private void drawXpCaptionNoCache(Graphics graphics, int i2, int i3, int i4, int i5, Color color) {
        ColorUIResource color2;
        if (this.isPalette) {
            drawXpPaletteCaptionNoCache(graphics, i2, i3, i4, i5, color);
            return;
        }
        int value = Theme.frameSpreadDarkDisabled.getValue();
        int value2 = Theme.frameSpreadLightDisabled.getValue();
        if (this.isActive) {
            color2 = Theme.frameBorderColor.getColor();
            value = Theme.frameSpreadDark.getValue();
            value2 = Theme.frameSpreadLight.getValue();
        } else {
            color2 = Theme.frameBorderDisabledColor.getColor();
        }
        graphics.setColor(color2);
        graphics.drawLine(i2 + 6, i3, (i2 + i4) - 7, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 82));
        graphics.drawLine(i2 + 3, i3, i2 + 3, i3);
        graphics.drawLine((i2 + i4) - 4, i3, (i2 + i4) - 4, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 156));
        graphics.drawLine(i2 + 4, i3, i2 + 4, i3);
        graphics.drawLine((i2 + i4) - 5, i3, (i2 + i4) - 5, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 215));
        graphics.drawLine(i2 + 5, i3, i2 + 5, i3);
        graphics.drawLine((i2 + i4) - 6, i3, (i2 + i4) - 6, i3);
        int i6 = i3 + 1;
        Color colorDarken = ColorRoutines.darken(color, 4 * value);
        graphics.setColor(colorDarken);
        graphics.drawLine(i2 + 3, i6, (i2 + i4) - 4, i6);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 139));
        graphics.drawLine(i2 + 2, i6, i2 + 2, i6);
        graphics.drawLine((i2 + i4) - 3, i6, (i2 + i4) - 3, i6);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 23));
        graphics.drawLine(i2 + 1, i6, i2 + 1, i6);
        graphics.drawLine((i2 + i4) - 2, i6, (i2 + i4) - 2, i6);
        int i7 = i6 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 4, i7, (i2 + i4) - 5, i7);
        graphics.setColor(color);
        graphics.drawLine(i2 + 3, i7, i2 + 3, i7);
        graphics.drawLine((i2 + i4) - 4, i7, (i2 + i4) - 4, i7);
        Color colorDarken2 = ColorRoutines.darken(color, 6 * value);
        graphics.setColor(colorDarken2);
        graphics.drawLine(i2 + 2, i7, i2 + 2, i7);
        graphics.drawLine((i2 + i4) - 3, i7, (i2 + i4) - 3, i7);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken2, 139));
        graphics.drawLine(i2 + 1, i7, i2 + 1, i7);
        graphics.drawLine((i2 + i4) - 2, i7, (i2 + i4) - 2, i7);
        int i8 = i7 + 1;
        graphics.setColor(color);
        graphics.drawLine(i2 + 7, i8, (i2 + i4) - 8, i8);
        graphics.setColor(color);
        graphics.drawLine(i2 + 2, i8, i2 + 2, i8);
        graphics.drawLine((i2 + i4) - 3, i8, (i2 + i4) - 3, i8);
        graphics.setColor(ColorRoutines.darken(color, 6 * value));
        graphics.drawLine(i2 + 1, i8, i2 + 1, i8);
        graphics.drawLine((i2 + i4) - 2, i8, (i2 + i4) - 2, i8);
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 3, i8, i2 + 3, i8);
        graphics.drawLine((i2 + i4) - 4, i8, (i2 + i4) - 4, i8);
        graphics.setColor(ColorRoutines.lighten(color, 7 * value2));
        graphics.drawLine(i2 + 4, i8, i2 + 4, i8);
        graphics.drawLine((i2 + i4) - 5, i8, (i2 + i4) - 5, i8);
        graphics.setColor(ColorRoutines.lighten(color, 3 * value2));
        graphics.drawLine(i2 + 5, i8, i2 + 5, i8);
        graphics.drawLine((i2 + i4) - 6, i8, (i2 + i4) - 6, i8);
        graphics.setColor(color);
        graphics.drawLine(i2 + 6, i8, i2 + 6, i8);
        graphics.drawLine((i2 + i4) - 7, i8, (i2 + i4) - 7, i8);
        int i9 = i8 + 1;
        graphics.setColor(ColorRoutines.darken(color, 2 * value));
        graphics.drawLine(i2 + 5, i9, (i2 + i4) - 6, i9);
        graphics.setColor(ColorRoutines.darken(color, 6 * value));
        graphics.drawLine(i2 + 1, i9, i2 + 1, i9);
        graphics.drawLine((i2 + i4) - 2, i9, (i2 + i4) - 2, i9);
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 2, i9, i2 + 2, i9);
        graphics.drawLine((i2 + i4) - 3, i9, (i2 + i4) - 3, i9);
        graphics.setColor(ColorRoutines.lighten(color, 5 * value2));
        graphics.drawLine(i2 + 3, i9, i2 + 3, i9);
        graphics.drawLine((i2 + i4) - 4, i9, (i2 + i4) - 4, i9);
        graphics.setColor(color);
        graphics.drawLine(i2 + 4, i9, i2 + 4, i9);
        graphics.drawLine((i2 + i4) - 5, i9, (i2 + i4) - 5, i9);
        int i10 = i9 + 1;
        if (this.isActive) {
            frameUpperColor = ColorRoutines.darken(color, 4 * value);
            graphics.setColor(frameUpperColor);
        } else {
            disabledUpperColor = ColorRoutines.darken(color, 4 * value);
            graphics.setColor(disabledUpperColor);
        }
        graphics.drawLine(i2 + 2, i10, (i2 + i4) - 3, i10);
        graphics.setColor(ColorRoutines.darken(color, 4 * value));
        graphics.drawLine(i2 + 1, i10, i2 + 1, i10);
        graphics.drawLine((i2 + i4) - 2, i10, (i2 + i4) - 2, i10);
        int i11 = i10 + 1;
        graphics.setColor(ColorRoutines.darken(color, 4 * value));
        graphics.fillRect(i2 + 1, i11, i4 - 2, 1);
        int i12 = i11 + 1;
        graphics.setColor(ColorRoutines.darken(color, 3 * value));
        graphics.fillRect(i2 + 1, i12, i4 - 2, 3);
        int i13 = i12 + 3;
        graphics.setColor(ColorRoutines.darken(color, 2 * value));
        graphics.fillRect(i2 + 1, i13, i4 - 2, 2);
        int i14 = i13 + 2;
        graphics.setColor(ColorRoutines.darken(color, 1 * value));
        graphics.fillRect(i2 + 1, i14, i4 - 2, 1);
        int i15 = i14 + 1;
        graphics.setColor(color);
        graphics.fillRect(i2 + 1, i15, i4 - 2, 2);
        int i16 = i15 + 2;
        graphics.setColor(ColorRoutines.lighten(color, 2 * value2));
        graphics.drawLine(i2 + 1, i16, (i2 + i4) - 2, i16);
        int i17 = i16 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 4 * value2));
        graphics.drawLine(i2 + 1, i17, (i2 + i4) - 2, i17);
        int i18 = i17 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 5 * value2));
        graphics.drawLine(i2 + 1, i18, (i2 + i4) - 2, i18);
        int i19 = i18 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 6 * value2));
        graphics.drawLine(i2 + 1, i19, (i2 + i4) - 2, i19);
        int i20 = i19 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 8 * value2));
        graphics.drawLine(i2 + 1, i20, (i2 + i4) - 2, i20);
        int i21 = i20 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 9 * value2));
        graphics.drawLine(i2 + 1, i21, (i2 + i4) - 2, i21);
        int i22 = i21 + 1;
        if (this.isActive) {
            frameLowerColor = ColorRoutines.lighten(color, 10 * value2);
            graphics.setColor(frameLowerColor);
        } else {
            disabledLowerColor = ColorRoutines.lighten(color, 10 * value2);
            graphics.setColor(disabledLowerColor);
        }
        graphics.drawLine(i2 + 1, i22, (i2 + i4) - 2, i22);
        int i23 = i22 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 4 * value2));
        graphics.drawLine(i2 + 1, i23, (i2 + i4) - 2, i23);
        int i24 = i23 + 1;
        graphics.setColor(ColorRoutines.darken(color, 2 * value));
        graphics.drawLine(i2 + 1, i24, (i2 + i4) - 2, i24);
        int i25 = i24 + 1;
        if (this.isActive) {
            graphics.setColor(Theme.frameLightColor.getColor());
        } else {
            graphics.setColor(Theme.frameLightDisabledColor.getColor());
        }
        graphics.drawLine(i2 + 1, i25, (i2 + i4) - 2, i25);
    }

    private void drawXpPaletteCaption(Graphics graphics, int i2, int i3, int i4, int i5, Color color) {
        ColorUIResource color2;
        int value = Theme.frameSpreadDarkDisabled.getValue();
        int value2 = Theme.frameSpreadLightDisabled.getValue();
        if (this.isActive) {
            color2 = Theme.frameBorderColor.getColor();
            value = Theme.frameSpreadDark.getValue();
            value2 = Theme.frameSpreadLight.getValue();
        } else {
            color2 = Theme.frameBorderDisabledColor.getColor();
        }
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 82));
        graphics.drawLine(i2 + 3, i3, i2 + 3, i3);
        graphics.drawLine((i2 + i4) - 4, i3, (i2 + i4) - 4, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 156));
        graphics.drawLine(i2 + 4, i3, i2 + 4, i3);
        graphics.drawLine((i2 + i4) - 5, i3, (i2 + i4) - 5, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 215));
        graphics.drawLine(i2 + 5, i3, i2 + 5, i3);
        graphics.drawLine((i2 + i4) - 6, i3, (i2 + i4) - 6, i3);
        int i6 = i3 + 1;
        Color colorDarken = ColorRoutines.darken(color, 4 * value);
        graphics.setColor(colorDarken);
        graphics.drawLine(i2 + 3, i6, i2 + 5, i6);
        graphics.drawLine((i2 + i4) - 6, i6, (i2 + i4) - 4, i6);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 139));
        graphics.drawLine(i2 + 2, i6, i2 + 2, i6);
        graphics.drawLine((i2 + i4) - 3, i6, (i2 + i4) - 3, i6);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 23));
        graphics.drawLine(i2 + 1, i6, i2 + 1, i6);
        graphics.drawLine((i2 + i4) - 2, i6, (i2 + i4) - 2, i6);
        int i7 = i6 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 4, i7, i2 + 5, i7);
        graphics.drawLine((i2 + i4) - 6, i7, (i2 + i4) - 5, i7);
        graphics.setColor(color);
        graphics.drawLine(i2 + 3, i7, i2 + 3, i7);
        graphics.drawLine((i2 + i4) - 4, i7, (i2 + i4) - 4, i7);
        Color colorDarken2 = ColorRoutines.darken(color, 6 * value);
        graphics.setColor(colorDarken2);
        graphics.drawLine(i2 + 2, i7, i2 + 2, i7);
        graphics.drawLine((i2 + i4) - 3, i7, (i2 + i4) - 3, i7);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken2, 139));
        graphics.drawLine(i2 + 1, i7, i2 + 1, i7);
        graphics.drawLine((i2 + i4) - 2, i7, (i2 + i4) - 2, i7);
        int i8 = i7 + 1;
        graphics.setColor(color);
        graphics.drawLine(i2 + 2, i8, i2 + 2, i8);
        graphics.drawLine((i2 + i4) - 3, i8, (i2 + i4) - 3, i8);
        graphics.setColor(ColorRoutines.darken(color, 6 * value));
        graphics.drawLine(i2 + 1, i8, i2 + 1, i8);
        graphics.drawLine((i2 + i4) - 2, i8, (i2 + i4) - 2, i8);
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 3, i8, i2 + 3, i8);
        graphics.drawLine((i2 + i4) - 4, i8, (i2 + i4) - 4, i8);
        graphics.setColor(ColorRoutines.lighten(color, 7 * value2));
        graphics.drawLine(i2 + 4, i8, i2 + 4, i8);
        graphics.drawLine((i2 + i4) - 5, i8, (i2 + i4) - 5, i8);
        graphics.setColor(ColorRoutines.lighten(color, 3 * value2));
        graphics.drawLine(i2 + 5, i8, i2 + 5, i8);
        graphics.drawLine((i2 + i4) - 6, i8, (i2 + i4) - 6, i8);
        graphics.setColor(color);
        graphics.drawLine(i2 + 6, i8, i2 + 6, i8);
        graphics.drawLine((i2 + i4) - 7, i8, (i2 + i4) - 7, i8);
        int i9 = i8 + 1;
        graphics.setColor(ColorRoutines.darken(color, 2 * value));
        graphics.drawLine(i2 + 5, i9, i2 + 6, i9);
        graphics.drawLine(((i2 + i2) + i4) - 7, i9, (i2 + i4) - 6, i9);
        graphics.setColor(ColorRoutines.darken(color, 6 * value));
        graphics.drawLine(i2 + 1, i9, i2 + 1, i9);
        graphics.drawLine((i2 + i4) - 2, i9, (i2 + i4) - 2, i9);
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 2, i9, i2 + 2, i9);
        graphics.drawLine((i2 + i4) - 3, i9, (i2 + i4) - 3, i9);
        graphics.setColor(ColorRoutines.lighten(color, 5 * value2));
        graphics.drawLine(i2 + 3, i9, i2 + 3, i9);
        graphics.drawLine((i2 + i4) - 4, i9, (i2 + i4) - 4, i9);
        graphics.setColor(color);
        graphics.drawLine(i2 + 4, i9, i2 + 4, i9);
        graphics.drawLine((i2 + i4) - 5, i9, (i2 + i4) - 5, i9);
        int i10 = i9 + 1;
        graphics.setColor(ColorRoutines.darken(color, 4 * value));
        graphics.drawLine(i2 + 1, i10, i2 + 1, i10);
        graphics.drawLine((i2 + i4) - 2, i10, (i2 + i4) - 2, i10);
        CaptionKey captionKey = new CaptionKey(this.isActive, this.titleHeight);
        Object obj = cache.get(captionKey);
        if (obj != null) {
            graphics.drawImage((Image) obj, i2 + 6, i3, (i2 + i4) - 6, i3 + 5, 0, 0, 1, 5, this.frame);
            graphics.drawImage((Image) obj, i2 + 1, i3 + 5, (i2 + i4) - 1, i3 + this.titleHeight, 0, 5, 1, this.titleHeight, this.frame);
            if (this.isActive) {
                frameUpperColor = ColorRoutines.darken(color, 4 * value);
                frameLowerColor = ColorRoutines.lighten(color, 10 * value2);
                return;
            } else {
                disabledUpperColor = ColorRoutines.darken(color, 4 * value);
                disabledLowerColor = ColorRoutines.lighten(color, 10 * value2);
                return;
            }
        }
        BufferedImage bufferedImage = new BufferedImage(1, this.titleHeight, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        graphics2.setColor(color2);
        graphics2.drawLine(0, 0, 1, 0);
        graphics2.setColor(ColorRoutines.darken(color, 4 * value));
        graphics2.drawLine(0, 1, 1, 1);
        graphics2.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics2.drawLine(0, 2, 1, 2);
        graphics2.setColor(color);
        graphics2.drawLine(0, 3, 1, 3);
        graphics2.setColor(ColorRoutines.darken(color, 2 * value));
        graphics2.drawLine(0, 4, 1, 4);
        graphics2.setColor(ColorRoutines.darken(color, 4 * value));
        graphics2.drawLine(0, 5, 1, 5);
        graphics2.drawLine(0, 6, 1, 6);
        graphics2.setColor(ColorRoutines.darken(color, 3 * value));
        graphics2.drawLine(0, 7, 1, 7);
        graphics2.setColor(ColorRoutines.darken(color, 2 * value));
        graphics2.drawLine(0, 8, 1, 8);
        graphics2.setColor(ColorRoutines.darken(color, value));
        graphics2.drawLine(0, 9, 1, 9);
        graphics2.setColor(color);
        graphics2.drawLine(0, 10, 1, 10);
        graphics2.setColor(ColorRoutines.lighten(color, 2 * value2));
        graphics2.drawLine(0, 11, 1, 11);
        graphics2.setColor(ColorRoutines.lighten(color, 4 * value2));
        graphics2.drawLine(0, 12, 1, 12);
        graphics2.setColor(ColorRoutines.lighten(color, 5 * value2));
        graphics2.drawLine(0, 13, 1, 13);
        graphics2.setColor(ColorRoutines.lighten(color, 6 * value2));
        graphics2.drawLine(0, 14, 1, 14);
        graphics2.setColor(ColorRoutines.lighten(color, 8 * value2));
        graphics2.drawLine(0, 15, 1, 15);
        graphics2.setColor(ColorRoutines.lighten(color, 9 * value2));
        graphics2.drawLine(0, 16, 1, 16);
        graphics2.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics2.drawLine(0, 17, 1, 17);
        graphics2.setColor(ColorRoutines.lighten(color, 4 * value2));
        graphics2.drawLine(0, 18, 1, 18);
        graphics2.setColor(ColorRoutines.darken(color, 2 * value));
        graphics2.drawLine(0, 19, 1, 19);
        if (this.isActive) {
            graphics2.setColor(Theme.frameLightColor.getColor());
        } else {
            graphics2.setColor(Theme.frameLightDisabledColor.getColor());
        }
        graphics2.drawLine(0, 20, 1, 20);
        graphics2.dispose();
        graphics.drawImage(bufferedImage, i2 + 6, i3, (i2 + i4) - 6, i3 + 5, 0, 0, 1, 5, this.frame);
        graphics.drawImage(bufferedImage, i2 + 1, i3 + 5, (i2 + i4) - 1, i3 + this.titleHeight, 0, 5, 1, this.titleHeight, this.frame);
        cache.put(captionKey, bufferedImage);
    }

    private void drawXpPaletteCaptionNoCache(Graphics graphics, int i2, int i3, int i4, int i5, Color color) {
        ColorUIResource color2;
        int value = Theme.frameSpreadDarkDisabled.getValue();
        int value2 = Theme.frameSpreadLightDisabled.getValue();
        if (this.isActive) {
            color2 = Theme.frameBorderColor.getColor();
            value = Theme.frameSpreadDark.getValue();
            value2 = Theme.frameSpreadLight.getValue();
        } else {
            color2 = Theme.frameBorderDisabledColor.getColor();
        }
        graphics.setColor(color2);
        graphics.drawLine(i2 + 6, i3, (i2 + i4) - 7, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 82));
        graphics.drawLine(i2 + 3, i3, i2 + 3, i3);
        graphics.drawLine((i2 + i4) - 4, i3, (i2 + i4) - 4, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 156));
        graphics.drawLine(i2 + 4, i3, i2 + 4, i3);
        graphics.drawLine((i2 + i4) - 5, i3, (i2 + i4) - 5, i3);
        graphics.setColor(ColorRoutines.getAlphaColor(color2, 215));
        graphics.drawLine(i2 + 5, i3, i2 + 5, i3);
        graphics.drawLine((i2 + i4) - 6, i3, (i2 + i4) - 6, i3);
        int i6 = i3 + 1;
        Color colorDarken = ColorRoutines.darken(color, 4 * value);
        graphics.setColor(colorDarken);
        graphics.drawLine(i2 + 3, i6, (i2 + i4) - 4, i6);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 139));
        graphics.drawLine(i2 + 2, i6, i2 + 2, i6);
        graphics.drawLine((i2 + i4) - 3, i6, (i2 + i4) - 3, i6);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken, 23));
        graphics.drawLine(i2 + 1, i6, i2 + 1, i6);
        graphics.drawLine((i2 + i4) - 2, i6, (i2 + i4) - 2, i6);
        int i7 = i6 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 4, i7, (i2 + i4) - 5, i7);
        graphics.setColor(color);
        graphics.drawLine(i2 + 3, i7, i2 + 3, i7);
        graphics.drawLine((i2 + i4) - 4, i7, (i2 + i4) - 4, i7);
        Color colorDarken2 = ColorRoutines.darken(color, 6 * value);
        graphics.setColor(colorDarken2);
        graphics.drawLine(i2 + 2, i7, i2 + 2, i7);
        graphics.drawLine((i2 + i4) - 3, i7, (i2 + i4) - 3, i7);
        graphics.setColor(ColorRoutines.getAlphaColor(colorDarken2, 139));
        graphics.drawLine(i2 + 1, i7, i2 + 1, i7);
        graphics.drawLine((i2 + i4) - 2, i7, (i2 + i4) - 2, i7);
        int i8 = i7 + 1;
        graphics.setColor(color);
        graphics.drawLine(i2 + 7, i8, (i2 + i4) - 8, i8);
        graphics.setColor(color);
        graphics.drawLine(i2 + 2, i8, i2 + 2, i8);
        graphics.drawLine((i2 + i4) - 3, i8, (i2 + i4) - 3, i8);
        graphics.setColor(ColorRoutines.darken(color, 6 * value));
        graphics.drawLine(i2 + 1, i8, i2 + 1, i8);
        graphics.drawLine((i2 + i4) - 2, i8, (i2 + i4) - 2, i8);
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 3, i8, i2 + 3, i8);
        graphics.drawLine((i2 + i4) - 4, i8, (i2 + i4) - 4, i8);
        graphics.setColor(ColorRoutines.lighten(color, 7 * value2));
        graphics.drawLine(i2 + 4, i8, i2 + 4, i8);
        graphics.drawLine((i2 + i4) - 5, i8, (i2 + i4) - 5, i8);
        graphics.setColor(ColorRoutines.lighten(color, 3 * value2));
        graphics.drawLine(i2 + 5, i8, i2 + 5, i8);
        graphics.drawLine((i2 + i4) - 6, i8, (i2 + i4) - 6, i8);
        graphics.setColor(color);
        graphics.drawLine(i2 + 6, i8, i2 + 6, i8);
        graphics.drawLine((i2 + i4) - 7, i8, (i2 + i4) - 7, i8);
        int i9 = i8 + 1;
        graphics.setColor(ColorRoutines.darken(color, 2 * value));
        graphics.drawLine(i2 + 5, i9, (i2 + i4) - 6, i9);
        graphics.setColor(ColorRoutines.darken(color, 6 * value));
        graphics.drawLine(i2 + 1, i9, i2 + 1, i9);
        graphics.drawLine((i2 + i4) - 2, i9, (i2 + i4) - 2, i9);
        graphics.setColor(ColorRoutines.lighten(color, 10 * value2));
        graphics.drawLine(i2 + 2, i9, i2 + 2, i9);
        graphics.drawLine((i2 + i4) - 3, i9, (i2 + i4) - 3, i9);
        graphics.setColor(ColorRoutines.lighten(color, 5 * value2));
        graphics.drawLine(i2 + 3, i9, i2 + 3, i9);
        graphics.drawLine((i2 + i4) - 4, i9, (i2 + i4) - 4, i9);
        graphics.setColor(color);
        graphics.drawLine(i2 + 4, i9, i2 + 4, i9);
        graphics.drawLine((i2 + i4) - 5, i9, (i2 + i4) - 5, i9);
        int i10 = i9 + 1;
        if (this.isActive) {
            frameUpperColor = ColorRoutines.darken(color, 4 * value);
            graphics.setColor(frameUpperColor);
        } else {
            disabledUpperColor = ColorRoutines.darken(color, 4 * value);
            graphics.setColor(disabledUpperColor);
        }
        graphics.drawLine(i2 + 2, i10, (i2 + i4) - 3, i10);
        graphics.setColor(ColorRoutines.darken(color, 4 * value));
        graphics.drawLine(i2 + 1, i10, i2 + 1, i10);
        graphics.drawLine((i2 + i4) - 2, i10, (i2 + i4) - 2, i10);
        int i11 = i10 + 1;
        graphics.setColor(ColorRoutines.darken(color, 4 * value));
        graphics.fillRect(i2 + 1, i11, i4 - 2, 1);
        int i12 = i11 + 1;
        graphics.setColor(ColorRoutines.darken(color, 3 * value));
        graphics.fillRect(i2 + 1, i12, i4 - 2, 1);
        int i13 = i12 + 1;
        graphics.setColor(ColorRoutines.darken(color, 2 * value));
        graphics.fillRect(i2 + 1, i13, i4 - 2, 1);
        int i14 = i13 + 1;
        graphics.setColor(ColorRoutines.darken(color, 1 * value));
        graphics.fillRect(i2 + 1, i14, i4 - 2, 1);
        int i15 = i14 + 1;
        graphics.setColor(color);
        graphics.fillRect(i2 + 1, i15, i4 - 2, 1);
        int i16 = i15 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 2 * value2));
        graphics.drawLine(i2 + 1, i16, (i2 + i4) - 2, i16);
        int i17 = i16 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 4 * value2));
        graphics.drawLine(i2 + 1, i17, (i2 + i4) - 2, i17);
        int i18 = i17 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 5 * value2));
        graphics.drawLine(i2 + 1, i18, (i2 + i4) - 2, i18);
        int i19 = i18 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 6 * value2));
        graphics.drawLine(i2 + 1, i19, (i2 + i4) - 2, i19);
        int i20 = i19 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 8 * value2));
        graphics.drawLine(i2 + 1, i20, (i2 + i4) - 2, i20);
        int i21 = i20 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 9 * value2));
        graphics.drawLine(i2 + 1, i21, (i2 + i4) - 2, i21);
        int i22 = i21 + 1;
        if (this.isActive) {
            frameLowerColor = ColorRoutines.lighten(color, 10 * value2);
            graphics.setColor(frameLowerColor);
        } else {
            disabledLowerColor = ColorRoutines.lighten(color, 10 * value2);
            graphics.setColor(disabledLowerColor);
        }
        graphics.drawLine(i2 + 1, i22, (i2 + i4) - 2, i22);
        int i23 = i22 + 1;
        graphics.setColor(ColorRoutines.lighten(color, 4 * value2));
        graphics.drawLine(i2 + 1, i23, (i2 + i4) - 2, i23);
        int i24 = i23 + 1;
        graphics.setColor(ColorRoutines.darken(color, 2 * value));
        graphics.drawLine(i2 + 1, i24, (i2 + i4) - 2, i24);
        int i25 = i24 + 1;
        if (this.isActive) {
            graphics.setColor(Theme.frameLightColor.getColor());
        } else {
            graphics.setColor(Theme.frameLightDisabledColor.getColor());
        }
        graphics.drawLine(i2 + 1, i25, (i2 + i4) - 2, i25);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return ((JInternalFrame) component).isMaximum() ? new Insets(0, 0, 0, 0) : new Insets(0, 3, 3, 3);
    }

    public void setActive(boolean z2) {
        this.isActive = z2;
    }
}
