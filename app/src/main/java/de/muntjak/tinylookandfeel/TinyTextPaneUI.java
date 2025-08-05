package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.JTextComponent;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTextPaneUI.class */
public class TinyTextPaneUI extends BasicTextPaneUI {
    JTextComponent editor;

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyTextPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextPaneUI, javax.swing.plaf.basic.BasicEditorPaneUI, javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        if (jComponent instanceof JTextComponent) {
            this.editor = (JTextComponent) jComponent;
        }
        super.installUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void installDefaults() {
        super.installDefaults();
    }
}
