package com.sun.java.swing.plaf.motif;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifSplitPaneUI.class */
public class MotifSplitPaneUI extends BasicSplitPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifSplitPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI
    public BasicSplitPaneDivider createDefaultDivider() {
        return new MotifSplitPaneDivider(this);
    }
}
