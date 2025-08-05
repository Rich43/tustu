package com.sun.java.swing.plaf.motif;

import java.awt.MenuContainer;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifMenuUI.class */
public class MotifMenuUI extends BasicMenuUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifMenuUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuUI
    protected ChangeListener createChangeListener(JComponent jComponent) {
        return new MotifChangeHandler((JMenu) jComponent, this);
    }

    private boolean popupIsOpen(JMenu jMenu, MenuElement[] menuElementArr) {
        JPopupMenu popupMenu = jMenu.getPopupMenu();
        for (int length = menuElementArr.length - 1; length >= 0; length--) {
            if (menuElementArr[length].getComponent() == popupMenu) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.swing.plaf.basic.BasicMenuUI, javax.swing.plaf.basic.BasicMenuItemUI
    protected MouseInputListener createMouseInputListener(JComponent jComponent) {
        return new MouseInputHandler();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifMenuUI$MotifChangeHandler.class */
    public class MotifChangeHandler extends BasicMenuUI.ChangeHandler {
        public MotifChangeHandler(JMenu jMenu, MotifMenuUI motifMenuUI) {
            super(jMenu, motifMenuUI);
        }

        @Override // javax.swing.plaf.basic.BasicMenuUI.ChangeHandler, javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JMenuItem jMenuItem = (JMenuItem) changeEvent.getSource();
            if (jMenuItem.isArmed() || jMenuItem.isSelected()) {
                jMenuItem.setBorderPainted(true);
            } else {
                jMenuItem.setBorderPainted(false);
            }
            super.stateChanged(changeEvent);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifMenuUI$MouseInputHandler.class */
    protected class MouseInputHandler implements MouseInputListener {
        protected MouseInputHandler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            JMenu jMenu = (JMenu) mouseEvent.getComponent();
            if (jMenu.isEnabled()) {
                if (jMenu.isTopLevelMenu()) {
                    if (jMenu.isSelected()) {
                        menuSelectionManagerDefaultManager.clearSelectedPath();
                    } else {
                        MenuContainer parent = jMenu.getParent();
                        if (parent != null && (parent instanceof JMenuBar)) {
                            menuSelectionManagerDefaultManager.setSelectedPath(new MenuElement[]{(MenuElement) parent, jMenu});
                        }
                    }
                }
                MenuElement[] path = MotifMenuUI.this.getPath();
                if (path.length > 0) {
                    MenuElement[] menuElementArr = new MenuElement[path.length + 1];
                    System.arraycopy(path, 0, menuElementArr, 0, path.length);
                    menuElementArr[path.length] = jMenu.getPopupMenu();
                    menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr);
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            JMenuItem jMenuItem = (JMenuItem) mouseEvent.getComponent();
            Point point = mouseEvent.getPoint();
            if (point.f12370x < 0 || point.f12370x >= jMenuItem.getWidth() || point.f12371y < 0 || point.f12371y >= jMenuItem.getHeight()) {
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
