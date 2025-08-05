package org.icepdf.ri.common;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/PageNumberTextFieldKeyListener.class */
public class PageNumberTextFieldKeyListener extends KeyAdapter {
    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyTyped(KeyEvent e2) {
        char c2 = e2.getKeyChar();
        if (!Character.isDigit(c2) && c2 != '\b' && c2 != 127 && c2 != 27 && c2 != '\n') {
            Toolkit.getDefaultToolkit().beep();
            e2.consume();
        }
    }
}
