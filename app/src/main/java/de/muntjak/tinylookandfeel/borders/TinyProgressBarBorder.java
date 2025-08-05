package de.muntjak.tinylookandfeel.borders;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/borders/TinyProgressBarBorder.class */
public class TinyProgressBarBorder extends AbstractBorder implements UIResource {
    protected static final Insets INSETS_YQ = new Insets(3, 3, 3, 3);

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        drawXpBorder(component, graphics, i2, i3, i4, i5);
    }

    private void drawXpBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        DrawRoutines.drawProgressBarBorder(graphics, Theme.progressBorderColor.getColor(), i2, i3, i4, i5);
        DrawRoutines.drawProgressBarBorder(graphics, Theme.progressDarkColor.getColor(), i2 + 1, i3 + 1, i4 - 2, i5 - 2);
        int i6 = i4 - 4;
        int i7 = i5 - 4;
        int i8 = i2 + 2;
        int i9 = i3 + 2;
        graphics.setColor(Theme.progressLightColor.getColor());
        graphics.drawLine(i8 + 1, i9, (i8 + i6) - 2, i9);
        graphics.drawLine(i8, i9 + 1, i8, (i9 + i7) - 2);
        graphics.setColor(Theme.progressTrackColor.getColor());
        graphics.drawLine(i8 + 1, (i9 + i7) - 1, (i8 + i6) - 2, (i9 + i7) - 1);
        graphics.drawLine((i8 + i6) - 1, i9 + 1, (i8 + i6) - 1, (i9 + i7) - 2);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return INSETS_YQ;
    }
}
