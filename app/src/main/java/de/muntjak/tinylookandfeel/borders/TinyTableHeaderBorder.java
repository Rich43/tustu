package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyTableHeaderBorder.class */
public class TinyTableHeaderBorder extends AbstractBorder implements UIResource {
    protected static final Insets insetsXP = new Insets(3, 0, 3, 2);
    protected Color color1;
    protected Color color2;
    protected Color color3;
    protected Color color4;
    protected Color color5;

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return insetsXP;
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.left = insetsXP.left;
        insets.top = insetsXP.top;
        insets.right = insetsXP.right;
        insets.bottom = insetsXP.bottom;
        return insets;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (this.color1 == null) {
            this.color1 = ColorRoutines.darken(component.getBackground(), 5);
            this.color2 = ColorRoutines.darken(component.getBackground(), 10);
            this.color3 = ColorRoutines.darken(component.getBackground(), 15);
            this.color4 = Theme.tableHeaderDarkColor.getColor();
            this.color5 = Theme.tableHeaderLightColor.getColor();
        }
        graphics.setColor(this.color1);
        graphics.drawLine(i2, (i3 + i5) - 3, (i2 + i4) - 1, (i3 + i5) - 3);
        graphics.setColor(this.color2);
        graphics.drawLine(i2, (i3 + i5) - 2, (i2 + i4) - 1, (i3 + i5) - 2);
        graphics.setColor(this.color3);
        graphics.drawLine(i2, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
        graphics.setColor(this.color4);
        graphics.drawLine((i2 + i4) - 2, i3 + 3, (i2 + i4) - 2, (i3 + i5) - 5);
        graphics.setColor(this.color5);
        graphics.drawLine((i2 + i4) - 1, i3 + 3, (i2 + i4) - 1, (i3 + i5) - 5);
    }
}
