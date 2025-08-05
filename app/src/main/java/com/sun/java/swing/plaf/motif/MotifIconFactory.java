package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifIconFactory.class */
public class MotifIconFactory implements Serializable {
    private static Icon checkBoxIcon;
    private static Icon radioButtonIcon;
    private static Icon menuItemCheckIcon;
    private static Icon menuItemArrowIcon;
    private static Icon menuArrowIcon;

    public static Icon getMenuItemCheckIcon() {
        return null;
    }

    public static Icon getMenuItemArrowIcon() {
        if (menuItemArrowIcon == null) {
            menuItemArrowIcon = new MenuItemArrowIcon();
        }
        return menuItemArrowIcon;
    }

    public static Icon getMenuArrowIcon() {
        if (menuArrowIcon == null) {
            menuArrowIcon = new MenuArrowIcon();
        }
        return menuArrowIcon;
    }

    public static Icon getCheckBoxIcon() {
        if (checkBoxIcon == null) {
            checkBoxIcon = new CheckBoxIcon();
        }
        return checkBoxIcon;
    }

    public static Icon getRadioButtonIcon() {
        if (radioButtonIcon == null) {
            radioButtonIcon = new RadioButtonIcon();
        }
        return radioButtonIcon;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifIconFactory$CheckBoxIcon.class */
    private static class CheckBoxIcon implements Icon, UIResource, Serializable {
        static final int csize = 13;
        private Color control;
        private Color foreground;
        private Color shadow;
        private Color highlight;
        private Color lightShadow;

        private CheckBoxIcon() {
            this.control = UIManager.getColor("control");
            this.foreground = UIManager.getColor("CheckBox.foreground");
            this.shadow = UIManager.getColor("controlShadow");
            this.highlight = UIManager.getColor("controlHighlight");
            this.lightShadow = UIManager.getColor("controlLightShadow");
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            AbstractButton abstractButton = (AbstractButton) component;
            ButtonModel model = abstractButton.getModel();
            boolean zIsBorderPaintedFlat = false;
            if (abstractButton instanceof JCheckBox) {
                zIsBorderPaintedFlat = ((JCheckBox) abstractButton).isBorderPaintedFlat();
            }
            boolean zIsPressed = model.isPressed();
            boolean zIsArmed = model.isArmed();
            model.isEnabled();
            boolean zIsSelected = model.isSelected();
            boolean z2 = (zIsPressed && !zIsArmed && zIsSelected) || (zIsPressed && zIsArmed && !zIsSelected);
            boolean z3 = !(!zIsPressed || zIsArmed || zIsSelected) || (zIsPressed && zIsArmed && zIsSelected);
            boolean z4 = (!zIsPressed && zIsArmed && zIsSelected) || !(zIsPressed || zIsArmed || !zIsSelected);
            if (zIsBorderPaintedFlat) {
                graphics.setColor(this.shadow);
                graphics.drawRect(i2 + 2, i3, 12, 12);
                if (z3 || z2) {
                    graphics.setColor(this.control);
                    graphics.fillRect(i2 + 3, i3 + 1, 11, 11);
                }
            }
            if (z2) {
                drawCheckBezel(graphics, i2, i3, 13, true, false, false, zIsBorderPaintedFlat);
                return;
            }
            if (z3) {
                drawCheckBezel(graphics, i2, i3, 13, true, true, false, zIsBorderPaintedFlat);
            } else if (z4) {
                drawCheckBezel(graphics, i2, i3, 13, false, false, true, zIsBorderPaintedFlat);
            } else if (!zIsBorderPaintedFlat) {
                drawCheckBezelOut(graphics, i2, i3, 13);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 13;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 13;
        }

        public void drawCheckBezelOut(Graphics graphics, int i2, int i3, int i4) {
            UIManager.getColor("controlShadow");
            Color color = graphics.getColor();
            graphics.translate(i2, i3);
            graphics.setColor(this.highlight);
            graphics.drawLine(0, 0, 0, i4 - 1);
            graphics.drawLine(1, 0, i4 - 1, 0);
            graphics.setColor(this.shadow);
            graphics.drawLine(1, i4 - 1, i4 - 1, i4 - 1);
            graphics.drawLine(i4 - 1, i4 - 1, i4 - 1, 1);
            graphics.translate(-i2, -i3);
            graphics.setColor(color);
        }

        public void drawCheckBezel(Graphics graphics, int i2, int i3, int i4, boolean z2, boolean z3, boolean z4, boolean z5) {
            Color color = graphics.getColor();
            graphics.translate(i2, i3);
            if (!z5) {
                if (z3) {
                    graphics.setColor(this.control);
                    graphics.fillRect(1, 1, i4 - 2, i4 - 2);
                    graphics.setColor(this.shadow);
                } else {
                    graphics.setColor(this.lightShadow);
                    graphics.fillRect(0, 0, i4, i4);
                    graphics.setColor(this.highlight);
                }
                graphics.drawLine(1, i4 - 1, i4 - 2, i4 - 1);
                if (z2) {
                    graphics.drawLine(2, i4 - 2, i4 - 3, i4 - 2);
                    graphics.drawLine(i4 - 2, 2, i4 - 2, i4 - 1);
                    if (z3) {
                        graphics.setColor(this.highlight);
                    } else {
                        graphics.setColor(this.shadow);
                    }
                    graphics.drawLine(1, 2, 1, i4 - 2);
                    graphics.drawLine(1, 1, i4 - 3, 1);
                    if (z3) {
                        graphics.setColor(this.shadow);
                    } else {
                        graphics.setColor(this.highlight);
                    }
                }
                graphics.drawLine(i4 - 1, 1, i4 - 1, i4 - 1);
                if (z3) {
                    graphics.setColor(this.highlight);
                } else {
                    graphics.setColor(this.shadow);
                }
                graphics.drawLine(0, 1, 0, i4 - 1);
                graphics.drawLine(0, 0, i4 - 1, 0);
            }
            if (z4) {
                graphics.setColor(this.foreground);
                graphics.drawLine(i4 - 2, 1, i4 - 2, 2);
                graphics.drawLine(i4 - 3, 2, i4 - 3, 3);
                graphics.drawLine(i4 - 4, 3, i4 - 4, 4);
                graphics.drawLine(i4 - 5, 4, i4 - 5, 6);
                graphics.drawLine(i4 - 6, 5, i4 - 6, 8);
                graphics.drawLine(i4 - 7, 6, i4 - 7, 10);
                graphics.drawLine(i4 - 8, 7, i4 - 8, 10);
                graphics.drawLine(i4 - 9, 6, i4 - 9, 9);
                graphics.drawLine(i4 - 10, 5, i4 - 10, 8);
                graphics.drawLine(i4 - 11, 5, i4 - 11, 7);
                graphics.drawLine(i4 - 12, 6, i4 - 12, 6);
            }
            graphics.translate(-i2, -i3);
            graphics.setColor(color);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifIconFactory$RadioButtonIcon.class */
    private static class RadioButtonIcon implements Icon, UIResource, Serializable {
        private Color dot;
        private Color highlight;
        private Color shadow;

        private RadioButtonIcon() {
            this.dot = UIManager.getColor("activeCaptionBorder");
            this.highlight = UIManager.getColor("controlHighlight");
            this.shadow = UIManager.getColor("controlShadow");
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            ButtonModel model = ((AbstractButton) component).getModel();
            getIconWidth();
            getIconHeight();
            boolean zIsPressed = model.isPressed();
            boolean zIsArmed = model.isArmed();
            model.isEnabled();
            boolean zIsSelected = model.isSelected();
            if ((zIsPressed && !zIsArmed && zIsSelected) || (zIsPressed && zIsArmed && !zIsSelected) || ((!zIsPressed && zIsArmed && zIsSelected) || !(zIsPressed || zIsArmed || !zIsSelected))) {
                graphics.setColor(this.shadow);
                graphics.drawLine(i2 + 5, i3 + 0, i2 + 8, i3 + 0);
                graphics.drawLine(i2 + 3, i3 + 1, i2 + 4, i3 + 1);
                graphics.drawLine(i2 + 9, i3 + 1, i2 + 9, i3 + 1);
                graphics.drawLine(i2 + 2, i3 + 2, i2 + 2, i3 + 2);
                graphics.drawLine(i2 + 1, i3 + 3, i2 + 1, i3 + 3);
                graphics.drawLine(i2, i3 + 4, i2, i3 + 9);
                graphics.drawLine(i2 + 1, i3 + 10, i2 + 1, i3 + 10);
                graphics.drawLine(i2 + 2, i3 + 11, i2 + 2, i3 + 11);
                graphics.setColor(this.highlight);
                graphics.drawLine(i2 + 3, i3 + 12, i2 + 4, i3 + 12);
                graphics.drawLine(i2 + 5, i3 + 13, i2 + 8, i3 + 13);
                graphics.drawLine(i2 + 9, i3 + 12, i2 + 10, i3 + 12);
                graphics.drawLine(i2 + 11, i3 + 11, i2 + 11, i3 + 11);
                graphics.drawLine(i2 + 12, i3 + 10, i2 + 12, i3 + 10);
                graphics.drawLine(i2 + 13, i3 + 9, i2 + 13, i3 + 4);
                graphics.drawLine(i2 + 12, i3 + 3, i2 + 12, i3 + 3);
                graphics.drawLine(i2 + 11, i3 + 2, i2 + 11, i3 + 2);
                graphics.drawLine(i2 + 10, i3 + 1, i2 + 10, i3 + 1);
                graphics.setColor(this.dot);
                graphics.fillRect(i2 + 4, i3 + 5, 6, 4);
                graphics.drawLine(i2 + 5, i3 + 4, i2 + 8, i3 + 4);
                graphics.drawLine(i2 + 5, i3 + 9, i2 + 8, i3 + 9);
                return;
            }
            graphics.setColor(this.highlight);
            graphics.drawLine(i2 + 5, i3 + 0, i2 + 8, i3 + 0);
            graphics.drawLine(i2 + 3, i3 + 1, i2 + 4, i3 + 1);
            graphics.drawLine(i2 + 9, i3 + 1, i2 + 9, i3 + 1);
            graphics.drawLine(i2 + 2, i3 + 2, i2 + 2, i3 + 2);
            graphics.drawLine(i2 + 1, i3 + 3, i2 + 1, i3 + 3);
            graphics.drawLine(i2, i3 + 4, i2, i3 + 9);
            graphics.drawLine(i2 + 1, i3 + 10, i2 + 1, i3 + 10);
            graphics.drawLine(i2 + 2, i3 + 11, i2 + 2, i3 + 11);
            graphics.setColor(this.shadow);
            graphics.drawLine(i2 + 3, i3 + 12, i2 + 4, i3 + 12);
            graphics.drawLine(i2 + 5, i3 + 13, i2 + 8, i3 + 13);
            graphics.drawLine(i2 + 9, i3 + 12, i2 + 10, i3 + 12);
            graphics.drawLine(i2 + 11, i3 + 11, i2 + 11, i3 + 11);
            graphics.drawLine(i2 + 12, i3 + 10, i2 + 12, i3 + 10);
            graphics.drawLine(i2 + 13, i3 + 9, i2 + 13, i3 + 4);
            graphics.drawLine(i2 + 12, i3 + 3, i2 + 12, i3 + 3);
            graphics.drawLine(i2 + 11, i3 + 2, i2 + 11, i3 + 2);
            graphics.drawLine(i2 + 10, i3 + 1, i2 + 10, i3 + 1);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 14;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 14;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifIconFactory$MenuItemCheckIcon.class */
    private static class MenuItemCheckIcon implements Icon, UIResource, Serializable {
        private MenuItemCheckIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 0;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 0;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifIconFactory$MenuItemArrowIcon.class */
    private static class MenuItemArrowIcon implements Icon, UIResource, Serializable {
        private MenuItemArrowIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 0;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 0;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifIconFactory$MenuArrowIcon.class */
    private static class MenuArrowIcon implements Icon, UIResource, Serializable {
        private Color focus;
        private Color shadow;
        private Color highlight;

        private MenuArrowIcon() {
            this.focus = UIManager.getColor("windowBorder");
            this.shadow = UIManager.getColor("controlShadow");
            this.highlight = UIManager.getColor("controlHighlight");
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            ButtonModel model = ((AbstractButton) component).getModel();
            getIconWidth();
            int iconHeight = getIconHeight();
            Color color = graphics.getColor();
            if (model.isSelected()) {
                if (MotifGraphicsUtils.isLeftToRight(component)) {
                    graphics.setColor(this.shadow);
                    graphics.fillRect(i2 + 1, i3 + 1, 2, iconHeight);
                    graphics.drawLine(i2 + 4, i3 + 2, i2 + 4, i3 + 2);
                    graphics.drawLine(i2 + 6, i3 + 3, i2 + 6, i3 + 3);
                    graphics.drawLine(i2 + 8, i3 + 4, i2 + 8, i3 + 5);
                    graphics.setColor(this.focus);
                    graphics.fillRect(i2 + 2, i3 + 2, 2, iconHeight - 2);
                    graphics.fillRect(i2 + 4, i3 + 3, 2, iconHeight - 4);
                    graphics.fillRect(i2 + 6, i3 + 4, 2, iconHeight - 6);
                    graphics.setColor(this.highlight);
                    graphics.drawLine(i2 + 2, i3 + iconHeight, i2 + 2, i3 + iconHeight);
                    graphics.drawLine(i2 + 4, (i3 + iconHeight) - 1, i2 + 4, (i3 + iconHeight) - 1);
                    graphics.drawLine(i2 + 6, (i3 + iconHeight) - 2, i2 + 6, (i3 + iconHeight) - 2);
                    graphics.drawLine(i2 + 8, (i3 + iconHeight) - 4, i2 + 8, (i3 + iconHeight) - 3);
                    return;
                }
                graphics.setColor(this.highlight);
                graphics.fillRect(i2 + 7, i3 + 1, 2, 10);
                graphics.drawLine(i2 + 5, i3 + 9, i2 + 5, i3 + 9);
                graphics.drawLine(i2 + 3, i3 + 8, i2 + 3, i3 + 8);
                graphics.drawLine(i2 + 1, i3 + 6, i2 + 1, i3 + 7);
                graphics.setColor(this.focus);
                graphics.fillRect(i2 + 6, i3 + 2, 2, 8);
                graphics.fillRect(i2 + 4, i3 + 3, 2, 6);
                graphics.fillRect(i2 + 2, i3 + 4, 2, 4);
                graphics.setColor(this.shadow);
                graphics.drawLine(i2 + 1, i3 + 4, i2 + 1, i3 + 5);
                graphics.drawLine(i2 + 3, i3 + 3, i2 + 3, i3 + 3);
                graphics.drawLine(i2 + 5, i3 + 2, i2 + 5, i3 + 2);
                graphics.drawLine(i2 + 7, i3 + 1, i2 + 7, i3 + 1);
                return;
            }
            if (MotifGraphicsUtils.isLeftToRight(component)) {
                graphics.setColor(this.highlight);
                graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i3 + iconHeight);
                graphics.drawLine(i2 + 2, i3 + 1, i2 + 2, (i3 + iconHeight) - 2);
                graphics.fillRect(i2 + 3, i3 + 2, 2, 2);
                graphics.fillRect(i2 + 5, i3 + 3, 2, 2);
                graphics.fillRect(i2 + 7, i3 + 4, 2, 2);
                graphics.setColor(this.shadow);
                graphics.drawLine(i2 + 2, (i3 + iconHeight) - 1, i2 + 2, i3 + iconHeight);
                graphics.fillRect(i2 + 3, (i3 + iconHeight) - 2, 2, 2);
                graphics.fillRect(i2 + 5, (i3 + iconHeight) - 3, 2, 2);
                graphics.fillRect(i2 + 7, (i3 + iconHeight) - 4, 2, 2);
                graphics.setColor(color);
                return;
            }
            graphics.setColor(this.highlight);
            graphics.fillRect(i2 + 1, i3 + 4, 2, 2);
            graphics.fillRect(i2 + 3, i3 + 3, 2, 2);
            graphics.fillRect(i2 + 5, i3 + 2, 2, 2);
            graphics.drawLine(i2 + 7, i3 + 1, i2 + 7, i3 + 2);
            graphics.setColor(this.shadow);
            graphics.fillRect(i2 + 1, (i3 + iconHeight) - 4, 2, 2);
            graphics.fillRect(i2 + 3, (i3 + iconHeight) - 3, 2, 2);
            graphics.fillRect(i2 + 5, (i3 + iconHeight) - 2, 2, 2);
            graphics.drawLine(i2 + 7, i3 + 3, i2 + 7, i3 + iconHeight);
            graphics.drawLine(i2 + 8, i3 + 1, i2 + 8, i3 + iconHeight);
            graphics.setColor(color);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 10;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 10;
        }
    }
}
