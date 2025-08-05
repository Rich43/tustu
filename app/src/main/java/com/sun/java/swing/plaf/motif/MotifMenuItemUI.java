package com.sun.java.swing.plaf.motif;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.MenuSelectionManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifMenuItemUI.class */
public class MotifMenuItemUI extends BasicMenuItemUI {
    protected ChangeListener changeListener;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifMenuItemUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void installListeners() {
        super.installListeners();
        this.changeListener = createChangeListener(this.menuItem);
        this.menuItem.addChangeListener(this.changeListener);
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.menuItem.removeChangeListener(this.changeListener);
    }

    protected ChangeListener createChangeListener(JComponent jComponent) {
        return new ChangeHandler();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected MouseInputListener createMouseInputListener(JComponent jComponent) {
        return new MouseInputHandler();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifMenuItemUI$ChangeHandler.class */
    protected class ChangeHandler implements ChangeListener {
        protected ChangeHandler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JMenuItem jMenuItem = (JMenuItem) changeEvent.getSource();
            LookAndFeel.installProperty(jMenuItem, AbstractButton.BORDER_PAINTED_CHANGED_PROPERTY, Boolean.valueOf(jMenuItem.isArmed() || jMenuItem.isSelected()));
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifMenuItemUI$MouseInputHandler.class */
    protected class MouseInputHandler implements MouseInputListener {
        protected MouseInputHandler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            MenuSelectionManager.defaultManager().setSelectedPath(MotifMenuItemUI.this.getPath());
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            JMenuItem jMenuItem = (JMenuItem) mouseEvent.getComponent();
            Point point = mouseEvent.getPoint();
            if (point.f12370x >= 0 && point.f12370x < jMenuItem.getWidth() && point.f12371y >= 0 && point.f12371y < jMenuItem.getHeight()) {
                menuSelectionManagerDefaultManager.clearSelectedPath();
                jMenuItem.doClick(0);
            } else {
                menuSelectionManagerDefaultManager.processMouseEvent(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }
    }
}
