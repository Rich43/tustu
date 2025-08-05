package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsMenuItemUI.class */
public class WindowsMenuItemUI extends BasicMenuItemUI {
    final WindowsMenuItemUIAccessor accessor = new WindowsMenuItemUIAccessor() { // from class: com.sun.java.swing.plaf.windows.WindowsMenuItemUI.1
        @Override // com.sun.java.swing.plaf.windows.WindowsMenuItemUIAccessor
        public JMenuItem getMenuItem() {
            return WindowsMenuItemUI.this.menuItem;
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
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WindowsMenuItemUI.class.desiredAssertionStatus();
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsMenuItemUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void paintText(Graphics graphics, JMenuItem jMenuItem, Rectangle rectangle, String str) {
        if (isVistaPainting()) {
            paintText(this.accessor, graphics, jMenuItem, rectangle, str);
            return;
        }
        ButtonModel model = jMenuItem.getModel();
        Color color = graphics.getColor();
        if (model.isEnabled() && (model.isArmed() || ((jMenuItem instanceof JMenu) && model.isSelected()))) {
            graphics.setColor(this.selectionForeground);
        }
        WindowsGraphicsUtils.paintText(graphics, jMenuItem, rectangle, str, 0);
        graphics.setColor(color);
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void paintBackground(Graphics graphics, JMenuItem jMenuItem, Color color) {
        if (isVistaPainting()) {
            paintBackground(this.accessor, graphics, jMenuItem, color);
        } else {
            super.paintBackground(graphics, jMenuItem, color);
        }
    }

    static void paintBackground(WindowsMenuItemUIAccessor windowsMenuItemUIAccessor, Graphics graphics, JMenuItem jMenuItem, Color color) {
        XPStyle xp = XPStyle.getXP();
        if (!$assertionsDisabled && !isVistaPainting(xp)) {
            throw new AssertionError();
        }
        if (isVistaPainting(xp)) {
            int width = jMenuItem.getWidth();
            int height = jMenuItem.getHeight();
            if (jMenuItem.isOpaque()) {
                Color color2 = graphics.getColor();
                graphics.setColor(jMenuItem.getBackground());
                graphics.fillRect(0, 0, width, height);
                graphics.setColor(color2);
            }
            xp.getSkin(jMenuItem, windowsMenuItemUIAccessor.getPart(jMenuItem)).paintSkin(graphics, 0, 0, width, height, windowsMenuItemUIAccessor.getState(jMenuItem));
        }
    }

    static void paintText(WindowsMenuItemUIAccessor windowsMenuItemUIAccessor, Graphics graphics, JMenuItem jMenuItem, Rectangle rectangle, String str) {
        if (!$assertionsDisabled && !isVistaPainting()) {
            throw new AssertionError();
        }
        if (isVistaPainting()) {
            TMSchema.State state = windowsMenuItemUIAccessor.getState(jMenuItem);
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jMenuItem, graphics);
            int displayedMnemonicIndex = jMenuItem.getDisplayedMnemonicIndex();
            if (WindowsLookAndFeel.isMnemonicHidden()) {
                displayedMnemonicIndex = -1;
            }
            WindowsGraphicsUtils.paintXPText(jMenuItem, windowsMenuItemUIAccessor.getPart(jMenuItem), state, graphics, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent(), str, displayedMnemonicIndex);
        }
    }

    static TMSchema.State getState(WindowsMenuItemUIAccessor windowsMenuItemUIAccessor, JMenuItem jMenuItem) {
        TMSchema.State state;
        ButtonModel model = jMenuItem.getModel();
        if (model.isArmed()) {
            state = model.isEnabled() ? TMSchema.State.HOT : TMSchema.State.DISABLEDHOT;
        } else {
            state = model.isEnabled() ? TMSchema.State.NORMAL : TMSchema.State.DISABLED;
        }
        return state;
    }

    static TMSchema.Part getPart(WindowsMenuItemUIAccessor windowsMenuItemUIAccessor, JMenuItem jMenuItem) {
        return TMSchema.Part.MP_POPUPITEM;
    }

    static boolean isVistaPainting(XPStyle xPStyle) {
        return xPStyle != null && xPStyle.isSkinDefined(null, TMSchema.Part.MP_POPUPITEM);
    }

    static boolean isVistaPainting() {
        return isVistaPainting(XPStyle.getXP());
    }
}
