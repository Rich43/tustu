package com.sun.java.swing.plaf.windows;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsLabelUI.class */
public class WindowsLabelUI extends BasicLabelUI {
    private static final Object WINDOWS_LABEL_UI_KEY = new Object();

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        WindowsLabelUI windowsLabelUI = (WindowsLabelUI) appContext.get(WINDOWS_LABEL_UI_KEY);
        if (windowsLabelUI == null) {
            windowsLabelUI = new WindowsLabelUI();
            appContext.put(WINDOWS_LABEL_UI_KEY, windowsLabelUI);
        }
        return windowsLabelUI;
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI
    protected void paintEnabledText(JLabel jLabel, Graphics graphics, String str, int i2, int i3) {
        int displayedMnemonicIndex = jLabel.getDisplayedMnemonicIndex();
        if (WindowsLookAndFeel.isMnemonicHidden()) {
            displayedMnemonicIndex = -1;
        }
        graphics.setColor(jLabel.getForeground());
        SwingUtilities2.drawStringUnderlineCharAt(jLabel, graphics, str, displayedMnemonicIndex, i2, i3);
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI
    protected void paintDisabledText(JLabel jLabel, Graphics graphics, String str, int i2, int i3) {
        int displayedMnemonicIndex = jLabel.getDisplayedMnemonicIndex();
        if (WindowsLookAndFeel.isMnemonicHidden()) {
            displayedMnemonicIndex = -1;
        }
        if ((UIManager.getColor("Label.disabledForeground") instanceof Color) && (UIManager.getColor("Label.disabledShadow") instanceof Color)) {
            graphics.setColor(UIManager.getColor("Label.disabledShadow"));
            SwingUtilities2.drawStringUnderlineCharAt(jLabel, graphics, str, displayedMnemonicIndex, i2 + 1, i3 + 1);
            graphics.setColor(UIManager.getColor("Label.disabledForeground"));
            SwingUtilities2.drawStringUnderlineCharAt(jLabel, graphics, str, displayedMnemonicIndex, i2, i3);
            return;
        }
        Color background = jLabel.getBackground();
        graphics.setColor(background.brighter());
        SwingUtilities2.drawStringUnderlineCharAt(jLabel, graphics, str, displayedMnemonicIndex, i2 + 1, i3 + 1);
        graphics.setColor(background.darker());
        SwingUtilities2.drawStringUnderlineCharAt(jLabel, graphics, str, displayedMnemonicIndex, i2, i3);
    }
}
