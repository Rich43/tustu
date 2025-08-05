package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLabelUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyLabelUI.class */
public class TinyLabelUI extends MetalLabelUI {
    protected static final TinyLabelUI SHARED_INSTANCE = new TinyLabelUI();

    public static ComponentUI createUI(JComponent jComponent) {
        return SHARED_INSTANCE;
    }
}
