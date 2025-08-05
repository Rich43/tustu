package com.sun.java.swing.plaf.motif;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.MenuSelectionManager;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifMenuMouseListener.class */
class MotifMenuMouseListener extends MouseAdapter {
    MotifMenuMouseListener() {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
    }
}
