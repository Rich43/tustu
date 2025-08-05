package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicArrowButton;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyScrollButton.class */
public class TinyScrollButton extends BasicArrowButton {
    private static final HashMap cache = new HashMap();
    private boolean isRollover;
    private TinyScrollBarUI scrollbarUI;

    /* renamed from: de.muntjak.tinylookandfeel.TinyScrollButton$1, reason: invalid class name */
    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyScrollButton$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyScrollButton$ScrollButtonKey.class */
    private static class ScrollButtonKey {
        private Dimension size;

        /* renamed from: c, reason: collision with root package name */
        private Color f12135c;
        private boolean vertical;
        private boolean pressed;
        private boolean enabled;
        private boolean rollover;

        private ScrollButtonKey(Dimension dimension, boolean z2, Color color, boolean z3, boolean z4, boolean z5) {
            this.size = dimension;
            this.vertical = z2;
            this.f12135c = color;
            this.pressed = z3;
            this.enabled = z4;
            this.rollover = z5;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ScrollButtonKey)) {
                return false;
            }
            ScrollButtonKey scrollButtonKey = (ScrollButtonKey) obj;
            return this.size.equals(scrollButtonKey.size) && this.vertical == scrollButtonKey.vertical && this.pressed == scrollButtonKey.pressed && this.enabled == scrollButtonKey.enabled && this.rollover == scrollButtonKey.rollover && this.f12135c.equals(scrollButtonKey.f12135c);
        }

        public int hashCode() {
            return this.size.hashCode() * this.f12135c.hashCode() * (this.pressed ? 1 : 2) * (this.enabled ? 4 : 8) * (this.rollover ? 16 : 32) * (this.vertical ? 64 : 128);
        }

        ScrollButtonKey(Dimension dimension, boolean z2, Color color, boolean z3, boolean z4, boolean z5, AnonymousClass1 anonymousClass1) {
            this(dimension, z2, color, z3, z4, z5);
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    public TinyScrollButton(int i2, TinyScrollBarUI tinyScrollBarUI) {
        super(i2);
        this.scrollbarUI = tinyScrollBarUI;
        setBorder(null);
        setRolloverEnabled(true);
        setMargin(new Insets(0, 0, 0, 0));
        setSize(getPreferredSize());
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        ColorUIResource color;
        boolean zIsThumbVisible = this.scrollbarUI.isThumbVisible();
        this.isRollover = false;
        if (!zIsThumbVisible) {
            color = Theme.scrollButtDisabledColor.getColor();
        } else if (getModel().isPressed()) {
            color = Theme.scrollButtPressedColor.getColor();
        } else if (getModel().isRollover() && Theme.scrollRollover.getValue()) {
            color = Theme.scrollButtRolloverColor.getColor();
            this.isRollover = true;
        } else {
            color = Theme.scrollButtColor.getColor();
        }
        graphics.setColor(color);
        if (TinyLookAndFeel.controlPanelInstantiated) {
            drawXpButtonNoCache(graphics, getSize(), color);
        } else {
            drawXpButton(graphics, getSize(), color);
        }
        if (zIsThumbVisible) {
            graphics.setColor(Theme.scrollArrowColor.getColor());
        } else {
            graphics.setColor(Theme.scrollArrowDisabledColor.getColor());
        }
        drawXpArrow(graphics, getSize());
    }

    private void drawXpButton(Graphics graphics, Dimension dimension, Color color) {
        boolean zIsThumbVisible = this.scrollbarUI.isThumbVisible();
        ScrollButtonKey scrollButtonKey = new ScrollButtonKey(dimension, this.direction == 1 || this.direction == 5, color, getModel().isPressed(), zIsThumbVisible, getModel().isRollover() && Theme.scrollRollover.getValue(), null);
        Object obj = cache.get(scrollButtonKey);
        if (obj != null) {
            graphics.drawImage((Image) obj, 0, 0, this);
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(dimension.width, dimension.height, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        int i2 = dimension.height;
        int i3 = dimension.width;
        int value = Theme.scrollSpreadLight.getValue();
        int value2 = Theme.scrollSpreadDark.getValue();
        if (!zIsThumbVisible) {
            value = Theme.scrollSpreadLightDisabled.getValue();
            value2 = Theme.scrollSpreadDarkDisabled.getValue();
        }
        switch (this.direction) {
            case 1:
            case 5:
                float f2 = (10.0f * value) / 10.0f;
                float f3 = (10.0f * value2) / 10.0f;
                int i4 = (i2 * 3) / 8;
                for (int i5 = 1; i5 < i2 - 1; i5++) {
                    if (i5 < i4) {
                        graphics2.setColor(ColorRoutines.lighten(color, (int) ((i4 - i5) * f2)));
                    } else if (i5 == i4) {
                        graphics2.setColor(color);
                    } else {
                        graphics2.setColor(ColorRoutines.darken(color, (int) ((i5 - i4) * f3)));
                    }
                    graphics2.drawLine(3, i5, i3 - 3, i5);
                }
                graphics2.setColor(Theme.scrollTrackBorderColor.getColor());
                graphics2.drawLine(0, 0, 0, i2 - 1);
                ColorUIResource color2 = !zIsThumbVisible ? Theme.scrollLightDisabledColor.getColor() : Theme.scrollBorderLightColor.getColor();
                graphics2.setColor(color2);
                graphics2.drawLine(2, 1, 2, i2 - 2);
                graphics2.drawLine(i3 - 2, 1, i3 - 2, i2 - 2);
                Color color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), 92);
                ColorUIResource color4 = !zIsThumbVisible ? Theme.scrollBorderDisabledColor.getColor() : Theme.scrollBorderColor.getColor();
                graphics2.setColor(color4);
                graphics2.drawRect(1, 0, i3 - 2, i2 - 1);
                graphics2.setColor(new Color(color4.getRed(), color4.getGreen(), color4.getBlue(), 92));
                graphics2.drawLine(2, 1, 2, 1);
                graphics2.drawLine(i3 - 2, 1, i3 - 2, 1);
                graphics2.drawLine(2, i2 - 2, 2, i2 - 2);
                graphics2.drawLine(i3 - 2, i2 - 2, i3 - 2, i2 - 2);
                graphics2.setColor(color3);
                graphics2.drawLine(1, 0, 1, 0);
                graphics2.drawLine(1, i2 - 1, 1, i2 - 1);
                graphics2.drawLine(i3 - 1, 0, i3 - 1, 0);
                graphics2.drawLine(i3 - 1, i2 - 1, i3 - 1, i2 - 1);
                break;
            case 3:
            case 7:
                float f4 = (10.0f * value) / 10.0f;
                float f5 = (10.0f * value2) / 10.0f;
                int i6 = i2 / 2;
                for (int i7 = 1; i7 < i3 - 1; i7++) {
                    if (i7 < i6) {
                        graphics2.setColor(ColorRoutines.lighten(color, (int) ((i6 - i7) * f4)));
                    } else if (i7 == i6) {
                        graphics2.setColor(color);
                    } else {
                        graphics2.setColor(ColorRoutines.darken(color, (int) ((i7 - i6) * f5)));
                    }
                    graphics2.drawLine(2, i7 + 1, i3 - 3, i7 + 1);
                }
                graphics2.setColor(Theme.scrollTrackBorderColor.getColor());
                graphics2.drawLine(0, 0, i3 - 1, 0);
                ColorUIResource color5 = !zIsThumbVisible ? Theme.scrollLightDisabledColor.getColor() : Theme.scrollBorderLightColor.getColor();
                graphics2.setColor(color5);
                graphics2.drawLine(1, 2, 1, i2 - 2);
                graphics2.drawLine(i3 - 2, 2, i3 - 2, i2 - 2);
                Color color6 = new Color(color5.getRed(), color5.getGreen(), color5.getBlue(), 92);
                ColorUIResource color7 = !zIsThumbVisible ? Theme.scrollBorderDisabledColor.getColor() : Theme.scrollBorderColor.getColor();
                graphics2.setColor(color7);
                graphics2.drawRect(0, 1, i3 - 1, i2 - 2);
                graphics2.setColor(new Color(color7.getRed(), color7.getGreen(), color7.getBlue(), 92));
                graphics2.drawLine(1, 2, 1, 2);
                graphics2.drawLine(i3 - 2, 2, i3 - 2, 2);
                graphics2.drawLine(1, i2 - 2, 1, i2 - 2);
                graphics2.drawLine(i3 - 2, i2 - 2, i3 - 2, i2 - 2);
                graphics2.setColor(color6);
                graphics2.drawLine(0, 1, 0, 1);
                graphics2.drawLine(i3 - 1, 1, i3 - 1, 1);
                graphics2.drawLine(0, i2 - 1, 0, i2 - 1);
                graphics2.drawLine(i3 - 1, i2 - 1, i3 - 1, i2 - 1);
                break;
        }
        graphics2.dispose();
        graphics.drawImage(bufferedImage, 0, 0, this);
        cache.put(scrollButtonKey, bufferedImage);
    }

    private void drawXpButtonNoCache(Graphics graphics, Dimension dimension, Color color) {
        boolean zIsThumbVisible = this.scrollbarUI.isThumbVisible();
        int i2 = dimension.height;
        int i3 = dimension.width;
        int value = Theme.scrollSpreadLight.getValue();
        int value2 = Theme.scrollSpreadDark.getValue();
        if (!zIsThumbVisible) {
            value = Theme.scrollSpreadLightDisabled.getValue();
            value2 = Theme.scrollSpreadDarkDisabled.getValue();
        }
        switch (this.direction) {
            case 1:
            case 5:
                float f2 = (10.0f * value) / 10.0f;
                float f3 = (10.0f * value2) / 10.0f;
                int i4 = (i2 * 3) / 8;
                for (int i5 = 1; i5 < i2 - 1; i5++) {
                    if (i5 < i4) {
                        graphics.setColor(ColorRoutines.lighten(color, (int) ((i4 - i5) * f2)));
                    } else if (i5 == i4) {
                        graphics.setColor(color);
                    } else {
                        graphics.setColor(ColorRoutines.darken(color, (int) ((i5 - i4) * f3)));
                    }
                    graphics.drawLine(3, i5, i3 - 3, i5);
                }
                graphics.setColor(Theme.scrollTrackBorderColor.getColor());
                graphics.drawLine(0, 0, 0, i2 - 1);
                ColorUIResource color2 = !zIsThumbVisible ? Theme.scrollLightDisabledColor.getColor() : Theme.scrollBorderLightColor.getColor();
                graphics.setColor(color2);
                graphics.drawLine(2, 1, 2, i2 - 2);
                graphics.drawLine(i3 - 2, 1, i3 - 2, i2 - 2);
                Color color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), 92);
                ColorUIResource color4 = !zIsThumbVisible ? Theme.scrollBorderDisabledColor.getColor() : Theme.scrollBorderColor.getColor();
                graphics.setColor(color4);
                graphics.drawRect(1, 0, i3 - 2, i2 - 1);
                graphics.setColor(new Color(color4.getRed(), color4.getGreen(), color4.getBlue(), 92));
                graphics.drawLine(2, 1, 2, 1);
                graphics.drawLine(i3 - 2, 1, i3 - 2, 1);
                graphics.drawLine(2, i2 - 2, 2, i2 - 2);
                graphics.drawLine(i3 - 2, i2 - 2, i3 - 2, i2 - 2);
                graphics.setColor(color3);
                graphics.drawLine(1, 0, 1, 0);
                graphics.drawLine(1, i2 - 1, 1, i2 - 1);
                graphics.drawLine(i3 - 1, 0, i3 - 1, 0);
                graphics.drawLine(i3 - 1, i2 - 1, i3 - 1, i2 - 1);
                break;
            case 3:
            case 7:
                float f4 = (10.0f * value) / 10.0f;
                float f5 = (10.0f * value2) / 10.0f;
                int i6 = i2 / 2;
                for (int i7 = 1; i7 < i3 - 1; i7++) {
                    if (i7 < i6) {
                        graphics.setColor(ColorRoutines.lighten(color, (int) ((i6 - i7) * f4)));
                    } else if (i7 == i6) {
                        graphics.setColor(color);
                    } else {
                        graphics.setColor(ColorRoutines.darken(color, (int) ((i7 - i6) * f5)));
                    }
                    graphics.drawLine(2, i7 + 1, i3 - 3, i7 + 1);
                }
                graphics.setColor(Theme.scrollTrackBorderColor.getColor());
                graphics.drawLine(0, 0, i3 - 1, 0);
                ColorUIResource color5 = !zIsThumbVisible ? Theme.scrollLightDisabledColor.getColor() : Theme.scrollBorderLightColor.getColor();
                graphics.setColor(color5);
                graphics.drawLine(1, 2, 1, i2 - 2);
                graphics.drawLine(i3 - 2, 2, i3 - 2, i2 - 2);
                Color color6 = new Color(color5.getRed(), color5.getGreen(), color5.getBlue(), 92);
                ColorUIResource color7 = !zIsThumbVisible ? Theme.scrollBorderDisabledColor.getColor() : Theme.scrollBorderColor.getColor();
                graphics.setColor(color7);
                graphics.drawRect(0, 1, i3 - 1, i2 - 2);
                graphics.setColor(new Color(color7.getRed(), color7.getGreen(), color7.getBlue(), 92));
                graphics.drawLine(1, 2, 1, 2);
                graphics.drawLine(i3 - 2, 2, i3 - 2, 2);
                graphics.drawLine(1, i2 - 2, 1, i2 - 2);
                graphics.drawLine(i3 - 2, i2 - 2, i3 - 2, i2 - 2);
                graphics.setColor(color6);
                graphics.drawLine(0, 1, 0, 1);
                graphics.drawLine(i3 - 1, 1, i3 - 1, 1);
                graphics.drawLine(0, i2 - 1, 0, i2 - 1);
                graphics.drawLine(i3 - 1, i2 - 1, i3 - 1, i2 - 1);
                break;
        }
    }

    private void drawXpArrow(Graphics graphics, Dimension dimension) {
        int i2 = 0;
        int i3 = 0;
        switch (this.direction) {
            case 1:
                i2 = (dimension.width - 8) / 2;
                i3 = (dimension.height - 5) / 2;
                graphics.translate(i2, i3);
                graphics.drawLine(4, 0, 4, 0);
                graphics.drawLine(3, 1, 5, 1);
                graphics.drawLine(2, 2, 6, 2);
                graphics.drawLine(1, 3, 3, 3);
                graphics.drawLine(5, 3, 7, 3);
                graphics.drawLine(0, 4, 2, 4);
                graphics.drawLine(6, 4, 8, 4);
                graphics.drawLine(1, 5, 1, 5);
                graphics.drawLine(7, 5, 7, 5);
                break;
            case 3:
                i2 = (dimension.width - 5) / 2;
                i3 = (dimension.height - 8) / 2;
                graphics.translate(i2, i3);
                graphics.drawLine(0, 1, 0, 1);
                graphics.drawLine(0, 7, 0, 7);
                graphics.drawLine(1, 0, 1, 2);
                graphics.drawLine(1, 6, 1, 8);
                graphics.drawLine(2, 1, 2, 3);
                graphics.drawLine(2, 5, 2, 7);
                graphics.drawLine(3, 2, 3, 6);
                graphics.drawLine(4, 3, 4, 5);
                graphics.drawLine(5, 4, 5, 4);
                break;
            case 5:
                i2 = (dimension.width - 8) / 2;
                i3 = (dimension.height - 5) / 2;
                graphics.translate(i2, i3);
                graphics.drawLine(1, 0, 1, 0);
                graphics.drawLine(7, 0, 7, 0);
                graphics.drawLine(0, 1, 2, 1);
                graphics.drawLine(6, 1, 8, 1);
                graphics.drawLine(1, 2, 3, 2);
                graphics.drawLine(5, 2, 7, 2);
                graphics.drawLine(2, 3, 6, 3);
                graphics.drawLine(3, 4, 5, 4);
                graphics.drawLine(4, 5, 4, 5);
                break;
            case 7:
                i2 = (dimension.width - 5) / 2;
                i3 = (dimension.height - 8) / 2;
                graphics.translate(i2, i3);
                graphics.drawLine(0, 4, 0, 4);
                graphics.drawLine(1, 3, 1, 5);
                graphics.drawLine(2, 2, 2, 6);
                graphics.drawLine(3, 1, 3, 3);
                graphics.drawLine(3, 5, 3, 7);
                graphics.drawLine(4, 0, 4, 2);
                graphics.drawLine(4, 6, 4, 8);
                graphics.drawLine(5, 1, 5, 1);
                graphics.drawLine(5, 7, 5, 7);
                break;
        }
        graphics.translate(-i2, -i3);
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(Theme.scrollSize.getValue(), Theme.scrollSize.getValue());
    }
}
