package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import sun.swing.CachedPainter;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory.class */
public class MetalIconFactory implements Serializable {
    private static Icon fileChooserDetailViewIcon;
    private static Icon fileChooserHomeFolderIcon;
    private static Icon fileChooserListViewIcon;
    private static Icon fileChooserNewFolderIcon;
    private static Icon fileChooserUpFolderIcon;
    private static Icon internalFrameAltMaximizeIcon;
    private static Icon internalFrameCloseIcon;
    private static Icon internalFrameDefaultMenuIcon;
    private static Icon internalFrameMaximizeIcon;
    private static Icon internalFrameMinimizeIcon;
    private static Icon radioButtonIcon;
    private static Icon treeComputerIcon;
    private static Icon treeFloppyDriveIcon;
    private static Icon treeHardDriveIcon;
    private static Icon menuArrowIcon;
    private static Icon menuItemArrowIcon;
    private static Icon checkBoxMenuItemIcon;
    private static Icon radioButtonMenuItemIcon;
    private static Icon checkBoxIcon;
    private static Icon oceanHorizontalSliderThumb;
    private static Icon oceanVerticalSliderThumb;
    public static final boolean DARK = false;
    public static final boolean LIGHT = true;
    private static final Dimension folderIcon16Size = new Dimension(16, 16);
    private static final Dimension fileIcon16Size = new Dimension(16, 16);
    private static final Dimension treeControlSize = new Dimension(18, 18);
    private static final Dimension menuArrowIconSize = new Dimension(4, 8);
    private static final Dimension menuCheckIconSize = new Dimension(10, 10);
    private static final int xOff = 4;

    public static Icon getFileChooserDetailViewIcon() {
        if (fileChooserDetailViewIcon == null) {
            fileChooserDetailViewIcon = new FileChooserDetailViewIcon();
        }
        return fileChooserDetailViewIcon;
    }

    public static Icon getFileChooserHomeFolderIcon() {
        if (fileChooserHomeFolderIcon == null) {
            fileChooserHomeFolderIcon = new FileChooserHomeFolderIcon();
        }
        return fileChooserHomeFolderIcon;
    }

    public static Icon getFileChooserListViewIcon() {
        if (fileChooserListViewIcon == null) {
            fileChooserListViewIcon = new FileChooserListViewIcon();
        }
        return fileChooserListViewIcon;
    }

    public static Icon getFileChooserNewFolderIcon() {
        if (fileChooserNewFolderIcon == null) {
            fileChooserNewFolderIcon = new FileChooserNewFolderIcon();
        }
        return fileChooserNewFolderIcon;
    }

    public static Icon getFileChooserUpFolderIcon() {
        if (fileChooserUpFolderIcon == null) {
            fileChooserUpFolderIcon = new FileChooserUpFolderIcon();
        }
        return fileChooserUpFolderIcon;
    }

    public static Icon getInternalFrameAltMaximizeIcon(int i2) {
        return new InternalFrameAltMaximizeIcon(i2);
    }

    public static Icon getInternalFrameCloseIcon(int i2) {
        return new InternalFrameCloseIcon(i2);
    }

    public static Icon getInternalFrameDefaultMenuIcon() {
        if (internalFrameDefaultMenuIcon == null) {
            internalFrameDefaultMenuIcon = new InternalFrameDefaultMenuIcon();
        }
        return internalFrameDefaultMenuIcon;
    }

    public static Icon getInternalFrameMaximizeIcon(int i2) {
        return new InternalFrameMaximizeIcon(i2);
    }

    public static Icon getInternalFrameMinimizeIcon(int i2) {
        return new InternalFrameMinimizeIcon(i2);
    }

    public static Icon getRadioButtonIcon() {
        if (radioButtonIcon == null) {
            radioButtonIcon = new RadioButtonIcon();
        }
        return radioButtonIcon;
    }

    public static Icon getCheckBoxIcon() {
        if (checkBoxIcon == null) {
            checkBoxIcon = new CheckBoxIcon();
        }
        return checkBoxIcon;
    }

    public static Icon getTreeComputerIcon() {
        if (treeComputerIcon == null) {
            treeComputerIcon = new TreeComputerIcon();
        }
        return treeComputerIcon;
    }

    public static Icon getTreeFloppyDriveIcon() {
        if (treeFloppyDriveIcon == null) {
            treeFloppyDriveIcon = new TreeFloppyDriveIcon();
        }
        return treeFloppyDriveIcon;
    }

    public static Icon getTreeFolderIcon() {
        return new TreeFolderIcon();
    }

    public static Icon getTreeHardDriveIcon() {
        if (treeHardDriveIcon == null) {
            treeHardDriveIcon = new TreeHardDriveIcon();
        }
        return treeHardDriveIcon;
    }

    public static Icon getTreeLeafIcon() {
        return new TreeLeafIcon();
    }

    public static Icon getTreeControlIcon(boolean z2) {
        return new TreeControlIcon(z2);
    }

    public static Icon getMenuArrowIcon() {
        if (menuArrowIcon == null) {
            menuArrowIcon = new MenuArrowIcon();
        }
        return menuArrowIcon;
    }

    public static Icon getMenuItemCheckIcon() {
        return null;
    }

    public static Icon getMenuItemArrowIcon() {
        if (menuItemArrowIcon == null) {
            menuItemArrowIcon = new MenuItemArrowIcon();
        }
        return menuItemArrowIcon;
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

    public static Icon getHorizontalSliderThumbIcon() {
        if (MetalLookAndFeel.usingOcean()) {
            if (oceanHorizontalSliderThumb == null) {
                oceanHorizontalSliderThumb = new OceanHorizontalSliderThumbIcon();
            }
            return oceanHorizontalSliderThumb;
        }
        return new HorizontalSliderThumbIcon();
    }

    public static Icon getVerticalSliderThumbIcon() {
        if (MetalLookAndFeel.usingOcean()) {
            if (oceanVerticalSliderThumb == null) {
                oceanVerticalSliderThumb = new OceanVerticalSliderThumbIcon();
            }
            return oceanVerticalSliderThumb;
        }
        return new VerticalSliderThumbIcon();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$FileChooserDetailViewIcon.class */
    private static class FileChooserDetailViewIcon implements Icon, UIResource, Serializable {
        private FileChooserDetailViewIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(2, 2, 5, 2);
            graphics.drawLine(2, 3, 2, 7);
            graphics.drawLine(3, 7, 6, 7);
            graphics.drawLine(6, 6, 6, 3);
            graphics.drawLine(2, 10, 5, 10);
            graphics.drawLine(2, 11, 2, 15);
            graphics.drawLine(3, 15, 6, 15);
            graphics.drawLine(6, 14, 6, 11);
            graphics.drawLine(8, 5, 15, 5);
            graphics.drawLine(8, 13, 15, 13);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.drawRect(3, 3, 2, 3);
            graphics.drawRect(3, 11, 2, 3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
            graphics.drawLine(4, 4, 4, 5);
            graphics.drawLine(4, 12, 4, 13);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 18;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 18;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$FileChooserHomeFolderIcon.class */
    private static class FileChooserHomeFolderIcon implements Icon, UIResource, Serializable {
        private FileChooserHomeFolderIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(8, 1, 1, 8);
            graphics.drawLine(8, 1, 15, 8);
            graphics.drawLine(11, 2, 11, 3);
            graphics.drawLine(12, 2, 12, 4);
            graphics.drawLine(3, 7, 3, 15);
            graphics.drawLine(13, 7, 13, 15);
            graphics.drawLine(4, 15, 12, 15);
            graphics.drawLine(6, 9, 6, 14);
            graphics.drawLine(10, 9, 10, 14);
            graphics.drawLine(7, 9, 9, 9);
            graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
            graphics.fillRect(8, 2, 1, 1);
            graphics.fillRect(7, 3, 3, 1);
            graphics.fillRect(6, 4, 5, 1);
            graphics.fillRect(5, 5, 7, 1);
            graphics.fillRect(4, 6, 9, 2);
            graphics.drawLine(9, 12, 9, 12);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.drawLine(4, 8, 12, 8);
            graphics.fillRect(4, 9, 2, 6);
            graphics.fillRect(11, 9, 2, 6);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 18;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 18;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$FileChooserListViewIcon.class */
    private static class FileChooserListViewIcon implements Icon, UIResource, Serializable {
        private FileChooserListViewIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(2, 2, 5, 2);
            graphics.drawLine(2, 3, 2, 7);
            graphics.drawLine(3, 7, 6, 7);
            graphics.drawLine(6, 6, 6, 3);
            graphics.drawLine(10, 2, 13, 2);
            graphics.drawLine(10, 3, 10, 7);
            graphics.drawLine(11, 7, 14, 7);
            graphics.drawLine(14, 6, 14, 3);
            graphics.drawLine(2, 10, 5, 10);
            graphics.drawLine(2, 11, 2, 15);
            graphics.drawLine(3, 15, 6, 15);
            graphics.drawLine(6, 14, 6, 11);
            graphics.drawLine(10, 10, 13, 10);
            graphics.drawLine(10, 11, 10, 15);
            graphics.drawLine(11, 15, 14, 15);
            graphics.drawLine(14, 14, 14, 11);
            graphics.drawLine(8, 5, 8, 5);
            graphics.drawLine(16, 5, 16, 5);
            graphics.drawLine(8, 13, 8, 13);
            graphics.drawLine(16, 13, 16, 13);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.drawRect(3, 3, 2, 3);
            graphics.drawRect(11, 3, 2, 3);
            graphics.drawRect(3, 11, 2, 3);
            graphics.drawRect(11, 11, 2, 3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
            graphics.drawLine(4, 4, 4, 5);
            graphics.drawLine(12, 4, 12, 5);
            graphics.drawLine(4, 12, 4, 13);
            graphics.drawLine(12, 12, 12, 13);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 18;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 18;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$FileChooserNewFolderIcon.class */
    private static class FileChooserNewFolderIcon implements Icon, UIResource, Serializable {
        private FileChooserNewFolderIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.fillRect(3, 5, 12, 9);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(1, 6, 1, 14);
            graphics.drawLine(2, 14, 15, 14);
            graphics.drawLine(15, 13, 15, 5);
            graphics.drawLine(2, 5, 9, 5);
            graphics.drawLine(10, 6, 14, 6);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
            graphics.drawLine(2, 6, 2, 13);
            graphics.drawLine(3, 6, 9, 6);
            graphics.drawLine(10, 7, 14, 7);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            graphics.drawLine(11, 3, 15, 3);
            graphics.drawLine(10, 4, 15, 4);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 18;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 18;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$FileChooserUpFolderIcon.class */
    private static class FileChooserUpFolderIcon implements Icon, UIResource, Serializable {
        private FileChooserUpFolderIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.fillRect(3, 5, 12, 9);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(1, 6, 1, 14);
            graphics.drawLine(2, 14, 15, 14);
            graphics.drawLine(15, 13, 15, 5);
            graphics.drawLine(2, 5, 9, 5);
            graphics.drawLine(10, 6, 14, 6);
            graphics.drawLine(8, 13, 8, 16);
            graphics.drawLine(8, 9, 8, 9);
            graphics.drawLine(7, 10, 9, 10);
            graphics.drawLine(6, 11, 10, 11);
            graphics.drawLine(5, 12, 11, 12);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
            graphics.drawLine(2, 6, 2, 13);
            graphics.drawLine(3, 6, 9, 6);
            graphics.drawLine(10, 7, 14, 7);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            graphics.drawLine(11, 3, 15, 3);
            graphics.drawLine(10, 4, 15, 4);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 18;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 18;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$PaletteCloseIcon.class */
    public static class PaletteCloseIcon implements Icon, UIResource, Serializable {
        int iconSize = 7;

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            ColorUIResource primaryControlDarkShadow;
            ButtonModel model = ((JButton) component).getModel();
            ColorUIResource primaryControlHighlight = MetalLookAndFeel.getPrimaryControlHighlight();
            ColorUIResource primaryControlInfo = MetalLookAndFeel.getPrimaryControlInfo();
            if (model.isPressed() && model.isArmed()) {
                primaryControlDarkShadow = primaryControlInfo;
            } else {
                primaryControlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
            }
            graphics.translate(i2, i3);
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawLine(0, 1, 5, 6);
            graphics.drawLine(1, 0, 6, 5);
            graphics.drawLine(1, 1, 6, 6);
            graphics.drawLine(6, 1, 1, 6);
            graphics.drawLine(5, 0, 0, 5);
            graphics.drawLine(5, 1, 1, 5);
            graphics.setColor(primaryControlHighlight);
            graphics.drawLine(6, 2, 5, 3);
            graphics.drawLine(2, 6, 3, 5);
            graphics.drawLine(6, 6, 6, 6);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return this.iconSize;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return this.iconSize;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$InternalFrameCloseIcon.class */
    private static class InternalFrameCloseIcon implements Icon, UIResource, Serializable {
        int iconSize;

        public InternalFrameCloseIcon(int i2) {
            this.iconSize = 16;
            this.iconSize = i2;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            JButton jButton = (JButton) component;
            ButtonModel model = jButton.getModel();
            ColorUIResource primaryControl = MetalLookAndFeel.getPrimaryControl();
            ColorUIResource primaryControl2 = MetalLookAndFeel.getPrimaryControl();
            ColorUIResource primaryControlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
            ColorUIResource black = MetalLookAndFeel.getBlack();
            ColorUIResource white = MetalLookAndFeel.getWhite();
            ColorUIResource white2 = MetalLookAndFeel.getWhite();
            if (jButton.getClientProperty("paintActive") != Boolean.TRUE) {
                primaryControl = MetalLookAndFeel.getControl();
                primaryControl2 = primaryControl;
                primaryControlDarkShadow = MetalLookAndFeel.getControlDarkShadow();
                if (model.isPressed() && model.isArmed()) {
                    primaryControl2 = MetalLookAndFeel.getControlShadow();
                    white = primaryControl2;
                    primaryControlDarkShadow = black;
                }
            } else if (model.isPressed() && model.isArmed()) {
                primaryControl2 = MetalLookAndFeel.getPrimaryControlShadow();
                white = primaryControl2;
                primaryControlDarkShadow = black;
            }
            int i4 = this.iconSize / 2;
            graphics.translate(i2, i3);
            graphics.setColor(primaryControl);
            graphics.fillRect(0, 0, this.iconSize, this.iconSize);
            graphics.setColor(primaryControl2);
            graphics.fillRect(3, 3, this.iconSize - 6, this.iconSize - 6);
            graphics.setColor(black);
            graphics.drawRect(1, 1, this.iconSize - 3, this.iconSize - 3);
            graphics.drawRect(2, 2, this.iconSize - 5, this.iconSize - 5);
            graphics.setColor(white2);
            graphics.drawRect(2, 2, this.iconSize - 3, this.iconSize - 3);
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawRect(2, 2, this.iconSize - 4, this.iconSize - 4);
            graphics.drawLine(3, this.iconSize - 3, 3, this.iconSize - 3);
            graphics.drawLine(this.iconSize - 3, 3, this.iconSize - 3, 3);
            graphics.setColor(black);
            graphics.drawLine(4, 5, 5, 4);
            graphics.drawLine(4, this.iconSize - 6, this.iconSize - 6, 4);
            graphics.setColor(white);
            graphics.drawLine(6, this.iconSize - 5, this.iconSize - 5, 6);
            graphics.drawLine(i4, i4 + 2, i4 + 2, i4);
            graphics.drawLine(this.iconSize - 5, this.iconSize - 5, this.iconSize - 4, this.iconSize - 5);
            graphics.drawLine(this.iconSize - 5, this.iconSize - 4, this.iconSize - 5, this.iconSize - 4);
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawLine(5, 5, this.iconSize - 6, this.iconSize - 6);
            graphics.drawLine(6, 5, this.iconSize - 5, this.iconSize - 6);
            graphics.drawLine(5, 6, this.iconSize - 6, this.iconSize - 5);
            graphics.drawLine(5, this.iconSize - 5, this.iconSize - 5, 5);
            graphics.drawLine(5, this.iconSize - 6, this.iconSize - 6, 5);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return this.iconSize;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return this.iconSize;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$InternalFrameAltMaximizeIcon.class */
    private static class InternalFrameAltMaximizeIcon implements Icon, UIResource, Serializable {
        int iconSize;

        public InternalFrameAltMaximizeIcon(int i2) {
            this.iconSize = 16;
            this.iconSize = i2;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            JButton jButton = (JButton) component;
            ButtonModel model = jButton.getModel();
            ColorUIResource primaryControl = MetalLookAndFeel.getPrimaryControl();
            ColorUIResource primaryControl2 = MetalLookAndFeel.getPrimaryControl();
            ColorUIResource primaryControlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
            ColorUIResource black = MetalLookAndFeel.getBlack();
            ColorUIResource white = MetalLookAndFeel.getWhite();
            ColorUIResource white2 = MetalLookAndFeel.getWhite();
            if (jButton.getClientProperty("paintActive") != Boolean.TRUE) {
                primaryControl = MetalLookAndFeel.getControl();
                primaryControl2 = primaryControl;
                primaryControlDarkShadow = MetalLookAndFeel.getControlDarkShadow();
                if (model.isPressed() && model.isArmed()) {
                    primaryControl2 = MetalLookAndFeel.getControlShadow();
                    white = primaryControl2;
                    primaryControlDarkShadow = black;
                }
            } else if (model.isPressed() && model.isArmed()) {
                primaryControl2 = MetalLookAndFeel.getPrimaryControlShadow();
                white = primaryControl2;
                primaryControlDarkShadow = black;
            }
            graphics.translate(i2, i3);
            graphics.setColor(primaryControl);
            graphics.fillRect(0, 0, this.iconSize, this.iconSize);
            graphics.setColor(primaryControl2);
            graphics.fillRect(3, 6, this.iconSize - 9, this.iconSize - 9);
            graphics.setColor(black);
            graphics.drawRect(1, 5, this.iconSize - 8, this.iconSize - 8);
            graphics.drawLine(1, this.iconSize - 2, 1, this.iconSize - 2);
            graphics.setColor(white2);
            graphics.drawRect(2, 6, this.iconSize - 7, this.iconSize - 7);
            graphics.setColor(white);
            graphics.drawRect(3, 7, this.iconSize - 9, this.iconSize - 9);
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawRect(2, 6, this.iconSize - 8, this.iconSize - 8);
            graphics.setColor(white);
            graphics.drawLine(this.iconSize - 6, 8, this.iconSize - 6, 8);
            graphics.drawLine(this.iconSize - 9, 6, this.iconSize - 7, 8);
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawLine(3, this.iconSize - 3, 3, this.iconSize - 3);
            graphics.setColor(black);
            graphics.drawLine(this.iconSize - 6, 9, this.iconSize - 6, 9);
            graphics.setColor(primaryControl);
            graphics.drawLine(this.iconSize - 9, 5, this.iconSize - 9, 5);
            graphics.setColor(primaryControlDarkShadow);
            graphics.fillRect(this.iconSize - 7, 3, 3, 5);
            graphics.drawLine(this.iconSize - 6, 5, this.iconSize - 3, 2);
            graphics.drawLine(this.iconSize - 6, 6, this.iconSize - 2, 2);
            graphics.drawLine(this.iconSize - 6, 7, this.iconSize - 3, 7);
            graphics.setColor(black);
            graphics.drawLine(this.iconSize - 8, 2, this.iconSize - 7, 2);
            graphics.drawLine(this.iconSize - 8, 3, this.iconSize - 8, 7);
            graphics.drawLine(this.iconSize - 6, 4, this.iconSize - 3, 1);
            graphics.drawLine(this.iconSize - 4, 6, this.iconSize - 3, 6);
            graphics.setColor(white2);
            graphics.drawLine(this.iconSize - 6, 3, this.iconSize - 6, 3);
            graphics.drawLine(this.iconSize - 4, 5, this.iconSize - 2, 3);
            graphics.drawLine(this.iconSize - 4, 8, this.iconSize - 3, 8);
            graphics.drawLine(this.iconSize - 2, 8, this.iconSize - 2, 7);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return this.iconSize;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return this.iconSize;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$InternalFrameDefaultMenuIcon.class */
    private static class InternalFrameDefaultMenuIcon implements Icon, UIResource, Serializable {
        private InternalFrameDefaultMenuIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            ColorUIResource windowBackground = MetalLookAndFeel.getWindowBackground();
            ColorUIResource primaryControl = MetalLookAndFeel.getPrimaryControl();
            ColorUIResource primaryControlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
            graphics.translate(i2, i3);
            graphics.setColor(primaryControl);
            graphics.fillRect(0, 0, 16, 16);
            graphics.setColor(windowBackground);
            graphics.fillRect(2, 6, 13, 9);
            graphics.drawLine(2, 2, 2, 2);
            graphics.drawLine(5, 2, 5, 2);
            graphics.drawLine(8, 2, 8, 2);
            graphics.drawLine(11, 2, 11, 2);
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawRect(1, 1, 13, 13);
            graphics.drawLine(1, 0, 14, 0);
            graphics.drawLine(15, 1, 15, 14);
            graphics.drawLine(1, 15, 14, 15);
            graphics.drawLine(0, 1, 0, 14);
            graphics.drawLine(2, 5, 13, 5);
            graphics.drawLine(3, 3, 3, 3);
            graphics.drawLine(6, 3, 6, 3);
            graphics.drawLine(9, 3, 9, 3);
            graphics.drawLine(12, 3, 12, 3);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 16;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 16;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$InternalFrameMaximizeIcon.class */
    private static class InternalFrameMaximizeIcon implements Icon, UIResource, Serializable {
        protected int iconSize;

        public InternalFrameMaximizeIcon(int i2) {
            this.iconSize = 16;
            this.iconSize = i2;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            JButton jButton = (JButton) component;
            ButtonModel model = jButton.getModel();
            ColorUIResource primaryControl = MetalLookAndFeel.getPrimaryControl();
            ColorUIResource primaryControl2 = MetalLookAndFeel.getPrimaryControl();
            ColorUIResource primaryControlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
            ColorUIResource black = MetalLookAndFeel.getBlack();
            ColorUIResource white = MetalLookAndFeel.getWhite();
            ColorUIResource white2 = MetalLookAndFeel.getWhite();
            if (jButton.getClientProperty("paintActive") != Boolean.TRUE) {
                primaryControl = MetalLookAndFeel.getControl();
                primaryControl2 = primaryControl;
                primaryControlDarkShadow = MetalLookAndFeel.getControlDarkShadow();
                if (model.isPressed() && model.isArmed()) {
                    primaryControl2 = MetalLookAndFeel.getControlShadow();
                    white = primaryControl2;
                    primaryControlDarkShadow = black;
                }
            } else if (model.isPressed() && model.isArmed()) {
                primaryControl2 = MetalLookAndFeel.getPrimaryControlShadow();
                white = primaryControl2;
                primaryControlDarkShadow = black;
            }
            graphics.translate(i2, i3);
            graphics.setColor(primaryControl);
            graphics.fillRect(0, 0, this.iconSize, this.iconSize);
            graphics.setColor(primaryControl2);
            graphics.fillRect(3, 7, this.iconSize - 10, this.iconSize - 10);
            graphics.setColor(white);
            graphics.drawRect(3, 7, this.iconSize - 10, this.iconSize - 10);
            graphics.setColor(white2);
            graphics.drawRect(2, 6, this.iconSize - 7, this.iconSize - 7);
            graphics.setColor(black);
            graphics.drawRect(1, 5, this.iconSize - 7, this.iconSize - 7);
            graphics.drawRect(2, 6, this.iconSize - 9, this.iconSize - 9);
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawRect(2, 6, this.iconSize - 8, this.iconSize - 8);
            graphics.setColor(black);
            graphics.drawLine(3, this.iconSize - 5, this.iconSize - 9, 7);
            graphics.drawLine(this.iconSize - 6, 4, this.iconSize - 5, 3);
            graphics.drawLine(this.iconSize - 7, 1, this.iconSize - 7, 2);
            graphics.drawLine(this.iconSize - 6, 1, this.iconSize - 2, 1);
            graphics.setColor(white);
            graphics.drawLine(5, this.iconSize - 4, this.iconSize - 8, 9);
            graphics.setColor(white2);
            graphics.drawLine(this.iconSize - 6, 3, this.iconSize - 4, 5);
            graphics.drawLine(this.iconSize - 4, 5, this.iconSize - 4, 6);
            graphics.drawLine(this.iconSize - 2, 7, this.iconSize - 1, 7);
            graphics.drawLine(this.iconSize - 1, 2, this.iconSize - 1, 6);
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawLine(3, this.iconSize - 4, this.iconSize - 3, 2);
            graphics.drawLine(3, this.iconSize - 3, this.iconSize - 2, 2);
            graphics.drawLine(4, this.iconSize - 3, 5, this.iconSize - 3);
            graphics.drawLine(this.iconSize - 7, 8, this.iconSize - 7, 9);
            graphics.drawLine(this.iconSize - 6, 2, this.iconSize - 4, 2);
            graphics.drawRect(this.iconSize - 3, 3, 1, 3);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return this.iconSize;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return this.iconSize;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$InternalFrameMinimizeIcon.class */
    private static class InternalFrameMinimizeIcon implements Icon, UIResource, Serializable {
        int iconSize;

        public InternalFrameMinimizeIcon(int i2) {
            this.iconSize = 16;
            this.iconSize = i2;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            JButton jButton = (JButton) component;
            ButtonModel model = jButton.getModel();
            ColorUIResource primaryControl = MetalLookAndFeel.getPrimaryControl();
            ColorUIResource primaryControl2 = MetalLookAndFeel.getPrimaryControl();
            ColorUIResource primaryControlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
            ColorUIResource black = MetalLookAndFeel.getBlack();
            ColorUIResource white = MetalLookAndFeel.getWhite();
            ColorUIResource white2 = MetalLookAndFeel.getWhite();
            if (jButton.getClientProperty("paintActive") != Boolean.TRUE) {
                primaryControl = MetalLookAndFeel.getControl();
                primaryControl2 = primaryControl;
                primaryControlDarkShadow = MetalLookAndFeel.getControlDarkShadow();
                if (model.isPressed() && model.isArmed()) {
                    primaryControl2 = MetalLookAndFeel.getControlShadow();
                    white = primaryControl2;
                    primaryControlDarkShadow = black;
                }
            } else if (model.isPressed() && model.isArmed()) {
                primaryControl2 = MetalLookAndFeel.getPrimaryControlShadow();
                white = primaryControl2;
                primaryControlDarkShadow = black;
            }
            graphics.translate(i2, i3);
            graphics.setColor(primaryControl);
            graphics.fillRect(0, 0, this.iconSize, this.iconSize);
            graphics.setColor(primaryControl2);
            graphics.fillRect(4, 11, this.iconSize - 13, this.iconSize - 13);
            graphics.setColor(white2);
            graphics.drawRect(2, 10, this.iconSize - 10, this.iconSize - 11);
            graphics.setColor(white);
            graphics.drawRect(3, 10, this.iconSize - 12, this.iconSize - 12);
            graphics.setColor(black);
            graphics.drawRect(1, 8, this.iconSize - 10, this.iconSize - 10);
            graphics.drawRect(2, 9, this.iconSize - 12, this.iconSize - 12);
            graphics.setColor(primaryControlDarkShadow);
            graphics.drawRect(2, 9, this.iconSize - 11, this.iconSize - 11);
            graphics.drawLine(this.iconSize - 10, 10, this.iconSize - 10, 10);
            graphics.drawLine(3, this.iconSize - 3, 3, this.iconSize - 3);
            graphics.setColor(primaryControlDarkShadow);
            graphics.fillRect(this.iconSize - 7, 3, 3, 5);
            graphics.drawLine(this.iconSize - 6, 5, this.iconSize - 3, 2);
            graphics.drawLine(this.iconSize - 6, 6, this.iconSize - 2, 2);
            graphics.drawLine(this.iconSize - 6, 7, this.iconSize - 3, 7);
            graphics.setColor(black);
            graphics.drawLine(this.iconSize - 8, 2, this.iconSize - 7, 2);
            graphics.drawLine(this.iconSize - 8, 3, this.iconSize - 8, 7);
            graphics.drawLine(this.iconSize - 6, 4, this.iconSize - 3, 1);
            graphics.drawLine(this.iconSize - 4, 6, this.iconSize - 3, 6);
            graphics.setColor(white2);
            graphics.drawLine(this.iconSize - 6, 3, this.iconSize - 6, 3);
            graphics.drawLine(this.iconSize - 4, 5, this.iconSize - 2, 3);
            graphics.drawLine(this.iconSize - 7, 8, this.iconSize - 3, 8);
            graphics.drawLine(this.iconSize - 2, 8, this.iconSize - 2, 7);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return this.iconSize;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return this.iconSize;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$CheckBoxIcon.class */
    private static class CheckBoxIcon implements Icon, UIResource, Serializable {
        private CheckBoxIcon() {
        }

        protected int getControlSize() {
            return 13;
        }

        private void paintOceanIcon(Component component, Graphics graphics, int i2, int i3) {
            ButtonModel model = ((JCheckBox) component).getModel();
            graphics.translate(i2, i3);
            int iconWidth = getIconWidth();
            int iconHeight = getIconHeight();
            if (model.isEnabled()) {
                if (model.isPressed() && model.isArmed()) {
                    graphics.setColor(MetalLookAndFeel.getControlShadow());
                    graphics.fillRect(0, 0, iconWidth, iconHeight);
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.fillRect(0, 0, iconWidth, 2);
                    graphics.fillRect(0, 2, 2, iconHeight - 2);
                    graphics.fillRect(iconWidth - 1, 1, 1, iconHeight - 1);
                    graphics.fillRect(1, iconHeight - 1, iconWidth - 2, 1);
                } else if (model.isRollover()) {
                    MetalUtils.drawGradient(component, graphics, "CheckBox.gradient", 0, 0, iconWidth, iconHeight, true);
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawRect(0, 0, iconWidth - 1, iconHeight - 1);
                    graphics.setColor(MetalLookAndFeel.getPrimaryControl());
                    graphics.drawRect(1, 1, iconWidth - 3, iconHeight - 3);
                    graphics.drawRect(2, 2, iconWidth - 5, iconHeight - 5);
                } else {
                    MetalUtils.drawGradient(component, graphics, "CheckBox.gradient", 0, 0, iconWidth, iconHeight, true);
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawRect(0, 0, iconWidth - 1, iconHeight - 1);
                }
                graphics.setColor(MetalLookAndFeel.getControlInfo());
            } else {
                graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                graphics.drawRect(0, 0, iconWidth - 1, iconHeight - 1);
            }
            graphics.translate(-i2, -i3);
            if (model.isSelected()) {
                drawCheck(component, graphics, i2, i3);
            }
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (MetalLookAndFeel.usingOcean()) {
                paintOceanIcon(component, graphics, i2, i3);
                return;
            }
            ButtonModel model = ((JCheckBox) component).getModel();
            int controlSize = getControlSize();
            if (model.isEnabled()) {
                if (model.isPressed() && model.isArmed()) {
                    graphics.setColor(MetalLookAndFeel.getControlShadow());
                    graphics.fillRect(i2, i3, controlSize - 1, controlSize - 1);
                    MetalUtils.drawPressed3DBorder(graphics, i2, i3, controlSize, controlSize);
                } else {
                    MetalUtils.drawFlush3DBorder(graphics, i2, i3, controlSize, controlSize);
                }
                graphics.setColor(component.getForeground());
            } else {
                graphics.setColor(MetalLookAndFeel.getControlShadow());
                graphics.drawRect(i2, i3, controlSize - 2, controlSize - 2);
            }
            if (model.isSelected()) {
                drawCheck(component, graphics, i2, i3);
            }
        }

        protected void drawCheck(Component component, Graphics graphics, int i2, int i3) {
            int controlSize = getControlSize();
            graphics.fillRect(i2 + 3, i3 + 5, 2, controlSize - 8);
            graphics.drawLine(i2 + (controlSize - 4), i3 + 3, i2 + 5, i3 + (controlSize - 6));
            graphics.drawLine(i2 + (controlSize - 4), i3 + 4, i2 + 5, i3 + (controlSize - 5));
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return getControlSize();
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return getControlSize();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$RadioButtonIcon.class */
    private static class RadioButtonIcon implements Icon, UIResource, Serializable {
        private RadioButtonIcon() {
        }

        public void paintOceanIcon(Component component, Graphics graphics, int i2, int i3) {
            ButtonModel model = ((JRadioButton) component).getModel();
            boolean zIsEnabled = model.isEnabled();
            boolean z2 = zIsEnabled && model.isPressed() && model.isArmed();
            boolean z3 = zIsEnabled && model.isRollover();
            graphics.translate(i2, i3);
            if (zIsEnabled && !z2) {
                MetalUtils.drawGradient(component, graphics, "RadioButton.gradient", 1, 1, 10, 10, true);
                graphics.setColor(component.getBackground());
                graphics.fillRect(1, 1, 1, 1);
                graphics.fillRect(10, 1, 1, 1);
                graphics.fillRect(1, 10, 1, 1);
                graphics.fillRect(10, 10, 1, 1);
            } else if (z2 || !zIsEnabled) {
                if (z2) {
                    graphics.setColor(MetalLookAndFeel.getPrimaryControl());
                } else {
                    graphics.setColor(MetalLookAndFeel.getControl());
                }
                graphics.fillRect(2, 2, 8, 8);
                graphics.fillRect(4, 1, 4, 1);
                graphics.fillRect(4, 10, 4, 1);
                graphics.fillRect(1, 4, 1, 4);
                graphics.fillRect(10, 4, 1, 4);
            }
            if (!zIsEnabled) {
                graphics.setColor(MetalLookAndFeel.getInactiveControlTextColor());
            } else {
                graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
            }
            graphics.drawLine(4, 0, 7, 0);
            graphics.drawLine(8, 1, 9, 1);
            graphics.drawLine(10, 2, 10, 3);
            graphics.drawLine(11, 4, 11, 7);
            graphics.drawLine(10, 8, 10, 9);
            graphics.drawLine(9, 10, 8, 10);
            graphics.drawLine(7, 11, 4, 11);
            graphics.drawLine(3, 10, 2, 10);
            graphics.drawLine(1, 9, 1, 8);
            graphics.drawLine(0, 7, 0, 4);
            graphics.drawLine(1, 3, 1, 2);
            graphics.drawLine(2, 1, 3, 1);
            if (z2) {
                graphics.fillRect(1, 4, 1, 4);
                graphics.fillRect(2, 2, 1, 2);
                graphics.fillRect(3, 2, 1, 1);
                graphics.fillRect(4, 1, 4, 1);
            } else if (z3) {
                graphics.setColor(MetalLookAndFeel.getPrimaryControl());
                graphics.fillRect(4, 1, 4, 2);
                graphics.fillRect(8, 2, 2, 2);
                graphics.fillRect(9, 4, 2, 4);
                graphics.fillRect(8, 8, 2, 2);
                graphics.fillRect(4, 9, 4, 2);
                graphics.fillRect(2, 8, 2, 2);
                graphics.fillRect(1, 4, 2, 4);
                graphics.fillRect(2, 2, 2, 2);
            }
            if (model.isSelected()) {
                if (zIsEnabled) {
                    graphics.setColor(MetalLookAndFeel.getControlInfo());
                } else {
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                }
                graphics.fillRect(4, 4, 4, 4);
                graphics.drawLine(4, 3, 7, 3);
                graphics.drawLine(8, 4, 8, 7);
                graphics.drawLine(7, 8, 4, 8);
                graphics.drawLine(3, 7, 3, 4);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (MetalLookAndFeel.usingOcean()) {
                paintOceanIcon(component, graphics, i2, i3);
                return;
            }
            ButtonModel model = ((JRadioButton) component).getModel();
            boolean zIsSelected = model.isSelected();
            Color background = component.getBackground();
            Color foreground = component.getForeground();
            ColorUIResource controlShadow = MetalLookAndFeel.getControlShadow();
            ColorUIResource controlDarkShadow = MetalLookAndFeel.getControlDarkShadow();
            Color controlHighlight = MetalLookAndFeel.getControlHighlight();
            Color controlHighlight2 = MetalLookAndFeel.getControlHighlight();
            Color color = background;
            if (!model.isEnabled()) {
                controlHighlight2 = background;
                controlHighlight = background;
                foreground = controlShadow;
                controlDarkShadow = controlShadow;
            } else if (model.isPressed() && model.isArmed()) {
                color = controlShadow;
                controlHighlight = controlShadow;
            }
            graphics.translate(i2, i3);
            if (component.isOpaque()) {
                graphics.setColor(color);
                graphics.fillRect(2, 2, 9, 9);
            }
            graphics.setColor(controlDarkShadow);
            graphics.drawLine(4, 0, 7, 0);
            graphics.drawLine(8, 1, 9, 1);
            graphics.drawLine(10, 2, 10, 3);
            graphics.drawLine(11, 4, 11, 7);
            graphics.drawLine(10, 8, 10, 9);
            graphics.drawLine(9, 10, 8, 10);
            graphics.drawLine(7, 11, 4, 11);
            graphics.drawLine(3, 10, 2, 10);
            graphics.drawLine(1, 9, 1, 8);
            graphics.drawLine(0, 7, 0, 4);
            graphics.drawLine(1, 3, 1, 2);
            graphics.drawLine(2, 1, 3, 1);
            graphics.setColor(controlHighlight);
            graphics.drawLine(2, 9, 2, 8);
            graphics.drawLine(1, 7, 1, 4);
            graphics.drawLine(2, 2, 2, 3);
            graphics.drawLine(2, 2, 3, 2);
            graphics.drawLine(4, 1, 7, 1);
            graphics.drawLine(8, 2, 9, 2);
            graphics.setColor(controlHighlight2);
            graphics.drawLine(10, 1, 10, 1);
            graphics.drawLine(11, 2, 11, 3);
            graphics.drawLine(12, 4, 12, 7);
            graphics.drawLine(11, 8, 11, 9);
            graphics.drawLine(10, 10, 10, 10);
            graphics.drawLine(9, 11, 8, 11);
            graphics.drawLine(7, 12, 4, 12);
            graphics.drawLine(3, 11, 2, 11);
            if (zIsSelected) {
                graphics.setColor(foreground);
                graphics.fillRect(4, 4, 4, 4);
                graphics.drawLine(4, 3, 7, 3);
                graphics.drawLine(8, 4, 8, 7);
                graphics.drawLine(7, 8, 4, 8);
                graphics.drawLine(3, 7, 3, 4);
            }
            graphics.translate(-i2, -i3);
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

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$TreeComputerIcon.class */
    private static class TreeComputerIcon implements Icon, UIResource, Serializable {
        private TreeComputerIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.fillRect(5, 4, 6, 4);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(2, 2, 2, 8);
            graphics.drawLine(13, 2, 13, 8);
            graphics.drawLine(3, 1, 12, 1);
            graphics.drawLine(12, 9, 12, 9);
            graphics.drawLine(3, 9, 3, 9);
            graphics.drawLine(4, 4, 4, 7);
            graphics.drawLine(5, 3, 10, 3);
            graphics.drawLine(11, 4, 11, 7);
            graphics.drawLine(5, 8, 10, 8);
            graphics.drawLine(1, 10, 14, 10);
            graphics.drawLine(14, 10, 14, 14);
            graphics.drawLine(1, 14, 14, 14);
            graphics.drawLine(1, 10, 1, 14);
            graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
            graphics.drawLine(6, 12, 8, 12);
            graphics.drawLine(10, 12, 12, 12);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 16;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 16;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$TreeHardDriveIcon.class */
    private static class TreeHardDriveIcon implements Icon, UIResource, Serializable {
        private TreeHardDriveIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(1, 4, 1, 5);
            graphics.drawLine(2, 3, 3, 3);
            graphics.drawLine(4, 2, 11, 2);
            graphics.drawLine(12, 3, 13, 3);
            graphics.drawLine(14, 4, 14, 5);
            graphics.drawLine(12, 6, 13, 6);
            graphics.drawLine(4, 7, 11, 7);
            graphics.drawLine(2, 6, 3, 6);
            graphics.drawLine(1, 7, 1, 8);
            graphics.drawLine(2, 9, 3, 9);
            graphics.drawLine(4, 10, 11, 10);
            graphics.drawLine(12, 9, 13, 9);
            graphics.drawLine(14, 7, 14, 8);
            graphics.drawLine(1, 10, 1, 11);
            graphics.drawLine(2, 12, 3, 12);
            graphics.drawLine(4, 13, 11, 13);
            graphics.drawLine(12, 12, 13, 12);
            graphics.drawLine(14, 10, 14, 11);
            graphics.setColor(MetalLookAndFeel.getControlShadow());
            graphics.drawLine(7, 6, 7, 6);
            graphics.drawLine(9, 6, 9, 6);
            graphics.drawLine(10, 5, 10, 5);
            graphics.drawLine(11, 6, 11, 6);
            graphics.drawLine(12, 5, 13, 5);
            graphics.drawLine(13, 4, 13, 4);
            graphics.drawLine(7, 9, 7, 9);
            graphics.drawLine(9, 9, 9, 9);
            graphics.drawLine(10, 8, 10, 8);
            graphics.drawLine(11, 9, 11, 9);
            graphics.drawLine(12, 8, 13, 8);
            graphics.drawLine(13, 7, 13, 7);
            graphics.drawLine(7, 12, 7, 12);
            graphics.drawLine(9, 12, 9, 12);
            graphics.drawLine(10, 11, 10, 11);
            graphics.drawLine(11, 12, 11, 12);
            graphics.drawLine(12, 11, 13, 11);
            graphics.drawLine(13, 10, 13, 10);
            graphics.setColor(MetalLookAndFeel.getControlHighlight());
            graphics.drawLine(4, 3, 5, 3);
            graphics.drawLine(7, 3, 9, 3);
            graphics.drawLine(11, 3, 11, 3);
            graphics.drawLine(2, 4, 6, 4);
            graphics.drawLine(8, 4, 8, 4);
            graphics.drawLine(2, 5, 3, 5);
            graphics.drawLine(4, 6, 4, 6);
            graphics.drawLine(2, 7, 3, 7);
            graphics.drawLine(2, 8, 3, 8);
            graphics.drawLine(4, 9, 4, 9);
            graphics.drawLine(2, 10, 3, 10);
            graphics.drawLine(2, 11, 3, 11);
            graphics.drawLine(4, 12, 4, 12);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 16;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 16;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$TreeFloppyDriveIcon.class */
    private static class TreeFloppyDriveIcon implements Icon, UIResource, Serializable {
        private TreeFloppyDriveIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.translate(i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.fillRect(2, 2, 12, 12);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(1, 1, 13, 1);
            graphics.drawLine(14, 2, 14, 14);
            graphics.drawLine(1, 14, 14, 14);
            graphics.drawLine(1, 1, 1, 14);
            graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
            graphics.fillRect(5, 2, 6, 5);
            graphics.drawLine(4, 8, 11, 8);
            graphics.drawLine(3, 9, 3, 13);
            graphics.drawLine(12, 9, 12, 13);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
            graphics.fillRect(8, 3, 2, 3);
            graphics.fillRect(4, 9, 8, 5);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlShadow());
            graphics.drawLine(5, 10, 9, 10);
            graphics.drawLine(5, 12, 8, 12);
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 16;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 16;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$ImageCacher.class */
    static class ImageCacher {
        Vector<ImageGcPair> images = new Vector<>(1, 1);
        ImageGcPair currentImageGcPair;

        ImageCacher() {
        }

        /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$ImageCacher$ImageGcPair.class */
        class ImageGcPair {
            Image image;
            GraphicsConfiguration gc;

            ImageGcPair(Image image, GraphicsConfiguration graphicsConfiguration) {
                this.image = image;
                this.gc = graphicsConfiguration;
            }

            boolean hasSameConfiguration(GraphicsConfiguration graphicsConfiguration) {
                return (graphicsConfiguration != null && graphicsConfiguration.equals(this.gc)) || (graphicsConfiguration == null && this.gc == null);
            }
        }

        Image getImage(GraphicsConfiguration graphicsConfiguration) {
            if (this.currentImageGcPair == null || !this.currentImageGcPair.hasSameConfiguration(graphicsConfiguration)) {
                Iterator<ImageGcPair> it = this.images.iterator();
                while (it.hasNext()) {
                    ImageGcPair next = it.next();
                    if (next.hasSameConfiguration(graphicsConfiguration)) {
                        this.currentImageGcPair = next;
                        return next.image;
                    }
                }
                return null;
            }
            return this.currentImageGcPair.image;
        }

        void cacheImage(Image image, GraphicsConfiguration graphicsConfiguration) {
            ImageGcPair imageGcPair = new ImageGcPair(image, graphicsConfiguration);
            this.images.addElement(imageGcPair);
            this.currentImageGcPair = imageGcPair;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$FolderIcon16.class */
    public static class FolderIcon16 implements Icon, Serializable {
        ImageCacher imageCacher;

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            GraphicsConfiguration graphicsConfiguration = component.getGraphicsConfiguration();
            if (this.imageCacher == null) {
                this.imageCacher = new ImageCacher();
            }
            Image image = this.imageCacher.getImage(graphicsConfiguration);
            if (image == null) {
                if (graphicsConfiguration != null) {
                    image = graphicsConfiguration.createCompatibleImage(getIconWidth(), getIconHeight(), 2);
                } else {
                    image = new BufferedImage(getIconWidth(), getIconHeight(), 2);
                }
                Graphics graphics2 = image.getGraphics();
                paintMe(component, graphics2);
                graphics2.dispose();
                this.imageCacher.cacheImage(image, graphicsConfiguration);
            }
            graphics.drawImage(image, i2, i3 + getShift(), null);
        }

        private void paintMe(Component component, Graphics graphics) {
            int i2 = MetalIconFactory.folderIcon16Size.width - 1;
            int i3 = MetalIconFactory.folderIcon16Size.height - 1;
            graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            graphics.drawLine(i2 - 5, 3, i2, 3);
            graphics.drawLine(i2 - 6, 4, i2, 4);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.fillRect(2, 7, 13, 8);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlShadow());
            graphics.drawLine(i2 - 6, 5, i2 - 1, 5);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(0, 6, 0, i3);
            graphics.drawLine(1, 5, i2 - 7, 5);
            graphics.drawLine(i2 - 6, 6, i2 - 1, 6);
            graphics.drawLine(i2, 5, i2, i3);
            graphics.drawLine(0, i3, i2, i3);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
            graphics.drawLine(1, 6, 1, i3 - 1);
            graphics.drawLine(1, 6, i2 - 7, 6);
            graphics.drawLine(i2 - 6, 7, i2 - 1, 7);
        }

        public int getShift() {
            return 0;
        }

        public int getAdditionalHeight() {
            return 0;
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return MetalIconFactory.folderIcon16Size.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MetalIconFactory.folderIcon16Size.height + getAdditionalHeight();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$TreeFolderIcon.class */
    public static class TreeFolderIcon extends FolderIcon16 {
        @Override // javax.swing.plaf.metal.MetalIconFactory.FolderIcon16
        public int getShift() {
            return -1;
        }

        @Override // javax.swing.plaf.metal.MetalIconFactory.FolderIcon16
        public int getAdditionalHeight() {
            return 2;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$FileIcon16.class */
    public static class FileIcon16 implements Icon, Serializable {
        ImageCacher imageCacher;

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            GraphicsConfiguration graphicsConfiguration = component.getGraphicsConfiguration();
            if (this.imageCacher == null) {
                this.imageCacher = new ImageCacher();
            }
            Image image = this.imageCacher.getImage(graphicsConfiguration);
            if (image == null) {
                if (graphicsConfiguration != null) {
                    image = graphicsConfiguration.createCompatibleImage(getIconWidth(), getIconHeight(), 2);
                } else {
                    image = new BufferedImage(getIconWidth(), getIconHeight(), 2);
                }
                Graphics graphics2 = image.getGraphics();
                paintMe(component, graphics2);
                graphics2.dispose();
                this.imageCacher.cacheImage(image, graphicsConfiguration);
            }
            graphics.drawImage(image, i2, i3 + getShift(), null);
        }

        private void paintMe(Component component, Graphics graphics) {
            int i2 = MetalIconFactory.fileIcon16Size.width - 1;
            int i3 = MetalIconFactory.fileIcon16Size.height - 1;
            graphics.setColor(MetalLookAndFeel.getWindowBackground());
            graphics.fillRect(4, 2, 9, 12);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            graphics.drawLine(2, 0, 2, i3);
            graphics.drawLine(2, 0, i2 - 4, 0);
            graphics.drawLine(2, i3, i2 - 1, i3);
            graphics.drawLine(i2 - 1, 6, i2 - 1, i3);
            graphics.drawLine(i2 - 6, 2, i2 - 2, 6);
            graphics.drawLine(i2 - 5, 1, i2 - 4, 1);
            graphics.drawLine(i2 - 3, 2, i2 - 3, 3);
            graphics.drawLine(i2 - 2, 4, i2 - 2, 5);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.drawLine(3, 1, 3, i3 - 1);
            graphics.drawLine(3, 1, i2 - 6, 1);
            graphics.drawLine(i2 - 2, 7, i2 - 2, i3 - 1);
            graphics.drawLine(i2 - 5, 2, i2 - 3, 4);
            graphics.drawLine(3, i3 - 1, i2 - 2, i3 - 1);
        }

        public int getShift() {
            return 0;
        }

        public int getAdditionalHeight() {
            return 0;
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return MetalIconFactory.fileIcon16Size.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MetalIconFactory.fileIcon16Size.height + getAdditionalHeight();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$TreeLeafIcon.class */
    public static class TreeLeafIcon extends FileIcon16 {
        @Override // javax.swing.plaf.metal.MetalIconFactory.FileIcon16
        public int getShift() {
            return 2;
        }

        @Override // javax.swing.plaf.metal.MetalIconFactory.FileIcon16
        public int getAdditionalHeight() {
            return 4;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$TreeControlIcon.class */
    public static class TreeControlIcon implements Icon, Serializable {
        protected boolean isLight;
        ImageCacher imageCacher;
        transient boolean cachedOrientation = true;

        public TreeControlIcon(boolean z2) {
            this.isLight = z2;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            GraphicsConfiguration graphicsConfiguration = component.getGraphicsConfiguration();
            if (this.imageCacher == null) {
                this.imageCacher = new ImageCacher();
            }
            Image image = this.imageCacher.getImage(graphicsConfiguration);
            if (image == null || this.cachedOrientation != MetalUtils.isLeftToRight(component)) {
                this.cachedOrientation = MetalUtils.isLeftToRight(component);
                if (graphicsConfiguration != null) {
                    image = graphicsConfiguration.createCompatibleImage(getIconWidth(), getIconHeight(), 2);
                } else {
                    image = new BufferedImage(getIconWidth(), getIconHeight(), 2);
                }
                Graphics graphics2 = image.getGraphics();
                paintMe(component, graphics2, i2, i3);
                graphics2.dispose();
                this.imageCacher.cacheImage(image, graphicsConfiguration);
            }
            if (MetalUtils.isLeftToRight(component)) {
                if (this.isLight) {
                    graphics.drawImage(image, i2 + 5, i3 + 3, i2 + 18, i3 + 13, 4, 3, 17, 13, null);
                    return;
                } else {
                    graphics.drawImage(image, i2 + 5, i3 + 3, i2 + 18, i3 + 17, 4, 3, 17, 17, null);
                    return;
                }
            }
            if (this.isLight) {
                graphics.drawImage(image, i2 + 3, i3 + 3, i2 + 16, i3 + 13, 4, 3, 17, 13, null);
            } else {
                graphics.drawImage(image, i2 + 3, i3 + 3, i2 + 16, i3 + 17, 4, 3, 17, 17, null);
            }
        }

        public void paintMe(Component component, Graphics graphics, int i2, int i3) {
            graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            int i4 = MetalUtils.isLeftToRight(component) ? 0 : 4;
            graphics.drawLine(i4 + 4, 6, i4 + 4, 9);
            graphics.drawLine(i4 + 5, 5, i4 + 5, 5);
            graphics.drawLine(i4 + 6, 4, i4 + 9, 4);
            graphics.drawLine(i4 + 10, 5, i4 + 10, 5);
            graphics.drawLine(i4 + 11, 6, i4 + 11, 9);
            graphics.drawLine(i4 + 10, 10, i4 + 10, 10);
            graphics.drawLine(i4 + 6, 11, i4 + 9, 11);
            graphics.drawLine(i4 + 5, 10, i4 + 5, 10);
            graphics.drawLine(i4 + 7, 7, i4 + 8, 7);
            graphics.drawLine(i4 + 7, 8, i4 + 8, 8);
            if (this.isLight) {
                if (MetalUtils.isLeftToRight(component)) {
                    graphics.drawLine(12, 7, 15, 7);
                    graphics.drawLine(12, 8, 15, 8);
                } else {
                    graphics.drawLine(4, 7, 7, 7);
                    graphics.drawLine(4, 8, 7, 8);
                }
            } else {
                graphics.drawLine(i4 + 7, 12, i4 + 7, 15);
                graphics.drawLine(i4 + 8, 12, i4 + 8, 15);
            }
            graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            graphics.drawLine(i4 + 5, 6, i4 + 5, 9);
            graphics.drawLine(i4 + 6, 5, i4 + 9, 5);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlShadow());
            graphics.drawLine(i4 + 6, 6, i4 + 6, 6);
            graphics.drawLine(i4 + 9, 6, i4 + 9, 6);
            graphics.drawLine(i4 + 6, 9, i4 + 6, 9);
            graphics.drawLine(i4 + 10, 6, i4 + 10, 9);
            graphics.drawLine(i4 + 6, 10, i4 + 9, 10);
            graphics.setColor(MetalLookAndFeel.getPrimaryControl());
            graphics.drawLine(i4 + 6, 7, i4 + 6, 8);
            graphics.drawLine(i4 + 7, 6, i4 + 8, 6);
            graphics.drawLine(i4 + 9, 7, i4 + 9, 7);
            graphics.drawLine(i4 + 7, 9, i4 + 7, 9);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
            graphics.drawLine(i4 + 8, 9, i4 + 9, 9);
            graphics.drawLine(i4 + 9, 8, i4 + 9, 8);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return MetalIconFactory.treeControlSize.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MetalIconFactory.treeControlSize.height;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$MenuArrowIcon.class */
    private static class MenuArrowIcon implements Icon, UIResource, Serializable {
        private MenuArrowIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            JMenuItem jMenuItem = (JMenuItem) component;
            ButtonModel model = jMenuItem.getModel();
            graphics.translate(i2, i3);
            if (!model.isEnabled()) {
                graphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
            } else if (model.isArmed() || ((component instanceof JMenu) && model.isSelected())) {
                graphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
            } else {
                graphics.setColor(jMenuItem.getForeground());
            }
            if (MetalUtils.isLeftToRight(jMenuItem)) {
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
            return MetalIconFactory.menuArrowIconSize.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MetalIconFactory.menuArrowIconSize.height;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$MenuItemArrowIcon.class */
    private static class MenuItemArrowIcon implements Icon, UIResource, Serializable {
        private MenuItemArrowIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return MetalIconFactory.menuArrowIconSize.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MetalIconFactory.menuArrowIconSize.height;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$CheckBoxMenuItemIcon.class */
    private static class CheckBoxMenuItemIcon implements Icon, UIResource, Serializable {
        private CheckBoxMenuItemIcon() {
        }

        public void paintOceanIcon(Component component, Graphics graphics, int i2, int i3) {
            ButtonModel model = ((JMenuItem) component).getModel();
            boolean zIsSelected = model.isSelected();
            boolean zIsEnabled = model.isEnabled();
            boolean zIsPressed = model.isPressed();
            boolean zIsArmed = model.isArmed();
            graphics.translate(i2, i3);
            if (zIsEnabled) {
                MetalUtils.drawGradient(component, graphics, "CheckBoxMenuItem.gradient", 1, 1, 7, 7, true);
                if (zIsPressed || zIsArmed) {
                    graphics.setColor(MetalLookAndFeel.getControlInfo());
                    graphics.drawLine(0, 0, 8, 0);
                    graphics.drawLine(0, 0, 0, 8);
                    graphics.drawLine(8, 2, 8, 8);
                    graphics.drawLine(2, 8, 8, 8);
                    graphics.setColor(MetalLookAndFeel.getPrimaryControl());
                    graphics.drawLine(9, 1, 9, 9);
                    graphics.drawLine(1, 9, 9, 9);
                } else {
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawLine(0, 0, 8, 0);
                    graphics.drawLine(0, 0, 0, 8);
                    graphics.drawLine(8, 2, 8, 8);
                    graphics.drawLine(2, 8, 8, 8);
                    graphics.setColor(MetalLookAndFeel.getControlHighlight());
                    graphics.drawLine(9, 1, 9, 9);
                    graphics.drawLine(1, 9, 9, 9);
                }
            } else {
                graphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
                graphics.drawRect(0, 0, 8, 8);
            }
            if (zIsSelected) {
                if (zIsEnabled) {
                    if (zIsArmed || ((component instanceof JMenu) && zIsSelected)) {
                        graphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
                    } else {
                        graphics.setColor(MetalLookAndFeel.getControlInfo());
                    }
                } else {
                    graphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
                }
                graphics.drawLine(2, 2, 2, 6);
                graphics.drawLine(3, 2, 3, 6);
                graphics.drawLine(4, 4, 8, 0);
                graphics.drawLine(4, 5, 9, 0);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (MetalLookAndFeel.usingOcean()) {
                paintOceanIcon(component, graphics, i2, i3);
                return;
            }
            JMenuItem jMenuItem = (JMenuItem) component;
            ButtonModel model = jMenuItem.getModel();
            boolean zIsSelected = model.isSelected();
            boolean zIsEnabled = model.isEnabled();
            boolean zIsPressed = model.isPressed();
            boolean zIsArmed = model.isArmed();
            graphics.translate(i2, i3);
            if (zIsEnabled) {
                if (zIsPressed || zIsArmed) {
                    graphics.setColor(MetalLookAndFeel.getControlInfo());
                    graphics.drawLine(0, 0, 8, 0);
                    graphics.drawLine(0, 0, 0, 8);
                    graphics.drawLine(8, 2, 8, 8);
                    graphics.drawLine(2, 8, 8, 8);
                    graphics.setColor(MetalLookAndFeel.getPrimaryControl());
                    graphics.drawLine(1, 1, 7, 1);
                    graphics.drawLine(1, 1, 1, 7);
                    graphics.drawLine(9, 1, 9, 9);
                    graphics.drawLine(1, 9, 9, 9);
                } else {
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawLine(0, 0, 8, 0);
                    graphics.drawLine(0, 0, 0, 8);
                    graphics.drawLine(8, 2, 8, 8);
                    graphics.drawLine(2, 8, 8, 8);
                    graphics.setColor(MetalLookAndFeel.getControlHighlight());
                    graphics.drawLine(1, 1, 7, 1);
                    graphics.drawLine(1, 1, 1, 7);
                    graphics.drawLine(9, 1, 9, 9);
                    graphics.drawLine(1, 9, 9, 9);
                }
            } else {
                graphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
                graphics.drawRect(0, 0, 8, 8);
            }
            if (zIsSelected) {
                if (zIsEnabled) {
                    if (model.isArmed() || ((component instanceof JMenu) && model.isSelected())) {
                        graphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
                    } else {
                        graphics.setColor(jMenuItem.getForeground());
                    }
                } else {
                    graphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
                }
                graphics.drawLine(2, 2, 2, 6);
                graphics.drawLine(3, 2, 3, 6);
                graphics.drawLine(4, 4, 8, 0);
                graphics.drawLine(4, 5, 9, 0);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return MetalIconFactory.menuCheckIconSize.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MetalIconFactory.menuCheckIconSize.height;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$RadioButtonMenuItemIcon.class */
    private static class RadioButtonMenuItemIcon implements Icon, UIResource, Serializable {
        private RadioButtonMenuItemIcon() {
        }

        public void paintOceanIcon(Component component, Graphics graphics, int i2, int i3) {
            ButtonModel model = ((JMenuItem) component).getModel();
            boolean zIsSelected = model.isSelected();
            boolean zIsEnabled = model.isEnabled();
            boolean zIsPressed = model.isPressed();
            boolean zIsArmed = model.isArmed();
            graphics.translate(i2, i3);
            if (zIsEnabled) {
                MetalUtils.drawGradient(component, graphics, "RadioButtonMenuItem.gradient", 1, 1, 7, 7, true);
                if (zIsPressed || zIsArmed) {
                    graphics.setColor(MetalLookAndFeel.getPrimaryControl());
                } else {
                    graphics.setColor(MetalLookAndFeel.getControlHighlight());
                }
                graphics.drawLine(2, 9, 7, 9);
                graphics.drawLine(9, 2, 9, 7);
                graphics.drawLine(8, 8, 8, 8);
                if (zIsPressed || zIsArmed) {
                    graphics.setColor(MetalLookAndFeel.getControlInfo());
                } else {
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                }
            } else {
                graphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
            }
            graphics.drawLine(2, 0, 6, 0);
            graphics.drawLine(2, 8, 6, 8);
            graphics.drawLine(0, 2, 0, 6);
            graphics.drawLine(8, 2, 8, 6);
            graphics.drawLine(1, 1, 1, 1);
            graphics.drawLine(7, 1, 7, 1);
            graphics.drawLine(1, 7, 1, 7);
            graphics.drawLine(7, 7, 7, 7);
            if (zIsSelected) {
                if (zIsEnabled) {
                    if (zIsArmed || ((component instanceof JMenu) && model.isSelected())) {
                        graphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
                    } else {
                        graphics.setColor(MetalLookAndFeel.getControlInfo());
                    }
                } else {
                    graphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
                }
                graphics.drawLine(3, 2, 5, 2);
                graphics.drawLine(2, 3, 6, 3);
                graphics.drawLine(2, 4, 6, 4);
                graphics.drawLine(2, 5, 6, 5);
                graphics.drawLine(3, 6, 5, 6);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (MetalLookAndFeel.usingOcean()) {
                paintOceanIcon(component, graphics, i2, i3);
                return;
            }
            JMenuItem jMenuItem = (JMenuItem) component;
            ButtonModel model = jMenuItem.getModel();
            boolean zIsSelected = model.isSelected();
            boolean zIsEnabled = model.isEnabled();
            boolean zIsPressed = model.isPressed();
            boolean zIsArmed = model.isArmed();
            graphics.translate(i2, i3);
            if (zIsEnabled) {
                if (zIsPressed || zIsArmed) {
                    graphics.setColor(MetalLookAndFeel.getPrimaryControl());
                    graphics.drawLine(3, 1, 8, 1);
                    graphics.drawLine(2, 9, 7, 9);
                    graphics.drawLine(1, 3, 1, 8);
                    graphics.drawLine(9, 2, 9, 7);
                    graphics.drawLine(2, 2, 2, 2);
                    graphics.drawLine(8, 8, 8, 8);
                    graphics.setColor(MetalLookAndFeel.getControlInfo());
                    graphics.drawLine(2, 0, 6, 0);
                    graphics.drawLine(2, 8, 6, 8);
                    graphics.drawLine(0, 2, 0, 6);
                    graphics.drawLine(8, 2, 8, 6);
                    graphics.drawLine(1, 1, 1, 1);
                    graphics.drawLine(7, 1, 7, 1);
                    graphics.drawLine(1, 7, 1, 7);
                    graphics.drawLine(7, 7, 7, 7);
                } else {
                    graphics.setColor(MetalLookAndFeel.getControlHighlight());
                    graphics.drawLine(3, 1, 8, 1);
                    graphics.drawLine(2, 9, 7, 9);
                    graphics.drawLine(1, 3, 1, 8);
                    graphics.drawLine(9, 2, 9, 7);
                    graphics.drawLine(2, 2, 2, 2);
                    graphics.drawLine(8, 8, 8, 8);
                    graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                    graphics.drawLine(2, 0, 6, 0);
                    graphics.drawLine(2, 8, 6, 8);
                    graphics.drawLine(0, 2, 0, 6);
                    graphics.drawLine(8, 2, 8, 6);
                    graphics.drawLine(1, 1, 1, 1);
                    graphics.drawLine(7, 1, 7, 1);
                    graphics.drawLine(1, 7, 1, 7);
                    graphics.drawLine(7, 7, 7, 7);
                }
            } else {
                graphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
                graphics.drawLine(2, 0, 6, 0);
                graphics.drawLine(2, 8, 6, 8);
                graphics.drawLine(0, 2, 0, 6);
                graphics.drawLine(8, 2, 8, 6);
                graphics.drawLine(1, 1, 1, 1);
                graphics.drawLine(7, 1, 7, 1);
                graphics.drawLine(1, 7, 1, 7);
                graphics.drawLine(7, 7, 7, 7);
            }
            if (zIsSelected) {
                if (zIsEnabled) {
                    if (model.isArmed() || ((component instanceof JMenu) && model.isSelected())) {
                        graphics.setColor(MetalLookAndFeel.getMenuSelectedForeground());
                    } else {
                        graphics.setColor(jMenuItem.getForeground());
                    }
                } else {
                    graphics.setColor(MetalLookAndFeel.getMenuDisabledForeground());
                }
                graphics.drawLine(3, 2, 5, 2);
                graphics.drawLine(2, 3, 6, 3);
                graphics.drawLine(2, 4, 6, 4);
                graphics.drawLine(2, 5, 6, 5);
                graphics.drawLine(3, 6, 5, 6);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return MetalIconFactory.menuCheckIconSize.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return MetalIconFactory.menuCheckIconSize.height;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$VerticalSliderThumbIcon.class */
    private static class VerticalSliderThumbIcon implements Icon, Serializable, UIResource {
        protected static MetalBumps controlBumps;
        protected static MetalBumps primaryBumps;

        public VerticalSliderThumbIcon() {
            controlBumps = new MetalBumps(6, 10, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlInfo(), MetalLookAndFeel.getControl());
            primaryBumps = new MetalBumps(6, 10, MetalLookAndFeel.getPrimaryControl(), MetalLookAndFeel.getPrimaryControlDarkShadow(), MetalLookAndFeel.getPrimaryControlShadow());
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            boolean zIsLeftToRight = MetalUtils.isLeftToRight(component);
            graphics.translate(i2, i3);
            if (component.hasFocus()) {
                graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            } else {
                graphics.setColor(component.isEnabled() ? MetalLookAndFeel.getPrimaryControlInfo() : MetalLookAndFeel.getControlDarkShadow());
            }
            if (zIsLeftToRight) {
                graphics.drawLine(1, 0, 8, 0);
                graphics.drawLine(0, 1, 0, 13);
                graphics.drawLine(1, 14, 8, 14);
                graphics.drawLine(9, 1, 15, 7);
                graphics.drawLine(9, 13, 15, 7);
            } else {
                graphics.drawLine(7, 0, 14, 0);
                graphics.drawLine(15, 1, 15, 13);
                graphics.drawLine(7, 14, 14, 14);
                graphics.drawLine(0, 7, 6, 1);
                graphics.drawLine(0, 7, 6, 13);
            }
            if (component.hasFocus()) {
                graphics.setColor(component.getForeground());
            } else {
                graphics.setColor(MetalLookAndFeel.getControl());
            }
            if (zIsLeftToRight) {
                graphics.fillRect(1, 1, 8, 13);
                graphics.drawLine(9, 2, 9, 12);
                graphics.drawLine(10, 3, 10, 11);
                graphics.drawLine(11, 4, 11, 10);
                graphics.drawLine(12, 5, 12, 9);
                graphics.drawLine(13, 6, 13, 8);
                graphics.drawLine(14, 7, 14, 7);
            } else {
                graphics.fillRect(7, 1, 8, 13);
                graphics.drawLine(6, 3, 6, 12);
                graphics.drawLine(5, 4, 5, 11);
                graphics.drawLine(4, 5, 4, 10);
                graphics.drawLine(3, 6, 3, 9);
                graphics.drawLine(2, 7, 2, 8);
            }
            int i4 = zIsLeftToRight ? 2 : 8;
            if (component.isEnabled()) {
                if (component.hasFocus()) {
                    primaryBumps.paintIcon(component, graphics, i4, 2);
                } else {
                    controlBumps.paintIcon(component, graphics, i4, 2);
                }
            }
            if (component.isEnabled()) {
                graphics.setColor(component.hasFocus() ? MetalLookAndFeel.getPrimaryControl() : MetalLookAndFeel.getControlHighlight());
                if (zIsLeftToRight) {
                    graphics.drawLine(1, 1, 8, 1);
                    graphics.drawLine(1, 1, 1, 13);
                } else {
                    graphics.drawLine(8, 1, 14, 1);
                    graphics.drawLine(1, 7, 7, 1);
                }
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 16;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 15;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$HorizontalSliderThumbIcon.class */
    private static class HorizontalSliderThumbIcon implements Icon, Serializable, UIResource {
        protected static MetalBumps controlBumps;
        protected static MetalBumps primaryBumps;

        public HorizontalSliderThumbIcon() {
            controlBumps = new MetalBumps(10, 6, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlInfo(), MetalLookAndFeel.getControl());
            primaryBumps = new MetalBumps(10, 6, MetalLookAndFeel.getPrimaryControl(), MetalLookAndFeel.getPrimaryControlDarkShadow(), MetalLookAndFeel.getPrimaryControlShadow());
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            graphics.translate(i2, i3);
            if (component.hasFocus()) {
                graphics.setColor(MetalLookAndFeel.getPrimaryControlInfo());
            } else {
                graphics.setColor(component.isEnabled() ? MetalLookAndFeel.getPrimaryControlInfo() : MetalLookAndFeel.getControlDarkShadow());
            }
            graphics.drawLine(1, 0, 13, 0);
            graphics.drawLine(0, 1, 0, 8);
            graphics.drawLine(14, 1, 14, 8);
            graphics.drawLine(1, 9, 7, 15);
            graphics.drawLine(7, 15, 14, 8);
            if (component.hasFocus()) {
                graphics.setColor(component.getForeground());
            } else {
                graphics.setColor(MetalLookAndFeel.getControl());
            }
            graphics.fillRect(1, 1, 13, 8);
            graphics.drawLine(2, 9, 12, 9);
            graphics.drawLine(3, 10, 11, 10);
            graphics.drawLine(4, 11, 10, 11);
            graphics.drawLine(5, 12, 9, 12);
            graphics.drawLine(6, 13, 8, 13);
            graphics.drawLine(7, 14, 7, 14);
            if (component.isEnabled()) {
                if (component.hasFocus()) {
                    primaryBumps.paintIcon(component, graphics, 2, 2);
                } else {
                    controlBumps.paintIcon(component, graphics, 2, 2);
                }
            }
            if (component.isEnabled()) {
                graphics.setColor(component.hasFocus() ? MetalLookAndFeel.getPrimaryControl() : MetalLookAndFeel.getControlHighlight());
                graphics.drawLine(1, 1, 13, 1);
                graphics.drawLine(1, 1, 1, 8);
            }
            graphics.translate(-i2, -i3);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 15;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 16;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$OceanVerticalSliderThumbIcon.class */
    private static class OceanVerticalSliderThumbIcon extends CachedPainter implements Icon, Serializable, UIResource {
        private static Polygon LTR_THUMB_SHAPE = new Polygon(new int[]{0, 8, 15, 8, 0}, new int[]{0, 0, 7, 14, 14}, 5);
        private static Polygon RTL_THUMB_SHAPE = new Polygon(new int[]{15, 15, 7, 0, 7}, new int[]{0, 14, 14, 7, 0}, 5);

        OceanVerticalSliderThumbIcon() {
            super(3);
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (!(graphics instanceof Graphics2D)) {
                return;
            }
            paint(component, graphics, i2, i3, getIconWidth(), getIconHeight(), Boolean.valueOf(MetalUtils.isLeftToRight(component)), Boolean.valueOf(component.hasFocus()), Boolean.valueOf(component.isEnabled()), MetalLookAndFeel.getCurrentTheme());
        }

        @Override // sun.swing.CachedPainter
        protected void paintToImage(Component component, Image image, Graphics graphics, int i2, int i3, Object[] objArr) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            boolean zBooleanValue = ((Boolean) objArr[0]).booleanValue();
            boolean zBooleanValue2 = ((Boolean) objArr[1]).booleanValue();
            boolean zBooleanValue3 = ((Boolean) objArr[2]).booleanValue();
            Rectangle clipBounds = graphics2D.getClipBounds();
            if (zBooleanValue) {
                graphics2D.clip(LTR_THUMB_SHAPE);
            } else {
                graphics2D.clip(RTL_THUMB_SHAPE);
            }
            if (!zBooleanValue3) {
                graphics2D.setColor(MetalLookAndFeel.getControl());
                graphics2D.fillRect(1, 1, 14, 14);
            } else if (zBooleanValue2) {
                MetalUtils.drawGradient(component, graphics2D, "Slider.focusGradient", 1, 1, 14, 14, false);
            } else {
                MetalUtils.drawGradient(component, graphics2D, "Slider.gradient", 1, 1, 14, 14, false);
            }
            graphics2D.setClip(clipBounds);
            if (zBooleanValue2) {
                graphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            } else {
                graphics2D.setColor(zBooleanValue3 ? MetalLookAndFeel.getPrimaryControlInfo() : MetalLookAndFeel.getControlDarkShadow());
            }
            if (zBooleanValue) {
                graphics2D.drawLine(1, 0, 8, 0);
                graphics2D.drawLine(0, 1, 0, 13);
                graphics2D.drawLine(1, 14, 8, 14);
                graphics2D.drawLine(9, 1, 15, 7);
                graphics2D.drawLine(9, 13, 15, 7);
            } else {
                graphics2D.drawLine(7, 0, 14, 0);
                graphics2D.drawLine(15, 1, 15, 13);
                graphics2D.drawLine(7, 14, 14, 14);
                graphics2D.drawLine(0, 7, 6, 1);
                graphics2D.drawLine(0, 7, 6, 13);
            }
            if (zBooleanValue2 && zBooleanValue3) {
                graphics2D.setColor(MetalLookAndFeel.getPrimaryControl());
                if (zBooleanValue) {
                    graphics2D.drawLine(1, 1, 8, 1);
                    graphics2D.drawLine(1, 1, 1, 13);
                    graphics2D.drawLine(1, 13, 8, 13);
                    graphics2D.drawLine(9, 2, 14, 7);
                    graphics2D.drawLine(9, 12, 14, 7);
                    return;
                }
                graphics2D.drawLine(7, 1, 14, 1);
                graphics2D.drawLine(14, 1, 14, 13);
                graphics2D.drawLine(7, 13, 14, 13);
                graphics2D.drawLine(1, 7, 7, 1);
                graphics2D.drawLine(1, 7, 7, 13);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 16;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 15;
        }

        @Override // sun.swing.CachedPainter
        protected Image createImage(Component component, int i2, int i3, GraphicsConfiguration graphicsConfiguration, Object[] objArr) {
            if (graphicsConfiguration == null) {
                return new BufferedImage(i2, i3, 2);
            }
            return graphicsConfiguration.createCompatibleImage(i2, i3, 2);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalIconFactory$OceanHorizontalSliderThumbIcon.class */
    private static class OceanHorizontalSliderThumbIcon extends CachedPainter implements Icon, Serializable, UIResource {
        private static Polygon THUMB_SHAPE = new Polygon(new int[]{0, 14, 14, 7, 0}, new int[]{0, 0, 8, 15, 8}, 5);

        OceanHorizontalSliderThumbIcon() {
            super(3);
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (!(graphics instanceof Graphics2D)) {
                return;
            }
            paint(component, graphics, i2, i3, getIconWidth(), getIconHeight(), Boolean.valueOf(component.hasFocus()), Boolean.valueOf(component.isEnabled()), MetalLookAndFeel.getCurrentTheme());
        }

        @Override // sun.swing.CachedPainter
        protected Image createImage(Component component, int i2, int i3, GraphicsConfiguration graphicsConfiguration, Object[] objArr) {
            if (graphicsConfiguration == null) {
                return new BufferedImage(i2, i3, 2);
            }
            return graphicsConfiguration.createCompatibleImage(i2, i3, 2);
        }

        @Override // sun.swing.CachedPainter
        protected void paintToImage(Component component, Image image, Graphics graphics, int i2, int i3, Object[] objArr) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            boolean zBooleanValue = ((Boolean) objArr[0]).booleanValue();
            boolean zBooleanValue2 = ((Boolean) objArr[1]).booleanValue();
            Rectangle clipBounds = graphics2D.getClipBounds();
            graphics2D.clip(THUMB_SHAPE);
            if (!zBooleanValue2) {
                graphics2D.setColor(MetalLookAndFeel.getControl());
                graphics2D.fillRect(1, 1, 13, 14);
            } else if (zBooleanValue) {
                MetalUtils.drawGradient(component, graphics2D, "Slider.focusGradient", 1, 1, 13, 14, true);
            } else {
                MetalUtils.drawGradient(component, graphics2D, "Slider.gradient", 1, 1, 13, 14, true);
            }
            graphics2D.setClip(clipBounds);
            if (zBooleanValue) {
                graphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            } else {
                graphics2D.setColor(zBooleanValue2 ? MetalLookAndFeel.getPrimaryControlInfo() : MetalLookAndFeel.getControlDarkShadow());
            }
            graphics2D.drawLine(1, 0, 13, 0);
            graphics2D.drawLine(0, 1, 0, 8);
            graphics2D.drawLine(14, 1, 14, 8);
            graphics2D.drawLine(1, 9, 7, 15);
            graphics2D.drawLine(7, 15, 14, 8);
            if (zBooleanValue && zBooleanValue2) {
                graphics2D.setColor(MetalLookAndFeel.getPrimaryControl());
                graphics2D.fillRect(1, 1, 13, 1);
                graphics2D.fillRect(1, 2, 1, 7);
                graphics2D.fillRect(13, 2, 1, 7);
                graphics2D.drawLine(2, 9, 7, 14);
                graphics2D.drawLine(8, 13, 12, 9);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 15;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 16;
        }
    }
}
