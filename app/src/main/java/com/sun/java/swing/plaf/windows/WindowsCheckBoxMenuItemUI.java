package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsCheckBoxMenuItemUI.class */
public class WindowsCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI {
    final WindowsMenuItemUIAccessor accessor = new WindowsMenuItemUIAccessor() { // from class: com.sun.java.swing.plaf.windows.WindowsCheckBoxMenuItemUI.1
        @Override // com.sun.java.swing.plaf.windows.WindowsMenuItemUIAccessor
        public JMenuItem getMenuItem() {
            return WindowsCheckBoxMenuItemUI.this.menuItem;
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsMenuItemUIAccessor
        public TMSchema.State getState(JMenuItem jMenuItem) {
            return WindowsMenuItemUI.getState(this, jMenuItem);
        }

        @Override // com.sun.java.swing.plaf.windows.WindowsMenuItemUIAccessor
        public TMSchema.Part getPart(JMenuItem jMenuItem) {
            return WindowsMenuItemUI.getPart(this, jMenuItem);
        }
    };

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsCheckBoxMenuItemUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void paintBackground(Graphics graphics, JMenuItem jMenuItem, Color color) {
        if (WindowsMenuItemUI.isVistaPainting()) {
            WindowsMenuItemUI.paintBackground(this.accessor, graphics, jMenuItem, color);
        } else {
            super.paintBackground(graphics, jMenuItem, color);
        }
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void paintText(Graphics graphics, JMenuItem jMenuItem, Rectangle rectangle, String str) {
        if (WindowsMenuItemUI.isVistaPainting()) {
            WindowsMenuItemUI.paintText(this.accessor, graphics, jMenuItem, rectangle, str);
            return;
        }
        ButtonModel model = jMenuItem.getModel();
        Color color = graphics.getColor();
        if (model.isEnabled() && model.isArmed()) {
            graphics.setColor(this.selectionForeground);
        }
        WindowsGraphicsUtils.paintText(graphics, jMenuItem, rectangle, str, 0);
        graphics.setColor(color);
    }
}
