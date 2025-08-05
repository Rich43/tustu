package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsMenuBarUI.class */
public class WindowsMenuBarUI extends BasicMenuBarUI {
    private WindowListener windowListener = null;
    private HierarchyListener hierarchyListener = null;
    private Window window = null;

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsMenuBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuBarUI
    protected void uninstallListeners() {
        uninstallWindowListener();
        if (this.hierarchyListener != null) {
            this.menuBar.removeHierarchyListener(this.hierarchyListener);
            this.hierarchyListener = null;
        }
        super.uninstallListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void installWindowListener() {
        if (this.windowListener == null) {
            Container topLevelAncestor = this.menuBar.getTopLevelAncestor();
            if (topLevelAncestor instanceof Window) {
                this.window = (Window) topLevelAncestor;
                this.windowListener = new WindowAdapter() { // from class: com.sun.java.swing.plaf.windows.WindowsMenuBarUI.1
                    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                    public void windowActivated(WindowEvent windowEvent) {
                        WindowsMenuBarUI.this.menuBar.repaint();
                    }

                    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                    public void windowDeactivated(WindowEvent windowEvent) {
                        WindowsMenuBarUI.this.menuBar.repaint();
                    }
                };
                ((Window) topLevelAncestor).addWindowListener(this.windowListener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uninstallWindowListener() {
        if (this.windowListener != null && this.window != null) {
            this.window.removeWindowListener(this.windowListener);
        }
        this.window = null;
        this.windowListener = null;
    }

    @Override // javax.swing.plaf.basic.BasicMenuBarUI
    protected void installListeners() {
        if (WindowsLookAndFeel.isOnVista()) {
            installWindowListener();
            this.hierarchyListener = new HierarchyListener() { // from class: com.sun.java.swing.plaf.windows.WindowsMenuBarUI.2
                @Override // java.awt.event.HierarchyListener
                public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
                    if ((hierarchyEvent.getChangeFlags() & 2) != 0) {
                        if (WindowsMenuBarUI.this.menuBar.isDisplayable()) {
                            WindowsMenuBarUI.this.installWindowListener();
                        } else {
                            WindowsMenuBarUI.this.uninstallWindowListener();
                        }
                    }
                }
            };
            this.menuBar.addHierarchyListener(this.hierarchyListener);
        }
        super.installListeners();
    }

    @Override // javax.swing.plaf.basic.BasicMenuBarUI
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        ActionMap uIActionMap = SwingUtilities.getUIActionMap(this.menuBar);
        if (uIActionMap == null) {
            uIActionMap = new ActionMapUIResource();
            SwingUtilities.replaceUIActionMap(this.menuBar, uIActionMap);
        }
        uIActionMap.put("takeFocus", new TakeFocus());
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsMenuBarUI$TakeFocus.class */
    private static class TakeFocus extends AbstractAction {
        private TakeFocus() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JMenuBar jMenuBar = (JMenuBar) actionEvent.getSource();
            JMenu menu = jMenuBar.getMenu(0);
            if (menu != null) {
                MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[]{jMenuBar, menu});
                WindowsLookAndFeel.setMnemonicHidden(false);
                WindowsLookAndFeel.repaintRootPane(jMenuBar);
            }
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        XPStyle xp = XPStyle.getXP();
        if (WindowsMenuItemUI.isVistaPainting(xp)) {
            xp.getSkin(jComponent, TMSchema.Part.MP_BARBACKGROUND).paintSkin(graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), isActive(jComponent) ? TMSchema.State.ACTIVE : TMSchema.State.INACTIVE);
        } else {
            super.paint(graphics, jComponent);
        }
    }

    static boolean isActive(JComponent jComponent) {
        JRootPane rootPane = jComponent.getRootPane();
        if (rootPane != null) {
            Container parent = rootPane.getParent();
            if (parent instanceof Window) {
                return ((Window) parent).isActive();
            }
            return true;
        }
        return true;
    }
}
