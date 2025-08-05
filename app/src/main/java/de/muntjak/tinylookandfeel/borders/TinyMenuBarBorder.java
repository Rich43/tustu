package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.TinyToolBarUI;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JMenuBar;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyMenuBarBorder.class */
public class TinyMenuBarBorder extends AbstractBorder implements UIResource {
    static final Insets borderInsets = new Insets(1, 1, 2, 1);

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return borderInsets;
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.top = borderInsets.top;
        insets.left = borderInsets.left;
        insets.bottom = borderInsets.bottom;
        insets.right = borderInsets.right;
        return insets;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (TinyToolBarUI.doesMenuBarBorderToolBar((JMenuBar) component)) {
            return;
        }
        graphics.translate(i2, i3);
        graphics.setColor(Theme.toolBarDarkColor.getColor());
        graphics.drawLine(0, i5 - 1, i4, i5 - 1);
        graphics.translate(-i2, -i3);
    }
}
