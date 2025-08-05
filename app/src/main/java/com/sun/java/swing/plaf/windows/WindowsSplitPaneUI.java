package com.sun.java.swing.plaf.windows;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsSplitPaneUI.class */
public class WindowsSplitPaneUI extends BasicSplitPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsSplitPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI
    public BasicSplitPaneDivider createDefaultDivider() {
        return new WindowsSplitPaneDivider(this);
    }
}
