package com.sun.java.swing.plaf.windows;

import java.awt.KeyEventPostProcessor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.basic.ComboPopup;
import sun.awt.AWTAccessor;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsRootPaneUI.class */
public class WindowsRootPaneUI extends BasicRootPaneUI {
    private static final WindowsRootPaneUI windowsRootPaneUI = new WindowsRootPaneUI();
    static final AltProcessor altProcessor = new AltProcessor();

    public static ComponentUI createUI(JComponent jComponent) {
        return windowsRootPaneUI;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsRootPaneUI$AltProcessor.class */
    static class AltProcessor implements KeyEventPostProcessor {
        static boolean altKeyPressed = false;
        static boolean menuCanceledOnPress = false;
        static JRootPane root = null;
        static Window winAncestor = null;

        AltProcessor() {
        }

        void altPressed(KeyEvent keyEvent) {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (selectedPath.length > 0 && !(selectedPath[0] instanceof ComboPopup)) {
                menuSelectionManagerDefaultManager.clearSelectedPath();
                menuCanceledOnPress = true;
                keyEvent.consume();
                return;
            }
            if (selectedPath.length > 0) {
                menuCanceledOnPress = false;
                WindowsLookAndFeel.setMnemonicHidden(false);
                WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
                keyEvent.consume();
                return;
            }
            menuCanceledOnPress = false;
            WindowsLookAndFeel.setMnemonicHidden(false);
            WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
            JMenuBar jMenuBar = root != null ? root.getJMenuBar() : null;
            if (jMenuBar == null && (winAncestor instanceof JFrame)) {
                jMenuBar = ((JFrame) winAncestor).getJMenuBar();
            }
            if ((jMenuBar != null ? jMenuBar.getMenu(0) : null) != null) {
                keyEvent.consume();
            }
        }

        void altReleased(KeyEvent keyEvent) {
            if (menuCanceledOnPress) {
                WindowsLookAndFeel.setMnemonicHidden(true);
                WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
                return;
            }
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            if (menuSelectionManagerDefaultManager.getSelectedPath().length == 0) {
                JMenuBar jMenuBar = root != null ? root.getJMenuBar() : null;
                if (jMenuBar == null && (winAncestor instanceof JFrame)) {
                    jMenuBar = ((JFrame) winAncestor).getJMenuBar();
                }
                JMenu menu = jMenuBar != null ? jMenuBar.getMenu(0) : null;
                boolean z2 = false;
                Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                if (defaultToolkit instanceof SunToolkit) {
                    z2 = SunToolkit.getContainingWindow(AWTAccessor.getKeyEventAccessor().getOriginalSource(keyEvent)) != winAncestor || keyEvent.getWhen() <= ((SunToolkit) defaultToolkit).getWindowDeactivationTime(winAncestor);
                }
                if (menu != null && !z2) {
                    menuSelectionManagerDefaultManager.setSelectedPath(new MenuElement[]{jMenuBar, menu});
                    return;
                } else {
                    if (!WindowsLookAndFeel.isMnemonicHidden()) {
                        WindowsLookAndFeel.setMnemonicHidden(true);
                        WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
                        return;
                    }
                    return;
                }
            }
            if (menuSelectionManagerDefaultManager.getSelectedPath()[0] instanceof ComboPopup) {
                WindowsLookAndFeel.setMnemonicHidden(true);
                WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
            }
        }

        @Override // java.awt.KeyEventPostProcessor
        public boolean postProcessKeyEvent(KeyEvent keyEvent) {
            if (keyEvent.isConsumed() && keyEvent.getKeyCode() != 18) {
                altKeyPressed = false;
                return false;
            }
            if (keyEvent.getKeyCode() == 18) {
                root = SwingUtilities.getRootPane(keyEvent.getComponent());
                winAncestor = root == null ? null : SwingUtilities.getWindowAncestor(root);
                if (keyEvent.getID() == 401) {
                    if (!altKeyPressed) {
                        altPressed(keyEvent);
                    }
                    altKeyPressed = true;
                    return true;
                }
                if (keyEvent.getID() == 402) {
                    if (altKeyPressed) {
                        altReleased(keyEvent);
                    } else if (MenuSelectionManager.defaultManager().getSelectedPath().length <= 0) {
                        WindowsLookAndFeel.setMnemonicHidden(true);
                        WindowsGraphicsUtils.repaintMnemonicsInWindow(winAncestor);
                    }
                    altKeyPressed = false;
                }
                root = null;
                winAncestor = null;
                return false;
            }
            altKeyPressed = false;
            return false;
        }
    }
}
