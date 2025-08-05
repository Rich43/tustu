package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopPaneUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyDesktopPaneUI.class */
public class TinyDesktopPaneUI extends BasicDesktopPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyDesktopPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }
}
