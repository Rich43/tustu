package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.View;
import org.slf4j.Marker;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifGraphicsUtils.class */
public class MotifGraphicsUtils implements SwingConstants {
    private static final String MAX_ACC_WIDTH = "maxAccWidth";

    static void drawPoint(Graphics graphics, int i2, int i3) {
        graphics.drawLine(i2, i3, i2, i3);
    }

    public static void drawGroove(Graphics graphics, int i2, int i3, int i4, int i5, Color color, Color color2) {
        Color color3 = graphics.getColor();
        graphics.translate(i2, i3);
        graphics.setColor(color);
        graphics.drawRect(0, 0, i4 - 2, i5 - 2);
        graphics.setColor(color2);
        graphics.drawLine(1, i5 - 3, 1, 1);
        graphics.drawLine(1, 1, i4 - 3, 1);
        graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
        graphics.drawLine(i4 - 1, i5 - 1, i4 - 1, 0);
        graphics.translate(-i2, -i3);
        graphics.setColor(color3);
    }

    public static void drawStringInRect(Graphics graphics, String str, int i2, int i3, int i4, int i5, int i6) {
        drawStringInRect(null, graphics, str, i2, i3, i4, i5, i6);
    }

    static void drawStringInRect(JComponent jComponent, Graphics graphics, String str, int i2, int i3, int i4, int i5, int i6) {
        FontMetrics fontMetrics;
        int i7;
        if (graphics.getFont() == null || (fontMetrics = SwingUtilities2.getFontMetrics(jComponent, graphics)) == null) {
            return;
        }
        if (i6 == 0) {
            int iStringWidth = SwingUtilities2.stringWidth(jComponent, fontMetrics, str);
            if (iStringWidth > i4) {
                iStringWidth = i4;
            }
            i7 = i2 + ((i4 - iStringWidth) / 2);
        } else if (i6 == 4) {
            int iStringWidth2 = SwingUtilities2.stringWidth(jComponent, fontMetrics, str);
            if (iStringWidth2 > i4) {
                iStringWidth2 = i4;
            }
            i7 = (i2 + i4) - iStringWidth2;
        } else {
            i7 = i2;
        }
        int ascent = ((i5 - fontMetrics.getAscent()) - fontMetrics.getDescent()) / 2;
        if (ascent < 0) {
            ascent = 0;
        }
        SwingUtilities2.drawString(jComponent, graphics, str, i7, ((i3 + i5) - ascent) - fontMetrics.getDescent());
    }

    public static void paintMenuItem(Graphics graphics, JComponent jComponent, Icon icon, Icon icon2, Color color, Color color2, int i2) {
        Icon icon3;
        JMenuItem jMenuItem = (JMenuItem) jComponent;
        ButtonModel model = jMenuItem.getModel();
        Dimension size = jMenuItem.getSize();
        Insets insets = jComponent.getInsets();
        Rectangle rectangle = new Rectangle(size);
        rectangle.f12372x += insets.left;
        rectangle.f12373y += insets.top;
        rectangle.width -= insets.right + rectangle.f12372x;
        rectangle.height -= insets.bottom + rectangle.f12373y;
        Rectangle rectangle2 = new Rectangle();
        Rectangle rectangle3 = new Rectangle();
        Rectangle rectangle4 = new Rectangle();
        Rectangle rectangle5 = new Rectangle();
        Rectangle rectangle6 = new Rectangle();
        Font font = graphics.getFont();
        Font font2 = jComponent.getFont();
        graphics.setFont(font2);
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jComponent, graphics, font2);
        FontMetrics fontMetrics2 = SwingUtilities2.getFontMetrics(jComponent, graphics, UIManager.getFont("MenuItem.acceleratorFont"));
        if (jComponent.isOpaque()) {
            if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color);
            } else {
                graphics.setColor(jComponent.getBackground());
            }
            graphics.fillRect(0, 0, size.width, size.height);
        }
        KeyStroke accelerator = jMenuItem.getAccelerator();
        String str = "";
        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                str = KeyEvent.getKeyModifiersText(modifiers) + Marker.ANY_NON_NULL_MARKER;
            }
            str = str + KeyEvent.getKeyText(accelerator.getKeyCode());
        }
        String strLayoutMenuItem = layoutMenuItem(jComponent, fontMetrics, jMenuItem.getText(), fontMetrics2, str, jMenuItem.getIcon(), icon, icon2, jMenuItem.getVerticalAlignment(), jMenuItem.getHorizontalAlignment(), jMenuItem.getVerticalTextPosition(), jMenuItem.getHorizontalTextPosition(), rectangle, rectangle2, rectangle3, rectangle4, rectangle5, rectangle6, jMenuItem.getText() == null ? 0 : i2, i2);
        Color color3 = graphics.getColor();
        if (icon != null) {
            if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color2);
            }
            icon.paintIcon(jComponent, graphics, rectangle5.f12372x, rectangle5.f12373y);
            graphics.setColor(color3);
        }
        if (jMenuItem.getIcon() != null) {
            if (!model.isEnabled()) {
                icon3 = jMenuItem.getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                icon3 = jMenuItem.getPressedIcon();
                if (icon3 == null) {
                    icon3 = jMenuItem.getIcon();
                }
            } else {
                icon3 = jMenuItem.getIcon();
            }
            if (icon3 != null) {
                icon3.paintIcon(jComponent, graphics, rectangle2.f12372x, rectangle2.f12373y);
            }
        }
        if (strLayoutMenuItem != null && !strLayoutMenuItem.equals("")) {
            View view = (View) jComponent.getClientProperty("html");
            if (view != null) {
                view.paint(graphics, rectangle3);
            } else {
                int displayedMnemonicIndex = jMenuItem.getDisplayedMnemonicIndex();
                if (!model.isEnabled()) {
                    graphics.setColor(jMenuItem.getBackground().brighter());
                    SwingUtilities2.drawStringUnderlineCharAt(jMenuItem, graphics, strLayoutMenuItem, displayedMnemonicIndex, rectangle3.f12372x, rectangle3.f12373y + fontMetrics2.getAscent());
                    graphics.setColor(jMenuItem.getBackground().darker());
                    SwingUtilities2.drawStringUnderlineCharAt(jMenuItem, graphics, strLayoutMenuItem, displayedMnemonicIndex, rectangle3.f12372x - 1, (rectangle3.f12373y + fontMetrics2.getAscent()) - 1);
                } else {
                    if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                        graphics.setColor(color2);
                    } else {
                        graphics.setColor(jMenuItem.getForeground());
                    }
                    SwingUtilities2.drawStringUnderlineCharAt(jMenuItem, graphics, strLayoutMenuItem, displayedMnemonicIndex, rectangle3.f12372x, rectangle3.f12373y + fontMetrics.getAscent());
                }
            }
        }
        if (str != null && !str.equals("")) {
            int iIntValue = 0;
            Container parent = jMenuItem.getParent();
            if (parent != null && (parent instanceof JComponent)) {
                Integer num = (Integer) ((JComponent) parent).getClientProperty(MAX_ACC_WIDTH);
                iIntValue = (num != null ? num.intValue() : rectangle4.width) - rectangle4.width;
            }
            graphics.setFont(UIManager.getFont("MenuItem.acceleratorFont"));
            if (!model.isEnabled()) {
                graphics.setColor(jMenuItem.getBackground().brighter());
                SwingUtilities2.drawString(jComponent, graphics, str, rectangle4.f12372x - iIntValue, rectangle4.f12373y + fontMetrics.getAscent());
                graphics.setColor(jMenuItem.getBackground().darker());
                SwingUtilities2.drawString(jComponent, graphics, str, (rectangle4.f12372x - iIntValue) - 1, (rectangle4.f12373y + fontMetrics.getAscent()) - 1);
            } else {
                if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                    graphics.setColor(color2);
                } else {
                    graphics.setColor(jMenuItem.getForeground());
                }
                SwingUtilities2.drawString(jComponent, graphics, str, rectangle4.f12372x - iIntValue, rectangle4.f12373y + fontMetrics2.getAscent());
            }
        }
        if (icon2 != null) {
            if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color2);
            }
            if (!(jMenuItem.getParent() instanceof JMenuBar)) {
                icon2.paintIcon(jComponent, graphics, rectangle6.f12372x, rectangle6.f12373y);
            }
        }
        graphics.setColor(color3);
        graphics.setFont(font);
    }

    private static String layoutMenuItem(JComponent jComponent, FontMetrics fontMetrics, String str, FontMetrics fontMetrics2, String str2, Icon icon, Icon icon2, Icon icon3, int i2, int i3, int i4, int i5, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3, Rectangle rectangle4, Rectangle rectangle5, Rectangle rectangle6, int i6, int i7) {
        SwingUtilities.layoutCompoundLabel(jComponent, fontMetrics, str, icon, i2, i3, i4, i5, rectangle, rectangle2, rectangle3, i6);
        if (str2 == null || str2.equals("")) {
            rectangle4.height = 0;
            rectangle4.width = 0;
        } else {
            rectangle4.width = SwingUtilities2.stringWidth(jComponent, fontMetrics2, str2);
            rectangle4.height = fontMetrics2.getHeight();
        }
        if (icon2 != null) {
            rectangle5.width = icon2.getIconWidth();
            rectangle5.height = icon2.getIconHeight();
        } else {
            rectangle5.height = 0;
            rectangle5.width = 0;
        }
        if (icon3 != null) {
            rectangle6.width = icon3.getIconWidth();
            rectangle6.height = icon3.getIconHeight();
        } else {
            rectangle6.height = 0;
            rectangle6.width = 0;
        }
        Rectangle rectangleUnion = rectangle2.union(rectangle3);
        if (isLeftToRight(jComponent)) {
            rectangle3.f12372x += rectangle5.width + i7;
            rectangle2.f12372x += rectangle5.width + i7;
            rectangle4.f12372x = (((rectangle.f12372x + rectangle.width) - rectangle6.width) - i7) - rectangle4.width;
            rectangle5.f12372x = rectangle.f12372x;
            rectangle6.f12372x = ((rectangle.f12372x + rectangle.width) - i7) - rectangle6.width;
        } else {
            rectangle3.f12372x -= rectangle5.width + i7;
            rectangle2.f12372x -= rectangle5.width + i7;
            rectangle4.f12372x = rectangle.f12372x + rectangle6.width + i7;
            rectangle5.f12372x = (rectangle.f12372x + rectangle.width) - rectangle5.width;
            rectangle6.f12372x = rectangle.f12372x + i7;
        }
        rectangle4.f12373y = (rectangleUnion.f12373y + (rectangleUnion.height / 2)) - (rectangle4.height / 2);
        rectangle6.f12373y = (rectangleUnion.f12373y + (rectangleUnion.height / 2)) - (rectangle6.height / 2);
        rectangle5.f12373y = (rectangleUnion.f12373y + (rectangleUnion.height / 2)) - (rectangle5.height / 2);
        return str;
    }

    private static void drawMenuBezel(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.setColor(color);
        graphics.fillRect(i2, i3, i4, i5);
        graphics.setColor(color.brighter().brighter());
        graphics.drawLine(i2 + 1, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 1, (i3 + i5) - 2, (i2 + i4) - 1, i3 + 1);
        graphics.setColor(color.darker().darker());
        graphics.drawLine(i2, i3, (i2 + i4) - 2, i3);
        graphics.drawLine(i2, i3 + 1, i2, (i3 + i5) - 2);
    }

    static boolean isLeftToRight(Component component) {
        return component.getComponentOrientation().isLeftToRight();
    }
}
