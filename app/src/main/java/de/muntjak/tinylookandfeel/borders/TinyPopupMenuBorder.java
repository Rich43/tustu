package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.TinyLookAndFeel;
import de.muntjak.tinylookandfeel.TinyPopupFactory;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyPopupMenuBorder.class */
public class TinyPopupMenuBorder extends AbstractBorder implements UIResource {
    public static final int SHADOW_SIZE = 5;
    private static final Insets INSETS_NO_SHADOW = new Insets(2, 2, 2, 2);
    private static final Insets INSETS_SHADOW_LEFT_TO_RIGHT = new Insets(2, 2, 7, 7);
    private static final Insets INSETS_SHADOW_RIGHT_TO_LEFT = new Insets(2, 7, 7, 2);
    public static final Image LEFT_TO_RIGHT_SHADOW_MASK = TinyLookAndFeel.loadIcon("leftToRightShadow.png").getImage();
    public static final Image RIGHT_TO_LEFT_SHADOW_MASK = TinyLookAndFeel.loadIcon("rightToLeftShadow.png").getImage();

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        boolean zEquals = Boolean.TRUE.equals(((JComponent) component).getClientProperty(TinyPopupFactory.SHADOW_POPUP_KEY));
        boolean zIsOrientationLeftToRight = isOrientationLeftToRight(component);
        graphics.translate(i2, i3);
        int i6 = 0;
        int i7 = i4;
        int i8 = i5;
        if (zEquals) {
            if (zIsOrientationLeftToRight) {
                i7 -= 5;
                i8 -= 5;
                graphics.drawImage((BufferedImage) ((JComponent) component).getClientProperty(TinyPopupFactory.VERTICAL_IMAGE_KEY), i7, 0, component);
                graphics.drawImage((BufferedImage) ((JComponent) component).getClientProperty(TinyPopupFactory.HORIZONTAL_IMAGE_KEY), 0, i8, component);
            } else {
                i8 -= 5;
                i6 = 5;
                BufferedImage bufferedImage = (BufferedImage) ((JComponent) component).getClientProperty(TinyPopupFactory.VERTICAL_IMAGE_KEY);
                if (bufferedImage != null) {
                    graphics.drawImage(bufferedImage, 0, 0, component);
                }
                BufferedImage bufferedImage2 = (BufferedImage) ((JComponent) component).getClientProperty(TinyPopupFactory.HORIZONTAL_IMAGE_KEY);
                if (bufferedImage2 != null) {
                    graphics.drawImage(bufferedImage2, 0, i8, component);
                }
            }
        }
        graphics.setColor(Theme.menuInnerHilightColor.getColor());
        graphics.drawLine(i6 + 1, 1, i7 - 3, 1);
        if (zIsOrientationLeftToRight) {
            graphics.drawLine(i6 + 1, 1, i6 + 1, i8 - 3);
        } else {
            graphics.drawLine(i7 - 2, 1, i7 - 2, i8 - 2);
        }
        graphics.setColor(Theme.menuInnerShadowColor.getColor());
        if (zIsOrientationLeftToRight) {
            graphics.drawLine(i7 - 2, 1, i7 - 2, i8 - 2);
        } else {
            graphics.drawLine(i6 + 1, 1, i6 + 1, i8 - 2);
        }
        graphics.drawLine(i6 + 1, i8 - 2, i7 - 2, i8 - 2);
        graphics.setColor(Theme.menuOuterHilightColor.getColor());
        graphics.drawLine(i6, 0, i7 - 2, 0);
        graphics.drawLine(i6, 0, i6, i8 - 1);
        graphics.setColor(Theme.menuOuterShadowColor.getColor());
        graphics.drawLine(i7 - 1, 0, i7 - 1, i8 - 1);
        graphics.drawLine(i6, i8 - 1, i7 - 1, i8 - 1);
        if (zEquals) {
            if (zIsOrientationLeftToRight) {
                graphics.drawImage(LEFT_TO_RIGHT_SHADOW_MASK, i7, 4, i7 + 5, 8, 6, 0, 11, 4, component);
                graphics.drawImage(LEFT_TO_RIGHT_SHADOW_MASK, 4, i8, 8, i8 + 5, 0, 6, 4, 11, component);
                graphics.drawImage(LEFT_TO_RIGHT_SHADOW_MASK, i7, i8, i7 + 5, i8 + 5, 6, 6, 11, 11, component);
                graphics.drawImage(LEFT_TO_RIGHT_SHADOW_MASK, i7, 8, i7 + 5, i8, 6, 4, 11, 5, component);
                graphics.drawImage(LEFT_TO_RIGHT_SHADOW_MASK, 8, i8, i7, i8 + 5, 4, 6, 5, 11, component);
            } else {
                graphics.drawImage(RIGHT_TO_LEFT_SHADOW_MASK, 0, 4, 5, 8, 0, 0, 5, 4, component);
                graphics.drawImage(RIGHT_TO_LEFT_SHADOW_MASK, i7 - 8, i8, i7 - 4, i8 + 5, 7, 6, 11, 11, component);
                graphics.drawImage(RIGHT_TO_LEFT_SHADOW_MASK, 0, i8, 5, i8 + 5, 0, 6, 6, 11, component);
                graphics.drawImage(RIGHT_TO_LEFT_SHADOW_MASK, 0, 8, 5, i8, 0, 4, 5, 5, component);
                graphics.drawImage(RIGHT_TO_LEFT_SHADOW_MASK, 5, i8, i7 - 8, i8 + 5, 5, 6, 6, 11, component);
            }
        }
        graphics.translate(-i2, -i3);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return TinyPopupFactory.isPopupShadowEnabled() ? isOrientationLeftToRight(component) ? INSETS_SHADOW_LEFT_TO_RIGHT : INSETS_SHADOW_RIGHT_TO_LEFT : INSETS_NO_SHADOW;
    }

    private boolean isOrientationLeftToRight(Component component) {
        Component invoker;
        if (!(component instanceof JComponent)) {
            return true;
        }
        Object clientProperty = ((JComponent) component).getClientProperty(TinyPopupFactory.COMPONENT_ORIENTATION_KEY);
        if (clientProperty == null && (component instanceof JPopupMenu) && (invoker = ((JPopupMenu) component).getInvoker()) != null) {
            clientProperty = invoker.getComponentOrientation();
        }
        if (clientProperty == null) {
            return true;
        }
        return ((ComponentOrientation) clientProperty).isLeftToRight();
    }

    public static String componentOrientationToString(ComponentOrientation componentOrientation) {
        return componentOrientation == null ? "<null>" : componentOrientation.isLeftToRight() ? "left-to-right" : "right-to-left";
    }
}
