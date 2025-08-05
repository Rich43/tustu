package com.sun.java.swing.plaf.motif;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.swing.text.Caret;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifEditorPaneUI.class */
public class MotifEditorPaneUI extends BasicEditorPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifEditorPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected Caret createCaret() {
        return MotifTextUI.createCaret();
    }
}
