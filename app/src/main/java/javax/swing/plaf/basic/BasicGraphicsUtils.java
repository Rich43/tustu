package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicGraphicsUtils.class */
public class BasicGraphicsUtils {
    private static final Insets GROOVE_INSETS = new Insets(2, 2, 2, 2);
    private static final Insets ETCHED_INSETS = new Insets(2, 2, 2, 2);

    public static void drawEtchedRect(Graphics graphics, int i2, int i3, int i4, int i5, Color color, Color color2, Color color3, Color color4) {
        Color color5 = graphics.getColor();
        graphics.translate(i2, i3);
        graphics.setColor(color);
        graphics.drawLine(0, 0, i4 - 1, 0);
        graphics.drawLine(0, 1, 0, i5 - 2);
        graphics.setColor(color2);
        graphics.drawLine(1, 1, i4 - 3, 1);
        graphics.drawLine(1, 2, 1, i5 - 3);
        graphics.setColor(color4);
        graphics.drawLine(i4 - 1, 0, i4 - 1, i5 - 1);
        graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
        graphics.setColor(color3);
        graphics.drawLine(i4 - 2, 1, i4 - 2, i5 - 3);
        graphics.drawLine(1, i5 - 2, i4 - 2, i5 - 2);
        graphics.translate(-i2, -i3);
        graphics.setColor(color5);
    }

    public static Insets getEtchedInsets() {
        return ETCHED_INSETS;
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

    public static Insets getGrooveInsets() {
        return GROOVE_INSETS;
    }

    public static void drawBezel(Graphics graphics, int i2, int i3, int i4, int i5, boolean z2, boolean z3, Color color, Color color2, Color color3, Color color4) {
        Color color5 = graphics.getColor();
        graphics.translate(i2, i3);
        if (z2 && z3) {
            graphics.setColor(color2);
            graphics.drawRect(0, 0, i4 - 1, i5 - 1);
            graphics.setColor(color);
            graphics.drawRect(1, 1, i4 - 3, i5 - 3);
        } else if (z2) {
            drawLoweredBezel(graphics, i2, i3, i4, i5, color, color2, color3, color4);
        } else if (z3) {
            graphics.setColor(color2);
            graphics.drawRect(0, 0, i4 - 1, i5 - 1);
            graphics.setColor(color4);
            graphics.drawLine(1, 1, 1, i5 - 3);
            graphics.drawLine(2, 1, i4 - 3, 1);
            graphics.setColor(color3);
            graphics.drawLine(2, 2, 2, i5 - 4);
            graphics.drawLine(3, 2, i4 - 4, 2);
            graphics.setColor(color);
            graphics.drawLine(2, i5 - 3, i4 - 3, i5 - 3);
            graphics.drawLine(i4 - 3, 2, i4 - 3, i5 - 4);
            graphics.setColor(color2);
            graphics.drawLine(1, i5 - 2, i4 - 2, i5 - 2);
            graphics.drawLine(i4 - 2, i5 - 2, i4 - 2, 1);
        } else {
            graphics.setColor(color4);
            graphics.drawLine(0, 0, 0, i5 - 1);
            graphics.drawLine(1, 0, i4 - 2, 0);
            graphics.setColor(color3);
            graphics.drawLine(1, 1, 1, i5 - 3);
            graphics.drawLine(2, 1, i4 - 3, 1);
            graphics.setColor(color);
            graphics.drawLine(1, i5 - 2, i4 - 2, i5 - 2);
            graphics.drawLine(i4 - 2, 1, i4 - 2, i5 - 3);
            graphics.setColor(color2);
            graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
            graphics.drawLine(i4 - 1, i5 - 1, i4 - 1, 0);
        }
        graphics.translate(-i2, -i3);
        graphics.setColor(color5);
    }

    public static void drawLoweredBezel(Graphics graphics, int i2, int i3, int i4, int i5, Color color, Color color2, Color color3, Color color4) {
        graphics.setColor(color2);
        graphics.drawLine(0, 0, 0, i5 - 1);
        graphics.drawLine(1, 0, i4 - 2, 0);
        graphics.setColor(color);
        graphics.drawLine(1, 1, 1, i5 - 2);
        graphics.drawLine(1, 1, i4 - 3, 1);
        graphics.setColor(color4);
        graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
        graphics.drawLine(i4 - 1, i5 - 1, i4 - 1, 0);
        graphics.setColor(color3);
        graphics.drawLine(1, i5 - 2, i4 - 2, i5 - 2);
        graphics.drawLine(i4 - 2, i5 - 2, i4 - 2, 1);
    }

    public static void drawString(Graphics graphics, String str, int i2, int i3, int i4) {
        int i5 = -1;
        if (i2 != 0) {
            char upperCase = Character.toUpperCase((char) i2);
            char lowerCase = Character.toLowerCase((char) i2);
            int iIndexOf = str.indexOf(upperCase);
            int iIndexOf2 = str.indexOf(lowerCase);
            if (iIndexOf == -1) {
                i5 = iIndexOf2;
            } else if (iIndexOf2 == -1) {
                i5 = iIndexOf;
            } else {
                i5 = iIndexOf2 < iIndexOf ? iIndexOf2 : iIndexOf;
            }
        }
        drawStringUnderlineCharAt(graphics, str, i5, i3, i4);
    }

    public static void drawStringUnderlineCharAt(Graphics graphics, String str, int i2, int i3, int i4) {
        SwingUtilities2.drawStringUnderlineCharAt(null, graphics, str, i2, i3, i4);
    }

    public static void drawDashedRect(Graphics graphics, int i2, int i3, int i4, int i5) {
        for (int i6 = i2; i6 < i2 + i4; i6 += 2) {
            graphics.fillRect(i6, i3, 1, 1);
            graphics.fillRect(i6, (i3 + i5) - 1, 1, 1);
        }
        for (int i7 = i3; i7 < i3 + i5; i7 += 2) {
            graphics.fillRect(i2, i7, 1, 1);
            graphics.fillRect((i2 + i4) - 1, i7, 1, 1);
        }
    }

    public static Dimension getPreferredButtonSize(AbstractButton abstractButton, int i2) {
        if (abstractButton.getComponentCount() > 0) {
            return null;
        }
        Icon icon = abstractButton.getIcon();
        String text = abstractButton.getText();
        FontMetrics fontMetrics = abstractButton.getFontMetrics(abstractButton.getFont());
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = new Rectangle();
        SwingUtilities.layoutCompoundLabel(abstractButton, fontMetrics, text, icon, abstractButton.getVerticalAlignment(), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalTextPosition(), abstractButton.getHorizontalTextPosition(), new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE), rectangle, rectangle2, text == null ? 0 : i2);
        Rectangle rectangleUnion = rectangle.union(rectangle2);
        Insets insets = abstractButton.getInsets();
        rectangleUnion.width += insets.left + insets.right;
        rectangleUnion.height += insets.top + insets.bottom;
        return rectangleUnion.getSize();
    }

    static boolean isLeftToRight(Component component) {
        return component.getComponentOrientation().isLeftToRight();
    }

    static boolean isMenuShortcutKeyDown(InputEvent inputEvent) {
        return (inputEvent.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0;
    }
}
