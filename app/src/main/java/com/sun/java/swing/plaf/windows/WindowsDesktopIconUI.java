package com.sun.java.swing.plaf.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsDesktopIconUI.class */
public class WindowsDesktopIconUI extends BasicDesktopIconUI {
    private int width;

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsDesktopIconUI();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    public void installDefaults() {
        super.installDefaults();
        this.width = UIManager.getInt("DesktopIcon.width");
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        jComponent.setOpaque(XPStyle.getXP() == null);
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        WindowsInternalFrameTitlePane windowsInternalFrameTitlePane = (WindowsInternalFrameTitlePane) this.iconPane;
        super.uninstallUI(jComponent);
        windowsInternalFrameTitlePane.uninstallListeners();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installComponents() {
        this.iconPane = new WindowsInternalFrameTitlePane(this.frame);
        this.desktopIcon.setLayout(new BorderLayout());
        this.desktopIcon.add(this.iconPane, BorderLayout.CENTER);
        if (XPStyle.getXP() != null) {
            this.desktopIcon.setBorder(null);
        }
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return getMinimumSize(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension minimumSize = super.getMinimumSize(jComponent);
        minimumSize.width = this.width;
        return minimumSize;
    }
}
