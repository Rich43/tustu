package javax.swing.plaf.metal;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.text.DefaultEditorKit;
import sun.awt.AppContext;
import sun.awt.OSInfo;
import sun.awt.SunToolkit;
import sun.security.action.GetPropertyAction;
import sun.swing.DefaultLayoutStyle;
import sun.swing.FilePane;
import sun.swing.SwingLazyValue;
import sun.swing.SwingUtilities2;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalLookAndFeel.class */
public class MetalLookAndFeel extends BasicLookAndFeel {
    private static boolean checkedWindows;
    private static boolean isWindows;
    private static boolean checkedSystemFontSettings;
    private static boolean useSystemFonts;
    private static boolean METAL_LOOK_AND_FEEL_INITED = false;
    static ReferenceQueue<LookAndFeel> queue = new ReferenceQueue<>();

    static boolean isWindows() {
        if (!checkedWindows) {
            if (((OSInfo.OSType) AccessController.doPrivileged(OSInfo.getOSTypeAction())) == OSInfo.OSType.WINDOWS) {
                isWindows = true;
                String str = (String) AccessController.doPrivileged(new GetPropertyAction("swing.useSystemFontSettings"));
                useSystemFonts = str != null && Boolean.valueOf(str).booleanValue();
            }
            checkedWindows = true;
        }
        return isWindows;
    }

    static boolean useSystemFonts() {
        Object obj;
        if (isWindows() && useSystemFonts) {
            return !METAL_LOOK_AND_FEEL_INITED || (obj = UIManager.get("Application.useSystemFontSettings")) == null || Boolean.TRUE.equals(obj);
        }
        return false;
    }

    private static boolean useHighContrastTheme() {
        Boolean bool;
        if (isWindows() && useSystemFonts() && (bool = (Boolean) Toolkit.getDefaultToolkit().getDesktopProperty("win.highContrast.on")) != null) {
            return bool.booleanValue();
        }
        return false;
    }

    static boolean usingOcean() {
        return getCurrentTheme() instanceof OceanTheme;
    }

    @Override // javax.swing.LookAndFeel
    public String getName() {
        return "Metal";
    }

    @Override // javax.swing.LookAndFeel
    public String getID() {
        return "Metal";
    }

    @Override // javax.swing.LookAndFeel
    public String getDescription() {
        return "The Java(tm) Look and Feel";
    }

    @Override // javax.swing.LookAndFeel
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override // javax.swing.LookAndFeel
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override // javax.swing.LookAndFeel
    public boolean getSupportsWindowDecorations() {
        return true;
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel
    protected void initClassDefaults(UIDefaults uIDefaults) {
        super.initClassDefaults(uIDefaults);
        uIDefaults.putDefaults(new Object[]{"ButtonUI", "javax.swing.plaf.metal.MetalButtonUI", "CheckBoxUI", "javax.swing.plaf.metal.MetalCheckBoxUI", "ComboBoxUI", "javax.swing.plaf.metal.MetalComboBoxUI", "DesktopIconUI", "javax.swing.plaf.metal.MetalDesktopIconUI", "FileChooserUI", "javax.swing.plaf.metal.MetalFileChooserUI", "InternalFrameUI", "javax.swing.plaf.metal.MetalInternalFrameUI", "LabelUI", "javax.swing.plaf.metal.MetalLabelUI", "PopupMenuSeparatorUI", "javax.swing.plaf.metal.MetalPopupMenuSeparatorUI", "ProgressBarUI", "javax.swing.plaf.metal.MetalProgressBarUI", "RadioButtonUI", "javax.swing.plaf.metal.MetalRadioButtonUI", "ScrollBarUI", "javax.swing.plaf.metal.MetalScrollBarUI", "ScrollPaneUI", "javax.swing.plaf.metal.MetalScrollPaneUI", "SeparatorUI", "javax.swing.plaf.metal.MetalSeparatorUI", "SliderUI", "javax.swing.plaf.metal.MetalSliderUI", "SplitPaneUI", "javax.swing.plaf.metal.MetalSplitPaneUI", "TabbedPaneUI", "javax.swing.plaf.metal.MetalTabbedPaneUI", "TextFieldUI", "javax.swing.plaf.metal.MetalTextFieldUI", "ToggleButtonUI", "javax.swing.plaf.metal.MetalToggleButtonUI", "ToolBarUI", "javax.swing.plaf.metal.MetalToolBarUI", "ToolTipUI", "javax.swing.plaf.metal.MetalToolTipUI", "TreeUI", "javax.swing.plaf.metal.MetalTreeUI", "RootPaneUI", "javax.swing.plaf.metal.MetalRootPaneUI"});
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel
    protected void initSystemColorDefaults(UIDefaults uIDefaults) {
        MetalTheme currentTheme = getCurrentTheme();
        ColorUIResource control = currentTheme.getControl();
        uIDefaults.putDefaults(new Object[]{"desktop", currentTheme.getDesktopColor(), "activeCaption", currentTheme.getWindowTitleBackground(), "activeCaptionText", currentTheme.getWindowTitleForeground(), "activeCaptionBorder", currentTheme.getPrimaryControlShadow(), "inactiveCaption", currentTheme.getWindowTitleInactiveBackground(), "inactiveCaptionText", currentTheme.getWindowTitleInactiveForeground(), "inactiveCaptionBorder", currentTheme.getControlShadow(), "window", currentTheme.getWindowBackground(), "windowBorder", control, "windowText", currentTheme.getUserTextColor(), "menu", currentTheme.getMenuBackground(), "menuText", currentTheme.getMenuForeground(), "text", currentTheme.getWindowBackground(), "textText", currentTheme.getUserTextColor(), "textHighlight", currentTheme.getTextHighlightColor(), "textHighlightText", currentTheme.getHighlightedTextColor(), "textInactiveText", currentTheme.getInactiveSystemTextColor(), "control", control, "controlText", currentTheme.getControlTextColor(), "controlHighlight", currentTheme.getControlHighlight(), "controlLtHighlight", currentTheme.getControlHighlight(), "controlShadow", currentTheme.getControlShadow(), "controlDkShadow", currentTheme.getControlDarkShadow(), "scrollbar", control, "info", currentTheme.getPrimaryControl(), "infoText", currentTheme.getPrimaryControlInfo()});
    }

    private void initResourceBundle(UIDefaults uIDefaults) {
        uIDefaults.addResourceBundle("com.sun.swing.internal.plaf.metal.resources.metal");
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel
    protected void initComponentDefaults(UIDefaults uIDefaults) {
        super.initComponentDefaults(uIDefaults);
        initResourceBundle(uIDefaults);
        Object acceleratorForeground = getAcceleratorForeground();
        Object acceleratorSelectedForeground = getAcceleratorSelectedForeground();
        Object control = getControl();
        Object controlHighlight = getControlHighlight();
        Object controlShadow = getControlShadow();
        Object controlDarkShadow = getControlDarkShadow();
        Object controlTextColor = getControlTextColor();
        Object focusColor = getFocusColor();
        Object inactiveControlTextColor = getInactiveControlTextColor();
        Object menuBackground = getMenuBackground();
        Object menuSelectedBackground = getMenuSelectedBackground();
        Object menuDisabledForeground = getMenuDisabledForeground();
        Object menuSelectedForeground = getMenuSelectedForeground();
        Object primaryControl = getPrimaryControl();
        Object primaryControlDarkShadow = getPrimaryControlDarkShadow();
        Object primaryControlShadow = getPrimaryControlShadow();
        Object systemTextColor = getSystemTextColor();
        Object insetsUIResource = new InsetsUIResource(0, 0, 0, 0);
        Object swingLazyValue = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders", "getTextFieldBorder");
        Object obj = uIDefaults2 -> {
            return new MetalBorders.DialogBorder();
        };
        Object obj2 = uIDefaults3 -> {
            return new MetalBorders.QuestionDialogBorder();
        };
        Object lazyInputMap = new UIDefaults.LazyInputMap(new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.previousWordAction, "ctrl KP_LEFT", DefaultEditorKit.previousWordAction, "ctrl RIGHT", DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl A", DefaultEditorKit.selectAllAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "ENTER", JTextField.notifyAction, "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation"});
        Object lazyInputMap2 = new UIDefaults.LazyInputMap(new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.beginLineAction, "ctrl KP_LEFT", DefaultEditorKit.beginLineAction, "ctrl RIGHT", DefaultEditorKit.endLineAction, "ctrl KP_RIGHT", DefaultEditorKit.endLineAction, "ctrl shift LEFT", DefaultEditorKit.selectionBeginLineAction, "ctrl shift KP_LEFT", DefaultEditorKit.selectionBeginLineAction, "ctrl shift RIGHT", DefaultEditorKit.selectionEndLineAction, "ctrl shift KP_RIGHT", DefaultEditorKit.selectionEndLineAction, "ctrl A", DefaultEditorKit.selectAllAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "ENTER", JTextField.notifyAction, "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation"});
        Object lazyInputMap3 = new UIDefaults.LazyInputMap(new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.previousWordAction, "ctrl KP_LEFT", DefaultEditorKit.previousWordAction, "ctrl RIGHT", DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl A", DefaultEditorKit.selectAllAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "UP", DefaultEditorKit.upAction, "KP_UP", DefaultEditorKit.upAction, "DOWN", DefaultEditorKit.downAction, "KP_DOWN", DefaultEditorKit.downAction, "PAGE_UP", DefaultEditorKit.pageUpAction, "PAGE_DOWN", DefaultEditorKit.pageDownAction, "shift PAGE_UP", "selection-page-up", "shift PAGE_DOWN", "selection-page-down", "ctrl shift PAGE_UP", "selection-page-left", "ctrl shift PAGE_DOWN", "selection-page-right", "shift UP", DefaultEditorKit.selectionUpAction, "shift KP_UP", DefaultEditorKit.selectionUpAction, "shift DOWN", DefaultEditorKit.selectionDownAction, "shift KP_DOWN", DefaultEditorKit.selectionDownAction, "ENTER", DefaultEditorKit.insertBreakAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "TAB", DefaultEditorKit.insertTabAction, "ctrl BACK_SLASH", "unselect", "ctrl HOME", DefaultEditorKit.beginAction, "ctrl END", DefaultEditorKit.endAction, "ctrl shift HOME", DefaultEditorKit.selectionBeginAction, "ctrl shift END", DefaultEditorKit.selectionEndAction, "ctrl T", "next-link-action", "ctrl shift T", "previous-link-action", "ctrl SPACE", "activate-link-action", "control shift O", "toggle-componentOrientation"});
        Object swingLazyValue2 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$ScrollPaneBorder");
        Object swingLazyValue3 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders", "getButtonBorder");
        Object swingLazyValue4 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders", "getToggleButtonBorder");
        Object swingLazyValue5 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[]{controlShadow});
        Object swingLazyValue6 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders", "getDesktopIconBorder");
        Object swingLazyValue7 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$MenuBarBorder");
        Object swingLazyValue8 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$PopupMenuBorder");
        Object swingLazyValue9 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$MenuItemBorder");
        Object swingLazyValue10 = new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$ToolBarBorder");
        Object swingLazyValue11 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[]{controlDarkShadow, new Integer(1)});
        Object swingLazyValue12 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[]{primaryControlDarkShadow});
        Object swingLazyValue13 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[]{controlDarkShadow});
        Object swingLazyValue14 = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new Object[]{focusColor});
        Object insetsUIResource2 = new InsetsUIResource(4, 2, 0, 6);
        Object insetsUIResource3 = new InsetsUIResource(0, 9, 1, 9);
        Object[] objArr = {new Integer(16)};
        MetalTheme currentTheme = getCurrentTheme();
        Object fontActiveValue = new FontActiveValue(currentTheme, 3);
        Object fontActiveValue2 = new FontActiveValue(currentTheme, 0);
        Object fontActiveValue3 = new FontActiveValue(currentTheme, 2);
        Object fontActiveValue4 = new FontActiveValue(currentTheme, 4);
        Object fontActiveValue5 = new FontActiveValue(currentTheme, 5);
        uIDefaults.putDefaults(new Object[]{"AuditoryCues.defaultCueList", new Object[]{"OptionPane.errorSound", "OptionPane.informationSound", "OptionPane.questionSound", "OptionPane.warningSound"}, "AuditoryCues.playList", null, "TextField.border", swingLazyValue, "TextField.font", fontActiveValue3, "PasswordField.border", swingLazyValue, "PasswordField.font", fontActiveValue3, "PasswordField.echoChar", (char) 8226, "TextArea.font", fontActiveValue3, "TextPane.background", uIDefaults.get("window"), "TextPane.font", fontActiveValue3, "EditorPane.background", uIDefaults.get("window"), "EditorPane.font", fontActiveValue3, "TextField.focusInputMap", lazyInputMap, "PasswordField.focusInputMap", lazyInputMap2, "TextArea.focusInputMap", lazyInputMap3, "TextPane.focusInputMap", lazyInputMap3, "EditorPane.focusInputMap", lazyInputMap3, "FormattedTextField.border", swingLazyValue, "FormattedTextField.font", fontActiveValue3, "FormattedTextField.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V", DefaultEditorKit.pasteAction, "ctrl X", DefaultEditorKit.cutAction, "COPY", DefaultEditorKit.copyAction, "PASTE", DefaultEditorKit.pasteAction, "CUT", DefaultEditorKit.cutAction, "control INSERT", DefaultEditorKit.copyAction, "shift INSERT", DefaultEditorKit.pasteAction, "shift DELETE", DefaultEditorKit.cutAction, "shift LEFT", DefaultEditorKit.selectionBackwardAction, "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction, "shift RIGHT", DefaultEditorKit.selectionForwardAction, "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, "ctrl LEFT", DefaultEditorKit.previousWordAction, "ctrl KP_LEFT", DefaultEditorKit.previousWordAction, "ctrl RIGHT", DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, "ctrl A", DefaultEditorKit.selectAllAction, "HOME", DefaultEditorKit.beginLineAction, "END", DefaultEditorKit.endLineAction, "shift HOME", DefaultEditorKit.selectionBeginLineAction, "shift END", DefaultEditorKit.selectionEndLineAction, "BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, "ctrl H", DefaultEditorKit.deletePrevCharAction, "DELETE", DefaultEditorKit.deleteNextCharAction, "ctrl DELETE", DefaultEditorKit.deleteNextWordAction, "ctrl BACK_SPACE", DefaultEditorKit.deletePrevWordAction, "RIGHT", DefaultEditorKit.forwardAction, "LEFT", DefaultEditorKit.backwardAction, "KP_RIGHT", DefaultEditorKit.forwardAction, "KP_LEFT", DefaultEditorKit.backwardAction, "ENTER", JTextField.notifyAction, "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation", "ESCAPE", "reset-field-edit", "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement"}), "Button.defaultButtonFollowsFocus", Boolean.FALSE, "Button.disabledText", inactiveControlTextColor, "Button.select", controlShadow, "Button.border", swingLazyValue3, "Button.font", fontActiveValue2, "Button.focus", focusColor, "Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "CheckBox.disabledText", inactiveControlTextColor, "Checkbox.select", controlShadow, "CheckBox.font", fontActiveValue2, "CheckBox.focus", focusColor, "CheckBox.icon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getCheckBoxIcon"), "CheckBox.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "CheckBox.totalInsets", new Insets(4, 4, 4, 4), "RadioButton.disabledText", inactiveControlTextColor, "RadioButton.select", controlShadow, "RadioButton.icon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getRadioButtonIcon"), "RadioButton.font", fontActiveValue2, "RadioButton.focus", focusColor, "RadioButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "RadioButton.totalInsets", new Insets(4, 4, 4, 4), "ToggleButton.select", controlShadow, "ToggleButton.disabledText", inactiveControlTextColor, "ToggleButton.focus", focusColor, "ToggleButton.border", swingLazyValue4, "ToggleButton.font", fontActiveValue2, "ToggleButton.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"SPACE", "pressed", "released SPACE", "released"}), "FileView.directoryIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeFolderIcon"), "FileView.fileIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeLeafIcon"), "FileView.computerIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeComputerIcon"), "FileView.hardDriveIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeHardDriveIcon"), "FileView.floppyDriveIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeFloppyDriveIcon"), "FileChooser.detailsViewIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserDetailViewIcon"), "FileChooser.homeFolderIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserHomeFolderIcon"), "FileChooser.listViewIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserListViewIcon"), "FileChooser.newFolderIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserNewFolderIcon"), "FileChooser.upFolderIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getFileChooserUpFolderIcon"), "FileChooser.usesSingleFilePane", Boolean.TRUE, "FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", FilePane.ACTION_CANCEL, "F2", FilePane.ACTION_EDIT_FILE_NAME, "F5", FilePane.ACTION_REFRESH, "BACK_SPACE", FilePane.ACTION_CHANGE_TO_PARENT_DIRECTORY}), "ToolTip.font", new FontActiveValue(currentTheme, 1), "ToolTip.border", swingLazyValue12, "ToolTip.borderInactive", swingLazyValue13, "ToolTip.backgroundInactive", control, "ToolTip.foregroundInactive", controlDarkShadow, "ToolTip.hideAccelerator", Boolean.FALSE, "ToolTipManager.enableToolTipMode", "activeApplication", "Slider.font", fontActiveValue2, "Slider.border", null, "Slider.foreground", primaryControlShadow, "Slider.focus", focusColor, "Slider.focusInsets", insetsUIResource, "Slider.trackWidth", new Integer(7), "Slider.majorTickLength", new Integer(6), "Slider.horizontalThumbIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getHorizontalSliderThumbIcon"), "Slider.verticalThumbIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getVerticalSliderThumbIcon"), "Slider.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "DOWN", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_DOWN", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "PAGE_DOWN", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "ctrl PAGE_DOWN", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "UP", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_UP", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "PAGE_UP", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "ctrl PAGE_UP", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "HOME", BasicSliderUI.Actions.MIN_SCROLL_INCREMENT, "END", BasicSliderUI.Actions.MAX_SCROLL_INCREMENT}), "ProgressBar.font", fontActiveValue2, "ProgressBar.foreground", primaryControlShadow, "ProgressBar.selectionBackground", primaryControlDarkShadow, "ProgressBar.border", swingLazyValue11, "ProgressBar.cellSpacing", 0, "ProgressBar.cellLength", 1, "ComboBox.background", control, "ComboBox.foreground", controlTextColor, "ComboBox.selectionBackground", primaryControlShadow, "ComboBox.selectionForeground", controlTextColor, "ComboBox.font", fontActiveValue2, "ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "DOWN", "selectNext", "KP_DOWN", "selectNext", "alt DOWN", "togglePopup", "alt KP_DOWN", "togglePopup", "alt UP", "togglePopup", "alt KP_UP", "togglePopup", "SPACE", "spacePopup", "ENTER", "enterPressed", "UP", "selectPrevious", "KP_UP", "selectPrevious"}), "InternalFrame.icon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameDefaultMenuIcon"), "InternalFrame.border", new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$InternalFrameBorder"), "InternalFrame.optionDialogBorder", new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$OptionDialogBorder"), "InternalFrame.paletteBorder", new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$PaletteBorder"), "InternalFrame.paletteTitleHeight", new Integer(11), "InternalFrame.paletteCloseIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory$PaletteCloseIcon"), "InternalFrame.closeIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameCloseIcon", objArr), "InternalFrame.maximizeIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameMaximizeIcon", objArr), "InternalFrame.iconifyIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameMinimizeIcon", objArr), "InternalFrame.minimizeIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getInternalFrameAltMaximizeIcon", objArr), "InternalFrame.titleFont", fontActiveValue4, "InternalFrame.windowBindings", null, "InternalFrame.closeSound", "sounds/FrameClose.wav", "InternalFrame.maximizeSound", "sounds/FrameMaximize.wav", "InternalFrame.minimizeSound", "sounds/FrameMinimize.wav", "InternalFrame.restoreDownSound", "sounds/FrameRestoreDown.wav", "InternalFrame.restoreUpSound", "sounds/FrameRestoreUp.wav", "DesktopIcon.border", swingLazyValue6, "DesktopIcon.font", fontActiveValue2, "DesktopIcon.foreground", controlTextColor, "DesktopIcon.background", control, "DesktopIcon.width", 160, "Desktop.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl F5", "restore", "ctrl F4", "close", "ctrl F7", "move", "ctrl F8", "resize", "RIGHT", JSplitPane.RIGHT, "KP_RIGHT", JSplitPane.RIGHT, "shift RIGHT", "shrinkRight", "shift KP_RIGHT", "shrinkRight", "LEFT", JSplitPane.LEFT, "KP_LEFT", JSplitPane.LEFT, "shift LEFT", "shrinkLeft", "shift KP_LEFT", "shrinkLeft", "UP", "up", "KP_UP", "up", "shift UP", "shrinkUp", "shift KP_UP", "shrinkUp", "DOWN", "down", "KP_DOWN", "down", "shift DOWN", "shrinkDown", "shift KP_DOWN", "shrinkDown", "ESCAPE", "escape", "ctrl F9", "minimize", "ctrl F10", "maximize", "ctrl F6", "selectNextFrame", "ctrl TAB", "selectNextFrame", "ctrl alt F6", "selectNextFrame", "shift ctrl alt F6", "selectPreviousFrame", "ctrl F12", "navigateNext", "shift ctrl F12", "navigatePrevious"}), "TitledBorder.font", fontActiveValue2, "TitledBorder.titleColor", systemTextColor, "TitledBorder.border", swingLazyValue5, "Label.font", fontActiveValue2, "Label.foreground", systemTextColor, "Label.disabledForeground", getInactiveSystemTextColor(), "List.font", fontActiveValue2, "List.focusCellHighlightBorder", swingLazyValue14, "List.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "HOME", "selectFirstRow", "shift HOME", "selectFirstRowExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRowChangeLead", "END", "selectLastRow", "shift END", "selectLastRowExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRowChangeLead", "PAGE_UP", "scrollUp", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDown", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"}), "ScrollBar.background", control, "ScrollBar.highlight", controlHighlight, "ScrollBar.shadow", controlShadow, "ScrollBar.darkShadow", controlDarkShadow, "ScrollBar.thumb", primaryControlShadow, "ScrollBar.thumbShadow", primaryControlDarkShadow, "ScrollBar.thumbHighlight", primaryControl, "ScrollBar.width", new Integer(17), "ScrollBar.allowsAbsolutePositioning", Boolean.TRUE, "ScrollBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_RIGHT", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "DOWN", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "KP_DOWN", BasicSliderUI.Actions.POSITIVE_UNIT_INCREMENT, "PAGE_DOWN", BasicSliderUI.Actions.POSITIVE_BLOCK_INCREMENT, "LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_LEFT", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "UP", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "KP_UP", BasicSliderUI.Actions.NEGATIVE_UNIT_INCREMENT, "PAGE_UP", BasicSliderUI.Actions.NEGATIVE_BLOCK_INCREMENT, "HOME", BasicSliderUI.Actions.MIN_SCROLL_INCREMENT, "END", BasicSliderUI.Actions.MAX_SCROLL_INCREMENT}), "ScrollPane.border", swingLazyValue2, "ScrollPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "unitScrollRight", "KP_RIGHT", "unitScrollRight", "DOWN", "unitScrollDown", "KP_DOWN", "unitScrollDown", "LEFT", "unitScrollLeft", "KP_LEFT", "unitScrollLeft", "UP", "unitScrollUp", "KP_UP", "unitScrollUp", "PAGE_UP", "scrollUp", "PAGE_DOWN", "scrollDown", "ctrl PAGE_UP", "scrollLeft", "ctrl PAGE_DOWN", "scrollRight", "ctrl HOME", "scrollHome", "ctrl END", "scrollEnd"}), "TabbedPane.font", fontActiveValue2, "TabbedPane.tabAreaBackground", control, "TabbedPane.background", controlShadow, "TabbedPane.light", control, "TabbedPane.focus", primaryControlDarkShadow, "TabbedPane.selected", control, "TabbedPane.selectHighlight", controlHighlight, "TabbedPane.tabAreaInsets", insetsUIResource2, "TabbedPane.tabInsets", insetsUIResource3, "TabbedPane.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"RIGHT", "navigateRight", "KP_RIGHT", "navigateRight", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "ctrl DOWN", "requestFocusForVisibleComponent", "ctrl KP_DOWN", "requestFocusForVisibleComponent"}), "TabbedPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl PAGE_DOWN", "navigatePageDown", "ctrl PAGE_UP", "navigatePageUp", "ctrl UP", "requestFocus", "ctrl KP_UP", "requestFocus"}), "Table.font", fontActiveValue3, "Table.focusCellHighlightBorder", swingLazyValue14, "Table.scrollPaneBorder", swingLazyValue2, "Table.dropLineColor", focusColor, "Table.dropLineShortColor", primaryControlDarkShadow, "Table.gridColor", controlShadow, "Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo", "F8", "focusHeader"}), "Table.ascendingSortIcon", SwingUtilities2.makeIcon(getClass(), MetalLookAndFeel.class, "icons/sortUp.png"), "Table.descendingSortIcon", SwingUtilities2.makeIcon(getClass(), MetalLookAndFeel.class, "icons/sortDown.png"), "TableHeader.font", fontActiveValue3, "TableHeader.cellBorder", new SwingLazyValue("javax.swing.plaf.metal.MetalBorders$TableHeaderBorder"), "MenuBar.border", swingLazyValue7, "MenuBar.font", fontActiveValue, "MenuBar.windowBindings", new Object[]{"F10", "takeFocus"}, "Menu.border", swingLazyValue9, "Menu.borderPainted", Boolean.TRUE, "Menu.menuPopupOffsetX", 0, "Menu.menuPopupOffsetY", 0, "Menu.submenuPopupOffsetX", new Integer(-4), "Menu.submenuPopupOffsetY", new Integer(-3), "Menu.font", fontActiveValue, "Menu.selectionForeground", menuSelectedForeground, "Menu.selectionBackground", menuSelectedBackground, "Menu.disabledForeground", menuDisabledForeground, "Menu.acceleratorFont", fontActiveValue5, "Menu.acceleratorForeground", acceleratorForeground, "Menu.acceleratorSelectionForeground", acceleratorSelectedForeground, "Menu.checkIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemCheckIcon"), "Menu.arrowIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuArrowIcon"), "MenuItem.border", swingLazyValue9, "MenuItem.borderPainted", Boolean.TRUE, "MenuItem.font", fontActiveValue, "MenuItem.selectionForeground", menuSelectedForeground, "MenuItem.selectionBackground", menuSelectedBackground, "MenuItem.disabledForeground", menuDisabledForeground, "MenuItem.acceleratorFont", fontActiveValue5, "MenuItem.acceleratorForeground", acceleratorForeground, "MenuItem.acceleratorSelectionForeground", acceleratorSelectedForeground, "MenuItem.acceleratorDelimiter", LanguageTag.SEP, "MenuItem.checkIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemCheckIcon"), "MenuItem.arrowIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemArrowIcon"), "MenuItem.commandSound", "sounds/MenuItemCommand.wav", "OptionPane.windowBindings", new Object[]{"ESCAPE", "close"}, "OptionPane.informationSound", "sounds/OptionPaneInformation.wav", "OptionPane.warningSound", "sounds/OptionPaneWarning.wav", "OptionPane.errorSound", "sounds/OptionPaneError.wav", "OptionPane.questionSound", "sounds/OptionPaneQuestion.wav", "OptionPane.errorDialog.border.background", new ColorUIResource(153, 51, 51), "OptionPane.errorDialog.titlePane.foreground", new ColorUIResource(51, 0, 0), "OptionPane.errorDialog.titlePane.background", new ColorUIResource(255, 153, 153), "OptionPane.errorDialog.titlePane.shadow", new ColorUIResource(204, 102, 102), "OptionPane.questionDialog.border.background", new ColorUIResource(51, 102, 51), "OptionPane.questionDialog.titlePane.foreground", new ColorUIResource(0, 51, 0), "OptionPane.questionDialog.titlePane.background", new ColorUIResource(153, 204, 153), "OptionPane.questionDialog.titlePane.shadow", new ColorUIResource(102, 153, 102), "OptionPane.warningDialog.border.background", new ColorUIResource(153, 102, 51), "OptionPane.warningDialog.titlePane.foreground", new ColorUIResource(102, 51, 0), "OptionPane.warningDialog.titlePane.background", new ColorUIResource(255, 204, 153), "OptionPane.warningDialog.titlePane.shadow", new ColorUIResource(204, 153, 102), "Separator.background", getSeparatorBackground(), "Separator.foreground", getSeparatorForeground(), "PopupMenu.border", swingLazyValue8, "PopupMenu.popupSound", "sounds/PopupMenuPopup.wav", "PopupMenu.font", fontActiveValue, "CheckBoxMenuItem.border", swingLazyValue9, "CheckBoxMenuItem.borderPainted", Boolean.TRUE, "CheckBoxMenuItem.font", fontActiveValue, "CheckBoxMenuItem.selectionForeground", menuSelectedForeground, "CheckBoxMenuItem.selectionBackground", menuSelectedBackground, "CheckBoxMenuItem.disabledForeground", menuDisabledForeground, "CheckBoxMenuItem.acceleratorFont", fontActiveValue5, "CheckBoxMenuItem.acceleratorForeground", acceleratorForeground, "CheckBoxMenuItem.acceleratorSelectionForeground", acceleratorSelectedForeground, "CheckBoxMenuItem.checkIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getCheckBoxMenuItemIcon"), "CheckBoxMenuItem.arrowIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemArrowIcon"), "CheckBoxMenuItem.commandSound", "sounds/MenuItemCommand.wav", "RadioButtonMenuItem.border", swingLazyValue9, "RadioButtonMenuItem.borderPainted", Boolean.TRUE, "RadioButtonMenuItem.font", fontActiveValue, "RadioButtonMenuItem.selectionForeground", menuSelectedForeground, "RadioButtonMenuItem.selectionBackground", menuSelectedBackground, "RadioButtonMenuItem.disabledForeground", menuDisabledForeground, "RadioButtonMenuItem.acceleratorFont", fontActiveValue5, "RadioButtonMenuItem.acceleratorForeground", acceleratorForeground, "RadioButtonMenuItem.acceleratorSelectionForeground", acceleratorSelectedForeground, "RadioButtonMenuItem.checkIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getRadioButtonMenuItemIcon"), "RadioButtonMenuItem.arrowIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getMenuItemArrowIcon"), "RadioButtonMenuItem.commandSound", "sounds/MenuItemCommand.wav", "Spinner.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement"}), "Spinner.arrowButtonInsets", insetsUIResource, "Spinner.border", swingLazyValue, "Spinner.arrowButtonBorder", swingLazyValue3, "Spinner.font", fontActiveValue2, "SplitPane.dividerSize", new Integer(10), "SplitPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "negativeIncrement", "DOWN", "positiveIncrement", "LEFT", "negativeIncrement", "RIGHT", "positiveIncrement", "KP_UP", "negativeIncrement", "KP_DOWN", "positiveIncrement", "KP_LEFT", "negativeIncrement", "KP_RIGHT", "positiveIncrement", "HOME", "selectMin", "END", "selectMax", "F8", "startResize", "F6", "toggleFocus", "ctrl TAB", "focusOutForward", "ctrl shift TAB", "focusOutBackward"}), "SplitPane.centerOneTouchButtons", Boolean.FALSE, "SplitPane.dividerFocusColor", primaryControl, "Tree.font", fontActiveValue3, "Tree.textBackground", getWindowBackground(), "Tree.selectionBorderColor", focusColor, "Tree.openIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeFolderIcon"), "Tree.closedIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeFolderIcon"), "Tree.leafIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeLeafIcon"), "Tree.expandedIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeControlIcon", new Object[]{false}), "Tree.collapsedIcon", new SwingLazyValue("javax.swing.plaf.metal.MetalIconFactory", "getTreeControlIcon", new Object[]{true}), "Tree.line", primaryControl, "Tree.hash", primaryControl, "Tree.rowHeight", 0, "Tree.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ADD", "expand", "SUBTRACT", SchemaSymbols.ATTVAL_COLLAPSE, "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPrevious", "KP_UP", "selectPrevious", "shift UP", "selectPreviousExtendSelection", "shift KP_UP", "selectPreviousExtendSelection", "ctrl shift UP", "selectPreviousExtendSelection", "ctrl shift KP_UP", "selectPreviousExtendSelection", "ctrl UP", "selectPreviousChangeLead", "ctrl KP_UP", "selectPreviousChangeLead", "DOWN", "selectNext", "KP_DOWN", "selectNext", "shift DOWN", "selectNextExtendSelection", "shift KP_DOWN", "selectNextExtendSelection", "ctrl shift DOWN", "selectNextExtendSelection", "ctrl shift KP_DOWN", "selectNextExtendSelection", "ctrl DOWN", "selectNextChangeLead", "ctrl KP_DOWN", "selectNextChangeLead", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "LEFT", "selectParent", "KP_LEFT", "selectParent", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "HOME", "selectFirst", "shift HOME", "selectFirstExtendSelection", "ctrl shift HOME", "selectFirstExtendSelection", "ctrl HOME", "selectFirstChangeLead", "END", "selectLast", "shift END", "selectLastExtendSelection", "ctrl shift END", "selectLastExtendSelection", "ctrl END", "selectLastChangeLead", "F2", "startEditing", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ctrl LEFT", "scrollLeft", "ctrl KP_LEFT", "scrollLeft", "ctrl RIGHT", "scrollRight", "ctrl KP_RIGHT", "scrollRight", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"}), "Tree.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"ESCAPE", "cancel"}), "ToolBar.border", swingLazyValue10, "ToolBar.background", menuBackground, "ToolBar.foreground", getMenuForeground(), "ToolBar.font", fontActiveValue, "ToolBar.dockingBackground", menuBackground, "ToolBar.floatingBackground", menuBackground, "ToolBar.dockingForeground", primaryControlDarkShadow, "ToolBar.floatingForeground", primaryControl, "ToolBar.rolloverBorder", uIDefaults4 -> {
            return MetalBorders.getToolBarRolloverBorder();
        }, "ToolBar.nonrolloverBorder", uIDefaults5 -> {
            return MetalBorders.getToolBarNonrolloverBorder();
        }, "ToolBar.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[]{"UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight"}), "RootPane.frameBorder", uIDefaults6 -> {
            return new MetalBorders.FrameBorder();
        }, "RootPane.plainDialogBorder", obj, "RootPane.informationDialogBorder", obj, "RootPane.errorDialogBorder", uIDefaults7 -> {
            return new MetalBorders.ErrorDialogBorder();
        }, "RootPane.colorChooserDialogBorder", obj2, "RootPane.fileChooserDialogBorder", obj2, "RootPane.questionDialogBorder", obj2, "RootPane.warningDialogBorder", uIDefaults8 -> {
            return new MetalBorders.WarningDialogBorder();
        }, "RootPane.defaultButtonWindowKeyBindings", new Object[]{"ENTER", BasicRootPaneUI.Actions.PRESS, "released ENTER", BasicRootPaneUI.Actions.RELEASE, "ctrl ENTER", BasicRootPaneUI.Actions.PRESS, "ctrl released ENTER", BasicRootPaneUI.Actions.RELEASE}});
        if (isWindows() && useSystemFonts() && currentTheme.isSystemTheme()) {
            Object metalFontDesktopProperty = new MetalFontDesktopProperty("win.messagebox.font.height", 0);
            uIDefaults.putDefaults(new Object[]{"OptionPane.messageFont", metalFontDesktopProperty, "OptionPane.buttonFont", metalFontDesktopProperty});
        }
        flushUnreferenced();
        uIDefaults.put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, SwingUtilities2.AATextInfo.getAATextInfo(SwingUtilities2.isLocalDisplay()));
        new AATextListener(this);
    }

    protected void createDefaultTheme() {
        getCurrentTheme();
    }

    @Override // javax.swing.plaf.basic.BasicLookAndFeel, javax.swing.LookAndFeel
    public UIDefaults getDefaults() {
        METAL_LOOK_AND_FEEL_INITED = true;
        createDefaultTheme();
        UIDefaults defaults = super.getDefaults();
        MetalTheme currentTheme = getCurrentTheme();
        currentTheme.addCustomEntriesToTable(defaults);
        currentTheme.install();
        return defaults;
    }

    @Override // javax.swing.LookAndFeel
    public void provideErrorFeedback(Component component) {
        super.provideErrorFeedback(component);
    }

    public static void setCurrentTheme(MetalTheme metalTheme) {
        if (metalTheme == null) {
            throw new NullPointerException("Can't have null theme");
        }
        AppContext.getAppContext().put("currentMetalTheme", metalTheme);
    }

    public static MetalTheme getCurrentTheme() {
        MetalTheme oceanTheme = (MetalTheme) AppContext.getAppContext().get("currentMetalTheme");
        if (oceanTheme == null) {
            if (useHighContrastTheme()) {
                oceanTheme = new MetalHighContrastTheme();
            } else if ("steel".equals((String) AccessController.doPrivileged(new GetPropertyAction("swing.metalTheme")))) {
                oceanTheme = new DefaultMetalTheme();
            } else {
                oceanTheme = new OceanTheme();
            }
            setCurrentTheme(oceanTheme);
        }
        return oceanTheme;
    }

    @Override // javax.swing.LookAndFeel
    public Icon getDisabledIcon(JComponent jComponent, Icon icon) {
        if ((icon instanceof ImageIcon) && usingOcean()) {
            return MetalUtils.getOceanDisabledButtonIcon(((ImageIcon) icon).getImage());
        }
        return super.getDisabledIcon(jComponent, icon);
    }

    @Override // javax.swing.LookAndFeel
    public Icon getDisabledSelectedIcon(JComponent jComponent, Icon icon) {
        if ((icon instanceof ImageIcon) && usingOcean()) {
            return MetalUtils.getOceanDisabledButtonIcon(((ImageIcon) icon).getImage());
        }
        return super.getDisabledSelectedIcon(jComponent, icon);
    }

    public static FontUIResource getControlTextFont() {
        return getCurrentTheme().getControlTextFont();
    }

    public static FontUIResource getSystemTextFont() {
        return getCurrentTheme().getSystemTextFont();
    }

    public static FontUIResource getUserTextFont() {
        return getCurrentTheme().getUserTextFont();
    }

    public static FontUIResource getMenuTextFont() {
        return getCurrentTheme().getMenuTextFont();
    }

    public static FontUIResource getWindowTitleFont() {
        return getCurrentTheme().getWindowTitleFont();
    }

    public static FontUIResource getSubTextFont() {
        return getCurrentTheme().getSubTextFont();
    }

    public static ColorUIResource getDesktopColor() {
        return getCurrentTheme().getDesktopColor();
    }

    public static ColorUIResource getFocusColor() {
        return getCurrentTheme().getFocusColor();
    }

    public static ColorUIResource getWhite() {
        return getCurrentTheme().getWhite();
    }

    public static ColorUIResource getBlack() {
        return getCurrentTheme().getBlack();
    }

    public static ColorUIResource getControl() {
        return getCurrentTheme().getControl();
    }

    public static ColorUIResource getControlShadow() {
        return getCurrentTheme().getControlShadow();
    }

    public static ColorUIResource getControlDarkShadow() {
        return getCurrentTheme().getControlDarkShadow();
    }

    public static ColorUIResource getControlInfo() {
        return getCurrentTheme().getControlInfo();
    }

    public static ColorUIResource getControlHighlight() {
        return getCurrentTheme().getControlHighlight();
    }

    public static ColorUIResource getControlDisabled() {
        return getCurrentTheme().getControlDisabled();
    }

    public static ColorUIResource getPrimaryControl() {
        return getCurrentTheme().getPrimaryControl();
    }

    public static ColorUIResource getPrimaryControlShadow() {
        return getCurrentTheme().getPrimaryControlShadow();
    }

    public static ColorUIResource getPrimaryControlDarkShadow() {
        return getCurrentTheme().getPrimaryControlDarkShadow();
    }

    public static ColorUIResource getPrimaryControlInfo() {
        return getCurrentTheme().getPrimaryControlInfo();
    }

    public static ColorUIResource getPrimaryControlHighlight() {
        return getCurrentTheme().getPrimaryControlHighlight();
    }

    public static ColorUIResource getSystemTextColor() {
        return getCurrentTheme().getSystemTextColor();
    }

    public static ColorUIResource getControlTextColor() {
        return getCurrentTheme().getControlTextColor();
    }

    public static ColorUIResource getInactiveControlTextColor() {
        return getCurrentTheme().getInactiveControlTextColor();
    }

    public static ColorUIResource getInactiveSystemTextColor() {
        return getCurrentTheme().getInactiveSystemTextColor();
    }

    public static ColorUIResource getUserTextColor() {
        return getCurrentTheme().getUserTextColor();
    }

    public static ColorUIResource getTextHighlightColor() {
        return getCurrentTheme().getTextHighlightColor();
    }

    public static ColorUIResource getHighlightedTextColor() {
        return getCurrentTheme().getHighlightedTextColor();
    }

    public static ColorUIResource getWindowBackground() {
        return getCurrentTheme().getWindowBackground();
    }

    public static ColorUIResource getWindowTitleBackground() {
        return getCurrentTheme().getWindowTitleBackground();
    }

    public static ColorUIResource getWindowTitleForeground() {
        return getCurrentTheme().getWindowTitleForeground();
    }

    public static ColorUIResource getWindowTitleInactiveBackground() {
        return getCurrentTheme().getWindowTitleInactiveBackground();
    }

    public static ColorUIResource getWindowTitleInactiveForeground() {
        return getCurrentTheme().getWindowTitleInactiveForeground();
    }

    public static ColorUIResource getMenuBackground() {
        return getCurrentTheme().getMenuBackground();
    }

    public static ColorUIResource getMenuForeground() {
        return getCurrentTheme().getMenuForeground();
    }

    public static ColorUIResource getMenuSelectedBackground() {
        return getCurrentTheme().getMenuSelectedBackground();
    }

    public static ColorUIResource getMenuSelectedForeground() {
        return getCurrentTheme().getMenuSelectedForeground();
    }

    public static ColorUIResource getMenuDisabledForeground() {
        return getCurrentTheme().getMenuDisabledForeground();
    }

    public static ColorUIResource getSeparatorBackground() {
        return getCurrentTheme().getSeparatorBackground();
    }

    public static ColorUIResource getSeparatorForeground() {
        return getCurrentTheme().getSeparatorForeground();
    }

    public static ColorUIResource getAcceleratorForeground() {
        return getCurrentTheme().getAcceleratorForeground();
    }

    public static ColorUIResource getAcceleratorSelectedForeground() {
        return getCurrentTheme().getAcceleratorSelectedForeground();
    }

    @Override // javax.swing.LookAndFeel
    public LayoutStyle getLayoutStyle() {
        return MetalLayoutStyle.INSTANCE;
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalLookAndFeel$FontActiveValue.class */
    private static class FontActiveValue implements UIDefaults.ActiveValue {
        private int type;
        private MetalTheme theme;

        FontActiveValue(MetalTheme metalTheme, int i2) {
            this.theme = metalTheme;
            this.type = i2;
        }

        @Override // javax.swing.UIDefaults.ActiveValue
        public Object createValue(UIDefaults uIDefaults) {
            FontUIResource subTextFont = null;
            switch (this.type) {
                case 0:
                    subTextFont = this.theme.getControlTextFont();
                    break;
                case 1:
                    subTextFont = this.theme.getSystemTextFont();
                    break;
                case 2:
                    subTextFont = this.theme.getUserTextFont();
                    break;
                case 3:
                    subTextFont = this.theme.getMenuTextFont();
                    break;
                case 4:
                    subTextFont = this.theme.getWindowTitleFont();
                    break;
                case 5:
                    subTextFont = this.theme.getSubTextFont();
                    break;
            }
            return subTextFont;
        }
    }

    static void flushUnreferenced() {
        while (true) {
            AATextListener aATextListener = (AATextListener) queue.poll();
            if (aATextListener != null) {
                aATextListener.dispose();
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalLookAndFeel$AATextListener.class */
    static class AATextListener extends WeakReference<LookAndFeel> implements PropertyChangeListener {
        private String key;
        private static boolean updatePending;

        AATextListener(LookAndFeel lookAndFeel) {
            super(lookAndFeel, MetalLookAndFeel.queue);
            this.key = SunToolkit.DESKTOPFONTHINTS;
            Toolkit.getDefaultToolkit().addPropertyChangeListener(this.key, this);
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            LookAndFeel lookAndFeel = get();
            if (lookAndFeel == null || lookAndFeel != UIManager.getLookAndFeel()) {
                dispose();
                return;
            }
            UIManager.getLookAndFeelDefaults().put(SwingUtilities2.AA_TEXT_PROPERTY_KEY, SwingUtilities2.AATextInfo.getAATextInfo(SwingUtilities2.isLocalDisplay()));
            updateUI();
        }

        void dispose() {
            Toolkit.getDefaultToolkit().removePropertyChangeListener(this.key, this);
        }

        private static void updateWindowUI(Window window) {
            SwingUtilities.updateComponentTreeUI(window);
            for (Window window2 : window.getOwnedWindows()) {
                updateWindowUI(window2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void updateAllUIs() {
            for (Frame frame : Frame.getFrames()) {
                updateWindowUI(frame);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized void setUpdatePending(boolean z2) {
            updatePending = z2;
        }

        private static synchronized boolean isUpdatePending() {
            return updatePending;
        }

        protected void updateUI() {
            if (!isUpdatePending()) {
                setUpdatePending(true);
                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.plaf.metal.MetalLookAndFeel.AATextListener.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AATextListener.updateAllUIs();
                        AATextListener.setUpdatePending(false);
                    }
                });
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalLookAndFeel$MetalLayoutStyle.class */
    private static class MetalLayoutStyle extends DefaultLayoutStyle {
        private static MetalLayoutStyle INSTANCE = new MetalLayoutStyle();

        private MetalLayoutStyle() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00c6  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00d4  */
        @Override // sun.swing.DefaultLayoutStyle, javax.swing.LayoutStyle
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int getPreferredGap(javax.swing.JComponent r8, javax.swing.JComponent r9, javax.swing.LayoutStyle.ComponentPlacement r10, int r11, java.awt.Container r12) {
            /*
                r7 = this;
                r0 = r7
                r1 = r8
                r2 = r9
                r3 = r10
                r4 = r11
                r5 = r12
                int r0 = super.getPreferredGap(r1, r2, r3, r4, r5)
                r0 = 0
                r13 = r0
                int[] r0 = javax.swing.plaf.metal.MetalLookAndFeel.AnonymousClass1.$SwitchMap$javax$swing$LayoutStyle$ComponentPlacement
                r1 = r10
                int r1 = r1.ordinal()
                r0 = r0[r1]
                switch(r0) {
                    case 1: goto L30;
                    case 2: goto L51;
                    case 3: goto Lb7;
                    default: goto Lbb;
                }
            L30:
                r0 = r11
                r1 = 3
                if (r0 == r1) goto L3d
                r0 = r11
                r1 = 7
                if (r0 != r1) goto L51
            L3d:
                r0 = r7
                r1 = r8
                r2 = r11
                int r0 = r0.getIndent(r1, r2)
                r14 = r0
                r0 = r14
                if (r0 <= 0) goto L4e
                r0 = r14
                return r0
            L4e:
                r0 = 12
                return r0
            L51:
                r0 = r8
                java.lang.String r0 = r0.getUIClassID()
                java.lang.String r1 = "ToggleButtonUI"
                if (r0 != r1) goto Lb0
                r0 = r9
                java.lang.String r0 = r0.getUIClassID()
                java.lang.String r1 = "ToggleButtonUI"
                if (r0 != r1) goto Lb0
                r0 = r8
                javax.swing.JToggleButton r0 = (javax.swing.JToggleButton) r0
                javax.swing.ButtonModel r0 = r0.getModel()
                r14 = r0
                r0 = r9
                javax.swing.JToggleButton r0 = (javax.swing.JToggleButton) r0
                javax.swing.ButtonModel r0 = r0.getModel()
                r15 = r0
                r0 = r14
                boolean r0 = r0 instanceof javax.swing.DefaultButtonModel
                if (r0 == 0) goto La5
                r0 = r15
                boolean r0 = r0 instanceof javax.swing.DefaultButtonModel
                if (r0 == 0) goto La5
                r0 = r14
                javax.swing.DefaultButtonModel r0 = (javax.swing.DefaultButtonModel) r0
                javax.swing.ButtonGroup r0 = r0.getGroup()
                r1 = r15
                javax.swing.DefaultButtonModel r1 = (javax.swing.DefaultButtonModel) r1
                javax.swing.ButtonGroup r1 = r1.getGroup()
                if (r0 != r1) goto La5
                r0 = r14
                javax.swing.DefaultButtonModel r0 = (javax.swing.DefaultButtonModel) r0
                javax.swing.ButtonGroup r0 = r0.getGroup()
                if (r0 == 0) goto La5
                r0 = 2
                return r0
            La5:
                boolean r0 = javax.swing.plaf.metal.MetalLookAndFeel.usingOcean()
                if (r0 == 0) goto Lae
                r0 = 6
                return r0
            Lae:
                r0 = 5
                return r0
            Lb0:
                r0 = 6
                r13 = r0
                goto Lbb
            Lb7:
                r0 = 12
                r13 = r0
            Lbb:
                r0 = r7
                r1 = r8
                r2 = r9
                r3 = r11
                boolean r0 = r0.isLabelAndNonlabel(r1, r2, r3)
                if (r0 == 0) goto Ld4
                r0 = r7
                r1 = r8
                r2 = r9
                r3 = r11
                r4 = r13
                r5 = 6
                int r4 = r4 + r5
                int r0 = r0.getButtonGap(r1, r2, r3, r4)
                return r0
            Ld4:
                r0 = r7
                r1 = r8
                r2 = r9
                r3 = r11
                r4 = r13
                int r0 = r0.getButtonGap(r1, r2, r3, r4)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.metal.MetalLookAndFeel.MetalLayoutStyle.getPreferredGap(javax.swing.JComponent, javax.swing.JComponent, javax.swing.LayoutStyle$ComponentPlacement, int, java.awt.Container):int");
        }

        @Override // sun.swing.DefaultLayoutStyle, javax.swing.LayoutStyle
        public int getContainerGap(JComponent jComponent, int i2, Container container) {
            super.getContainerGap(jComponent, i2, container);
            return getButtonGap(jComponent, i2, 12 - getButtonAdjustment(jComponent, i2));
        }

        @Override // sun.swing.DefaultLayoutStyle
        protected int getButtonGap(JComponent jComponent, JComponent jComponent2, int i2, int i3) {
            int buttonGap = super.getButtonGap(jComponent, jComponent2, i2, i3);
            if (buttonGap > 0) {
                int buttonAdjustment = getButtonAdjustment(jComponent, i2);
                if (buttonAdjustment == 0) {
                    buttonAdjustment = getButtonAdjustment(jComponent2, flipDirection(i2));
                }
                buttonGap -= buttonAdjustment;
            }
            if (buttonGap < 0) {
                return 0;
            }
            return buttonGap;
        }

        private int getButtonAdjustment(JComponent jComponent, int i2) {
            String uIClassID = jComponent.getUIClassID();
            if (uIClassID == "ButtonUI" || uIClassID == "ToggleButtonUI") {
                if (!MetalLookAndFeel.usingOcean()) {
                    if ((i2 == 3 || i2 == 5) && (jComponent.getBorder() instanceof UIResource)) {
                        return 1;
                    }
                    return 0;
                }
                return 0;
            }
            if (i2 == 5) {
                if ((uIClassID == "RadioButtonUI" || uIClassID == "CheckBoxUI") && !MetalLookAndFeel.usingOcean()) {
                    return 1;
                }
                return 0;
            }
            return 0;
        }
    }
}
