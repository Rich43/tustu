package com.sun.java.swing.plaf.motif;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.Caret;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTextPaneUI.class */
public class MotifTextPaneUI extends BasicTextPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifTextPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected Caret createCaret() {
        return MotifTextUI.createCaret();
    }
}
