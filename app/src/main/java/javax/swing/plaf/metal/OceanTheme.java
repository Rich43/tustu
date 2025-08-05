package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Arrays;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.IconUIResource;
import sun.swing.PrintColorUIResource;
import sun.swing.SwingLazyValue;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/OceanTheme.class */
public class OceanTheme extends DefaultMetalTheme {
    private static final ColorUIResource PRIMARY1 = new ColorUIResource(6521535);
    private static final ColorUIResource PRIMARY2 = new ColorUIResource(10729676);
    private static final ColorUIResource PRIMARY3 = new ColorUIResource(12111845);
    private static final ColorUIResource SECONDARY1 = new ColorUIResource(8030873);
    private static final ColorUIResource SECONDARY2 = new ColorUIResource(12111845);
    private static final ColorUIResource SECONDARY3 = new ColorUIResource(15658734);
    private static final ColorUIResource CONTROL_TEXT_COLOR = new PrintColorUIResource(3355443, Color.BLACK);
    private static final ColorUIResource INACTIVE_CONTROL_TEXT_COLOR = new ColorUIResource(10066329);
    private static final ColorUIResource MENU_DISABLED_FOREGROUND = new ColorUIResource(10066329);
    private static final ColorUIResource OCEAN_BLACK = new PrintColorUIResource(3355443, Color.BLACK);
    private static final ColorUIResource OCEAN_DROP = new ColorUIResource(13822463);

    /* loaded from: rt.jar:javax/swing/plaf/metal/OceanTheme$COIcon.class */
    private static class COIcon extends IconUIResource {
        private Icon rtl;

        public COIcon(Icon icon, Icon icon2) {
            super(icon);
            this.rtl = icon2;
        }

        @Override // javax.swing.plaf.IconUIResource, javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (MetalUtils.isLeftToRight(component)) {
                super.paintIcon(component, graphics, i2, i3);
            } else {
                this.rtl.paintIcon(component, graphics, i2, i3);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/OceanTheme$IFIcon.class */
    private static class IFIcon extends IconUIResource {
        private Icon pressed;

        public IFIcon(Icon icon, Icon icon2) {
            super(icon);
            this.pressed = icon2;
        }

        @Override // javax.swing.plaf.IconUIResource, javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            ButtonModel model = ((AbstractButton) component).getModel();
            if (model.isPressed() && model.isArmed()) {
                this.pressed.paintIcon(component, graphics, i2, i3);
            } else {
                super.paintIcon(component, graphics, i2, i3);
            }
        }
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public void addCustomEntriesToTable(UIDefaults uIDefaults) {
        Object swingLazyValue = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[]{getPrimary1()});
        Object objAsList = Arrays.asList(new Float(0.3f), new Float(0.0f), new ColorUIResource(14543091), getWhite(), getSecondary2());
        Object colorUIResource = new ColorUIResource(13421772);
        ColorUIResource colorUIResource2 = new ColorUIResource(14342874);
        Object colorUIResource3 = new ColorUIResource(13164018);
        Object iconResource = getIconResource("icons/ocean/directory.gif");
        Object iconResource2 = getIconResource("icons/ocean/file.gif");
        Object objAsList2 = Arrays.asList(new Float(0.3f), new Float(0.2f), colorUIResource3, getWhite(), new ColorUIResource(SECONDARY2));
        uIDefaults.putDefaults(new Object[]{"Button.gradient", objAsList, "Button.rollover", Boolean.TRUE, "Button.toolBarBorderBackground", INACTIVE_CONTROL_TEXT_COLOR, "Button.disabledToolBarBorderBackground", colorUIResource, "Button.rolloverIconType", "ocean", "CheckBox.rollover", Boolean.TRUE, "CheckBox.gradient", objAsList, "CheckBoxMenuItem.gradient", objAsList, "FileChooser.homeFolderIcon", getIconResource("icons/ocean/homeFolder.gif"), "FileChooser.newFolderIcon", getIconResource("icons/ocean/newFolder.gif"), "FileChooser.upFolderIcon", getIconResource("icons/ocean/upFolder.gif"), "FileView.computerIcon", getIconResource("icons/ocean/computer.gif"), "FileView.directoryIcon", iconResource, "FileView.hardDriveIcon", getIconResource("icons/ocean/hardDrive.gif"), "FileView.fileIcon", iconResource2, "FileView.floppyDriveIcon", getIconResource("icons/ocean/floppy.gif"), "Label.disabledForeground", getInactiveControlTextColor(), "Menu.opaque", Boolean.FALSE, "MenuBar.gradient", Arrays.asList(new Float(1.0f), new Float(0.0f), getWhite(), colorUIResource2, new ColorUIResource(colorUIResource2)), "MenuBar.borderColor", colorUIResource, "InternalFrame.activeTitleGradient", objAsList, "InternalFrame.closeIcon", new UIDefaults.LazyValue() { // from class: javax.swing.plaf.metal.OceanTheme.1
            @Override // javax.swing.UIDefaults.LazyValue
            public Object createValue(UIDefaults uIDefaults2) {
                return new IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/close.gif", uIDefaults2), OceanTheme.this.getHastenedIcon("icons/ocean/close-pressed.gif", uIDefaults2));
            }
        }, "InternalFrame.iconifyIcon", new UIDefaults.LazyValue() { // from class: javax.swing.plaf.metal.OceanTheme.2
            @Override // javax.swing.UIDefaults.LazyValue
            public Object createValue(UIDefaults uIDefaults2) {
                return new IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/iconify.gif", uIDefaults2), OceanTheme.this.getHastenedIcon("icons/ocean/iconify-pressed.gif", uIDefaults2));
            }
        }, "InternalFrame.minimizeIcon", new UIDefaults.LazyValue() { // from class: javax.swing.plaf.metal.OceanTheme.3
            @Override // javax.swing.UIDefaults.LazyValue
            public Object createValue(UIDefaults uIDefaults2) {
                return new IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/minimize.gif", uIDefaults2), OceanTheme.this.getHastenedIcon("icons/ocean/minimize-pressed.gif", uIDefaults2));
            }
        }, "InternalFrame.icon", getIconResource("icons/ocean/menu.gif"), "InternalFrame.maximizeIcon", new UIDefaults.LazyValue() { // from class: javax.swing.plaf.metal.OceanTheme.4
            @Override // javax.swing.UIDefaults.LazyValue
            public Object createValue(UIDefaults uIDefaults2) {
                return new IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/maximize.gif", uIDefaults2), OceanTheme.this.getHastenedIcon("icons/ocean/maximize-pressed.gif", uIDefaults2));
            }
        }, "InternalFrame.paletteCloseIcon", new UIDefaults.LazyValue() { // from class: javax.swing.plaf.metal.OceanTheme.5
            @Override // javax.swing.UIDefaults.LazyValue
            public Object createValue(UIDefaults uIDefaults2) {
                return new IFIcon(OceanTheme.this.getHastenedIcon("icons/ocean/paletteClose.gif", uIDefaults2), OceanTheme.this.getHastenedIcon("icons/ocean/paletteClose-pressed.gif", uIDefaults2));
            }
        }, "List.focusCellHighlightBorder", swingLazyValue, "MenuBarUI", "javax.swing.plaf.metal.MetalMenuBarUI", "OptionPane.errorIcon", getIconResource("icons/ocean/error.png"), "OptionPane.informationIcon", getIconResource("icons/ocean/info.png"), "OptionPane.questionIcon", getIconResource("icons/ocean/question.png"), "OptionPane.warningIcon", getIconResource("icons/ocean/warning.png"), "RadioButton.gradient", objAsList, "RadioButton.rollover", Boolean.TRUE, "RadioButtonMenuItem.gradient", objAsList, "ScrollBar.gradient", objAsList, "Slider.altTrackColor", new ColorUIResource(13820655), "Slider.gradient", objAsList2, "Slider.focusGradient", objAsList2, "SplitPane.oneTouchButtonsOpaque", Boolean.FALSE, "SplitPane.dividerFocusColor", colorUIResource3, "TabbedPane.borderHightlightColor", getPrimary1(), "TabbedPane.contentAreaColor", colorUIResource3, "TabbedPane.contentBorderInsets", new Insets(4, 2, 3, 3), "TabbedPane.selected", colorUIResource3, "TabbedPane.tabAreaBackground", colorUIResource2, "TabbedPane.tabAreaInsets", new Insets(2, 2, 0, 6), "TabbedPane.unselectedBackground", SECONDARY3, "Table.focusCellHighlightBorder", swingLazyValue, "Table.gridColor", SECONDARY1, "TableHeader.focusCellBackground", colorUIResource3, "ToggleButton.gradient", objAsList, "ToolBar.borderColor", colorUIResource, "ToolBar.isRollover", Boolean.TRUE, "Tree.closedIcon", iconResource, "Tree.collapsedIcon", new UIDefaults.LazyValue() { // from class: javax.swing.plaf.metal.OceanTheme.6
            @Override // javax.swing.UIDefaults.LazyValue
            public Object createValue(UIDefaults uIDefaults2) {
                return new COIcon(OceanTheme.this.getHastenedIcon("icons/ocean/collapsed.gif", uIDefaults2), OceanTheme.this.getHastenedIcon("icons/ocean/collapsed-rtl.gif", uIDefaults2));
            }
        }, "Tree.expandedIcon", getIconResource("icons/ocean/expanded.gif"), "Tree.leafIcon", iconResource2, "Tree.openIcon", iconResource, "Tree.selectionBorderColor", getPrimary1(), "Tree.dropLineColor", getPrimary1(), "Table.dropLineColor", getPrimary1(), "Table.dropLineShortColor", OCEAN_BLACK, "Table.dropCellBackground", OCEAN_DROP, "Tree.dropCellBackground", OCEAN_DROP, "List.dropCellBackground", OCEAN_DROP, "List.dropLineColor", getPrimary1()});
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    boolean isSystemTheme() {
        return true;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    public String getName() {
        return "Ocean";
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getPrimary1() {
        return PRIMARY1;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getPrimary2() {
        return PRIMARY2;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getPrimary3() {
        return PRIMARY3;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getSecondary1() {
        return SECONDARY1;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getSecondary2() {
        return SECONDARY2;
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getSecondary3() {
        return SECONDARY3;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getBlack() {
        return OCEAN_BLACK;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getDesktopColor() {
        return MetalTheme.white;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getInactiveControlTextColor() {
        return INACTIVE_CONTROL_TEXT_COLOR;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getControlTextColor() {
        return CONTROL_TEXT_COLOR;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public ColorUIResource getMenuDisabledForeground() {
        return MENU_DISABLED_FOREGROUND;
    }

    private Object getIconResource(String str) {
        return SwingUtilities2.makeIcon(getClass(), OceanTheme.class, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Icon getHastenedIcon(String str, UIDefaults uIDefaults) {
        return (Icon) ((UIDefaults.LazyValue) getIconResource(str)).createValue(uIDefaults);
    }
}
