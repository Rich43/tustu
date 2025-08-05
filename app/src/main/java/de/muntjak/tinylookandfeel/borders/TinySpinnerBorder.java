package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinySpinnerBorder.class */
public class TinySpinnerBorder extends AbstractBorder implements UIResource {
    private static final Insets insets = new Insets(2, 2, 2, 2);

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return insets;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        drawXpBorder(component, graphics, i2, i3, i4, i5);
    }

    private void drawXpBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (component.isEnabled()) {
            DrawRoutines.drawBorder(graphics, Theme.spinnerBorderColor.getColor(), i2, i3, i4, i5);
        } else {
            DrawRoutines.drawBorder(graphics, Theme.spinnerBorderDisabledColor.getColor(), i2, i3, i4, i5);
        }
    }
}
