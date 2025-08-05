package de.muntjak.tinylookandfeel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/MenuItemIconFactory.class */
public class MenuItemIconFactory implements Serializable {
    private static final HashMap cache = new HashMap();
    private static final Dimension CHECK_ICON_SIZE = new Dimension(10, 10);
    private static final Dimension ARROW_ICON_SIZE = new Dimension(4, 8);
    private static final int SYSTEM_CLOSE_ICON = 1;
    private static final int SYSTEM_ICONIFY_ICON = 2;
    private static final int SYSTEM_MAXIMIZE_ICON = 3;
    private static final int SYSTEM_RESTORE_ICON = 4;
    private static Icon checkBoxMenuItemIcon;
    private static Icon radioButtonMenuItemIcon;
    private static Icon menuArrowIcon;
    private static Icon systemCloseIcon;
    private static Icon systemIconifyIcon;
    private static Icon systemMaximizeIcon;
    private static Icon systemRestoreIcon;

    /* renamed from: de.muntjak.tinylookandfeel.MenuItemIconFactory$1, reason: invalid class name */
    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/MenuItemIconFactory$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/MenuItemIconFactory$CheckBoxMenuItemIcon.class */
    private static class CheckBoxMenuItemIcon implements Icon, UIResource, Serializable {
        private CheckBoxMenuItemIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            ButtonModel model = ((JMenuItem) component).getModel();
            if (model.isSelected()) {
                boolean zIsEnabled = model.isEnabled();
                model.isPressed();
                model.isArmed();
                graphics.translate(i2, i3);
                if (!zIsEnabled) {
                    graphics.setColor(Theme.menuIconDisabledColor.getColor());
                } else if (model.isArmed() || ((component instanceof JMenu) && model.isSelected())) {
                    graphics.setColor(Theme.menuIconRolloverColor.getColor());
                } else {
                    graphics.setColor(Theme.menuIconColor.getColor());
                }
                graphics.drawLine(2, 4, 2, 6);
                graphics.drawLine(3, 5, 3, 7);
                graphics.drawLine(4, 6, 4, 8);
                graphics.drawLine(5, 5, 5, 7);
                graphics.drawLine(6, 4, 6, 6);
                graphics.drawLine(7, 3, 7, 5);
                graphics.drawLine(8, 2, 8, 4);
                graphics.translate(-i2, -i3);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return MenuItemIconFactory.CHECK_ICON_SIZE.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MenuItemIconFactory.CHECK_ICON_SIZE.height;
        }

        CheckBoxMenuItemIcon(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/MenuItemIconFactory$MenuArrowIcon.class */
    private static class MenuArrowIcon implements Icon, UIResource, Serializable {
        private MenuArrowIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            JMenuItem jMenuItem = (JMenuItem) component;
            ButtonModel model = jMenuItem.getModel();
            graphics.translate(i2, i3);
            if (!model.isEnabled()) {
                graphics.setColor(Theme.menuItemDisabledFgColor.getColor());
            } else if (model.isArmed() || ((component instanceof JMenu) && model.isSelected())) {
                graphics.setColor(Theme.menuItemSelectedTextColor.getColor());
            } else {
                graphics.setColor(jMenuItem.getForeground());
            }
            if (component.getComponentOrientation().isLeftToRight()) {
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
            return MenuItemIconFactory.ARROW_ICON_SIZE.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MenuItemIconFactory.ARROW_ICON_SIZE.height;
        }

        MenuArrowIcon(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/MenuItemIconFactory$RadioButtonMenuItemIcon.class */
    private static class RadioButtonMenuItemIcon implements Icon, UIResource, Serializable {
        private static final int[][] ALPHA_BORDER = {new int[]{255, 255, 163, 84, 25, 25, 84, 163, 255, 255}, new int[]{255, 127, 92, 171, 230, 230, 171, 92, 127, 255}, new int[]{163, 92, 255, 255, 255, 255, 255, 255, 92, 163}, new int[]{84, 171, 255, 255, 255, 255, 255, 255, 171, 84}, new int[]{25, 230, 255, 255, 255, 255, 255, 255, 230, 25}, new int[]{25, 230, 255, 255, 255, 255, 255, 255, 230, 25}, new int[]{84, 171, 255, 255, 255, 255, 255, 255, 171, 84}, new int[]{163, 92, 255, 255, 255, 255, 255, 255, 92, 163}, new int[]{255, 127, 92, 171, 230, 230, 171, 92, 127, 255}, new int[]{255, 255, 163, 84, 25, 25, 84, 163, 255, 255}};
        private static final int[][] ALPHA_CHECK = {new int[]{255, 255, 163, 84, 25, 25, 84, 163, 255, 255}, new int[]{255, 127, 92, 171, 230, 230, 171, 92, 127, 255}, new int[]{163, 92, 255, 255, 255, 255, 255, 255, 92, 163}, new int[]{84, 171, 255, 170, 63, 63, 170, 255, 171, 84}, new int[]{25, 230, 255, 63, 0, 0, 63, 255, 230, 25}, new int[]{25, 230, 255, 63, 0, 0, 63, 255, 230, 25}, new int[]{84, 171, 255, 170, 63, 63, 170, 255, 171, 84}, new int[]{163, 92, 255, 255, 255, 255, 255, 255, 92, 163}, new int[]{255, 127, 92, 171, 230, 230, 171, 92, 127, 255}, new int[]{255, 255, 163, 84, 25, 25, 84, 163, 255, 255}};

        /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/MenuItemIconFactory$RadioButtonMenuItemIcon$RadioKey.class */
        class RadioKey {

            /* renamed from: c, reason: collision with root package name */
            private Color f12123c;
            private Color background;
            private boolean selected;
            private final RadioButtonMenuItemIcon this$0;

            RadioKey(RadioButtonMenuItemIcon radioButtonMenuItemIcon, Color color, Color color2, boolean z2) {
                this.this$0 = radioButtonMenuItemIcon;
                this.f12123c = color;
                this.background = color2;
                this.selected = z2;
            }

            public boolean equals(Object obj) {
                if (obj == null || !(obj instanceof RadioKey)) {
                    return false;
                }
                RadioKey radioKey = (RadioKey) obj;
                return this.selected == radioKey.selected && this.f12123c.equals(radioKey.f12123c) && this.background.equals(radioKey.background);
            }

            public int hashCode() {
                return this.f12123c.hashCode() * this.background.hashCode() * (this.selected ? 1 : 2);
            }
        }

        private RadioButtonMenuItemIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            JMenuItem jMenuItem = (JMenuItem) component;
            ButtonModel model = jMenuItem.getModel();
            boolean zIsSelected = model.isSelected();
            boolean zIsEnabled = model.isEnabled();
            boolean zIsPressed = model.isPressed();
            boolean zIsArmed = model.isArmed();
            jMenuItem.getBackground();
            graphics.translate(i2, i3);
            ColorUIResource color = zIsEnabled ? (zIsPressed || zIsArmed) ? Theme.menuIconRolloverColor.getColor() : Theme.menuIconColor.getColor() : Theme.menuIconDisabledColor.getColor();
            if (TinyLookAndFeel.controlPanelInstantiated) {
                paintIconNoCache(graphics, color, zIsSelected);
            } else {
                paintIcon(graphics, color, jMenuItem, zIsSelected);
            }
            graphics.translate(-i2, -i3);
        }

        private void paintIcon(Graphics graphics, Color color, JMenuItem jMenuItem, boolean z2) {
            Color background = jMenuItem.getBackground();
            if (jMenuItem.getModel().isArmed()) {
                background = Theme.menuItemRolloverColor.getColor();
            } else if (background instanceof ColorUIResource) {
                background = Theme.menuPopupColor.getColor();
            }
            RadioKey radioKey = new RadioKey(this, color, background, z2);
            Object obj = MenuItemIconFactory.cache.get(radioKey);
            if (obj != null) {
                graphics.drawImage((Image) obj, 0, 0, jMenuItem);
                return;
            }
            BufferedImage bufferedImage = new BufferedImage(MenuItemIconFactory.CHECK_ICON_SIZE.width, MenuItemIconFactory.CHECK_ICON_SIZE.height, 2);
            Graphics graphics2 = bufferedImage.getGraphics();
            graphics2.setColor(background);
            graphics2.fillRect(0, 0, MenuItemIconFactory.CHECK_ICON_SIZE.width, MenuItemIconFactory.CHECK_ICON_SIZE.height);
            for (int i2 = 0; i2 < 10; i2++) {
                for (int i3 = 0; i3 < 10; i3++) {
                    if (ALPHA_BORDER[i3][i2] != 255) {
                        graphics2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - ALPHA_BORDER[i3][i2]));
                        graphics2.drawLine(i3, i2, i3, i2);
                    }
                }
            }
            if (z2) {
                for (int i4 = 3; i4 < 7; i4++) {
                    for (int i5 = 3; i5 < 7; i5++) {
                        graphics2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - ALPHA_CHECK[i5][i4]));
                        graphics2.drawLine(i5, i4, i5, i4);
                    }
                }
            }
            graphics2.dispose();
            graphics.drawImage(bufferedImage, 0, 0, jMenuItem);
            MenuItemIconFactory.cache.put(radioKey, bufferedImage);
        }

        private void paintIconNoCache(Graphics graphics, Color color, boolean z2) {
            for (int i2 = 0; i2 < 10; i2++) {
                for (int i3 = 0; i3 < 10; i3++) {
                    if (ALPHA_BORDER[i3][i2] != 255) {
                        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - ALPHA_BORDER[i3][i2]));
                        graphics.drawLine(i3, i2, i3, i2);
                    }
                }
            }
            if (z2) {
                for (int i4 = 3; i4 < 7; i4++) {
                    for (int i5 = 3; i5 < 7; i5++) {
                        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - ALPHA_CHECK[i5][i4]));
                        graphics.drawLine(i5, i4, i5, i4);
                    }
                }
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return MenuItemIconFactory.CHECK_ICON_SIZE.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MenuItemIconFactory.CHECK_ICON_SIZE.height;
        }

        RadioButtonMenuItemIcon(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/MenuItemIconFactory$SystemMenuIcon.class */
    private static class SystemMenuIcon implements Icon, UIResource, Serializable {
        private int style;

        SystemMenuIcon(int i2) {
            this.style = i2;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (component instanceof JMenuItem) {
                JMenuItem jMenuItem = (JMenuItem) component;
                if (!jMenuItem.isEnabled()) {
                    graphics.setColor(Theme.menuIconDisabledColor.getColor());
                } else if (jMenuItem.isArmed()) {
                    graphics.setColor(Theme.menuIconRolloverColor.getColor());
                } else {
                    graphics.setColor(Theme.menuIconColor.getColor());
                }
                if (this.style == 1) {
                    graphics.drawLine(i2 + 1, i3 + 1, i2 + 2, i3 + 1);
                    graphics.drawLine(i2 + 8, i3 + 1, i2 + 9, i3 + 1);
                    graphics.drawLine(i2 + 1, i3 + 2, i2 + 3, i3 + 2);
                    graphics.drawLine(i2 + 7, i3 + 2, i2 + 9, i3 + 2);
                    graphics.drawLine(i2 + 2, i3 + 3, i2 + 4, i3 + 3);
                    graphics.drawLine(i2 + 6, i3 + 3, i2 + 8, i3 + 3);
                    graphics.drawLine(i2 + 3, i3 + 4, i2 + 7, i3 + 4);
                    graphics.drawLine(i2 + 4, i3 + 5, i2 + 6, i3 + 5);
                    graphics.drawLine(i2 + 3, i3 + 6, i2 + 7, i3 + 6);
                    graphics.drawLine(i2 + 2, i3 + 7, i2 + 4, i3 + 7);
                    graphics.drawLine(i2 + 6, i3 + 7, i2 + 8, i3 + 7);
                    graphics.drawLine(i2 + 1, i3 + 8, i2 + 3, i3 + 8);
                    graphics.drawLine(i2 + 7, i3 + 8, i2 + 9, i3 + 8);
                    graphics.drawLine(i2 + 1, i3 + 9, i2 + 2, i3 + 9);
                    graphics.drawLine(i2 + 8, i3 + 9, i2 + 9, i3 + 9);
                    return;
                }
                if (this.style == 2) {
                    graphics.fillRect(i2 + 1, i2 + 8, 7, 2);
                    return;
                }
                if (this.style == 3) {
                    graphics.drawLine(i2 + 0, i3 + 0, i2 + 9, i3 + 0);
                    graphics.drawRect(i2 + 0, i2 + 1, 9, 8);
                } else if (this.style == 4) {
                    graphics.fillRect(i2 + 2, i2 + 1, 8, 2);
                    graphics.drawLine(i2 + 9, i3 + 3, i2 + 9, i3 + 6);
                    graphics.drawLine(i2 + 8, i3 + 6, i2 + 8, i3 + 6);
                    graphics.drawLine(i2 + 2, i3 + 3, i2 + 2, i3 + 3);
                    graphics.drawLine(i2 + 0, i3 + 4, i2 + 7, i3 + 4);
                    graphics.drawRect(i2 + 0, i2 + 5, 7, 4);
                }
            }
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

    public static Icon getCheckBoxMenuItemIcon() {
        if (checkBoxMenuItemIcon == null) {
            checkBoxMenuItemIcon = new CheckBoxMenuItemIcon(null);
        }
        return checkBoxMenuItemIcon;
    }

    public static Icon getRadioButtonMenuItemIcon() {
        if (radioButtonMenuItemIcon == null) {
            radioButtonMenuItemIcon = new RadioButtonMenuItemIcon(null);
        }
        return radioButtonMenuItemIcon;
    }

    public static Icon getMenuArrowIcon() {
        if (menuArrowIcon == null) {
            menuArrowIcon = new MenuArrowIcon(null);
        }
        return menuArrowIcon;
    }

    public static Icon getSystemCloseIcon() {
        if (systemCloseIcon == null) {
            systemCloseIcon = new SystemMenuIcon(1);
        }
        return systemCloseIcon;
    }

    public static Icon getSystemIconifyIcon() {
        if (systemIconifyIcon == null) {
            systemIconifyIcon = new SystemMenuIcon(2);
        }
        return systemIconifyIcon;
    }

    public static Icon getSystemMaximizeIcon() {
        if (systemMaximizeIcon == null) {
            systemMaximizeIcon = new SystemMenuIcon(3);
        }
        return systemMaximizeIcon;
    }

    public static Icon getSystemRestoreIcon() {
        if (systemRestoreIcon == null) {
            systemRestoreIcon = new SystemMenuIcon(4);
        }
        return systemRestoreIcon;
    }

    public static void clearCache() {
        cache.clear();
    }
}
