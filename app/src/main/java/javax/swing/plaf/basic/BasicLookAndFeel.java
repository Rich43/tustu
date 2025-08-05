package javax.swing.plaf.basic;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Locale;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.text.DefaultEditorKit;
import org.slf4j.Marker;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.swing.FilePane;
import sun.swing.SwingLazyValue;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicLookAndFeel.class */
public abstract class BasicLookAndFeel extends LookAndFeel implements Serializable {
    static boolean needsEventHelper;
    private Clip clipPlaying;
    private transient Object audioLock = new Object();
    AWTEventHelper invocator = null;
    private PropertyChangeListener disposer = null;

    @Override // javax.swing.LookAndFeel
    public UIDefaults getDefaults() {
        UIDefaults uIDefaults = new UIDefaults(610, 0.75f);
        initClassDefaults(uIDefaults);
        initSystemColorDefaults(uIDefaults);
        initComponentDefaults(uIDefaults);
        return uIDefaults;
    }

    @Override // javax.swing.LookAndFeel
    public void initialize() {
        if (needsEventHelper) {
            installAWTEventListener();
        }
    }

    void installAWTEventListener() {
        if (this.invocator == null) {
            this.invocator = new AWTEventHelper();
            needsEventHelper = true;
            this.disposer = new PropertyChangeListener() { // from class: javax.swing.plaf.basic.BasicLookAndFeel.1
                @Override // java.beans.PropertyChangeListener
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    BasicLookAndFeel.this.uninitialize();
                }
            };
            AppContext.getAppContext().addPropertyChangeListener(AppContext.GUI_DISPOSED, this.disposer);
        }
    }

    @Override // javax.swing.LookAndFeel
    public void uninitialize() {
        AppContext appContext = AppContext.getAppContext();
        synchronized (BasicPopupMenuUI.MOUSE_GRABBER_KEY) {
            Object obj = appContext.get(BasicPopupMenuUI.MOUSE_GRABBER_KEY);
            if (obj != null) {
                ((BasicPopupMenuUI.MouseGrabber) obj).uninstall();
            }
        }
        synchronized (BasicPopupMenuUI.MENU_KEYBOARD_HELPER_KEY) {
            Object obj2 = appContext.get(BasicPopupMenuUI.MENU_KEYBOARD_HELPER_KEY);
            if (obj2 != null) {
                ((BasicPopupMenuUI.MenuKeyboardHelper) obj2).uninstall();
            }
        }
        if (this.invocator != null) {
            AccessController.doPrivileged(this.invocator);
            this.invocator = null;
        }
        if (this.disposer != null) {
            appContext.removePropertyChangeListener(AppContext.GUI_DISPOSED, this.disposer);
            this.disposer = null;
        }
    }

    protected void initClassDefaults(UIDefaults uIDefaults) {
        uIDefaults.putDefaults(new Object[]{"ButtonUI", "javax.swing.plaf.basic.BasicButtonUI", "CheckBoxUI", "javax.swing.plaf.basic.BasicCheckBoxUI", "ColorChooserUI", "javax.swing.plaf.basic.BasicColorChooserUI", "FormattedTextFieldUI", "javax.swing.plaf.basic.BasicFormattedTextFieldUI", "MenuBarUI", "javax.swing.plaf.basic.BasicMenuBarUI", "MenuUI", "javax.swing.plaf.basic.BasicMenuUI", "MenuItemUI", "javax.swing.plaf.basic.BasicMenuItemUI", "CheckBoxMenuItemUI", "javax.swing.plaf.basic.BasicCheckBoxMenuItemUI", "RadioButtonMenuItemUI", "javax.swing.plaf.basic.BasicRadioButtonMenuItemUI", "RadioButtonUI", "javax.swing.plaf.basic.BasicRadioButtonUI", "ToggleButtonUI", "javax.swing.plaf.basic.BasicToggleButtonUI", "PopupMenuUI", "javax.swing.plaf.basic.BasicPopupMenuUI", "ProgressBarUI", "javax.swing.plaf.basic.BasicProgressBarUI", "ScrollBarUI", "javax.swing.plaf.basic.BasicScrollBarUI", "ScrollPaneUI", "javax.swing.plaf.basic.BasicScrollPaneUI", "SplitPaneUI", "javax.swing.plaf.basic.BasicSplitPaneUI", "SliderUI", "javax.swing.plaf.basic.BasicSliderUI", "SeparatorUI", "javax.swing.plaf.basic.BasicSeparatorUI", "SpinnerUI", "javax.swing.plaf.basic.BasicSpinnerUI", "ToolBarSeparatorUI", "javax.swing.plaf.basic.BasicToolBarSeparatorUI", "PopupMenuSeparatorUI", "javax.swing.plaf.basic.BasicPopupMenuSeparatorUI", "TabbedPaneUI", "javax.swing.plaf.basic.BasicTabbedPaneUI", "TextAreaUI", "javax.swing.plaf.basic.BasicTextAreaUI", "TextFieldUI", "javax.swing.plaf.basic.BasicTextFieldUI", "PasswordFieldUI", "javax.swing.plaf.basic.BasicPasswordFieldUI", "TextPaneUI", "javax.swing.plaf.basic.BasicTextPaneUI", "EditorPaneUI", "javax.swing.plaf.basic.BasicEditorPaneUI", "TreeUI", "javax.swing.plaf.basic.BasicTreeUI", "LabelUI", "javax.swing.plaf.basic.BasicLabelUI", "ListUI", "javax.swing.plaf.basic.BasicListUI", "ToolBarUI", "javax.swing.plaf.basic.BasicToolBarUI", "ToolTipUI", "javax.swing.plaf.basic.BasicToolTipUI", "ComboBoxUI", "javax.swing.plaf.basic.BasicComboBoxUI", "TableUI", "javax.swing.plaf.basic.BasicTableUI", "TableHeaderUI", "javax.swing.plaf.basic.BasicTableHeaderUI", "InternalFrameUI", "javax.swing.plaf.basic.BasicInternalFrameUI", "DesktopPaneUI", "javax.swing.plaf.basic.BasicDesktopPaneUI", "DesktopIconUI", "javax.swing.plaf.basic.BasicDesktopIconUI", "FileChooserUI", "javax.swing.plaf.basic.BasicFileChooserUI", "OptionPaneUI", "javax.swing.plaf.basic.BasicOptionPaneUI", "PanelUI", "javax.swing.plaf.basic.BasicPanelUI", "ViewportUI", "javax.swing.plaf.basic.BasicViewportUI", "RootPaneUI", "javax.swing.plaf.basic.BasicRootPaneUI"});
    }

    protected void initSystemColorDefaults(UIDefaults uIDefaults) {
        loadSystemColors(uIDefaults, new String[]{"desktop", "#005C5C", "activeCaption", "#000080", "activeCaptionText", "#FFFFFF", "activeCaptionBorder", "#C0C0C0", "inactiveCaption", "#808080", "inactiveCaptionText", "#C0C0C0", "inactiveCaptionBorder", "#C0C0C0", "window", "#FFFFFF", "windowBorder", "#000000", "windowText", "#000000", "menu", "#C0C0C0", "menuText", "#000000", "text", "#C0C0C0", "textText", "#000000", "textHighlight", "#000080", "textHighlightText", "#FFFFFF", "textInactiveText", "#808080", "control", "#C0C0C0", "controlText", "#000000", "controlHighlight", "#C0C0C0", "controlLtHighlight", "#FFFFFF", "controlShadow", "#808080", "controlDkShadow", "#000000", "scrollbar", "#E0E0E0", "info", "#FFFFE1", "infoText", "#000000"}, isNativeLookAndFeel());
    }

    protected void loadSystemColors(UIDefaults uIDefaults, String[] strArr, boolean z2) {
        if (z2) {
            for (int i2 = 0; i2 < strArr.length; i2 += 2) {
                Color color = Color.black;
                try {
                    color = (Color) SystemColor.class.getField(strArr[i2]).get(null);
                } catch (Exception e2) {
                }
                uIDefaults.put(strArr[i2], new ColorUIResource(color));
            }
            return;
        }
        for (int i3 = 0; i3 < strArr.length; i3 += 2) {
            Color colorDecode = Color.black;
            try {
                colorDecode = Color.decode(strArr[i3 + 1]);
            } catch (NumberFormatException e3) {
                e3.printStackTrace();
            }
            uIDefaults.put(strArr[i3], new ColorUIResource(colorDecode));
        }
    }

    private void initResourceBundle(UIDefaults uIDefaults) {
        uIDefaults.setDefaultLocale(Locale.getDefault());
        uIDefaults.addResourceBundle("com.sun.swing.internal.plaf.basic.resources.basic");
    }

    protected void initComponentDefaults(UIDefaults uIDefaults) {
        initResourceBundle(uIDefaults);
        Object num = new Integer(500);
        Object l2 = new Long(1000L);
        Integer num2 = new Integer(12);
        Integer num3 = new Integer(0);
        Integer num4 = new Integer(1);
        Object swingLazyValue = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[]{Font.DIALOG, num3, num2});
        Object swingLazyValue2 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[]{"Serif", num3, num2});
        Object swingLazyValue3 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[]{"SansSerif", num3, num2});
        Object swingLazyValue4 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[]{"Monospaced", num3, num2});
        Object swingLazyValue5 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[]{Font.DIALOG, num4, num2});
        Object colorUIResource = new ColorUIResource(Color.red);
        Object colorUIResource2 = new ColorUIResource(Color.black);
        Object colorUIResource3 = new ColorUIResource(Color.white);
        ColorUIResource colorUIResource4 = new ColorUIResource(Color.yellow);
        Object colorUIResource5 = new ColorUIResource(Color.gray);
        new ColorUIResource(Color.lightGray);
        Object colorUIResource6 = new ColorUIResource(Color.darkGray);
        Object colorUIResource7 = new ColorUIResource(224, 224, 224);
        Color color = uIDefaults.getColor("control");
        Color color2 = uIDefaults.getColor("controlDkShadow");
        Color color3 = uIDefaults.getColor("controlHighlight");
        Color color4 = uIDefaults.getColor("controlLtHighlight");
        Color color5 = uIDefaults.getColor("controlShadow");
        Color color6 = uIDefaults.getColor("controlText");
        Color color7 = uIDefaults.getColor("menu");
        Color color8 = uIDefaults.getColor("menuText");
        Color color9 = uIDefaults.getColor("textHighlight");
        Color color10 = uIDefaults.getColor("textHighlightText");
        Color color11 = uIDefaults.getColor("textInactiveText");
        Color color12 = uIDefaults.getColor("textText");
        Color color13 = uIDefaults.getColor("window");
        Object insetsUIResource = new InsetsUIResource(0, 0, 0, 0);
        Object insetsUIResource2 = new InsetsUIResource(2, 2, 2, 2);
        Object insetsUIResource3 = new InsetsUIResource(3, 3, 3, 3);
        Object swingLazyValue6 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders$MarginBorder");
        Object swingLazyValue7 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getEtchedBorderUIResource");
        Object swingLazyValue8 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getLoweredBevelBorderUIResource");
        Object swingLazyValue9 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getInternalFrameBorder");
        Object swingLazyValue10 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getBlackLineBorderUIResource");
        Object swingLazyValue11 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", null, new Object[]{colorUIResource4});
        Object emptyBorderUIResource = new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1);
        Object swingLazyValue12 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$BevelBorderUIResource", null, new Object[]{new Integer(0), color4, color, color2, color5});
        Object swingLazyValue13 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getButtonBorder");
        Object swingLazyValue14 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getToggleButtonBorder");
        Object swingLazyValue15 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getRadioButtonBorder");
        Object objMakeIcon = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/NewFolder.gif");
        Object objMakeIcon2 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/UpFolder.gif");
        Object objMakeIcon3 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/HomeFolder.gif");
        Object objMakeIcon4 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/DetailsView.gif");
        Object objMakeIcon5 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/ListView.gif");
        Object objMakeIcon6 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Directory.gif");
        Object objMakeIcon7 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/File.gif");
        Object objMakeIcon8 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Computer.gif");
        Object objMakeIcon9 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/HardDrive.gif");
        Object objMakeIcon10 = SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/FloppyDrive.gif");
        Object swingLazyValue16 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getInternalFrameBorder");
        Object obj = new UIDefaults.ActiveValue() { // from class: javax.swing.plaf.basic.BasicLookAndFeel.2
            @Override // javax.swing.UIDefaults.ActiveValue
            public Object createValue(UIDefaults uIDefaults2) {
                return new DefaultListCellRenderer.UIResource();
            }
        };
        Object swingLazyValue17 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getMenuBarBorder");
        Object swingLazyValue18 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getMenuItemCheckIcon");
        Object swingLazyValue19 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getMenuItemArrowIcon");
        Object swingLazyValue20 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getMenuArrowIcon");
        Object swingLazyValue21 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getCheckBoxIcon");
        Object swingLazyValue22 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getRadioButtonIcon");
        Object swingLazyValue23 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getCheckBoxMenuItemIcon");
        Object swingLazyValue24 = new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "getRadioButtonMenuItemIcon");
        Object dimensionUIResource = new DimensionUIResource(262, 90);
        Object num5 = new Integer(0);
        Object swingLazyValue25 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$EmptyBorderUIResource", new Object[]{num5, num5, num5, num5});
        Integer num6 = new Integer(10);
        Object swingLazyValue26 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$EmptyBorderUIResource", new Object[]{num6, num6, num2, num6});
        Object swingLazyValue27 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$EmptyBorderUIResource", new Object[]{new Integer(6), num5, num5, num5});
        Object swingLazyValue28 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getProgressBarBorder");
        Object dimensionUIResource2 = new DimensionUIResource(8, 8);
        Object dimensionUIResource3 = new DimensionUIResource(4096, 4096);
        Object dimensionUIResource4 = new DimensionUIResource(10, 10);
        Object swingLazyValue29 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getSplitPaneBorder");
        Object swingLazyValue30 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getSplitPaneDividerBorder");
        Object insetsUIResource4 = new InsetsUIResource(0, 4, 1, 4);
        Object insetsUIResource5 = new InsetsUIResource(2, 2, 2, 1);
        Object insetsUIResource6 = new InsetsUIResource(3, 2, 0, 2);
        Object insetsUIResource7 = new InsetsUIResource(2, 2, 3, 3);
        Object swingLazyValue31 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getTextFieldBorder");
        Object num7 = new Integer(4);
        Object[] objArr = {"CheckBoxMenuItem.commandSound", "InternalFrame.closeSound", "InternalFrame.maximizeSound", "InternalFrame.minimizeSound", "InternalFrame.restoreDownSound", "InternalFrame.restoreUpSound", "MenuItem.commandSound", "OptionPane.errorSound", "OptionPane.informationSound", "OptionPane.questionSound", "OptionPane.warningSound", "PopupMenu.popupSound", "RadioButtonMenuItem.commandSound"};
        Object[] objArr2 = {"shift F10", BasicRootPaneUI.Actions.POST_POPUP, "CONTEXT_MENU", BasicRootPaneUI.Actions.POST_POPUP};
        uIDefaults.putDefaults(new Object[]{"AuditoryCues.cueList", objArr, "AuditoryCues.allAuditoryCues", objArr, "AuditoryCues.noAuditoryCues", new Object[]{"mute"}, "AuditoryCues.playList", null, "Button.defaultButtonFollowsFocus", Boolean.TRUE, "Button.font", swingLazyValue, "Button.background", color, "Button.foreground", color6, "Button.shadow", color5, "Button.darkShadow", color2, "Button.light", color3, "Button.highlight", color4, "Button.border", swingLazyValue13, "Button.margin", new InsetsUIResource(2, 14, 2, 14), "Button.textIconGap", num7, "Button.textShiftOffset", num5, "Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released", "ENTER", "pressed", "released ENTER", "released"}), "ToggleButton.font", swingLazyValue, "ToggleButton.background", color, "ToggleButton.foreground", color6, "ToggleButton.shadow", color5, "ToggleButton.darkShadow", color2, "ToggleButton.light", color3, "ToggleButton.highlight", color4, "ToggleButton.border", swingLazyValue14, "ToggleButton.margin", new InsetsUIResource(2, 14, 2, 14), "ToggleButton.textIconGap", num7, "ToggleButton.textShiftOffset", num5, "ToggleButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "RadioButton.font", swingLazyValue, "RadioButton.background", color, "RadioButton.foreground", color6, "RadioButton.shadow", color5, "RadioButton.darkShadow", color2, "RadioButton.light", color3, "RadioButton.highlight", color4, "RadioButton.border", swingLazyValue15, "RadioButton.margin", insetsUIResource2, "RadioButton.textIconGap", num7, "RadioButton.textShiftOffset", num5, "RadioButton.icon", swingLazyValue22, "RadioButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released", "RETURN", "pressed"}), "CheckBox.font", swingLazyValue, "CheckBox.background", color, "CheckBox.foreground", color6, "CheckBox.border", swingLazyValue15, "CheckBox.margin", insetsUIResource2, "CheckBox.textIconGap", num7, "CheckBox.textShiftOffset", num5, "CheckBox.icon", swingLazyValue21, "CheckBox.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "FileChooser.useSystemExtensionHiding", Boolean.FALSE, "ColorChooser.font", swingLazyValue, "ColorChooser.background", color, "ColorChooser.foreground", color6, "ColorChooser.swatchesSwatchSize", new Dimension(10, 10), "ColorChooser.swatchesRecentSwatchSize", new Dimension(10, 10), "ColorChooser.swatchesDefaultRecentColor", color, "ComboBox.font", swingLazyValue3, "ComboBox.background", color13, "ComboBox.foreground", color12, "ComboBox.buttonBackground", color, "ComboBox.buttonShadow", color5, "ComboBox.buttonDarkShadow", color2, "ComboBox.buttonHighlight", color4, "ComboBox.selectionBackground", color9, "ComboBox.selectionForeground", color10, "ComboBox.disabledBackground", color, "ComboBox.disabledForeground", color11, "ComboBox.timeFactor", l2, "ComboBox.isEnterSelectablePopup", Boolean.FALSE, "ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "ENTER", "enterPressed"}), "ComboBox.noActionOnKeyNavigation", Boolean.FALSE, "FileChooser.newFolderIcon", objMakeIcon, "FileChooser.upFolderIcon", objMakeIcon2, "FileChooser.homeFolderIcon", objMakeIcon3, "FileChooser.detailsViewIcon", objMakeIcon4, "FileChooser.listViewIcon", objMakeIcon5, "FileChooser.readOnly", Boolean.FALSE, "FileChooser.usesSingleFilePane", Boolean.FALSE, "FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", FilePane.ACTION_CANCEL, "F5", FilePane.ACTION_REFRESH}), "FileView.directoryIcon", objMakeIcon6, "FileView.fileIcon", objMakeIcon7, "FileView.computerIcon", objMakeIcon8, "FileView.hardDriveIcon", objMakeIcon9, "FileView.floppyDriveIcon", objMakeIcon10, "InternalFrame.titleFont", swingLazyValue5, "InternalFrame.borderColor", color, "InternalFrame.borderShadow", color5, "InternalFrame.borderDarkShadow", color2, "InternalFrame.borderHighlight", color4, "InternalFrame.borderLight", color3, "InternalFrame.border", swingLazyValue16, "InternalFrame.icon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/JavaCup16.png"), "InternalFrame.maximizeIcon", new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "createEmptyFrameIcon"), "InternalFrame.minimizeIcon", new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "createEmptyFrameIcon"), "InternalFrame.iconifyIcon", new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "createEmptyFrameIcon"), "InternalFrame.closeIcon", new SwingLazyValue("javax.swing.plaf.basic.BasicIconFactory", "createEmptyFrameIcon"), "InternalFrame.closeSound", null, "InternalFrame.maximizeSound", null, "InternalFrame.minimizeSound", null, "InternalFrame.restoreDownSound", null, "InternalFrame.restoreUpSound", null, "InternalFrame.activeTitleBackground", uIDefaults.get("activeCaption"), "InternalFrame.activeTitleForeground", uIDefaults.get("activeCaptionText"), "InternalFrame.inactiveTitleBackground", uIDefaults.get("inactiveCaption"), "InternalFrame.inactiveTitleForeground", uIDefaults.get("inactiveCaptionText"), "InternalFrame.windowBindings", new Object[]{"shift ESCAPE", "showSystemMenu", "ctrl SPACE", "showSystemMenu", "ESCAPE", "hideSystemMenu"}, "InternalFrameTitlePane.iconifyButtonOpacity", Boolean.TRUE, "InternalFrameTitlePane.maximizeButtonOpacity", Boolean.TRUE, "InternalFrameTitlePane.closeButtonOpacity", Boolean.TRUE, "DesktopIcon.border", swingLazyValue16, "Desktop.minOnScreenInsets", insetsUIResource3, "Desktop.background", uIDefaults.get("desktop"), "Desktop.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl F5", "restore", "ctrl F4", "close", "ctrl F7", "move", "ctrl F8", "resize", "RIGHT", JSplitPane.RIGHT, "KP_RIGHT", JSplitPane.RIGHT, "shift RIGHT", "shrinkRight", "shift KP_RIGHT", "shrinkRight", "LEFT", JSplitPane.LEFT, "KP_LEFT", JSplitPane.LEFT, "shift LEFT", "shrinkLeft", "shift KP_LEFT", "shrinkLeft", "UP", "up", "KP_UP", "up", "shift UP", "shrinkUp", "shift KP_UP", "shrinkUp", "DOWN", "down", "KP_DOWN", "down", "shift DOWN", "shrinkDown", "shift KP_DOWN", "shrinkDown", "ESCAPE", "escape", "ctrl F9", "minimize", "ctrl F10", "maximize", "ctrl F6", "selectNextFrame", "ctrl TAB", "selectNextFrame", "ctrl alt F6", "selectNextFrame", "shift ctrl alt F6", "selectPreviousFrame", "ctrl F12", "navigateNext", "shift ctrl F12", "navigatePrevious"}), "Label.font", swingLazyValue, "Label.background", color, "Label.foreground", color6, "Label.disabledForeground", colorUIResource3, "Label.disabledShadow", color5, "Label.border", null, "List.font", swingLazyValue, "List.background", color13, "List.foreground", color12, "List.selectionBackground", color9, "List.selectionForeground", color10, "List.noFocusBorder", emptyBorderUIResource, "List.focusCellHighlightBorder", swingLazyValue11, "List.dropLineColor", color5, "List.border", null, "List.cellRenderer", obj, "List.timeFactor", l2, "List.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "HOME", "selectFirstRow", "shift HOME", "selectFirstRowExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRowChangeLead", "END", "selectLastRow", "shift END", "selectLastRowExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRowChangeLead", "PAGE_UP", "scrollUp", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDown", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"}), "List.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"LEFT", "selectNextColumn", "KP_LEFT", "selectNextColumn", "shift LEFT", "selectNextColumnExtendSelection", "shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl shift LEFT", "selectNextColumnExtendSelection", "ctrl shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl LEFT", "selectNextColumnChangeLead", "ctrl KP_LEFT", "selectNextColumnChangeLead", "RIGHT", "selectPreviousColumn", "KP_RIGHT", "selectPreviousColumn", "shift RIGHT", "selectPreviousColumnExtendSelection", "shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl RIGHT", "selectPreviousColumnChangeLead", "ctrl KP_RIGHT", "selectPreviousColumnChangeLead"}), "MenuBar.font", swingLazyValue, "MenuBar.background", color7, "MenuBar.foreground", color8, "MenuBar.shadow", color5, "MenuBar.highlight", color4, "MenuBar.border", swingLazyValue17, "MenuBar.windowBindings", new Object[]{"F10", "takeFocus"}, "MenuItem.font", swingLazyValue, "MenuItem.acceleratorFont", swingLazyValue, "MenuItem.background", color7, "MenuItem.foreground", color8, "MenuItem.selectionForeground", color10, "MenuItem.selectionBackground", color9, "MenuItem.disabledForeground", null, "MenuItem.acceleratorForeground", color8, "MenuItem.acceleratorSelectionForeground", color10, "MenuItem.acceleratorDelimiter", Marker.ANY_NON_NULL_MARKER, "MenuItem.border", swingLazyValue6, "MenuItem.borderPainted", Boolean.FALSE, "MenuItem.margin", insetsUIResource2, "MenuItem.checkIcon", swingLazyValue18, "MenuItem.arrowIcon", swingLazyValue19, "MenuItem.commandSound", null, "RadioButtonMenuItem.font", swingLazyValue, "RadioButtonMenuItem.acceleratorFont", swingLazyValue, "RadioButtonMenuItem.background", color7, "RadioButtonMenuItem.foreground", color8, "RadioButtonMenuItem.selectionForeground", color10, "RadioButtonMenuItem.selectionBackground", color9, "RadioButtonMenuItem.disabledForeground", null, "RadioButtonMenuItem.acceleratorForeground", color8, "RadioButtonMenuItem.acceleratorSelectionForeground", color10, "RadioButtonMenuItem.border", swingLazyValue6, "RadioButtonMenuItem.borderPainted", Boolean.FALSE, "RadioButtonMenuItem.margin", insetsUIResource2, "RadioButtonMenuItem.checkIcon", swingLazyValue24, "RadioButtonMenuItem.arrowIcon", swingLazyValue19, "RadioButtonMenuItem.commandSound", null, "CheckBoxMenuItem.font", swingLazyValue, "CheckBoxMenuItem.acceleratorFont", swingLazyValue, "CheckBoxMenuItem.background", color7, "CheckBoxMenuItem.foreground", color8, "CheckBoxMenuItem.selectionForeground", color10, "CheckBoxMenuItem.selectionBackground", color9, "CheckBoxMenuItem.disabledForeground", null, "CheckBoxMenuItem.acceleratorForeground", color8, "CheckBoxMenuItem.acceleratorSelectionForeground", color10, "CheckBoxMenuItem.border", swingLazyValue6, "CheckBoxMenuItem.borderPainted", Boolean.FALSE, "CheckBoxMenuItem.margin", insetsUIResource2, "CheckBoxMenuItem.checkIcon", swingLazyValue23, "CheckBoxMenuItem.arrowIcon", swingLazyValue19, "CheckBoxMenuItem.commandSound", null, "Menu.font", swingLazyValue, "Menu.acceleratorFont", swingLazyValue, "Menu.background", color7, "Menu.foreground", color8, "Menu.selectionForeground", color10, "Menu.selectionBackground", color9, "Menu.disabledForeground", null, "Menu.acceleratorForeground", color8, "Menu.acceleratorSelectionForeground", color10, "Menu.border", swingLazyValue6, "Menu.borderPainted", Boolean.FALSE, "Menu.margin", insetsUIResource2, "Menu.checkIcon", swingLazyValue18, "Menu.arrowIcon", swingLazyValue20, "Menu.menuPopupOffsetX", new Integer(0), "Menu.menuPopupOffsetY", new Integer(0), "Menu.submenuPopupOffsetX", new Integer(0), "Menu.submenuPopupOffsetY", new Integer(0), "Menu.shortcutKeys", new int[]{SwingUtilities2.getSystemMnemonicKeyMask(), SwingUtilities2.setAltGraphMask(SwingUtilities2.getSystemMnemonicKeyMask())}, "Menu.crossMenuMnemonic", Boolean.TRUE, "Menu.cancelMode", "hideLastSubmenu", "Menu.preserveTopLevelSelection", Boolean.FALSE, "PopupMenu.font", swingLazyValue, "PopupMenu.background", color7, "PopupMenu.foreground", color8, "PopupMenu.border", swingLazyValue9, "PopupMenu.popupSound", null, "PopupMenu.selectedWindowInputMapBindings", new Object[]{"ESCAPE", "cancel", "DOWN", "selectNext", "KP_DOWN", "selectNext", "UP", "selectPrevious", "KP_UP", "selectPrevious", "LEFT", "selectParent", "KP_LEFT", "selectParent", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "ENTER", RuntimeModeler.RETURN, "ctrl ENTER", RuntimeModeler.RETURN, "SPACE", RuntimeModeler.RETURN}, "PopupMenu.selectedWindowInputMapBindings.RightToLeft", new Object[]{"LEFT", "selectChild", "KP_LEFT", "selectChild", "RIGHT", "selectParent", "KP_RIGHT", "selectParent"}, "PopupMenu.consumeEventOnClose", Boolean.FALSE, "OptionPane.font", swingLazyValue, "OptionPane.background", color, "OptionPane.foreground", color6, "OptionPane.messageForeground", color6, "OptionPane.border", swingLazyValue26, "OptionPane.messageAreaBorder", swingLazyValue25, "OptionPane.buttonAreaBorder", swingLazyValue27, "OptionPane.minimumSize", dimensionUIResource, "OptionPane.errorIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Error.gif"), "OptionPane.informationIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Inform.gif"), "OptionPane.warningIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Warn.gif"), "OptionPane.questionIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/Question.gif"), "OptionPane.windowBindings", new Object[]{"ESCAPE", "close"}, "OptionPane.errorSound", null, "OptionPane.informationSound", null, "OptionPane.questionSound", null, "OptionPane.warningSound", null, "OptionPane.buttonClickThreshhold", num, "Panel.font", swingLazyValue, "Panel.background", color, "Panel.foreground", color12, "ProgressBar.font", swingLazyValue, "ProgressBar.foreground", color9, "ProgressBar.background", color, "ProgressBar.selectionForeground", color, "ProgressBar.selectionBackground", color9, "ProgressBar.border", swingLazyValue28, "ProgressBar.cellLength", new Integer(1), "ProgressBar.cellSpacing", num5, "ProgressBar.repaintInterval", new Integer(50), "ProgressBar.cycleTime", new Integer(3000), "ProgressBar.horizontalSize", new DimensionUIResource(146, 12), "ProgressBar.verticalSize", new DimensionUIResource(12, 146), "Separator.shadow", color5, "Separator.highlight", color4, "Separator.background", color4, "Separator.foreground", color5, "ScrollBar.background", colorUIResource7, "ScrollBar.foreground", color, "ScrollBar.track", uIDefaults.get("scrollbar"), "ScrollBar.trackHighlight", color2, "ScrollBar.thumb", color, "ScrollBar.thumbHighlight", color4, "ScrollBar.thumbDarkShadow", color2, "ScrollBar.thumbShadow", color5, "ScrollBar.border", null, "ScrollBar.minimumThumbSize", dimensionUIResource2, "ScrollBar.maximumThumbSize", dimensionUIResource3, "ScrollBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "DOWN", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_DOWN", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "PAGE_DOWN", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "UP", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_UP", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "PAGE_UP", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "HOME", BasicSliderUI.Actions.MIN_SCROLL_INCREMENT, "END", BasicSliderUI.Actions.MAX_SCROLL_INCREMENT}), "ScrollBar.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "LEFT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT}), "ScrollBar.width", new Integer(16), "ScrollPane.font", swingLazyValue, "ScrollPane.background", color, "ScrollPane.foreground", color6, "ScrollPane.border", swingLazyValue31, "ScrollPane.viewportBorder", null, "ScrollPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "unitScrollRight", "KP_RIGHT", "unitScrollRight", "DOWN", "unitScrollDown", "KP_DOWN", "unitScrollDown", "LEFT", "unitScrollLeft", "KP_LEFT", "unitScrollLeft", "UP", "unitScrollUp", "KP_UP", "unitScrollUp", "PAGE_UP", "scrollUp", "PAGE_DOWN", "scrollDown", "ctrl PAGE_UP", "scrollLeft", "ctrl PAGE_DOWN", "scrollRight", "ctrl HOME", "scrollHome", "ctrl END", "scrollEnd"}), "ScrollPane.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"ctrl PAGE_UP", "scrollRight", "ctrl PAGE_DOWN", "scrollLeft"}), "Viewport.font", swingLazyValue, "Viewport.background", color, "Viewport.foreground", color12, "Slider.font", swingLazyValue, "Slider.foreground", color, "Slider.background", color, "Slider.highlight", color4, "Slider.tickColor", Color.black, "Slider.shadow", color5, "Slider.focus", color2, "Slider.border", null, "Slider.horizontalSize", new Dimension(200, 21), "Slider.verticalSize", new Dimension(21, 200), "Slider.minimumHorizontalSize", new Dimension(36, 21), "Slider.minimumVerticalSize", new Dimension(21, 36), "Slider.focusInsets", insetsUIResource2, "Slider.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "DOWN", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_DOWN", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "PAGE_DOWN", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "UP", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_UP", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "PAGE_UP", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "HOME", BasicSliderUI.Actions.MIN_SCROLL_INCREMENT, "END", BasicSliderUI.Actions.MAX_SCROLL_INCREMENT}), "Slider.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "LEFT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT}), "Slider.onlyLeftMouseButtonDrag", Boolean.TRUE, "Spinner.font", swingLazyValue4, "Spinner.background", color, "Spinner.foreground", color, "Spinner.border", swingLazyValue31, "Spinner.arrowButtonBorder", null, "Spinner.arrowButtonInsets", null, "Spinner.arrowButtonSize", new Dimension(16, 5), "Spinner.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement"}), "Spinner.editorBorderPainted", Boolean.FALSE, "Spinner.editorAlignment", 11, "SplitPane.background", color, "SplitPane.highlight", color4, "SplitPane.shadow", color5, "SplitPane.darkShadow", color2, "SplitPane.border", swingLazyValue29, "SplitPane.dividerSize", new Integer(7), "SplitPaneDivider.border", swingLazyValue30, "SplitPaneDivider.draggingColor", colorUIResource6, "SplitPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "negativeIncrement", "DOWN", "positiveIncrement", "LEFT", "negativeIncrement", "RIGHT", "positiveIncrement", "KP_UP", "negativeIncrement", "KP_DOWN", "positiveIncrement", "KP_LEFT", "negativeIncrement", "KP_RIGHT", "positiveIncrement", "HOME", "selectMin", "END", "selectMax", "F8", "startResize", "F6", "toggleFocus", "ctrl TAB", "focusOutForward", "ctrl shift TAB", "focusOutBackward"}), "TabbedPane.font", swingLazyValue, "TabbedPane.background", color, "TabbedPane.foreground", color6, "TabbedPane.highlight", color4, "TabbedPane.light", color3, "TabbedPane.shadow", color5, "TabbedPane.darkShadow", color2, "TabbedPane.selected", null, "TabbedPane.focus", color6, "TabbedPane.textIconGap", num7, "TabbedPane.tabsOverlapBorder", Boolean.FALSE, "TabbedPane.selectionFollowsFocus", Boolean.TRUE, "TabbedPane.labelShift", 1, "TabbedPane.selectedLabelShift", -1, "TabbedPane.tabInsets", insetsUIResource4, "TabbedPane.selectedTabPadInsets", insetsUIResource5, "TabbedPane.tabAreaInsets", insetsUIResource6, "TabbedPane.contentBorderInsets", insetsUIResource7, "TabbedPane.tabRunOverlay", new Integer(2), "TabbedPane.tabsOpaque", Boolean.TRUE, "TabbedPane.contentOpaque", Boolean.TRUE, "TabbedPane.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "navigateRight", "KP_RIGHT", "navigateRight", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "ctrl DOWN", "requestFocusForVisibleComponent", "ctrl KP_DOWN", "requestFocusForVisibleComponent"}), "TabbedPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl PAGE_DOWN", "navigatePageDown", "ctrl PAGE_UP", "navigatePageUp", "ctrl UP", "requestFocus", "ctrl KP_UP", "requestFocus"}), "Table.font", swingLazyValue, "Table.foreground", color6, "Table.background", color13, "Table.selectionForeground", color10, "Table.selectionBackground", color9, "Table.dropLineColor", color5, "Table.dropLineShortColor", colorUIResource2, "Table.gridColor", colorUIResource5, "Table.focusCellBackground", color13, "Table.focusCellForeground", color6, "Table.focusCellHighlightBorder", swingLazyValue11, "Table.scrollPaneBorder", swingLazyValue8, "Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo", "F8", "focusHeader"}), "Table.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "selectPreviousColumn", "KP_RIGHT", "selectPreviousColumn", "shift RIGHT", "selectPreviousColumnExtendSelection", "shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl RIGHT", "selectPreviousColumnChangeLead", "ctrl KP_RIGHT", "selectPreviousColumnChangeLead", "LEFT", "selectNextColumn", "KP_LEFT", "selectNextColumn", "shift LEFT", "selectNextColumnExtendSelection", "shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl shift LEFT", "selectNextColumnExtendSelection", "ctrl shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl LEFT", "selectNextColumnChangeLead", "ctrl KP_LEFT", "selectNextColumnChangeLead", "ctrl PAGE_UP", "scrollRightChangeSelection", "ctrl PAGE_DOWN", "scrollLeftChangeSelection", "ctrl shift PAGE_UP", "scrollRightExtendSelection", "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection"}), "Table.ascendingSortIcon", new SwingLazyValue("sun.swing.icon.SortArrowIcon", null, new Object[]{Boolean.TRUE, "Table.sortIconColor"}), "Table.descendingSortIcon", new SwingLazyValue("sun.swing.icon.SortArrowIcon", null, new Object[]{Boolean.FALSE, "Table.sortIconColor"}), "Table.sortIconColor", color5, "TableHeader.font", swingLazyValue, "TableHeader.foreground", color6, "TableHeader.background", color, "TableHeader.cellBorder", swingLazyValue12, "TableHeader.focusCellBackground", uIDefaults.getColor("text"), "TableHeader.focusCellForeground", null, "TableHeader.focusCellBorder", null, "TableHeader.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", BasicTableHeaderUI.Actions.TOGGLE_SORT_ORDER, "LEFT", BasicTableHeaderUI.Actions.SELECT_COLUMN_TO_LEFT, "KP_LEFT", BasicTableHeaderUI.Actions.SELECT_COLUMN_TO_LEFT, "RIGHT", BasicTableHeaderUI.Actions.SELECT_COLUMN_TO_RIGHT, "KP_RIGHT", BasicTableHeaderUI.Actions.SELECT_COLUMN_TO_RIGHT, "alt LEFT", BasicTableHeaderUI.Actions.MOVE_COLUMN_LEFT, "alt KP_LEFT", BasicTableHeaderUI.Actions.MOVE_COLUMN_LEFT, "alt RIGHT", BasicTableHeaderUI.Actions.MOVE_COLUMN_RIGHT, "alt KP_RIGHT", BasicTableHeaderUI.Actions.MOVE_COLUMN_RIGHT, "alt shift LEFT", BasicTableHeaderUI.Actions.RESIZE_LEFT, "alt shift KP_LEFT", BasicTableHeaderUI.Actions.RESIZE_LEFT, "alt shift RIGHT", BasicTableHeaderUI.Actions.RESIZE_RIGHT, "alt shift KP_RIGHT", BasicTableHeaderUI.Actions.RESIZE_RIGHT, "ESCAPE", BasicTableHeaderUI.Actions.FOCUS_TABLE}), "TextField.font", swingLazyValue3, "TextField.background", color13, "TextField.foreground", color12, "TextField.shadow", color5, "TextField.darkShadow", color2, "TextField.light", color3, "TextField.highlight", color4, "TextField.inactiveForeground", color11, "TextField.inactiveBackground", color, "TextField.selectionBackground", color9, "TextField.selectionForeground", color10, "TextField.caretForeground", color12, "TextField.caretBlinkRate", num, "TextField.border", swingLazyValue31, "TextField.margin", insetsUIResource, "FormattedTextField.font", swingLazyValue3, "FormattedTextField.background", color13, "FormattedTextField.foreground", color12, "FormattedTextField.inactiveForeground", color11, "FormattedTextField.inactiveBackground", color, "FormattedTextField.selectionBackground", color9, "FormattedTextField.selectionForeground", color10, "FormattedTextField.caretForeground", color12, "FormattedTextField.caretBlinkRate", num, "FormattedTextField.border", swingLazyValue31, "FormattedTextField.margin", insetsUIResource, "FormattedTextField.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.previousWordAction, "ctrl KP_LEFT", DefaultEditorKit.previousWordAction, "ctrl RIGHT", DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl A", DefaultEditorKit.selectAllAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "ENTER", JTextField.notifyAction, "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation", "ESCAPE", "reset-field-edit", "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement"}), "PasswordField.font", swingLazyValue4, "PasswordField.background", color13, "PasswordField.foreground", color12, "PasswordField.inactiveForeground", color11, "PasswordField.inactiveBackground", color, "PasswordField.selectionBackground", color9, "PasswordField.selectionForeground", color10, "PasswordField.caretForeground", color12, "PasswordField.caretBlinkRate", num, "PasswordField.border", swingLazyValue31, "PasswordField.margin", insetsUIResource, "PasswordField.echoChar", '*', "TextArea.font", swingLazyValue4, "TextArea.background", color13, "TextArea.foreground", color12, "TextArea.inactiveForeground", color11, "TextArea.selectionBackground", color9, "TextArea.selectionForeground", color10, "TextArea.caretForeground", color12, "TextArea.caretBlinkRate", num, "TextArea.border", swingLazyValue6, "TextArea.margin", insetsUIResource, "TextPane.font", swingLazyValue2, "TextPane.background", colorUIResource3, "TextPane.foreground", color12, "TextPane.selectionBackground", color9, "TextPane.selectionForeground", color10, "TextPane.caretForeground", color12, "TextPane.caretBlinkRate", num, "TextPane.inactiveForeground", color11, "TextPane.border", swingLazyValue6, "TextPane.margin", insetsUIResource3, "EditorPane.font", swingLazyValue2, "EditorPane.background", colorUIResource3, "EditorPane.foreground", color12, "EditorPane.selectionBackground", color9, "EditorPane.selectionForeground", color10, "EditorPane.caretForeground", color12, "EditorPane.caretBlinkRate", num, "EditorPane.inactiveForeground", color11, "EditorPane.border", swingLazyValue6, "EditorPane.margin", insetsUIResource3, "html.pendingImage", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/image-delayed.png"), "html.missingImage", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/image-failed.png"), "TitledBorder.font", swingLazyValue, "TitledBorder.titleColor", color6, "TitledBorder.border", swingLazyValue7, "ToolBar.font", swingLazyValue, "ToolBar.background", color, "ToolBar.foreground", color6, "ToolBar.shadow", color5, "ToolBar.darkShadow", color2, "ToolBar.light", color3, "ToolBar.highlight", color4, "ToolBar.dockingBackground", color, "ToolBar.dockingForeground", colorUIResource, "ToolBar.floatingBackground", color, "ToolBar.floatingForeground", colorUIResource6, "ToolBar.border", swingLazyValue7, "ToolBar.separatorSize", dimensionUIResource4, "ToolBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight"}), "ToolTip.font", swingLazyValue3, "ToolTip.background", uIDefaults.get("info"), "ToolTip.foreground", uIDefaults.get("infoText"), "ToolTip.border", swingLazyValue10, "ToolTipManager.enableToolTipMode", "allWindows", "Tree.paintLines", Boolean.TRUE, "Tree.lineTypeDashed", Boolean.FALSE, "Tree.font", swingLazyValue, "Tree.background", color13, "Tree.foreground", color12, "Tree.hash", colorUIResource5, "Tree.textForeground", color12, "Tree.textBackground", uIDefaults.get("text"), "Tree.selectionForeground", color10, "Tree.selectionBackground", color9, "Tree.selectionBorderColor", colorUIResource2, "Tree.dropLineColor", color5, "Tree.editorBorder", swingLazyValue10, "Tree.leftChildIndent", new Integer(7), "Tree.rightChildIndent", new Integer(13), "Tree.rowHeight", new Integer(16), "Tree.scrollsOnExpand", Boolean.TRUE, "Tree.openIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/TreeOpen.gif"), "Tree.closedIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/TreeClosed.gif"), "Tree.leafIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/TreeLeaf.gif"), "Tree.expandedIcon", null, "Tree.collapsedIcon", null, "Tree.changeSelectionWithFocus", Boolean.TRUE, "Tree.drawsFocusBorderAroundIcon", Boolean.FALSE, "Tree.timeFactor", l2, "Tree.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPrevious", "KP_UP", "selectPrevious", "shift UP", "selectPreviousExtendSelection", "shift KP_UP", "selectPreviousExtendSelection", "ctrl shift UP", "selectPreviousExtendSelection", "ctrl shift KP_UP", "selectPreviousExtendSelection", "ctrl UP", "selectPreviousChangeLead", "ctrl KP_UP", "selectPreviousChangeLead", "DOWN", "selectNext", "KP_DOWN", "selectNext", "shift DOWN", "selectNextExtendSelection", "shift KP_DOWN", "selectNextExtendSelection", "ctrl shift DOWN", "selectNextExtendSelection", "ctrl shift KP_DOWN", "selectNextExtendSelection", "ctrl DOWN", "selectNextChangeLead", "ctrl KP_DOWN", "selectNextChangeLead", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "LEFT", "selectParent", "KP_LEFT", "selectParent", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "HOME", "selectFirst", "shift HOME", "selectFirstExtendSelection", "ctrl shift HOME", "selectFirstExtendSelection", "ctrl HOME", "selectFirstChangeLead", "END", "selectLast", "shift END", "selectLastExtendSelection", "ctrl shift END", "selectLastExtendSelection", "ctrl END", "selectLastChangeLead", "F2", "startEditing", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ctrl LEFT", "scrollLeft", "ctrl KP_LEFT", "scrollLeft", "ctrl RIGHT", "scrollRight", "ctrl KP_RIGHT", "scrollRight", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"}), "Tree.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "selectParent", "KP_RIGHT", "selectParent", "LEFT", "selectChild", "KP_LEFT", "selectChild"}), "Tree.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", "cancel"}), "RootPane.ancestorInputMap", new UIDefaults.LazyInputMap(objArr2), "RootPane.defaultButtonWindowKeyBindings", new Object[]{"ENTER", BasicRootPaneUI.Actions.PRESS, "released ENTER", BasicRootPaneUI.Actions.RELEASE, "ctrl ENTER", BasicRootPaneUI.Actions.PRESS, "ctrl released ENTER", BasicRootPaneUI.Actions.RELEASE}});
    }

    static int getFocusAcceleratorKeyMask() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof SunToolkit) {
            return ((SunToolkit) defaultToolkit).getFocusAcceleratorKeyMask();
        }
        return 8;
    }

    static Object getUIOfType(ComponentUI componentUI, Class cls) {
        if (cls.isInstance(componentUI)) {
            return componentUI;
        }
        return null;
    }

    protected ActionMap getAudioActionMap() {
        ActionMap actionMapUIResource = (ActionMap) UIManager.get("AuditoryCues.actionMap");
        if (actionMapUIResource == null) {
            Object[] objArr = (Object[]) UIManager.get("AuditoryCues.cueList");
            if (objArr != null) {
                actionMapUIResource = new ActionMapUIResource();
                for (int length = objArr.length - 1; length >= 0; length--) {
                    actionMapUIResource.put(objArr[length], createAudioAction(objArr[length]));
                }
            }
            UIManager.getLookAndFeelDefaults().put("AuditoryCues.actionMap", actionMapUIResource);
        }
        return actionMapUIResource;
    }

    protected Action createAudioAction(Object obj) {
        if (obj != null) {
            return new AudioAction((String) obj, (String) UIManager.get(obj));
        }
        return null;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicLookAndFeel$AudioAction.class */
    private class AudioAction extends AbstractAction implements LineListener {
        private String audioResource;
        private byte[] audioBuffer;

        public AudioAction(String str, String str2) {
            super(str);
            this.audioResource = str2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.audioBuffer == null) {
                this.audioBuffer = BasicLookAndFeel.this.loadAudioData(this.audioResource);
            }
            if (this.audioBuffer != null) {
                cancelCurrentSound(null);
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(this.audioBuffer));
                    Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioInputStream.getFormat()));
                    clip.open(audioInputStream);
                    clip.addLineListener(this);
                    synchronized (BasicLookAndFeel.this.audioLock) {
                        BasicLookAndFeel.this.clipPlaying = clip;
                    }
                    clip.start();
                } catch (Exception e2) {
                }
            }
        }

        @Override // javax.sound.sampled.LineListener
        public void update(LineEvent lineEvent) {
            if (lineEvent.getType() == LineEvent.Type.STOP) {
                cancelCurrentSound((Clip) lineEvent.getLine());
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001b A[Catch: all -> 0x0031, TryCatch #0 {, blocks: (B:6:0x0010, B:10:0x002d, B:8:0x001b), top: B:21:0x0010 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void cancelCurrentSound(javax.sound.sampled.Clip r4) {
            /*
                r3 = this;
                r0 = 0
                r5 = r0
                r0 = r3
                javax.swing.plaf.basic.BasicLookAndFeel r0 = javax.swing.plaf.basic.BasicLookAndFeel.this
                java.lang.Object r0 = javax.swing.plaf.basic.BasicLookAndFeel.access$100(r0)
                r1 = r0
                r6 = r1
                monitor-enter(r0)
                r0 = r4
                if (r0 == 0) goto L1b
                r0 = r4
                r1 = r3
                javax.swing.plaf.basic.BasicLookAndFeel r1 = javax.swing.plaf.basic.BasicLookAndFeel.this     // Catch: java.lang.Throwable -> L31
                javax.sound.sampled.Clip r1 = javax.swing.plaf.basic.BasicLookAndFeel.access$200(r1)     // Catch: java.lang.Throwable -> L31
                if (r0 != r1) goto L2c
            L1b:
                r0 = r3
                javax.swing.plaf.basic.BasicLookAndFeel r0 = javax.swing.plaf.basic.BasicLookAndFeel.this     // Catch: java.lang.Throwable -> L31
                javax.sound.sampled.Clip r0 = javax.swing.plaf.basic.BasicLookAndFeel.access$200(r0)     // Catch: java.lang.Throwable -> L31
                r5 = r0
                r0 = r3
                javax.swing.plaf.basic.BasicLookAndFeel r0 = javax.swing.plaf.basic.BasicLookAndFeel.this     // Catch: java.lang.Throwable -> L31
                r1 = 0
                javax.sound.sampled.Clip r0 = javax.swing.plaf.basic.BasicLookAndFeel.access$202(r0, r1)     // Catch: java.lang.Throwable -> L31
            L2c:
                r0 = r6
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L31
                goto L38
            L31:
                r7 = move-exception
                r0 = r6
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L31
                r0 = r7
                throw r0
            L38:
                r0 = r5
                if (r0 == 0) goto L49
                r0 = r5
                r1 = r3
                r0.removeLineListener(r1)
                r0 = r5
                r0.close()
            L49:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.basic.BasicLookAndFeel.AudioAction.cancelCurrentSound(javax.sound.sampled.Clip):void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] loadAudioData(final String str) {
        if (str == null) {
            return null;
        }
        byte[] bArr = (byte[]) AccessController.doPrivileged(new PrivilegedAction<byte[]>() { // from class: javax.swing.plaf.basic.BasicLookAndFeel.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public byte[] run2() {
                try {
                    InputStream resourceAsStream = BasicLookAndFeel.this.getClass().getResourceAsStream(str);
                    if (resourceAsStream == null) {
                        return null;
                    }
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(resourceAsStream);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                    byte[] bArr2 = new byte[1024];
                    while (true) {
                        int i2 = bufferedInputStream.read(bArr2);
                        if (i2 > 0) {
                            byteArrayOutputStream.write(bArr2, 0, i2);
                        } else {
                            bufferedInputStream.close();
                            byteArrayOutputStream.flush();
                            return byteArrayOutputStream.toByteArray();
                        }
                    }
                } catch (IOException e2) {
                    System.err.println(e2.toString());
                    return null;
                }
            }
        });
        if (bArr == null) {
            System.err.println(getClass().getName() + "/" + str + " not found.");
            return null;
        }
        if (bArr.length == 0) {
            System.err.println("warning: " + str + " is zero-length");
            return null;
        }
        return bArr;
    }

    protected void playSound(Action action) {
        Object[] objArr;
        if (action != null && (objArr = (Object[]) UIManager.get("AuditoryCues.playList")) != null) {
            HashSet hashSet = new HashSet();
            for (Object obj : objArr) {
                hashSet.add(obj);
            }
            String str = (String) action.getValue("Name");
            if (hashSet.contains(str)) {
                action.actionPerformed(new ActionEvent(this, 1001, str));
            }
        }
    }

    static void installAudioActionMap(ActionMap actionMap) {
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        if (lookAndFeel instanceof BasicLookAndFeel) {
            actionMap.setParent(((BasicLookAndFeel) lookAndFeel).getAudioActionMap());
        }
    }

    static void playSound(JComponent jComponent, Object obj) {
        ActionMap actionMap;
        Action action;
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        if ((lookAndFeel instanceof BasicLookAndFeel) && (actionMap = jComponent.getActionMap()) != null && (action = actionMap.get(obj)) != null) {
            ((BasicLookAndFeel) lookAndFeel).playSound(action);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicLookAndFeel$AWTEventHelper.class */
    class AWTEventHelper implements AWTEventListener, PrivilegedAction<Object> {
        AWTEventHelper() {
            AccessController.doPrivileged(this);
        }

        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Object run2() {
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            if (BasicLookAndFeel.this.invocator == null) {
                defaultToolkit.addAWTEventListener(this, 16L);
                return null;
            }
            defaultToolkit.removeAWTEventListener(BasicLookAndFeel.this.invocator);
            return null;
        }

        @Override // java.awt.event.AWTEventListener
        public void eventDispatched(AWTEvent aWTEvent) {
            int id = aWTEvent.getID();
            if ((id & 16) != 0) {
                MouseEvent mouseEvent = (MouseEvent) aWTEvent;
                if (mouseEvent.isPopupTrigger()) {
                    MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
                    if (selectedPath != null && selectedPath.length != 0) {
                        return;
                    }
                    Object source = mouseEvent.getSource();
                    Component component = null;
                    if (source instanceof JComponent) {
                        component = (JComponent) source;
                    } else if (source instanceof BasicSplitPaneDivider) {
                        component = (JComponent) ((BasicSplitPaneDivider) source).getParent();
                    }
                    if (component != null && component.getComponentPopupMenu() != null) {
                        Point popupLocation = component.getPopupLocation(mouseEvent);
                        if (popupLocation == null) {
                            popupLocation = SwingUtilities.convertPoint((Component) source, mouseEvent.getPoint(), component);
                        }
                        component.getComponentPopupMenu().show(component, popupLocation.f12370x, popupLocation.f12371y);
                        mouseEvent.consume();
                    }
                }
            }
            if (id == 501) {
                Object source2 = aWTEvent.getSource();
                if (!(source2 instanceof Component)) {
                    return;
                }
                Component component2 = (Component) source2;
                if (component2 != null) {
                    Component parent = component2;
                    while (true) {
                        Component component3 = parent;
                        if (component3 != null && !(component3 instanceof Window)) {
                            if (component3 instanceof JInternalFrame) {
                                try {
                                    ((JInternalFrame) component3).setSelected(true);
                                } catch (PropertyVetoException e2) {
                                }
                            }
                            parent = component3.getParent();
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }
}
