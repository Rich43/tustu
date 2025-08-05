package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.Serializable;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicIconFactory.class */
public class BasicIconFactory implements Serializable {
    private static Icon frame_icon;
    private static Icon checkBoxIcon;
    private static Icon radioButtonIcon;
    private static Icon checkBoxMenuItemIcon;
    private static Icon radioButtonMenuItemIcon;
    private static Icon menuItemCheckIcon;
    private static Icon menuItemArrowIcon;
    private static Icon menuArrowIcon;

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

    public static Icon createEmptyFrameIcon() {
        if (frame_icon == null) {
            frame_icon = new EmptyFrameIcon();
        }
        return frame_icon;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicIconFactory$EmptyFrameIcon.class */
    private static class EmptyFrameIcon implements Icon, Serializable {
        int height;
        int width;

        private EmptyFrameIcon() {
            this.height = 16;
            this.width = 14;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return this.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return this.height;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicIconFactory$CheckBoxIcon.class */
    private static class CheckBoxIcon implements Icon, Serializable {
        static final int csize = 13;

        private CheckBoxIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
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

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicIconFactory$RadioButtonIcon.class */
    private static class RadioButtonIcon implements Icon, UIResource, Serializable {
        private RadioButtonIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
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

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicIconFactory$CheckBoxMenuItemIcon.class */
    private static class CheckBoxMenuItemIcon implements Icon, UIResource, Serializable {
        private CheckBoxMenuItemIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (((AbstractButton) component).getModel().isSelected()) {
                graphics.drawLine(i2 + 7, i3 + 1, i2 + 7, i3 + 3);
                graphics.drawLine(i2 + 6, i3 + 2, i2 + 6, i3 + 4);
                graphics.drawLine(i2 + 5, i3 + 3, i2 + 5, i3 + 5);
                graphics.drawLine(i2 + 4, i3 + 4, i2 + 4, i3 + 6);
                graphics.drawLine(i2 + 3, i3 + 5, i2 + 3, i3 + 7);
                graphics.drawLine(i2 + 2, i3 + 4, i2 + 2, i3 + 6);
                graphics.drawLine(i2 + 1, i3 + 3, i2 + 1, i3 + 5);
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

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicIconFactory$RadioButtonMenuItemIcon.class */
    private static class RadioButtonMenuItemIcon implements Icon, UIResource, Serializable {
        private RadioButtonMenuItemIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            AbstractButton abstractButton = (AbstractButton) component;
            abstractButton.getModel();
            if (abstractButton.isSelected()) {
                graphics.fillOval(i2 + 1, i3 + 1, getIconWidth(), getIconHeight());
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 6;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 6;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicIconFactory$MenuItemCheckIcon.class */
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

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicIconFactory$MenuItemArrowIcon.class */
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

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicIconFactory$MenuArrowIcon.class */
    private static class MenuArrowIcon implements Icon, UIResource, Serializable {
        private MenuArrowIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            Polygon polygon = new Polygon();
            polygon.addPoint(i2, i3);
            polygon.addPoint(i2 + getIconWidth(), i3 + (getIconHeight() / 2));
            polygon.addPoint(i2, i3 + getIconHeight());
            graphics.fillPolygon(polygon);
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
}
