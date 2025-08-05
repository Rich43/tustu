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
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.plaf.metal.MetalCheckBoxIcon;
import javax.swing.table.TableCellRenderer;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyCheckBoxIcon.class */
public class TinyCheckBoxIcon extends MetalCheckBoxIcon {
    private static final HashMap cache = new HashMap();
    private static int checkSize = 13;

    /* renamed from: a, reason: collision with root package name */
    private static final int[][] f12124a = {new int[]{255, 255, 255, 242, 228, 209, 187, 165, 142, 120, 104}, new int[]{255, 255, 242, 228, 209, 187, 165, 142, 120, 104, 86}, new int[]{255, 242, 228, 209, 187, 165, 142, 120, 104, 86, 72}, new int[]{242, 228, 209, 187, 165, 142, 120, 104, 86, 72, 56}, new int[]{228, 209, 187, 165, 142, 120, 104, 86, 72, 56, 42}, new int[]{209, 187, 165, 142, 120, 104, 86, 72, 56, 42, 28}, new int[]{187, 165, 142, 120, 104, 86, 72, 56, 42, 28, 17}, new int[]{165, 142, 120, 104, 86, 72, 56, 42, 28, 17, 9}, new int[]{142, 120, 104, 86, 72, 56, 42, 28, 17, 9, 0}, new int[]{120, 104, 86, 72, 56, 42, 28, 17, 9, 0, 0}, new int[]{104, 86, 72, 56, 42, 28, 17, 9, 0, 0, 0}};

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyCheckBoxIcon$CheckKey.class */
    private static class CheckKey {

        /* renamed from: c, reason: collision with root package name */
        private Color f12125c;
        private boolean pressed;
        private boolean enabled;
        private boolean rollover;
        private boolean focused;

        CheckKey(Color color, boolean z2, boolean z3, boolean z4, boolean z5) {
            this.f12125c = color;
            this.pressed = z2;
            this.enabled = z3;
            this.rollover = z4;
            this.focused = z5;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof CheckKey)) {
                return false;
            }
            CheckKey checkKey = (CheckKey) obj;
            return this.pressed == checkKey.pressed && this.enabled == checkKey.enabled && this.rollover == checkKey.rollover && this.focused == checkKey.focused && this.f12125c.equals(checkKey.f12125c);
        }

        public int hashCode() {
            return this.f12125c.hashCode() * (this.pressed ? 1 : 2) * (this.enabled ? 4 : 8) * (this.rollover ? 16 : 32);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyCheckBoxIcon$DisabledCheckKey.class */
    private static class DisabledCheckKey {
        int spread1 = Theme.buttonSpreadLightDisabled.getValue();
        int spread2 = Theme.buttonSpreadDarkDisabled.getValue();

        /* renamed from: c, reason: collision with root package name */
        Color f12126c;
        Color back;

        DisabledCheckKey(Color color, Color color2) {
            this.f12126c = color;
            this.back = color2;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof DisabledCheckKey)) {
                return false;
            }
            DisabledCheckKey disabledCheckKey = (DisabledCheckKey) obj;
            return this.f12126c.equals(disabledCheckKey.f12126c) && this.back.equals(disabledCheckKey.back) && this.spread1 == disabledCheckKey.spread1 && this.spread2 == disabledCheckKey.spread2;
        }

        public int hashCode() {
            return this.f12126c.hashCode() * this.back.hashCode() * this.spread1 * this.spread2;
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyCheckBoxIcon$EnabledCheckKey.class */
    private static class EnabledCheckKey {
        int spread1 = Theme.buttonSpreadLight.getValue();
        int spread2 = Theme.buttonSpreadDark.getValue();

        /* renamed from: c, reason: collision with root package name */
        Color f12127c;
        Color back;

        EnabledCheckKey(Color color, Color color2) {
            this.f12127c = color;
            this.back = color2;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof EnabledCheckKey)) {
                return false;
            }
            EnabledCheckKey enabledCheckKey = (EnabledCheckKey) obj;
            return this.f12127c.equals(enabledCheckKey.f12127c) && this.back.equals(enabledCheckKey.back) && this.spread1 == enabledCheckKey.spread1 && this.spread2 == enabledCheckKey.spread2;
        }

        public int hashCode() {
            return this.f12127c.hashCode() * this.back.hashCode() * this.spread1 * this.spread2;
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    @Override // javax.swing.plaf.metal.MetalCheckBoxIcon
    protected int getControlSize() {
        return getIconWidth();
    }

    private void paintFlatCheck(JCheckBox jCheckBox, Graphics graphics, int i2, int i3) {
        if (jCheckBox.isSelected()) {
            if (jCheckBox.isEnabled()) {
                graphics.setColor(Theme.buttonCheckColor.getColor());
            } else {
                graphics.setColor(Theme.buttonCheckDisabledColor.getColor());
            }
            drawXpCheckMark(graphics, i2, i3);
        }
        graphics.setColor(Theme.buttonBorderColor.getColor());
        graphics.drawRect(i2, i3, checkSize - 1, checkSize - 1);
    }

    @Override // javax.swing.plaf.metal.MetalCheckBoxIcon, javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        JCheckBox jCheckBox = (JCheckBox) component;
        Container parent = jCheckBox.getParent();
        if (jCheckBox.isBorderPaintedFlat() || (jCheckBox instanceof TableCellRenderer) || (parent instanceof JTable)) {
            paintFlatCheck(jCheckBox, graphics, i2, i3);
            return;
        }
        Color color = !jCheckBox.isEnabled() ? Theme.buttonDisabledColor.getColor() : jCheckBox.getModel().isPressed() ? jCheckBox.getModel().isRollover() ? Theme.buttonPressedColor.getColor() : Theme.buttonNormalColor.getColor() : jCheckBox.getModel().isRollover() ? Theme.buttonRolloverBgColor.getColor() : Theme.buttonNormalColor.getColor();
        graphics.setColor(color);
        if (TinyLookAndFeel.controlPanelInstantiated) {
            drawXpCheckNoCache(graphics, jCheckBox, color, i2, i3, getIconWidth(), getIconHeight());
        } else {
            drawXpCheck(graphics, jCheckBox, color, i2, i3, getIconWidth(), getIconHeight());
        }
        if (jCheckBox.isSelected()) {
            if (jCheckBox.isEnabled()) {
                graphics.setColor(Theme.buttonCheckColor.getColor());
            } else {
                graphics.setColor(Theme.buttonCheckDisabledColor.getColor());
            }
            drawXpCheckMark(graphics, i2, i3);
        }
    }

    private void drawXpCheck(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, int i4, int i5) {
        boolean zIsPressed = abstractButton.getModel().isPressed();
        boolean zIsArmed = abstractButton.getModel().isArmed();
        boolean zIsEnabled = abstractButton.isEnabled();
        boolean zIsRollover = abstractButton.getModel().isRollover();
        boolean z2 = Theme.buttonFocusBorder.getValue() && !zIsRollover && abstractButton.isFocusOwner();
        CheckKey checkKey = new CheckKey(color, zIsPressed, zIsEnabled, zIsRollover || zIsArmed, z2);
        Object obj = cache.get(checkKey);
        if (obj != null) {
            graphics.drawImage((Image) obj, i2, i3, abstractButton);
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(i4, i5, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        int value = Theme.buttonSpreadLight.getValue();
        int value2 = Theme.buttonSpreadDark.getValue();
        if (!abstractButton.isEnabled()) {
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
        graphics2.fillRect(1, 1, i4 - 2, i5 - 2);
        for (int i8 = 0; i8 < 11; i8++) {
            for (int i9 = 0; i9 < 11; i9++) {
                graphics2.setColor(new Color(colorLighten.getRed(), colorLighten.getGreen(), colorLighten.getBlue(), 255 - f12124a[i9][i8]));
                graphics2.drawLine(i9 + 1, i8 + 1, i9 + 1, i8 + 1);
            }
        }
        if (abstractButton.isEnabled()) {
            graphics2.setColor(Theme.buttonBorderColor.getColor());
            graphics2.drawRect(0, 0, i4 - 1, i5 - 1);
            if (zIsRollover && Theme.buttonRolloverBorder.getValue() && !zIsPressed) {
                DrawRoutines.drawRolloverCheckBorder(graphics2, Theme.buttonRolloverColor.getColor(), 0, 0, i4, i5);
            } else if (z2 && !zIsPressed) {
                DrawRoutines.drawRolloverCheckBorder(graphics2, Theme.buttonDefaultColor.getColor(), 0, 0, i4, i5);
            }
        } else {
            graphics2.setColor(Theme.buttonBorderDisabledColor.getColor());
            graphics2.drawRect(0, 0, i4 - 1, i5 - 1);
        }
        graphics2.dispose();
        graphics.drawImage(bufferedImage, i2, i3, abstractButton);
        cache.put(checkKey, bufferedImage);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v107, types: [de.muntjak.tinylookandfeel.TinyCheckBoxIcon$EnabledCheckKey] */
    private void drawXpCheckNoCache(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, int i4, int i5) {
        Graphics graphics2;
        boolean zIsPressed = abstractButton.getModel().isPressed();
        boolean zIsArmed = abstractButton.getModel().isArmed();
        boolean zIsEnabled = abstractButton.isEnabled();
        boolean zIsRollover = abstractButton.getModel().isRollover();
        boolean z2 = Theme.buttonFocusBorder.getValue() && !zIsRollover && abstractButton.isFocusOwner();
        BufferedImage bufferedImage = null;
        DisabledCheckKey enabledCheckKey = null;
        if ((zIsPressed || zIsArmed || zIsRollover || z2) ? false : true) {
            enabledCheckKey = zIsEnabled ? new EnabledCheckKey(color, Theme.buttonBorderColor.getColor()) : new DisabledCheckKey(color, Theme.buttonBorderDisabledColor.getColor());
            Object obj = cache.get(enabledCheckKey);
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
        graphics2.fillRect(1, 1, i4 - 2, i5 - 2);
        for (int i10 = 0; i10 < 11; i10++) {
            for (int i11 = 0; i11 < 11; i11++) {
                graphics2.setColor(new Color(colorLighten.getRed(), colorLighten.getGreen(), colorLighten.getBlue(), 255 - f12124a[i11][i10]));
                graphics2.drawLine(i11 + 1, i10 + 1, i11 + 1, i10 + 1);
            }
        }
        if (bufferedImage == null) {
            graphics2.translate(-i2, -i3);
        }
        if (zIsEnabled) {
            graphics2.setColor(Theme.buttonBorderColor.getColor());
            graphics2.drawRect(i8, i9, i4 - 1, i5 - 1);
            if (zIsRollover && Theme.buttonRolloverBorder.getValue() && !zIsPressed) {
                DrawRoutines.drawRolloverCheckBorder(graphics2, Theme.buttonRolloverColor.getColor(), i8, i9, i4, i5);
            } else if (z2 && !zIsPressed) {
                DrawRoutines.drawRolloverCheckBorder(graphics2, Theme.buttonDefaultColor.getColor(), i8, i9, i4, i5);
            }
        } else {
            graphics2.setColor(Theme.buttonBorderDisabledColor.getColor());
            graphics2.drawRect(i8, i9, i4 - 1, i5 - 1);
        }
        if (bufferedImage != null) {
            graphics2.dispose();
            graphics.drawImage(bufferedImage, i2, i3, abstractButton);
            cache.put(enabledCheckKey, bufferedImage);
        }
    }

    private void drawXpCheckMark(Graphics graphics, int i2, int i3) {
        graphics.drawLine(i2 + 3, i3 + 5, i2 + 3, i3 + 7);
        graphics.drawLine(i2 + 4, i3 + 6, i2 + 4, i3 + 8);
        graphics.drawLine(i2 + 5, i3 + 7, i2 + 5, i3 + 9);
        graphics.drawLine(i2 + 6, i3 + 6, i2 + 6, i3 + 8);
        graphics.drawLine(i2 + 7, i3 + 5, i2 + 7, i3 + 7);
        graphics.drawLine(i2 + 8, i3 + 4, i2 + 8, i3 + 6);
        graphics.drawLine(i2 + 9, i3 + 3, i2 + 9, i3 + 5);
    }

    @Override // javax.swing.plaf.metal.MetalCheckBoxIcon, javax.swing.Icon
    public int getIconWidth() {
        return checkSize;
    }

    @Override // javax.swing.plaf.metal.MetalCheckBoxIcon, javax.swing.Icon
    public int getIconHeight() {
        return checkSize;
    }
}
