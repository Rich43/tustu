package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsGraphicsUtils.class */
public class WindowsGraphicsUtils {
    public static void paintText(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, String str, int i2) {
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(abstractButton, graphics);
        int displayedMnemonicIndex = abstractButton.getDisplayedMnemonicIndex();
        if (WindowsLookAndFeel.isMnemonicHidden()) {
            displayedMnemonicIndex = -1;
        }
        if (XPStyle.getXP() != null && !(abstractButton instanceof JMenuItem)) {
            paintXPText(abstractButton, graphics, rectangle.f12372x + i2, rectangle.f12373y + fontMetrics.getAscent() + i2, str, displayedMnemonicIndex);
        } else {
            paintClassicText(abstractButton, graphics, rectangle.f12372x + i2, rectangle.f12373y + fontMetrics.getAscent() + i2, str, displayedMnemonicIndex);
        }
    }

    static void paintClassicText(AbstractButton abstractButton, Graphics graphics, int i2, int i3, String str, int i4) {
        ButtonModel model = abstractButton.getModel();
        abstractButton.getForeground();
        if (model.isEnabled()) {
            if ((!(abstractButton instanceof JMenuItem) || !model.isArmed()) && (!(abstractButton instanceof JMenu) || (!model.isSelected() && !model.isRollover()))) {
                graphics.setColor(abstractButton.getForeground());
            }
            SwingUtilities2.drawStringUnderlineCharAt(abstractButton, graphics, str, i4, i2, i3);
            return;
        }
        Color color = UIManager.getColor("Button.shadow");
        Color color2 = UIManager.getColor("Button.disabledShadow");
        if (model.isArmed()) {
            color = UIManager.getColor("Button.disabledForeground");
        } else {
            if (color2 == null) {
                color2 = abstractButton.getBackground().darker();
            }
            graphics.setColor(color2);
            SwingUtilities2.drawStringUnderlineCharAt(abstractButton, graphics, str, i4, i2 + 1, i3 + 1);
        }
        if (color == null) {
            color = abstractButton.getBackground().brighter();
        }
        graphics.setColor(color);
        SwingUtilities2.drawStringUnderlineCharAt(abstractButton, graphics, str, i4, i2, i3);
    }

    static void paintXPText(AbstractButton abstractButton, Graphics graphics, int i2, int i3, String str, int i4) {
        paintXPText(abstractButton, WindowsButtonUI.getXPButtonType(abstractButton), WindowsButtonUI.getXPButtonState(abstractButton), graphics, i2, i3, str, i4);
    }

    static void paintXPText(AbstractButton abstractButton, TMSchema.Part part, TMSchema.State state, Graphics graphics, int i2, int i3, String str, int i4) {
        XPStyle xp = XPStyle.getXP();
        if (xp == null) {
            return;
        }
        Color foreground = abstractButton.getForeground();
        if (foreground instanceof UIResource) {
            foreground = xp.getColor(abstractButton, part, state, TMSchema.Prop.TEXTCOLOR, abstractButton.getForeground());
            if (part == TMSchema.Part.TP_BUTTON && state == TMSchema.State.DISABLED && foreground.equals(xp.getColor(abstractButton, part, TMSchema.State.NORMAL, TMSchema.Prop.TEXTCOLOR, abstractButton.getForeground()))) {
                foreground = xp.getColor(abstractButton, TMSchema.Part.BP_PUSHBUTTON, state, TMSchema.Prop.TEXTCOLOR, foreground);
            }
            TMSchema.TypeEnum typeEnum = xp.getTypeEnum(abstractButton, part, state, TMSchema.Prop.TEXTSHADOWTYPE);
            if (typeEnum == TMSchema.TypeEnum.TST_SINGLE || typeEnum == TMSchema.TypeEnum.TST_CONTINUOUS) {
                Color color = xp.getColor(abstractButton, part, state, TMSchema.Prop.TEXTSHADOWCOLOR, Color.black);
                Point point = xp.getPoint(abstractButton, part, state, TMSchema.Prop.TEXTSHADOWOFFSET);
                if (point != null) {
                    graphics.setColor(color);
                    SwingUtilities2.drawStringUnderlineCharAt(abstractButton, graphics, str, i4, i2 + point.f12370x, i3 + point.f12371y);
                }
            }
        }
        graphics.setColor(foreground);
        SwingUtilities2.drawStringUnderlineCharAt(abstractButton, graphics, str, i4, i2, i3);
    }

    static boolean isLeftToRight(Component component) {
        return component.getComponentOrientation().isLeftToRight();
    }

    static void repaintMnemonicsInWindow(Window window) {
        if (window == null || !window.isShowing()) {
            return;
        }
        for (Window window2 : window.getOwnedWindows()) {
            repaintMnemonicsInWindow(window2);
        }
        repaintMnemonicsInContainer(window);
    }

    static void repaintMnemonicsInContainer(Container container) {
        for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
            Component component = container.getComponent(i2);
            if (component != null && component.isVisible()) {
                if ((component instanceof AbstractButton) && ((AbstractButton) component).getMnemonic() != 0) {
                    component.repaint();
                } else if ((component instanceof JLabel) && ((JLabel) component).getDisplayedMnemonic() != 0) {
                    component.repaint();
                } else if (component instanceof Container) {
                    repaintMnemonicsInContainer((Container) component);
                }
            }
        }
    }
}
