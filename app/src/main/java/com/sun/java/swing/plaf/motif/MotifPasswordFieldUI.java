package com.sun.java.swing.plaf.motif;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.text.Caret;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifPasswordFieldUI.class */
public class MotifPasswordFieldUI extends BasicPasswordFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifPasswordFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected Caret createCaret() {
        return MotifTextUI.createCaret();
    }
}
