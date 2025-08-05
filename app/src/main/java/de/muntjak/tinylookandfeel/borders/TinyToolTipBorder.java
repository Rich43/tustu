package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyToolTipBorder.class */
public class TinyToolTipBorder implements Border {
    private static final Insets insets = new Insets(3, 3, 3, 3);
    private boolean active;

    public TinyToolTipBorder(boolean z2) {
        this.active = z2;
    }

    @Override // javax.swing.border.Border
    public boolean isBorderOpaque() {
        return false;
    }

    @Override // javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (this.active) {
            graphics.setColor(Theme.tipBorderColor.getColor());
        } else {
            graphics.setColor(Theme.tipBorderDis.getColor());
        }
        graphics.drawRect(i2, i3, i4 - 1, i5 - 1);
    }

    @Override // javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return insets;
    }
}
