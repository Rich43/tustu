package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.metal.MetalSplitPaneUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySplitPaneUI.class */
public class TinySplitPaneUI extends MetalSplitPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new TinySplitPaneUI();
    }

    @Override // javax.swing.plaf.metal.MetalSplitPaneUI, javax.swing.plaf.basic.BasicSplitPaneUI
    public BasicSplitPaneDivider createDefaultDivider() {
        return new TinySplitPaneDivider(this);
    }
}
