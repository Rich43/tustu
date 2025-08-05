package com.sun.java.swing.plaf.windows;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsCheckBoxUI.class */
public class WindowsCheckBoxUI extends WindowsRadioButtonUI {
    private static final Object WINDOWS_CHECK_BOX_UI_KEY = new Object();
    private static final String propertyPrefix = "CheckBox.";
    private boolean defaults_initialized = false;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        WindowsCheckBoxUI windowsCheckBoxUI = (WindowsCheckBoxUI) appContext.get(WINDOWS_CHECK_BOX_UI_KEY);
        if (windowsCheckBoxUI == null) {
            windowsCheckBoxUI = new WindowsCheckBoxUI();
            appContext.put(WINDOWS_CHECK_BOX_UI_KEY, windowsCheckBoxUI);
        }
        return windowsCheckBoxUI;
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicToggleButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public String getPropertyPrefix() {
        return propertyPrefix;
    }

    @Override // com.sun.java.swing.plaf.windows.WindowsRadioButtonUI, javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            this.icon = UIManager.getIcon(getPropertyPrefix() + "icon");
            this.defaults_initialized = true;
        }
    }

    @Override // com.sun.java.swing.plaf.windows.WindowsRadioButtonUI, javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.defaults_initialized = false;
    }
}
