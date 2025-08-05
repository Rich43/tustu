package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.util.ColorRoutines;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.plaf.ColorUIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRadioButtonIcon.class */
class TinyRadioButtonIcon extends TinyCheckBoxIcon {
    private static final HashMap cache = new HashMap();
    private static final int[][] ALPHA = {new int[]{255, 255, 255, 242, 228, 209, 187, 165, 142, 255, 255}, new int[]{255, 255, 242, 228, 209, 187, 165, 142, 120, 104, 255}, new int[]{255, 242, 228, 209, 187, 165, 142, 120, 104, 86, 72}, new int[]{242, 228, 209, 187, 165, 142, 120, 104, 86, 72, 56}, new int[]{228, 209, 187, 165, 142, 120, 104, 86, 72, 56, 42}, new int[]{209, 187, 165, 142, 120, 104, 86, 72, 56, 42, 28}, new int[]{187, 165, 142, 120, 104, 86, 72, 56, 42, 28, 17}, new int[]{165, 142, 120, 104, 86, 72, 56, 42, 28, 17, 9}, new int[]{142, 120, 104, 86, 72, 56, 42, 28, 17, 9, 0}, new int[]{255, 104, 86, 72, 56, 42, 28, 17, 9, 0, 255}, new int[]{255, 255, 72, 56, 42, 28, 17, 9, 0, 255, 255}};

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRadioButtonIcon$DisabledRadioKey.class */
    private static class DisabledRadioKey {
        int spread1 = Theme.buttonSpreadLightDisabled.getValue();
        int spread2 = Theme.buttonSpreadDarkDisabled.getValue();

        /* renamed from: c, reason: collision with root package name */
        Color f12131c;
        Color back;

        DisabledRadioKey(Color color, Color color2) {
            this.f12131c = color;
            this.back = color2;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof DisabledRadioKey)) {
                return false;
            }
            DisabledRadioKey disabledRadioKey = (DisabledRadioKey) obj;
            return this.f12131c.equals(disabledRadioKey.f12131c) && this.back.equals(disabledRadioKey.back) && this.spread1 == disabledRadioKey.spread1 && this.spread2 == disabledRadioKey.spread2;
        }

        public int hashCode() {
            return this.f12131c.hashCode() * this.back.hashCode() * this.spread1 * this.spread2;
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRadioButtonIcon$EnabledRadioKey.class */
    private static class EnabledRadioKey {
        int spread1 = Theme.buttonSpreadLight.getValue();
        int spread2 = Theme.buttonSpreadDark.getValue();

        /* renamed from: c, reason: collision with root package name */
        Color f12132c;
        Color back;

        EnabledRadioKey(Color color, Color color2) {
            this.f12132c = color;
            this.back = color2;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof EnabledRadioKey)) {
                return false;
            }
            EnabledRadioKey enabledRadioKey = (EnabledRadioKey) obj;
            return this.f12132c.equals(enabledRadioKey.f12132c) && this.back.equals(enabledRadioKey.back) && this.spread1 == enabledRadioKey.spread1 && this.spread2 == enabledRadioKey.spread2;
        }

        public int hashCode() {
            return this.f12132c.hashCode() * this.back.hashCode() * this.spread1 * this.spread2;
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRadioButtonIcon$RadioKey.class */
    private static class RadioKey {

        /* renamed from: c, reason: collision with root package name */
        private Color f12133c;
        private Color background;
        private boolean pressed;
        private boolean enabled;
        private boolean rollover;
        private boolean focused;

        RadioKey(Color color, Color color2, boolean z2, boolean z3, boolean z4, boolean z5) {
            this.f12133c = color;
            this.background = color2;
            this.pressed = z2;
            this.enabled = z3;
            this.rollover = z4;
            this.focused = z5;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof RadioKey)) {
                return false;
            }
            RadioKey radioKey = (RadioKey) obj;
            return this.pressed == radioKey.pressed && this.enabled == radioKey.enabled && this.rollover == radioKey.rollover && this.focused == radioKey.focused && this.f12133c.equals(radioKey.f12133c) && this.background.equals(radioKey.background);
        }

        public int hashCode() {
            return this.f12133c.hashCode() * this.background.hashCode() * (this.pressed ? 1 : 2) * (this.enabled ? 4 : 8) * (this.rollover ? 16 : 32);
        }
    }

    TinyRadioButtonIcon() {
    }

    public static void clearCache() {
        cache.clear();
    }

    @Override // de.muntjak.tinylookandfeel.TinyCheckBoxIcon, javax.swing.plaf.metal.MetalCheckBoxIcon, javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        AbstractButton abstractButton = (AbstractButton) component;
        ColorUIResource color = !abstractButton.isEnabled() ? Theme.buttonDisabledColor.getColor() : abstractButton.getModel().isPressed() ? (abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed()) ? Theme.buttonPressedColor.getColor() : Theme.buttonNormalColor.getColor() : abstractButton.getModel().isRollover() ? Theme.buttonRolloverBgColor.getColor() : Theme.buttonNormalColor.getColor();
        graphics.setColor(color);
        if (TinyLookAndFeel.controlPanelInstantiated) {
            drawXpRadioNoCache(graphics, abstractButton, color, i2, i3, getIconWidth(), getIconHeight());
        } else {
            drawXpRadio(graphics, abstractButton, color, i2, i3, getIconWidth(), getIconHeight());
        }
        if (abstractButton.isSelected()) {
            ColorUIResource color2 = !abstractButton.isEnabled() ? Theme.buttonCheckDisabledColor.getColor() : Theme.buttonCheckColor.getColor();
            graphics.setColor(color2);
            drawXpCheckMark(graphics, color2, i2, i3);
        }
    }

    private void drawXpRadio(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, int i4, int i5) {
        boolean zIsPressed = abstractButton.getModel().isPressed();
        boolean zIsArmed = abstractButton.getModel().isArmed();
        boolean zIsEnabled = abstractButton.isEnabled();
        boolean zIsRollover = abstractButton.getModel().isRollover();
        boolean z2 = Theme.buttonFocusBorder.getValue() && !zIsRollover && abstractButton.isFocusOwner();
        Color background = abstractButton.getBackground();
        if (!abstractButton.isOpaque()) {
            Container parent = abstractButton.getParent();
            Color background2 = parent.getBackground();
            while (true) {
                background = background2;
                if (parent == null || parent.isOpaque()) {
                    break;
                }
                parent = parent.getParent();
                background2 = parent.getBackground();
            }
        }
        RadioKey radioKey = new RadioKey(color, background, zIsPressed, zIsEnabled, zIsRollover || zIsArmed, z2);
        Object obj = cache.get(radioKey);
        if (obj != null) {
            graphics.drawImage((Image) obj, i2, i3, abstractButton);
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(i4, i5, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        graphics2.setColor(background);
        graphics2.fillRect(0, 0, i4, i5);
        int value = Theme.buttonSpreadLight.getValue();
        int value2 = Theme.buttonSpreadDark.getValue();
        if (!zIsEnabled) {
            value = Theme.buttonSpreadLightDisabled.getValue();
            value2 = Theme.buttonSpreadDarkDisabled.getValue();
        }
        int i6 = value * 5;
        int i7 = value2 * 4;
        if (zIsPressed && (zIsRollover || zIsArmed)) {
            i7 *= 2;
        }
        Color colorLighten = ColorRoutines.lighten(color, i6);
        graphics2.setColor(ColorRoutines.darken(colorLighten, i7));
        graphics2.fillRect(2, 2, i4 - 4, i5 - 4);
        graphics2.fillRect(1, 5, 1, 3);
        graphics2.fillRect(11, 5, 1, 3);
        graphics2.fillRect(5, 1, 3, 1);
        graphics2.fillRect(5, 11, 3, 1);
        for (int i8 = 0; i8 < 11; i8++) {
            for (int i9 = 0; i9 < 11; i9++) {
                graphics2.setColor(new Color(colorLighten.getRed(), colorLighten.getGreen(), colorLighten.getBlue(), 255 - ALPHA[i9][i8]));
                graphics2.drawLine(i9 + 1, i8 + 1, i9 + 1, i8 + 1);
            }
        }
        if (zIsEnabled) {
            if (zIsRollover && Theme.buttonRolloverBorder.getValue() && !zIsPressed) {
                DrawRoutines.drawXpRadioRolloverBorder(graphics2, Theme.buttonRolloverColor.getColor(), 0, 0, i4, i5);
            } else if (z2 && !zIsPressed) {
                DrawRoutines.drawXpRadioRolloverBorder(graphics2, Theme.buttonDefaultColor.getColor(), 0, 0, i4, i5);
            }
            DrawRoutines.drawXpRadioBorder(graphics2, Theme.buttonBorderColor.getColor(), 0, 0, i4, i5);
        } else {
            DrawRoutines.drawXpRadioBorder(graphics2, Theme.buttonBorderDisabledColor.getColor(), 0, 0, i4, i5);
        }
        graphics2.dispose();
        graphics.drawImage(bufferedImage, i2, i3, abstractButton);
        cache.put(radioKey, bufferedImage);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v109, types: [de.muntjak.tinylookandfeel.TinyRadioButtonIcon$EnabledRadioKey] */
    private void drawXpRadioNoCache(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, int i4, int i5) {
        Graphics graphics2;
        boolean zIsPressed = abstractButton.getModel().isPressed();
        boolean zIsArmed = abstractButton.getModel().isArmed();
        boolean zIsEnabled = abstractButton.isEnabled();
        boolean zIsRollover = abstractButton.getModel().isRollover();
        boolean z2 = Theme.buttonFocusBorder.getValue() && !zIsRollover && abstractButton.isFocusOwner();
        BufferedImage bufferedImage = null;
        DisabledRadioKey enabledRadioKey = null;
        if ((zIsPressed || zIsArmed || zIsRollover || z2) ? false : true) {
            enabledRadioKey = zIsEnabled ? new EnabledRadioKey(color, Theme.buttonBorderColor.getColor()) : new DisabledRadioKey(color, Theme.buttonBorderDisabledColor.getColor());
            Object obj = cache.get(enabledRadioKey);
            if (obj != null) {
                graphics.drawImage((Image) obj, i2, i3, abstractButton);
                return;
            }
            bufferedImage = new BufferedImage(i4, i5, 2);
        }
        int value = Theme.buttonSpreadLight.getValue();
        int value2 = Theme.buttonSpreadDark.getValue();
        if (!zIsEnabled) {
            value = Theme.buttonSpreadLightDisabled.getValue();
            value2 = Theme.buttonSpreadDarkDisabled.getValue();
        }
        int i6 = value * 5;
        int i7 = value2 * 4;
        if (zIsPressed && (zIsRollover || zIsArmed)) {
            i7 *= 2;
        }
        Color colorLighten = ColorRoutines.lighten(color, i6);
        int i8 = i2;
        int i9 = i3;
        if (bufferedImage != null) {
            graphics2 = bufferedImage.getGraphics();
            Color background = abstractButton.getBackground();
            if (!abstractButton.isOpaque()) {
                Container parent = abstractButton.getParent();
                Color background2 = parent.getBackground();
                while (true) {
                    background = background2;
                    if (parent == null || parent.isOpaque()) {
                        break;
                    }
                    parent = parent.getParent();
                    background2 = parent.getBackground();
                }
            }
            graphics2.setColor(background);
            graphics2.fillRect(0, 0, i4 - 1, i5 - 1);
            i8 = 0;
            i9 = 0;
        } else {
            graphics2 = graphics;
            graphics2.translate(i2, i3);
        }
        graphics2.setColor(ColorRoutines.darken(colorLighten, i7));
        graphics2.fillRect(2, 2, i4 - 4, i5 - 4);
        graphics2.fillRect(1, 5, 1, 3);
        graphics2.fillRect(11, 5, 1, 3);
        graphics2.fillRect(6, 1, 1, 1);
        graphics2.fillRect(6, 11, 1, 1);
        for (int i10 = 0; i10 < 11; i10++) {
            for (int i11 = 0; i11 < 11; i11++) {
                graphics2.setColor(new Color(colorLighten.getRed(), colorLighten.getGreen(), colorLighten.getBlue(), 255 - ALPHA[i11][i10]));
                graphics2.drawLine(i11 + 1, i10 + 1, i11 + 1, i10 + 1);
            }
        }
        if (bufferedImage == null) {
            graphics2.translate(-i2, -i3);
        }
        if (zIsEnabled) {
            if (zIsRollover && Theme.buttonRolloverBorder.getValue() && !zIsPressed) {
                DrawRoutines.drawXpRadioRolloverBorder(graphics2, Theme.buttonRolloverColor.getColor(), i8, i9, i4, i5);
            } else if (z2 && !zIsPressed) {
                DrawRoutines.drawXpRadioRolloverBorder(graphics2, Theme.buttonDefaultColor.getColor(), i8, i9, i4, i5);
            }
            DrawRoutines.drawXpRadioBorder(graphics2, Theme.buttonBorderColor.getColor(), i8, i9, i4, i5);
        } else {
            DrawRoutines.drawXpRadioBorder(graphics2, Theme.buttonBorderDisabledColor.getColor(), i8, i9, i4, i5);
        }
        if (bufferedImage != null) {
            graphics2.dispose();
            graphics.drawImage(bufferedImage, i2, i3, abstractButton);
            cache.put(enabledRadioKey, bufferedImage);
        }
    }

    private void drawXpCheckMark(Graphics graphics, Color color, int i2, int i3) {
        graphics.translate(i2, i3);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 224));
        graphics.fillRect(5, 5, 3, 3);
        graphics.setColor(color);
        graphics.drawLine(6, 6, 6, 6);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 192));
        graphics.drawLine(6, 4, 6, 4);
        graphics.drawLine(4, 6, 4, 6);
        graphics.drawLine(8, 6, 8, 6);
        graphics.drawLine(6, 8, 6, 8);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 128));
        graphics.drawLine(5, 4, 5, 4);
        graphics.drawLine(7, 4, 7, 4);
        graphics.drawLine(4, 5, 4, 5);
        graphics.drawLine(8, 5, 8, 5);
        graphics.drawLine(4, 7, 4, 7);
        graphics.drawLine(8, 7, 8, 7);
        graphics.drawLine(5, 8, 5, 8);
        graphics.drawLine(7, 8, 7, 8);
        graphics.translate(-i2, -i3);
    }
}
