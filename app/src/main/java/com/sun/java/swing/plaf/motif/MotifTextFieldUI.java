package com.sun.java.swing.plaf.motif;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.Caret;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTextFieldUI.class */
public class MotifTextFieldUI extends BasicTextFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifTextFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected Caret createCaret() {
        return MotifTextUI.createCaret();
    }
}
