package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.WindowsTextUI;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.Caret;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsTextAreaUI.class */
public class WindowsTextAreaUI extends BasicTextAreaUI {
    @Override // javax.swing.plaf.basic.BasicTextUI
    protected Caret createCaret() {
        return new WindowsTextUI.WindowsCaret();
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsTextAreaUI();
    }
}
