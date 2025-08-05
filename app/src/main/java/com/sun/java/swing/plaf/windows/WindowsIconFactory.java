package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.UIResource;
import sun.swing.MenuItemCheckIconFactory;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory.class */
public class WindowsIconFactory implements Serializable {
    private static Icon frame_closeIcon;
    private static Icon frame_iconifyIcon;
    private static Icon frame_maxIcon;
    private static Icon frame_minIcon;
    private static Icon frame_resizeIcon;
    private static Icon checkBoxIcon;
    private static Icon radioButtonIcon;
    private static Icon checkBoxMenuItemIcon;
    private static Icon radioButtonMenuItemIcon;
    private static Icon menuItemCheckIcon;
    private static Icon menuItemArrowIcon;
    private static Icon menuArrowIcon;
    private static VistaMenuItemCheckIconFactory menuItemCheckIconFactory;

    public static Icon getMenuItemCheckIcon() {
        if (menuItemCheckIcon == null) {
            menuItemCheckIcon = new MenuItemCheckIcon();
        }
        return menuItemCheckIcon;
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

    public static Icon getCheckBoxMenuItemIcon() {
        if (checkBoxMenuItemIcon == null) {
            checkBoxMenuItemIcon = new CheckBoxMenuItemIcon();
        }
        return checkBoxMenuItemIcon;
    }

    public static Icon getRadioButtonMenuItemIcon() {
        if (radioButtonMenuItemIcon == null) {
            radioButtonMenuItemIcon = new RadioButtonMenuItemIcon();
        }
        return radioButtonMenuItemIcon;
    }

    static synchronized VistaMenuItemCheckIconFactory getMenuItemCheckIconFactory() {
        if (menuItemCheckIconFactory == null) {
            menuItemCheckIconFactory = new VistaMenuItemCheckIconFactory();
        }
        return menuItemCheckIconFactory;
    }

    public static Icon createFrameCloseIcon() {
        if (frame_closeIcon == null) {
            frame_closeIcon = new FrameButtonIcon(TMSchema.Part.WP_CLOSEBUTTON);
        }
        return frame_closeIcon;
    }

    public static Icon createFrameIconifyIcon() {
        if (frame_iconifyIcon == null) {
            frame_iconifyIcon = new FrameButtonIcon(TMSchema.Part.WP_MINBUTTON);
        }
        return frame_iconifyIcon;
    }

    public static Icon createFrameMaximizeIcon() {
        if (frame_maxIcon == null) {
            frame_maxIcon = new FrameButtonIcon(TMSchema.Part.WP_MAXBUTTON);
        }
        return frame_maxIcon;
    }

    public static Icon createFrameMinimizeIcon() {
        if (frame_minIcon == null) {
            frame_minIcon = new FrameButtonIcon(TMSchema.Part.WP_RESTOREBUTTON);
        }
        return frame_minIcon;
    }

    public static Icon createFrameResizeIcon() {
        if (frame_resizeIcon == null) {
            frame_resizeIcon = new ResizeIcon();
        }
        return frame_resizeIcon;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$FrameButtonIcon.class */
    private static class FrameButtonIcon implements Icon, Serializable {
        private TMSchema.Part part;

        private FrameButtonIcon(TMSchema.Part part) {
            this.part = part;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            int i4;
            TMSchema.State state;
            int iconWidth = getIconWidth();
            int iconHeight = getIconHeight();
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                XPStyle.Skin skin = xp.getSkin(component, this.part);
                AbstractButton abstractButton = (AbstractButton) component;
                ButtonModel model = abstractButton.getModel();
                JInternalFrame jInternalFrame = (JInternalFrame) SwingUtilities.getAncestorOfClass(JInternalFrame.class, abstractButton);
                if (jInternalFrame != null && jInternalFrame.isSelected()) {
                    if (!model.isEnabled()) {
                        state = TMSchema.State.DISABLED;
                    } else if (model.isArmed() && model.isPressed()) {
                        state = TMSchema.State.PUSHED;
                    } else if (model.isRollover()) {
                        state = TMSchema.State.HOT;
                    } else {
                        state = TMSchema.State.NORMAL;
                    }
                } else if (!model.isEnabled()) {
                    state = TMSchema.State.INACTIVEDISABLED;
                } else if (model.isArmed() && model.isPressed()) {
                    state = TMSchema.State.INACTIVEPUSHED;
                } else if (model.isRollover()) {
                    state = TMSchema.State.INACTIVEHOT;
                } else {
                    state = TMSchema.State.INACTIVENORMAL;
                }
                skin.paintSkin(graphics, 0, 0, iconWidth, iconHeight, state);
                return;
            }
            graphics.setColor(Color.black);
            int i5 = (iconWidth / 12) + 2;
            int i6 = iconHeight / 5;
            int i7 = (iconHeight - (i6 * 2)) - 1;
            int i8 = ((iconWidth * 3) / 4) - 3;
            int iMax = Math.max(iconHeight / 8, 2);
            int iMax2 = Math.max(iconWidth / 15, 1);
            if (this.part != TMSchema.Part.WP_CLOSEBUTTON) {
                if (this.part == TMSchema.Part.WP_MINBUTTON) {
                    graphics.fillRect(i5, (i6 + i7) - iMax, i8 - (i8 / 3), iMax);
                    return;
                }
                if (this.part == TMSchema.Part.WP_MAXBUTTON) {
                    graphics.fillRect(i5, i6, i8, iMax);
                    graphics.fillRect(i5, i6, iMax2, i7);
                    graphics.fillRect((i5 + i8) - iMax2, i6, iMax2, i7);
                    graphics.fillRect(i5, (i6 + i7) - iMax2, i8, iMax2);
                    return;
                }
                if (this.part == TMSchema.Part.WP_RESTOREBUTTON) {
                    graphics.fillRect(i5 + (i8 / 3), i6, i8 - (i8 / 3), iMax);
                    graphics.fillRect(i5 + (i8 / 3), i6, iMax2, i7 / 3);
                    graphics.fillRect((i5 + i8) - iMax2, i6, iMax2, i7 - (i7 / 3));
                    graphics.fillRect((i5 + i8) - (i8 / 3), ((i6 + i7) - (i7 / 3)) - iMax2, i8 / 3, iMax2);
                    graphics.fillRect(i5, i6 + (i7 / 3), i8 - (i8 / 3), iMax);
                    graphics.fillRect(i5, i6 + (i7 / 3), iMax2, i7 - (i7 / 3));
                    graphics.fillRect(((i5 + i8) - (i8 / 3)) - iMax2, i6 + (i7 / 3), iMax2, i7 - (i7 / 3));
                    graphics.fillRect(i5, (i6 + i7) - iMax2, i8 - (i8 / 3), iMax2);
                    return;
                }
                return;
            }
            if (iconWidth > 47) {
                i4 = 6;
            } else if (iconWidth > 37) {
                i4 = 5;
            } else if (iconWidth > 26) {
                i4 = 4;
            } else if (iconWidth > 16) {
                i4 = 3;
            } else {
                i4 = iconWidth > 12 ? 2 : 1;
            }
            int i9 = (iconHeight / 12) + 2;
            if (i4 == 1) {
                if (i8 % 2 == 1) {
                    i5++;
                    i8++;
                }
                graphics.drawLine(i5, i9, (i5 + i8) - 2, (i9 + i8) - 2);
                graphics.drawLine((i5 + i8) - 2, i9, i5, (i9 + i8) - 2);
                return;
            }
            if (i4 == 2) {
                if (i8 > 6) {
                    i5++;
                    i8--;
                }
                graphics.drawLine(i5, i9, (i5 + i8) - 2, (i9 + i8) - 2);
                graphics.drawLine((i5 + i8) - 2, i9, i5, (i9 + i8) - 2);
                graphics.drawLine(i5 + 1, i9, (i5 + i8) - 1, (i9 + i8) - 2);
                graphics.drawLine((i5 + i8) - 1, i9, i5 + 1, (i9 + i8) - 2);
                return;
            }
            int i10 = i5 + 2;
            int i11 = i9 + 1;
            int i12 = i8 - 2;
            graphics.drawLine(i10, i11, (i10 + i12) - 1, (i11 + i12) - 1);
            graphics.drawLine((i10 + i12) - 1, i11, i10, (i11 + i12) - 1);
            graphics.drawLine(i10 + 1, i11, (i10 + i12) - 1, (i11 + i12) - 2);
            graphics.drawLine((i10 + i12) - 2, i11, i10, (i11 + i12) - 2);
            graphics.drawLine(i10, i11 + 1, (i10 + i12) - 2, (i11 + i12) - 1);
            graphics.drawLine((i10 + i12) - 1, i11 + 1, i10 + 1, (i11 + i12) - 1);
            for (int i13 = 4; i13 <= i4; i13++) {
                graphics.drawLine((i10 + i13) - 2, i11, (i10 + i12) - 1, ((i11 + i12) - i13) + 1);
                graphics.drawLine(i10, (i11 + i13) - 2, ((i10 + i12) - i13) + 1, (i11 + i12) - 1);
                graphics.drawLine(((i10 + i12) - i13) + 1, i11, i10, ((i11 + i12) - i13) + 1);
                graphics.drawLine((i10 + i12) - 1, (i11 + i13) - 2, (i10 + i13) - 2, (i11 + i12) - 1);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            int i2;
            if (XPStyle.getXP() != null) {
                i2 = UIManager.getInt("InternalFrame.titleButtonHeight") - 2;
                Dimension partSize = XPStyle.getPartSize(TMSchema.Part.WP_CLOSEBUTTON, TMSchema.State.NORMAL);
                if (partSize != null && partSize.width != 0 && partSize.height != 0) {
                    i2 = (int) ((i2 * partSize.width) / partSize.height);
                }
            } else {
                i2 = UIManager.getInt("InternalFrame.titleButtonWidth") - 2;
            }
            if (XPStyle.getXP() != null) {
                i2 -= 2;
            }
            return i2;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return UIManager.getInt("InternalFrame.titleButtonHeight") - 4;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$ResizeIcon.class */
    private static class ResizeIcon implements Icon, Serializable {
        private ResizeIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.setColor(UIManager.getColor("InternalFrame.resizeIconHighlight"));
            graphics.drawLine(0, 11, 11, 0);
            graphics.drawLine(4, 11, 11, 4);
            graphics.drawLine(8, 11, 11, 8);
            graphics.setColor(UIManager.getColor("InternalFrame.resizeIconShadow"));
            graphics.drawLine(1, 11, 11, 1);
            graphics.drawLine(2, 11, 11, 2);
            graphics.drawLine(5, 11, 11, 5);
            graphics.drawLine(6, 11, 11, 6);
            graphics.drawLine(9, 11, 11, 9);
            graphics.drawLine(10, 11, 11, 10);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 13;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 13;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$CheckBoxIcon.class */
    private static class CheckBoxIcon implements Icon, Serializable {
        static final int csize = 13;

        private CheckBoxIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            TMSchema.State state;
            JCheckBox jCheckBox = (JCheckBox) component;
            ButtonModel model = jCheckBox.getModel();
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                if (model.isSelected()) {
                    state = TMSchema.State.CHECKEDNORMAL;
                    if (!model.isEnabled()) {
                        state = TMSchema.State.CHECKEDDISABLED;
                    } else if (model.isPressed() && model.isArmed()) {
                        state = TMSchema.State.CHECKEDPRESSED;
                    } else if (model.isRollover()) {
                        state = TMSchema.State.CHECKEDHOT;
                    }
                } else {
                    state = TMSchema.State.UNCHECKEDNORMAL;
                    if (!model.isEnabled()) {
                        state = TMSchema.State.UNCHECKEDDISABLED;
                    } else if (model.isPressed() && model.isArmed()) {
                        state = TMSchema.State.UNCHECKEDPRESSED;
                    } else if (model.isRollover()) {
                        state = TMSchema.State.UNCHECKEDHOT;
                    }
                }
                xp.getSkin(component, TMSchema.Part.BP_CHECKBOX).paintSkin(graphics, i2, i3, state);
                return;
            }
            if (!jCheckBox.isBorderPaintedFlat()) {
                graphics.setColor(UIManager.getColor("CheckBox.shadow"));
                graphics.drawLine(i2, i3, i2 + 11, i3);
                graphics.drawLine(i2, i3 + 1, i2, i3 + 11);
                graphics.setColor(UIManager.getColor("CheckBox.highlight"));
                graphics.drawLine(i2 + 12, i3, i2 + 12, i3 + 12);
                graphics.drawLine(i2, i3 + 12, i2 + 11, i3 + 12);
                graphics.setColor(UIManager.getColor("CheckBox.darkShadow"));
                graphics.drawLine(i2 + 1, i3 + 1, i2 + 10, i3 + 1);
                graphics.drawLine(i2 + 1, i3 + 2, i2 + 1, i3 + 10);
                graphics.setColor(UIManager.getColor("CheckBox.light"));
                graphics.drawLine(i2 + 1, i3 + 11, i2 + 11, i3 + 11);
                graphics.drawLine(i2 + 11, i3 + 1, i2 + 11, i3 + 10);
                if ((model.isPressed() && model.isArmed()) || !model.isEnabled()) {
                    graphics.setColor(UIManager.getColor("CheckBox.background"));
                } else {
                    graphics.setColor(UIManager.getColor("CheckBox.interiorBackground"));
                }
                graphics.fillRect(i2 + 2, i3 + 2, 9, 9);
            } else {
                graphics.setColor(UIManager.getColor("CheckBox.shadow"));
                graphics.drawRect(i2 + 1, i3 + 1, 10, 10);
                if ((model.isPressed() && model.isArmed()) || !model.isEnabled()) {
                    graphics.setColor(UIManager.getColor("CheckBox.background"));
                } else {
                    graphics.setColor(UIManager.getColor("CheckBox.interiorBackground"));
                }
                graphics.fillRect(i2 + 2, i3 + 2, 9, 9);
            }
            if (model.isEnabled()) {
                graphics.setColor(UIManager.getColor("CheckBox.foreground"));
            } else {
                graphics.setColor(UIManager.getColor("CheckBox.shadow"));
            }
            if (model.isSelected()) {
                graphics.drawLine(i2 + 9, i3 + 3, i2 + 9, i3 + 3);
                graphics.drawLine(i2 + 8, i3 + 4, i2 + 9, i3 + 4);
                graphics.drawLine(i2 + 7, i3 + 5, i2 + 9, i3 + 5);
                graphics.drawLine(i2 + 6, i3 + 6, i2 + 8, i3 + 6);
                graphics.drawLine(i2 + 3, i3 + 7, i2 + 7, i3 + 7);
                graphics.drawLine(i2 + 4, i3 + 8, i2 + 6, i3 + 8);
                graphics.drawLine(i2 + 5, i3 + 9, i2 + 5, i3 + 9);
                graphics.drawLine(i2 + 3, i3 + 5, i2 + 3, i3 + 5);
                graphics.drawLine(i2 + 3, i3 + 6, i2 + 4, i3 + 6);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                return xp.getSkin(null, TMSchema.Part.BP_CHECKBOX).getWidth();
            }
            return 13;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                return xp.getSkin(null, TMSchema.Part.BP_CHECKBOX).getHeight();
            }
            return 13;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$RadioButtonIcon.class */
    private static class RadioButtonIcon implements Icon, UIResource, Serializable {
        private RadioButtonIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            TMSchema.State state;
            AbstractButton abstractButton = (AbstractButton) component;
            ButtonModel model = abstractButton.getModel();
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                XPStyle.Skin skin = xp.getSkin(abstractButton, TMSchema.Part.BP_RADIOBUTTON);
                if (model.isSelected()) {
                    state = TMSchema.State.CHECKEDNORMAL;
                    if (!model.isEnabled()) {
                        state = TMSchema.State.CHECKEDDISABLED;
                    } else if (model.isPressed() && model.isArmed()) {
                        state = TMSchema.State.CHECKEDPRESSED;
                    } else if (model.isRollover()) {
                        state = TMSchema.State.CHECKEDHOT;
                    }
                } else {
                    state = TMSchema.State.UNCHECKEDNORMAL;
                    if (!model.isEnabled()) {
                        state = TMSchema.State.UNCHECKEDDISABLED;
                    } else if (model.isPressed() && model.isArmed()) {
                        state = TMSchema.State.UNCHECKEDPRESSED;
                    } else if (model.isRollover()) {
                        state = TMSchema.State.UNCHECKEDHOT;
                    }
                }
                skin.paintSkin(graphics, i2, i3, state);
                return;
            }
            if ((model.isPressed() && model.isArmed()) || !model.isEnabled()) {
                graphics.setColor(UIManager.getColor("RadioButton.background"));
            } else {
                graphics.setColor(UIManager.getColor("RadioButton.interiorBackground"));
            }
            graphics.fillRect(i2 + 2, i3 + 2, 8, 8);
            graphics.setColor(UIManager.getColor("RadioButton.shadow"));
            graphics.drawLine(i2 + 4, i3 + 0, i2 + 7, i3 + 0);
            graphics.drawLine(i2 + 2, i3 + 1, i2 + 3, i3 + 1);
            graphics.drawLine(i2 + 8, i3 + 1, i2 + 9, i3 + 1);
            graphics.drawLine(i2 + 1, i3 + 2, i2 + 1, i3 + 3);
            graphics.drawLine(i2 + 0, i3 + 4, i2 + 0, i3 + 7);
            graphics.drawLine(i2 + 1, i3 + 8, i2 + 1, i3 + 9);
            graphics.setColor(UIManager.getColor("RadioButton.highlight"));
            graphics.drawLine(i2 + 2, i3 + 10, i2 + 3, i3 + 10);
            graphics.drawLine(i2 + 4, i3 + 11, i2 + 7, i3 + 11);
            graphics.drawLine(i2 + 8, i3 + 10, i2 + 9, i3 + 10);
            graphics.drawLine(i2 + 10, i3 + 9, i2 + 10, i3 + 8);
            graphics.drawLine(i2 + 11, i3 + 7, i2 + 11, i3 + 4);
            graphics.drawLine(i2 + 10, i3 + 3, i2 + 10, i3 + 2);
            graphics.setColor(UIManager.getColor("RadioButton.darkShadow"));
            graphics.drawLine(i2 + 4, i3 + 1, i2 + 7, i3 + 1);
            graphics.drawLine(i2 + 2, i3 + 2, i2 + 3, i3 + 2);
            graphics.drawLine(i2 + 8, i3 + 2, i2 + 9, i3 + 2);
            graphics.drawLine(i2 + 2, i3 + 3, i2 + 2, i3 + 3);
            graphics.drawLine(i2 + 1, i3 + 4, i2 + 1, i3 + 7);
            graphics.drawLine(i2 + 2, i3 + 8, i2 + 2, i3 + 8);
            graphics.setColor(UIManager.getColor("RadioButton.light"));
            graphics.drawLine(i2 + 2, i3 + 9, i2 + 3, i3 + 9);
            graphics.drawLine(i2 + 4, i3 + 10, i2 + 7, i3 + 10);
            graphics.drawLine(i2 + 8, i3 + 9, i2 + 9, i3 + 9);
            graphics.drawLine(i2 + 9, i3 + 8, i2 + 9, i3 + 8);
            graphics.drawLine(i2 + 10, i3 + 7, i2 + 10, i3 + 4);
            graphics.drawLine(i2 + 9, i3 + 3, i2 + 9, i3 + 3);
            if (model.isSelected()) {
                if (model.isEnabled()) {
                    graphics.setColor(UIManager.getColor("RadioButton.foreground"));
                } else {
                    graphics.setColor(UIManager.getColor("RadioButton.shadow"));
                }
                graphics.fillRect(i2 + 4, i3 + 5, 4, 2);
                graphics.fillRect(i2 + 5, i3 + 4, 2, 4);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                return xp.getSkin(null, TMSchema.Part.BP_RADIOBUTTON).getWidth();
            }
            return 13;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                return xp.getSkin(null, TMSchema.Part.BP_RADIOBUTTON).getHeight();
            }
            return 13;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$CheckBoxMenuItemIcon.class */
    private static class CheckBoxMenuItemIcon implements Icon, UIResource, Serializable {
        private CheckBoxMenuItemIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (((AbstractButton) component).getModel().isSelected()) {
                int iconHeight = i3 - (getIconHeight() / 2);
                graphics.drawLine(i2 + 9, iconHeight + 3, i2 + 9, iconHeight + 3);
                graphics.drawLine(i2 + 8, iconHeight + 4, i2 + 9, iconHeight + 4);
                graphics.drawLine(i2 + 7, iconHeight + 5, i2 + 9, iconHeight + 5);
                graphics.drawLine(i2 + 6, iconHeight + 6, i2 + 8, iconHeight + 6);
                graphics.drawLine(i2 + 3, iconHeight + 7, i2 + 7, iconHeight + 7);
                graphics.drawLine(i2 + 4, iconHeight + 8, i2 + 6, iconHeight + 8);
                graphics.drawLine(i2 + 5, iconHeight + 9, i2 + 5, iconHeight + 9);
                graphics.drawLine(i2 + 3, iconHeight + 5, i2 + 3, iconHeight + 5);
                graphics.drawLine(i2 + 3, iconHeight + 6, i2 + 4, iconHeight + 6);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 9;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 9;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$RadioButtonMenuItemIcon.class */
    private static class RadioButtonMenuItemIcon implements Icon, UIResource, Serializable {
        private RadioButtonMenuItemIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            AbstractButton abstractButton = (AbstractButton) component;
            abstractButton.getModel();
            if (abstractButton.isSelected()) {
                graphics.fillRoundRect(i2 + 3, i3 + 3, getIconWidth() - 6, getIconHeight() - 6, 4, 4);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 12;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 12;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$MenuItemCheckIcon.class */
    private static class MenuItemCheckIcon implements Icon, UIResource, Serializable {
        private MenuItemCheckIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 9;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 9;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$MenuItemArrowIcon.class */
    private static class MenuItemArrowIcon implements Icon, UIResource, Serializable {
        private MenuItemArrowIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 4;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 8;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$MenuArrowIcon.class */
    private static class MenuArrowIcon implements Icon, UIResource, Serializable {
        private MenuArrowIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            XPStyle xp = XPStyle.getXP();
            if (WindowsMenuItemUI.isVistaPainting(xp)) {
                TMSchema.State state = TMSchema.State.NORMAL;
                if (component instanceof JMenuItem) {
                    state = ((JMenuItem) component).getModel().isEnabled() ? TMSchema.State.NORMAL : TMSchema.State.DISABLED;
                }
                XPStyle.Skin skin = xp.getSkin(component, TMSchema.Part.MP_POPUPSUBMENU);
                if (WindowsGraphicsUtils.isLeftToRight(component)) {
                    skin.paintSkin(graphics, i2, i3, state);
                    return;
                }
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.translate(i2 + skin.getWidth(), i3);
                graphics2D.scale(-1.0d, 1.0d);
                skin.paintSkin(graphics2D, 0, 0, state);
                graphics2D.dispose();
                return;
            }
            graphics.translate(i2, i3);
            if (WindowsGraphicsUtils.isLeftToRight(component)) {
                graphics.drawLine(0, 0, 0, 7);
                graphics.drawLine(1, 1, 1, 6);
                graphics.drawLine(2, 2, 2, 5);
                graphics.drawLine(3, 3, 3, 4);
            } else {
                graphics.drawLine(4, 0, 4, 7);
                graphics.drawLine(3, 1, 3, 6);
                graphics.drawLine(2, 2, 2, 5);
                graphics.drawLine(1, 3, 1, 4);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            XPStyle xp = XPStyle.getXP();
            if (WindowsMenuItemUI.isVistaPainting(xp)) {
                return xp.getSkin(null, TMSchema.Part.MP_POPUPSUBMENU).getWidth();
            }
            return 4;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            XPStyle xp = XPStyle.getXP();
            if (WindowsMenuItemUI.isVistaPainting(xp)) {
                return xp.getSkin(null, TMSchema.Part.MP_POPUPSUBMENU).getHeight();
            }
            return 8;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$VistaMenuItemCheckIconFactory.class */
    static class VistaMenuItemCheckIconFactory implements MenuItemCheckIconFactory {
        private static final int OFFSET = 3;

        VistaMenuItemCheckIconFactory() {
        }

        @Override // sun.swing.MenuItemCheckIconFactory
        public Icon getIcon(JMenuItem jMenuItem) {
            return new VistaMenuItemCheckIcon(jMenuItem);
        }

        @Override // sun.swing.MenuItemCheckIconFactory
        public boolean isCompatible(Object obj, String str) {
            return (obj instanceof VistaMenuItemCheckIcon) && ((VistaMenuItemCheckIcon) obj).type == getType(str);
        }

        public Icon getIcon(String str) {
            return new VistaMenuItemCheckIcon(str);
        }

        static int getIconWidth() {
            XPStyle xp = XPStyle.getXP();
            return (xp != null ? xp.getSkin(null, TMSchema.Part.MP_POPUPCHECK).getWidth() : 16) + 6;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Class<? extends JMenuItem> getType(Component component) {
            Class<? extends JMenuItem> cls = null;
            if (component instanceof JCheckBoxMenuItem) {
                cls = JCheckBoxMenuItem.class;
            } else if (component instanceof JRadioButtonMenuItem) {
                cls = JRadioButtonMenuItem.class;
            } else if (component instanceof JMenu) {
                cls = JMenu.class;
            } else if (component instanceof JMenuItem) {
                cls = JMenuItem.class;
            }
            return cls;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Class<? extends JMenuItem> getType(String str) {
            Class<? extends JMenuItem> cls;
            if (str == "CheckBoxMenuItem") {
                cls = JCheckBoxMenuItem.class;
            } else if (str == "RadioButtonMenuItem") {
                cls = JRadioButtonMenuItem.class;
            } else if (str == "Menu") {
                cls = JMenu.class;
            } else if (str == "MenuItem") {
                cls = JMenuItem.class;
            } else {
                cls = JMenuItem.class;
            }
            return cls;
        }

        /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsIconFactory$VistaMenuItemCheckIconFactory$VistaMenuItemCheckIcon.class */
        private static class VistaMenuItemCheckIcon implements Icon, UIResource, Serializable {
            private final JMenuItem menuItem;
            private final Class<? extends JMenuItem> type;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !WindowsIconFactory.class.desiredAssertionStatus();
            }

            VistaMenuItemCheckIcon(JMenuItem jMenuItem) {
                this.type = VistaMenuItemCheckIconFactory.getType(jMenuItem);
                this.menuItem = jMenuItem;
            }

            VistaMenuItemCheckIcon(String str) {
                this.type = VistaMenuItemCheckIconFactory.getType(str);
                this.menuItem = null;
            }

            @Override // javax.swing.Icon
            public int getIconHeight() {
                int height;
                Icon laFIcon = getLaFIcon();
                if (laFIcon != null) {
                    return laFIcon.getIconHeight();
                }
                Icon icon = getIcon();
                if (icon != null) {
                    height = icon.getIconHeight();
                } else {
                    XPStyle xp = XPStyle.getXP();
                    if (xp != null) {
                        height = xp.getSkin(null, TMSchema.Part.MP_POPUPCHECK).getHeight();
                    } else {
                        height = 16;
                    }
                }
                return height + 6;
            }

            @Override // javax.swing.Icon
            public int getIconWidth() {
                int iconWidth;
                Icon laFIcon = getLaFIcon();
                if (laFIcon != null) {
                    return laFIcon.getIconWidth();
                }
                Icon icon = getIcon();
                if (icon != null) {
                    iconWidth = icon.getIconWidth() + 6;
                } else {
                    iconWidth = VistaMenuItemCheckIconFactory.getIconWidth();
                }
                return iconWidth;
            }

            @Override // javax.swing.Icon
            public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
                TMSchema.State state;
                TMSchema.State state2;
                Icon laFIcon = getLaFIcon();
                if (laFIcon != null) {
                    laFIcon.paintIcon(component, graphics, i2, i3);
                    return;
                }
                if (!$assertionsDisabled && this.menuItem != null && component != this.menuItem) {
                    throw new AssertionError();
                }
                Icon icon = getIcon();
                if ((this.type == JCheckBoxMenuItem.class || this.type == JRadioButtonMenuItem.class) && ((AbstractButton) component).isSelected()) {
                    TMSchema.Part part = TMSchema.Part.MP_POPUPCHECKBACKGROUND;
                    TMSchema.Part part2 = TMSchema.Part.MP_POPUPCHECK;
                    if (isEnabled(component, null)) {
                        state = icon != null ? TMSchema.State.BITMAP : TMSchema.State.NORMAL;
                        state2 = this.type == JRadioButtonMenuItem.class ? TMSchema.State.BULLETNORMAL : TMSchema.State.CHECKMARKNORMAL;
                    } else {
                        state = TMSchema.State.DISABLEDPUSHED;
                        state2 = this.type == JRadioButtonMenuItem.class ? TMSchema.State.BULLETDISABLED : TMSchema.State.CHECKMARKDISABLED;
                    }
                    XPStyle xp = XPStyle.getXP();
                    if (xp != null) {
                        xp.getSkin(component, part).paintSkin(graphics, i2, i3, getIconWidth(), getIconHeight(), state);
                        if (icon == null) {
                            xp.getSkin(component, part2).paintSkin(graphics, i2 + 3, i3 + 3, state2);
                        }
                    }
                }
                if (icon != null) {
                    icon.paintIcon(component, graphics, i2 + 3, i3 + 3);
                }
            }

            private static WindowsMenuItemUIAccessor getAccessor(JMenuItem jMenuItem) {
                WindowsMenuItemUIAccessor windowsMenuItemUIAccessor = null;
                ButtonUI ui = jMenuItem != null ? jMenuItem.getUI() : null;
                if (ui instanceof WindowsMenuItemUI) {
                    windowsMenuItemUIAccessor = ((WindowsMenuItemUI) ui).accessor;
                } else if (ui instanceof WindowsMenuUI) {
                    windowsMenuItemUIAccessor = ((WindowsMenuUI) ui).accessor;
                } else if (ui instanceof WindowsCheckBoxMenuItemUI) {
                    windowsMenuItemUIAccessor = ((WindowsCheckBoxMenuItemUI) ui).accessor;
                } else if (ui instanceof WindowsRadioButtonMenuItemUI) {
                    windowsMenuItemUIAccessor = ((WindowsRadioButtonMenuItemUI) ui).accessor;
                }
                return windowsMenuItemUIAccessor;
            }

            private static boolean isEnabled(Component component, TMSchema.State state) {
                WindowsMenuItemUIAccessor accessor;
                if (state == null && (component instanceof JMenuItem) && (accessor = getAccessor((JMenuItem) component)) != null) {
                    state = accessor.getState((JMenuItem) component);
                }
                if (state != null) {
                    return (state == TMSchema.State.DISABLED || state == TMSchema.State.DISABLEDHOT || state == TMSchema.State.DISABLEDPUSHED) ? false : true;
                }
                if (component != null) {
                    return component.isEnabled();
                }
                return true;
            }

            private Icon getIcon() {
                Icon disabledIcon;
                if (this.menuItem == null) {
                    return null;
                }
                WindowsMenuItemUIAccessor accessor = getAccessor(this.menuItem);
                TMSchema.State state = accessor != null ? accessor.getState(this.menuItem) : null;
                if (isEnabled(this.menuItem, null)) {
                    if (state == TMSchema.State.PUSHED) {
                        disabledIcon = this.menuItem.getPressedIcon();
                    } else {
                        disabledIcon = this.menuItem.getIcon();
                    }
                } else {
                    disabledIcon = this.menuItem.getDisabledIcon();
                }
                return disabledIcon;
            }

            private Icon getLaFIcon() {
                Icon icon = (Icon) UIManager.getDefaults().get(typeToString(this.type));
                if ((icon instanceof VistaMenuItemCheckIcon) && ((VistaMenuItemCheckIcon) icon).type == this.type) {
                    icon = null;
                }
                return icon;
            }

            private static String typeToString(Class<? extends JMenuItem> cls) {
                if (!$assertionsDisabled && cls != JMenuItem.class && cls != JMenu.class && cls != JCheckBoxMenuItem.class && cls != JRadioButtonMenuItem.class) {
                    throw new AssertionError();
                }
                StringBuilder sb = new StringBuilder(cls.getName());
                sb.delete(0, sb.lastIndexOf("J") + 1);
                sb.append(".checkIcon");
                return sb.toString();
            }
        }
    }
}
