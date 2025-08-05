package javax.swing.plaf.multi;

import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/plaf/multi/MultiLookAndFeel.class */
public class MultiLookAndFeel extends LookAndFeel {
    @Override // javax.swing.LookAndFeel
    public String getName() {
        return "Multiplexing Look and Feel";
    }

    @Override // javax.swing.LookAndFeel
    public String getID() {
        return "Multiplex";
    }

    @Override // javax.swing.LookAndFeel
    public String getDescription() {
        return "Allows multiple UI instances per component instance";
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
    public UIDefaults getDefaults() {
        Object[] objArr = {"ButtonUI", "javax.swing.plaf.multi.MultiButtonUI", "CheckBoxMenuItemUI", "javax.swing.plaf.multi.MultiMenuItemUI", "CheckBoxUI", "javax.swing.plaf.multi.MultiButtonUI", "ColorChooserUI", "javax.swing.plaf.multi.MultiColorChooserUI", "ComboBoxUI", "javax.swing.plaf.multi.MultiComboBoxUI", "DesktopIconUI", "javax.swing.plaf.multi.MultiDesktopIconUI", "DesktopPaneUI", "javax.swing.plaf.multi.MultiDesktopPaneUI", "EditorPaneUI", "javax.swing.plaf.multi.MultiTextUI", "FileChooserUI", "javax.swing.plaf.multi.MultiFileChooserUI", "FormattedTextFieldUI", "javax.swing.plaf.multi.MultiTextUI", "InternalFrameUI", "javax.swing.plaf.multi.MultiInternalFrameUI", "LabelUI", "javax.swing.plaf.multi.MultiLabelUI", "ListUI", "javax.swing.plaf.multi.MultiListUI", "MenuBarUI", "javax.swing.plaf.multi.MultiMenuBarUI", "MenuItemUI", "javax.swing.plaf.multi.MultiMenuItemUI", "MenuUI", "javax.swing.plaf.multi.MultiMenuItemUI", "OptionPaneUI", "javax.swing.plaf.multi.MultiOptionPaneUI", "PanelUI", "javax.swing.plaf.multi.MultiPanelUI", "PasswordFieldUI", "javax.swing.plaf.multi.MultiTextUI", "PopupMenuSeparatorUI", "javax.swing.plaf.multi.MultiSeparatorUI", "PopupMenuUI", "javax.swing.plaf.multi.MultiPopupMenuUI", "ProgressBarUI", "javax.swing.plaf.multi.MultiProgressBarUI", "RadioButtonMenuItemUI", "javax.swing.plaf.multi.MultiMenuItemUI", "RadioButtonUI", "javax.swing.plaf.multi.MultiButtonUI", "RootPaneUI", "javax.swing.plaf.multi.MultiRootPaneUI", "ScrollBarUI", "javax.swing.plaf.multi.MultiScrollBarUI", "ScrollPaneUI", "javax.swing.plaf.multi.MultiScrollPaneUI", "SeparatorUI", "javax.swing.plaf.multi.MultiSeparatorUI", "SliderUI", "javax.swing.plaf.multi.MultiSliderUI", "SpinnerUI", "javax.swing.plaf.multi.MultiSpinnerUI", "SplitPaneUI", "javax.swing.plaf.multi.MultiSplitPaneUI", "TabbedPaneUI", "javax.swing.plaf.multi.MultiTabbedPaneUI", "TableHeaderUI", "javax.swing.plaf.multi.MultiTableHeaderUI", "TableUI", "javax.swing.plaf.multi.MultiTableUI", "TextAreaUI", "javax.swing.plaf.multi.MultiTextUI", "TextFieldUI", "javax.swing.plaf.multi.MultiTextUI", "TextPaneUI", "javax.swing.plaf.multi.MultiTextUI", "ToggleButtonUI", "javax.swing.plaf.multi.MultiButtonUI", "ToolBarSeparatorUI", "javax.swing.plaf.multi.MultiSeparatorUI", "ToolBarUI", "javax.swing.plaf.multi.MultiToolBarUI", "ToolTipUI", "javax.swing.plaf.multi.MultiToolTipUI", "TreeUI", "javax.swing.plaf.multi.MultiTreeUI", "ViewportUI", "javax.swing.plaf.multi.MultiViewportUI"};
        MultiUIDefaults multiUIDefaults = new MultiUIDefaults(objArr.length / 2, 0.75f);
        multiUIDefaults.putDefaults(objArr);
        return multiUIDefaults;
    }

    public static ComponentUI createUIs(ComponentUI componentUI, Vector vector, JComponent jComponent) {
        ComponentUI ui = UIManager.getDefaults().getUI(jComponent);
        if (ui != null) {
            vector.addElement(ui);
            LookAndFeel[] auxiliaryLookAndFeels = UIManager.getAuxiliaryLookAndFeels();
            if (auxiliaryLookAndFeels != null) {
                for (LookAndFeel lookAndFeel : auxiliaryLookAndFeels) {
                    ComponentUI ui2 = lookAndFeel.getDefaults().getUI(jComponent);
                    if (ui2 != null) {
                        vector.addElement(ui2);
                    }
                }
            }
            if (vector.size() == 1) {
                return (ComponentUI) vector.elementAt(0);
            }
            return componentUI;
        }
        return null;
    }

    protected static ComponentUI[] uisToArray(Vector vector) {
        if (vector == null) {
            return new ComponentUI[0];
        }
        int size = vector.size();
        if (size > 0) {
            ComponentUI[] componentUIArr = new ComponentUI[size];
            for (int i2 = 0; i2 < size; i2++) {
                componentUIArr[i2] = (ComponentUI) vector.elementAt(i2);
            }
            return componentUIArr;
        }
        return null;
    }
}
