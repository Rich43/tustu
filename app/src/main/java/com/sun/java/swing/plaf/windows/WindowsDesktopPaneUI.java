package com.sun.java.swing.plaf.windows;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsDesktopPaneUI.class */
public class WindowsDesktopPaneUI extends BasicDesktopPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsDesktopPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void installDesktopManager() {
        this.desktopManager = this.desktop.getDesktopManager();
        if (this.desktopManager == null) {
            this.desktopManager = new WindowsDesktopManager();
            this.desktop.setDesktopManager(this.desktopManager);
        }
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void installDefaults() {
        super.installDefaults();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        if (!this.desktop.requestDefaultFocus()) {
            this.desktop.requestFocus();
        }
    }
}
