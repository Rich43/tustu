package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsMenuUI.class */
public class WindowsMenuUI extends BasicMenuUI {
    protected Integer menuBarHeight;
    protected boolean hotTrackingOn;
    final WindowsMenuItemUIAccessor accessor = new WindowsMenuItemUIAccessor() { // from class: com.sun.java.swing.plaf.windows.WindowsMenuUI.1
        @Override // com.sun.java.swing.plaf.windows.WindowsMenuItemUIAccessor
        public JMenuItem getMenuItem() {
            return WindowsMenuUI.this.menuItem;
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsMenuItemUIAccessor
        public TMSchema.State getState(JMenuItem jMenuItem) {
            TMSchema.State state = jMenuItem.isEnabled() ? TMSchema.State.NORMAL : TMSchema.State.DISABLED;
            ButtonModel model = jMenuItem.getModel();
            if (model.isArmed() || model.isSelected()) {
                state = jMenuItem.isEnabled() ? TMSchema.State.PUSHED : TMSchema.State.DISABLEDPUSHED;
            } else if (model.isRollover() && ((JMenu) jMenuItem).isTopLevelMenu()) {
                state = jMenuItem.isEnabled() ? TMSchema.State.HOT : TMSchema.State.DISABLEDHOT;
                MenuElement[] subElements = ((JMenuBar) jMenuItem.getParent()).getSubElements();
                int length = subElements.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    if (!((JMenuItem) subElements[i2]).isSelected()) {
                        i2++;
                    } else {
                        state = state;
                        break;
                    }
                }
            }
            if (!((JMenu) jMenuItem).isTopLevelMenu()) {
                if (state == TMSchema.State.PUSHED) {
                    state = TMSchema.State.HOT;
                } else if (state == TMSchema.State.DISABLEDPUSHED) {
                    state = TMSchema.State.DISABLEDHOT;
                }
            }
            if (((JMenu) jMenuItem).isTopLevelMenu() && WindowsMenuItemUI.isVistaPainting() && !WindowsMenuBarUI.isActive(jMenuItem)) {
                state = TMSchema.State.DISABLED;
            }
            return state;
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsMenuItemUIAccessor
        public TMSchema.Part getPart(JMenuItem jMenuItem) {
            return ((JMenu) jMenuItem).isTopLevelMenu() ? TMSchema.Part.MP_BARITEM : TMSchema.Part.MP_POPUPITEM;
        }
    };

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsMenuUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuUI, javax.swing.plaf.basic.BasicMenuItemUI
    protected void installDefaults() {
        super.installDefaults();
        if (!WindowsLookAndFeel.isClassicWindows()) {
            this.menuItem.setRolloverEnabled(true);
        }
        this.menuBarHeight = Integer.valueOf(UIManager.getInt("MenuBar.height"));
        Object obj = UIManager.get("MenuBar.rolloverEnabled");
        this.hotTrackingOn = obj instanceof Boolean ? ((Boolean) obj).booleanValue() : true;
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void paintBackground(Graphics graphics, JMenuItem jMenuItem, Color color) {
        if (WindowsMenuItemUI.isVistaPainting()) {
            WindowsMenuItemUI.paintBackground(this.accessor, graphics, jMenuItem, color);
            return;
        }
        JMenu jMenu = (JMenu) jMenuItem;
        ButtonModel model = jMenu.getModel();
        if (WindowsLookAndFeel.isClassicWindows() || !jMenu.isTopLevelMenu() || (XPStyle.getXP() != null && (model.isArmed() || model.isSelected()))) {
            super.paintBackground(graphics, jMenu, color);
            return;
        }
        Color color2 = graphics.getColor();
        int width = jMenu.getWidth();
        int height = jMenu.getHeight();
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        Color color3 = lookAndFeelDefaults.getColor("controlLtHighlight");
        Color color4 = lookAndFeelDefaults.getColor("controlShadow");
        graphics.setColor(jMenu.getBackground());
        graphics.fillRect(0, 0, width, height);
        if (jMenu.isOpaque()) {
            if (model.isArmed() || model.isSelected()) {
                graphics.setColor(color4);
                graphics.drawLine(0, 0, width - 1, 0);
                graphics.drawLine(0, 0, 0, height - 2);
                graphics.setColor(color3);
                graphics.drawLine(width - 1, 0, width - 1, height - 2);
                graphics.drawLine(0, height - 2, width - 1, height - 2);
            } else if (model.isRollover() && model.isEnabled()) {
                boolean z2 = false;
                MenuElement[] subElements = ((JMenuBar) jMenu.getParent()).getSubElements();
                int i2 = 0;
                while (true) {
                    if (i2 >= subElements.length) {
                        break;
                    }
                    if (!((JMenuItem) subElements[i2]).isSelected()) {
                        i2++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
                if (!z2) {
                    if (XPStyle.getXP() != null) {
                        graphics.setColor(this.selectionBackground);
                        graphics.fillRect(0, 0, width, height);
                    } else {
                        graphics.setColor(color3);
                        graphics.drawLine(0, 0, width - 1, 0);
                        graphics.drawLine(0, 0, 0, height - 2);
                        graphics.setColor(color4);
                        graphics.drawLine(width - 1, 0, width - 1, height - 2);
                        graphics.drawLine(0, height - 2, width - 1, height - 2);
                    }
                }
            }
        }
        graphics.setColor(color2);
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void paintText(Graphics graphics, JMenuItem jMenuItem, Rectangle rectangle, String str) {
        if (WindowsMenuItemUI.isVistaPainting()) {
            WindowsMenuItemUI.paintText(this.accessor, graphics, jMenuItem, rectangle, str);
            return;
        }
        JMenu jMenu = (JMenu) jMenuItem;
        ButtonModel model = jMenuItem.getModel();
        Color color = graphics.getColor();
        boolean zIsRollover = model.isRollover();
        if (zIsRollover && jMenu.isTopLevelMenu()) {
            MenuElement[] subElements = ((JMenuBar) jMenu.getParent()).getSubElements();
            int i2 = 0;
            while (true) {
                if (i2 >= subElements.length) {
                    break;
                }
                if (!((JMenuItem) subElements[i2]).isSelected()) {
                    i2++;
                } else {
                    zIsRollover = false;
                    break;
                }
            }
        }
        if ((model.isSelected() && (WindowsLookAndFeel.isClassicWindows() || !jMenu.isTopLevelMenu())) || (XPStyle.getXP() != null && (zIsRollover || model.isArmed() || model.isSelected()))) {
            graphics.setColor(this.selectionForeground);
        }
        WindowsGraphicsUtils.paintText(graphics, jMenuItem, rectangle, str, 0);
        graphics.setColor(color);
    }

    @Override // javax.swing.plaf.basic.BasicMenuUI, javax.swing.plaf.basic.BasicMenuItemUI
    protected MouseInputListener createMouseInputListener(JComponent jComponent) {
        return new WindowsMouseInputHandler();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsMenuUI$WindowsMouseInputHandler.class */
    protected class WindowsMouseInputHandler extends BasicMenuUI.MouseInputHandler {
        protected WindowsMouseInputHandler() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicMenuUI.MouseInputHandler, java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            super.mouseEntered(mouseEvent);
            JMenu jMenu = (JMenu) mouseEvent.getSource();
            if (WindowsMenuUI.this.hotTrackingOn && jMenu.isTopLevelMenu() && jMenu.isRolloverEnabled()) {
                jMenu.getModel().setRollover(true);
                WindowsMenuUI.this.menuItem.repaint();
            }
        }

        @Override // javax.swing.plaf.basic.BasicMenuUI.MouseInputHandler, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            super.mouseExited(mouseEvent);
            JMenu jMenu = (JMenu) mouseEvent.getSource();
            ButtonModel model = jMenu.getModel();
            if (jMenu.isRolloverEnabled()) {
                model.setRollover(false);
                WindowsMenuUI.this.menuItem.repaint();
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected Dimension getPreferredMenuItemSize(JComponent jComponent, Icon icon, Icon icon2, int i2) {
        Dimension preferredMenuItemSize = super.getPreferredMenuItemSize(jComponent, icon, icon2, i2);
        if ((jComponent instanceof JMenu) && ((JMenu) jComponent).isTopLevelMenu() && this.menuBarHeight != null && preferredMenuItemSize.height < this.menuBarHeight.intValue()) {
            preferredMenuItemSize.height = this.menuBarHeight.intValue();
        }
        return preferredMenuItemSize;
    }
}
