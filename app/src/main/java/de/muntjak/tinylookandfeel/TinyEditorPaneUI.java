package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.swing.text.JTextComponent;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyEditorPaneUI.class */
public class TinyEditorPaneUI extends BasicEditorPaneUI {
    JTextComponent editor;

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyEditorPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicEditorPaneUI, javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
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
