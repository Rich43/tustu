package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyFormattedTextFieldUI.class */
public class TinyFormattedTextFieldUI extends TinyTextFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyFormattedTextFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextFieldUI, javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "FormattedTextField";
    }
}
