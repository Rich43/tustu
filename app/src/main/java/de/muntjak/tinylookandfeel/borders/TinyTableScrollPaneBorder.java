package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyTableScrollPaneBorder.class */
public class TinyTableScrollPaneBorder extends AbstractBorder implements UIResource {
    private static final Insets insets = new Insets(1, 1, 1, 1);

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.setColor(Theme.tableBorderLightColor.getColor());
        graphics.drawLine((i2 + i4) - 1, i3, (i2 + i4) - 1, (i3 + i5) - 1);
        graphics.drawLine(i2, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
        graphics.setColor(Theme.tableBorderDarkColor.getColor());
        graphics.drawLine(i2, i3, i2, (i3 + i5) - 1);
        graphics.drawLine(i2, i3, (i2 + i4) - 1, i3);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return insets;
    }
}
