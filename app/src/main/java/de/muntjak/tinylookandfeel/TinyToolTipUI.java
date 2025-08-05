package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToolTipUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyToolTipUI.class */
public class TinyToolTipUI extends MetalToolTipUI {
    protected static TinyToolTipUI sharedInstance = new TinyToolTipUI();

    public static ComponentUI createUI(JComponent jComponent) {
        return sharedInstance;
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI
    protected void installDefaults(JComponent jComponent) {
        super.installDefaults(jComponent);
    }
}
