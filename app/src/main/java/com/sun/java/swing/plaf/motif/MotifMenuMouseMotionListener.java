package com.sun.java.swing.plaf.motif;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.MenuSelectionManager;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifMenuMouseMotionListener.class */
class MotifMenuMouseMotionListener implements MouseMotionListener {
    MotifMenuMouseMotionListener() {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
    }
}
