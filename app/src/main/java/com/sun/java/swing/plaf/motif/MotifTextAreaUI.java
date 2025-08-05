package com.sun.java.swing.plaf.motif;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.Caret;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifTextAreaUI.class */
public class MotifTextAreaUI extends BasicTextAreaUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifTextAreaUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected Caret createCaret() {
        return MotifTextUI.createCaret();
    }
}
