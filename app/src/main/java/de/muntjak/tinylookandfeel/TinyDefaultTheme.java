package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.util.DrawRoutines;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyDefaultTheme.class */
public class TinyDefaultTheme extends DefaultMetalTheme {
    @Override // javax.swing.plaf.metal.MetalTheme
    public void addCustomEntriesToTable(UIDefaults uIDefaults) {
        super.addCustomEntriesToTable(uIDefaults);
        uIDefaults.put("Button.margin", Theme.buttonMargin);
        uIDefaults.put("CheckBox.margin", Theme.checkMargin);
        uIDefaults.put("RadioButton.margin", Theme.checkMargin);
        uIDefaults.put("Button.background", Theme.buttonNormalColor.getColor());
        uIDefaults.put("Button.font", Theme.buttonFont.getFont());
        uIDefaults.put("CheckBox.font", Theme.checkFont.getFont());
        uIDefaults.put("CheckBoxMenuItem.font", Theme.menuItemFont.getFont());
        uIDefaults.put("ComboBox.font", Theme.comboFont.getFont());
        uIDefaults.put("Label.font", Theme.labelFont.getFont());
        uIDefaults.put("List.font", Theme.listFont.getFont());
        uIDefaults.put("Menu.font", Theme.menuFont.getFont());
        uIDefaults.put("MenuItem.font", Theme.menuItemFont.getFont());
        uIDefaults.put("ProgressBar.font", Theme.progressBarFont.getFont());
        uIDefaults.put("RadioButton.font", Theme.radioFont.getFont());
        uIDefaults.put("RadioButtonMenuItem.font", Theme.menuItemFont.getFont());
        uIDefaults.put("Table.font", Theme.tableFont.getFont());
        uIDefaults.put("TableHeader.font", Theme.tableHeaderFont.getFont());
        uIDefaults.put("TitledBorder.font", Theme.titledBorderFont.getFont());
        uIDefaults.put("ToolTip.font", Theme.toolTipFont.getFont());
        uIDefaults.put("Tree.font", Theme.treeFont.getFont());
        uIDefaults.put("PasswordField.font", Theme.passwordFont.getFont());
        uIDefaults.put("TextArea.font", Theme.textAreaFont.getFont());
        uIDefaults.put("TextField.font", Theme.textFieldFont.getFont());
        uIDefaults.put("FormattedTextField.font", Theme.textFieldFont.getFont());
        uIDefaults.put("TextPane.font", Theme.textPaneFont.getFont());
        uIDefaults.put("EditorPane.font", Theme.editorFont.getFont());
        uIDefaults.put("InternalFrame.font", Theme.editorFont.getFont());
        uIDefaults.put("InternalFrame.normalTitleFont", Theme.internalFrameTitleFont.getFont());
        uIDefaults.put("InternalFrame.paletteTitleFont", Theme.internalPaletteTitleFont.getFont());
        uIDefaults.put("Frame.titleFont", Theme.frameTitleFont.getFont());
        uIDefaults.put("TabbedPane.font", Theme.tabFont.getFont());
        uIDefaults.put("Button.foreground", Theme.buttonFontColor.getColor());
        uIDefaults.put("CheckBox.foreground", Theme.checkFontColor.getColor());
        uIDefaults.put("Menu.foreground", Theme.menuFontColor.getColor());
        uIDefaults.put("MenuItem.foreground", Theme.menuItemFontColor.getColor());
        uIDefaults.put("CheckBoxMenuItem.foreground", Theme.menuItemFontColor.getColor());
        uIDefaults.put("RadioButtonMenuItem.foreground", Theme.menuItemFontColor.getColor());
        uIDefaults.put("RadioButton.foreground", Theme.radioFontColor.getColor());
        uIDefaults.put("TabbedPane.foreground", Theme.tabFontColor.getColor());
        uIDefaults.put("TitledBorder.titleColor", Theme.titledBorderFontColor.getColor());
        uIDefaults.put("Label.foreground", Theme.labelFontColor.getColor());
        uIDefaults.put("TableHeader.foreground", Theme.tableHeaderFontColor.getColor());
        uIDefaults.put("TableHeader.background", Theme.tableHeaderBackColor.getColor());
        uIDefaults.put("Table.foreground", Theme.tableFontColor.getColor());
        uIDefaults.put("Table.background", Theme.tableBackColor.getColor());
        uIDefaults.put("Table.selectionForeground", Theme.tableSelectedForeColor.getColor());
        uIDefaults.put("Table.selectionBackground", Theme.tableSelectedBackColor.getColor());
        uIDefaults.put("Table.gridColor", Theme.tableGridColor.getColor());
        uIDefaults.put("Table.focusCellHighlightBorder", new BorderUIResource(new LineBorder(Theme.tableFocusBorderColor.getColor())));
        uIDefaults.put("Table.alternateRowColor", Theme.tableAlternateRowColor.getColor());
        uIDefaults.put("ProgressBar.foreground", Theme.progressColor.getColor());
        uIDefaults.put("ProgressBar.background", Theme.progressTrackColor.getColor());
        uIDefaults.put("ProgressBar.selectionForeground", Theme.progressSelectForeColor.getColor());
        uIDefaults.put("ProgressBar.selectionBackground", Theme.progressSelectBackColor.getColor());
        uIDefaults.put("PopupMenu.background", Theme.menuPopupColor);
        uIDefaults.put("TabbedPane.background", Theme.tabNormalColor.getColor());
        uIDefaults.put("TabbedPane.tabAreaInsets", Theme.tabAreaInsets);
        uIDefaults.put("TabbedPane.tabInsets", Theme.tabInsets);
        uIDefaults.put("MenuBar.background", Theme.menuBarColor.getColor());
        uIDefaults.put("ToolBar.background", Theme.toolBarColor.getColor());
        uIDefaults.put("EditorPane.caretForeground", Theme.textCaretColor.getColor());
        uIDefaults.put("PasswordField.caretForeground", Theme.textCaretColor.getColor());
        uIDefaults.put("TextArea.caretForeground", Theme.textCaretColor.getColor());
        uIDefaults.put("TextField.caretForeground", Theme.textCaretColor.getColor());
        uIDefaults.put("FormattedTextField.caretForeground", Theme.textCaretColor.getColor());
        uIDefaults.put("List.foreground", Theme.listTextColor.getColor());
        uIDefaults.put("List.background", Theme.listBgColor.getColor());
        uIDefaults.put("ComboBox.foreground", Theme.comboTextColor.getColor());
        uIDefaults.put("ComboBox.background", Theme.comboBgColor.getColor());
        uIDefaults.put("ComboBox.disabledBackground", Theme.textDisabledBgColor.getColor());
        uIDefaults.put("EditorPane.background", Theme.textBgColor.getColor());
        uIDefaults.put("EditorPane.foreground", Theme.textTextColor.getColor());
        uIDefaults.put("PasswordField.background", Theme.textBgColor.getColor());
        uIDefaults.put("PasswordField.foreground", Theme.textTextColor.getColor());
        uIDefaults.put("PasswordField.inactiveBackground", Theme.textDisabledBgColor.getColor());
        uIDefaults.put("TextArea.background", Theme.textBgColor.getColor());
        uIDefaults.put("TextArea.foreground", Theme.textTextColor.getColor());
        uIDefaults.put("TextArea.inactiveBackground", Theme.textDisabledBgColor.getColor());
        uIDefaults.put("TextField.background", Theme.textBgColor.getColor());
        uIDefaults.put("TextField.foreground", Theme.textTextColor.getColor());
        uIDefaults.put("TextField.inactiveBackground", Theme.textDisabledBgColor.getColor());
        uIDefaults.put("FormattedTextField.background", Theme.textBgColor.getColor());
        uIDefaults.put("FormattedTextField.foreground", Theme.textTextColor.getColor());
        uIDefaults.put("FormattedTextField.inactiveBackground", Theme.textDisabledBgColor.getColor());
        uIDefaults.put("TextPane.background", Theme.textPaneBgColor.getColor());
        uIDefaults.put("EditorPane.background", Theme.editorPaneBgColor.getColor());
        uIDefaults.put("OptionPane.messageForeground", Theme.textTextColor.getColor());
        uIDefaults.put("PasswordField.selectionBackground", Theme.textSelectedBgColor.getColor());
        uIDefaults.put("PasswordField.selectionForeground", Theme.textSelectedTextColor.getColor());
        uIDefaults.put("TextField.selectionBackground", Theme.textSelectedBgColor.getColor());
        uIDefaults.put("TextField.selectionForeground", Theme.textSelectedTextColor.getColor());
        uIDefaults.put("FormattedTextField.selectionBackground", Theme.textSelectedBgColor.getColor());
        uIDefaults.put("FormattedTextField.selectionForeground", Theme.textSelectedTextColor.getColor());
        uIDefaults.put("TextArea.selectionBackground", Theme.textSelectedBgColor.getColor());
        uIDefaults.put("TextArea.selectionForeground", Theme.textSelectedTextColor.getColor());
        uIDefaults.put("TextPane.selectionBackground", Theme.textSelectedBgColor.getColor());
        uIDefaults.put("TextPane.selectionForeground", Theme.textSelectedTextColor.getColor());
        uIDefaults.put("ComboBox.selectionBackground", Theme.comboSelectedBgColor.getColor());
        uIDefaults.put("ComboBox.selectionForeground", Theme.comboSelectedTextColor.getColor());
        uIDefaults.put("ComboBox.focusBackground", Theme.comboSelectedBgColor.getColor());
        uIDefaults.put("List.selectionForeground", Theme.listSelectedTextColor.getColor());
        uIDefaults.put("List.selectionBackground", Theme.listSelectedBgColor.getColor());
        uIDefaults.put("List.focusCellHighlightBorder", new BorderUIResource(new LineBorder(Theme.listFocusBorderColor.getColor())));
        uIDefaults.put("Tree.background", Theme.treeBgColor.getColor());
        uIDefaults.put("Tree.textBackground", Theme.treeTextBgColor.getColor());
        uIDefaults.put("Tree.textForeground", Theme.treeTextColor.getColor());
        uIDefaults.put("Tree.selectionBackground", Theme.treeSelectedBgColor.getColor());
        uIDefaults.put("Tree.selectionForeground", Theme.treeSelectedTextColor.getColor());
        uIDefaults.put("Tree.hash", Theme.treeLineColor.getColor());
        uIDefaults.put("Tree.line", Theme.treeLineColor.getColor());
        uIDefaults.put("Button.disabledText", Theme.buttonDisabledFgColor.getColor());
        uIDefaults.put("CheckBox.disabledText", Theme.checkDisabledFgColor.getColor());
        uIDefaults.put("RadioButton.disabledText", Theme.radioDisabledFgColor.getColor());
        uIDefaults.put("ToggleButton.disabledText", Theme.disColor.getColor());
        uIDefaults.put("ToggleButton.disabledSelectedText", Theme.disColor.getColor());
        uIDefaults.put("TextArea.inactiveForeground", Theme.disColor.getColor());
        uIDefaults.put("TextField.inactiveForeground", Theme.disColor.getColor());
        uIDefaults.put("FormattedTextField.inactiveForeground", Theme.disColor.getColor());
        uIDefaults.put("TextPane.inactiveForeground", Theme.disColor.getColor());
        uIDefaults.put("PasswordField.inactiveForeground", Theme.disColor.getColor());
        uIDefaults.put("ComboBox.disabledForeground", Theme.disColor.getColor());
        uIDefaults.put("Label.disabledForeground", Theme.disColor.getColor());
        uIDefaults.put("textInactiveText", Theme.disColor.getColor());
        uIDefaults.put("Desktop.background", Theme.desktopPaneBgColor.getColor());
        uIDefaults.put("Separator.background", Theme.separatorColor.getColor());
        uIDefaults.put("TitledBorder.border", new LineBorder(Theme.titledBorderColor.getColor()));
        uIDefaults.put("ToolTip.background", Theme.tipBgColor.getColor());
        uIDefaults.put("ToolTip.backgroundInactive", Theme.tipBgDis.getColor());
        uIDefaults.put("ToolTip.foreground", Theme.tipTextColor.getColor());
        uIDefaults.put("ToolTip.foregroundInactive", Theme.tipTextDis.getColor());
        uIDefaults.put("Panel.background", Theme.backColor.getColor());
        for (int i2 = 0; i2 < 20; i2++) {
            if (Theme.colorize[i2].getValue()) {
                Icon uncolorizedSystemIcon = TinyLookAndFeel.getUncolorizedSystemIcon(i2);
                if (uncolorizedSystemIcon == null || !(uncolorizedSystemIcon instanceof ImageIcon)) {
                    uIDefaults.put(TinyLookAndFeel.getSystemIconName(i2), uncolorizedSystemIcon);
                } else {
                    uIDefaults.put(TinyLookAndFeel.getSystemIconName(i2), DrawRoutines.colorizeIcon(((ImageIcon) uncolorizedSystemIcon).getImage(), Theme.colorizer[i2]));
                }
            }
        }
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    public String getName() {
        return "TinyLaF Default Theme";
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getSecondary3() {
        return Theme.backColor.getColor();
    }

    @Override // javax.swing.plaf.metal.DefaultMetalTheme, javax.swing.plaf.metal.MetalTheme
    public FontUIResource getControlTextFont() {
        return Theme.labelFont.getFont();
    }
}
