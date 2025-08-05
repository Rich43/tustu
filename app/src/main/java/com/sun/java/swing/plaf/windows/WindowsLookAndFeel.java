package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.WindowsIconFactory;
import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import com.sun.java.swing.plaf.windows.XPStyle;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.security.AccessController;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.LookAndFeel;
import javax.swing.MenuSelectionManager;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.text.DefaultEditorKit;
import org.slf4j.Marker;
import sun.awt.OSInfo;
import sun.awt.SunToolkit;
import sun.awt.shell.ShellFolder;
import sun.awt.windows.WToolkit;
import sun.font.FontUtilities;
import sun.security.action.GetPropertyAction;
import sun.swing.DefaultLayoutStyle;
import sun.swing.FilePane;
import sun.swing.ImageIconUIResource;
import sun.swing.StringUIClientPropertyKey;
import sun.swing.SwingLazyValue;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel.class */
public class WindowsLookAndFeel extends BasicLookAndFeel {
    static final Object HI_RES_DISABLED_ICON_CLIENT_KEY;
    private boolean updatePending = false;
    private boolean useSystemFontSettings = true;
    private boolean useSystemFontSizeSettings;
    private DesktopProperty themeActive;
    private DesktopProperty dllName;
    private DesktopProperty colorName;
    private DesktopProperty sizeName;
    private DesktopProperty aaSettings;
    private transient LayoutStyle style;
    private int baseUnitX;
    private int baseUnitY;
    private static boolean isMnemonicHidden;
    private static boolean isClassicWindows;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WindowsLookAndFeel.class.desiredAssertionStatus();
        HI_RES_DISABLED_ICON_CLIENT_KEY = new StringUIClientPropertyKey("WindowsLookAndFeel.generateHiResDisabledIcon");
        isMnemonicHidden = true;
        isClassicWindows = false;
    }

    @Override // javax.swing.LookAndFeel
    public String getName() {
        return "Windows";
    }

    @Override // javax.swing.LookAndFeel
    public String getDescription() {
        return "The Microsoft Windows Look and Feel";
    }

    @Override // javax.swing.LookAndFeel
    public String getID() {
        return "Windows";
    }

    @Override // javax.swing.LookAndFeel
    public boolean isNativeLookAndFeel() {
        return OSInfo.getOSType() == OSInfo.OSType.WINDOWS;
    }

    @Override // javax.swing.LookAndFeel
    public boolean isSupportedLookAndFeel() {
        return isNativeLookAndFeel();
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public void initialize() {
        super.initialize();
        if (OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_95) <= 0) {
            isClassicWindows = true;
        } else {
            isClassicWindows = false;
            XPStyle.invalidateStyle();
        }
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("swing.useSystemFontSettings"));
        this.useSystemFontSettings = str == null || Boolean.valueOf(str).booleanValue();
        if (this.useSystemFontSettings) {
            Object obj = UIManager.get("Application.useSystemFontSettings");
            this.useSystemFontSettings = obj == null || Boolean.TRUE.equals(obj);
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(WindowsRootPaneUI.altProcessor);
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel
    protected void initClassDefaults(UIDefaults uIDefaults) {
        super.initClassDefaults(uIDefaults);
        uIDefaults.putDefaults(new Object[]{"ButtonUI", "com.sun.java.swing.plaf.windows.WindowsButtonUI", "CheckBoxUI", "com.sun.java.swing.plaf.windows.WindowsCheckBoxUI", "CheckBoxMenuItemUI", "com.sun.java.swing.plaf.windows.WindowsCheckBoxMenuItemUI", "LabelUI", "com.sun.java.swing.plaf.windows.WindowsLabelUI", "RadioButtonUI", "com.sun.java.swing.plaf.windows.WindowsRadioButtonUI", "RadioButtonMenuItemUI", "com.sun.java.swing.plaf.windows.WindowsRadioButtonMenuItemUI", "ToggleButtonUI", "com.sun.java.swing.plaf.windows.WindowsToggleButtonUI", "ProgressBarUI", "com.sun.java.swing.plaf.windows.WindowsProgressBarUI", "SliderUI", "com.sun.java.swing.plaf.windows.WindowsSliderUI", "SeparatorUI", "com.sun.java.swing.plaf.windows.WindowsSeparatorUI", "SplitPaneUI", "com.sun.java.swing.plaf.windows.WindowsSplitPaneUI", "SpinnerUI", "com.sun.java.swing.plaf.windows.WindowsSpinnerUI", "TabbedPaneUI", "com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI", "TextAreaUI", "com.sun.java.swing.plaf.windows.WindowsTextAreaUI", "TextFieldUI", "com.sun.java.swing.plaf.windows.WindowsTextFieldUI", "PasswordFieldUI", "com.sun.java.swing.plaf.windows.WindowsPasswordFieldUI", "TextPaneUI", "com.sun.java.swing.plaf.windows.WindowsTextPaneUI", "EditorPaneUI", "com.sun.java.swing.plaf.windows.WindowsEditorPaneUI", "TreeUI", "com.sun.java.swing.plaf.windows.WindowsTreeUI", "ToolBarUI", "com.sun.java.swing.plaf.windows.WindowsToolBarUI", "ToolBarSeparatorUI", "com.sun.java.swing.plaf.windows.WindowsToolBarSeparatorUI", "ComboBoxUI", "com.sun.java.swing.plaf.windows.WindowsComboBoxUI", "TableHeaderUI", "com.sun.java.swing.plaf.windows.WindowsTableHeaderUI", "InternalFrameUI", "com.sun.java.swing.plaf.windows.WindowsInternalFrameUI", "DesktopPaneUI", "com.sun.java.swing.plaf.windows.WindowsDesktopPaneUI", "DesktopIconUI", "com.sun.java.swing.plaf.windows.WindowsDesktopIconUI", "FileChooserUI", "com.sun.java.swing.plaf.windows.WindowsFileChooserUI", "MenuUI", "com.sun.java.swing.plaf.windows.WindowsMenuUI", "MenuItemUI", "com.sun.java.swing.plaf.windows.WindowsMenuItemUI", "MenuBarUI", "com.sun.java.swing.plaf.windows.WindowsMenuBarUI", "PopupMenuUI", "com.sun.java.swing.plaf.windows.WindowsPopupMenuUI", "PopupMenuSeparatorUI", "com.sun.java.swing.plaf.windows.WindowsPopupMenuSeparatorUI", "ScrollBarUI", "com.sun.java.swing.plaf.windows.WindowsScrollBarUI", "RootPaneUI", "com.sun.java.swing.plaf.windows.WindowsRootPaneUI"});
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel
    protected void initSystemColorDefaults(UIDefaults uIDefaults) {
        loadSystemColors(uIDefaults, new String[]{"desktop", "#005C5C", "activeCaption", "#000080", "activeCaptionText", "#FFFFFF", "activeCaptionBorder", "#C0C0C0", "inactiveCaption", "#808080", "inactiveCaptionText", "#C0C0C0", "inactiveCaptionBorder", "#C0C0C0", "window", "#FFFFFF", "windowBorder", "#000000", "windowText", "#000000", "menu", "#C0C0C0", "menuPressedItemB", "#000080", "menuPressedItemF", "#FFFFFF", "menuText", "#000000", "text", "#C0C0C0", "textText", "#000000", "textHighlight", "#000080", "textHighlightText", "#FFFFFF", "textInactiveText", "#808080", "control", "#C0C0C0", "controlText", "#000000", "controlHighlight", "#C0C0C0", "controlLtHighlight", "#FFFFFF", "controlShadow", "#808080", "controlDkShadow", "#000000", "scrollbar", "#E0E0E0", "info", "#FFFFE1", "infoText", "#000000"}, isNativeLookAndFeel());
    }

    private void initResourceBundle(UIDefaults uIDefaults) {
        uIDefaults.addResourceBundle("com.sun.java.swing.plaf.windows.resources.windows");
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel
    protected void initComponentDefaults(UIDefaults uIDefaults) {
        super.initComponentDefaults(uIDefaults);
        initResourceBundle(uIDefaults);
        Object swingLazyValue = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[]{Font.DIALOG, 0, 12});
        Object swingLazyValue2 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[]{"SansSerif", 0, 12});
        Object swingLazyValue3 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[]{"Monospaced", 0, 12});
        Object swingLazyValue4 = new SwingLazyValue("javax.swing.plaf.FontUIResource", null, new Object[]{Font.DIALOG, 1, 12});
        Object colorUIResource = new ColorUIResource(Color.red);
        Object colorUIResource2 = new ColorUIResource(Color.black);
        Object colorUIResource3 = new ColorUIResource(Color.white);
        Object colorUIResource4 = new ColorUIResource(Color.gray);
        Object colorUIResource5 = new ColorUIResource(Color.darkGray);
        isClassicWindows = OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_95) <= 0;
        Object objCreateExpandedIcon = WindowsTreeUI.ExpandedIcon.createExpandedIcon();
        Object objCreateCollapsedIcon = WindowsTreeUI.CollapsedIcon.createCollapsedIcon();
        Object lazyInputMap = new UIDefaults.LazyInputMap(new Object[]{"control C", DefaultEditorKit.copyAction, "control V", DefaultEditorKit.pasteAction, "control X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "control A", DefaultEditorKit.selectAllAction, "control BACK_SLASH", "unselect", "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "control LEFT", DefaultEditorKit.previousWordAction, "control RIGHT", DefaultEditorKit.nextWordAction, "control shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "control shift RIGHT", DefaultEditorKit.selectionNextWordAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "ENTER", JTextField.notifyAction, "control shift O", "toggle-componentOrientation"});
        Object lazyInputMap2 = new UIDefaults.LazyInputMap(new Object[]{"control C", DefaultEditorKit.copyAction, "control V", DefaultEditorKit.pasteAction, "control X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "control A", DefaultEditorKit.selectAllAction, "control BACK_SLASH", "unselect", "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "control LEFT", DefaultEditorKit.beginLineAction, "control RIGHT", DefaultEditorKit.endLineAction, "control shift LEFT", DefaultEditorKit.selectionBeginLineAction, "control shift RIGHT", DefaultEditorKit.selectionEndLineAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "ENTER", JTextField.notifyAction, "control shift O", "toggle-componentOrientation"});
        Object lazyInputMap3 = new UIDefaults.LazyInputMap(new Object[]{"control C", DefaultEditorKit.copyAction, "control V", DefaultEditorKit.pasteAction, "control X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "control LEFT", DefaultEditorKit.previousWordAction, "control RIGHT", DefaultEditorKit.nextWordAction, "control shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "control shift RIGHT", DefaultEditorKit.selectionNextWordAction, "control A", DefaultEditorKit.selectAllAction, "control BACK_SLASH", "unselect", "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "control HOME", DefaultEditorKit.beginAction, "control END", DefaultEditorKit.endAction, "control shift HOME", DefaultEditorKit.selectionBeginAction, "control shift END", DefaultEditorKit.selectionEndAction, "UP", DefaultEditorKit.upAction, "DOWN", DefaultEditorKit.downAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "PAGE_UP", DefaultEditorKit.pageUpAction, "PAGE_DOWN", DefaultEditorKit.pageDownAction, "shift PAGE_UP", "selection-page-up", "shift PAGE_DOWN", "selection-page-down", "ctrl shift PAGE_UP", "selection-page-left", "ctrl shift PAGE_DOWN", "selection-page-right", "shift UP", DefaultEditorKit.selectionUpAction, "shift DOWN", DefaultEditorKit.selectionDownAction, "ENTER", DefaultEditorKit.insertBreakAction, "TAB", DefaultEditorKit.insertTabAction, "control T", "next-link-action", "control shift T", "previous-link-action", "control SPACE", "activate-link-action", "control shift O", "toggle-componentOrientation"});
        Object desktopProperty = new DesktopProperty("win.3d.backgroundColor", uIDefaults.get("control"));
        Object desktopProperty2 = new DesktopProperty("win.3d.lightColor", uIDefaults.get("controlHighlight"));
        Object desktopProperty3 = new DesktopProperty("win.3d.highlightColor", uIDefaults.get("controlLtHighlight"));
        Object desktopProperty4 = new DesktopProperty("win.3d.shadowColor", uIDefaults.get("controlShadow"));
        Object desktopProperty5 = new DesktopProperty("win.3d.darkShadowColor", uIDefaults.get("controlDkShadow"));
        Object desktopProperty6 = new DesktopProperty("win.button.textColor", uIDefaults.get("controlText"));
        Object desktopProperty7 = new DesktopProperty("win.menu.backgroundColor", uIDefaults.get("menu"));
        DesktopProperty desktopProperty8 = new DesktopProperty("win.menubar.backgroundColor", uIDefaults.get("menu"));
        Object desktopProperty9 = new DesktopProperty("win.menu.textColor", uIDefaults.get("menuText"));
        Object desktopProperty10 = new DesktopProperty("win.item.highlightColor", uIDefaults.get("textHighlight"));
        Object desktopProperty11 = new DesktopProperty("win.item.highlightTextColor", uIDefaults.get("textHighlightText"));
        Object desktopProperty12 = new DesktopProperty("win.frame.backgroundColor", uIDefaults.get("window"));
        Object desktopProperty13 = new DesktopProperty("win.frame.textColor", uIDefaults.get("windowText"));
        Object desktopProperty14 = new DesktopProperty("win.frame.sizingBorderWidth", 1);
        Object desktopProperty15 = new DesktopProperty("win.frame.captionHeight", 18);
        Object desktopProperty16 = new DesktopProperty("win.frame.captionButtonWidth", 16);
        Object desktopProperty17 = new DesktopProperty("win.frame.captionButtonHeight", 16);
        Object desktopProperty18 = new DesktopProperty("win.text.grayedTextColor", uIDefaults.get("textInactiveText"));
        Object desktopProperty19 = new DesktopProperty("win.scrollbar.backgroundColor", uIDefaults.get("scrollbar"));
        Object focusColorProperty = new FocusColorProperty();
        Object xPColorValue = new XPColorValue(TMSchema.Part.EP_EDIT, null, TMSchema.Prop.FILLCOLOR, desktopProperty12);
        Object windowsFontSizeProperty = swingLazyValue;
        Object windowsFontSizeProperty2 = swingLazyValue3;
        Object windowsFontSizeProperty3 = swingLazyValue;
        Object windowsFontSizeProperty4 = swingLazyValue;
        Object windowsFontSizeProperty5 = swingLazyValue4;
        Object windowsFontSizeProperty6 = swingLazyValue2;
        Object windowsFontSizeProperty7 = windowsFontSizeProperty3;
        Object desktopProperty20 = new DesktopProperty("win.scrollbar.width", 16);
        Object desktopProperty21 = new DesktopProperty("win.menu.height", null);
        Object desktopProperty22 = new DesktopProperty("win.item.hotTrackingOn", true);
        Object desktopProperty23 = new DesktopProperty("win.menu.keyboardCuesOn", Boolean.TRUE);
        if (this.useSystemFontSettings) {
            windowsFontSizeProperty = getDesktopFontValue("win.menu.font", windowsFontSizeProperty);
            windowsFontSizeProperty2 = getDesktopFontValue("win.ansiFixed.font", windowsFontSizeProperty2);
            windowsFontSizeProperty3 = getDesktopFontValue("win.defaultGUI.font", windowsFontSizeProperty3);
            windowsFontSizeProperty4 = getDesktopFontValue("win.messagebox.font", windowsFontSizeProperty4);
            windowsFontSizeProperty5 = getDesktopFontValue("win.frame.captionFont", windowsFontSizeProperty5);
            windowsFontSizeProperty7 = getDesktopFontValue("win.icon.font", windowsFontSizeProperty7);
            windowsFontSizeProperty6 = getDesktopFontValue("win.tooltip.font", windowsFontSizeProperty6);
            uIDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, SwingUtilities2.AATextInfo.getAATextInfo(true));
            this.aaSettings = new FontDesktopProperty(SunToolkit.DESKTOPFONTHINTS);
        }
        if (this.useSystemFontSizeSettings) {
            windowsFontSizeProperty = new WindowsFontSizeProperty("win.menu.font.height", Font.DIALOG, 0, 12);
            windowsFontSizeProperty2 = new WindowsFontSizeProperty("win.ansiFixed.font.height", "Monospaced", 0, 12);
            windowsFontSizeProperty3 = new WindowsFontSizeProperty("win.defaultGUI.font.height", Font.DIALOG, 0, 12);
            windowsFontSizeProperty4 = new WindowsFontSizeProperty("win.messagebox.font.height", Font.DIALOG, 0, 12);
            windowsFontSizeProperty5 = new WindowsFontSizeProperty("win.frame.captionFont.height", Font.DIALOG, 1, 12);
            windowsFontSizeProperty6 = new WindowsFontSizeProperty("win.tooltip.font.height", "SansSerif", 0, 12);
            windowsFontSizeProperty7 = new WindowsFontSizeProperty("win.icon.font.height", Font.DIALOG, 0, 12);
        }
        if (!(this instanceof WindowsClassicLookAndFeel) && OSInfo.getOSType() == OSInfo.OSType.WINDOWS && OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_XP) >= 0 && AccessController.doPrivileged(new GetPropertyAction("swing.noxp")) == null) {
            this.themeActive = new TriggerDesktopProperty(WToolkit.XPSTYLE_THEME_ACTIVE);
            this.dllName = new TriggerDesktopProperty("win.xpstyle.dllName");
            this.colorName = new TriggerDesktopProperty("win.xpstyle.colorName");
            this.sizeName = new TriggerDesktopProperty("win.xpstyle.sizeName");
        }
        uIDefaults.putDefaults(new Object[]{"AuditoryCues.playList", null, "Application.useSystemFontSettings", Boolean.valueOf(this.useSystemFontSettings), "TextField.focusInputMap", lazyInputMap, "PasswordField.focusInputMap", lazyInputMap2, "TextArea.focusInputMap", lazyInputMap3, "TextPane.focusInputMap", lazyInputMap3, "EditorPane.focusInputMap", lazyInputMap3, "Button.font", windowsFontSizeProperty3, "Button.background", desktopProperty, "Button.foreground", desktopProperty6, "Button.shadow", desktopProperty4, "Button.darkShadow", desktopProperty5, "Button.light", desktopProperty2, "Button.highlight", desktopProperty3, "Button.disabledForeground", desktopProperty18, "Button.disabledShadow", desktopProperty3, "Button.focus", focusColorProperty, "Button.dashedRectGapX", new XPValue(3, 5), "Button.dashedRectGapY", new XPValue(3, 4), "Button.dashedRectGapWidth", new XPValue(6, 10), "Button.dashedRectGapHeight", new XPValue(6, 8), "Button.textShiftOffset", new XPValue(0, 1), "Button.showMnemonics", desktopProperty23, "Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "Caret.width", new DesktopProperty("win.caret.width", null), "CheckBox.font", windowsFontSizeProperty3, "CheckBox.interiorBackground", desktopProperty12, "CheckBox.background", desktopProperty, "CheckBox.foreground", desktopProperty13, "CheckBox.shadow", desktopProperty4, "CheckBox.darkShadow", desktopProperty5, "CheckBox.light", desktopProperty2, "CheckBox.highlight", desktopProperty3, "CheckBox.focus", focusColorProperty, "CheckBox.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "CheckBox.totalInsets", new Insets(4, 4, 4, 4), "CheckBoxMenuItem.font", windowsFontSizeProperty, "CheckBoxMenuItem.background", desktopProperty7, "CheckBoxMenuItem.foreground", desktopProperty9, "CheckBoxMenuItem.selectionForeground", desktopProperty11, "CheckBoxMenuItem.selectionBackground", desktopProperty10, "CheckBoxMenuItem.acceleratorForeground", desktopProperty9, "CheckBoxMenuItem.acceleratorSelectionForeground", desktopProperty11, "CheckBoxMenuItem.commandSound", "win.sound.menuCommand", "ComboBox.font", windowsFontSizeProperty3, "ComboBox.background", desktopProperty12, "ComboBox.foreground", desktopProperty13, "ComboBox.buttonBackground", desktopProperty, "ComboBox.buttonShadow", desktopProperty4, "ComboBox.buttonDarkShadow", desktopProperty5, "ComboBox.buttonHighlight", desktopProperty3, "ComboBox.selectionBackground", desktopProperty10, "ComboBox.selectionForeground", desktopProperty11, "ComboBox.editorBorder", new XPValue(new EmptyBorder(1, 2, 1, 1), new EmptyBorder(1, 4, 1, 4)), "ComboBox.disabledBackground", new XPColorValue(TMSchema.Part.CP_COMBOBOX, TMSchema.State.DISABLED, TMSchema.Prop.FILLCOLOR, desktopProperty), "ComboBox.disabledForeground", new XPColorValue(TMSchema.Part.CP_COMBOBOX, TMSchema.State.DISABLED, TMSchema.Prop.TEXTCOLOR, desktopProperty18), "ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "DOWN", "selectNext2", "KP_DOWN", "selectNext2", "UP", "selectPrevious2", "KP_UP", "selectPrevious2", "ENTER", "enterPressed", "F4", "togglePopup", "alt DOWN", "togglePopup", "alt KP_DOWN", "togglePopup", "alt UP", "togglePopup", "alt KP_UP", "togglePopup"}), "Desktop.background", new DesktopProperty("win.desktop.backgroundColor", uIDefaults.get("desktop")), "Desktop.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl F5", "restore", "ctrl F4", "close", "ctrl F7", "move", "ctrl F8", "resize", "RIGHT", JSplitPane.RIGHT, "KP_RIGHT", JSplitPane.RIGHT, "LEFT", JSplitPane.LEFT, "KP_LEFT", JSplitPane.LEFT, "UP", "up", "KP_UP", "up", "DOWN", "down", "KP_DOWN", "down", "ESCAPE", "escape", "ctrl F9", "minimize", "ctrl F10", "maximize", "ctrl F6", "selectNextFrame", "ctrl TAB", "selectNextFrame", "ctrl alt F6", "selectNextFrame", "shift ctrl alt F6", "selectPreviousFrame", "ctrl F12", "navigateNext", "shift ctrl F12", "navigatePrevious"}), "DesktopIcon.width", 160, "EditorPane.font", windowsFontSizeProperty3, "EditorPane.background", desktopProperty12, "EditorPane.foreground", desktopProperty13, "EditorPane.selectionBackground", desktopProperty10, "EditorPane.selectionForeground", desktopProperty11, "EditorPane.caretForeground", desktopProperty13, "EditorPane.inactiveForeground", desktopProperty18, "EditorPane.inactiveBackground", desktopProperty12, "EditorPane.disabledBackground", desktopProperty, "FileChooser.homeFolderIcon", new LazyWindowsIcon(null, "icons/HomeFolder.gif"), "FileChooser.listFont", windowsFontSizeProperty7, "FileChooser.listViewBackground", new XPColorValue(TMSchema.Part.LVP_LISTVIEW, null, TMSchema.Prop.FILLCOLOR, desktopProperty12), "FileChooser.listViewBorder", new XPBorderValue(TMSchema.Part.LVP_LISTVIEW, new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getLoweredBevelBorderUIResource")), "FileChooser.listViewIcon", new LazyWindowsIcon("fileChooserIcon ListView", "icons/ListView.gif"), "FileChooser.listViewWindowsStyle", Boolean.TRUE, "FileChooser.detailsViewIcon", new LazyWindowsIcon("fileChooserIcon DetailsView", "icons/DetailsView.gif"), "FileChooser.viewMenuIcon", new LazyWindowsIcon("fileChooserIcon ViewMenu", "icons/ListView.gif"), "FileChooser.upFolderIcon", new LazyWindowsIcon("fileChooserIcon UpFolder", "icons/UpFolder.gif"), "FileChooser.newFolderIcon", new LazyWindowsIcon("fileChooserIcon NewFolder", "icons/NewFolder.gif"), "FileChooser.useSystemExtensionHiding", Boolean.TRUE, "FileChooser.usesSingleFilePane", Boolean.TRUE, "FileChooser.noPlacesBar", new DesktopProperty("win.comdlg.noPlacesBar", Boolean.FALSE), "FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", FilePane.ACTION_CANCEL, "F2", FilePane.ACTION_EDIT_FILE_NAME, "F5", FilePane.ACTION_REFRESH, "BACK_SPACE", FilePane.ACTION_CHANGE_TO_PARENT_DIRECTORY}), "FileView.directoryIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/Directory.gif"), "FileView.fileIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/File.gif"), "FileView.computerIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/Computer.gif"), "FileView.hardDriveIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/HardDrive.gif"), "FileView.floppyDriveIcon", SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/FloppyDrive.gif"), "FormattedTextField.font", windowsFontSizeProperty3, "InternalFrame.titleFont", windowsFontSizeProperty5, "InternalFrame.titlePaneHeight", desktopProperty15, "InternalFrame.titleButtonWidth", desktopProperty16, "InternalFrame.titleButtonHeight", desktopProperty17, "InternalFrame.titleButtonToolTipsOn", desktopProperty22, "InternalFrame.borderColor", desktopProperty, "InternalFrame.borderShadow", desktopProperty4, "InternalFrame.borderDarkShadow", desktopProperty5, "InternalFrame.borderHighlight", desktopProperty3, "InternalFrame.borderLight", desktopProperty2, "InternalFrame.borderWidth", desktopProperty14, "InternalFrame.minimizeIconBackground", desktopProperty, "InternalFrame.resizeIconHighlight", desktopProperty2, "InternalFrame.resizeIconShadow", desktopProperty4, "InternalFrame.activeBorderColor", new DesktopProperty("win.frame.activeBorderColor", uIDefaults.get("windowBorder")), "InternalFrame.inactiveBorderColor", new DesktopProperty("win.frame.inactiveBorderColor", uIDefaults.get("windowBorder")), "InternalFrame.activeTitleBackground", new DesktopProperty("win.frame.activeCaptionColor", uIDefaults.get("activeCaption")), "InternalFrame.activeTitleGradient", new DesktopProperty("win.frame.activeCaptionGradientColor", uIDefaults.get("activeCaption")), "InternalFrame.activeTitleForeground", new DesktopProperty("win.frame.captionTextColor", uIDefaults.get("activeCaptionText")), "InternalFrame.inactiveTitleBackground", new DesktopProperty("win.frame.inactiveCaptionColor", uIDefaults.get("inactiveCaption")), "InternalFrame.inactiveTitleGradient", new DesktopProperty("win.frame.inactiveCaptionGradientColor", uIDefaults.get("inactiveCaption")), "InternalFrame.inactiveTitleForeground", new DesktopProperty("win.frame.inactiveCaptionTextColor", uIDefaults.get("inactiveCaptionText")), "InternalFrame.maximizeIcon", WindowsIconFactory.createFrameMaximizeIcon(), "InternalFrame.minimizeIcon", WindowsIconFactory.createFrameMinimizeIcon(), "InternalFrame.iconifyIcon", WindowsIconFactory.createFrameIconifyIcon(), "InternalFrame.closeIcon", WindowsIconFactory.createFrameCloseIcon(), "InternalFrame.icon", new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane$ScalableIconUIResource", new Object[]{new Object[]{SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/JavaCup16.png"), SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/JavaCup32.png")}}), "InternalFrame.closeSound", "win.sound.close", "InternalFrame.maximizeSound", "win.sound.maximize", "InternalFrame.minimizeSound", "win.sound.minimize", "InternalFrame.restoreDownSound", "win.sound.restoreDown", "InternalFrame.restoreUpSound", "win.sound.restoreUp", "InternalFrame.windowBindings", new Object[]{"shift ESCAPE", "showSystemMenu", "ctrl SPACE", "showSystemMenu", "ESCAPE", "hideSystemMenu"}, "Label.font", windowsFontSizeProperty3, "Label.background", desktopProperty, "Label.foreground", desktopProperty13, "Label.disabledForeground", desktopProperty18, "Label.disabledShadow", desktopProperty3, "List.font", windowsFontSizeProperty3, "List.background", desktopProperty12, "List.foreground", desktopProperty13, "List.selectionBackground", desktopProperty10, "List.selectionForeground", desktopProperty11, "List.lockToPositionOnScroll", Boolean.TRUE, "List.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "HOME", "selectFirstRow", "shift HOME", "selectFirstRowExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRowChangeLead", "END", "selectLastRow", "shift END", "selectLastRowExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRowChangeLead", "PAGE_UP", "scrollUp", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDown", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"}), "PopupMenu.font", windowsFontSizeProperty, "PopupMenu.background", desktopProperty7, "PopupMenu.foreground", desktopProperty9, "PopupMenu.popupSound", "win.sound.menuPopup", "PopupMenu.consumeEventOnClose", Boolean.TRUE, "Menu.font", windowsFontSizeProperty, "Menu.foreground", desktopProperty9, "Menu.background", desktopProperty7, "Menu.useMenuBarBackgroundForTopLevel", Boolean.TRUE, "Menu.selectionForeground", desktopProperty11, "Menu.selectionBackground", desktopProperty10, "Menu.acceleratorForeground", desktopProperty9, "Menu.acceleratorSelectionForeground", desktopProperty11, "Menu.menuPopupOffsetX", 0, "Menu.menuPopupOffsetY", 0, "Menu.submenuPopupOffsetX", -4, "Menu.submenuPopupOffsetY", -3, "Menu.crossMenuMnemonic", Boolean.FALSE, "Menu.preserveTopLevelSelection", Boolean.TRUE, "MenuBar.font", windowsFontSizeProperty, "MenuBar.background", new XPValue(desktopProperty8, desktopProperty7), "MenuBar.foreground", desktopProperty9, "MenuBar.shadow", desktopProperty4, "MenuBar.highlight", desktopProperty3, "MenuBar.height", desktopProperty21, "MenuBar.rolloverEnabled", desktopProperty22, "MenuBar.windowBindings", new Object[]{"F10", "takeFocus"}, "MenuItem.font", windowsFontSizeProperty, "MenuItem.acceleratorFont", windowsFontSizeProperty, "MenuItem.foreground", desktopProperty9, "MenuItem.background", desktopProperty7, "MenuItem.selectionForeground", desktopProperty11, "MenuItem.selectionBackground", desktopProperty10, "MenuItem.disabledForeground", desktopProperty18, "MenuItem.acceleratorForeground", desktopProperty9, "MenuItem.acceleratorSelectionForeground", desktopProperty11, "MenuItem.acceleratorDelimiter", Marker.ANY_NON_NULL_MARKER, "MenuItem.commandSound", "win.sound.menuCommand", "MenuItem.disabledAreNavigable", Boolean.TRUE, "RadioButton.font", windowsFontSizeProperty3, "RadioButton.interiorBackground", desktopProperty12, "RadioButton.background", desktopProperty, "RadioButton.foreground", desktopProperty13, "RadioButton.shadow", desktopProperty4, "RadioButton.darkShadow", desktopProperty5, "RadioButton.light", desktopProperty2, "RadioButton.highlight", desktopProperty3, "RadioButton.focus", focusColorProperty, "RadioButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "RadioButton.totalInsets", new Insets(4, 4, 4, 4), "RadioButtonMenuItem.font", windowsFontSizeProperty, "RadioButtonMenuItem.foreground", desktopProperty9, "RadioButtonMenuItem.background", desktopProperty7, "RadioButtonMenuItem.selectionForeground", desktopProperty11, "RadioButtonMenuItem.selectionBackground", desktopProperty10, "RadioButtonMenuItem.disabledForeground", desktopProperty18, "RadioButtonMenuItem.acceleratorForeground", desktopProperty9, "RadioButtonMenuItem.acceleratorSelectionForeground", desktopProperty11, "RadioButtonMenuItem.commandSound", "win.sound.menuCommand", "OptionPane.font", windowsFontSizeProperty4, "OptionPane.messageFont", windowsFontSizeProperty4, "OptionPane.buttonFont", windowsFontSizeProperty4, "OptionPane.background", desktopProperty, "OptionPane.foreground", desktopProperty13, "OptionPane.buttonMinimumWidth", new XPDLUValue(50, 50, 3), "OptionPane.messageForeground", desktopProperty6, "OptionPane.errorIcon", new LazyWindowsIcon("optionPaneIcon Error", "icons/Error.gif"), "OptionPane.informationIcon", new LazyWindowsIcon("optionPaneIcon Information", "icons/Inform.gif"), "OptionPane.questionIcon", new LazyWindowsIcon("optionPaneIcon Question", "icons/Question.gif"), "OptionPane.warningIcon", new LazyWindowsIcon("optionPaneIcon Warning", "icons/Warn.gif"), "OptionPane.windowBindings", new Object[]{"ESCAPE", "close"}, "OptionPane.errorSound", "win.sound.hand", "OptionPane.informationSound", "win.sound.asterisk", "OptionPane.questionSound", "win.sound.question", "OptionPane.warningSound", "win.sound.exclamation", "FormattedTextField.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.previousWordAction, "ctrl KP_LEFT", DefaultEditorKit.previousWordAction, "ctrl RIGHT", DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl A", DefaultEditorKit.selectAllAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "ENTER", JTextField.notifyAction, "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation", "ESCAPE", "reset-field-edit", "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement"}), "FormattedTextField.inactiveBackground", desktopProperty, "FormattedTextField.disabledBackground", desktopProperty, "Panel.font", windowsFontSizeProperty3, "Panel.background", desktopProperty, "Panel.foreground", desktopProperty13, "PasswordField.font", windowsFontSizeProperty3, "PasswordField.background", xPColorValue, "PasswordField.foreground", desktopProperty13, "PasswordField.inactiveForeground", desktopProperty18, "PasswordField.inactiveBackground", desktopProperty, "PasswordField.disabledBackground", desktopProperty, "PasswordField.selectionBackground", desktopProperty10, "PasswordField.selectionForeground", desktopProperty11, "PasswordField.caretForeground", desktopProperty13, "PasswordField.echoChar", new XPValue(new Character((char) 9679), new Character('*')), "ProgressBar.font", windowsFontSizeProperty3, "ProgressBar.foreground", desktopProperty10, "ProgressBar.background", desktopProperty, "ProgressBar.shadow", desktopProperty4, "ProgressBar.highlight", desktopProperty3, "ProgressBar.selectionForeground", desktopProperty, "ProgressBar.selectionBackground", desktopProperty10, "ProgressBar.cellLength", 7, "ProgressBar.cellSpacing", 2, "ProgressBar.indeterminateInsets", new Insets(3, 3, 3, 3), "RootPane.defaultButtonWindowKeyBindings", new Object[]{"ENTER", BasicRootPaneUI.Actions.PRESS, "released ENTER", BasicRootPaneUI.Actions.RELEASE, "ctrl ENTER", BasicRootPaneUI.Actions.PRESS, "ctrl released ENTER", BasicRootPaneUI.Actions.RELEASE}, "ScrollBar.background", desktopProperty19, "ScrollBar.foreground", desktopProperty, "ScrollBar.track", colorUIResource3, "ScrollBar.trackForeground", desktopProperty19, "ScrollBar.trackHighlight", colorUIResource2, "ScrollBar.trackHighlightForeground", colorUIResource5, "ScrollBar.thumb", desktopProperty, "ScrollBar.thumbHighlight", desktopProperty3, "ScrollBar.thumbDarkShadow", desktopProperty5, "ScrollBar.thumbShadow", desktopProperty4, "ScrollBar.width", desktopProperty20, "ScrollBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "DOWN", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_DOWN", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "PAGE_DOWN", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "ctrl PAGE_DOWN", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "UP", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_UP", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "PAGE_UP", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "ctrl PAGE_UP", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "HOME", BasicSliderUI.Actions.MIN_SCROLL_INCREMENT, "END", BasicSliderUI.Actions.MAX_SCROLL_INCREMENT}), "ScrollPane.font", windowsFontSizeProperty3, "ScrollPane.background", desktopProperty, "ScrollPane.foreground", desktopProperty6, "ScrollPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "unitScrollRight", "KP_RIGHT", "unitScrollRight", "DOWN", "unitScrollDown", "KP_DOWN", "unitScrollDown", "LEFT", "unitScrollLeft", "KP_LEFT", "unitScrollLeft", "UP", "unitScrollUp", "KP_UP", "unitScrollUp", "PAGE_UP", "scrollUp", "PAGE_DOWN", "scrollDown", "ctrl PAGE_UP", "scrollLeft", "ctrl PAGE_DOWN", "scrollRight", "ctrl HOME", "scrollHome", "ctrl END", "scrollEnd"}), "Separator.background", desktopProperty3, "Separator.foreground", desktopProperty4, "Slider.font", windowsFontSizeProperty3, "Slider.foreground", desktopProperty, "Slider.background", desktopProperty, "Slider.highlight", desktopProperty3, "Slider.shadow", desktopProperty4, "Slider.focus", desktopProperty5, "Slider.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "DOWN", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_DOWN", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "PAGE_DOWN", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "UP", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_UP", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "PAGE_UP", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "HOME", BasicSliderUI.Actions.MIN_SCROLL_INCREMENT, "END", BasicSliderUI.Actions.MAX_SCROLL_INCREMENT}), "Spinner.font", windowsFontSizeProperty3, "Spinner.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement"}), "SplitPane.background", desktopProperty, "SplitPane.highlight", desktopProperty3, "SplitPane.shadow", desktopProperty4, "SplitPane.darkShadow", desktopProperty5, "SplitPane.dividerSize", 5, "SplitPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "negativeIncrement", "DOWN", "positiveIncrement", "LEFT", "negativeIncrement", "RIGHT", "positiveIncrement", "KP_UP", "negativeIncrement", "KP_DOWN", "positiveIncrement", "KP_LEFT", "negativeIncrement", "KP_RIGHT", "positiveIncrement", "HOME", "selectMin", "END", "selectMax", "F8", "startResize", "F6", "toggleFocus", "ctrl TAB", "focusOutForward", "ctrl shift TAB", "focusOutBackward"}), "TabbedPane.tabsOverlapBorder", new XPValue(Boolean.TRUE, Boolean.FALSE), "TabbedPane.tabInsets", new XPValue(new InsetsUIResource(1, 4, 1, 4), new InsetsUIResource(0, 4, 1, 4)), "TabbedPane.tabAreaInsets", new XPValue(new InsetsUIResource(3, 2, 2, 2), new InsetsUIResource(3, 2, 0, 2)), "TabbedPane.font", windowsFontSizeProperty3, "TabbedPane.background", desktopProperty, "TabbedPane.foreground", desktopProperty6, "TabbedPane.highlight", desktopProperty3, "TabbedPane.light", desktopProperty2, "TabbedPane.shadow", desktopProperty4, "TabbedPane.darkShadow", desktopProperty5, "TabbedPane.focus", desktopProperty6, "TabbedPane.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "navigateRight", "KP_RIGHT", "navigateRight", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "ctrl DOWN", "requestFocusForVisibleComponent", "ctrl KP_DOWN", "requestFocusForVisibleComponent"}), "TabbedPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl TAB", "navigateNext", "ctrl shift TAB", "navigatePrevious", "ctrl PAGE_DOWN", "navigatePageDown", "ctrl PAGE_UP", "navigatePageUp", "ctrl UP", "requestFocus", "ctrl KP_UP", "requestFocus"}), "Table.font", windowsFontSizeProperty3, "Table.foreground", desktopProperty6, "Table.background", desktopProperty12, "Table.highlight", desktopProperty3, "Table.light", desktopProperty2, "Table.shadow", desktopProperty4, "Table.darkShadow", desktopProperty5, "Table.selectionForeground", desktopProperty11, "Table.selectionBackground", desktopProperty10, "Table.gridColor", colorUIResource4, "Table.focusCellBackground", desktopProperty12, "Table.focusCellForeground", desktopProperty6, "Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo", "F8", "focusHeader"}), "Table.sortIconHighlight", desktopProperty4, "Table.sortIconLight", colorUIResource3, "TableHeader.font", windowsFontSizeProperty3, "TableHeader.foreground", desktopProperty6, "TableHeader.background", desktopProperty, "TableHeader.focusCellBackground", new XPValue(XPValue.NULL_VALUE, desktopProperty12), "TextArea.font", windowsFontSizeProperty2, "TextArea.background", desktopProperty12, "TextArea.foreground", desktopProperty13, "TextArea.inactiveForeground", desktopProperty18, "TextArea.inactiveBackground", desktopProperty12, "TextArea.disabledBackground", desktopProperty, "TextArea.selectionBackground", desktopProperty10, "TextArea.selectionForeground", desktopProperty11, "TextArea.caretForeground", desktopProperty13, "TextField.font", windowsFontSizeProperty3, "TextField.background", xPColorValue, "TextField.foreground", desktopProperty13, "TextField.shadow", desktopProperty4, "TextField.darkShadow", desktopProperty5, "TextField.light", desktopProperty2, "TextField.highlight", desktopProperty3, "TextField.inactiveForeground", desktopProperty18, "TextField.inactiveBackground", desktopProperty, "TextField.disabledBackground", desktopProperty, "TextField.selectionBackground", desktopProperty10, "TextField.selectionForeground", desktopProperty11, "TextField.caretForeground", desktopProperty13, "TextPane.font", windowsFontSizeProperty3, "TextPane.background", desktopProperty12, "TextPane.foreground", desktopProperty13, "TextPane.selectionBackground", desktopProperty10, "TextPane.selectionForeground", desktopProperty11, "TextPane.inactiveBackground", desktopProperty12, "TextPane.disabledBackground", desktopProperty, "TextPane.caretForeground", desktopProperty13, "TitledBorder.font", windowsFontSizeProperty3, "TitledBorder.titleColor", new XPColorValue(TMSchema.Part.BP_GROUPBOX, null, TMSchema.Prop.TEXTCOLOR, desktopProperty13), "ToggleButton.font", windowsFontSizeProperty3, "ToggleButton.background", desktopProperty, "ToggleButton.foreground", desktopProperty6, "ToggleButton.shadow", desktopProperty4, "ToggleButton.darkShadow", desktopProperty5, "ToggleButton.light", desktopProperty2, "ToggleButton.highlight", desktopProperty3, "ToggleButton.focus", desktopProperty6, "ToggleButton.textShiftOffset", 1, "ToggleButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "ToolBar.font", windowsFontSizeProperty, "ToolBar.background", desktopProperty, "ToolBar.foreground", desktopProperty6, "ToolBar.shadow", desktopProperty4, "ToolBar.darkShadow", desktopProperty5, "ToolBar.light", desktopProperty2, "ToolBar.highlight", desktopProperty3, "ToolBar.dockingBackground", desktopProperty, "ToolBar.dockingForeground", colorUIResource, "ToolBar.floatingBackground", desktopProperty, "ToolBar.floatingForeground", colorUIResource5, "ToolBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight"}), "ToolBar.separatorSize", null, "ToolTip.font", windowsFontSizeProperty6, "ToolTip.background", new DesktopProperty("win.tooltip.backgroundColor", uIDefaults.get("info")), "ToolTip.foreground", new DesktopProperty("win.tooltip.textColor", uIDefaults.get("infoText")), "ToolTipManager.enableToolTipMode", "activeApplication", "Tree.selectionBorderColor", colorUIResource2, "Tree.drawDashedFocusIndicator", Boolean.TRUE, "Tree.lineTypeDashed", Boolean.TRUE, "Tree.font", windowsFontSizeProperty3, "Tree.background", desktopProperty12, "Tree.foreground", desktopProperty13, "Tree.hash", colorUIResource4, "Tree.leftChildIndent", 8, "Tree.rightChildIndent", 11, "Tree.textForeground", desktopProperty13, "Tree.textBackground", desktopProperty12, "Tree.selectionForeground", desktopProperty11, "Tree.selectionBackground", desktopProperty10, "Tree.expandedIcon", objCreateExpandedIcon, "Tree.collapsedIcon", objCreateCollapsedIcon, "Tree.openIcon", new ActiveWindowsIcon("win.icon.shellIconBPP", "shell32Icon 5", "icons/TreeOpen.gif"), "Tree.closedIcon", new ActiveWindowsIcon("win.icon.shellIconBPP", "shell32Icon 4", "icons/TreeClosed.gif"), "Tree.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ADD", "expand", "SUBTRACT", SchemaSymbols.ATTVAL_COLLAPSE, "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPrevious", "KP_UP", "selectPrevious", "shift UP", "selectPreviousExtendSelection", "shift KP_UP", "selectPreviousExtendSelection", "ctrl shift UP", "selectPreviousExtendSelection", "ctrl shift KP_UP", "selectPreviousExtendSelection", "ctrl UP", "selectPreviousChangeLead", "ctrl KP_UP", "selectPreviousChangeLead", "DOWN", "selectNext", "KP_DOWN", "selectNext", "shift DOWN", "selectNextExtendSelection", "shift KP_DOWN", "selectNextExtendSelection", "ctrl shift DOWN", "selectNextExtendSelection", "ctrl shift KP_DOWN", "selectNextExtendSelection", "ctrl DOWN", "selectNextChangeLead", "ctrl KP_DOWN", "selectNextChangeLead", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "LEFT", "selectParent", "KP_LEFT", "selectParent", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "HOME", "selectFirst", "shift HOME", "selectFirstExtendSelection", "ctrl shift HOME", "selectFirstExtendSelection", "ctrl HOME", "selectFirstChangeLead", "END", "selectLast", "shift END", "selectLastExtendSelection", "ctrl shift END", "selectLastExtendSelection", "ctrl END", "selectLastChangeLead", "F2", "startEditing", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ctrl LEFT", "scrollLeft", "ctrl KP_LEFT", "scrollLeft", "ctrl RIGHT", "scrollRight", "ctrl KP_RIGHT", "scrollRight", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"}), "Tree.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", "cancel"}), "Viewport.font", windowsFontSizeProperty3, "Viewport.background", desktopProperty, "Viewport.foreground", desktopProperty13});
        uIDefaults.putDefaults(getLazyValueDefaults());
        initVistaComponentDefaults(uIDefaults);
    }

    static boolean isOnVista() {
        return OSInfo.getOSType() == OSInfo.OSType.WINDOWS && OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_VISTA) >= 0;
    }

    private void initVistaComponentDefaults(UIDefaults uIDefaults) {
        if (!isOnVista()) {
            return;
        }
        String[] strArr = {"MenuItem", "Menu", "CheckBoxMenuItem", "RadioButtonMenuItem"};
        Object[] objArr = new Object[strArr.length * 2];
        int i2 = 0;
        for (String str : strArr) {
            String str2 = str + ".opaque";
            Object obj = uIDefaults.get(str2);
            int i3 = i2;
            int i4 = i2 + 1;
            objArr[i3] = str2;
            i2 = i4 + 1;
            objArr[i4] = new XPValue(Boolean.FALSE, obj);
        }
        uIDefaults.putDefaults(objArr);
        int i5 = 0;
        for (int i6 = 0; i6 < strArr.length; i6++) {
            String str3 = strArr[i6] + ".acceleratorSelectionForeground";
            Object obj2 = uIDefaults.get(str3);
            int i7 = i5;
            int i8 = i5 + 1;
            objArr[i7] = str3;
            i5 = i8 + 1;
            objArr[i8] = new XPValue(uIDefaults.getColor(strArr[i6] + ".acceleratorForeground"), obj2);
        }
        uIDefaults.putDefaults(objArr);
        WindowsIconFactory.VistaMenuItemCheckIconFactory menuItemCheckIconFactory = WindowsIconFactory.getMenuItemCheckIconFactory();
        int i9 = 0;
        for (String str4 : strArr) {
            String str5 = str4 + ".checkIconFactory";
            Object obj3 = uIDefaults.get(str5);
            int i10 = i9;
            int i11 = i9 + 1;
            objArr[i10] = str5;
            i9 = i11 + 1;
            objArr[i11] = new XPValue(menuItemCheckIconFactory, obj3);
        }
        uIDefaults.putDefaults(objArr);
        int i12 = 0;
        for (int i13 = 0; i13 < strArr.length; i13++) {
            String str6 = strArr[i13] + ".checkIcon";
            Object obj4 = uIDefaults.get(str6);
            int i14 = i12;
            int i15 = i12 + 1;
            objArr[i14] = str6;
            i12 = i15 + 1;
            objArr[i15] = new XPValue(menuItemCheckIconFactory.getIcon(strArr[i13]), obj4);
        }
        uIDefaults.putDefaults(objArr);
        int i16 = 0;
        for (String str7 : strArr) {
            String str8 = str7 + ".evenHeight";
            Object obj5 = uIDefaults.get(str8);
            int i17 = i16;
            int i18 = i16 + 1;
            objArr[i17] = str8;
            i16 = i18 + 1;
            objArr[i18] = new XPValue(Boolean.TRUE, obj5);
        }
        uIDefaults.putDefaults(objArr);
        InsetsUIResource insetsUIResource = new InsetsUIResource(0, 0, 0, 0);
        int i19 = 0;
        for (String str9 : strArr) {
            String str10 = str9 + ".margin";
            Object obj6 = uIDefaults.get(str10);
            int i20 = i19;
            int i21 = i19 + 1;
            objArr[i20] = str10;
            i19 = i21 + 1;
            objArr[i21] = new XPValue(insetsUIResource, obj6);
        }
        uIDefaults.putDefaults(objArr);
        int i22 = 0;
        for (String str11 : strArr) {
            String str12 = str11 + ".checkIconOffset";
            Object obj7 = uIDefaults.get(str12);
            int i23 = i22;
            int i24 = i22 + 1;
            objArr[i23] = str12;
            i22 = i24 + 1;
            objArr[i24] = new XPValue(0, obj7);
        }
        uIDefaults.putDefaults(objArr);
        Integer numValueOf = Integer.valueOf(WindowsPopupMenuUI.getSpanBeforeGutter() + WindowsPopupMenuUI.getGutterWidth() + WindowsPopupMenuUI.getSpanAfterGutter());
        int i25 = 0;
        for (String str13 : strArr) {
            String str14 = str13 + ".afterCheckIconGap";
            Object obj8 = uIDefaults.get(str14);
            int i26 = i25;
            int i27 = i25 + 1;
            objArr[i26] = str14;
            i25 = i27 + 1;
            objArr[i27] = new XPValue(numValueOf, obj8);
        }
        uIDefaults.putDefaults(objArr);
        UIDefaults.ActiveValue activeValue = new UIDefaults.ActiveValue() { // from class: com.sun.java.swing.plaf.windows.WindowsLookAndFeel.1
            @Override // javax.swing.UIDefaults.ActiveValue
            public Object createValue(UIDefaults uIDefaults2) {
                return Integer.valueOf(WindowsIconFactory.VistaMenuItemCheckIconFactory.getIconWidth() + WindowsPopupMenuUI.getSpanBeforeGutter() + WindowsPopupMenuUI.getGutterWidth() + WindowsPopupMenuUI.getSpanAfterGutter());
            }
        };
        int i28 = 0;
        for (String str15 : strArr) {
            String str16 = str15 + ".minimumTextOffset";
            Object obj9 = uIDefaults.get(str16);
            int i29 = i28;
            int i30 = i28 + 1;
            objArr[i29] = str16;
            i28 = i30 + 1;
            objArr[i30] = new XPValue(activeValue, obj9);
        }
        uIDefaults.putDefaults(objArr);
        uIDefaults.put("PopupMenu.border", new XPBorderValue(TMSchema.Part.MENU, new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getInternalFrameBorder"), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        uIDefaults.put("Table.ascendingSortIcon", new XPValue(new SkinIcon(TMSchema.Part.HP_HEADERSORTARROW, TMSchema.State.SORTEDDOWN), new SwingLazyValue("sun.swing.plaf.windows.ClassicSortArrowIcon", null, new Object[]{Boolean.TRUE})));
        uIDefaults.put("Table.descendingSortIcon", new XPValue(new SkinIcon(TMSchema.Part.HP_HEADERSORTARROW, TMSchema.State.SORTEDUP), new SwingLazyValue("sun.swing.plaf.windows.ClassicSortArrowIcon", null, new Object[]{Boolean.FALSE})));
    }

    private Object getDesktopFontValue(String str, Object obj) {
        if (this.useSystemFontSettings) {
            return new WindowsFontProperty(str, obj);
        }
        return null;
    }

    private Object[] getLazyValueDefaults() {
        XPBorderValue xPBorderValue = new XPBorderValue(TMSchema.Part.BP_PUSHBUTTON, new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getButtonBorder"));
        XPBorderValue xPBorderValue2 = new XPBorderValue(TMSchema.Part.EP_EDIT, new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getTextFieldBorder"));
        XPValue xPValue = new XPValue(new InsetsUIResource(2, 2, 2, 2), new InsetsUIResource(1, 1, 1, 1));
        XPBorderValue xPBorderValue3 = new XPBorderValue(TMSchema.Part.EP_EDIT, xPBorderValue2, new EmptyBorder(2, 2, 2, 2));
        XPValue xPValue2 = new XPValue(new InsetsUIResource(1, 1, 1, 1), null);
        XPBorderValue xPBorderValue4 = new XPBorderValue(TMSchema.Part.CP_COMBOBOX, xPBorderValue2);
        SwingLazyValue swingLazyValue = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getFocusCellHighlightBorder");
        SwingLazyValue swingLazyValue2 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getEtchedBorderUIResource");
        SwingLazyValue swingLazyValue3 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getInternalFrameBorder");
        SwingLazyValue swingLazyValue4 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getLoweredBevelBorderUIResource");
        SwingLazyValue swingLazyValue5 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders$MarginBorder");
        SwingLazyValue swingLazyValue6 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getMenuBarBorder");
        XPBorderValue xPBorderValue5 = new XPBorderValue(TMSchema.Part.MENU, new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getInternalFrameBorder"));
        SwingLazyValue swingLazyValue7 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getProgressBarBorder");
        SwingLazyValue swingLazyValue8 = new SwingLazyValue("javax.swing.plaf.basic.BasicBorders", "getRadioButtonBorder");
        XPBorderValue xPBorderValue6 = new XPBorderValue(TMSchema.Part.LBP_LISTBOX, xPBorderValue2);
        XPBorderValue xPBorderValue7 = new XPBorderValue(TMSchema.Part.LBP_LISTBOX, swingLazyValue4);
        SwingLazyValue swingLazyValue9 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getTableHeaderBorder");
        SwingLazyValue swingLazyValue10 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsBorders", "getToolBarBorder");
        SwingLazyValue swingLazyValue11 = new SwingLazyValue("javax.swing.plaf.BorderUIResource", "getBlackLineBorderUIResource");
        SwingLazyValue swingLazyValue12 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getCheckBoxIcon");
        SwingLazyValue swingLazyValue13 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getRadioButtonIcon");
        SwingLazyValue swingLazyValue14 = new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getRadioButtonMenuItemIcon");
        return new Object[]{"Button.border", xPBorderValue, "CheckBox.border", swingLazyValue8, "ComboBox.border", xPBorderValue4, "DesktopIcon.border", swingLazyValue3, "FormattedTextField.border", xPBorderValue2, "FormattedTextField.margin", xPValue, "InternalFrame.border", swingLazyValue3, "List.focusCellHighlightBorder", swingLazyValue, "Table.focusCellHighlightBorder", swingLazyValue, "Menu.border", swingLazyValue5, "MenuBar.border", swingLazyValue6, "MenuItem.border", swingLazyValue5, "PasswordField.border", xPBorderValue2, "PasswordField.margin", xPValue, "PopupMenu.border", xPBorderValue5, "ProgressBar.border", swingLazyValue7, "RadioButton.border", swingLazyValue8, "ScrollPane.border", xPBorderValue6, "Spinner.border", xPBorderValue3, "Spinner.arrowButtonInsets", xPValue2, "Spinner.arrowButtonSize", new Dimension(17, 9), "Table.scrollPaneBorder", xPBorderValue7, "TableHeader.cellBorder", swingLazyValue9, "TextArea.margin", xPValue, "TextField.border", xPBorderValue2, "TextField.margin", xPValue, "TitledBorder.border", new XPBorderValue(TMSchema.Part.BP_GROUPBOX, swingLazyValue2), "ToggleButton.border", swingLazyValue8, "ToolBar.border", swingLazyValue10, "ToolTip.border", swingLazyValue11, "CheckBox.icon", swingLazyValue12, "Menu.arrowIcon", new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getMenuArrowIcon"), "MenuItem.checkIcon", new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getMenuItemCheckIcon"), "MenuItem.arrowIcon", new SwingLazyValue("com.sun.java.swing.plaf.windows.WindowsIconFactory", "getMenuItemArrowIcon"), "RadioButton.icon", swingLazyValue13, "RadioButtonMenuItem.checkIcon", swingLazyValue14, "InternalFrame.layoutTitlePaneAtOrigin", new XPValue(Boolean.TRUE, Boolean.FALSE), "Table.ascendingSortIcon", new XPValue(new SwingLazyValue("sun.swing.icon.SortArrowIcon", null, new Object[]{Boolean.TRUE, "Table.sortIconColor"}), new SwingLazyValue("sun.swing.plaf.windows.ClassicSortArrowIcon", null, new Object[]{Boolean.TRUE})), "Table.descendingSortIcon", new XPValue(new SwingLazyValue("sun.swing.icon.SortArrowIcon", null, new Object[]{Boolean.FALSE, "Table.sortIconColor"}), new SwingLazyValue("sun.swing.plaf.windows.ClassicSortArrowIcon", null, new Object[]{Boolean.FALSE}))};
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public void uninitialize() {
        super.uninitialize();
        if (WindowsPopupMenuUI.mnemonicListener != null) {
            MenuSelectionManager.defaultManager().removeChangeListener(WindowsPopupMenuUI.mnemonicListener);
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(WindowsRootPaneUI.altProcessor);
        DesktopProperty.flushUnreferencedProperties();
    }

    public static void setMnemonicHidden(boolean z2) {
        if (UIManager.getBoolean("Button.showMnemonics")) {
            isMnemonicHidden = false;
        } else {
            isMnemonicHidden = z2;
        }
    }

    public static boolean isMnemonicHidden() {
        if (UIManager.getBoolean("Button.showMnemonics")) {
            isMnemonicHidden = false;
        }
        return isMnemonicHidden;
    }

    public static boolean isClassicWindows() {
        return isClassicWindows;
    }

    @Override // javax.swing.LookAndFeel
    public void provideErrorFeedback(Component component) {
        super.provideErrorFeedback(component);
    }

    @Override // javax.swing.LookAndFeel
    public LayoutStyle getLayoutStyle() {
        LayoutStyle windowsLayoutStyle = this.style;
        if (windowsLayoutStyle == null) {
            windowsLayoutStyle = new WindowsLayoutStyle();
            this.style = windowsLayoutStyle;
        }
        return windowsLayoutStyle;
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel
    protected Action createAudioAction(Object obj) {
        if (obj != null) {
            return new AudioAction((String) obj, (String) UIManager.get(obj));
        }
        return null;
    }

    static void repaintRootPane(Component component) {
        JRootPane jRootPane = null;
        while (component != null) {
            if (component instanceof JRootPane) {
                jRootPane = (JRootPane) component;
            }
            component = component.getParent();
        }
        if (jRootPane != null) {
            jRootPane.repaint();
        } else {
            component.repaint();
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$AudioAction.class */
    private static class AudioAction extends AbstractAction {
        private Runnable audioRunnable;
        private String audioResource;

        public AudioAction(String str, String str2) {
            super(str);
            this.audioResource = str2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.audioRunnable == null) {
                this.audioRunnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty(this.audioResource);
            }
            if (this.audioRunnable != null) {
                new Thread(this.audioRunnable).start();
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$LazyWindowsIcon.class */
    private static class LazyWindowsIcon implements UIDefaults.LazyValue {
        private String nativeImage;
        private String resource;

        LazyWindowsIcon(String str, String str2) {
            this.nativeImage = str;
            this.resource = str2;
        }

        @Override // javax.swing.UIDefaults.LazyValue
        public Object createValue(UIDefaults uIDefaults) {
            Image image;
            if (this.nativeImage != null && (image = (Image) ShellFolder.get(this.nativeImage)) != null) {
                return new ImageIcon(image);
            }
            return SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, this.resource);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$ActiveWindowsIcon.class */
    private class ActiveWindowsIcon implements UIDefaults.ActiveValue {
        private Icon icon;
        private String nativeImageName;
        private String fallbackName;
        private DesktopProperty desktopProperty;

        ActiveWindowsIcon(String str, String str2, String str3) {
            this.nativeImageName = str2;
            this.fallbackName = str3;
            if (OSInfo.getOSType() == OSInfo.OSType.WINDOWS && OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_XP) < 0) {
                this.desktopProperty = new TriggerDesktopProperty(str) { // from class: com.sun.java.swing.plaf.windows.WindowsLookAndFeel.ActiveWindowsIcon.1
                    {
                        WindowsLookAndFeel windowsLookAndFeel = WindowsLookAndFeel.this;
                    }

                    @Override // com.sun.java.swing.plaf.windows.WindowsLookAndFeel.TriggerDesktopProperty, com.sun.java.swing.plaf.windows.DesktopProperty
                    protected void updateUI() {
                        ActiveWindowsIcon.this.icon = null;
                        super.updateUI();
                    }
                };
            }
        }

        @Override // javax.swing.UIDefaults.ActiveValue
        public Object createValue(UIDefaults uIDefaults) {
            Image image;
            if (this.icon == null && (image = (Image) ShellFolder.get(this.nativeImageName)) != null) {
                this.icon = new ImageIconUIResource(image);
            }
            if (this.icon == null && this.fallbackName != null) {
                this.icon = (Icon) ((UIDefaults.LazyValue) SwingUtilities2.makeIcon(WindowsLookAndFeel.class, BasicLookAndFeel.class, this.fallbackName)).createValue(uIDefaults);
            }
            return this.icon;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$SkinIcon.class */
    private static class SkinIcon implements Icon, UIResource {
        private final TMSchema.Part part;
        private final TMSchema.State state;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !WindowsLookAndFeel.class.desiredAssertionStatus();
        }

        SkinIcon(TMSchema.Part part, TMSchema.State state) {
            this.part = part;
            this.state = state;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            XPStyle xp = XPStyle.getXP();
            if (!$assertionsDisabled && xp == null) {
                throw new AssertionError();
            }
            if (xp != null) {
                xp.getSkin(null, this.part).paintSkin(graphics, i2, i3, this.state);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            int width = 0;
            XPStyle xp = XPStyle.getXP();
            if (!$assertionsDisabled && xp == null) {
                throw new AssertionError();
            }
            if (xp != null) {
                width = xp.getSkin(null, this.part).getWidth();
            }
            return width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            int height = 0;
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                height = xp.getSkin(null, this.part).getHeight();
            }
            return height;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$WindowsFontProperty.class */
    private static class WindowsFontProperty extends DesktopProperty {
        WindowsFontProperty(String str, Object obj) {
            super(str, obj);
        }

        @Override // com.sun.java.swing.plaf.windows.DesktopProperty
        public void invalidate(LookAndFeel lookAndFeel) {
            if ("win.defaultGUI.font.height".equals(getKey())) {
                ((WindowsLookAndFeel) lookAndFeel).style = null;
            }
            super.invalidate(lookAndFeel);
        }

        @Override // com.sun.java.swing.plaf.windows.DesktopProperty
        protected Object configureValue(Object obj) {
            int screenResolution;
            if (obj instanceof Font) {
                Font compositeFontUIResource = (Font) obj;
                if ("MS Sans Serif".equals(compositeFontUIResource.getName())) {
                    int size = compositeFontUIResource.getSize();
                    try {
                        screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
                    } catch (HeadlessException e2) {
                        screenResolution = 96;
                    }
                    if (Math.round((size * 72.0f) / screenResolution) < 8) {
                        size = Math.round((8 * screenResolution) / 72.0f);
                    }
                    FontUIResource fontUIResource = new FontUIResource("Microsoft Sans Serif", compositeFontUIResource.getStyle(), size);
                    if (fontUIResource.getName() != null && fontUIResource.getName().equals(fontUIResource.getFamily())) {
                        compositeFontUIResource = fontUIResource;
                    } else if (size != compositeFontUIResource.getSize()) {
                        compositeFontUIResource = new FontUIResource("MS Sans Serif", compositeFontUIResource.getStyle(), size);
                    }
                }
                if (FontUtilities.fontSupportsDefaultEncoding(compositeFontUIResource)) {
                    if (!(compositeFontUIResource instanceof UIResource)) {
                        compositeFontUIResource = new FontUIResource(compositeFontUIResource);
                    }
                } else {
                    compositeFontUIResource = FontUtilities.getCompositeFontUIResource(compositeFontUIResource);
                }
                return compositeFontUIResource;
            }
            return super.configureValue(obj);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$WindowsFontSizeProperty.class */
    private static class WindowsFontSizeProperty extends DesktopProperty {
        private String fontName;
        private int fontSize;
        private int fontStyle;

        WindowsFontSizeProperty(String str, String str2, int i2, int i3) {
            super(str, null);
            this.fontName = str2;
            this.fontSize = i3;
            this.fontStyle = i2;
        }

        @Override // com.sun.java.swing.plaf.windows.DesktopProperty
        protected Object configureValue(Object obj) {
            if (obj == null) {
                obj = new FontUIResource(this.fontName, this.fontStyle, this.fontSize);
            } else if (obj instanceof Integer) {
                obj = new FontUIResource(this.fontName, this.fontStyle, ((Integer) obj).intValue());
            }
            return obj;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$XPValue.class */
    private static class XPValue implements UIDefaults.ActiveValue {
        protected Object classicValue;
        protected Object xpValue;
        private static final Object NULL_VALUE = new Object();

        XPValue(Object obj, Object obj2) {
            this.xpValue = obj;
            this.classicValue = obj2;
        }

        @Override // javax.swing.UIDefaults.ActiveValue
        public Object createValue(UIDefaults uIDefaults) {
            Object classicValue = null;
            if (XPStyle.getXP() != null) {
                classicValue = getXPValue(uIDefaults);
            }
            if (classicValue == null) {
                classicValue = getClassicValue(uIDefaults);
            } else if (classicValue == NULL_VALUE) {
                classicValue = null;
            }
            return classicValue;
        }

        protected Object getXPValue(UIDefaults uIDefaults) {
            return recursiveCreateValue(this.xpValue, uIDefaults);
        }

        protected Object getClassicValue(UIDefaults uIDefaults) {
            return recursiveCreateValue(this.classicValue, uIDefaults);
        }

        private Object recursiveCreateValue(Object obj, UIDefaults uIDefaults) {
            if (obj instanceof UIDefaults.LazyValue) {
                obj = ((UIDefaults.LazyValue) obj).createValue(uIDefaults);
            }
            if (obj instanceof UIDefaults.ActiveValue) {
                return ((UIDefaults.ActiveValue) obj).createValue(uIDefaults);
            }
            return obj;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$XPBorderValue.class */
    private static class XPBorderValue extends XPValue {
        private final Border extraMargin;

        XPBorderValue(TMSchema.Part part, Object obj) {
            this(part, obj, null);
        }

        XPBorderValue(TMSchema.Part part, Object obj, Border border) {
            super(part, obj);
            this.extraMargin = border;
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsLookAndFeel.XPValue
        public Object getXPValue(UIDefaults uIDefaults) {
            XPStyle xp = XPStyle.getXP();
            Border border = xp != null ? xp.getBorder(null, (TMSchema.Part) this.xpValue) : null;
            if (border != null && this.extraMargin != null) {
                return new BorderUIResource.CompoundBorderUIResource(border, this.extraMargin);
            }
            return border;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$XPColorValue.class */
    private static class XPColorValue extends XPValue {
        XPColorValue(TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop, Object obj) {
            super(new XPColorValueKey(part, state, prop), obj);
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsLookAndFeel.XPValue
        public Object getXPValue(UIDefaults uIDefaults) {
            XPColorValueKey xPColorValueKey = (XPColorValueKey) this.xpValue;
            XPStyle xp = XPStyle.getXP();
            if (xp != null) {
                return xp.getColor(xPColorValueKey.skin, xPColorValueKey.prop, null);
            }
            return null;
        }

        /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$XPColorValue$XPColorValueKey.class */
        private static class XPColorValueKey {
            XPStyle.Skin skin;
            TMSchema.Prop prop;

            XPColorValueKey(TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop) {
                this.skin = new XPStyle.Skin(part, state);
                this.prop = prop;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$XPDLUValue.class */
    private class XPDLUValue extends XPValue {
        private int direction;

        XPDLUValue(int i2, int i3, int i4) {
            super(Integer.valueOf(i2), Integer.valueOf(i3));
            this.direction = i4;
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsLookAndFeel.XPValue
        public Object getXPValue(UIDefaults uIDefaults) {
            return Integer.valueOf(WindowsLookAndFeel.this.dluToPixels(((Integer) this.xpValue).intValue(), this.direction));
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsLookAndFeel.XPValue
        public Object getClassicValue(UIDefaults uIDefaults) {
            return Integer.valueOf(WindowsLookAndFeel.this.dluToPixels(((Integer) this.classicValue).intValue(), this.direction));
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$TriggerDesktopProperty.class */
    private class TriggerDesktopProperty extends DesktopProperty {
        TriggerDesktopProperty(String str) {
            super(str, null);
            getValueFromDesktop();
        }

        @Override // com.sun.java.swing.plaf.windows.DesktopProperty
        protected void updateUI() {
            super.updateUI();
            getValueFromDesktop();
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$FontDesktopProperty.class */
    private class FontDesktopProperty extends TriggerDesktopProperty {
        FontDesktopProperty(String str) {
            super(str);
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsLookAndFeel.TriggerDesktopProperty, com.sun.java.swing.plaf.windows.DesktopProperty
        protected void updateUI() {
            UIManager.getLookAndFeelDefaults().put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, SwingUtilities2.AATextInfo.getAATextInfo(true));
            super.updateUI();
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$WindowsLayoutStyle.class */
    private class WindowsLayoutStyle extends DefaultLayoutStyle {
        private WindowsLayoutStyle() {
        }

        @Override // sun.swing.DefaultLayoutStyle, javax.swing.LayoutStyle
        public int getPreferredGap(JComponent jComponent, JComponent jComponent2, LayoutStyle.ComponentPlacement componentPlacement, int i2, Container container) {
            super.getPreferredGap(jComponent, jComponent2, componentPlacement, i2, container);
            switch (componentPlacement) {
                case INDENT:
                    if (i2 == 3 || i2 == 7) {
                        int indent = getIndent(jComponent, i2);
                        if (indent > 0) {
                            return indent;
                        }
                        return 10;
                    }
                    break;
                case RELATED:
                    break;
                case UNRELATED:
                    return getButtonGap(jComponent, jComponent2, i2, WindowsLookAndFeel.this.dluToPixels(7, i2));
                default:
                    return 0;
            }
            if (isLabelAndNonlabel(jComponent, jComponent2, i2)) {
                return getButtonGap(jComponent, jComponent2, i2, WindowsLookAndFeel.this.dluToPixels(3, i2));
            }
            return getButtonGap(jComponent, jComponent2, i2, WindowsLookAndFeel.this.dluToPixels(4, i2));
        }

        @Override // sun.swing.DefaultLayoutStyle, javax.swing.LayoutStyle
        public int getContainerGap(JComponent jComponent, int i2, Container container) {
            super.getContainerGap(jComponent, i2, container);
            return getButtonGap(jComponent, i2, WindowsLookAndFeel.this.dluToPixels(7, i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int dluToPixels(int i2, int i3) {
        if (this.baseUnitX == 0) {
            calculateBaseUnits();
        }
        if (i3 == 3 || i3 == 7) {
            return (i2 * this.baseUnitX) / 4;
        }
        if ($assertionsDisabled || i3 == 1 || i3 == 5) {
            return (i2 * this.baseUnitY) / 8;
        }
        throw new AssertionError();
    }

    private void calculateBaseUnits() {
        FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(UIManager.getFont("Button.font"));
        this.baseUnitX = fontMetrics.stringWidth("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        this.baseUnitX = ((this.baseUnitX / 26) + 1) / 2;
        this.baseUnitY = (fontMetrics.getAscent() + fontMetrics.getDescent()) - 1;
    }

    @Override // javax.swing.LookAndFeel
    public Icon getDisabledIcon(JComponent jComponent, Icon icon) {
        if (icon != null && jComponent != null && Boolean.TRUE.equals(jComponent.getClientProperty(HI_RES_DISABLED_ICON_CLIENT_KEY)) && icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconWidth(), 2);
            icon.paintIcon(jComponent, bufferedImage.getGraphics(), 0, 0);
            return new ImageIconUIResource(jComponent.createImage(new FilteredImageSource(bufferedImage.getSource(), new RGBGrayFilter())));
        }
        return super.getDisabledIcon(jComponent, icon);
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$RGBGrayFilter.class */
    private static class RGBGrayFilter extends RGBImageFilter {
        public RGBGrayFilter() {
            this.canFilterIndexColorModel = true;
        }

        @Override // java.awt.image.RGBImageFilter
        public int filterRGB(int i2, int i3, int i4) {
            float f2 = (((((i4 >> 16) & 255) / 255.0f) + (((i4 >> 8) & 255) / 255.0f)) + ((i4 & 255) / 255.0f)) / 3.0f;
            float f3 = ((i4 >> 24) & 255) / 255.0f;
            float fMin = Math.min(1.0f, ((1.0f - f2) / 2.857143f) + f2);
            return (((int) (f3 * 255.0f)) << 24) | (((int) (fMin * 255.0f)) << 16) | (((int) (fMin * 255.0f)) << 8) | ((int) (fMin * 255.0f));
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLookAndFeel$FocusColorProperty.class */
    private static class FocusColorProperty extends DesktopProperty {
        public FocusColorProperty() {
            super("win.3d.backgroundColor", Color.BLACK);
        }

        @Override // com.sun.java.swing.plaf.windows.DesktopProperty
        protected Object configureValue(Object obj) {
            Object desktopProperty = Toolkit.getDefaultToolkit().getDesktopProperty("win.highContrast.on");
            if (desktopProperty == null || !((Boolean) desktopProperty).booleanValue()) {
                return Color.BLACK;
            }
            return Color.BLACK.equals(obj) ? Color.WHITE : Color.BLACK;
        }
    }
}
