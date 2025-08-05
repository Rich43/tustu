package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.Component;
import java.awt.Graphics;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyTableHeaderRolloverBorder.class */
public class TinyTableHeaderRolloverBorder extends TinyTableHeaderBorder {
    @Override // de.muntjak.tinylookandfeel.borders.TinyTableHeaderBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (this.color1 == null) {
            this.color1 = Theme.tableHeaderRolloverColor.getColor();
            this.color2 = ColorRoutines.lighten(this.color1, 25);
        }
        graphics.setColor(this.color1);
        graphics.drawLine(i2, (i3 + i5) - 3, (i2 + i4) - 1, (i3 + i5) - 3);
        graphics.drawLine(i2, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
        graphics.setColor(this.color2);
        graphics.drawLine(i2, (i3 + i5) - 2, (i2 + i4) - 1, (i3 + i5) - 2);
    }
}
