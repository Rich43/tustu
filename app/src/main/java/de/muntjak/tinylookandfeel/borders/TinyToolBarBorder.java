package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JToolBar;
import javax.swing.plaf.metal.MetalBorders;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyToolBarBorder.class */
public class TinyToolBarBorder extends MetalBorders.ToolBarBorder {
    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return getBorderInsets(component, new Insets(0, 0, 0, 0));
    }

    @Override // javax.swing.plaf.metal.MetalBorders.ToolBarBorder, javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.right = 2;
        insets.bottom = 2;
        insets.left = 2;
        insets.top = 2;
        if (!(component instanceof JToolBar)) {
            return insets;
        }
        if (((JToolBar) component).isFloatable()) {
            if (((JToolBar) component).getOrientation() != 0) {
                insets.top = 10;
            } else if (component.getComponentOrientation().isLeftToRight()) {
                insets.left = 10;
            } else {
                insets.right = 10;
            }
        }
        Insets margin = ((JToolBar) component).getMargin();
        if (margin != null) {
            insets.left += margin.left;
            insets.top += margin.top;
            insets.right += margin.right;
            insets.bottom += margin.bottom;
        }
        return insets;
    }

    @Override // javax.swing.plaf.metal.MetalBorders.ToolBarBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (component instanceof JToolBar) {
            drawXPBorder(component, graphics, i2, i3, i4, i5);
            if (((JToolBar) component).getOrientation() == 0) {
                graphics.setColor(Theme.toolBarLightColor.getColor());
                graphics.drawLine(i2, i3, i4 - 1, i3);
                graphics.setColor(Theme.toolBarDarkColor.getColor());
                graphics.drawLine(i2, i5 - 1, i4 - 1, i5 - 1);
                return;
            }
            graphics.setColor(Theme.toolBarLightColor.getColor());
            graphics.drawLine(i2, i3, i2, i5 - 1);
            graphics.setColor(Theme.toolBarDarkColor.getColor());
            graphics.drawLine(i4 - 1, i3, i4 - 1, i5 - 1);
        }
    }

    protected void drawXPBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.translate(i2, i3);
        if (((JToolBar) component).isFloatable()) {
            if (((JToolBar) component).getOrientation() == 0) {
                int i6 = 3;
                if (!component.getComponentOrientation().isLeftToRight()) {
                    i6 = (component.getBounds().width - 8) + 3;
                }
                graphics.setColor(Theme.toolGripLightColor.getColor());
                graphics.drawLine(i6, 3, i6 + 1, 3);
                graphics.drawLine(i6, 3, i6, i5 - 5);
                graphics.setColor(Theme.toolGripDarkColor.getColor());
                graphics.drawLine(i6, i5 - 4, i6 + 1, i5 - 4);
                graphics.drawLine(i6 + 2, 3, i6 + 2, i5 - 4);
            } else {
                graphics.setColor(Theme.toolGripLightColor.getColor());
                graphics.drawLine(3, 3, 3, 4);
                graphics.drawLine(3, 3, i4 - 4, 3);
                graphics.setColor(Theme.toolGripDarkColor.getColor());
                graphics.drawLine(i4 - 4, 4, i4 - 4, 5);
                graphics.drawLine(3, 5, i4 - 4, 5);
            }
        }
        graphics.translate(-i2, -i3);
    }
}
