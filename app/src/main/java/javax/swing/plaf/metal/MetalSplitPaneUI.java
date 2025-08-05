package javax.swing.plaf.metal;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalSplitPaneUI.class */
public class MetalSplitPaneUI extends BasicSplitPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalSplitPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneUI
    public BasicSplitPaneDivider createDefaultDivider() {
        return new MetalSplitPaneDivider(this);
    }
}
