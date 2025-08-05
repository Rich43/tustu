package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.borders.TinyButtonBorder;
import de.muntjak.tinylookandfeel.borders.TinyFrameBorder;
import de.muntjak.tinylookandfeel.borders.TinyInternalFrameBorder;
import de.muntjak.tinylookandfeel.borders.TinyMenuBarBorder;
import de.muntjak.tinylookandfeel.borders.TinyPopupMenuBorder;
import de.muntjak.tinylookandfeel.borders.TinyProgressBarBorder;
import de.muntjak.tinylookandfeel.borders.TinyScrollPaneBorder;
import de.muntjak.tinylookandfeel.borders.TinySpinnerBorder;
import de.muntjak.tinylookandfeel.borders.TinyTableHeaderBorder;
import de.muntjak.tinylookandfeel.borders.TinyTableHeaderRolloverBorder;
import de.muntjak.tinylookandfeel.borders.TinyTableScrollPaneBorder;
import de.muntjak.tinylookandfeel.borders.TinyTextFieldBorder;
import de.muntjak.tinylookandfeel.borders.TinyToolBarBorder;
import de.muntjak.tinylookandfeel.borders.TinyToolTipBorder;
import java.awt.Color;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.icepdf.core.util.PdfOps;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyLookAndFeel.class */
public class TinyLookAndFeel extends MetalLookAndFeel {
    public static final boolean PRINT_CACHE_SIZES = false;
    static final int MINIMUM_FRAME_WIDTH = 104;
    static final int MINIMUM_INTERNAL_FRAME_WIDTH = 32;
    public static final String VERSION_STRING = "1.4.0";
    public static final String DATE_STRING = "2009/8/25";
    public static Robot ROBOT;
    static Class class$de$muntjak$tinylookandfeel$TinyLookAndFeel;
    public static boolean controlPanelInstantiated = false;
    private static boolean isInstalled = false;

    @Override // javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public void initialize() throws Throwable {
        super.initialize();
        if (!isInstalled) {
            isInstalled = true;
            searchDefaultTheme();
            UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo("TinyLookAndFeel", "de.muntjak.tinylookandfeel.TinyLookAndFeel"));
        }
        TinyPopupFactory.install();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(TinyMenuUI.ALT_PROCESSOR);
        clearAllCaches();
    }

    public static void clearAllCaches() {
        TinyTitlePane.clearCache();
        TinyInternalFrameBorder.clearCache();
        TinyButtonUI.clearCache();
        TinyCheckBoxIcon.clearCache();
        TinyComboBoxButton.clearCache();
        TinyProgressBarUI.clearCache();
        TinyRadioButtonIcon.clearCache();
        TinyScrollBarUI.clearCache();
        TinyScrollButton.clearCache();
        TinySpinnerButtonUI.clearCache();
        TinyWindowButtonUI.clearCache();
        MenuItemIconFactory.clearCache();
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public void uninitialize() {
        super.uninitialize();
        TinyPopupFactory.uninstall();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(TinyMenuUI.ALT_PROCESSOR);
    }

    private void searchDefaultTheme() throws Throwable {
        Class clsClass$;
        if (controlPanelInstantiated) {
            return;
        }
        String externalForm = null;
        if (class$de$muntjak$tinylookandfeel$TinyLookAndFeel == null) {
            clsClass$ = class$("de.muntjak.tinylookandfeel.TinyLookAndFeel");
            class$de$muntjak$tinylookandfeel$TinyLookAndFeel = clsClass$;
        } else {
            clsClass$ = class$de$muntjak$tinylookandfeel$TinyLookAndFeel;
        }
        URL resource = clsClass$.getResource("/Default.theme");
        if (Theme.loadTheme(resource)) {
            externalForm = resource.toExternalForm();
        } else {
            URL resource2 = Thread.currentThread().getContextClassLoader().getResource(Theme.DEFAULT_THEME);
            if (Theme.loadTheme(resource2)) {
                externalForm = resource2.toExternalForm();
            } else {
                try {
                    URL url = new File(TinyUtils.getSystemProperty("user.home"), Theme.DEFAULT_THEME).toURI().toURL();
                    if (Theme.loadTheme(url)) {
                        externalForm = url.toExternalForm();
                    } else {
                        URL url2 = new File(TinyUtils.getSystemProperty("user.dir"), Theme.DEFAULT_THEME).toURI().toURL();
                        if (Theme.loadTheme(url2)) {
                            externalForm = url2.toExternalForm();
                        }
                    }
                } catch (MalformedURLException e2) {
                } catch (AccessControlException e3) {
                }
            }
        }
        if (externalForm == null) {
            System.out.println(new StringBuffer().append("TinyLaF v1.4.0\n").append("'Default.theme' not found - using YQ default theme.").toString());
        } else {
            System.out.println(new StringBuffer().append("TinyLaF v1.4.0\n").append("Theme: ").append(externalForm).toString());
        }
    }

    @Override // javax.swing.plaf.metal.MetalLookAndFeel, javax.swing.LookAndFeel
    public String getID() {
        return "TinyLookAndFeel";
    }

    @Override // javax.swing.plaf.metal.MetalLookAndFeel, javax.swing.LookAndFeel
    public String getName() {
        return "TinyLookAndFeel";
    }

    @Override // javax.swing.plaf.metal.MetalLookAndFeel, javax.swing.LookAndFeel
    public String getDescription() {
        return "TinyLookAndFeel";
    }

    @Override // javax.swing.plaf.metal.MetalLookAndFeel, javax.swing.LookAndFeel
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override // javax.swing.plaf.metal.MetalLookAndFeel, javax.swing.LookAndFeel
    public final boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override // javax.swing.plaf.metal.MetalLookAndFeel, javax.swing.LookAndFeel
    public boolean getSupportsWindowDecorations() {
        return true;
    }

    @Override // javax.swing.plaf.metal.MetalLookAndFeel, javax.swing.plaf.basic.BasicLookAndFeel
    protected void initClassDefaults(UIDefaults uIDefaults) {
        super.initClassDefaults(uIDefaults);
        uIDefaults.putDefaults(new Object[]{"ButtonUI", "de.muntjak.tinylookandfeel.TinyButtonUI", "CheckBoxUI", "de.muntjak.tinylookandfeel.TinyCheckBoxUI", "TextFieldUI", "de.muntjak.tinylookandfeel.TinyTextFieldUI", "TextAreaUI", "de.muntjak.tinylookandfeel.TinyTextAreaUI", "FormattedTextFieldUI", "de.muntjak.tinylookandfeel.TinyFormattedTextFieldUI", "PasswordFieldUI", "de.muntjak.tinylookandfeel.TinyPasswordFieldUI", "EditorPaneUI", "de.muntjak.tinylookandfeel.TinyEditorPaneUI", "TextPaneUI", "de.muntjak.tinylookandfeel.TinyTextPaneUI", "SliderUI", "de.muntjak.tinylookandfeel.TinySliderUI", "SpinnerUI", "de.muntjak.tinylookandfeel.TinySpinnerUI", "ToolBarUI", "de.muntjak.tinylookandfeel.TinyToolBarUI", "ToolBarSeparatorUI", "de.muntjak.tinylookandfeel.TinyToolBarSeparatorUI", "MenuBarUI", "de.muntjak.tinylookandfeel.TinyMenuBarUI", "MenuUI", "de.muntjak.tinylookandfeel.TinyMenuUI", "MenuItemUI", "de.muntjak.tinylookandfeel.TinyMenuItemUI", "CheckBoxMenuItemUI", "de.muntjak.tinylookandfeel.TinyCheckBoxMenuItemUI", "RadioButtonMenuItemUI", "de.muntjak.tinylookandfeel.TinyRadioButtonMenuItemUI", "ScrollBarUI", "de.muntjak.tinylookandfeel.TinyScrollBarUI", "TabbedPaneUI", "de.muntjak.tinylookandfeel.TinyTabbedPaneUI", "ToggleButtonUI", "de.muntjak.tinylookandfeel.TinyButtonUI", "ScrollPaneUI", "de.muntjak.tinylookandfeel.TinyScrollPaneUI", "ProgressBarUI", "de.muntjak.tinylookandfeel.TinyProgressBarUI", "InternalFrameUI", "de.muntjak.tinylookandfeel.TinyInternalFrameUI", "RadioButtonUI", "de.muntjak.tinylookandfeel.TinyRadioButtonUI", "ComboBoxUI", "de.muntjak.tinylookandfeel.TinyComboBoxUI", "PopupMenuSeparatorUI", "de.muntjak.tinylookandfeel.TinyPopupMenuSeparatorUI", "SeparatorUI", "de.muntjak.tinylookandfeel.TinySeparatorUI", "SplitPaneUI", "de.muntjak.tinylookandfeel.TinySplitPaneUI", "FileChooserUI", "de.muntjak.tinylookandfeel.TinyFileChooserUI", "ListUI", "de.muntjak.tinylookandfeel.TinyListUI", "TreeUI", "de.muntjak.tinylookandfeel.TinyTreeUI", "LabelUI", "de.muntjak.tinylookandfeel.TinyLabelUI", "TableUI", "de.muntjak.tinylookandfeel.TinyTableUI", "TableHeaderUI", "de.muntjak.tinylookandfeel.TinyTableHeaderUI", "ToolTipUI", "de.muntjak.tinylookandfeel.TinyToolTipUI", "RootPaneUI", "de.muntjak.tinylookandfeel.TinyRootPaneUI", "DesktopPaneUI", "de.muntjak.tinylookandfeel.TinyDesktopPaneUI"});
    }

    @Override // javax.swing.plaf.metal.MetalLookAndFeel
    protected void createDefaultTheme() {
        setCurrentTheme(new TinyDefaultTheme());
    }

    @Override // javax.swing.plaf.metal.MetalLookAndFeel, javax.swing.plaf.basic.BasicLookAndFeel
    protected void initComponentDefaults(UIDefaults uIDefaults) {
        super.initComponentDefaults(uIDefaults);
        EmptyBorder emptyBorder = new EmptyBorder(0, 0, 0, 0);
        uIDefaults.put("Button.border", new TinyButtonBorder.CompoundBorderUIResource(new TinyButtonBorder(), new BasicBorders.MarginBorder()));
        TinyTextFieldBorder tinyTextFieldBorder = new TinyTextFieldBorder();
        uIDefaults.put("FormattedTextField.border", tinyTextFieldBorder);
        uIDefaults.put("TextField.border", tinyTextFieldBorder);
        uIDefaults.put("PasswordField.border", tinyTextFieldBorder);
        uIDefaults.put("ComboBox.border", emptyBorder);
        uIDefaults.put("Table.scrollPaneBorder", new TinyTableScrollPaneBorder());
        uIDefaults.put("TableHeader.cellBorder", new TinyTableHeaderBorder());
        uIDefaults.put("TableHeader.cellRolloverBorder", new TinyTableHeaderRolloverBorder());
        uIDefaults.put("Table.alternateRowColor", new ColorUIResource(228, 230, 236));
        uIDefaults.put("Spinner.border", new TinySpinnerBorder());
        uIDefaults.put("ProgressBar.border", new TinyProgressBarBorder());
        uIDefaults.put("ToolBar.border", new TinyToolBarBorder());
        uIDefaults.put("ToolTip.border", new BorderUIResource(new TinyToolTipBorder(true)));
        uIDefaults.put("ToolTip.borderInactive", new BorderUIResource(new TinyToolTipBorder(false)));
        TinyInternalFrameBorder tinyInternalFrameBorder = new TinyInternalFrameBorder();
        uIDefaults.put("InternalFrame.border", tinyInternalFrameBorder);
        uIDefaults.put("InternalFrame.paletteBorder", tinyInternalFrameBorder);
        uIDefaults.put("InternalFrame.optionDialogBorder", tinyInternalFrameBorder);
        uIDefaults.put("MenuBar.border", new TinyMenuBarBorder());
        EmptyBorder emptyBorder2 = new EmptyBorder(2, 4, 2, 4);
        uIDefaults.put("Menu.border", new EmptyBorder(2, 5, 2, 6));
        uIDefaults.put("MenuItem.border", emptyBorder2);
        uIDefaults.put("CheckBoxMenuItem.border", emptyBorder2);
        uIDefaults.put("RadioButtonMenuItem.border", emptyBorder2);
        uIDefaults.put("PopupMenu.border", new TinyPopupMenuBorder());
        uIDefaults.put("ScrollPane.border", new TinyScrollPaneBorder());
        uIDefaults.put("Slider.trackWidth", new Integer(4));
        uIDefaults.put("CheckBox.border", new BasicBorders.MarginBorder());
        uIDefaults.put("RadioButton.border", new BasicBorders.MarginBorder());
        uIDefaults.put("RadioButton.margin", new InsetsUIResource(2, 2, 2, 2));
        uIDefaults.put("CheckBox.margin", new InsetsUIResource(2, 2, 2, 2));
        uIDefaults.put("SplitPane.dividerSize", new Integer(7));
        if (TinyUtils.isOSLinux()) {
            uIDefaults.put("FileChooser.readOnly", Boolean.TRUE);
        }
        uIDefaults.put("TabbedPane.tabInsets", new Insets(1, 6, 4, 6));
        uIDefaults.put("TabbedPane.selectedTabPadInsets", new Insets(2, 2, 1, 2));
        uIDefaults.put("TabbedPane.tabAreaInsets", new Insets(6, 2, 0, 0));
        uIDefaults.put("TabbedPane.contentBorderInsets", new Insets(1, 1, 3, 3));
        uIDefaults.put("PopupMenu.foreground", new Color(255, 0, 0));
        uIDefaults.put("RootPane.colorChooserDialogBorder", TinyFrameBorder.getInstance());
        uIDefaults.put("RootPane.errorDialogBorder", TinyFrameBorder.getInstance());
        uIDefaults.put("RootPane.fileChooserDialogBorder", TinyFrameBorder.getInstance());
        uIDefaults.put("RootPane.frameBorder", TinyFrameBorder.getInstance());
        uIDefaults.put("RootPane.informationDialogBorder", TinyFrameBorder.getInstance());
        uIDefaults.put("RootPane.plainDialogBorder", TinyFrameBorder.getInstance());
        uIDefaults.put("RootPane.questionDialogBorder", TinyFrameBorder.getInstance());
        uIDefaults.put("RootPane.warningDialogBorder", TinyFrameBorder.getInstance());
        uIDefaults.put("CheckBoxMenuItem.checkIcon", MenuItemIconFactory.getCheckBoxMenuItemIcon());
        uIDefaults.put("RadioButtonMenuItem.checkIcon", MenuItemIconFactory.getRadioButtonMenuItemIcon());
        uIDefaults.put("Menu.arrowIcon", MenuItemIconFactory.getMenuArrowIcon());
        uIDefaults.put("InternalFrame.frameTitleHeight", new Integer(25));
        uIDefaults.put("InternalFrame.paletteTitleHeight", new Integer(16));
        uIDefaults.put("InternalFrame.icon", loadIcon("InternalFrameIcon.png"));
        uIDefaults.put("Tree.expandedIcon", loadIcon("TreeMinusIcon.png"));
        uIDefaults.put("Tree.collapsedIcon", loadIcon("TreePlusIcon.png"));
        uIDefaults.put("Tree.openIcon", loadIcon("TreeFolderOpenedIcon.png"));
        uIDefaults.put("Tree.closedIcon", loadIcon("TreeFolderClosedIcon.png"));
        uIDefaults.put("Tree.leafIcon", loadIcon("TreeLeafIcon.png"));
        uIDefaults.put("FileView.directoryIcon", loadIcon("DirectoryIcon.png"));
        uIDefaults.put("FileView.computerIcon", loadIcon("ComputerIcon.png"));
        uIDefaults.put("FileView.fileIcon", loadIcon("FileIcon.png"));
        uIDefaults.put("FileView.floppyDriveIcon", loadIcon("FloppyIcon.png"));
        uIDefaults.put("FileView.hardDriveIcon", loadIcon("HarddiskIcon.png"));
        uIDefaults.put("FileChooser.detailsViewIcon", loadIcon("FileDetailsIcon.png"));
        uIDefaults.put("FileChooser.homeFolderIcon", loadIcon("HomeFolderIcon.png"));
        uIDefaults.put("FileChooser.listViewIcon", loadIcon("FileListIcon.png"));
        uIDefaults.put("FileChooser.newFolderIcon", loadIcon("NewFolderIcon.png"));
        uIDefaults.put("FileChooser.upFolderIcon", loadIcon("ParentDirectoryIcon.png"));
        uIDefaults.put("OptionPane.errorIcon", loadIcon("ErrorIcon.png"));
        uIDefaults.put("OptionPane.informationIcon", loadIcon("InformationIcon.png"));
        uIDefaults.put("OptionPane.warningIcon", loadIcon("WarningIcon.png"));
        uIDefaults.put("OptionPane.questionIcon", loadIcon("QuestionIcon.png"));
    }

    public static Icon getUncolorizedSystemIcon(int i2) {
        switch (i2) {
            case 0:
                return loadIcon("InternalFrameIcon.png");
            case 1:
                return loadIcon("TreeFolderClosedIcon.png");
            case 2:
                return loadIcon("TreeFolderOpenedIcon.png");
            case 3:
                return loadIcon("TreeLeafIcon.png");
            case 4:
                return loadIcon("TreeMinusIcon.png");
            case 5:
                return loadIcon("TreePlusIcon.png");
            case 6:
                return loadIcon("ComputerIcon.png");
            case 7:
                return loadIcon("FloppyIcon.png");
            case 8:
                return loadIcon("HarddiskIcon.png");
            case 9:
                return loadIcon("DirectoryIcon.png");
            case 10:
                return loadIcon("FileIcon.png");
            case 11:
                return loadIcon("ParentDirectoryIcon.png");
            case 12:
                return loadIcon("HomeFolderIcon.png");
            case 13:
                return loadIcon("NewFolderIcon.png");
            case 14:
                return loadIcon("FileListIcon.png");
            case 15:
                return loadIcon("FileDetailsIcon.png");
            case 16:
                return loadIcon("InformationIcon.png");
            case 17:
                return loadIcon("QuestionIcon.png");
            case 18:
                return loadIcon("WarningIcon.png");
            default:
                return loadIcon("ErrorIcon.png");
        }
    }

    public static String getSystemIconName(int i2) {
        switch (i2) {
            case 0:
                return "InternalFrame.icon";
            case 1:
                return "Tree.closedIcon";
            case 2:
                return "Tree.openIcon";
            case 3:
                return "Tree.leafIcon";
            case 4:
                return "Tree.expandedIcon";
            case 5:
                return "Tree.collapsedIcon";
            case 6:
                return "FileView.computerIcon";
            case 7:
                return "FileView.floppyDriveIcon";
            case 8:
                return "FileView.hardDriveIcon";
            case 9:
                return "FileView.directoryIcon";
            case 10:
                return "FileView.fileIcon";
            case 11:
                return "FileChooser.upFolderIcon";
            case 12:
                return "FileChooser.homeFolderIcon";
            case 13:
                return "FileChooser.newFolderIcon";
            case 14:
                return "FileChooser.listViewIcon";
            case 15:
                return "FileChooser.detailsViewIcon";
            case 16:
                return "OptionPane.informationIcon";
            case 17:
                return "OptionPane.questionIcon";
            case 18:
                return "OptionPane.warningIcon";
            default:
                return "OptionPane.errorIcon";
        }
    }

    public static ImageIcon loadIcon(String str) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        URL resource = str.indexOf("/") != -1 ? Thread.currentThread().getContextClassLoader().getResource(new StringBuffer().append("de/muntjak/tinylookandfeel/").append(str).toString()) : Thread.currentThread().getContextClassLoader().getResource(new StringBuffer().append("de/muntjak/tinylookandfeel/icons/").append(str).toString());
        if (resource == null) {
            if (str.indexOf("/") != -1) {
                if (class$de$muntjak$tinylookandfeel$TinyLookAndFeel == null) {
                    clsClass$2 = class$("de.muntjak.tinylookandfeel.TinyLookAndFeel");
                    class$de$muntjak$tinylookandfeel$TinyLookAndFeel = clsClass$2;
                } else {
                    clsClass$2 = class$de$muntjak$tinylookandfeel$TinyLookAndFeel;
                }
                resource = clsClass$2.getResource(new StringBuffer().append("/de/muntjak/tinylookandfeel/").append(str).toString());
            } else {
                if (class$de$muntjak$tinylookandfeel$TinyLookAndFeel == null) {
                    clsClass$ = class$("de.muntjak.tinylookandfeel.TinyLookAndFeel");
                    class$de$muntjak$tinylookandfeel$TinyLookAndFeel = clsClass$;
                } else {
                    clsClass$ = class$de$muntjak$tinylookandfeel$TinyLookAndFeel;
                }
                resource = clsClass$.getResource(new StringBuffer().append("/de/muntjak/tinylookandfeel/icons/").append(str).toString());
            }
            if (resource == null) {
                System.err.println(new StringBuffer().append("TinyLaF: Icon directory could not be resolved. fileName argument:\"").append(str).append(PdfOps.DOUBLE_QUOTE__TOKEN).toString());
                return null;
            }
        }
        return new ImageIcon(resource);
    }

    static Class class$(String str) throws Throwable {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError().initCause(e2);
        }
    }

    static {
        if (ROBOT == null) {
            try {
                ROBOT = new Robot();
            } catch (Exception e2) {
                ROBOT = null;
            }
        }
    }
}
